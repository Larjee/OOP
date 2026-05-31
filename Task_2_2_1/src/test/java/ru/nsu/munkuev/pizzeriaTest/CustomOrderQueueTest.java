package ru.nsu.munkuev.pizzeriaTest;

import org.junit.jupiter.api.Test;

import ru.nsu.munkuev.pizzeria.Order;
import ru.nsu.munkuev.pizzeria.CustomOrderQueue;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomOrderQueueTest {

    @Test
    void shouldPutTakeSnapshotAndStopAccepting() throws Exception {
        CustomOrderQueue queue = new CustomOrderQueue();
        Order first = new Order(1);
        Order second = new Order(2);

        queue.put(first);
        queue.put(second);

        assertTrue(queue.isAccepting());
        assertEquals(List.of(first, second), queue.snapshot());
        assertFalse(queue.isEmpty());

        assertSame(first, queue.take());
        assertSame(second, queue.take());
        assertTrue(queue.isEmpty());

        queue.stopAccepting();
        assertFalse(queue.isAccepting());
        assertNull(queue.take());
        assertThrows(IllegalStateException.class, () -> queue.put(new Order(3)));
    }
}
