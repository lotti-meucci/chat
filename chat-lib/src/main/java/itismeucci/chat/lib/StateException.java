package itismeucci.chat.lib;

public final class StateException extends ValidationException
{
	@Override
	public StateErrorSchema getResponse()
	{
		try
		{
			return new StateErrorSchema();
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
