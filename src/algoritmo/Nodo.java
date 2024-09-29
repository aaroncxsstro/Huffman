package algoritmo;

public class Nodo {
    String bits; // Representación en bits
    int frecuencia; // Frecuencia de los bits
    Nodo izquierda, derecha; // Hijos izquierdo y derecho

    // Constructor para un nodo hoja
    public Nodo(String bits, int frecuencia) {
        this.bits = bits;
        this.frecuencia = frecuencia;
        this.izquierda = null;
        this.derecha = null;
    }
    
    // Constructor para un nodo interno
    public Nodo(int frecuencia, Nodo izquierda, Nodo derecha) {
        this.frecuencia = frecuencia;
        this.izquierda = izquierda;
        this.derecha = derecha;
        this.bits = ""; // Este campo no es relevante para nodos internos
    }
}
