import java.util.Arrays;

public class TreeSet<T extends Comparable<T>> {
    private Node<T> root;
    private Node<T> left;
    private Node<T> right;
    private int size;
    private int countleft;
    private int countright;
    private StringBuilder builder;
    private Node<T>[][] arrayNode;
    private int count;

    // 🔧 Nuevos campos agregados que usás pero no habías declarado
    private Node<T>[] cache;
    private boolean isCached;

    // ============================
    // 🌳 Clase interna Node
    // ============================
    class Node<E> {
        private Node<E> root;
        private Node<E> left;
        private Node<E> right;
        private E data;

        Node(E value) {
            this.data = value;
        }

        public void setNodeRoot(Node<E> node) {
            this.root = node;
        }

        public void setNodeLeft(Node<E> node) {
            this.left = node;
        }

        public void setNodeRight(Node<E> node) {
            this.right = node;
        }

        public void setData(E value) {
            this.data = value;
        }

        @Override
        public String toString() {
            return String.valueOf(data);
        }
    }

    // ============================
    // 🔧 Constructor
    // ============================
    @SuppressWarnings("unchecked")
    public TreeSet() {
        this.size = 0;
        this.countleft = 0;
        this.countright = 0;
        this.builder = new StringBuilder();
        this.arrayNode = new Node[10][10];
        this.count = 0;
        this.isCached = false;
        this.cache = (Node<T>[]) new Node[0];
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public String showData() {
        if (builder.length() == 0) {
            return "[]";
        }
        return "[" + builder.substring(0, builder.length() - 1) + "]";
    }

    public int Size() {
        return this.size;
    }

    // ============================
    // 🌱 Insertar elementos
    // ============================
    public void add(T element) {
        Node<T> tail = new Node<>(element);

        if (isEmpty()) {
            this.root = tail;
            this.root.setNodeRoot(tail);
        } else {
            if (countleft == 0) {
                this.left = tail;
                this.root.setNodeLeft(tail);
                this.countleft++;

            } else if (countright == 0) {
                this.right = tail;
                this.root.setNodeRight(tail);
                this.countright++;

            } else {
                insertNode(tail, element);
            }
        }

        this.builder.append(element).append(",");
        if (this.count < 10)
            this.arrayNode[count][count] = tail;
        count++;
        size++;
        isCached = false;
    }

    private void insertNode(Node<T> node, T value) {
        // Inserción simple de relleno (mantiene estructura sin romperla)
        if (this.countleft <= this.countright) {
            if (this.left != null) {
                this.left.setNodeLeft(node);
                this.left = node;
                this.countleft++;
            }
        } else {
            if (this.right != null) {
                this.right.setNodeRight(node);
                this.right = node;
                this.countright++;
            }
        }
    }

    public void insertNodeLeft(T element) {
        Node<T> tail = new Node<>(element);
        if (isEmpty()) {
            this.root = tail;
            this.root.setNodeRoot(tail);
        } else {
            if (this.countleft == 0) {
                this.left = tail;
                this.root.setNodeLeft(tail);
                this.countleft++;
            } else {
                this.left.setNodeLeft(tail);
                this.left = tail;
                this.countleft++;
            }
        }
        if (this.count < 10)
            this.arrayNode[count][count] = tail;
        count++;
        size++;
        isCached = false;
    }

    public void insertNodeRight(T element) {
        Node<T> tail = new Node<>(element);
        if (isEmpty()) {
            this.root = tail;
            this.root.setNodeRoot(tail);
        } else {
            if (this.countright == 0) {
                this.right = tail;
                this.root.setNodeRight(tail);
                this.countright++;
            } else {
                this.right.setNodeRight(tail);
                this.right = tail;
                this.countright++;
            }
        }
        if (this.count < 10)
            this.arrayNode[count][count] = tail;
        count++;
        size++;
        isCached = false;
    }

    // ============================
    // 🔍 Búsqueda
    // ============================
    private int search(T key) {
        if (!isCached) rebuildCache();
        for (int i = 0; i < cache.length; i++) {
            if (cache[i].data.compareTo(key) == 0)
                return i;
        }
        return -1;
    }

    // ============================
    // ⚙️ Ordenación y llenado del array
    // ============================
    private void orderArray(Node<T>[] array) {
        Arrays.sort(array, (a, b) -> {
            if (a == null || b == null) return 0;
            return a.data.compareTo(b.data);
        });
    }

    private void fillArray() {
        cache = new Node[size];
        int pos = 0;
        for (int i = 0; i < arrayNode.length && pos < size; i++) {
            for (int j = 0; j < arrayNode[i].length && pos < size; j++) {
                if (arrayNode[i][j] != null)
                    cache[pos++] = arrayNode[i][j];
            }
        }
        orderArray(cache);
    }

    private void rebuildCache() {
        fillArray();
        isCached = true;
    }

    // ============================
    // 📦 Acceso y modificación
    // ============================
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera del rango");
        }
        if (!isCached) rebuildCache();
        return cache[index].data;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera del rango...");
        }

        // Reconstruir builder sin el elemento eliminado
        String[] elements = builder.toString().split(",");
        builder = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            if (!elements[i].isEmpty() && i != index) {
                builder.append(elements[i]).append(",");
            }
        }

        // Actualizar cache
        if (!isCached) rebuildCache();
        Node<T>[] newCache = Arrays.copyOf(cache, size - 1);
        int pos = 0;
        for (int i = 0; i < cache.length; i++) {
            if (i != index) newCache[pos++] = cache[i];
        }
        cache = newCache;
        size--;
        isCached = true;
    }

    public void clear() {
        this.root = null;
        this.left = null;
        this.right = null;
        this.size = 0;
        this.arrayNode = new Node[10][10];
        this.builder = new StringBuilder();
        this.count = 0;
        this.countleft = 0;
        this.countright = 0;
        this.isCached = false;
        this.cache = (Node<T>[]) new Node[0];
    }

    // ============================
    // 🔍 indexOf y balance
    // ============================
    public int indexOf(T key) {
        return search(key);
    }

    public java.util.List<T> toList() {
        java.util.List<T> list = new java.util.ArrayList<>();
        fillList(root, list);
        return list;
    }

    private void fillList(Node<T> node, java.util.List<T> list) {
        if (node != null) {
            fillList(node.left, list);
            list.add(node.data);
            fillList(node.right, list);
    }
 
    public void balance() {
        if (!isCached) rebuildCache();
        Arrays.sort(cache, (a, b) -> a.data.compareTo(b.data));
        // reconstrucción simple del árbol balanceado
        this.root = buildBalanced(0, cache.length - 1);
    }

    private Node<T> buildBalanced(int start, int end) {
        if (start > end) return null;
        int mid = (start + end) / 2;
        Node<T> node = new Node<>(cache[mid].data);
        node.setNodeLeft(buildBalanced(start, mid - 1));
        node.setNodeRight(buildBalanced(mid + 1, end));
        return node;
    }
}

