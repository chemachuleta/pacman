package juego;

import multimedia.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Mapa implements Dibujable {
    private static final Color COLOR_SUELO = Color.BLACK;
    private static final int TAM_CELDA = 32;
    private Image imagenMuro;
    private Image imagenMoneda;
    private Lienzo lienzo;
    private char[][] mapa;


    public Mapa(Lienzo lienzo) {
        setLienzo(lienzo);
        try {
            this.imagenMuro = ImageIO.read(new File("src/assets/Muro32.png"));
            Image monedaOriginal = ImageIO.read(new File("src/assets/Moneda32.png"));
            this.imagenMoneda = monedaOriginal.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException("No se puede cargar la imagen: " + e);
        }
    }

    public void setLienzo(Lienzo lienzo) {
        this.lienzo = lienzo;
    }

    public int getAncho() {
        return mapa[1].length;
    }

    public void cargarNivel(Nivel nivel) {
        this.mapa = nivel.getDiseño();
        generarPuntos();
    }

    public int getAlto() {
        return mapa.length;
    }


    private char getContenidoMapa(int x, int y) {
        return mapa[y][x];
    }

    private char getContenidoMapa(Posicion posicion) {
        return mapa[posicion.getY()][posicion.getX()];
    }

    private void setContenidoMapa(int x, int y, char c) {
        mapa[y][x] = c;
    }

    private void setContenidoMapa(Posicion posicion, char c) {
        mapa[posicion.getY()][posicion.getX()] = c;
    }

    public boolean esTransitable(Posicion posicion) {
        int x = posicion.getX();
        int y = posicion.getY();

        return x >= 0 && x < mapa.length && y >= 0 && y < mapa[0].length && getContenidoMapa(x, y) != '#';
    }

    // TODO Esto del destino está hecho raro... Lo suyo sería que el actor se moviera hacia donde sea, o que, si no se puede, se lanzase una excepción y gestionarla. Pero esto de estar usando Posiciones de usar y tirar no mola.
    public Posicion calcularDestino(Posicion actual, Direccion dir) {
        Posicion nueva = actual.desplazar(dir);
        if (esTransitable(nueva)) return nueva;
        return actual;
    }

    public void generarPuntos() {
        for (int x = 0; x < mapa.length; x++) {
            for (int y = 0; y < mapa[0].length; y++) {
                if (getContenidoMapa(x, y) == ' ') setContenidoMapa(x, y, '·');
            }
        }
    }

    public boolean hayPunto(Posicion posicion) {
        return getContenidoMapa(posicion) == '·';
    }

    public boolean quedanMonedas() {
        for (int x = 0; x < getAncho(); x++) {
            for (int y = 0; y < getAlto(); y++) {
                if (getContenidoMapa(x, y) == '·') {
                    return true;
                }
            }
        }
        return false;
    }

    public void retirarPunto(Posicion posicion) {
        setContenidoMapa(posicion, ' ');
    }

    public void dibujar() {
        lienzo.limpiar();

        for (int x = 0; x < getAncho(); x++) {
            for (int y = 0; y < getAlto(); y++) {
                lienzo.marcarPixel(x, y, COLOR_SUELO);

                if (getContenidoMapa(x, y) == '#') {
                    lienzo.dibujarImagen(x, y, this.imagenMuro);
                } else if (getContenidoMapa(x, y) == '·') {
                    dibujarMonedaCentrada(x, y);
                }
            }
        }
    }

    private void dibujarMonedaCentrada(int celdaX, int celdaY) {
        int anchoMoneda = imagenMoneda.getWidth(null);
        int altoMoneda = imagenMoneda.getHeight(null);
        int posXPixel = celdaX * TAM_CELDA + (TAM_CELDA - anchoMoneda) / 2;
        int posYPixel = celdaY * TAM_CELDA + (TAM_CELDA - altoMoneda) / 2;

        Graphics2D g = ((VentanaMultimedia)lienzo).getGraphics2D();
        g.drawImage(imagenMoneda, posXPixel, posYPixel, null);
    }
}
