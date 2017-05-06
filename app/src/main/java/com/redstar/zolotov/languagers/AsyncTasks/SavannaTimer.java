package com.redstar.zolotov.languagers.AsyncTasks;

/*
Таймер мниигры саванна
№12
 */

import android.os.AsyncTask;
import android.widget.TextView;
import com.redstar.zolotov.languagers.Objects.Savanna;
import com.redstar.zolotov.languagers.Objects.WordsController;

public class SavannaTimer extends AsyncTask<Boolean, String, Void> {
    private Savanna savanna;
    private TextView timer;
    private boolean isStop = false;

    public SavannaTimer(Savanna savanna, TextView timer) {
        this.savanna = savanna;
        this.timer = timer;
    }

    /*
    ---Фоновый процесс (сам таймер) №1---
    * true - обычный таймер
    * false - стартовый таймер
     */
    @Override
    protected Void doInBackground(Boolean... booleans) {
        try {
            if (booleans[0]) {
                Thread.sleep(WordsController.SAVANNA_DELAY);
                publishProgress();
            }
            else {
                publishProgress("3");
                Thread.sleep(1000);
                publishProgress("2");
                Thread.sleep(1000);
                publishProgress("1");
                Thread.sleep(1000);
                publishProgress("0");
                Thread.sleep(1000);
                publishProgress("");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    ---Остановка таймера №2---
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        isStop = true;
    }

    /*
    ---Обновление интерфейса №3---
    * проверка на активность таймера
    * если нет значений (обычный таймер) - запуск конец таймера
    * если "" (конец обратного отсчета при старте) - скрыть текст аймера, запуск следующего слова
    * если число (обратный отсчет) - вывод оставшегося времени на экран
     */
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (isStop) { return; }

        if (values.length == 0) {
            savanna.TimeEnd();
        }
        else {
            if (values[0].equals("")) {
                timer.setText(values[0]);
                savanna.gameIsStart = true;
                savanna.DownWord();
            }
            else {
                timer.setTextSize(50);
                timer.setText(values[0]);
            }
        }
    }
}
