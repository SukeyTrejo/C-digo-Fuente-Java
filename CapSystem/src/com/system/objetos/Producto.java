package com.system.objetos;

public class Producto {
	private int id;
	private String modelo;
	private String tipo;
	private double precio;
	private Marca marca;
	private int stock;
	private byte[] imagenProducto;
	private int estado;

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getModelo() {
		return modelo;
	}
	
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public double getPrecio() {
		return precio;
	}
	
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	public Marca getMarca() {
		return marca;
	}
	
	public void setMarca(Marca marca) {
		this.marca = marca;
	}
	
	public int getStock() {
		return stock;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public byte[] getImagenProducto() {
		return imagenProducto;
	}
	
	public void setImagenProducto(byte[] imagenProducto) {
		this.imagenProducto = imagenProducto;
	}
	
	public int getEstado() {
		return estado;
	}
	
	public void setEstado(int estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "<html> <p> <strong> ID: </strong> " + id + ","
				+ "<br> <strong> Modelo: </strong> " + modelo + ","
				+ "<br> <strong> Tipo: </strong>" + tipo + ","
				+ "<br> <strong> Precio: </strong> " + precio + ","
				+ "<br> <strong> Marca: </strong> " + marca + ", "
				+ "<br> <strong> Stock: </strong> " + stock + "<br><br></p></html>";
	}

	
}
