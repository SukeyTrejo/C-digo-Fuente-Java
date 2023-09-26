package com.system.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import com.system.bd.Conexion;
import com.system.bd.InformeVentasBD;
import com.system.bd.TicketBD;
import com.system.frames.Ticket;
import com.system.hilos.InformeProductosHilo;
import com.system.hilos.InformeProductosHiloCB;
import com.system.hilos.InformeVentasHilo;
import com.system.objetos.ProductoCompra;
import com.system.objetos.RenderImagen;
import com.system.objetos.Venta;
import com.toedter.calendar.JDateChooser;


public class InformePanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
    
    //Elementos
    private JPanel bgPanel;
    private JPanel izqPanel; //Panel izquierdo
    private JLabel fechaIniLabel;
    private JDateChooser fechaIni;
    private JLabel fechaFinLabel;
    private JDateChooser fechaFin;
    private JLabel ventasTabelLabel;
	private JScrollPane ventasScroll;
    private JButton informeVentasBtn;
    private JButton mostrarTicketBtn;
    private JButton informeProductosBtn;
    private JPanel derPanel; //Panel derecho
    private JLabel informeProdLabel;
    private JLabel productosTableLabel;
    private JScrollPane productosScroll;
	private JLabel marcaInformelbl;
	private JLabel tipoInformelbl;
	private JComboBox<String> marcaCb ;
	private JComboBox<String> tipoCb;
	private JComboBox<String> proveedorCb;
	private JLabel provInformelbl;
	private DefaultTableModel defaultTableModelVentas;
	private DefaultTableModel defaultTableModelProductos;
	private JTable ventasTable;
	private JTable productosTable;

	public InformePanel() {
		setBackground(new Color(255, 255, 255));
		setLayout(new BorderLayout());
		
		initComponents();
		
		inicializarFecha();
	
		InformeProductosHiloCB informeProductosHiloCB = new InformeProductosHiloCB(this);
		Thread hilo = new Thread( informeProductosHiloCB );
		hilo.start();
		
		recuperarDatosVentas();
		recuperarDatosProductos();
	}
	
	private void initComponents() {
		bgPanel = new JPanel();
		add(bgPanel, BorderLayout.CENTER);
		
		izqPanel = new JPanel();
		izqPanel.setBackground(new Color(255, 255, 255));
		izqPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		fechaIniLabel = new JLabel("Fecha de Inicio:");
		fechaIniLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fechaIniLabel.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		
		fechaIni = new JDateChooser();
		fechaIni.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		fechaIni.setDateFormatString("yyyy-MM-d");
		fechaIni.setBackground(Color.WHITE);
		JButton fechaIniBtn = fechaIni.getCalendarButton();
		fechaIniBtn.setIcon(new ImageIcon("images/calendar_img.png"));
		fechaIniBtn.setContentAreaFilled(false);
		fechaIniBtn.setBorder(null);
	
		fechaFinLabel = new JLabel("Fecha de Fin:");
		fechaFinLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fechaFinLabel.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		
		fechaFin = new JDateChooser();
		fechaFin.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		fechaFin.setDateFormatString("yyyy-MM-d");
		fechaFin.setBackground(Color.WHITE);
		JButton fechaFinBtn = fechaFin.getCalendarButton();
		fechaFinBtn.setIcon(new ImageIcon("images/calendar_img.png"));
		fechaFinBtn.setContentAreaFilled(false);
		fechaFinBtn.setBorder(null);
		
		informeVentasBtn = new JButton("Mostrar Ventas");
		informeVentasBtn.setContentAreaFilled(false);
		informeVentasBtn.setOpaque(true);
		informeVentasBtn.setForeground(Color.WHITE);
		informeVentasBtn.setFont(new Font("Century Gothic", Font.BOLD, 15));
		informeVentasBtn.setFocusable(false);
		informeVentasBtn.setBackground(Color.DARK_GRAY);
		informeVentasBtn.addActionListener(this);
		
		ventasScroll = new JScrollPane();
		
		ventasTabelLabel = new JLabel(" Ventas");
		ventasTabelLabel.setOpaque(true);
		ventasTabelLabel.setForeground(Color.WHITE);
		ventasTabelLabel.setFont(new Font("Century Gothic", Font.BOLD, 14));
		ventasTabelLabel.setBackground(Color.DARK_GRAY);
		
		mostrarTicketBtn = new JButton("Mostrar Ticket de Ventas");
		mostrarTicketBtn.setContentAreaFilled(false);
		mostrarTicketBtn.setOpaque(true);
		mostrarTicketBtn.setForeground(Color.WHITE);
		mostrarTicketBtn.setFont(new Font("Century Gothic", Font.BOLD, 15));
		mostrarTicketBtn.setFocusable(false);
		mostrarTicketBtn.setBackground(Color.DARK_GRAY);
		mostrarTicketBtn.addActionListener(this);
		
		ventasTable = new JTable();
		ventasTable.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		ventasScroll.setViewportView(ventasTable);
		
		//Panel derecho
		derPanel = new JPanel();
		derPanel.setBackground(new Color(255, 255, 255));
		derPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		informeProdLabel = new JLabel("Informe de Productos Vendidos");
		informeProdLabel.setHorizontalAlignment(SwingConstants.CENTER);
		informeProdLabel.setFont(new Font("Century Gothic", Font.BOLD, 20));
		
		productosTableLabel = new JLabel(" Productos");
		productosTableLabel.setOpaque(true);
		productosTableLabel.setForeground(Color.WHITE);
		productosTableLabel.setFont(new Font("Century Gothic", Font.BOLD, 14));
		productosTableLabel.setBackground(Color.DARK_GRAY);
		
		productosScroll = new JScrollPane();
		
		marcaInformelbl = new JLabel("Marca:");
		marcaInformelbl.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		
		tipoInformelbl = new JLabel("Tipo:");
		tipoInformelbl.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		
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
		tipoCb.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		tipoCb.setFocusable(false);
		tipoCb.setModel(tipoModel);
		tipoCb.setBackground(Color.WHITE);
		
		provInformelbl = new JLabel("Proveedor:");
		provInformelbl.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		
		proveedorCb = new JComboBox<String>();
		proveedorCb.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		proveedorCb.setFocusable(false);
		proveedorCb.setBackground(Color.WHITE);
		proveedorCb.addItem( "Seleccionar" );
		
		productosTable = new JTable();
		productosTable.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		
		productosScroll.setViewportView(productosTable);
		
		marcaCb = new JComboBox<String>();
		marcaCb.setFont(new Font("Century Gothic", Font.PLAIN, 15));
		marcaCb.setFocusable(false);
		marcaCb.setBackground(Color.WHITE);
		marcaCb.addItem("Seleccionar");
		
		informeProductosBtn = new JButton("Mostrar Productos");
		informeProductosBtn.setContentAreaFilled(false);
		informeProductosBtn.setOpaque(true);
		informeProductosBtn.setForeground(Color.WHITE);
		informeProductosBtn.setFont(new Font("Century Gothic", Font.BOLD, 15));
		informeProductosBtn.setFocusable(false);
		informeProductosBtn.setBackground(Color.DARK_GRAY);
		informeProductosBtn.addActionListener(this);
		GroupLayout gl_derPanel = new GroupLayout(derPanel);
		gl_derPanel.setHorizontalGroup(
			gl_derPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_derPanel.createSequentialGroup()
					.addGap(19)
					.addGroup(gl_derPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(informeProdLabel, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
						.addGroup(gl_derPanel.createSequentialGroup()
							.addComponent(marcaInformelbl, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
							.addGap(230))
						.addGroup(gl_derPanel.createSequentialGroup()
							.addComponent(marcaCb, 0, 430, Short.MAX_VALUE)
							.addGap(10))
						.addGroup(gl_derPanel.createSequentialGroup()
							.addComponent(provInformelbl, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
							.addGap(230))
						.addGroup(gl_derPanel.createSequentialGroup()
							.addComponent(proveedorCb, 0, 430, Short.MAX_VALUE)
							.addGap(10))
						.addGroup(gl_derPanel.createSequentialGroup()
							.addComponent(tipoInformelbl, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
							.addGap(230))
						.addGroup(gl_derPanel.createSequentialGroup()
							.addComponent(tipoCb, 0, 430, Short.MAX_VALUE)
							.addGap(10))
						.addGroup(gl_derPanel.createSequentialGroup()
							.addComponent(informeProductosBtn, GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
							.addGap(11))
						.addGroup(gl_derPanel.createSequentialGroup()
							.addComponent(productosTableLabel, GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
							.addGap(9))
						.addGroup(gl_derPanel.createSequentialGroup()
							.addComponent(productosScroll, GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
							.addGap(9)))
					.addGap(9))
		);
		gl_derPanel.setVerticalGroup(
			gl_derPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_derPanel.createSequentialGroup()
					.addGap(10)
					.addComponent(informeProdLabel, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(marcaInformelbl, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(marcaCb, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addComponent(provInformelbl, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(proveedorCb, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(tipoInformelbl, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(tipoCb, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(informeProductosBtn, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addGroup(gl_derPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(productosTableLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_derPanel.createSequentialGroup()
							.addGap(26)
							.addComponent(productosScroll, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)))
					.addGap(27))
		);
		derPanel.setLayout(gl_derPanel);
		GroupLayout gl_izqPanel = new GroupLayout(izqPanel);
		gl_izqPanel.setHorizontalGroup(
			gl_izqPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_izqPanel.createSequentialGroup()
					.addGap(9)
					.addGroup(gl_izqPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_izqPanel.createSequentialGroup()
							.addComponent(fechaIniLabel, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(fechaIni, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
							.addGap(1))
						.addGroup(gl_izqPanel.createSequentialGroup()
							.addComponent(fechaFinLabel, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(fechaFin, GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
							.addGap(0))
						.addGroup(gl_izqPanel.createSequentialGroup()
							.addComponent(informeVentasBtn, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
							.addGap(1))
						.addGroup(gl_izqPanel.createSequentialGroup()
							.addComponent(ventasScroll, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
							.addGap(1))
						.addGroup(gl_izqPanel.createSequentialGroup()
							.addComponent(ventasTabelLabel, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
							.addGap(1))
						.addGroup(gl_izqPanel.createSequentialGroup()
							.addComponent(mostrarTicketBtn, GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
							.addGap(1)))
					.addGap(9))
		);
		gl_izqPanel.setVerticalGroup(
			gl_izqPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_izqPanel.createSequentialGroup()
					.addGap(8)
					.addGroup(gl_izqPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_izqPanel.createSequentialGroup()
							.addGap(10)
							.addComponent(fechaIniLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
						.addComponent(fechaIni, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
					.addGap(4)
					.addGroup(gl_izqPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_izqPanel.createSequentialGroup()
							.addGap(7)
							.addComponent(fechaFinLabel, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE))
						.addComponent(fechaFin, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
					.addGap(8)
					.addComponent(informeVentasBtn, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addGroup(gl_izqPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_izqPanel.createSequentialGroup()
							.addGap(26)
							.addComponent(ventasScroll, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
						.addComponent(ventasTabelLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addComponent(mostrarTicketBtn, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addGap(10))
		);
		izqPanel.setLayout(gl_izqPanel);
		GroupLayout gl_bgPanel = new GroupLayout(bgPanel);
		gl_bgPanel.setHorizontalGroup(
			gl_bgPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bgPanel.createSequentialGroup()
					.addGap(10)
					.addComponent(izqPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(derPanel, GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
					.addGap(10))
		);
		gl_bgPanel.setVerticalGroup(
			gl_bgPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bgPanel.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_bgPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(izqPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(derPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(10))
		);
		bgPanel.setLayout(gl_bgPanel);
				
	}
	
	public void inicializarFecha() {
		Calendar fecha = Calendar.getInstance();
		
		fechaIni.setDate( fecha.getTime() );
		fechaFin.setDate( fecha.getTime() );
	}
	
	public void recuperarDatosVentas() {
		InformeVentasHilo informePanel = new InformeVentasHilo(this);
		Thread hilo = new Thread(informePanel);
		hilo.start();
	}
	
	public void crearModeloTablaVentas() {
		defaultTableModelVentas = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		defaultTableModelVentas.addColumn("ID");
 		defaultTableModelVentas.addColumn("Fecha");
 		defaultTableModelVentas.addColumn("Hora");
 		defaultTableModelVentas.addColumn("Total Comprado");
 		
 		cargarDatosVentas();
	}

	public void cargarDatosVentas() {
		
		Object[] datos = new Object[4];
		
		Conexion conexion = new Conexion();
		InformeVentasBD ventasBD = new InformeVentasBD( conexion.conectar() );
		
		DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-d");
		List<Venta> listaVentas = ventasBD.informeVentas( formatoFecha.format( fechaIni.getDate() ).toString(), formatoFecha.format( fechaFin.getDate() ).toString());
		for (Venta venta : listaVentas) {
			
			datos[0] = venta.getIdVenta();
			datos[1] = venta.getFecha_venta();
			datos[2] = venta.getHora_venta();
			datos[3] = venta.getTotal();
			
			defaultTableModelVentas.addRow( datos);
		}
		
		ventasTable.setModel( defaultTableModelVentas );	
		ventasTable.setRowHeight( 30 );
	}

	public void llenarComboProveedores() {
		//Llenar datos de proveedor
		Conexion conexion = new Conexion();
		InformeVentasBD informeVentasBD = new InformeVentasBD( conexion.conectar() );
		for (String proveedor : informeVentasBD.recuperarProveedores()) {
			proveedorCb.addItem( proveedor );
		}
	}
	
	public void llenarComboMarcas() {
		//Llenar datos de marca
		Conexion conexion = new Conexion();
		InformeVentasBD informeVentasBD = new InformeVentasBD( conexion.conectar() );
		for (String marca : informeVentasBD.recuperarMarcas()) {
			marcaCb.addItem( marca );
		}
	}
	
public void recuperarDatosProductos() {
		
		InformeProductosHilo informeProductosHilos = new InformeProductosHilo( this );
		Thread hilo = new Thread( informeProductosHilos );
		hilo.start();
		
	}
	
	public void crearModeloTablaProductos () {
		defaultTableModelProductos = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		
		defaultTableModelProductos.addColumn("Imagen");
		defaultTableModelProductos.addColumn("ID");
		defaultTableModelProductos.addColumn("Modelo");
		defaultTableModelProductos.addColumn("Tipo");
		defaultTableModelProductos.addColumn("Marca");
		defaultTableModelProductos.addColumn("Proveedor");
		defaultTableModelProductos.addColumn("Cantidad");
		
		cargarDatosProductos();
	}
	
	public void cargarDatosProductos() {
		productosTable.setDefaultRenderer(Object.class, new RenderImagen()); //Permite visualizar las imagenes en la tabla
		Object datos[] = new Object[7];
		
		//Traer los datos
		Conexion conexion = new Conexion();
		InformeVentasBD informeVentasBD = new InformeVentasBD( conexion.conectar() );
		List<ProductoCompra> listaProductos = informeVentasBD.informeProductos( marcaCb.getSelectedItem().toString(),
					proveedorCb.getSelectedItem().toString(), 
					tipoCb.getSelectedItem().toString());
		
		int imgWidth = 100;
		int imgHeight = 100;
		
		if (listaProductos != null) {
			for ( ProductoCompra producto : listaProductos ) {
				
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
				datos[4] = producto.getMarca().getNombre();
				datos[5] = producto.getMarca().getNombreProveedor();
				datos[6] = producto.getCantidad();

				defaultTableModelProductos.addRow(datos);
			}
			
			productosTable.setModel(defaultTableModelProductos);
		
			productosTable.getColumnModel().getColumn(0).setMaxWidth( imgWidth );
			productosTable.getColumnModel().getColumn(0).setMinWidth( imgWidth );
			productosTable.getColumnModel().getColumn(0).setPreferredWidth( imgWidth );
			
			productosTable.setRowHeight( imgHeight );
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == informeVentasBtn) {
			
			recuperarDatosVentas();
			
		} else if (e.getSource() == informeProductosBtn ) {
			
			recuperarDatosProductos();
			
		} else if (e.getSource() == mostrarTicketBtn) {
			
			try {
				Conexion conexion = new Conexion();
				TicketBD ticketBD = new TicketBD( conexion.conectar() );
				Venta venta = ticketBD.informacionVenta( (int) ventasTable.getValueAt( ventasTable.getSelectedRow(),  0));
				
				Ticket ticket = new Ticket(venta);
		        ticket.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		        ticket.setSize( 400, 900 );
		        ticket.setLocationRelativeTo(null);
		        ticket.setResizable(false);
		        ticket.setVisible(true);
		        
			}catch (ArrayIndexOutOfBoundsException ex) {
				JOptionPane.showMessageDialog(null, "Selecciona la venta para mostrar el ticket", "Seleccion Venta", JOptionPane.WARNING_MESSAGE);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
