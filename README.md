# DBSCANSD
Java implementation for DBSCANSD, a trajectory clustering algorithm.

# Brief Introduction

DBSCANSD (Density-Based Spatial Clustering of Applicationswith Noise considering Speed and Direction)[1] is a clustering algorithm extended from DBSCAN [2]. It can consider speed and direction, which is essential for maritime lanes extraction. The output of this algorithm is a set of Gravity Vectors (GV) and  Sampled Stopping Points (SSP). 

In the present version, the implementation has not included generating SSP yet, but I shall add this part later.

Since the AIS data provided for this project is confidential, I cannot upload it to github as example. But I generated a toy data set and put it in the src folder which can be tested with the program. And it will be great if you use this algorithm for other domains' problems, such as tracking data of vehicles, pedestrian, hurricane or animals. 

More details about this algorithm can be found in [1]. The link is as following:

http://ieeexplore.ieee.org/xpls/abs_all.jsp?arnumber=7004281

# How To Run The Code ?

After downloading it to local, 

1. cd to the folder of src/boliu/dbscansd/

2. compile all the .java files using:

    javac *.java

3. cd to the folder of src/

4. execute the program using the following either command:

      java boliu.dbscansd.Main inputfile outputfile eps minPts maxSpd maxDir isStop 
    
        --e.g. java boliu.dbscansd.Main toy_data.csv output 70000 0.03 50 2 2.5 0
        
        In this way, the program will do the job on toy_data.csv file. 
        It will first extract the first 70,000 moving points from the data and then run DBSCANSD on the dataset. 
        The final output will be two files: 
          output_gv.csv (gravity vectors) and output_movingclusters.csv (original clustering results with more rows).
    


5. waiting for the result :)  The running time will vary with different sizes of the input data and other input parameters.

6. Star it if it helps  \*-\*


# Reference
[1] Liu, Bo, et al. "Knowledge-based clustering of ship trajectories using density-based approach." Big Data (Big Data), 2014 IEEE International Conference on. IEEE, 2014.

[2] Ester, Martin, et al. "A density-based algorithm for discovering clusters in large spatial databases with noise." Kdd. Vol. 96. No. 34. 1996.
