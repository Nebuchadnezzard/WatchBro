package DataBase;

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
    private static final String DATABSE_NAME="AppMob.db";

    // nom table utilisateurs
    public static final String TABLE_UTILISATEUR = "utilisateur";
    // nom des colonnes de la table

    public static final String COLUMN_ID = "IdUtilisateur";
    public static final String COLUMN_NOMUTIL= "nom";


    //création de la base de données

    public SqLiteDB(Context context) {
        super(context, DATABSE_NAME, null, DATABSE_VERSION);
    }


    // création de tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_UTILISATEUR_TABLE = "CREATE TABLE" + TABLE_UTILISATEUR + "("
                + COLUMN_ID  + "INTEGER PRIMARY KEY," + COLUMN_NOMUTIL + " TEXT" + ")";

        db.execSQL(CREATE_UTILISATEUR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop les anciennes versions existantes
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UTILISATEUR);

        // recréer les tables
        onCreate(db);

    }

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
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Utilisateur utilisateur = new Utilisateur(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return utilisateur;
    }


    // supprimer un utilisateur
    public void deleteUtilisateur (Utilisateur utilisateur) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_UTILISATEUR, COLUMN_ID + " = ?",
                new String[] { String.valueOf(utilisateur.getID()) });
        db.close();
    }


    // Updating  user
    public int updateUtilisateur (Utilisateur utilisateur) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMUTIL, utilisateur.getName());

        // updating ligne
        return db.update(TABLE_UTILISATEUR, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(utilisateur.getID()) });
    }


}
