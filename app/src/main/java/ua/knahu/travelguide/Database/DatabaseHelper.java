package ua.knahu.travelguide.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ua.knahu.travelguide.Sightseeing;

/**
 * Класс, необходимый для работы с базой данных.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Название таблицы
    public static final String TABLE_NAME = "Sightseeings";
    // Название базы данных
    static final String DB_NAME = "Sightseeings.DB";
    private static final int SCHEMA = 1;

    private Context myContext;

    /**
     * Конструктор
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context,  DB_NAME, null, SCHEMA);
        this.myContext = context;
    }

    /**
     * Получить локации из базы данных.
     *
     * @param db
     * @return
     */
    public  List<Sightseeing> getListFromDB(SQLiteDatabase db,  List<Sightseeing> lSightseeings){

        Cursor cursor = db.rawQuery("SELECT * FROM Sightseeings", null);

        if (cursor.moveToFirst()) {
            do {
                String title = null;
                if (cursor.getString(1) != null)
                    title = cursor.getString(1);

                String about = null;
                if (cursor.getString(2) != null)
                    about = cursor.getString(2);

                Uri picture = null;
                if (cursor.getString(3) != null)
                    picture = Uri.parse(cursor.getString(3));

                LatLng coordinates = null;
                if (cursor.getString(4) != null) {
                    String temp = cursor.getString(4);
                    temp = temp.replace(" ", "");
                    temp = temp.replace("lat/lng:(", "");
                    temp = temp.replace(")", "");
                    String[] latlong = temp.split(",");
                    coordinates = null;
                    try {
                        double latitude = Double.parseDouble(latlong[0]);
                        double longitude = Double.parseDouble(latlong[1]);
                        coordinates = new LatLng(latitude, longitude);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                Sightseeing sightseeing = new Sightseeing(lSightseeings.size()+1, title, about, picture, coordinates);
                lSightseeings.add(sightseeing);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return lSightseeings;
    }

    /**
     * Метод, для создания базы данных и таблицы с информацией
     *
     * При первоначальном создании таблицы, добавляет локацию - институт ХАДИ.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
       if (!isTableExists(db)) {
            String sql = "CREATE TABLE IF NOT EXISTS Sightseeings (\n"
                    + "    id integer PRIMARY KEY,\n"
                    + "    title text,\n"
                    + "    about text,\n"
                    + "    picture text,\n"
                    + "    latlong text\n"
                    + ");";
            try {
                db.execSQL(sql);
                Sightseeing sightseeingKhadi = new Sightseeing(0,"ХНАДУ","Харківський національний автомобільно-дорожній університет — державний вищий навчальний заклад IV рівня акредитації, підпорядкований Міністерству освіти і науки України, розташований у Харкові. Має 6 гуртожитків, базу відпочинку в Криму. У цей час університет здійснює підготовку фахівців з 9 напрямів, 17 спеціальностям і 25 спеціалізаціям, а також перепідготовку і підвищення їх кваліфікації, включаючи надання другої вищої освіти. Діють екстернат, магістратура, аспірантура і докторантура.\n" +
                        "\n" +
                        "У структурі університету 11 факультетів, 32 кафедри, регіональний ліцей, центр міжнародного утворення і співпраці, кафедра військової підготовки. Видалені факультети ХНАДУ в м. Сімферополі і в м. Херсоні забезпечують очно-заочну форми навчання. Навчально-консультаційні пункти і центри ХНАДУ, розташовані в Нікополе, Кривому Розі, Кременчуці, Полтаві, Куп'янську та Донецьку, є структурними підрозділами факультету заочного навчання і забезпечують підготовку фахівців з переліку спеціальностей згідно з існуючою ліцензією. Головний навчально-лабораторний корпус (вул. Ярослава Мудрого, 25). В ньому розміщені адміністрація і підрозділи, які забезпечують функціонування університету; факультети — автомобільний, транспортних систем; центри — заочного навчання, підвищення кваліфікації та індивідуальної післядипломної освіти, аудиторії, лабораторії, кабінети та комп'ютерні класи.\n" +
                        "\n" +
                        "Навчально-лабораторний корпус дорожньо-будівельного факультету. В ньому розміщені кафедри, аудиторії, лабораторії факультету, регіональний ліцей, видавництво, лабораторія швидкісних автомобілів, центр професійно-технічної та довузівської підготовки.\n" +
                        "\n" +
                        "Навчально-лабораторні корпуси механічного факультету та факультету управління та бізнесу, полігон автомобільної і дорожньої техніки (519 м.р. м. Харкова).\n" +
                        "\n" +
                        "Навчально-лабораторний корпус факультету мехатроніки транспортних засобів, навчальна лабораторія діагностики автомобільного транспорту (вул. Пушкінська, 106).\n" +
                        "\n" +
                        "Науково-технічна бібліотека університету. До послуг студентів, викладачів та науковців університету науково-технічна бібліотека університету, яка входить в шістку найкращих вузівських бібліотек України. Сьогодні бібліотека має сучасну комп'ютерну техніку, локальну мережу, електронні бази даних, Internet, а відвідувачі мають доступ до матеріалів електронних бібліотек світу. Бібліотечний фонд — 480401 екземплярів, якими користуються більш ніж 13 тисяч читачів. Сім пунктів обслуговування знаходяться за межами головного навчального корпусу. Це справжній бібліотечний городок з розгалуженою системою обслуговування.\n" +
                        "\n" +
                        "Студмістечко університету. Для проживання студентів ХНАДУ організоване студмістечко, що нараховує 7 гуртожитків. На території студмістечка, де мешкають понад три тисячі студентів, молодих викладачів, співробітників університету створена власна інфраструктура, що забезпечує гідні умови життя для його мешканців.",
                        null,
                        new LatLng(50.00596754847798, 36.243501864373684));

                Sightseeing sightseeingUkrain = new Sightseeing(1,"Україна","Украї́на — держава, розташована в Східній та частково в Центральній Європі, у південно-західній частині Східноєвропейської рівнини. Площа становить 603 628 км². Найбільша за площею країна з тих, чия територія повністю лежить у Європі.[4] Межує з Білоруссю на півночі, Польщею, Словаччиною та Угорщиною — на заході, Румунією та Молдовою — на південному заході, Росією на сході і північному сході. На півдні і південному сході омивається Чорним і Азовським морями. " +
                        "\n" +
                        "На сучасній території України відомі поселення багатьох археологічних культур, починаючи з доби палеоліту — мустьєрської, гребениківської, кукрецької, трипільської, середньостогівської, ямної, бойових сокир, чорноліської тощо. В античні часи на території України виникли державні утворення скіфів, давньогрецьких колоністів, готів, але відправним пунктом української словянської державності й культури вважається Київська Русь IX—XIII століть[5]. Після монгольської навали її спадкоємцем стало Руське королівство XIII—XIV століття[5]. Воно було поглинуте сусідніми Литвою та Польщею, обєднаними з XVI століття у федеративну Річ Посполиту.",
                        null,
                        new LatLng(49.89511733195786,35.72994936257601));


                Sightseeing sightseeingFrance = new Sightseeing(2,"Франція","Фра́нція, офіційна назва Францу́зька Респу́бліка — держава на заході Європи, республіка, що межує на північному сході з Бельгією, Люксембургом і Німеччиною, на сході з Німеччиною, Швейцарією, південному заході з Іспанією й Андоррою, на південному сході з Італією та Монако на півдні омивається Середземним морем, на заході — Атлантичним океаном.",
                        null,
                        new LatLng(47.013477819470076,2.495272122323513));

                Sightseeing sightseeingGermany= new Sightseeing(3,"Німеччина","Німе́ччина — країна в Центральній Європі, демократична федеративна республіка. Складається з 16 федеральних земель. Столиця і найбільше місто країни — Берлін. Займає площу 357 578 км². Клімат помірний сезонний. Населення — 82,9 млн осіб. Член Європейського Союзу та НАТО. Країна з найбільшою кількістю населення і найбільшим рівнем економіки в Європейському союзі. Провідна політична сила на Європейському континенті, технологічний лідер в багатьох галузях. Німеччина є другим за популярністю місцем міграції в світі, поступаючись тільки США.",
                        null,
                        new LatLng(51.13150928814649,10.191471576690674));

                this.addRecord(db,sightseeingKhadi);
                this.addRecord(db,sightseeingUkrain);
                this.addRecord(db,sightseeingFrance);
                this.addRecord(db,sightseeingGermany);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Удаляет таблицу и создает новую.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Сохраняет данные в таблицу.
     *
     * @param sightseeing
     * @return
     */
    private ContentValues getValues(Sightseeing sightseeing)
    {
        ContentValues values = new ContentValues();
        if (sightseeing.getTitle()!= null)
            values.put("title",sightseeing.getTitle());
        if (sightseeing.getAbout()!= null)
            values.put("about",sightseeing.getAbout());
        if (sightseeing.getPicture()!= null)
            values.put("picture",sightseeing.getPicture().toString());
        if (sightseeing.getAbout()!= null)
            values.put("latlong",sightseeing.getCoordinates().toString());
        return values;
    }

    /**
     * Проверяет, создана ли таблица.
     *
     * @param db
     * @return
     */
    public boolean isTableExists(SQLiteDatabase db) {
        try {
            String query = "SELECT * FROM Sightseeings";
            try (Cursor cursor = db.rawQuery(query, null)) {
                if (cursor != null) {
                    if (cursor.getCount() >= 0) {
                        return true;
                    }
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Возвращает, айди элемента в базе данных.
     *
     * @param db
     * @return
     */
    public int getRecordId(SQLiteDatabase db, int position) {
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM Sightseeings", null);

            if(cursor!=null) {

                if (cursor.moveToPosition(position)) {
                    return Integer.parseInt( cursor.getString(0));
                }
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Сохраняет локацию в таблицу.
     *
     * @param db
     * @param sightseeing
     */
    public void addRecord(SQLiteDatabase db, Sightseeing sightseeing) {
        if (isTableExists(db)) {
            ContentValues values = getValues(sightseeing);
            db.insert("Sightseeings", null, values);
        }
    }

    /**
     * Обновляет локацию, сохраненную в таблице.
     *
     * @param db
     * @param sightseeing
     */
    public void updateRecord(SQLiteDatabase db, Sightseeing sightseeing) {
        if (isTableExists(db)) {
            ContentValues values = getValues(sightseeing);
            int i = db.update("Sightseeings", values, "id = " + getRecordId(db,sightseeing.getId()-1), null);
        }
    }

    /**
     * Удаляет локацию из базы данных.
     *
     * @param db
     * @param id
     */
    public void deleteRecord(SQLiteDatabase db, int id) {
        if (isTableExists(db)) {
            db.delete("Sightseeings", "id = " + getRecordId(db,id), null);
        }
    }
}
