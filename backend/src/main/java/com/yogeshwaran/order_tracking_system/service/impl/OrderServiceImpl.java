package com.yogeshwaran.order_tracking_system.service.impl;

import com.yogeshwaran.order_tracking_system.dto.order.*;
import com.yogeshwaran.order_tracking_system.entity.*;
import com.yogeshwaran.order_tracking_system.exception.BusinessValidationException;
import com.yogeshwaran.order_tracking_system.exception.ResourceNotFoundException;
import com.yogeshwaran.order_tracking_system.repository.OrderRepository;
import com.yogeshwaran.order_tracking_system.repository.ProductRepository;
import com.yogeshwaran.order_tracking_system.security.SecurityUtils;
import com.yogeshwaran.order_tracking_system.service.OrderService;
import com.yogeshwaran.order_tracking_system.util.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final SecurityUtils securityUtils;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        User customer = securityUtils.getCurrentUser();

        Order order = new Order();
        order.setCustomer(customer);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest line : request.getItems()) {
            Product product = productRepository.findById(line.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

            if (!product.isAvailable()) {
                throw new BusinessValidationException(product.getName() + " is currently unavailable.");
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(line.getQuantity());
            item.setPriceAtOrder(product.getPrice());
            items.add(item);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(line.getQuantity())));
        }

        order.setItems(items);
        order.setTotalAmount(total);

        return OrderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders() {
        User customer = securityUtils.getCurrentUser();
        return orderRepository.findByCustomer_IdOrderByPlacedAtDesc(customer.getId())
                .stream().map(OrderMapper::toResponse).toList();
    }

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            OrderStatus.PLACED, Set.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
            OrderStatus.CONFIRMED, Set.of(OrderStatus.PREPARING, OrderStatus.CANCELLED),
            OrderStatus.PREPARING, Set.of(OrderStatus.OUT_FOR_DELIVERY),
            OrderStatus.OUT_FOR_DELIVERY, Set.of(OrderStatus.DELIVERED)
    );

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(String status) {
        List<Order> orders = (status == null || status.isBlank())
                ? orderRepository.findAllByOrderByPlacedAtDesc()
                : orderRepository.findByStatusOrderByPlacedAtDesc(OrderStatus.valueOf(status.toUpperCase()));
        return orders.stream().map(OrderMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long orderId, String newStatusRaw) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        OrderStatus newStatus = OrderStatus.valueOf(newStatusRaw.toUpperCase());
        OrderStatus current = order.getStatus();

        Set<OrderStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(current, Set.of());
        if (!allowed.contains(newStatus)) {
            throw new BusinessValidationException(
                    "Cannot move an order from " + current + " to " + newStatus + ".");
        }

        order.setStatus(newStatus);

        OrderResponse response = OrderMapper.toResponse(orderRepository.save(order));
        messagingTemplate.convertAndSend("/topic/orders/" + orderId, response);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        User current = securityUtils.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        boolean isOwner = order.getCustomer().getId().equals(current.getId());
        boolean isAdmin = securityUtils.currentUserHasRole("ADMIN");
        if (!isOwner && !isAdmin) {
            throw new BusinessValidationException("You do not have access to this order.");
        }
        return OrderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancelMyOrder(Long orderId) {
        User current = securityUtils.getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        if (!order.getCustomer().getId().equals(current.getId())) {
            throw new BusinessValidationException("You can only cancel your own orders.");
        }

        if (order.getStatus() != OrderStatus.PLACED && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessValidationException(
                    "This order can no longer be cancelled (current status: " + order.getStatus() + ").");
        }

        order.setStatus(OrderStatus.CANCELLED);
        OrderResponse response = OrderMapper.toResponse(orderRepository.save(order));
        messagingTemplate.convertAndSend("/topic/orders/" + orderId, response);
        return response;
    }
}
