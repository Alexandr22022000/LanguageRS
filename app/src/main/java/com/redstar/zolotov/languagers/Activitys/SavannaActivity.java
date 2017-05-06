package com.redstar.zolotov.languagers.Activitys;

/*
Активность саванны
№7
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.redstar.zolotov.languagers.Objects.Savanna;
import com.redstar.zolotov.languagers.Objects.Word;
import com.redstar.zolotov.languagers.Objects.WordsController;
import com.redstar.zolotov.languagers.R;

import java.util.ArrayList;
import java.util.Random;

public class SavannaActivity extends AppCompatActivity {
    private Savanna savanna;

    /*
    ---При старте №1---
    * создание саванны
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_savanna);

        Button[] buttons = new Button[4];
        buttons[0] = (Button) findViewById(R.id.button_1);
        buttons[1] = (Button) findViewById(R.id.button_2);
        buttons[2] = (Button) findViewById(R.id.button_3);
        buttons[3] = (Button) findViewById(R.id.button_4);

        savanna = new Savanna(this, buttons, (TextView) findViewById(R.id.word), (TextView) findViewById(R.id.toast), (TextView) findViewById(R.id.text_hp_count), (TextView) findViewById(R.id.text_words_count), (Button) findViewById(R.id.button_start));
    }

    /*
    ---Обработка нажатий на клавиши №2---
    * 0 (START):
        * писк самого низкого приаретета
        * выборка по приорететам см.
        * вывод полученных слов
    * 1 - в саванну
    * 2 - в саванну
    * 3 - в саванну
    * 4 - в саванну
     */
    public void PressKey (View v) {
        switch (v.getId()) {
            case R.id.button_start:
                try {
                    int lowAcquire = WordsController.KNOW_WORDS.get(0).ACQUIRE;
                    for (Word word : WordsController.KNOW_WORDS) {
                        if (word.ACQUIRE < lowAcquire) {
                            lowAcquire = word.ACQUIRE;
                        }
                    }

                    ArrayList<Word> words = new ArrayList<>();
                    while (true) {
                        ArrayList<Word> buferWords = new ArrayList<>();
                        for (Word word : WordsController.KNOW_WORDS) {                                                          //выбор слов с текущим приорететом
                            if (word.ACQUIRE == lowAcquire) {
                                buferWords.add(word);
                            }
                        }

                        if (buferWords.size() <= (WordsController.SAVANNA_WORDS_COUNT - words.size())) {                        //если слов не больше чем нужно - взять все
                            words.addAll(buferWords);
                        }
                        else {
                            Random random = new Random();
                            while (words.size() < WordsController.SAVANNA_WORDS_COUNT) {                                        //если нет - выбирать рандомные
                                int randomItem = (buferWords.size() == 1) ? 0 : random.nextInt(buferWords.size() - 1);
                                words.add(buferWords.get(randomItem));
                                buferWords.remove(randomItem);
                            }
                        }

                        if (words.size() >= WordsController.SAVANNA_WORDS_COUNT) {                                              //если набрано нужное количество слов - закончить, если нет - поднять теущий приоретет
                            break;
                        }

                        lowAcquire++;
                    }
                    savanna.StartGame(words);
                }
                catch (Exception e) {
                    MainActivity.ErrorLog(SavannaActivity.this, e, 720);
                }
                break;

            case R.id.button_1:
                savanna.PressKey(0);
                break;

            case R.id.button_2:
                savanna.PressKey(1);
                break;

            case R.id.button_3:
                savanna.PressKey(2);
                break;

            case R.id.button_4:
                savanna.PressKey(3);
                break;
        }
    }
}
