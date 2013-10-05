/*
 * =====================================================================================
 *
 *       Filename:  dlist_string.c
 *
 *    Description:  dlist test program(string)
 *
 *        Version:  1.0
 *        Created:  10/05/2013 10:49:19 PM
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  shuai fan, 
 *   Organization:  Dlut
 *
 * =====================================================================================
 */

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "dlist.h"

static dlist_ret print_string_cb(void *ctx, void *data);
static dlist_ret string_to_upper_cb(void *ctx, void *data);
static dlist_ret free_string_cb(void *ctx, void *data);

int main() {
    dlist *list;

    list = dlist_new();

    dlist_append(list, strdup("one"));
    dlist_append(list, strdup("two"));

    dlist_foreach(list, print_string_cb, NULL);

    dlist_foreach(list, string_to_upper_cb, NULL);
    dlist_foreach(list, print_string_cb, NULL);

    dlist_foreach(list, free_string_cb, NULL);

    dlist_free(list);

    return 0;
}

static dlist_ret print_string_cb(void *ctx, void *data) {
    printf ("%s\n", (char*)data);

    return DLIST_RET_OK;
}

static dlist_ret string_to_upper_cb(void *ctx, void *data) {
    char *p = (char*)data;
    while (*p != '\0' && islower(*p)) {
        *p = toupper(*p);
        p++;
    }

    return DLIST_RET_OK;
}

static dlist_ret free_string_cb(void *ctx, void *data) {
    char *p = (char*)data;
    free(p);

    return DLIST_RET_OK;
}
