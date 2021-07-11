package qwikCut.qwikCam.Runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.onvif.ver10.schema.FloatRange;
import org.onvif.ver10.schema.Profile;

import de.onvif.soap.OnvifDevice;
import de.onvif.soap.devices.PtzDevices;
import jakarta.xml.soap.SOAPException;
import qwikCut.qwikCam.UI.MainUI;

public class Main
{
	private static MainUI window;
	private static CtrlHandler controller;
	public static ControllerInterface sync = new DataHandler();
	public static CameraInterface cameraInt;

	// main program loop
	// will start all threads for the software
	// will copy needed files for the software
	public static void main(String[] args)
	{
		// Attempt to copy the needed files from the jar to the system
		try
		{
			ArrayList<String> fileNames = new ArrayList<>();
			fileNames.add("jinput-dx8_64.dll");
			fileNames.add("jinput-raw_64.dll");
			fileNames.add("jinput-wintab.dll");
//			fileNames.add("HCNETSDK.dll");
			copyAndPath(fileNames);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	      try {
	          OnvifDevice nvt = new OnvifDevice("192.168.2.233:80", "admin", "12345678");
	          List<Profile> profiles = nvt.getDevices().getProfiles();
	          String profileToken = profiles.get(0).getToken();
	 			
	          PtzDevices ptzDevices = nvt.getPtz();

	          FloatRange panRange = ptzDevices.getPanSpaces(profileToken);
	          FloatRange tiltRange = ptzDevices.getTiltSpaces(profileToken);
	          float zoom = ptzDevices.getZoomSpaces(profileToken).getMax();

	          float x = (panRange.getMax() + panRange.getMin()) / 3f;
	          float y = (tiltRange.getMax() + tiltRange.getMin()) / 3f;

	          if (ptzDevices.isAbsoluteMoveSupported(profileToken)) {
	          ptzDevices.absoluteMove(profileToken, x, y, zoom);
	          }
	       }
	       catch (ConnectException e) {
	          System.err.println("Could not connect to NVT.");
	       }
	       catch (SOAPException e) {
	          e.printStackTrace();
	       }

		// start the threads
		controller = new CtrlHandler(sync);
		cameraInt = new CameraHandler(sync);
		window = new MainUI(sync, cameraInt);
		window.setCombo(controller.getList());
	}

	private static void copyAndPath(ArrayList<String> name) throws IOException
	{
		String path = "";
		for (String s : name)
		{
			InputStream in = Main.class.getResourceAsStream(s);
			byte[] buffer = new byte[1024];
			int read = -1;
			File temp = new File(new File(System.getProperty("java.io.tmpdir")), s);
			FileOutputStream fos = new FileOutputStream(temp);

			while ((read = in.read(buffer)) != -1)
			{
				fos.write(buffer, 0, read);
			}
			fos.close();
			in.close();
			System.out.println(temp.getAbsolutePath());
			path = temp.getAbsolutePath();
		}

		StringBuilder sb = new StringBuilder();

		for (String s : path.split("\\\\"))
		{
			if (!(s.contains(".dll")))
			{
				sb.append(s + "\\");
			}
		}

		String full = sb.toString();
		System.out.println(full.substring(0, full.length() - 1));

		System.setProperty("net.java.games.input.librarypath", full.substring(0, full.length() - 1));
	}

}
