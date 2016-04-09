package pl.angrymarschmallow.lubnews;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    ListView lv;
    List<TagContent> array_list;
    private RecyclerView mRecyclerView;
    private SimpleCursorAdapter mCursorAdapter;
    private DataBase dataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listView);
        wypelnijListe();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Main22Activity.class);
                startActivity(intent);
            }
        });

        dataBase = new DataBase(this);


        try {
            array_list = new HttpAsyncTask().execute("http://briefler-bodolsog.rhcloud.com/api").get();
            List<String> array_string = new ArrayList<>();

            for(int i= 0; i < array_list.size() ; i++){
                array_string.add(array_list.get(i).getName());
            }


            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    array_string
            );

            lv.setAdapter(arrayAdapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        Intent intent = new Intent(this, Main22Activity.class);
//        startActivity(intent);
    }

    private void wypelnijListe(){
        getLoaderManager().initLoader(0, null, (android.app.LoaderManager.LoaderCallbacks<Cursor>) this);

        String[] mapujZ = new String[] {DataBase.KOLUMNA1, DataBase.KOLUMNA2,DataBase.KOLUMNA3,DataBase.KOLUMNA4,DataBase.KOLUMNA5};
        int[] mapujDo = new int[]{R.id.listView };

        mCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.fragment_item, null, mapujZ, mapujDo, 0);

        lv.setAdapter(mCursorAdapter);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projekcja = {DataBase.KOLUMNA1, DataBase.KOLUMNA2,DataBase.KOLUMNA3,DataBase.KOLUMNA4,DataBase.KOLUMNA5};
        CursorLoader cursorLoader = new CursorLoader(this ,DataValues.URI_ZAWARTOSCI, projekcja, null, null ,null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, List<TagContent>> {
        String data;
        @Override
        protected List<TagContent> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try{
                URL link = new URL(params[0]);
                connection = (HttpURLConnection)  link.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";

                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }
                List<TagContent> tags = new ArrayList<>();

                String finalJson = buffer.toString();
                data = buffer.toString();
                JSONObject jsonObject = new JSONObject(finalJson);

                JSONArray parentArray = jsonObject.getJSONArray("tags");
                for(int j =0;   j < parentArray.length(); j++) {
                    TagContent tag = new TagContent();
                    tag.setName(parentArray.getJSONObject(j).getString("name"));
                    tag.setNews_count(parentArray.getJSONObject(j).getInt("news_count"));

                    JSONArray jsonNewsArray = parentArray.getJSONObject(j).getJSONArray("news_list");
                    List<TagContent.News_list> news_list = new ArrayList<>();
                    for (int i = 0; i < jsonNewsArray.length(); i++) {
                        TagContent.News_list news = new TagContent.News_list();
                        news.setDescription(jsonNewsArray.getJSONObject(i).getString("description"));
                        news.setTitle(jsonNewsArray.getJSONObject(i).getString("title"));
                        news.setUrl(jsonNewsArray.getJSONObject(i).getString("url"));
                        news_list.add(news);

                        dataBase.dodajWartosc(tag.getName(), news.getDescription(), news.getTitle(), news.getUrl(), "0");
                    }
                    tag.setNews_list(news_list);
                    tags.add(tag);

                }

                return tags;
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                if (connection != null) connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<TagContent> result) {
            super.onPostExecute(result);
        }
    }
}
