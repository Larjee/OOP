package ru.nsu.munkuev.pizzeriaTest;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;

import ru.nsu.munkuev.pizzeria.OrderGenerator;
import ru.nsu.munkuev.pizzeria.PizzeriaConfig;
import ru.nsu.munkuev.pizzeria.CustomOrderQueue;
import ru.nsu.munkuev.pizzeria.Order;
import ru.nsu.munkuev.pizzeria.OrderState;

import static org.junit.jupiter.api.Assertions.*;

class OrderGeneratorAndOrderTest {

    @Test
    void shouldGenerateConfiguredOrdersAndStopQueue() throws Exception {
        PizzeriaConfig config = new PizzeriaConfig();
        config.ordersToGenerate = 3;
        config.orderGenerationIntervalMs = 1;

        CustomOrderQueue queue = new CustomOrderQueue();
        new OrderGenerator(config, queue).run();

        assertEquals(1, queue.take().getId());
        assertEquals(2, queue.take().getId());
        assertEquals(3, queue.take().getId());
        assertNull(queue.take());
        assertFalse(queue.isAccepting());
    }

    @Test
    void shouldPreserveOrderDataAndPrintStateChanges() {
        Instant createdAt = Instant.parse("2026-04-20T12:00:00Z");
        Order order = new Order(7, createdAt, OrderState.QUEUED);
        assertEquals(7, order.getId());
        assertEquals(createdAt, order.getCreatedAt());
        assertEquals(OrderState.QUEUED, order.getState());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream original = System.out;
        try {
            System.setOut(new PrintStream(output));
            order.setState(OrderState.BAKING);
        } finally {
            System.setOut(original);
        }

        assertEquals(OrderState.BAKING, order.getState());
        assertTrue(output.toString().contains("[7] " + OrderState.BAKING.label()));
    }

    @Test
    void shouldExposeRussianLabels() {
        assertEquals("в очереди", OrderState.QUEUED.label());
        assertEquals("сериализован на следующий день", OrderState.SUSPENDED.label());
    }
}
