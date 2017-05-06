package com.redstar.zolotov.languagers.Objects;

/*
Контроллер слов
№8
 */

import android.content.Context;
import com.redstar.zolotov.languagers.Activitys.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class WordsController {
    static public ArrayList<Word> NEW_WORDS;
    static public ArrayList<Word> LEARN_WORDS;
    static public ArrayList<Word> KNOW_WORDS;
    static public int WORDS_OF_DAY, DAY_OF_WORDS, SAVANNA_WORDS_COUNT, SAVANNA_HP, SAVANNA_DELAY;


    /*
    ---Чтение слов из файла Main №1---
    * получение настроик
    * получение новых слов
    * плоучение слов на изучении
    * получение изученных слов
    * запуск сортировки
     */
    public static void InputWords (Context context) {
        try {
            BufferedReader R = new BufferedReader(new InputStreamReader(context.openFileInput("EnglishWords")));
            String settings = R.readLine();
            int[] settingsArray = new int[5];

            int start = 0, end, i = 0;
            while (start != -1) {
                end = settings.indexOf(";", start + 1);
                settingsArray[i] = Integer.parseInt(settings.substring(start + 1, (end == -1) ? settings.length() : end));
                i++;
                start = end;
            }

            WORDS_OF_DAY = settingsArray[0];
            DAY_OF_WORDS = settingsArray[1];
            SAVANNA_WORDS_COUNT = settingsArray[2];
            SAVANNA_HP = settingsArray[3];
            SAVANNA_DELAY = settingsArray[4];

            NEW_WORDS = new ArrayList<>();

            String s = R.readLine();
            while (!s.equals("**********")) {
                NEW_WORDS.add(new Word(
                        s.substring(0, s.indexOf("#", 0)),
                        s.substring(s.indexOf("#", 0) + 1, s.indexOf("@", 0)),
                        s.substring(s.indexOf("@", 0) + 1, s.indexOf("|", 0)),
                        Integer.parseInt(s.substring(s.indexOf("|", 0) + 1, s.length()))));
                s = R.readLine();
            }

            LEARN_WORDS = new ArrayList<>();

            s = R.readLine();
            while (!s.equals("**********")) {
                LEARN_WORDS.add(new Word(
                        s.substring(0, s.indexOf("#", 0)),
                        s.substring(s.indexOf("#", 0) + 1, s.indexOf("@", 0)),
                        s.substring(s.indexOf("@", 0) + 1, s.indexOf("|", 0)),
                        Integer.parseInt(s.substring(s.indexOf("|", 0) + 1, s.length()))));
                s = R.readLine();
            }

            KNOW_WORDS = new ArrayList<>();

            s = R.readLine();
            s = (s == null) ? "" : s;
            while (!s.isEmpty()) {
                KNOW_WORDS.add(new Word(
                        s.substring(0, s.indexOf("#", 0)),
                        s.substring(s.indexOf("#", 0) + 1, s.indexOf("@", 0)),
                        s.substring(s.indexOf("@", 0) + 1, s.indexOf("|", 0)),
                        Integer.parseInt(s.substring(s.indexOf("|", 0) + 1, s.length()))));
                s = R.readLine();
                s = (s == null) ? "" : s;
            }
            R.close();
        }
        catch (Exception e) {
            MainActivity.ErrorLog(context, e, 810);
        }

        SotrWords(context);
    }

    /*
    ---Запись слов в файл Main №2---
    * подготовка настроик
    * подготовка новых слов
    * подготовка слов на изучении
    * подготовка изученных слов
    * запись слов
     */
    public static void OutputWords (Context context) {
        try {
            String settings = ";" + WORDS_OF_DAY + ";" + DAY_OF_WORDS + ";" + SAVANNA_WORDS_COUNT + ";" + SAVANNA_HP + ";" + SAVANNA_DELAY;

            StringBuilder builder = new StringBuilder();
            builder.append(settings);
            builder.append("\n");

            for (Word NEW_WORD : NEW_WORDS) {
                builder.append(NEW_WORD.WORD);
                builder.append("#");
                builder.append(NEW_WORD.TRANSLATION_SMALL);
                builder.append("@");
                builder.append(NEW_WORD.TRANSLATION);
                builder.append("|");
                builder.append(NEW_WORD.ACQUIRE);
                builder.append("\n");
            }

            builder.append("**********");
            builder.append("\n");

            for (Word LEARN_WORD : LEARN_WORDS) {
                builder.append(LEARN_WORD.WORD);
                builder.append("#");
                builder.append(LEARN_WORD.TRANSLATION_SMALL);
                builder.append("@");
                builder.append(LEARN_WORD.TRANSLATION_SMALL);
                builder.append("|");
                builder.append(LEARN_WORD.ACQUIRE);
                builder.append("\n");
            }

            builder.append("**********");
            builder.append("\n");

            for (Word KNOW_WORD : KNOW_WORDS) {
                builder.append(KNOW_WORD.WORD);
                builder.append("#");
                builder.append(KNOW_WORD.TRANSLATION_SMALL);
                builder.append("@");
                builder.append(KNOW_WORD.TRANSLATION);
                builder.append("|");
                builder.append(KNOW_WORD.ACQUIRE);
                builder.append("\n");
            }

            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput("EnglishWords", Context.MODE_PRIVATE));
            osw.write(builder.toString());
            osw.close();
        }
        catch (Exception e) {
            MainActivity.ErrorLog(context, e, 820);
        }
    }

    /*
    ---Получение текста файла Main №3---
     */
    public static String GetMainText (Context context) {
        try {
            BufferedReader R = new BufferedReader(new InputStreamReader(context.openFileInput("EnglishWords")));
            StringBuilder stringBuilder = new StringBuilder();
            String bufer = R.readLine();
            while ((bufer != null) && (!bufer.equals(""))) {
                stringBuilder
                        .append(bufer)
                        .append("\n");
                bufer = R.readLine();
            }
            return stringBuilder.toString();
        }
        catch (Exception e) {
            MainActivity.ErrorLog(context, e, 830);
        }
        return "Exception";
    }

    /*
    ---Установка текста файла Main №4---
     */
    public static void SetMainText (String text, Context context) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput("EnglishWords", Context.MODE_PRIVATE));
            osw.write(text);
            osw.close();
        }
        catch (Exception e) {
            MainActivity.ErrorLog(context, e, 840);
        }
    }

    /*
    ---Создание файла Main №5---
     */
    public static void CreateWordsFile (Context context) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput("EnglishWords", Context.MODE_PRIVATE));
            osw.write(";5;3;5;3;5000" + "\n" + CreateStandardWords() + "**********" + "\n" + "**********");
            osw.close();
        }
        catch (Exception e) {
            MainActivity.ErrorLog(context, e, 850);
        }
    }

    /*
    ---Сортировка слов №6---
    * запись слов в Map
    * сортировка Map по ключу
    * запись отсартированных значений в ArrayList
    * Все повторяется 3 раза для каждого ArrayList
     */
    public static void SotrWords (Context context) {
        try {
            Map<String, Word> map = new TreeMap<>();
            for (Word word : NEW_WORDS) {
                map.put(word.WORD, word);
            }

            NEW_WORDS = new ArrayList<>();
            for (Map.Entry<String, Word> entry : map.entrySet()) {
                NEW_WORDS.add(entry.getValue());
            }

            map = new TreeMap<>();
            for (Word word : LEARN_WORDS) {
                map.put(word.WORD, word);
            }

            LEARN_WORDS = new ArrayList<>();
            for (Map.Entry<String, Word> entry : map.entrySet()) {
                LEARN_WORDS.add(entry.getValue());
            }

            map = new TreeMap<>();
            for (Word word : KNOW_WORDS) {
                map.put(word.WORD, word);
            }

            KNOW_WORDS = new ArrayList<>();
            for (Map.Entry<String, Word> entry : map.entrySet()) {
                KNOW_WORDS.add(entry.getValue());
            }
        }
        catch (Exception e) {
            MainActivity.ErrorLog(context, e, 860);
        }
    }

    /*
    ---Вывод начальных слов (МНОГО КОДА!) №7---
     */
    private static String CreateStandardWords() {
        StringBuilder builder = new StringBuilder();

        builder.append("angle#угол@угол, поворачивать|0\n");
        builder.append("ant#муравей@муравей|0\n");
        builder.append("apple#яблоко@яблоко|0\n");
        builder.append("arch#арка@арка, дуга, выгибать|0\n");
        builder.append("arm#рука@рука, вооружать|0\n");
        builder.append("army#армия@армия|0\n");
        builder.append("bag#сумка@сумка|0\n");
        builder.append("ball#мяч@мяч|0\n");
        builder.append("bank#банк@банк|0\n");
        builder.append("basin#бассейн@бассейн|0\n");
        builder.append("basket#корзина@корзина|0\n");
        builder.append("bath#ванна@ванна, купаться|0\n");
        builder.append("bed#кровать@кровать|0\n");
        builder.append("bee#пчела@пчела|0\n");
        builder.append("bell#колокольчик@колокольчик|0\n");
        builder.append("berry#ягода@ягода|0\n");
        builder.append("bird#птица@птица|0\n");
        builder.append("blade#лезвие@лезвие|0\n");
        builder.append("board#доска@доска|0\n");
        builder.append("boat#лодка@лодка, судно|0\n");
        builder.append("bone#кость@кость|0\n");
        builder.append("book#книга@книга|0\n");
        builder.append("boot#ботинок@ботинок, загружать|0\n");
        builder.append("bottle#бутылка@бутылка|0\n");
        builder.append("box#коробка@коробка|0\n");
        builder.append("boy#мальчик@мальчик|0\n");
        builder.append("brain#мозг@мозг|0\n");
        builder.append("brake#тормоз@тормоз, тормозить|0\n");
        builder.append("branch#ветвь@ветвь, отделение|0\n");
        builder.append("brick#кирпич@кирпич|0\n");
        builder.append("bridge#мост@мост|0\n");
        builder.append("brush#щётка@щётка, кисть|0\n");
        builder.append("bucket#ведро@ведро, черпать|0\n");
        builder.append("bulb#луковица@луковица, выпирать|0\n");
        builder.append("button#кнопка@кнопка, застёгивать|0\n");
        builder.append("baby#ребёнок@ребёнок, младенец|0\n");
        builder.append("cake#пирог@пирог|0\n");
        builder.append("camera#камера@камера|0\n");
        builder.append("card#карта@карта, чесать|0\n");
        builder.append("cart#везти@везти|0\n");
        builder.append("carriage#вагон@вагон|0\n");
        builder.append("cat#кот@кот|0\n");
        builder.append("chain#цепь@цепь, сеть|0\n");
        builder.append("cheese#сыр@сыр|0\n");
        builder.append("chest#грудь@грудь, сундук|0\n");
        builder.append("chin#подбородок@подбородок|0\n");
        builder.append("church#церковь@церковь|0\n");
        builder.append("circle#круг@круг|0\n");
        builder.append("clock#часы@часы|0\n");
        builder.append("cloud#облако@облако|0\n");
        builder.append("coat#пальто@пальто, покрывать|0\n");
        builder.append("collar#воротник@воротник, хватать|0\n");
        builder.append("comb#расчёсывать@расчёсывать|0\n");
        builder.append("cord#шнур@шнур, связывать|0\n");
        builder.append("cow#корова@корова|0\n");
        builder.append("cup#чашка@чашка|0\n");
        builder.append("curtain#занавес@занавес, штора, занавешивать|0\n");
        builder.append("cushion#подушка@подушка, смягчать|0\n");
        builder.append("dog#собака@собака|0\n");
        builder.append("door#дверь@дверь|0\n");
        builder.append("drain#утечка@утечка, истощать|0\n");
        builder.append("drawer#ящик@ящик|0\n");
        builder.append("dress#платье@платье, одевать|0\n");
        builder.append("drop#капля@капля, опускать|0\n");
        builder.append("ear#ухо@ухо|0\n");
        builder.append("egg#яйцо@яйцо|0\n");
        builder.append("engine#двигатель@двигатель|0\n");
        builder.append("eye#глаз@глаз	|0\n");
        builder.append("face#лицо@лицо|0\n");
        builder.append("farm#ферма@ферма|0\n");
        builder.append("feather#перо@перо, украшать|0\n");
        builder.append("finger#палец@палец|0\n");
        builder.append("fish#рыба@рыба|0\n");
        builder.append("flag#флаг@флаг, сигнализировать|0\n");
        builder.append("floor#пол@пол, этаж, дно|0\n");
        builder.append("fly#муха@муха, лететь|0\n");
        builder.append("foot#нога@нога|0\n");
        builder.append("fork#вилка@вилка|0\n");
        builder.append("fowl#домашняя@домашняя птица|0\n");
        builder.append("frame#структура@структура, рамка, создавать|0\n");
        builder.append("garden#сад@сад|0\n");
        builder.append("girl#девочка@девочка|0\n");
        builder.append("glove#перчатка@перчатка|0\n");
        builder.append("goat#коза@коза|0\n");
        builder.append("gun#оружие@оружие|0\n");
        builder.append("hair#волосы@волосы|0\n");
        builder.append("hammer#молоток@молоток|0\n");
        builder.append("hand#рука@рука|0\n");
        builder.append("hat#шляпа@шляпа|0\n");
        builder.append("head#голова@голова|0\n");
        builder.append("heart#сердце@сердце|0\n");
        builder.append("hook#крюк@крюк, вербовать|0\n");
        builder.append("horn#рожок@рожок|0\n");
        builder.append("horse#лошадь@лошадь|0\n");
        builder.append("hospital#больница@больница|0\n");
        builder.append("house#дом@дом|0\n");
        builder.append("island#остров@остров|0\n");
        builder.append("jewel#драгоценный@драгоценный камень|0\n");
        builder.append("kettle#чайник@чайник|0\n");
        builder.append("key#ключ@ключ|0\n");
        builder.append("knee#колено@колено|0\n");
        builder.append("knife#нож@нож|0\n");
        builder.append("knot#узел@узел|0\n");
        builder.append("leaf#лист@лист, покрывать листвой|0\n");
        builder.append("leg#нога@нога|0\n");
        builder.append("library#библиотека@библиотека|0\n");
        builder.append("line#линия@линия, очередь, выравнивать|0\n");
        builder.append("lip#губа@губа|0\n");
        builder.append("lock#замок@замок|0\n");
        builder.append("map#карта@карта|0\n");
        builder.append("match#спичка@спичка, сделки, соответствовать|0\n");
        builder.append("monkey#обезьяна@обезьяна|0\n");
        builder.append("moon#луна@луна|0\n");
        builder.append("mouth#рот@рот, жевать|0\n");
        builder.append("muscle#мускул@мускул|0\n");
        builder.append("nail#ноготь@ноготь|0\n");
        builder.append("neck#шея@шея, обниматься|0\n");
        builder.append("needle#игла@игла|0\n");
        builder.append("nerve#нерв@нерв|0\n");
        builder.append("net#чистый@чистый, сеть|0\n");
        builder.append("nose#нос@нос|0\n");
        builder.append("nut#орех@орех|0\n");
        builder.append("office#офис@офис|0\n");
        builder.append("orange#апельсин@апельсин, оранжевый|0\n");
        builder.append("oven#духовка@духовка, печь|0\n");
        builder.append("parcel#пакет@пакет, распределять|0\n");
        builder.append("pen#ручка@ручка|0\n");
        builder.append("pencil#карандаш@карандаш|0\n");
        builder.append("picture#картина@картина|0\n");
        builder.append("pig#свинья@свинья|0\n");
        builder.append("pin#булавка@булавка, прикреплять|0\n");
        builder.append("pipe#труба@труба|0\n");
        builder.append("plane#самолёт@самолёт|0\n");
        builder.append("plate#пластина@пластина|0\n");
        builder.append("plow#плуг@плуг, пахать|0\n");
        builder.append("pocket#карман@карман, присваивать|0\n");
        builder.append("pot#горшок@горшок|0\n");
        builder.append("potato#картофель@картофель|0\n");
        builder.append("prison#тюрьма@тюрьма|0\n");
        builder.append("pump#насос@насос, качать|0\n");
        builder.append("rail#рельс@рельс, перевозить поездом|0\n");
        builder.append("rat#крыса@крыса|0\n");
        builder.append("receipt#квитанция@квитанция|0\n");
        builder.append("ring#кольцо@кольцо, звонить|0\n");
        builder.append("rod#прут@прут|0\n");
        builder.append("roof#крыша@крыша|0\n");
        builder.append("root#корень@корень|0\n");
        builder.append("sail#парус@парус|0\n");
        builder.append("school#школа@школа|0\n");
        builder.append("scissors#ножницы@ножницы|0\n");
        builder.append("screw#винт@винт|0\n");
        builder.append("seed#семя@семя|0\n");
        builder.append("sheep#овцы@овцы|0\n");
        builder.append("shelf#полка@полка|0\n");
        builder.append("ship#корабль@корабль|0\n");
        builder.append("shirt#рубашка@рубашка|0\n");
        builder.append("shoe#ботинок@ботинок|0\n");
        builder.append("skin#кожа@кожа, очищать|0\n");
        builder.append("skirt#юбка@юбка|0\n");
        builder.append("snake#змея@змея|0\n");
        builder.append("sock#носок@носок|0\n");
        builder.append("spade#лопата@лопата|0\n");
        builder.append("sponge#губка@губка|0\n");
        builder.append("spoon#ложка@ложка|0\n");
        builder.append("spring#весна@весна|0\n");
        builder.append("square#квадрат@квадрат|0\n");
        builder.append("stamp#печать@печать, марка, отпечатывать|0\n");
        builder.append("star#звезда@звезда|0\n");
        builder.append("station#станция@станция|0\n");
        builder.append("stem#стебель@стебель, происходить|0\n");
        builder.append("stick#палка@палка, прикреплять|0\n");
        builder.append("stocking#снабжать@снабжать|0\n");
        builder.append("stomach#живот@живот, смелость, переваривать|0\n");
        builder.append("store#магазин@магазин, запас|0\n");
        builder.append("street#улица@улица|0\n");
        builder.append("sun#солнце@солнце|0\n");
        builder.append("table#стол@стол|0\n");
        builder.append("tail#хвост@хвост, выслеживать|0\n");
        builder.append("thread#нить@нить, пронизывать|0\n");
        builder.append("throat#горло@горло|0\n");
        builder.append("thumb#листать@листать, большой палец|0\n");
        builder.append("ticket#билет@билет|0\n");
        builder.append("toe#палец@палец ноги|0\n");
        builder.append("tongue#язык@язык|0\n");
        builder.append("tooth#зуб@зуб|0\n");
        builder.append("town#город@город|0\n");
        builder.append("train#поезд@поезд|0\n");
        builder.append("tray#поднос@поднос|0\n");
        builder.append("tree#дерево@дерево|0\n");
        builder.append("trousers#брюки@брюки|0\n");
        builder.append("umbrella#зонт@зонт|0\n");
        builder.append("wall#стена@стена|0\n");
        builder.append("watch#часы@часы|0\n");
        builder.append("wheel#колесо@колесо, вертеть|0\n");
        builder.append("whip#кнут@кнут, хлестать|0\n");
        builder.append("whistle#свист@свист, свистеть|0\n");
        builder.append("window#окно@окно|0\n");
        builder.append("wing#крыло@крыло|0\n");
        builder.append("wire#провод@провод|0\n");
        builder.append("worm#червь@червь|0\n");

        builder.append("account#счет@счет, считать|0\n");
        builder.append("act#действия@действия|0\n");
        builder.append("addition#дополнение@дополнение|0\n");
        builder.append("adjust#регулировать@регулировать, приспосабливать|0\n");
        builder.append("advertisement#реклама@реклама|0\n");
        builder.append("agreement#соглашение@соглашение|0\n");
        builder.append("air#воздух@воздух, проветривать|0\n");
        builder.append("amount#количество@количество, означать, составлять|0\n");
        builder.append("amusement#развлечение@развлечение|0\n");
        builder.append("animal#животное@животное|0\n");
        builder.append("answer#ответ@ответ, отвечать|0\n");
        builder.append("apparatus#аппарат@аппарат|0\n");
        builder.append("approval#одобрение@одобрение|0\n");
        builder.append("argument#спор@спор, аргумент|0\n");
        builder.append("art#искусство@искусство, художественный|0\n");
        builder.append("attack#нападение@нападение, нападать|0\n");
        builder.append("attempt#попытка@попытка, пытаться|0\n");
        builder.append("attention#внимание@внимание|0\n");
        builder.append("attraction#достопримечательности@достопримечательности|0\n");
        builder.append("authority#власть@власть, полномочие|0\n");
        builder.append("back#назад@назад, спина, отступать|0\n");
        builder.append("balance#баланс@баланс, уравновешивать|0\n");
        builder.append("base#основной@основной|0\n");
        builder.append("behavior#поведение@поведение|0\n");
        builder.append("belief#вера@вера|0\n");
        builder.append("birth#рождение@рождение|0\n");
        builder.append("bit#частица@частица, бит|0\n");
        builder.append("bite#укус@укус, кусать|0\n");
        builder.append("blood#кровь@кровь|0\n");
        builder.append("blow#удар@удар, дуть|0\n");
        builder.append("body#тело@тело|0\n");
        builder.append("brass#медь@медь, руководство|0\n");
        builder.append("bread#хлеб@хлеб|0\n");
        builder.append("breath#дыхание@дыхание|0\n");
        builder.append("brother#брат@брат|0\n");
        builder.append("building#здание@здание, строение|0\n");
        builder.append("burn#ожог@ожог, гореть|0\n");
        builder.append("burst#взрыв@взрыв, взрывать|0\n");
        builder.append("business#бизнес@бизнес, деловой|0\n");
        builder.append("butter#масло@масло|0\n");
        builder.append("canvas#холст@холст|0\n");
        builder.append("care#забота@забота, лечение|0\n");
        builder.append("cause#причина@причина, вызывать|0\n");
        builder.append("chalk#мел@мел, рисовать мелом|0\n");
        builder.append("chance#шанс@шанс, случайный, рисковать|0\n");
        builder.append("change#изменение@изменение, замена|0\n");
        builder.append("cloth#ткань@ткань, одежда|0\n");
        builder.append("coal#уголь@уголь|0\n");
        builder.append("color#цвет@цвет|0\n");
        builder.append("comfort#комфорт@комфорт|0\n");
        builder.append("committee#комитет@комитет|0\n");
        builder.append("company#компания@компания|0\n");
        builder.append("comparison#сравнение@сравнение|0\n");
        builder.append("competition#соревнование@соревнование|0\n");
        builder.append("condition#условие@условие, обуславливать|0\n");
        builder.append("connection#связь@связь|0\n");
        builder.append("control#контроль@контроль|0\n");
        builder.append("cook#готовить@готовить|0\n");
        builder.append("copper#медь@медь|0\n");
        builder.append("copy#копия@копия|0\n");
        builder.append("cork#пробка@пробка|0\n");
        builder.append("cotton#хлопок@хлопок|0\n");
        builder.append("cough#кашель@кашель|0\n");
        builder.append("country#страна@страна|0\n");
        builder.append("cover#покрытие@покрытие, покрывать, обложка|0\n");
        builder.append("crack#первоклассный@первоклассный, взламывать|0\n");
        builder.append("credit#кредит@кредит, верить|0\n");
        builder.append("crime#преступление@преступление|0\n");
        builder.append("crush#давка@давка, сокрушать|0\n");
        builder.append("cry#плакать@плакать|0\n");
        builder.append("current#текущий@текущий, поток|0\n");
        builder.append("curve#кривая@кривая, избегать|0\n");
        builder.append("damage#повреждение@повреждение, повреждать|0\n");
        builder.append("danger#опасность@опасность|0\n");
        builder.append("daughter#дочь@дочь|0\n");
        builder.append("day#день@день|0\n");
        builder.append("death#смерть@смерть|0\n");
        builder.append("debt#долг@долг|0\n");
        builder.append("decision#решение@решение|0\n");
        builder.append("degree#уровень@уровень, градус, степень|0\n");
        builder.append("design#намереваться@намереваться|0\n");
        builder.append("desire#желание@желание, желать|0\n");
        builder.append("destruction#разрушение@разрушение|0\n");
        builder.append("detail#детализировать@детализировать, детали|0\n");
        builder.append("development#развитие@развитие|0\n");
        builder.append("digestion#вываривание@вываривание|0\n");
        builder.append("direction#управление@управление|0\n");
        builder.append("discovery#открытие@открытие|0\n");
        builder.append("discussion#обсуждение@обсуждение|0\n");
        builder.append("disease#болезнь@болезнь|0\n");
        builder.append("disgust#отвращение@отвращение|0\n");
        builder.append("distance#дистанция@дистанция|0\n");
        builder.append("distribution#распределение@распределение|0\n");
        builder.append("division#подразделение@подразделение|0\n");
        builder.append("doubt#сомнение@сомнение|0\n");
        builder.append("drink#пить@пить, напиток|0\n");
        builder.append("driving#вождение@вождение|0\n");
        builder.append("dust#чистить@чистить, пыль|0\n");
        builder.append("earth#земля@земля|0\n");
        builder.append("edge#продвигаться@продвигаться, край|0\n");
        builder.append("education#образование@образование|0\n");
        builder.append("effect#производить@производить, эффект|0\n");
        builder.append("end#заканчивать@заканчивать, конец|0\n");
        builder.append("error#ошибка@ошибка|0\n");
        builder.append("event#случай@случай, событие|0\n");
        builder.append("example#пример@пример|0\n");
        builder.append("exchange#обмен@обмен, обменивать|0\n");
        builder.append("existence#существование@существование|0\n");
        builder.append("expansion#расширение@расширение|0\n");
        builder.append("experience#опыт@опыт, испытывать|0\n");
        builder.append("expert#опытный@опытный, эксперт|0\n");
        builder.append("fact#факт@факт|0\n");
        builder.append("fall#падение@падение, осень|0\n");
        builder.append("family#семья@семья|0\n");
        builder.append("father#папа@папа|0\n");
        builder.append("fear#бояться@бояться, страх|0\n");
        builder.append("feeling#чувство@чувство|0\n");
        builder.append("fiction#беллетристика@беллетристика, фикция|0\n");
        builder.append("field#область@область, поле, выставлять|0\n");
        builder.append("fight#борьба@борьба, бороться|0\n");
        builder.append("fire#гонь@гонь|0\n");
        builder.append("flame#пылать@пылать, пламя|0\n");
        builder.append("flight#полет@полет, рейс|0\n");
        builder.append("flower#цветок@цветок|0\n");
        builder.append("fold#сгиб@сгиб, сворачивать|0\n");
        builder.append("food#еда@еда, продовольствие|0\n");
        builder.append("force#сила@сила, вынуждать, вызывать|0\n");
        builder.append("form#форма@форма|0\n");
        builder.append("friend#друг@друг|0\n");
        builder.append("front#выходить@выходить, фронт, передний|0\n");
        builder.append("fruit#фрукт@фрукт|0\n");
        builder.append("glass#стекло@стекло, стакан|0\n");
        builder.append("gold#золото@золото|0\n");
        builder.append("government#государство@государство|0\n");
        builder.append("grain#зерно@зерно|0\n");
        builder.append("grass#трава@трава|0\n");
        builder.append("grip#власть@власть, захват, захватывать|0\n");
        builder.append("group#группа@группа|0\n");
        builder.append("growth#рост@рост|0\n");
        builder.append("guide#гид@гид, справочник, путеводитель|0\n");
        builder.append("harbor#приютить@приютить, питать, вставать на якорь, гавань|0\n");
        builder.append("harmony#гармония@гармония|0\n");
        builder.append("hate#ненавидеть@ненавидеть, очень не хотеть, ненависть|0\n");
        builder.append("hearing#слушание@слушание, слух|0\n");
        builder.append("heat#нагревать@нагревать, высокая температура|0\n");
        builder.append("help#помощь@помощь, помогать|0\n");
        builder.append("history#история@история|0\n");
        builder.append("hole#продырявливать@продырявливать, отверстие|0\n");
        builder.append("hope#надежда@надежда, надеяться|0\n");
        builder.append("hour#час@час|0\n");
        builder.append("humor#юмор@юмор|0\n");
        builder.append("ice#лёд@лёд|0\n");
        builder.append("idea#идея@идея|0\n");
        builder.append("impulse#импульс@импульс|0\n");
        builder.append("increase#увеличивать@увеличивать, увеличение|0\n");
        builder.append("industry#индустрия@индустрия|0\n");
        builder.append("ink#чернила@чернила|0\n");
        builder.append("insect#насекомое@насекомое|0\n");
        builder.append("instrument#инструмент@инструмент|0\n");
        builder.append("insurance#страховка@страховка|0\n");
        builder.append("interest#интерес@интерес, процент, доля|0\n");
        builder.append("invention#изобретение@изобретение|0\n");
        builder.append("iron#утюг@утюг, гладить|0\n");
        builder.append("jelly#превращать@превращать в желе, желе|0\n");
        builder.append("join#присоединяться@присоединяться, объединение, соединение|0\n");
        builder.append("journey#путешествовать@путешествовать, поездка|0\n");
        builder.append("judge#судья@судья, судить|0\n");
        builder.append("jump#прыгать@прыгать, прыжок|0\n");
        builder.append("kick#удар@удар, ударить|0\n");
        builder.append("kiss#поцелуй@поцелуй, целовать|0\n");
        builder.append("knowledge#знание@знание|0\n");
        builder.append("land#земля@земля|0\n");
        builder.append("language#язык@язык|0\n");
        builder.append("laugh#смеяться@смеяться, смех|0\n");
        builder.append("law#законный@законный, закон|0\n");
        builder.append("lead#принуждать@принуждать, побеждать, вести, лидерство|0\n");
        builder.append("learning#изучение@изучение, обучение|0\n");
        builder.append("leather#кожа@кожа|0\n");
        builder.append("letter#письмо@письмо|0\n");
        builder.append("level#уровень@уровень, выравнивать, направлять|0\n");
        builder.append("lift#поднимать@поднимать, лифт|0\n");
        builder.append("light#освещать@освещать, легкий, свет|0\n");
        builder.append("limit#ограничивать@ограничивать|0\n");
        builder.append("linen#льняной@льняной, полотно|0\n");
        builder.append("liquid#жидкий@жидкий, жидкость|0\n");
        builder.append("list#список@список, перечислять|0\n");
        builder.append("loss#потеря@потеря|0\n");
        builder.append("love#любовь@любовь|0\n");
        builder.append("machine#машина@машина|0\n");
        builder.append("man#человек@человек|0\n");
        builder.append("mark#марка@марка|0\n");
        builder.append("market#рынок@рынок|0\n");
        builder.append("mass#масса@масса|0\n");
        builder.append("meal#еда@еда|0\n");
        builder.append("measure#мера@мера, измерять|0\n");
        builder.append("meat#мясо@мясо|0\n");
        builder.append("meeting#встреча@встреча|0\n");
        builder.append("memory#память@память|0\n");
        builder.append("metal#металл@металл|0\n");
        builder.append("middle#средний@средний, середина|0\n");
        builder.append("milk#молоко@молоко|0\n");
        builder.append("mind#ум@ум, возражать|0\n");
        builder.append("mine#мой@мой, месторождение, мина|0\n");
        builder.append("minute#минута@минута|0\n");
        builder.append("mist#туман@туман|0\n");
        builder.append("money#деньги@деньги|0\n");
        builder.append("month#месяц@месяц|0\n");
        builder.append("morning#утро@утро|0\n");
        builder.append("mother#мать@мать|0\n");
        builder.append("motion#двигаться@двигаться, движение, жест|0\n");
        builder.append("mountain#гора@гора, горный|0\n");
        builder.append("move#двигать@двигать, шаг, движение|0\n");
        builder.append("music#музыка@музыка|0\n");
        builder.append("name#имя@имя|0\n");
        builder.append("nation#нация@нация|0\n");
        builder.append("need#потребность@потребность, требоваться|0\n");
        builder.append("news#новости@новости|0\n");
        builder.append("night#ночь@ночь|0\n");
        builder.append("noise#шум@шум|0\n");
        builder.append("note#примечание@примечание, отмечать|0\n");
        builder.append("number#число@число, номер|0\n");
        builder.append("observation#наблюдение@наблюдение|0\n");
        builder.append("offer#предложение@предложение, предлагать|0\n");
        builder.append("oil#масло@масло|0\n");
        builder.append("operation#операция@операция, действие|0\n");
        builder.append("opinion#мнение@мнение|0\n");
        builder.append("order#заказ@заказ, заказывать, приказывать|0\n");
        builder.append("organization#организация@организация|0\n");
        builder.append("ornament#украшение@украшение, украшать|0\n");
        builder.append("owner#владелец@владелец|0\n");
        builder.append("page#страница@страница|0\n");
        builder.append("pain#боль@боль, причинять боль|0\n");
        builder.append("paint#краска@краска, рисовать, красить|0\n");
        builder.append("paper#бумага@бумага|0\n");
        builder.append("part#часть@часть, отделять, разделяться|0\n");
        builder.append("paste#приклеивать@приклеивать, паста|0\n");
        builder.append("payment#оплата@оплата|0\n");
        builder.append("peace#мир@мир|0\n");
        builder.append("person#персона@персона|0\n");
        builder.append("place#размещать@размещать, помещать, занимать место, место|0\n");
        builder.append("plant#завод@завод, растение, прививать, сеять|0\n");
        builder.append("play#играть@играть|0\n");
        builder.append("pleasure#удовольствие@удовольствие|0\n");
        builder.append("point#пункт@пункт, точка, указывать|0\n");
        builder.append("poison#яд@яд, отравлять|0\n");
        builder.append("polish#полировать@полировать|0\n");
        builder.append("porter#швейцар@швейцар,носильщик|0\n");
        builder.append("position#помещать@помещать, позиция|0\n");
        builder.append("powder#порошок@порошок|0\n");
        builder.append("power#сила@сила, власть|0\n");
        builder.append("price#цена@цена|0\n");
        builder.append("print#печатать@печатать|0\n");
        builder.append("process#обрабатывать@обрабатывать, процесс|0\n");
        builder.append("produce#продукт@продукт, производить|0\n");
        builder.append("profit#прибыль@прибыль, получать прибыль|0\n");
        builder.append("property#свойства@свойства|0\n");
        builder.append("prose#проза@проза|0\n");
        builder.append("protest#возражать@возражать, протест|0\n");
        builder.append("pull#напряжение@напряжение, тянуть|0\n");
        builder.append("punishment#наказание@наказание|0\n");
        builder.append("purpose#намереваться@намереваться, цель|0\n");
        builder.append("push#толчок@толчок, подталкивать|0\n");
        builder.append("quality#качество@качество, качественный|0\n");
        builder.append("question#вопрос@вопрос	|0\n");
        builder.append("rain#дождь@дождь|0\n");
        builder.append("range#диапазон@диапазон, располагаться|0\n");
        builder.append("rate#норма@норма,разряд|0\n");
        builder.append("ray#луч@луч|0\n");
        builder.append("reaction#реакция@реакция|0\n");
        builder.append("reading#чтение@чтение|0\n");
        builder.append("reason#рассуждать@рассуждать, причина, разум|0\n");
        builder.append("record#рекордный@рекордный, отчёт, делать запись|0\n");
        builder.append("regret#сожаление@сожаление, сожалеть|0\n");
        builder.append("relation#отношение@отношение|0\n");
        builder.append("religion#религия@религия|0\n");
        builder.append("representative#представитель@представитель|0\n");
        builder.append("request#запрос@запрос, просить|0\n");
        builder.append("respect#уважение@уважение, уважать|0\n");
        builder.append("rest#отдых@отдых, отдыхать, оставаться|0\n");
        builder.append("reward#награда@награда, вознаграждать|0\n");
        builder.append("rhythm#ритм@ритм|0\n");
        builder.append("rice#рис@рис|0\n");
        builder.append("river#река@река|0\n");
        builder.append("road#дорога@дорога|0\n");
        builder.append("roll#рулон@рулон, ведомость, катиться, въезжать|0\n");
        builder.append("room#комната@комната|0\n");
        builder.append("rub#протирать@протирать, тереться|0\n");
        builder.append("rule#правило@правило|0\n");
        builder.append("run#бег@бег, бегать|0\n");
        builder.append("salt#соль@соль, солить|0\n");
        builder.append("sand#песок@песок|0\n");
        builder.append("scale#измерять@измерять, масштаб|0\n");
        builder.append("science#наука@наука|0\n");
        builder.append("sea#море@море|0\n");
        builder.append("seat#сиденье@сиденье, усаживать, место|0\n");
        builder.append("secretary#секретарь@секретарь|0\n");
        builder.append("selection#выбор@выбор|0\n");
        builder.append("self#сам@сам|0\n");
        builder.append("sense#чувство@чувство, значения, смысл, ощущать|0\n");
        builder.append("servant#слуга@слуга|0\n");
        builder.append("sex#секс@секс, пол|0\n");
        builder.append("shade#оттенок@оттенок,тень, заштриховывать|0\n");
        builder.append("shake#встряска@встряска, встряхивать, дрожать, потрясать|0\n");
        builder.append("shame#позор@позор, позорить|0\n");
        builder.append("shock#шок@шок, потрясать|0\n");
        builder.append("side#сторона@сторона, примыкать|0\n");
        builder.append("sign#знак@знак, признак, подписывать|0\n");
        builder.append("silk#шёлк@шёлк|0\n");
        builder.append("silver#серебро@серебро|0\n");
        builder.append("sister#сестра@сестра|0\n");
        builder.append("size#размер@размер|0\n");
        builder.append("sky#небо@небо|0\n");
        builder.append("sleep#спать@спать|0\n");
        builder.append("slip#промах@промах, бланк, подсовывать, скользить|0\n");
        builder.append("slope#наклон@наклон, клониться|0\n");
        builder.append("smash#удар@удар, разбиваться|0\n");
        builder.append("smell#запах@запах, пахнуть|0\n");
        builder.append("smile#улыбка@улыбка, улыбаться|0\n");
        builder.append("smoke#дым@дым, курить|0\n");
        builder.append("sneeze#чиханье@чиханье, чихать|0\n");
        builder.append("snow#снег@снег|0\n");
        builder.append("soap#мыло@мыло, мылить|0\n");
        builder.append("society#общество@общество|0\n");
        builder.append("son#сын@сын|0\n");
        builder.append("song#песня@песня|0\n");
        builder.append("sort#вид@вид, сортировать|0\n");
        builder.append("sound#звук@звук|0\n");
        builder.append("soup#суп@суп|0\n");
        builder.append("space#пространство@пространство, космос|0\n");
        builder.append("stage#стадия@стадия, сцена, организовывать|0\n");
        builder.append("start#начинать@начинать|0\n");
        builder.append("statement#утверждение@утверждение|0\n");
        builder.append("steam#пар@пар, париться, двигаться|0\n");
        builder.append("steel#сталь@сталь|0\n");
        builder.append("step#шаг@шаг, шагать|0\n");
        builder.append("stitch#стежок@стежок, сшивать|0\n");
        builder.append("stone#камень@камень|0\n");
        builder.append("stop#останавливаться@останавливаться, остановка|0\n");
        builder.append("story#история@история|0\n");
        builder.append("stretch#отрезки@отрезки, протягивать, простираться|0\n");
        builder.append("structure#структура@структура|0\n");
        builder.append("substance#вещество@вещество, сущность|0\n");
        builder.append("sugar#сахар@сахар|0\n");
        builder.append("suggestion#предложение@предложение, предположение|0\n");
        builder.append("summer#лето@лето|0\n");
        builder.append("support#поддержка@поддержка, поддерживать|0\n");
        builder.append("surprise#сюрприз@сюрприз|0\n");
        builder.append("swim#плаванье@плаванье, плавать|0\n");
        builder.append("system#система@система|0\n");
        builder.append("talk#разговор@разговор, говорить|0\n");
        builder.append("taste#вкус@вкус, испытывать|0\n");
        builder.append("tax#налог@налог, облагать налогом|0\n");
        builder.append("teaching#обучение@обучение|0\n");
        builder.append("tendency#тенденция@тенденция|0\n");
        builder.append("test#проверять@проверять, тест|0\n");
        builder.append("theory#теория@теория|0\n");
        builder.append("thing#вещь@вещь|0\n");
        builder.append("thought#мысль@мысль|0\n");
        builder.append("thunder#гром@гром, греметь|0\n");
        builder.append("time#время@время|0\n");
        builder.append("tin#олово@олово,консервная банка|0\n");
        builder.append("top#возглавлять@возглавлять, вершина, главный|0\n");
        builder.append("touch#прикосновение@прикосновение, касаться|0\n");
        builder.append("trade#торговля@торговля, обменивать|0\n");
        builder.append("transport#транспорт@транспорт, транспортировать|0\n");
        builder.append("trick#уловка@уловка, обманывать|0\n");
        builder.append("trouble#проблема@проблема, беспокоить|0\n");
        builder.append("turn#поворот@поворот, поворачивать|0\n");
        builder.append("twist#завихрение@завихрение, крутить|0\n");
        builder.append("unit#единица@единица|0\n");
        builder.append("use#использование@использование, использовать|0\n");
        builder.append("value#ценность@ценность, оценивать|0\n");
        builder.append("verse#стих@стих|0\n");
        builder.append("vessel#судно@судно, сосуд|0\n");
        builder.append("view#вид@вид, взгляд, рассматривать|0\n");
        builder.append("voice#голос@голос, высказывать|0\n");
        builder.append("war#война@война|0\n");
        builder.append("wash#мытьё@мытьё, стирать, чистить|0\n");
        builder.append("waste#ненужный@ненужный, отходы, трата|0\n");
        builder.append("water#вода@вода|0\n");
        builder.append("wave#волна@волна|0\n");
        builder.append("wax#воск@воск, натирать воском|0\n");
        builder.append("weather#погода@погода|0\n");
        builder.append("week#неделя@неделя|0\n");
        builder.append("weight#вес@вес, нагружать|0\n");
        builder.append("wind#ветер@ветер|0\n");
        builder.append("wine#вино@вино|0\n");
        builder.append("winter#зима@зима, зимовать|0\n");
        builder.append("woman#женщина@женщина|0\n");
        builder.append("wood#лес@лес|0\n");
        builder.append("wool#шерсть@шерсть|0\n");
        builder.append("word#слово@слово|0\n");
        builder.append("work#работа@работа, работать|0\n");
        builder.append("wound#проветривать@проветривать, рана, ранить|0\n");
        builder.append("writing#письмо@письмо|0\n");

        builder.append("come#приходить@приходить, приезжать|0\n");
        builder.append("get#получать@получать, заставлять|0\n");
        builder.append("give#давать@давать|0\n");
        builder.append("go#ходить@ходить, идти|0\n");
        builder.append("keep#продолжать@продолжать, держать, оставлять, не допускать|0\n");
        builder.append("let#позволять@позволять|0\n");
        builder.append("make#делать/сделать@делать/сделать, заставлять|0\n");
        builder.append("put#помещать@помещать|0\n");
        builder.append("seem#казаться@казаться, представляться|0\n");
        builder.append("take#брать/взять@брать/взять|0\n");
        builder.append("be#быть@быть|0\n");
        builder.append("do#делать@делать|0\n");
        builder.append("have#иметь@иметь, съесть, знать|0\n");
        builder.append("say#говорить@говорить|0\n");
        builder.append("see#видеть@видеть|0\n");
        builder.append("send#посылать@посылать|0\n");
        builder.append("may#мочь@мочь|0\n");
        builder.append("will#быть@быть хотеть|0\n");
        builder.append("about#о@о |0\n");
        builder.append("across#через@через|0\n");
        builder.append("after#после@после|0\n");
        builder.append("against#против@против|0\n");
        builder.append("among#среди@среди|0\n");
        builder.append("at#в@в |0\n");
        builder.append("before#перед@перед|0\n");
        builder.append("between#между@между|0\n");
        builder.append("by#к@к, в соответствии с, за, на|0\n");
        builder.append("down#вниз@вниз|0\n");
        builder.append("from#из@из|0\n");
        builder.append("in#в@в|0\n");
        builder.append("off#прочь@прочь, от|0\n");
        builder.append("on#на@на|0\n");
        builder.append("over#по@по|0\n");
        builder.append("through#через@через|0\n");
        builder.append("to#к@к, до, в|0\n");
        builder.append("under#под@под|0\n");
        builder.append("up#вверх@вверх|0\n");
        builder.append("with#с@с |0\n");
        builder.append("as#поскольку@поскольку, как|0\n");
        builder.append("for#для@для|0\n");
        builder.append("of#из@из, о, от|0\n");
        builder.append("till#пока@пока, до|0\n");
        builder.append("than#чем@чем|0\n");
        builder.append("a#любой@любой, один, каждый, некий|0\n");
        builder.append("all#все@все, весь|0\n");
        builder.append("any#любой@любой, никто|0\n");
        builder.append("every#каждый@каждый|0\n");
        builder.append("no#никакой@никакой, нет|0\n");
        builder.append("other#другой@другой|0\n");
        builder.append("some#некоторый@некоторый, немного|0\n");
        builder.append("such#такой@такой, таким образом|0\n");
        builder.append("that#что@что|0\n");
        builder.append("this#это@это, этот|0\n");
        builder.append("i#я@я |0\n");
        builder.append("he#он@он |0\n");
        builder.append("you#ты@ты, вы|0\n");
        builder.append("who#кто@кто|0\n");
        builder.append("and#и@и |0\n");
        builder.append("because#потому@потому что|0\n");
        builder.append("but#а@а, но|0\n");
        builder.append("or#или@или|0\n");
        builder.append("if#если@если|0\n");
        builder.append("though#хотя@хотя|0\n");
        builder.append("while#в@в то время как|0\n");
        builder.append("how#как@как|0\n");
        builder.append("when#когда@когда|0\n");
        builder.append("where#где@где, куда, откуда|0\n");
        builder.append("why#почему@почему|0\n");
        builder.append("again#снова@снова|0\n");
        builder.append("ever#никогда@никогда|0\n");
        builder.append("far#самый@самый дальний|0\n");
        builder.append("forward#отправлять@отправлять, вперед|0\n");
        builder.append("here#здесь@здесь, сюда|0\n");
        builder.append("near#рядом@рядом, около|0\n");
        builder.append("now#теперь@теперь, сейчас|0\n");
        builder.append("out#вне@вне, снаружи|0\n");
        builder.append("still#все@все еще|0\n");
        builder.append("then#тогда@тогда|0\n");
        builder.append("there#там@там, туда|0\n");
        builder.append("together#вместе@вместе|0\n");
        builder.append("well#хорошо@хорошо, намного|0\n");
        builder.append("almost#почти@почти|0\n");
        builder.append("enough#достаточно@достаточно|0\n");
        builder.append("even#еще@еще, даже|0\n");
        builder.append("little#маленький@маленький|0\n");
        builder.append("much#много@много|0\n");
        builder.append("not#не@не|0\n");
        builder.append("only#только@только|0\n");
        builder.append("quite#весьма@весьма|0\n");
        builder.append("so#так@так|0\n");
        builder.append("very#очень@очень|0\n");
        builder.append("tomorrow#завтра@завтра|0\n");
        builder.append("yesterday#вчера@вчера|0\n");
        builder.append("north#север@север|0\n");
        builder.append("south#юг@юг|0\n");
        builder.append("east#восток@восток|0\n");
        builder.append("west#запад@запад|0\n");
        builder.append("please#пожалуйста@пожалуйста|0\n");
        builder.append("yes#да@да|0\n");

        builder.append("able#способный@способный, быть в состоянии|0\n");
        builder.append("acid#кислота@кислота, кислый|0\n");
        builder.append("angry#сердитый@сердитый|0\n");
        builder.append("beautiful#красивый@красивый|0\n");
        builder.append("black#чёрный@чёрный|0\n");
        builder.append("boiling#кипение@кипение|0\n");
        builder.append("bright#яркий@яркий, умный|0\n");
        builder.append("broken#сломанный@сломанный|0\n");
        builder.append("brown#коричневый@коричневый|0\n");
        builder.append("cheap#дешёвый@дешёвый|0\n");
        builder.append("chemical#химикат@химикат|0\n");
        builder.append("chief#главный@главный|0\n");
        builder.append("clean#чистый@чистый, чистить|0\n");
        builder.append("clear#ясный@ясный, очищать, оправдываться|0\n");
        builder.append("common#общий@общий|0\n");
        builder.append("complex#комплекс@комплекс|0\n");
        builder.append("conscious#сознательный@сознательный|0\n");
        builder.append("cut#резать@резать|0\n");
        builder.append("deep#глубокий@глубокий, глубоко|0\n");
        builder.append("dependent#зависимый@зависимый|0\n");
        builder.append("early#рано@рано, ранний|0\n");
        builder.append("elastic#эластичный@эластичный|0\n");
        builder.append("equal#равный@равный, равняться|0\n");
        builder.append("fat#толстый@толстый, жир|0\n");
        builder.append("fertile#плодородный@плодородный|0\n");
        builder.append("first#первый@первый|0\n");
        builder.append("fixed#неподвижный@неподвижный, неизменный|0\n");
        builder.append("flat#плоский@плоский, квартира, плоскость|0\n");
        builder.append("free#свобода@свобода, бесплатный|0\n");
        builder.append("frequent#частый@частый, часто посещать|0\n");
        builder.append("full#полный@полный|0\n");
        builder.append("general#общий@общий, генерал|0\n");
        builder.append("good#хороший@хороший|0\n");
        builder.append("great#великий@великий, великолепный|0\n");
        builder.append("grey#серый@серый|0\n");
        builder.append("hanging#вывешивание@вывешивание, висение|0\n");
        builder.append("happy#счастье@счастье|0\n");
        builder.append("hard#трудный@трудный, тяжёлый, твёрдый|0\n");
        builder.append("healthy#здоровый@здоровый|0\n");
        builder.append("high#высокий@высокий, высоко|0\n");
        builder.append("hollow#пустота@пустота|0\n");
        builder.append("important#важный@важный|0\n");
        builder.append("jewel#драгоценный@драгоценный камень|0\n");
        builder.append("kind#добрый@добрый, вид|0\n");
        builder.append("like#подобный@подобный, любить, нравиться|0\n");
        builder.append("living#проживаниe@проживаниe|0\n");
        builder.append("long#долго@долго, длинный|0\n");
        builder.append("male#мужской@мужской, мужчина|0\n");
        builder.append("married#женатый@женатый, замужем|0\n");
        builder.append("material#материал@материал|0\n");
        builder.append("medical#медицинский@медицинский|0\n");
        builder.append("military#военный@военный|0\n");
        builder.append("natural#натуральный@натуральный|0\n");
        builder.append("necessary#необходимый@необходимый|0\n");
        builder.append("new#новый@новый|0\n");
        builder.append("normal#нормальный@нормальный|0\n");
        builder.append("open#открытый@открытый, открывать|0\n");
        builder.append("parallel#параллельный@параллельный, находить|0\n");
        builder.append("past#мимо@мимо, прошлый|0\n");
        builder.append("physical#физический@физический|0\n");
        builder.append("political#политический@политический|0\n");
        builder.append("poor#бедный@бедный, плохой, слабый|0\n");
        builder.append("possible#возможный@возможный|0\n");
        builder.append("present#существующий@существующий, подарок|0\n");
        builder.append("private#личный@личный, приватный|0\n");
        builder.append("probable#вероятный@вероятный|0\n");
        builder.append("quick#быстрый@быстрый|0\n");
        builder.append("quiet#тихий@тихий, успокаивать|0\n");
        builder.append("ready#готовый@готовый, готов|0\n");
        builder.append("read#читать@читать|0\n");
        builder.append("regular#регулярный@регулярный|0\n");
        builder.append("responsible#ответственный@ответственный|0\n");
        builder.append("right#верный@верный, право|0\n");
        builder.append("round#вокруг@вокруг, круглый, раунд|0\n");
        builder.append("same#то@то же самое|0\n");
        builder.append("second#секунда@секунда, второй|0\n");
        builder.append("separate#отдельный@отдельный, отделять|0\n");
        builder.append("serious#серьёзный@серьёзный|0\n");
        builder.append("sharp#острый@острый|0\n");
        builder.append("smooth#гладкий@гладкий, мягкий, приглаживать|0\n");
        builder.append("sticky#липкий@липкий|0\n");
        builder.append("stiff#жесткий@жесткий|0\n");
        builder.append("straight#прямой@прямой|0\n");
        builder.append("strong#сильный@сильный|0\n");
        builder.append("sudden#внезапный@внезапный|0\n");
        builder.append("sweet#сладкий@сладкий|0\n");
        builder.append("tall#высокий@высокий|0\n");
        builder.append("thick#толстый@толстый, густой|0\n");
        builder.append("tight#трудный@трудный|0\n");
        builder.append("tired#усталый@усталый|0\n");
        builder.append("true#правда@правда, правдивый|0\n");
        builder.append("violent#сильный@сильный, жестокий|0\n");
        builder.append("waiting#ожидание@ожидание|0\n");
        builder.append("warm#теплый@теплый, нагревать|0\n");
        builder.append("wet#влажный@влажный|0\n");
        builder.append("wide#широкий@широкий|0\n");
        builder.append("wise#мудрый@мудрый|0\n");
        builder.append("yellow#жёлтый@жёлтый|0\n");
        builder.append("young#молодой@молодой|0\n");

        builder.append("awake#активный@активный, пробуждать, просыпаться|0\n");
        builder.append("bad#плохой@плохой|0\n");
        builder.append("bent#склонность@склонность, сгибать|0\n");
        builder.append("bitter#горький@горький, ожесточенный|0\n");
        builder.append("blue#синий@синий|0\n");
        builder.append("certain#уверенный@уверенный, определенный|0\n");
        builder.append("cold#холодный@холодный|0\n");
        builder.append("complete#полный@полный, заканчивать|0\n");
        builder.append("cruel#жестокий@жестокий	|0\n");
        builder.append("dark#темнота@темнота, темный|0\n");
        builder.append("dead#мёртвый@мёртвый|0\n");
        builder.append("dear#дорогой@дорогой|0\n");
        builder.append("delicate#тонкий@тонкий, деликатный|0\n");
        builder.append("different#различный@различный, другой|0\n");
        builder.append("dirty#грязный@грязный|0\n");
        builder.append("dry#сухой@сухой, сушить|0\n");
        builder.append("false#фальшивый@фальшивый|0\n");
        builder.append("feeble#слабый@слабый|0\n");
        builder.append("female#женский@женский|0\n");
        builder.append("foolish#глупый@глупый|0\n");
        builder.append("future#будущий@будущий, будущее|0\n");
        builder.append("green#зелёный@зелёный|0\n");
        builder.append("ill#плохо@плохо, болеть|0\n");
        builder.append("last#последний@последний, длиться|0\n");
        builder.append("late#опаздывать@опаздывать|0\n");
        builder.append("left#лево@лево, левый|0\n");
        builder.append("loose#свободный@свободный, освобождать|0\n");
        builder.append("loud#громкий@громкий|0\n");
        builder.append("low#низкий@низкий|0\n");
        builder.append("mixed#перемешанный@перемешанный|0\n");
        builder.append("narrow#узкий@узкий, сужаться|0\n");
        builder.append("old#старый@старый|0\n");
        builder.append("opposite#напротив@напротив, противоположный	|0\n");
        builder.append("public#публичный@публичный|0\n");
        builder.append("rough#грубый@грубый|0\n");
        builder.append("sad#расстроенный@расстроенный|0\n");
        builder.append("safe#безопасный@безопасный|0\n");
        builder.append("secret#секретный@секретный|0\n");
        builder.append("short#короткий@короткий|0\n");
        builder.append("simple#простой@простой|0\n");
        builder.append("slow#медленный@медленный|0\n");
        builder.append("small#маленький@маленький|0\n");
        builder.append("soft#мягкий@мягкий|0\n");
        builder.append("solid#твёрдый@твёрдый, солидный|0\n");
        builder.append("special#специальный@специальный|0\n");
        builder.append("strange#странный@странный|0\n");
        builder.append("thin#худой@худой, тонкий, жидкий|0\n");
        builder.append("white#белый@белый |0\n");
        builder.append("wrong#неправильный@неправильный, несправедливость|0\n");

        return builder.toString();
    }
}
