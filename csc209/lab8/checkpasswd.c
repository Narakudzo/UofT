#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

/* Read a user id and password from standard input, 
   - create a new process to run the validate program
   - use exec (probably execlp) to load the validate program.
   - send 'validate' the user id and password on a pipe, 
   - print a message 
        "Password verified" if the user id and password matched, 
        "Invalid password", or 
        "No such user"
     depending on the return value of 'validate'.

Setting the character arrays to have a capacity of 256 when we are only
expecting to get 10 bytes in each is a cheesy way of preventing most
overflow problems.
*/

#define MAXLINE 256
#define MAXPASSWD 10

void strip(char *str, int capacity) {
    char *ptr;
    if((ptr = strchr(str, '\n')) == NULL) {
        str[capacity - 1] = '\0';
    } else {
        *ptr = '\0';
    }
}


int main(void) {
    char userid[MAXLINE];
    char password[MAXLINE];

    /* Read a user id and password from stdin */
    printf("User id:\n");
    if((fgets(userid, MAXLINE, stdin)) == NULL) {
        fprintf(stderr, "Could not read from stdin\n"); 
        exit(1);
    }
    strip(userid, MAXPASSWD);

    printf("Password:\n");
    if((fgets(password, MAXLINE, stdin)) == NULL) {
        fprintf(stderr, "Could not read from stdin\n"); 
        exit(1);
    }
    strip(password, MAXPASSWD);

    int pfd[2];

    if(pipe(pfd) == -1) {
        perror("pipe");
        exit(1);
    }

    switch (fork()) {
    case -1:
        perror("fork");
        exit(1);

    case 0:   /* Child process for executing ./validate. */
        if (close(pfd[1]) == -1) {
            perror("close pfd_1 child");
            exit(1);
        }
        if (pfd[0] !=  fileno(stdin)) {
            if (dup2(pfd[0],  fileno(stdin)) == -1) {
                perror("dup2");
                exit(1);
            }
            if (close(pfd[0]) == -1) {
                perror("close pfd_0 child");
                exit(1);
            }
        }
        /* Read from pipe */
        execlp("./validate", "validate", NULL);
        perror("execl ./validate");
        exit(1);

     default:    /* Parent process */
         if (close(pfd[0]) == -1) {
             perror("close pfd_0 parent");
             exit(1);
         }
         write(pfd[1], userid, MAXPASSWD);
         write(pfd[1], password, MAXPASSWD);

         if (close(pfd[1]) == -1) {
             perror("close pdf_1 parent");
             exit(1);
         }
 
        int status, exit_status;
        if(wait(&status) != -1) {
          if(WIFEXITED(status)) {
              exit_status = WEXITSTATUS(status);
              switch(exit_status) {
                  case 1:
                      printf("Error occured!!\n");
                      break;
                  case 2:
                      printf("Invalid password\n");
                      break;
                  case 3:
                      printf("No such user\n");
                      break;
                  default:
                      printf("Password verified\n");
                      break;
              }
          } else {
              printf("[%d] Child exited abnormally\n", getpid());
          }
        }
    }

    return 0;
}
