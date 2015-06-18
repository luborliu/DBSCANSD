package boliu.dbscansd;

import java.util.ArrayList;

import boliu.util.TrajectoryPoint;

/**
 * Cluster class, each cluster includes a set of trajectory points
 * @author Bo Liu
 *
 */
public class Cluster {
	
	private ArrayList<TrajectoryPoint> cluster ;
	
	private double avgSOG;	//the average speed of the whole cluster, not useful so far
	private double avgCOG;	//the average direction of the whole cluster, which is useful for calculating the GV
	
	public Cluster() {
	}

	public ArrayList<TrajectoryPoint> getCluster() {
		return cluster;
	}

	public void setCluster(ArrayList<TrajectoryPoint> cluster) {
		this.cluster = cluster;
	}

	public double getAvgSOG() {
		return avgSOG;
	}

	public void setAvgSOG(double avgSOG) {
		this.avgSOG = avgSOG;
	}

	public double getAvgCOG() {
		return avgCOG;
	}

	public void setAvgCOG(double avgCOG) {
		this.avgCOG = avgCOG;
	}
	
	
	public double calculateAverageDirection() {
		double sum = 0;
		
		double maxCOG = -1000;
		double minCOG = 1000;
		
		for(int i=0; i<this.cluster.size();i++) {
			sum = sum+this.cluster.get(i).getCOG();
			
			if(this.cluster.get(i).getCOG()>maxCOG) {
				maxCOG = this.cluster.get(i).getCOG();
			}
			if(this.cluster.get(i).getCOG()<minCOG) {
				minCOG = this.cluster.get(i).getCOG();
			}
			
		}
		
		double avg = sum/(double)(this.cluster.size());
		
	System.out.println("maxCOG: "+maxCOG+" minCOG: "+minCOG);
		
		return avg;
		
	}
	
	
	



}
