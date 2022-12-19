package itismeucci.chat.lib;

import itismeucci.chat.lib.schemas.JoinErrorSchema;

/** Eccezione generata quando uno schema "join" non è valido. */
public final class JoinException extends ValidationException
{
	/** Tipo di errore. */
	private final JoinErrorType error;

	/**
	 * Crea una istanza col tipo di errore specificato.
	 * @param error Il tipo di errore.
	 */
	public JoinException(JoinErrorType error)
	{
		this.error = error;
	}

	@Override
	public JoinErrorSchema getResponse()
	{
		try
		{
			return new JoinErrorSchema(error);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
