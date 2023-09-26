package com.system.frames;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.system.components.*;
import com.system.panels.*;
import com.system.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu extends JFrame  implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private final Color primaryColor = new Color(0x475471);
    private final Color secondaryColor = new Color(0x475A90);
    private final Color bgColor = new Color(0xFFFFFF);
    
    private final Color overButtonColor = new Color(0xD6D9DF);
    private final Color bgButtonColor = new Color(0x404040);
    private final Color overTextButtonColor = new Color(0x000000);
    private final Color bgTextButtonColor = new Color(0xffffff);
    
	private JPanel bgPanel;
	private GradientPanel topPanel;
	private JPanel actionPanel;
	private JPanel leftActionPanel;
	private JButton ventaBtn;
	private JButton productosBtn;
	private JButton informeBtn;
	private JButton proveedoresBtn;
	private JButton marcasBtn;
	private JPanel rightActionPanel;
	private ImageIcon cerrarSesionIcon = new ImageIcon("images/logout_img.png");
	private JButton cerrarSesionBtn;
	private JPanel mainPanel;
	
	private JLabel logoLabel;
	private final String logoName = "CAPS A&D";
	
	//Constructores
	public Menu() {
		super("Menu");
		initComponents();
	}
	
	public Menu(String frameName) {
		super( frameName );
		initComponents();
	}
	
	//Métodos
	private void initComponents() {
		//Cracterísticas del JFrame
		setMinimumSize(new Dimension(950, 700));
	    setSize( 965, 740);
  		
	    //Panel Superior
	    ImageIcon iconoTitulo = new ImageIcon("images/cap_logo.png");
		Image imagenTitulo= iconoTitulo.getImage();
		Image redimensionTitulo = imagenTitulo.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		iconoTitulo = new ImageIcon(redimensionTitulo);
		
  		logoLabel = new JLabel(); //Logo de la empresa
  		logoLabel.setText(logoName);
  		logoLabel.setFont(new Font("Verdana", Font.BOLD, 30));
  		logoLabel.setForeground(bgColor);
  		logoLabel.setIcon(iconoTitulo);
  		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
  		
  		//Subpanel de botones
  		ventaBtn = new RegularButton(overButtonColor, bgButtonColor, overTextButtonColor, bgTextButtonColor);
  		ventaBtn.setFont(new Font("Century Gothic", Font.BOLD, 13));
  		ventaBtn.setText("VENTA");
  		ventaBtn.addActionListener(this);
  		ventaBtn.setPreferredSize(new Dimension(150,50));
  		
  		productosBtn = new RegularButton(overButtonColor, bgButtonColor, overTextButtonColor, bgTextButtonColor);
  		productosBtn.setFont(new Font("Century Gothic", Font.BOLD, 13));
  		productosBtn.setText("PRODUCTOS");
  		productosBtn.addActionListener(this);
  		productosBtn.setPreferredSize(new Dimension(150,50));
  		
  		informeBtn = new RegularButton(overButtonColor, bgButtonColor, overTextButtonColor, bgTextButtonColor);
  		informeBtn.setFont(new Font("Century Gothic", Font.BOLD, 13));
  		informeBtn.setText("INFORMES");
  		informeBtn.addActionListener(this);
  		informeBtn.setPreferredSize(new Dimension(150,50));
  		
  		proveedoresBtn = new RegularButton(overButtonColor, bgButtonColor, overTextButtonColor, bgTextButtonColor);
  		proveedoresBtn.setFont(new Font("Century Gothic", Font.BOLD, 13));
  		proveedoresBtn.setText("PROVEEDORES");
  		proveedoresBtn.addActionListener(this);
  		proveedoresBtn.setPreferredSize(new Dimension(150,50));
  		
  		marcasBtn = new RegularButton(overButtonColor, bgButtonColor, overTextButtonColor, bgTextButtonColor);
  		marcasBtn.setFont(new Font("Century Gothic", Font.BOLD, 13));
  		marcasBtn.addActionListener(this);
  		marcasBtn.setText("MARCAS");
  		marcasBtn.setPreferredSize(new Dimension(150,50));
  		
  		leftActionPanel = new JPanel();
  		leftActionPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 0 ,0));
  		leftActionPanel.setBackground(bgButtonColor);
  		leftActionPanel.add(ventaBtn);
  		leftActionPanel.add(productosBtn);
  		leftActionPanel.add(informeBtn);
  		leftActionPanel.add(proveedoresBtn);
  		leftActionPanel.add(marcasBtn);
  		
  		cerrarSesionBtn = new RegularButton(overButtonColor, bgButtonColor, overTextButtonColor, bgTextButtonColor);
  		cerrarSesionBtn.setIcon(cerrarSesionIcon);
  		cerrarSesionBtn.addActionListener(this);
  		cerrarSesionBtn.setPreferredSize(new Dimension(50,50));
  		
  		rightActionPanel = new JPanel();
  		rightActionPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 0 ,0));
  		rightActionPanel.setBackground(bgButtonColor);
  		rightActionPanel.add(cerrarSesionBtn);
  		
  		actionPanel = new JPanel();
  		actionPanel.setBackground(bgButtonColor);

  		topPanel = new GradientPanel(primaryColor, secondaryColor);
  		topPanel.setLayout(new BorderLayout());
  		topPanel.setPreferredSize(new Dimension(0, 150));
  		topPanel.add(logoLabel, BorderLayout.CENTER);
  		topPanel.add(actionPanel, BorderLayout.SOUTH);
  		GroupLayout gl_actionPanel = new GroupLayout(actionPanel);
  		gl_actionPanel.setHorizontalGroup(
  			gl_actionPanel.createParallelGroup(Alignment.LEADING)
  				.addGroup(gl_actionPanel.createSequentialGroup()
  					.addGap(0)
  					.addComponent(leftActionPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
  					.addComponent(rightActionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
  					.addGap(0))
  		);
  		gl_actionPanel.setVerticalGroup(
  			gl_actionPanel.createParallelGroup(Alignment.LEADING)
  				.addGroup(gl_actionPanel.createSequentialGroup()
  					.addGap(0)
  					.addGroup(gl_actionPanel.createParallelGroup(Alignment.LEADING)
  						.addComponent(leftActionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
  						.addComponent(rightActionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
  		);
  		actionPanel.setLayout(gl_actionPanel);

  		//Panel Principal
  		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(new Color(0xff0000));
	   
  		//Panel Contenedor
  		bgPanel = new JPanel();
  		bgPanel.setLayout(new BorderLayout());
  		bgPanel.setBackground(bgColor);
  		bgPanel.add(topPanel, BorderLayout.NORTH);
  		bgPanel.add(mainPanel, BorderLayout.CENTER);
  		
  		getContentPane().add(bgPanel, BorderLayout.CENTER);
  		
  		//Mostrar el panel de ventas dentro del mainPanel
	    cambiarPanel(new VentasPanel(), mainPanel.getSize());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ventaBtn) {
		    cambiarPanel(new VentasPanel(), mainPanel.getSize());
		    
		} else if (e.getSource() == productosBtn ) {
		    cambiarPanel(new ProductosPanel(), mainPanel.getSize());
		    
		} else if (e.getSource() == informeBtn) {
		    cambiarPanel(new InformePanel(), mainPanel.getSize());
		    
		} else if (e.getSource() == proveedoresBtn) {
		    cambiarPanel(new ProveedoresPanel(), mainPanel.getSize());
		    
		} else if (e.getSource() == marcasBtn){
		    cambiarPanel(new MarcasPanel(), mainPanel.getSize());
			 
		} else if (e.getSource() == cerrarSesionBtn) {
		    //Cerrar panel de menú
			dispose();
			
			//Morar panel de Inicio de Sesión
			JFrame login = new Login("Sesión");
	        ImageIcon imageIcon = new ImageIcon("images/key_icon.png");
	        login.setIconImage(imageIcon.getImage());
	        
	        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        //login.setResizable(false);
	        login.setVisible(true);
	        login.setLocationRelativeTo(null);
	        
		} 
	}
	
	public void cambiarPanel(JPanel nuevoPanel, Dimension size) {
		JPanel panel = nuevoPanel;
		panel.setSize(size);
		panel.setLocation(0,0);
		
		mainPanel.removeAll();
		mainPanel.add(panel, BorderLayout.CENTER);
		mainPanel.revalidate();
		mainPanel.repaint();
		
	}
	
}
