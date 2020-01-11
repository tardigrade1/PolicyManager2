package in.gen2.policymanager.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.gen2.policymanager.models.SalesRepsDatamodel;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SRSqliteData
        extends SQLiteOpenHelper {
    /* open database, if doesn't exist, create it */

    public static final String DATABASE_NAME = "PolicyManager.db";
    public static final String CONTACTS_TABLE_NAME = "SRNameList";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_SR_NO = "srNo";
    private HashMap hp;

    public SRSqliteData(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME +
                        " (ID INTEGER PRIMARY KEY,name TEXT,srNo TEXT)"
        );
    }

    public boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
//        onCreate(db);
    }


    public boolean insertSr(String name, String srNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("srNo", srNo);
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }


    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateSr(Integer id, String name, String srNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("srNo", srNo);

        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteSr(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CONTACTS_TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CONTACTS_TABLE_NAME);
        db.execSQL("VACUUM");
        db.close();
    }
    //get the all SrNames
    public ArrayList<SalesRepsDatamodel> getSrNames() {
        ArrayList<SalesRepsDatamodel> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT *FROM " + CONTACTS_TABLE_NAME;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SalesRepsDatamodel salesRepsDatamodel = new SalesRepsDatamodel();
                salesRepsDatamodel.setId(cursor.getString(0));
                salesRepsDatamodel.setName(cursor.getString(1));
                salesRepsDatamodel.setSrNo(cursor.getString(2));
                arrayList.add(salesRepsDatamodel);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }
}
