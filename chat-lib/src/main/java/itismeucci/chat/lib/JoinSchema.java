package itismeucci.chat.lib;

/** Classe dello schema "join". */
public final class JoinSchema extends Schema
{
	/** Nome utente. */
	private String username;

	/**
	 * Crea un'istanza con il relativo nome utente.
	 * @param username Nome utente.
	 * @throws JoinException Il nome utente non è valido secondo il protocollo JCSP.
	 */
	protected JoinSchema(String username) throws SchemaException, JoinException
	{
		super("join");

		if (username == null)
			throw new SchemaException();

		if (username.length() < 2 || username.length() > 16)
			throw new JoinException(JoinExceptionType.LENGTH);

		for (var i = 0; i < username.length(); i++)
			if (username.charAt(i) <= 0x20 || username.charAt(i) > 0x7E)
				throw new JoinException(JoinExceptionType.INTERVAL);

		this.username = username;
	}

	/**
	 * Crea un'istanza con il relativo nome utente
	 * controllandone l'unicità.
	 * @param username Nome utente.
	 * @param existingUsernames Insieme di nomi utente esistenti.
	 * @throws JoinException Il nome utente non è valido secondo il protocollo JCSP.
	 */
	protected JoinSchema(String username, Iterable<String> existingUsernames) throws
		SchemaException,
		JoinException
	{
		this(username);

		for (var existingUsername : existingUsernames)
			if (existingUsername.equals(existingUsername))
				throw new JoinException(JoinExceptionType.UNIQUENESS);
	}

	/**
	 * Getter del nome utente.
	 * @return Nome utente.
	 */
	public final String getUsername()
	{
		return username;
	}
}
