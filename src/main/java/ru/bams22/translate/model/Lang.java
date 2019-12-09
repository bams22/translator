package ru.bams22.translate.model;


public enum Lang {
    ENGLISH {
        @Override
        public String toString() {
            return "en";
        }
    },
    RUSSIAN {
        @Override
        public String toString() {
            return "ru";
        }
    }
}
