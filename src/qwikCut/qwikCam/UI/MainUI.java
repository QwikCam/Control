package qwikCut.qwikCam.UI;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;

import qwikCut.qwikCam.Runner.CameraInterface;
import qwikCut.qwikCam.Runner.ControllerInterface;

public class MainUI
{
	private JFrame frame;
	public JComboBox<String> ctrlSelect;
	public JLabel ctrlSelectLabel;
	public JLabel panInputLabel;
	public JLabel tiltInputLabel;
	public JLabel zoomLvlLabel;
	public JProgressBar panProgressBar;
	public JProgressBar tiltProgressBar;
	public JProgressBar zoomProgressBar;
	public JButton cameraButton;
	public JButton controllerUIBtn;
	public JTextPane infoDsp;
	public JButton cameraSettingsBtn;
	public JButton ctrlConfirm, streamUriBtn;
	public JRadioButton speedBtn;

	private ControllerInterface ctrlHandler;
	private CameraInterface camera;

	/**
	 * Create the application.
	 */
	public MainUI(ControllerInterface sync, CameraInterface camera)
	{
		this.ctrlHandler = sync;
		this.camera = camera;
		initialize();
	}

	// Reads the data of each axis and updates the progress bars
	// runs constantly every 100ms
	private void startProb()
	{
		TimerTask RX = new TimerTask()
		{
			@Override
			public void run()
			{
				tiltProgressBar.setValue(ctrlHandler.readController(2));
				panProgressBar.setValue(ctrlHandler.readController(4));
				zoomProgressBar.setValue(-ctrlHandler.readController(3));
				speedBtn.setSelected(ctrlHandler.speedBtn());
				frame.repaint();
			}
		};

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(RX, 0, 100);
	}

	// Fill camera Information
	// runs for one minute every 100ms after its called
	private void updateCamera()
	{
		ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(2);
		ScheduledFuture<?> schedule = executor.scheduleAtFixedRate(() ->
		{
			if (!camera.hasConnection())
			{
				infoDsp.setText("No Camera connected!");
			} else
			{
				infoDsp.setText(camera.getCameraInfo());
			}
			
			if (camera.getStreamURL() != null)
			{
				streamUriBtn.setEnabled(true);
			}
			
			frame.repaint();

		}, 0, 500, TimeUnit.MILLISECONDS);

		executor.schedule(() -> schedule.cancel(false), 1, TimeUnit.MINUTES);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setTitle("QwikCam Control");
		frame.setBounds(100, 100, 836, 340);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		URL iconURL = getClass().getResource("/qwikCut/qwikCam/UI/logo.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frame.setIconImage(icon.getImage());

		ctrlSelect = new JComboBox<>();
		springLayout.putConstraint(SpringLayout.NORTH, ctrlSelect, 48, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, ctrlSelect, 144, SpringLayout.WEST, frame.getContentPane());
		ctrlSelect.setToolTipText("Controller Selection");
		frame.getContentPane().add(ctrlSelect);

		ctrlSelectLabel = new JLabel("Select Controller");
		springLayout.putConstraint(SpringLayout.NORTH, ctrlSelectLabel, 28, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, ctrlSelectLabel, -6, SpringLayout.NORTH, ctrlSelect);
		springLayout.putConstraint(SpringLayout.EAST, ctrlSelectLabel, -11, SpringLayout.EAST, ctrlSelect);
		frame.getContentPane().add(ctrlSelectLabel);

		panInputLabel = new JLabel("Pan Input");
		springLayout.putConstraint(SpringLayout.NORTH, panInputLabel, 33, SpringLayout.SOUTH, ctrlSelect);
		springLayout.putConstraint(SpringLayout.WEST, panInputLabel, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, ctrlSelectLabel, 0, SpringLayout.WEST, panInputLabel);
		springLayout.putConstraint(SpringLayout.WEST, ctrlSelect, 0, SpringLayout.WEST, panInputLabel);
		frame.getContentPane().add(panInputLabel);

		panProgressBar = new JProgressBar();
		panProgressBar.setMaximum(1000);
		panProgressBar.setMinimum(-1000);
		springLayout.putConstraint(SpringLayout.NORTH, panProgressBar, 6, SpringLayout.SOUTH, panInputLabel);
		springLayout.putConstraint(SpringLayout.WEST, panProgressBar, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(panProgressBar);

		tiltInputLabel = new JLabel("Tilt Input");
		springLayout.putConstraint(SpringLayout.NORTH, tiltInputLabel, 17, SpringLayout.SOUTH, panProgressBar);
		springLayout.putConstraint(SpringLayout.WEST, tiltInputLabel, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(tiltInputLabel);

		tiltProgressBar = new JProgressBar();
		tiltProgressBar.setMinimum(-1000);
		tiltProgressBar.setMaximum(1000);
		springLayout.putConstraint(SpringLayout.NORTH, tiltProgressBar, 37, SpringLayout.SOUTH, panProgressBar);
		springLayout.putConstraint(SpringLayout.WEST, tiltProgressBar, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(tiltProgressBar);

		zoomLvlLabel = new JLabel("Zoom Level");
		springLayout.putConstraint(SpringLayout.NORTH, zoomLvlLabel, 23, SpringLayout.SOUTH, tiltProgressBar);
		springLayout.putConstraint(SpringLayout.WEST, zoomLvlLabel, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(zoomLvlLabel);

		zoomProgressBar = new JProgressBar();
		zoomProgressBar.setMinimum(-1000);
		zoomProgressBar.setMaximum(1000);
		springLayout.putConstraint(SpringLayout.NORTH, zoomProgressBar, 43, SpringLayout.SOUTH, tiltProgressBar);
		springLayout.putConstraint(SpringLayout.WEST, zoomProgressBar, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(zoomProgressBar);

		cameraButton = new JButton("Camera Select");
		springLayout.putConstraint(SpringLayout.NORTH, cameraButton, 0, SpringLayout.NORTH, ctrlSelect);
		springLayout.putConstraint(SpringLayout.WEST, cameraButton, 77, SpringLayout.EAST, ctrlSelect);
		cameraButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						try
						{
							if (!camera.hasConnection())
								new CameraUI(camera);
						} catch (Exception e)
						{
							e.printStackTrace();
						} finally
						{
							updateCamera();
						}
					}
				});
			}
		});
		frame.getContentPane().add(cameraButton);

		controllerUIBtn = new JButton("Controller UI");
		springLayout.putConstraint(SpringLayout.WEST, controllerUIBtn, 0, SpringLayout.WEST, cameraButton);
		springLayout.putConstraint(SpringLayout.SOUTH, controllerUIBtn, 0, SpringLayout.SOUTH, panProgressBar);
		frame.getContentPane().add(controllerUIBtn);
		controllerUIBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent E)
			{
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						try
						{
							new ControllerUI(ctrlHandler);

						} catch (Exception e)
						{
							e.printStackTrace();
						}
					}

				});
			}

		});

		infoDsp = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, infoDsp, 28, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, infoDsp, -454, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, infoDsp, 149, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, infoDsp, -31, SpringLayout.EAST, frame.getContentPane());
		infoDsp.setEditable(false);
		frame.getContentPane().add(infoDsp);

		cameraSettingsBtn = new JButton("Camera Settings");
		springLayout.putConstraint(SpringLayout.WEST, cameraSettingsBtn, 372, SpringLayout.EAST, zoomProgressBar);
		springLayout.putConstraint(SpringLayout.SOUTH, cameraSettingsBtn, 0, SpringLayout.SOUTH, zoomProgressBar);
		cameraSettingsBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						try
						{
//							if (!camera.hasConnection())
								new CameraSettingUI(camera);
//								System.out.println("test");
						} catch (Exception e)
						{
							e.printStackTrace();
						} finally
						{
							updateCamera();
						}
					}
				});
			}
		});
		frame.getContentPane().add(cameraSettingsBtn);

		ctrlConfirm = new JButton("Confirm Controller");
//		ctrlConfirm.setVisible(ctrlHandler.getLinearityChange());
		springLayout.putConstraint(SpringLayout.NORTH, ctrlConfirm, 6, SpringLayout.SOUTH, ctrlSelect);
		springLayout.putConstraint(SpringLayout.WEST, ctrlConfirm, 10, SpringLayout.WEST, frame.getContentPane());
		ctrlConfirm.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						String selection = (String) ctrlSelect.getSelectedItem();

						// check if its a valid controller
						if (!selection.equals("Please select from below"))
						{
							if (ctrlHandler.setController(selection))
							{
								System.out.println("Set");
								ctrlSelect.setEnabled(false);
								ctrlConfirm.setEnabled(false);
								startProb();
							} else
							{
								System.out.println("Not found");
							}
						}
					}
				});
			}
		});
		frame.getContentPane().add(ctrlConfirm);
		
		streamUriBtn = new JButton("Copy Stream URI");
		streamUriBtn.setEnabled(false);
		springLayout.putConstraint(SpringLayout.NORTH, streamUriBtn, 0, SpringLayout.NORTH, tiltProgressBar);
		springLayout.putConstraint(SpringLayout.WEST, streamUriBtn, 0, SpringLayout.WEST, cameraSettingsBtn);
		streamUriBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						String uri = camera.getStreamURL();
						StringSelection select = new StringSelection(uri);
						Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
						cb.setContents(select, null);
					}
				});
			}
		});
		frame.getContentPane().add(streamUriBtn);
		
		speedBtn = new JRadioButton("Speed Button");
		speedBtn.setEnabled(false);
		springLayout.putConstraint(SpringLayout.NORTH, speedBtn, 0, SpringLayout.NORTH, zoomLvlLabel);
		springLayout.putConstraint(SpringLayout.EAST, speedBtn, 0, SpringLayout.EAST, cameraButton);
		frame.getContentPane().add(speedBtn);
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() 
		{
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) 
		    {
		    	if (camera.hasConnection())
		    	{
			        if (JOptionPane.showConfirmDialog(frame, 
				            "Are you sure you want to close this window?", "Close Window?", 
				            JOptionPane.YES_NO_OPTION,
				            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
				        {
				        	camera.close();
				            System.exit(0);
				        }
		    	}
		    }
		});

		frame.setVisible(true);
	}

	public void setCombo(HashSet<String> list)
	{
		for (String s : list)
		{
			ctrlSelect.addItem(s);
		}
	}
}
