package com.nirvaysofttech.FinancePro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Financial Pro.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "FinancialPro";
    private static final String ID = "id";
    private static final String LOAN_AMT = "loan_amt";
    private static final String INTEREST_RATE = "interest_rate";
    private static final String LOAN_TERM = "loan_term";
    private static final String TERM_UNIT = "termUnit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LOAN_AMT + " TEXT, " +
                INTEREST_RATE + " TEXT, " +
                LOAN_TERM + " TEXT, " +
                TERM_UNIT + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertValues(String loanAmt, String interestRate, String loanTerm, String termUnit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LOAN_AMT, loanAmt);
        cv.put(INTEREST_RATE, interestRate);
        cv.put(LOAN_TERM, loanTerm);
        cv.put(TERM_UNIT, termUnit);
        long result = db.insert(TABLE_NAME, null, cv);
        return result != -1;
    }

    public List<String> getAllEntries() {
        List<String> entries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String record = "Loan Amount: " + cursor.getString(1) +
                        ", Interest Rate: " + cursor.getString(2) +
                        ", Loan Term: " + cursor.getString(3) +
                        ", Term Unit: " + cursor.getString(4);
                entries.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return entries;
    }
}
