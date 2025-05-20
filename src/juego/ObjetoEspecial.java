package juego;

import multimedia.Dibujable;
import multimedia.Lienzo;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ObjetoEspecial implements Dibujable {
    private Image imagen;
    private Posicion posicion;
    private Lienzo lienzo;
    private Mapa mapa;
    private int puntos;
    protected boolean visible;
    private int tiempoVisible;

    public ObjetoEspecial(String rutaImagen, int puntos, Mapa mapa) {
        try {
            this.imagen = ImageIO.read(new File("src/assets/" + rutaImagen));
        } catch (IOException e){
            throw new RuntimeException("No se puede cargar la imagen: " + e);
        }
        this.puntos = puntos;
        this.mapa = mapa;
        this.visible = false;
        this.tiempoVisible = 350;

    }

    public void aparecer() {
        this.posicion = mapa.obtenerPosicionVaciaAleatoria();

        this.visible = true;
        this.tiempoVisible = 350;
    }

    public void tick(){
        if (visible){
            tiempoVisible--;
            if (tiempoVisible <= 0){
                visible = false;
            }
        }
    }

    public void setLienzo(Lienzo lienzo) {
        this.lienzo = lienzo;
    }

    public void dibujar() {
        if (visible && lienzo != null){
            lienzo.dibujarImagen(posicion.getX(), posicion.getY(), imagen);
        }
    }

    public boolean colisionaPacman(Posicion posicionPacman) {
        return visible && posicionPacman.equals(posicionPacman);
    }

    public int getPuntos() {
        return puntos;
    }

    public boolean isVisible() {
        return visible;
    }
}
