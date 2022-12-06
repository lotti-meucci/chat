package itismeucci.chat.lib;

/** Classe dello schema "join-error". */
public final class JoinErrorSchema extends Schema
{
	/** Tipo di errore. */
	private String error;

	/** [Reflection] crea un'istanza. */
	private JoinErrorSchema()
	{
		super("join-error");
	}

	/**
	 * Crea un'istanza con il relativo tipo di errore.
	 * @param error Il tipo di errore.
	 * @throws SchemaException Tipo di errore nullo.
	 */
	protected JoinErrorSchema(JoinErrorType error) throws SchemaException
	{
		this();

		if (error == null)
			throw new SchemaException();

		this.error = error.name().toLowerCase();
	}

	/**
	 * Getter del tipo di errore.
	 * @return Il tipo di errore.
	 */
	public JoinErrorType getError()
	{
		try
		{
			return JoinErrorType.valueOf(error.toUpperCase());
		}
		catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public JoinErrorSchema cloneSchema() throws SchemaException
	{
		return new JoinErrorSchema(getError());
	}
}
