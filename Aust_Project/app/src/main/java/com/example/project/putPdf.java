package com.example.project;

public class putPdf {
    String name;
    String url;
    String time;

    putPdf()
    {

    }
    putPdf(String name,String url,String time){

        this.name =  name;
        this.url =  url;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
