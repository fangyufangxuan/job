#ifdef __cplusplus
extern "C" {
#endif

#define DLIST_RET_OK 0

struct _dlist;
typedef struct _dlist dlist;

typedef int (*dlist_print_func_cb)(void *data);
int dlist_print(dlist *l, dlist_print_func_cb print);

dlist* dlist_new(void *data);

void dlist_free(dlist *l);

void dlist_append(dlist *l, void *data);

void dlist_delete_node(dlist **l, int index);

#ifdef __cplusplus
}
#endif
