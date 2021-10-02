package com.example.project;

public class Upload {

    public String image_title;
    public String date;
    public String hour;
    public String imageUrl;
    public String description;

    public Upload()
    {

    }

    public Upload(String image_title,String date,String hour,String imageUrl,String description)
    {

        this.image_title=image_title;
        this.date=date;
        this.hour=hour;
        this.imageUrl=imageUrl;
        this.description =description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public String getImage_title() {
        return image_title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setImage_title(String image_title) {
        this.image_title = image_title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
