package boliu.dbscansd;
import java.util.ArrayList;


public class Main {
	
	
	public static void main(String[] args) {
		
		//executeAlgorithm("src/toy_data.csv","src/test_output",120000, 0.03, 50, 2, 2.5, false);
		if(args.length==8) {
			try{
				int lineNum = Integer.parseInt(args[2]);
				double eps = Double.parseDouble(args[3]);
				int minPts = Integer.parseInt(args[4]);
				double maxSpd = Double.parseDouble(args[5]);
				double maxDir = Double.parseDouble(args[6]);
				boolean isStopPoint = Integer.parseInt(args[7])==0?false:true;
				executeAlgorithm(args[0], args[1], lineNum, eps, minPts, maxSpd, maxDir, isStopPoint);
			} catch(NumberFormatException e) {
				//e.printStackTrace();
				System.out.println("Please check your input parameters are in correct format and order. ");
			}
			
		} else {
			System.out.println("Please run the program with 8 input parameters:\n"
							+ "args[0]: the input file path\n"
							+ "args[1]: the output file path\n"
							+ "args[2]: the designated number of trajectory points for clustering\n"
							+ "args[3]: eps  	   1st parameter of DBSCANSD, the radius\n"
							+ "args[4]: minPoints 2nd parameter of DBSCANSD, the minimum number of points\n"
							+ "args[5]: maxSpd	   3rd parameter of DBSCANSD, the maximum Speeds' difference\n"
							+ "args[6]: maxDir	   4th parameter of DBSCANSD, the maximum Directions' difference\n"
							+ "args[7]: boolean value (0/1), if you would like to cluster stopping points (1) or moving points (0)\n"
							+ "e.g. java Main toy_data.csv output 70000 0.03 50 2 2.5 1\n");
		}
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
		ArrayList<TrajectoryPoint> points = FileIO.readFile(inPath,lineNum, isStopPoint);
			
		DBScanSD dbs = new DBScanSD();
		
		
		ArrayList<Cluster> clusteringResults = dbs.applyDBScanSD(points, eps, minPoints, maxSpd, maxDir,isStopPoint); 
	
		System.gc();
		
		for(int i=0; i<clusteringResults.size(); i++) {
			
			if(isStopPoint) {
				ArrayList<TrajectoryPoint> ppl = clusteringResults.get(i).getCluster();	
				FileIO.writeClustersToFile(outPath+"_stoppingclusters.csv", ppl, i);
			} else {			
				ArrayList<GravityVector> ppl = GravityVectorExtraction.extractGravityVector(clusteringResults.get(i));
				FileIO.writeGVsToFile(outPath+"_gv.csv", ppl, i);
				ArrayList<TrajectoryPoint> tpl = clusteringResults.get(i).getCluster();
				FileIO.writeClustersToFile(outPath+"_movingclusters.csv", tpl, i);
			}
		}
		
				
	}

}
