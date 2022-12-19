package itismeucci.chat.lib.schemas;
import itismeucci.chat.lib.*;

/** Classe dello schema "notification". */
public class NotificationSchema extends Schema
{
	/** Utente mittente (username). */
	private String sender;

	/** Contenuto notifica. */
	private String text;

	/** [Reflection] crea un'istanza. */
	private NotificationSchema()
	{
		super("notification");
	}

	/**
	 * Crea un'istanza col relativo mittente e contenuto della notifica.
	 * @param sender Nome utente del mittente.
	 * @param text Contenuto della notifica.
	 * @throws SchemaException Mittente e/o contenuto nulli.
	 */
	public NotificationSchema(String sender, String text) throws SchemaException
	{
		this();

		if (sender == null || text == null)
			throw new SchemaException();

		this.sender = sender;
		this.text = text;
	}

	/**
	 * Getter del mittente della notifica.
	 * @return Il mittente della notifica.
	 */
	public String getSender()
	{
		return sender;
	}

	/**
	 * Getter del contenuto della notifica.
	 * @return Il contenuto della notifica.
	 */
	public String getText()
	{
		return text;
	}

	@Override
	public NotificationSchema cloneSchema() throws SchemaException
	{
		return new NotificationSchema(getSender(), getText());
	}
}
