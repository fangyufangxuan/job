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

dlist* dlist_new() {
    dlist *head = NULL;

    head = dlist_new_node((void*)0);

    return head;
}

void dlist_free(dlist *l) {
    while (l->data > 0) {
        dlist_delete_node(l, 0);
    }
}

void dlist_append(dlist *l, void *data) {
    dlist *new_node;
    dlist *p = l->next;

    if (new_node = dlist_new_node(data)) {
        while (p != NULL && p->next != NULL) {
            p = p->next;
        }

        if (l->next == NULL) {
            new_node->prior = l;
            l->next = new_node;
        }
        else {
            new_node->prior = p;
            p->next = new_node;
        }
        l->data++;
    }
}

void dlist_delete_node(dlist *l, int index) {
    dlist *p;
    int count = 0;

    p = l->next;

    while (p && count < index) {
        p = p->next;
        count++;
    }

    p->prior->next = p->next;
    if (p->next) {
        p->next->prior = p->prior;
    }

    (int)(l->data)--;
    dlist_free_node(p);
}

int dlist_count(dlist *l) {
    return (int)(l->data);
}

dlist_ret dlist_foreach(dlist *l, dlist_visit_func_cb visit, void *ctx) {
    dlist_ret ret = DLIST_RET_OK;
    dlist *node = l->next;

    while (node != NULL && ret != DLIST_RET_STOP) {
        ret = visit(ctx, node->data);
        node = node->next;
    }

    return ret;
}
