package itismeucci.chat.server;

public final class App
{
	private App()
	{
		throw new UnsupportedOperationException();
	}

	public static void main(String[] args) throws Exception
	{
		System.out.println("Server in ascolto sulla porta " + ChatServer.PORT);
		var server = new ChatServer();
		server.listen();
	}
}
