package calllog.example.my_computer.calllog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.listview.Call_info;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Call_info";
    public static final String TABLE_CONTACTS = "Call_records";
    public static final String Number = " Number ";
    public static final String Name = " Name ";
    public static final String Category = " Category ";
    public static final String Duration = " Duration ";
    public static final String Date_time = " Date_time ";
    public static final String Type = " Type ";
    public static final String ID = " ID ";
    public static final String Date_long = " Date_long ";
    public static final String Date = " Date ";
    public static final String Time = " Time ";
    private Context context;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + ID + " INTEGER PRIMARY KEY  AUTOINCREMENT  , " + Number
                + " VARCHAR   , " + Name + " VARCHAR  , " + Category
                + " VARCHAR , " + Type + " VARCHAR   , " + Date_long + " UNSIGNED BIG INT ," + Duration
                + " VARCHAR  , " + Date_time + " DATETIME , " + Date + " DATETIME , " + Time + " DATETIME  " + " ); ";

        try {
            db.execSQL(CREATE_CONTACTS_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addContact(Call_info call_info) {
        SQLiteDatabase db = null;
        try {


            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(Number, call_info.getNumber());
            values.put(Name, call_info.getName());
            values.put(Category, call_info.getCategory());
            values.put(Type, call_info.getType());
            values.put(Date_long, call_info.getDate_long());
            values.put(Duration, call_info.getDuration());
            values.put(Date_time, call_info.getDate_time());
            values.put(Date, call_info.getDate());
            values.put(Time, call_info.getTime());

            db.insert(TABLE_CONTACTS, null, values);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public List<Call_info> get_sql_values(Sql_statements_all a) {

        List<Call_info> contactList = null;

        try {
            contactList = new ArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();
            String sql = Sql_statements_all.get_sql(a);
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();

            do {

                contactList.add(new Call_info(

                        cursor.getString(cursor.getColumnIndexOrThrow("Number")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Date_time")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("Date_long")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Duration"))));

            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            return contactList;

        } catch (Exception e) {


            return contactList;
        }
    }

    public List<Call_info> get_Date(String a) {

        List<Call_info> contactList = null;

        try {
            contactList = new ArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();

            String sql = " SELECT * FROM " + TABLE_CONTACTS + " WHERE " + Date + " > " + String.format(" '%s' ", a);
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();

            do {

                contactList.add(new Call_info(

                        cursor.getString(cursor.getColumnIndexOrThrow("Number")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Date_time")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("Date_long")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Duration"))));

            } while (cursor.moveToNext());
            db.close();
            return contactList;

        } catch (Exception e) {


            return contactList;
        }
    }

    public List<Call_info> get_sql_values(Sql_statements_selected a, String number) {

        List<Call_info> contactList = null;

        try {
            contactList = new ArrayList<Call_info>();

            SQLiteDatabase db = this.getWritableDatabase();
            String sql = Sql_statements_selected.get_sql(a, number);
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();

            do {

                contactList.add(new Call_info(

                        cursor.getString(cursor.getColumnIndexOrThrow("Number")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Date_time")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("Date_long")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Duration"))));

            } while (cursor.moveToNext());
            cursor.close();
            db.close();
            return contactList;

        } catch (Exception e) {
            return contactList;
        }
    }

    enum Sql_statements_all {

        Select_Distinct, Incoming_call_sql, Outgoing_call_sql, Incoming_sms_sql,
        Outgoing_sms_sql, Missed_calls_sql, Rejected_calls_sql;


        public static String get_sql(Sql_statements_all h) {

            switch (h) {

                case Select_Distinct:

                    return " SELECT  * , max(Date_long)  FROM   Call_records GROUP BY Number  ORDER BY  Date desc ";

                case Incoming_call_sql:

                    return "SELECT * FROM   Call_records  WHERE  Type  = 'Incoming'  AND   Category  = 'Call' GROUP BY  Number  ORDER BY + Date_time  desc ; ";


                case Outgoing_call_sql:
                    return " SELECT * FROM   Call_records  WHERE  Type  = 'Outgoing'  AND   Category  = 'Call' GROUP BY  Number  ORDER BY + Date_time  desc ; ";

                case Incoming_sms_sql:
                    return " SELECT * FROM   Call_records  WHERE  Type  = 'Incoming'  AND   Category  = 'Sms' GROUP BY  Number  ORDER BY + Date_time  desc ; ";

                case Outgoing_sms_sql:

                    return
                            "SELECT * FROM   Call_records  WHERE  Type  = 'Outgoing'  AND   Category  = 'Sms' GROUP BY  Number  ORDER BY + Date_time  desc ;  ";

                case Missed_calls_sql:

                    return
                            "SELECT * FROM   Call_records  WHERE  Type  = 'Missed'  AND   Category  = 'Call' GROUP BY  Number  ORDER BY + Date_time  desc ;";
                case Rejected_calls_sql:

                    return
                            "SELECT * FROM   Call_records  WHERE  Type  = 'Rejected'  AND   Category  = 'Call' GROUP BY  Number  ORDER BY + Date_time  desc ;";
            }
            return null;
        }
    }

    enum Sql_statements_selected {

        Select_all, Incoming_call_sql, Outgoing_call_sql, Incoming_sms_sql, Outgoing_sms_sql;


        public static String get_sql(Sql_statements_selected h, String Val) {

            switch (h) {

                case Select_all:


                    return
                            " SELECT * FROM   Call_records "
                                    + "  WHERE   Number =  " + String.format("'%s'", Val) +

                                    " ORDER BY  Date_time  DESC ";


                case Incoming_call_sql:

                    return
                            " SELECT * FROM   Call_records "
                                    + "  WHERE   Number =  " + String.format("'%s'", Val) +
                                    " AND Type   = 'Incoming'  AND  Category  = 'Call' " +
                                    " ORDER BY  Date_time  DESC ";


                case Outgoing_call_sql:

                    return
                            " SELECT * FROM   Call_records "
                                    + "  WHERE   Number =  " + String.format("'%s'", Val) +
                                    " AND Type   = 'Outgoing'  AND  Category  = 'Call' " +
                                    " ORDER BY  Date_time  DESC ";


                case Incoming_sms_sql:


                    return
                            " SELECT * FROM   Call_records "
                                    + "  WHERE   Number =  " + String.format("'%s'", Val) +
                                    " AND Type   = 'Incoming'  AND  Category  = 'Sms' " +
                                    " ORDER BY  Date_time  DESC ";


                case Outgoing_sms_sql:

                    return
                            " SELECT * FROM   Call_records "
                                    + "  WHERE   Number =  " + String.format("'%s'", Val) +
                                    " AND Type   = 'Outgoing'  AND  Category  = 'Sms' " +
                                    " ORDER BY  Date_time  DESC ";


            }
            return null;
        }
    }

    public List<Call_info> get_All_Call_info() {

        List<Call_info> contactList = null;

        try {
            contactList = new ArrayList<>();

            SQLiteDatabase db = this.getWritableDatabase();
            String sql_statement = "SELECT * FROM  " + TABLE_CONTACTS;
            Cursor cursor = db.rawQuery(sql_statement, null);
            cursor.moveToFirst();

            do {


                contactList.add(new Call_info(

                        cursor.getString(cursor.getColumnIndexOrThrow("Number")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Date_time")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("Date_long")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Duration"))));

            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            return contactList;

        } catch (Exception e) {
            return contactList;
        }
    }


    public List<Call_info> getWhereVal(String Val) {

        List<Call_info> contactList = null;

        try {
            contactList = new ArrayList<>();

            String sql_statement = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + Number +
                    " = " + String.format("'%s'", Val);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql_statement, null);
            cursor.moveToFirst();

//            do {
//
//                contactList.add(new Call_info(cursor.getString(0), cursor.getString(1),
//                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
//
//            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            return contactList;

        } catch (Exception e) {

            e.printStackTrace();
            return contactList;
        }
    }


    public List<Call_info> get_New_callinfo(String Val) {

        List<Call_info> contactList = null;

        try {
            contactList = new ArrayList<>();

            String sql_statement = " Select max(Date_time) from call_records";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql_statement, null);
            cursor.moveToFirst();

//            do {
//
//                contactList.add(new Call_info(cursor.getString(0), cursor.getString(1),
//                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
//
//            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            return contactList;

        } catch (Exception e) {

            e.printStackTrace();
            return contactList;
        }
    }

    public List<Call_info> getSearchVal(String Val) {

        List<Call_info> contactList = null;

        try {
            contactList = new ArrayList<Call_info>();

            String sql_statement = "SELECT * FROM " + TABLE_CONTACTS + " WHERE "
                    + Name + " LIKE " + String.format("'%s%s'", Val, "%") + " OR " +
                    Number + " LIKE " + String.format("'%s%s'", Val, "%") +
                    " GROUP BY "
                    + Number;


            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql_statement, null);
            cursor.moveToFirst();

            do {

                contactList.add(new Call_info(

                        cursor.getString(cursor.getColumnIndexOrThrow("Number")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Category")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Type")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Date_time")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("Date_long")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Time")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Duration"))));

            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            return contactList;

        } catch (Exception e) {

            e.printStackTrace();
            return contactList;
        }
    }

    public long getMaxDate() {

        ArrayList<Call_info> contactList = null;
        long max_date = 0;
        try {


            String sql_statement = " SELECT    max(Date_long) FROM Call_records ";
//            SELECT   * , max(Date_long)  FROM   Call_records Group by Number  ORDER BY  Date desc  ;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql_statement, null);
            cursor.moveToFirst();

            contactList = new ArrayList<>();
            do {


                max_date = cursor.getLong(0);

            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            return max_date;

        } catch (Exception e) {

            e.printStackTrace();
            return max_date;
        }
    }


    public int deleteContact(String Val, String Val1, String Val2) {
        int delete = 0;
        SQLiteDatabase db = null;
        try {

            db = this.getWritableDatabase();

            delete = db.delete(

                    TABLE_CONTACTS, Number + " = ?  AND " + Date + " = ?  AND" + Time + " = ?",
                    new String[]{String.valueOf(Val), String.valueOf(Val1), String.valueOf(Val2)});

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }


        return delete;
    }


//    public int deleteContact_sort(String val) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int delete = db.delete(TABLE_CONTACTS, PHONE_NUMBER + " = ?",
//                new String[]{String.valueOf(val)});
//
//
//        db.close();
//        return delete;
//    }


    //            String sql_statement = " SELECT " + Number + " , " + Name + " FROM " + TABLE_CONTACTS + " GROUP BY "
//                    + Number + " ORDER BY " + Date + " DESC  ";
//
//
//            String Incoming_call_sql = " SELECT " + Number + " , " + Name + " FROM " + TABLE_CONTACTS
//                    + " WHERE " + Type + "  = 'INCOMING'  AND " + Category + " = 'Call' " + "GROUP BY"
//                    + Number + " ORDER BY " + Date + " DESC  ";
//
//            String Outgoing_call_sql = " SELECT " + Number + " , " + Name + " FROM " + TABLE_CONTACTS
//                    + " WHERE " + Type + "  = 'OUTGOING'  AND " + Category + " = 'Call' " + "GROUP BY"
//                    + Number + " ORDER BY " + Date + " DESC  ";
//
//
//            String Outgoing_sms_sql = " SELECT " + Number + " , " + Name + " FROM " + TABLE_CONTACTS
//                    + " WHERE " + Type + "  = 'OUTGOING'  AND " + Category + " = 'Sms' " + "GROUP BY"
//                    + Number + " ORDER BY " + Date + " DESC  ";
//
//            String sql_statement2 = " SELECT " + Number + " , " + Name + " FROM " + TABLE_CONTACTS
//
//                    + " WHERE " + Type + " = ' MISSED ' " +
//                    "GROUP BY"
//                    + Number + " ORDER BY " + Date + " DESC  ";


//              if (s6.equals("1")) {
//                Type = "INCOMING";
//
//            } else if (s6.equals("2")) {
//                Type = "OUTGOING";
//
//            } else if (s6.equals("3")) {
//                Type = "MISSED";
//
//            } else if (s6.equals("4")) {
//                Type = "Voice_Mail";
//
//            } else if (s6.equals("5")) {
//                Type = "Rejected";
//
//            } else if (s6.equals("6")) {
//                Type = "Refused_List";
//
//            }
//            if (s8.equals("100")) {
//                Category = "Call";
//
//            } else if (s8.equals("200")) {
//                Category = "Voice";
//
//            } else if (s8.equals("300")) {
//                Category = "Sms";
//
//            }
}