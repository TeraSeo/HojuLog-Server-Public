package com.promo.web.exception;

public class RestaurantPostNotFoundException extends RuntimeException {
    public RestaurantPostNotFoundException(String message) {
        super(message);
    }
}
