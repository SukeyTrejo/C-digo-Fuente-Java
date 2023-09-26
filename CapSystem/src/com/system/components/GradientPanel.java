package com.system.components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GradientPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Color primaryColor = new Color(0xFFF);
	private Color secondaryColor = new Color(0xFFF);
	
	public GradientPanel(Color primaryColor, Color secondaryColor) {
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int width = getWidth();
		int height = getHeight();
		
		GradientPaint gp = new GradientPaint(width/2, 0,this.primaryColor, width/2, height, this.secondaryColor);
		g2.setPaint(gp);
		g2.fillRect(0, 0, width, height);
		
	}

	public Color getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(Color primaryColor) {
		this.primaryColor = primaryColor;
		repaint();
	}

	public Color getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(Color secondaryColor) {
		this.secondaryColor = secondaryColor;
		repaint();
	}
	
	
	
}
