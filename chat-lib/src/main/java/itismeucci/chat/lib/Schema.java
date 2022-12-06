package itismeucci.chat.lib;
import java.util.*;
import com.fasterxml.jackson.annotation.*;

/** Classe di base per ogni schema JSON di JCSP. */
public abstract class Schema
{
	/** Nome dello schema. */
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private String schema;

	/** Classi degli schemi. */
	private static final List<Class<? extends Schema>> classes = List.of(
		SchemaErrorSchema.class,
		StateErrorSchema.class,
		JoinSchema.class,
		JoinErrorSchema.class
	);

	/**
	 * Crea un'istanza con il relativo nome dello schema.
	 * @param schema Nome dello schema.
	 */
	protected Schema(String schema) throws SchemaException
	{
		this.schema = schema;
	}

	/**
	 * Getter del nome dello schema.
	 * @return Nome dello schema.
	 */
	@JsonIgnore
	public final String getSchema()
	{
		return schema;
	}

	public abstract Schema cloneSchema() throws ValidationException;

	private static Class<? extends Schema> getSchemaClass(String name)
	{
		if (name != null)
		{
			for (var schemaClass : classes)
			{
				try
				{
					var constructor = schemaClass.getDeclaredConstructor();
					constructor.setAccessible(true);

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

		schema.cloneSchema();
		return schema;
	}

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
