package itismeucci.chat.lib;
import java.util.*;

/** Classe di base per ogni schema JSON di JCSP. */
public abstract class Schema
{
	/** Nome dello schema. */
	private String schema;

	/** Associazioni nome delle schema / classe dello schema. */
	private static final Map<String, Class<? extends Schema>> bindings = Map.of(
		"join", JoinSchema.class
	);

	/**
	 * Crea un'istanza con il relativo nome dello schema.
	 * @param schema Nome dello schema.
	 */
	protected Schema(String schema)
	{
		if (schema == null)
			throw new IllegalArgumentException("'schema' is null.");

		this.schema = schema;
	}

	/**
	 * Getter del nome dello schema.
	 * @return Nome dello schema.
	 */
	public final String getSchema()
	{
		return schema;
	}

	/**
	 * Questo metodo genera un'istanza schema da JSON.
	 * @param json Stringa JSON da interpretare.
	 * @return Istanza della classe relativa allo schema.
	 */
	public final static Schema fromJson(String json) throws
		SchemaException
	{
		try
		{
			var node = Utils.mapper.readTree(json);
			var name = node.get("schema").asText();

			if (bindings.containsKey(name))
				return Utils.mapper.readValue(json, bindings.get(name));
			else
				throw new Exception();
		}
		catch (Exception e)
		{
			throw new SchemaException();
		}
	}
}
