package itismeucci.chat.lib;

/** Classe dello schema "schema-error". */
public final class SchemaErrorSchema extends Schema
{
	/** Crea un'istanza. */
	public SchemaErrorSchema()
	{
		super("schema-error");
	}

	@Override
	public SchemaErrorSchema cloneSchema()
	{
		return new SchemaErrorSchema();
	}
}
