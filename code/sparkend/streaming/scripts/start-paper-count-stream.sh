#!/bin/bash

# clear mysql `subject_paper_count` table
mysql -u hadoop -p123456 cc_data < ~/scripts/create_paper_count_table.sql

# put data to hdfs
hdfs dfs -rm -r /data/paperCount/*
hdfs dfs -put ~/data/json/* /data/paperCount

# delete check point files
hdfs dfs -rm -r /data/checkpoint/paperCountBySubject/*

# start paper count stream
spark-submit --class cc.Main ~/ccstreaming.jar paperCount