package itismeucci.chat.lib;
import com.fasterxml.jackson.annotation.*;

/** Classe dello schema "join". */
public final class JoinSchema extends Schema
{
	/** Nome utente. */
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private String username;

	private JoinSchema() throws SchemaException
	{
		super("join");
	}

	/**
	 * Crea un'istanza con il relativo nome utente.
	 * @param username Nome utente.
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
	@JsonIgnore
	public String getUsername()
	{
		return username;
	}

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
