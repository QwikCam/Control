package qwikCut.qwikCam.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

import qwikCut.qwikCam.Runner.ControllerInterface;

public class ControllerUI
{
	private final Action action = new Confirm(); 
	private ControllerInterface ctrlInterface;
	private JComboBox xMovementDrop,yMovementDrop,zMovementDrop;

	private JFrame frame;
	/**
	 * Create the application.
	 */
	public ControllerUI(ControllerInterface ctrlInterface)
	{
		this.ctrlInterface= ctrlInterface;
		initialize();
		frame.setVisible(true);
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JButton xDeadzoneBtn = new JButton("Set Deadzone");
		springLayout.putConstraint(SpringLayout.NORTH, xDeadzoneBtn, 44, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, xDeadzoneBtn, 66, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(xDeadzoneBtn);
		
		JButton yDeadzoneBtn = new JButton("Set Deadzone");
		springLayout.putConstraint(SpringLayout.WEST, yDeadzoneBtn, 0, SpringLayout.WEST, xDeadzoneBtn);
		springLayout.putConstraint(SpringLayout.SOUTH, yDeadzoneBtn, -124, SpringLayout.SOUTH, frame.getContentPane());
		frame.getContentPane().add(yDeadzoneBtn);
		
		JButton zDeadzoneBtn = new JButton("Set Deadzone");
		springLayout.putConstraint(SpringLayout.NORTH, zDeadzoneBtn, 35, SpringLayout.SOUTH, yDeadzoneBtn);
		springLayout.putConstraint(SpringLayout.WEST, zDeadzoneBtn, 0, SpringLayout.WEST, xDeadzoneBtn);
		frame.getContentPane().add(zDeadzoneBtn);
		
		xMovementDrop = new JComboBox();
		springLayout.putConstraint(SpringLayout.NORTH, xMovementDrop, 44, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, xMovementDrop, 71, SpringLayout.EAST, xDeadzoneBtn);
		springLayout.putConstraint(SpringLayout.EAST, xMovementDrop, -169, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(xMovementDrop);
		xMovementDrop.addItem("Linear");
		xMovementDrop.addItem("Exponential");
		
		zMovementDrop = new JComboBox();
		zMovementDrop.setModel(new DefaultComboBoxModel(new String[] {"Linear", "Exponential"}));
		springLayout.putConstraint(SpringLayout.EAST, zMovementDrop, 0, SpringLayout.EAST, xMovementDrop);
		springLayout.putConstraint(SpringLayout.NORTH, zMovementDrop, -22, SpringLayout.SOUTH, zDeadzoneBtn);
		springLayout.putConstraint(SpringLayout.WEST, zMovementDrop, 71, SpringLayout.EAST, zDeadzoneBtn);
		springLayout.putConstraint(SpringLayout.SOUTH, zMovementDrop, 0, SpringLayout.SOUTH, zDeadzoneBtn);
		frame.getContentPane().add(zMovementDrop);
		zMovementDrop.addItem("Linear");
		zMovementDrop.addItem("Exponential");
		
		
		JButton cancelBtn = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.SOUTH, cancelBtn, 0, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, cancelBtn, 0, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
//				System.out.println("test");
				frame.dispose();
			}
		});
		
		yMovementDrop = new JComboBox();
		yMovementDrop.setModel(new DefaultComboBoxModel(new String[] {"Linear", "Exponential"}));
		springLayout.putConstraint(SpringLayout.NORTH, yMovementDrop, 0, SpringLayout.NORTH, yDeadzoneBtn);
		springLayout.putConstraint(SpringLayout.WEST, yMovementDrop, 0, SpringLayout.WEST, xMovementDrop);
		springLayout.putConstraint(SpringLayout.EAST, yMovementDrop, 0, SpringLayout.EAST, xMovementDrop);
		frame.getContentPane().add(yMovementDrop);
		
		JButton closeBtn = new JButton("Confirm & Close");
		closeBtn.setAction(action);
		springLayout.putConstraint(SpringLayout.SOUTH, closeBtn, 0, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, closeBtn, -24, SpringLayout.WEST, cancelBtn);
		frame.getContentPane().add(closeBtn);
		
	}
	private class Confirm extends AbstractAction
	{
		public Confirm()
		{
			putValue(NAME,"Confirm & Close");
		}
		public void actionPerformed(ActionEvent E)
		{
			// THIS IS WHERE I SEND STUFF
			String selectionX = (String) xMovementDrop.getSelectedItem(); 
			String selectionY = (String) yMovementDrop.getSelectedItem(); 
			String selectionZ = (String) zMovementDrop.getSelectedItem(); 
			int selection[] = new int[3];
			if(selectionX.equalsIgnoreCase("Linear"))
			{
				selection[0]=0;
			}
			else
			{
				selection[0]=1;
			}
			if(selectionY.equalsIgnoreCase("Linear"))
			{
				selection[1]=0;
			}
			else
			{
				selection[1]=1;
			}
			if(selectionZ.equalsIgnoreCase("Linear"))
			{
				selection[1]=0;
			}
			else
			{
				selection[1]=1;
			}
			ctrlInterface.setLinearity(selection);
			

			frame.dispose();
		}
	}
}
