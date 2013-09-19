/*
 * From: http://www.linuxidc.com/Linux/2012-10/72600.htm
 */
#include <stdio.h>
#include <sys/types.h>
#include <unistd.h>

int main(void)
{
    int i = 0;
    for (i = 0; i < 2; i++)
    {
        fork();
        printf("-");
    }

    return 0;
}
