package com.system.objetos;

public class ProductoCompra extends Producto {
	private int cantidad;

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	@Override
	public String toString() {
		return "<html> <br> <div> <strong style='color: #FF0000'> ID: " + super.getId() + "</strong> ,"
				+ "<br> <strong> Modelo: </strong> " + super.getModelo() + ","
				+ "<br> <strong> Tipo: </strong>" + super.getTipo() + ","
				+ "<br> <strong> Precio: </strong> " + super.getPrecio() + ","
				+ "<br> <strong> Marca: </strong> " + super.getMarca()+ ", "
				+ "<br> <strong style='color: #FF0000'> Cantidad: " + this.cantidad+ "</strong><div> <br><br></html>";
	}
}
