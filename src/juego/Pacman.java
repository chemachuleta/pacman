package juego;

import multimedia.*;

import java.awt.event.KeyEvent;

public class Pacman extends Actor {
    private EstadoJuego estado;
    private Teclado teclado;
    private Mapa mapa;

    public Pacman(Coordinador coordinador, Lienzo lienzo, Teclado teclado, Mapa mapa, EstadoJuego estado) {
        super("Pacman32.png", coordinador, lienzo, mapa);
        this.estado = estado;
        this.teclado = teclado;
        this.posicion = new Posicion(7, 8);
        this.mapa = mapa;
    }

    public void tick() throws SalirDelJuegoException {
        try {
            if (teclado.pulsada(KeyEvent.VK_UP) || teclado.pulsada(KeyEvent.VK_W)) mover(Direccion.ARR);
            if (teclado.pulsada(KeyEvent.VK_LEFT) || teclado.pulsada(KeyEvent.VK_A)) mover(Direccion.IZD);
            if (teclado.pulsada(KeyEvent.VK_DOWN) || teclado.pulsada(KeyEvent.VK_S)) mover(Direccion.ABA);
            if (teclado.pulsada(KeyEvent.VK_RIGHT) || teclado.pulsada(KeyEvent.VK_D)) mover(Direccion.DCH);
            if (teclado.pulsada(KeyEvent.VK_Q)) throw new SalirDelJuegoException("Saliendo del juego...");
        } catch (MovimientoInvalidoException e) {
            // No hacemos nada. Pierde el turno.
        }
    }
}
