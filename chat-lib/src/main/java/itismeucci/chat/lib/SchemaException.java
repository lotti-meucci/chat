package itismeucci.chat.lib;

/** Eccezione generata quando uno schema è inesistente o mal formattato. */
public final class SchemaException extends ValidationException
{
	@Override
	public SchemaErrorSchema getResponse()
	{
		return new SchemaErrorSchema();
	}
}
