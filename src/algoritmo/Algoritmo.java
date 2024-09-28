package algoritmo;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Algoritmo {

    public static void main(String[] args) {
        Scanner tec = new Scanner(System.in);
        System.out.println("Introduce la cadena a comprimir");
        String cadena = tec.nextLine();
        
        Comprimir(cadena);
        tec.close();
    }
    
    private static void Comprimir(String cadena) {

        byte[] bytes = cadena.getBytes(StandardCharsets.UTF_8); 
        for (int i = 0; i < bytes.length; i++) {

            if ((bytes[i] & 0x80) != 0) { 
                StringBuilder bits = new StringBuilder();
                StringBuilder caracteres = new StringBuilder();
                
                int numBytes = 1;
                while (i + numBytes < bytes.length && (bytes[i + numBytes] & 0xC0) == 0x80) {
                    numBytes++;
                }
                
                byte[] multibyteCharacter = new byte[numBytes];
                for (int j = 0; j < numBytes; j++) {
                    multibyteCharacter[j] = bytes[i + j]; 
                    bits.append(String.format("%8s", Integer.toBinaryString(multibyteCharacter[j] & 0xFF)).replace(' ', '0')).append(" ");
                }

                String characterString = new String(multibyteCharacter, StandardCharsets.UTF_8);
                
                System.out.println("Carácter: " + characterString + " -> Bits: " + bits.toString());
                
                i += numBytes - 1;
            } else {
                char caracter = (char) bytes[i];
                String bits = String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0'); 
                System.out.println("Carácter: " + caracter + " -> Bits: " + bits); 
            }
        }
    }
}
