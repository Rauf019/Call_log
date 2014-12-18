package calllog.example.my_computer.calllog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dropbox.client2.session.Session;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.listview.Call_info;
import com.listview.SwipeMenu;
import com.listview.SwipeMenuCreator;
import com.listview.SwipeMenuItem;
import com.listview.SwipeMenuListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    DataBaseHelper dataBaseHelper;
    ListView listView;
    SwipeRefreshLayout swipeView = null;
    List_Adapter adapter;
    SwipeMenuListView mListView;
    SearchView mSearchView;
    Detail_Adapter mAdapter;
    SharedPreferences preference;


    final static private Session.AccessType ACCESS_TYPE = Session.AccessType.AUTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);





        preference = getSharedPreferences("preference", 0);
        listView = (ListView) findViewById(R.id.listView);
        dataBaseHelper = new DataBaseHelper(getApplicationContext());
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setEnabled(false);
        adapter = new List_Adapter(getApplicationContext(), Database_status());


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                        {

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                final int[] item_selected = new int[1];
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

                                                mAdapter = new Detail_Adapter(getApplicationContext(), R.layout.item_list_app, whereVal);
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
                                                        item_selected[0] = i;
                                                        Detail_Database_status(i, itemAtPosition.getNumber());
//                                                        switch (i) {
//                                                            case 0:
//                                                                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
//                                                                        Sql_statements_selected.Select_all, itemAtPosition.getNumber()));
//                                                                break;
//                                                            case 1:
//                                                                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
//                                                                        Sql_statements_selected.Incoming_call_sql, itemAtPosition.getNumber()));
//                                                                break;
//                                                            case 2:
//                                                                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
//                                                                        Sql_statements_selected.Outgoing_call_sql, itemAtPosition.getNumber()));
//                                                                break;
//                                                            case 3:
//                                                                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
//                                                                        Sql_statements_selected.Incoming_sms_sql, itemAtPosition.getNumber()));
//                                                                break;
//                                                            case 4:
//                                                                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
//                                                                        Sql_statements_selected.Outgoing_sms_sql, itemAtPosition.getNumber()));
//                                                                break;
//
//                                                        }D

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
                                                        Call_info item = mAdapter.getItem(position);

                                                        int position1 = position;

                                                        int index1 = index;
                                                        switch (index) {
                                                            case 0:
                                                                dataBaseHelper.deleteContact(item.getNumber(), item.getDate(), item.getTime());
                                                                Detail_Database_status(item_selected[0], itemAtPosition.getNumber());

                                                                adapter.updateList(Database_status());


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
                                        }

        );

        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()

                                       {
                                           @Override
                                           public void onRefresh() {


                                               swipeView.setRefreshing(false);
                                               long aLong = 0;
                                               String current_number = null;
                                               try {

                                                   ContentResolver cr = getContentResolver();
                                                   Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null
                                                           , null, null, null);
                                                   cursor.moveToFirst();

                                                   try {

                                                       current_number = remove_plus(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) != null ?
                                                               cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) : "");

                                                   } catch (Exception e) {
                                                       e.printStackTrace();
                                                   }

                                                   try {

                                                       aLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));


                                                   } catch (NumberFormatException e) {
                                                       e.printStackTrace();
                                                   }

                                                   Long maxDate = new DataBaseHelper(getApplicationContext()).getMaxDate();

                                                   if (maxDate > 0) {

                                                       if (maxDate == aLong) {

                                                           Toast.makeText(getApplicationContext(), " Up to Date", Toast.LENGTH_SHORT).show();
                                                       } else {
                                                           swipeView.setRefreshing(true);
                                                           Toast.makeText(getApplicationContext(), " Starting Update ", Toast.LENGTH_SHORT).show();
                                                           new NewUpdate().execute();

                                                       }
                                                   } else {
                                                       Toast.makeText(getApplicationContext(), " Starting Update ", Toast.LENGTH_SHORT).show();

                                                       new Task().execute();

                                                   }
                                               } catch (Exception e) {
                                                   e.getMessage();
                                               }


                                           }
                                       }

        );


        listView.setOnScrollListener(new AbsListView.OnScrollListener()

                                     {
                                         @Override
                                         public void onScrollStateChanged(AbsListView absListView, int i) {

                                         }

                                         @Override
                                         public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
                                                              int totalItemCount) {
                                             if (firstVisibleItem == 0)
                                                 swipeView.setEnabled(true);
                                             else
                                                 swipeView.setEnabled(false);
                                         }
                                     }

        );
    }

    public class NewUpdate extends AsyncTask<Void, Integer, Void> {
        DataBaseHelper db;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

//            Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();

            swipeView.setRefreshing(false);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            db = new DataBaseHelper(getApplicationContext());

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Call_info call_info;

            try {


                Long maxDate = db.getMaxDate();


                Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                        new String[]{CallLog.Calls.DATE, CallLog.Calls.DURATION,
                                CallLog.Calls.NUMBER, CallLog.Calls._ID},
                        CallLog.Calls.DATE + "> ?",
                        new String[]{String.valueOf(maxDate)},
                        CallLog.Calls.NUMBER);
                cursor.moveToFirst();


                do {


                    call_info = new Call_info();
                    try {

                        String s = remove_plus(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) != null ?
                                cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) : "");
                        call_info.setNumber(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {

                        call_info.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)) != null ?
                                cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)) : "");

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
                            Type = "Incoming";

                        } else if (s6.equals("2")) {
                            Type = "Outgoing";

                        } else if (s6.equals("3")) {
                            Type = "Missed";

                        } else if (s6.equals("4")) {
                            Type = "VoiceMail";

                        } else if (s6.equals("5")) {
                            Type = "Rejected";

                        } else if (s6.equals("6")) {
                            Type = "RefusedList";

                        }
                        call_info.setType(Type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        Long s3 = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));

                        call_info.setDate_long(s3);

                        Date aLong = new Date(Long.valueOf(s3));

                        SimpleDateFormat formatter
                                = new SimpleDateFormat("yyyy-MM-dd");
                        call_info.setDate(formatter.format(aLong));

                        formatter = new SimpleDateFormat("hh:mm:ss a");
                        call_info.setTime(formatter.format(aLong));

                        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

                        call_info.setDate_time(formatter.format(aLong));

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }


                    try {
                        String s4 = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)) != null ?
                                cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)) : "";
                        call_info.setDuration(s4);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

//                    Call_info call_info1 = call_info;
                    db.addContact(call_info);

                }
                while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Unable to Add", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    public List<Call_info> Detail_Database_status(int key, String Number) {


        switch (key) {
            case 0:
                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
                        Sql_statements_selected.Select_all, Number));
                break;
            case 1:
                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
                        Sql_statements_selected.Incoming_call_sql, Number));
                break;
            case 2:
                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
                        Sql_statements_selected.Outgoing_call_sql, Number));
                break;
            case 3:
                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
                        Sql_statements_selected.Incoming_sms_sql, Number));
                break;
            case 4:
                mAdapter.updateList(dataBaseHelper.get_sql_values(DataBaseHelper.
                        Sql_statements_selected.Outgoing_sms_sql, Number));
                break;

        }


        return null;
    }

    public List<Call_info> Database_status() {

        int key = preference.getInt("item", 0);


        switch (key) {

            case 0:

                return dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Select_Distinct);

            case 1:
                return
                        dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Incoming_call_sql);

            case 2:
                return
                        dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Outgoing_call_sql);

            case 3:
                return
                        dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Missed_calls_sql);

            case 4:
                return
                        dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Rejected_calls_sql);

            case 5:
                return
                        dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Incoming_sms_sql);
            case 6:
                return
                        dataBaseHelper.get_sql_values(DataBaseHelper.Sql_statements_all.Outgoing_sms_sql);

        }

        return null;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {

        adapter.updateList(dataBaseHelper.getSearchVal(s));

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public boolean onClose() {
        adapter.updateList(Database_status());
        return false;
    }

    class Task extends AsyncTask<Void, Integer, Void> {

        int dec_count;
        ProgressDialog progressDialog;
        DataBaseHelper dataBaseHelper;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dataBaseHelper = new DataBaseHelper(getApplicationContext());
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait.....");
            progressDialog.setTitle("Creating Backup ");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected Void doInBackground(Void... urls) {

            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, null
                    , null, null, null);
            progressDialog.setMax(cursor.getCount());

            cursor.moveToFirst();
            try {
                do {
                    Call_info call_info = new Call_info();

                    try {

                        String s = remove_plus(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) != null ?
                                cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)) : "");
                        call_info.setNumber(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {

                        call_info.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)) != null ?
                                cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)) : "");

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
                            Type = "Incoming";

                        } else if (s6.equals("2")) {
                            Type = "Outgoing";

                        } else if (s6.equals("3")) {
                            Type = "Missed";

                        } else if (s6.equals("4")) {
                            Type = "VoiceMail";

                        } else if (s6.equals("5")) {
                            Type = "Rejected";

                        } else if (s6.equals("6")) {
                            Type = "RefusedList";

                        }
                        call_info.setType(Type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        Long s3 = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));

                        call_info.setDate_long(s3);

                        Date aLong = new Date(Long.valueOf(s3));

                        SimpleDateFormat formatter
                                = new SimpleDateFormat("yyyy-MM-dd");
                        call_info.setDate(formatter.format(aLong));

                        formatter = new SimpleDateFormat("hh:mm:ss a");
                        call_info.setTime(formatter.format(aLong));

                        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

                        call_info.setDate_time(formatter.format(aLong));

                    } catch (NumberFormatException e) {
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
//            swipeView.setRefreshing(false);
//            List<Call_info> all_call_info = dataBaseHelper.get_All_Call_info();
//            adapter.updateList(Database_status());
            progressDialog.hide();


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {

            mSearchView.setIconified(false);

        }
        if (id == R.id.action_settings1) {
//

      startActivity(new Intent(this,DBRoulette.class));


//            final CharSequence[] items = {" By All ", " By Incoming Call ", " By Outgoing Call  ", " By Missed Call  ", " By Rejected Call  ", " By Incoming Sms", "By Outgoing Sms"};
//
//            final SharedPreferences.Editor editor = preference.edit();
//
//
//            int key = preference.getInt("item", 0);
//
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setTitle("Filter By");
//
//            builder.setSingleChoiceItems(items, key, new DialogInterface.OnClickListener() {
//
//
//                public void onClick(DialogInterface dialog, int item) {
//
//                    switch (item) {
//                        case 0:
//                            editor.putInt("item", item);
//                            editor.commit();
//                            adapter.updateList(Database_status());
//
//                            break;
//                        case 1:
//                            editor.putInt("item", item);
//                            editor.commit();
//
//                            adapter.updateList(Database_status());
//
//
//                            break;
//                        case 2:
//                            editor.putInt("item", item);
//                            editor.commit();
//                            adapter.updateList(Database_status());
//
//                            break;
//                        case 5:
//                            editor.putInt("item", item);
//                            editor.commit();
//
//                            adapter.updateList(Database_status());
//
//                            break;
//                        case 4:
//                            editor.putInt("item", item);
//                            editor.commit();
//
//                            adapter.updateList(Database_status());
//
//
//                            break;
//                        case 3:
//                            editor.putInt("item", item);
//                            editor.commit();
//
//                            adapter.updateList(Database_status());
//
//
//                            break;
//
//                    }
//
//                    dialog.dismiss();
//                }
//            }).show();

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
