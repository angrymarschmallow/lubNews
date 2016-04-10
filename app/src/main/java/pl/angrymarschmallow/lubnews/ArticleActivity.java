package pl.angrymarschmallow.lubnews;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ArticleActivity extends AppCompatActivity {

    private DataBase dataBase;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ArrayList<String>> mDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Bundle extras = getIntent().getExtras();

        String tag = extras.getString("tag");

        dataBase = new DataBase(this);

        int opcja = 1;
        new HttpAsyncTask().execute("http://briefler-bodolsog.rhcloud.com/api/2016-04-09/", opcja+"");

        Cursor cursor = dataBase.getTagWszystkie();
        //lv.setText(cursor.getCount()+"");

        mDataSet = new ArrayList<>(); //cursor.getCount()][];

        while(cursor.moveToNext()){
            ArrayList<String> c = new ArrayList<String>();
            //if(cursor.getString(6) == tag){
            c.add(cursor.getString(0));
            c.add(cursor.getString(1));
            c.add(cursor.getString(2));
            c.add(cursor.getString(3));
            c.add(cursor.getString(4));
            c.add(cursor.getString(5));
            c.add(cursor.getString(6));
            c.add(cursor.getString(7));
            c.add(cursor.getString(8));
            mDataSet.add(c);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new MyArticleAdapter(mDataSet);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);


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
