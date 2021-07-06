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
		if (ptzDevices.isRelativeMoveSupported(profileToken))
		{
			movementType = 1;
			return;
		} else if (ptzDevices.isAbsoluteMoveSupported(profileToken))
		{
			movementType = 0;
			return;
		} else if (ptzDevices.isContinuosMoveSupported(profileToken))
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
				// -1 means not set
				// 0 means absolute
				// 1 means relative
				// 2 means continuous
//				int movementType = -1;
				switch (movementType)
				{
				case 0:
//					System.out.println("case 0");
					moveAbsolute();
					break;
				case 1:
//					System.out.println("case 1");
					moveRelative();
					break;
				case 2:
//					System.out.println("case 2");
					moveContinous();
					break;
				default:
//					System.out.println("case default");
					break;
				}
			}
		};

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(tx, 0, 10);
	}

	private void moveAbsolute()
	{

	}

	private void moveRelative()
	{

	}

	private void moveContinous()
	{
		float X = (controller.readController(4) / 1000f)*(pan/100f);
		float Y = (controller.readController(2) / 1000f)*(tilt/100f);
		float Z = (controller.readController(3) / 1000f)*(zoom/100f);
		
		float deltaX = Math.abs(X - lastX);
		float deltaY = Math.abs(Y - lastY);
		float deltaZ = Math.abs(Z - lastZ);
		
//		System.out.println("Moving, dX: " + deltaX + ", dY: " + deltaY + ", dZ: " + deltaZ);
		
		if (deltaX > 0.1 || deltaY > .01 || deltaZ > 0.1)
		{
			ptzDevices.continuousMove(profileToken, X, Y, Z);
			lastX = X;
			lastY = Y;
			lastZ = Z;
		}
		
		if (X == 0f && Y == 0f && Z == 0f)
		{
			ptzDevices.stopMove(profileToken);
		}

//		System.out.println("Moving, X: " + X + ", Y: " + Y + ", Z: " + Z);
//		ptzDevices.continuousMove(profileToken, X, Y, Z);
//
//		try
//		{
//			TimeUnit.MILLISECONDS.sleep(100);
//		} catch (InterruptedException e)
//		{
//			e.printStackTrace();
//		}
//		ptzDevices.stopMove(profileToken);
	}

	private void buildCameraInfo()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("Relative movement support: " + ptzDevices.isRelativeMoveSupported(profileToken) + "\n");
		sb.append("Absolute movement support: " + ptzDevices.isAbsoluteMoveSupported(profileToken) + "\n");
		sb.append("Continous movement support: " + ptzDevices.isContinuosMoveSupported(profileToken) + "\n");
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

//		System.out.println(pass);

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
	
	@Override
	public void setSpeedLimits(int pan, int tilt, int zoom)
	{
		this.pan = pan;
		this.tilt = tilt;
		this.zoom = zoom;
	}
	
	@Override
	public void close()
	{
		ptzDevices.stopMove(profileToken);
	}
}
