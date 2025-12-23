package ru.nsu.munkuev;

import java.util.Objects;

public final class Heading implements Block {
    private final int level;        //Могут быть значения от 1 до 6
    private final Inline text;

    public Heading(int level, Inline text) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("Heading level must be 1..6");
        }
        this.level = level;
        this.text = Objects.requireNonNull(text);
    }

    @Override public String toMarkdown() {
        return "#".repeat(level) + " " + text.toMarkdown();
    }

    @Override public boolean equals(Object o) {
        return (o instanceof Heading h) && level == h.level && Objects.equals(text, h.text);
    }

    @Override public int hashCode() {
        return Objects.hash(level, text);
    }
}
