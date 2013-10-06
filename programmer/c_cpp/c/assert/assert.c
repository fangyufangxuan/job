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
#include "assert.h"

#define ERROR -1

int main(int argc, char *argv[]) {
    char *p = NULL;

    return_val_if_fail(p != NULL, ERROR);
}
