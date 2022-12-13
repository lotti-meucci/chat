package itismeucci.chat.lib;

/** Classe dello schema "check". */
public class CheckSchema extends Schema
{
	/** Crea un'istanza. */
	public CheckSchema()
	{
		super("check");
	}

	@Override
	public CheckSchema cloneSchema()
	{
		return new CheckSchema();
	}
}
