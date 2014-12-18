package com.listview;

public class Call_info {

    private String Number;
    private String Name;
    private String Date;
    private String Time;
    private String Duration;
    private String Category;
    private String Type;
    private String Date_time;
    private Long Date_long;

    public Call_info(String Number, String Name, String Category, String Type, String datetime, Long Date_long,
                     String Date, String Time,
                     String Duration) {

        this.Number = Number;
        this.Name = Name;
        this.Date_long = Date_long;
        this.Date = Date;
        this.Time = Time;
        this.Duration = Duration;
        this.Category = Category;
        this.Type = Type;
        this.Date_time = datetime;

    }

    public Call_info() {
    }

    public Long getDate_long() {
        return Date_long;
    }

    public void setDate_long(Long date_long) {
        Date_long = date_long;
    }

    public String getDate_time() {
        return Date_time;
    }

    public void setDate_time(String date_time) {
        Date_time = date_time;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String log_Type) {

        Category = log_Type;

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}




