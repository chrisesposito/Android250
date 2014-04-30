// DatabaseConnector.java
// Provides easy connection and creation of UserContacts database.
package com.fsq.homework252;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseConnector 
{
   private static final String DATABASE_NAME = "Homework2_DB2";
      
   private SQLiteDatabase database; 
   private DatabaseOpenHelper databaseOpenHelper; 


   public DatabaseConnector(Context context) 
   {
      databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
   }

   // open the database connection
   public void open() throws SQLException 
   {
      database = databaseOpenHelper.getWritableDatabase();
   }

   // close the database connection
   public void close() 
   {
      if (database != null)
         database.close(); 
   } 

   // inserts a new contact in the database
   public long insertContact(String name, String email) 
   {
      ContentValues newContact = new ContentValues();
      newContact.put("name", name);
      newContact.put("email", email);

      open(); 
      long rowID = database.insert("contacts", null, newContact);
      close(); 
      return rowID;
   } 

   // updates an existing contact in the database
   public void updateContact(long id, String name, String email) 
   {
      ContentValues editContact = new ContentValues();
      editContact.put("name", name);
      editContact.put("email", email);

      open(); 
      database.update("contacts", editContact, "_id=" + id, null);
      close(); 
   } 

   // return a Cursor with all contact names in the database
   public Cursor getAllContacts() 
   {
      return database.query("contacts", new String[] {"_id", "name"}, 
         null, null, null, null, "name");
   } 

   // return a Cursor containing specified contact's information 
   public Cursor getOneContact(long id) 
   {
      return database.query(
         "contacts", null, "_id=" + id, null, null, null, null);
   } 

   // delete the contact specified by the given id 
   public void deleteContact(long id) 
   {
      open(); 
      database.delete("contacts", "_id=" + id, null);
      close(); 
   } 
   
   private class DatabaseOpenHelper extends SQLiteOpenHelper 
   {
      public DatabaseOpenHelper(Context context, String name,
         CursorFactory factory, int version) 
      {
         super(context, name, factory, version);
      }

      // creates the contacts table when the database is created
      @Override
      public void onCreate(SQLiteDatabase db) 
      {
         // query to create a new table named contacts
         String createQuery = "CREATE TABLE contacts" +
            "(_id integer primary key autoincrement," +
            "name TEXT, email TEXT);";
                  
         db.execSQL(createQuery); // execute query to create the database
      } 

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
      }
   } 
} 

