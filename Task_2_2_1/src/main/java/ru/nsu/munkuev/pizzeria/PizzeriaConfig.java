package ru.nsu.munkuev.pizzeria;

import java.util.List;

public class PizzeriaConfig {
    public List<Integer> bakers;
    public List<Integer> couriers;
    public int storageCapacity;
    public int workDurationMs;
    public int orderGenerationIntervalMs;
    public int ordersToGenerate;
    public int courierTripTimeMs;
    public ShutdownMode shutdownMode;
    public String snapshotFile;
}
