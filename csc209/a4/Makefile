CC = gcc
PORT=57751
CFLAGS = -DPORT=$(PORT) -g -Wall
DEPS = battle.h

all: battle

battle: battle.o
	${CC} ${CFLAGS} -o $@ battle.o

%.o: %.c $(DEPS)
	${CC} ${CFLAGS} -c $<
	
clean:
	rm *.o battle
