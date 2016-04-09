package pl.angrymarschmallow.lubnews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 09.04.2016.
 */
public class DataBase extends SQLiteOpenHelper{

    public final static int WERSA_BAZY = 1;
    public final static String ID = "_id";
    public final static String NAZWA_BAZY = "lubBaza";
    public final static String NAZWA_TABELI = "components";
    public final static String KOLUMNA1 = "name";
    public final static String KOLUMNA2 = "description";
    public final static String KOLUMNA3 = "title";
    public final static String KOLUMNA4 = "url";
    public final static String KOLUMNA5 = "readed";
    public final static String TW_BAZY = "CREATE TABLE" + NAZWA_TABELI + "(" + ID+
            "INTEGER PRIMARY KEY AUTOINCREMENT, "+ KOLUMNA1+ "TEXT NOT NULL, " + KOLUMNA2+
            "TEXT NOT NULL, "+ KOLUMNA3 + "TEXT NOT NULL, "+ KOLUMNA4+ "TEXT NOT NULL, " +
            KOLUMNA5+"BOOLEAN NOT NULL );";



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
    }

    public static String INSERT(String name, String description, String title, String url, int readed ){
        return "INSERT INTO "+NAZWA_TABELI+
                "(" + KOLUMNA1 + ","+ KOLUMNA2 + ","+ KOLUMNA3 + ","+ KOLUMNA4 + ","+ KOLUMNA5 + ") "+
                "VALUES( '" + name +"','"+ description +"','"+ url + "'," +readed +")";


    }
}
