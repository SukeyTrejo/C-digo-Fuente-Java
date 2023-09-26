package com.system.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.system.bd.Conexion;
import com.system.bd.MarcasBD;
import com.system.bd.ProveedoresBD;
import com.system.hilos.MarcasHilo;
import com.system.hilos.MarcasHiloCB;
import com.system.objetos.Marca;
import com.system.objetos.Proveedor;

public class MarcasPanel extends JPanel implements ActionListener {
	
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
    private JPanel bgPanel ;
    private JLabel marcasLabel;
    private JLabel nomMarcaLabel;
	private JTextField nomMarcaTxt;
	private JLabel provMarcaLabel;
	private JComboBox<Object> provMarcaCb;
	private JLabel descMarcaLabel;
	private JTextField descMarcaTxt;
	private DefaultTableModel defaultTableModel;
	private JScrollPane marcaScroll;
	private JTable marcaTable;

	private JPanel encabezadoTabla;
	private JLabel marcaslbl;
	private JButton recargarBtn;
	private JPanel panelDeBusqueda;
	private JButton buscarBtn;
	private JTextField buscarTxt;

	private JButton agregarBtn;
	private JButton modificarBtn;
	private JButton eliminarBtn;
	
	public MarcasPanel() {
		setBackground(new Color(255, 255, 255));
		setLayout(new BorderLayout());
		
		initComponents();
		
		recuperarDatos(""); 
		
		MarcasHiloCB marcasHiloCB = new MarcasHiloCB(this);
		Thread hilo = new Thread( marcasHiloCB );
		hilo.start();
		
	}

	private void initComponents() {
		bgPanel = new JPanel();
		bgPanel.setBackground(new Color(255, 255, 255));
		add(bgPanel, BorderLayout.CENTER);
		
		//Formulario
		marcasLabel = new JLabel("INFORMACIÓN DE LAS MARCAS");
		marcasLabel.setHorizontalAlignment(SwingConstants.CENTER);
		marcasLabel.setFont(new Font("Century Gothic", Font.BOLD, 17));
		
		nomMarcaLabel = new JLabel("Nombre:");
		nomMarcaLabel.setFont(estiloCampos);
		
		nomMarcaTxt = new JTextField();
		nomMarcaTxt.setFont(estiloCampos);
		nomMarcaTxt.setColumns(10);
		nomMarcaTxt.setBorder(new LineBorder(new Color(192, 192, 192)));
		
		provMarcaLabel = new JLabel("Proveedor:");
		provMarcaLabel.setFont(estiloCampos);
		
		provMarcaCb = new JComboBox<>();
		provMarcaCb.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		provMarcaCb.setBorder(new LineBorder(new Color(192, 192, 192)));
		provMarcaCb.setBackground(bgColor);
		provMarcaCb.setFocusable(false);
		
		Proveedor defaultItem = new Proveedor();
		defaultItem.setId(-1);
		defaultItem.setNombre("Seleccionar");
		provMarcaCb.addItem( defaultItem );
		
		descMarcaLabel = new JLabel("Descripción:");
		descMarcaLabel.setFont(estiloCampos);
		
		descMarcaTxt = new JTextField();
		descMarcaTxt.setFont(estiloCampos);
		descMarcaTxt.setColumns(10);
		descMarcaTxt.setBorder(new LineBorder(new Color(192, 192, 192)));
		
		marcaScroll = new JScrollPane();
		
		//Botones
		agregarBtn = new JButton("Agregar");
		agregarBtn.setFont(estiloBoton);
		agregarBtn.setContentAreaFilled(false);
		agregarBtn.setOpaque(true);
		agregarBtn.setForeground(bgColor);
		agregarBtn.setBackground(agregarColorBtn);
		agregarBtn.addActionListener(this);
		
		modificarBtn = new JButton("Modificar");
		modificarBtn.setFont(estiloBoton);
		modificarBtn.setContentAreaFilled(false);
		modificarBtn.setOpaque(true);
		modificarBtn.setForeground(bgColor);
		modificarBtn.setBackground(modificarColorBtn);
		modificarBtn.addActionListener(this);
		
		eliminarBtn = new JButton("Eliminar");
		eliminarBtn.setFont(estiloBoton);
		eliminarBtn.setContentAreaFilled(false);
		eliminarBtn.setOpaque(true);
		eliminarBtn.setForeground(bgColor);
		eliminarBtn.setBackground(eliminarColorBtn);
		eliminarBtn.addActionListener(this);
		
		marcaTable = new JTable();
		marcaTable.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		marcaTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//Recuperar datos
				nomMarcaTxt.setText( (String) marcaTable.getValueAt(marcaTable.getSelectedRow() , 1) );
				
				Proveedor proveedor = new Proveedor();
				proveedor.setId( (int) marcaTable.getValueAt(marcaTable.getSelectedRow() , 2) );
				proveedor.setNombre( (String) marcaTable.getValueAt(marcaTable.getSelectedRow() , 3) );
	
				provMarcaCb.getModel().setSelectedItem(  proveedor );
				descMarcaTxt.setText( (String) marcaTable.getValueAt(marcaTable.getSelectedRow() , 4) );		
				
			}
		});
		marcaScroll.setViewportView(marcaTable);
		
		encabezadoTabla = new JPanel();
		encabezadoTabla.setBackground(Color.DARK_GRAY);
		
		marcaslbl = new JLabel("Marcas");
		marcaslbl.setForeground(Color.WHITE);
		marcaslbl.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		
		ImageIcon recargarIcon = new ImageIcon("images/refresh_img.png");
		recargarBtn = new JButton("");
		recargarBtn.setFocusable(false);
		recargarBtn.setContentAreaFilled(false);
		recargarBtn.setBorder(null);
		recargarBtn.setIcon(recargarIcon);
		recargarBtn.addActionListener( this );
		
		GroupLayout gl_encabezadoTabla = new GroupLayout(encabezadoTabla);
		gl_encabezadoTabla.setHorizontalGroup(
			gl_encabezadoTabla.createParallelGroup(Alignment.LEADING)
				.addGap(0, 579, Short.MAX_VALUE)
				.addGroup(gl_encabezadoTabla.createSequentialGroup()
					.addGap(10)
					.addComponent(marcaslbl, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
					.addGap(350)
					.addComponent(recargarBtn, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(9))
		);
		gl_encabezadoTabla.setVerticalGroup(
			gl_encabezadoTabla.createParallelGroup(Alignment.LEADING)
				.addGap(0, 35, Short.MAX_VALUE)
				.addGroup(gl_encabezadoTabla.createSequentialGroup()
					.addGap(3)
					.addGroup(gl_encabezadoTabla.createParallelGroup(Alignment.LEADING)
						.addComponent(marcaslbl, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(recargarBtn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
		);
		encabezadoTabla.setLayout(gl_encabezadoTabla);
		
		panelDeBusqueda = new JPanel();
		panelDeBusqueda.setBorder(new LineBorder(new Color(192, 192, 192)));
		panelDeBusqueda.setBackground(Color.WHITE);
		
		ImageIcon buscarIcon = new ImageIcon("images/search_img.png");
		buscarBtn = new JButton();
		buscarBtn.setFocusable(false);
		buscarBtn.setContentAreaFilled(false);
		buscarBtn.setBorder(null);
		buscarBtn.setIcon(buscarIcon);
		buscarBtn.addActionListener(this);
		
		buscarTxt = new JTextField();
		buscarTxt.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		buscarTxt.setColumns(10);
		buscarTxt.setBorder(null);
		GroupLayout gl_panelDeBusqueda = new GroupLayout(panelDeBusqueda);
		gl_panelDeBusqueda.setHorizontalGroup(
			gl_panelDeBusqueda.createParallelGroup(Alignment.LEADING)
				.addGap(0, 580, Short.MAX_VALUE)
				.addGroup(gl_panelDeBusqueda.createSequentialGroup()
					.addGap(9)
					.addComponent(buscarTxt, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
					.addComponent(buscarBtn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
		);
		gl_panelDeBusqueda.setVerticalGroup(
			gl_panelDeBusqueda.createParallelGroup(Alignment.LEADING)
				.addGap(0, 35, Short.MAX_VALUE)
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
								.addComponent(marcaScroll, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
								.addComponent(encabezadoTabla, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
							.addGap(10)
							.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_bgPanel.createSequentialGroup()
									.addGap(40)
									.addComponent(marcasLabel, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE))
								.addComponent(nomMarcaLabel, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(nomMarcaTxt, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(provMarcaLabel, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(provMarcaCb, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(descMarcaLabel, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(descMarcaTxt, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
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
						.addGroup(gl_bgPanel.createSequentialGroup()
							.addGap(33)
							.addComponent(marcaScroll, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
							.addGap(2))
						.addComponent(encabezadoTabla, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_bgPanel.createSequentialGroup()
							.addGap(46)
							.addComponent(marcasLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addGap(28)
							.addComponent(nomMarcaLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(nomMarcaTxt, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(provMarcaLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(provMarcaCb, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addGap(8)
							.addComponent(descMarcaLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
							.addGap(3)
							.addComponent(descMarcaTxt, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
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
		defaultTableModel = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		defaultTableModel.addColumn("ID");
 		defaultTableModel.addColumn("Nombre");
 		defaultTableModel.addColumn("ID Proveedor");
 		defaultTableModel.addColumn("Nombre Proveedor");
 		defaultTableModel.addColumn("Descripcion");
 		defaultTableModel.addColumn("Estado");
 		
 		cargarDatos( cadena );
	}
	
	public void cargarDatos( String cadena ) {
		
		Object[] datos = new Object[6];
		
		Conexion conexion = new Conexion();
		MarcasBD marcasBD = new MarcasBD( conexion.conectar() );
		
		List<Marca> listaMarcas = marcasBD.buscar( cadena );
		for (Marca marca : listaMarcas) {
			
			datos[0] = marca.getId();
			datos[1] = marca.getNombre();
			datos[2] = marca.getIdProveedor();
			datos[3] = marca.getNombreProveedor();
			datos[4] = marca.getDescripcion();
			datos[5] = marca.getEstado();
			
			defaultTableModel.addRow( datos);
		}
		
		marcaTable.setModel( defaultTableModel );
		
		//Ocultar columna
		marcaTable.getColumnModel().getColumn(2).setMaxWidth(0);
		marcaTable.getColumnModel().getColumn(2).setMinWidth(0);
		marcaTable.getColumnModel().getColumn(2).setPreferredWidth(0);
		
		//Ocultar columna
		marcaTable.getColumnModel().getColumn(5).setMaxWidth( 0 );
		marcaTable.getColumnModel().getColumn(5).setMinWidth( 0 );
		marcaTable.getColumnModel().getColumn(5).setPreferredWidth( 0 );
		
		marcaTable.setRowHeight( 30 );
	}
	
	public void limpiarDatos() {
		
		//Limpiar campos
		nomMarcaTxt.setText("");
		provMarcaCb.setSelectedIndex(0);
		descMarcaTxt.setText("");
		
		//Limpiar seleccion
		marcaTable.clearSelection();

	}
	
	public void recuperarDatos(String cadena) {
		
		MarcasHilo marcasHilo = new MarcasHilo( this, cadena);
		Thread hilo = new Thread( marcasHilo );
		hilo.start();
		
	}

	public void llenarComboProveedores() {
		//Llenar datos de proveedor
		Conexion conexion = new Conexion();
		ProveedoresBD proveedoresBD = new ProveedoresBD( conexion.conectar() );
		for (Proveedor proveedor : proveedoresBD.buscar("")) {
			provMarcaCb.addItem(proveedor);
		}
	}
	
	public boolean verificarCampos() {
		
		if (nomMarcaTxt.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "Nombre de marca vacío", "Nombre vacío", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		if (provMarcaCb.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "Selecciona un proveedor", "Proveedor vacío", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		if (descMarcaTxt.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "Descripción de marca vacía", "Descripción vacía", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == agregarBtn) {
			
			if (verificarCampos()) {
				return;	
			}
			
			Proveedor proveedor = (Proveedor) provMarcaCb.getSelectedItem();
			
			Marca marca = new Marca();
			marca.setNombre( nomMarcaTxt.getText());
			marca.setIdProveedor( proveedor.getId() );
			marca.setDescripcion( descMarcaTxt.getText() );
			marca.setEstado(1);
			
			Conexion conexion = new Conexion();
			MarcasBD marcasBD = new MarcasBD( conexion.conectar() );
			marcasBD.insertar( marca );
			
			limpiarDatos();

		} else if( e.getSource() == modificarBtn ) {
			
			if (verificarCampos()) {
				return;	
			}
			
			try {
				//Objeto con información a seleccionar
				Marca marca = new Marca();
				
				marca.setId( (int) marcaTable.getValueAt(marcaTable.getSelectedRow() , 0)); 
				marca.setNombre( nomMarcaTxt.getText() ); 
				
				Proveedor proveedor = (Proveedor) provMarcaCb.getSelectedItem();
				marca.setIdProveedor ( proveedor.getId() ); 
				marca.setDescripcion( descMarcaTxt.getText() ); 
				
				Conexion conexion = new Conexion();
				MarcasBD marcasBD = new MarcasBD( conexion.conectar() );
				marcasBD.modificar( marca );
				
				limpiarDatos();
				
			} catch (ArrayIndexOutOfBoundsException ex) {
				JOptionPane.showMessageDialog( null, "Selecciona una marca para modificarla", "Advertencia", JOptionPane.WARNING_MESSAGE);
				
			} catch (Exception ex) {
				ex.printStackTrace();
				
			}
			
		} else if (e.getSource() == eliminarBtn) {
			
			try {	
				//Objeto con información a seleccionar
				Marca marca = new Marca();
				marca.setId( (int) marcaTable.getValueAt(marcaTable.getSelectedRow() , 0)); 
				marca.setEstado(0);
			
				Conexion conexion = new Conexion();
				MarcasBD marcasBD = new MarcasBD( conexion.conectar() );
				marcasBD.eliminar( marca);
			
				
				limpiarDatos();		
			} catch (ArrayIndexOutOfBoundsException ex) {
				JOptionPane.showMessageDialog( null, "Selecciona una marca para eliminarla", "Advertencia", JOptionPane.WARNING_MESSAGE);
				
			}
		} else if (e.getSource() == buscarBtn) {
			
			recuperarDatos( buscarTxt.getText() ); 
			
		} else if (e.getSource() == recargarBtn) {
			
			recuperarDatos( "" ); 
			
		}
	}
}
