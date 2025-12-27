package ru.nsu.munkuev;

import java.util.List;

final class BlocksUtility {
    static String joinBlocks(List<? extends Block> blocks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < blocks.size(); i++) {
            sb.append(blocks.get(i).toMarkdown());
            if (i + 1 < blocks.size()) sb.append("\n\n"); // пустая строка между блоками
        }
        return sb.toString();
    }

    static String trimLastNewLines(String text) {
        if (text == null) {
            return "";
        }
        if (!text.endsWith("\n") && !text.endsWith("\r")) {
            return text;
        }
        return text.replaceAll("[\\r\\n]+$", "");
    }

    private BlocksUtility() {
        throw new UnsupportedOperationException("Utility class");
    }
}