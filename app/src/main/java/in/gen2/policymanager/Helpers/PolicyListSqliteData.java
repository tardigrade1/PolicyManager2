package in.gen2.policymanager.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import in.gen2.policymanager.models.PoliciesFormData;
import in.gen2.policymanager.models.SalesRepsDatamodel;

public class PolicyListSqliteData
        extends SQLiteOpenHelper {
    /* open database, if doesn't exist, create it */

    public static final String DATABASE_NAME = "PolicyManager.db";
    public static final String TABLE_NAME = "policyList";
    private HashMap hp;

    public PolicyListSqliteData(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_NAME +
                        " (id INTEGER PRIMARY KEY,name TEXT,srNo TEXT,applicationNo TEXT,purchaseDate TEXT)"
        );
    }

    public boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
    }

    public boolean isTableExists() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_NAME + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        return true;
    }

    public boolean insertPolicies(String name, String srNo, String applicationNo, String purchaseDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(!isTableExists()){
            db.execSQL(
                    "create table " + TABLE_NAME +
                            " (id INTEGER PRIMARY KEY,name TEXT,srNo TEXT,applicationNo TEXT,purchaseDate TEXT)"
            );
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("srNo", srNo);
        contentValues.put("applicationNo", applicationNo);
        contentValues.put("purchaseDate", purchaseDate);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }


    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updatePolicies(Integer id, String name, String srNo, String applicationNo, String purchaseDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("srNo", srNo);
        contentValues.put("applicationNo", applicationNo);
        contentValues.put("purchaseDate", purchaseDate);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deletePolicies(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("VACUUM");
        db.close();
    }

    //get the all SrNames
    public ArrayList<PoliciesFormData> getApplicantNames() {
        ArrayList<PoliciesFormData> arrayList = new ArrayList<>();

        // select all query
        String select_query = "SELECT *FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PoliciesFormData policiesFormData = new PoliciesFormData();
                policiesFormData.setId(cursor.getString(0));
                policiesFormData.setApplicantName(cursor.getString(1));
                policiesFormData.setSrNo(cursor.getString(2));
                policiesFormData.setApplicationNo(cursor.getString(3));
                policiesFormData.setPurchaseDate(cursor.getString(4));
                arrayList.add(policiesFormData);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
}
