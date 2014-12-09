package calllog.example.my_computer.calllog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Call_info";
    public static final String TABLE_CONTACTS = "Call_records";
    public static final String NUMBER = " NUMBER ";
    public static final String ID= " ID ";
    public static final String NAME = " NAME ";
    public static final String Date = " Date ";
    public static final String Time = " Time ";
    public static final String Category = " Category ";
    public static final String Duration = " Duration ";
    public static final String Type = " Type ";
    private Context context;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + NUMBER + " VARCHAR ," + NAME + " VARCHAR , " + Category
                + " VARCHAR , " + Type + " VARCHAR ," + Date + " VARCHAR ," + Time +
                " VARCHAR , " + Duration + " VARCHAR " + ");";


        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addContact(Call_info call_info) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NUMBER, call_info.getNumber());
            values.put(NAME, call_info.getName());
            values.put(Category, call_info.getCategory());
            values.put(Type, call_info.getType());
            values.put(Date, call_info.getDate());
            values.put(Time, call_info.getTime());
            values.put(Duration, call_info.getDuration());
            db.insert(TABLE_CONTACTS, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public Contact getContact(String a) {
//
//
//        try {
//
//            SQLiteDatabase db = this.getReadableDatabase();
//
//            Cursor cursor = db.query(TABLE_CONTACTS, new String[]{
//                         Category,Type, Time,Date,Duration },
//                    NUMBER + "= ?", new String[]{String.valueOf(a)},
//                    null, null, null, null);
//            cursor.moveToFirst();
//
//            do {
//
//                contact.set_phoneNumber(cursor.getString(0));
//                contact.set_Name(cursor.getString(1));
//                contact.set_is_Call_block(stringToBool(cursor.getString(2)));
//                contact.set_is_Msg_block(stringToBool(cursor.getString(3)));
//                contact.setPhoto(cursor.getString(4));
//
//            } while (cursor.moveToNext());
//
//            cursor.close();
//            db.close();
//            return contact;
//
//        } catch (Exception e) {
//
//            return contact;
//        }
//
//
//    }
//
//
//    public List<Contact> Sort_By(String val) {
//
//        List<Contact> contactList = null;
//        try {
//
//
//            SQLiteDatabase db = this.getReadableDatabase();
//            contactList = new ArrayList<Contact>();
//
//            String sql_statement = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + val + " = " + " 1 ";
//            Cursor cursor = db.rawQuery(sql_statement, null);
//            cursor.moveToFirst();
//
//            do {
//                Contact contact = new Contact();
//                contact.set_phoneNumber(cursor.getString(0));
//                contact.set_Name(cursor.getString(1));
//                contact.set_is_Call_block(stringToBool(cursor.getString(2)));
//                contact.set_is_Msg_block(stringToBool(cursor.getString(3)));
//                contact.setPhoto(cursor.getString(4));
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//
//            cursor.close();
//            db.close();
//            return contactList;
//        } catch (Exception e) {
//
//            String message = e.getMessage();
//
//            return contactList;
//        }
//
//
//    }

    public List<Call_info> get_sql_values(Sql_statements_all a) {

        List<Call_info> contactList = null;

        try {
            contactList = new ArrayList<Call_info>();

            SQLiteDatabase db = this.getWritableDatabase();
            String sql = Sql_statements_all.get_sql(a);
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();

            do {

                contactList.add(
                        new Call_info(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                                cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));

            } while (cursor.moveToNext());

            cursor.close();
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

                contactList.add(
                        new Call_info(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                                cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));

            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            return contactList;

        } catch (Exception e) {


            return contactList;
        }
    }

    enum Sql_statements_all {

        Select_Distinct, Incoming_call_sql, Outgoing_call_sql, Incoming_sms_sql, Outgoing_sms_sql ,Miss_calls_sql;


        public static String get_sql(Sql_statements_all h) {

            switch (h) {

                case Select_Distinct:

                    return " SELECT * FROM " + TABLE_CONTACTS +
                            " ORDER BY " + Date + " DESC  ";

//                return " SELECT " + NUMBER + " , " + NAME + " FROM " + TABLE_CONTACTS + " GROUP BY "
//                        + NUMBER + " ORDER BY " + Date + " DESC  ";

                case Incoming_call_sql:
                    return " SELECT * FROM " + TABLE_CONTACTS
                            + " WHERE " + Type + "  = 'INCOMING'  AND " + Category + " = 'Call' " + "GROUP BY"
                            + NUMBER + " ORDER BY " + Date + " DESC  ";


                case Outgoing_call_sql:
                    return " SELECT * FROM " + TABLE_CONTACTS
                            + " WHERE " + Type + "  = 'OUTGOING'  AND " + Category + " = 'Call' " + "GROUP BY"
                            + NUMBER + " ORDER BY " + Date + " DESC  ";

                case Incoming_sms_sql:
                    return " SELECT * FROM " + TABLE_CONTACTS
                            + " WHERE " + Type + "  = 'INCOMING'  AND " + Category + " = 'Sms' " + "GROUP BY"
                            + NUMBER + " ORDER BY " + Date + " DESC  ";

                case Outgoing_sms_sql:

                    return
                            " SELECT * FROM " + TABLE_CONTACTS
                                    + " WHERE " + Type + "  = 'OUTGOING'  AND " + Category + " = 'Sms' " + "GROUP BY"
                                    + NUMBER + " ORDER BY " + Date + " DESC  ";


            }
            return null;
        }
    }

    enum Sql_statements_selected {

        Select_all, Incoming_call_sql, Outgoing_call_sql, Incoming_sms_sql, Outgoing_sms_sql;


        public static String get_sql(Sql_statements_selected h, String Val) {

            switch (h) {

                case Select_all:

                    return " SELECT * FROM " + TABLE_CONTACTS +
                            " WHERE   " + NUMBER + " = " + String.format("'%s'", Val) +
                            " ORDER BY " + Date + " DESC  ";
//                return " SELECT " + NUMBER + " , " + NAME + " FROM " + TABLE_CONTACTS + " GROUP BY "
//                        + NUMBER + " ORDER BY " + Date + " DESC  ";

                case Incoming_call_sql:

                    return " SELECT * FROM " + TABLE_CONTACTS
                            + " WHERE   " + NUMBER + " = " + String.format("'%s'", Val) + " AND "
                            + Type + "  = 'INCOMING'  AND " + Category + " = 'Call' " + " ORDER BY " + Date + " DESC  ";


                case Outgoing_call_sql:
                    return " SELECT * FROM " + TABLE_CONTACTS
                            + " WHERE " + NUMBER + " = " + String.format("'%s'", Val) + " AND " + Type + "  = 'OUTGOING'  AND " + Category + " = 'Call' "
                            + " ORDER BY " + Date + " DESC  ";

                case Incoming_sms_sql:
                    return " SELECT * FROM " + TABLE_CONTACTS
                            + " WHERE " + NUMBER + " = " + String.format("'%s'", Val) + " AND " + Type + "  = 'INCOMING'  AND " + Category + " = 'Sms' " + " ORDER BY " + Date + " DESC  ";

                case Outgoing_sms_sql:

                    return
                            " SELECT * FROM " + TABLE_CONTACTS
                                    + " WHERE " + NUMBER + " = " + String.format("'%s'", Val) + " AND " + Type + "  = 'OUTGOING'  AND " + Category + " = 'Sms' " + " ORDER BY " + Date + " DESC  ";


            }
            return null;
        }
    }

    public List<Call_info> get_All_Call_info() {

        List<Call_info> contactList = null;

        try {
            contactList = new ArrayList<Call_info>();

            SQLiteDatabase db = this.getWritableDatabase();
            String sql_statement = "SELECT * FROM  " + TABLE_CONTACTS;

            Cursor cursor = db.rawQuery(sql_statement, null);
            cursor.moveToFirst();

            do {
                contactList.add(new Call_info(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));


            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            return contactList;

        } catch (Exception e) {


            return contactList;
        }
    }

    public List<Call_info> get_First_row() {

        List<Call_info> contactList = null;

        try {
            contactList = new ArrayList<Call_info>();

            SQLiteDatabase db = this.getWritableDatabase();
            String sql_statement = " SELECT  * FROM " + TABLE_CONTACTS + " LIMIT 1";
            Cursor cursor = db.rawQuery(sql_statement, null);
            cursor.moveToFirst();

            do {
                contactList.add(new Call_info(
                        cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));


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
            contactList = new ArrayList<Call_info>();

            String sql_statement = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + NUMBER + " = " + String.format("'%s'", Val);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql_statement, null);
            cursor.moveToFirst();

            do {
                String string = cursor.getString(0);
                String string1 = cursor.getString(1);
                String string2 = cursor.getString(2);
                String string3 = cursor.getString(3);
                String string4 = cursor.getString(4);
                String string5 = cursor.getString(5);
                String string6 = cursor.getString(6);

                contactList.add(new Call_info(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));

            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            return contactList;

        } catch (Exception e) {

            e.printStackTrace();
            return contactList;
        }
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    //
//    public boolean updateContact(Contact contact) {
//        try {
//
//
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(PHONE_NUMBER, contact.get_phoneNumber());
//            values.put(NAME, contact.get_Name());
//            values.put(IS_CALL_BLOCK, contact.get_is_Call_block());
//            values.put(IS_MSG_BLOCK, contact.get_is_Msg_block());
//            values.put(PHOTO, contact.getPhoto());
//
//            int update = db.update(TABLE_CONTACTS, values, PHONE_NUMBER + " = ?",
//                    new String[]{String.valueOf(contact.get_phoneNumber())});
//            db.close();
//            return true;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
    public int deleteContact(String val) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(TABLE_CONTACTS, NUMBER + " = ?",
                new String[]{String.valueOf(val)});
        db.close();
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


    //            String sql_statement = " SELECT " + NUMBER + " , " + NAME + " FROM " + TABLE_CONTACTS + " GROUP BY "
//                    + NUMBER + " ORDER BY " + Date + " DESC  ";
//
//
//            String Incoming_call_sql = " SELECT " + NUMBER + " , " + NAME + " FROM " + TABLE_CONTACTS
//                    + " WHERE " + Type + "  = 'INCOMING'  AND " + Category + " = 'Call' " + "GROUP BY"
//                    + NUMBER + " ORDER BY " + Date + " DESC  ";
//
//            String Outgoing_call_sql = " SELECT " + NUMBER + " , " + NAME + " FROM " + TABLE_CONTACTS
//                    + " WHERE " + Type + "  = 'OUTGOING'  AND " + Category + " = 'Call' " + "GROUP BY"
//                    + NUMBER + " ORDER BY " + Date + " DESC  ";
//
//
//            String Outgoing_sms_sql = " SELECT " + NUMBER + " , " + NAME + " FROM " + TABLE_CONTACTS
//                    + " WHERE " + Type + "  = 'OUTGOING'  AND " + Category + " = 'Sms' " + "GROUP BY"
//                    + NUMBER + " ORDER BY " + Date + " DESC  ";
//
//            String sql_statement2 = " SELECT " + NUMBER + " , " + NAME + " FROM " + TABLE_CONTACTS
//
//                    + " WHERE " + Type + " = ' MISSED ' " +
//                    "GROUP BY"
//                    + NUMBER + " ORDER BY " + Date + " DESC  ";


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