package itismeucci.chat.lib.schemas;
import itismeucci.chat.lib.*;

/** Classe dello schema "state-error". */
public final class StateErrorSchema extends Schema
{
	/** Crea un'istanza. */
	public StateErrorSchema()
	{
		super("state-error");
	}

	@Override
	public StateErrorSchema cloneSchema()
	{
		return new StateErrorSchema();
	}
}
