package com.redstar.zolotov.languagers.Activitys;

/*
Активность добавления/изменения слова
№3
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.redstar.zolotov.languagers.R;

public class SetWordActivity extends AppCompatActivity {
    private EditText word, smoleTranslation, translation;
    private Spinner spinner;
    private int id = 0, acquire;
    private boolean isNew;

    /*
    ---При создании №1---
    * получение типа (true - новые слова, false - изученные)
    * если есть входящие данные:
        * вывести слово и прерводы на экран
        * запомнить id
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setwords);

        word = (EditText) findViewById(R.id.word);
        smoleTranslation = (EditText) findViewById(R.id.small_translation);
        translation = (EditText) findViewById(R.id.translation);
        spinner = (Spinner) findViewById(R.id.spinner);

        try {
            isNew = getIntent().getBooleanExtra("type", false);
            if (!isNew) {
                ((LinearLayout) findViewById(R.id.layout)).removeView(spinner);
            }

            if (getIntent().getStringExtra("word") != null) {
                word.setText(getIntent().getStringExtra("word"));
                smoleTranslation.setText(getIntent().getStringExtra("smoleTranslation"));
                String trans = getIntent().getStringExtra("translation");
                translation.setText(trans.equals("none") ? "" : trans);

                if (isNew) {
                    spinner.setSelection(getIntent().getIntExtra("acquire", 0));
                }
                else {
                    acquire = getIntent().getIntExtra("acquire", 0);
                }

                id = getIntent().getIntExtra("id", 0);
            }
        }
        catch (Exception e) {
            MainActivity.ErrorLog(SetWordActivity.this, e, 310);
        }
    }

    /*
    ---Обработка нажатий на клавиши №2---
    * получение данных
    * проверка корректности
    * вывод данных
     */
    public void PressKey (View v) {
        try {
            String wor = word.getText().toString();
            String st = smoleTranslation.getText().toString();
            String trans = translation.getText().toString();

            if (((wor != null) && (!wor.isEmpty())) && ((st != null) && (!st.isEmpty()))) {
                Intent intent = new Intent();

                intent.putExtra("word", wor);
                intent.putExtra("smoleTranslation", st);
                intent.putExtra("translation", ((trans == null) || (trans.isEmpty())) ? "none" : trans);

                if (isNew) {
                    intent.putExtra("acquire", spinner.getSelectedItemPosition());
                }
                else {
                    intent.putExtra("acquire", acquire);
                }

                intent.putExtra("id", id);

                setResult(RESULT_OK, intent);
                finish();
            }
        }
        catch (Exception e) {
            MainActivity.ErrorLog(SetWordActivity.this, e, 210);
        }
    }
}
