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
 *         Author:  YOUR NAME (shuai fan), 
 *   Organization:  Dlut
 *
 * =====================================================================================
 */

#include <stdio.h>
#include "dlist.h"

int main() {
    int i = 0;
    dlist *list;

    list = dlist_new(0);

    for (i = 1; i < 10; i++) {
        dlist_append(list, i);
    }

    dlist_print(list);

    for (i = 0; i <= 9; i++) {
        dlist_delete_node(&list, i);
        dlist_print(list);
    }

    return 0;
}
