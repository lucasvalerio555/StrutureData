import java.util.ArrayList;
import java.util.List;

public class HashMap<K extends Comparable<K>, V> {

    private static final int MATRIX_SIZE = 10;

    private Node<K, V> start;
    private Node<K, V> next;
    private int size = 0;

    private Node<K, V>[][] arrayNode;
    private int nextRow = 0, nextCol = 0; // para inserción eficiente
    private StringBuilder builder = new StringBuilder();

    // ======================================================
    // NODE
    // ======================================================
    public class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }
        public Node<K, V> getNext() { return next; }
        public void setNext(Node<K, V> next) { this.next = next; }
    }

    // ======================================================
    // CONSTRUCTOR
    // ======================================================
    @SuppressWarnings("unchecked")
    public HashMap() {
        arrayNode = new Node[MATRIX_SIZE][MATRIX_SIZE];
    }

    // ======================================================
    // ESTADO
    // ======================================================
    public boolean isEmpty() {
        return start == null;
    }

    public int size() {
        return size;
    }

    // ======================================================
    // PUT
    // ======================================================
    public void put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);

        // Lista enlazada
        if (isEmpty()) {
            start = node;
            next = node;
        } else {
            next.setNext(node);
            next = node;
        }

        builder.append(key).append(value).append(",");

        // Matriz
        arrayNode[nextRow][nextCol] = node;
        incrementMatrixPosition();

        size++;

        // Reordenar matriz
        orderMatrix();
    }

    private void incrementMatrixPosition() {
        nextCol++;
        if (nextCol >= MATRIX_SIZE) {
            nextCol = 0;
            nextRow++;
        }
        if (nextRow >= MATRIX_SIZE) nextRow = MATRIX_SIZE - 1;
    }

    // ======================================================
    // GET
    // ======================================================
    public V get(K key) {
        int pos = search(key);
        if (pos == -1) return null;
        int row = pos / MATRIX_SIZE;
        int col = pos % MATRIX_SIZE;
        return arrayNode[row][col].getValue();
    }

    // ======================================================
    // REMOVE
    // ======================================================
    public void remove(int index) {
        // implementar si se necesita
    }

    // ======================================================
    // CLEAR
    // ======================================================
    public void clear() {
        start = null;
        next = null;
        arrayNode = new Node[MATRIX_SIZE][MATRIX_SIZE];
        size = 0;
        builder = new StringBuilder();
        nextRow = 0;
        nextCol = 0;
    }

    // ======================================================
    // SEARCH (binaria)
    // ======================================================
    private int search(K key) {
        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int row = mid / MATRIX_SIZE;
            int col = mid % MATRIX_SIZE;

            Node<K, V> node = arrayNode[row][col];
            if (node == null) break;

            int cmp = key.compareTo(node.getKey());

            if (cmp == 0) return mid;
            else if (cmp > 0) left = mid + 1;
            else right = mid - 1;
        }

        return -1;
    }

    // ======================================================
    // ORDENAR MATRIZ (QUICKSORT)
    // ======================================================
    private void orderMatrix() {
        if (size > 1)
            quickSortMatrix(0, size - 1);
    }

    private void quickSortMatrix(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            quickSortMatrix(low, pi - 1);
            quickSortMatrix(pi + 1, high);
        }
    }

    private int partition(int low, int high) {
        Node<K,V> pivot = getNodeByPosition(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            Node<K,V> current = getNodeByPosition(j);
            if (current.getKey().compareTo(pivot.getKey()) <= 0) {
                i++;
                swapNodes(i, j);
            }
        }

        swapNodes(i + 1, high);
        return i + 1;
    }

    private Node<K,V> getNodeByPosition(int pos) {
        int row = pos / MATRIX_SIZE;
        int col = pos % MATRIX_SIZE;
        return arrayNode[row][col];
    }

    private void swapNodes(int pos1, int pos2) {
        int row1 = pos1 / MATRIX_SIZE;
        int col1 = pos1 % MATRIX_SIZE;

        int row2 = pos2 / MATRIX_SIZE;
        int col2 = pos2 % MATRIX_SIZE;

        Node<K,V> tmp = arrayNode[row1][col1];
        arrayNode[row1][col1] = arrayNode[row2][col2];
        arrayNode[row2][col2] = tmp;
    }

    // ======================================================
    // MOSTRAR DATOS
    // ======================================================
    public String showData() {
        return "[" + builder + "]";
    }

    public int indexOf(K key){
        return search(key);
    }

    // ======================================================
    // NUEVO MÉTODO: obtener todos los nodos
    // ======================================================
    public List<Node<K,V>> getAllNodes() {
        List<Node<K,V>> list = new ArrayList<>();
        Node<K,V> current = start;
        while (current != null) {
            list.add(current);
            current = current.getNext();
        }
        return list;
    }

    // ======================================================
    // NUEVO MÉTODO: obtener todas las claves (keySet)
    // ======================================================
    public List<K> keySet() {
        List<K> keys = new ArrayList<>();
        Node<K,V> current = start;
        while (current != null) {
            keys.add(current.getKey());
            current = current.getNext();
        }
        return keys;
    }
}

