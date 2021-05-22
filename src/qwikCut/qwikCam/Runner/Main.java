package qwikCut.qwikCam.Runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import qwikCut.qwikCam.UI.MainUI;

public class Main
{
	private static MainUI window;
	private static CtrlHandler controller;
	public static ControllerInterface sync = new DataHandler();
	public static CameraInterface cameraInt = new CameraHandler();

	public static void main(String[] args)
	{
		try
		{
			ArrayList<String> fileNames = new ArrayList<>();
			fileNames.add("jinput-dx8_64.dll");
			fileNames.add("jinput-raw_64.dll");
			fileNames.add("jinput-wintab.dll");
			copyAndPath(fileNames);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		controller = new CtrlHandler(sync);
		window = new MainUI(sync, cameraInt);
		window.setCombo(controller.getList());


		// the lines commented below will get the date and time from the camera
		// this is useful for verifying the camera is communicating.
//		try
//		{
//			OnvifDevice nvt = new OnvifDevice("192.168.2.191:8000", "admin", "");
//			Date nvtDate = nvt.getDevices().getDate();
//			System.out.println(new SimpleDateFormat().format(nvtDate));
//		} catch (ConnectException e)
//		{
//			System.err.println("Could not connect to NVT.");
//		} catch (SOAPException e)
//		{
//			e.printStackTrace();
//		}
		
		// the lines below are a basic test for moving the camera
		// useful for verification of camera's ability to move
//		try
//		{
//			OnvifDevice nvt = new OnvifDevice("192.168.2.191:8000", "admin", "");
//			List<Profile> profiles = nvt.getDevices().getProfiles();
////			System.out.println(profiles.toString());
//			
//			String profileToken = profiles.get(0).getToken();
//			
//			PtzDevices ptzDevices = nvt.getPtz();
//			
//			FloatRange panRange = ptzDevices.getPanSpaces(profileToken);
//			FloatRange tiltRange = ptzDevices.getTiltSpaces(profileToken);
//			float zoom = ptzDevices.getZoomSpaces(profileToken).getMin();
//			
//			float x = (panRange.getMax() + panRange.getMin()) / 2f;
//			float y = (tiltRange.getMax() + tiltRange.getMin()) / 2f;
//			
//			System.out.println("Max = " + panRange.getMax() + ", Min = " + panRange.getMin());
//			
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
//		}
//		catch (Exception e)
//		{
//			System.out.println("Crash");
//			e.printStackTrace();
//		}
		
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
