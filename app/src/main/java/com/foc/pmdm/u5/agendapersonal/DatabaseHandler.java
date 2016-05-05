package com.foc.pmdm.u5.agendapersonal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javacasm on 03/12/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    static int DATABASE_VERSION = 1;

    static String DATABASE_NAME="contactosDB";

    String TABLE_CONTACTS="contatos";

    String COLUMNA_ID="id";
    String COLUMNA_NAME="nombre";
    String COLUMNA_PHONE="telefono";

    public DatabaseHandler(Context context)
    {
        // Si no existe se llamará a onCreate, si existe y es antiero a onUpgrade
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    // Crea las tablas si no existe
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String strCreateTablas="CREATE TABLE "+TABLE_CONTACTS+
                "(" +COLUMNA_ID+" INTEGER PRIMARY KEY, "+COLUMNA_NAME+" TEXT, "+COLUMNA_PHONE+" TEXT "+")"; // sentencia de creacion

        db.execSQL(strCreateTablas);
    }

    // Actualiza la estructura de la base de datos si es antigua
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL("DROP TABLE "+TABLE_CONTACTS);

        onCreate(db);
    }


    // Adding new contact
    void addContact(Contacto contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMNA_NAME, contact.getName()); // Contact Name
        values.put(COLUMNA_PHONE, contact.getPhoneNumber()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Updating single contact
    public int updateContact(Contacto contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMNA_NAME, contact.getName());
        values.put(COLUMNA_PHONE, contact.getPhoneNumber());

        // updating row
        return db.update(TABLE_CONTACTS, values, COLUMNA_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }


    // Deleting single contact
    public void deleteContact(Contacto contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, COLUMNA_ID + " = ?",
                new String[]{String.valueOf(contact.getID())});
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    Contacto getContact(String id)
    {

        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.query(TABLE_CONTACTS,new String[]{COLUMNA_ID,COLUMNA_NAME,COLUMNA_PHONE},
                COLUMNA_ID +" =?",new String[]{id},null,null,null );

        if(cursor!=null)
        {
            cursor.moveToFirst();
            Contacto contacto=new Contacto(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
                    cursor.getString(2));
            return contacto;
        }
        return null;
    }

    // Getting All Contacts
    public List<Contacto> getAllContacts() {
        List<Contacto> contactList = new ArrayList<Contacto>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contacto contact = new Contacto();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // return contact list
        return contactList;

    }

}
