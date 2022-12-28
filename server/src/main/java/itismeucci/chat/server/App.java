package itismeucci.chat.server;

public class App
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("Server in ascolto sulla porta " + ChatServer.PORT);
		var server = new ChatServer();
		server.listen();
	}
}
