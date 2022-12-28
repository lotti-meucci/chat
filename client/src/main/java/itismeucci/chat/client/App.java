package itismeucci.chat.client;
import java.io.IOException;
import java.util.*;
import itismeucci.chat.lib.*;
import itismeucci.chat.lib.schemas.*;

public class App
{
	public static final Scanner scanner = new Scanner(System.in);
	public static ChatClient client;

	public static void printList(Iterable<String> usernames)
	{
		System.out.println("Lista utenti:");

		for (var username : usernames)
			System.out.println(username);
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
		var command = scanner.nextLine().toLowerCase().trim();

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
					break;

				case "messaggio-gruppo":
					var text = scanner.nextLine();
					var list = getList();
					list.remove(client.getUsername());
					client.transferSchema(new SendSchema(client.getId(), text, list));
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
			try
			{
				client.exit();
				Runtime.getRuntime().halt(0);
			} catch (Exception e) { }
		}));

		boolean repeat;

		do
		{
			repeat = false;
			System.out.println("Chat 1.0");
			System.out.print("Username: ");
			var username = scanner.nextLine();

			try
			{
				client = new ChatClient(username);
			}
			catch (IOException e)
			{
				client.onIOException(e);
				repeat = true;
				continue;
			}
			catch (ValidationException e)
			{
				client.onValidationException(e);
				repeat = true;
				continue;
			}

			System.out.println("scrivere ? per ottenere la lista dei comandi");
			while (runCommandLine());
		} while (repeat);
	}
}
