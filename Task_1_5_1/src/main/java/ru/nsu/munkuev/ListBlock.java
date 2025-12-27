package ru.nsu.munkuev;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ListBlock implements Block {
    public enum Type { ORDERED, UNORDERED }

    public static final class Item {
        private final List<Block> blocks;

        public Item(List<Block> blocks) {
            this.blocks = List.copyOf(blocks);
        }

        public List<Block> blocks() {
            return blocks;
        }

        @Override public boolean equals(Object o) {
            return (o instanceof Item i) && Objects.equals(blocks, i.blocks);
        }
        @Override public int hashCode() {
            return Objects.hash(blocks);
        }
    }

    private final Type type;
    private final List<Item> items;

    private ListBlock(Type type, List<Item> items) {
        this.type = Objects.requireNonNull(type);
        this.items = List.copyOf(items);
    }

    @Override public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < items.size(); idx++) {
            String prefix = (type == Type.UNORDERED) ? "- " : (idx + 1) + ". ";
            String itemText = BlocksUtility.joinBlocks(items.get(idx).blocks());
            String[] lines = itemText.split("\\R", -1);

            // первая строка с маркером, остальные — с отступом
            sb.append(prefix).append(lines.length == 0 ? "" : lines[0]);
            for (int i = 1; i < lines.length; i++) {
                sb.append("\n").append("  ").append(lines[i]);
            }
            if (idx + 1 < items.size()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override public boolean equals(Object o) {
        return (o instanceof ListBlock l)
                && type == l.type
                && Objects.equals(items, l.items);
    }
    @Override public int hashCode() { return Objects.hash(type, items); }

    public static final class Builder {
        private final Type type;
        private final List<Item> items = new ArrayList<>();
        private List<Block> current = null;

        public Builder(Type type) { this.type = Objects.requireNonNull(type); }

        public Builder beginItem() {
            if (current != null) {
                throw new IllegalStateException("Previous item not ended");
            }
            current = new ArrayList<>();
            return this;
        }

        public Builder addToItem(Block b) {
            if (current == null) {
                beginItem();
            }
            current.add(Objects.requireNonNull(b));
            return this;
        }

        public Builder endItem() {
            if (current == null) {
                throw new IllegalStateException("No item started");
            }
            items.add(new Item(current));
            current = null;
            return this;
        }

        public Builder addItem(Block single) {
            return beginItem().addToItem(single).endItem();
        }

        public ListBlock build() {
            if (current != null) {
                endItem();
            }
            return new ListBlock(type, items);
        }
    }
}
