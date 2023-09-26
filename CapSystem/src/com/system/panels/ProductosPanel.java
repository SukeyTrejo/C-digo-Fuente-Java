package com.system.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.util.StringUtils;
import com.system.bd.Conexion;
import com.system.bd.MarcasBD;
import com.system.bd.ProductosBD;
import com.system.hilos.ProductosHilo;
import com.system.hilos.ProductosHiloCB;
import com.system.objetos.Marca;
import com.system.objetos.Producto;
import com.system.objetos.Proveedor;
import com.system.objetos.RenderImagen;
import javax.swing.DefaultComboBoxModel;

public class ProductosPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	//Colores de interfaz
	private final Color primaryColor = new Color(0x404040);
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
    
    private JButton imagenBtn;
    private JLabel modeloLabel;
	private JTextField modeloTxt;
	private JLabel tipoLabel;
	private JLabel stockLabel;
	private JTextField stockTxt;
	private JLabel marcaLabel;
	private JComboBox<Marca> marcaCb;
	private JLabel precioLabel;
	private JTextField precioTxt;
	private JScrollPane productosScroll;
	private DefaultTableModel defaultTableModel;
	private JTable productosTable;

	private JButton agregarBtn;
	private JButton modificarBtn;
	private JButton eliminarBtn;
	private JPanel panelDeBusqueda;
	private JTextField buscarTxt;
	private JButton buscarBtn;
	private JButton recargarBtn;	
	private JComboBox<String> tipoCb;
	
	public ProductosPanel() {
		setPreferredSize(new Dimension(950, 550));
		setLayout(new BorderLayout(0, 0));
		
		initComponents();
		
		ProductosHiloCB productosHiloCB = new ProductosHiloCB( this );
		Thread hilo = new Thread( productosHiloCB );
		hilo.start();
		
		recuperarDatos("");
	}
	
	private void initComponents() {
		bgPanel = new JPanel();
		bgPanel.setBackground(new Color(255, 255, 255));
		add(bgPanel);
		
		ImageIcon addIcon = new ImageIcon("images/add_img.png");		
		imagenBtn = new JButton("");
		imagenBtn.setIcon(addIcon);
		imagenBtn.setContentAreaFilled(false);
		imagenBtn.setOpaque(true);
		imagenBtn.setForeground( bgColor );
		imagenBtn.setBackground( primaryColor );
		imagenBtn.setFocusable(false);
		imagenBtn.addActionListener(this);
		
		modeloLabel = new JLabel("Modelo:");
		modeloLabel.setFont(estiloCampos);
		
		modeloTxt = new JTextField();
		modeloTxt.setBorder(new LineBorder(new Color(192, 192, 192)));
		modeloTxt.setFont(estiloCampos);
		modeloTxt.setColumns(10);
		
		tipoLabel = new JLabel("Tipo:");
		tipoLabel.setFont(estiloCampos);
		
		stockLabel = new JLabel("Stock:");
		stockLabel.setFont(estiloCampos);
		
		stockTxt = new JTextField();
		stockTxt.setBorder(new LineBorder(new Color(192, 192, 192)));
		stockTxt.setFont(estiloCampos);
		stockTxt.setColumns(10);
		
		precioLabel = new JLabel("Precio:");
		precioLabel.setFont(estiloCampos);
		
		precioTxt = new JTextField();
		precioTxt.setBorder(new LineBorder(new Color(192, 192, 192)));
		precioTxt.setFont(estiloCampos);
		precioTxt.setColumns(10);
		
		marcaLabel = new JLabel("Marca:");
		marcaLabel.setFont(estiloCampos);
		
		agregarBtn = new JButton("Agregar");
		agregarBtn.setPreferredSize(new Dimension(0, 50));
		agregarBtn.setFont(estiloBoton);
		agregarBtn.setContentAreaFilled(false);
		agregarBtn.setOpaque(true);
		agregarBtn.setForeground( bgColor );
		agregarBtn.setBackground( agregarColorBtn );
		agregarBtn.addActionListener(this);
		
		modificarBtn = new JButton("Modificar");
		modificarBtn.setPreferredSize(new Dimension(0, 50));
		modificarBtn.setFont(estiloBoton);
		modificarBtn.setContentAreaFilled(false);
		modificarBtn.setOpaque(true);
		modificarBtn.setForeground(bgColor);
		modificarBtn.setBackground( modificarColorBtn );
		modificarBtn.addActionListener(this);
		
		eliminarBtn = new JButton("Eliminar");
		eliminarBtn.setPreferredSize(new Dimension(0, 50));
		eliminarBtn.setFont(estiloBoton);
		eliminarBtn.setContentAreaFilled(false);
		eliminarBtn.setOpaque(true);
		eliminarBtn.setForeground( bgColor );
		eliminarBtn.setBackground( eliminarColorBtn );
		eliminarBtn.addActionListener(this);
		
		productosScroll = new JScrollPane();
				
		productosTable = new JTable();
		productosTable.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		productosTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		productosTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
			
				Marca marca = new Marca();
				marca.setId( (int) productosTable.getValueAt( productosTable.getSelectedRow(),  5) );
				marca.setNombre( (String) productosTable.getValueAt( productosTable.getSelectedRow(),  6) );
				marca.setNombreProveedor( (String) productosTable.getValueAt( productosTable.getSelectedRow(),  8) );
				
				//Colocar información
				marcaCb.getModel().setSelectedItem( marca);
				modeloTxt.setText( (String) productosTable.getValueAt( productosTable.getSelectedRow(),  2) );
				precioTxt.setText( ((Double) productosTable.getValueAt( productosTable.getSelectedRow(),  4)).toString() );
				tipoCb.getModel().setSelectedItem( (String) productosTable.getValueAt( productosTable.getSelectedRow(),  3) );
				stockTxt.setText( Integer.toString((int) productosTable.getValueAt( productosTable.getSelectedRow(),  9)) );
				
				Icon iconProd = ((JLabel) productosTable.getValueAt( productosTable.getSelectedRow(),  0)).getIcon();
				imagenBtn.setIcon( iconProd );
			}
		});
		productosScroll.setViewportView(productosTable);
		
		JPanel encabezadoTabla = new JPanel();
		encabezadoTabla.setBackground(Color.DARK_GRAY);
		
		JLabel productoslbl = new JLabel("Productos");
		productoslbl.setForeground(Color.WHITE);
		productoslbl.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		
		ImageIcon recargarIcon = new ImageIcon( "images/refresh_img.png");
		recargarBtn = new JButton("");
		recargarBtn.setIcon(recargarIcon);
		recargarBtn.setFocusable(false);
		recargarBtn.setContentAreaFilled(false);
		recargarBtn.setBorder(null);
		recargarBtn.addActionListener(this);
		
		GroupLayout gl_encabezadoTabla = new GroupLayout(encabezadoTabla);
		gl_encabezadoTabla.setHorizontalGroup(
			gl_encabezadoTabla.createParallelGroup(Alignment.LEADING)
				.addGap(0, 580, Short.MAX_VALUE)
				.addGroup(gl_encabezadoTabla.createSequentialGroup()
					.addGap(10)
					.addComponent(productoslbl, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
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
						.addComponent(productoslbl, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(recargarBtn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
		);
		encabezadoTabla.setLayout(gl_encabezadoTabla);
		
		panelDeBusqueda = new JPanel();
		panelDeBusqueda.setBorder(new LineBorder(new Color(192, 192, 192)));
		panelDeBusqueda.setBackground(Color.WHITE);
		
		buscarTxt = new JTextField();
		buscarTxt.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		buscarTxt.setColumns(10);
		buscarTxt.setBorder(null);
		
		ImageIcon buscarImg = new ImageIcon("images/search_img.png");
		buscarBtn = new JButton();
		buscarBtn.setIcon(buscarImg);
		buscarBtn.setFocusable(false);
		buscarBtn.setContentAreaFilled(false);
		buscarBtn.setBorder(null);
		buscarBtn.addActionListener(this);
		
		GroupLayout gl_panelDeBusqueda = new GroupLayout(panelDeBusqueda);
		gl_panelDeBusqueda.setHorizontalGroup(
			gl_panelDeBusqueda.createParallelGroup(Alignment.LEADING)
				.addGap(0, 580, Short.MAX_VALUE)
				.addGroup(gl_panelDeBusqueda.createSequentialGroup()
					.addGap(9)
					.addComponent(buscarTxt, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
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
		
		Marca marca = new Marca();
		marca.setId(-1);
		marca.setNombre("Seleccionar");
		marca.setNombreProveedor(" Proveedor ");
		
		marcaCb = new JComboBox<>();
		marcaCb.setBorder(new LineBorder(new Color(192, 192, 192)));
		marcaCb.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		marcaCb.addItem(marca);
		marcaCb.addActionListener(this);
		
		Proveedor proveedor = new Proveedor();
		proveedor.setId(-1);
		proveedor.setNombre("Seleccionar");
		
		DefaultComboBoxModel<String> tipoModel = new DefaultComboBoxModel<String>();
		tipoModel.addElement("Seleccionar");
		tipoModel.addElement("Snapback");
		tipoModel.addElement("Adjustable");
		tipoModel.addElement("Curva");
		tipoModel.addElement("Cerrada");
		tipoModel.addElement("Small-Medium");
		tipoModel.addElement("Medium-Large");
		tipoModel.addElement( "MVP");
		tipoModel.addElement("59FIFTY 6 7/8");
		tipoModel.addElement("59FIFTY 7");
		tipoModel.addElement("59FIFTY 7 1/8");
		tipoModel.addElement("59FIFTY 7 1/4");
		tipoModel.addElement("59FIFTY 7 3/8");
		tipoModel.addElement("59FIFTY 7 1/2");
		tipoModel.addElement("59FIFTY 7 5/8");
		tipoModel.addElement("59FIFTY 7 3/4");
		tipoModel.addElement("59FIFTY 7 7/8");
		tipoModel.addElement("59FIFTY 8");
		tipoModel.addElement("59FIFTY 8 1/8");
		tipoModel.addElement("59FIFTY 8 1/4");
		
		tipoCb = new JComboBox<String>();
		tipoCb.setBorder(new LineBorder(new Color(192, 192, 192)));
		tipoCb.setModel(tipoModel);
		
		tipoCb.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		GroupLayout gl_bgPanel = new GroupLayout(bgPanel);
		gl_bgPanel.setHorizontalGroup(
			gl_bgPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bgPanel.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_bgPanel.createSequentialGroup()
							.addComponent(imagenBtn, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_bgPanel.createSequentialGroup()
									.addComponent(modeloLabel, GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
									.addGap(356))
								.addComponent(modeloTxt, GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
								.addGroup(gl_bgPanel.createSequentialGroup()
									.addGap(4)
									.addComponent(marcaLabel, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
									.addGap(6)
									.addComponent(tipoLabel, GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
									.addGap(24))
								.addGroup(gl_bgPanel.createSequentialGroup()
									.addComponent(marcaCb, 0, 345, Short.MAX_VALUE)
									.addGap(10)
									.addComponent(tipoCb, 0, 350, Short.MAX_VALUE)
									.addGap(1))
								.addGroup(gl_bgPanel.createSequentialGroup()
									.addComponent(precioLabel, GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
									.addGap(6)
									.addComponent(stockLabel, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
									.addGap(4))
								.addGroup(gl_bgPanel.createSequentialGroup()
									.addComponent(precioTxt, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
									.addGap(10)
									.addComponent(stockTxt, GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))))
						.addGroup(gl_bgPanel.createSequentialGroup()
							.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(encabezadoTabla, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(productosScroll, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
								.addComponent(panelDeBusqueda, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
							.addGap(10)
							.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(agregarBtn, GroupLayout.PREFERRED_SIZE, 326, GroupLayout.PREFERRED_SIZE)
								.addComponent(modificarBtn, GroupLayout.PREFERRED_SIZE, 326, GroupLayout.PREFERRED_SIZE)
								.addComponent(eliminarBtn, GroupLayout.PREFERRED_SIZE, 326, GroupLayout.PREFERRED_SIZE))))
					.addGap(10))
		);
		gl_bgPanel.setVerticalGroup(
			gl_bgPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bgPanel.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(imagenBtn, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_bgPanel.createSequentialGroup()
							.addGap(24)
							.addComponent(modeloLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
							.addGap(3)
							.addComponent(modeloTxt, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addGap(9)
							.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_bgPanel.createSequentialGroup()
									.addGap(2)
									.addComponent(marcaLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
								.addComponent(tipoLabel))
							.addGap(1)
							.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(marcaCb, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_bgPanel.createSequentialGroup()
									.addGap(1)
									.addComponent(tipoCb, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)))
							.addGap(10)
							.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(precioLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
								.addComponent(stockLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
							.addGap(5)
							.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(precioTxt, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addComponent(stockTxt, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))))
					.addGap(5)
					.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_bgPanel.createSequentialGroup()
							.addComponent(encabezadoTabla, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(productosScroll, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
							.addGap(11)
							.addComponent(panelDeBusqueda, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_bgPanel.createSequentialGroup()
							.addGap(6)
							.addComponent(agregarBtn, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(modificarBtn, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
							.addGap(9)
							.addComponent(eliminarBtn, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)))
					.addGap(11))
		);
		bgPanel.setLayout(gl_bgPanel);

	}
	
	public void llenarComboMarcas() {
		//Llenar datos de proveedor
		Conexion conexion = new Conexion();
		MarcasBD marcasBD = new MarcasBD( conexion.conectar() );
		for (Marca marca : marcasBD.buscar("")) {
			marcaCb.addItem( marca );
		}
	}
	
	public void recuperarDatos(String cadena) {
		
		ProductosHilo productosHilo = new ProductosHilo(this, cadena);
		Thread hilo = new Thread( productosHilo );
		hilo.start();
		
	}
	
	public void crearModeloTabla ( String cadena ) {
		defaultTableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		defaultTableModel.addColumn("Imagen");
		defaultTableModel.addColumn("ID");
		defaultTableModel.addColumn("Modelo");
		defaultTableModel.addColumn("Tipo");
		defaultTableModel.addColumn("Precio");
		defaultTableModel.addColumn("ID Marca");
		defaultTableModel.addColumn("Marca");
		defaultTableModel.addColumn("ID Proveedor");
		defaultTableModel.addColumn("Proveedor");
		defaultTableModel.addColumn("Almacen");
		defaultTableModel.addColumn("Estado");
		
		cargarDatos( cadena );
	}
	
	public void cargarDatos( String cadena ) {
		productosTable.setDefaultRenderer(Object.class, new RenderImagen()); //Permite visualizar las imagenes en la tabla
		Object datos[] = new Object[11];
		
		//Traer los datos
		Conexion conexion = new Conexion();
		ProductosBD productosBD = new ProductosBD( conexion.conectar() );
		List<Producto> listaProductos = productosBD.buscar( cadena );
		
		int imgWidth = imagenBtn.getWidth();
		int imgHeight = imagenBtn.getHeight();
		
		if (listaProductos != null) {
			for ( Producto producto : listaProductos ) {
				
				//Colocar Imagen
				try {
					byte[] image = producto.getImagenProducto();
					BufferedImage bufferedImage = null;
					InputStream inputStream = new ByteArrayInputStream(image);
					bufferedImage = ImageIO.read(inputStream);
					
					ImageIcon icono = new ImageIcon( bufferedImage.getScaledInstance( imgWidth, imgHeight, Image.SCALE_SMOOTH));
					datos[0] = new JLabel(icono);
				} catch (Exception e) {
					datos[0] = new JLabel("No imagen");
				}
				
				datos[1] = producto.getId();
				datos[2] = producto.getModelo();
				datos[3] = producto.getTipo();
				datos[4] = producto.getPrecio();
				datos[5] = producto.getMarca().getId();
				datos[6] = producto.getMarca().getNombre();
				datos[7] = producto.getMarca().getIdProveedor();
				datos[8] = producto.getMarca().getNombreProveedor();
				datos[9] = producto.getStock();
				datos[10] = producto.getEstado();

				defaultTableModel.addRow(datos);
			}
			
			productosTable.setModel(defaultTableModel);
	
			productosTable.getColumnModel().getColumn(0).setMaxWidth( imgWidth );
			productosTable.getColumnModel().getColumn(0).setMinWidth( imgWidth );
			productosTable.getColumnModel().getColumn(0).setPreferredWidth( imgWidth );
			
			//Ocultar columna
			productosTable.getColumnModel().getColumn(5).setMaxWidth( 0 );
			productosTable.getColumnModel().getColumn(5).setMinWidth( 0 );
			productosTable.getColumnModel().getColumn(5).setPreferredWidth( 0 );
			
			//Ocultar columna
			productosTable.getColumnModel().getColumn(7).setMaxWidth( 0 );
			productosTable.getColumnModel().getColumn(7).setMinWidth( 0 );
			productosTable.getColumnModel().getColumn(7).setPreferredWidth( 0 );
			
			//Ocultar columna
			productosTable.getColumnModel().getColumn(10).setMaxWidth( 0 );
			productosTable.getColumnModel().getColumn(10).setMinWidth( 0 );
			productosTable.getColumnModel().getColumn(10).setPreferredWidth( 0 );
			
			productosTable.setRowHeight( imgHeight );
		}
	}
	
	public byte[] convertirIconABytes() {
		byte[] bytes = null;
		
		try {
			Icon icon = imagenBtn.getIcon();		
			BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.SCALE_SMOOTH);
			Graphics g = bi.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.setColor(Color.WHITE);
			g.dispose();
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", os);
			InputStream fis = new ByteArrayInputStream(os.toByteArray());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
			    bos.write(buf, 0, readNum);
			    System.out.println("read " + readNum + " bytes,");
			}
			 
			bytes = bos.toByteArray();

		} catch (IOException e) {
			System.err.print("Error en conversión de imagen a bytes");
			e.printStackTrace();
		}
		
		return bytes;
	}
	
	private void limpiarDatos() {
		
		marcaCb.setSelectedIndex(0);
		modeloTxt.setText("");
		precioTxt.setText("");
		tipoCb.setSelectedIndex(0);
		stockTxt.setText("");
		imagenBtn.setIcon( new ImageIcon("images/add_img.png"));
		
		productosTable.clearSelection();
		
	}
	
	public boolean verificarCampos() {
		if (imagenBtn.getIcon().toString() == "images/add_img.png") {
			JOptionPane.showMessageDialog(this, "Imagen vacía", "Imagen vacía", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		if (marcaCb.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "Selecciona una marca", "Marca vacío", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		if (modeloTxt.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "Modelo de marca vacía", "Modelo vacío", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		if (tipoCb.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "Selecciona un tipo", "Tipo vacío", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		if ( precioTxt.getText().length() == 0 ) {
			JOptionPane.showMessageDialog(this, "El precio esta vacío", "Precio vacío", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		try {
			@SuppressWarnings("unused")
			double precio = Double.parseDouble( precioTxt.getText() );
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "El precio no es un número", "Precio", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		if ( stockTxt.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "La cantidad en almacen esta vacía", "Almacen vacío", JOptionPane.WARNING_MESSAGE);
			return true;
		}
		
		if (!StringUtils.isStrictlyNumeric( stockTxt.getText() )) {
			JOptionPane.showMessageDialog(this, "La cantidad en almacen no es un número", "Almacen", JOptionPane.WARNING_MESSAGE);
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
			
			Marca marca = new Marca();
			marca.setId( ((Marca) marcaCb.getSelectedItem()).getId());
			
			Producto producto = new Producto();
			producto.setModelo( modeloTxt.getText() );
			producto.setTipo( ( (String) tipoCb.getSelectedItem()).toString() );
			producto.setPrecio( Double.parseDouble( precioTxt.getText()) );
			producto.setMarca( marca );
			producto.setStock( Integer.parseInt( stockTxt.getText() ));
			producto.setImagenProducto( convertirIconABytes() );
			producto.setEstado(1);
			
			Conexion conexion = new Conexion();
			ProductosBD productosBD = new ProductosBD( conexion.conectar());
			productosBD.insertar( producto );
			
			limpiarDatos();
			
		} else if (e.getSource() == modificarBtn) {
			
			if (verificarCampos()) {
				return;
			}
				
			try {
				Marca marca = new Marca();
				marca.setId( ((Marca) marcaCb.getSelectedItem()).getId());
				
				Producto producto = new Producto();
				producto.setId( (int) productosTable.getValueAt( productosTable.getSelectedRow(),  1));
				producto.setModelo( modeloTxt.getText() );
				producto.setTipo( ( (String) tipoCb.getSelectedItem()).toString() );
				producto.setPrecio( Double.parseDouble( precioTxt.getText()) );
				producto.setMarca( marca );
				producto.setStock( Integer.parseInt( stockTxt.getText() ));
				producto.setImagenProducto( convertirIconABytes() );
				
				Conexion conexion = new Conexion();
				ProductosBD productosBD = new ProductosBD( conexion.conectar());
				productosBD.modificar( producto );
				
				limpiarDatos();
			} catch (ArrayIndexOutOfBoundsException ex) {
				JOptionPane.showMessageDialog(null, "Selecciona el producto a modificar desde la tabla", "Modificación", JOptionPane.WARNING_MESSAGE);
				
			} catch (Exception ex) {
				ex.printStackTrace();
				
			}
			
		} else if (e.getSource() == eliminarBtn) {
			
			try {
				
				Producto producto = new Producto();
				producto.setId( (int) productosTable.getValueAt( productosTable.getSelectedRow(),  1));
				producto.setEstado(0);
				
				Conexion conexion = new Conexion();
				ProductosBD productosBD = new ProductosBD( conexion.conectar());
				productosBD.eliminar( producto );
				
				limpiarDatos();
			} catch (ArrayIndexOutOfBoundsException ex) {
				JOptionPane.showMessageDialog(null, "Selecciona el producto a modificar desde la tabla", "Modificación", JOptionPane.WARNING_MESSAGE);
				
			} catch (Exception ex) {
				ex.printStackTrace();
				
			}
			
		} else if (e.getSource() == imagenBtn) {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("JPG, PNG & GIF", "jpg", "png", "gif");
			fileChooser.setFileFilter( extensionFilter );
			
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				//Obtener imagen
				String ruta = fileChooser.getSelectedFile().getAbsolutePath();
				Image image = new ImageIcon(ruta).getImage();
				ImageIcon icon = new ImageIcon( image.getScaledInstance( imagenBtn.getWidth(), imagenBtn.getHeight(), Image.SCALE_SMOOTH));
				
				//Colocar imagen sobre el botón
				imagenBtn.setIcon(icon);
				imagenBtn.setBorder(new LineBorder(Color.BLACK, 1 ));
			}
			
		} else if (e.getSource() == recargarBtn) {
			
			recuperarDatos("");
			
		} else if (e.getSource() == buscarBtn) {
			
			recuperarDatos( buscarTxt.getText() );
			
		} 
	}
}
