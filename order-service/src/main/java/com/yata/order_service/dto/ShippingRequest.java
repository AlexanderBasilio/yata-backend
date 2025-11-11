package com.yata.order_service.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para la solicitud de cálculo de costo de envío.
 * Recibe las coordenadas de destino.
 */
public class ShippingRequest {

    @NotNull(message = "La latitud de destino es requerida.")
    private Double destLat;

    @NotNull(message = "La longitud de destino es requerida.")
    private Double destLon;

    // --- Getters y Setters ---
    public Double getDestLat() {
        return destLat;
    }

    public void setDestLat(Double destLat) {
        this.destLat = destLat;
    }

    public Double getDestLon() {
        return destLon;
    }

    public void setDestLon(Double destLon) {
        this.destLon = destLon;
    }
}