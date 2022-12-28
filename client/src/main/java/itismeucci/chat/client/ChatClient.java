package itismeucci.chat.client;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import org.davidmoten.text.utils.*;
import itismeucci.chat.lib.*;
import itismeucci.chat.lib.schemas.*;

/** Classe che rappresenta un client per chat. */
public final class ChatClient extends Client
{
	/** Indirizzo server. */
	public static final String ADDRESS = "localhost";

	/** Porta del server. */
	public static final int PORT = 60005;

	/** Contiene l'ultima lista utenti richiesta. */
	private Iterable<String> latestList = null;

	/**
	 * Crea un'istanza con il relativo nome utente.
	 * Chiamando questo costruttore sarà inviato uno schema "join" al server.
	 * @param username Nome dell'utente.
	 * @throws IOException Impossibile comunicare col server.
	 * @throws ValidationException Errore con la richiesta di accesso.
	 */
	public ChatClient(String username) throws IOException, ValidationException
	{
		super(new DPDUSocket(new Socket(ADDRESS, PORT)), username);
		new Thread(this::listen).start();
	}

	/**
	 * Attende uno schema "list-update" in ingresso e ne ritorna l'attributo "usernames".
	 * @return Lista degli utenti.
	 */
	public Iterable<String> waitListUpdate()
	{
		var oldList = latestList;

		while (oldList == latestList)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (Exception e) { }
		}

		return latestList;
	}

	/**
	 * Invia lo schema specificato al server.
	 * @param schema Schema da inviare.
	 * @throws IOException Errore di connessione col server.
	 */
	public void transferSchema(Schema schema) throws IOException
	{
		transfer(new DPDU(schema, new byte[] { }));
	}

	@Override
	public boolean onDPDUReceived(DPDU dpdu)
	{
		if (!super.onDPDUReceived(dpdu))
			return false;

		var schema = dpdu.getJ();

		if (schema instanceof ListUpdateSchema)
		{
			synchronized (this)
			{
				latestList = ((ListUpdateSchema)schema).getUsernames();
			}
		}
		else if (schema instanceof NotificationSchema)
		{
			var notificationWidth = 40;
			var s = (NotificationSchema)schema;
			System.out.println();
			System.out.println();

			// Se il campo R del DPDU è presente allora ne controlla il contenuto come stringa,
			// se la stringa equivale a "broadcast" allora indica la notifica come di gruppo.
			if (dpdu.getM() > 0 && new String(dpdu.getR()).equals("broadcast"))
				System.out.println("=========== NOTIFICA DI GRUPPO ===========");
			else
				System.out.println("============ NOTIFICA PRIVATA ============");

			// Stampa l'orario e il giorno correnti.
			System.out.println(" " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
			System.out.println(" Mittente: " + s.getSender());
			System.out.println("------------------------------------------");

			// Utilizza una libreria di word-wrapping per far rientrare la notifica nel suo frame.
			System.out.println(
				" " + WordWrap
					.from(s.getText())
					.maxWidth(notificationWidth - 2)
					.insertHyphens(true)
					.wrap()
					.replaceAll("\n", "\n ")
			);

			System.out.println("==========================================");
			System.out.println();
			System.out.print("... ");
		}

		return true;
	}

	@Override
	public boolean onIOException(IOException e)
	{
		if (isExited())
			return false;

		App.printIOErrors(e);
		return true;
	}

	@Override
	public boolean onValidationException(ValidationException e)
	{
		App.printValidationErrors(e);
		return true;
	}
}
