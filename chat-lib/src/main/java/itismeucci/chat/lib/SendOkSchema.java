package itismeucci.chat.lib;

/** Classe dello schema "send-ok". */
public class SendOkSchema extends Schema
{
	/** Crea un'istanza. */
	public SendOkSchema()
	{
		super("send-ok");
	}

	@Override
	public SendOkSchema cloneSchema()
	{
		return new SendOkSchema();
	}
}
