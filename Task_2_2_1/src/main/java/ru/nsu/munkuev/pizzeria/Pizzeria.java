package ru.nsu.munkuev.pizzeria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Pizzeria {
    private final PizzeriaConfig config;
    private final CustomOrderQueue orderQueue;
    private final Storage storage;
    private final PizzeriaLifecycle lifecycle;
    private final SnapshotService snapshotService;
    private final List<Thread> workerThreads = new ArrayList<>();
    private Thread generatorThread;

    public Pizzeria(PizzeriaConfig config) {
        this.config = config;
        this.orderQueue = new CustomOrderQueue();
        this.storage = new Storage(config.storageCapacity);
        this.lifecycle = new PizzeriaLifecycle();
        this.snapshotService = new SnapshotService();
    }

    public void open() throws InterruptedException, IOException {
        startWorkers();
        Thread.sleep(config.workDurationMs);
        shutdown();
    }

    private void startWorkers() {
        generatorThread = new Thread(new OrderGenerator(config, orderQueue), "order-generator");
        generatorThread.start();

        for (int i = 0; i < config.bakers.size(); i++) {
            Thread t = new Thread(new Baker(i + 1, config.bakers.get(i), orderQueue, storage, lifecycle), "baker-" + (i + 1));
            workerThreads.add(t);
            t.start();
        }
        for (int i = 0; i < config.couriers.size(); i++) {
            Thread t = new Thread(new Courier(i + 1, config.couriers.get(i), config.courierTripTimeMs, storage, lifecycle), "courier-" + (i + 1));
            workerThreads.add(t);
            t.start();
        }
    }

    private void shutdown() throws InterruptedException, IOException {
        lifecycle.stopAcceptingOrders();
        orderQueue.stopAccepting();

        if (config.shutdownMode == ShutdownMode.GRACEFUL_FINISH) {
            generatorThread.join();
            waitUntilDrained();
            storage.close();
            joinWorkers();
            return;
        }

        generatorThread.interrupt();
        storage.close();
        interruptWorkers();
        joinWorkers();
        snapshotService.save(config.snapshotFile, collectUnfinishedOrders());
    }

    private void waitUntilDrained() throws InterruptedException {
        while (!orderQueue.isEmpty() || !storage.isEmpty() || !lifecycle.getActiveOrders().isEmpty()) {
            Thread.sleep(50);
        }
    }

    private void interruptWorkers() {
        for (Thread thread : workerThreads) {
            thread.interrupt();
        }
    }

    private void joinWorkers() throws InterruptedException {
        for (Thread thread : workerThreads) {
            thread.join();
        }
    }

    private List<Order> collectUnfinishedOrders() {
        Map<Integer, Order> result = new LinkedHashMap<>();
        for (Order order : orderQueue.snapshot()) {
            result.put(order.getId(), order);
        }
        for (Order order : storage.snapshot()) {
            result.put(order.getId(), order);
        }
        for (Order order : lifecycle.getActiveOrders()) {
            result.put(order.getId(), order);
        }
        return result.values().stream()
                .sorted(Comparator.comparingInt(Order::getId))
                .toList();
    }
}
