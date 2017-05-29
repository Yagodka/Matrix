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
        timedTest(1000, 1);
        timedTest(1000, 2);
        timedTest(1000, 4);
        timedTest(1000, 8);

        timedTest(2000, 1);
        timedTest(2000, 2);
        timedTest(2000, 4);
        timedTest(2000, 8);

        timedTest(4000, 1);
        timedTest(4000, 2);
        timedTest(4000, 4);
        timedTest(4000, 8);
    }

    private static void timedTest(int dimension, int parallelism) {

        Matrix mr1 = new Matrix(dimension, dimension);
        Matrix mr2 = new Matrix(dimension, dimension);

        long start = System.nanoTime();
        int dim = mr1.multiply(mr2, parallelism).getValue().length;
        long end = System.nanoTime();
        double seconds = (double)(end - start) / 1000000000.0;
        System.out.println("Dim: " + dim + ", time: " + seconds + " s. Parallelism: " + parallelism);
    }
}
