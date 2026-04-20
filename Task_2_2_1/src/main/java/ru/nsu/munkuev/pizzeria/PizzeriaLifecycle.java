package ru.nsu.munkuev.pizzeria;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PizzeriaLifecycle {
    private volatile boolean acceptingOrders = true;
    private final Set<Order> activeOrders = ConcurrentHashMap.newKeySet();

    public boolean isAcceptingOrders() {
        return acceptingOrders;
    }

    public void stopAcceptingOrders() {
        this.acceptingOrders = false;
    }

    public void registerActive(Order order) {
        activeOrders.add(order);
    }

    public void registerActiveBatch(Collection<Order> orders) {
        activeOrders.addAll(orders);
    }

    public void unregisterActive(Order order) {
        activeOrders.remove(order);
    }

    public Set<Order> getActiveOrders() {
        return Set.copyOf(activeOrders);
    }

    public boolean shouldBakersContinue() {
        return true;
    }

    public boolean shouldCouriersContinue() {
        return true;
    }
}
