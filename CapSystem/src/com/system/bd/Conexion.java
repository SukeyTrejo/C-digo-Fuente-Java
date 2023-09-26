package com.system.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Conexion {
	
	private static final String CONTROLADOR = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/bdproyecto";
	private static final String USUARIO = "capsystem";
	private static final String CLAVE = "capsystem";

	static {
		try {
			Class.forName(CONTROLADOR);
		} catch (ClassNotFoundException e) {
			
			System.err.println("Fallo en el controlador");
			e.printStackTrace();
			System.exit(1); //Salir
			
		}
	}
	
	public Connection conectar() {
		Connection conexion = null;
		try {
			
			conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);

		} catch (SQLException e) {
			
			JOptionPane.showMessageDialog(null, "Error en la conexión de la base de datos. Verifque el puerto 3306 esté habilitado y la base de datos exista, así como el usuario", "Error", JOptionPane.ERROR_MESSAGE);
			System.err.println( "Error en la conexión de la base de datos. Verifque el puerto 3306 esté habilitado y la base de datos exista");
			e.printStackTrace();
			System.exit(1);
			
		}
		
		return conexion;
	}
}
