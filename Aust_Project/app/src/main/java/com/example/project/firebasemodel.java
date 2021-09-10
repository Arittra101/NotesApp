package com.example.project;

public class firebasemodel {

        private String title;
        private String content;

        public  firebasemodel()
        {

        }
        public firebasemodel(String title,String content)
        {
            this.title =title;
            this.content =content;
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
}
