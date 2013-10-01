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
 *         Author:  YOUR NAME (shuai fan), 
 *   Organization:  Dlut
 *
 * =====================================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include "dlist.h"

dlist* dlist_new(int data) {
    dlist *node = NULL;

    node = dlist_new_node(data);

    return node;
}

void dlist_free(dlist *l) {
    dlist *node = NULL;

    node = l;

    while (node->next) {
        node = node->next;
        dlist_delete_node(&l, node->prior->data);
    }
}

void dlist_print(dlist *l) {
    dlist *node = l;

    while (node) {
        printf(" %d", node->data);
        node = node->next;
    }

    printf("\n");
}

dlist* dlist_new_node(int data) {
    dlist *node = NULL;

    if (node = malloc(sizeof(dlist))) {
        node->data = data;
        node->prior = NULL;
        node->next = NULL;
    }

    return node;
}

void dlist_free_node(dlist *l) {
    l->prior = NULL;
    l->next = NULL;

    free(l);
}

void dlist_append(dlist *l, int data) {
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

void dlist_delete_node(dlist **l, int data) {
    dlist *p;

    p = *l;

    while (p) {
        if (p->data == data) {
            break;
        }

        p = p->next;
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
