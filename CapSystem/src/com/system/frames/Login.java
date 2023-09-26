package com.system.frames;
import com.system.components.*;
import com.system.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class Login extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private final Color primaryColor = new Color(0x475471);
    private final Color secondaryColor = new Color(0x475490);
    private final Color bgColor = new Color(0xFFFFFF);
    
    private JPanel bgPanel;
    private GradientPanel topPanel;
    private JPanel mainPanel;
	private JPanel contentPanel;
    private JPanel messagePanel;
    
    private JLabel loginLabel;
    private JLabel userLabel;
    private JLabel passwordLabel;
    private MessageLabel messageLabel;
    
    private CustomTextField userField;
    private CustomPasswordField passwordField;
    private CustomButton loginButton;
    
    public Login(){
        super("Iniciar Sesión");
        initComponents();
    }

    public Login(String nameForm){
        super(nameForm);
        
        initComponents();
    }

    private void initComponents() {  
    	//Caracteristicas de JFrame
    	setMinimumSize(new Dimension(450,800));
        setSize(450, 800);
        
        //Panel Superior
    	topPanel = new GradientPanel(primaryColor, secondaryColor);
    	topPanel.setPreferredSize(new Dimension(0,100));
    	
    	//Panel Principal
        loginLabel = new JLabel("Inicio de Sesión");
        loginLabel.setBounds(44, 54, 314, 50);
        loginLabel.setFont(new Font("Century Gothic", Font.BOLD, 40));
        loginLabel.setForeground(primaryColor);

        userLabel = new JLabel("Cuenta de usuario");
        userLabel.setBounds(23, 139, 211, 26);
        userLabel.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        userLabel.setForeground(new Color(0x000000));

        userField = new CustomTextField(primaryColor,secondaryColor);
        userField.setBounds(23, 168, 356, 40);
        userField.setPrefixIcon(new ImageIcon("images/user_icon.png"));
        userField.setToolTipText("Cuenta de usuario");

        passwordLabel = new JLabel("Contraseña de usuario");
        passwordLabel.setBounds(23, 232, 242, 26);
        passwordLabel.setFont(new Font("Century Gothic", Font.PLAIN, 20));
        passwordLabel.setForeground(new Color(0x000000));

        passwordField = new CustomPasswordField(primaryColor,secondaryColor);
        passwordField.setBounds(23, 263, 356, 40);
        passwordField.setPrefixIcon(new ImageIcon("images/password_icon.png"));
        passwordField.setToolTipText("Contraseña de usuario");
        
        loginButton = new CustomButton(primaryColor, secondaryColor);
        loginButton.setIcon(new ImageIcon("images/loginButton_icon.png"));
        loginButton.setBounds(23, 364, 356, 67);
        loginButton.setForeground(new Color(0xFFFFFF));
        loginButton.setFont(new Font("Century Gothic", Font.BOLD, 14));
        loginButton.addActionListener(this);

        contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(400, 600));
        contentPanel.setBackground(bgColor);
        contentPanel.setLayout(null);
        contentPanel.add(loginLabel);
        contentPanel.add(userLabel);
        contentPanel.add(userField);
        contentPanel.add(passwordLabel);
        contentPanel.add(passwordField);
        contentPanel.add(loginButton);
        
        mainPanel = new JPanel();
        mainPanel.setOpaque(true);
        mainPanel.setBackground(bgColor);
        mainPanel.setLayout(new MigLayout("al center center, wrap, gapy 20", "[]", "[]"));
        mainPanel.add(contentPanel, "cell 0 0,alignx center,aligny center");
    	
      //Panel de Mensajes
        messageLabel = new MessageLabel("Bienvenido", 0);
        messageLabel.setFont(new Font("Centur Gothic", Font.PLAIN, 20));
        messageLabel.setPreferredSize(new Dimension(0,50));
        messageLabel.setForeground(bgColor);
        messageLabel.setBackgroundColor(primaryColor);
        
        messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.setBackground(bgColor);
        messagePanel.add(messageLabel, BorderLayout.CENTER);
  
        //Panel Contenedor
        bgPanel = new JPanel();
        bgPanel.setLayout(new BorderLayout());
        bgPanel.add(topPanel, BorderLayout.NORTH);
        bgPanel.add(mainPanel, BorderLayout.CENTER);
        bgPanel.add(messagePanel, BorderLayout.SOUTH);
        add(bgPanel);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginButton) {
			
			if (userField.getText().equals("Argenis08") && String.valueOf(passwordField.getPassword()).equals("191123") ) {
				 dispose(); //Close Login form
				
				 JFrame menuFrame = new Menu();
				 ImageIcon imageIcon = new ImageIcon("images/cap_icon.png");
				 menuFrame.setIconImage(imageIcon.getImage());
			        
		        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        menuFrame.setVisible(true); //Open Menu Form
		        menuFrame.setLocationRelativeTo(null);
		   
				
			} else {
				
				messageLabel.setInformation("Error en sesión");
				messageLabel.setTypeMessage(3);
				
			}
		} 
	}
}
