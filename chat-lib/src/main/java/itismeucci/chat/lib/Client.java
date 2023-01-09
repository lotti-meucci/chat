package itismeucci.chat.lib;
import java.io.*;
import java.util.*;
import itismeucci.chat.lib.schemas.*;

/** Classe astratta che rappresenta una applicazione client JCSP. */
public abstract class Client
{
	/** Indica se il client è in ascolto per messaggi da parte del server. */
	private boolean listening;

	/** ID del client. */
	private final UUID id;

	/** Nome utente del client. */
	private final String username;

	/** Canale di trasmissione. */
	private final DPDUSocket socket;

	/**
	 * Crea un'istanza con il relativo canale di trasmissione e nome utente.
	 * Chiamando questo costruttore sarà inviato uno schema "join" al server.
	 * @param socket Canale di trasmissione.
	 * @param username Nome dell'utente.
	 * @throws IOException Impossibile comunicare col server.
	 * @throws ValidationException Errore con la richiesta di accesso.
	 */
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

	/**
	 * Getter del canale di trasmissione.
	 * @return Il canale di trasmissione.
	 */
	public final DPDUSocket getSocket()
	{
		return socket;
	}

	/**
	 * Getter del nome utente.
	 * @return Il nome utente.
	 */
	public final String getUsername()
	{
		return username;
	}

	/**
	 * Getter dell'ID del client.
	 * @return L'ID del client.
	 */
	public final UUID getId()
	{
		return id;
	}

	/**
	 * Indica se il client è in ascolto per messaggi da parte del server.
	 * @return Vero se il client è in ascolto, altrimenti falso.
	 */
	public final boolean isListening()
	{
		return listening;
	}


	public final boolean isExited()
	{
		return socket.isClosed();
	}

	/**
	 * Invia un DPDU al server.
	 * @param dpdu DPDU da inviare.
	 * @throws IOException Impossibile comunicare col server.
	 */
	public final void transfer(DPDU dpdu) throws IOException
	{
		getSocket().transfer(dpdu);
	}

	/** Resta in ascolto per DPDU inviati dal server. */
	public final void listen()
	{
		if (isListening())
			throw new IllegalStateException("The client was already listening.");

		listening = true;

		while (listening)
		{
			try
			{
				var dpdu = getSocket().receive();
				var schema = dpdu.getJ();

				if (schema instanceof SchemaErrorSchema)
					throw new SchemaException();
				else if (schema instanceof StateErrorSchema)
					throw new StateException();
				else if (schema instanceof JoinErrorSchema)
					throw new JoinException(((JoinErrorSchema)schema).getError());
				else if (schema instanceof SendErrorSchema)
				{
					var s = (SendErrorSchema)schema;
					throw new SendException(s.getError(), s.getUsernames());
				}

				listening = onDPDUReceived(dpdu);
			}
			catch (IOException e)
			{
				listening = onIOException(e);
			}
			catch (ValidationException e)
			{
				listening = onValidationException(e);
			}
			catch (Exception e)
			{
				return;
			}
		}
	}

	/**
	 * Interrompe la comunicazione col server inviando uno schema "exit"
	 * e chiudendo il canale di trasmissione.
	 */
	public final void exit()
	{
		try
		{
			transfer(new DPDU(new ExitSchema(id), new byte[] { }));
		}
		catch (Exception e) { }
	}

	/**
	 * Metodo richiamato all'occorrenza in ricezione di un DPDU da parte del server
	 * (di base supporta le richieste con schema "check").
	 * @param dpdu DPDU ricevuto.
	 * @return Falso se il client deve smettere di ascoltare in ricezione, altrimenti vero.
	 */
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

	/**
	 * Metodo richiamato all'occorrenza di un'eccezione di tipo I/O durante l'ascolto in ricezione.
	 * @param e L'eccezione in questione.
	 * @return Falso se il client deve smettere di ascoltare in ricezione, altrimenti vero.
	 */
	public abstract boolean onIOException(IOException e);

	/**
	 * Metodo richiamato all'occorrenza di un'eccezione di validazione del DPDU
	 * durante l'ascolto in ricezione.
	 * @param e L'eccezione in questione.
	 * @return Falso se il client deve smettere di ascoltare in ricezione, altrimenti vero.
	 */
	public abstract boolean onValidationException(ValidationException e);
}
