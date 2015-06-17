package boliu.dbscansd;

import java.util.ArrayList;
import java.util.Arrays;

import boliu.dbscansd.Cluster;
import boliu.util.GravityVector;
import boliu.util.MappingPoint;


public class GravityVectorExtraction {
	
	/**
	 * extract the GVs from the moving cluster
	 * @param cluster
	 * @return an arraylist of GVs
	 */
	public static ArrayList<GravityVector> extractGravityVector(Cluster cluster) {
		
		double avgCOG = cluster.calculateAverageDirection();
		ArrayList<MappingPoint> mpLst = new ArrayList<MappingPoint>();
		for(int i=0; i<cluster.getCluster().size(); i++) {	
			MappingPoint mp = MappingPoint.convertPointToMappingPoint(cluster.getCluster().get(i), avgCOG);
			mpLst.add(mp);
		}
		
		insertionSort(mpLst);
		
		ArrayList<GravityVector> ppL = new ArrayList<GravityVector>();
		int count = 0;
		int k = 0;
		double sum_x = 0;
		double sum_y = 0;
		double sum_SOG = 0;
		double sum_COG = 0;
		
		ArrayList<MappingPoint> traPointsTMP = new ArrayList<MappingPoint>();
		double medianDistance = 0;
		
		while(count<mpLst.size()) {
			if(mpLst.get(count).getMappingtude()-mpLst.get(k).getMappingtude()<0.01) {//0.1作为平分距离
				sum_x = sum_x+mpLst.get(count).getLongitude();
				sum_y = sum_y+mpLst.get(count).getLatitude();
				
				sum_SOG = sum_SOG+mpLst.get(count).getSOG();
				sum_COG = sum_COG+mpLst.get(count).getCOG();
				
				
				
				traPointsTMP.add(mpLst.get(count));
				
				count++;
			} else {
				double x = 0;
				double y = 0;
				
				double sog = 0;
				double cog = 0;
				if(count==k) {
					x = mpLst.get(count).getLongitude();
					y = mpLst.get(count).getLatitude();
					sog = mpLst.get(count).getSOG();
					cog = mpLst.get(count).getCOG();
					medianDistance = 1;
					count++;
				} else {				
					x = sum_x/(double)(count-k);
					y = sum_y/(double)(count-k);
					
					sog = sum_SOG/(double)(count-k);
					cog = sum_COG/(double)(count-k);

					//insert median distance calculation					
					double[] distances = new double[traPointsTMP.size()];
					for(int i=0; i<traPointsTMP.size();i++) {
						double lon=traPointsTMP.get(i).getLongitude();
						double lat=traPointsTMP.get(i).getLatitude();
						double dist = gpsDistance(lat, lon, y, x);
						distances[i]=dist;
						
					}
					//medianDistance
					medianDistance = quartile(distances, 50);
					//end insert median distance calculation					
				}
				GravityVector gv = new GravityVector(x,y,cog,sog,medianDistance);
				
				ppL.add(gv);
				
				sum_x = 0;
				sum_y = 0;
				sum_COG = 0;
				sum_SOG = 0;
				k = count;
				traPointsTMP.clear();
			}
			
		}
	
		double x = sum_x/(double)(count-k);
		double y = sum_y/(double)(count-k);
		double sog = sum_SOG/(double)(count-k);
		double cog = sum_COG/(double)(count-k);

		//add median distance
		
		double[] distances = new double[traPointsTMP.size()];
		for(int i=0; i<traPointsTMP.size();i++) {
			double lon=traPointsTMP.get(i).getLongitude();
			double lat=traPointsTMP.get(i).getLatitude();
			double dist = gpsDistance(lat, lon, y, x);
			distances[i]=dist;			
		}
		
		double secondQuartileDistance = quartile(distances, 50);
		
		GravityVector pp = new GravityVector(x, y, cog, sog, secondQuartileDistance);
		ppL.add(pp);
		return ppL;
		
	}
	
	public static void insertionSort(ArrayList<MappingPoint> mpl) {
		
		for(int i=1; i<mpl.size(); i++) {
			int k = i;
			MappingPoint mp = mpl.get(i);
			boolean insertAlready = false;
			while(mpl.get(i).getMappingtude()<mpl.get(k-1).getMappingtude()) {
				if(k==1) {
					
					mpl.remove(i);
					mpl.add(0, mp);
					insertAlready = true;
					break;
				}
				k--;				
			}
			
			if(!insertAlready) {
				mpl.remove(i);
				mpl.add(k,mp);
			}
			
		}
		
	}
	 /**
     * Retrive the quartile value from an array, used for generating relative distance.
     * @param values THe array of data
     * @param lowerPercent The percent cut off. For the lower quartile use 25,
     *        for the upper-quartile use 75
     * @return the quartile value
     */
	public static double quartile(double[] values, double lowerPercent) {

        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        }

        // order the values
        double[] v = new double[values.length];
        System.arraycopy(values, 0, v, 0, values.length);
        Arrays.sort(v);
        int n=0;
        if(v.length==1) {
        	n = 0;
        }
        else n = (int) Math.round(v.length * lowerPercent / 100);
        
        System.out.println(v.length+","+n);
        
        return v[n];

    }
	/**
	 * calculate gps distance between two points
	 * @param lat1 latitude1
	 * @param lng1 longitude1
	 * @param lat2 latitude2
	 * @param lng2 latitude2
	 * @return the gps distance between the two trajectory points 
	 */
	public static double gpsDistance(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    int meterConversion = 1609;

	    return (double) (dist * meterConversion);
	}
	

}
