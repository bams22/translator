package ru.bams22.translate.model;

public enum FileCharset {
    UTF_8 {
        @Override
        public String toString() {
            return "UTF-8";
        }
    },
    windows_1251 {
        @Override
        public String toString() {
            return "windows-1251";
        }
    }
}
