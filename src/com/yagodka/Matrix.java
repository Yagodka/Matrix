package com.yagodka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Matrix {

    private int[][] value;

    private Matrix(int[][] value) {
        this.value = value;
    }

    Matrix(int rows, int cols) {
        Random r = new Random(0);
        value = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.value[i][j] = r.nextInt();
            }
        }
    }

    Matrix(String fileName) {

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String thisLine;
            // header
            if ((thisLine = br.readLine()) != null) {
                String[] header = thisLine.split(" ");
                value = new int[Integer.parseInt(header[0])][Integer.parseInt(header[1])];
                br.readLine();
            }

            // data
            int i = 0;
            while ((thisLine = br.readLine()) != null) {
                if (thisLine.trim().equals("")) {
                    break;
                } else {
                    String[] lineArray = thisLine.split("\t");
                    for (int j = 0; j < lineArray.length; j++) {
                        value[i][j] = Integer.parseInt(lineArray[j]);
                    }
                }
                i++;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
    }

    int[][] getValue() {
        return value;
    }

    private int rows() {
        return value.length;
    }

    private int cols() {
        return value[0].length;
    }

    Matrix multiply(Matrix other, int parallelism) {

        int[][] result = new int[rows()][cols()];
        ExecutorService executor = Executors.newFixedThreadPool(parallelism);
        List<Future<int[][]>> callables = new ArrayList<>();

        int parts = Math.max(rows() / parallelism, 1);

        for (int i = 0; i < rows(); i += parts) {
            Callable<int[][]> worker = new PartMultiplier(value, other.getValue(), i, i + parts);
            callables.add(executor.submit(worker));
        }

        // retrieve the result from parts
        int start = 0;
        for (Future<int[][]> future : callables) {
            try {
                System.arraycopy(future.get(), start, result, start, parts);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            start += parts;
        }
        executor.shutdown();

        return new Matrix(result);
    }

    private class PartMultiplier implements Callable<int[][]> {

        int[][] a, b, c;
        int start, end;

        PartMultiplier(int[][] a, int[][] b, int s, int e) {
            this.a = a;
            this.b = b;
            this.c = new int[a.length][b[0].length];
            start = s;
            end = e;
        }

        @Override
        public int[][] call() {
            System.err.println(Thread.currentThread().getName());
            for (int i = start; i < end; i++) {
                for (int k = 0; k < b.length; k++) {
                    for (int j = 0; j < b[0].length; j++) {
                        c[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
            return c;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(value.length);
        for (int[] row : value) {
            int i = 0;
            for (int number : row) {
                if (i != 0) {
                    sb.append("\t");
                } else {
                    i++;
                }
                sb.append(number);
            }
            sb.append("\n\r");
        }
        return sb.toString();
    }
}
