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

public class MarcasBD {
	private Connection conexion;
	
	private String sqlAgregar = "INSERT INTO marcas (nombre_marca, proveedor_id, descripcion_marca, estado_marca)"
			+ "VALUES (?, ?, ?, ? )";
	
	private String sqlModificar = "UPDATE marcas SET nombre_marca = ?, proveedor_id = ?, descripcion_marca = ? "
			+ "WHERE id_marcas = ? ";
	
	private String sqlEliminar = "UPDATE marcas SET estado_marca = ? WHERE id_marcas = ? ";
	
	public MarcasBD( Connection conexion ) {
		this.conexion = conexion;
	}
	
	public void insertar( Marca marca ) {
		if ( this.conexion != null ) {
			PreparedStatement ps = null;
			
			try {
				ps = conexion.prepareStatement( sqlAgregar );
				ps.setString(1, marca.getNombre());
				ps.setInt(2, marca.getIdProveedor());
				ps.setString(3, marca.getDescripcion());
				ps.setInt(4, marca.getEstado());
				ps.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Se ha insertado el registro correctamente", "Inserción Correcta", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "No se insertó el registro", "Error", JOptionPane.ERROR_MESSAGE);
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
			System.err.println("Debe establecerse el objeto de conexion");
		}
	}
	
	public void modificar( Marca marca ) {
		if (conexion != null) {
			PreparedStatement ps = null;
			
			try {
				ps = conexion.prepareStatement( sqlModificar );
				ps.setString(1, marca.getNombre());
				ps.setInt(2, marca.getIdProveedor());
				ps.setString(3, marca.getDescripcion());
				ps.setInt(4, marca.getId());
				ps.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Se ha modificado correctamente el registro", "Modifiación Correcta", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Error al modfiicar el registro", "Error", JOptionPane.ERROR_MESSAGE);
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
			System.err.println("Debe establecerse el objeto de conexion");
		}
	}
	
	public void eliminar( Marca marca ) {
		if (conexion != null) {
			PreparedStatement ps = null;
			
			try {
	
				ps = conexion.prepareStatement( sqlEliminar );
				ps.setInt(1, marca.getEstado() );
				ps.setInt(2, marca.getId());
				ps.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Se ha eliminado correctamente el registro", "Eliminación Correcta", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Error al elimiar el registro", "Error", JOptionPane.ERROR_MESSAGE);
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
			System.err.println("Debe establecerse el objeto de conexion");
		}
	}
	
	

	public List<Marca> buscar( String cadena ) {
		List<Marca> listaMarcas = new ArrayList<Marca>();
		
		if (conexion != null) {
			Statement stmt = null;
			ResultSet rs = null;
			
			String busqueda = "SELECT m.id_marcas, m.nombre_marca, m.proveedor_id, p.nombre_proveedor, m.descripcion_marca, m.estado_marca "
					+ "FROM marcas AS m "
					+ "INNER JOIN proveedores AS p ON  m.proveedor_id = p.id_proveedor "
					+ "WHERE ((id_marcas LIKE '%" + cadena + "%' OR "
						+ "m.nombre_marca LIKE '%" + cadena + "%' OR "
								+ "p.id_proveedor LIKE '%" + cadena +"%' OR "
										+ "m.descripcion_marca LIKE '%" + cadena + "%')"
												+ "and estado_marca = 1)";
			try {
				
				stmt = conexion.createStatement();
				rs = stmt.executeQuery(busqueda);
				
				while (rs.next()) {
					Marca marca = new Marca();
					marca.setId(rs.getInt(1));
					marca.setNombre(rs.getString(2));
					marca.setIdProveedor(rs.getInt(3));
					marca.setNombreProveedor(rs.getString(4));
					marca.setDescripcion(rs.getString(5));
					marca.setEstado(rs.getInt(6));
					
					listaMarcas.add( marca );
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
}
