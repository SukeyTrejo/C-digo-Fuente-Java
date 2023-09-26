package com.system.hilos;

import com.system.panels.VentasPanel;

public class VentasHilo implements Runnable {
	private VentasPanel ventasPanel;
	private String cadena;
	
	public VentasHilo( VentasPanel ventasPanel, String cadena) {
		this.ventasPanel = ventasPanel;
		this.cadena = cadena;
	}
	
	@Override
	public void run() {
		this.ventasPanel.crearModeloTabla(cadena);
	}
}
