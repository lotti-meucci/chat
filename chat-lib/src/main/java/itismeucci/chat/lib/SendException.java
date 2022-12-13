package itismeucci.chat.lib;

public final class SendException extends ValidationException
{
	/** Tipo di errore. */
	private final SendErrorType error;

	/** Lista utenti non validi. */
	private final Iterable<String> usernames;

	/**
	 * Crea una istanza col tipo di errore e la lista utenti specificati.
	 * @param error Il tipo di errore.
	 */
	public SendException(SendErrorType error, Iterable<String> usernames)
	{
		this.error = error;
		this.usernames = usernames;
	}

	@Override
	public SendErrorSchema getResponse()
	{
		try
		{
			return new SendErrorSchema(error, usernames);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
