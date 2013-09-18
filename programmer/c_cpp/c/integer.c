#include <stdio.h>

int foo(int n)
{
    return n & -n;
}

void main(int argc, char *argv[])
{
    int a = 0x80000000;
    int b = 3;

    printf("%d\n", foo(a - b));
}
