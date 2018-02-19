package com.example.administrator.smsfilterapp.Db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.administrator.smsfilterapp.model.SmsStorageData;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "smsstoragedata";
   private static final String SMS_DATA = "smsdata";
   private static final String KEY_POSITION_ID = "_id";


    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DATE = "date";
    private static final String KEY_BODY = "body";



    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + SMS_DATA + "("
                + KEY_POSITION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + KEY_ADDRESS + " INTEGER ," + KEY_DATE + " TEXT," + KEY_BODY + " TEXT"
                +")";
        db.execSQL(CREATE_CONTACTS_TABLE);


        Log.e("table","...."+CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SMS_DATA);

        onCreate(db);
    }

    public void addSmsData(SmsStorageData storageData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

           values.put(KEY_POSITION_ID, storageData.get_id());
        values.put(KEY_ADDRESS, storageData.getAddress());
        values.put(KEY_DATE, storageData.getDate());
        values.put(KEY_BODY,storageData.getBody());

        db.insert(SMS_DATA, null, values);
        db.close();
    }

    SmsStorageData getSmsData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SMS_DATA, new String[] { KEY_POSITION_ID,KEY_ADDRESS,
                        KEY_DATE,KEY_BODY }, KEY_ADDRESS + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SmsStorageData contact = new SmsStorageData(
        );
        // return contact
        return contact;
    }

    public List<SmsStorageData> getAllSmsData() {
        List<SmsStorageData> contactList = new ArrayList<SmsStorageData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SMS_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SmsStorageData contact = new SmsStorageData();
                contact.set_id(cursor.getString(0));

                contact.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
                contact.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                contact.setBody(cursor.getString(cursor.getColumnIndex(KEY_BODY)));




                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int updateProduct(SmsStorageData contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        // updating row
        return db.update(SMS_DATA, values, KEY_ADDRESS + " = ?",
                new String[] { String.valueOf(contact.get_id()) });
    }

    // Deleting single contact
    public void deleteProduct(SmsStorageData contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SMS_DATA, KEY_POSITION_ID + " = ?",
                new String[] { String.valueOf(contact.get_id()) });
        db.close();
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + SMS_DATA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }


}