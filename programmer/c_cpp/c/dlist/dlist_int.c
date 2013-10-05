/*
 * =====================================================================================
 *
 *       Filename:  dlist_int.c
 *
 *    Description:  dlist test program(int)
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

    list = dlist_new();

    for (i = 0; i < 10; i++) {
        dlist_append(list, (void*)i);
        printf("count = %2d,\t", dlist_count(list));
        dlist_foreach(list, print_int_cb, NULL);
        printf("\n");
    }

    long long sum = 0;
    dlist_foreach(list, sum_cb, &sum);
    printf("\nsum = %lld\n", sum);

    int max = 0;
    dlist_foreach(list, max_cb, &max);
    printf("max = %d\n\n", max);

    for (i = 0; i < 10; i++) {
        dlist_delete_node(list, 0);
        printf("count = %2d,\t", dlist_count(list));
        dlist_foreach(list, print_int_cb, NULL);
        printf("\n");
    }

    dlist_free(list);

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
