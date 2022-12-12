package itismeucci.chat.lib;
import java.util.*;

//TODO: finish

/** Classe dello schema "send". */
public final class SendSchema extends SignedSchema implements UsernamesContainer
{
	/** [Reflection] crea un'istanza. */
	private SendSchema()
	{
		super("send");
	}

	/**
	 * Crea un'istanza col relativo UUID del richiedente.
	 * @param id ID utente richiedente.
	 */
	protected SendSchema(UUID id, String text, Iterable<String> usernames) throws SchemaException
	{
		super("send", id);
	}

	@Override
	public Iterable<String> getUsernames()
	{
		return null;
	}

	@Override
	public SendSchema cloneSchema()
	{
		return null;
	}

}
