package com.watchbro.watchbro.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by charfi on 08/03/2018.
 */

public class SqLiteDB extends SQLiteOpenHelper{

    // version de la base de donnéeq
    private static final int DATABSE_VERSION= 1;
    //nom de la base de données
    private static final String DATABSE_NAME="WatchBro.db";

    // nom table utilisateurs
    public static final String TABLE_UTILISATEUR = "utilisateur";
    // nom des colonnes de la table
    public static final String COLUMN_ID = "IdUtilisateur";
    public static final String COLUMN_NOMUTIL= "nom";


    // nom table NbPas
    public static final String TABLE_PAS = "Pas";
    // nom des colonnes de la table
    public static final String COLUMN_NBPAS = "nbPas";
    public static final String COLUMN_DATEPAS = "datePas";

    // nom table cardio
    public static final String TABLE_CARDIO = "cardio";
    // nom des colonnes de la table
    public static final String COLUMN_NBCARDIO = "NbCardio";
    public static final String COLUMN_DATECARDIO= "dateCardio";

    //création de la base de données
    public SqLiteDB(Context context) {

        super(context, DATABSE_NAME, null, DATABSE_VERSION);
    }


    //requête création de tables
    public static final String CREATE_UTILISATEUR_TABLE = "CREATE TABLE" + TABLE_UTILISATEUR + "("
            + COLUMN_ID  + "INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NOMUTIL + " TEXT NOT NULL" + ")";

    public static final String CREATE_PAS_TABLE = "CREATE TABLE" + TABLE_PAS + "("
            + COLUMN_NBPAS  + "INTEGER," + COLUMN_DATEPAS + " TEXT NOT NULL, util_id INTEGER," +
            " FOREIGN KEY(util_id) REFERENCES" + TABLE_UTILISATEUR +"(" + COLUMN_ID + ")" + ")";

    public static final String CREATE_CARDIO_TABLE = "CREATE TABLE" + TABLE_CARDIO + "("
            + COLUMN_NBCARDIO  + "INTEGER," + COLUMN_DATECARDIO + " TEXT NOT NULL, util_id INTEGER," +
            " FOREIGN KEY(util_id) REFERENCES" + TABLE_UTILISATEUR +"(" + COLUMN_ID + ")" + ")";

    // création de tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_UTILISATEUR_TABLE);
        db.execSQL(CREATE_PAS_TABLE);
        db.execSQL(CREATE_CARDIO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop les anciennes versions existantes
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UTILISATEUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDIO);

        // recréer les tables
        onCreate(db);

    }

    /*UTILISATEUR*/

    // ajouter un nouvel utilisateur
    public void addUtil(Utilisateur utilisateur) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues newutil = new ContentValues();
        newutil.put(COLUMN_NOMUTIL, utilisateur.getName());

        //inserérer dans la table
        db.insert(TABLE_UTILISATEUR, null, newutil);
        db.close();
    }

    // get Utilisateur
    public Utilisateur getUtilisateur(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_UTILISATEUR, new String[] { COLUMN_ID,
                        COLUMN_NOMUTIL}, COLUMN_ID + "=?",
                        new String[] {
                            String.valueOf(id)
                        }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Utilisateur utilisateur = new Utilisateur(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        // return Utilisateur
        return utilisateur;
    }


    // supprimer un utilisateur
    public void deleteUtilisateur (Utilisateur utilisateur) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_UTILISATEUR, COLUMN_ID + " = ?",
                new String[] { String.valueOf(utilisateur.getID()) });
        db.close();
    }


    // mettre à jour un utilisateur
    public int updateUtilisateur (Utilisateur utilisateur) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMUTIL, utilisateur.getName());

        // updating ligne
        return db.update(TABLE_UTILISATEUR, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(utilisateur.getID()) });
    }




}
