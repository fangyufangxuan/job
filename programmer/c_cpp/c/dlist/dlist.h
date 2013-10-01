typedef struct dlist {
    int data;
    struct dlist *prior;
    struct dlist *next;
} dlist;

dlist* dlist_new(int data);

void dlist_free(dlist *l);

void dlist_print(dlist *l);

dlist* dlist_new_node(int data);

void dlist_free_node(dlist *l);

void dlist_append(dlist *l, int data);

void dlist_delete_node(dlist **l, int data);
