package qwikCut.qwikCam.Runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class CtrlHandler
{
	private HashSet<String> displayed;
	
	private HashMap<String, Controller> ctrlMap = new HashMap<>();
	
	private ControllerInterface dataHandler;

	public CtrlHandler(ControllerInterface sync)
	{
		this.dataHandler = sync;
		init();
		dataHandler.setInputMap(ctrlMap);
	}

	private void init()
	{
		Controller[] all = ControllerEnvironment.getDefaultEnvironment().getControllers();
		ArrayList<Controller> filtered = new ArrayList<>();

		displayed = new HashSet<>();
		displayed.add("Please select from below");

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
				ctrlMap.put(temp.getName(), temp);
				filtered.add(temp);
			}
		}
	}

	public HashSet<String> getList()
	{
		return this.displayed;
	}
}
