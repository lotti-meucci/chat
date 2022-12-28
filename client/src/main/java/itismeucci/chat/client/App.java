package itismeucci.chat.client;
import java.io.*;
import java.util.*;
import itismeucci.chat.lib.*;
import itismeucci.chat.lib.schemas.*;

public final class App
{
	public static final Scanner scanner = new Scanner(System.in);
	public static ChatClient client;

	private App()
	{
		throw new UnsupportedOperationException();
	}

	public static void printList(Iterable<String> usernames)
	{
		System.out.println("Lista utenti:");

		for (var username : usernames)
			System.out.println(username);
	}

	public static void printIOErrors(IOException e)
	{
		System.out.println("OPS! Errore di comunicazione.");
	}

	public static void printValidationErrors(ValidationException e)
	{
		if (e instanceof SchemaException || e instanceof StateException)
			System.out.println("OPS! Errore generico.");
		else if (e instanceof JoinException)
		{
			var s = ((JoinException)e).getResponse();
			System.out.print("OPS! ");

			switch (s.getError())
			{
				case UNIQUENESS:
					System.out.println("Il nome utente non è univoco.");
					break;

				case INTERVAL:
					System.out.println("Il nome utente contiene caratteri non validi.");
					break;

				case LENGTH:
					System.out.println("Il nome utente ha una lunghezza non valida.");
					break;

				default:
					System.out.println("Il nome utente non è valido.");
					break;
			}
		}
		else if (e instanceof SendException)
		{
			var s = ((SendException)e).getResponse();

			switch (s.getError()) {
				case INVISIBLE:
					System.out.println("OPS! Il testo specificato non è valido per un messaggio.");
					break;

				case STILL:
					System.out.println("OPS! Non è stato specificato alcun destinatario.");
					break;

				case TARGET:
					System.out.println("OPS! I seguenti destinatari non sono validi:");
					System.out.println("- " + String.join("\n- ", s.getUsernames()));
					System.out.println();
					break;

				default:
					System.out.println("OPS! Errore nell'invio del messaggio");
					break;
			}
		}
	}

	// Fa richiesta della lista utenti e ne attende l'arrivo asincrono.
	public static ArrayList<String> getList() throws IOException, ValidationException
	{
		client.transferSchema(new ListSchema(client.getId()));
		var iterable = client.waitListUpdate();
		var list = new ArrayList<String>();

		for (var username : iterable)
			list.add(username);

		return list;
	}

	public static boolean runCommandLine()
	{
		System.out.print("> ");
		String command = null;

		try
		{
			command = scanner.nextLine().toLowerCase().trim();
		}
		catch (Exception e)
		{
			return false;
		}

		try
		{
			switch (command)
			{
				case "?":
					System.out.println("Lista comandi:");
					System.out.println("?");
					System.out.println("lista");
					System.out.println("messaggio");
					System.out.println("messaggio-gruppo");
					System.out.println("disconnetti");
					break;

				case "lista":
					printList(getList());
					break;

				case "messaggio":
					System.out.print("Contenuto del messaggio: ");
					String text = null;

					try
					{
						text = scanner.nextLine();
					}
					catch (Exception e)
					{
						return false;
					}

					System.out.print("Username del destinatario: ");
					String target = null;

					try
					{
						target = scanner.nextLine();
					}
					catch (Exception e)
					{
						return false;
					}

					client.transferSchema(new SendSchema(client.getId(), text, List.of(target)));

					try
					{
						Thread.sleep(250);
					}
					catch (Exception e) { }
					break;

				case "messaggio-gruppo":
					var list = getList();
					list.remove(client.getUsername());

					if (list.size() == 0)
					{
						System.out.println("OPS! Sei l'unico utente connesso.");
						break;
					}

					System.out.print("Contenuto del messaggio: ");
					String broadcastText = null;

					try
					{
						broadcastText = scanner.nextLine();
					}
					catch (Exception e)
					{
						return false;
					}

					// Utilizza l'intera lista utenti come destinatario del messaggio.
					// Una stringa "broadcast" è inserita nel campo R del DPDU
					// per indicare che il messaggio è un messaggio di gruppo.
					client.transfer(
						new DPDU(
							new SendSchema(client.getId(), broadcastText, list),
							"broadcast".getBytes()
						)
					);

					try
					{
						Thread.sleep(250);
					}
					catch (Exception e) { }
					break;

				case "disconnetti":
					client.exit();
					return false;

				case "jesus":
					System.out.println();
					System.out.println("   #  ");
					System.out.println(" #####");
					System.out.println("   #  ");
					System.out.println("   #  ");
					System.out.println();
					break;

				case "":
					break;

				default:
					System.out.println("OPS! Comando non esistente.");
					break;
			}
		}
		catch (ValidationException e)
		{
			client.onValidationException(e);
		}
		catch (IOException e)
		{
			client.onIOException(e);
		}

		return true;
	}

	public static void main(String[] args) throws Exception
	{
		Runtime.getRuntime().addShutdownHook(new Thread(() ->
		{
			if (client != null)
				client.exit();

			Runtime.getRuntime().halt(0);
		}));

		System.out.println("Chat 1.0");
		boolean repeat;

		do
		{
			repeat = false;
			System.out.print("Username: ");
			String username = null;

			try
			{
				username = scanner.nextLine();
			}
			catch (Exception e)
			{
				break;
			}

			try
			{
				client = new ChatClient(username);
			}
			catch (IOException e)
			{
				printIOErrors(e);
				repeat = true;
				continue;
			}
			catch (ValidationException e)
			{
				printValidationErrors(e);
				repeat = true;
				continue;
			}

			System.out.println("scrivere ? per ottenere la lista dei comandi");
			while (runCommandLine());
		}
		while (repeat);
	}
}
