package itismeucci.chat.lib;
import java.util.*;
import itismeucci.chat.lib.schemas.*;

/** Classe che rappresenta un client JCSP dal punto di vista di un server JCSP. */
public class ClientHandler implements Runnable
{
	/** Nome dell'utente */
	private String username;

	/** Server padre del client. */
	private final Server parent;

	/** Canale di trasmissione. */
	private final DPDUSocket socket;

	/**
	 * Crea un'istanza con il relativo server padre e canale di trasmissione.
	 * @param parent Il server padre del client.
	 * @param socket Il canale di trasmissione.
	 */
	public ClientHandler(Server parent, DPDUSocket socket)
	{
		if (parent == null)
			throw new IllegalArgumentException("'parent' was null.");

		if (socket == null)
			throw new IllegalArgumentException("'socket' was null.");

		this.username = "";
		this.parent = parent;
		this.socket = socket;
	}

	/**
	 * Getter del server padre.
	 * @return Il server padre del client.
	 */
	public final Server getParent()
	{
		return parent;
	}

	/**
	 * Getter del canale di trasmissione.
	 * @return Il canale di trasmissione.
	 */
	public final DPDUSocket getSocket()
	{
		return socket;
	}

	/**
	 * Getter del nome utente del client.
	 * @return Il nome utente del client.
	 */
	public final String getUsername()
	{
		return username;
	}

	/**
	 * Tenta il trasferimento di un DPDU da parte del server verso il client.
	 * @param dpdu DPDU da trasferire.
	 * @return Vero se la trasmissione è andata a buon fine, altrimenti falso.
	 */
	private final boolean tryTransfer(DPDU dpdu)
	{
		try
		{
			getSocket().transfer(dpdu);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	@Override
	public void run()
	{
		var maxSleepCount = 5;
		var sleepCount = 0;

		for (;;)
		{
			DPDU dpdu = null;

			try
			{
				dpdu = getSocket().receive();
				sleepCount = 0;
			}
			catch (Exception e)
			{
				if (sleepCount >= maxSleepCount)
				{
					var id = new UUID(0, 0);

					for (var entry : this.parent.getClients().entrySet())
					{
						if (entry.getValue() == this)
						{
							id = entry.getKey();
							break;
						}
					}

					getParent().getClients().remove(id);
					getSocket().close();
					return;
				}

				try
				{
					Thread.sleep(2000);
				}
				catch (Exception x) { }

				++sleepCount;
				continue;
			}

			var schema = dpdu.getJ();

			try
			{
				if (schema instanceof SignedSchema)
					((SignedSchema)schema).checkIdExistence(getParent().getClients().keySet());

				if (schema instanceof JoinSchema)
				{
					var s = (JoinSchema)schema;
					s.checkUsernameUniqeness(getParent().getUsernames());
					username = s.getUsername();
					var id = UUID.randomUUID();
					getParent().getClients().put(id, this);
					tryTransfer(new DPDU(new JoinOkSchema(id), dpdu.getR()));
				}
				else if (schema instanceof ListSchema)
					tryTransfer(new DPDU(new ListUpdateSchema(getParent().getUsernames()), dpdu.getR()));
				else if (schema instanceof SendSchema)
				{
					var s = (SendSchema)schema;
					s.checkUsernamesValidity(getUsername(), parent.getUsernames());
					var clients = getParent().getClients().values();

					// Invia una notifica ad ogni utente selezionato dalla richiesta.
					for (var client : clients)
					{
						for (var username : s.getUsernames())
						{
							if (client.getUsername().equals(username))
							{
								client.tryTransfer(new DPDU(
									new NotificationSchema(getUsername(), s.getText()),
									dpdu.getR()
								));
							}
						}
					}

					tryTransfer(new DPDU(new SendOkSchema(), dpdu.getR()));
				}
				else if (schema instanceof HelloSchema)
					parent.onClientHello(((HelloSchema)schema).getId());
				else if (schema instanceof ExitSchema)
				{
					getParent().getClients().remove(((ExitSchema)schema).getId());
					getSocket().close();
					return;
				}
			}
			catch (ValidationException e)
			{
				tryTransfer(new DPDU(e.getResponse(), dpdu.getR()));
			}
		}
	}
}
