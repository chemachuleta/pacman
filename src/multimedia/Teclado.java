package multimedia;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Teclado implements KeyListener, Tickable {
	private static final int NUM_TECLAS = 256;
	
	private boolean[] estadoTeclasBasico;

	private enum EstadoTeclaCon1Vez {
		LIBRE,
		PRESIONADA_1VEZ,
		PRESIONADA_MAS
	}

	private EstadoTeclaCon1Vez[] estadoTeclasCon1Vez;

	public Teclado() {
		estadoTeclasBasico = new boolean[NUM_TECLAS];
		
		estadoTeclasCon1Vez = new EstadoTeclaCon1Vez[NUM_TECLAS];
		for (int i = 0; i < NUM_TECLAS; ++i) {
			estadoTeclasCon1Vez[i] = EstadoTeclaCon1Vez.LIBRE;
		}
	}

	public boolean pulsada(int codigoTecla) {
		return estadoTeclasBasico[codigoTecla];
	}

	public synchronized void procesarEstadosCon1Vez() {
		for (int i = 0; i < NUM_TECLAS; ++i) {
			if (estadoTeclasBasico[i]) {
				if (estadoTeclasCon1Vez[i] == EstadoTeclaCon1Vez.LIBRE) {
					estadoTeclasCon1Vez[i] = EstadoTeclaCon1Vez.PRESIONADA_1VEZ;
				} else {
					estadoTeclasCon1Vez[i] = EstadoTeclaCon1Vez.PRESIONADA_MAS;
				}
			} else {
				estadoTeclasCon1Vez[i] = EstadoTeclaCon1Vez.LIBRE;
			}
		}
	}

	public boolean pulsada1Vez(int codigoTecla) {
		return estadoTeclasCon1Vez[codigoTecla] == EstadoTeclaCon1Vez.PRESIONADA_1VEZ;
	}

	public synchronized void keyPressed(KeyEvent evento) {
		int codigo = evento.getKeyCode();

		if (codigo >= 0 && codigo < NUM_TECLAS) {
			estadoTeclasBasico[codigo] = true;
		}
	}

	public synchronized void keyReleased(KeyEvent evento) {
		int codigo = evento.getKeyCode();

		if (codigo >= 0 && codigo < NUM_TECLAS) {
			estadoTeclasBasico[codigo] = false;
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void tick() {
		procesarEstadosCon1Vez();
	}
}