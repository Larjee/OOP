package ru.nsu.munkuev.pizzeriaTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.munkuev.pizzeria.Order;
import ru.nsu.munkuev.pizzeria.PizzeriaLifecycle;
import ru.nsu.munkuev.pizzeria.PizzeriaConfig;
import ru.nsu.munkuev.pizzeria.Pizzeria;
import ru.nsu.munkuev.pizzeria.ShutdownMode;
import ru.nsu.munkuev.pizzeria.Storage;
import ru.nsu.munkuev.pizzeria.CustomOrderQueue;
import ru.nsu.munkuev.pizzeria.SnapshotUnit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PizzeriaIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldCompleteWorkInGracefulMode() throws Exception {
        PizzeriaConfig config = new PizzeriaConfig();
        config.bakers = List.of(5);
        config.couriers = List.of(2);
        config.storageCapacity = 2;
        config.workDurationMs = 40;
        config.orderGenerationIntervalMs = 1;
        config.ordersToGenerate = 4;
        config.courierTripTimeMs = 2;
        config.shutdownMode = ShutdownMode.GRACEFUL_FINISH;
        config.snapshotFile = tempDir.resolve("graceful.json").toString();

        Pizzeria pizzeria = new Pizzeria(config);
        pizzeria.open();

        Storage storage = (Storage) getField(pizzeria, "storage");
        CustomOrderQueue queue = (CustomOrderQueue) getField(pizzeria, "orderQueue");
        PizzeriaLifecycle lifecycle = (PizzeriaLifecycle) getField(pizzeria, "lifecycle");

        assertTrue(storage.isEmpty());
        assertTrue(queue.isEmpty());
        assertTrue(lifecycle.getActiveOrders().isEmpty());
        assertFalse(Files.exists(Path.of(config.snapshotFile)));
    }

    @Test
    void shouldSnapshotUnfinishedOrdersInSnapshotMode() throws Exception {
        Path snapshot = tempDir.resolve("snapshot.json");
        PizzeriaConfig config = new PizzeriaConfig();
        config.bakers = List.of(100);
        config.couriers = List.of(100);
        config.storageCapacity = 1;
        config.workDurationMs = 10;
        config.orderGenerationIntervalMs = 1;
        config.ordersToGenerate = 20;
        config.courierTripTimeMs = 100;
        config.shutdownMode = ShutdownMode.SNAPSHOT_AND_STOP;
        config.snapshotFile = snapshot.toString();

        new Pizzeria(config).open();

        assertTrue(Files.exists(snapshot));
        SnapshotUnit unit = new ObjectMapper().readValue(snapshot.toFile(), SnapshotUnit.class);
        assertFalse(unit.pendingOrders.isEmpty());
        assertTrue(unit.pendingOrders.stream().allMatch(o -> "SUSPENDED".equals(o.state)));
    }

    @Test
    void shouldCollectUniqueSortedUnfinishedOrdersFromAllStages() throws Exception {
        PizzeriaConfig config = new PizzeriaConfig();
        config.bakers = List.of(1);
        config.couriers = List.of(1);
        config.storageCapacity = 2;
        config.workDurationMs = 1;
        config.orderGenerationIntervalMs = 1;
        config.ordersToGenerate = 1;
        config.courierTripTimeMs = 1;
        config.shutdownMode = ShutdownMode.GRACEFUL_FINISH;
        config.snapshotFile = tempDir.resolve("unused.json").toString();

        Pizzeria pizzeria = new Pizzeria(config);
        CustomOrderQueue queue = (CustomOrderQueue) getField(pizzeria, "orderQueue");
        Storage storage = (Storage) getField(pizzeria, "storage");
        PizzeriaLifecycle lifecycle = (PizzeriaLifecycle) getField(pizzeria, "lifecycle");

        Order order3 = new Order(3);
        Order order1 = new Order(1);
        Order order2 = new Order(2);
        Order duplicateOrder2 = order2;

        queue.put(order3);
        storage.put(order1);
        lifecycle.registerActiveBatch(List.of(order2, duplicateOrder2));

        Method collect = Pizzeria.class.getDeclaredMethod("collectUnfinishedOrders");
        collect.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Order> result = (List<Order>) collect.invoke(pizzeria);

        assertEquals(List.of(1, 2, 3), result.stream().map(Order::getId).toList());
    }

    private Object getField(Object target, String name) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }
}
