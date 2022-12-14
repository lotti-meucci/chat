package itismeucci.chat.lib.schemas;
import java.util.*;
import itismeucci.chat.lib.*;

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
	public ListSchema(UUID id) throws SchemaException
	{
		super("list", id);
	}

	@Override
	public ListSchema cloneSchema() throws SchemaException
	{
		return new ListSchema(getId());
	}
}
