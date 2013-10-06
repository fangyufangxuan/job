/*
 * =====================================================================================
 *
 *       Filename:  assert.c
 *
 *    Description:  assert
 *
 *        Version:  1.0
 *        Created:  10/06/2013 06:21:13 AM
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  YOUR NAME (), 
 *   Organization:  
 *
 * =====================================================================================
 */

#include <stdio.h>
#include <stdlib.h>

#define return_if_fail(p) if(!(p))              \
    {printf("%s:%d Warning:  "#p" failed.\n",   \
        __func__, __LINE__); return; }

#define return_val_if_fail(p, ret) if(!(p))     \
    {printf("%s:%d Warning: "#p" failed.\n",    \
        __func__, __LINE__); return (ret); }

#define ERROR -1

int main(int argc, char *argv[]) {
    char *p = NULL;

    return_val_if_fail(p != NULL, ERROR);
}
