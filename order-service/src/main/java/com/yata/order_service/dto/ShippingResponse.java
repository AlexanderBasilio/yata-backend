package com.yata.order_service.dto;

/**
 * DTO para la respuesta del cálculo de costo de envío.
 */
public class ShippingResponse {

    private double shippingCost;
    private String currency = "PEN"; // Soles Peruanos

    public ShippingResponse(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    // --- Getters y Setters ---
    public double getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}