package juego;

import multimedia.*;

import java.util.ArrayList;
import java.util.List;

public class Coordinador implements Dibujable {
    private List<Nivel> niveles;
    private int nivelActual;
    private EstadoJuego estado;
    private Mapa mapa;
    private Pacman pacman;
    private ArrayList<Fantasma> fantasmas = new ArrayList<>();
    private Lienzo lienzo;
    private Teclado teclado;

    public Coordinador(Lienzo lienzo, Teclado teclado) {
        this.lienzo = lienzo;
        this.teclado = teclado;
        this.niveles = Nivel.crearNiveles();
        this.nivelActual = 0;

        iniciarNivel(niveles.get(nivelActual));
    }

    private void iniciarNivel(Nivel nivel) {
        estado = new EstadoJuego(lienzo);
        mapa = new Mapa(lienzo);
        mapa.cargarNivel(nivel);
        situarActores();
    }

    public void setLienzo(Lienzo lienzo) {
        this.lienzo = lienzo;
    }

    private void situarActores() {
        pacman = new Pacman(this, lienzo, teclado, mapa, estado);
        fantasmas.clear();

        Nivel nivelActual = niveles.get(this.nivelActual);
        for (int i = 0; i < nivelActual.getNumeroFantasmas(); i++) {
            fantasmas.add(new Fantasma(this, lienzo, pacman, mapa));
        }
    }

    private boolean estaLibre(Posicion posicion) {
        if (!mapa.esTransitable(posicion)) return false;

        if (posicion.equals(pacman.getPosicion())) return false;

        for (Fantasma fantasma : fantasmas) {
            if (posicion.equals(fantasma.getPosicion())) return false;
        }

        return true;
    }

    public EstadoJuego getEstado() {
        return this.estado;
    }

    public Posicion obtenerPosicionVaciaAleatoria() {
        Posicion posicion;

        do {
            posicion = new Posicion(mapa);
        } while (!estaLibre(posicion));

        return posicion;
    }

    public void tick() throws PacmanComidoException, SalirDelJuegoException, NivelCompletadoException, JuegoCompletadoException {
        pacman.tick();

        if (mapa.hayPunto(pacman.getPosicion())) {
            estado.incrementarPuntuacion();
            mapa.retirarPunto(pacman.getPosicion());

            if (!mapa.quedanMonedas()) {
                nivelActual++;
                if (nivelActual < niveles.size()) {
                    throw new NivelCompletadoException("¡Nivel " + (nivelActual) + " completado!");
                } else {
                    throw new JuegoCompletadoException("¡Has completado todos los niveles!");
                }
            }
        }

        for (Fantasma fantasma : fantasmas) {
            fantasma.tick();
        }
    }

    public void dibujar() {
        lienzo.limpiar();

        mapa.dibujar();
        pacman.dibujar();

        for (Fantasma fantasma : fantasmas) {
            fantasma.dibujar();
        }

        estado.dibujar();

        lienzo.volcar();
    }

    public int getNivelActual() {
        return this.nivelActual;
    }

    public void pasarSiguienteNivel() {
        if (nivelActual < niveles.size()) {
            nivelActual++;
            iniciarNivel(niveles.get(nivelActual));
            situarActores();
        }
    }
}
