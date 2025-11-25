import java.util.*;

class Compress {
    private char[] dir = null;
    private StringBuilder buffer = null;
    private int SIZE_DIR = 0;

    // Método para comprimir texto usando LZ77
    public String compressText(String text) {
        dir = text.toCharArray();
        SIZE_DIR = dir.length; 
        buffer = new StringBuilder();

        for (int i = 0; i < SIZE_DIR; i++) {
            int longestMatchLength = 0;
            int matchDistance = 0;

            for (int j = 0; j < i; j++) {
                int matchLen = 0;
                while (i + matchLen < SIZE_DIR && dir[j + matchLen] == dir[i + matchLen]) {
                    matchLen++;
                }

                if (matchLen > longestMatchLength) {
                    longestMatchLength = matchLen;
                    matchDistance = i - j;
                }
            }

            if (longestMatchLength > 0) {
                buffer.append("(").append(matchDistance).append(",").append(longestMatchLength).append(")");
                i += longestMatchLength - 1; // Skip the matched characters
            } else {
                buffer.append("(").append(0).append(",").append(0).append(",").append(dir[i]).append(")");
            }
        }
        return buffer.toString(); // Return the compressed string
    }

    // Método para descomprimir usando LZ77
    public String decompressLZ77(String compressedText) {
        StringBuilder originalText = new StringBuilder();
        String[] tokens = compressedText.split("\\)");

        for (String token : tokens) {
            if (token.trim().isEmpty()) continue;

            String[] parts = token.substring(1).split(",");
            int distance = Integer.parseInt(parts[0].trim());
            int length = Integer.parseInt(parts[1].trim());

            if (distance == 0 && length == 0) {
                originalText.append(parts[2].trim()); // Literal character
            } else {
                int start = originalText.length() - distance;
                for (int i = 0; i < length; i++) {
                    originalText.append(originalText.charAt(start + i)); // Repeated part
                }
            }
        }
        return originalText.toString(); // Return the decompressed text
    }
}

class HuffmanNode {
    int frequency;
    char character;
    HuffmanNode left, right;

    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = this.right = null;
    }

    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
}

class Huffman {
    private Map<Character, String> huffmanCodes = new HashMap<>();
    private PriorityQueue<HuffmanNode> priorityQueue;

    // Construir el árbol de Huffman
    public void buildTree(Map<Character, Integer> freqMap) {
        priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.frequency));

        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            int sum = left.frequency + right.frequency;
            priorityQueue.add(new HuffmanNode(sum, left, right));
        }

        generateCodes(priorityQueue.peek(), "");
    }

    // Generar los códigos de Huffman
    private void generateCodes(HuffmanNode node, String code) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                huffmanCodes.put(node.character, code);
            }
            generateCodes(node.left, code + "0");
            generateCodes(node.right, code + "1");
        }
    }

    // Codificar el texto usando los códigos de Huffman
    public String encode(String input) {
        StringBuilder encodedText = new StringBuilder();
        for (char character : input.toCharArray()) {
            encodedText.append(huffmanCodes.get(character));
        }
        return encodedText.toString();
    }

    // Decodificar el texto usando los códigos de Huffman
    public String decode(String encodedText) {
        StringBuilder decodedText = new StringBuilder();
        HuffmanNode current = priorityQueue.peek();

        for (char bit : encodedText.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;

            if (current.left == null && current.right == null) {
                decodedText.append(current.character);
                current = priorityQueue.peek(); // volver a la raíz del árbol
            }
        }

        return decodedText.toString();
    }

    public Map<Character, String> getHuffmanCodes() {
        return huffmanCodes;
    }
}

public class Main {
    public static void main(String[] args) {
        // 1. Compresión LZ77
        String text = "ABABABABABAB";
        Compress compressor = new Compress();
        String lz77Compressed = compressor.compressText(text);
        System.out.println("Texto comprimido con LZ77: " + lz77Compressed);

        // 2. Crear un mapa de frecuencias para Huffman
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : lz77Compressed.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        // 3. Aplicar Huffman
        Huffman huffman = new Huffman();
        huffman.buildTree(freqMap);

        // 4. Codificar la salida de LZ77 con Huffman
        String huffmanEncoded = huffman.encode(lz77Compressed);
        System.out.println("Texto codificado con Huffman: " + huffmanEncoded);

        // 5. Decodificación: Primero decodificamos con Huffman
        String huffmanDecoded = huffman.decode(huffmanEncoded);
        System.out.println("Texto decodificado con Huffman: " + huffmanDecoded);

        // 6. Descompresión: Luego descomprimimos con LZ77
        String lz77Decoded = compressor.decompressLZ77(huffmanDecoded);
        System.out.println("Texto original descomprimido: " + lz77Decoded);
    }
}

