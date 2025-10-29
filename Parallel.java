import java.util.Arrays;

public class Parallel {
    static int size;
    static int[] SortArray = new int[size];
    static int threads;

    public Parallel(int[] baseArray, int numThreads) {
        SortArray = baseArray.clone();
        size = SortArray.length;
        threads = numThreads;
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

    }

    public static void insertionSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        insertionParallel(resultArray);

        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }

    private static void insertionParallel(int[] array){

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

            Thread leftThread = new Thread(() -> Serial.mergeSort(array, left, mid));
            Thread rightThread = new Thread(() -> Serial.mergeSort(array, mid + 1, right));

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

        quickParallel(resultArray, 0, size -1);
        
        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }

    private static void quickParallel(int[] array, int high, int low){
        if (low < high) {
            int pivot = Serial.partitioning(array, low, high);
            Thread left = new Thread(() -> Serial.quickSort(array, low, pivot - 1));
            Thread right = new Thread(() -> Serial.quickSort(array, pivot + 1, high));
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
