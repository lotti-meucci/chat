package itismeucci.chat.lib;
import java.util.*;

/** Classe di base di uno schema JSON di JCSP con ID. */
public abstract class SignedSchema extends Schema
{
	/** UUID dell'utente. */
	private UUID id;

	/**
	 * Crea un'istanza con il relativo nome dello schema e ID utente.
	 * @param schema Nome dello schema.
	 * @param id UUID dell'utente.
	 */
	protected SignedSchema(String schema, UUID id) throws SchemaException
	{
		super(schema);

		if (id == null)
			throw new SchemaException();

		this.id = id;
	}

	/**
	 * Crea un'istanza con il relativo nome dello schema e ID utente
	 * controllandone l'esistenza.
	 * @param schema Nome dello schema.
	 * @param id UUID dell'utente.
	 * @param existingIds Insieme di UUID esistenti.
	 */
	protected SignedSchema(String schema, UUID id, Iterable<UUID> existingIds) throws
		SchemaException,
		StateException
	{
		this(schema, id);
		var exists = false;

		for (var existingId : existingIds)
		{
			if (id.equals(existingId))
			{
				exists = true;
				break;
			}
		}

		if (!exists)
			throw new StateException();
	}

	/**
	 * Getter dell'UUID dell'utente.
	 * @return UUID dell'utente.
	 */
	public final UUID getId()
	{
		return id;
	}
}
