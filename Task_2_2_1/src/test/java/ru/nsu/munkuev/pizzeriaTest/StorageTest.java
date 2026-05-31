package ru.nsu.munkuev.pizzeriaTest;

import ru.nsu.munkuev.pizzeria.Storage;
import ru.nsu.munkuev.pizzeria.Order;
import ru.nsu.munkuev.pizzeria.OrderState;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


class StorageTest {

    @Test
    void shouldPutAndTakeBatchRespectingCapacity() throws Exception {
        Storage storage = new Storage(3);
        Order first = new Order(1);
        Order second = new Order(2);

        storage.put(first);
        storage.put(second);

        assertEquals(OrderState.STORED, first.getState());
        assertEquals(OrderState.STORED, second.getState());
        assertFalse(storage.isEmpty());
        assertEquals(List.of(first, second), storage.snapshot());

        List<Order> batch = storage.takeBatch(5);
        assertEquals(List.of(first, second), batch);
        assertTrue(storage.isEmpty());
    }

    @Test
    void shouldWaitWhenFullAndContinueAfterSpaceReleased() throws Exception {
        Storage storage = new Storage(1);
        Order first = new Order(1);
        Order second = new Order(2);
        storage.put(first);

        Thread producer = new Thread(() -> {
            try {
                storage.put(second);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        producer.start();
        Thread.sleep(80);

        assertEquals(OrderState.WAITING_FOR_STORAGE, second.getState());

        List<Order> batch = storage.takeBatch(1);
        assertEquals(List.of(first), batch);

        producer.join(1000);
        assertFalse(producer.isAlive());
        assertEquals(OrderState.STORED, second.getState());
        assertEquals(List.of(second), storage.snapshot());
    }

    @Test
    void shouldReturnEmptyBatchWhenClosedAndEmpty() throws Exception {
        Storage storage = new Storage(1);
        storage.close();

        assertTrue(storage.takeBatch(2).isEmpty());

        Order order = new Order(1);
        storage.put(order);
        assertTrue(storage.isEmpty());
    }
}
