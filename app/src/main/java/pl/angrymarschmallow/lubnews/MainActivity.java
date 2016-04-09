package pl.angrymarschmallow.lubnews;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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

public class MainActivity extends AppCompatActivity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.listView);

        List<Tag> array_list;


        try {

            array_list = new HttpAsyncTask().execute("http://briefler-bodolsog.rhcloud.com/api/test/").get();
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

    private class HttpAsyncTask extends AsyncTask<String, Void, List<Tag>> {
        String data;
        @Override
        protected List<Tag> doInBackground(String... params) {
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
                List<Tag> tags = new ArrayList<>();

                String finalJson = buffer.toString();
                data = buffer.toString();
                JSONObject jsonObject = new JSONObject(finalJson);

                JSONArray parentArray = jsonObject.getJSONArray("tags");
                for(int j =0;   j < parentArray.length(); j++) {
                    Tag tag = new Tag();
                    tag.setName(parentArray.getJSONObject(j).getString("name"));
                    tag.setNews_count(parentArray.getJSONObject(j).getInt("news_count"));

                    JSONArray jsonNewsArray = parentArray.getJSONObject(j).getJSONArray("news_list");
                    List<Tag.News_list> news_list = new ArrayList<>();

                    for (int i = 0; i < jsonNewsArray.length(); i++) {
                        Tag.News_list news = new Tag.News_list();
                        news.setDescription(jsonNewsArray.getJSONObject(i).getString("description"));
                        news.setTitle(jsonNewsArray.getJSONObject(i).getString("title"));
                        news.setUrl(jsonNewsArray.getJSONObject(i).getString("url"));
                        news_list.add(news);
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
        protected void onPostExecute(List<Tag> result) {
            super.onPostExecute(result);
        }
    }
}
