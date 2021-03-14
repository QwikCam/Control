package qwikCut.qwikCam.Runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.onvif.soap.OnvifDevice;
import jakarta.xml.soap.SOAPException;
import qwikCut.qwikCam.UI.MainUI;

public class Main
{
	private static MainUI window;
	private static CtrlHandler controller;
	public static ControllerInterface sync = new DataHandler();

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
		window = new MainUI(sync);
		window.setCombo(controller.getList());

		try
		{
			OnvifDevice nvt = new OnvifDevice("192.168.2.191:8000", "admin", "");
			Date nvtDate = nvt.getDevices().getDate();
			System.out.println(new SimpleDateFormat().format(nvtDate));
		} catch (ConnectException e)
		{
			System.err.println("Could not connect to NVT.");
		} catch (SOAPException e)
		{
			e.printStackTrace();
		}
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
