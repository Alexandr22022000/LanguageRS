package com.redstar.zolotov.languagers.Objects;

/*
Объект слово
№9
 */

public class Word {
    public String WORD, TRANSLATION_SMALL, TRANSLATION;
    public int ACQUIRE;

    public Word (String word,String translationSmall,  String translation, int acquire) {
        WORD = word;
        TRANSLATION_SMALL = translationSmall;
        TRANSLATION = translation;
        ACQUIRE = acquire;
    }

    /*
    Перенос слова в другой ArrayList №2---
     */
    public Word MigrationWord (int acquire) {
        ACQUIRE = acquire;
        return this;
    }

    /*
    ---Добавление дня при изучении №3---
     */
    public void TimerWord () {
        ACQUIRE++;
    }
}
