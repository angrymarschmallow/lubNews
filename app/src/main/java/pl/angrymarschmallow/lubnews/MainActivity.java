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
    private String[][] mDataSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //lv = (TextView) findViewById(R.id.textView);

        dataBase = new DataBase(this);


        try {
            if(dataBase.dajWszystkie() == null)
            array_list = new HttpAsyncTask().execute("http://briefler-bodolsog.rhcloud.com/api/2016-04-09/").get();

            Cursor cursor = dataBase.dajWszystkie();
            //lv.setText(cursor.getCount()+"");

            mDataSet = new String[cursor.getCount()- 1][5];
            int licznik = 0;

            while(cursor.moveToNext()){

                mDataSet[licznik][0] = cursor.getString(0);
                mDataSet[licznik][1] = cursor.getString(1);
                mDataSet[licznik][2] = cursor.getString(2);
                mDataSet[licznik][3] = cursor.getString(3);
                mDataSet[licznik][4] = cursor.getString(4);
                licznik++;
            }



            //lv.setAdapter(arrayAdapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(mDataSet);
        mRecyclerView.setAdapter(mAdapter);
//        Intent intent = new Intent(this, Main22Activity.class);
//        startActivity(intent);
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

                JSONObject parentObject = jsonObject.getJSONObject("tags");
                //for(int j =0;   j < parentArray.length(); j++) {
                    TagContent tag = new TagContent();
//                    tag.setName(parentArray.getJSONObject(j).getString("name"));
//                    tag.setNews_count(parentArray.getJSONObject(j).getInt("news_count"));

                    JSONArray jsonNewsArray = parentObject.getJSONArray("tags_list");
                    List<TagContent.News_list> news_list = new ArrayList<>();
                    for (int i = 0; i < jsonNewsArray.length(); i++) {
                        TagContent.News_list news = new TagContent.News_list();
                        news.setDescription((jsonNewsArray.getJSONObject(i).getInt("id")+""));
                        news.setTitle(jsonNewsArray.getJSONObject(i).getString("tag"));
                        news.setUrl(jsonNewsArray.getJSONObject(i).getInt("weight")+"");
                        news_list.add(news);
                        Log.d("id: ", news.getDescription());
                        dataBase.dodajWartosc("name", news.getDescription(), news.getTitle(), news.getUrl(), "0");
                    }
                    tag.setNews_list(news_list);
                    tags.add(tag);
                //}

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
