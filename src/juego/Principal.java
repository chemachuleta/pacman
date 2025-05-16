package juego;

import multimedia.*;

import java.awt.*;

public class Principal {
    private static final int MILLIS = 150;
    private static final int TAM_PIXEL = 32;

    public static void espera(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        int anchoVentana = 15;
        int altoVentana = 15;
        Color colorFondo = Color.BLACK;

        VentanaMultimedia ventana = new VentanaMultimedia("PacMan", anchoVentana, altoVentana, TAM_PIXEL, colorFondo);
        Coordinador coordinador = new Coordinador(ventana, ventana.getTeclado());
        try {
            while (true) {
                coordinador.dibujar();
                ventana.getTeclado().tick();
                coordinador.tick();
                espera(MILLIS);
            }
        } catch (NivelCompletadoException e) {
            System.out.println(e.getMessage());
            try {
                coordinador.pasarSiguienteNivel();
            } catch (JuegoCompletadoException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Puntuación final: " + coordinador.getEstado().getPuntuacion());
            }
        } catch (PacmanComidoException e) {
            System.out.println("¡Game Over! Te han comido.");
        } catch (SalirDelJuegoException e) {
            System.out.println("Has elegido salir del juego.");
        } catch (JuegoCompletadoException e) {
            System.out.println(e.getMessage());
        }
    }
}
