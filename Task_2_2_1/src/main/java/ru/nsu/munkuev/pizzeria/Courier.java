package ru.nsu.munkuev.pizzeria;

import java.util.List;

public class Courier implements Runnable {
    private final int id;
    private final int trunkCapacity;
    private final int tripTimeMs;
    private final Storage storage;
    private final PizzeriaLifecycle lifecycle;

    public Courier(int id, int trunkCapacity, int tripTimeMs, Storage storage, PizzeriaLifecycle lifecycle) {
        this.id = id;
        this.trunkCapacity = trunkCapacity;
        this.tripTimeMs = tripTimeMs;
        this.storage = storage;
        this.lifecycle = lifecycle;
    }

    @Override
    public void run() {
        try {
            while (true) {
                List<Order> batch = storage.takeBatch(trunkCapacity);
                if (batch.isEmpty()) {
                    break;
                }
                lifecycle.registerActiveBatch(batch);
                for (Order order : batch) {
                    order.setState(OrderState.DELIVERING);
                }
                Thread.sleep(tripTimeMs);
                for (Order order : batch) {
                    order.setState(OrderState.DELIVERED);
                    lifecycle.unregisterActive(order);
                }
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return "Courier{" + "id=" + id + '}';
    }
}
