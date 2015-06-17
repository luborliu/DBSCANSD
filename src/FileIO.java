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

import boliu.util.GravityVector;
import boliu.util.TrajectoryPoint;

public class FileIO {
	
	

	public static ArrayList<TrajectoryPoint> readFile(String path, int readLinesNum) {
		
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
			while((recordLine = inBuffer.readLine())!= null&& countt<readLinesNum) {//????????????50000
				//countt++; move it to below such that we can ignore those stop points
				
				Scanner sc = new Scanner(recordLine);
				sc.useDelimiter("\",\"");
				
				String MMSI = null;
				String Message_ID = null;
				
				
				if (sc.hasNext()) { 

                    String firstElement = sc.next();
					if(firstElement.equals("MMSI,Message_ID,Repeat_indicator,Time,Millisecond,Region,Country,Base_station,Online_data,Group_code,Sequence_ID,Channel,Data_length,Vessel_Name,Call_sign,IMO,Ship_Type,Dimension_to_Bow,Dimension_to_stern,Dimension_to_port,Dimension_to_starboard,Draught,Destination,AIS_version,Navigational_status,ROT,SOG,Accuracy,Longitude,Latitude,COG,Heading,Regional,Maneuver,RAIM_flag,Communication_flag,Communication_state,UTC_year,UTC_month,UTC_day,UTC_hour,UTC_minute,UTC_second,Fixing_device,Transmission_control,ETA_month,ETA_day,ETA_hour,ETA_minute,Sequence,Destination_ID,Retransmit_flag,Country_code,Functional_ID,Data,Destination_ID_1,Sequence_1,Destination_ID_2,Sequence_2,Destination_ID_3,Sequence_3,Destination_ID_4,Sequence_4,Altitude,Altitude_sensor,Data_terminal,Mode,Safety_text,Non-standard_bits,Name_extension,Name_extension_padding,Message_ID_1_1,Offset_1_1,Message_ID_1_2,Offset_1_2,Message_ID_2_1,Offset_2_1,Destination_ID_A,Offset_A,Increment_A,Destination_ID_B,offsetB,incrementB,data_msg_type,station_ID,Z_count,num_data_words,health,unit_flag,display,DSC,band,msg22,offset1,num_slots1,timeout1,Increment_1,Offset_2,Number_slots_2,Timeout_2,Increment_2,Offset_3,Number_slots_3,Timeout_3,Increment_3,Offset_4,Number_slots_4,Timeout_4,Increment_4,ATON_type,ATON_name,off_position,ATON_status,Virtual_ATON,Channel_A,Channel_B,Tx_Rx_mode,Power,Message_indicator,Channel_A_bandwidth,Channel_B_bandwidth,Transzone_size,Longitude_1,Latitude_1,Longitude_2,Latitude_2,Station_Type,Report_Interval,Quiet_Time,Part_Number,Vendor_ID,Mother_ship_MMSI,Destination_indicator,Binary_flag,GNSS_status,spare,spare2,spare3,spare4")) {
						System.out.println("record is title");
						continue;
					} else
						MMSI = firstElement.substring(1);
					
			
					Message_ID = sc.next();				
					String Repeat_indicator = sc.next();
					String Time =sc.next().trim();
					String Millisecond = sc.next().trim();
					String Region = sc.next();
					String Country = sc.next();
					String Base_station = sc.next();
					String Online_data = sc.next();
					String Group_code = sc.next();
					String Sequence_ID = sc.next();
					String Channel = sc.next();
					String Data_length = sc.next();
					String Vessel_Name = sc.next();
					String Call_sign = sc.next();
					String IMO = sc.next();						
					String Ship_Type = sc.next();
				    String Dimension_to_Bow = sc.next();
					String Dimension_to_stern = sc.next();
					String Dimension_to_port = sc.next();
					String Dimension_to_starboard = sc.next();
					String Draught = sc.next();
					String Destination = sc.next();
					String AIS_version = sc.next();
					String Navigational_status = sc.next();
					String ROT = sc.next().trim();
					String SOG = sc.next().trim();
					String Accuracy = sc.next();
					String Longitude = sc.next().trim();
					String Latitude = sc.next().trim();
					String COG = sc.next().trim();
							
					TrajectoryPoint p = new TrajectoryPoint();
					p.setMmsi(MMSI);
					//TrajectoryPoint.setLongtitude(Double.parseDouble(Longitude)*3+400);
					//TrajectoryPoint.setLatitude(Double.parseDouble(Latitude)*3);
					
					if(COG.equals("None")) {
						COG = "0.0";
					} 
					if (ROT.equals("None")) {
						ROT = "0.0";
					} 
					if (SOG.equals("None")) {
						SOG = "0.0";
					}
			//this condition is to ignore those stop points(with speed <= 0.3)
			if(Double.parseDouble(SOG)<=0.5) { //remember to change it to 0.3
				continue;
			}
					
					countt++;
					
					
					p.setLongitude(Double.parseDouble(Longitude));
					p.setLatitude(Double.parseDouble(Latitude));
					p.setCOG(Double.parseDouble(COG));
					//p.setROT(Double.parseDouble(ROT));
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




	public static ArrayList<TrajectoryPoint> readFileOfStopPoints(String path, int readLinesNum) {
	
		DataInputStream in;
		BufferedReader inBuffer = null;
		ArrayList<TrajectoryPoint> ssAL = new ArrayList<TrajectoryPoint>();
		//ArrayList<ArrayList<TrajectoryPoint>> trajectoriesAL = new ArrayList<ArrayList<TrajectoryPoint>>();	
	
						
		try {
			in = new DataInputStream(new BufferedInputStream(   
			        new FileInputStream(path)));						
			
			inBuffer = new BufferedReader(   
	                new InputStreamReader(in,"utf-8"),5*1024*1024);
			
			String recordLine = null;			
			
			int countt=0;
			while((recordLine = inBuffer.readLine())!= null&& countt<readLinesNum) {//????????????50000
				//countt++; move it to below such that we can ignore those stop points
				
				Scanner sc = new Scanner(recordLine);
				sc.useDelimiter("\",\"");
				
				String MMSI = null;
				String Message_ID = null;
				
				
				if (sc.hasNext()) { 
	
	                String firstElement = sc.next();
					if(firstElement.equals("MMSI,Message_ID,Repeat_indicator,Time,Millisecond,Region,Country,Base_station,Online_data,Group_code,Sequence_ID,Channel,Data_length,Vessel_Name,Call_sign,IMO,Ship_Type,Dimension_to_Bow,Dimension_to_stern,Dimension_to_port,Dimension_to_starboard,Draught,Destination,AIS_version,Navigational_status,ROT,SOG,Accuracy,Longitude,Latitude,COG,Heading,Regional,Maneuver,RAIM_flag,Communication_flag,Communication_state,UTC_year,UTC_month,UTC_day,UTC_hour,UTC_minute,UTC_second,Fixing_device,Transmission_control,ETA_month,ETA_day,ETA_hour,ETA_minute,Sequence,Destination_ID,Retransmit_flag,Country_code,Functional_ID,Data,Destination_ID_1,Sequence_1,Destination_ID_2,Sequence_2,Destination_ID_3,Sequence_3,Destination_ID_4,Sequence_4,Altitude,Altitude_sensor,Data_terminal,Mode,Safety_text,Non-standard_bits,Name_extension,Name_extension_padding,Message_ID_1_1,Offset_1_1,Message_ID_1_2,Offset_1_2,Message_ID_2_1,Offset_2_1,Destination_ID_A,Offset_A,Increment_A,Destination_ID_B,offsetB,incrementB,data_msg_type,station_ID,Z_count,num_data_words,health,unit_flag,display,DSC,band,msg22,offset1,num_slots1,timeout1,Increment_1,Offset_2,Number_slots_2,Timeout_2,Increment_2,Offset_3,Number_slots_3,Timeout_3,Increment_3,Offset_4,Number_slots_4,Timeout_4,Increment_4,ATON_type,ATON_name,off_position,ATON_status,Virtual_ATON,Channel_A,Channel_B,Tx_Rx_mode,Power,Message_indicator,Channel_A_bandwidth,Channel_B_bandwidth,Transzone_size,Longitude_1,Latitude_1,Longitude_2,Latitude_2,Station_Type,Report_Interval,Quiet_Time,Part_Number,Vendor_ID,Mother_ship_MMSI,Destination_indicator,Binary_flag,GNSS_status,spare,spare2,spare3,spare4")) {
						System.out.println("record is title");
						continue;
					} else
						MMSI = firstElement.substring(1);
					
					Message_ID = sc.next();				
					String Repeat_indicator = sc.next();
					String Time =sc.next().trim();
					String Millisecond = sc.next().trim();
					String Region = sc.next();
					String Country = sc.next();
					String Base_station = sc.next();
					String Online_data = sc.next();
					String Group_code = sc.next();
					String Sequence_ID = sc.next();
					String Channel = sc.next();
					String Data_length = sc.next();
					String Vessel_Name = sc.next();
					String Call_sign = sc.next();
					String IMO = sc.next();						
					String Ship_Type = sc.next();
				    String Dimension_to_Bow = sc.next();
					String Dimension_to_stern = sc.next();
					String Dimension_to_port = sc.next();
					String Dimension_to_starboard = sc.next();
					String Draught = sc.next();
					String Destination = sc.next();
					String AIS_version = sc.next();
					String Navigational_status = sc.next();
					String ROT = sc.next().trim();
					String SOG = sc.next().trim();
					String Accuracy = sc.next();
					String Longitude = sc.next().trim();
					String Latitude = sc.next().trim();
					String COG = sc.next().trim();
							
					TrajectoryPoint p = new TrajectoryPoint();
					p.setMmsi(MMSI);
					//TrajectoryPoint.setLongtitude(Double.parseDouble(Longitude)*3+400);
					//TrajectoryPoint.setLatitude(Double.parseDouble(Latitude)*3);
					
					if(COG.equals("None")) {
						COG = "0.0";
					} 
					if (ROT.equals("None")) {
						ROT = "0.0";
					} 
					if (SOG.equals("None")) {
						SOG = "0.0";
					}
			//this condition is to ignore those moving points(with speed > 0.5)
			if(Double.parseDouble(SOG)>0.5) { //remember to change it to 0.3
				continue;
			}
					
					countt++;
					
					
					p.setLongitude(Double.parseDouble(Longitude));
					p.setLatitude(Double.parseDouble(Latitude));
					p.setCOG(Double.parseDouble(COG));
					//p.setROT(Double.parseDouble(ROT));
					p.setSOG(Double.parseDouble(SOG));
					p.setTimestamp(timeToSecondsNum(Time));
					
					ssAL.add(p);
	
				}
			}
																			
			
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
				bw.write(clusterindex+","+ppl.get(i).getLongitude()+","+ppl.get(i).getLatitude()+","+ppl.get(i).getSOG()+","+ppl.get(i).getCOG()+","+ppl.get(i).getThirdQuartileDistance()+"\n");
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
