package com.sep.billardapp.example.billard_app_02;

/***
 * Class for billard table holes
 */
public class Hole {
    double x,y,r;
    double dist;

    public Hole (double x, double y){
        this.x=x;
        this.y=y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // better to put in this class or leave in MainActivity?
    public void initializeDistances(double[] chosenBallCoords){
            double dist = calcEukDistance(x - chosenBallCoords[0], y=chosenBallCoords[1]);
    }

    /***
     * calculates the 2D euclidian distance between two distances, distance1 and distance2.
     * @param distance1 distance from x1 to x2
     * @param distance2 distance from y1 to y2
     * @return
     */
    public static double calcEukDistance(double distance1, double distance2){
        double distance = Math.sqrt(distance1*distance1 + distance2*distance2);
        return distance;
    }
}
