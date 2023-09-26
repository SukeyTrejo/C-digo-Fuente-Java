package com.system.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.system.objetos.Marca;
import com.system.objetos.Producto;

public class ProductosBD {
	private Connection conexion;
	
	private String sqlAgregar = "INSERT INTO productos (modelo_producto, "
			+ "tipo_producto, precio_producto, id_marca_producto, stock_producto, imagen_producto, "
			+ "estado_producto) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private String sqlModificar= "UPDATE productos SET modelo_producto = ?,"
			+ "tipo_producto = ?, precio_producto = ?, id_marca_producto = ?, stock_producto = ?, imagen_producto = ? "
			+ "WHERE id_producto = ?";
	
	private String sqlEliminar= "UPDATE productos SET estado_producto = 0 WHERE id_producto = ?";
	
	public ProductosBD(Connection conexion) {
		this.conexion = conexion;
	}
	
	public void insertar( Producto producto ) {
		if (conexion != null) {
			PreparedStatement ps = null;
			
			try {
				ps = conexion.prepareStatement(sqlAgregar);
				ps.setString(1, producto.getModelo());
				ps.setString(2, producto.getTipo());
				ps.setDouble(3, producto.getPrecio());
				ps.setInt(4, producto.getMarca().getId());
				ps.setInt(5, producto.getStock());
				ps.setBytes(6, producto.getImagenProducto());
				ps.setInt(7, producto.getEstado());
				ps.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Se ha insertado el registro correctamente", "Inserción Correcta", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "No se inserto el registro", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} finally {
				try {
					conexion.close();
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
					
		} else {
			System.err.println("Debes inicializar la conexion");
		}
	}
	
	public void modificar( Producto producto ) {
		if (conexion != null) {
			PreparedStatement ps = null;
			
			try {
				ps = conexion.prepareStatement(sqlModificar);
				ps.setString(1, producto.getModelo());
				ps.setString(2, producto.getTipo());
				ps.setDouble(3, producto.getPrecio());
				ps.setInt(4, producto.getMarca().getId());
				ps.setInt(5, producto.getStock());
				ps.setBytes(6, producto.getImagenProducto());
				ps.setInt(7, producto.getId());
				ps.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Se ha modificado el registro correctamente", "Modificación Correcta", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "No se modifico el registro", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} finally {
				try {
					conexion.close();
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
					
		} else {
			System.err.println("Debes inicializar la conexion");
		}
	}
	
	public void eliminar( Producto producto ) {
		if (conexion != null) {
			PreparedStatement ps = null;
			
			try {
				ps = conexion.prepareStatement(sqlEliminar);
				ps.setInt(1, producto.getId());
				ps.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Se ha eliminado el registro correctamente", "Eliminación Correcta", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "No se elimino el registro", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} finally {
				try {
					conexion.close();
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
					
		} else {
			System.err.println("Debes inicializar la conexion");
		}
	}
	
	public List<Producto> buscar( String cadena ) {
		List<Producto> listaProducto = new ArrayList<Producto>();
		
		if (conexion != null) {
			Statement stmt = null;
			ResultSet rs = null;
			String sqlBuscar = "SELECT p.id_producto, p.modelo_producto, p.tipo_producto, p.precio_producto, "
					+ "p.id_marca_producto, m.nombre_marca, m.proveedor_id, prov.nombre_proveedor, p.stock_producto, p.imagen_producto, p.estado_producto "
					+ "FROM productos AS p "
					+ "INNER JOIN marcas AS m ON p.id_marca_producto = m.id_marcas "
					+ "INNER JOIN proveedores AS prov ON m.proveedor_id = prov.id_proveedor "
					+ "WHERE ((p.id_producto LIKE '%" + cadena +"%' OR p.modelo_producto LIKE '%" + cadena +"%' "
					+ "OR p.tipo_producto LIKE '%" + cadena + "%' OR m.nombre_marca LIKE '%" + cadena + "%' OR prov.nombre_proveedor LIKE '%" + cadena + "%') AND p.estado_producto = 1) ";
			
			try {
				stmt = conexion.createStatement();
				rs = stmt.executeQuery( sqlBuscar );
				
				while (rs.next()) {
		
					//Crear producto
					Producto producto = new Producto();
					producto.setId( rs.getInt(1) );
					producto.setModelo( rs.getString(2) );
					producto.setTipo( rs.getString(3) );
					producto.setPrecio( rs.getDouble(4) );
					
					Marca marca = new Marca();
					marca.setId( rs.getInt(5) );
					marca.setNombre( rs.getString(6) );
					marca.setIdProveedor( rs.getInt(7) );
					marca.setNombreProveedor( rs.getString(8));
					
					
					producto.setMarca( marca );
					producto.setStock( rs.getInt(9) );
					producto.setImagenProducto( rs.getBytes(10) );
					producto.setEstado( rs.getInt(11) );
					
					listaProducto.add(producto);
				}
				
			} catch (SQLException e) {
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
			System.err.println("Debes inicializar la conexion");
		}
		
		return listaProducto;
	}
}
