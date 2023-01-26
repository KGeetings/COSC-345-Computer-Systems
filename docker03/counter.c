#include <stdio.h>
#include <limits.h>

int main() {
    printf("Maximum integer value: %d\n", INT_MAX);
    int result = INT_MAX + 1;
    printf("Result of overflow calculation: %d\n", result);
    return 0;
}
