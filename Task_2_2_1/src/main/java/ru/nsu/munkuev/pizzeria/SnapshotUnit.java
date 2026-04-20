package ru.nsu.munkuev.pizzeria;

import java.util.ArrayList;
import java.util.List;

public class SnapshotUnit {
    public List<OrderSnapshot> pendingOrders = new ArrayList<>();

    public static class OrderSnapshot {
        public int id;
        public String state;

        public OrderSnapshot() {}

        public OrderSnapshot(int id, String state) {
            this.id = id;
            this.state = state;
        }
    }
}
