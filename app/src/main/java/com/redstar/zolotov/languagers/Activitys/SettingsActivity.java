package com.redstar.zolotov.languagers.Activitys;

/*
Активность настроик
№6
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.redstar.zolotov.languagers.Objects.WordsController;
import com.redstar.zolotov.languagers.R;

public class SettingsActivity extends AppCompatActivity {
    private EditText wordsOfDay, daysOfWords, savannaWords, savannaHp, savannaDelay;

    /*
    ---При старте №1---
    * установка значений
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);

        wordsOfDay = (EditText) findViewById(R.id.text_words_of_day);
        daysOfWords = (EditText) findViewById(R.id.text_day_of_words);
        savannaWords = (EditText) findViewById(R.id.text_savanna_words);
        savannaHp = (EditText) findViewById(R.id.text_savanna_hp);
        savannaDelay = (EditText) findViewById(R.id.text_savanna_delay);

        wordsOfDay.setText(WordsController.WORDS_OF_DAY + "");
        daysOfWords.setText(WordsController.DAY_OF_WORDS + "");
        savannaWords.setText(WordsController.SAVANNA_WORDS_COUNT + "");
        savannaHp.setText(WordsController.SAVANNA_HP + "");
        savannaDelay.setText(WordsController.SAVANNA_DELAY / 1000 + "");
    }

    /*
    ---Создание меню №2---
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    /*
    ---Обработка нажатий в меню №3---
    * 0 - запуск проверки корректности, получение данных, зауск списи в файл
    * 1 - назад
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_apple:
                try {
                    if (CeekSetting()) {
                        return false;
                    }
                    WordsController.WORDS_OF_DAY = Integer.parseInt(wordsOfDay.getText().toString());
                    WordsController.DAY_OF_WORDS = Integer.parseInt(daysOfWords.getText().toString());
                    WordsController.SAVANNA_WORDS_COUNT = Integer.parseInt(savannaWords.getText().toString());
                    WordsController.SAVANNA_HP = Integer.parseInt(savannaHp.getText().toString());
                    WordsController.SAVANNA_DELAY = Integer.parseInt(savannaDelay.getText().toString()) * 1000;

                    WordsController.OutputWords(SettingsActivity.this);
                    return true;
                }
                catch (Exception e) {
                    MainActivity.ErrorLog(SettingsActivity.this, e, 630);
                    return false;
                }

            case R.id.item_back:
                finish();
                return true;
        }
        return false;
    }

    /*
    ---Проверка корректности №4---
     */
    private boolean CeekSetting () {
        try {
            if (
                    (Integer.parseInt(wordsOfDay.getText().toString()) <= 0) ||
                    (Integer.parseInt(daysOfWords.getText().toString()) <= 0) ||
                    (Integer.parseInt(savannaWords.getText().toString()) <= 0) ||
                    (Integer.parseInt(savannaHp.getText().toString()) <= 0) ||
                    (Integer.parseInt(savannaDelay.getText().toString()) <= 0)) {

                Toast.makeText(this, "Не корректные настройки", Toast.LENGTH_LONG).show();
                return true;
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Не корректные настройки", Toast.LENGTH_LONG).show();
            return true;
        }
    }
}
