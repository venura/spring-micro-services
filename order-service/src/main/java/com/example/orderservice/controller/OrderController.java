package com.example.orderservice.controller;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.OrderDTO;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    private final StreamBridge streamBridge;
    private final ExecutorService traceableExecutorService;

    @GetMapping
    public String getOrderNumber() {
        return "sdfdfd";
    }

    @PostMapping
    public String placeOrder(@RequestBody OrderDTO orderDTO) {
        log.info("Placing order.....");
        circuitBreakerFactory.configureExecutorService(traceableExecutorService);
        //Casting to TraceableCircuitBreaker will be thrown if not set `spring.sleuth.circuitbreaker.enabled`
        Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("inventory");
        Supplier<Boolean> bs = () -> orderDTO.getOrderLineItems().stream()
                .allMatch(orderLineItem -> inventoryClient.checkStock(orderLineItem.getSkuCode()));
        //runs in a different thread therefore need to configure to pass the security context from parent thread to chidl
        //pls see the security config
        boolean allProductsInStock = circuitBreaker.run(bs, throwable -> false);

        if (allProductsInStock) {
            log.info("All products available....");
            var order = new Order();
            order.setOrderLineItems(orderDTO.getOrderLineItems());
            order.setOrderNumber(UUID.randomUUID().toString());
            orderRepository.save(order);
            //need to add MessageBuilder.withPayload to support distributed tracing via async calls
            //need to do the same change in notification service
            streamBridge.send("notificationEventSupplier-out-0", MessageBuilder.withPayload(order.getId()).build());
            log.info("notificationEventSupplier-out-0", order.getId());
            return "Order placed successfully";
        } else {
            log.warn("All stocks are not available.....");
            return "Order failed";
        }
    }
}
