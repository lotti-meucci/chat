package itismeucci.chat.lib;

public final class JoinException extends Exception
{
	private final JoinExceptionType type;

	public JoinException(JoinExceptionType type)
	{
		super(type.name().toLowerCase());
		this.type = type;
	}

	public JoinExceptionType getType()
	{
		return type;
	}
}
