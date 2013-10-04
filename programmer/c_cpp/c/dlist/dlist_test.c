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

static dlist_ret print_int_cb(void *ctx, void *data);
static dlist_ret sum_cb(void *ctx, void *data);
static dlist_ret max_cb(void *ctx, void *data);

int main() {
    int i = 0;
    dlist *list;
    int first = 0;

    list = dlist_new((void*)first);
    dlist_foreach(list, print_int_cb, NULL);
    printf("\n");

    for (i = 1; i < 10; i++) {
        dlist_append(list, (void*)i);
        dlist_foreach(list, print_int_cb, NULL);
        printf("\n");
    }

    long long sum = 0;
    dlist_foreach(list, sum_cb, &sum);
    printf("\nsum = %lld\n", sum);

    int max = 0;
    dlist_foreach(list, max_cb, &max);
    printf("max = %d\n\n", max);

    for (i = 0; i <= 9; i++) {
        dlist_delete_node(&list, 0);
        dlist_foreach(list, print_int_cb, NULL);
        printf("\n");
    }

    return 0;
}

static dlist_ret print_int_cb(void *ctx, void *data) {
    printf ("%d ", (int)data);

    return DLIST_RET_OK;
}

static dlist_ret sum_cb(void *ctx, void *data) {
    long long *result = ctx;
    *result += (int)data;

    return DLIST_RET_OK;
}

static dlist_ret max_cb(void *ctx, void *data) {
    int *result = ctx;
    if ((int)data > *result) {
        *result = (int)data;
    }

    return DLIST_RET_OK;
}
