package qwikCut.qwikCam.Runner;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.onvif.ver10.schema.Profile;

import de.onvif.soap.OnvifDevice;
import de.onvif.soap.devices.PtzDevices;

// This class handles the movement of the camera
// along with all communication and connection.
// it analyzes current input values to determine motion
// Also determine which method of movement the camera will use
public class CameraHandler implements CameraInterface
{
	// Connection info
	String ip = null;
	String username = null;
	String password = null;
	int validConn = -1;

	// ONVIF related vars
	OnvifDevice camera = null;
	PtzDevices ptzDevices; // = camera.getPtz();
	String profileToken; // = profiles.get(0).getToken();
	List<Profile> profiles;// = camera.getDevices().getProfiles();

	// controller data
	ControllerInterface controller;
	float lastX = 0f;
	float lastY = 0f;
	float lastZ = 0f;

	// movementType variable allows for the software to know which
	// method to run after it determines which methods are supported
	// -1 means not set
	// 0 means absolute
	// 1 means relative
	// 2 means continuous
	int movementType = -1;

	// Camera information for GUI
	String cameraInfo = null;
	String cameraUri = null;
	
	// Camera Speed limits
	int pan = 50;
	int tilt = 50;
	int zoom = 50;
	
	// camera command limiter
	boolean hasMoved = false;

	public CameraHandler(ControllerInterface controller)
	{
		this.controller = controller;
		move();
	}

	// This method communicates with the camera to determine
	// which movement method the camera supports.
	// If the camera supports more then one it will choice
	// the method that works best for the software
	private void getOptimalMoveMethod()
	{
//		if (ptzDevices.isRelativeMoveSupported(profileToken))
//		{
////			movementType = 2;
//			movementType = 1;
//			return;
//		} else if (ptzDevices.isAbsoluteMoveSupported(profileToken))
//		{
//			movementType = 0;
//			return;
//		} else if (ptzDevices.isContinuosMoveSupported(profileToken))
//		{
//			movementType = 2;
//			return;
//		}
		if (ptzDevices.isContinuosMoveSupported(profileToken))
		{
			movementType = 2;
			return;
		}

		movementType = -1;
	}

	// This method establishes the connection to the camera
	// It collects all the information from the UI
	// returns a 1 if the connection was successful
	// returns a 0 if the connection was unsuccessful
	// returns a -1 otherwise
	private int connectCamera()
	{
		try
		{
			camera = new OnvifDevice(ip, username, password);
			profiles = camera.getDevices().getProfiles();

			profileToken = profiles.get(0).getToken();

			ptzDevices = camera.getPtz();
			
			

			validConn = 1;
			getOptimalMoveMethod();

			// if the camera does not support any of the movement types
			// make this method return 0 to throw a warning to the user
			// make sure to reference this escape clause in troubleshooting
			// steps since it may be unclear.
			if (movementType == -1)
			{
				JOptionPane.showMessageDialog(new JPanel(), "The camera you attempted to connect to\nis not supported!\n\nError Code: 101");
				return 0;
			}

			buildCameraInfo();

			return 1;

		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(new JPanel(), e.getMessage());
			System.out.println("Crash");
			e.printStackTrace();
			return 0;
		}
	}

	// checks if the camera needs to be moved based of input
	// if it does it will call the optimal method based on prior
	// logic. If the prior logic has not ran it will exit.
	private void move()
	{
		TimerTask tx = new TimerTask()
		{
			@Override
			public void run()
			{
				switch (movementType)
				{
				case 0:
					moveAbsolute();
					break;
				case 1:
					moveRelative();
					break;
				case 2:
					moveContinous();
					break;
				default:
					break;
				}
			}
		};

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(tx, 0, 10);
	}

	// All move methods have maximum float values as 1.0
	// All move methods have minimum float values of -1.0
	private void moveAbsolute()
	{
		// not implemented since camera did not respond or support
	}

	private void moveRelative()
	{
		// not implemented since camera did not respond or support
		ptzDevices.relativeMove(profileToken, 1f, 1f, 1f);
	}

	private void moveContinous()
	{
		// get the current controller inputs scaled by the speed
		float X = ((controller.readController(4) / 1000f)*(pan/100f));
		float Y = ((controller.readController(2) / 1000f)*(tilt/100f));
		float Z = -(controller.readController(3) / 1000f)*(zoom/100f);
//		float button = controller.readController(6);
		
//		if (button == 1.0f)
//		{
//			if (X > 0)
//			{
//				X = Math.min(X*2f, 1f);
//			}
//			else
//			{
//				X = Math.max(X*2f, -1f);
//			}
//			if (Y > 0)
//			{
//				Y = Math.min(Y*2f, 1f);
//			}
//			else
//			{
//				Y = Math.max(Y*2f, -1f);
//			}
//		}
//		
		// calculate how much the controller input changed from the last check
		float deltaX = Math.abs(X - lastX);
		float deltaY = Math.abs(Y - lastY);
		float deltaZ = Math.abs(Z - lastZ);
		
		// If any of the directions have changed more then a threashold tell the camera
		// to move in the new direction then save the last inputs for the next check
		if (deltaX > 0.05 || deltaY > .05 || deltaZ > 0.05)
		{
			System.out.println("X: " + X + ", Y: " + Y);
			ptzDevices.continuousMove(profileToken, X, Y, Z);
			lastX = X;
			lastY = Y;
			lastZ = Z;
			hasMoved = true;
		}
		
		// if all the inputs are zero tell the camera to stop moving
		if (X == 0f && Y == 0f && Z == 0f && hasMoved)
		{
			ptzDevices.stopMove(profileToken);
			hasMoved = false;
		}
	}

	// Get all useful information from the camera to aid in the operation of the camera
	// formatted to be readable.
	private void buildCameraInfo()
	{
		StringBuilder sb = new StringBuilder();

		// Determine which movement types are supported with the order determining which one is used if multiple are supported
		sb.append("All possible movement types sorted by priority of use:");
		sb.append("Relative movement support: " + ptzDevices.isRelativeMoveSupported(profileToken) + "\n");
		sb.append("Absolute movement support: " + ptzDevices.isAbsoluteMoveSupported(profileToken) + "\n");
		sb.append("Continous movement support: " + ptzDevices.isContinuosMoveSupported(profileToken) + "\n");
		
		// ask the camera for the main stream URI and display it
		try
		{
			cameraUri = camera.getMedia().getRTSPStreamUri(profileToken);
			sb.append("Stream URL: \n" + cameraUri);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		cameraInfo = sb.toString();
	}

	// This method fires once the cameraUI has collected the information
	// After this is ran the software starts talking with the camera.
	@Override
	public int setConnectionInfo(String ip, String user, String pass)
	{
		this.ip = ip;
		this.username = user;
		this.password = pass;

		return connectCamera();
	}

	// Used to determine if the user can open the cameraUI
	// if there is a valid connection return true otherwise false
	@Override
	public boolean hasConnection()
	{
		return validConn == 1;
	}

	// display the camera info for the main GUI
	@Override
	public String getCameraInfo()
	{
		if (cameraInfo == null)
			return "No camera information avaliable!";
		else
			return cameraInfo;
	}

	// used to pass the stream uri that the camera provices
	// to the gui so it can be used to insert onto the clipboard
	@Override
	public String getStreamURL()
	{
		return cameraUri;
	}
	
	// collect the user set maximum camera speed
	@Override
	public void setSpeedLimits(int pan, int tilt, int zoom)
	{
		this.pan = pan;
		this.tilt = tilt;
		this.zoom = zoom;
	}
	
	// This method is incomplete since not all movement types have code
	// The goal of this method is to make sure the camera is stopped before the
	// software releases control since there is no disconnect method.
	@Override
	public void close()
	{
		ptzDevices.stopMove(profileToken);
	}
	
	// Pass the speed limit values back when called to allow for the UI
	// to display what was set prior for easier adjustments.
	@Override
	public int[] getSpeedLimits()
	{
		int[] retval = new int[3];
		
		retval[0] = pan;
		retval[1] = tilt;
		retval[2] = zoom;
		
		return retval;
	}
}
