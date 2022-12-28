package itismeucci.chat.lib;
import java.io.*;
import java.net.*;
import java.util.*;

/** Classe astratta che rappresenta una applicazione server JCSP. */
public abstract class Server
{
	/** Socket TCP per accettare connessioni in ingresso. */
	private final ServerSocket serverSocket;

	/** L'insieme di client connessi identificati dal proprio ID. */
	private final HashMap<UUID, ClientHandler> clients;

	/**
	 * Crea un'istanza con la relativa porta.
	 * @param port Porta del server.
	 * @throws IOException Impossibile utilizzare la porta specificata.
	 */
	public Server(int port) throws IOException
	{
		this.serverSocket = new ServerSocket(port);
		this.clients = new HashMap<>();
	}

	/**
	 * Getter della porta del server.
	 * @return La porta del server.
	 */
	public final int getPort()
	{
		return serverSocket.getLocalPort();
	}

	/**
	 * Getter dei client connessi.
	 * @return L'insieme di client connessi identificati dal proprio ID.
	 */
	public final Map<UUID, ClientHandler> getClients()
	{
		return clients;
	}

	/**
	 * Getter dei nomi utenti dei client connessi.
	 * @return L'insieme di nomi utenti dei client connessi.
	 */
	public final Iterable<String> getUsernames()
	{
		var usernames = new ArrayList<String>();

		for (var client : clients.values())
			usernames.add(client.getUsername());

		return usernames;
	}

	/** Ascolta le connessioni da parte dei client e le gestisce in maniera asincrona. */
	public final void listen()
	{
		for (;;)
		{
			try
			{
				new Thread(new ClientHandler(this, new DPDUSocket(serverSocket.accept()))).start();
			}
			catch (Exception e) { }
		}
	}

	/**
	 * Metodo richiamato all'occorrenza di uno schema "hello" in ricezione da un client.
	 * @param id ID del client richiedente.
	 */
	public abstract void onClientHello(UUID id);
}
