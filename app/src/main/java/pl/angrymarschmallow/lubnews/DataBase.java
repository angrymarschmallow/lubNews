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

    public final static int WERSA_BAZY = 2;
    public final static String ID = "_id";
    public final static String NAZWA_BAZY = "lubBaza";
    public final static String NAZWA_TABELI = "components";
    public final static String KOLUMNA1 = "name";
    public final static String KOLUMNA2 = "description";
    public final static String KOLUMNA3 = "title";
    public final static String KOLUMNA4 = "url";
    public final static String KOLUMNA5 = "readed";
    public final static String TW_BAZY = "CREATE TABLE " + NAZWA_TABELI + "(" + ID+
            " INTEGER PRIMARY KEY AUTOINCREMENT, "+ KOLUMNA1+ " TEXT NOT NULL, " + KOLUMNA2+
            " TEXT NOT NULL, "+ KOLUMNA3 + " TEXT NOT NULL, "+ KOLUMNA4+ " TEXT NOT NULL, " +
            KOLUMNA5+" BOOLEAN NOT NULL );";
    private static final String DROP_TODO_TABLE =
            "DROP TABLE IF EXISTS " + NAZWA_TABELI;



    public DataBase(Context context) {
        super(context, NAZWA_BAZY, null, WERSA_BAZY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO create database

        db.execSQL(TW_BAZY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO upgrade database
        db.execSQL(DROP_TODO_TABLE);

        onCreate(db);
    }

    public void dodajWartosc(String name, String descryption, String title, String url, String readed){
        SQLiteDatabase dataBase = getWritableDatabase();
                ContentValues wartosci = new ContentValues();
                wartosci.put(KOLUMNA1, name);
                wartosci.put(KOLUMNA2, descryption);
                wartosci.put(KOLUMNA3, title);
                wartosci.put(KOLUMNA4, url);
                wartosci.put(KOLUMNA5, readed);
        dataBase.insertOrThrow(NAZWA_TABELI, null, wartosci);
    }

    public Cursor dajWszystkie(){
        String[] kolumny = {KOLUMNA1, KOLUMNA2, KOLUMNA3, KOLUMNA4, KOLUMNA5 };
        SQLiteDatabase database = getReadableDatabase();
        Cursor kursor = database.query(NAZWA_TABELI, kolumny, null, null, null, null, null);
        return kursor;
    }
}
