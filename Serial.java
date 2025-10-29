import java.util.Arrays;

public class Serial {
    static int size;
    static int[] SortArray = new int[size];   
    public Serial(int[] baseArray) {
        SortArray = baseArray.clone();
        size = SortArray.length;
    }   

    private static void print(int[] array, long time) { 
        System.out.println(Arrays.toString(array));
        System.out.println("TEMPO: "+ time + "ms");
    }

    public void run() {
        System.out.println("--- EXECUTANDO ALGORITMOS SERIAIS ---");

        System.out.println("1. BUBBLE SORT");
        bubbleSort();
        System.out.println("2. INSERTION SORT");
        insertionSort();
        System.out.println("3. MERGE SORT");
        mergeSort();
        System.out.println("4. QUICK SORT");
        quickSort();


        System.out.println("\n--- FIM DAS EXECUÇÕES SERIAIS --- ");
    }

    //estrutura basica das funções:
    //  1. monta o novo array que vai ser retornado
    //  2. inicia cronometro
    //  3. logica do algoritmo
    //  4. fecha e print cronometro e array organizado

    public static void bubbleSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        int swap;

        for(int i = 0; i<size-1; i++){
            for(int j = 0; i<size-1; i++){
                if(resultArray[i] > resultArray[j]){
                    swap = resultArray[i];
                    resultArray[i] = resultArray[j];
                    resultArray[j] = swap;
                }
            }
        }

        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }

    public static void insertionSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        for (int i = 1; i < size; i++) {
            int current = resultArray[i];
            int j = i - 1;
            while (j >= 0 && resultArray[j] > current) {
                resultArray[j + 1] = resultArray[j];
                j -= 1 ;
            }
            resultArray[j + 1] = current;
        }

        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }

    public static void mergeSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        mergeSort(SortArray, 0, size/2);
        
        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }    

    public static void mergeSort(int[] array, int left, int right){
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    public static void merge(int[] array, int left, int mid, int right){
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, mid + 1, R, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]){
                array[k++] = L[i++];
            }
            else {
                array[k++] = R[j++];
            };
        }

        while (i < n1){
            array[k++] = L[i++];
        }
        while (j < n2){
            array[k++] = R[j++];
        } 
    }

    public static void quickSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        quickSort(resultArray, 0, size - 1);
        
        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }

    public static void quickSort(int[] array, int low, int high){
        if (low<high) {
            int pi = partitioning(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1 ,high);
        }
    }

    public static int partitioning(int[] array, int low, int high){
        int pivot = array[high];
        int i = low - 1;
        int swap;

        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                swap = array[i];
                array[i] = array[j];
                array[j] = swap;
            }
        }

        int temp = array[i + 1];
        array[i+1] = array[high];
        array[high] = temp;
        return i + 1;
    }
}