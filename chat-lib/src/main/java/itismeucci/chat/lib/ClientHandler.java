package itismeucci.chat.lib;
import java.util.*;
import itismeucci.chat.lib.schemas.*;

public class ClientHandler implements Runnable
{
	private String username;
	private final Server parent;
	private final DPDUSocket socket;
	private Runnable onHello = () -> { };

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

	public ClientHandler(Server parent, DPDUSocket socket, Runnable onHello)
	{
		this(parent, socket);
		this.onHello = onHello;
	}

	public final Server getParent()
	{
		return parent;
	}

	public final DPDUSocket getSocket()
	{
		return socket;
	}

	public final String getUsername()
	{
		return username;
	}

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
		for (;;)
		{
			DPDU dpdu = null;

			try
			{
				dpdu = getSocket().receive();
			}
			catch (Exception e)
			{
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
					onHello.run();
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
