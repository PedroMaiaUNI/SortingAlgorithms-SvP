import java.util.Arrays;

public class Parallel {
    static int size;
    static int[] SortArray = new int[size];
    public Parallel(int[] baseArray) {
        SortArray = baseArray.clone();
        size = SortArray.length;
    }   

    private static void print(int[] array, long time) { 
        System.out.println(Arrays.toString(array));
        System.out.println(time);
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
    //  5. retorna o novo array

    public static void bubbleSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        //LOGICA

        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }

    public static void insertionSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        //LOGICA

        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }

    public static void mergeSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        //LOGICA
        
        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }  

    public static void quickSort(){
        int[] resultArray = SortArray.clone();
        long start = System.currentTimeMillis();

        //LOGICA
        
        long end = System.currentTimeMillis();
        print(resultArray, end - start);
    }

    
}
