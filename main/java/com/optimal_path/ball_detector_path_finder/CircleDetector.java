package com.sep.billardapp.example.billard_app_02;

import com.sep.billardapp.Helpers.MatrixSolver;

/***
 * detects whether sets of pixels which have significant color difference are circles or not
 */
public class CircleDetector {

    // just to calculate totalXX, totalXY
    double totalX;
    // just to caculate totalYY, totalXY
    double totalY;
    // for input into matrix
    double totalXX;
    // for input into matrix
    double totalXY;
    // for input into matrix
    double totalYY;
    // for input into matrix
    double one;
    // for input into vector B
    double rightRowOne;
    // for input into vector B
    double rightRowTwo;
    // for input into vector B
    double rightRowThree;

    // the matrix which will contain the values, which when combined with vector B, can be used to solve for the solution vector
    double [][] matrix= new double[3][3];
    double[] B= new double[3];
    MatrixSolver mSolver = new MatrixSolver();

    // circle center's x coordinate
    public double cx;
    // circle center's y coordinate
    public double cy;

    // circle center's radius
    public double r;
    int counter;

    /***
     * uses solve method of MatrixSolver instance to solve for the solution vector x from Matrix matrix and vector B
     * (Matrix*x = B). This components of the solution vector are then used to find the x,y coordinates of circle center
     * as well as the circle's radius
     */
    public void fit() {
        positEquationSystem();
        double[] solutionVector= new double[3];
        mSolver.solve(matrix, B, solutionVector);

        /*positEquationSystem();
        for(int i=0; i<3; i++){
            double sum=0;
            for (int j=0; j<3; j++){
                sum += matrix[i][j]*solutionVector[j];
            }
            System.out.println(sum-B[i]);
        } */

        double a = solutionVector[0];
        double b = solutionVector[1];
        double c = solutionVector[2];

        cx=a/-2;  // center's x-coordinate
        cy=b/-2;  // center's y-coordinate
        r= Math.sqrt(cx*cx + cy*cy -c); // radius

    }

    /***
     * sets matrix and vector values
     */

    /***
     * ( xx xy x ) = 0
     * ( xy yy y ) = 0
     * ( x  y  1 ) = 0
     */
    private void positEquationSystem(){
        matrix[0][0]= totalXX;
        matrix[0][1]= totalXY;
        matrix[0][2]= totalX;
        matrix[1][0]= totalXY;
        matrix[1][1]= totalYY;
        matrix[1][2]= totalY;
        matrix[2][0]= totalX;
        matrix[2][1]= totalY;
        matrix[2][2]= one;

        B[0]= rightRowOne;
        B[1]= rightRowTwo;
        B[2]= rightRowThree;

    }

    /***
     * resets the values
     */
    public void reset() {
        rightRowOne=0;
        rightRowTwo=0;
        rightRowThree=0;
        totalX=0;
        totalY=0;
        totalXX=0;
        totalXY=0;
        totalYY=0;
        one=0;
        counter=0;

    }

    /***
     * adds the values (derived from x,y) * weight for each circle to find totalX, totalY, totalXX, totalYY,
     * totalXY, one, rightRowOne, rightRowTwo, and rightRowThree. These values are needed to detect circles
     * by finding min_{a, b, c} of sum (x² + y² + a*x + b*y + c)²
     */

    /***
     * (x-cx)^2 + (y-cy)^2 = r^2
     * x^2 - 2xcx + cx^2 + y^2 -2ycy + cy^2 = r^2
     * x^2 + y^2 + (2cx)x + (-2cy)y +
     * x^2 + y^2 + ax + by + c = 0  (*)
     * min (*)
     * d(*)/da = ax^2 + bxy + cx + C
     * d(*)/db =
     * d(*)/dc =
     *
     *
     */
    public void add(double x, double y, double weight) {
        // one coeff is the sum of the x's
        totalX+=x*weight;
        totalY+=y*weight;
        totalXX+=x*x*weight;
        totalYY+=y*y*weight;
        totalXY+=x*y*weight;
        one+=weight;

        rightRowOne+= (-1*x*x*x-x*y*y)*weight;
        rightRowTwo+= (-1*x*x*y - y*y*y)*weight;
        rightRowThree+= (-1*x*x-1*y*y)*weight;

        counter++;

    }

}