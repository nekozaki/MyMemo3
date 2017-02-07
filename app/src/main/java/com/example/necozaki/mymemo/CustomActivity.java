package com.example.necozaki.mymemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.Time;
import java.util.Calendar;
import android.app.TimePickerDialog;

public class CustomActivity extends AppCompatActivity {

    private TimePickerDialog.OnTimeSetListener varTimeSetListener;
    private long memoId;
    private int notificationId = 0;
    private String tododata;
    private int AlarmNumber;
    private String pendingid;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

/*
        MemoOpenHelper memoOpenHelper = new MemoOpenHelper(this);
        SQLiteDatabase db = memoOpenHelper.getWritableDatabase();






        Cursor c = null;
        c = db.query(
                MemoContract.Memos.TABLE_NAME,
                null, // fields
                MemoContract.Memos._ID +  " = " + memoId, // where
                new String[] {""}, // where arg
                null, // groupBy
                null, // having
                null // order by
        );
        Log.v("DB_TEST", "Count: " + c.getCount());
        while(c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(MemoContract.Memos._ID));
            String name = c.getString(c.getColumnIndex(MemoContract.Memos.COL_CREATE));
            int score = c.getInt(c.getColumnIndex(MemoContract.Memos.COL_UPDATED));
            Log.v("DB_TEST", "id: " + id + " name: " + name + " score: " + score);
        }
        c.close();

        // close db
        db.close();


        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(pendingid);
        */

        CalendarView calendarview = (CalendarView) this.findViewById(R.id.calendarView2);
        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int hour[] = new int[2];
                month = month + 1;

                Calendar calendar = Calendar.getInstance();
                int nowyeah = calendar.get(Calendar.YEAR);
                int nowmonth = calendar.get(Calendar.MONTH);
                int nowday = calendar.get(Calendar.DAY_OF_MONTH);

                // EditTextを取得
                EditText editText = (EditText) findViewById(R.id.editText);
                // EditTextの中身を取得
                String Todo = editText.getText().toString().trim();

                if(year < nowyeah || month - 1 < nowmonth || dayOfMonth < nowday) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(CustomActivity.this);
                    alert.setTitle("日時選択");
                    alert.setMessage("過去の日時を選択することはできません。");
                    alert.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    alert.create().show();

                } else if (Todo.equals("")) {

                    editText.setError("予定が入力されていません");



                }
                else{
                    //時間指定
                    TimePickerShow(year, month, dayOfMonth);
                }

            }
        });




        Intent intent = getIntent();

        memoId = intent.getLongExtra(MainActivity.EXTRA_MYID, 0L);
        tododata = intent.getStringExtra(MainActivity.todoData);
        AlarmNumber = intent.getIntExtra(MainActivity.AlarmNum,0);
        pendingid = intent.getStringExtra(MainActivity.pendingintent);

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(tododata);


    //    TimePicker timePicker = (TimePicker) this.findViewById(R.id.timePicker);
   //     timePicker.setIs24HourView(true);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }





    public void setAlarm(int year, int month, int dayOfMonth, int hour, int minute) {

        //checkboxがチェックされていたら繰り返しON
        final CheckBox chcbox = (CheckBox)findViewById(R.id.checkBox);
        boolean repeat = false;
        if (chcbox.isChecked() == true)
        {
         repeat = true;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minute);
        int week = calendar.get(Calendar.DAY_OF_WEEK);

        // EditTextを取得
        EditText editText = (EditText) findViewById(R.id.editText);
        // EditTextの中身を取得
        String Todo = editText.getText().toString().trim();
        //timePickerさんにお伺い
        //TimePicker timePicker = (TimePicker) this.findViewById(R.id.timePicker);

            int minuteOne;
            int minuteTwo;
          //  Calendar c = Calendar.getInstance();

            minuteOne = minute;
            if(minuteOne >= 10){
                minuteTwo = minuteOne % 10;
                minuteOne = minuteOne / 10;
            } else {
                minuteTwo = minuteOne;
                minuteOne = 0;
            }
            String weekday = "???";
            switch (week){
                case 1:
                    weekday = "日曜日";
                    break;
                case 2:
                    weekday = "月曜日";
                    break;
                case 3:
                    weekday = "火曜日";
                    break;
                case 4:
                    weekday = "水曜日";
                    break;
                case 5:
                    weekday = "木曜日";
                    break;
                case 6:
                    weekday = "金曜日";
                    break;
                case 7:
                    weekday = "土曜日";
                    break;
            }

            // ポップアップ表示
            Toast.makeText(
                    CustomActivity.this,
                   month + "月" + dayOfMonth + "日　" + weekday + "\n" + hour + "時" + minuteOne + minuteTwo + "分にセットしました\n"   + "' " + Todo ,
                    Toast.LENGTH_SHORT).show();

            //AlarmManager
            AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

            // AlarmManagerから通知を受け取るレシーバーを定義する
            Intent bootIntent = new Intent(CustomActivity.this, AlarmReceiver.class);
            bootIntent.putExtra("notificationId",notificationId);
            bootIntent.putExtra("todo",Todo);

        //pendingintentの第二引数は重複不可であるため、現時点では日付+時刻の運用とする
        String pendingId = Integer.toString(month) + Integer.toString(dayOfMonth) + Integer.toString(hour) + Integer.toString(minute);
        int pendingid = Integer.parseInt(pendingId);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                CustomActivity.this,
                pendingid,
                bootIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        int sethour = hour;
        int setminute = 0;
        if(minuteOne > 0){
            setminute = minuteOne * 10;
        }
        setminute += minuteTwo;

        Calendar startTime = Calendar.getInstance();

        int idx = week;
        // SUNDAY(1)、MONDAY(2)、TUESDAY(3)、WEDNESDAY(4)、THURSDAY(5)、FRIDAY(6)、および SATURDAY(7)
        if(idx > 0){
            startTime.set(Calendar.DAY_OF_WEEK, idx);
        }

        startTime.set(Calendar.HOUR_OF_DAY, sethour);
        startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        startTime.set(Calendar.MINUTE, setminute);
        startTime.set(Calendar.SECOND, 0);

        long alarmStartTime = startTime.getTimeInMillis();

        alarm.set(
                AlarmManager.RTC_WAKEUP,
                alarmStartTime,
                alarmIntent
        );
        notificationId++;

        String title = Todo;
        String updated = Integer.toString(hour) + ":" + Integer.toString(minuteOne) + Integer.toString(minuteTwo);
        String date = "";
        if (dayOfMonth < 10) {
            date = Integer.toString(month) + "/0" + Integer.toString(dayOfMonth);
        } else{
            date = Integer.toString(month) + "/" + Integer.toString(dayOfMonth);
        }

        if(repeat){
            //繰り返しONの場合はさらに繰り返しサービスを実行
            //idは月+日+曜日
            pendingId = Integer.toString(hour) + Integer.toString(minute) + Integer.toString(idx);
            pendingid = Integer.parseInt(pendingId);

            alarmIntent = PendingIntent.getBroadcast(
                    CustomActivity.this,
                    pendingid,
                    bootIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

             alarm.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    alarmStartTime,
                    60480000,
                    alarmIntent
            );
            // ポップアップ表示
            Toast.makeText(
                    CustomActivity.this,
                    "毎週" + idx + "曜日の" + hour + "時" + minuteOne + minuteTwo + "分にセットしました\n"   + "' " + Todo ,
                    Toast.LENGTH_SHORT).show();

        }











            ContentValues values = new ContentValues();
            values.put(MemoContract.Memos.COL_TITLE, title);
            values.put(MemoContract.Memos.COL_UPDATED, updated);
            if(repeat){
                values.put(MemoContract.Memos.COL_CREATE, weekday);
            } else {
                values.put(MemoContract.Memos.COL_CREATE, date);
            }

        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(MemoContract.Memos._ID);



            //データ更新
            Uri uri = ContentUris.withAppendedId(
                    MemoContentProvider.CONTENT_URI,
                    memoId
            );

            getContentResolver().update(
                    uri,
                    values,
                    MemoContract.Memos._ID + "= ?",
                    new String[]{Long.toString(memoId)}

            );




    }

    //データ削除
    public void delete2(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("削除してもよろしいですか？")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = ContentUris.withAppendedId(
                                MemoContentProvider.CONTENT_URI,
                                memoId
                        );
                        getContentResolver().delete(
                                uri,
                                MemoContract.Memos._ID + " = ?",
                                new String[] { Long.toString(memoId) }
                        );
                        finish();
                    }
                })
                .show();
    }
    //時間選択
    public int[] TimePickerShow(final int year, final int month, final int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        final int hour[] = new int[2];
        final int yeah = year;
        hour[0] = calendar.get(Calendar.HOUR_OF_DAY);
        hour[1] = calendar.get(Calendar.MINUTE);
        TimePickerDialog timepick = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener(){
                    public void  onTimeSet(TimePicker view,int hourOfDay, int minute){
                        Calendar calendar = Calendar.getInstance();
                        int nowmonth = calendar.get(Calendar.MONTH);
                        nowmonth = nowmonth + 1;
                        int nowday = calendar.get(Calendar.DAY_OF_MONTH);
                        AlertDialog.Builder alert = new AlertDialog.Builder(CustomActivity.this);

                        //当日選択の場合のみ、過去の時間を選択させない処理
                        if(nowmonth == month && nowday == dayOfMonth){

                            if(hourOfDay < hour[0]) {
                                alert.setTitle("日時選択");
                                alert.setMessage("過去の時間を選択することはできません。");
                                alert.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                alert.create().show();
                                return;

                            }else if(hourOfDay == hour[0]){
                                    if(minute <= hour[1]){
                                        alert.setTitle("日時選択");
                                        alert.setMessage("過去の時間を選択することはできません。");
                                        alert.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                        alert.create().show();
                                        return;
                                    }
                                }

                        }
                        hour[0] = hourOfDay;
                        hour[1] = minute;
                        setAlarm(yeah, month, dayOfMonth, hour[0], hour[1]);


                        // ポップアップ表示
                 /*       Toast.makeText(
                                CustomActivity.this,
                                year + "年" + month + "月" + dayOfMonth
                                        + "\n" + hour[0] + hour[1],

                                Toast.LENGTH_SHORT).show();
                   */

                    }
                },
                hour[0],
                hour[1],
                true);
        timepick.show();

        return hour;
    }




    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Custom Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }





}
