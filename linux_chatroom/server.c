/*	server.c	*/
/*	author	:	shuai
	date	:	2011.11.23
*/

#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

#define MAXSIZE 255
#define	PORT	2000	
#define BACKLOG	10

int main(int argc, char ** argv)
{
	int listenfd, connectfd;
	struct sockaddr_in server;
	struct sockaddr_in client;
	socklen_t addr_len;
	char buffer[MAXSIZE];
	pid_t pid;
	
	listenfd = socket(AF_INET, SOCK_STREAM, 0);
	if (listenfd == -1)
	{
		printf("socket error.\n");
		exit(0);
	}
	
	bzero(& server, sizeof(server));
	server.sin_family = AF_INET;
	server.sin_port = htons(PORT);
	server.sin_addr.s_addr = htonl(INADDR_ANY);

	if (bind(listenfd,(struct sockaddr *) & server, sizeof(struct sockaddr_in)) == -1)
	{
		printf("bind error.\n");
		exit(0);
	}
	if (listen(listenfd,BACKLOG) == -1)
	{
		printf("listen error.\n");
		exit(0);
	}

	addr_len = sizeof(struct sockaddr_in);
	while(1)
	{
		connectfd = accept(listenfd, (struct sockaddr *) & client, & addr_len);
		if (connectfd == -1)
		{
			printf("error in accept.\n");
			exit(0);
		}
		
		pid = fork();
		if (pid > 0)
		{
			close(listenfd);
			while(1)
			{
				recv(connectfd, buffer, MAXSIZE, 0);
				printf("client>>%s", buffer);
			}
		}
		else if (pid == 0)
		{
			close(listenfd);
			while(1)
			{
				fgets(buffer, MAXSIZE, stdin);
				send(connectfd, buffer, sizeof(buffer), 0);
			}	
		}
		else
		{
			printf("fork error.\n");
			exit(0);
		}		
	}
	close(listenfd);
	close(connectfd);
}
