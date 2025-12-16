package ru.nsu.munkuev;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SubstringTest {

    private final SubstringFinder substring = new SubstringFinder();

    @Test
    void asciiSingleOccurrence() throws IOException {
        Path file = createTempFile("hello world");
        int[] res = substring.find(file, "world");
        assertArrayEquals(new int[]{6}, res);
    }

    @Test
    void asciiMultipleAndOverlapping() throws IOException {
        Path file = createTempFile("aaaa");
        int[] res = substring.find(file, "aa");
        assertArrayEquals(new int[]{0, 1, 2}, res);
    }

    @Test
    void cyrillicSingleOccurrence() throws IOException {
        Path file = createTempFile("привет мир");
        int[] res = substring.find(file, "мир");
        assertArrayEquals(new int[]{7}, res);
    }

    @Test
    void cyrillicMultipleOccurrences() throws IOException {
        Path file = createTempFile("кот котик кот");
        int[] res = substring.find(file, "кот");
        assertArrayEquals(new int[]{0, 4, 10}, res);
    }

    @Test
    void chineseText() throws IOException {
        Path file = createTempFile("你好，世界！你好！");
        int[] res = substring.find(file, "你好");
        assertArrayEquals(new int[]{0, 6}, res);
    }

    @Test
    void noOccurrences() throws IOException {
        Path file = createTempFile("abcdefg");
        int[] res = substring.find(file, "xyz");
        assertArrayEquals(new int[0], res);
    }

    @Test
    void substringLongerThanFile() throws IOException {
        Path file = createTempFile("hi");
        int[] res = substring.find(file, "hello");
        assertArrayEquals(new int[0], res);
    }

    @Test
    void emptySubstringReturnsEmpty() throws IOException {
        Path file = createTempFile("whatever");
        int[] res = substring.find(file, "");
        assertArrayEquals(new int[0], res);
    }

    @Test
    void emptyFile() throws IOException {
        Path file = createTempFile("");
        int[] res = substring.find(file, "test");
        assertArrayEquals(new int[0], res);
    }

    @Test
    void matchAtStartAndEnd() throws IOException {
        Path file = createTempFile("абвгд абв");
        int[] res = substring.find(file, "абв");
        assertArrayEquals(new int[]{0, 6}, res);
    }

    @Test
    void invalidUtf8ThrowsRuntimeException() throws IOException {
        Path path = Files.createTempFile("substring-test-invalid-", ".bin");
        byte[] invalid = new byte[]{(byte) 0xC3, 0x28};
        Files.write(path, invalid);

        assertThrows(IOException.class,
                () -> substring.find(path, "x"));
    }

    private Path createTempFile(String content) throws IOException {
        Path path = Files.createTempFile("substring-test-", ".txt");
        Files.writeString(path, content, StandardCharsets.UTF_8);
        return path;
    }
}
