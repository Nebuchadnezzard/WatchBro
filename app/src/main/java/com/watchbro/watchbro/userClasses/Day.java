package com.watchbro.watchbro.userClasses;

/**
 * Created by couderc on 14/03/2018.
 */

public class Day {
    public int numJour;
    public int nbPas;
    public int nbBPMMoyen;

    public Day() {

    }

    public Day(int nbPas, int nbBPMMoyen, int numJour) {
        this.nbPas = nbPas;
        this.nbBPMMoyen = nbBPMMoyen;
        this.numJour = numJour;
    }
}
