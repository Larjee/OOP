package ru.nsu.munkuev;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;


/**
 * Утилитный класс для поиска подстроки в текстовом файле в кодировке UTF-8.
 * <p>
 * Класс реализует потоковый посимвольный (по Unicode code point'ам) поиск:
 * файл читается последовательно из {@link java.io.InputStream}, без
 * загрузки всего содержимого в память, что позволяет работать с очень
 * большими файлами.
 * <p>
 * Искомая подстрока и содержимое файла сравниваются на уровне Unicode
 * code point'ов, а возвращаемые индексы соответствуют позициям символов
 * (именно символов, а не байтов) в тексте файла.
 * <p>
 * Основной метод {@link #find(String, String)} принимает имя
 * файла и подстроку, и возвращает массив всех индексов начала вхождений
 * этой подстроки. В случае отсутствия вхождений возвращается пустой массив.
 */
public class SubstringFinder{
    private static final int ASCII_MASK           = 0b1000_0000;

    private static final int TWO_BYTE_MASK        = 0b1110_0000;
    private static final int TWO_BYTE_PREFIX      = 0b1100_0000;

    private static final int THREE_BYTE_MASK      = 0b1111_0000;
    private static final int THREE_BYTE_PREFIX    = 0b1110_0000;

    private static final int FOUR_BYTE_MASK       = 0b1111_1000;
    private static final int FOUR_BYTE_PREFIX     = 0b1111_0000;

    private static final int CONT_MASK            = 0b1100_0000;
    private static final int CONT_PREFIX          = 0b1000_0000;

    private static final int CONT_PAYLOAD_MASK    = 0b0011_1111;


    /**
     * Ищет заданную подстроку в указанном файле. Возвращает индексы всех символьных вхождений подстроки в файле.
     * В случае отсутствия подстроки в файле, возвращается пустой массив.
     * @param filename имя файла в котором будет искаться подстрока
     * @param substring подстрока
     * @return массив всех вхождений подстроки в файле
     *
     */
    public int[] find(String filename, String substring) {
        int[] pattern = substring.codePoints().toArray();
        int m = pattern.length;

        if (m == 0) {
            return new int[0];
        }

        List<Integer> positions = new ArrayList<>();

        try (InputStream in = new FileInputStream(filename)) {
            int[] window = new int[m]; //Бегающее окно
            int winSize = 0;           //Размер окна
            int cpIndex = 0;           //индекс по символам (code point’ам)

            int codePoint;
            while ((codePoint = readUtf8CodePoint(in)) != -1) {
                if (winSize < m) {
                    window[winSize++] = codePoint;
                }
                else {
                    System.arraycopy(window, 1, window, 0, m - 1);
                    window[m - 1] = codePoint;
                }

                if (winSize == m && isStringsEquals(window, pattern)) {
                    positions.add(cpIndex - m + 1);
                }

                cpIndex++;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        int[] res = new int[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            res[i] = positions.get(i);
        }
        return res;
    }

    /**
     * Проверяет на равенство два массива. Используется в поиске подстроки, когда сравнивается "бегающее окно"
     * с заданной подстрокой.
     * @param a первый массив
     * @param b второй массив
     * @return {@code true} если равны, {@code false} в противном случае
     */
    private boolean isStringsEquals(int[] a, int[] b) {
        for (int i = 0; i < b.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * Считывает один Unicode-символ (code point), закодированный в UTF-8,
     * из переданного входного потока.
     * <p>
     * Возвращает целочисленное значение code point'а или -1,
     * если достигнут конец потока до чтения очередного символа.
     * В случае некорректной UTF-8-последовательности или
     * обрыва многобайтного символа бросает {@link IOException}.
     *
     * @param in входной поток, из которого читаются байты UTF-8
     * @return считанный Unicode code point или -1 при конце потока
     * @throws IOException при ошибке ввода-вывода или некорректной UTF-8 последовательности
     */
    private int readUtf8CodePoint(InputStream in) throws IOException {
        int b0 = in.read();
        if (b0 == -1) return -1;

        // 1-байтовый (ASCII)
        if ((b0 & ASCII_MASK) == 0) {
            return b0;
        }

        int size;
        int codePoint;

        // Два октета
        if ((b0 & TWO_BYTE_MASK) == TWO_BYTE_PREFIX) {
            size = 1;
            codePoint = b0 & 0b0001_1111;
        }
        // Три октета
        else if ((b0 & THREE_BYTE_MASK) == THREE_BYTE_PREFIX) {
            size = 2;
            codePoint = b0 & 0b0000_1111;
        }
        // Четыре октета
        else if ((b0 & FOUR_BYTE_MASK) == FOUR_BYTE_PREFIX) {
            size = 3;
            codePoint = b0 & 0b0000_0111;
        } else {
            throw new IOException("Invalid UTF-8 start byte: " + b0);
        }

        for (int i = 0; i < size; i++) {
            int bx = in.read();
            if (bx == -1) {
                throw new IOException("Truncated UTF-8 sequence");
            }
            if ((bx & CONT_MASK) != CONT_PREFIX) {
                throw new IOException("Invalid UTF-8 continuation byte: " + bx);
            }
            codePoint = (codePoint << 6) | (bx & CONT_PAYLOAD_MASK);
        }

        return codePoint;
    }

}
