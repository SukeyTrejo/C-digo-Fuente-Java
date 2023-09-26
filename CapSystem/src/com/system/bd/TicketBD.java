package com.system.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.system.objetos.Marca;
import com.system.objetos.ProductoCompra;
import com.system.objetos.Venta;

public class TicketBD {
	private Connection conexion;
	
	public TicketBD( Connection conexion ) {
		this.conexion = conexion;
	}
	
	public Venta informacionVenta( int id ) {
		Venta venta = new Venta();
		
		if (conexion != null) {
			
			String sqlBuscarUltimoRegistro = "SELECT * FROM ventas "
					+ "WHERE id_venta = " + id + "";
			
			Statement stmt = null;
			ResultSet rs = null;
			
			try {
				stmt = conexion.createStatement();
				rs = stmt.executeQuery( sqlBuscarUltimoRegistro );
				
				if (rs.next()) {
					venta.setIdVenta( rs.getInt(1) );
					venta.setFecha_venta( rs.getString(2) );
					venta.setHora_venta( rs.getString(3) );
				}
				
				venta.setListaProductos( recuperarProductos( venta ));
				
			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				try {
					stmt.close();
					rs.close();
					conexion.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
					
		} else {
			System.err.println("Debes inicializar la conexion");
		}
		
		return venta;
	}
	
	private List<ProductoCompra> recuperarProductos( Venta venta) {
		int total = 0;
		List<ProductoCompra> productos = new ArrayList<ProductoCompra>();
		
		if (conexion != null) {
			Statement stmt = null;
			ResultSet rs = null;
			
			 String sqlInformeVentas = "SELECT p.id_producto, p.modelo_producto, p.tipo_producto, m.nombre_marca, prov.nombre_proveedor, rv.cantidad_producto, rv.precio_unitario_producto "
			 		+ "FROM registro_ventas AS rv "
			 		+ "INNER JOIN productos AS p ON rv.id_producto_venta = p.id_producto "
			 		+ "INNER JOIN marcas AS m ON p.id_marca_producto = m.id_marcas "
			 		+ "INNER JOIN proveedores AS prov ON m.proveedor_id = prov.id_proveedor "
			 		+ "WHERE rv.id_registro_venta = " + venta.getIdVenta() + "";
			 
			try {
				
				stmt = conexion.createStatement();
				rs = stmt.executeQuery(sqlInformeVentas);
				
				
				while (rs.next()) {
					ProductoCompra productoCompra = new ProductoCompra();
					productoCompra.setId( rs.getInt(1));
					productoCompra.setModelo( rs.getString(2));
					productoCompra.setTipo( rs.getString(3));
					
					Marca marcaInfo = new Marca();
					marcaInfo.setNombre( rs.getString(4));
					marcaInfo.setNombreProveedor( rs.getString(5));
					
					productoCompra.setMarca(marcaInfo);
					productoCompra.setCantidad( rs.getInt(6));
					productoCompra.setPrecio( rs.getDouble(7));
					
					total += ( productoCompra.getCantidad() * productoCompra.getPrecio() );
					
					productos.add( productoCompra);	
				}
				
				venta.setTotal(total);
				
			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				try {
					stmt.close();
					rs.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		} else {
			System.err.println("Debes inicializar la conexion");
		}
		
		return productos;
	}
}
