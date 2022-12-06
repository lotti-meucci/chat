package itismeucci.chat.lib;

public final class SchemaErrorSchema extends Schema
{
	public SchemaErrorSchema() throws SchemaException
	{
		super("schema-error");
	}

	@Override
	public SchemaErrorSchema cloneSchema() throws SchemaException
	{
		return new SchemaErrorSchema();
	}
}
