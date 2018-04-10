package com.example.rudnev.remindme.sql;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

public class RemindMeContentProvider /*extends ContentProvider*/ {
    //final String LOG_TAG = "RemindLogs";
//
    //// // Константы для БД
    //// БД
    //static final String DB_NAME = "myRemindDB";
    //static final int DB_VERSION = 1;
//
    //// Таблица
    //static final String REMIND_TABLE = "remindtable";
//
    //// Поля
    //static final String REMIND_ID = "id";
//
    //// Скрипт создания таблицы
    //static final String DB_CREATE = "create table remindtable ("
    //        + "id integer primary key autoincrement,"
    //        + "title text, note text, date date"
    //        + ");";
//
    //// // Uri
    //// authority
    //static final String AUTHORITY = "com.rudnev.providers.RemindMe";
//
    //// path
    //static final String REMIND_PATH = "remindtable";
//
    //// Общий Uri
    //public static final Uri REMIND_CONTENT_URI = Uri.parse("content://"
    //        + AUTHORITY + "/" + REMIND_PATH);
//
    //// Типы данных
    //// набор строк
    //static final String REMIND_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
    //        + AUTHORITY + "." + REMIND_PATH;
//
    //// одна строка
    //static final String REMIND_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
    //        + AUTHORITY + "." + REMIND_PATH;
//
    ////// UriMatcher
    //// общий Uri
    //static final int URI_REMINDS = 1;
//
    //// Uri с указанным ID
    //static final int URI_REMINDS_ID = 2;
//
    //// описание и создание UriMatcher
    //private static final UriMatcher uriMatcher;
    //static {
    //    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //    uriMatcher.addURI(AUTHORITY, REMIND_PATH, URI_REMINDS);
    //    uriMatcher.addURI(AUTHORITY, REMIND_PATH + "/#", URI_REMINDS_ID);
    //}
//
    //RemindDBHelper dbHelper;
    //SQLiteDatabase db;
//
    //public boolean onCreate() {
    //    Log.d(LOG_TAG, "onCreate");
    //    dbHelper = new RemindDBHelper(getContext());
    //    return true;
    //}
//
    //// чтение
    //public Cursor query(@NonNull Uri uri, String[] projection, String selection,
    //                    String[] selectionArgs, String sortOrder) {
    //    Log.d(LOG_TAG, "query, " + uri.toString());
    //    // проверяем Uri
    //    /*switch (uriMatcher.match(uri)) {
    //        case URI_REMINDS: // общий Uri
    //            Log.d(LOG_TAG, "URI_CONTACTS");
    //            break;
    //        case URI_REMINDS_ID: // Uri с ID
    //            String id = uri.getLastPathSegment();
    //            Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
    //            // добавляем ID к условию выборки
    //            if (TextUtils.isEmpty(selection)) {
    //                selection = REMIND_ID + " = " + id;
    //            } else {
    //                selection = selection + " AND " + REMIND_ID + " = " + id;
    //            }
    //            break;
    //        default:
    //            throw new IllegalArgumentException("Wrong URI: " + uri);
    //    }*/
    //    String id = uri.getLastPathSegment();
    //    Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
    //    // добавляем ID к условию выборки
    //    try {
    //        if (TextUtils.isEmpty(selection)) {
    //            //selection = REMIND_ID + " = " + id;
    //        } else {
    //            //selection = selection + " AND " + REMIND_ID + " = " + id;
    //        }
    //    }
    //    catch (Exception ex){
    //        throw new IllegalArgumentException("Wrong URI: " + uri);
    //    }
    //    db = dbHelper.getWritableDatabase();
    //    Cursor cursor = db.query(REMIND_TABLE, projection, selection,
    //            selectionArgs, null, null, sortOrder);
    //    // просим ContentResolver уведомлять этот курсор
    //    // об изменениях данных в CONTACT_CONTENT_URI
    //    cursor.setNotificationUri(getContext().getContentResolver(),
    //            REMIND_CONTENT_URI);
    //    return cursor;
    //}
//
    //public Uri insert(@NonNull Uri uri, ContentValues values) {
    //    Log.d(LOG_TAG, "insert, " + uri.toString());
    //    if (uriMatcher.match(uri) != URI_REMINDS)
    //        throw new IllegalArgumentException("Wrong URI: " + uri);
//
    //    db = dbHelper.getWritableDatabase();
    //    long rowID = db.insert(REMIND_TABLE, null, values);
    //    Uri resultUri = ContentUris.withAppendedId(REMIND_CONTENT_URI, rowID);
    //    // уведомляем ContentResolver, что данные по адресу resultUri изменились
    //    getContext().getContentResolver().notifyChange(resultUri, null);
    //    return resultUri;
    //}
//
    //public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
    //    Log.d(LOG_TAG, "delete, " + uri.toString());
    //    switch (uriMatcher.match(uri)) {
    //        case URI_REMINDS:
    //            Log.d(LOG_TAG, "URI_CONTACTS");
    //            break;
    //        case URI_REMINDS_ID:
    //            String id = uri.getLastPathSegment();
    //            Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
    //            if (TextUtils.isEmpty(selection)) {
    //                selection = REMIND_ID + " = " + id;
    //            } else {
    //                selection = selection + " AND " + REMIND_ID + " = " + id;
    //            }
    //            break;
    //        default:
    //            throw new IllegalArgumentException("Wrong URI: " + uri);
    //    }
    //    db = dbHelper.getWritableDatabase();
    //    int cnt = db.delete(REMIND_TABLE, selection, selectionArgs);
    //    getContext().getContentResolver().notifyChange(uri, null);
    //    return cnt;
    //}
//
    //public int update(@NonNull Uri uri, ContentValues values, String selection,
    //                  String[] selectionArgs) {
    //    Log.d(LOG_TAG, "update, " + uri.toString());
    //    switch (uriMatcher.match(uri)) {
    //        case URI_REMINDS:
    //            Log.d(LOG_TAG, "URI_CONTACTS");
//
    //            break;
    //        case URI_REMINDS_ID:
    //            String id = uri.getLastPathSegment();
    //            Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
    //            if (TextUtils.isEmpty(selection)) {
    //                selection = REMIND_ID + " = " + id;
    //            } else {
    //                selection = selection + " AND " + REMIND_ID + " = " + id;
    //            }
    //            break;
    //        default:
    //            throw new IllegalArgumentException("Wrong URI: " + uri);
    //    }
    //    db = dbHelper.getWritableDatabase();
    //    int cnt = db.update(REMIND_TABLE, values, selection, selectionArgs);
    //    getContext().getContentResolver().notifyChange(uri, null);
    //    return cnt;
    //}
//
    //public String getType(@NonNull Uri uri) {
    //    Log.d(LOG_TAG, "getType, " + uri.toString());
    //    switch (uriMatcher.match(uri)) {
    //        case URI_REMINDS:
    //            return REMIND_CONTENT_TYPE;
    //        case URI_REMINDS_ID:
    //            return REMIND_CONTENT_ITEM_TYPE;
    //    }
    //    return null;
    //}
}
