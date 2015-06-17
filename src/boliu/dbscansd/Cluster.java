package boliu.dbscansd;

import java.util.ArrayList;

import boliu.util.TrajectoryPoint;

public class Cluster {
	
	private ArrayList<TrajectoryPoint> cluster ;
	
	private double avgSOG;
	private double avgCOG;		//之后可以考虑在这里进行改进 改成representative points of the cluster

	
	//增加一个数组存储那些corepoints 的position 方便后期查找
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
