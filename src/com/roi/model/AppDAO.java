package com.roi.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDAO extends SQLiteOpenHelper {
	
	// Database version
	private static final int DATABASE_VERSION = 3;
	// Database name
	private static final String DATABASE_NAME = "mobileDB";
	
	
	//===================================USER=======================================
	
	// User table name
	public static final String USER_TABLE_NAME = "user";
	
	// User table columns names
    private static final String USER_ID = "userid";
    private static final String USER_NAME = "name";
    private static final String USER_PASSWORD = "password";
    
    // User create table
    private static final String USER_TABLE_CREATE = 
    		"CREATE TABLE " + USER_TABLE_NAME + " ("
            + USER_ID + " INTEGER PRIMARY KEY," 
            + USER_NAME + " TEXT,"
            + USER_PASSWORD + " TEXT " + ")";
    
    //===================================MARKER=======================================
	
  	// Marker table name
  	public static final String MARKER_TABLE_NAME = "marker";
  	
  	// Marker table columns names
    private static final String MARKER_ID = "markerid";
    private static final String MARKER_BARCODE = "barcode";
    private static final String MARKER_CREATED_BY_USER = "createdByUser";
    private static final String MARKER_DESCRIPTION = "description";
    private static final String MARKER_X_POS = "xPos";
    private static final String MARKER_Y_POS = "yPos";
    private static final String MARKER_IS_SHOWN = "isShown";
    
    // Marker create table
    private static final String MARKER_TABLE_CREATE = 
     	"CREATE TABLE " + MARKER_TABLE_NAME + " ("
            + MARKER_ID + " INTEGER PRIMARY KEY," 
            + MARKER_BARCODE + " TEXT,"
            + MARKER_CREATED_BY_USER + " TEXT, " 
            + MARKER_DESCRIPTION + " TEXT, " 
            + MARKER_X_POS + " REAL, "
            + MARKER_Y_POS + " REAL,"
            + MARKER_IS_SHOWN + " TEXT " + ")";
      
    // AppDAO instance
    private static AppDAO appDAO = null;
    
    // private C'tor
    private AppDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    // Getting instance of AppDAO to initiate singleton pattern
    public static AppDAO getInstance(Context context)
    {
    	if (appDAO == null)
    	{
    		appDAO = new AppDAO(context);
    	}
    	
    	return appDAO;
    }
    
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(USER_TABLE_CREATE);
        db.execSQL(MARKER_TABLE_CREATE);
    }
    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MARKER_TABLE_NAME);
        
        // Create table again
        onCreate(db);
    }
    
    // Adding new user
    public void addUser(User user) 
    {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(USER_ID, getMaxId(USER_TABLE_NAME) + 1);
        values.put(USER_NAME, user.getUserName());
        values.put(USER_PASSWORD, user.getPassword());
        
        db.insert(USER_TABLE_NAME, null, values);
        db.close();
    }

    // Getting single user
    public User getUser(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(USER_TABLE_NAME, new String[] { USER_ID,
        		USER_NAME, USER_PASSWORD }, USER_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
     
        User user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        
        return user;
    }
    
    // Getting single user
    public User getUserByName(String name)
    {
    	User user = null;
    	
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(USER_TABLE_NAME, new String[] { USER_ID,
        		USER_NAME, USER_PASSWORD }, USER_NAME + "=?",
                new String[] { name }, null, null, null, null);
        
        if (cursor != null) {
        	
        	if(cursor.moveToFirst()) {
        		user = new User(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        	}
        }
            
        return user;
    }
    
    // Deleting single user
    public void deleteUser(User user) 
    {
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.delete(USER_TABLE_NAME, USER_ID + " = ?",
        		new String[] { String.valueOf(user.getUserId()) });
    }
    
    
    
    // Adding new marker
    public void addMarker(Marker marker)
    {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(MARKER_ID, getMaxId(MARKER_TABLE_NAME) + 1);
        values.put(MARKER_BARCODE, marker.getBarcode());
        values.put(MARKER_CREATED_BY_USER, marker.getCreatedByUser());
        values.put(MARKER_DESCRIPTION, marker.getDescription());
        values.put(MARKER_X_POS, marker.getxPos());
        values.put(MARKER_Y_POS, marker.getyPos());
        values.put(MARKER_IS_SHOWN, marker.getIsShown());
        
        db.insert(MARKER_TABLE_NAME, null, values);
        db.close();
    }
    
    // Getting single marker
    public Marker getMarker(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(MARKER_TABLE_NAME, new String[] { MARKER_ID,
        		MARKER_BARCODE, MARKER_CREATED_BY_USER, MARKER_DESCRIPTION, MARKER_X_POS, MARKER_Y_POS, MARKER_IS_SHOWN }, MARKER_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        
        if (cursor != null)
            cursor.moveToFirst();
     
        Marker marker = new Marker(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
        		Float.parseFloat(cursor.getString(4)), Float.parseFloat(cursor.getString(5)), cursor.getString(6));
        
        return marker;
    }
    
    // Getting single marker by barcode
    public Marker getMarkerByBarcode(String barcode)
    {
    	Marker marker = null;
    	
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(MARKER_TABLE_NAME, new String[] { MARKER_ID,
        		MARKER_BARCODE, MARKER_CREATED_BY_USER, MARKER_DESCRIPTION, MARKER_X_POS, MARKER_Y_POS, MARKER_IS_SHOWN }, MARKER_BARCODE + "=?",
                new String[] { barcode }, null, null, null, null);
        
        if (cursor != null) {
        	
        	if (cursor.moveToFirst()) {
        		marker = new Marker(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                		Float.parseFloat(cursor.getString(4)), Float.parseFloat(cursor.getString(5)), cursor.getString(6));
        	}
        }
            
        return marker;
    }
    
    // Getting all markers
    public ArrayList<Marker> getAllMarkers()
    {
    	ArrayList<Marker> markersArrayList = new ArrayList<Marker>();
    	
       // Select All Query
       String selectQuery = "SELECT  * FROM " + MARKER_TABLE_NAME;
    
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
    
       // looping through all rows and adding to list
       if (cursor.moveToFirst()) 
       {
           do {
        	   Marker marker = new Marker(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),
               		Float.parseFloat(cursor.getString(4)), Float.parseFloat(cursor.getString(5)), cursor.getString(6));
        	   
               // Adding credit Card to list
        	   markersArrayList.add(0,marker);
           } while (cursor.moveToNext());
       }
       
       return markersArrayList;
   }
    
    // Updating single marker
    public int updateMarker(Marker marker)
    {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(MARKER_ID, marker.getMarkerId());
        values.put(MARKER_BARCODE, marker.getBarcode());
        values.put(MARKER_CREATED_BY_USER, marker.getCreatedByUser());
        values.put(MARKER_DESCRIPTION, marker.getDescription());
        values.put(MARKER_X_POS, marker.getxPos());
        values.put(MARKER_Y_POS, marker.getyPos());
        values.put(MARKER_IS_SHOWN, marker.getIsShown());
        
        // updating row
        int rowsAffected = db.update(MARKER_TABLE_NAME, values, MARKER_ID + " = ?",
                new String[] { String.valueOf(marker.getMarkerId()) });
         
        return rowsAffected;
    }
    
    // Deleting single marker
    public void deleteMarker(Marker marker) 
    {
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.delete(MARKER_TABLE_NAME, MARKER_ID + " = ?",
        		new String[] { String.valueOf(marker.getMarkerId()) });
    }
    
    
    // Getting max id 
    public int getMaxId(String tableName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
     
        String query = "SELECT MAX("+tableName+"id) AS max_id FROM " + tableName;
        Cursor cursor = db.rawQuery(query, null);

        int id = 0;     
        if (cursor.moveToFirst())
        {
            do
            {           
                id = cursor.getInt(0);                  
            } while(cursor.moveToNext());           
        }
        
        return id;
    }
}
