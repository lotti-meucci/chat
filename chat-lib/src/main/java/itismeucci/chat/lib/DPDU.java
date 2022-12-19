package itismeucci.chat.lib;
import java.nio.*;

/** Classe che rappresenta un DPDU di JCSP.*/
public final class DPDU
{
	/** Campo J. */
	private final Schema j;

	/** Campo R. */
	private final byte[] r;

	/**
	 * Crea un'istanza con il relativo campo J e R.
	 * I campi N e M vengono dedotti dai due parametri.
	 * @param j Campo J in forma di schema.
	 * @param r Campo R in forma di array di byte.
	 */
	public DPDU(Schema j, byte[] r)
	{
		this.j = j;
		this.r = r.clone();
	}

	/**
	 * Getter del campo N.
	 * @return Campo N.
	 */
	public long getN()
	{
		return Schema.toJson(j).getBytes().length;
	}

	/**
	 * Getter del campo J.
	 * @return Campo J.
	 */
	public Schema getJ()
	{
		return j;
	}

	/**
	 * Getter del campo M.
	 * @return Campo M.
	 */
	public long getM()
	{
		return r.length;
	}

	/**
	 * Getter del campo R.
	 * @return Campo R.
	 */
	public byte[] getR()
	{
		return r.clone();
	}

	/**
	 * Getter di un array di byte spedibile via socket che rappresenta il DPDU.
	 * @return Byte del DPDU.
	 */
	public byte[] getBytes()
	{
		var j = Schema.toJson(this.j).getBytes();
		var buffer = ByteBuffer.allocate((int)(16 + getN() + getM()));
		buffer.putLong(j.length);
		buffer.put(j);
		buffer.putLong(r.length);
		buffer.put(r);
		return buffer.array();
	}
}
