package com.system.hilos;

import com.system.panels.MarcasPanel;

public class MarcasHiloCB implements Runnable {
	private MarcasPanel marcasPanel;
	
	public MarcasHiloCB( MarcasPanel marcasPanel ) {
		this.marcasPanel = marcasPanel;
	}
	
	@Override
	public void run() {
		this.marcasPanel.llenarComboProveedores();
	}
}
