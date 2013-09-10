#!/bin/bash
input_file='qunar.txt'
last=`cat $input_file | cut -d' ' -f1 | sort -nr | head -n 2 | tail -n 1`
length=`cat $input_file | wc -l`
i=1
while [ $i -lt $length ]
do
    s=`cat $input_file | sed -n ${i}p | awk '{print $1}'`
    id=`cat $input_file | sed -n ${i}p | awk '{print $2}'`
    if [ $s -ge $last ]
    then
        echo $id
    fi
    i=$(($i+1))
done
