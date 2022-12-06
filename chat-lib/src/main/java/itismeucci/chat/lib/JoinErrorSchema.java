package itismeucci.chat.lib;
import com.fasterxml.jackson.annotation.*;

/** Classe dello schema "join-error". */
public final class JoinErrorSchema extends Schema
{
	/** Tipo di errore. */
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	private String error;

	private JoinErrorSchema() throws SchemaException
	{
		super("join-error");
	}

	protected JoinErrorSchema(JoinErrorType error) throws SchemaException
	{
		this();

		if (error == null)
			throw new IllegalArgumentException("'joinErrorType' was null.");

		this.error = error.name().toLowerCase();
	}

	@JsonIgnore
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
		try
		{
			return new JoinErrorSchema();
		}
		catch (SchemaException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
