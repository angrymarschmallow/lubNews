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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity{

    TextView lv;
    List<TagContent> array_list;
    private DataBase dataBase;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ArrayList<String>> mDataSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //lv = (TextView) findViewById(R.id.textView);

        dataBase = new DataBase(this);


        int opcja = 2;
        new HttpAsyncTask().execute("http://briefler-bodolsog.rhcloud.com/api/2016-04-09/", opcja+"");

        Cursor cursor = dataBase.getWszystkie();
        //lv.setText(cursor.getCount()+"");

        mDataSet = new ArrayList<>(); //cursor.getCount()][];

        while(cursor.moveToNext()){
            ArrayList<String> c = new ArrayList<String>();
                c.add(cursor.getString(0));
                c.add(cursor.getString(1));
                c.add(cursor.getString(2));
                c.add(cursor.getString(3));
                c.add(cursor.getString(4));
                c.add(cursor.getString(5));
                c.add(cursor.getString(6));
            mDataSet.add(c);
        }


        //lv.setAdapter(arrayAdapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new MyAdapter(mDataSet);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
//        Intent intent = new Intent(this, Main22Activity.class);
//        startActivity(intent);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, Void> {
        String data;
        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL link = new URL(params[0]);
                connection = (HttpURLConnection) link.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject jsonObject = new JSONObject(finalJson);

                JSONObject parentObject = jsonObject.getJSONObject("date_split");
                String day = parentObject.getString("day");
                String month = parentObject.getString("month");
                String year = parentObject.getString("year");

                if (params[1].equals("1")) {
                    int tag_id = jsonObject.getInt("tag_id");
                    String tag_name = jsonObject.getString("tag_name");

                    JSONArray jsonNewsArray = parentObject.getJSONArray("news_list");

                    for (int i = 0; i < jsonNewsArray.length(); i++) {
                        String desc = jsonNewsArray.getJSONObject(i).getString("desc");
                        String img = jsonNewsArray.getJSONObject(i).getString("img");
                        JSONObject source = jsonNewsArray.getJSONObject(i).getJSONObject("source");
                        String title = source.getString("name");
                        String url = source.getString("url");
                        dataBase.dodajTagWartosc(tag_id, day, month, year, tag_name, desc, title, url, "0");
                    }
                } else if (params[1].equals("2")) {

                    parentObject = jsonObject.getJSONObject("tags");

                    JSONArray jsonNewsArray = parentObject.getJSONArray("tags_list");

                    for (int i = 0; i < jsonNewsArray.length(); i++) {

                        int id = jsonNewsArray.getJSONObject(i).getInt("id");
                        String tag = jsonNewsArray.getJSONObject(i).getString("tag");
                        String weight = jsonNewsArray.getJSONObject(i).getString("weight");
                        dataBase.dodajWartosc(id, day, month, year, tag, weight, "0");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (connection != null) connection.disconnect();
            }
            return null;
        }
    }


}
