package cypher.destino.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by karhack on 28/11/16.
 */
public class Helperss extends SQLiteOpenHelper {
    private static final String TAG = "Helperss";
    public static String DbName = "MySqlite";
    public static String TABLE_Name = "Geotable";

    public static String ID_KEY = "id";
    public static String NAME_KEY = "name";
    public static String ADDRESS_KEY = "address";
    public static String LAT_KEY = "latitude";
    public static String LON_KEY = "longitude";
    public static String RADIUS_KEY = "radius";
    public static String LAT_KEY_CURRENT = "latitudeCurrent";
    public static String LON_KEY_CURRENT = "longitudeCurrent";
    public static String TIME_STAMP = "timestamp";
    public static String STATUS_KEY = "status";
    public static String ACTION_KEY = "action";
    public static String HITS_KEY = "hits";

    String[] keyArray = {ID_KEY,NAME_KEY,ADDRESS_KEY,LAT_KEY,LON_KEY,RADIUS_KEY,LAT_KEY_CURRENT,LON_KEY_CURRENT,TIME_STAMP,STATUS_KEY,ACTION_KEY, HITS_KEY};

    public Helperss(Context context) {
        super(context, DbName, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {

            String CREATE_TABLE = "CREATE TABLE "+TABLE_Name+"("+ID_KEY+" INTEGER PRIMARY KEY,"+NAME_KEY+" TEXT,"+ADDRESS_KEY+" TEXT,"+LAT_KEY+" TEXT,"
                    +LON_KEY+" TEXT,"+RADIUS_KEY+" TEXT,"+LAT_KEY_CURRENT+" TEXT,"+LON_KEY_CURRENT+" TEXT,"+TIME_STAMP+" TEXT,"+STATUS_KEY+" TEXT,"+ACTION_KEY+" TEXT,"+HITS_KEY+" TEXT)";
            sqLiteDatabase.execSQL(CREATE_TABLE);
        } catch (Exception e) {
            Log.e(TAG, "onCreate Err : " + e.getLocalizedMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_Name);
        onCreate(sqLiteDatabase);
    }

    public void addRow(SqlObject newObj) {
        try {
            ContentValues contentVals = new ContentValues();
            contentVals.put(NAME_KEY, newObj.name);
            contentVals.put(ADDRESS_KEY, newObj.address);
            contentVals.put(LAT_KEY, newObj.latitude);
            contentVals.put(LON_KEY, newObj.longitude);
            contentVals.put(RADIUS_KEY, newObj.radius);
            contentVals.put(LAT_KEY_CURRENT, newObj.latitudeCurrent);
            contentVals.put(LON_KEY_CURRENT, newObj.longitudeCurrent);
            contentVals.put(TIME_STAMP, newObj.timestamp);
            contentVals.put(STATUS_KEY, newObj.status);
            contentVals.put(ACTION_KEY, newObj.action);
            contentVals.put(HITS_KEY, newObj.hits);



            SQLiteDatabase sql = getWritableDatabase();
            sql.insert(TABLE_Name, null, contentVals);
            Log.e(TAG, "Record added");
            sql.close();
        } catch (Exception e) {
            Log.e(TAG, "Error adding to db : " + e.getLocalizedMessage());
        }
    }

    public SqlObject getSingleRow(int id) {
        try {
            SQLiteDatabase sql = getWritableDatabase();
            Cursor cursor = sql.query(TABLE_Name, keyArray , ID_KEY + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }
            SqlObject newObj = new SqlObject(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11));

            sql.close();
            Log.e(TAG, "Single record fetched");
            return newObj;
        } catch (Exception e) {
            Log.e(TAG, "getSingleRecord err : " + e.getLocalizedMessage());
            return null;
        }
    }

    public ArrayList<SqlObject> getAllRecords(){
        try{
        ArrayList<SqlObject> arrayList = new ArrayList<>();
        SQLiteDatabase sql = getReadableDatabase();
        String GET_ALL = "SELECT * FROM "+TABLE_Name;
        Cursor cursor = sql.rawQuery(GET_ALL, null);
            if (cursor.moveToFirst()){
                do {
                    SqlObject sqlObject = new SqlObject();
                    sqlObject.id = cursor.getInt(0);
                    sqlObject.name = cursor.getString(1);
                    sqlObject.address = cursor.getString(2);
                    sqlObject.latitude = cursor.getString(3);
                    sqlObject.longitude = cursor.getString(4);
                    sqlObject.radius = cursor.getString(5);
                    sqlObject.latitudeCurrent = cursor.getString(6);
                    sqlObject.longitudeCurrent = cursor.getString(7);
                    sqlObject.timestamp = cursor.getString(8);
                    sqlObject.status = cursor.getString(9);
                    sqlObject.action = cursor.getString(10);
                    sqlObject.hits = cursor.getString(11);


                    arrayList.add(sqlObject);
                }while (cursor.moveToNext());
            }
            Log.e(TAG, "All records fetched");
            sql.close();
            return arrayList;
        }catch (Exception e){
            Log.e(TAG, "get all err : " + e.getLocalizedMessage());
            return  null;
        }
    }


    public void deleteRecord(int id){
        SQLiteDatabase sql = getWritableDatabase();
        sql.delete(TABLE_Name, ID_KEY+"=?",new String[]{String.valueOf(id)});
        sql.close();
        Log.e(TAG, "Record deleted");
        sql.close();
    }

    public void updateRecord(SqlObject sqlObject){
        //to be refurbished later for addition in database
        try{
        SQLiteDatabase sql = getWritableDatabase();
            ContentValues contentVals = new ContentValues();


            Log.e(TAG, "name : "+sqlObject.name);
            Log.e(TAG, "radius : "+sqlObject.radius);
            Log.e(TAG, "action : "+sqlObject.action);


            if (sqlObject.name != null){
                contentVals.put(NAME_KEY, sqlObject.name);
            }if (sqlObject.address != null){
                contentVals.put(ADDRESS_KEY, sqlObject.address);
            }if (sqlObject.latitude != null){
                contentVals.put(LAT_KEY, sqlObject.latitude);
            }if (sqlObject.longitude != null){
                contentVals.put(LON_KEY, sqlObject.longitude);
            }if (sqlObject.radius != null){
                contentVals.put(RADIUS_KEY, sqlObject.radius);
            }if (sqlObject.latitudeCurrent != null){
                contentVals.put(LAT_KEY_CURRENT, sqlObject.latitudeCurrent);
            }if (sqlObject.longitudeCurrent != null){
                contentVals.put(LON_KEY_CURRENT, sqlObject.longitudeCurrent);
            }if (sqlObject.timestamp != null){
                contentVals.put(TIME_STAMP, sqlObject.timestamp);
            }if (sqlObject.status != null){
                contentVals.put(STATUS_KEY, sqlObject.status);
            }if (sqlObject.action != null){
                contentVals.put(ACTION_KEY, sqlObject.action);
            }if (sqlObject.hits != null){
                contentVals.put(HITS_KEY, sqlObject.hits);
            }

        sql.update(TABLE_Name, contentVals,ID_KEY+"=?",new String[]{String.valueOf(sqlObject.id)});
        sql.close();

            Log.e(TAG, "Database updated");
        }catch (Exception e){
            Log.e(TAG, "Update db err : "+e.getLocalizedMessage());
        }}

    public void deletAll(){
        try{
        SQLiteDatabase sql  = getWritableDatabase();
        sql.execSQL("DELETE FROM "+TABLE_Name);
            Log.e(TAG, "Database Table flushed");
            sql.close();
        }catch (Exception e){
            Log.e(TAG, "Deleting err : "+e.getLocalizedMessage());
        }}
}

