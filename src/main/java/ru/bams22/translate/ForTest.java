package ru.bams22.translate;


import java.io.*;

public class ForTest {
    public static void main(String[] args) {
        String str = "\"Здравствуй МИР!!!\"";
        File outputFile = new File("test.txt");
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "Cp1251"))) {
                bufferedWriter.write(str);
                bufferedWriter.newLine();
                bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
