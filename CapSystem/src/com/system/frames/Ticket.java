package com.system.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.system.objetos.ProductoCompra;
import com.system.objetos.Venta;

public class Ticket extends JFrame {
	private static final long serialVersionUID = 1L;
	private int total;
	private JPanel ticketPanel;
	private JPanel cuerpoPanel;
	private JPanel encabezado;
	private JLabel titulo;
	private JLabel fechalbl;
	private JLabel lblHora;
	private JLabel fechaValorlbl;
	private JLabel horaValorlbl;
	private JScrollPane scrollPane;
	private JList<ProductoCompra> listaProductos;
	private JPanel piePanel;
	private final JLabel totallbl = new JLabel("Total:");
	private JLabel totalValorlbl;
	
	public Ticket( Venta venta ) {
		ImageIcon logoIcon = new ImageIcon("images/cap_logo.png");
		setIconImage( logoIcon.getImage() );
		setResizable(false);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		ticketPanel = new JPanel();
		getContentPane().add(ticketPanel, BorderLayout.CENTER);
		ticketPanel.setLayout(new BorderLayout(0, 0));
		
		cuerpoPanel = new JPanel();
		ticketPanel.add(cuerpoPanel, BorderLayout.CENTER);
		cuerpoPanel.setLayout(new BorderLayout(0, 0));
		
		encabezado = new JPanel();
		encabezado.setBackground(new Color(255, 255, 255));
		encabezado.setPreferredSize(new Dimension(10, 100));
		cuerpoPanel.add(encabezado, BorderLayout.NORTH);
		encabezado.setLayout(null);
		
		titulo = new JLabel("Ticket");
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		titulo.setFont(new Font("Century Gothic", Font.BOLD, 25));
		titulo.setBounds(108, 11, 200, 38);
		encabezado.add(titulo);
		
		fechalbl = new JLabel("Fecha:");
		fechalbl.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		fechalbl.setBounds(33, 75, 46, 14);
		encabezado.add(fechalbl);
		
		lblHora = new JLabel("Hora:");
		lblHora.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		lblHora.setBounds(260, 75, 46, 14);
		encabezado.add(lblHora);
		
		fechaValorlbl = new JLabel( venta.getFecha_venta() );
		fechaValorlbl.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		fechaValorlbl.setBounds(89, 75, 186, 14);
		encabezado.add(fechaValorlbl);
		
		horaValorlbl = new JLabel( venta.getHora_venta());
		horaValorlbl.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		horaValorlbl.setBounds(316, 75, 82, 14);
		encabezado.add(horaValorlbl);
		
		scrollPane = new JScrollPane();
		cuerpoPanel.add(scrollPane, BorderLayout.CENTER);
		
		listaProductos = new JList<>();
		listaProductos.setEnabled(false);
		scrollPane.setViewportView(listaProductos);
		
		total = 0;
		DefaultListModel<ProductoCompra> listaProductosModel = new DefaultListModel<>();
		for (ProductoCompra producto : venta.getListaProductos()) {
			listaProductosModel.addElement(producto);
			total += (producto.getCantidad() * producto.getPrecio());
		}
		listaProductos.setModel(listaProductosModel);
		
		piePanel = new JPanel();
		piePanel.setBackground(new Color(255, 255, 255));
		piePanel.setPreferredSize(new Dimension(10, 50));
		ticketPanel.add(piePanel, BorderLayout.SOUTH);
		piePanel.setLayout(null);
		totallbl.setHorizontalAlignment(SwingConstants.TRAILING);
		totallbl.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		totallbl.setBounds(207, 11, 63, 31);
		piePanel.add(totallbl);
		
		totalValorlbl = new JLabel( "" + total + "");
		totalValorlbl.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		totalValorlbl.setBounds(280, 11, 144, 31);
		piePanel.add(totalValorlbl);
	}
}