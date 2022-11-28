package itismeucci.chat.lib;
import java.util.Map;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

public abstract class Schema
{
	private static final Map<String,Object> bindings = Map.of(
		"join", JoinSchema.class
	);

	public static <T extends Schema> T getSchema(String json, Class<T> schemaClass) throws SchemaException
	{
		try
		{
			var node = Utils.mapper.readTree(json);
			var name = node.get("schema").asText();

			if (bindings.containsKey(name) && bindings.get(name).equals(schemaClass))
			{
				return Utils.mapper.readValue(json, schemaClass);
			}
			else
			{
				throw new Exception();
			}
		}
		catch (Exception e)
		{
			throw new SchemaException("Invalid schema.");
		}
	}
}
