package itismeucci.chat.lib;
import itismeucci.chat.lib.schemas.*;

/**
 * Eccezione generata quando uno schema non ha validità nello stato corrente
 * o ha un UUID errato.
 */
public final class StateException extends ValidationException
{
	@Override
	public StateErrorSchema getResponse()
	{
		return new StateErrorSchema();
	}
}
