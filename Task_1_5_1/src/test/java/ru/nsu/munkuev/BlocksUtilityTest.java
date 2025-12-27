package ru.nsu.munkuev;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.nsu.munkuev.BlocksUtility.trimLastNewLines;

class BlocksUtilityTest {

    @ParameterizedTest
    @MethodSource
    void testTrimLastNewLines(String text, String result) {
        assertEquals(result, trimLastNewLines(text));
    }

    public static Stream<Arguments> testTrimLastNewLines() {
        return Stream.of(
                Arguments.of("test", "test"),
                Arguments.of("test\n", "test"),
                Arguments.of("test\r\n", "test"),
                Arguments.of("test\r\n\r\n", "test"),
                Arguments.of("test\n\n\n", "test"),
                Arguments.of("", ""),
                Arguments.of(null, "")
        );
    }
}