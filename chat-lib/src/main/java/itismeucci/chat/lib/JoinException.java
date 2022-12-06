package itismeucci.chat.lib;

public final class JoinException extends ValidationException
{
	private final JoinErrorType errorType;

	public JoinException(JoinErrorType errorType)
	{
		this.errorType = errorType;
	}

	@Override
	public JoinErrorSchema getResponse()
	{
		try
		{
			return new JoinErrorSchema(errorType);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
