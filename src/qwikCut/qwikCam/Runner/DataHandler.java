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
					}
					else if (id == Axis.RY)
					{
						// Right Stick Up and Down
						RY = inputMap(id, data);
					}
					else if (id == Axis.Z)
					{
						// Triggers
						Z = inputMap(id, data);
					}
					else if (id == Axis.X)
					{
						// Left Stick Left and Right
						X = inputMap(id, data);
					}
					else if (id == Axis.Y)
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
		
		System.out.println(setting[0]);

		
	}
	
	private int inputMap(Identifier id, float data)
	{
		
		// Right Stick Left and Right
		
		// this line is where we apply our input map
		// for example: RX = inputMap(RX, data)
	
		if (id == Axis.RX)
		{
			if (linearityX == 1)
			{
				int testValue;
				if(data<0) 
				{
					testValue =  (int)((data*.06+(.94*data*data*data))*-1000);
				}
				else
					testValue =  (int)((data*.06+(.94*data*data*data))*1000);
				//System.out.println(testValue + " ," + (int)(data*1000));
				return testValue;				
			}
			else 
			{
				return (int)(data*1000);
			}
				
		}
		else if (id == Axis.RY)
		{
			// Right Stick Up and Down
			if (linearityY == 1)
			{
				int testValue;
				if(data<0) 
				{
					testValue =  (int)((data*.06+(.94*data*data))*-1000);
				}
				else
					testValue =  (int)((data*.06+(.94*data*data*data))*1000);
				
				System.out.println(testValue + " ," + (int)(data*1000));
				return testValue;				
			}
			else 
			{
				return (int)(data*1000);
			}
		}
		else if (id == Axis.Z)
		{
			// Triggers
			return (int)(data*1000);
		}
		else if (id == Axis.X)
		{
			// Left Stick Left and Right
			return (int)(data*1000);
		}
		else if (id == Axis.Y)
		{
			// Left Stick Up and Down
			return (int)(data*1000);
		}
		return -1;
	}
}
