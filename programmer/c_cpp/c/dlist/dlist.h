#ifdef __cplusplus
extern "C" {
#endif

struct _dlist;
typedef struct _dlist dlist;

typedef enum dlist_ret {
    DLIST_RET_OK = 0,
    DLIST_RET_STOP = 1,
} dlist_ret;

typedef dlist_ret (*dlist_visit_func_cb)(void *ctx, void *data);
dlist_ret dlist_foreach(dlist *l, dlist_visit_func_cb visit, void *ctx);

dlist* dlist_new(void *data);

void dlist_free(dlist *l);

void dlist_append(dlist *l, void *data);

void dlist_delete_node(dlist **l, int index);

#ifdef __cplusplus
}
#endif
