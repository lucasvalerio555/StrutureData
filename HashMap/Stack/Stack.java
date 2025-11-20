public class Stack<T extends Comparable<T>> {

    private Node<T>[][] ArrayNode = (Node<T>[][]) new Node[50][50]; // Tamaño máximo 50x50
    private Node<T> start = null;
    private Node<T> next = null;
    private int size = 0;
    private int MATRIX_SIZE = 50;
    private StringBuilder builder = new StringBuilder();

    public boolean isEmpty() {
        return start == null;
    }

    public int Size() {
        return size;
    }

    public String showData() {
        if (size == 0) return "[]";
        return "[" + builder.substring(0, builder.length() - 1) + "]";
    }

    public void push(T element) {
        Node<T> node = new Node<>(element);
        if (isEmpty()) {
            start = node;
            next = node;
        } else {
            next.setNext(node);
            next = node;
        }

        // Actualizar matriz
        int row = size / MATRIX_SIZE;
        int col = size % MATRIX_SIZE;
        if (row < MATRIX_SIZE && col < MATRIX_SIZE) {
            ArrayNode[row][col] = node;
        }

        size++;
        builder.append(element).append(",");
    }

    public void pop() {
        if (isEmpty()) return;

        // Eliminar último nodo de la lista enlazada
        Node<T> current = start;
        Node<T> previous = null;
        while (current.getNext() != null) {
            previous = current;
            current = current.getNext();
        }

        if (previous != null) {
            previous.setNext(null);
            next = previous;
        } else {
            start = null;
            next = null;
        }

        // Limpiar la matriz
        int row = (size - 1) / MATRIX_SIZE;
        int col = (size - 1) % MATRIX_SIZE;
        if (row < MATRIX_SIZE && col < MATRIX_SIZE) {
            ArrayNode[row][col] = null;
        }

        // Actualizar builder
        int lastComma = builder.lastIndexOf(",");
        if (lastComma != -1) builder.delete(lastComma, builder.length());

        size--;
    }

    public T peek() {
        if (isEmpty()) return null;
        return next.getData();
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Fuera del rango");
        int row = index / MATRIX_SIZE;
        int col = index % MATRIX_SIZE;
        Node<T> node = ArrayNode[row][col];
        if (node != null) return node.getData();

        // fallback: recorrer la lista
        Node<T> current = start;
        int count = 0;
        while (current != null) {
            if (count == index) return current.getData();
            current = current.getNext();
            count++;
        }
        return null;
    }

    public int indexOf(T element) {
        Node<T> current = start;
        int index = 0;
        while (current != null) {
            if (current.getData().equals(element)) return index;
            current = current.getNext();
            index++;
        }
        return -1;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Fuera del rango");

        Node<T> current = start;
        Node<T> previous = null;
        int count = 0;
        while (current != null && count < index) {
            previous = current;
            current = current.getNext();
            count++;
        }

        if (previous != null) previous.setNext(current.getNext());
        else start = current.getNext();

        // Actualizar next si se eliminó el último
        if (index == size - 1) next = previous;

        // Limpiar matriz
        int row = index / MATRIX_SIZE;
        int col = index % MATRIX_SIZE;
        if (row < MATRIX_SIZE && col < MATRIX_SIZE) ArrayNode[row][col] = null;

        // Actualizar builder
        int startIndex = builder.indexOf(current.getData().toString());
        if (startIndex != -1) {
            int endIndex = startIndex + current.getData().toString().length() + 1; // +1 por coma
            builder.delete(startIndex, endIndex);
        }

        size--;
    }

}

// Clase Node interna
class Node<T extends Comparable<T>> {
    private Node<T> next = null;
    private T data = null;

    public Node(T element) { this.data = element; }
    public void setNext(Node<T> node) { this.next = node; }
    public Node<T> getNext() { return this.next; }
    public T getData() { return this.data; }
}

