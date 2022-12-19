package itismeucci.chat.lib.schemas;
import java.util.*;
import itismeucci.chat.lib.*;

/** Classe dello schema "list-update". */
public final class ListUpdateSchema extends Schema implements UsernamesContainer
{
	/** Lista utenti. */
	private String[] usernames;

	/** [Reflection] crea un'istanza. */
	private ListUpdateSchema()
	{
		super("list-update");
	}

	/**
	 * Crea un'istanza con la relativa lista utenti.
	 * @param usernames Lista utenti.
	 * @throws SchemaException Lista utenti nulla.
	 */
	public ListUpdateSchema(Iterable<String> usernames) throws SchemaException
	{
		this();

		if (usernames == null || Utils.exists(null, usernames))
			throw new SchemaException();

		this.usernames = Utils.toArray(usernames, new String[] { });
	}

	@Override
	public Iterable<String> getUsernames()
	{
		return Arrays.asList(usernames.clone());
	}

	@Override
	public ListUpdateSchema cloneSchema() throws SchemaException
	{
		return new ListUpdateSchema(getUsernames());
	}
}
