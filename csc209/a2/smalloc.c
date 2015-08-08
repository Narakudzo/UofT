#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/mman.h>
#include "smalloc.h"



void *mem;
struct block *freelist;
struct block *allocated_list;


void *smalloc(unsigned int nbytes) {
	void *temp_addr;

	temp_addr = search_matched_freelist(nbytes);
	if(temp_addr == NULL)
	{
		temp_addr = search_available_freelist(nbytes);
		if(temp_addr != NULL)
		{
			add_new_allocated_list(nbytes, temp_addr);
		}
	}
	return temp_addr;
}

void *search_matched_freelist(unsigned int nbytes) {
	struct block *cur, *prev;
	
	for (cur = freelist, prev = NULL;
		 cur != NULL && cur->size != nbytes;
		 prev = cur, cur = cur->next)
	;
	if (cur == NULL) {
		return NULL;
	}
	if (prev == NULL) {
		freelist = freelist->next;
	}
	else {
		prev->next = cur->next;
	}
	struct block *new_allocated_list;
	new_allocated_list = allocated_list;
	cur->next = new_allocated_list;
	allocated_list = cur;
	return cur->addr;
}

void *search_available_freelist(unsigned int nbytes) {
	struct block *cur, *prev;
	void *temp_addr;

	for (cur = freelist, prev = NULL;
		 cur != NULL && cur->size < nbytes;
		 prev = cur, cur = cur->next)
	;
	if (cur == NULL) {
		return NULL;
	}
	if (prev == NULL) {
		temp_addr = freelist->addr;
		freelist->addr = freelist->addr + nbytes;
		freelist->size = freelist->size - nbytes;
	}
	else {
		temp_addr = cur->addr;
		cur->addr = cur->addr + nbytes;
		cur->size = cur->size - nbytes;
	}
	return temp_addr;
}

void add_new_allocated_list(unsigned int nbytes, void *addr) {
	if (allocated_list == NULL) {
		allocated_list = malloc(sizeof(struct block));
		allocated_list->addr = addr;
		allocated_list->size = nbytes;
		allocated_list->next = NULL;
	} else {
		struct block *new_allocated_list;;
		new_allocated_list = allocated_list;
		allocated_list = malloc(sizeof(struct block));
		allocated_list->addr = addr;
		allocated_list->size = nbytes;
		allocated_list->next = new_allocated_list;
	}
}

int sfree(void *addr) {
	struct block *cur, *prev;
	
	for (cur = allocated_list, prev = NULL;
		 cur != NULL && cur->addr != addr;
		 prev = cur, cur = cur->next)
	;
	if (cur == NULL) {
		return -1;
	}
	if (prev == NULL) {
		allocated_list = allocated_list->next;
	}
	else {
		prev->next = cur->next;
	}
	struct block *new_freelist;
	new_freelist = freelist;
	cur->next = new_freelist;
	freelist = cur;
	return 0;
}

/* Initialize the memory space used by smalloc,
 * freelist, and allocated_list
 * Note:  mmap is a system call that has a wide variety of uses.  In our
 * case we are using it to allocate a large region of memory. 
 * - mmap returns a pointer to the allocated memory
 * Arguments:
 * - NULL: a suggestion for where to place the memory. We will let the 
 *         system decide where to place the memory.
 * - PROT_READ | PROT_WRITE: we will use the memory for both reading
 *         and writing.
 * - MAP_PRIVATE | MAP_ANON: the memory is just for this process, and 
 *         is not associated with a file.
 * - -1: because this memory is not associated with a file, the file 
 *         descriptor argument is set to -1
 * - 0: only used if the address space is associated with a file.
 */
void mem_init(int size) {
    mem = mmap(NULL, size,  PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANON, -1, 0);
    if(mem == MAP_FAILED) {
         perror("mmap");
         exit(1);
    }

    /* Initialize freelist and allocated_list */
    freelist = malloc(sizeof(struct block));
    freelist->addr = mem;
    freelist->size = size;
    freelist->next = NULL;
    allocated_list = NULL;
}

void mem_clean(){
    struct block *tmp_list;
    while (freelist != NULL) {
       tmp_list = freelist;
       freelist = freelist->next;
       free(tmp_list);
    }
    while (allocated_list != NULL) {
       tmp_list = allocated_list;
       allocated_list = allocated_list->next;
       free(tmp_list);
    }
}

