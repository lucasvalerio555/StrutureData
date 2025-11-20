import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class ListLinked<T extends Comparable<T>> {
    private Node<T> start = null;
    private Node<T> next = null;
    private int size = 0;
    private int count = 0; // Esta variable no se usa en el código proporcionado.

    // Matriz de nodos (Inicialización correcta para arrays genéricos)
    private final Node<T>[][] ArrayNode = (Node<T>[][]) new Node[10][10]; 
    private final int MATRIX_SIZE = 100; // Tamaño máximo que puede almacenar la matriz 10x10.

    public boolean isEmpty() {
        return start == null;
    }

    public int size() { // Corregido a camelCase
        return size;
    }

    public String showData() {
        if (size == 0) return "[]";
        StringBuilder builder = new StringBuilder();
        Node<T> current = start;
        while (current != null) {
            builder.append(current.getData()).append(",");
            current = current.getNode();
        }
        return "[" + builder.substring(0, builder.length() - 1) + "]";
    }

    // Corregido a camelCase
    private void quickSort(Node<T>[] array, int low, int high) { 
        if (low < high) {
            int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    private int partition(Node<T>[] array, int low, int high) {
        T pivot = array[high].getData();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j].getData().compareTo(pivot) <= 0) {
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
     * Llena la matriz y ordena con QuickSort.
     * Es crucial para el acceso O(1) pero ineficiente en cada add/remove.
     */
    private void fillArray() {
        if (size > MATRIX_SIZE) {
            System.err.println("Advertencia: La lista excede el tamaño de la matriz (100).");
            return;
        }

        // Se usa un casting para la inicialización de array genérico.
        Node<T>[] tempArray = (Node<T>[]) new Node[size];
        Node<T> current = start;
        for (int i = 0; i < size; i++) {
            tempArray[i] = current;
            current = current.getNode();
        }

        // Ordenar con QuickSort
        quickSort(tempArray, 0, size - 1);

        // Llenar la matriz 10x10
        for (int i = 0; i < size; i++) {
            ArrayNode[i / 10][i % 10] = tempArray[i];
        }
        // Limpiar las celdas restantes si la lista se ha reducido
        for (int i = size; i < MATRIX_SIZE; i++) {
            ArrayNode[i / 10][i % 10] = null;
        }
    }

    public void add(T element) {
        Node<T> tail = new Node<>(element);
        if (isEmpty()) {
            start = tail;
            next = start;
        } else {
            next.setNode(tail);
            next = tail;
        }
        size++;
        // Solo llenar/ordenar si el elemento cabe en la matriz
        if (size <= MATRIX_SIZE) { 
             fillArray(); // Ordenamos la matriz tras cada inserción
        } else {
            // Si excede el tamaño, la matriz no estará sincronizada
            System.err.println("Advertencia: No se puede ordenar. Se excedió el tamaño de la matriz (100).");
        }
    }

    public void remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Fuera de rango.");

        if (index == 0) {
            start = start.getNode();
        } else {
            Node<T> prev = getNode(index - 1);
            Node<T> current = prev.getNode();
            prev.setNode(current.getNode());
            
            // Si el nodo eliminado era el "next" (último), actualizar next.
            if (current == next) {
                next = prev;
            }
        }
        size--;
        // Solo llenar/ordenar si el elemento anterior cabía en la matriz
        if (size < MATRIX_SIZE) { 
            fillArray(); // Reordenamos matriz tras eliminación
        }
    }
    
    // Sobrecarga para buscar y eliminar por elemento. (Necesario para App)
    public void remove(T element) {
        int index = indexOf(element);
        if (index != -1) {
            // Encuentra la posición en la lista enlazada original
            Node<T> current = start;
            Node<T> prev = null;
            int count = 0;

            // La búsqueda secuencial es necesaria porque la matriz solo apunta al nodo, 
            // pero la eliminación debe hacerse en la lista enlazada original.
            while (current != null && !current.getData().equals(element)) {
                prev = current;
                current = current.getNode();
                count++;
            }
            
            if (current != null) {
                if (prev == null) {
                    start = current.getNode();
                } else {
                    prev.setNode(current.getNode());
                }
                
                // Si el nodo eliminado era el "next" (último), actualizar next.
                if (current == next) {
                    next = prev;
                }
                
                size--;
                // Solo llenar/ordenar si el elemento anterior cabía en la matriz
                if (size < MATRIX_SIZE) { 
                    fillArray(); // Reordenamos matriz tras eliminación
                }
            } else {
                 // Elemento no encontrado
                 throw new IllegalArgumentException("Elemento no encontrado en la lista enlazada.");
            }
        } else {
            throw new IllegalArgumentException("Elemento no encontrado en la lista.");
        }
    }

    // Nodo directo desde lista O(n)
    private Node<T> getNode(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        Node<T> current = start;
        for (int i = 0; i < index; i++) current = current.getNode();
        return current;
    }

    // GET O(1) usando matriz (asumiendo que size <= MATRIX_SIZE)
    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        
        // El acceso es O(1) solo si el índice está dentro de la matriz
        if (index < MATRIX_SIZE) {
            return ArrayNode[index / 10][index % 10].getData();
        } else {
            // Si excede el límite de la matriz, debe usarse el acceso O(n) de la lista enlazada
            return getNode(index).getData(); 
        }
    }

    public void clear() {
        start = null;
        next = null;
        size = 0;
        count = 0;
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                ArrayNode[i][j] = null;
    }

    public T[] toArray() {
        // Se usa un casting para la inicialización de array genérico.
        T[] array = (T[]) new Comparable[size];
        for (int i = 0; i < size; i++) {
             array[i] = get(i);
        }
        return array;
    }

    /**
     * Busca el índice de un elemento usando búsqueda binaria en la matriz.
     * Retorna el índice lineal (0 a size-1) o -1 si no se encuentra.
     */
    public int indexOf(T element) {
        if (size == 0 || size > MATRIX_SIZE) return -1;

        int left = 0;
        int right = size - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // Mejor cálculo de mid para evitar overflow
            
            // Acceso al nodo en la matriz
            Node<T> node = ArrayNode[mid / 10][mid % 10];
            
            if (node == null) return -1; // No debería ocurrir si size es correcto
            
            int cmp = node.getData().compareTo(element);

            if (cmp == 0) return mid; // ¡Encontrado!
            if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }

       return -1; // No encontrado
    }

    public void sort() {
      // Solo ordena si cabe en la matriz.
      if (size <= MATRIX_SIZE) { 
        fillArray(); // Ordena toda la lista usando QuickSort y actualiza la matriz
      } else {
         System.err.println("No se puede ordenar. Se excedió el tamaño de la matriz (100).");
      }
    }

    public void reverse() {
        Node<T> prev = null;
        Node<T> current = start;
        Node<T> tempNext = start; // Guardamos el antiguo inicio para que sea el nuevo 'next' (cola)

        while (current != null) {
            Node<T> nextNode = current.getNode();
            current.setNode(prev);
            prev = current;
            current = nextNode;
        }
        start = prev;
        next = tempNext; // El antiguo 'start' es ahora el nuevo 'next' (cola)
        
        // Aunque la lista enlazada se invirtió, la matriz sigue el ordenamiento de T.
        // Por lo tanto, se debe llamar a fillArray() para reordenar la matriz.
        if (size <= MATRIX_SIZE) { 
           fillArray(); 
        }
    }

    // Método 'contains' mejorado usando indexOf
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    // ------------------------------
    // MÉTODO FILTER
    // ------------------------------
    public ListLinked<T> filter(Predicate<T> pred) {
        ListLinked<T> filtered = new ListLinked<>();
        Node<T> current = start;
        while (current != null) {
            if (pred.test(current.getData())) {
                filtered.add(current.getData());
            }
            current = current.getNode();
        }
        return filtered;
    }
}

/**
 * Clase nodo genérica (se mantuvo la original, solo se quitó el import no usado)
 */
class Node<T extends Comparable<T>> {
    private Node<T> next = null;
    private T data;

    Node(T value) {
        this.data = value;
    }

    public void setNode(Node<T> next) {
        this.next = next;
    }

    public Node<T> getNode() {
        return next;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
