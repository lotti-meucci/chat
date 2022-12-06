package itismeucci.chat.lib;
import com.fasterxml.jackson.databind.*;

public final class Utils
{
	public static final ObjectMapper mapper = new ObjectMapper();

	public static <T extends Enum<T>> T getEnumConst(Class<T> enumClass, String name)
	{
		try
		{
			return Enum.valueOf(enumClass, name);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	private Utils() { }
}
