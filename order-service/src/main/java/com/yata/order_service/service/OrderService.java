package com.yata.order_service.service;

import com.yata.order_service.dto.CreateOrderRequest;
import com.yata.order_service.dto.CreateOrderResponse;
import com.yata.order_service.model.*;
import com.yata.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yata.order_service.notification.SlackService; // <--- NUEVO IMPORT

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final SlackService slackService; // <--- NUEVA INYECCIÓN

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {

        // 1. Generar ID único
        String orderId = UUID.randomUUID().toString();

        // 2. Mapear items
        var items = request.getItems().stream()
                .map(itemDTO -> OrderItem.builder()
                        .productId(itemDTO.getProductId())
                        .name(itemDTO.getName())
                        .price(itemDTO.getPrice())
                        .quantity(itemDTO.getQuantity())
                        .unit(itemDTO.getUnit())
                        .build())
                .collect(Collectors.toList());

        // 3. Crear orden
        Order order = Order.builder()
                .orderId(orderId)
                .items(items)
                .location(Location.builder()
                        .address(request.getLocation().getAddress())
                        .latitude(request.getLocation().getLatitude())
                        .longitude(request.getLocation().getLongitude())
                        .city(request.getLocation().getCity())
                        .region(request.getLocation().getRegion())
                        .build())
                .payment(Payment.builder()
                        .method(request.getPayment().getMethod())
                        .cashAmount(request.getPayment().getCashAmount())
                        .changeAmount(request.getPayment().getChangeAmount())
                        .build())
                .summary(Summary.builder()
                        .subtotal(request.getSummary().getSubtotal())
                        .serviceCost(request.getSummary().getServiceCost())
                        .shippingCost(request.getSummary().getShippingCost())
                        .total(request.getSummary().getTotal())
                        .build())
                .deliveryNote(request.getDeliveryNote())
                .status("PENDING")
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .build();

        // 3.b. LÓGICA DE CÁLCULO DE PUNTOS (Nuevo Bloque)
        double totalAmount = request.getSummary().getTotal(); // Obtiene el total (ej: 73.50)

        // Redondea al entero más cercano (Math.round() devuelve long, se hace cast a int)
        int pointsEarned = (int) Math.round(totalAmount);

        // Asigna los puntos calculados a la entidad Order
        order.setPointsEarned(pointsEarned);
        // Si la compra fue 73.50, pointsEarned es 74. Si fue 73.49, es 73

        // 4. Asignar orden a cada item (relación bidireccional)
        items.forEach(item -> item.setOrder(order));

        // 5. Guardar en DB
        orderRepository.save(order);

        System.out.println("✅ Orden guardada: " + orderId);
        System.out.println("👤 Cliente: " + request.getCustomerName());
        System.out.println("📱 Teléfono: " + request.getCustomerPhone());

        // 5.b. Notificar a Slack (NUEVA LÍNEA CLAVE)
        slackService.notifyNewOrder(request);
        // Esta llamada no bloqueará la respuesta gracias a la ejecución asíncrona

        // 6. Respuesta
        return CreateOrderResponse.builder()
                .success(true)
                .orderId(orderId)
                .pointsEarned(pointsEarned) // Opcional: Incluye los puntos ganados en la respuesta
                .message("Pedido creado exitosamente")
                .build();
    }
}