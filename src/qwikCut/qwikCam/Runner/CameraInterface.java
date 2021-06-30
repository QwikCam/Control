package qwikCut.qwikCam.Runner;

public interface CameraInterface
{
	public int setConnectionInfo(String ip, String user, String pass);
	
	public boolean hasConnection();
	
	public String getCameraInfo();
	
	public String getStreamURL();
	
	public void setSpeedLimits(int pan, int tilt, int zoon);
}
