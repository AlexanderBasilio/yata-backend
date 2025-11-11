package com.yata.order_service.controller;

import com.yata.order_service.service.ShippingCalculator;
import com.yata.order_service.dto.ShippingRequest;
import com.yata.order_service.dto.ShippingResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para manejar solicitudes relacionadas con el cálculo de envío.
 * Expone el endpoint para que el frontend obtenga el costo de envío en tiempo real.
 */
@RestController
@RequestMapping("/api/orders") // Prefijo de la URL
public class ShippingController {

    private final ShippingCalculator shippingCalculator;

    // Inyección de dependencia del servicio
    // Dependency injection of the service (assuming Spring Boot)
    public ShippingController(ShippingCalculator shippingCalculator) {
        this.shippingCalculator = shippingCalculator;
    }

    /**
     * Endpoint para calcular el costo de envío.
     * Método: POST
     * URL: /api/orders/shipping-cost
     * * @param request DTO que contiene las coordenadas (Lat/Lon) de destino.
     * @return ResponseEntity con el costo de envío y la moneda.
     */
    @PostMapping("/shipping-cost")
    public ResponseEntity<?> calculateShippingCost(@Valid @RequestBody ShippingRequest request) {
        try {
            double cost = shippingCalculator.calculateShippingCost(
                    request.getDestLat(),
                    request.getDestLon()
            );

            ShippingResponse response = new ShippingResponse(cost);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            // Maneja el caso de "Fuera de cobertura" o "Error de tarifas"
            // Handles "Out of coverage" or "Tariff error" cases
            return new ResponseEntity<>(
                    "Error al calcular el costo de envío: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            // Manejo de errores genéricos (e.g., error de base de datos, error interno)
            return new ResponseEntity<>("Error interno del servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
