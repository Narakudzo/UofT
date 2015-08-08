#ifndef _BATTLE_H
#define _BATTLE_H

/** Maximum hitpoint ans powermove */
#define MAX_HITPOINT 30;
#define MAX_POWERMOVE 3;

/** Client's linked list */
struct client {
    int fd;
    struct in_addr ipaddr;
    char myname[20];
    int hitpoint, powermove, opponent, speak;
    struct client *next;
};

/** Adds client to Client's linked list, and returns updated linked list */
static struct client *addclient(struct client *top, int fd, struct in_addr addr);

/** Removes client from Client's linked list, and returns updated linked list */
static struct client *removeclient(struct client *top, int fd);

/** Returns the deepest Client of Client's linked list, or NULL */
struct client *getplayer1(struct client *top);

/** Returns the second deepest client of Client's linked list, or NULL */
struct client *getplayer2(struct client *top);

/** Returns the client in the position of Client's linked list, or NULL */
struct client *getplayer(struct client *top, int position);

/** Returns the size of Client's linked list */
int getclientsize(struct client *top);

/** Writes messages to player1 and player2 to initialize battle,
 *  and set opponent member of client */
static void initializebattle(struct client *player1, struct client *player2);

/** Writes generic console messages to attack */
static void consolep1(struct client *player1, struct client *player2);

/** Writes generic console messages to defender */
static void consolep2(struct client *player1, struct client *player2);

/** Switches the position of player1 and player2 in Client's linked list */
static void switchplayer(struct client *player1, struct client *player2);

/** Calculates damage and reduces a defender's hitpoint for attack */
int attack(struct client *player1, struct client *player2);

/** Calculates damage and reduces a defender's hitpoint for powermove */
int powermove(struct client *player1, struct client *player2);

/** Sets opponent member of Client's linked list to -1 */
static void cleargame(struct client *top);

/** Broadcasts messages to all members of Client's linked list except
 *  one specified in int fd */
static void broadcastall(struct client *top, char *s, int size, int fd);

/** Broadcasts messages to all members of Client's linked list */
static void broadcast(struct client *top, char *s, int size);

/** Handles client's inputs */
int handleclient(struct client *p, struct client *top);

/** Receives client's connection */
int bindandlisten(void);

#endif

