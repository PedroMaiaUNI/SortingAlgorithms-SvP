import java.util.Arrays;

public class Parallel {
    static int size;
    static int[] SortArray;
    static int numThreads;

    public Parallel(int[] baseArray, int nThreads) {
        SortArray = baseArray.clone();
        size = SortArray.length;
        numThreads = nThreads;
    }   

    private static void print(int[] array, long time) { 
        System.out.println(Arrays.toString(array));
        System.out.println("TEMPO: "+ time + "ms");
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
    //  4. fecha e print cronometro e array organizado

    public static void bubbleSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        bubbleParallel(resultArray);

        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }

    private static void bubbleParallel(int[] array){
        //Odd-Even Transposition Sort:
        //valores são comparados em pares ao mesmo tempo
        //a cada passo, altera entre (indice par, indice impar) para (indice impar, indice par)
        for (int phase = 0; phase < size; phase++) {
            int startIndex = (phase % 2 == 0) ? 0 : 1;

            Thread[] threads = new Thread[numThreads];
            int chunkSize = (int) Math.ceil((size / 2) / numThreads);

            for (int t = 0; t < numThreads; t++) {
                int startPair = t * chunkSize;
                int endPair;
                if (t == numThreads-1){
                    endPair = size/2;
                }else{
                    endPair = startPair + chunkSize;
                }

                threads[t] = new Thread(() -> {
                    for (int i = startIndex + 2 * startPair; i < Math.min(size - 1, startIndex + 2 * endPair); i += 2) {
                        if (array[i] > array[i + 1]) {
                            //bubble:
                            int temp = array[i];
                            array[i] = array[i + 1];
                            array[i + 1] = temp;
                        }
                    }
                });
                threads[t].start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void insertionSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        insertionParallel(resultArray);

        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }

    private static void insertionParallel(int[] array){
        //Esse algoritmo é dado como impossível de se paralelizar
        //Ainda assim, vou fazer uma leve tentativa de implementar
        // só para verificar se há algum ganho de desempenho
        //paralelizando a busca pelo indice a ser colocado o valor

        for (int i = 1; i < size; i++) {
            int key = array[i];
            int chunkSize = Math.max(1, i / numThreads);
            Thread[] threads = new Thread[numThreads];
            int[] localBest = new int[numThreads];
            Arrays.fill(localBest, -1); 

            for (int t = 0; t < numThreads; t++) {
                final int threadId = t;
                int startIdx = t * chunkSize;
                int endIdx = Math.min(startIdx + chunkSize, i);

                threads[t] = new Thread(() -> {
                    for (int j = endIdx - 1; j >= startIdx; j--) {
                        if (array[j] <= key) {
                            localBest[threadId] = j;
                            break;
                        }
                    }
                });
                threads[t].start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int bestIndex = -1;
            for (int idx : localBest) {
                if (idx > bestIndex) bestIndex = idx;
            }

            for (int j = i - 1; j > bestIndex; j--) {
                array[j + 1] = array[j];
            }
            array[bestIndex + 1] = key;
        }
    }

    public static void mergeSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        mergeParallel(resultArray, 0, size-1);
        
        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }  

    public static void mergeParallel(int[] array, int left, int right){
        if (left < right) {
            int mid = (left + right) / 2;

            Thread leftThread = new Thread(() -> mergeParallel(array, left, mid));
            Thread rightThread = new Thread(() -> mergeParallel(array, mid + 1, right));

            leftThread.start();
            rightThread.start();
            try {
                leftThread.join();
                rightThread.join();
            } 
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Serial.merge(array, left, mid, right);
        }

    }

    public static void quickSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        quickParallel(resultArray, 0, size -1, numThreads);
        
        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }

    private static void quickParallel(int[] array, int low, int high, int nThreads){
        if (low < high) {
            int pivot = Serial.partitioning(array, low, high);

            if(nThreads <= 1){
                quickParallel(array, low, pivot - 1, 1);
                quickParallel(array, pivot + 1, high, 1);
            } else{
                Thread left = new Thread(() -> quickParallel(array, low, pivot - 1, nThreads/2));
                Thread right = new Thread(() -> quickParallel(array, pivot + 1, high, nThreads/2));
                left.start();
                right.start();
                try {
                    left.join();
                    right.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }   
    }
}
