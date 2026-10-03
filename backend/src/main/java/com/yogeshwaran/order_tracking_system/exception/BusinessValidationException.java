package com.yogeshwaran.order_tracking_system.exception;

public class BusinessValidationException extends RuntimeException {
    public BusinessValidationException(String message) { super(message); }
}