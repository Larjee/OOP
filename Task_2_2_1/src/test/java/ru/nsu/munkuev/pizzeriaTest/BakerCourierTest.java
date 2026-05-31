package ru.nsu.munkuev.pizzeriaTest;

import org.junit.jupiter.api.Test;

import ru.nsu.munkuev.pizzeria.CustomOrderQueue;
import ru.nsu.munkuev.pizzeria.Storage;
import ru.nsu.munkuev.pizzeria.PizzeriaLifecycle;
import ru.nsu.munkuev.pizzeria.Order;
import ru.nsu.munkuev.pizzeria.Baker;
import ru.nsu.munkuev.pizzeria.OrderState;
import ru.nsu.munkuev.pizzeria.Courier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BakerCourierTest {

    @Test
    void bakerShouldBakeAndMoveOrderToStorage() throws Exception {
        CustomOrderQueue queue = new CustomOrderQueue();
        Storage storage = new Storage(2);
        PizzeriaLifecycle lifecycle = new PizzeriaLifecycle();
        Order order = new Order(1);
        queue.put(order);
        queue.stopAccepting();

        Thread bakerThread = new Thread(new Baker(1, 10, queue, storage, lifecycle));
        bakerThread.start();
        bakerThread.join(1000);

        assertFalse(bakerThread.isAlive());
        assertEquals(OrderState.STORED, order.getState());
        assertEquals(List.of(order), storage.snapshot());
        assertTrue(lifecycle.getActiveOrders().isEmpty());
        assertTrue(new Baker(99, 1, queue, storage, lifecycle).toString().contains("99"));
    }

    @Test
    void courierShouldDeliverOrdersFromStorage() throws Exception {
        Storage storage = new Storage(5);
        PizzeriaLifecycle lifecycle = new PizzeriaLifecycle();
        Order first = new Order(1);
        Order second = new Order(2);
        storage.put(first);
        storage.put(second);
        storage.close();

        Thread courierThread = new Thread(new Courier(2, 2, 10, storage, lifecycle));
        courierThread.start();
        courierThread.join(1000);

        assertFalse(courierThread.isAlive());
        assertEquals(OrderState.DELIVERED, first.getState());
        assertEquals(OrderState.DELIVERED, second.getState());
        assertTrue(lifecycle.getActiveOrders().isEmpty());
        assertTrue(new Courier(77, 1, 1, storage, lifecycle).toString().contains("77"));
    }
}
