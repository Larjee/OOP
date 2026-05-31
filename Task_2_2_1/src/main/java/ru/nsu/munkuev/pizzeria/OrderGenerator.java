package ru.nsu.munkuev.pizzeria;

public class OrderGenerator implements Runnable {
    private final PizzeriaConfig config;
    private final CustomOrderQueue queue;

    public OrderGenerator(PizzeriaConfig config, CustomOrderQueue queue) {
        this.config = config;
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 1; i <= config.ordersToGenerate; i++) {
            if (!queue.isAccepting()) {
                break;
            }
            Order order = new Order(i);
            order.setState(OrderState.QUEUED);
            queue.put(order);
            try {
                Thread.sleep(config.orderGenerationIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        queue.stopAccepting();
    }
}
