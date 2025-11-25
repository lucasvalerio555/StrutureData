public class Queues<T extends Comparable<T>> {

    public class Node<T> {

        private T data;
        private Node<T> next;

        public Node(T data) {
           this.data = data;
           this.next = null;
        }

        public T getData() {
           return data;
        }

        public Node<T> getNext() { 
           return next;
        }

        public void setNext(Node<T> next) {
           this.next = next;
        }
    }


    // ============================================
    // ATRIBUTOS DE LA COLA
    // ============================================
    private Node<T> start = null;
    private Node<T> end = null;
    private int size = 0;
    private StringBuilder builder = new StringBuilder();

    // MATRIZ
    private final int MATRIX_SIZE = 10;
    // La declaración se mantiene con el casting necesario para genéricos.
    private Node<T>[][] arrayNode = (Node<T>[][]) new Node[MATRIX_SIZE][MATRIX_SIZE]; 
    private int r = 0; // Fila de inserción
    private int c = 0; // Columna de inserción

    // ============================================
    // MÉTODOS BÁSICOS DE QUEUE
    // ============================================
    public boolean isEmpty() {
        return this.start == null; 
    }

    public int Size() {
        return this.size;
    }

    public String showData() {
        // Regenera el string builder a partir de la lista enlazada (la fuente de verdad)
        // para asegurar que refleje los cambios de pop/remove.
        StringBuilder tempBuilder = new StringBuilder();
        Node<T> current = start;
        while (current != null) {
            tempBuilder.append(current.getData()).append(",");
            current = current.getNext();
        }
        
        String result = tempBuilder.toString();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return "[" + result + "]";
    }

    public void push(T element) {
        Node<T> newNode = new Node<>(element);

        if (isEmpty()) {
            this.start = newNode;
            this.end = newNode;
        } else {
            this.end.setNext(newNode);
            this.end = newNode;
        }

        // Registrar en matriz (solo si hay espacio)
        if (r < MATRIX_SIZE) {
            arrayNode[r][c] = newNode;
            c++;
            if (c == MATRIX_SIZE) {
                c = 0;
                r++;
            }
        }
        
        // NOTA: El StringBuilder se actualizará automáticamente en showData().
        // Para que se actualice inmediatamente en la variable 'builder', 
        // deberías llamarlo aquí, pero es mejor hacerlo solo en showData.

        size++;
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }

        T value = start.getData();
        
        // 1. Eliminar de la lista enlazada (cola)
        start = start.getNext();
        if (start == null) end = null;
        size--;

        // 2. Reorganizar las estructuras auxiliares
        // Llama a fillArray para re-ordenar y re-construir la matriz.
        // Se llama solo si hay elementos restantes para evitar excepciones.
        if (size > 0) {
            fillArray();
        } else {
            // Si la cola está vacía, limpia la matriz.
            clearArray();
        }
        
        return value;
    }

    // ============================================
    // BÚSQUEDA BINARIA (CORREGIDA)
    // ============================================
    private int search(T key) {
        // Nota: Asume que la fila 0 de la matriz está ordenada.
        int left = 0;
        int right = MATRIX_SIZE - 1;

        while (left <= right) {
            int middle = (left + right) / 2;

            if (arrayNode[0][middle] == null)
                return -1; 

            int comp = key.compareTo(arrayNode[0][middle].getData());

            if (comp == 0)
                return middle;
            else if (comp > 0)
                left = middle + 1;
            else
                right = middle - 1;
        }

        return -1;
    }

    // ============================================
    // ORDENAR MATRIZ — QUICKSORT
    // ============================================
    public void orderMatrix(Node<T>[][] array) {
        // Esta función ahora actúa como un wrapper para la lógica de fillArray().
        int total = MATRIX_SIZE * MATRIX_SIZE;
        Node<T>[] flat = (Node<T>[]) new Node[total]; 
        int index = 0;

        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                flat[index++] = array[i][j];
            }
        }
        
        quickSort(flat, 0, index - 1); // index - 1 es el último elemento real
        
        // Pasar vector ordenado → matriz (re-llenar)
        index = 0;
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (index < total) {
                    array[i][j] = flat[index++];
                } else {
                    array[i][j] = null;
                }
            }
        }
    }

    // ============================================
    // QUICKSORT — IMPLEMENTACIÓN COMPLETA
    // ============================================
    private void quickSort(Node<T>[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    private int partition(Node<T>[] array, int low, int high) {
        Node<T> pivot = array[high];
        if (pivot == null) {
            int pivotIdx = high;
            while(pivotIdx >= low && array[pivotIdx] == null) {
                pivotIdx--;
            }
            if (pivotIdx < low) return low;
            pivot = array[pivotIdx];
            Node<T> tempSwap = array[high];
            array[high] = array[pivotIdx];
            array[pivotIdx] = tempSwap;
        }
        
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] == null) continue; 

            if (array[j].getData().compareTo(pivot.getData()) <= 0) {
                i++;
                Node<T> temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        Node<T> temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    /**
     * Llena/actualiza la matriz de 10x10 usando los datos ordenados de la LinkedList.
     */
    private void fillArray(){
        if (size > MATRIX_SIZE * MATRIX_SIZE) { 
            System.err.println("Advertencia: La lista excede el tamaño de la matriz (100).");
            return;
        }

        // 1. Convertir LinkedList a un array temporal
        Node<T>[] tempArray = (Node<T>[]) new Node[size];
        Node<T> current = start;
        for (int i = 0; i < size; i++) {
            tempArray[i] = current;
            current = current.getNext();
        }

        // 2. Ordenar con QuickSort
        quickSort(tempArray, 0, size - 1);

        // 3. Llenar la matriz 10x10 con los nodos ordenados
        for (int i = 0; i < size; i++) {
            arrayNode[i / MATRIX_SIZE][i % MATRIX_SIZE] = tempArray[i]; 
        }
        // 4. Limpiar las celdas restantes (si la cola se redujo)
        for (int i = size; i < MATRIX_SIZE * MATRIX_SIZE; i++) {
            arrayNode[i / MATRIX_SIZE][i % MATRIX_SIZE] = null; 
        }       
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango.");
        }
        
        // Retorna el elemento usando el mapeo directo del índice a la matriz 10x10
        int row = index / MATRIX_SIZE;
        int col = index % MATRIX_SIZE;

        Node<T> node = arrayNode[row][col];

        return (node != null) ? node.getData() : null;
    }


    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango.");
        }
        
        int pos = index; 

        // 1. Eliminar de la LinkedList (Cola)
        if (pos == 0) {
            start = start.getNext();
           if (start == null) end = null;
        } else {
            Node<T> prev = start;
            for (int i = 0; i < pos - 1; i++) {
                prev = prev.getNext();
            }

            Node<T> target = prev.getNext();
            prev.setNext(target.getNext());

            if (target == end) {
                end = prev;
            }
        }

        size--;

        // 2. Reconstruir la Matriz: 
        // Llamar a fillArray() hace todo el trabajo (ordenar, compactar y rellenar la matriz) 
        // a partir de la lista enlazada ya reducida.
        if (size > 0) {
            fillArray();
        } else {
            clearArray();
        }
    } 
    
    /**
     * Limpia la matriz (arrayNode) y resetea los índices de inserción.
     */
    private void clearArray() {
        this.arrayNode = (Node<T>[][]) new Node[MATRIX_SIZE][MATRIX_SIZE];
        this.r = 0;
        this.c = 0;
    }

    public void clear(){
        this.start = null;
        this.end = null;
        this.size = 0;
        this.builder = new StringBuilder();
        
        // La sintaxis corregida para limpiar la matriz y los índices
        clearArray();
    }
}
