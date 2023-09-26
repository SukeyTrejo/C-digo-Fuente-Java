package com.system.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.util.StringUtils;
import com.system.bd.Conexion;
import com.system.bd.ProveedoresBD;
import com.system.hilos.ProveedoresHilo;
import com.system.objetos.Proveedor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;

public class ProveedoresPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	//Colores de interfaz
    private final Color bgColor = new Color(0xFFFFFF);
    
    //Colores botones
    private final Color agregarColorBtn = new Color(0x0E7221);
    private final Color modificarColorBtn = new Color(0xFF880C);
    private final Color eliminarColorBtn = new Color(0xCF3401);
    
    //Estilos de letra
    private final Font estiloCampos = new Font("Century Gothic", Font.PLAIN, 15);
    private final Font estiloBoton = new Font("Century Gothic", Font.BOLD, 20);
    
    //Elementos
	private JPanel bgPanel;	
	private JLabel proveedorLabel;
	private JLabel nomProvLabel;
	private JTextField nombreProvTxt;
	private JLabel telProvLabel;
	private JTextField telProvTxt;
	private JTextField correoProvTxt;
	private JLabel correoProvLabel;
	private JScrollPane provScroll;
	private DefaultTableModel defaultTableModel;
	private JTable provTable;
	private JButton agregarBtn;
	private JButton modificarBtn;
	private JButton eliminarBtn;
	private JButton recargarBtn;
	
	private JPanel panelDeBusqueda;
	private JTextField buscarTxt;
	private JButton buscarBtn;
	
	public ProveedoresPanel() {
		setBackground(new Color(255, 255, 255));	
		setLayout(new BorderLayout());
	
		initComponents();
		
		//Traer todos los datos de la base de datos
		recuperarDatos("");
	}
	
	private void initComponents() {
		bgPanel = new JPanel();
		bgPanel.setBackground(new Color(255, 255, 255));
		add(bgPanel, BorderLayout.CENTER);
		
		proveedorLabel = new JLabel("INFORMACIÓN DEL PROVEEDOR");
		proveedorLabel.setFont(new Font("Century Gothic", Font.BOLD, 17));
		proveedorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		nomProvLabel = new JLabel("Nombre:");
		nomProvLabel.setFont(estiloCampos);
		
		nombreProvTxt = new JTextField();
		nombreProvTxt.setFont(estiloCampos);
		nombreProvTxt.setColumns(10);
		
		telProvLabel = new JLabel("Teléfono:");
		telProvLabel.setFont(estiloCampos);
		
		telProvTxt = new JTextField();
		telProvTxt.setFont(estiloCampos);
		telProvTxt.setColumns(10);
		
		correoProvLabel = new JLabel("Correo:");
		correoProvLabel.setFont(estiloCampos);
		
		correoProvTxt = new JTextField();
		correoProvTxt.setFont(estiloCampos);
		correoProvTxt.setColumns(10);
		
		provScroll = new JScrollPane();
		
		//Botones
		agregarBtn = new JButton("Agregar");
		agregarBtn.setFont(estiloBoton);
		agregarBtn.setFocusable(false);
		agregarBtn.setContentAreaFilled(false);
		agregarBtn.setOpaque(true);
		agregarBtn.setForeground( bgColor );
		agregarBtn.setBackground(agregarColorBtn);
		agregarBtn.addActionListener(this);
		
		modificarBtn = new JButton("Modificar");
		modificarBtn.setFont(estiloBoton);
		modificarBtn.setFocusable(false);
		modificarBtn.setContentAreaFilled(false);
		modificarBtn.setOpaque(true);
		modificarBtn.setForeground( bgColor );
		modificarBtn.setBackground(modificarColorBtn);
		modificarBtn.addActionListener(this);
		
		eliminarBtn = new JButton("Eliminar");
		eliminarBtn.setFont(estiloBoton);
		eliminarBtn.setFocusable(false);
		eliminarBtn.setContentAreaFilled(false);
		eliminarBtn.setOpaque(true);
		eliminarBtn.setForeground( bgColor );
		eliminarBtn.setBackground(eliminarColorBtn);
		eliminarBtn.addActionListener(this);
		
		
		provTable = new JTable();	
		provTable.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		provTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				//Cargar información
				nombreProvTxt.setText( (String) provTable.getValueAt(provTable.getSelectedRow() , 1) );
				telProvTxt.setText( (String) provTable.getValueAt(provTable.getSelectedRow() , 2) );
				correoProvTxt.setText( (String) provTable.getValueAt(provTable.getSelectedRow() , 3) );
				
			}
		});
		provScroll.setViewportView(provTable);
		
		JPanel encabezadoTabla = new JPanel();
		encabezadoTabla.setBackground( Color.DARK_GRAY);
		
		ImageIcon recargarIcon = new ImageIcon("images/refresh_img.png");
		recargarBtn = new JButton("");
		recargarBtn.setContentAreaFilled(false);
		recargarBtn.setBorder(null);
		recargarBtn.setFocusable(false);
		recargarBtn.setIcon(recargarIcon);
		recargarBtn.addActionListener(this);
		
		JLabel lblProveedores = new JLabel(" Proveedores");
		lblProveedores.setForeground(Color.WHITE);
		lblProveedores.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		GroupLayout gl_encabezadoTabla = new GroupLayout(encabezadoTabla);
		gl_encabezadoTabla.setHorizontalGroup(
			gl_encabezadoTabla.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_encabezadoTabla.createSequentialGroup()
					.addGap(10)
					.addComponent(lblProveedores, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
					.addGap(350)
					.addComponent(recargarBtn, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(9))
		);
		gl_encabezadoTabla.setVerticalGroup(
			gl_encabezadoTabla.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_encabezadoTabla.createSequentialGroup()
					.addGap(3)
					.addGroup(gl_encabezadoTabla.createParallelGroup(Alignment.LEADING)
						.addComponent(lblProveedores, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(recargarBtn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
		);
		encabezadoTabla.setLayout(gl_encabezadoTabla);
		
		panelDeBusqueda = new JPanel();
		panelDeBusqueda.setBorder(new LineBorder(new Color(192, 192, 192)));
		panelDeBusqueda.setBackground(new Color(255, 255, 255));
		
		ImageIcon buscarIcon = new ImageIcon("images/search_img.png");
		buscarBtn = new JButton();
		buscarBtn.setIcon(buscarIcon);
		buscarBtn.setFocusable(false);
		buscarBtn.setContentAreaFilled(false);
		buscarBtn.setBorder(null);
		buscarBtn.addActionListener(this);
		
		buscarTxt = new JTextField();
		buscarTxt.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		buscarTxt.setColumns(10);
		buscarTxt.setBorder(null);
		GroupLayout gl_panelDeBusqueda = new GroupLayout(panelDeBusqueda);
		gl_panelDeBusqueda.setHorizontalGroup(
			gl_panelDeBusqueda.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelDeBusqueda.createSequentialGroup()
					.addGap(9)
					.addComponent(buscarTxt, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
					.addComponent(buscarBtn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
		);
		gl_panelDeBusqueda.setVerticalGroup(
			gl_panelDeBusqueda.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelDeBusqueda.createSequentialGroup()
					.addGap(1)
					.addGroup(gl_panelDeBusqueda.createParallelGroup(Alignment.LEADING)
						.addComponent(buscarTxt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(buscarBtn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
		);
		panelDeBusqueda.setLayout(gl_panelDeBusqueda);
		GroupLayout gl_bgPanel = new GroupLayout(bgPanel);
		gl_bgPanel.setHorizontalGroup(
			gl_bgPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bgPanel.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_bgPanel.createSequentialGroup()
							.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(encabezadoTabla, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
								.addComponent(provScroll, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
							.addGap(10)
							.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_bgPanel.createSequentialGroup()
									.addGap(40)
									.addComponent(proveedorLabel, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE))
								.addComponent(nomProvLabel, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(nombreProvTxt, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(telProvLabel, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(telProvTxt, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(correoProvLabel, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(correoProvTxt, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(agregarBtn, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(modificarBtn, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(eliminarBtn, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_bgPanel.createSequentialGroup()
							.addComponent(panelDeBusqueda, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
							.addGap(350)))
					.addGap(10))
		);
		gl_bgPanel.setVerticalGroup(
			gl_bgPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bgPanel.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(encabezadoTabla, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_bgPanel.createSequentialGroup()
							.addGap(33)
							.addComponent(provScroll, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE))
						.addGroup(gl_bgPanel.createSequentialGroup()
							.addGap(46)
							.addComponent(proveedorLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addGap(28)
							.addComponent(nomProvLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(nombreProvTxt, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(telProvLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(telProvTxt, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(correoProvLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(correoProvTxt, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addGap(24)
							.addComponent(agregarBtn, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(modificarBtn, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(11)
							.addComponent(eliminarBtn, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)))
					.addGap(9)
					.addComponent(panelDeBusqueda, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(25))
		);
		bgPanel.setLayout(gl_bgPanel);
	}
	
	public void crearModeloTabla( String cadena ) {
		defaultTableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		defaultTableModel.addColumn("ID");
 		defaultTableModel.addColumn("Nombre");
 		defaultTableModel.addColumn("Teléfono");
 		defaultTableModel.addColumn("Correo");
 		defaultTableModel.addColumn("Estado");
 		
 		cargarDatos( cadena );
	}
	
	public void cargarDatos( String cadena ) {
		
		Object datos[] = new Object[5];
		
		Conexion conexion = new Conexion();
		ProveedoresBD proveedoresBD = new ProveedoresBD( conexion.conectar() );
		
		List<Proveedor> listaProveedores = proveedoresBD.buscar( cadena );
		for (Proveedor proveedor : listaProveedores) {
			
			datos[0] = proveedor.getId();
			datos[1] = proveedor.getNombre();
			datos[2] = proveedor.getTelefono();
			datos[3] = proveedor.getCorreo();
			datos[4] = proveedor.getEstado();
			
			defaultTableModel.addRow(datos);
		}
		
		provTable.setModel( defaultTableModel );
		
		//Ocultar columna
		provTable.getColumnModel().getColumn(4).setMaxWidth( 0 );
		provTable.getColumnModel().getColumn(4).setMinWidth( 0 );
		provTable.getColumnModel().getColumn(4).setPreferredWidth( 0 );
		
		provTable.setRowHeight( 30 );
				
	}
	
	public void recuperarDatos( String cadena ) {
		
		ProveedoresHilo proveedoresHilo = new ProveedoresHilo( this, cadena);
		Thread hilo = new Thread( proveedoresHilo );
		hilo.start();
		
	}

	public void limpiarDatos() {
		nombreProvTxt.setText("");
		telProvTxt.setText("");
		correoProvTxt.setText("");
		
		provTable.clearSelection();
	}
	
	private boolean verificarCampos() {
		
		if ( nombreProvTxt.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "Nombre de proveedor vacío", "Nombre vacío", JOptionPane.WARNING_MESSAGE);
			return true;
		}
	
		if ( telProvTxt.getText().length() < 10 || telProvTxt.getText().length() > 10) {
			JOptionPane.showMessageDialog(this, "La longitud del teléfono es incorrecta", "Teléfono", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		if (!StringUtils.isStrictlyNumeric( telProvTxt.getText() )) {
			JOptionPane.showMessageDialog(this, "El teléfono no es un número", "Télefono", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		if ( correoProvTxt.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "Correo de proveedor vacío", "Correo vacío", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == agregarBtn) {
			
			if (verificarCampos()) { //Verificar formato de campos
				return;
			}
			
			//Nombre, telefono, correo, estado
			Proveedor proveedor = new Proveedor();
			proveedor.setNombre( nombreProvTxt.getText() );
			proveedor.setTelefono( telProvTxt.getText() );
			proveedor.setCorreo( correoProvTxt.getText() );
			proveedor.setEstado(1);
			
			Conexion conexion = new Conexion();
			ProveedoresBD proveedoresBD = new ProveedoresBD( conexion.conectar() );
			proveedoresBD.insertar(proveedor);
			
			limpiarDatos();

		} else if( e.getSource() == modificarBtn ) {
			
			if (verificarCampos()) {
				return;
			}
			
			try {
				
				Proveedor proveedor = new Proveedor();
				proveedor.setId( (int) provTable.getValueAt(provTable.getSelectedRow() , 0)); 
				proveedor.setNombre( nombreProvTxt.getText()); 
				proveedor.setTelefono( telProvTxt.getText() ); 
				proveedor.setCorreo( correoProvTxt.getText() ); 
				
				//Modificar registro
				Conexion conexion = new Conexion();
				ProveedoresBD proveedoresBD = new ProveedoresBD( conexion.conectar() );
				proveedoresBD.modificar( proveedor );
				
				limpiarDatos();
			
			} catch (ArrayIndexOutOfBoundsException ex) {
				JOptionPane.showMessageDialog( null, "Selecciona un proveedor para modificarlo", "Advertencia", JOptionPane.WARNING_MESSAGE);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			
		} else if (e.getSource() == eliminarBtn) {
			
			try {
				
				Proveedor proveedor = new Proveedor();
				proveedor.setId( (int) provTable.getValueAt(provTable.getSelectedRow() , 0)); 
				proveedor.setEstado( 0 ); 
			
				Conexion conexion = new Conexion();
				ProveedoresBD proveedoresBD = new ProveedoresBD( conexion.conectar() );
				proveedoresBD.eliminar( proveedor );
				
				limpiarDatos();
			} catch (ArrayIndexOutOfBoundsException ex) {
				JOptionPane.showMessageDialog( null, "Selecciona un proveedor para eliminarlo", "Advertencia", JOptionPane.WARNING_MESSAGE);
				
			} catch (Exception ex) {
				ex.printStackTrace();
				
			}
				
		} else if (e.getSource() == buscarBtn) {
			
			recuperarDatos( buscarTxt.getText() );
			
		} else if (e.getSource() == recargarBtn) {
			
			recuperarDatos( "" );
			
		}
	}
	
}
