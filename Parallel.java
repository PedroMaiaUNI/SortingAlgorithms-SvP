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

        for (int i = 1; i < size; i++) {
            int key = array[i];

            int threadsToUse = Math.min(numThreads, i); // não usar mais threads que elementos
            if (threadsToUse <= 0) threadsToUse = 1;

            int chunkSize = (int) Math.ceil(i / (double) threadsToUse);

            Thread[] threads = new Thread[threadsToUse];
            int[] localBest = new int[threadsToUse];
            for (int t = 0; t < threadsToUse; t++) localBest[t] = -1;

            for (int t = 0; t < threadsToUse; t++) {
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

            for (int t = 0; t < threadsToUse; t++) {
                try { threads[t].join(); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }

            int bestIndex = -1;
            for (int t = 0; t < threadsToUse; t++) {
                if (localBest[t] > bestIndex) bestIndex = localBest[t];
            }

            for (int j = i - 1; j > bestIndex; j--) {
                array[j + 1] = array[j];
            }
            array[bestIndex + 1] = key;
        }
    }

    public static long mergeSort(){
        int[] resultArray = SortArray.clone();
        long start = System.nanoTime();

        mergeParallel(resultArray, 0, size-1, numThreads);
        
        long end = System.nanoTime();
        isSorted(resultArray);
        return (end - start) ;
    }  

    public static void mergeParallel(int[] array, int left, int right, int nThreads){
        if (left < right) {
            if (nThreads <= 1) {
                Serial.mergeSort(array, left, right);
                return;
            }

            int mid = (left + right) / 2;

            Thread leftThread = new Thread(() -> mergeParallel(array, left, mid, nThreads/2));
            Thread rightThread = new Thread(() -> mergeParallel(array, mid + 1, right, nThreads/2));

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

    public static long quickSort(){
        int[] resultArray = SortArray.clone();
        long start = System.nanoTime();

        quickParallel(resultArray, 0, size -1, numThreads);
        
        long end = System.nanoTime();
        isSorted(resultArray);
        return (end - start) ;
    }

    private static void quickParallel(int[] array, int low, int high, int nThreads){
        if (low < high) {
            if (nThreads <= 1) {
                Serial.quickSort(array, low, high);
                return;
            }
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
