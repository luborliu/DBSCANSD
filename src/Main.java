import java.util.ArrayList;

import boliu.dbscansd.*;
import boliu.util.GravityVector;
import boliu.util.TrajectoryPoint;


public class Main {
	
	
	public static void main(String[] args) {
		
		executeAlgorithm("src/201302-04jdfkportdata.csv","src/movingClustersJuanDeFuca_dir2.5",120000, 0.03, 50, 2, 2.5, false);

	}
	
	
	
	
	
	/**
	 *  
	 * @param inPath the input file path
	 * @param outPath the output file path
	 * @param lineNum the designated number of trajectory points for clustering
	 * @param eps  1st parameter of DBSCANSD, the radius
	 * @param minPoints 2nd parameter of DBSCANSD, the minimum number of points
	 * @param maxSpd	3rd parameter of DBSCANSD, the maximum SOG difference
	 * @param maxDir	4th parameter of DBSCANSD, the maximum COG difference
	 * @param isStopPoint	boolean condition, if the trajectory points are stopping points
	 */
	private static void executeAlgorithm(String inPath, String outPath, int lineNum, double eps, int minPoints, double maxSpd, double maxDir, boolean isStopPoint) {
		//Runtime rt = Runtime.getRuntime();
		ArrayList<TrajectoryPoint> points = new ArrayList<TrajectoryPoint>();
		if(!isStopPoint) {
			points = FileIO.readFile(inPath,lineNum);
		} else {
			points = FileIO.readFileOfStopPoints(inPath,lineNum);
		}
			
		DBScanSD dbs = new DBScanSD();
		
		
		ArrayList<Cluster> clusteringResults = dbs.applyDBScanSD(points, eps, minPoints, maxSpd, maxDir,isStopPoint); 
	
		System.gc();
		
		for(int i=0; i<clusteringResults.size(); i++) {
			
			if(isStopPoint) {
				ArrayList<TrajectoryPoint> ppl = clusteringResults.get(i).getCluster();	
				FileIO.writeClustersToFile(outPath, ppl, i);
			} else {			
				ArrayList<GravityVector> ppl = GravityVectorExtraction.extractGravityVector(clusteringResults.get(i));
				FileIO.writeGVsToFile(outPath+"_gv", ppl, i);
				ArrayList<TrajectoryPoint> tpl = clusteringResults.get(i).getCluster();
				FileIO.writeClustersToFile(outPath+"_original", tpl, i);
			}
		}
		
		//DBScanFrameExtrLA dbsframeEm = new DBScanFrameExtrLA(resultClusters, points, 125100, -48000, outPath,isStopPoint);
		//System.out.println(rt.freeMemory());
				
	}

}
