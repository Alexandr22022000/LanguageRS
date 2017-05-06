package com.redstar.zolotov.languagers.Activitys;

/*
Активность отображения списка новых и изученных слов
№2
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import com.redstar.zolotov.languagers.Adapters.WordsAdapter;
import com.redstar.zolotov.languagers.Objects.Word;
import com.redstar.zolotov.languagers.Objects.WordsController;
import com.redstar.zolotov.languagers.R;

import java.util.ArrayList;

public class WordsActivity extends AppCompatActivity {
    private ListView list;
    private EditText screen;
    private ArrayList<Integer> ids;
    private ArrayList<Word> words;
    private boolean type;

    /*
    ---При создании №1---
    * получение типа активности (новые слова - true, изученные - false)
    * запуск обновления листа
    * установка действия при изменении строки поиска
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);

        list = (ListView) findViewById(R.id.list_view);
        screen = (EditText) findViewById(R.id.screen);

        type = getIntent().getBooleanExtra("type", true);
        words = (type ? WordsController.NEW_WORDS : WordsController.KNOW_WORDS);

        WordsUpdata();
        /*screen.setOnKeyListener(new View.OnKeyListener()
           {
               @Override
               public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    WordsUpdata();
                    return false;
               }
           }
        );*/

        screen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                WordsUpdata();
            }
        });
    }

    /*
    ---Создание меню №2---
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.words_list_menu, menu);
        return true;
    }

    /*
    ---Обработка нажатий в меню №3---
    * 0 - назад
    * 1 - добавить слово (запрос с кодом 0)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                finish();
                return true;

            case R.id.add_word:
                Intent intent = new Intent(this, SetWordActivity.class);
                intent.putExtra("type", type);
                startActivityForResult(intent, 0);
                return true;
        }
        return false;
    }

    /*
    ---Получение ответа на запрос №4---
    * проверка на успешность запроса
    * если код - 0, добавление слова, если 1 замена
    * запуск обновления листа
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode != RESULT_OK) {
                return;
            }

            if (requestCode == 0) {
                words.add(new Word(
                        data.getStringExtra("word"),
                        data.getStringExtra("smoleTranslation"),
                        data.getStringExtra("translation"),
                        data.getIntExtra("acquire", 0)));
            }
            else {
                words.set(data.getIntExtra("id", 0), new Word(
                        data.getStringExtra("word"),
                        data.getStringExtra("smoleTranslation"),
                        data.getStringExtra("translation"),
                        data.getIntExtra("acquire", 0)));
            }

            WordsController.OutputWords(WordsActivity.this);
            WordsController.SotrWords(WordsActivity.this);
            words = type ? WordsController.NEW_WORDS : WordsController.KNOW_WORDS;
            WordsUpdata();
        }
        catch (Exception e) {
            MainActivity.ErrorLog(WordsActivity.this, e, 240);
        }
    }

    /*
    ---Обработка выбора слова из листа №5---
    * Вывести диалог: забыл/знаю, изменить, удалить
    * 0 - перемещение слова + вы уверенны?
        * синхронизация массивов
        * запуск обновления
    * 1 - запрос с кодом 1
    * 2 - удаление слова + вы уверенны?
        * синхронизация массивов
        * запуск обновления
     */
    public void WordSelected (final int id) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setItems(new String[]{type ? "Знаю" : "Забыл", "Изменить", "Удалить"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                switch (i) {
                                    case 0:
                                        try {
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(WordsActivity.this);

                                            builder1
                                                    .setTitle("Вы уверены?")
                                                    .setNegativeButton("Нет", null)
                                                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            (type ? WordsController.KNOW_WORDS : WordsController.NEW_WORDS).add(words.get(ids.get(id)).MigrationWord(type ? 0 : 1));
                                                            words.remove((int) ids.get(id));

                                                            WordsController.OutputWords(WordsActivity.this);
                                                            WordsController.SotrWords(WordsActivity.this);
                                                            words = type ? WordsController.NEW_WORDS : WordsController.KNOW_WORDS;
                                                            WordsUpdata();
                                                        }
                                                    })
                                                    .setCancelable(true);

                                            builder1.create().show();
                                        }
                                        catch (Exception e) {
                                            MainActivity.ErrorLog(WordsActivity.this, e, 252);
                                        }
                                        break;

                                    case 1:
                                        Intent intent = new Intent(WordsActivity.this, SetWordActivity.class);

                                        intent.putExtra("word", words.get(ids.get(id)).WORD);
                                        intent.putExtra("smoleTranslation", words.get(ids.get(id)).TRANSLATION_SMALL);
                                        intent.putExtra("translation", words.get(ids.get(id)).TRANSLATION);
                                        intent.putExtra("acquire", words.get(ids.get(id)).ACQUIRE);
                                        intent.putExtra("id", ids.get(id));
                                        intent.putExtra("type", type);

                                        startActivityForResult(intent, 1);
                                        break;

                                    case 2:
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(WordsActivity.this);

                                        builder2
                                                .setTitle("Вы уверены?")
                                                .setNegativeButton("Нет", null)
                                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        try {
                                                            words.remove((int) ids.get(id));
                                                            WordsController.OutputWords(WordsActivity.this);
                                                            WordsController.SotrWords(WordsActivity.this);
                                                            words = type ? WordsController.NEW_WORDS : WordsController.KNOW_WORDS;
                                                            WordsUpdata();
                                                        }
                                                        catch (Exception e) {
                                                            MainActivity.ErrorLog(WordsActivity.this, e, 253);
                                                        }
                                                    }
                                                })
                                                .setCancelable(true);

                                        builder2.create().show();

                                        break;
                                }
                            }
                            catch (Exception e) {
                                MainActivity.ErrorLog(WordsActivity.this, e, 251);
                            }
                        }
                    })
                    .setCancelable(true);
            builder.create().show();
        }
        catch (Exception e) {
            MainActivity.ErrorLog(WordsActivity.this, e, 250);
        }
    }

    /*
    ---Обновление листа №6---
    * защита от null в строке поиска
    * если строка поиска пуста:
        ids = 0, 1, 2, 3...
    * если нет:
        * создание нового массива
        * добавление только соответствующих элементов
    * обновление листа
     */
    private void WordsUpdata () {
        try {
            String textScreen = (screen.getText() == null) ? "" : screen.getText().toString();
            if (textScreen.equals("")) {
                ids = new ArrayList<>();
                for (int i = 0; i < words.size(); i++) {
                    ids.add(i);
                }
                list.setAdapter(new WordsAdapter(WordsActivity.this, new ArrayList<>(words)));
            }
            else {
                ArrayList<Word> wordsSelect = new ArrayList<>();
                ids = new ArrayList<>();
                for (int i = 0; i < words.size(); i++) {
                    if ((words.get(i).WORD.contains(textScreen)) || (words.get(i).TRANSLATION_SMALL.contains(textScreen)) || (words.get(i).TRANSLATION.contains(textScreen))) {
                        wordsSelect.add(words.get(i));
                        ids.add(i);
                    }
                }
                list.setAdapter(new WordsAdapter(WordsActivity.this, wordsSelect));
            }
        }
        catch (Exception e) {
            MainActivity.ErrorLog(WordsActivity.this, e, 260);
        }
    }
}
