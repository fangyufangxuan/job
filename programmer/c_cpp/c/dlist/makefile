all: dlist.so dlist_int dlist_string

dlist.so:
	gcc --shared -o dlist.so dlist.c

dlist_int:
	gcc -g -o dlist_int dlist_int.c dlist.so

dlist_string:
	gcc -g -o dlist_string dlist_string.c dlist.so

clean:
	rm -rf *.o dlist.so dlist_int dlist_string
