package itismeucci.chat.lib;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;

/** Utility statiche interne alla libreria. */
public final class Utils
{
	/** Jackson mapper. */
	public static final ObjectMapper mapper = new ObjectMapper();

	static
	{
		// Imposta il mapper per avere accesso unicamente ai campi escludendo i getter.
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
	}

	/** Questo costruttore se chiamato genera un'eccezione. */
	private Utils()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Controlla se un oggetto fa parte di un oggetto iterabile.
	 * @param <T> Tipo dell'oggetto.
	 * @param object L'oggetto in questione.
	 * @param existingObjects L'oggetto iterabile.
	 * @return Vero se l'oggetto esiste, altrimenti falso.
	 */
	public static <T> boolean exists(T object, Iterable<T> existingObjects)
	{
		var exists = false;

		if (existingObjects != null)
		{
			for (var existingObj : existingObjects)
			{
				if (object == null)
				{
					if (object == existingObj)
					{
						exists = true;
						break;
					}
				}
				else if (object.equals(existingObj))
				{
					exists = true;
					break;
				}
			}
		}

		return exists;
	}

	/**
	 * Converte un oggetto iterabile in un array.
	 * @param <T> Tipo in questione.
	 * @param iterable L'oggetto iterabile.
	 * @param array L'istanza dell'array di ritorno.
	 * @return L'array in questione.
	 */
	public static <T> T[] toArray(Iterable<T> iterable, T[] array)
	{
		var list = new ArrayList<T>();

		for (var object : iterable)
			list.add(object);

		return list.toArray(array);
	}
}
