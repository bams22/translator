package ru.bams22.translate.model;

import java.io.*;
import java.sql.*;
import java.util.*;


public class Translate {
    private TranslateOptions translateOptions;
    private String[] text;
    private String[] textResult;
    private String translateCourse;
    private Connection SQLConnection;
    private String courseDB;
    private String invCourseDB;

    public Translate(TranslateOptions translateOptions) {
        this.translateOptions = translateOptions;
        this.translateCourse = translateOptions.inputLang + "-" + translateOptions.outputLang;
        this.courseDB = translateOptions.inputLang + "_" + translateOptions.outputLang;
        this.invCourseDB = translateOptions.outputLang + "_" + translateOptions.inputLang;
        try {
            SQLConnection = connectToDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private List<Word> getWords(String str, String chars) {
        if (str == null || str.length() == 0)
            return null;

        List<Word> result = new ArrayList<>();
        char[] strChar = str.toCharArray();
        boolean isWord;
        boolean isNeedTranslate = false;
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < strChar.length; i++) {
            isWord = chars.contains(String.valueOf(strChar[i]));
            if (isWord ^= isNeedTranslate) {
                result.add(new Word(buffer.toString(), isNeedTranslate));
                isNeedTranslate = !isNeedTranslate;
                buffer = new StringBuilder();
            }
            buffer.append(strChar[i]);
        }
        if (buffer.length() != 0) {
            result.add(new Word(buffer.toString(), isNeedTranslate));
        }
        return result;
    }

    private String translateString(String str) {
        String chars = "";
        if (translateOptions.inputLang == Lang.RUSSIAN) {
            chars = "йцукенгшщзхъфывапролджэячсмитьбюёЁЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ";
        } else if (translateOptions.inputLang == Lang.ENGLISH) {
            chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM'";
        } else {
            System.exit(-1);
        }
        StringBuilder result = new StringBuilder();
        List<Word> words = getWords(str, chars);
        if (words == null) {
            return result.toString();
        }
        for (Word word: words) {
            result.append(word.translate());
        }
        return result.toString();
    }

    private void translateText() {
        textResult = new String[text.length];
        for (int i = 0; i < textResult.length; i++) {
            textResult[i] = translateString(text[i]);
        }
    }

    private void readFile() {
        List<String> textList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(translateOptions.inputFile), translateOptions.inputFileCharset.toString()))) {
            while (reader.ready()) {
                textList.add(reader.readLine());
            }
            text = textList.toArray(new String[textList.size()]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void writeFile() {
        String prefix = "translate_" + translateCourse + "_";
        File outputFile = new File(translateOptions.inputFile.getParent() + "/" + prefix + translateOptions.inputFile.getName());
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            for (String str: textResult) {
                fileWriter.write(str);
                fileWriter.write("\n");
                fileWriter.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Connection connectToDB() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:resources/test.db3");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    private String getSQLTranslate(String source) {
        try {
            Statement statement = SQLConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("select translate from " + courseDB + " where source='" + source + "';");
            if (resultSet.isClosed()) {
                return null;
            }
            return resultSet.getString("translate");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setSQLTranslate(String source, String translate) {
        try {
            Statement statement = SQLConnection.createStatement();
            System.out.println(source + "-" + translate);
            statement.executeUpdate("insert into " + courseDB + " VALUES ('"+ source + "', '" + translate + "');");
            System.out.println(translate + "-" + source);
            statement.executeUpdate("insert into " + invCourseDB + " VALUES ('"+ translate + "', '" + source + "');");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void work() {
        readFile();
        translateText();
        writeFile();
    }


    class Word {
        String value;
        boolean needTranslate;
        Word(String value, boolean needTranslate) {
            this.value = value;
            this.needTranslate = needTranslate;
        }

        String translate() {
            if (needTranslate) {
                String result = getSQLTranslate(value);
                if (result != null) {
                    return result;
                } else {
                    String trn = translateOptions.translater.translate(translateCourse, value);
                    setSQLTranslate(value, trn);
                    return trn;
                }
            } else {
                return value;
            }
        }
    }
}