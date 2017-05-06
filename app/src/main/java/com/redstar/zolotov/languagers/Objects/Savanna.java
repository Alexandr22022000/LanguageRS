package com.redstar.zolotov.languagers.Objects;

/*
Минигра саванна
№11
 */

import android.app.Activity;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import com.redstar.zolotov.languagers.Activitys.MainActivity;
import com.redstar.zolotov.languagers.AsyncTasks.SavannaTimer;
import com.redstar.zolotov.languagers.R;

import java.util.ArrayList;
import java.util.Random;

public class Savanna {
    private Button[] buttons;
    private TextView word, toast, textHp, textWords;
    private Animation animation;
    private ArrayList<Word> words;
    private int thisWord, trueOption, hp;
    private SavannaTimer timer;
    private Button button;
    private Context context;
    public boolean gameIsStart = false;

    public Savanna (Activity context, Button[] buttons, TextView word, TextView toast, TextView textHp, TextView textWords, Button button) {
        this.buttons = buttons;
        this.word = word;
        this.toast = toast;
        this.textHp = textHp;
        this.textWords = textWords;
        this.button = button;
        this.context = context;
        animation = AnimationUtils.loadAnimation(context, R.anim.anim_savanna);
        animation.setDuration(WordsController.SAVANNA_DELAY);
    }

    /*
    ---Запуск игры №1---
    * запись слов
    * запуск обратного отсчета
     */
    public void StartGame (ArrayList<Word> words) {
        this.words = words;
        button.setVisibility(-1);
        thisWord = 0;
        hp = WordsController.SAVANNA_HP;
        textHp.setText(hp + "");
        timer = new SavannaTimer(this, toast);
        timer.execute(false);
    }

    /*
    ---Обработка нажатий на клавиши (варианты ответа) №2---
    * проверка заущена ли игра
    * остановка таймера
    * если ответ верный, следующие слово и +1 к изучаймости слова
    * если нет, запуск время окончено (не правильно)
     */
    public void PressKey (int id) {
        try {
            if (!gameIsStart) { return; }

            timer.cancel(false);
            if (id == trueOption) {
                words.get(thisWord - 1).ACQUIRE = words.get(thisWord - 1).ACQUIRE + 1;
                DownWord();
            }
            else {
                TimeEnd();
            }
        }
        catch (Exception e) {
            MainActivity.ErrorLog(context, e, 1120);
        }
    }

    /*
    ---Время окончено (не верный ответ) №3---
    * -1 hp
    * -1 к изучаймости слова
    * если hp кончилось, вывод вы "вы проиграли" и остановка игры
    * иначе, следующее слово
     */
    public void TimeEnd () {
        try {
            hp--;
            textHp.setText(hp + "");

            words.get(thisWord - 1).ACQUIRE = words.get(thisWord - 1).ACQUIRE - 1;

            if (hp <= 0) {
                toast.setTextSize(30);
                toast.setText("Вы проиграли");
                word.setText("");
                gameIsStart = false;
                button.setVisibility(1);
            }
            else {
                DownWord();
            }
        }
        catch (Exception e) {
            MainActivity.ErrorLog(context, e, 1130);
        }
    }

    /*
    ---Новое слово №4---
    * обновить счетчик слов
    * проверить не кончились ли слова (если кончились, то победа)
    * выбор правильного варианта ответа
    * получение ArrayList всех переводов слов
    * удаление правильного варианта ответа из ArrayList
    * установка рандомных переводов в свободные ячейки ответов
    * установка текста и анимации
    * запуск таймера
     */
    public void DownWord () {
        textWords.setText((words.size() - thisWord) + "");

        if (thisWord >= words.size()) {
            toast.setTextSize(30);
            toast.setText("Вы выйграли");
            word.setText("");
            gameIsStart = false;
            button.setVisibility(1);
            return;
        }

        Random random = new Random();
        String[] options = new String[4];

        trueOption = random.nextInt(3);
        options[trueOption] = words.get(thisWord).TRANSLATION_SMALL;
        word.setText(words.get(thisWord).WORD);
        thisWord++;

        ArrayList<String> falseOptions = new ArrayList<>();
        for (Word word : WordsController.NEW_WORDS) {
            falseOptions.add(word.TRANSLATION_SMALL);
        }
        for (Word word : WordsController.LEARN_WORDS) {
            falseOptions.add(word.TRANSLATION_SMALL);
        }
        for (Word word : WordsController.KNOW_WORDS) {
            falseOptions.add(word.TRANSLATION_SMALL);
        }

        for (int i = 0; i < falseOptions.size(); i++) {
            if (falseOptions.get(i).equals(options[trueOption])) {
                falseOptions.remove(i);
                break;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (i != trueOption) {
                options[i] = falseOptions.get(random.nextInt(falseOptions.size() - 1));
            }
        }

        for (int i = 0; i < 4; i++) {
            buttons[i].setText(options[i]);
        }

        word.setAnimation(animation);
        animation.start();
        timer = new SavannaTimer(this, toast);
        timer.execute(true);
    }
}
