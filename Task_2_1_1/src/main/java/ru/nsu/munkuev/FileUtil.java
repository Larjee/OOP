package ru.nsu.munkuev;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Вспомогательный класс для работы с массивами и файлами
 */
public class FileUtil {

    /**
     * Метод, записывающий массив в файл
     * @param arr массив
     * @param filename имя файла
     * @throws IOException
     */
    public static void writeArrayToFile(int[] arr, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int value : arr) {
                writer.write(Integer.toString(value));
                writer.newLine();
            }
        }
    }
}
