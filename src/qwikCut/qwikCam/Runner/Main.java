package qwikCut.qwikCam.Runner;

import java.awt.EventQueue;

import qwikCut.qwikCam.UI.mainUI;

public class Main 
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					mainUI window = new mainUI();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
}
