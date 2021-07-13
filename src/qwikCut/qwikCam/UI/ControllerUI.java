package qwikCut.qwikCam.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

import qwikCut.qwikCam.Runner.ControllerInterface;
import javax.swing.JSlider;
import javax.swing.JLabel;
import java.awt.Font;

public class ControllerUI
{
	private final Action action = new Confirm();
	private ControllerInterface ctrlInterface;
	private JComboBox<String> xMovementDrop, yMovementDrop, zMovementDrop;

	private JFrame frmControllerSettings;
	private JSlider slider;
	
	private int[] readLinearity;
	private int readDeadzone;

	/**
	 * Create the application.
	 */
	public ControllerUI(ControllerInterface ctrlInterface)
	{
		this.ctrlInterface = ctrlInterface;
		this.readLinearity = ctrlInterface.getLinearity();
		this.readDeadzone = ctrlInterface.getDeadzone();
		initialize();
		frmControllerSettings.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmControllerSettings = new JFrame();
		frmControllerSettings.setTitle("QwikCam Control - Controller Settings");
		frmControllerSettings.setBounds(100, 100, 500, 300);
		frmControllerSettings.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmControllerSettings.getContentPane().setLayout(springLayout);
		
		URL iconURL = getClass().getResource("/qwikCut/qwikCam/UI/logo.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frmControllerSettings.setIconImage(icon.getImage());

		xMovementDrop = new JComboBox<String>();
		springLayout.putConstraint(SpringLayout.NORTH, xMovementDrop, 45, SpringLayout.NORTH, frmControllerSettings.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, xMovementDrop, 10, SpringLayout.WEST, frmControllerSettings.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, xMovementDrop, -395, SpringLayout.EAST, frmControllerSettings.getContentPane());
		frmControllerSettings.getContentPane().add(xMovementDrop);
		xMovementDrop.addItem("Linear");
		xMovementDrop.addItem("Exponential");
		xMovementDrop.setSelectedIndex(readLinearity[0]);

		zMovementDrop = new JComboBox<String>();
		springLayout.putConstraint(SpringLayout.WEST, zMovementDrop, 10, SpringLayout.WEST, frmControllerSettings.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, zMovementDrop, -77, SpringLayout.SOUTH, frmControllerSettings.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, zMovementDrop, 0, SpringLayout.EAST, xMovementDrop);
		frmControllerSettings.getContentPane().add(zMovementDrop);
		zMovementDrop.addItem("Linear");
		zMovementDrop.addItem("Exponential");
		zMovementDrop.setSelectedIndex(readLinearity[2]);

		JButton cancelBtn = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.SOUTH, cancelBtn, 0, SpringLayout.SOUTH, frmControllerSettings.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, cancelBtn, 0, SpringLayout.EAST, frmControllerSettings.getContentPane());
		frmControllerSettings.getContentPane().add(cancelBtn);
		cancelBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				frmControllerSettings.dispose();
			}
		});

		yMovementDrop = new JComboBox<String>();
		springLayout.putConstraint(SpringLayout.NORTH, yMovementDrop, 103, SpringLayout.NORTH, frmControllerSettings.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, zMovementDrop, 39, SpringLayout.SOUTH, yMovementDrop);
		springLayout.putConstraint(SpringLayout.WEST, yMovementDrop, 10, SpringLayout.WEST, frmControllerSettings.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, yMovementDrop, 0, SpringLayout.EAST, xMovementDrop);
		yMovementDrop.setModel(new DefaultComboBoxModel<String>(new String[] { "Linear", "Exponential" }));
		frmControllerSettings.getContentPane().add(yMovementDrop);
		yMovementDrop.setSelectedIndex(readLinearity[1]);

		JButton closeBtn = new JButton("Confirm & Close");
		closeBtn.setAction(action);
		springLayout.putConstraint(SpringLayout.SOUTH, closeBtn, 0, SpringLayout.SOUTH, frmControllerSettings.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, closeBtn, -24, SpringLayout.WEST, cancelBtn);
		frmControllerSettings.getContentPane().add(closeBtn);
		
		slider = new JSlider();
		slider.setMinorTickSpacing(2);
		slider.setMajorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		System.out.println(readDeadzone);
		slider.setValue(readDeadzone);
		springLayout.putConstraint(SpringLayout.NORTH, slider, 14, SpringLayout.NORTH, yMovementDrop);
		springLayout.putConstraint(SpringLayout.EAST, slider, -89, SpringLayout.EAST, frmControllerSettings.getContentPane());
		frmControllerSettings.getContentPane().add(slider);
		
		JLabel lblNewLabel = new JLabel("Deadzone Sensitivity");
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel, -15, SpringLayout.NORTH, slider);
		springLayout.putConstraint(SpringLayout.EAST, lblNewLabel, -141, SpringLayout.EAST, frmControllerSettings.getContentPane());
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		frmControllerSettings.getContentPane().add(lblNewLabel);
	}

	@SuppressWarnings("serial")
	private class Confirm extends AbstractAction
	{
		public Confirm()
		{
			putValue(NAME, "Confirm & Close");
		}

		public void actionPerformed(ActionEvent E)
		{
			// THIS IS WHERE I SEND STUFF
			String selectionX = (String) xMovementDrop.getSelectedItem();
			String selectionY = (String) yMovementDrop.getSelectedItem();
			String selectionZ = (String) zMovementDrop.getSelectedItem();
			int selection[] = new int[3];
			if (selectionX.equalsIgnoreCase("Linear"))
			{
				selection[0] = 0;
			} else
			{
				selection[0] = 1;
			}
			if (selectionY.equalsIgnoreCase("Linear"))
			{
				selection[1] = 0;
			} else
			{
				selection[1] = 1;
			}
			if (selectionZ.equalsIgnoreCase("Linear"))
			{
				selection[2] = 0;
			} else
			{
				selection[2] = 1;
			}
			ctrlInterface.setLinearity(selection);
			System.out.println(slider.getValue());
			ctrlInterface.setDeadzone(slider.getValue());

			frmControllerSettings.dispose();
		}
	}
}
