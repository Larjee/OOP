package ru.nsu.munkuev;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SubstringTest {

    private final Substring substring = new Substring();

    private String createTempFile(String content) throws IOException {
        Path path = Files.createTempFile("substring-test-", ".txt");
        Files.writeString(path, content, StandardCharsets.UTF_8);
        return path.toString();
    }

    @Test
    void asciiSingleOccurrence() throws IOException {
        String file = createTempFile("hello world");
        int[] res = substring.find(file, "world");
        assertArrayEquals(new int[]{6}, res);
    }

    @Test
    void asciiMultipleAndOverlapping() throws IOException {
        String file = createTempFile("aaaa");
        int[] res = substring.find(file, "aa");
        assertArrayEquals(new int[]{0, 1, 2}, res);
    }

    @Test
    void cyrillicSingleOccurrence() throws IOException {
        String file = createTempFile("привет мир");
        int[] res = substring.find(file, "мир");
        assertArrayEquals(new int[]{7}, res);
    }

    @Test
    void cyrillicMultipleOccurrences() throws IOException {
        String file = createTempFile("кот котик кот");
        int[] res = substring.find(file, "кот");
        assertArrayEquals(new int[]{0, 4, 10}, res);
    }

    @Test
    void chineseText() throws IOException {
        String file = createTempFile("你好，世界！你好！");
        int[] res = substring.find(file, "你好");
        assertArrayEquals(new int[]{0, 6}, res);
    }

    @Test
    void noOccurrences() throws IOException {
        String file = createTempFile("abcdefg");
        int[] res = substring.find(file, "xyz");
        assertArrayEquals(new int[0], res);
    }

    @Test
    void substringLongerThanFile() throws IOException {
        String file = createTempFile("hi");
        int[] res = substring.find(file, "hello");
        assertArrayEquals(new int[0], res);
    }

    @Test
    void emptySubstringReturnsEmpty() throws IOException {
        String file = createTempFile("whatever");
        int[] res = substring.find(file, "");
        assertArrayEquals(new int[0], res);
    }

    @Test
    void emptyFile() throws IOException {
        String file = createTempFile("");
        int[] res = substring.find(file, "test");
        assertArrayEquals(new int[0], res);
    }

    @Test
    void matchAtStartAndEnd() throws IOException {
        String file = createTempFile("абвгд абв");
        int[] res = substring.find(file, "абв");
        assertArrayEquals(new int[]{0, 6}, res);
    }

    @Test
    void invalidUtf8ThrowsRuntimeException() throws IOException {
        Path path = Files.createTempFile("substring-test-invalid-", ".bin");
        byte[] invalid = new byte[]{(byte) 0xC3, 0x28};
        Files.write(path, invalid);

        assertThrows(RuntimeException.class,
                () -> substring.find(path.toString(), "x"));
    }
}
