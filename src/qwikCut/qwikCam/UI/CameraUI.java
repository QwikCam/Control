package qwikCut.qwikCam.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class CameraUI
{

	private JFrame frmQwikcamControl;
	private JTextField ipTextField;
	private JTextField usernameInput;
	private JLabel passwordLabel;
	private JPasswordField passwordField;

	/**
	 * Create the application.
	 */
	public CameraUI()
	{
		initialize();
		frmQwikcamControl.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmQwikcamControl = new JFrame();
		frmQwikcamControl.setTitle("QwikCam Control - Camera Selection");
		frmQwikcamControl.setBounds(100, 100, 344, 300);
		frmQwikcamControl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmQwikcamControl.getContentPane().setLayout(springLayout);
		
		JLabel ipLabel = new JLabel("Camera IP");
		springLayout.putConstraint(SpringLayout.NORTH, ipLabel, 10, SpringLayout.NORTH, frmQwikcamControl.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, ipLabel, 10, SpringLayout.WEST, frmQwikcamControl.getContentPane());
		frmQwikcamControl.getContentPane().add(ipLabel);
		
		ipTextField = new JTextField();
		ipTextField.setText("Enter IPv4");
		springLayout.putConstraint(SpringLayout.NORTH, ipTextField, 3, SpringLayout.SOUTH, ipLabel);
		springLayout.putConstraint(SpringLayout.WEST, ipTextField, 0, SpringLayout.WEST, ipLabel);
		frmQwikcamControl.getContentPane().add(ipTextField);
		ipTextField.setColumns(10);
		
		usernameInput = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, usernameInput, 32, SpringLayout.SOUTH, ipTextField);
		springLayout.putConstraint(SpringLayout.WEST, usernameInput, 0, SpringLayout.WEST, ipLabel);
		usernameInput.setText("Username");
		usernameInput.setColumns(10);
		frmQwikcamControl.getContentPane().add(usernameInput);
		
		JLabel usernameLabel = new JLabel("Camera Username");
		springLayout.putConstraint(SpringLayout.NORTH, usernameLabel, 15, SpringLayout.SOUTH, ipTextField);
		springLayout.putConstraint(SpringLayout.WEST, usernameLabel, 0, SpringLayout.WEST, ipLabel);
		frmQwikcamControl.getContentPane().add(usernameLabel);
		
		passwordLabel = new JLabel("Camera Password");
		springLayout.putConstraint(SpringLayout.NORTH, passwordLabel, 6, SpringLayout.SOUTH, usernameInput);
		springLayout.putConstraint(SpringLayout.WEST, passwordLabel, 0, SpringLayout.WEST, ipLabel);
		frmQwikcamControl.getContentPane().add(passwordLabel);
		
		passwordField = new JPasswordField();
		springLayout.putConstraint(SpringLayout.NORTH, passwordField, 6, SpringLayout.SOUTH, passwordLabel);
		springLayout.putConstraint(SpringLayout.WEST, passwordField, 10, SpringLayout.WEST, frmQwikcamControl.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, passwordField, 0, SpringLayout.EAST, ipTextField);
		passwordField.setHorizontalAlignment(SwingConstants.LEFT);
		frmQwikcamControl.getContentPane().add(passwordField);
		
		JButton verifyButton = new JButton("Verify");
		springLayout.putConstraint(SpringLayout.NORTH, verifyButton, 16, SpringLayout.SOUTH, passwordField);
		springLayout.putConstraint(SpringLayout.WEST, verifyButton, 0, SpringLayout.WEST, ipLabel);
		frmQwikcamControl.getContentPane().add(verifyButton);
		
		JButton confirmBtn = new JButton("Confirm and Close");
		springLayout.putConstraint(SpringLayout.NORTH, confirmBtn, 6, SpringLayout.SOUTH, verifyButton);
		springLayout.putConstraint(SpringLayout.WEST, confirmBtn, 0, SpringLayout.WEST, ipLabel);
		frmQwikcamControl.getContentPane().add(confirmBtn);
		
		JButton cancelBtn = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.NORTH, cancelBtn, 6, SpringLayout.SOUTH, confirmBtn);
		springLayout.putConstraint(SpringLayout.WEST, cancelBtn, 0, SpringLayout.WEST, ipLabel);
		cancelBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				frmQwikcamControl.dispose();
			}
		});
		frmQwikcamControl.getContentPane().add(cancelBtn);
	}
}
