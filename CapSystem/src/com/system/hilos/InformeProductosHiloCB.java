package com.system.hilos;

import com.system.panels.InformePanel;

public class InformeProductosHiloCB implements Runnable {
private InformePanel informePanel;
	
	public InformeProductosHiloCB( InformePanel informePanel) {
		this.informePanel = informePanel;
	}
	
	@Override
	public void run() {
		this.informePanel.llenarComboMarcas();
		this.informePanel.llenarComboProveedores();
	}
}
