#include <iostream>
#include <memory>

using namespace std;

int main()
{
    char a[] = "abcde ";
    int b[20] = {3, 4};
    char c[2][3] = {"aa", "bb"};

    cout << sizeof(a) << endl;
    cout << sizeof(b) << endl;
    cout << sizeof(c) << endl;

    return 0;
}
