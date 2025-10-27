public class Serial {
    static int size;
    static int[] SortArray = new int[size];
    public Serial(int size, int[] baseArray) {
        this.size = size;
        SortArray = baseArray.clone();
    }   

    //estrutura basica das funções:
    //  1. monta o novo array que vai ser retornado
    //  2. logica do algoritmo
    //  3. retorna o nobo array

    public static int[] BubbleSort(){
        //classico java: se eu fizesse resultArray = sortArray, ele faria referencia e não uma copia
        int[] resultArray = SortArray.clone();

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
        return resultArray;
    }

    public static int[] MergeSort(){
        int[] resultArray = SortArray.clone();

        mergeS(SortArray, 0, size/2);
        
        return resultArray;
    }    

    private static void mergeS(int[] array, int left, int right){
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeS(array, left, mid);
            mergeS(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private static void merge(int[] array, int left, int mid, int right){
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, mid + 1, R, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) array[k++] = L[i++];
            else array[k++] = R[j++];
        }

        while (i < n1){
            array[k++] = L[i++];
        }
        while (j < n2){
            array[k++] = R[j++];
        } 
    }

    
}