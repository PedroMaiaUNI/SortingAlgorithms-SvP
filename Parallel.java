import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.IntStream;

public class Parallel {
    static int size;
    static int[] SortArray;
    static int numThreads;
    static ForkJoinPool pool;

    public Parallel(int[] baseArray, int nThreads) {
        SortArray = baseArray.clone();
        size = SortArray.length;
        numThreads = nThreads;
        pool = new ForkJoinPool(numThreads);
    }   

    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                System.err.printf("❌ Erro: array não está ordenado na posição %d (valores %d e %d)%n",
                        i - 1, arr[i - 1], arr[i]);
                return false;
            }
        }
        return true;
    }

    public void run() {
        System.out.println("--- EXECUTANDO ALGORITMOS PARALELOS ---");

        System.out.println("1. BUBBLE SORT");
        bubbleSort();
        System.out.println("2. INSERTION SORT");
        insertionSort();
        System.out.println("3. MERGE SORT");
        mergeSort();
        System.out.println("4. QUICK SORT");
        quickSort();


        System.out.println("\n--- FIM DAS EXECUÇÕES PARALELAS --- ");
    }

    //estrutura basica das funções:
    //  1. monta o novo array que vai ser retornado
    //  2. inicia cronometro
    //  3. logica do algoritmo
    //  4. retorna cronometro

    public static long bubbleSort(){
        int[] resultArray = SortArray.clone();
        long start = System.nanoTime();

        bubbleParallel(resultArray);

        long end = System.nanoTime();
        isSorted(resultArray);
        return (end - start) ;
    }

    private static void bubbleParallel(int[] array){
        //Odd-Even Transposition Sort:
        //valores são comparados em pares ao mesmo tempo
        //a cada passo, altera entre (indice par, indice impar) para (indice impar, indice par)
        ForkJoinPool pool = new ForkJoinPool(numThreads);

        for (int phase = 0; phase < size; phase++) {
            int startIndex = (phase % 2 == 0) ? 0 : 1;

            pool.submit(() -> 
                IntStream.range(startIndex, size - 1)
                        .parallel()
                        .filter(i -> i % 2 == startIndex % 2)
                        .forEach(i -> {
                            if (array[i] > array[i + 1]) {
                                int tmp = array[i];
                                array[i] = array[i + 1];
                                array[i + 1] = tmp;
                            }
                        })
            ).join();
        }

        pool.shutdown();
    }

    public static long insertionSort(){
        int[] resultArray = SortArray.clone();
        long start = System.nanoTime();

        insertionParallel(resultArray);

        long end = System.nanoTime();
        isSorted(resultArray);
        return (end - start) ;
    }

    private static void insertionParallel(int[] array){
        //Esse algoritmo é dado como impossível de se paralelizar
        //Ainda assim, vou fazer uma leve tentativa de implementar
        // só para verificar se há algum ganho de desempenho
        //paralelizando a busca pelo indice a ser colocado o valor

        ForkJoinPool pool = new ForkJoinPool(numThreads);

        for (int i = 1; i < size; i++) {
            int key = array[i];

            int threadsToUse = Math.min(numThreads, i);
            int chunkSize = (int) Math.ceil(i / (double) threadsToUse);

            int[] localBest = new int[threadsToUse];
            Arrays.fill(localBest, -1);

            int index = i;

            pool.submit(() ->
                IntStream.range(0, threadsToUse).parallel().forEach(t -> {
                    int start = t * chunkSize;
                    int end = Math.min(start + chunkSize, index);

                    int low = start, high = end - 1, found = -1;
                    while (low <= high) {
                        int mid = (low + high) >>> 1;
                        if (array[mid] <= key) {
                            found = mid;
                            low = mid + 1;
                        } else {
                            high = mid - 1;
                        }
                    }
                    localBest[t] = found;
                })
            ).join();

            int bestIndex = -1;
            for (int idx : localBest) if (idx > bestIndex) bestIndex = idx;

            for (int j = i - 1; j > bestIndex; j--) array[j + 1] = array[j];
            array[bestIndex + 1] = key;
        }

        pool.shutdown();
    }

    public static long mergeSort(){
        int[] resultArray = SortArray.clone();
        long start = System.nanoTime();

        pool.invoke(new MergeParallel(resultArray, 0, size - 1));
        
        long end = System.nanoTime();
        isSorted(resultArray);
        return (end - start) ;
    }  

    static class MergeParallel extends RecursiveAction {
        int[] array;
        int left, right;
        static final int THRESHOLD = 10_000;

        MergeParallel(int[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (right - left < THRESHOLD) {
                Serial.mergeSort(array, left, right);
                return;
            }
            int mid = (left + right) / 2;
            MergeParallel t1 = new MergeParallel(array, left, mid);
            MergeParallel t2 = new MergeParallel(array, mid + 1, right);
            invokeAll(t1, t2);
            Serial.merge(array, left, mid, right);
        }
    }

    public static long quickSort(){
        int[] resultArray = SortArray.clone();
        long start = System.nanoTime();

        pool.invoke(new QuickParallel(resultArray, 0, size - 1));
        
        long end = System.nanoTime();
        isSorted(resultArray);
        return (end - start) ;
    }

    static class QuickParallel extends RecursiveAction {
        int[] array;
        int low, high;
        static final int THRESHOLD = 10_000;

        QuickParallel(int[] array, int low, int high) {
            this.array = array;
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
            if (high - low < THRESHOLD) {
                Serial.quickSort(array, low, high);
                return;
            }
            int pivot = Serial.partitioning(array, low, high);
            QuickParallel left = new QuickParallel(array, low, pivot - 1);
            QuickParallel right = new QuickParallel(array, pivot + 1, high);
            invokeAll(left, right);
        }
    }
}
