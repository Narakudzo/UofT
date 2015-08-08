#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <sys/types.h>
#include <sys/mman.h>
#include "smalloc.h"

#define SIZE 4096 * 64

/* mytest.c for smalloc and sfree.
 * 
 * 1. Empty freelist (allocate all memories).
 * 
 * 2. Release some blocks back to freelist.
 * 
 * 3. Allocate randomly for several times. So some released blocks match
 *    with a requested block. When matched, the block is already malloc'ed.
 *    So make sure that those malloc'ed blocks are re-linked to allocated_list
 *    without requesting new malloc().
 * 
 * 4. Empty all allocated_list.
 * 
 * 5. Again, randomly call smalloc() for several times.
 * 
 * If valgrind does not indicate any memory leak after running mytest,
 * smalloc is good to go!
 */ 

int main(void) {

    mem_init(SIZE);
 
    char *ptrs[20];
    int i, j = SIZE;

    /* Call smalloc 5 times */
    
    for(i = 0; i < 5; i++) {
        int num_bytes = (i+1) * 10;
        ptrs[i] = smalloc(num_bytes);
        write_to_mem(num_bytes, ptrs[i], i);
        j -= num_bytes;
    }
    
    /* Call smalloc for the remaining memory */
    ptrs[i] = smalloc(j);
    write_to_mem(j, ptrs[i], i);
    
    printf("List of allocated blocks:\n");
    print_allocated();
    printf("All blocks are allocated\n\n");
    printf("Now freeing up 3 blocks from allocated_list\n");
    for(i = 0; i < 3; i++) {
		printf("    freeing %p result = %d\n", ptrs[i], sfree(ptrs[i]));
    }
    printf("List of free blocks:\n");
    print_free();
    printf("\nNow calling smalloc 14 times for random bytes:\n");
    srand(time(NULL));
    for(i = 6; i < 20; i++) {
        int num_bytes = 1 + (rand() % 10);
        ptrs[i] = smalloc(num_bytes);
        if(ptrs[i] != NULL) {
            write_to_mem(num_bytes, ptrs[i], i);
	    printf("    allocating %p for %d bytes...\n", ptrs[i], num_bytes);
	}
    }
    printf("List of allocated blocks:\n");
    print_allocated();
    printf("List of free blocks:\n");
    print_free();
    printf("\nNow freeing all allocated lists:\n");
    for(i = 3; i < 20; i++) {
	printf("    freeing %p result = %d\n", ptrs[i], sfree(ptrs[i]));
    }
    printf("List of allocated blocks:\n");
    print_allocated();
    printf("List of free blocks:\n");
    print_free();
    printf("\nAgain... calling smalloc() for 10 times with random bytes...\n");
    for(i = 0; i < 10; i++) {
        int num_bytes = 1 + (rand() % 10);
        ptrs[i] = smalloc(num_bytes);
        write_to_mem(num_bytes, ptrs[i], i);
    }
    printf("List of allocated blocks:\n");
    print_allocated();
    printf("List of free blocks:\n");
    print_free();
    mem_clean();
    return 0;

}
