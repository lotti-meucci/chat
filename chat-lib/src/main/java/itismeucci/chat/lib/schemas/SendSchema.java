package itismeucci.chat.lib.schemas;
import java.util.*;
import itismeucci.chat.lib.*;

/** Classe dello schema "send". */
public final class SendSchema extends SignedSchema implements UsernamesContainer
{
	/** Contenuto del messaggio. */
	private String text;

	/** Lista utenti destinatari. */
	private String[] usernames;

	/** [Reflection] crea un'istanza. */
	private SendSchema()
	{
		super("send");
	}

	/**
	 * Crea un'istanza col relativo UUID del richiedente,
	 * il contenuto testuale del messaggio
	 * e la lista degli utenti destinatari.
	 * @param id ID utente richiedente.
	 * @param text Contenuto del messaggio.
	 * @param usernames Nomi utente dei destinatari del messaggio.
	 * @throws SchemaException Tipo di errore nullo.
	 *
	 * @throws SendException
	 * 	La lista destinatari o il contenuto del messaggio non sono validi secondo JCSP
	 */
	public SendSchema(UUID id, String text, Iterable<String> usernames) throws
		SchemaException,
		SendException
	{
		super("send", id);

		if (text == null || usernames == null || Utils.exists(null, usernames))
			throw new SchemaException();

		if (text.isEmpty())
			throw new SendException(SendErrorType.INVISIBLE, List.of());

		this.text = text;
		this.usernames = Utils.toArray(usernames, new String[] { });

		if (this.usernames.length == 0)
			throw new SendException(SendErrorType.STILL, usernames);
	}

	/**
	 * Getter del contenuto del messaggio.
	 * @return Il contenuto del messaggio.
	 */
	public String getText()
	{
		return text;
	}

	public void checkUsernamesValidity(String sender, Iterable<String> existingUsernames) throws
		SendException
	{
		var invalidUsernames = new ArrayList<String>();

		for (var username : usernames)
			if (!Utils.exists(username, existingUsernames) || username.equals(sender))
				invalidUsernames.add(username);

		if (invalidUsernames.size() > 0)
			throw new SendException(SendErrorType.TARGET, invalidUsernames);
	}

	@Override
	public Iterable<String> getUsernames()
	{
		return Arrays.asList(usernames.clone());
	}

	@Override
	public SendSchema cloneSchema() throws SchemaException, SendException
	{
		return new SendSchema(getId(), getText(), getUsernames());
	}
}
