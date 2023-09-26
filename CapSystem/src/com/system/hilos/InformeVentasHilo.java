package com.system.hilos;

import com.system.panels.InformePanel;

public class InformeVentasHilo implements Runnable {
	private InformePanel informePanel;
	
	public InformeVentasHilo( InformePanel informePanel) {
		this.informePanel = informePanel;
	}
	
	@Override
	public void run() {
		this.informePanel.crearModeloTablaVentas();
	}
}
