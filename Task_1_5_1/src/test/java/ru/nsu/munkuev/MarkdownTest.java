package ru.nsu.munkuev;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

class MarkdownTest {
    // ============================================================
    // =========================== Text ===========================
    // ============================================================
    @Test
    void plainEscapePipeAndBackslash() {
        assertEquals("a\\|b", new Text.Plain("a|b").toMarkdown());
        assertEquals("a\\\\b", new Text.Plain("a\\b").toMarkdown());
    }

    @Test
    void bold_italic_strike_codeRenderCorrectly() {
        assertEquals("**x**", new Text.Bold("x").toMarkdown());
        assertEquals("*x*", new Text.Italic("x").toMarkdown());
        assertEquals("~~x~~", new Text.Strike("x").toMarkdown());
        assertEquals("`x`", new Text.Code("x").toMarkdown());
    }

    @Test
    void codeEscapeBackticks() {
        assertEquals("`a\\`b`", new Text.Code("a`b").toMarkdown());
    }

    @Test
    void bold_hashCode() {
        var a = new Text.Bold(new Text.Plain("10"));
        var b = new Text.Bold(new Text.Plain("10"));
        var c = new Text.Bold(new Text.Plain("11"));

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);
        assertNotEquals(null, a);
        assertNotEquals("not text", a);
    }


    // ============================================================
    // =========================== HeadingCodeBlock ===========================
    // ============================================================
    @Test
    void headingRenderCorrectly() {
        var h1 = new Heading(1, new Text.Plain("Title"));
        var h3 = new Heading(3, new Text.Plain("Title"));

        assertEquals("# Title", h1.toMarkdown());
        assertEquals("### Title", h3.toMarkdown());
    }

    @Test
    void headingRejectWrongLevel() {
        assertThrows(IllegalArgumentException.class, () -> new Heading(0, new Text.Plain("x")));
        assertThrows(IllegalArgumentException.class, () -> new Heading(7, new Text.Plain("x")));
    }

    @Test
    void codeBlockRenderWithLanguageAndContent() {
        var cb = new CodeBlock("java", "int x = 1;");
        assertEquals("```java\nint x = 1;\n```", cb.toMarkdown());
    }

    @Test
    void codeBlockBuilderBuildCorrectly() {
        var cb = new CodeBlock.Builder()
                .language("txt")
                .line("a")
                .line("b")
                .build();

        assertEquals("```txt\na\nb\n```", cb.toMarkdown());
    }

    @Test
    void link_hashCode() {
        var a = new Link(new Text.Plain("x"), "u");
        var b = new Link(new Text.Plain("x"), "u");
        var c = new Link(new Text.Plain("x"), "v");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }


    // ============================================================
    // =========================== ListQuote ===========================
    // ============================================================
    @Test
    void unorderedListRenderCorrectly() {
        var list = new ListBlock.Builder(ListBlock.Type.UNORDERED)
                .addItem(new Heading(1, new Text.Plain("A")))
                .addItem(new Heading(1, new Text.Plain("B")))
                .build();

        assertEquals("- # A\n- # B", list.toMarkdown());
    }

    @Test
    void orderedListRenderCorrectly() {
        var list = new ListBlock.Builder(ListBlock.Type.ORDERED)
                .addItem(new Heading(1, new Text.Plain("A")))
                .addItem(new Heading(1, new Text.Plain("B")))
                .build();

        assertEquals("1. # A\n2. # B", list.toMarkdown());
    }

    @Test
    void listBuilderThrowWhenEndItemWithoutBegin() {
        var b = new ListBlock.Builder(ListBlock.Type.UNORDERED);
        assertThrows(IllegalStateException.class, b::endItem);
    }

    @Test
    void quotePrefixEveryLineWithGt() {
        var quote = new Quote.Builder()
                .add(new Heading(1, new Text.Plain("Hello")))
                .add(new Heading(2, new Text.Plain("World")))
                .build();
        assertEquals("> # Hello\n> \n> ## World", quote.toMarkdown());
    }

    @Test
    void taskListRenderCorrectly() {
        var tasks = new TaskList.Builder()
                .add(false, "a")
                .add(true, new Text.Bold("b"))
                .build();

        assertEquals("- [ ] a\n- [x] **b**", tasks.toMarkdown());
    }

    @Test
    void taskList_hashCode() {
        var a = new TaskList.Builder().add(false, "x").build();
        var b = new TaskList.Builder().add(false, "x").build();
        var c = new TaskList.Builder().add(true, "x").build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }


    // ============================================================
    // =========================== Table ===========================
    // ============================================================
    @Test
    void tableMatchExampleFormat() {
        var table = new Table.Builder()
                .withAlignments(Table.ALIGN_RIGHT, Table.ALIGN_LEFT)
                .withRowLimit(8)
                .addRow("Index", "Random")
                .addRow(1, new Text.Bold("9"))
                .addRow(2, 5)
                .addRow(3, 9)
                .addRow(4, new Text.Bold("7"))
                .addRow(5, 1)
                .build();

        String expected =
                  "| Index | Random |\n"
                + "| ----: | ------ |\n"
                + "|     1 | **9**  |\n"
                + "|     2 | 5      |\n"
                + "|     3 | 9      |\n"
                + "|     4 | **7**  |\n"
                + "|     5 | 1      |";

        assertEquals(expected, table.toMarkdown());
        assertEquals(expected, table.toString());
    }

    @Test
    void tableRespectRowLimit() {
        var b = new Table.Builder()
                .withRowLimit(2)
                .addRow("A", "B")
                .addRow(1, 2)
                .addRow(3, 4);

        var t = b.build();
        String md = t.toMarkdown();
        assertEquals(3, md.split("\\R").length);
        assertTrue(md.contains("| 1 | 2 |"));
        assertFalse(md.contains("| 3 | 4 |"));
    }

    @Test
    void tableThrowOnColumnCountMismatch() {
        var b = new Table.Builder().addRow("A", "B");
        assertThrows(IllegalArgumentException.class, () -> b.addRow(1));
    }

    @Test
    void withRowLimitRejectNonPositive() {
        assertThrows(IllegalArgumentException.class, () -> new Table.Builder().withRowLimit(0));
        assertThrows(IllegalArgumentException.class, () -> new Table.Builder().withRowLimit(-1));
    }



    // ============================================================
    // =========================== Element ===========================
    // ============================================================
    @Test
    void text_nullReturnEmptyPlain() {
        Text t = Element.text(null);
        assertEquals("", t.toMarkdown());
        assertTrue(t instanceof Text.Plain);
    }

    @Test
    void text_objectUseToString() {
        Text t = Element.text(123);
        assertEquals("123", t.toMarkdown());
    }

    @Test
    void text_elementUseToMarkdown() {
        Element e = () -> "**x**";
        Text t = Element.text(e);
        assertEquals("**x**", t.toMarkdown());
    }

    @Test
    void inlineReturnInlineIfAlreadyInline() {
        Inline in = new Text.Bold("x");
        assertSame(in, Element.inline(in));
    }

    @Test
    void inlinedWrapNonInlineIntoPlain() {
        Inline in = Element.inline(42);
        assertEquals("42", in.toMarkdown());
        assertTrue(in instanceof Text.Plain);
    }


    // ============================================================
    // =========================== Image ===========================
    // ============================================================
    @Test
    void imageRenderCorrectly() {
        Image img = new Image(new Text.Plain("alt"), "https://x.y/img.png");
        assertEquals("![alt](https://x.y/img.png)", img.toMarkdown());
    }

    @Test
    void image_nullUrl_BecomeEmpty() {
        Image img = new Image(new Text.Plain("alt"), null);
        assertEquals("![alt]()", img.toMarkdown());
    }

    @Test
    void equals_hashCode_Work() {
        Image a = new Image(new Text.Plain("alt"), "u");
        Image b = new Image(new Text.Plain("alt"), "u");
        Image c = new Image(new Text.Plain("alt2"), "u");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "nope");
    }

    @Test
    void ctor_RejectNullAlt() {
        assertThrows(NullPointerException.class, () -> new Image(null, "u"));
    }


    // ============================================================
    // =========================== Document ===========================
    // ============================================================
    @Test
    void documentJoinBlocksWithBlankLine() {
        Document doc = new Document.Builder()
                .add(new Heading(1, new Text.Plain("A")))
                .add(new Heading(2, new Text.Plain("B")))
                .build();

        assertEquals("# A\n\n## B", doc.toMarkdown());
        assertEquals(doc.toMarkdown(), doc.toString());
    }

    @Test
    void hashCode_document() {
        Document a = new Document.Builder().add(new Heading(1, new Text.Plain("X"))).build();
        Document b = new Document.Builder().add(new Heading(1, new Text.Plain("X"))).build();
        Document c = new Document.Builder().add(new Heading(1, new Text.Plain("Y"))).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }

    @Test
    void builderRejectNullBlock() {
        assertThrows(NullPointerException.class, () -> new Document.Builder().add(null));
    }
}
