package itismeucci.chat.lib;

/** Tipo di errore "join". */
public enum JoinErrorType
{
	/** Il nome utente non è univoco. */
	UNIQUENESS,

	/** La lunghezza del nome utente non è valida secondo il protocollo JCSP. */
	LENGTH,

	/** Uno o più caratteri del nome utente non sono validi secondo il protocollo JCSP. */
	INTERVAL
}
