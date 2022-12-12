package itismeucci.chat.lib;

import java.util.Arrays;

public class ListUpdateSchema extends Schema implements UsernamesContainer
{
	private String[] usernames;

	private ListUpdateSchema()
	{
		super("list-update");
	}

	public ListUpdateSchema(Iterable<String> usernames)
	{
		this();

		if (usernames == null)
			throw new SchemaException();

		// ...
	}

	@Override
	public Iterable<String> getUsernames()
	{
		return Arrays.asList(usernames.clone());
	}

	@Override
	public ListUpdateSchema cloneSchema()
	{
		return null;
	}
}
