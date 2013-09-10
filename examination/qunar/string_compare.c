#include <stdio.h>

const char * string_compare(const char *str1, const char *str2)
{
    const char *p1 = str1, *p2 = str2;
    while (*p1 != '\0' && *p2 != '\0' && *p1 == *p2)
    {
        p1++;
        p2++;
    }
    
    return p2;
}

void main()
{
    char *str1 = "helloworld";
    char *str2 = "hellonewworld";

    printf("%s\n", string_compare(str1, str2));
}
