package com.system.hilos;

import com.system.panels.ProductosPanel;

public class ProductosHiloCB implements Runnable{
	private ProductosPanel productosPanel;
	
	public ProductosHiloCB( ProductosPanel productosPanel ) {
		this.productosPanel = productosPanel;
	}
	
	@Override
	public void run() {
		this.productosPanel.llenarComboMarcas();
	}
}
