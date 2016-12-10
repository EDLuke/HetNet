package android_network.hetnet.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class PolicyVectorStoreHelper extends SQLiteOpenHelper {


    public static final String TABLE_POLICY_VECTORS = "policy_vectors";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_APP_ID = "app_id";
    public static final String COLUMN_APP_TYPE = "app_id";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LON = "longitude";
    public static final String COLUMN_SSID = "ssid";
    public static final String COLUMN_BANDWIDTH = "bandwidth";
    public static final String COLUMN_STRENGTH = "strength";
    public static final String COLUMN_FREQUENCY = "frequency";
    public static final String COLUMN_CONNECT_TIME = "connect_time";
    public static final String COLUMN_COST = "cost";

    public static final String[] TABLE_ROWS = {
            COLUMN_ID, COLUMN_APP_ID, COLUMN_APP_TYPE,  COLUMN_SSID, COLUMN_BANDWIDTH,
            COLUMN_STRENGTH, COLUMN_FREQUENCY, COLUMN_CONNECT_TIME, COLUMN_COST
    };

    private static final String DATABASE_NAME = "policy_vectors.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_POLICY_VECTORS + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_APP_ID
            + " text not null," + COLUMN_LAT
            + " bigint not null," + COLUMN_LON
            + " bigint not null,"  + COLUMN_SSID
            + " text not null," + COLUMN_BANDWIDTH
            + " bigint not null," + COLUMN_STRENGTH
            + " bigint not null," + COLUMN_FREQUENCY
            + " bigint not null," + COLUMN_CONNECT_TIME
            + " bigint not null," + COLUMN_COST
            + " biging not null);";


    public PolicyVectorStoreHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(PolicyVectorStoreHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLICY_VECTORS);
        onCreate(db);

    }


    public long storePolicyVector(PolicyVector v) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(this.COLUMN_APP_ID, v.getApplicationId());
        values.put(this.COLUMN_LAT, v.getLatitude());
        values.put(this.COLUMN_LON, v.getLongitude());
        values.put(this.COLUMN_SSID, v.getNetworkSSID());
        values.put(this.COLUMN_BANDWIDTH, v.getBandwidth());
        values.put(this.COLUMN_STRENGTH, v.getSignalStrength());
        values.put(this.COLUMN_FREQUENCY, v.getSignalFrequency());
        values.put(this.COLUMN_CONNECT_TIME, v.getTimeToConnect());
        values.put(this.COLUMN_COST, v.getCost());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(this.TABLE_POLICY_VECTORS, null, values);
        db.close();
        return newRowId;

    }

    public PolicyVector getPolicyVector(int id) {
        // Getting single policy vector
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_POLICY_VECTORS, this.TABLE_ROWS, this.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PolicyVector vector = new PolicyVector(cursor.getString(0), cursor.getString(1),
                Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
                cursor.getString(4), Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
                Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)));
        // return the policy vector if it exists
        return vector;

    }


    // Get PolicyVectors stored in the database
    public List<PolicyVector> getAllStoredPolicyVectors() {
        List<PolicyVector> policyVectorList = new ArrayList<PolicyVector>();
        // Select All Query returns all rows
        String selectQuery = "SELECT  * FROM " + TABLE_POLICY_VECTORS;
        //Open the database in read mode
        SQLiteDatabase db = this.getReadableDatabase();
        //Perform the select query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // loop through all returned rows and add them to a list
        if (cursor.moveToFirst()) {
            do {
                PolicyVector vector = new PolicyVector(cursor.getString(0), cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
                        cursor.getString(4), Integer.parseInt(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)),
                        Integer.parseInt(cursor.getString(8)), Integer.parseInt(cursor.getString(9)));
                policyVectorList.add(vector);
            } while (cursor.moveToNext());
        }

        // return all stored policies
        return policyVectorList;
    }


    // Getting contacts Count
    public int getPolicyVectorCount() {
        String countQuery = "SELECT  * FROM " + TABLE_POLICY_VECTORS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public void clearDatabase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POLICY_VECTORS);
    }

}
