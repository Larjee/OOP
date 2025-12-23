package ru.nsu.munkuev;

public interface Element {
    String toMarkdown();

    default String toStringMarkdown() { // если вдруг удобнее явно
        return toMarkdown();
    }

    static Text text(Object o) {
        if (o == null) return new Text.Plain("");
        if (o instanceof Element e) return new Text.Plain(e.toMarkdown()); // редко нужно
        return new Text.Plain(String.valueOf(o));
    }

    static Inline inline(Object o) {
        if (o instanceof Inline in) return in;
        return new Text.Plain(String.valueOf(o));
    }
}