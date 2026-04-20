package ru.nsu.munkuev.pizzeria;

public class Baker implements Runnable {
    private final int id;
    private final int bakingTimeMs;
    private final CustomOrderQueue orderQueue;
    private final Storage storage;
    private final PizzeriaLifecycle lifecycle;

    public Baker(int id, int bakingTimeMs, CustomOrderQueue orderQueue, Storage storage, PizzeriaLifecycle lifecycle) {
        this.id = id;
        this.bakingTimeMs = bakingTimeMs;
        this.orderQueue = orderQueue;
        this.storage = storage;
        this.lifecycle = lifecycle;
    }

    @Override
    public void run() {
        try {
            while (lifecycle.shouldBakersContinue()) {
                Order order = orderQueue.take();
                if (order == null) {
                    break;
                }
                lifecycle.registerActive(order);
                order.setState(OrderState.BAKING);
                Thread.sleep(bakingTimeMs);
                storage.put(order);
                lifecycle.unregisterActive(order);
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return "Baker{" + "id=" + id + '}';
    }
}
