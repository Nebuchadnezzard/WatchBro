package com.watchbro.watchbro.DataBase;
//package com.androidhive.androidsqlite;

/**
 * Created by charfi on 12/03/2018.
 */

public class Utilisateur {

    private int _idUtil;
    private String _nomUtil;

    // constructeur vide
    public Utilisateur(){
    }


    // constructeur
    public Utilisateur(int _idUtil, String _nomUtil){
        this._idUtil=_idUtil;
        this._nomUtil = _nomUtil;
    }

    // constructeur
    public Utilisateur(String _nomUtil){
        this._nomUtil = _nomUtil;
    }


    //getId
    public int getID(){
        return this._idUtil;
    }

    // setting id
    public void setID(int id){
        this._idUtil = id;
    }

    // getting name
    public String getName(){
        return this._nomUtil;
    }

    // setting name
    public void setName(String nom){
        this._nomUtil = nom;
    }


}
