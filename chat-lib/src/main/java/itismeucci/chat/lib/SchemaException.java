package itismeucci.chat.lib;
import itismeucci.chat.lib.schemas.*;

/** Eccezione generata quando uno schema è inesistente o mal formattato. */
public final class SchemaException extends ValidationException
{
	@Override
	public SchemaErrorSchema getResponse()
	{
		return new SchemaErrorSchema();
	}
}
