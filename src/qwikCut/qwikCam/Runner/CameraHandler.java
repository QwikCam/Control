package qwikCut.qwikCam.Runner;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
	PtzDevices ptzDevices; //= camera.getPtz();
	String profileToken; // = profiles.get(0).getToken();
	List<Profile> profiles;// = camera.getDevices().getProfiles();
	
	public CameraHandler()
	{
		move();
	}
	
	// movementType variable allows for the software to know which
	// method to run after it determines which methods are supported
	// -1 means not set
	// 0 means absolute
	// 1 means relative
	// 2 means continuous
	int movementType = -1;
	
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
		}
		else if (ptzDevices.isAbsoluteMoveSupported(profileToken))
		{
			movementType = 0;
			return;
		}
		else if (ptzDevices.isContinuosMoveSupported(profileToken))
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
				return 0;
			}
			
			return 1;
			
//			if (ptzDevices.isContinuosMoveSupported(profileToken))
//			{
//				System.out.println("Moving");
//				ptzDevices.continuousMove(profileToken, 1f, 1f, zoom);
//			}
//			else
//			{
//				System.out.println("no move");
//			}
//			TimeUnit.SECONDS.sleep(1);
//			ptzDevices.stopMove(profileToken);
		}
		catch (Exception e)
		{
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
				// check if we know the method to call
				if (movementType == -1)
				{
					return;
				}
			}
		};
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(tx, 0, 10);
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
}
