package vlu.android.numberseven.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "number7.db";
    public static final String TABLE_NAME = "User";
    public static final String COL_1 = "Username";
    public static final String COL_2 = "Password";
    public static final String COL_3 = "FullName";
    public static final String COL_4 = "Email";
    public static String PATH = "/data/data/vlu.android.numberseven/databases/number7.db";
    public UserHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Do nothing here as the table is already created manually
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing here as the table is already created manually
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }


    public boolean insertData(String fullname, String email, String username, String password) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, username);
        contentValues.put(COL_2, password);
        contentValues.put(COL_3, fullname);
        contentValues.put(COL_4, email);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1;
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
        String hashedPassword = hashPassword(password); // Hash the password input
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = ? AND " + COL_2 + " = ?", new String[]{username, hashedPassword});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cursor getUserDetails(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = ?", new String[]{username});
    }

    public boolean updateUserDetails(String username, String fullname, String email) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3, fullname);
        contentValues.put(COL_4, email);
        int result = db.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{username});
        db.close();
        return result > 0;
    }

    public boolean updateUserPassword(String username, String newPassword) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, hashPassword(newPassword));
        int result = db.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{username});
        db.close();
        return result > 0;
    }

}
