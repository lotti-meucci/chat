package itismeucci.chat.lib;

public final class StateErrorSchema extends Schema
{
	public StateErrorSchema() throws SchemaException
	{
		super("state-error");
	}

	@Override
	public StateErrorSchema cloneSchema() throws SchemaException
	{
		return new StateErrorSchema();
	}
}
