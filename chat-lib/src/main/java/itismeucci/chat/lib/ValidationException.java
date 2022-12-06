package itismeucci.chat.lib;

/** Classe di base per le eccezioni degli schemi. */
public abstract class ValidationException extends Exception
{
	/**
	 * Getter dello schema di risposta generato.
	 * @return Schema di risposta generato.
	 */
	abstract public Schema getResponse();
}
