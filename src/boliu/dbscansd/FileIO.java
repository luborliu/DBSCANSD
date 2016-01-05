package boliu.dbscansd;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class FileIO {
	
	public static ArrayList<TrajectoryPoint> readFile(String path, int readLinesNum, boolean isStoppingPoint) {
		
		DataInputStream in;
		BufferedReader inBuffer = null;
		ArrayList<TrajectoryPoint> ssAL = new ArrayList<TrajectoryPoint>();
						
		try {
			in = new DataInputStream(new BufferedInputStream(   
			        new FileInputStream(path)));						
			
			inBuffer = new BufferedReader(   
	                new InputStreamReader(in,"utf-8"),5*1024*1024);
			
			String recordLine = null;			
			
			int countt=0;
			while((recordLine = inBuffer.readLine())!= null&& countt<readLinesNum) {			
				Scanner sc = new Scanner(recordLine);
				sc.useDelimiter(",");
				
				String MMSI = null;
				
				
				if (sc.hasNext()) { 
                    String firstElement = sc.next();
					if(firstElement.equals("MMSI")) {
						System.out.println("start loading data...");
						continue;
					} else
						MMSI = firstElement.substring(0);
								
					String Time =sc.next().trim();
					String SOG = sc.next().trim();
					String Longitude = sc.next().trim();
					String Latitude = sc.next().trim();
					String COG = sc.next().trim();
							
					TrajectoryPoint p = new TrajectoryPoint();
					p.setMmsi(MMSI);
					
					if(COG.equals("None")) {
						COG = "0.0";
					} 
					if (SOG.equals("None")) {
						SOG = "0.0";
					}
					if(!isStoppingPoint&&Double.parseDouble(SOG)<=0.5)	continue;
					if(isStoppingPoint&&Double.parseDouble(SOG)>0.5) continue;					
					countt++;
					
					p.setLongitude(Double.parseDouble(Longitude));
					p.setLatitude(Double.parseDouble(Latitude));
					p.setCOG(Double.parseDouble(COG));
					p.setSOG(Double.parseDouble(SOG));
					p.setTimestamp(timeToSecondsNum(Time));
					
					ssAL.add(p);
						
	
				}
			}
			System.out.println("read "+countt+" lines");																
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Unable to open input file");
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			try {
				inBuffer.close();				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}        		
		return ssAL;
		
	}


	public static long timeToSecondsNum(String time) {
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date dt2 = null;
		try {
			dt2 = sdf.parse(time);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 1351738800
		long lTime = dt2.getTime()/1000-1351738800;
		return lTime;
	}



	/**
	 * write GVs of the moving cluster to File
	 * @param outPath
	 * @param ppl
	 * @param clusterindex
	 */
	public static void writeGVsToFile(String outPath, ArrayList<GravityVector> ppl, int clusterindex) {
				
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		OutputStreamWriter osw = null;
		try {	
			File file = new File(outPath);
			fos = new FileOutputStream(outPath, true);
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			
			if (file.exists()&&file.length()==0) {														
				bw.write("clusterindex,Longitude,Latitude,SOG,COG,MedianDistance\n");
			}	
			
			for(int i=0; i<ppl.size(); i++) {
				bw.write(clusterindex+","+ppl.get(i).getLongitude()+","+ppl.get(i).getLatitude()+","+ppl.get(i).getSOG()+","+ppl.get(i).getCOG()+","+ppl.get(i).getMedianDistance()+"\n");
			}
				
				//System.out.println("I: "+countI+" E: "+countE);
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Unable to open input file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * write the original clustering result to file
	 * @param outPath
	 * @param ppl
	 * @param clusterindex
	 */
	public static void writeClustersToFile(String outPath, ArrayList<TrajectoryPoint> ppl, int clusterindex) {
		
		
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		OutputStreamWriter osw = null;
		try {	
			File file = new File(outPath);
			fos = new FileOutputStream(outPath, true);
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			
			if (file.exists()&&file.length()==0) {														
				bw.write("clusterindex,Longitude,Latitude,SOG,COG\n");
			}	
			
			for(int i=0; i<ppl.size(); i++) {
				bw.write(clusterindex+","+ppl.get(i).getLongitude()+","+ppl.get(i).getLatitude()+","+ppl.get(i).getSOG()+","+ppl.get(i).getCOG()+"\n");
			}
				
				//System.out.println("I: "+countI+" E: "+countE);
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Unable to open input file");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
