package ru.nsu.munkuev.pizzeria;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Storage {
    private final int capacity;
    private final LinkedList<Order> readyOrders = new LinkedList<>();
    private boolean open = true;

    public Storage(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(Order order) throws InterruptedException {
        while (readyOrders.size() >= capacity && open) {
            order.setState(OrderState.WAITING_FOR_STORAGE);
            wait();
        }
        if (!open) {
            return;
        }
        readyOrders.addLast(order);
        order.setState(OrderState.STORED);
        notifyAll();
    }

    public synchronized List<Order> takeBatch(int maxCount) throws InterruptedException {
        while (readyOrders.isEmpty() && open) {
            wait();
        }
        if (readyOrders.isEmpty()) {
            return List.of();
        }
        int count = Math.min(maxCount, readyOrders.size());
        List<Order> batch = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            batch.add(readyOrders.removeFirst());
        }
        notifyAll();
        return batch;
    }

    public synchronized void close() {
        open = false;
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return readyOrders.isEmpty();
    }

    public synchronized List<Order> snapshot() {
        return List.copyOf(readyOrders);
    }
}
