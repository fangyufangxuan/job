#!/bin/bash
input_file='qunar.txt'
cat $input_file | sort -k 1nr | head -n 2
