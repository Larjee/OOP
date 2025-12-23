package ru.nsu.munkuev;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Quote implements Block {
    private final List<Block> blocks;

    public Quote(List<Block> blocks) {
        this.blocks = List.copyOf(blocks);
    }

    @Override public String toMarkdown() {
        // Каждый строковый вывод блоков превратим в строки и добавим "> " в начало
        String raw = BlocksUtility.joinBlocks(blocks); String[] lines = raw.split("\\R", -1);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            sb.append("> ").append(lines[i]);
            if (i + 1 < lines.length) sb.append("\n");
        }
        return sb.toString();
    }

    @Override public boolean equals(Object o) {
        return (o instanceof Quote q) && Objects.equals(blocks, q.blocks);
    }

    @Override public int hashCode() { return Objects.hash(blocks); }

    public static final class Builder {
        private final List<Block> blocks = new ArrayList<>();
        public Builder add(Block b) {
            blocks.add(Objects.requireNonNull(b)); return this;
        }
        public Quote build() {
            return new Quote(blocks);
        }
    }
}
