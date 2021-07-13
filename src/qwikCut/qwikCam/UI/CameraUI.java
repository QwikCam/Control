package qwikCut.qwikCam.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import qwikCut.qwikCam.Runner.CameraInterface;

public class CameraUI
{

	public static JButton confirmBtn2;
	private JFrame frmQwikcamControl2;
	private JTextField ipTextField;
	private JTextField usernameInput;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private final Action action = new Verify();
	private final Action action_1 = new Confirm();
	
	private CameraInterface camera;

	/**
	 * Create the application.
	 */
	public CameraUI(CameraInterface camera)
	{
		initialize();
		frmQwikcamControl2.setVisible(true);
		this.camera = camera;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmQwikcamControl2 = new JFrame();
		frmQwikcamControl2.setTitle("QwikCam Control - Camera Selection");
		frmQwikcamControl2.setBounds(100, 100, 344, 300);
//		frmQwikcamControl2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmQwikcamControl2.getContentPane().setLayout(springLayout);
		
		URL iconURL = getClass().getResource("/qwikCut/qwikCam/UI/logo.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frmQwikcamControl2.setIconImage(icon.getImage());

		JLabel ipLabel = new JLabel("Camera IP");
		springLayout.putConstraint(SpringLayout.NORTH, ipLabel, 10, SpringLayout.NORTH, frmQwikcamControl2.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, ipLabel, 10, SpringLayout.WEST, frmQwikcamControl2.getContentPane());
		frmQwikcamControl2.getContentPane().add(ipLabel);

		ipTextField = new JTextField();
		ipTextField.setText("Enter IPv4");
		springLayout.putConstraint(SpringLayout.NORTH, ipTextField, 3, SpringLayout.SOUTH, ipLabel);
		springLayout.putConstraint(SpringLayout.WEST, ipTextField, 0, SpringLayout.WEST, ipLabel);
		frmQwikcamControl2.getContentPane().add(ipTextField);
		ipTextField.setColumns(10);

		usernameInput = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, usernameInput, 32, SpringLayout.SOUTH, ipTextField);
		springLayout.putConstraint(SpringLayout.WEST, usernameInput, 0, SpringLayout.WEST, ipLabel);
		usernameInput.setText("Username");
		usernameInput.setColumns(10);
		frmQwikcamControl2.getContentPane().add(usernameInput);

		JLabel usernameLabel = new JLabel("Camera Username");
		springLayout.putConstraint(SpringLayout.NORTH, usernameLabel, 15, SpringLayout.SOUTH, ipTextField);
		springLayout.putConstraint(SpringLayout.WEST, usernameLabel, 0, SpringLayout.WEST, ipLabel);
		frmQwikcamControl2.getContentPane().add(usernameLabel);

		passwordLabel = new JLabel("Camera Password");
		springLayout.putConstraint(SpringLayout.NORTH, passwordLabel, 6, SpringLayout.SOUTH, usernameInput);
		springLayout.putConstraint(SpringLayout.WEST, passwordLabel, 0, SpringLayout.WEST, ipLabel);
		frmQwikcamControl2.getContentPane().add(passwordLabel);

		passwordField = new JPasswordField();
		springLayout.putConstraint(SpringLayout.NORTH, passwordField, 6, SpringLayout.SOUTH, passwordLabel);
		springLayout.putConstraint(SpringLayout.WEST, passwordField, 10, SpringLayout.WEST, frmQwikcamControl2.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, passwordField, 0, SpringLayout.EAST, ipTextField);
		passwordField.setHorizontalAlignment(SwingConstants.LEFT);
		frmQwikcamControl2.getContentPane().add(passwordField);

		JButton verifyButton = new JButton("Verify");
		verifyButton.setAction(action);
		springLayout.putConstraint(SpringLayout.NORTH, verifyButton, 16, SpringLayout.SOUTH, passwordField);
		springLayout.putConstraint(SpringLayout.WEST, verifyButton, 0, SpringLayout.WEST, ipLabel);
		frmQwikcamControl2.getContentPane().add(verifyButton);

		JButton confirmBtn = new JButton("Confirm and Close");
		confirmBtn.setAction(action_1);
		springLayout.putConstraint(SpringLayout.NORTH, confirmBtn, 6, SpringLayout.SOUTH, verifyButton);
		springLayout.putConstraint(SpringLayout.WEST, confirmBtn, 0, SpringLayout.WEST, ipLabel);
		frmQwikcamControl2.getContentPane().add(confirmBtn);
		confirmBtn.setEnabled(false);
		confirmBtn2 = confirmBtn;

		JButton cancelBtn = new JButton("Cancel");
		springLayout.putConstraint(SpringLayout.NORTH, cancelBtn, 6, SpringLayout.SOUTH, confirmBtn);
		springLayout.putConstraint(SpringLayout.WEST, cancelBtn, 0, SpringLayout.WEST, ipLabel);
		cancelBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
//				System.out.println("test");
				frmQwikcamControl2.dispose();
			}
		});
		frmQwikcamControl2.getContentPane().add(cancelBtn);
	}

	@SuppressWarnings("serial")
	private class Verify extends AbstractAction
	{
		public Verify()
		{
			putValue(NAME, "Check Connection");
			putValue(SHORT_DESCRIPTION, "Verify Camera Exists");
		}

		public void actionPerformed(ActionEvent e)
		{
			// ping the given IP address to verify the device can be reached
			String ip = ipTextField.getText();
			String[] noPort = ip.split(":");
			
			// check for a port
			boolean port = true;
			
			if (noPort.length != 2)
			{
				JOptionPane.showMessageDialog(null, "Please specify a port!\n If you do not know the port please\naccess the cameras web page and look\nat the settings or read the instructions!");
				port = false;
			}
			
			// check if the string matches valid IPv4 format
			if (isValid(noPort[0]) && port)
			{
				try
				{
					InetAddress processedIP = InetAddress.getByName(noPort[0]);
					
					if (processedIP.isReachable(5000))
					{
						JOptionPane.showMessageDialog(null, "This IP address can be reached!");
						confirmBtn2.setEnabled(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "This IP address is not reachable!");
					}
				}
				catch (Exception IPError)
				{
					IPError.printStackTrace();
				}
			}
			else if (!isValid(noPort[0]))
			{
				JOptionPane.showMessageDialog(null, "The IP address you entered was not valid!");
			}
		}
	}

	@SuppressWarnings("serial")
	private class Confirm extends AbstractAction
	{
		public Confirm()
		{
			putValue(NAME, "Confirm and close");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e)
		{
			//OnvifDevice nvt = new OnvifDevice("192.168.2.191:8000", "admin", "");
			String ip = ipTextField.getText();
			String username = usernameInput.getText();
			String password = String.valueOf(passwordField.getPassword());
			
			int retval = camera.setConnectionInfo(ip, username, password);
			
			// if the camera connection fails tell the user
			// and do not close
			if (retval == 0)
			{
				JOptionPane.showMessageDialog(null, "The camera did not accept your credentials or \n it does not have the features needed for the software!");
			}
			else
			{
				frmQwikcamControl2.dispose();
			}
		}
	}
	
	private boolean isValid(String ip)
	{
		String[] parts = ip.split("\\.");
//		System.out.println(Arrays.toString(parts));
		
		if (parts.length != 4)
		{
//			System.out.println("length");
			return false;
		}
		
		for (String s : parts)
		{
			try
			{
				int value = Integer.parseInt(s);
				
				if (!((value >= 0) && (value) <= 255))
				{
//					System.out.println("value");
					return false;
				}
			}
			catch (Exception e)
			{
				return false;
			}
		}
		
		return true;
	}
}
