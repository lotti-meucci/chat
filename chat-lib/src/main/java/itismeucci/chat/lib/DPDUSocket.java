package itismeucci.chat.lib;
import java.io.*;
import java.net.*;
import com.google.common.primitives.*;

public class DPDUSocket
{
	private final Socket socket;
	private final InputStream input;
	private final OutputStream output;

	public DPDUSocket(Socket socket)
	{
		if (socket == null)
			throw new IllegalArgumentException("'socket' was null.");

		try
		{
			this.input = socket.getInputStream();
			this.output = socket.getOutputStream();
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException("'socket' was invalid.");
		}

		this.socket = socket;
	}

	public final Socket getSocket()
	{
		return socket;
	}

	public final boolean isClosed()
	{
		return getSocket().isClosed();
	}

	public final void transfer(DPDU dpdu) throws IOException
	{
		synchronized (output)
		{
			output.write(dpdu.getBytes());
		}
	}

	public final DPDU receive() throws ValidationException, IOException
	{
		synchronized (input)
		{
			var n = Longs.fromByteArray(input.readNBytes(8));
			var j = Schema.fromJson(new String(input.readNBytes((int)n)));
			var m = Longs.fromByteArray(input.readNBytes(8));
			var r = input.readNBytes((int)m);
			return new DPDU(j, r);
		}
	}

	public final void close()
	{
		try
		{
			getSocket().close();
		}
		catch (Exception e) { }
	}
}
