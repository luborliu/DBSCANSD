package boliu.dbscansd;

import java.util.ArrayList;
import java.util.Iterator;

import boliu.util.TrajectoryPoint;

public class DBScanSD {
	
	private ArrayList<Cluster> resultClusters = new ArrayList<Cluster>();
	
	
	/**
	 * apply DBScan algorithm on the data
	 * @param pointsList
	 * @param eps
	 * @param minPoints
	 * @param speed
	 * @param direction
	 * @param rotDifference the max ROT (rate of turn) difference
	 * @param minrot  min ROT that can be regarded as a waypoint or turnpoint
	 * @return
	 */
	public ArrayList<Cluster> applyDBScanSD(ArrayList<TrajectoryPoint> pointsList, double eps, int minPoints, 
			double speed, double direction, boolean isStopPoint) {
		
	      for(int index=0;index<pointsList.size();index++) {
	    	  // we should mark the point as visited, here it is no problem it is because 
	    	  // here we use a for loop , it can stop 	  	    	  
	        ArrayList<TrajectoryPoint> tmpLst = new ArrayList<TrajectoryPoint>();
	        TrajectoryPoint p = pointsList.get(index);
	        if(p.isVisited()&&index!=(pointsList.size()-1)&&index%4096!=0)
	        	continue;
	        tmpLst = isCorePoint(pointsList, p, eps, minPoints, speed, direction, isStopPoint);
	        	        
	        if(tmpLst!=null||index==(pointsList.size()-1)||index%4096==0){
	        	Cluster dbsc = new Cluster();
	        	dbsc.setCluster(tmpLst);
	        	if(tmpLst!=null) {
	        		resultClusters.add(dbsc);
	        	}
	        	int length=resultClusters.size();
				      
				boolean flag = true;
				
				if((index%4096==0)||(index==(pointsList.size()-1)))  { 					
					while(flag) {
				    	  flag = false;
					      for(int i=0;i<length;i++){
					        for(int j=0;j<length;j++){
					          if(i!=j){
					        	  if(i == length) {
					        		  flag = true;
					        		  continue;
					        	  }
					        	  if(mergeClusters(resultClusters.get(i), resultClusters.get(j))) {
					            	resultClusters.remove(j);
					            	j--;
					            	length--;
					        	  }
					          }
					        }
						 }
				      }
				}
	        }
	      }

	      return resultClusters;
		    
	}
	
	
	public boolean mergeClusters(Cluster clusterA, Cluster clusterB) {
		
		boolean merge = false;
		
		if(clusterA.getCluster() == null || clusterB.getCluster() == null) {
			return false;
		} 
		
		for(int index = 0; index < clusterB.getCluster().size(); index++) {
			TrajectoryPoint p = clusterB.getCluster().get(index);
			if(p.isCorePoint() && clusterA.getCluster().contains(p)) {
				merge = true;
				break;
			}
		}
		
		if(merge) {
			for(int index=0; index<clusterB.getCluster().size();index++) {
				if(!clusterA.getCluster().contains(clusterB.getCluster().get(index))) {
					clusterA.getCluster().add(clusterB.getCluster().get(index));
				}
			}
						
		}
		
		return merge;
		
	}
	
	
	
	public ArrayList<TrajectoryPoint> isCorePoint(ArrayList<TrajectoryPoint> lst, TrajectoryPoint p,
			double eps, int minPoints, double speed, double direction, boolean isStopPoint) {
		
		int count = 0;
		ArrayList<TrajectoryPoint> tmpList = new ArrayList<TrajectoryPoint>();
		
		for(Iterator<TrajectoryPoint> it = lst.iterator(); it.hasNext();) {
			TrajectoryPoint q = it.next();
			if(DBScanUtility.isDensityReachable(p, q, eps, minPoints, speed, direction, isStopPoint)) {
				count++;
				if(!tmpList.contains(q)) {
					tmpList.add(q);
				}				
			}		
		}		
		
		if(count>=minPoints) {	// || (((Math.abs(p.getROT()))>=minrot)&&count>=40)) {
			p.setCorePoint(true);
			p.setVisited(true);
			return tmpList;
		}
		
		return null;
	}

}
