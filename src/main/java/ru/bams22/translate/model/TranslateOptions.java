package ru.bams22.translate.model;

import java.io.File;

public class TranslateOptions {
    public Translater translater;
    public File inputFile;
    public FileCharset inputFileCharset;
    public FileCharset outputFileCharset;
    public Lang inputLang;
    public Lang outputLang;
}
