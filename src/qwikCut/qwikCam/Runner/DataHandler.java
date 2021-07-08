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
	
	// Given user linearity setting map the -1 to 1 float data to 0 to 1000 int data
	// that will be used through the program. Also apply the math to allow for an
	// exponential controller input curve.
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
	
	// Sets the controller to be read for input
	// returns true if no errors happen.
	@Override
	public boolean setController(String ctrl)
	{
		Controller selection = map.get(ctrl);

		if (selection == null)
			return false;

		this.ctrl = selection;

		pollController();
		return true;
	}

	// reads the last recorded value for an axis and returns it
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

	// sets the map between controller name and controller object
	@Override
	public void setInputMap(HashMap<String, Controller> map)
	{
		this.map = map;
	}

	// set the use specified linearity setting for the input map
	@Override
	public void setLinearity(int[] setting)
	{
		// linear = 0
		this.linearityX = setting[0];
		this.linearityY = setting[1];
		this.linearityZ = setting[2];
		hasRan = true;
	}
	
	// gets the last linearity setting
	@Override
	public int[] getLinearity()
	{
		int[] retval = new int[3];
		retval[0] = this.linearityX;
		retval[1] = this.linearityY;
		retval[2] = this.linearityZ;
		
		return retval;
	}

	// tells the software if the user has modified any linearity settings
	@Override
	public boolean getLinearityChange()
	{
		return hasRan;
	}
	
	// tells the software what range of inputs to ignore to prevent a sloppy
	// controller driving the input without the user demanding input
	@Override
	public void setDeadzone(int zone)
	{
		// zone 0-100 -> 0-1
		// data 0-1
		this.zone = zone/100f;
	}
	
	// returns the deadzone value that was set
	@Override
	public int getDeadzone()
	{
		return (int)(this.zone*100);
	}

}
