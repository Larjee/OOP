package ru.nsu.munkuev;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Document implements Element {
    private final List<Block> blocks;

    private Document(List<Block> blocks) {
        this.blocks = List.copyOf(blocks);
    }

    @Override
    public String toMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < blocks.size(); i++) {
            sb.append(blocks.get(i).toMarkdown());
            if (i + 1 < blocks.size()) {
                sb.append("\n\n");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toMarkdown();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Document d) && Objects.equals(blocks, d.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blocks);
    }

    public static final class Builder {
        private final List<Block> blocks = new ArrayList<>();

        public Builder add(Block b) {
            blocks.add(Objects.requireNonNull(b));
            return this;
        }

        public Document build() {
            return new Document(blocks);
        }
    }
}
