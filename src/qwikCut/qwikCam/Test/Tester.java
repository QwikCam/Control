package qwikCut.qwikCam.Test;

import java.util.List;

import be.teletask.onvif.DiscoveryManager;
import be.teletask.onvif.listeners.DiscoveryListener;
import be.teletask.onvif.listeners.OnvifResponseListener;
import be.teletask.onvif.models.Device;
import be.teletask.onvif.models.OnvifDevice;
import be.teletask.onvif.responses.OnvifResponse;

public class Tester implements OnvifResponseListener
{
	public Tester()
	{
		init();
	}
	
	public void init()
	{
		DiscoveryManager manager = new DiscoveryManager();
		manager.setDiscoveryTimeout(10000);
		manager.discover(new DiscoveryListener() {
		    @Override
		    public void onDiscoveryStarted() {
		        System.out.println("Discovery started");
		    }

		    @Override
		    public void onDevicesFound(List<Device> devices) {
		        for (Device device : devices)
		            System.out.println("Devices found: " + device.getHostName());
		    }
		});
	}

	@Override
	public void onError(OnvifDevice arg0, int arg1, String arg2)
	{
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onResponse(OnvifDevice arg0, OnvifResponse arg1)
	{
		// TODO Auto-generated method stub
		
	}
}
