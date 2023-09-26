package com.system.swing;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MessageLabel extends JLabel {
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT = 0;
	public static final int SUCCESSFUL = 1;
	public static final int INFORMATION = 2;
	public static final int ERROR = 3;
	private static Color defaultColor = new Color(0xC2C2C2);
	private static Color succesfulColor = new Color(0x26C15A);
	private static Color informationColor = new Color(0x2475DE);
	private static Color errorColor = new Color(0xDE2724);
	private Color backgroundColor;
	private String information;
	private int typeMessage;
	private int radius = 0;
	
	public MessageLabel() {
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		this.backgroundColor = new Color(0xFFFFFF);
	}
	
	public MessageLabel(String information, int typeMessage) {
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		setText(information);
		
		switch(typeMessage) {
		case 0:
			this.backgroundColor = defaultColor;
			break;
		case 1:
			this.backgroundColor = succesfulColor;
			break;
		case 2:
			this.backgroundColor = informationColor;
			break;
		case 3:
			this.backgroundColor = errorColor;
			break;
		default:
			this.backgroundColor = new Color(0xFFFFFF);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(this.backgroundColor);
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
		super.paintComponent(g);
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
		setText(information);
	}

	public int getTypeMessage() {
		return typeMessage;
	}

	public void setTypeMessage(int typeMessage) {
		this.typeMessage = typeMessage;
		switch(typeMessage) {
		case 0:
			this.backgroundColor = defaultColor;
			break;
		case 1:
			this.backgroundColor = succesfulColor;
			break;
		case 2:
			this.backgroundColor = informationColor;
			break;
		case 3:
			this.backgroundColor = errorColor;
			break;
		default:
			this.backgroundColor = new Color(0xFFFFFF);
		}
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		repaint();
	}
	
	
	
	
}
