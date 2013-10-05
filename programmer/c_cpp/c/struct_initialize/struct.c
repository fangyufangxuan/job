/*
 * =====================================================================================
 *
 *       Filename:  struct.c
 *
 *    Description:  struct initialize
 *
 *        Version:  1.0
 *        Created:  10/06/2013 05:29:08 AM
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  shuai fan,
 *   Organization:  
 *
 * =====================================================================================
 */

struct s {
    int l;
    char *p;
};

int main(int argc, char *argv[]) {

    //struct s s1 = { 4, "abcd" }; // Error
    struct s s1 = { .l = 4, .p = "abcd" };

    return 0;
}
