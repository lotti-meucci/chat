package itismeucci.chat.lib;
import java.util.*;
import com.fasterxml.jackson.annotation.*;

/** Classe di base di uno schema JSON di JCSP con ID. */
public abstract class SignedSchema extends Schema
{
	/** UUID dell'utente. */
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
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
	 * Getter dell'UUID dell'utente.
	 * @return UUID dell'utente.
	 */
	@JsonIgnore
	public final UUID getId()
	{
		return id;
	}

	public final void checkIdExistence(Iterable<UUID> existingIds) throws StateException
	{
		var exists = false;

		if (existingIds != null)
		{
			for (var existingId : existingIds)
			{
				if (getId().equals(existingId))
				{
					exists = true;
					break;
				}
			}
		}

		if (!exists)
			throw new StateException();
	}
}
