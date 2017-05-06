package com.redstar.zolotov.languagers.Adapters;

/*
Адаптер для построения списка слов
№10
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.redstar.zolotov.languagers.Activitys.LearnWordsActivity;
import com.redstar.zolotov.languagers.Activitys.NewLearnActivity;
import com.redstar.zolotov.languagers.Activitys.WordsActivity;
import com.redstar.zolotov.languagers.Objects.Word;
import com.redstar.zolotov.languagers.R;

import java.util.ArrayList;

public class WordsAdapter extends BaseAdapter {
    private ArrayList<String> words, translation;
    private LayoutInflater layout;
    private Activity activity;
    private int type;

    /*
    ---Конструктор для просмотра слов №1---
    * Перевод Word в String
     */
    public WordsAdapter (WordsActivity activity, ArrayList<Word> wordObjects) {
        ArrayList<String> words = new ArrayList<>(), translation = new ArrayList<>();

        for (Word word : wordObjects) {
            words.add(word.WORD);
            translation.add(word.TRANSLATION_SMALL);
        }

        this.words = words;
        this.translation = translation;
        this.activity = activity;
        type = 0;
        layout = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*
    ---Конструктор для изучения новых слов №2---
     */
    public WordsAdapter (NewLearnActivity activity, ArrayList<String> words, ArrayList<String> translation) {
        this.words = words;
        this.translation = translation;
        this.activity = activity;
        type = 1;
        layout = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*
    ---Конструктор для повторения слов №3---
    * Перевод Word в String
     */
    public WordsAdapter (LearnWordsActivity activity, ArrayList<Word> wordObjects) {
        ArrayList<String> words = new ArrayList<>(), translation = new ArrayList<>();

        for (Word word : wordObjects) {
            words.add(word.WORD);
            translation.add(word.TRANSLATION_SMALL);
        }
        this.words = words;
        this.translation = translation;
        this.activity = activity;
        type = 2;
        layout = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /*
    ---Генерация View №4---
    * генерация view
    * установка текста
    * установка действия при нажатии
     */
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layout.inflate(R.layout.item_word, viewGroup, false);
        }

        ((TextView) view.findViewById(R.id.item_text)).setText(words.get(i) + " - " + translation.get(i));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type) {
                    case 0:
                        ((WordsActivity) activity).WordSelected(i);
                        break;

                    case 1:
                        ((NewLearnActivity) activity).WordSelected(i);
                        break;

                    case 2:
                        ((LearnWordsActivity) activity).WordSelected(i);
                        break;
                }
            }
        });

        return view;
    }
}
