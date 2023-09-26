package com.system.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import com.system.objetos.Proveedor;

public class ProveedoresBD {
	private Connection conexion;
	
	private String sqlAgregar = "INSERT INTO proveedores (nombre_proveedor, telefono_proveedor, correo_proveedor, estado_proveedor)"
			+ "VALUES (?, ?, ?, ?)";
	
	private String sqlModificar = "UPDATE proveedores SET nombre_proveedor = ?, telefono_proveedor = ?, correo_proveedor = ? WHERE id_proveedor = ? ";
	
	private String sqlEliminar = "UPDATE proveedores SET estado_proveedor = ? WHERE id_proveedor = ? ";
	
	public ProveedoresBD( Connection conexion ) {
		this.conexion = conexion;
	}
	
	public void insertar( Proveedor proveedor ) {
		if (conexion != null) {
			PreparedStatement ps = null;
	
			try {
				ps = conexion.prepareStatement(sqlAgregar);
				ps.setString(1 , proveedor.getNombre());
				ps.setString(2 , proveedor.getTelefono());
				ps.setString(3 , proveedor.getCorreo());
				ps.setInt(4 , proveedor.getEstado());
				ps.executeUpdate();
				
				JOptionPane.showMessageDialog(null, "Se ha insertado el registro correctamente", "Inserción Correcta", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Error al insertar el registro", "Error", JOptionPane.ERROR_MESSAGE);
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
	
	public void modificar( Proveedor proveedor ) {
		if (conexion != null) {
			PreparedStatement ps = null;
			
			try {
				ps = conexion.prepareStatement( sqlModificar );
				ps.setString(1 , proveedor.getNombre());
				ps.setString(2 , proveedor.getTelefono());
				ps.setString(3 , proveedor.getCorreo());
				ps.setInt(4, proveedor.getId());
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
	
	public void eliminar( Proveedor proveedor ) {
		if (conexion != null) {
			PreparedStatement ps = null;
			
			try {
				ps = conexion.prepareStatement( sqlEliminar );
				ps.setInt(1, proveedor.getEstado());
				ps.setInt(2, proveedor.getId());
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
	
	public List<Proveedor> buscar( String cadena ) {
		List<Proveedor> listaProveedores = new ArrayList<Proveedor>();
		
		if (conexion != null) {
			Statement stmt = null;
			ResultSet rs = null;
			
			String busqueda = "SELECT * FROM proveedores "
					+ "WHERE ((id_proveedor LIKE '%" + cadena + "%' OR "
						+ "nombre_proveedor LIKE '%" + cadena + "%' OR "
								+ "correo_proveedor LIKE '%" + cadena +"%' OR "
										+ "telefono_proveedor LIKE '%" + cadena + "%')"
												+ "and estado_proveedor = 1)";
			try {
				
				stmt = conexion.createStatement();
				rs = stmt.executeQuery(busqueda);
				
				while (rs.next()) {
					
					Proveedor proveedor = new Proveedor();
					proveedor.setId( rs.getInt(1) );
					proveedor.setNombre( rs.getString(2) );
					proveedor.setTelefono( rs.getString(3) );
					proveedor.setCorreo( rs.getString(4) );
					proveedor.setEstado( rs.getInt(5) );
					
					listaProveedores.add(proveedor);
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
