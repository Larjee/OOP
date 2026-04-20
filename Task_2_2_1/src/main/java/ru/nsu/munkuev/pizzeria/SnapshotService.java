package ru.nsu.munkuev.pizzeria;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class SnapshotService {
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public void save(String fileName, Collection<Order> orders) throws IOException {
        SnapshotUnit unit = new SnapshotUnit();
        for (Order order : orders) {
            order.setState(OrderState.SUSPENDED);
            unit.pendingOrders.add(new SnapshotUnit.OrderSnapshot(order.getId(), order.getState().name()));
        }
        objectMapper.writeValue(new File(fileName), unit);
    }
}
