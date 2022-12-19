package itismeucci.chat.lib;
import java.io.*;
import java.util.*;
import itismeucci.chat.lib.schemas.*;

public abstract class Client
{
	private boolean listening;
	private final UUID id;
	private final String username;
	private final DPDUSocket socket;

	public Client(DPDUSocket socket, String username) throws IOException, ValidationException
	{
		if (socket == null)
			throw new IllegalArgumentException("'socket' was null.");

		synchronized (socket)
		{
			socket.transfer(new DPDU(new JoinSchema(username), new byte[] { }));
			var schema = socket.receive().getJ();

			if (schema instanceof JoinOkSchema)
				id = ((JoinOkSchema)schema).getId();
			else if (schema instanceof JoinErrorSchema)
				throw new JoinException(((JoinErrorSchema)schema).getError());
			else
				throw new StateException();
		}

		this.socket = socket;
		this.username = username;
		this.listening = false;
	}

	public final DPDUSocket getSocket()
	{
		return socket;
	}

	public final String getUsername()
	{
		return username;
	}

	public final UUID getId()
	{
		return id;
	}

	public final boolean isListening()
	{
		return listening;
	}

	public final void transfer(DPDU dpdu) throws IOException
	{
		getSocket().transfer(dpdu);
	}

	public final void listen()
	{
		if (isListening())
			throw new IllegalStateException("The client was already listening.");

		while (listening)
		{
			try
			{
				listening = onDPDUReceived(getSocket().receive());
			}
			catch (IOException e)
			{
				listening = onIOException(e);
			}
			catch (ValidationException e)
			{
				listening = onValidationException(e);
			}
		}
	}

	public final void exit()
	{
		try
		{
			transfer(new DPDU(new ExitSchema(id), new byte[] { }));
		}
		catch (Exception e) { }

		getSocket().close();
	}

	public boolean onDPDUReceived(DPDU dpdu)
	{
		if (dpdu == null)
			return false;

		if (dpdu.getJ() instanceof CheckSchema)
		{
			try
			{
				transfer(new DPDU(new HelloSchema(getId()), dpdu.getR()));
			}
			catch (Exception e) { }
		}

		return true;
	}

	public abstract boolean onIOException(IOException e);
	public abstract boolean onValidationException(ValidationException e);
}
