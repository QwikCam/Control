package qwikCut.qwikCam.Runner;

import java.awt.EventQueue;

import qwikCut.qwikCam.UI.MainUI;

public class Main
{
	private static MainUI window;
	
	public static void main(String[] args)
	{
		startGUI();
	}
	
	private static void startGUI()
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					window = new MainUI();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
