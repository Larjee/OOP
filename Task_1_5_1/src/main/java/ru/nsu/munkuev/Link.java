package ru.nsu.munkuev;

import java.util.Objects;

public final class Link implements Inline {
    private final Inline label;
    private final String url;

    public Link(Inline label, String url) {
        this.label = Objects.requireNonNull(label);
        this.url = url == null ? "" : url;
    }

    @Override public String toMarkdown() {
        return "[" + label.toMarkdown() + "](" + url + ")";
    }

    @Override public boolean equals(Object o) {
        return (o instanceof Link l) && Objects.equals(label, l.label) && Objects.equals(url, l.url);
    }

    @Override public int hashCode() {
        return Objects.hash(label, url);
    }
}