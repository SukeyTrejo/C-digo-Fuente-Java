package com.system.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

import com.system.objetos.ProductoCompra;
import com.system.objetos.Venta;

public class VentasBD {
	private Connection conexion;
	
	private String sqlAgregarVenta = "INSERT INTO ventas (fecha_venta, hora_venta) VALUES ( NOW(), NOW())";
	
	private String sqlAgregarProductosVenta = "INSERT INTO registro_ventas (id_registro_venta, id_producto_venta, cantidad_producto, precio_unitario_producto)"
			+ " VALUES (?, ?, ?, ?)";
	
	private String sqlBuscarUltimoRegistro = "SELECT * FROM ventas ORDER BY id_venta DESC Limit 1";
	
	private String sqlModificarProductos = "UPDATE productos SET stock_producto = (stock_producto - ?) "
			+ "WHERE id_producto = ? ";
	
	public VentasBD(Connection conexion) {
		this.conexion = conexion;
	}
	
	public void insertar( Venta venta ) {
		if (conexion != null) {
			PreparedStatement ps = null;
			
			try {
				ps = conexion.prepareStatement(sqlAgregarVenta);
				ps.executeUpdate();
				
				insertarProductosComprados( venta );
				
				JOptionPane.showMessageDialog(null, "Se ha realizado la venta correctamente", "Venta realizada", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "No se inserto el registro", "Error", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			} finally {
				try {
					ps.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
					
		} else {
			System.err.println("Debes inicializar la conexion");
		}
	}
	
	private void insertarProductosComprados( Venta venta ) {
		if (conexion != null) {
			PreparedStatement psInsertar = null;
			PreparedStatement psModificar = null;
			
			try {
				//Colocar id de venta generado al insertar la venta
				venta = traerUltimoRegistroVenta(venta);
				
				psInsertar = conexion.prepareStatement(sqlAgregarProductosVenta);
				psModificar = conexion.prepareStatement(sqlModificarProductos);
				
				//Registrar los productos comprados
				for (ProductoCompra productoCompra : venta.getListaProductos()) {
					psInsertar.setInt(1, venta.getIdVenta());
					psInsertar.setInt(2,  productoCompra.getId());
					psInsertar.setInt(3,  productoCompra.getCantidad());
					psInsertar.setDouble(4,  productoCompra.getPrecio());
					psInsertar.executeUpdate();
					
					psModificar.setInt(1, productoCompra.getCantidad());
					psModificar.setInt(2, productoCompra.getId());
					psModificar.executeUpdate();
				
				}
					
			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				try {
					psInsertar.close();
					psModificar.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		} else {
			System.err.println("Debes inicializar la conexion");
		}
	}
	
	private Venta traerUltimoRegistroVenta( Venta venta) {
		if (conexion != null) {
			Statement stmt = null;
			ResultSet rs = null;
			
			try {
				stmt = conexion.createStatement();
				rs = stmt.executeQuery( sqlBuscarUltimoRegistro );
				
				if (rs.next()) {
					venta.setIdVenta( rs.getInt(1) );
				}
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
		
		return venta;
	}
}
