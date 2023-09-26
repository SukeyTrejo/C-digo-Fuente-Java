package com.system.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import com.system.bd.Conexion;
import com.system.bd.ProductosBD;
import com.system.bd.VentasBD;
import com.system.frames.Ticket;
import com.system.hilos.VentasHilo;
import com.system.objetos.Marca;
import com.system.objetos.Producto;
import com.system.objetos.ProductoCompra;
import com.system.objetos.RenderImagen;
import com.system.objetos.Venta;

public class VentasPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	//Colores de interfaz
	private final Color primaryColor = new Color(0x404040);
    private final Color secondaryColor = new Color(0x0E7221);
    private final Color bgColor = new Color(0xFFFFFF);
    
    //Estilos de letra
    private final Font estiloCampos = new Font("Century Gothic", Font.PLAIN, 15);
    private final Font estiloBoton = new Font("Century Gothic", Font.BOLD, 15);
    
    private JPanel leftPanel;
    private ImageIcon addIcon;
    private JLabel imagelbl;
    private JScrollPane productosScroll;
   
    private JPanel rightPanel;
    private JLabel listaLabel;
    private JScrollPane listaScroll;
    private DefaultListModel<Producto> listaListModel;
    private JList<Producto> listaCompras;
    private double totalCompra = 0.0;
    
    private JLabel totalLabel;
    private JTextField totalTxt;
    private JButton quitarBtn;
    private JButton registrarVentaBtn;
    private DefaultTableModel defaultTableModel;
    private JTable productosTable;
    private JPanel panelDeBusqueda;
    private JTextField buscarTxt;
    private JButton buscarBtn;
    private JPanel encabezadoTabla;
    private JLabel productoslbl;
    private JPanel panelCantidad;
    private JTextField cantidadTxt;
    private JButton agregarBtn;
    private JScrollPane informacionScroll;
    private DefaultListModel<Producto> informacionListModel;
    private JList<Producto> listaInformacion;
    private JLabel informacionlbl;

	public VentasPanel() {
		setPreferredSize(new Dimension(950, 550));
		setBackground(new Color(255, 255, 255));
		
		initComponents();
		
		recuperarDatos("");
	}
	
	private void initComponents() {
		//Panel izquierdo
		leftPanel = new JPanel();
		leftPanel.setBackground(new Color(255, 255, 255));
		leftPanel.setBorder(new MatteBorder(1, 1, 1, 1,(Color) primaryColor));
		
		addIcon = new ImageIcon("images/add_img.png");
		imagelbl = new JLabel();
		imagelbl.setIcon(addIcon);
		imagelbl.setBackground(primaryColor);
		imagelbl.setPreferredSize(new Dimension(180, 180));
		imagelbl.setHorizontalAlignment(SwingConstants.CENTER);
		imagelbl.setOpaque(true);
		imagelbl.setFocusable(false);
		
		productosScroll = new JScrollPane();
		productosTable = new JTable();
		productosTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Producto producto = new Producto();
				
				//Crear objeto de producto seleccionado
				producto.setId( (int) productosTable.getValueAt( productosTable.getSelectedRow(),  1) );
				producto.setModelo( (String) productosTable.getValueAt( productosTable.getSelectedRow(),  2) );
				producto.setTipo( (String) productosTable.getValueAt( productosTable.getSelectedRow(),  3) );
				producto.setPrecio( (double) productosTable.getValueAt( productosTable.getSelectedRow(),  4) );
				
				Marca marca = new Marca();
				marca.setId( (int) productosTable.getValueAt( productosTable.getSelectedRow(),  5) );
				marca.setNombre( (String) productosTable.getValueAt( productosTable.getSelectedRow(),  6) );
				marca.setIdProveedor( (int) productosTable.getValueAt( productosTable.getSelectedRow(), 7) );
				marca.setNombreProveedor( (String) productosTable.getValueAt( productosTable.getSelectedRow(),  8));
				
				producto.setMarca( marca );
				
				producto.setStock( (int) productosTable.getValueAt( productosTable.getSelectedRow(),  9) );
				producto.setEstado( (int) productosTable.getValueAt( productosTable.getSelectedRow(),  10) );
				
				//Mostrar imagen
				Icon icono = ((JLabel) productosTable.getValueAt( productosTable.getSelectedRow(),  0)).getIcon();
				imagelbl.setIcon( icono );
				
				//Agregar elemento seleccionado en la lista
				informacionListModel.clear();
				informacionListModel.add(0, producto);
				listaInformacion.setModel( informacionListModel );
				
			}
		});
		productosTable.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		productosScroll.setViewportView(productosTable);
		
		panelDeBusqueda = new JPanel();
		panelDeBusqueda.setBorder(new LineBorder(new Color(192, 192, 192)));
		panelDeBusqueda.setBackground(Color.WHITE);
		
		buscarTxt = new JTextField();
		buscarTxt.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		buscarTxt.setColumns(10);
		buscarTxt.setBorder(null);
		
		ImageIcon buscarIcon = new ImageIcon("images/search_img.png");
		buscarBtn = new JButton();
		buscarBtn.setIcon(buscarIcon);
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
		
		encabezadoTabla = new JPanel();
		encabezadoTabla.setBackground(Color.DARK_GRAY);
		productoslbl = new JLabel("Productos");
		productoslbl.setForeground(Color.WHITE);
		productoslbl.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		GroupLayout gl_encabezadoTabla = new GroupLayout(encabezadoTabla);
		gl_encabezadoTabla.setHorizontalGroup(
			gl_encabezadoTabla.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_encabezadoTabla.createSequentialGroup()
					.addContainerGap()
					.addComponent(productoslbl, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
					.addGap(356))
		);
		gl_encabezadoTabla.setVerticalGroup(
			gl_encabezadoTabla.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_encabezadoTabla.createSequentialGroup()
					.addComponent(productoslbl, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		encabezadoTabla.setLayout(gl_encabezadoTabla);
		
		panelCantidad = new JPanel();
		panelCantidad.setBorder(new LineBorder(new Color(192, 192, 192)));
		panelCantidad.setBackground(Color.WHITE);
		
		JLabel cantidadlbl = new JLabel("Cantidad");
		cantidadlbl.setForeground(new Color(128, 128, 128));
		cantidadlbl.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		
		cantidadTxt = new JTextField();
		cantidadTxt.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		cantidadTxt.setColumns(10);
		cantidadTxt.setBorder(null);
		
		rightPanel = new JPanel();
		rightPanel.setBackground(new Color(255, 255, 255));
		rightPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) primaryColor));
		
		listaScroll = new JScrollPane();
		listaLabel = new JLabel(" Lista de Compras");
		listaLabel.setFont(estiloCampos);
		listaLabel.setOpaque(true);
		listaLabel.setForeground(Color.WHITE);
		listaLabel.setBackground(Color.DARK_GRAY);
		
		listaCompras = new JList<Producto>();
		listaListModel= new DefaultListModel<Producto>();
		listaCompras.setModel( listaListModel );
		listaCompras.setFont(new Font("Century Gothic", Font.PLAIN, 17));
		
		listaScroll.setViewportView(listaCompras);
		
		totalLabel = new JLabel("Total");
		totalLabel.setForeground(new Color(128, 128, 128));
		totalLabel.setFont(estiloCampos);
		
		totalTxt = new JTextField();
		totalTxt.setHorizontalAlignment(SwingConstants.CENTER);
		totalTxt.setText("0.0");
		totalTxt.setFont(estiloCampos);
		totalTxt.setBorder(null);
		totalTxt.setEnabled(false);
		totalTxt.setBackground(Color.WHITE);
		totalTxt.setColumns(10);
		
		ImageIcon eliminarIcon = new ImageIcon("images/eliminar_producto.png");
		quitarBtn = new JButton();
		quitarBtn.setIcon(eliminarIcon);
		quitarBtn.setContentAreaFilled(false);
		quitarBtn.setFocusable(false);
		quitarBtn.setFocusable(false);
		quitarBtn.addActionListener(this);
		
		registrarVentaBtn = new JButton("Registrar Venta");
		registrarVentaBtn.setFont(estiloBoton);
		registrarVentaBtn.setForeground(bgColor);
		registrarVentaBtn.setBackground(secondaryColor);
		registrarVentaBtn.setContentAreaFilled(false);
		registrarVentaBtn.setOpaque(true);
		registrarVentaBtn.setFocusable(false);
		registrarVentaBtn.addActionListener(this);
		
		ImageIcon agregarIcon = new ImageIcon("images/agregar_producto.png");
		agregarBtn = new JButton();
		agregarBtn.setIcon(agregarIcon);
		agregarBtn.setFocusable(false);
		agregarBtn.setContentAreaFilled(false);
		agregarBtn.setBorder(null);
		agregarBtn.addActionListener(this);
		
		informacionScroll = new JScrollPane();
		informacionScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		informacionScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		informacionScroll.setBorder(null);
		
		informacionlbl = new JLabel("Información");
		informacionlbl.setHorizontalAlignment(SwingConstants.CENTER);
		informacionlbl.setOpaque(true);
		informacionlbl.setForeground(Color.WHITE);
		informacionlbl.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		informacionlbl.setBackground(Color.DARK_GRAY);
		
		Producto producto = new Producto();
		listaInformacion = new JList<>();
		listaInformacion.setEnabled(false);
		informacionListModel = new DefaultListModel<Producto>();
		informacionListModel.add(0, producto);
		listaInformacion.setModel(informacionListModel);
		
		listaInformacion.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		informacionScroll.setViewportView(listaInformacion);
		GroupLayout gl_rightPanel = new GroupLayout(rightPanel);
		gl_rightPanel.setHorizontalGroup(
			gl_rightPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addGap(9)
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(listaScroll, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
						.addComponent(listaLabel, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
						.addGroup(gl_rightPanel.createSequentialGroup()
							.addComponent(totalLabel, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(totalTxt, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
							.addGap(145)
							.addComponent(quitarBtn, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
						.addComponent(registrarVentaBtn, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE))
					.addGap(9))
		);
		gl_rightPanel.setVerticalGroup(
			gl_rightPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_rightPanel.createSequentialGroup()
							.addGap(25)
							.addComponent(listaScroll, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
						.addComponent(listaLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addGap(2)
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_rightPanel.createSequentialGroup()
							.addGap(9)
							.addComponent(totalLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_rightPanel.createSequentialGroup()
							.addGap(5)
							.addComponent(totalTxt, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addComponent(quitarBtn, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
					.addGap(6)
					.addComponent(registrarVentaBtn, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addGap(10))
		);
		rightPanel.setLayout(gl_rightPanel);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(10)
					.addComponent(rightPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(10))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(rightPanel, GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
					.addGap(11))
		);
		
		GroupLayout gl_panelCantidad = new GroupLayout(panelCantidad);
		gl_panelCantidad.setHorizontalGroup(
			gl_panelCantidad.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelCantidad.createSequentialGroup()
					.addGap(9)
					.addComponent(cantidadTxt, GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
					.addComponent(agregarBtn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
		);
		gl_panelCantidad.setVerticalGroup(
			gl_panelCantidad.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelCantidad.createSequentialGroup()
					.addGap(1)
					.addGroup(gl_panelCantidad.createParallelGroup(Alignment.LEADING)
						.addComponent(cantidadTxt, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(agregarBtn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
		);
		panelCantidad.setLayout(gl_panelCantidad);
		GroupLayout gl_leftPanel = new GroupLayout(leftPanel);
		gl_leftPanel.setHorizontalGroup(
			gl_leftPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addGap(9)
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addGap(179)
							.addComponent(informacionlbl, GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
							.addGap(1))
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addComponent(imagelbl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(13)
							.addComponent(informacionScroll, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
							.addGap(1))
						.addComponent(panelDeBusqueda, GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addComponent(encabezadoTabla, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
							.addGap(1))
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addComponent(productosScroll, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
							.addGap(1))
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addComponent(cantidadlbl, GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
							.addGap(367))
						.addComponent(panelCantidad, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(16))
		);
		gl_leftPanel.setVerticalGroup(
			gl_leftPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addComponent(informacionlbl, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(informacionScroll, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE))
						.addComponent(imagelbl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(8)
					.addComponent(panelDeBusqueda, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(encabezadoTabla, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addComponent(productosScroll, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
					.addGap(4)
					.addComponent(cantidadlbl, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(panelCantidad, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(7))
		);
		leftPanel.setLayout(gl_leftPanel);
		setLayout(groupLayout);
	}

	public void recuperarDatos(String cadena) {
		
		VentasHilo ventasHilo = new VentasHilo(this, cadena);
		Thread hilo = new Thread( ventasHilo );
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
		Object datos[] = new Object[12];
		
		//Traer los datos
		Conexion conexion = new Conexion();
		ProductosBD productosBD = new ProductosBD( conexion.conectar() );
		List<Producto> listaProductos = productosBD.buscar( cadena );
		
		int imgWidth = imagelbl.getWidth();
		int imgHeight = imagelbl.getHeight();
		
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
	
	public boolean suficienciaAlmacen() throws NumberFormatException, ArrayIndexOutOfBoundsException{
		try {
			if ( Integer.parseInt( cantidadTxt.getText() ) > (int) productosTable.getValueAt( productosTable.getSelectedRow(), 9) ){
				JOptionPane.showMessageDialog(null, "No tienes suficientes productos para la venta", "Insuficiencia de almacen", JOptionPane.WARNING_MESSAGE);
				cantidadTxt.setText("");
				return true;
			}
		} catch (NumberFormatException ex) {
			throw new NumberFormatException();
			
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new ArrayIndexOutOfBoundsException();
			
		}
		return false;
	}
	
	public boolean productoEnLista( Producto producto ) {
		//Recorrer todos los elementos de la lista
		for (int i = 0; i < listaCompras.getModel().getSize(); i++) {
			ProductoCompra productoCompra = (ProductoCompra) listaCompras.getModel().getElementAt(i);
			
			if (productoCompra.getId() == producto.getId()) { //Si encuentra el elemento debe cambiar la cantidad
				
				//Nueva cantidad de compra
				int cantidadPrevia = productoCompra.getCantidad();
				productoCompra.setCantidad( cantidadPrevia + Integer.parseInt(cantidadTxt.getText()));
				
				listaListModel.setElementAt(productoCompra, i);
				listaCompras.setModel( listaListModel );
				
				return true;
			}
		}
		return false;
	}
	
	private void limpiarDatos() {
		
		//Limpiar cantidad
		cantidadTxt.setText("");
		
		//Limpiar lista de información
		informacionListModel.clear();
		
		Producto producto = new Producto();
		informacionListModel.add(0, producto);
		
		//Limpiar imagen
		imagelbl.setIcon( new ImageIcon("images/add_img.png"));
		
		//Deseleccionar un producto
		productosTable.clearSelection();
		
		//Limpiar seleccion de lista de compra
		listaCompras.clearSelection();
	}
	
	private void limpiarListaCompra(){
		//Resetear total de compra
		totalCompra = 0.0;
		totalTxt.setText( Double.toString( totalCompra ) );
		
		//Limpiar lista
		listaCompras.clearSelection();
		listaListModel.clear();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == agregarBtn) {
			try {
				if (suficienciaAlmacen()) { //Verificar la cantidad de productos en almacen
					return;
				}
				
				if ( Integer.parseInt( cantidadTxt.getText() ) <= 0 ) {
					JOptionPane.showMessageDialog(this, "Coloca la cantidad de compra mayor de cero", "Cantidad de compra", JOptionPane.WARNING_MESSAGE);
					cantidadTxt.setText("");
					return;
				}
				
				//Recuperar valor seleccionado de tabla productos
				Producto producto = (Producto) listaInformacion.getModel().getElementAt(0);
				
				if ( !productoEnLista( producto ) ) { //Si no se encuentra el producto en la lista agregarlo, si no modificar la cantidad del producto sobre la lista
					
					//Crear producto para lista de compra
					ProductoCompra productoCompra = new ProductoCompra();
					
					productoCompra.setId( producto.getId() );
					productoCompra.setModelo( producto.getModelo() );
					productoCompra.setTipo( producto.getTipo() );
					productoCompra.setPrecio( producto.getPrecio() );
					productoCompra.setMarca( producto.getMarca() );
					productoCompra.setCantidad( Integer.parseInt( cantidadTxt.getText() ) );
					
					//Agregar a lista
					listaListModel.add(0, productoCompra);
					
				}
				
				//Sumar valor a total de compra
				totalCompra += Double.parseDouble(cantidadTxt.getText()) * producto.getPrecio();
				totalTxt.setText( Double.toString(totalCompra));
				
				//Reducir existencia en tabla
				productosTable.setValueAt( 
						(producto.getStock() - Integer.parseInt( cantidadTxt.getText()) ), //Reducir valor en tabla
						productosTable.getSelectedRow(), 9); //Ubicación del lugar a reducir su valor [Fila] [Columna] = Posicion de celda
				
				//Limpiar seleccion
				limpiarDatos();
				
			} catch ( NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Coloca la cantidad de compra mayor de cero", "Cantidad de compra", JOptionPane.WARNING_MESSAGE);
				cantidadTxt.setText("");
				
			} catch (ArrayIndexOutOfBoundsException ex) {
				JOptionPane.showMessageDialog(null, "Selecciona el producto a comprar", "Seleccion Producto", JOptionPane.WARNING_MESSAGE);
			
			} catch (Exception ex) {
				
				ex.printStackTrace();
			}
			
		} else if (e.getSource() == quitarBtn) {
			try {
				//Recuperar valor seleccionado de lista de compra
				ProductoCompra productoCompra = (ProductoCompra) listaCompras.getSelectedValue();
				
				//Reducir valor de total
				totalCompra -= (double) productoCompra.getCantidad() * productoCompra.getPrecio();
				totalTxt.setText( Double.toString(totalCompra) );
				
				//Quitar producto de la lista de compra
				listaListModel.remove( listaCompras.getSelectedIndex() );
				
				//Aumentar existencia en tabla
				for (int i = 0; i < productosTable.getModel().getRowCount(); i++) {
					if (productoCompra.getId() == (int) productosTable.getValueAt(i, 1)) {
						productosTable.setValueAt( 
								(productoCompra.getCantidad() +  (int) productosTable.getValueAt(i, 9)), //Aumentar valor en tabla
								i, 9); //Ubicación del lugar a reducir su valor [Fila] [Columna] = Posicion de celda
						
						break;
					}
				}
				
				limpiarDatos();
				
			} catch (NullPointerException ex) {
				JOptionPane.showMessageDialog(null, "Selecciona un producto de la lista que quieres eliminar", "Seleccion Producto", JOptionPane.WARNING_MESSAGE);
			
			} catch (Exception ex) {
				ex.printStackTrace();
				
			}
			
		} else if (e.getSource() == registrarVentaBtn) {
			if (listaCompras.getModel().getSize() == 0) {
				JOptionPane.showMessageDialog(null, "Selecciona al menos un producto para registrar la venta", "Lista vacía", JOptionPane.WARNING_MESSAGE);
				return;
			}
	        
	        //Recuperar objetos de lista de compra
	        List<ProductoCompra> productosCompra = new ArrayList<ProductoCompra>();
	        for (int i = 0; i < listaCompras.getModel().getSize(); i++) {
	        	productosCompra.add( (ProductoCompra) listaCompras.getModel().getElementAt(i) );
	        }
	
	        Venta venta = new Venta();
	        venta.setListaProductos(productosCompra);
			
	        //Insertar venta
	        Conexion conexion = new Conexion();
	        VentasBD ventasBD = new VentasBD( conexion.conectar() );
	        ventasBD.insertar(venta);
	        
	        //Establecer hora de ticket
	        DateFormat fecha = new SimpleDateFormat("yyyy-MM-d");
	        DateFormat hora = new SimpleDateFormat("HH:mm:ss");
	        Calendar cal = Calendar.getInstance();
		    cal.add(Calendar.HOUR, -1);
		    Date date = cal.getTime();
		     
	        venta.setFecha_venta( fecha.format(date));
	        venta.setHora_venta( hora.format(date));
	     
	        Ticket ticket = new Ticket(venta);
	        ticket.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
	        ticket.setSize( 400, 900 );
	        ticket.setLocationRelativeTo(null);
	        ticket.setResizable(false);
	        ticket.setVisible(true);
	        
	        limpiarListaCompra();
			
		} else if ( e.getSource() == buscarBtn ) {
			
			crearModeloTabla( buscarTxt.getText() );
			
		}
	}
}