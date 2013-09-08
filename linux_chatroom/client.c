/*	client.c	*/
/*	author	:	shuai
	date	:	2011.11.23
*/

#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <strings.h>

#define MAXSIZE	255
#define PORT	2000

int main(int argc, char ** argv)
{
	int sockfd;
	struct sockaddr_in ser_addr;
	char * local_name;
	struct hostent * sh;
	pid_t pid;
	char buffer[MAXSIZE];
		
	if (argc != 2)
	{
		printf("parameters not match.\n");
		exit(0);
	}
	
	if ((sh = gethostbyname(argv[1])) == NULL)
	{
		printf("gethostbyname error.\n");
		exit(0);
	}

	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd == -1)
	{
		printf("socket error.\n");
		exit(0);
	}

	bzero(& ser_addr, sizeof(struct sockaddr_in));
	ser_addr.sin_family = AF_INET;
	ser_addr.sin_port = htons(PORT);
	ser_addr.sin_addr = * ((struct in_addr *) sh -> h_addr);
	
	if (connect(sockfd, (struct sockaddr *) & ser_addr, sizeof(ser_addr)) == -1)
	{
		printf("connect error.\n");
		exit(0);
	}
	
	pid = fork();

	if (pid > 0)
	{
		for(;;)
		{
			fgets(buffer, MAXSIZE, stdin);
			send(sockfd, buffer, sizeof(buffer), 0);	
		}
	}
	else if (pid == 0)
	{
		for(;;)
		{
			recv(sockfd, buffer, MAXSIZE, 0);
			printf("server>>%s", buffer);
		}
	}		
	else
	{
		printf("fork error.\n");
		exit(0);
	}

	close(sockfd);
}
