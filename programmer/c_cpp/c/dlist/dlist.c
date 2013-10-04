/*
 * =====================================================================================
 *
 *       Filename:  dlist.c
 *
 *    Description:  Double linked list
 *
 *        Version:  1.0
 *        Created:  07/21/2013 10:25:39 PM
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  shuai fan, 
 *   Organization:  Dlut
 *
 * =====================================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include "dlist.h"

struct _dlist
{
    struct _dlist* prior;
    struct _dlist* next;
    void *data;
};

static dlist* dlist_new_node(void *data) {
    dlist *node = NULL;

    if (node = malloc(sizeof(dlist))) {
        node->data = data;
        node->prior = NULL;
        node->next = NULL;
    }

    return node;
}

static void dlist_free_node(dlist *l) {
    l->prior = NULL;
    l->next = NULL;

    free(l);
}

dlist* dlist_new(void *data) {
    dlist *node = NULL;

    node = dlist_new_node(data);

    return node;
}

void dlist_free(dlist *l) {
    dlist *node = NULL;

    node = l;

    while (node->next) {
        node = node->next;
        dlist_delete_node(&l, 0);
    }
}

void dlist_append(dlist *l, void *data) {
    dlist *new_node;
    dlist *p;

    p = l;

    if (new_node = dlist_new_node(data)) {
        while (p->next) {
            p = p->next;
        }

        new_node->prior = p;
        p->next = new_node;     
    }
}

void dlist_delete_node(dlist **l, int index) {
    dlist *p;
    int count = 0;

    p = *l;

    while (p && count < index) {
        p = p->next;
        count++;
    }

    if (p == *l) {
        *l = p->next;
    } else {
        p->prior->next = p->next;
    }

    if (p->next) {
        p->next->prior = p->prior;
    }

    dlist_free_node(p);
}

int dlist_print(dlist *l, dlist_print_func_cb print) {
    int ret = DLIST_RET_OK;
    dlist *node = l;

    while (node != NULL) {
        print(node->data);
        node = node->next;
    }

    return ret;
}
