import java.util.Arrays;

public class Serial {
    static int size;
    static int[] SortArray;   
    public Serial(int[] baseArray) {
        SortArray = baseArray.clone();
        size = SortArray.length;
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
        bubbleSort();
        insertionSort();
        mergeSort();
        quickSort();

    }

    //estrutura basica das funções:
    //  1. monta o novo array que vai ser retornado
    //  2. inicia cronometro
    //  3. logica do algoritmo
    //  4. fecha e print cronometro e array organizado

    public static long bubbleSort(){
        int[] resultArray = SortArray.clone();
        long start = System.nanoTime();

        int swap;

        for(int i = 0; i<size-1; i++){
            for(int j = 0; j<size - i- 1; j++){
                if(resultArray[j] > resultArray[j+1]){
                    swap = resultArray[j];
                    resultArray[j] = resultArray[j+1];
                    resultArray[j+1] = swap;
                }
            }
        }

        long end = System.nanoTime();
        isSorted(resultArray);
        return (end - start) ;
    }

    public static long insertionSort(){
        int[] resultArray = SortArray.clone();
        long start = System.nanoTime();

        for (int i = 1; i < size; i++) {
            int current = resultArray[i];
            int j = i - 1;
            while (j >= 0 && resultArray[j] > current) {
                resultArray[j + 1] = resultArray[j];
                j -= 1 ;
            }
            resultArray[j + 1] = current;
        }

        long end = System.nanoTime();
        isSorted(resultArray);
        return (end - start) ;
    }

    public static long mergeSort(){
        int[] resultArray = SortArray.clone();
        long start = System.nanoTime();

        mergeSort(resultArray, 0, size-1);
        
        long end = System.nanoTime();
        isSorted(resultArray);
        return (end - start) ;
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

    public static long quickSort(){
        int[] resultArray = SortArray.clone();
        long start = System.nanoTime();

        quickSort(resultArray, 0, size - 1);
        
        long end = System.nanoTime();
        isSorted(resultArray);
        return (end - start) ;
    }

    public static void quickSort(int[] array, int low, int high) {
        while (low < high) {
            int pi = partitioning(array, low, high);

            // Evita recursão infinita
            if (pi == high) {
                high--;
            } else if (pi == low) {
                low++;
            } else {
                // Chama recursivamente apenas na menor partição (otimização tail recursion)
                if (pi - low < high - pi) {
                    quickSort(array, low, pi - 1);
                    low = pi + 1;
                } else {
                    quickSort(array, pi + 1, high);
                    high = pi - 1;
                }
            }
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