/*	theclient.c	*/
/*	author	:	shuai
	date	:	2011.11.23
*/

#include <sys/types.h>
#include <sys/socket.h>
#include <pthread.h>
#include <netdb.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <strings.h>

// 缓冲区大小
#define MAXSIZE	255
// 端口号
#define PORT	2000

// 父子线程共享 socket 文件描述符
int sockfd;

// 接收服务器端的消息
void recv_msg()
{
	// 接收消息缓冲区，存放接收到的消息
	char recv_buffer[MAXSIZE];
	// 循环，接收服务器端的消息
	for (;;)
	{
		// 将 socket 读到的数据存放到接收消息缓冲区中
		recv(sockfd, recv_buffer, MAXSIZE, 0);
		// 显示读取到的消息
		printf("server>>%s", recv_buffer);
	}
	// 退出线程
	pthread_exit(NULL);
}

int main(int argc, char ** argv)
{
	// 存放服务器端相关信息的数据结构
	struct sockaddr_in ser_addr;
	// 存放地址信息
	struct hostent * sh;
	// 子线程线程号
	pthread_t tid;
	// 发送数据缓冲区
	char buffer[MAXSIZE];
	
	// 程序运行参数如： ./theclient localhost
	// 若参数不符，退出程序
	if (argc != 2)
	{
		printf("parameters not match.\n");
		exit(0);
	}
	
	// 获取网络地址信息
	if ((sh = gethostbyname(argv[1])) == NULL)
	{
		printf("gethostbyname error.\n");
		exit(0);
	}
	
	// 生成套接字，保存套接字文件描述符到 sockfd
	sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (sockfd == -1)
	{
		printf("socket error.\n");
		exit(0);
	}
	
	// 将套接字相关数据结构清零(初始化)
	bzero(& ser_addr, sizeof(struct sockaddr_in));
	// 设置协议为 IPv4
	ser_addr.sin_family = AF_INET;
	// 设置端口号
	// 将端口号从主机字节序转换为网络字节序
	ser_addr.sin_port = htons(PORT);
	// 设置网络地址
	ser_addr.sin_addr = * ((struct in_addr *) sh -> h_addr);

	// connect 系统调用，连接 sockfd 到服务器
	// 服务器地址由 ser_addr给出，ser_addr已由之前部分设置
	if (connect(sockfd, (struct sockaddr *) & ser_addr, sizeof(ser_addr)) == -1)
	{
		printf("connect error.\n");
		exit(0);
	}

	// 创建接收信息子线程
	pthread_create(& tid, NULL, (void *) recv_msg, NULL);

	// 循环，从 stdin 读取欲发送到服务器的消息，并将消息发送给服务器
	for (;;)
	{
		// 从 stdin 读取消息到缓冲区
		fgets(buffer, MAXSIZE, stdin);
		// 将缓冲区数据发送到服务器
		send(sockfd, buffer, sizeof(buffer), 0);
	}
	
	// 等待子线程退出
	pthread_join(tid, NULL);
	
	// 关闭 socket
	close(sockfd);

	return 0;
}
