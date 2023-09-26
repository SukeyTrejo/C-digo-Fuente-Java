package com.system.hilos;

import com.system.panels.InformePanel;

public class InformeProductosHilo implements Runnable {
	private InformePanel informePanel;
	
	public InformeProductosHilo( InformePanel informePanel) {
		this.informePanel = informePanel;
	}
	
	@Override
	public void run() {
		this.informePanel.crearModeloTablaProductos();
	}
}
