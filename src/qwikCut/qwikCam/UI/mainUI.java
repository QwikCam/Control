package qwikCut.qwikCam.UI;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

public class mainUI
{

	private JFrame frmQwikcamControl;
	public JComboBox<String> ctrlSelect;
	public JLabel ctrlSelectLabel;
	public JLabel panInputLabel;
	public JLabel tiltInputLabel;
	public JLabel zoomLvlLabel;
	public JProgressBar panProgressBar;
	public JProgressBar tiltProgressBar;
	public JProgressBar zoomProgressBar;
	public JButton cameraButton;
	public JButton panDeadzoneBtn;
	public JButton tiltDeadzoneBtn;
	public JButton zoomDeadzoneBrn;
	public JTextPane infoDsp;
	public JButton cameraSettingsBtn;

	/**
	 * Create the application.
	 */
	public mainUI()
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
		frmQwikcamControl.setTitle("QwikCam Control");
		frmQwikcamControl.setBounds(100, 100, 683, 439);
		frmQwikcamControl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmQwikcamControl.getContentPane().setLayout(springLayout);

		ctrlSelect = new JComboBox<>();
		springLayout.putConstraint(SpringLayout.NORTH, ctrlSelect, 48, SpringLayout.NORTH, frmQwikcamControl.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, ctrlSelect, 144, SpringLayout.WEST, frmQwikcamControl.getContentPane());
		ctrlSelect.setToolTipText("Controller Selection");
		frmQwikcamControl.getContentPane().add(ctrlSelect);

		ctrlSelectLabel = new JLabel("Select Controller");
		springLayout.putConstraint(SpringLayout.NORTH, ctrlSelectLabel, 28, SpringLayout.NORTH, frmQwikcamControl.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, ctrlSelectLabel, -6, SpringLayout.NORTH, ctrlSelect);
		springLayout.putConstraint(SpringLayout.EAST, ctrlSelectLabel, -11, SpringLayout.EAST, ctrlSelect);
		frmQwikcamControl.getContentPane().add(ctrlSelectLabel);

		panInputLabel = new JLabel("Pan Input");
		springLayout.putConstraint(SpringLayout.NORTH, panInputLabel, 33, SpringLayout.SOUTH, ctrlSelect);
		springLayout.putConstraint(SpringLayout.WEST, panInputLabel, 10, SpringLayout.WEST, frmQwikcamControl.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, ctrlSelectLabel, 0, SpringLayout.WEST, panInputLabel);
		springLayout.putConstraint(SpringLayout.WEST, ctrlSelect, 0, SpringLayout.WEST, panInputLabel);
		frmQwikcamControl.getContentPane().add(panInputLabel);

		panProgressBar = new JProgressBar();
		panProgressBar.setMaximum(1);
		panProgressBar.setMinimum(-1);
		springLayout.putConstraint(SpringLayout.NORTH, panProgressBar, 6, SpringLayout.SOUTH, panInputLabel);
		springLayout.putConstraint(SpringLayout.WEST, panProgressBar, 10, SpringLayout.WEST, frmQwikcamControl.getContentPane());
		frmQwikcamControl.getContentPane().add(panProgressBar);

		tiltInputLabel = new JLabel("Tilt Input");
		springLayout.putConstraint(SpringLayout.NORTH, tiltInputLabel, 17, SpringLayout.SOUTH, panProgressBar);
		springLayout.putConstraint(SpringLayout.WEST, tiltInputLabel, 10, SpringLayout.WEST, frmQwikcamControl.getContentPane());
		frmQwikcamControl.getContentPane().add(tiltInputLabel);

		tiltProgressBar = new JProgressBar();
		tiltProgressBar.setMinimum(-1);
		tiltProgressBar.setMaximum(1);
		springLayout.putConstraint(SpringLayout.NORTH, tiltProgressBar, 37, SpringLayout.SOUTH, panProgressBar);
		springLayout.putConstraint(SpringLayout.WEST, tiltProgressBar, 10, SpringLayout.WEST, frmQwikcamControl.getContentPane());
		frmQwikcamControl.getContentPane().add(tiltProgressBar);

		zoomLvlLabel = new JLabel("Zoom Level");
		springLayout.putConstraint(SpringLayout.NORTH, zoomLvlLabel, 23, SpringLayout.SOUTH, tiltProgressBar);
		springLayout.putConstraint(SpringLayout.WEST, zoomLvlLabel, 10, SpringLayout.WEST, frmQwikcamControl.getContentPane());
		frmQwikcamControl.getContentPane().add(zoomLvlLabel);

		zoomProgressBar = new JProgressBar();
		zoomProgressBar.setMinimum(-1);
		zoomProgressBar.setMaximum(1);
		springLayout.putConstraint(SpringLayout.NORTH, zoomProgressBar, 43, SpringLayout.SOUTH, tiltProgressBar);
		springLayout.putConstraint(SpringLayout.WEST, zoomProgressBar, 10, SpringLayout.WEST, frmQwikcamControl.getContentPane());
		frmQwikcamControl.getContentPane().add(zoomProgressBar);

		cameraButton = new JButton("Camera Select");
		springLayout.putConstraint(SpringLayout.NORTH, cameraButton, 0, SpringLayout.NORTH, ctrlSelect);
		springLayout.putConstraint(SpringLayout.WEST, cameraButton, 77, SpringLayout.EAST, ctrlSelect);
		frmQwikcamControl.getContentPane().add(cameraButton);

		panDeadzoneBtn = new JButton("DeadZone Set");
		springLayout.putConstraint(SpringLayout.WEST, panDeadzoneBtn, 0, SpringLayout.WEST, cameraButton);
		springLayout.putConstraint(SpringLayout.SOUTH, panDeadzoneBtn, 0, SpringLayout.SOUTH, panProgressBar);
		frmQwikcamControl.getContentPane().add(panDeadzoneBtn);

		tiltDeadzoneBtn = new JButton("DeadZone Set");
		springLayout.putConstraint(SpringLayout.WEST, tiltDeadzoneBtn, 0, SpringLayout.WEST, cameraButton);
		springLayout.putConstraint(SpringLayout.SOUTH, tiltDeadzoneBtn, 0, SpringLayout.SOUTH, tiltProgressBar);
		frmQwikcamControl.getContentPane().add(tiltDeadzoneBtn);

		zoomDeadzoneBrn = new JButton("DeadZone Set");
		springLayout.putConstraint(SpringLayout.WEST, zoomDeadzoneBrn, 0, SpringLayout.WEST, cameraButton);
		springLayout.putConstraint(SpringLayout.SOUTH, zoomDeadzoneBrn, 0, SpringLayout.SOUTH, zoomProgressBar);
		frmQwikcamControl.getContentPane().add(zoomDeadzoneBrn);

		infoDsp = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, infoDsp, 28, SpringLayout.NORTH, frmQwikcamControl.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, infoDsp, -292, SpringLayout.EAST, frmQwikcamControl.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, infoDsp, 149, SpringLayout.NORTH, frmQwikcamControl.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, infoDsp, -31, SpringLayout.EAST, frmQwikcamControl.getContentPane());
		infoDsp.setText("Camera Information");
		infoDsp.setEditable(false);
		frmQwikcamControl.getContentPane().add(infoDsp);

		cameraSettingsBtn = new JButton("Camera Settings");
		springLayout.putConstraint(SpringLayout.WEST, cameraSettingsBtn, 0, SpringLayout.WEST, infoDsp);
		springLayout.putConstraint(SpringLayout.SOUTH, cameraSettingsBtn, 0, SpringLayout.SOUTH, zoomProgressBar);
		frmQwikcamControl.getContentPane().add(cameraSettingsBtn);
	}
}
