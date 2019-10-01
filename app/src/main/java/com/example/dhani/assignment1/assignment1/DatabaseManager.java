package com.example.dhani.assignment1;

/**
 * Created by dhani on 30/08/2017.
 */

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager
{
    public static final String DB_NAME = "StudentManager";
    public static final int DB_VERSION = 1;
    private static final String CREATE_TABLE_FRIENDS = "CREATE TABLE Friends (" +
            "friendID INTEGER PRIMARY KEY AUTOINCREMENT, firstname TEXT, lastname TEXT, " +
            "gender TEXT, age INTEGER, address TEXT, picture TEXT);";
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE Tasks (" +
            "taskID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, location TEXT, status TEXT);";
    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE Events (" +
            "eventID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, minute INTEGER, hour INTEGER, " +
            "day INTEGER, month INTEGER, year INTEGER, location TEXT);";
    private SQLHelper helper;
    private SQLiteDatabase db;
    private Context context;

    // Constructors.
    public DatabaseManager(Context c)
    {
        this.context = c;
        helper = new SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }

    public DatabaseManager openReadable() throws android.database.SQLException
    {
        helper = new SQLHelper(context);
        db = helper.getReadableDatabase();
        return this;
    }

    // Function to close database.
    public void close()
    {
        helper.close();
    }

    // Function to add a friend.
    public boolean addFriend(String firstname, String lastname, String gender, String age,
                             String address, String picture)
    {
        try
        {
            db.execSQL("INSERT INTO Friends (firstname, lastname, gender, age, address, picture) VALUES ('" +
                    firstname + "', '" + lastname + "', '" + gender + "', '" + age + "', '" +
                    address + "', '" + picture + "');");
        }
        catch (Exception exception)
        {
            return false;
        }

        return true;
    }

    // Function to update a friend-record.
    public boolean updateFriend(int id, String firstname, String lastname, String gender,
                                String age, String address, String picture)
    {
        try
        {
            db.execSQL("UPDATE Friends SET " +
                    "firstname = '" + firstname + "', " +
                    "lastname = '" + lastname + "', " +
                    "gender = '" + gender + "', " +
                    "age = '" + age + "', " +
                    "address = '" + address + "', " +
                    "picture = '" + picture + "' " +
                    "WHERE friendID = " + id +
                    ";");
        }
        catch (Exception exception)
        {
            return false;
        }

        return true;
    }

    // Function to delete a friend-record.
    public boolean deleteFriend(int id)
    {
        try
        {
            db.execSQL("DELETE FROM Friends WHERE friendID = " + id + ";");
        }
        catch (Exception exception)
        {
            return false;
        }

        return true;
    }

    // Function to generate a list of friends-names in the database.
    public String[] retrieveNames()
    {
        // Initalise variables.
        String[] names;
        Cursor queryResult;

        // Execute query and store result.
        queryResult = db.rawQuery("SELECT firstname, lastname FROM Friends", null);

        // Allocate memory for 'names' array.
        names = new String[queryResult.getCount()];

        // Move cursor to first name.
        queryResult.moveToFirst();

        // Iterate through query-result & store names in string-array.
        for (int i = 0; i < queryResult.getCount(); i++)
        {
            names[i] = queryResult.getString(0) + ' ' + queryResult.getString(1);

            queryResult.moveToNext();
        }

        // Close query.
        if (queryResult != null && !queryResult.isClosed())
        {
            queryResult.close();
        }

        return names;
    }

    // Function to query all friends.
    public String[][] queryAllFriends()
    {
        // Initalise variables.
        String[][] records;
        Cursor queryResult;
        int x = 0, y = 0; // x = record, y = field.

        // Execute query and store result.
        queryResult = db.rawQuery("SELECT * FROM Friends", null);

        // Allocate memory for 'records' 2d-array.
        records = new String[queryResult.getCount()][7];

        // Move cursor to item.
        queryResult.moveToFirst();

        // Iterate through query-result & store records in string-2d-array.
        for (int i = 0; i < (queryResult.getCount() * 7); i++)
        {
            records[x][y] = queryResult.getString(y);

            y++;

            if (y == 7)
            {
                y = 0;
                queryResult.moveToNext();
                x++;
            }
        }

        // Close query.
        if (queryResult != null && !queryResult.isClosed())
        {
            queryResult.close();
        }

        return records;
    }

    // Function to query a specific friend.
    public String[] queryFriend(int id)
    {
        // Initalise variables.
        String[] friend;
        Cursor queryResult;

        // Execute query and store result.
        queryResult = db.rawQuery("SELECT * FROM Friends WHERE friendID = " + id, null);

        // Allocate memory for 'records' 2d-array.
        friend = new String[7];

        // Move cursor to item.
        queryResult.moveToFirst();

        // Iterate through query-result & store records in string-2d-array.
        for (int i = 0; i < 7; i++)
            friend[i] = queryResult.getString(i);

        // Close query.
        if (queryResult != null && !queryResult.isClosed())
        {
            queryResult.close();
        }

        return friend;
    }

    // Function to query all tasks.
    public String[][] queryAllTasks()
    {
        // Initalise variables.
        String[][] tasks;
        Cursor queryResult;
        int x = 0, y = 0; // x = record, y = field.

        // Execute query and store result.
        queryResult = db.rawQuery("SELECT * FROM Tasks ORDER BY status DESC", null);

        // Allocate memory for 'records' 2d-array.
        tasks = new String[queryResult.getCount()][4];

        // Move cursor to item.
        queryResult.moveToFirst();

        // Iterate through query-result & store records in string-2d-array.
        for (int i = 0; i < (queryResult.getCount() * 4); i++)
        {
            tasks[x][y] = queryResult.getString(y);

            y++;

            if (y == 4)
            {
                y = 0;
                queryResult.moveToNext();
                x++;
            }
        }

        // Close query.
        if (queryResult != null && !queryResult.isClosed())
        {
            queryResult.close();
        }

        return tasks;
    }

    // Function to add a task.
    public boolean addTask(String name, String location)
    {
        try
        {
            db.execSQL("INSERT INTO Tasks (name, location, status) VALUES ('" + name  + "', '" +
                    location + "', 'INCOMPLETE');");
        }
        catch (Exception exception)
        {
            return false;
        }

        return true;
    }

    // Function to edit a task.
    public boolean updateTask(int id, String name, String location, String status)
    {
        try
        {
            db.execSQL("UPDATE Tasks SET " +
                    "name = '" + name + "', " +
                    "location = '" + location + "', " +
                    "status = '" + status + "' " +
                    "WHERE taskID = " + id +
                    ";");
        }
        catch (Exception exception)
        {
            return false;
        }

        return true;
    }

    // Function to update a tasks status.
    public boolean updateTaskStatus(int id, String status)
    {
        try
        {
            db.execSQL("UPDATE Tasks SET " +
                    "status = '" + status + "' " +
                    "WHERE taskID = " + id +
                    ";");
        }
        catch (Exception exception)
        {
            return false;
        }

        return true;
    }

    // Function to delete a task.
    public boolean deleteTask(int id)
    {
        try
        {
            db.execSQL("DELETE FROM Tasks WHERE taskID = " + id + ";");
        }
        catch (Exception exception)
        {
            return false;
        }

        return true;
    }

    // Function to query all events.
    public String[][] queryAllEvents()
    {
        // Initalise variables.
        String[][] events;
        Cursor queryResult;
        int x = 0, y = 0; // x = record, y = field.

        // Execute query and store result.
        queryResult = db.rawQuery("SELECT * FROM Events ORDER BY year, month, day, hour, minute",
                null);

        // Allocate memory for 'records' 2d-array.
        events = new String[queryResult.getCount()][8];

        // Move cursor to item.
        queryResult.moveToFirst();

        // Iterate through query-result & store records in string-2d-array.
        for (int i = 0; i < (queryResult.getCount() * 8); i++)
        {
            events[x][y] = queryResult.getString(y);

            y++;

            if (y == 8)
            {
                y = 0;
                queryResult.moveToNext();
                x++;
            }
        }

        // Close query.
        if (queryResult != null && !queryResult.isClosed())
        {
            queryResult.close();
        }

        return events;
    }

    // Function to add an event.
    public boolean addEvent(String name, String minute, String hour, String day, String month,
                            String year, String location)
    {
        try
        {
            db.execSQL("INSERT INTO Events (name, minute, hour, day, month, year, location) VALUES" +
                    " ('" + name + "', " + minute + ", " + hour + ", " + day + ", " + month + ", " +
                    year + ", '" + location + "');");
        }
        catch (Exception exception)
        {
            return false;
        }

        return true;
    }

    // Function to delete an event.
    public boolean deleteEvent(int id)
    {
        try
        {
            db.execSQL("DELETE FROM Events WHERE eventID = " + id + ";");
        }
        catch (Exception exception)
        {
            return false;
        }

        return true;
    }

    // Function to clear all records.
    public void clearRecords()
    {
        db = helper.getWritableDatabase();
        db.delete("Friends", null, null);
        db.delete("Tasks", null, null);
        db.delete("Events", null, null);
    }

    // Extra.
    public class SQLHelper extends SQLiteOpenHelper
    {
        public SQLHelper (Context c)
        {
            super(c, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_TABLE_FRIENDS);
            db.execSQL(CREATE_TABLE_TASKS);
            db.execSQL(CREATE_TABLE_EVENTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w("Products table", "Upgrading database i.e. dropping table and re-creating it");
            db.execSQL("DROP TABLE IF EXISTS Friends");
            db.execSQL("DROP TABLE IF EXISTS Tasks");
            db.execSQL("DROP TABLE IF EXISTS Events");
            onCreate(db);
        }
    }
}