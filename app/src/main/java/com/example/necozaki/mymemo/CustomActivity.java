package com.example.necozaki.mymemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

public class CustomActivity extends AppCompatActivity {


    private long memoId;
    private int notificationId = 0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        Intent intent = getIntent();
        memoId = intent.getLongExtra(MainActivity.EXTRA_MYID, 0L);

        TimePicker timePicker = (TimePicker) this.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void setAlarm(View view) {

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        //timePickerさんにお伺い
        TimePicker timePicker = (TimePicker) this.findViewById(R.id.timePicker);

        // EditTextを取得
        EditText editText = (EditText) findViewById(R.id.editText);
        // EditTextの中身を取得
        String Todo = editText.getText().toString().trim();

        if (Todo.equals("")) {

            editText.setError("予定が入力されていません");

        } else {

            int hour;
            int minuteOne;
            int minuteTwo;
            Calendar c = Calendar.getInstance();
            hour = timePicker.getHour();
            minuteOne = timePicker.getMinute();
            if(minuteOne >= 10){
                minuteTwo = minuteOne % 10;
                minuteOne = minuteOne / 10;
            } else {
                minuteTwo = minuteOne;
                minuteOne = 0;
            }

            String weekday = (String)spinner.getSelectedItem();
            // ポップアップ表示
            Toast.makeText(
                    CustomActivity.this,
                    weekday + "\n" + hour + "時" + minuteOne + minuteTwo + "分にセットしました\n"   + "' " + Todo + " '",
                    Toast.LENGTH_SHORT).show();

            Intent bootIntent = new Intent(CustomActivity.this, AlarmReceiver.class);
            bootIntent.putExtra("notificationId",notificationId);
            bootIntent.putExtra("todo",Todo);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(
                    CustomActivity.this,
                    0,
                    bootIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

            int sethour = hour;
            int setminute = 0;
            if(minuteOne > 0){
                setminute = minuteOne * 10;
            }

            setminute += minuteTwo;

            Calendar startTime = Calendar.getInstance();


            int idx = spinner.getSelectedItemPosition();
            //このフィールドの値は1から7で、
            // SUNDAY(1)、MONDAY(2)、TUESDAY(3)、WEDNESDAY(4)、THURSDAY(5)、FRIDAY(6)、および SATURDAY(7) になります。
            if(idx > 0){
                startTime.set(Calendar.DAY_OF_WEEK, idx);
            }

            startTime.set(Calendar.HOUR_OF_DAY, sethour);
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
            String updated = Integer.toString(hour) + " : " + Integer.toString(minuteOne) + Integer.toString(minuteTwo);

            ContentValues values = new ContentValues();
            values.put(MemoContract.Memos.COL_TITLE, title);
            values.put(MemoContract.Memos.COL_UPDATED, updated);


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
