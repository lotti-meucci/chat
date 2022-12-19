package itismeucci.chat.lib.schemas;
import java.util.*;
import itismeucci.chat.lib.*;

/** Classe dello schema "join-ok". */
public final class JoinOkSchema extends SignedSchema
{
	/** [Reflection] crea un'istanza. */
	private JoinOkSchema()
	{
		super("join-ok");
	}

	/**
	 * Crea un'istanza col relativo UUID.
	 * @param id Nuovo ID utente.
	 */
	public JoinOkSchema(UUID id) throws SchemaException
	{
		super("join-ok", id);
	}

	@Override
	public JoinOkSchema cloneSchema() throws SchemaException
	{
		return new JoinOkSchema(getId());
	}
}
