package com.example.project;

public class MyBookmark {

    private String title;
    private String content;


    public  MyBookmark()
    {

    }
    public MyBookmark(String title,String content)
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
