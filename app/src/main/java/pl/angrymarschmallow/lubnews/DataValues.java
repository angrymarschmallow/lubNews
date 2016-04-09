package pl.angrymarschmallow.lubnews;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

/**
 * Created by Admin on 09.04.2016.
 */
public class DataValues extends ContentProvider {
    private DataBase mDataBase;

    private static final String IDENTYFIKATOR = "pl.angrymarschmallow.lubnews";

    public static final Uri URI_ZAWARTOSCI = Uri.parse("content://" + IDENTYFIKATOR + "/"
            + DataBase.NAZWA_TABELI);

    private static final int CALA_TABELA = 1;
    private static final int WYBRANY_WIERSZ = 2;
    private static final UriMatcher sDopasowanieUri = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sDopasowanieUri.addURI(IDENTYFIKATOR, DataBase.NAZWA_TABELI, CALA_TABELA);
        sDopasowanieUri.addURI(IDENTYFIKATOR, DataBase.NAZWA_TABELI + "/#", WYBRANY_WIERSZ);
    }

    @Override
    public boolean onCreate() {
        mDataBase = new DataBase((getContext()));
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int typUri = sDopasowanieUri.match(uri);
        SQLiteDatabase baza = mDataBase.getWritableDatabase();
        Cursor kursor = null;
        switch (typUri){
            case CALA_TABELA:
                    kursor = baza.query(false, DataBase.NAZWA_TABELI, projection, selection, selectionArgs,
                        null, null, sortOrder, null, null);
                break;
            case WYBRANY_WIERSZ:
                    kursor = baza.query(false, DataBase.NAZWA_TABELI, projection, dodajIdDoSelekcji(selection, uri),
                            selectionArgs, null, null, sortOrder, null, null);
                break;
            default:
                throw new IllegalArgumentException("Nieznane URI: " + uri);
        }
        kursor.setNotificationUri(getContext().getContentResolver(), uri);

        return kursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int typUri = sDopasowanieUri.match(uri);
        SQLiteDatabase baza = mDataBase.getWritableDatabase();

        long idDodanego = 0;
        switch (typUri){
            case CALA_TABELA:
                idDodanego = baza.insert(DataBase.NAZWA_TABELI, null, values);
                break;
            default:
                throw  new IllegalArgumentException("Nieznane URI: "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int typUri = sDopasowanieUri.match(uri);
        SQLiteDatabase baza = mDataBase.getWritableDatabase();
        int liczbaUsunietych = 0;
        switch (typUri){
            case CALA_TABELA:
                    liczbaUsunietych = baza.delete(DataBase.NAZWA_TABELI, selection, selectionArgs);
                break;
            case WYBRANY_WIERSZ:
                    liczbaUsunietych = baza.delete(DataBase.NAZWA_TABELI, dodajIdDoSelekcji(selection, uri), selectionArgs);
                break;
            default:
                throw  new IllegalArgumentException("Nieznane URI: "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return liczbaUsunietych;
    }

    private String dodajIdDoSelekcji(String selekcja, Uri uri) {
        if (selekcja != null && !selekcja.equals(""))
            selekcja = selekcja + " and " + DataBase.ID + "=" + uri.getLastPathSegment();
        else
            selekcja = DataBase.ID + "=" + uri.getLastPathSegment();
        return selekcja;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //TODO
        return 0;
    }
}
