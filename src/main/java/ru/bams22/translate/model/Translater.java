package ru.bams22.translate.model;


public interface Translater {
    String translate(String lang, String input);
    String translate(String input);
}
