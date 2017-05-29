package com.yagodka;

public class Main {

    public static void main(String[] args) {

        // For test data
        Matrix m1 = new Matrix("m3x3.csv");
        Matrix m2 = new Matrix("m3x3.csv");
        int threads = Runtime.getRuntime().availableProcessors();

        System.out.println(m1);
        System.out.println(m2);

        long start = System.nanoTime();
        Matrix m3 = m1.multiply(m2, threads);
        long end = System.nanoTime();
        double seconds = (double)(end - start) / 1000000000.0;

        System.out.println(m3);
        System.out.println("Time: " + seconds + " s. Number of treads: " + threads);

        // For big matrices with random data
        timedTest(1, 1000);
        timedTest(2, 1000);
        timedTest(4, 1000);
        timedTest(8, 1000);

        timedTest(1, 2000);
        timedTest(2, 2000);
        timedTest(4, 2000);
        timedTest(8, 2000);

        timedTest(1, 4000);
        timedTest(2, 4000);
        timedTest(4, 4000);
        timedTest(8, 4000);
    }

    private static void timedTest(int threads, int dimension) {

        Matrix mr1 = new Matrix(dimension, dimension);
        Matrix mr2 = new Matrix(dimension, dimension);

        long start = System.nanoTime();
        int dim = mr1.multiply(mr2, threads).getValue().length;
        long end = System.nanoTime();
        double seconds = (double)(end - start) / 1000000000.0;
        System.out.println("Dim: " + dim + ", time: " + seconds + " s. Treads: " + threads);
    }
}
