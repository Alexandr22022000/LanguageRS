package com.redstar.zolotov.languagers.Activitys;

/*
Активность главного меню
№1
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.redstar.zolotov.languagers.Objects.WordsController;
import com.redstar.zolotov.languagers.R;

public class MainActivity extends AppCompatActivity {

    /*
    ---Метод при старте №1---
    * Проверка наличия файла Main
    * Вызов создания файла Main, если его нет
    * Вызов получение данных из файла Main
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String[] files = fileList();
            boolean isCreate = false;

            for (String file : files) {
                if (file.equals("EnglishWords")) {
                    isCreate = true;
                }
            }

            if (isCreate) {
                WordsController.InputWords(this);
            }
            else {
                WordsController.CreateWordsFile(this);
                WordsController.InputWords(this);
            }
        }
        catch (Exception e) {
            ErrorLog(MainActivity.this, e, 110);
        }
    }

    /*
    ---Создание меню №2---
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /*
    ---Обработка нажатий в меню №3---
    * 0 - открытие настроик
    * 1 - резервное копирование
    * 2 - резервная загрузка
    * 3 - резервный сброс
    * 4 - об авторе
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.item_settings:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    break;

                case R.id.item_copy:
                    final EditText text = new EditText(this);
                    text.setText(WordsController.GetMainText(MainActivity.this));
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder
                            .setTitle("Скопируйте код ниже")
                            .setView(text)
                            .setCancelable(true)
                            .setPositiveButton("ОК", null);
                    builder.create().show();
                    return true;

                case R.id.item_past:
                    final EditText text1 = new EditText(this);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1
                            .setTitle("Вставте ранее скопированый код ниже")
                            .setView(text1)
                            .setCancelable(true)
                            .setNegativeButton("Отмена", null)
                            .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    WordsController.SetMainText(text1.getText().toString(), MainActivity.this);
                                    WordsController.InputWords(MainActivity.this);
                                }
                            });
                    builder1.create().show();
                    return true;

                case R.id.item_reset:
                    AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
                    builder3
                            .setTitle("Вы уверены?")
                            .setNegativeButton("Нет", null)
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    WordsController.CreateWordsFile(MainActivity.this);
                                    WordsController.InputWords(MainActivity.this);
                                }
                            })
                            .setCancelable(true);
                    builder3.create().show();
                    break;

                case R.id.item_about:
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2
                            .setTitle("Информация об авторе")
                            .setMessage("Компания: RedStar. Разрабочик: Золотов Александр Александрович. Данное приложение созданно для изучения возможностей IntelliJ IDEA.")
                            .setIcon(R.mipmap.red_star)
                            .setCancelable(true)
                            .setPositiveButton("ОК", null);
                    builder2.create().show();
                    return true;
            }
        }
        catch (Exception e) {
            MainActivity.ErrorLog(MainActivity.this, e, 130);
        }
        return false;
    }

    /*
    ---Обработка нажатий на кнопки №4---
    * 0 - учить новые слова + проверка
    * 1 - слова на изучении + проверка
    * 2 - новые слова
    * 3 - изученные слова
    * 4 - саванна + проверка
     */
    public void PressKey (View v) {
        try {
            switch (v.getId()) {
                case R.id.learn_key:
                    if (WordsController.NEW_WORDS.size() == 0) {
                        Toast.makeText(MainActivity.this, "Нет новых слов", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startActivity(new Intent(this, NewLearnActivity.class));
                    }
                    break;

                case R.id.memo_key:
                    if (WordsController.LEARN_WORDS.size() == 0) {
                        Toast.makeText(MainActivity.this, "Нет слов на изучении", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startActivity(new Intent(this, LearnWordsActivity.class));
                    }
                    break;

                case R.id.new_words_key:
                    Intent intent = new Intent(this, WordsActivity.class);
                    intent.putExtra("type", true);
                    startActivity(intent);
                    break;

                case R.id.learn_words_key:
                    //startActivity(new Intent(this, KnowWordsActivity.class));
                    Intent intent1 = new Intent(this, WordsActivity.class);
                    intent1.putExtra("type", false);
                    startActivity(intent1);
                    break;

                case R.id.savanna_game:
                    if ((WordsController.NEW_WORDS.size() + WordsController.LEARN_WORDS.size() + WordsController.KNOW_WORDS.size() < 4) || (WordsController.KNOW_WORDS.size() < WordsController.SAVANNA_WORDS_COUNT)) {
                        Toast.makeText(this, "Не достаточно слов для игры", Toast.LENGTH_LONG).show();
                    }
                    else {
                        startActivity(new Intent(this, SavannaActivity.class));
                    }
                    break;
            }
        }
        catch (Exception e) {
            MainActivity.ErrorLog(MainActivity.this, e, 140);
        }
    }

    /*
    ---Вывод сообщения об ошибки №0---
     */
    public static void ErrorLog (Context context, Exception e, int code) {
        try {
            EditText editText = new EditText(context);
            editText.setText("Error:" + code + "; " + e.toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder
                    .setTitle("Ошибка №" + code + ". Отправте код ошибки автору приложения.")
                    .setView(editText)
                    .setPositiveButton("OK", null)
                    .setCancelable(true);
            builder.create().show();
        }
        catch (Exception ex) {
            Toast.makeText(context, "Неизвестная ошибка " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
