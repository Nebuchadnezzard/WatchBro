package com.watchbro.watchbro.DataBase;

import java.util.Date;

/**
 * Created by nawel on 15/03/2018.
 */

public class Cardio {

    public class NbPAs {

        private int cardio;
        private Date datecardio;

        public NbPAs(int cardio, Date datecardio){
            this.cardio=cardio;
            this.datecardio=datecardio;
        }

        public int getNbpas(){ return this.cardio;}
        public void setNbpas(int cardio){ this.cardio=cardio;}

        public Date getDatepas(){ return this.datecardio;}
        public void setDatepas(Date datecardio){ this.datecardio=datecardio;}

    }

}
