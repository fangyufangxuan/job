#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char *relat_path_2_abs(const char *path)
{
    char *abs_path;
    char *start, *end;
    char *p;
    char *index;
    int length = 0;
    int parent_count = 0;

    length = strlen(path);
    abs_path = (char *)malloc(sizeof(char) * length);
    strcpy(abs_path, path);

    start = abs_path;
    end = abs_path + length - 1;
    p = end;

    while (p > start)
    {
        p--;
        while (*p != '/')
        {
            p--;
        }

        if (*(p + 1) == '.' && *(p + 2) == '.')
        {
            parent_count++;
            index = p;
        }
    }

    while (parent_count > 0)
    {
        *end = '\0';
        while (*end != '/')
        {
            end--;
        }

        if (strcmp(end + 1, "..") != 0)
        {
            parent_count--;
        }
    }

    p = index + 3;
    while (p < end)
    {
        *index = *p;
        index++;
        p++;
    }
    *index = '\0';

    return abs_path;
}

void main()
{
    char *relate_path = "/home/aaa/../test/temp/new/../";
    char *abs_path = relat_path_2_abs(relate_path);
    printf("%s\n", abs_path);

    free(abs_path);
}
