package qwikCut.qwikCam.UI;

import javax.swing.JFrame;

import qwikCut.qwikCam.Runner.CameraInterface;
import javax.swing.SpringLayout;
import javax.swing.JSlider;
import javax.swing.JLabel;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class CameraSettingUI
{
	private CameraInterface camera;

	private JFrame frame;
	private JSlider panSlider, tiltSlider, zoomSlider;
	
	private int[] speedLimits;

	/**
	 * Create the application.
	 */
	public CameraSettingUI(CameraInterface camera)
	{
		this.camera = camera;
		this.speedLimits = camera.getSpeedLimits();
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setTitle("QwikCam Control - Camera Settings");
		frame.setBounds(100, 100, 306, 331);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		URL iconURL = getClass().getResource("/qwikCut/qwikCam/UI/logo.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frame.setIconImage(icon.getImage());
		
		panSlider = new JSlider();
		panSlider.setValue(speedLimits[0]);
		panSlider.setToolTipText("Pan speed limit");
		springLayout.putConstraint(SpringLayout.NORTH, panSlider, 43, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panSlider, 21, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(panSlider);
		
		JLabel panLabel = new JLabel("Pan Speed Limit");
		panLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		springLayout.putConstraint(SpringLayout.WEST, panLabel, 34, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panLabel, -7, SpringLayout.NORTH, panSlider);
		frame.getContentPane().add(panLabel);
		
		JLabel tiltLabel = new JLabel("Tilt Speed Limit");
		springLayout.putConstraint(SpringLayout.WEST, tiltLabel, 0, SpringLayout.WEST, panLabel);
		tiltLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(tiltLabel);
		
		tiltSlider = new JSlider();
		springLayout.putConstraint(SpringLayout.SOUTH, tiltLabel, -6, SpringLayout.NORTH, tiltSlider);
		springLayout.putConstraint(SpringLayout.NORTH, tiltSlider, 40, SpringLayout.SOUTH, panSlider);
		springLayout.putConstraint(SpringLayout.WEST, tiltSlider, 0, SpringLayout.WEST, panSlider);
		tiltSlider.setValue(speedLimits[1]);
		tiltSlider.setToolTipText("Tilt speed limit");
		frame.getContentPane().add(tiltSlider);
		
		JLabel zoomLabel = new JLabel("Zoom Speed Limit");
		springLayout.putConstraint(SpringLayout.NORTH, zoomLabel, 20, SpringLayout.SOUTH, tiltSlider);
		springLayout.putConstraint(SpringLayout.WEST, zoomLabel, 0, SpringLayout.WEST, panLabel);
		zoomLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.getContentPane().add(zoomLabel);
		
		zoomSlider = new JSlider();
		springLayout.putConstraint(SpringLayout.NORTH, zoomSlider, 17, SpringLayout.SOUTH, zoomLabel);
		springLayout.putConstraint(SpringLayout.EAST, zoomSlider, 0, SpringLayout.EAST, panSlider);
		zoomSlider.setValue(speedLimits[2]);
		zoomSlider.setToolTipText("Zoom speed limit");
		frame.getContentPane().add(zoomSlider);
		
		JButton closeBtn = new JButton("Apply and Close");
		springLayout.putConstraint(SpringLayout.NORTH, closeBtn, 27, SpringLayout.SOUTH, zoomSlider);
		springLayout.putConstraint(SpringLayout.WEST, closeBtn, 88, SpringLayout.WEST, frame.getContentPane());
		closeBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						int panLimit = panSlider.getValue();
						int tiltLimit = tiltSlider.getValue();
						int zoomLimit = zoomSlider.getValue();
						
						camera.setSpeedLimits(panLimit, tiltLimit, zoomLimit);
						frame.dispose();
					}
				});
			}
		});
		frame.getContentPane().add(closeBtn);
	}
}
