package juego;

import multimedia.*;

import java.util.Random;

public class Fantasma extends Actor {
    private Random random = new Random();

    private final Pacman pacman;

    public Fantasma(Coordinador coordinador, Lienzo lienzo, Pacman pacman, Mapa mapa) {
        super("FantasmaNaranja32.png", coordinador, lienzo, mapa);
        this.pacman = pacman;
        posicion = coordinador.obtenerPosicionVaciaAleatoria();
    }

    public void tick() throws PacmanComidoException {
        if (this.posicion.equals(pacman.getPosicion())) throw new PacmanComidoException("¡Pacman ha sido comido!");

        boolean movido = false;

        while (!movido) {
            try {
                Direccion dir = Direccion.values()[random.nextInt(4)];
                mover(dir);
                movido = true;
            } catch (MovimientoInvalidoException ignored) {
            }
        }

        if (this.posicion.equals(pacman.getPosicion())) throw new PacmanComidoException("¡Pacman ha sido comido!");
    }
}
