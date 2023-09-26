package com.system.hilos;

import com.system.panels.ProveedoresPanel;

public class ProveedoresHilo implements Runnable{
	private String cadena;
	private ProveedoresPanel proveedoresPanel;
	
	public ProveedoresHilo( ProveedoresPanel proveedoresPanel, String cadena) {
		this.proveedoresPanel = proveedoresPanel;
		this.cadena = cadena;
	}
	
	@Override
	public void run() {
		this.proveedoresPanel.crearModeloTabla(cadena);
	}
}
