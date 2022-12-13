package itismeucci.chat.lib;
import java.util.*;

/** Classe di base per ogni schema JSON di JCSP. */
public abstract class Schema
{
	/** Nome dello schema. */
	private String schema;

	/** Classi degli schemi. */
	private static final List<Class<? extends Schema>> classes = List.of(
		SchemaErrorSchema.class,
		StateErrorSchema.class,
		JoinSchema.class,
		JoinErrorSchema.class,
		JoinOkSchema.class
	);

	/**
	 * Crea un'istanza con il relativo nome dello schema.
	 * @param schema Nome dello schema.
	 */
	protected Schema(String schema)
	{
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
	 * Clona l'istanza corrente.
	 * @return Una copia dell'istanza corrente.
	 * @throws ValidationException Clonazione non valida.
	 */
	public abstract Schema cloneSchema() throws ValidationException;

	/**
	 * Getter della classe per nome dello schema.
	 * @param name Nome dello schema.
	 * @return Classe con il relativo nome dello schema.
	 */
	private static Class<? extends Schema> getSchemaClass(String name)
	{
		if (name != null)
		{
			for (var schemaClass : classes)
			{
				try
				{
					// Ottiene il construttore vuoto della classe e lo rende accessibile.
					var constructor = schemaClass.getDeclaredConstructor();
					constructor.setAccessible(true);

					// Crea un'istanza della classe con il costruttore appena ottenuto
					// e controlla se il suo nome dello schema equivale al nome specificato.
					if (name.equals(constructor.newInstance().getSchema()))
						return schemaClass;
				}
				catch (Exception e) { }
			}
		}

		return null;
	}

	/**
	 * Questo metodo genera un'istanza schema da JSON.
	 * @param json Stringa JSON da interpretare.
	 * @return Istanza della classe relativa allo schema.
	 * @throws ValidationException Lo schema non è valido.
	 * @throws SchemaException Il nome dello schema non esiste.
	 */
	public final static Schema fromJson(String json) throws
		ValidationException, SchemaException
	{
		Schema schema = null;

		try
		{
			var node = Utils.mapper.readTree(json);
			var name = node.get("schema").asText();
			var schemaClass = getSchemaClass(name);

			if (schemaClass == null)
				throw new Exception();

			schema = Utils.mapper.readValue(json, schemaClass);
		}
		catch (Exception e)
		{
			throw new SchemaException();
		}

		// Clona lo schema per controllarne la validità.
		schema.cloneSchema();

		return schema;
	}

	/**
	 * Converte lo schema specificato in una stringa JSON.
	 * @param obj Oggetto dello schema.
	 * @return Stringa JSON corrispondente allo schema specificato.
	 */
	public final static String toJson(Schema obj)
	{
		if (obj == null)
			throw new IllegalArgumentException("'obj' was null.");

		try
		{
			return Utils.mapper.writeValueAsString(obj);
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("'obj' was invalid.");
		}
	}
}
