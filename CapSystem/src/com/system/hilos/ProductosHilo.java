package com.system.hilos;

import com.system.panels.ProductosPanel;

public class ProductosHilo implements Runnable {
	private ProductosPanel productosPanel;
	private String cadena;
	
	public ProductosHilo( ProductosPanel productosPanel, String cadena) {
		this.productosPanel = productosPanel;
		this.cadena = cadena;
	}
	
	@Override
	public void run() {
		this.productosPanel.crearModeloTabla(cadena);
	}
}
