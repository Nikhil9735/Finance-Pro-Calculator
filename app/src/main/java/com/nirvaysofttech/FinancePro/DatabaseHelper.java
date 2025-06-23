package com.nirvaysofttech.FinancePro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Financial Pro.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "FinancialPro";
    private static final String SELECTED_OPTION = "selectedOption";
    private static final String SAVE_RECORD_NAME = "save_record_name";
    private static final String LOAN_AMT = "loan_amt";
    private static final String INTEREST_RATE = "interest_rate";
    private static final String LOAN_TERM = "loan_term";
    private static final String TERM_UNIT = "termUnit";
    private static final String EMI_AMT = "emiAmt";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                SELECTED_OPTION + " TEXT, " +
                SAVE_RECORD_NAME + " TEXT PRIMARY KEY, " +
                LOAN_AMT + " TEXT, " +
                INTEREST_RATE + " TEXT, " +
                LOAN_TERM + " TEXT, " +
                TERM_UNIT + " TEXT, " +
                EMI_AMT + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertValues(String selectedOption, String save_record_name, String loanAmt, String interestRate, String loanTerm, String termUnit, String emiAmt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SELECTED_OPTION, selectedOption);
        cv.put(SAVE_RECORD_NAME, save_record_name);
        cv.put(LOAN_AMT, loanAmt);
        cv.put(INTEREST_RATE, interestRate);
        cv.put(LOAN_TERM, loanTerm);
        cv.put(TERM_UNIT, termUnit);
        cv.put(EMI_AMT, emiAmt);
        long result = db.insert(TABLE_NAME, null, cv);
        return result != -1;
    }

    public List<Map<String, String>> getAllEntriesAsMap() {
        List<Map<String, String>> entries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Map<String, String> entry = new HashMap<>();
                entry.put("selectedOption", cursor.getString(0));
                entry.put("save_record_name", cursor.getString(1));
                entry.put("loan_amt", cursor.getString(2));
                entry.put("interest_rate", cursor.getString(3));
                entry.put("loan_term", cursor.getString(4));
                entry.put("term_unit", cursor.getString(5));
                entry.put("emi_amt", cursor.getString(6));
                entries.add(entry);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return entries;
    }

    public boolean deleteRecordById(String save_record_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_NAME, SAVE_RECORD_NAME + " = ?", new String[]{save_record_name});
        return deletedRows > 0;
    }

    public boolean checkNameExists(String save_record_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT save_record_name FROM FinancialPro WHERE save_record_name = ?", new String[]{save_record_name});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean updateRecord(String selectedOption, String save_record_name, String loanAmt, String interestRate, String loanTerm, String termUnit, String emiAmt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SELECTED_OPTION, selectedOption);
        cv.put(LOAN_AMT, loanAmt);
        cv.put(INTEREST_RATE, interestRate);
        cv.put(LOAN_TERM, loanTerm);
        cv.put(TERM_UNIT, termUnit);
        cv.put(EMI_AMT, emiAmt);
        int rows = db.update(TABLE_NAME, cv, SAVE_RECORD_NAME + " = ?", new String[]{save_record_name});
        return rows > 0;
    }
}
