package juego;

import multimedia.Dibujable;
import multimedia.Lienzo;

import java.awt.*;

public class EstadoJuego implements Dibujable {
    private int puntuacion;
    private Lienzo lienzo;

    public EstadoJuego(Lienzo lienzo) {
        setLienzo(lienzo);
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public void incrementarPuntuacion() {
        puntuacion++;
    }

    public void setLienzo(Lienzo lienzo) {
        this.lienzo = lienzo;
    }

    public void dibujar() {
        lienzo.escribirTexto(0, 0, "Puntuación: " + puntuacion, Color.GREEN);
    }
}