package itismeucci.chat.lib;
import java.io.*;
import java.net.*;
import java.util.*;

public abstract class Server
{
	private final ServerSocket serverSocket;
	private final HashMap<UUID, ClientHandler> clients;

	public Server(int port) throws IOException
	{
		this.serverSocket = new ServerSocket(port);
		this.clients = new HashMap<>();
	}

	public final int getPort()
	{
		return serverSocket.getLocalPort();
	}

	public final Map<UUID, ClientHandler> getClients()
	{
		return clients;
	}

	public final Iterable<String> getUsernames()
	{
		var usernames = new ArrayList<String>();

		for (var client : clients.values())
			usernames.add(client.getUsername());

		return usernames;
	}

	public final void listen()
	{
		for (;;)
		{
			try
			{
				new Thread(new ClientHandler(this, new DPDUSocket(serverSocket.accept())));
			}
			catch (Exception e) { }
		}
	}
}
