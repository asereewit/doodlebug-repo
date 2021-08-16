/*
 * Name: Jaydee Sereewit
 * Class: CPTR241
 * Assignment: Final Project 
 * Date: March 15, 2018
 * Description: This program is a GUI predator(doodlebug) and pray(ant) simulation.
 * Result: This program works as expected 
 */
package hw20;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;

public class Simulation implements KeyListener{
	JLabel[][] jlabs = new JLabel[World.cellWidth][World.cellLength];
	ImageIcon antIcon = new ImageIcon("ant.jpg");
	ImageIcon bugIcon = new ImageIcon("bug.jpg");
	World predPraySimulation = new World(World.cellLength*World.cellWidth-150, 150);
	
	Simulation() {
		JFrame simulationfrm = new JFrame("Simulation");
		simulationfrm.setLayout(new GridLayout(20,20,1,1)); 
		simulationfrm.setSize(840, 820); // the icons are 42x41. 
		simulationfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		simulationfrm.addKeyListener(this);
		
		// create labels
		for(int i=0; i<20; i++) {
			for(int j=0; j<20; j++) {
				jlabs[i][j]=new JLabel();
			}
		}
		
		// add  border
		Border border = BorderFactory.createEtchedBorder();
		for(int i=0; i<20; i++) {
			for(int j=0; j<20; j++) {
				jlabs[i][j].setBorder(border);
			}
		}
		
		// add labels to frame
		for(int i=0; i<20; i++) {
			for(int j=0; j<20; j++) {
				simulationfrm.add(jlabs[i][j]);
			}
		}
		
		updateSimulationGUI(); // shows the world initialized graphically. 
		simulationfrm.setVisible(true);
	}	
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			predPraySimulation.elapse(); //every time Enter is pressed, simulation moves one time step.
			updateSimulationGUI(); //update the world graphically.
		}
	}
	
	public void keyReleased(KeyEvent e) {
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	// this methods check the 2D array of cells are occupied by doodlebugs, ants or neither. 
	public void updateSimulationGUI() {
	for(int i=0; i<World.cellWidth; i++) {
			for(int j=0; j<World.cellLength; j++) {
				if(predPraySimulation.world[i][j].getOccupiedByAnt() && !predPraySimulation.world[i][j].getOccupiedByDoodlebug()) { //if a cell is occupied by ant, update the labels icon with ant icon
					jlabs[i][j].setIcon(antIcon);
				} else if(predPraySimulation.world[i][j].getOccupiedByDoodlebug() && !predPraySimulation.world[i][j].getOccupiedByAnt()) { // if a cell is occupied by doodlebug, update the label icon with dooldebug icon
					jlabs[i][j].setIcon(bugIcon);
				} else { 
					jlabs[i][j].setIcon(null); // if neither, set icon to null. 
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Simulation();
			}
		});
		
	}

}
