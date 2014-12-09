package calllog.example.my_computer.calllog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.listview.SwipeMenu;
import com.listview.SwipeMenuCreator;
import com.listview.SwipeMenuItem;
import com.listview.SwipeMenuListView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends ActionBarActivity {


    DataBaseHelper dataBaseHelper;
    ListView listView;
    SwipeRefreshLayout swipeView = null;
    Adapter adapter;

    private SwipeMenuListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setEnabled(false);

//        List<Call_info> allContacts = dataBaseHelper.get_All_Call_info();

        List<Call_info> allContacts1 =
                dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Select_Distinct);

        adapter = new Adapter(getApplicationContext(), allContacts1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Call_info itemAtPosition = (Call_info) adapterView.getItemAtPosition(i);


                Dialog dialog = new Dialog(adapterView.getContext());
                dialog.setContentView(R.layout.dialog);

                final List<Call_info> whereVal = dataBaseHelper.getWhereVal(itemAtPosition.getNumber());

                if (itemAtPosition.getName().isEmpty()) {
                    dialog.setTitle(itemAtPosition.getNumber());

                } else {
                    dialog.setTitle(itemAtPosition.getName());
                }
                mListView = (SwipeMenuListView) dialog.findViewById(R.id.swipe_list);
                final AppAdapter mAdapter = new AppAdapter(getApplicationContext(), R.layout.item_list_app, whereVal);
                mListView.setAdapter(mAdapter);
                Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
                final String[] items = {" By All ", " By Incoming Call ", " By Outgoing Call  ", " By Incoming Sms", "By Outgoing Sms"};
                final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, items);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        switch (i) {
                            case 0:
                                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
                                        Sql_statements_selected.Select_all, itemAtPosition.getNumber()));
                                break;
                            case 1:
                                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
                                        Sql_statements_selected.Incoming_call_sql, itemAtPosition.getNumber()));
                                break;
                            case 2:
                                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
                                        Sql_statements_selected.Outgoing_call_sql, itemAtPosition.getNumber()));
                                break;
                            case 3:
                                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
                                        Sql_statements_selected.Incoming_sms_sql, itemAtPosition.getNumber()));
                                break;
                            case 4:
                                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
                                        Sql_statements_selected.Outgoing_sms_sql, itemAtPosition.getNumber()));
                                break;

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                SwipeMenuCreator creator = new SwipeMenuCreator() {
                    //
                    @Override
                    public void create(SwipeMenu menu) {

                        SwipeMenuItem deleteItem2 = new SwipeMenuItem(
                                getApplicationContext());

                        deleteItem2.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                0x3F, 0x25)));

                        deleteItem2.setWidth(dp2px(90));

                        deleteItem2.setIcon(android.R.drawable.ic_menu_delete);

                        menu.addMenuItem(deleteItem2);

                    }
                };


                mListView.setMenuCreator(creator);

                mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                        SwipeMenuItem swipeMenuItem = menu.getMenuItems().get(index);

                        switch (index) {
                            case 0:

//                             dataBaseHelper.deleteContact(swipeMenuItem.get)



                                break;


                        }
                        return false;
                    }
                });


                mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

                    @Override
                    public void onSwipeStart(int position) {

                    }

                    @Override
                    public void onSwipeEnd(int position) {

                    }
                });

                dialog.show();


            }
        });

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                if (get_first_number()) {
                    new Task().execute();

                } else {
                    swipeView.setRefreshing(false);
//                    Toast.makeText(getApplicationContext(), "Up to Date", Toast.LENGTH_SHORT).show();
                }

            }
        });


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipeView.setEnabled(true);
                else
                    swipeView.setEnabled(false);
            }
        });
    }

    class Task extends AsyncTask<Void, Integer, Void> {


        int dec_count;
        //        ProgressDialog progressDialog;
        DataBaseHelper dataBaseHelper;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            progressDialog.setProgress(values[0]);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dataBaseHelper = new DataBaseHelper(getApplicationContext());
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setMessage("Its loading....");
//            progressDialog.setTitle("ProgressDialog bar example");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.show();

        }

        protected Void doInBackground(Void... urls) {

            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null
                    , null, null, null);

//            progressDialog.setMax(cursor.getCount());
            cursor.moveToFirst();

            try {

                do {

                    Call_info call_info = new Call_info();

                    try {
                        String s2 = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) != null ?
                                cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) : "";

                        String s = remove_plus(s2);

                        call_info.setNumber(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        String s1 = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)) != null ?
                                cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)) : "";
                        call_info.setName(s1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        String s8 = cursor.getString(cursor.getColumnIndex("logtype")) != null ?
                                cursor.getString(cursor.getColumnIndex("logtype")) : "";

                        String Category = null;
                        if (s8.equals("100")) {
                            Category = "Call";

                        } else if (s8.equals("200")) {
                            Category = "Voice";

                        } else if (s8.equals("300")) {
                            Category = "Sms";

                        }
                        call_info.setCategory(Category);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        String s6 = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)) != null ?
                                cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)) : "";

                        String Type = null;
                        if (s6.equals("1")) {
                            Type = "INCOMING";

                        } else if (s6.equals("2")) {
                            Type = "OUTGOING";

                        } else if (s6.equals("3")) {
                            Type = "MISSED";

                        } else if (s6.equals("4")) {
                            Type = "Voice_Mail";

                        } else if (s6.equals("5")) {
                            Type = "Rejected";

                        } else if (s6.equals("6")) {
                            Type = "Refused_List";

                        }
                        call_info.setType(Type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Date aLong = null;
                    Format formatter;
                    try {
                        String s3 = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)) != null ?
                                cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)) : "";


                        aLong = new Date(Long.valueOf(s3));
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String sa = formatter.format(aLong);
                        call_info.setDate(sa);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    try {
                        formatter = new SimpleDateFormat("hh:mm:ss a");
                        String sb = formatter.format(aLong);
                        call_info.setTime(sb);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        String s4 = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)) != null ?
                                cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)) : "";
                        call_info.setDuration(s4);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    dataBaseHelper.addContact(call_info);

                    onProgressUpdate(dec_count++);
                }
                while (cursor.moveToNext());

                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            List<Call_info> all_call_info = dataBaseHelper.get_All_Call_info();

            swipeView.setRefreshing(false);
//            adapter.updateList(allContacts1);
//            progressDialog.hide();


        }
    }

    boolean get_first_number() {


        ContentResolver cr = getApplicationContext().getContentResolver();
        Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null
                , null, null, null);
        cursor.moveToFirst();
        String Number_phone = null;

        try {

            String s = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) != null ?
                    cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) : "";
            Number_phone = remove_plus(s);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String Date_phone = null;

        try {

            String s3 = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)) != null ?
                    cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)) : "";
            Date date = new Date(Long.valueOf(s3));

            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");

            Date_phone = formatter.format(date);


        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            List<Call_info> allContact2s = dataBaseHelper.get_First_row();

            if (!(allContact2s.isEmpty())) {
                Call_info call_info = allContact2s.get(0);

                if (call_info.getTime().equals(Date_phone) && call_info.getNumber().equals(Number_phone)) {

                    return false;


                } else {

                    return true;
                }
            } else {

                return true;

            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {


            final CharSequence[] items = {" By All ", " By Incoming Call ", " By Outgoing Call  ", " By Incoming Sms", "By Outgoing Sms"};

            SharedPreferences preference = getSharedPreferences("preference", 0);
            final SharedPreferences.Editor editor = preference.edit();
            int key = preference.getInt("item", 0);


            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Filter By");

            builder.setSingleChoiceItems(items, key, new DialogInterface.OnClickListener() {


                public void onClick(DialogInterface dialog, int item) {

                    switch (item) {
                        case 0:
                            List<Call_info> allContacts1 =
                                    dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Select_Distinct);
                            adapter.updateList(allContacts1);

                            break;
                        case 1:
                            List<Call_info> allContacts2 =
                                    dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Incoming_call_sql);
                            adapter.updateList(allContacts2);


                            break;
                        case 2:
                            List<Call_info> allContacts3 =
                                    dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Outgoing_call_sql);
                            adapter.updateList(allContacts3);

                            break;
                        case 3:
                            List<Call_info> allContacts4 =
                                    dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Incoming_sms_sql);
                            adapter.updateList(allContacts4);

                            break;
                        case 4:
                            List<Call_info> allContacts5 =
                                    dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Outgoing_sms_sql);
                            adapter.updateList(allContacts5);


                            break;

                    }
                    editor.putInt("item", item);
                    editor.commit();
                    dialog.dismiss();
                }
            }).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String remove_plus(String phoneNumber) {
        try {
            if (phoneNumber.charAt(0) == '+') {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, "");
                return "0" + String.valueOf(numberProto.getNationalNumber());
            } else {
                return phoneNumber;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
