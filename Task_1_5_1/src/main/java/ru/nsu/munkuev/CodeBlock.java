package ru.nsu.munkuev;

import java.util.Objects;

public final class CodeBlock implements Block {
    private final String language;
    private final String code;

    public CodeBlock(String language, String code) {
        this.language = language == null ? "" : language;
        this.code = code == null ? "" : code;
    }

    @Override
    public String toMarkdown() {
        return "```" + language + "\n" + code + "\n```";
    }

    @Override
    public String toString() {
        return toMarkdown();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof CodeBlock cb)
                && Objects.equals(language, cb.language)
                && Objects.equals(code, cb.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, code);
    }

    public static final class Builder {
        private String language = "";
        private final StringBuilder code = new StringBuilder();

        public Builder language(String lang) {
            this.language = lang == null ? "" : lang;
            return this;
        }

        public Builder line(String s) {
            code.append(s == null ? "" : s).append("\n");
            return this;
        }

        public Builder raw(String s) {
            code.append(s == null ? "" : s);
            return this;
        }

        public CodeBlock build() {
            String c = code.toString();
            if (c.endsWith("\n")) c = c.substring(0, c.length() - 1);
            return new CodeBlock(language, c);
        }
    }
}
