# DBSCANSD
Java implementation for DBSCANSD, a trajectory clustering algorithm.

# Brief Introduction

DBSCANSD (Density-Based Spatial Clustering of Applicationswith Noise considering Speed and Direction)[1] is a clustering algorithm extended from DBSCAN [2]. It can consider speed and direction, which is essential for maritime lanes extraction. The output of this algorithm is a set of Gravity Vectors (GV) and  Sampled Stopping Points (SSP). 

In the present verstion, the implementation has not included generating SSP yet, but I shall add this part later.

Since the AIS data provided for this project is confidential, I cannot upload it to github as example. But it will be great if you use this algorithm for other domains' problems, such as tracking data of vehicles, pedestrian, hurricane or animals. 

More details about this algorithm can be found in [1].

# Reference
[1] Liu, Bo, et al. "Knowledge-based clustering of ship trajectories using density-based approach." Big Data (Big Data), 2014 IEEE International Conference on. IEEE, 2014.

[2] Ester, Martin, et al. "A density-based algorithm for discovering clusters in large spatial databases with noise." Kdd. Vol. 96. No. 34. 1996.
