package com.system.hilos;

import com.system.panels.MarcasPanel;

public class MarcasHilo implements Runnable{
	private String cadena;
	private MarcasPanel marcasPanel;
	
	public MarcasHilo( MarcasPanel marcasPanel, String cadena) {
		this.marcasPanel = marcasPanel;
		this.cadena = cadena;
	}
	
	@Override
	public void run() {
		this.marcasPanel.crearModeloTabla(cadena);
	}
}
