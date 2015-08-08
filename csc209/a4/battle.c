/**
 * CSC209 Assignment 4 - March 31, 2015
 * Spencer Ogawa
 * Student#: 994417751
 * CDF: g4spence
 * 
 * [Battle Game - Description]
 * 
 * Client is set in linked list. So the client who comes first will
 * play with the player who comes second.
 * 
 * Since the player who wins is always the player who attacks, this
 * program swtiches players between attacker and its opponent in the
 * linked list. So the attacker is always set in the end of the linked
 * list. And the opponent loses, this player will move away to the
 * head of the linked list.
 * 
 * For example, if there are 5 clients:
 * 
 *   1. p5->p4->p3->p2->p1 = p1 attacks p2
 *   2. p5->p4->p3->p1->p2 = p2 attacks p1
 *   3. p5->p4->p3->p2->p1 = p1 attacks p2
 *   4. p5->p4->p3->p1->p2 = p2 attacks p1 - p2 wins and p1 pushes away
 *   5. p1->p5->p4->p3->p2 = p2 attacks p3
 * 
 * So in each attack, the deepest client in linked list and the second
 * deepest client in linked list switches.
 * 
 */

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <time.h>
#include "battle.h"

#ifndef PORT
#define PORT 57751
#endif

int main(void) {

    int clientfd, maxfd, nready;
    struct client *p;
    struct client *head = NULL;
    socklen_t len;
    struct sockaddr_in q;
    //struct timeval tv;
    fd_set allset;
    fd_set rset;
    time_t t;
    int i;
    
    /** random number */
    srand((unsigned) time(&t));

    int listenfd = bindandlisten();
    /** 
     * initialize allset and add listenfd to the 
     *  set of file descriptors passed into select
     */
    FD_ZERO(&allset);
    FD_SET(listenfd, &allset);
    /** maxfd identifies how far into the set to search */
    maxfd = listenfd;

    for(;;) {
        /** When two players are in battle, swap between p1 and p2. */
        int swapplayer = 0;
        
        /** Player who loses is always player2. */
        int moveplayer2 = 0;
        
        /** make a copy of the set before we pass it into select */
        rset = allset;
        nready = select(maxfd + 1, &rset, NULL, NULL, NULL); 

        if(nready == -1) {
            perror("select");
            continue;
        }

        if (FD_ISSET(listenfd, &rset)){
            printf("a new client is connecting\n");  
            len = sizeof(q);
            if((clientfd = accept(listenfd, (struct sockaddr *)&q, &len)) < 0) {
                perror("accept");
                exit(1);
            }
            FD_SET(clientfd, &allset);
            if(clientfd > maxfd) {
                maxfd = clientfd;
            }
            /** Ask new connection his/her name */
            char message[20] = "What is your name? ";
            write(clientfd, message, sizeof(message));
            head = addclient(head, clientfd, q.sin_addr);
        }
        
        for(i = 0; i <= maxfd; i++) {
            if(FD_ISSET(i, &rset)) {
                for(p = head; p != NULL; p = p->next) {
                    if(p->fd == i) {
                        int result = handleclient(p, head);
                        if(result == -1) {
                            head = removeclient(head, p->fd);
                            FD_CLR(p->fd, &allset);
                            close(p->fd);
                        } else if (result == 1) {
                            /** In battle, so switch player1/player2 */
                            swapplayer = 1;
                        } else if (result == 2) {
                            /** Battle ends, so moves away player2 */
                            moveplayer2 = 1;
                        }
                        break;
                    }
                }
            }
        }
        
        /** Get current player1 and player2 */
        struct client *player1 = getplayer1(head);
        struct client *player2 = getplayer2(head);

        /** Push player2 to the head of the list */
        if (moveplayer2 == 1) {
            if (player2->fd == head->fd) {
                cleargame(head);
            } else {
                int i = getclientsize(head);
                i = i - 2;
                if(i > 0) {
                    for(; i > 0; i--) {
                        struct client *tmp1;
                        struct client *tmp2;
                        tmp1 = getplayer(head, i);
                        tmp2 = getplayer(head, i - 1);
                        switchplayer(tmp1, tmp2);
                    }
                }
                cleargame(head);
            }
            moveplayer2 = 0;            
        }
        
        /** If in battle, swtch back/forth p1/p2 */
        if (swapplayer == 1) {
            switchplayer(player1, player2);
            swapplayer = 0;
        }
        
        struct client *p1 = getplayer1(head);
        struct client *p2 = getplayer2(head);
        
        /** If there are 2 clients, initialize the game */
        if (p1 != NULL && p2 != NULL) {
            if (p1->opponent < 0 &&
                p2->opponent < 0 &&
                (int) strlen(p1->myname) != 0 &&
                (int) strlen(p2->myname) != 0) {
                initializebattle(p1, p2);
            }
        } else if (p1 != NULL && p2 == NULL) {
            if(strlen(p1->myname) > 0) {
                char outbuf[50];
                sprintf(outbuf, "\r\nAwaiting opponent...\r\n");
                write(p1->fd, outbuf, strlen(outbuf));
            }
        }
    }
    return 0;
}

int handleclient(struct client *p, struct client *top) {

    char tmp[1];
    char buf[256];
    char outbuf[256];
    int speakicanon = 0;

    struct client *p1 = getplayer1(top);
    struct client *p2 = getplayer2(top);
    
    int len = 0;
    int current = 0;
    while((current = read(p->fd, tmp, 1)) > 0) {
        
        /** Speak command without icanon (means -icanon) */
        if(tmp[0] == 's' && 
            p->fd == p1->fd &&
            p1->opponent >= 0 &&
            p1->speak == 0) {
            p1->speak = 1;
            speakicanon = 1;
            sprintf(outbuf, "\r\nYou speak: ");
            write(p1->fd, outbuf, strlen(outbuf));
            return 0;
        }
        
        /** Attack command */
        if(tmp[0] == 'a' &&
            p->fd == p1->fd &&
            p1->opponent >= 0 &&
            p1->speak == 0) {
            return attack(p1, p2);
        }
        
        /** Powermove command */
        if(tmp[0] == 'p' &&
            p->fd == p1->fd &&
            p1->opponent >= 0 &&
            p1->powermove > 0 &&
            p1->speak == 0) {
            return powermove(p1, p2);
        }
        
        /** Ignore Player 2 that is in battle */
        if (p2 != NULL) {
            if (p->fd == p2->fd && strlen(p2->myname) > 0){
                len = 1;
                break;
            }
        }
        
        /** Ignore other players; only accepts for setting name */
        if(strlen(p->myname) > 0 && p->opponent < 0) {
            len = 1;
            break;
        }
        
        buf[len] = tmp[0];
        len += current;
        if(tmp[0] == '\n') {
            break;
        }

    }
    
    /** Check if name is set. If not, set name and broadcast message */
    if (len > 0 && (int) strlen(p->myname) == 0) {
        buf[len - 1] = '\0';
        if ((int) strlen(buf) == 0) {
            sprintf(outbuf, "What is your name? ");
            write(p->fd, outbuf, strlen(outbuf));
        } else {
            printf("New client: %s added\r\n", buf);
            sprintf(outbuf, "Welcome, %s! Awaiting opponent...\r\n", buf);
            strncpy(p->myname, buf, len);
            write(p->fd, outbuf, strlen(outbuf));
            outbuf[strlen(outbuf)] = '\0';
            sprintf(outbuf, "**%s enters the arena**\r\n", buf);
            broadcastall(top, outbuf, strlen(outbuf), p->fd);
        }
        return 0;
    } else if(len > 0 && p->fd == p1->fd && p1->opponent >= 0 &&
                         p1->speak == 1 && speakicanon == 0 && buf[0] != '\n') {
        /** Speaking to opponent here */
        p1->speak = 0;
        buf[len] = '\0';
        sprintf(outbuf, "%s takes a break to tell you:\r\n%s\r\n\r\n", p1->myname, buf);
        write(p1->opponent, outbuf, strlen(outbuf));
        consolep1(p1, p2);
        consolep2(p1, p2);
        return 0;
    } else if (len > 0) {
        return 0;
    } else if(len == 0) {
        /** Socket is closed. Client left. */
        sprintf(outbuf, "**%s leaves**\r\n", p->myname);
        broadcast(top, outbuf, strlen(outbuf));
        return -1;
    } else { // shouldn't happen
        perror("read");
        return -1;
    }
}

 /** 
  * bind and listen, abort on error 
  * returns socket connection
  */
int bindandlisten(void) {

    struct sockaddr_in r;
    int listenfd;

    if ((listenfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        perror("socket");
        exit(1);
    }
    int yes = 1;
    if((setsockopt(listenfd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int))) == -1) {
        perror("setsockopt");
    }
    memset(&r, '\0', sizeof(r));
    r.sin_family = AF_INET;
    r.sin_addr.s_addr = INADDR_ANY;
    r.sin_port = htons(PORT);

    if (bind(listenfd, (struct sockaddr *)&r, sizeof r)) {
        perror("bind");
        exit(1);
    }

    if (listen(listenfd, 5)) {
        perror("listen");
        exit(1);
    }
    return listenfd;
}


static struct client *addclient(struct client *top, int fd, struct in_addr addr) {

    struct client *p = malloc(sizeof(struct client));
    if (!p) {
        fprintf(stderr, "out of memory!\n");  /** highly unlikely to happen */
        exit(1);
    }
    
    printf("Adding client... no name yet\n");
    
    /** Setting up client (initialize client) */
    p->fd = fd;
    p->ipaddr = addr;
    p->hitpoint = MAX_HITPOINT;
    p->hitpoint = p->hitpoint - (rand() % 11);
    p->powermove = MAX_POWERMOVE;
    p->myname[0] = '\0';
    p->opponent = -1;
    p->speak = 0;
    p->next = top;
    top = p;

    return top;
}

static void cleargame(struct client *top) {

    struct client *p;
    p = top;
    if (p == NULL) {
        return;
    } else {
        p->opponent = -1;
        p->hitpoint = MAX_HITPOINT;
        p->hitpoint = p->hitpoint - (rand() % 11);
        p->powermove = MAX_POWERMOVE;
        cleargame(p->next);
    }
}

static struct client *removeclient(struct client *top, int fd) {

    struct client **p;
    
for (p = &top; *p && (*p)->fd != fd; p = &(*p)->next)
        ;  
    /** Copy and paste method from the sample */
    if (*p) {
        struct client *t = (*p)->next;
        if ((*p)->opponent >= 0) {
            cleargame(top);
        }
        *p = t;
    } else {
        fprintf(stderr, "Trying to remove fd %d, but I don't know about it\n",
                 fd);
    }
    return top;
}

struct client *getplayer1(struct client *top) {

    struct client *p;
    p = top;
    if (p == NULL) {
        return NULL;
    }
    if (p->next == NULL) {
        return p;
    } else {
        return getplayer1(p->next);
    }
}

struct client *getplayer2(struct client *top) {

    struct client *p;
    p = top;
    if (p == NULL || p->next == NULL) {
        return NULL;
    }
    if (p->next->next == NULL) {
        return p;
    } else {
        return getplayer2(p->next);
    }
}

struct client *getplayer(struct client *top, int position) {

    struct client *p;
    p = top;
    if (position <= 0) {
        return p;
    } else {
        position = position - 1;
        return getplayer(p->next, position);
    }
}

int getclientsize(struct client *top) {
    
    struct client *p;
    p = top;
    
    if (p == NULL) {
        return 0;
    }
    if (p->next == NULL) {
        return 1;
    }
    return 1 + getclientsize(p->next);
}

static void initializebattle(struct client *player1, struct client *player2) {

    struct client *p1,*p2;
    p1 = player1;
    p2 = player2;
    
    p1->opponent = p2->fd;
    p2->opponent = p1->fd;
    
    char message1[256];
    char message2[256];
    int j = 0,k = 0;
    j += sprintf(message1 + j, "You engage %s!\r\n", p2->myname);
    j += sprintf(message1 + j, "Your hitpoints: %d\r\n", p1->hitpoint);
    j += sprintf(message1 + j, "Your powermoves: %d\r\n\r\n", p1->powermove);
    j += sprintf(message1 + j, "%s's hitpoints: %d\r\n\r\n", p2->myname, p2->hitpoint);
    j += sprintf(message1 + j, "(a)ttack\r\n(p)owermove\r\n(s)peak something\r\n");
    k += sprintf(message2 + k, "You engage %s!\r\n", p1->myname);
    k += sprintf(message2 + k, "Your hitpoints: %d\r\n", p2->hitpoint);
    k += sprintf(message2 + k, "Your powermoves: %d\r\n\r\n", p2->powermove);
    k += sprintf(message2 + k, "%s's hitpoints: %d\r\n\r\n", p1->myname, p1->hitpoint);
    k += sprintf(message2 + k, "Waiting for %s to strike...\r\n\r\n", p1->myname);
    write(p1->fd, message1, strlen(message1));
    write(p2->fd, message2,strlen(message2));
}

static void consolep1(struct client *player1, struct client *player2) {
    
    struct client *p1, *p2;
    p1 = player1;
    p2 = player2;
    
    char message[256];
    int j = 0;
    
    j += sprintf(message + j, "\nYour hitpoints: %d\r\n", p1->hitpoint);
    j += sprintf(message + j, "Your powermoves: %d\r\n\r\n", p1->powermove);
    j += sprintf(message + j, "%s's hitpoints: %d\r\n\r\n", p2->myname, p2->hitpoint);
    if (p1->powermove > 0) {
        j += sprintf(message + j, "(a)ttack\r\n(p)owermove\r\n(s)peak something\r\n");
    } else {
        j += sprintf(message + j, "(a)ttack\r\n(s)peak something\r\n");
    }
    write(p1->fd, message, strlen(message));
}

static void consolep2(struct client *player1, struct client *player2) {

    struct client *p1, *p2;
    p1 = player1;
    p2 = player2;
    
    char message[256];
    int k = 0;

    k += sprintf(message + k, "\nYour hitpoints: %d\r\n", p2->hitpoint);
    k += sprintf(message + k, "Your powermoves: %d\r\n\r\n", p2->powermove);
    k += sprintf(message + k, "%s's hitpoints: %d\r\n\r\n", p1->myname, p1->hitpoint);
    k += sprintf(message + k, "Waiting for %s to strike...\r\n\r\n", p1->myname);
    write(p2->fd, message, strlen(message));
}

static void switchplayer(struct client *player1, struct client *player2) {

    struct client *p1, *p2;
    p1 = player1;
    p2 = player2;
    int fd;
    struct in_addr ipaddr;
    char myname[20];
    int hitpoint, powermove, opponent, speak;
    fd = p1->fd;
    ipaddr = p1->ipaddr;
    strcpy(myname, p1->myname);
    hitpoint = p1->hitpoint;
    powermove = p1->powermove;
    opponent = p1->opponent;
    speak = p1->speak;
    
    p1->fd = p2->fd;
    p1->ipaddr = p2->ipaddr;
    strcpy(p1->myname, p2->myname);
    p1->hitpoint = p2->hitpoint;
    p1->powermove = p2->powermove;
    p1->opponent = p2->opponent;
    p1->speak = p2->speak;
    
    p2->fd = fd;
    p2->ipaddr = ipaddr;
    strcpy(p2->myname, myname);
    p2->hitpoint = hitpoint;
    p2->powermove = powermove;
    p2->opponent = opponent;
    p2->speak = speak;

}

int attack(struct client *player1, struct client *player2) {

    struct client *p1, *p2;
    p1 = player1;
    p2 = player2;
    
    int damage = (rand() % 5) + 2;
    p2->hitpoint = p2->hitpoint - damage;
    char hit1[50];
    char hit2[50];
    sprintf(hit1, "\r\nYou hit %s for %d damage!\r\n", p2->myname, damage);
    sprintf(hit2, "\r\n%s hits you for %d damage!\r\n", p1->myname, damage);
    write(p1->fd, hit1, strlen(hit1));
    write(p2->fd, hit2, strlen(hit2));
    
    /** Player2 loses */
    if (p2->hitpoint <= 0) {
        char outbuf[256];
        int j = 0;
        j += sprintf(outbuf + j, "You are no match for %s. ", p1->myname);
        j += sprintf(outbuf + j, "You scurry away...\r\n\r\n");
        j += sprintf(outbuf + j, "Awaiting opponent...\r\n\r\n");
        write(p2->fd, outbuf, strlen(outbuf));
        char win[256];
        int k = 0;
        k += sprintf(win + k, "%s gives up. You win!\r\n\r\n", p2->myname);
        k += sprintf(win + k, "Awaiting opponent...\r\n\r\n");
        write(p1->fd, win, strlen(win));
        return 2;
    } else {
        consolep1(p2, p1);
        consolep2(p2, p1);
    }
    return 1;
}

int powermove(struct client *player1, struct client *player2) {

    struct client *p1, *p2;
    p1 = player1;
    p2 = player2;
    
    p1->powermove = p1->powermove - 1;
    
    int damage = (rand() % 5) + 2;
    damage = damage * (rand() % 2);
    if (damage > 0) {
        damage = damage * 3;
        p2->hitpoint = p2->hitpoint - damage;
        char hit1[50];
        char hit2[50];
        sprintf(hit1, "\r\nYou hit %s for %d damage!\r\n", p2->myname, damage);
        sprintf(hit2, "\r\n%s powermoves you for %d damage!\r\n", p1->myname, damage);
        write(p1->fd, hit1, strlen(hit1));
        write(p2->fd, hit2, strlen(hit2));
    } else {
        char hit1[50];
        char hit2[50];
        sprintf(hit1, "\r\nYou missed!\r\n");
        sprintf(hit2, "\r\n%s missed you!\r\n", p1->myname);
        write(p1->fd, hit1, strlen(hit1));
        write(p2->fd, hit2, strlen(hit2));
    }
    /** Player2 loses */
    if (p2->hitpoint <= 0) {
        char outbuf[256];
        int j = 0;
        j += sprintf(outbuf + j, "You are no match for %s. ", p1->myname);
        j += sprintf(outbuf + j, "You scurry away...\r\n\r\n");
        j += sprintf(outbuf + j, "Awaiting opponent...\r\n\r\n");
        write(p2->fd, outbuf, strlen(outbuf));
        char win[256];
        int k = 0;
        k += sprintf(win + k, "%s gives up. You win!\r\n\r\n", p2->myname);
        k += sprintf(win + k, "Awaiting opponent...\r\n\r\n");
        write(p1->fd, win, strlen(win));
        return 2;
    } else {
        consolep1(p2, p1);
        consolep2(p2, p1);
    }
    return 1;
}

static void broadcastall(struct client *top, char *s, int size, int fd) {
    struct client *p;
    for (p = top; p; p = p->next) {
        if (p->fd != fd) {
            write(p->fd, s, size);
        }
    }
}

static void broadcast(struct client *top, char *s, int size) {
    struct client *p;
    for (p = top; p; p = p->next) {
        write(p->fd, s, size);
    }
}
