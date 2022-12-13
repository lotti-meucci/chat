package itismeucci.chat.lib;

/** Tipo di errore "send". */
public enum SendErrorType
{
	/** Il testo non è valido. */
	INVISIBLE,

	/** Non ci sono destinatari. */
	STILL,

	/** I destinatari non sono validi. */
	TARGET
}
