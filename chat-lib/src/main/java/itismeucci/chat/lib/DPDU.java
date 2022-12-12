package itismeucci.chat.lib;
import java.nio.*;

public final class DPDU
{
	private final byte[] j;
	private final byte[] r;

	public DPDU(Schema j, byte[] r)
	{
		this.j = Schema.toJson(j).getBytes();
		this.r = r.clone();
	}

	public long getN()
	{
		return j.length;
	}

	public String getJ()
	{
		return new String(j.clone());
	}

	public long getM()
	{
		return r.length;
	}

	public byte[] getR()
	{
		return r.clone();
	}

	public byte[] getBytes()
	{
		var buffer = ByteBuffer.allocate((int)(16 + getN() + getM()));
		buffer.putLong(j.length);
		buffer.put(j);
		buffer.putLong(r.length);
		buffer.put(r);
		return buffer.array();
	}
}
