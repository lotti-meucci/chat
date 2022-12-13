package itismeucci.chat.lib;
import java.util.*;

/** Classe dello schema "send-error". */
public final class SendErrorSchema extends Schema implements UsernamesContainer
{
	/** Tipo di errore. */
	private String error;

	/** Lista utenti non validi. */
	private String[] usernames;

	/** [Reflection] crea un'istanza. */
	private SendErrorSchema()
	{
		super("send-error");
	}

	/**
	 * Crea un'istanza con il relativo tipo di errore e gli utenti non validi.
	 * @param error Il tipo di errore.
	 * @param usernames Nomi utenti non validi.
	 * @throws SchemaException Tipo di errore nullo.
	 */
	public SendErrorSchema(SendErrorType error, Iterable<String> usernames) throws SchemaException
	{
		this();

		if (error == null || usernames == null || Utils.exists(null, usernames))
			throw new SchemaException();

		this.error = error.name().toLowerCase();
		this.usernames = Utils.toArray(usernames, new String[] { });

		if (this.usernames.length > 0 && error != SendErrorType.TARGET)
			throw new SchemaException();

		if (this.usernames.length == 0 && error == SendErrorType.TARGET)
			throw new SchemaException();
	}

	/**
	 * Getter del tipo di errore.
	 * @return Il tipo di errore.
	 */
	public SendErrorType getError()
	{
		try
		{
			return SendErrorType.valueOf(error.toUpperCase());
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public SendErrorSchema cloneSchema() throws SchemaException
	{
		return new SendErrorSchema(getError(), getUsernames());
	}

	@Override
  public Iterable<String> getUsernames()
	{
		return Arrays.asList(usernames.clone());
  }
}
