package ru.nsu.munkuev;

import java.util.Objects;

public final class Image implements Inline {
    private final Inline alt;
    private final String url;

    public Image(Inline alt, String url) {
        this.alt = Objects.requireNonNull(alt);
        this.url = url == null ? "" : url;
    }

    @Override public String toMarkdown() {
        return "![" + alt.toMarkdown() + "](" + url + ")";
    }

    @Override public boolean equals(Object o) {
        return (o instanceof Image i) && Objects.equals(alt, i.alt) && Objects.equals(url, i.url);
    }

    @Override public int hashCode() {
        return Objects.hash(alt, url);
    }
}
