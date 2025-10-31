import java.io.*;
import java.util.*;

public class Main {

    private static final int[] data_sizes = {1000, 10000, 100000};
    private static final int[] threads_total = {2, 4, 8};
    private static final int samples = 5;
    private static final String[] algorithms = {"bubble", "merge", "quick", "insertion"};
    private static final String CSV_FILE = "results.csv";

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            writer.println("Type,Algorithm,DataSize,Threads,Sample,Time(ms)");

            int totalTasks = data_sizes.length * algorithms.length * (1 + threads_total.length) * samples;
            int completedTasks = 0;

            System.out.println("=== Benchmark de Algoritmos de Ordenação ===");
            System.out.println("Total estimado de execuções: " + totalTasks);
            System.out.println("--------------------------------------------");

            for (int size : data_sizes) {
                int[] baseArray = generateRandomArray(size);
                System.out.println("\n➡️  Conjunto de dados: " + size + " elementos");

                for (String algorithm : algorithms) {

                    // SERIAL
                    for (int s = 1; s <= samples; s++) {
                        Serial serial = new Serial(baseArray);
                        long time = runSerial(serial, algorithm);

                        writer.printf("Serial,%s,%d,%d,%d,%d%n", algorithm, size, 1, s, time);
                        completedTasks++;
                        printProgress(completedTasks, totalTasks, algorithm, "Serial", 1, time);
                    }

                    // PARALELO
                    for (int threads : threads_total) {
                        for (int s = 1; s <= samples; s++) {
                            Parallel parallel = new Parallel(baseArray, threads);
                            long time = runParallel(parallel, algorithm);

                            writer.printf("Parallel,%s,%d,%d,%d,%d%n", algorithm, size, threads, s, time);
                            completedTasks++;
                            printProgress(completedTasks, totalTasks, algorithm, "Parallel", threads, time);
                        }
                    }
                }
            }

            System.out.println("\n✅ Benchmark finalizado! Resultados salvos em " + CSV_FILE);

        } catch (IOException e) {
            System.err.println("Erro ao gravar CSV: " + e.getMessage());
        }
    }


    private static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = rand.nextInt(size * 10);
        return arr;
    }

    private static long runSerial(Serial s, String algo) {
        return switch (algo) {
            case "bubble" -> s.bubbleSort();
            case "merge" -> s.mergeSort();
            case "quick" -> s.quickSort();
            case "insertion" -> s.insertionSort();
            default -> throw new IllegalArgumentException("Algoritmo desconhecido: " + algo);
        };
    }

    private static long runParallel(Parallel p, String algo) {
        return switch (algo) {
            case "bubble" -> p.bubbleSort();
            case "merge" -> p.mergeSort();
            case "quick" -> p.quickSort();
            case "insertion" -> p.insertionSort();
            default -> throw new IllegalArgumentException("Algoritmo desconhecido: " + algo);
        };
    }

    private static void printProgress(int done, int total, String algo, String type, int threads, long time) {
        float miliTime = (float) time/1000000;
        System.out.printf("Tarefa %d de %d \t| %-8s | %-9s | Threads: %-2d | Tempo: %.3f ms%n",
                done, total, type, algo, threads, miliTime);
    }
}

