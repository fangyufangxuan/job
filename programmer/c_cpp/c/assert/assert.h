#ifndef ASSERT_H_
#define ASSERT_H_

#define return_if_fail(p) if(!(p))              \
    {printf("%s:%d Warning:  "#p" failed.\n",   \
        __func__, __LINE__); return; }

#define return_val_if_fail(p, ret) if(!(p))     \
    {printf("%s:%d Warning: "#p" failed.\n",    \
        __func__, __LINE__); return (ret); }

#endif
