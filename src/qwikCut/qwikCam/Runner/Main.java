package qwikCut.qwikCam.Runner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import qwikCut.qwikCam.Test.Tester;
import qwikCut.qwikCam.UI.MainUI;

public class Main
{
	private static MainUI window;
	private static CtrlHandler controller;
	public static ControllerInterface sync = new DataHandler();
	public static CameraInterface cameraInt;

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
		
		Tester tester = new Tester();

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
