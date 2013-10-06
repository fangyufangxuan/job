all: array struct

array: array.c
	gcc -g -o array array.c

struct: struct.c
	gcc -g -o struct struct.c

.PHONY: clean
	rm -rf array struct *.o *.out
