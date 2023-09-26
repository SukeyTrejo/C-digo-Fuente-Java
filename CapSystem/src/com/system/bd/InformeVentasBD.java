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

public class InformeVentasBD {
	private Connection conexion;

	public InformeVentasBD(Connection conexion) {
		this.conexion = conexion;
	}
	
	public List<Venta> informeVentas( String fechaIni, String fechaFin) {
		List<Venta> ventas = new ArrayList<Venta>();
		
		if (conexion != null) {
			Statement stmt = null;
			ResultSet rs = null;
			
			 String sqlInformeVentas = "SELECT v.id_venta, v.fecha_venta, v.hora_venta, sum( rv.precio_unitario_producto * rv.cantidad_producto ) 'total_venta' "
					+ "FROM ventas as v "
					+ "INNER JOIN registro_ventas as rv ON v.id_venta = id_registro_venta "
					+ "WHERE (fecha_venta >= '" + fechaIni + "' AND fecha_venta <= '" + fechaFin + "' ) "
					+ "GROUP BY v.id_venta "
					+ "ORDER BY v.id_venta DESC";
			
			try {
				
				stmt = conexion.createStatement();
				rs = stmt.executeQuery(sqlInformeVentas);
				
				while (rs.next()) {
					Venta venta = new Venta();
					venta.setIdVenta( rs.getInt(1) );
					venta.setFecha_venta( rs.getString(2) );
					venta.setHora_venta( rs.getString(3));
					venta.setTotal( rs.getDouble(4));
					
					ventas.add(venta);
				}
				
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
		
		return ventas;
	}
	
	public List<ProductoCompra> informeProductos( String marca, String proveedor, String tipo) {
		List<ProductoCompra> productos = new ArrayList<ProductoCompra>();
		
		if (conexion != null) {
			Statement stmt = null;
			ResultSet rs = null;
			
			String clausulaWhere = "";
			clausulaWhere += ( marca == "Seleccionar" ) ? "": "m.nombre_marca = '" + marca + "' AND ";
			clausulaWhere += ( proveedor == "Seleccionar" ) ? "": "prov.nombre_proveedor = '" + proveedor + "' AND ";
			clausulaWhere += ( tipo == "Seleccionar" ) ? "": "p.tipo_producto = '" + tipo + "' AND ";
			
			 String sqlInformeVentas = "SELECT p.imagen_producto, p.id_producto, p.modelo_producto, p.tipo_producto, m.nombre_marca, prov.nombre_proveedor, sum( rv.cantidad_producto ) 'Total' "
			 		+ "FROM registro_ventas AS rv "
			 		+ "INNER JOIN productos AS p ON rv.id_producto_venta = p.id_producto "
			 		+ "INNER JOIN marcas AS m ON p.id_marca_producto = m.id_marcas "
			 		+ "INNER JOIN proveedores AS prov ON m.proveedor_id = prov.id_proveedor "
			 		+ "WHERE " + clausulaWhere + " p.estado_producto = 1 "
			 		+ "GROUP BY p.id_producto "
			 		+ "ORDER BY Total DESC";
			 
			try {
				
				stmt = conexion.createStatement();
				rs = stmt.executeQuery(sqlInformeVentas);
				
				while (rs.next()) {
					ProductoCompra productoCompra = new ProductoCompra();
					productoCompra.setImagenProducto( rs.getBytes(1) );
					productoCompra.setId( rs.getInt(2));
					productoCompra.setModelo( rs.getString(3));
					productoCompra.setTipo( rs.getString(4));
					
					Marca marcaInfo = new Marca();
					marcaInfo.setNombre( rs.getString(5));
					marcaInfo.setNombreProveedor( rs.getString(6));
					
					productoCompra.setMarca(marcaInfo);
					productoCompra.setCantidad( rs.getInt(7));
					
					productos.add( productoCompra);
				}
				
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
		
		return productos;
	}
	
	public List<String> recuperarMarcas () {
		List<String> listaMarcas = new ArrayList<String>();
		
		if (conexion != null) {
			Statement stmt = null;
			ResultSet rs = null;
			
			String busqueda = "SELECT nombre_marca "
					+ "FROM marcas "
					+ "WHERE estado_marca = 1 "
					+ "GROUP BY nombre_marca ";
			
			try {
				
				stmt = conexion.createStatement();
				rs = stmt.executeQuery(busqueda);
				
				while (rs.next()) {
					listaMarcas.add( rs.getString(1) );
				}
				
			} catch (SQLException e) {
				
				System.err.println("No se pudo realizar la busqueda");
				e.printStackTrace();
				
			} finally {
				try {
					stmt.close();
					rs.close();
					conexion.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("Debe establecerse el objeto de conexion");
		}
		
		return listaMarcas;
	}
	
	public List<String> recuperarProveedores () {
		List<String> listaProveedores = new ArrayList<String>();
		
		if (conexion != null) {
			Statement stmt = null;
			ResultSet rs = null;
			
			String busqueda = "SELECT nombre_proveedor "
					+ "FROM proveedores "
					+ "WHERE estado_proveedor = 1 "
					+ "GROUP BY nombre_proveedor";
			
			try {
				
				stmt = conexion.createStatement();
				rs = stmt.executeQuery(busqueda);
				
				while (rs.next()) {		
					listaProveedores.add( rs.getString(1));
				}
				
			} catch (SQLException e) {
				
				System.err.println("No se pudo realizar la busqueda");
				e.printStackTrace();
				
			} finally {
				try {
					
					stmt.close();
					rs.close();
					conexion.close();

				} catch (SQLException e ) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("Debe establecerse el objeto de conexion");
		}
		
		return listaProveedores;
	}
}
