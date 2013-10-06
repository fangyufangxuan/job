/*
 * =====================================================================================
 *
 *       Filename:  struct.c
 *
 *    Description:  struct test
 *
 *        Version:  1.0
 *        Created:  10/06/2013 09:09:04 PM
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  shuai fan,
 *   Organization:  
 *
 * =====================================================================================
 */

#include <stdio.h>

struct st {
    int one;
    char two;
};

typedef struct st st;

int main(int argc, char *argv[]) {
    st s = { .one = 1, .two = 'a' };

    printf("one = %d, two = %c\n", s.one, s.two);

    return 0;
}
