package qwikCut.qwikCam.Runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.soap.SOAPException;

import org.onvif.ver10.schema.PTZSpeed;
import org.onvif.ver10.schema.Vector1D;
import org.onvif.ver10.schema.Vector2D;
import org.onvif.ver20.ptz.wsdl.PTZ;

import de.onvif.soap.OnvifDevice;
//import jakarta.xml.soap.SOAPException;
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
//	    	  URL temp = new URL("http://192.168.2.233:80/");
	          OnvifDevice nvt = new OnvifDevice("192.168.2.233/", "admin", "12345678");
//	          Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
	          String profileToken = "MediaProfile000";
	          
	          PTZ test = nvt.getPtz();
	          PTZSpeed speed = new PTZSpeed();
	          Vector2D vector = new Vector2D();
	          vector.setX(1f);
	          vector.setY(1f);
	          speed.setPanTilt(vector);
	          Vector1D v1d = new Vector1D();
	          v1d.setX(0f);
	          speed.setZoom(v1d);
	          Duration duration = DatatypeFactory.newInstance().newDuration(1000);
	          
	          test.continuousMove(profileToken, speed, duration);
	          
	       }
	       catch (ConnectException e) {
	          System.err.println("Could not connect to NVT.");
	       }
	       catch (SOAPException e) {
	          e.printStackTrace();
	       } catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatatypeConfigurationException e)
		{
			// TODO Auto-generated catch block
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
