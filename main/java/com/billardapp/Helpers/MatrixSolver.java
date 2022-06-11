package com.sep.billardapp.Helpers;

/***
 * Solves for the solution vector, with Matrix A and vector B as inputs (Ax=b --> x as solution vector)
 */
public class MatrixSolver {
    /***
     * Solves for the solution vector, with Matrix A and vector B as inputs (Ax=b --> x as solution vector)
     */
    public void solve(double[][] A, double[] B, double[] solutionVector)
    {
        int N = B.length;
        for (int k = 0; k < N; k++)
        {
            int max = k;
            for (int i = k + 1; i < N; i++)
                if (Math.abs(A[i][k]) > Math.abs(A[max][k]))
                    max = i;

            double[] temp = A[k];
            A[k] = A[max];
            A[max] = temp;

            double t = B[k];
            B[k] = B[max];
            B[max] = t;

            for (int i = k + 1; i < N; i++)
            {
                double factor = A[i][k] / A[k][k];
                B[i] -= factor * B[k];
                for (int j = k; j < N; j++)
                    A[i][j] -= factor * A[k][j];
            }
        }

        for (int i = N - 1; i >= 0; i--)
        {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++)
                sum += A[i][j] * solutionVector[j];
            solutionVector[i] = (B[i] - sum) / A[i][i];
        }
    }
}