package itismeucci.chat.lib.schemas;
import itismeucci.chat.lib.*;

/** Classe dello schema "join". */
public final class JoinSchema extends Schema
{
	/** Nome utente. */
	private String username;

	/** [Reflection] crea un'istanza. */
	private JoinSchema()
	{
		super("join");
	}

	/**
	 * Crea un'istanza con il relativo nome utente.
	 * @param username Nome utente.
	 * @throws SchemaException Il nome utente è nullo.
	 * @throws JoinException Il nome utente non è valido secondo il protocollo JCSP.
	 */
	public JoinSchema(String username) throws SchemaException, JoinException
	{
		this();

		if (username == null)
			throw new SchemaException();

		if (username.length() < 2 || username.length() > 16)
			throw new JoinException(JoinErrorType.LENGTH);

		for (var i = 0; i < username.length(); i++)
			if (username.charAt(i) <= 0x20 || username.charAt(i) > 0x7E)
				throw new JoinException(JoinErrorType.INTERVAL);

		this.username = username;
	}

	/**
	 * Getter del nome utente.
	 * @return Nome utente.
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * Genera un'eccezione se il nome utente esiste già.
	 * @param existingUsernames Nomi utente esistenti.
	 * @throws JoinException Il nome utente è gia presente (uniqeness).
	 */
	public void checkUsernameUniqeness(Iterable<String> existingUsernames) throws JoinException
	{
		if (existingUsernames == null)
			return;

		for (var existingUsername : existingUsernames)
			if (getUsername().equals(existingUsername))
				throw new JoinException(JoinErrorType.UNIQUENESS);
	}

	@Override
	public JoinSchema cloneSchema() throws SchemaException, JoinException
	{
		return new JoinSchema(getUsername());
	}
}
