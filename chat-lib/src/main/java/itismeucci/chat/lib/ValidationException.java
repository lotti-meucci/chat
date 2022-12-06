package itismeucci.chat.lib;

public abstract class ValidationException extends Exception
{
	abstract public Schema getResponse();
}
