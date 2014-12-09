package calllog.example.my_computer.calllog;

public class Call_info {


    private String Number, Name, Date, Time, Duration, Category, Type;

    public Call_info(String Number, String Name, String Category, String Type , String Date, String Time, String Duration) {


        this.Number = Number;
        this.Name = Name;
        this.Date = Date;
        this.Time = Time;
        this.Duration = Duration;
        this.Category = Category;
        this.Type = Type;


    }

    public Call_info() {

    }

    public Call_info(String Number, String Name) {

        this.Number = Number;
        this.Name = Name;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
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

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}




