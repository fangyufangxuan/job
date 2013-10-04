/*
 * =====================================================================================
 *
 *       Filename:  dlist_test.c
 *
 *    Description:  dlist test program
 *
 *        Version:  1.0
 *        Created:  07/21/2013 10:49:19 PM
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  shuai fan, 
 *   Organization:  Dlut
 *
 * =====================================================================================
 */

#include <stdio.h>
#include "dlist.h"

static int print_int(void *data);

int main() {
    int i = 0;
    dlist *list;
    int first = 0;

    list = dlist_new((void*)first);
    dlist_print(list, print_int);
    printf("\n");

    for (i = 1; i < 10; i++) {
        dlist_append(list, (void*)i);
        dlist_print(list, print_int);
        printf("\n");
    }

    for (i = 0; i <= 9; i++) {
        dlist_delete_node(&list, 0);
        dlist_print(list, print_int);
        printf("\n");
    }

    return 0;
}

static int print_int(void *data) {
    printf ("%d ", (int)data);

    return DLIST_RET_OK;
}
