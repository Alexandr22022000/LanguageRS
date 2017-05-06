package com.redstar.zolotov.languagers.Activitys;
/*
Активность слов на изучении
№5
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.redstar.zolotov.languagers.Adapters.WordsAdapter;
import com.redstar.zolotov.languagers.Objects.Word;
import com.redstar.zolotov.languagers.Objects.WordsController;
import com.redstar.zolotov.languagers.R;

import java.util.ArrayList;

public class LearnWordsActivity extends AppCompatActivity {
    private ListView list;
    private ArrayList<Integer> idsInAdapter;
    private EditText screen;

    /*
    ---При создании №1---
    * добавления действия при изменении текста поиска
    * перенос слов с истекшим сроком изучения
    * получение id
    * сортировка id по убыванию (для удаления)
    * перенос новых слов
    * запуск обновления
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);

        list = (ListView) findViewById(R.id.list_view);
        screen = (EditText) findViewById(R.id.screen);

        screen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Updata();
            }
        });

        try {
            if (getIntent().getIntegerArrayListExtra("ids") != null) {
                for (int i = 0; i < WordsController.LEARN_WORDS.size(); i++) {
                    WordsController.LEARN_WORDS.get(i).TimerWord();

                    if (WordsController.LEARN_WORDS.get(i).ACQUIRE > WordsController.DAY_OF_WORDS) {
                        WordsController.KNOW_WORDS.add(WordsController.LEARN_WORDS.get(i).MigrationWord(0));
                        WordsController.LEARN_WORDS.remove(i);
                        i--;
                    }
                }

                ArrayList<Integer> ids = getIntent().getIntegerArrayListExtra("ids");

                boolean ceek = true;
                int bufer;

                while (ceek) {
                    ceek = false;
                    for (int i = 0; i < ids.size() - 1; i++) {
                        if (ids.get(i) < ids.get(i + 1)) {
                            bufer = ids.get(i);
                            ids.set(i, ids.get(i + 1));
                            ids.set(i + 1, bufer);
                            ceek = true;
                        }
                    }
                }

                for (Integer id : ids) {
                    WordsController.LEARN_WORDS.add(WordsController.NEW_WORDS.get(id).MigrationWord(0));
                    WordsController.NEW_WORDS.remove((int) id);
                }

                WordsController.OutputWords(LearnWordsActivity.this);
                WordsController.SotrWords(LearnWordsActivity.this);
            }

            Updata();
        }
        catch (Exception e) {
            MainActivity.ErrorLog(LearnWordsActivity.this, e, 510);
        }
    }

    /*
    ---Получение ответа на запрос №5---
    * проерка успешности запроса
    * добавление слова
    * запуск обновления
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        int id = data.getIntExtra("id", 0);

        WordsController.LEARN_WORDS.set(idsInAdapter.get(id), new Word(
                data.getStringExtra("word"),
                data.getStringExtra("smoleTranslation"),
                data.getStringExtra("translation"),
                data.getIntExtra("acquire", 0)));

        WordsController.OutputWords(LearnWordsActivity.this);
        WordsController.SotrWords(LearnWordsActivity.this);
        Updata();
    }

    /*
    ---Обработка выбора слова №2---
    * открытие окна: изменить, знаю, забыл, удалить
    * 0 - запрос на изменение слова (1, false)
    * 1 - перенос в изученные запуск удаление + вы уверены?
    * 2 - перенос в новые, удаление + вы уверены?
    * 3 - удаление + вы уверены?
     */
    public void WordSelected (final int id) {
        try {
            if (idsInAdapter.get(id).equals(-1)) {
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(LearnWordsActivity.this);
            builder
                    .setItems(new String[]{"Изменить", "Знаю", "Забыл", "Удалить"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:
                                    Intent intent = new Intent(LearnWordsActivity.this, SetWordActivity.class);

                                    intent.putExtra("word", WordsController.LEARN_WORDS.get(idsInAdapter.get(id)).WORD);
                                    intent.putExtra("smoleTranslation", WordsController.LEARN_WORDS.get(idsInAdapter.get(id)).TRANSLATION_SMALL);
                                    intent.putExtra("translation", WordsController.LEARN_WORDS.get(idsInAdapter.get(id)).TRANSLATION);
                                    intent.putExtra("acquire", WordsController.LEARN_WORDS.get(idsInAdapter.get(id)).ACQUIRE);
                                    intent.putExtra("id", id);
                                    intent.putExtra("type", false);

                                    startActivityForResult(intent, 1);
                                    break;

                                case 1:
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(LearnWordsActivity.this);
                                    builder1
                                            .setTitle("Вы уверены?")
                                            .setNegativeButton("Нет", null)
                                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    try {
                                                        WordsController.KNOW_WORDS.add(WordsController.LEARN_WORDS.get(idsInAdapter.get(id)).MigrationWord(0));
                                                        WordsController.LEARN_WORDS.remove((int) idsInAdapter.get(id));
                                                        WordsController.OutputWords(LearnWordsActivity.this);
                                                        WordsController.SotrWords(LearnWordsActivity.this);
                                                        Updata();
                                                    }
                                                    catch (Exception e) {
                                                        MainActivity.ErrorLog(LearnWordsActivity.this, e, 521);
                                                    }
                                                }
                                            })
                                            .setCancelable(true);
                                    builder1.create().show();
                                    break;

                                case 2:
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(LearnWordsActivity.this);
                                    builder2
                                            .setTitle("Вы уверены?")
                                            .setNegativeButton("Нет", null)
                                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    try {
                                                        WordsController.NEW_WORDS.add(WordsController.LEARN_WORDS.get(idsInAdapter.get(id)).MigrationWord(1));
                                                        WordsController.LEARN_WORDS.remove((int) idsInAdapter.get(id));
                                                        WordsController.OutputWords(LearnWordsActivity.this);
                                                        WordsController.SotrWords(LearnWordsActivity.this);

                                                        Updata();
                                                    }
                                                    catch (Exception e) {
                                                        MainActivity.ErrorLog(LearnWordsActivity.this, e, 522);
                                                    }
                                                }
                                            })
                                            .setCancelable(true);
                                    builder2.create().show();
                                    break;

                                case 3:
                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(LearnWordsActivity.this);
                                    builder3
                                            .setTitle("Вы уверены?")
                                            .setNegativeButton("Нет", null)
                                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    WordsController.LEARN_WORDS.remove((int) idsInAdapter.get(id));
                                                    WordsController.OutputWords(LearnWordsActivity.this);
                                                    WordsController.SotrWords(LearnWordsActivity.this);
                                                    Updata();
                                                }
                                            })
                                            .setCancelable(true);
                                    builder3.create().show();
                                    break;
                            }
                        }
                    })
                    .setCancelable(true);
            builder.create().show();
        }
        catch (Exception e) {
            MainActivity.ErrorLog(LearnWordsActivity.this, e, 520);
        }
    }

    /*
    ---Обновление листа №4---
    * выборка новых слов
    * выборка остальных слов
    * если сторока поиска не пуста, убрать не соотверствующие слова
    * обновление листа
     */
    private void Updata () {
        try {
            ArrayList<Word> words = new ArrayList<>();
            idsInAdapter = new ArrayList<>();

            for (int i = 0; i < WordsController.LEARN_WORDS.size(); i++) {
                if (WordsController.LEARN_WORDS.get(i).ACQUIRE == 0) {
                    words.add(WordsController.LEARN_WORDS.get(i));
                    idsInAdapter.add(i);
                }
            }

            words.add(new Word("-----", "-----", "", -1));
            idsInAdapter.add(-1);

            for (int i = 0; i < WordsController.LEARN_WORDS.size(); i++) {
                if (WordsController.LEARN_WORDS.get(i).ACQUIRE != 0) {
                    words.add(WordsController.LEARN_WORDS.get(i));
                    idsInAdapter.add(i);
                }
            }

            String scr = (screen.getText() == null) ? "" : screen.getText().toString();
            if (!scr.equals("")) {
                for (int i = 0; i <words.size(); i++) {
                    if ((!words.get(i).WORD.contains(scr)) && (!words.get(i).TRANSLATION_SMALL.contains(scr)) && (!words.get(i).TRANSLATION.contains(scr)) && (words.get(i).ACQUIRE != -1)) {
                        words.remove(i);
                        idsInAdapter.remove(i);
                        i--;
                    }
                }
            }

            list.setAdapter(new WordsAdapter(LearnWordsActivity.this, words));
        }
        catch (Exception e) {
            MainActivity.ErrorLog(LearnWordsActivity.this, e, 540);
        }
    }
}
