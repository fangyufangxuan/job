#include <stdio.h>
#include <string.h>

void main(int argc, char *argv[])
{
    char str_arr[20] = "hello";
    const char *str = "hello";

    printf("sizeof(str_arr) = %d\n", sizeof(str_arr));
    printf("strlen(str_arr) = %d\n", strlen(str_arr));

    printf("sizeof(\"hello\") = %d\n", sizeof("hello"));
    printf("sizeof(str) = %d\n", sizeof(str));
    printf("strlen(\"hello\") = %d\n", strlen("hello"));
    printf("strlen(str) = %d\n", strlen(str));
}
