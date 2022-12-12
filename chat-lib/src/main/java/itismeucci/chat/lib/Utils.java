package itismeucci.chat.lib;
import com.fasterxml.jackson.databind.*;

import java.util.ArrayList;

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

	public static <T> boolean exists(T id, Iterable<T> existingIds)
	{
		var exists = false;

		if (existingIds != null)
		{
			for (var existingId : existingIds)
			{
				if (id.equals(existingId))
				{
					exists = true;
					break;
				}
			}
		}

		return exists;
	}

	public static <T> T[] toArray(Iterable<T> iterable, T[] array)
	{
		var list = new ArrayList<T>();

		for (var object : iterable)
			list.add(object);

		return list.toArray(array);
	}
}
