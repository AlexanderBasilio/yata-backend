package com.yata.order_service.notification;

import com.yata.order_service.dto.CreateOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SlackService {

    // 1. Inyectar la Variable de Entorno
    @Value("${SLACK_WEBHOOK_URL}")
    private String slackWebhookUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void notifyNewOrder(CreateOrderRequest request) {
        if (slackWebhookUrl == null || slackWebhookUrl.isEmpty()) {
            log.warn("⚠️ SLACK_WEBHOOK_URL no está configurada. No se enviará la notificación.");
            return;
        }

        try {
            // 2. Construir el Payload (el mensaje en formato JSON que Slack espera)
            String slackMessage = buildSlackPayload(request);

            // 3. Crear la Solicitud HTTP POST
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(slackWebhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(slackMessage))
                    .build();

            // 4. Enviar la Solicitud de forma asíncrona para no bloquear el hilo principal
            httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            log.info("✅ Notificación de Slack enviada con éxito.");
                        } else {
                            log.error("❌ Error al enviar notificación a Slack. Código: {} | Cuerpo: {}", response.statusCode(), response.body());
                        }
                    })
                    .exceptionally(ex -> {
                        log.error("❌ Excepción al comunicarse con Slack: {}", ex.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            log.error("Error general en el servicio de Slack: {}", e.getMessage(), e);
        }
    }

    // Método auxiliar para formatear el mensaje
    private String buildSlackPayload(CreateOrderRequest request) throws Exception {
        // Formatear el resumen de los ítems
        String itemsSummary = request.getItems().stream()
                .map(item -> String.format("- %dx %s (%.2f USD)",
                        item.getQuantity(), item.getName(), item.getPrice() * item.getQuantity()))
                .collect(Collectors.joining("\n"));

        // Crear el mensaje con Markdown para Slack
        String messageText = String.format(
                "*🔔 ¡NUEVA ORDEN RECIBIDA! #%s*\n" +
                        "--------------------------------\n" +
                        "*👤 Cliente:* %s\n" +
                        "*📱 Teléfono:* %s\n" +
                        "*📍 Dirección:* %s, %s\n" +
                        "*💰 Total:* %.2f USD (Método: %s)\n\n" +
                        "*📦 Detalle de Ítems:*\n%s",
                // Los campos que necesitas
                request.getCustomerName().toUpperCase().substring(0, 3) + "..." + System.currentTimeMillis() % 1000, // ID Simple para el mensaje
                request.getCustomerName(),
                request.getCustomerPhone(),
                request.getLocation().getAddress(), request.getLocation().getCity(),
                request.getSummary().getTotal(), request.getPayment().getMethod(),
                itemsSummary
        );

        // El formato JSON que el Webhook de Slack espera
        // 'text' es el campo obligatorio que contiene el mensaje
        return objectMapper.writeValueAsString(
                java.util.Map.of("text", messageText)
        );
    }
}