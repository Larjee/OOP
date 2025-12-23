package ru.nsu.munkuev;

import java.util.Objects;

public abstract class Text implements Inline {

    public static final class Plain extends Text {
        private final String value;

        public Plain(String value) {
            this.value = value == null ? "" : value;
        }

        @Override public String toMarkdown() {
            return escape(value);
        }

        public String value() {
            return value;
        }

        @Override public boolean equals(Object o) {
            return (o instanceof Plain p) && Objects.equals(value, p.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    public static final class Bold extends Text {
        private final Inline inner;

        public Bold(Inline inner) {
            this.inner = Objects.requireNonNull(inner);
        }

        public Bold(String s) {
            this(new Plain(s));
        }

        @Override public String toMarkdown() {
            return "**" + inner.toMarkdown() + "**";
        }

        @Override public boolean equals(Object o) {
            return (o instanceof Bold b) && Objects.equals(inner, b.inner);
        }

        @Override public int hashCode() {
            return Objects.hash(inner);
        }
    }

    public static final class Italic extends Text {
        private final Inline inner;

        public Italic(Inline inner) {
            this.inner = Objects.requireNonNull(inner);
        }

        public Italic(String s) {
            this(new Plain(s));
        }

        @Override public String toMarkdown() {
            return "*" + inner.toMarkdown() + "*";
        }

        @Override public boolean equals(Object o) {
            return (o instanceof Italic i) && Objects.equals(inner, i.inner);
        }

        @Override public int hashCode() {
            return Objects.hash(inner);
        }
    }

    public static final class Strike extends Text {
        private final Inline inner;

        public Strike(Inline inner) {
            this.inner = Objects.requireNonNull(inner);
        }

        public Strike(String s) {
            this(new Plain(s));
        }

        @Override public String toMarkdown() {
            return "~~" + inner.toMarkdown() + "~~";
        }

        @Override public boolean equals(Object o) {
            return (o instanceof Strike s) && Objects.equals(inner, s.inner);
        }

        @Override public int hashCode() {
            return Objects.hash(inner);
        }
    }

    public static final class Code extends Text {
        private final String code;

        public Code(String code) {
            this.code = code == null ? "" : code;
        }

        @Override public String toMarkdown() {
            return "`" + code.replace("`", "\\`") + "`";
        }

        @Override public boolean equals(Object o) {
            return (o instanceof Code c) && Objects.equals(code, c.code);
        }

        @Override public int hashCode() {
            return Objects.hash(code);
        }
    }

    static String escape(String s) {
        return s.replace("\\", "\\\\").replace("|", "\\|");
    }
}
