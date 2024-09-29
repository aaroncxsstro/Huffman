package algoritmo;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Algoritmo {

    public static void main(String[] args) {
        Scanner tec = new Scanner(System.in);
        System.out.println("Introduce la cadena a comprimir"); 
        String cadena = tec.nextLine(); 
        
        // Extraigo la representación en bits de la cadena
        List<String> bitsRepresentacion = extraerBytes(cadena);
        
        // Llamo al método Comprimir para comprimir la representación en bits
        Map<Character, String> codigoHuffman = construirArbolHuffman(cadena);
        String cadenaComprimida = comprimir(cadena, codigoHuffman);
        
        // Mostrar la cadena en bits y su recuento de bits
        String cadenaBits = String.join(" ", bitsRepresentacion);
        System.out.println("Cadena sin comprimir en bits: " + cadenaBits);
        System.out.println("Recuento de bits (sin comprimir): " + (cadena.getBytes(StandardCharsets.UTF_8).length * 8));

        // Mostrar la cadena comprimida y su recuento de bits
        System.out.println("Cadena comprimida: " + cadenaComprimida);
        System.out.println("Recuento de bits (comprimida): " + obtenerRecuentoBitsComprimidos(cadenaComprimida));
        
        // Cierro el Scanner para evitar fugas de recursos
        tec.close();
    }
    
    // Método que extrae los bytes de la cadena y devuelve la representación en bits
    private static List<String> extraerBytes(String cadena) {
        List<String> bitsRepresentacion = new ArrayList<>(); 
        byte[] bytes = cadena.getBytes(StandardCharsets.UTF_8); 
        
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0x80) != 0) {
                int numBytes = 1; 
                
                while (i + numBytes < bytes.length && (bytes[i + numBytes] & 0xC0) == 0x80) {
                    numBytes++; 
                }
                
                byte[] multibyteCharacter = new byte[numBytes];
                for (int j = 0; j < numBytes; j++) {
                    multibyteCharacter[j] = bytes[i + j]; 
                    String bits = String.format("%8s", Integer.toBinaryString(multibyteCharacter[j] & 0xFF)).replace(' ', '0');
                    bitsRepresentacion.add(bits);
                }

                i += numBytes - 1;
            } else {
                String bits = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0'); 
                bitsRepresentacion.add(bits);
            }
        }

        return bitsRepresentacion;
    }

    // Método para construir el árbol de Huffman
    private static Map<Character, String> construirArbolHuffman(String cadena) {
        Map<Character, Integer> frecuencia = new HashMap<>();
        
        // Contar la frecuencia de cada carácter
        for (char c : cadena.toCharArray()) {
            frecuencia.put(c, frecuencia.getOrDefault(c, 0) + 1);
        }
        
        // Crear una cola de prioridad
        PriorityQueue<Nodo> colaPrioridad = new PriorityQueue<>(Comparator.comparingInt(n -> n.frecuencia));
        
        // Crear nodos hoja para cada carácter y añadirlos a la cola
        for (Map.Entry<Character, Integer> entry : frecuencia.entrySet()) {
            colaPrioridad.add(new Nodo(String.valueOf(entry.getKey()), entry.getValue()));
        }

        // Construir el árbol de Huffman
        while (colaPrioridad.size() > 1) {
            Nodo izquierda = colaPrioridad.poll();
            Nodo derecha = colaPrioridad.poll();
            Nodo nuevoNodo = new Nodo(izquierda.frecuencia + derecha.frecuencia, izquierda, derecha);
            colaPrioridad.add(nuevoNodo);
        }
        
        // Obtener el nodo raíz del árbol
        Nodo raiz = colaPrioridad.poll();
        
        // Crear un mapa para almacenar el código de Huffman
        Map<Character, String> codigoHuffman = new HashMap<>();
        generarCodigoHuffman(raiz, "", codigoHuffman);
        
        return codigoHuffman;
    }

    // Método recursivo para generar el código de Huffman
    private static void generarCodigoHuffman(Nodo nodo, String codigo, Map<Character, String> codigoHuffman) {
        if (nodo.izquierda == null && nodo.derecha == null) {
            // Es un nodo hoja, guardar el código
            codigoHuffman.put(nodo.bits.charAt(0), codigo);
            return;
        }
        // Recorrer los hijos
        generarCodigoHuffman(nodo.izquierda, codigo + "0", codigoHuffman);
        generarCodigoHuffman(nodo.derecha, codigo + "1", codigoHuffman);
    }

 // Método de compresión que recibe la cadena y el código de Huffman
    private static String comprimir(String cadena, Map<Character, String> codigoHuffman) {
        StringBuilder cadenaComprimida = new StringBuilder();
        for (char c : cadena.toCharArray()) {
            cadenaComprimida.append(codigoHuffman.get(c)).append(" "); // Agregar el código correspondiente y un espacio
        }
        return cadenaComprimida.toString().trim(); // Retornar la cadena sin el espacio final
    }

    // Método para obtener el recuento de bits de la cadena comprimida
    private static int obtenerRecuentoBitsComprimidos(String cadenaComprimida) {
        // Contar solo los caracteres de la cadena comprimida (sin espacios)
        return Arrays.stream(cadenaComprimida.split(" "))
                     .mapToInt(String::length)
                     .sum();
    }
}
