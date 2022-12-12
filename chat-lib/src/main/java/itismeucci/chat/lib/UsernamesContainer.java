package itismeucci.chat.lib;

/** Interfaccia per uno schema JSON di JCSP con ID. */
public interface UsernamesContainer
{
	/**
	 * Getter della lista dei nomi utenti.
	 * @return Lista di nomi utenti.
	 */
	public Iterable<String> getUsernames();
}
