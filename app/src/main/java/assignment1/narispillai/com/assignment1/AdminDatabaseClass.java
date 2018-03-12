package assignment1.narispillai.com.assignment1;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import assignment1.narispillai.com.assignment1.AdminUserClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminDatabaseClass extends SQLiteOpenHelper {

    //Database variable declaration
    private int id;
    private String username;
    private String gender;
    private String email;
    private String password;
    private String birthday;
    private String position;
    private String address;
    private String phonenumber;
    private String dobmd;

    //Admin database version
    private static final int DATABASE_VERSION = 1;

    //Admin database Name
    private static final String DATABASE_NAME = "AdminListData.db";

    //Admin table name
    private static final String TABLE_ADMIN = "adm";

    //Admin table columns names declaration
    private static final String COLUMN_ADMIN_ID = "ad_id";
    private static final String COLUMN_ADMIN_USERNAME = "ad_username";
    private static final String COLUMN_ADMIN_PASSWORD = "ad_password";
    private static final String COLUMN_ADMIN_BIRTHDAY = "ad_birthday";
    private static final String COLUMN_ADMIN_GENDER = "ad_gender";
    private static final String COLUMN_ADMIN_EMAIL = "ad_email";
    private static final String COLUMN_ADMIN_POSITION = "ad_position";
    private static final String COLUMN_ADMIN_ADDRESS = "ad_address";
    private static final String COLUMN_ADMIN_PHONE = "ad_phone";
    private static final String COLUMN_ADMIN_DOBMD= "ad_dmofbirth";

    //Table query declaration
    private String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_ADMIN + "("
            + COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ADMIN_USERNAME + " TEXT,"
            + COLUMN_ADMIN_PASSWORD + " TEXT,"
            + COLUMN_ADMIN_BIRTHDAY + " TEXT,"
            + COLUMN_ADMIN_GENDER + " TEXT,"
            + COLUMN_ADMIN_EMAIL + " TEXT,"
            + COLUMN_ADMIN_POSITION + " TEXT,"
            + COLUMN_ADMIN_ADDRESS + " TEXT,"
            + COLUMN_ADMIN_PHONE + " TEXT,"
            + COLUMN_ADMIN_DOBMD + " TEXT" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_ADMIN;

    public AdminDatabaseClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADMIN_TABLE);
    }

    //In case of upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void addUser(AdminUserClass adminuserclass) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues admininfovalues = new ContentValues();
        admininfovalues.put(COLUMN_ADMIN_USERNAME, adminuserclass.getUsername());
        admininfovalues.put(COLUMN_ADMIN_PASSWORD, adminuserclass.getPassword());
        admininfovalues.put(COLUMN_ADMIN_BIRTHDAY, adminuserclass.getBirthday());
        admininfovalues.put(COLUMN_ADMIN_GENDER, adminuserclass.getGender());
        admininfovalues.put(COLUMN_ADMIN_EMAIL, adminuserclass.getEmail());
        admininfovalues.put(COLUMN_ADMIN_POSITION, adminuserclass.getPosition());
        admininfovalues.put(COLUMN_ADMIN_ADDRESS, adminuserclass.getAddress());
        admininfovalues.put(COLUMN_ADMIN_PHONE, adminuserclass.getPhoneNumber());
        admininfovalues.put(COLUMN_ADMIN_DOBMD, adminuserclass.getDMofbirth());

        // Inserting row when new admin is added
        db.insert(TABLE_ADMIN, null, admininfovalues);
        db.close();
    }


    public List<AdminUserClass> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ADMIN_ID,
                COLUMN_ADMIN_USERNAME,
                COLUMN_ADMIN_PASSWORD,
                COLUMN_ADMIN_BIRTHDAY,
                COLUMN_ADMIN_GENDER,
                COLUMN_ADMIN_EMAIL,
                COLUMN_ADMIN_POSITION,
                COLUMN_ADMIN_ADDRESS,
                COLUMN_ADMIN_PHONE,
                COLUMN_ADMIN_DOBMD
        };
        // sorting orders
        String sortOrder =
                COLUMN_ADMIN_USERNAME + " ASC";
        List<AdminUserClass> adminList = new ArrayList<AdminUserClass>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_ADMIN, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AdminUserClass adminuser = new AdminUserClass();
                adminuser.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_ID))));
                adminuser.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_USERNAME)));
                adminuser.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_PASSWORD)));
                adminuser.setBirthday(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_BIRTHDAY)));
                adminuser.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_GENDER)));
                adminuser.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_EMAIL)));
                adminuser.setPosition(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_POSITION)));
                adminuser.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_ADDRESS)));
                adminuser.setPhonenumber(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_PHONE)));
                adminuser.setDMofbirth(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_DOBMD)));
                // Adding user record to list
                adminList.add(adminuser);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return adminList;
    }

    public void updateAdmin(AdminUserClass adminupdate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues adcontent = new ContentValues();
        adcontent.put(COLUMN_ADMIN_USERNAME, adminupdate.getUsername());
        adcontent.put(COLUMN_ADMIN_PASSWORD, adminupdate.getPassword());
        adcontent.put(COLUMN_ADMIN_BIRTHDAY, adminupdate.getBirthday());
        adcontent.put(COLUMN_ADMIN_GENDER, adminupdate.getGender());
        adcontent.put(COLUMN_ADMIN_EMAIL, adminupdate.getEmail());
        adcontent.put(COLUMN_ADMIN_POSITION, adminupdate.getPosition());
        adcontent.put(COLUMN_ADMIN_ADDRESS, adminupdate.getAddress());
        adcontent.put(COLUMN_ADMIN_PHONE, adminupdate.getPhoneNumber());
        adcontent.put(COLUMN_ADMIN_DOBMD, adminupdate.getDMofbirth());

        // updating row
        db.update(TABLE_ADMIN, adcontent, COLUMN_ADMIN_USERNAME + " = ?",
                new String[]{String.valueOf(adminupdate.getUsername())});
        db.close();
    }


    public void deleteAdmin(AdminUserClass admindelete) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete admin record by username
        db.delete(TABLE_ADMIN, COLUMN_ADMIN_USERNAME + " = ?",
                new String[]{String.valueOf(admindelete.getUsername())});
        db.close();
    }


    public boolean checkUserUsername(String username) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ADMIN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_ADMIN_USERNAME + " = ?";

        // selection argument
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_ADMIN, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public List<AdminUserClass> returnAdminInfo(String adminusername) {

        List<AdminUserClass> adminList = new ArrayList<AdminUserClass>();

        // array of columns to fetch
        String[] columns = {
                COLUMN_ADMIN_ID,
                COLUMN_ADMIN_USERNAME,
                COLUMN_ADMIN_PASSWORD,
                COLUMN_ADMIN_BIRTHDAY,
                COLUMN_ADMIN_GENDER,
                COLUMN_ADMIN_EMAIL,
                COLUMN_ADMIN_POSITION,
                COLUMN_ADMIN_ADDRESS,
                COLUMN_ADMIN_PHONE,
                COLUMN_ADMIN_DOBMD
        };

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_ADMIN_USERNAME + " = ?";

        // selection argument
        String[] selectionArgs = {adminusername};

        Cursor cursor = db.query(TABLE_ADMIN, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        if (cursorCount > 0) {
            cursor.moveToFirst();
            AdminUserClass adminuser = new AdminUserClass();
            adminuser.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_ID))));
            adminuser.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_USERNAME)));
            adminuser.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_PASSWORD)));
            adminuser.setBirthday(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_BIRTHDAY)));
            adminuser.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_GENDER)));
            adminuser.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_EMAIL)));
            adminuser.setPosition(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_POSITION)));
            adminuser.setAddress(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_ADDRESS)));
            adminuser.setPhonenumber(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_PHONE)));
            adminuser.setDMofbirth(cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_DOBMD)));
            // Adding user record to list
            adminList.add(adminuser);
        }
        cursor.close();
        db.close();
        return adminList;
    }


    public String checkBirthday(String today) {

        AdminUserClass admin = new AdminUserClass();

        String name;

        // array of columns to fetch
        String[] columns = {
                COLUMN_ADMIN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_ADMIN_DOBMD + " = ?";

        // selection argument
        String[] selectionArgs = {today};

        Cursor cursor = db.query(TABLE_ADMIN, //Table to query
                null,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();


        if (cursorCount > 0) {
            cursor.moveToFirst();
            name = cursor.getString(1);
            return name;
        }
        cursor.close();
        db.close();
        return null;

    }

    public String checkCEOName(String ceoname){

        AdminUserClass admin = new AdminUserClass();

        String position;

        // array of columns to fetch
        String[] columns = {
                COLUMN_ADMIN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_ADMIN_USERNAME + " = ?";

        // selection argument
        String[] selectionArgs = {ceoname};

        Cursor cursor = db.query(TABLE_ADMIN, //Table to query
                null,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();


        if (cursorCount > 0) {
            cursor.moveToFirst();
            position = cursor.getString(6);
            return position;
        }
        cursor.close();
        db.close();
        return null;
    }


    public boolean checkUserPositionEdit(String username, String position) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ADMIN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_ADMIN_USERNAME + " = ?" + " AND " + COLUMN_ADMIN_POSITION + " = ?";

        // selection argument
        String[] selectionArgs = {username,position};

        Cursor cursor = db.query(TABLE_ADMIN, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        else {
            boolean got = checkUserPosition(position);
            if(got==true){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public boolean checkUserPosition(String position) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ADMIN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_ADMIN_POSITION + " = ?";

        // selection argument
        String[] selectionArgs = {position};

        Cursor cursor = db.query(TABLE_ADMIN, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {

            //position query was here
            if(position.equals("Ordinary Staff"))
            {
                return true;
            }
            else{
                return false;//previosly true
            }
        }
        return true;//previosly false
    }


    public boolean checkUserEmailEdit(String username,String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ADMIN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_ADMIN_USERNAME + " = ?" + " AND " + COLUMN_ADMIN_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {username,email};

        Cursor cursor = db.query(TABLE_ADMIN, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        else {
            boolean got = checkUserEmail(email);
            if(got==true){
                return true;
            }
            else{
                return false;
            }
        }
    }


    public boolean checkUserEmail(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ADMIN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_ADMIN_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_ADMIN, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return false;
        }

        return true;
    }



    public boolean checkUser(String username, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ADMIN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_ADMIN_USERNAME + " = ?" + " AND " + COLUMN_ADMIN_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_ADMIN, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
}

