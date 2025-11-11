package com.yata.order_service.util;

/**
 * Clase de utilidad para calcular la distancia del círculo máximo (distancia aérea)
 * entre dos puntos Latitud/Longitud usando la Fórmula del Haversine.
 */
public final class HaversineCalculator {

    // Radio medio de la Tierra en kilómetros (km)
    private static final double RADIO_TIERRA_KM = 6371.0;

    /**
     * Constructor privado para evitar la instanciación de esta clase de utilidad.
     */
    private HaversineCalculator() {
        // No se permite la instanciación
    }

    /**
     * Calcula la distancia del círculo máximo entre dos coordenadas Lat/Lon.
     * * @param lat1 Latitud del punto 1 (en grados decimales)
     * @param lon1 Longitud del punto 1 (en grados decimales)
     * @param lat2 Latitud del punto 2 (en grados decimales)
     * @param lon2 Longitud del punto 2 (en grados decimales)
     * @return La distancia entre los puntos en kilómetros (km)
     */
    public static double calcularDistanciaKm(double lat1, double lon1, double lat2, double lon2) {

        // 1. Convertir grados a radianes y calcular la diferencia
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        // Diferencia de Latitud y Longitud en radianes
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = Math.toRadians(lon2 - lon1);

        // 2. Aplicar la parte 'a' de la fórmula (la función Haversine)
        // a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        // 3. Aplicar la parte 'c' de la fórmula (distancia angular)
        // c = 2 ⋅ atan2( √a , √(1−a) )
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 4. Calcular la distancia final
        // d = R ⋅ c
        double distancia = RADIO_TIERRA_KM * c;

        return distancia;
    }
}