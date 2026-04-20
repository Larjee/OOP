package ru.nsu.munkuev.pizzeria;

import java.util.LinkedList;
import java.util.List;

public class CustomOrderQueue {
    private final LinkedList<Order> queue = new LinkedList<>();
    private boolean accepting = true;

    public synchronized void put(Order order) {
        if (!accepting) {
            throw new IllegalStateException("Queue does not accept new orders");
        }
        queue.addLast(order);
        notifyAll();
    }

    public synchronized Order take() throws InterruptedException {
        while (queue.isEmpty() && accepting) {
            wait();
        }
        if (queue.isEmpty()) {
            return null;
        }
        return queue.removeFirst();
    }

    public synchronized void stopAccepting() {
        accepting = false;
        notifyAll();
    }

    public synchronized boolean isAccepting() {
        return accepting;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized List<Order> snapshot() {
        return List.copyOf(queue);
    }
}
