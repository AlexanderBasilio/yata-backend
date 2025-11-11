package com.yata.order_service.service;

import com.yata.order_service.util.HaversineCalculator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Clase interna inmutable para representar un rango de tarifa de envío
// Internal immutable class to represent a shipping rate range
final class ShippingRate {
    final double minDistanceKm;
    final double maxDistanceKm;
    final double costSoles;

    public ShippingRate(double minDistanceKm, double maxDistanceKm, double costSoles) {
        this.minDistanceKm = minDistanceKm;
        this.maxDistanceKm = maxDistanceKm;
        this.costSoles = costSoles;
    }
}

/**
 * Servicio encargado de calcular el costo de envío basado en la distancia y las reglas de negocio.
 */
@Service
public class ShippingCalculator {

    // --- Coordenadas Fijas de la Tienda / Almacén (Origen) ---
    // ESTOS VALORES DEBEN OBTENERSE DE UN ARCHIVO DE CONFIGURACIÓN (Ej: application.properties)
    // THESE VALUES MUST BE OBTAINED FROM A CONFIGURATION FILE (e.g., application.properties)
    private static final double ORIGEN_LATITUDE = -12.04092; // Ejemplo: Latitud de almacén 1
    private static final double ORIGEN_LONGITUDE = -75.23145; // Ejemplo: Longitud de almacén 1

    // --- Tabla de Tarifas (Convertida de Metros a Kilómetros) ---
    // Esta lista debería cargarse idealmente desde una base de datos para facilitar su gestión.
    // Los rangos originales en metros se dividieron entre 1000 para obtener KM.
    private final List<ShippingRate> rates = List.of(
            new ShippingRate(0.000, 0.100, 2.00),    // 0 - 100 m
            new ShippingRate(0.101, 0.200, 2.30),    // 101 - 200 m
            new ShippingRate(0.201, 0.400, 2.60),    // 201 - 400 m
            new ShippingRate(0.401, 0.750, 3.00),    // 401 - 750 m
            new ShippingRate(0.751, 1.000, 3.40),    // 751 - 1000 m (1 km)
            new ShippingRate(1.001, 1.250, 3.70),
            new ShippingRate(1.251, 1.500, 4.00),
            new ShippingRate(1.501, 2.000, 4.70),
            new ShippingRate(2.001, 2.500, 5.40),
            new ShippingRate(2.501, 3.000, 6.10),
            new ShippingRate(3.001, 3.500, 6.80),
            new ShippingRate(3.501, 4.000, 7.60),
            new ShippingRate(4.001, 4.500, 8.30),
            new ShippingRate(4.501, 5.000, 9.00),
            new ShippingRate(5.001, 5.500, 9.80),
            new ShippingRate(5.501, 6.000, 10.50),
            new ShippingRate(6.001, 6.500, 11.30),
            new ShippingRate(6.501, 7.000, 12.00),
            new ShippingRate(7.001, 7.500, 12.70),
            new ShippingRate(7.501, 8.000, 13.50)    // 7501 - 8000 m (8 km)
            // No se definió una tarifa para > 8.0 km. Manejamos esto con un error o tarifa máxima.
    );

    /**
     * Calcula el costo de envío en Soles (PEN) para una ubicación de destino.
     * * @param destLat Latitud del destino (cliente).
     * @param destLon Longitud del destino (cliente).
     * @return El costo de envío en soles (S/.)
     * @throws RuntimeException Si la distancia supera el rango de tarifas definido.
     */
    public double calculateShippingCost(double destLat, double destLon) {

        // 1. Calcular la distancia usando el HaversineCalculator
        // La distancia será en kilómetros (km).
        double distanceKm = HaversineCalculator.calcularDistanciaKm(
                ORIGEN_LATITUDE, ORIGEN_LONGITUDE,
                destLat, destLon
        );

        // 2. Buscar la tarifa correspondiente en la tabla de tarifas
        Optional<ShippingRate> rateFound = rates.stream()
                .filter(rate -> distanceKm >= rate.minDistanceKm && distanceKm <= rate.maxDistanceKm)
                .findFirst();

        // 3. Devolver la tarifa o lanzar una excepción
        if (rateFound.isPresent()) {
            // Utilizamos Math.round para asegurar que el costo final solo tenga 2 decimales (para Soles/PEN)
            return Math.round(rateFound.get().costSoles * 100.0) / 100.0;
        } else {
            // Manejo de errores: Si la distancia es mayor al rango máximo (8000 km)
            // Aquí puedes decidir si quieres devolver una tarifa plana alta o un error.
            if (distanceKm > 8000) {
                // Convertimos la distancia a metros solo para el mensaje de error para claridad
                int distanceMeters = (int) Math.round(distanceKm * 1000);
                throw new RuntimeException("Distancia de envío (" + distanceMeters + " metros / " + String.format("%.3f", distanceKm) + " km) excede el límite máximo de cobertura (8.0 km).");
            }
            // Esto solo ocurriría si hay un error en la definición de los rangos.
            throw new RuntimeException("No se encontró una tarifa para la distancia: " + String.format("%.3f", distanceKm) + " km.");
        }
    }
}
