package com.sep.billardapp.Helpers;

/***
 * finds the roots of parabolic equation
 */
public class RootCalc {

    /***
     * finds the roots of parabolic equation
     * @param a parameter for equation t = (-b +- sqrt (b^2 -4ac))/2a
     * @param b parameter for equation t = (-b +- sqrt (b^2 -4ac))/2a
     * @param c parameter for equation t = (-b +- sqrt (b^2 -4ac))/2a
     * @return roots
     */
    public static double findRoots(double a, double b, double c) {
        double root1, root2;

        double determinant = b*b-4*a*c;

        if (determinant > 0) {

            root1=(-b + Math.sqrt(determinant))/(2 * a);
            root2=(-b - Math.sqrt(determinant))/(2 * a);

            System.out.format("root1 = %.2f and root2 = %.2f", root1, root2);

            if (root1<root2)
                return root1;
            else
                return root2;
        }

        else if (determinant == 0) {
            root1 = root2 = -b/(2 * a);
            System.out.format("root1 = root2 = %.2f;", root1);
        }
        return 0;
    }
}