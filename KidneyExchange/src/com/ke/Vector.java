package com.ke;

import java.util.Random;

public class Vector {
    
    public enum TYPE {
            BLUE,
            ORANGE,
            GREEN,
            RED,
            BLACK
    }
    
    public TYPE donorType;
    public TYPE patientType;
    public int number;
    
    public Vector(int num) {
        this.number = num;
        this.donorType = randomType();
        this.patientType = randomType();
        while (donorType == patientType) {
            this.patientType = randomType();
        }
    }
    
    public Vector(int num, TYPE donorType, TYPE patientType) {
        this.number = num;
        this.donorType = donorType;
        this.patientType = patientType;
    }
    
    public TYPE randomType() {
        Random ran = new Random();
        TYPE type;
        int num = ran.nextInt(5);
        switch (num) {
        case 0:
                type = TYPE.BLUE;
                return type;
        case 1:
                type = TYPE.ORANGE;
                return type;
        case 2:
                type = TYPE.GREEN;
                return type;
        case 3:
                type = TYPE.RED;
                return type;
        case 4:
                type = TYPE.BLACK;
                return type;
        default:
                type = null;
                return type;
        }
    }

}
