package pl.angrymarschmallow.lubnews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 09.04.2016.
 */
public class DataBase extends SQLiteOpenHelper{

    public final static int WERSA_BAZY = 3;

    public final static String NAZWA_BAZY = "lubaza.db";
    public final static String NAZWA_TABELI = "components";
    public final static String ID = "_id";
    public final static String KOLUMNA1 = "day";
    public final static String KOLUMNA2 = "month";
    public final static String KOLUMNA3 = "year";
    public final static String KOLUMNA4 = "tag";
    public final static String KOLUMNA5 = "weight";
    public final static String KOLUMNA6 = "readed";
    public final static String TW_BAZY =
            "CREATE TABLE " + NAZWA_TABELI + "(" +
            ID+ " INTEGER PRIMARY KEY, "+
            KOLUMNA1+ " TEXT NOT NULL, " +
            KOLUMNA2+ " TEXT NOT NULL, "+
            KOLUMNA3+ " TEXT NOT NULL, "+
            KOLUMNA4+ " TEXT NOT NULL, " +
            KOLUMNA5+ " TEXT NOT NULL, "+
            KOLUMNA6+ " TEXT NOT NULL );";

    private static final String DROP_TODO_TABLE =
            "DROP TABLE IF EXISTS " + NAZWA_TABELI;

    public final static String NAZWA_TAGTAB = "tag";
    public final static String TAG_ID = "tag_id";
    public final static String TAGKOLUMNA1 = "day";
    public final static String TAGKOLUMNA2 = "month";
    public final static String TAGKOLUMNA3 = "year";
    public final static String TAGKOLUMNA4 = "name";
    public final static String TAGKOLUMNA5 = "desc";
    public final static String TAGKOLUMNA6 = "tag_title";
    public final static String TAGKOLUMNA7 = "url";
    public final static String TAGKOLUMNA8 = "tag_readed";
    public final static String TW_TABELA2 =
            "CREATE TABLE " + NAZWA_TAGTAB + "(" +
            TAG_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TAGKOLUMNA1+ " TEXT NOT NULL, "+
            TAGKOLUMNA2+ " TEXT NOT NULL, "+
            TAGKOLUMNA3+ " TEXT NOT NULL, "+
            TAGKOLUMNA4+ " TEXT NOT NULL, "+
            TAGKOLUMNA5+ " TEXT NOT NULL, "+
            TAGKOLUMNA6+ " TEXT NOT NULL, "+
            TAGKOLUMNA7+ " TEXT NOT NULL, "+
            TAGKOLUMNA8+ " TEXT NOT NULL);";

    private static final String DROP_TODO_TAG_TABLE =
            "DROP TABLE IF EXISTS " + NAZWA_TAGTAB;

    public DataBase(Context context) {
        super(context, NAZWA_BAZY, null, WERSA_BAZY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DROP_TODO_TABLE);
        db.execSQL(DROP_TODO_TAG_TABLE);

        db.execSQL(TW_BAZY);
        db.execSQL(TW_TABELA2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TODO_TABLE);
        db.execSQL(DROP_TODO_TAG_TABLE);

        onCreate(db);
    }

    public void dodajWartosc(int id,String day, String month, String year, String title,String weight, String readed){
        SQLiteDatabase dataBase = getWritableDatabase();
        ContentValues wartosci = new ContentValues();
        wartosci.put(ID, id);
        wartosci.put(KOLUMNA1, day);
        wartosci.put(KOLUMNA2, month);
        wartosci.put(KOLUMNA3, year);
        wartosci.put(KOLUMNA4, title);
        wartosci.put(KOLUMNA5, weight);
        wartosci.put(KOLUMNA6, readed);

        dataBase.insertOrThrow(NAZWA_TABELI, null, wartosci);
    }

    public Cursor getWszystkie(){
        String[] kolumny = { ID, KOLUMNA1, KOLUMNA2, KOLUMNA3, KOLUMNA4, KOLUMNA5, KOLUMNA6 };
        SQLiteDatabase database = getReadableDatabase();
        Cursor kursor = database.query(NAZWA_TABELI, kolumny, null, null, null, null, null);
        return kursor;
    }

    public void dodajTagWartosc(int id,String day, String month, String year, String name, String desc, String tag_title, String url, String readed){
        SQLiteDatabase dataBase = getWritableDatabase();
        ContentValues wartosci = new ContentValues();
        wartosci.put(TAG_ID, id);
        wartosci.put(TAGKOLUMNA1, day);
        wartosci.put(TAGKOLUMNA2, month);
        wartosci.put(TAGKOLUMNA3, year);
        wartosci.put(TAGKOLUMNA4, name);
        wartosci.put(TAGKOLUMNA5, desc);
        wartosci.put(TAGKOLUMNA6, tag_title);
        wartosci.put(TAGKOLUMNA7, url);
        wartosci.put(TAGKOLUMNA8, readed);

        dataBase.insertOrThrow(NAZWA_TAGTAB, null, wartosci);
    }

    public Cursor getTagWszystkie(){
        String[] kolumny = { TAG_ID, TAGKOLUMNA1, TAGKOLUMNA2, TAGKOLUMNA3, TAGKOLUMNA4, TAGKOLUMNA5, TAGKOLUMNA6, TAGKOLUMNA7, TAGKOLUMNA7 };
        SQLiteDatabase database = getReadableDatabase();
        Cursor kursor = database.query(NAZWA_TAGTAB, kolumny, null, null, null, null, null);
        return kursor;
    }

}
