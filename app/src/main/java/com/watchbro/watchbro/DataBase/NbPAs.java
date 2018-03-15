package com.watchbro.watchbro.DataBase;

import java.util.Date;

/**
 * Created by nawel on 15/03/2018.
 */

public class NbPAs {

    private int nbpas;
    private Date datepas;

    public NbPAs(int nbpas, Date datepas){
        this.nbpas=nbpas;
        this.datepas=datepas;
    }

    public int getNbpas(){ return this.nbpas;}
    public void setNbpas(int nbpas){ this.nbpas=nbpas;}

    public Date getDatepas(){ return this.datepas;}
    public void setDatepas(Date datepas){ this.datepas=datepas;}






}
