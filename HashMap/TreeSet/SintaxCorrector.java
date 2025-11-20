import java.util.*;
import java.io.*;

public class SintaxCorrector {

    // Usaremos un HashMap como un placeholder funcional para la memoria.
    private Map<String, String> memory = new HashMap<>(); 
    // Usamos la clase TreeSet personalizada definida arriba
    private TreeSet<String> tree = new TreeSet<>(); 

    private int score = 100;
    private final String MEMORY_FILE = "memory.txt";
    private StringBuilder builder = null;
    private char[] textCorrect = null;

    
    public SintaxCorrector() {
        this.builder = new StringBuilder();
        
        // Carga la memoria (comentada en el original)
        // loadMemory();

        // === ESTRUCTURA DE TRIE ORTOGRÁFICO ESPAÑOL ===
        // Las llamadas a insertNodeLeft/Right ahora funcionan con la clase TreeSet personalizada.

        // *************** NIVEL 1: HIJOS DE LA RAÍZ (START) ***************
        // Partimos de A (Nodo de Letra Mayúscula)
        tree.insertNodeLeft("A");
        tree.insertNodeRight("c");
        tree.insertNodeLeft("b");
        // ... (resto de las inserciones de A)
        tree.insertNodeRight("f");
        tree.insertNodeLeft("q");
        tree.insertNodeRight("v");
        tree.insertNodeLeft("u");
        tree.insertNodeRight("r");
        tree.insertNodeLeft("p");
        tree.insertNodeRight("n");
        tree.insertNodeLeft("ñ");
        tree.insertNodeRight("d");
        tree.insertNodeLeft("z");
        tree.insertNodeRight("h");

        // Partimos de B (Nodo de Letra Mayúscula)
        tree.insertNodeLeft("B");
        tree.insertNodeRight("o");
        tree.insertNodeLeft("i");
        tree.insertNodeRight("u");
        tree.insertNodeLeft("e");
        tree.insertNodeRight("r");

        // Partimos de C (Nodo de Letra Mayúscula)
        tree.insertNodeLeft("C");
        tree.insertNodeRight("p");
        tree.insertNodeLeft("e");
        tree.insertNodeRight("o");
        tree.insertNodeLeft("u");
        tree.insertNodeRight("i");
        tree.insertNodeLeft("a");

        // *************** NIVEL 2 ***************
        // Partimos de D
        tree.insertNodeRight("D");
        tree.insertNodeLeft("o");
        tree.insertNodeRight("i");
        tree.insertNodeLeft("u");
        tree.insertNodeRight("a");
        tree.insertNodeLeft("r");
        tree.insertNodeRight("p");

        // Partimos de E
        tree.insertNodeLeft("E");
        tree.insertNodeRight("m");
        tree.insertNodeLeft("s");
        tree.insertNodeRight("p");
        tree.insertNodeLeft("f");
        tree.insertNodeRight("g");
        tree.insertNodeLeft("h");
        tree.insertNodeRight("j");
        tree.insertNodeLeft("r");
        tree.insertNodeRight("v");
        tree.insertNodeLeft("b");
        tree.insertNodeRight("c");
        tree.insertNodeLeft("t");

        // Partimos de F
        tree.insertNodeLeft("F");
        tree.insertNodeRight("e");
        tree.insertNodeLeft("i");
        tree.insertNodeRight("u");
        tree.insertNodeLeft("r");
        tree.insertNodeRight("o");
        tree.insertNodeLeft("a");
        tree.insertNodeRight("l");

        // Partimos de G
        tree.insertNodeRight("G");
        tree.insertNodeLeft("p");
        tree.insertNodeRight("u");
        tree.insertNodeLeft("f");
        tree.insertNodeRight("r");
        tree.insertNodeLeft("e");
        tree.insertNodeRight("i");
        tree.insertNodeLeft("a");
        tree.insertNodeRight("p");

        // *************** NIVEL 3 ***************

        // Partimos de H
        tree.insertNodeLeft("H");
        tree.insertNodeRight("o");
        tree.insertNodeLeft("e");
        tree.insertNodeRight("u");
        tree.insertNodeLeft("a");
        tree.insertNodeRight("i");

        // Partimos de I
        tree.insertNodeLeft("I");
        tree.insertNodeRight("u");
        tree.insertNodeLeft("o");
        tree.insertNodeRight("p");
        tree.insertNodeLeft("a");
        tree.insertNodeRight("s");
        tree.insertNodeLeft("t");
        tree.insertNodeRight("ñ");
        tree.insertNodeLeft("m");
        tree.insertNodeRight("r");

        // === Continuación del árbol desde J hasta Z ===

        // Partimos de J
        tree.insertNodeLeft("J");
        tree.insertNodeRight("u");
        tree.insertNodeLeft("e");
        tree.insertNodeRight("a");
        tree.insertNodeLeft("o");
        tree.insertNodeRight("i");
        tree.insertNodeLeft("o");
        tree.insertNodeRight("r");

        // Partimos de K
        tree.insertNodeLeft("K");
        tree.insertNodeRight("L"); 
        tree.insertNodeLeft("m");
        tree.insertNodeRight("a");
        tree.insertNodeLeft("o");
        tree.insertNodeRight("p");
        tree.insertNodeLeft("q");
        tree.insertNodeRight("r");

        // Partimos de L
        tree.insertNodeLeft("L");
        tree.insertNodeRight("M"); 
        tree.insertNodeLeft("n");
        tree.insertNodeRight("ñ");
        tree.insertNodeLeft("o");
        tree.insertNodeRight("p");
        tree.insertNodeLeft("q");
        tree.insertNodeRight("r");
        tree.insertNodeLeft("s");
        tree.insertNodeRight("a");

        // Partimos de M
        tree.insertNodeLeft("M");
        tree.insertNodeRight("N"); 
        tree.insertNodeLeft("o");
        tree.insertNodeRight("p");
        tree.insertNodeLeft("q");
        tree.insertNodeRight("r");
        tree.insertNodeLeft("s");
        tree.insertNodeRight("t");
        tree.insertNodeLeft("u");
        tree.insertNodeRight("v");

        // Partimos de N
        tree.insertNodeLeft("N");
        tree.insertNodeRight("Ñ"); 
        tree.insertNodeLeft("o");
        tree.insertNodeRight("p");
        tree.insertNodeLeft("q");
        tree.insertNodeRight("r");
        tree.insertNodeLeft("s");
        tree.insertNodeRight("t");

        // Partimos de Ñ
        tree.insertNodeLeft("Ñ");
        tree.insertNodeRight("o");
        tree.insertNodeLeft("p");
        tree.insertNodeRight("q");
        tree.insertNodeLeft("r");
        tree.insertNodeRight("s");
        tree.insertNodeLeft("t");
        tree.insertNodeRight("u");

        // Partimos de O
        tree.insertNodeLeft("O");
        tree.insertNodeRight("p");
        tree.insertNodeLeft("q");
        tree.insertNodeRight("r");
        tree.insertNodeLeft("s");
        tree.insertNodeRight("t");
        tree.insertNodeLeft("u");
        tree.insertNodeRight("v");
        tree.insertNodeLeft("w");
        tree.insertNodeRight("l");

        // Partimos de P
        tree.insertNodeLeft("P");
        tree.insertNodeRight("Q"); 
        tree.insertNodeLeft("r");
        tree.insertNodeRight("s");
        tree.insertNodeLeft("t");
        tree.insertNodeRight("u");
        tree.insertNodeLeft("v");
        tree.insertNodeRight("w");

        // Partimos de Q
        tree.insertNodeLeft("Q");
        tree.insertNodeRight("r");
        tree.insertNodeLeft("s");
        tree.insertNodeRight("t");
        tree.insertNodeLeft("u");
        tree.insertNodeRight("v");
        tree.insertNodeLeft("w");
        tree.insertNodeRight("x");

        // Partimos de R
        tree.insertNodeLeft("R");
        tree.insertNodeRight("s");
        tree.insertNodeLeft("t");
        tree.insertNodeRight("u");
        tree.insertNodeLeft("v");
        tree.insertNodeRight("w");
        tree.insertNodeLeft("x");
        tree.insertNodeRight("y");

        // Partimos de S
        tree.insertNodeLeft("S");
        tree.insertNodeRight("t");
        tree.insertNodeLeft("u");
        tree.insertNodeRight("v");
        tree.insertNodeLeft("w");
        tree.insertNodeRight("x");
        tree.insertNodeLeft("y");
        tree.insertNodeRight("z");

        // Partimos de T
        tree.insertNodeLeft("T");
        tree.insertNodeRight("W"); 
        tree.insertNodeLeft("u");
        tree.insertNodeRight("v");
        tree.insertNodeLeft("w");
        tree.insertNodeRight("x");
        tree.insertNodeLeft("y");
        tree.insertNodeRight("z");

        // Partimos de W
        tree.insertNodeLeft("W");
        tree.insertNodeRight("X"); 
        tree.insertNodeLeft("y");
        tree.insertNodeRight("z");

        // Partimos de X
        tree.insertNodeLeft("X");
        tree.insertNodeLeft("e");
        tree.insertNodeRight("o");
        tree.insertNodeLeft("i");

        // Partimos de Y
        tree.insertNodeRight("Y");
        tree.insertNodeLeft("i");
        tree.insertNodeRight("a");
        tree.insertNodeLeft("o");
        tree.insertNodeRight("e");

        // Partimos de Z
        tree.insertNodeRight("Z");
        tree.insertNodeLeft("a");
        tree.insertNodeRight("o");
        tree.insertNodeLeft("u");
        tree.insertNodeRight("i");
        tree.insertNodeLeft("r");
        tree.insertNodeRight("e");
        
        tree.balance();
    }
    
    // Función auxiliar
    private static boolean isVowel(char c) {
        return "aeiouAEIOUáéíóúÁÉÍÓÚ".indexOf(c) != -1;
    }

    /**
     * ** LÓGICA MODIFICADA **: Busca el prefijo en la estructura. 
     * Utiliza indexOf del TreeSet personalizado (que usa la caché ordenada).
     */
    private boolean checkWordInTrie(String word) {
        // 1. Verificar la palabra completa
        if (tree.indexOf(word) != -1) {
            return true;
        }

        // 2. Verificar prefijos de dos letras (ej: 'Ho')
        if (word.length() >= 2) {
            String prefix = word.substring(0, 2);
            // La primera letra debe estar en mayúscula si fue la inicial
            String check = Character.toUpperCase(prefix.charAt(0)) + prefix.substring(1).toLowerCase();
            if (tree.indexOf(check) != -1) {
                return true;
            }
        }
        
        // 3. Verificar solo la letra inicial (ej: 'H')
        String initial = word.substring(0, 1).toUpperCase();
        if (tree.indexOf(initial) != -1) {
            return true;
        }

        return false;
    }


    /**
     * Corrige mayúsculas y puntuación básica del texto, y evalúa la primera palabra.
     */
    public String analitics(String text) {
        if (text == null || text.isEmpty()) return text;

        char[] chars = text.toCharArray();
        
        // Corrección de Mayúsculas y puntuación
        if (Character.isLowerCase(chars[0])) {
            chars[0] = Character.toUpperCase(chars[0]);
        }

        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == '.' && Character.isLowerCase(chars[i + 1])) {
                chars[i + 1] = Character.toUpperCase(chars[i + 1]);
            }
        }

        // Lógica de corrección ortográfica (usando el índice del árbol)
        if (tree.indexOf(String.valueOf(chars[0]).toUpperCase()) != -1) {
            
            // Limpiamos el builder para evitar acumulación de llamadas anteriores
            this.builder.setLength(0); 
            String processedText = this.builder.append(String.valueOf(chars)).toString();
            String firstWord = processedText.split(" ")[0].replaceAll("[^a-zA-ZñÑáéíóúÁÉÍÓÚ]", ""); 
            
            // Si la palabra no está vacía, la verificamos
            if (!firstWord.isEmpty()) {
                // CORREGIDO: Usamos text.length()
                textCorrect = new char[text.length() - 1]; 

                if (checkWordInTrie(firstWord)) {
                    // Secuencia válida
                    this.score = 100;
                } else {
                    // Secuencia inválida
                    this.score = this.score - 10;
                 }
            } else {
                 this.score = 0;
            }
        }

        // Limpieza general de comas
        return cleanCommas(new String(chars));
    }


    public void addAsentLetter(String word) {
        if (word == null || word.isEmpty()) return;

        char[] chars = word.toCharArray();
        boolean hasAccent = false;

        for (char c : chars) {
            if ("áéíóúÁÉÍÓÚ".indexOf(c) != -1) {
                hasAccent = true;
            }
        }

        if (hasAccent) {
            System.out.println("✅ La palabra contiene acento: " + word);
        } else {
            System.out.println("❌ La palabra no tiene acento: " + word);
        }
    }


    public void addDir(char[] text) {
        if (text.length == 0) return;

        try {
            FileWriter write = new FileWriter(MEMORY_FILE, true);
            write.write(new String(text) + System.lineSeparator());
            write.close();

        } catch (IOException e) {
            System.err.println(""+ e.getMessage());
        }
    }

    /**
     * Limpia comas innecesarias y deja solo las que son gramaticalmente válidas.
     */
    public String cleanCommas(String text) {
        if (text == null || text.isEmpty()) return text;

        text = text.replaceAll(",\\s*,+", ", "); 
        text = text.replaceAll("\\s+,", ",");    

        text = text.replaceAll("(?i)\\b(pero|aunque|sin embargo|sino)\\b", ", $1");

        text = text.replaceAll("\\s+([.,!?;:])", "$1");
        text = text.replaceAll("([.,!?;:])(\\S)", "$1 $2");

        text = text.trim();
        if (text.length() > 0 && Character.isLowerCase(text.charAt(0))) {
            text = Character.toUpperCase(text.charAt(0)) + text.substring(1);
        }

        return text.trim();
    }


    /**
     * Guarda secuencias en archivo de memoria.
     */
    public void memorys(String key, String value) {
        if (key == null || value == null) return;
        memory.put(key, value);

        try (FileWriter file = new FileWriter(MEMORY_FILE, true)) {
            file.write(key + "=" + value + System.lineSeparator());

        } catch (IOException e) {
            System.err.println("❌ Error al guardar: " + e.getMessage());
        }
    }

    /**
     * Carga memoria previa desde archivo.
     */
    public void loadMemory() {
        File file = new File(MEMORY_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    memory.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("⚠ No se pudo cargar memoria: " + e.getMessage());
        }
    }


    /**
     * Devuelve sugerencias desde memoria que empiecen con cierto prefijo.
     */
    public List<String> suggestions(String prefix) {
        List<String> list = new ArrayList<>();
        for (String k : memory.keySet()) {
            if (k.startsWith(prefix)) list.add(k);
        }
        return list;
    }
}

     
