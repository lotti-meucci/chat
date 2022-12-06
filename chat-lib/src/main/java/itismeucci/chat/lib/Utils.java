package itismeucci.chat.lib;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect.*;

/** Utility statiche interne alla libreria. */
public final class Utils
{
	/** Jackson mapper. */
	public static final ObjectMapper mapper = new ObjectMapper();

	static
	{
		// Imposta il mapper per avere accesso unicamente ai campi escludendo i getter.
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}

	private Utils()
	{
		throw new UnsupportedOperationException();
	}
}
