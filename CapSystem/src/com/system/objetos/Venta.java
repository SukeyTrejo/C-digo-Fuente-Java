package com.system.objetos;

import java.util.List;

public class Venta {
	private int idVenta;
	private String fecha_venta;
	private String hora_venta;
	private List<ProductoCompra> listaProductos;
	private double total;
	
	public int getIdVenta() {
		return idVenta;
	}
	public void setIdVenta(int idVenta) {
		this.idVenta = idVenta;
	}
	public String getFecha_venta() {
		return fecha_venta;
	}
	public void setFecha_venta(String fecha_venta) {
		this.fecha_venta = fecha_venta;
	}
	public String getHora_venta() {
		return hora_venta;
	}
	public void setHora_venta(String hora_venta) {
		this.hora_venta = hora_venta;
	}
	public List<ProductoCompra> getListaProductos() {
		return listaProductos;
	}
	public void setListaProductos(List<ProductoCompra> listaProductos) {
		this.listaProductos = listaProductos;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	@Override
	public String toString() {
		return "Venta [idVenta=" + idVenta + ", fecha_venta=" + fecha_venta + ", hora_venta=" + hora_venta
				+ ", listaProductos=" + listaProductos + "]";
	}
	
	
}
