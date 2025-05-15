package juego;

import multimedia.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class Actor implements Dibujable {
    protected Coordinador coordinador;
    private Lienzo lienzo;
    protected Image imagen;
    protected Posicion posicion;
    protected Mapa mapa;

    public Actor(String nombreFicheroImagen, Coordinador coordinador, Lienzo lienzo, Mapa mapa) {
        this.coordinador = coordinador;
        this.mapa = mapa;

        setLienzo(lienzo);

        try {
            imagen = ImageIO.read(new File("src/assets/" + nombreFicheroImagen));
        } catch (IOException e){
            throw new RuntimeException("No se puede cargar la imagen: " + e);
        }
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setLienzo(Lienzo lienzo) {
        this.lienzo = lienzo;
    }

    public void mover(Direccion dir) throws MovimientoInvalidoException {
        Posicion destino = mapa.calcularDestino(posicion, dir);
        if (destino.equals(posicion)) throw new MovimientoInvalidoException("Movimiento no válido.");
        this.posicion = destino;
    }

    public void dibujar() {
        lienzo.dibujarImagen(posicion.getX(), posicion.getY(), imagen);
    }
}