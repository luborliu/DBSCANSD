package boliu.dbscansd;

import boliu.util.TrajectoryPoint;

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
	
	
	
	/**
	 * compare whether two points are directly density-reachable plus ROT factor
	 * @param p1 the first point
	 * @param p2 the second point
	 * @param eps the radius
	 * @param minLns the minimum number of the points included
	 * @param speed the max speed difference
	 * @param direction the max COG difference
	 * @param ROTdifference the max ROT (rate of turn) difference
	 * @param minROT  min ROT that can be regarded as a waypoint or turnpoint
	 * @return true if the two points are directly density-reachable
	 */
	/**
	public static boolean compareDistanceBetweenTwoPointsWithROT(TrajectoryPoint p1, TrajectoryPoint p2,		
			double eps, int minLns, double speed, double direction, double ROTdifference, double minROT, boolean isStopPoint) {
		
		boolean result = false;
		
		if(caculateDistanceBetweenTwoPoints(p1,p2)<=eps) {
			
			if(isStopPoint) {
				result=true;
				return result;
			}
			
			//add the distance calculation between two near stop points(in this case we do not need to consider direction)
			//if(p1.getSOG()<=0.3)
			
			if(Math.abs(p1.getROT())>=minROT) {
				
				if(Math.abs(p2.getROT())>=minROT && Math.abs(p1.getROT()-p2.getROT())<ROTdifference){//ROTdifference) {//??????difference???????????????
					//if(Math.abs(p1.getCOG()-p2.getCOG())<direction) {
						//???????????????direction?????????//????????????speed?????????
						result = true;
				//	}
				}
				
			}
			
//			if(p1.getROT()-p2.getROT()<ROT&&Math.min(Math.abs(p1.getROT()), Math.abs(p2.getROT()))>3) {//3????????????????????? ??????rot???
//				if(Math.abs(p1.getSOG()-p2.getSOG())<speed) {
//					result = true;
//				}
//			}
			
			else if(Math.abs(p1.getCOG()-p2.getCOG())<direction) {
				
				if(Math.abs(p1.getSOG()-p2.getSOG())<speed) {
					if(Math.abs(p1.getROT()-p2.getROT())<ROTdifference) {
						result = true;
					}
				}				
			}
			
		}
		
		return result;		
	}
**/

}
