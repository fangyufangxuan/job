/*	theserver.c	*/
/*	author	:	shuai
	date	:	2011.11.23
*/

#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <pthread.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

// 缓冲区大小
#define MAXSIZE 255
// 端口号
#define	PORT	2000
// socket 阻塞队列最大长度
#define BACKLOG	10

// 与客户端连接的 socket 文件描述符
int connectfd;
// 发送数据缓冲区
char send_buffer[MAXSIZE];
// 接收数据缓冲区
char recv_buffer[MAXSIZE];

// 读取用户输入的数据，发送到客户端
void send_msg()
{
    for (;;)
	{
		// 从 stdin 读取用户输入的消息
		fgets(send_buffer, MAXSIZE, stdin);
		//printf("send_buffer is:%s", send_buffer);
		// 将数据发送到 connectfd 连接的客户端
        	send(connectfd, send_buffer, sizeof(send_buffer), 0);
	}
	// 退出线程
	pthread_exit(NULL);
}

int main(int argc, char ** argv)
{
	// int listenfd, connectfd;
	// 监听客户端连接的套接字(文件描述符)
	int listenfd;
	// 服务器端套接字相关数据结构
	struct sockaddr_in server;
	// 客户端套接字相关数据结构
	struct sockaddr_in client;
	// 地址信息数据结构长度
	socklen_t addr_len;
	// 子线程线程号
	pthread_t tid;
	// char buffer[MAXSIZE];
	
	// 创建监听套接字
	listenfd = socket(AF_INET, SOCK_STREAM, 0);
	if (listenfd == -1)
	{
		printf("socket error.\n");
		exit(0);
	}
	
	// 服务器端套接字信息初始化
	bzero(& server, sizeof(server));
	// 设置协议为 IPv4
	server.sin_family = AF_INET;
	// 设置端口号
	// 将端口号从主机字节序转换为网络字节序
	server.sin_port = htons(PORT);
	// 转换32位长的地址为网络字节序
	server.sin_addr.s_addr = htonl(INADDR_ANY);
	
	// 将监听套接字与服务器端绑定
	if (bind(listenfd,(struct sockaddr *) & server, sizeof(struct sockaddr_in)) == -1)
	{
		printf("bind error.\n");
		exit(0);
	}
	// 监听套接字开始监听
	if (listen(listenfd,BACKLOG) == -1)
	{
		printf("listen error.\n");
		exit(0);
	}
	
	// 获取网络地址长度
	addr_len = sizeof(struct sockaddr_in);
	
	// 循环，接收客户端请求，并处理
	while(1)
	{
		// 从监听队列获取一个已完成连接的套接字保存到 connectfd
		connectfd = accept(listenfd, (struct sockaddr *) & client, & addr_len);
		if (connectfd == -1)
		{
			printf("error in accept.\n");
			exit(0);
		}
		
		// 创建发送消息子线程
		pthread_create( & tid, NULL, (void *) send_msg, NULL);
		
		// 循环，接收客户端发送来的消息，并显示
        	while(1)
        	{
			// 从 connectfd 套接字接收消息保存到接收消息缓冲区
            		recv(connectfd, recv_buffer, MAXSIZE, 0);
			// 显示接收到的消息
            		printf("client>>%s", recv_buffer);
        	}
	}
	// 等待子线程退出
	pthread_join(tid, NULL);
	// 关闭监听套接字
	close(listenfd);
	// 关闭已连接的套接字
	close(connectfd);

	return 0;
}
