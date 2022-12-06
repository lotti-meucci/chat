package itismeucci.chat.lib;

public final class SchemaException extends ValidationException
{
	@Override
	public SchemaErrorSchema getResponse()
	{
		try
		{
			return new SchemaErrorSchema();
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
