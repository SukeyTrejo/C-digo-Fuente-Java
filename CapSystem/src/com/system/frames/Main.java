package com.system.frames;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args){
    	//Cambiar estilo de proyecto
    	try { 
    	    UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); 
    	} catch (Exception ex) { 
    	    ex.printStackTrace(); 
    	}
    	
    	//Iniciar proyecto
        JFrame login = new Login("Sesión");
        ImageIcon imageIcon = new ImageIcon("images/key_icon.png");
        login.setIconImage(imageIcon.getImage());
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //login.setResizable(false);
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }
}
