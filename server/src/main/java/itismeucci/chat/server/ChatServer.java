package itismeucci.chat.server;
import java.io.*;
import java.util.*;

import itismeucci.chat.lib.*;

public final class ChatServer extends Server
{
	/** Porta del server. */
	public static final int PORT = 60005;

	/** Crea un'istanza. */
	public ChatServer() throws IOException
	{
		super(PORT);
	}

	@Override
	public void onClientHello(UUID id) { }
}
