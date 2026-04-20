package ru.nsu.munkuev.pizzeriaTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import ru.nsu.munkuev.pizzeria.Storage;
import ru.nsu.munkuev.pizzeria.Order;
import ru.nsu.munkuev.pizzeria.OrderState;
import ru.nsu.munkuev.pizzeria.PizzeriaLifecycle;
import ru.nsu.munkuev.pizzeria.SnapshotService;
import ru.nsu.munkuev.pizzeria.SnapshotUnit;

import static org.junit.jupiter.api.Assertions.*;

class PizzeriaLifecycleAndSnapshotTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldTrackActiveOrdersAndAcceptanceFlag() {
        PizzeriaLifecycle lifecycle = new PizzeriaLifecycle();
        Order first = new Order(1);
        Order second = new Order(2);

        assertTrue(lifecycle.isAcceptingOrders());
        assertTrue(lifecycle.shouldBakersContinue());
        assertTrue(lifecycle.shouldCouriersContinue());

        lifecycle.registerActive(first);
        lifecycle.registerActiveBatch(List.of(second));
        assertEquals(2, lifecycle.getActiveOrders().size());

        lifecycle.unregisterActive(first);
        assertEquals(1, lifecycle.getActiveOrders().size());
        assertTrue(lifecycle.getActiveOrders().contains(second));

        lifecycle.stopAcceptingOrders();
        assertFalse(lifecycle.isAcceptingOrders());
    }

    @Test
    void shouldSerializeSuspendedOrders(@TempDir Path tempDir) throws Exception {
        SnapshotService service = new SnapshotService();
        Path file = tempDir.resolve("snapshot.json");
        Order first = new Order(1, Instant.now(), OrderState.BAKING);
        Order second = new Order(2, Instant.now(), OrderState.STORED);

        service.save(file.toString(), List.of(first, second));

        assertEquals(OrderState.SUSPENDED, first.getState());
        assertEquals(OrderState.SUSPENDED, second.getState());

        SnapshotUnit Unit = new ObjectMapper().readValue(file.toFile(), SnapshotUnit.class);
        assertEquals(2, Unit.pendingOrders.size());
        assertEquals(1, Unit.pendingOrders.get(0).id);
        assertEquals("SUSPENDED", Unit.pendingOrders.get(0).state);
        assertEquals(2, Unit.pendingOrders.get(1).id);

        SnapshotUnit.OrderSnapshot snapshot = new SnapshotUnit.OrderSnapshot(10, "QUEUED");
        assertEquals(10, snapshot.id);
        assertEquals("QUEUED", snapshot.state);
    }
}
