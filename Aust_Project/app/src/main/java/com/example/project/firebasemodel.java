package com.example.project;

public class firebasemodel {

        private String title;
        private String content;
        private  String bookmark;
        private String date;
        private String time;


        public  firebasemodel()
        {

        }
        public firebasemodel(String title,String content,String bookmark,String date,String time)
        {
            this.title =title;
            this.content =content;
            this.bookmark = bookmark;
            this.date = date;
            this.time = time;

        }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getBookmark() {
        return bookmark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
