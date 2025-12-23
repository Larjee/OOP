package ru.nsu.munkuev;

import java.util.List;

final class BlocksUtility {
    public BlocksUtility() {}

    static String joinBlocks(List<? extends Block> blocks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < blocks.size(); i++) {
            sb.append(blocks.get(i).toMarkdown());
            if (i + 1 < blocks.size()) sb.append("\n\n"); // пустая строка между блоками
        }
        return sb.toString();
    }
}