package com.example.necozaki.mymemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private long memoId;
    private SimpleCursorAdapter adapter;
    public final static String EXTRA_MYID = "com.example.necozaki.mymemo.MYID";
    public final static String todoData = "";
    public final static String AlarmNum = "";
    public final static String pendingintent = "";
    private final String TAG = "Test02_01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final String[] from = {
                MemoContract.Memos.COL_TITLE,
                MemoContract.Memos.COL_UPDATED,
                MemoContract.Memos.COL_CREATE,//日付
        };

        int[] to = {
                R.id.text1,
                R.id.text2,
                R.id.text3,
        };

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.my_list_item,
                null,
                from,
                to,
                0
        );



        ListView myListView = (ListView) findViewById(R.id.MyListView);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id
            ) {
                Intent intent = new Intent(MainActivity.this, CustomActivity.class);
                intent.putExtra(EXTRA_MYID, id);
                intent.putExtra(todoData,from[0]);
                intent.putExtra(AlarmNum,position);
                intent.putExtra(pendingintent, MemoContract.Memos.COL_UPDATED);
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(0, null,this);
    }

    //新データ挿入
    public void insert2(View view){

       ContentValues values = new ContentValues();
        values.put(MemoContract.Memos.COL_TITLE, "new");
        values.put(MemoContract.Memos.COL_BODY, "new");
        values.put(MemoContract.Memos.COL_UPDATED, "0:00");
        values.put(MemoContract.Memos.COL_CREATE, "1/01");


        getContentResolver().insert(
                MemoContentProvider.CONTENT_URI,
                values
        );
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MemoContract.Memos._ID,
                MemoContract.Memos.COL_TITLE,
                MemoContract.Memos.COL_UPDATED,
                MemoContract.Memos.COL_CREATE,

        };
        return new CursorLoader(
                this,
                MemoContentProvider.CONTENT_URI,
                projection,
                null,
                null,
                MemoContract.Memos._ID
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }



}
