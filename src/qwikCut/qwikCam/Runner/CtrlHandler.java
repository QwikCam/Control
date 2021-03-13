package qwikCut.qwikCam.Runner;

import java.io.IOException;
import java.util.ArrayList;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class CtrlHandler
{
	private ArrayList<String> displayed;

	public CtrlHandler()
	{
		try
		{
			init();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void init() throws IOException
	{
		Controller[] all = ControllerEnvironment.getDefaultEnvironment().getControllers();
		ArrayList<Controller> filtered = new ArrayList<>();
//		Controller selected = null;

		System.out.printf("Please select the controller to use from the list below.\n\n");

		int j = 1;

		displayed = new ArrayList<>();

		for (int i = 0; i < all.length; i++)
		{
			// clean input to remove any duplicates and common devices
			Controller temp = all[i];
			System.out.println(temp.getType());

			if (temp.getName().contains("HID"))
			{
				continue;
			}

			if (temp.getType() != Controller.Type.GAMEPAD && temp.getType() != Controller.Type.FINGERSTICK && temp.getType() != Controller.Type.STICK)
			{
				continue;
			}

			if (displayed.add(temp.getName()))
			{
				System.out.printf((j++) + ". " + all[i] + "\twith type: " + all[i].getType() + "\n");
				filtered.add(temp);
//				selected = temp;
			}
		}
	}

	public ArrayList<String> getList()
	{
		return this.displayed;
	}
}
