package itismeucci.chat.lib;
import java.util.*;

/** Classe dello schema "list". */
public final class ListSchema extends SignedSchema
{
	/** [Reflection] crea un'istanza. */
	private ListSchema()
	{
		super("list");
	}

	/**
	 * Crea un'istanza col relativo UUID del richiedente.
	 * @param id ID utente richiedente.
	 */
	protected ListSchema(UUID id) throws SchemaException
	{
		super("list", id);
	}

	@Override
	public ListSchema cloneSchema() throws SchemaException
	{
		return new ListSchema(getId());
	}
}
