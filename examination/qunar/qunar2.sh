#!/bin/bash
input_file='qunar.txt'
lines=2
cat $input_file | sort -k 1nr | head -n $lines | awk '{print $2}'
