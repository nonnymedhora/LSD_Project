/**
 * 
 */
package org.bawaweb.lsd.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class LSDFrame extends JFrame {

	private static final long serialVersionUID = -3311734860577681273L;

	private LSDPanel 			lsdPanel;

	private JComboBox<Integer> 	dimensionBox;
	private final Integer[] 	dimensionChoiceArr	=	new Integer[] {	2,3,4,5,6,7,8,9,10,11 };
	
	private int 				selectedDimension;// = 5;

	public LSDFrame(int size) {
		/*	centering commented
		 Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);*/
		this.selectedDimension = size;
//		this.setLocation(500,200);		// using setLocationRelativeTo(null) [ after pack in initComponents ] instead
		initComponents();
	}

	private void initComponents() {
		
		Container cp;
		cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

	    cp.add(new JLabel("BaWaZ LSD: "), BorderLayout.NORTH);
	    
	    this.lsdPanel = new LSDPanel(this.selectedDimension);

	    cp.add(this.lsdPanel, BorderLayout.CENTER);
		
		this.dimensionBox = new JComboBox<Integer>(dimensionChoiceArr);
	    this.dimensionBox.setSelectedItem(this.selectedDimension);
	    
	    this.dimensionBox.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		int selected = (int) dimensionBox.getSelectedItem(); 
	    		regenerate(selected);
	    	}
	    });
	    
	    cp.add(this.dimensionBox, BorderLayout.SOUTH);

		this.pack();
		this.setLocationRelativeTo(null);
	}
	protected void regenerate(int newDim) {
		// haven't figured out how to repaint/refresh
		// the panel in-place	
		//	updateUI,validate,update,repaint have'nt worked
//		this.lsdPanel = new LSDPanel(newDim);this.lsdPanel.repaint();
//		revalidate();	//repaint(); //updateUI();	
		
		// so using this hack
		this.dispose();
		generateLSDFrame(newDim);
				
	}

	public LSDPanel getLsdPanel() {
		return lsdPanel;
	}

	public void setLsdPanel(LSDPanel lsdPanel) {
		this.lsdPanel = lsdPanel;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				generateLSDFrame(5);
			}
		});

	}

	private static void generateLSDFrame(int dim) {
		final LSDFrame frame = new LSDFrame(dim);
		frame.setTitle("Latin Squares");
		frame.setSize(420, 420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}

}
