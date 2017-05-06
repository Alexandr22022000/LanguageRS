package com.redstar.zolotov.languagers.Activitys;

/*
Активность изучения новых слов
№ 4
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.redstar.zolotov.languagers.Adapters.WordsAdapter;
import com.redstar.zolotov.languagers.Objects.Word;
import com.redstar.zolotov.languagers.Objects.WordsController;
import com.redstar.zolotov.languagers.R;

import java.util.ArrayList;
import java.util.Random;

public class NewLearnActivity extends AppCompatActivity {
    private ListView list;
    private ArrayList<String> words, translation;
    private ArrayList<Integer> idsArray;

    /*
    ---При создании №1---
    * получение очень приорететных слов
    * выборка из очень приорететных слов
    * получение приорететных слов
    * выборка приорететных слов
    * получение не приорететных слов
    * выборка не приорететных слов
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_newlearn);

        list = (ListView) findViewById(R.id.list_item);

        try {
            idsArray = new ArrayList<>();
            ArrayList<Integer> ids = new ArrayList<>();
            Random random = new Random();
            words = new ArrayList<>();
            translation = new ArrayList<>();

            for (int i = 0; i < WordsController.NEW_WORDS.size(); i++) {
                if (WordsController.NEW_WORDS.get(i).ACQUIRE == 2) {
                    ids.add(i);
                }
            }

            for (int i = 0; (i < WordsController.WORDS_OF_DAY) && (ids.size() != 0); i++) {
                int rand = (ids.size() == 1) ? 0 : random.nextInt( ids.size() - 1);
                idsArray.add(ids.get(rand));

                words.add(WordsController.NEW_WORDS.get(ids.get(rand)).WORD);
                translation.add(WordsController.NEW_WORDS.get(ids.get(rand)).TRANSLATION_SMALL);

                ids.remove(rand);
            }

            if (WordsController.WORDS_OF_DAY != words.size()) {
                //int secondCount = ((count - words.size() - (count / 2)) < 0) ? (count - words.size()) : (count / 2);        //SMART
                int secondCount = WordsController.WORDS_OF_DAY - words.size();

                ids = new ArrayList<>();
                for (int i = 0; i < WordsController.NEW_WORDS.size(); i++) {
                    if (WordsController.NEW_WORDS.get(i).ACQUIRE == 1) {
                        ids.add(i);
                    }
                }

                for (int i = 0; (i < secondCount) && (ids.size() != 0); i++) {
                    int rand = (ids.size() == 1) ? 0 : random.nextInt( ids.size() - 1);
                    idsArray.add(ids.get(rand));

                    words.add(WordsController.NEW_WORDS.get(ids.get(rand)).WORD);
                    translation.add(WordsController.NEW_WORDS.get(ids.get(rand)).TRANSLATION_SMALL);

                    ids.remove(rand);
                }

                if (WordsController.WORDS_OF_DAY != words.size()) {
                    secondCount = WordsController.WORDS_OF_DAY - words.size();

                    ids = new ArrayList<>();
                    for (int i = 0; i < WordsController.NEW_WORDS.size(); i++) {
                        if (WordsController.NEW_WORDS.get(i).ACQUIRE == 0) {
                            ids.add(i);
                        }
                    }

                    for (int i = 0; (i < secondCount) && (ids.size() != 0); i++) {
                        int rand = (ids.size() == 1) ? 0 : random.nextInt( ids.size() - 1);
                        idsArray.add(ids.get(rand));

                        words.add(WordsController.NEW_WORDS.get(ids.get(rand)).WORD);
                        translation.add(WordsController.NEW_WORDS.get(ids.get(rand)).TRANSLATION_SMALL);

                        ids.remove(rand);
                    }
                }
            }

            list.setAdapter(new WordsAdapter(NewLearnActivity.this, words, translation));
        }
        catch (Exception e) {
            MainActivity.ErrorLog(NewLearnActivity.this, e, 410);
        }
    }

    /*
    ---Ответ на запрос №6---
    * проверка успешности запроса
    * изсенение слова в массиве
    * изменение слова в листе
    * обновление листа
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        int id = data.getIntExtra("id", 0);
        String word = data.getStringExtra("word"), translationWord = data.getStringExtra("translation");

        WordsController.NEW_WORDS.set(idsArray.get(id), new Word(
                word,
                data.getStringExtra("smoleTranslation"),
                translationWord,
                data.getIntExtra("acquire", 0)));
        WordsController.OutputWords(NewLearnActivity.this);
        WordsController.SotrWords(NewLearnActivity.this);

        words.set(id, word);
        translation.set(id, translationWord);
        list.setAdapter(new WordsAdapter(NewLearnActivity.this, words, translation));
    }

    /*
    ---Обработка нажатия на клавиши №2---
    * добавление новых слов на изучение + вы уверены?
     */
    public void PressKey (View v) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(NewLearnActivity.this);
        builder1
                .setTitle("Вы уверены? Это действие нельзя отменить!")
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(NewLearnActivity.this, LearnWordsActivity.class);
                        intent.putExtra("ids", idsArray);
                        startActivity(intent);
                        NewLearnActivity.this.finish();
                    }
                })
                .setCancelable(true);

        builder1.create().show();
    }

    /*
    ---Обработка выбора слова №3---
    * вывод окна: другое слово, знаю, удалить
    * 0 - вызов перерендора слова
    * 1 - запрос на изменение слова (1 false)
    * 2 - копирование слова + вызов перерендора слов + вы уверены
    * 3 - вызов удаление слов + вы уверены
     */
    public void WordSelected (final int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(NewLearnActivity.this);

        builder
                .setItems(new String[]{"Другое слово", "Изменить", "Знаю", "Удалить"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                GenerateNewWord(id, true);
                                break;

                            case 1:
                                Intent intent = new Intent(NewLearnActivity.this, SetWordActivity.class);

                                intent.putExtra("word", WordsController.NEW_WORDS.get(idsArray.get(id)).WORD);
                                intent.putExtra("smoleTranslation", WordsController.NEW_WORDS.get(idsArray.get(id)).TRANSLATION_SMALL);
                                intent.putExtra("translation", WordsController.NEW_WORDS.get(idsArray.get(id)).TRANSLATION);
                                intent.putExtra("acquire", WordsController.NEW_WORDS.get(idsArray.get(id)).ACQUIRE);
                                intent.putExtra("id", id);
                                intent.putExtra("type", false);

                                startActivityForResult(intent, 1);
                                break;

                            case 2:
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(NewLearnActivity.this);
                            builder1
                                    .setTitle("Вы уверены?")
                                    .setNegativeButton("Нет", null)
                                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            WordsController.KNOW_WORDS.add(WordsController.NEW_WORDS.get(idsArray.get(id)).MigrationWord(0));

                                            RemoveItem(id);
                                        }
                                    })
                                    .setCancelable(true);

                            builder1.create().show();
                            break;

                            case 3:
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(NewLearnActivity.this);
                                builder2
                                        .setTitle("Вы уверены?")
                                        .setNegativeButton("Нет", null)
                                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                RemoveItem(id);
                                            }
                                        })
                                        .setCancelable(true);

                                builder2.create().show();
                                break;
                        }
                    }
                })
                .setCancelable(true);

        builder.create().show();
    }

    /*
    ---Перерендр слова №4---
    * если нет слов:
        * если замена: "нет слов" иначе удаление
    * иначе:
        * не использаванных новых слов
        * выбор из них рандомного
        * запись его в массив
     */
    private void GenerateNewWord (int id, boolean isReG) {
        try {
            if (words.size() == WordsController.NEW_WORDS.size() + (isReG ? 0 : 1)) {
                if (isReG) {
                    Toast.makeText(this, "Нет новых слов", Toast.LENGTH_SHORT).show();
                    return;
                }

                words.remove(id);
                translation.remove(id);
                idsArray.remove(id);
            }
            else {
                ArrayList<Integer> ids = new ArrayList<>();

                for (int i = 0; i < WordsController.NEW_WORDS.size(); i++) {
                    boolean isCeek = true;
                    for (Integer id2 : idsArray) {
                        if (i == id2) {
                            isCeek = false;
                        }
                    }

                    if (isCeek) {
                        ids.add(i);
                    }
                }
                int random = (ids.size() == 1) ? 0 : new Random().nextInt(ids.size() - 1);

                words.set(id, WordsController.NEW_WORDS.get(ids.get(random)).WORD);
                translation.set(id, WordsController.NEW_WORDS.get(ids.get(random)).TRANSLATION_SMALL);
                idsArray.set(id, ids.get(random));
            }

            list.setAdapter(new WordsAdapter(NewLearnActivity.this, words, translation));
        }
        catch (Exception e) {
            MainActivity.ErrorLog(NewLearnActivity.this, e, 440);
        }
    }

    /*
    ---Удаление слова №5---
    * удаление элемента
    * сдвиг id ссылок
     */
    private void RemoveItem (int id) {
        try {
            WordsController.NEW_WORDS.remove((int) idsArray.get(id));

            for (int ii = 0; ii < idsArray.size(); ii++) {
                if (idsArray.get(ii) > idsArray.get(id)) {
                    idsArray.set(ii, idsArray.get(ii) - 1);
                }
            }

            WordsController.OutputWords(NewLearnActivity.this);

            GenerateNewWord(id, false);
        }
        catch (Exception e) {
            MainActivity.ErrorLog(NewLearnActivity.this, e, 450);
        }
    }
}
