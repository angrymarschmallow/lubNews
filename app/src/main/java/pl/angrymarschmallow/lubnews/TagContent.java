package pl.angrymarschmallow.lubnews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mazek27 on 2016-04-09.
 */
public class TagContent {
    private String name;
    private int news_count;
    private List<News_list> news_list;



    public static class News_list {
        private int id;
        private  String description;
        private  String title;
        private  String url;

        public String getDescription() {
            return description;
        }



        public void setDescription(String description) {
            this.description = description;
        }


        public  String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public  String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public List<News_list> getNews_list() {
        return news_list;
    }

    public void setNews_list(List<News_list> news_list) {
        this.news_list = news_list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNews_count() {
        return news_count;
    }

    public void setNews_count(int news_count) {
        this.news_count = news_count;
    }
}

