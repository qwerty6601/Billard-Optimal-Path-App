package com.sep.billardapp.example.billard_app_02;

import com.sep.billardapp.Helpers.RootCalc;

public class redBallVector {
    // Dummy Coordinates

    // not making chosenBall class w/ own x, y coordinates bc dont need to know ball, just need it x,y coords?
    double posRedX;
    double posRedY;
    double posWhiteX;
    double posWhiteY;
    double sin, cos;
    double velWX, velWY;
    double velRX;
    double velRY;
    double comX, comY;


    public redBallVector(double posRedX, double PosRedY, double posWhiteX, double posWhiteY) {
        this.posRedX = posRedX;
        this.posRedY = PosRedY;
        this.posWhiteX = posWhiteX;
        this.posWhiteY = posWhiteY;
    }

    public void contactPointFinder(){
        double a = velWX * velWX + velWY*velWY;
        double b = 2*(posWhiteX - posRedX)* velWX + 2*(posWhiteY - posRedY)*velWY;
        double c = (posWhiteX - posRedX)*(posWhiteX - posRedX) + (posWhiteY - posRedY)*(posWhiteY - posRedY);
        double t= RootCalc.findRoots(a, b, c); // good findRoots is static bc don't need to make instance rite?
        double distanceX = velWX *t;
        double distanceY = velWY*t;

        posWhiteX += distanceX;
        posWhiteY = distanceY;
    }

    public void rotateRedBall() {
        double l = Math.sqrt(Math.abs(posRedX-posWhiteX) * Math.abs(posRedX-posWhiteX) + Math.abs(posRedY-posWhiteY) * Math.abs(posRedY-posWhiteY));
        sin = Math.abs(posRedY-posWhiteY)/l;
        cos = Math.abs(posRedX-posWhiteX)/l;

        posRedX = cos*posRedX + sin*posRedY;
        posRedY = -sin*posRedX + cos*posRedY;
    }

    // 3
    public void adjustVelocity(){
        comX = velWX /2;
        comY = velWY/2;

        velWX /= 2;
        velWY /=2;

        velRX = velWX /2;
        velRY = velWY/2;
    }

    // 4: bounce of y-axis
    public void changeX(){
        velWX= -velWX;
        velRX = -velRX;
    }

    // 5. undo boost
    public void undoBoost(){
        velRX += comX;
        velRY += comY;

        velWX += comX;
        velWY += comY;
    }

    // 6. rotate velR back to orig Matrix
    public void rotateBack(){
        velRX = cos*posRedX - sin*posRedY;
        velRX = sin*posRedX + cos*posRedY;
    }


}
