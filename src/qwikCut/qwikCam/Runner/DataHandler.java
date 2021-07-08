package qwikCut.qwikCam.Runner;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Component.Identifier.Axis;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class DataHandler implements ControllerInterface
{
	private Controller ctrl;
	private HashMap<String, Controller> map;
	private int linearityX = 0;
	private int linearityY = 0;
	private int linearityZ = 0;
	private boolean hasRan = false;
	private float zone = 0;

	private int RX, RY, Z, X, Y;

	@Override
	public boolean setController(String ctrl)
	{
		Controller selection = map.get(ctrl);

		if (selection == null)
			return false;

		this.ctrl = selection;

//		for (Component c : this.ctrl.getComponents())
//		{
//			System.out.println(c.getName());
//		}

		pollController();
		return true;
	}

	@Override
	public int readController(int axis)
	{
		switch (axis)
		{
		case 1:
			return RX;
		case 2:
			return RY;
		case 3:
			return Z;
		case 4:
			return X;
		case 5:
			return Y;
		default:
			return 0;
		}
	}

	@Override
	public void setInputMap(HashMap<String, Controller> map)
	{
		this.map = map;
	}

	// reads the values from the controller
	// stores the axis data to variables for other methods to read
	private void pollController()
	{
		TimerTask tx = new TimerTask()
		{
			@Override
			public void run()
			{
				EventQueue eventQueue = ctrl.getEventQueue();
				Event event = new Event();
				ctrl.poll();
				eventQueue.getNextEvent(event);

				Component comp = event.getComponent();
				float data = 0f;

				if (comp != null)
				{
					Identifier id = comp.getIdentifier();
					data = comp.getPollData();
					if (id == Axis.RX)
					{
						// Right Stick Left and Right

						// this line is where we apply our input map
						// for example: RX = inputMap(RX, data)
						RX = inputMap(id, data);
					} else if (id == Axis.RY)
					{
						// Right Stick Up and Down
						RY = inputMap(id, -1*data);
					} else if (id == Axis.Z)
					{
						// Triggers
						Z = inputMap(id, data);
					} else if (id == Axis.X)
					{
						// Left Stick Left and Right
						X = inputMap(id, data);
					} else if (id == Axis.Y)
					{
						// Left Stick Up and Down
						Y = inputMap(id, data);
					}
				}

			}
		};

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(tx, 0, 10);
	}

	@Override
	public void setLinearity(int[] setting)
	{
		// linear = 0
		this.linearityX = setting[0];
		this.linearityY = setting[1];
		this.linearityZ = setting[2];
		hasRan = true;

		System.out.println(setting[0] + "," + setting[1] + "," + setting[2]);

	}
	
	@Override
	public int[] getLinearity()
	{
		int[] retval = new int[3];
		retval[0] = this.linearityX;
		retval[1] = this.linearityY;
		retval[2] = this.linearityZ;
		
		return retval;
	}

	@Override
	public boolean getLinearityChange()
	{
		return hasRan;
	}
	
	@Override
	public void setDeadzone(int zone)
	{
		// zone 0-100 -> 0-1
		// data 0-1
		this.zone = zone/100f;
	}
	
	@Override
	public int getDeadzone()
	{
		return (int)(this.zone*100);
	}

	private int inputMap(Identifier id, float data)
	{
		if (id == Axis.RX)
		{
			if (linearityX == 1)
			{

				int testValue = (int) (Math.pow(data, 3) * 1000);
				if (Math.abs(data) < zone)
					return 0;
				else
					return testValue;
			} else
			{
				if (Math.abs(data) < zone)
					return 0;
				else
					return (int) (data * 1000);
			}

		} else if (id == Axis.RY)
		{
			// Right Stick Up and Down
			if (linearityY == 1)
			{
				int testValue = (int) (Math.pow(data, 3) * 1000);
				if (Math.abs(data) < zone)
					return 0;
				else
					return testValue;
			} else
			{
				if (Math.abs(data) < zone)
					return 0;
				else
					return (int) (data * 1000);
			}
		} else if (id == Axis.Z)
		{
			// Right Stick Up and Down
			if (linearityZ == 1)
			{
				int testValue = (int) (Math.pow(data, 3) * 1000);
				if (Math.abs(data) < zone)
					return 0;
				else
					return testValue;
			} else
			{
				if (Math.abs(data) < zone)
					return 0;
				else
					return (int) (data * 1000);
			}
		} else if (id == Axis.X)
		{
			// Right Stick Up and Down
			if (linearityX == 1)
			{
				int testValue = (int) (Math.pow(data, 3) * 1000);
				if (Math.abs(data) < zone)
					return 0;
				else
					return testValue;
			} else
			{
				if (Math.abs(data) < zone)
					return 0;
				else
					return (int) (data * 1000);
			}
		} else if (id == Axis.Y)
		{
			// Right Stick Up and Down
			if (linearityY == 1)
			{
				int testValue = (int) (Math.pow(data, 3) * 1000);
				if (Math.abs(data) < zone)
					return 0;
				else
					return testValue;
			} else
			{
				if (Math.abs(data) < zone)
					return 0;
				else
					return (int) (data * 1000);
			}
		}
		return -1;
	}
}
