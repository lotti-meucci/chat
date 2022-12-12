package itismeucci.chat.client;
import java.util.*;
import itismeucci.chat.lib.*;

public class App
{
	private static final Scanner scanner = new Scanner(System.in);
	private static final ChatClient client = new ChatClient();

	private static void printList(String[] usernames)
	{
		System.out.println("Lista utenti:");
		for(int i=0;i<usernames.length;i++){
			System.out.println(usernames[i]);
		}
	}

	private static boolean runCommandLine()
	{
		System.out.print("> ");

		var comando = scanner.nextLine().toLowerCase().trim();
		switch (comando) {
			case "?":
				System.out.println("Lista comandi:");
				System.out.println("?");
				System.out.println("lista");
				System.out.println("messaggio");
				System.out.println("messaggio-gruppo");
				System.out.println("disconnetti");
				break;
			case "lista":

				break;

			case "messaggio":

				break;

			case "messaggio-gruppo":

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
			System.out.println("Comando non esistente");
				break;
		}

		return true;
	}

	public static void main(String[] args) throws Exception
	{
		System.out.println("Chat 1.0");
		System.out.print("username: ");
		var username = scanner.nextLine();

		var schema = client.join(username);
		if(schema instanceof JoinErrorSchema){
			var errorSchema = (JoinErrorSchema)schema;
			System.out.print("ERRORE: ");
			switch (errorSchema.getError()) {
				case UNIQUENESS:
					System.out.println("nome utente non univoco");
					break;

				case INTERVAL:
					System.out.println("contiene caratteri non validi");
					break;

				case LENGTH:
					System.out.println("lunghezza non valida");
					break;

				default:

					break;
			}
		}
		else if(schema instanceof JoinOkSchema){
			var okSchema = (JoinOkSchema)schema;
			System.out.println("scrivere ? per ottenere la lista dei comandi");
			while (runCommandLine());
		}
		else {
			System.out.println("ERRORE GENERICO");
		}
	}
}
