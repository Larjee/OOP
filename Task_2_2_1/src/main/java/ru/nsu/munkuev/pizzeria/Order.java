package ru.nsu.munkuev.pizzeria;

import java.time.Instant;

public class Order {
    private final int id;
    private final Instant createdAt;
    private volatile OrderState state;

    public Order(int id) {
        this(id, Instant.now(), OrderState.QUEUED);
    }

    public Order(int id, Instant createdAt, OrderState state) {
        this.id = id;
        this.createdAt = createdAt;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public synchronized OrderState getState() {
        return state;
    }

    public synchronized void setState(OrderState state) {
        this.state = state;
        System.out.printf("[%d] %s%n", id, state.label());
    }
}
