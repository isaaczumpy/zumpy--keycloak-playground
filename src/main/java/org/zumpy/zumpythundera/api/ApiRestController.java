package org.zumpy.zumpythundera.api;


import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Slf4j
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class ApiRestController {

    @Data
    @Builder
    public static class Order {
        private UUID id;
        private Float price;
    }

    private ResponseEntity<HashMap<String, Object>> createResponse(HttpStatus status, String message, Object data) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", message);
        if (data != null) response.put("data", data);
        return new ResponseEntity<>(response, status);
    }

    private final HashMap<UUID, Order> orders = new HashMap<>();

    private Order createOrder(Float price) {
        UUID id = UUID.randomUUID();
        Order order = Order.builder().id(id).price(price).build();
        orders.put(id, order);
        return order;
    }

    @GetMapping("/public/ping")
    public ResponseEntity<HashMap<String, Object>> pingEndpoint() {
        return createResponse(HttpStatus.OK, "Pong", null);
    }

    @PostMapping("/orders")
    @PreAuthorize("hasRole('CREATE_ORDER')")
    public ResponseEntity<HashMap<String, Object>> createOrderEndpoint(@RequestBody Map<String, Float> requestBody) {
        Float price = requestBody.get("price");
        if (price == null) return createResponse(HttpStatus.BAD_REQUEST, "Price is required", null);
        Order order = createOrder(price);
        return createResponse(HttpStatus.CREATED, "Order created", order);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('VIEW_ORDER')")
    public ResponseEntity<HashMap<String, Object>> getOrdersEndpoint() {
        List<Order> orderList = new ArrayList<>(this.orders.values());
        return createResponse(HttpStatus.OK, "Orders retrieved", orderList);
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('VIEW_ORDER')")
    public ResponseEntity<HashMap<String, Object>> getOrderByIdEndpoint(@PathVariable UUID id) {
        Order order = orders.get(id);
        if (order == null) return createResponse(HttpStatus.NOT_FOUND, "Order not found", null);
        return createResponse(HttpStatus.OK, "Order retrieved", order);
    }

    @DeleteMapping("/orders/{id}")
    @PreAuthorize("hasRole('DELETE_ORDER')")
    public ResponseEntity<HashMap<String, Object>> deleteOrdersEndpoint(@PathVariable UUID id) {
        Order order = orders.remove(id);
        if (order == null) return createResponse(HttpStatus.NOT_FOUND, "Order not found", null);
        return createResponse(HttpStatus.OK, "Order deleted", order);
    }

}
