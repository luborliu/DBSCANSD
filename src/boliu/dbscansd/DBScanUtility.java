package boliu.dbscansd;


public class DBScanUtility {
	
	/**
	 * calculate the distance between two points based on their longitude and latitude
	 * @param p1 point 1
	 * @param p2 point 2
	 */
	private static double caculateDistanceBetweenTwoPoints(TrajectoryPoint p1, TrajectoryPoint p2) {
	
		
		double dx=p1.getLongitude()-p2.getLongitude();
		double dy=p1.getLatitude()-p2.getLatitude();
		
		double distance=Math.sqrt(dx*dx+dy*dy);
		
		return distance;		
	}
	
	/**
	 * compare whether two points are directly density-reachable
	 * @param p1 the first point
	 * @param p2 the second point
	 * @param eps the radius
	 * @param minPts the minimum number of the points included
	 * @param maxSpd the max speed difference
	 * @param maxDir the max COG difference
	 * @return TRUE if the two points are directly density-reachable
	 */
	public static boolean isDensityReachable(TrajectoryPoint p1, TrajectoryPoint p2,		
			double eps, int minPts, double maxSpd, double maxDir, boolean isStopPoint) {
		
		boolean result = false;
		
		if(caculateDistanceBetweenTwoPoints(p1,p2)<=eps) {
			//if they are stopping points, we can directly use original 
			//DBSCAN algorithm without considering speed or direction
			if(isStopPoint) {	
				return true;
			}			
			if(Math.abs(p1.getCOG()-p2.getCOG())<maxDir) {				
				if(Math.abs(p1.getSOG()-p2.getSOG())<maxSpd) {
					result = true;
				}				
			}			
		}		
		return result;		
	}

}
