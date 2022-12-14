package itismeucci.chat.lib;
import java.util.*;

/** Classe di base di uno schema JSON di JCSP con ID. */
public abstract class SignedSchema extends Schema
{
	/** UUID dell'utente. */
	private UUID id;

	/**
	 * Crea un'istanza con il relativo nome dello schema.
	 * @param schema Nome dello schema.
	 */
	protected SignedSchema(String schema)
	{
		super(schema);
	}

	/**
	 * Crea un'istanza con il relativo nome dello schema e ID utente.
	 * @param schema Nome dello schema.
	 * @param id UUID dell'utente.
	 * @throws SchemaException ID nullo.
	 */
	protected SignedSchema(String schema, UUID id) throws SchemaException
	{
		super(schema);

		if (id == null)
			throw new SchemaException();

		this.id = id;
	}

	/**
	 * Getter dell'UUID dell'utente.
	 * @return UUID dell'utente.
	 */
	public final UUID getId()
	{
		return id;
	}

	/**
	 * Genera un'eccezione se l'ID non esiste già.
	 * @param existingIds UUID esistenti.
	 * @throws StateException UUID non presente.
	 */
	public final void checkIdExistence(Iterable<UUID> existingIds) throws StateException
	{
		if (!Utils.exists(getId(), existingIds))
			throw new StateException();
	}
}
