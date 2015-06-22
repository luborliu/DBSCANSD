package boliu.util;

import boliu.util.TrajectoryPoint;
/**
 * Gravity Vector class, it includes lon, lat, sog, cog and median distance
 * @author Bo Liu
 *
 */
public class GravityVector extends TrajectoryPoint{
	
	private double medianDistance;
	
	
	public double getMedianDistance() {
		return medianDistance;
	}

	public void setMedianDistance(double medianDistance) {
		this.medianDistance = medianDistance;
	}

	
	/** 
	 * @param longitude
	 * @param latitude
	 * @param COG
	 * @param SOG
	 * @param medianDistance
	 */
	public GravityVector(double longitude, double latitude, double COG, double SOG, double medianDistance) {
		
		this.latitude = latitude;
		this.longitude = longitude;
		this.COG = COG;
		this.SOG = SOG;
		this.medianDistance = medianDistance;
		
	}
	
	
	
	

}
