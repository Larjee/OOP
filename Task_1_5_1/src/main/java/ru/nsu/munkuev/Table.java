package ru.nsu.munkuev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Table implements Block {

    public static final int ALIGN_LEFT   = 0;
    public static final int ALIGN_RIGHT  = 1;
    public static final int ALIGN_CENTER = 2;

    private final int[] alignments;           // может быть null
    private final List<List<Inline>> rows;    // rows[0] = header

    private Table(int[] alignments, List<List<Inline>> rows) {
        this.alignments = alignments == null ? null : alignments.clone();
        this.rows = List.copyOf(rows);
    }

    @Override
    public String toMarkdown() {
        if (rows.isEmpty()) {
            return "";
        }

        //Сериализуем все ячейки заранее
        final int cols = rows.get(0).size();
        List<List<String>> rendered = new ArrayList<>(rows.size());
        for (List<Inline> row : rows) {
            List<String> r = new ArrayList<>(cols);
            for (int c = 0; c < cols; c++) {
                r.add(row.get(c).toMarkdown());
            }
            rendered.add(r);
        }

        //Считаем ширины колонок
        int[] widths = new int[cols];
        for (List<String> row : rendered) {
            for (int c = 0; c < cols; c++) {
                widths[c] = Math.max(widths[c], row.get(c).length());
            }
        }

        //Строим вывод
        StringBuilder sb = new StringBuilder();
        sb.append(rowLine(rendered.get(0), widths)).append("\n");
        sb.append(alignLine(cols, widths)).append("\n");
        for (int i = 1; i < rendered.size(); i++) {
            sb.append(rowLine(rendered.get(i), widths));
            if (i + 1 < rendered.size()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }


    @Override
    public String toString() {
        return toMarkdown();
    }

    private String rowLine(List<String> row, int[] widths) {
        StringBuilder sb = new StringBuilder("| ");
        for (int c = 0; c < row.size(); c++) {
            int a = (alignments != null && c < alignments.length) ? alignments[c] : ALIGN_LEFT;
            sb.append(pad(row.get(c), widths[c], a)).append(" | ");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private String alignLine(int cols, int[] widths) {
        StringBuilder sb = new StringBuilder("| ");
        for (int c = 0; c < cols; c++) {
            int a = (alignments != null && c < alignments.length) ? alignments[c] : ALIGN_LEFT;
            sb.append(alignmentCell(widths[c], a)).append(" | ");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private static String pad(String s, int width, int align) {
        int diff = width - s.length();
        if (diff <= 0) {
            return s;
        }

        return switch (align) {
            case ALIGN_RIGHT -> " ".repeat(diff) + s;
            case ALIGN_CENTER -> {
                int left = diff / 2;
                int right = diff - left;
                yield " ".repeat(left) + s + " ".repeat(right);
            }
            default -> s + " ".repeat(diff); // LEFT
        };
    }

    private static String alignmentCell(int width, int align) {
        int w = Math.max(3, width); // Минимум для валидного маркдауна

        return switch (align) {
            case ALIGN_RIGHT -> "-".repeat(w-1) + ":";
            case ALIGN_CENTER -> ":" + "-".repeat(Math.max(1, w - 2)) + ":";
            default -> "-".repeat(w);
        };
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Table t) && Arrays.equals(this.alignments, t.alignments) && Objects.equals(this.rows, t.rows);
    }

    @Override
    public int hashCode() {
        return 31 * Arrays.hashCode(alignments) + Objects.hash(rows);
    }

    // ========================= Builder =========================

    public static final class Builder {
        private int[] alignments = null;
        private int rowLimit = Integer.MAX_VALUE;
        private final List<List<Inline>> rows = new ArrayList<>();
        private Integer colCount = null;

        public Builder withAlignments(int... alignments) {
            this.alignments = alignments == null ? null : alignments.clone();
            return this;
        }

        public Builder withRowLimit(int limit) {
            if (limit <= 0) {
                throw new IllegalArgumentException("rowLimit must be > 0");
            }
            this.rowLimit = limit;
            return this;
        }

        public Builder addRow(Object... cells) {
            if (rows.size() >= rowLimit) {
                return this;
            }

            if (cells == null) {
                cells = new Object[0];
            }

            List<Inline> row = new ArrayList<>(cells.length);
            for (Object cell : cells) {
                row.add(asInline(cell));
            }

            if (colCount == null) {
                colCount = row.size();
            }
            if (row.size() != colCount) {
                throw new IllegalArgumentException("All rows must have same column count");
            }

            rows.add(List.copyOf(row));
            return this;
        }

        private Inline asInline(Object cell) {
            if (cell instanceof Inline in) {
                return in;
            }
            return new Text.Plain(String.valueOf(cell));
        }

        public Table build() {
            return new Table(alignments, rows);
        }
    }
}
