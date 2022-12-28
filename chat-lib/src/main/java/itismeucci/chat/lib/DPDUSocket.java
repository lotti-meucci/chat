package itismeucci.chat.lib;
import java.io.*;
import java.net.*;
import com.google.common.primitives.*;

/** Rappresenta un canale di trasmissione per DPDU. */
public class DPDUSocket
{
	/** Socket TCP di base per i trasferimenti. */
	private final Socket socket;

	/** Stream di input del socket. */
	private final InputStream input;

	/** Stream di output del socket. */
	private final OutputStream output;

	/**
	 * Crea un'istanza col relativo socket TCP.
	 * @param socket Il socket TCP.
	 */
	public DPDUSocket(Socket socket)
	{
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

	/**
	 * Getter del socket TCP.
	 * @return Il socket TCP.
	 */
	public final Socket getSocket()
	{
		return socket;
	}

	/**
	 * Indica se il canale di trasmissione è chiuso.
	 * @return Vero se il canale di trasmissione è chiuso, altrimenti falso se aperto.
	 */
	public final boolean isClosed()
	{
		return getSocket().isClosed();
	}

	/**
	 * Trasferisce un DPDU sul canale di trasmissione.
	 * @param dpdu DPDU da trasferire.
	 * @throws IOException Errore di comunicazione durante il trasferimento.
	 */
	public final void transfer(DPDU dpdu) throws IOException
	{
		synchronized (output)
		{
			output.write(dpdu.getBytes());
		}
	}

	/**
	 * Riceve un DPDU in input dal canale di trasmissione.
	 * @return DPDU ottenuta.
	 * @throws ValidationException DPDU non valido.
	 * @throws IOException Errore durante la lettura dal canale di trasmissione.
	 */
	public final DPDU receive() throws ValidationException, IOException
	{
		synchronized (input)
		{
			long n = Longs.fromByteArray(input.readNBytes(8));
			var j = Schema.fromJson(new String(input.readNBytes((int)n)));
			var m = Longs.fromByteArray(input.readNBytes(8));
			var r = input.readNBytes((int)m);
			return new DPDU(j, r);
		}
	}

	/** Chiude il canale di trasmissione. */
	public final void close()
	{
		try
		{
			getSocket().close();
		}
		catch (Exception e) { }
	}
}
