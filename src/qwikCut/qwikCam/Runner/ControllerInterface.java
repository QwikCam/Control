package qwikCut.qwikCam.Runner;

import java.util.HashMap;

import net.java.games.input.Controller;

public interface ControllerInterface
{
	public boolean setController(String controller);
	
	public int readController(int axis);
	
	public void setInputMap(HashMap<String, Controller> map);
	
	public void setLinearity(int[] setting);
}
