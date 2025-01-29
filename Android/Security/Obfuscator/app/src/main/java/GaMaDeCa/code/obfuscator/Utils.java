package GaMaDeCa.code.obfuscator;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

public class Utils
{
	public static byte[] getStrBytes(String string,String codificacao) {
		byte[] barr=null;
		try {
			barr=string.getBytes(codificacao);
		} catch (UnsupportedEncodingException uee) {
			barr=string.getBytes();
		}
		return barr;
	}
	public static String DecArray(byte[] entrada) {
		String decarr="[";
		int f=entrada.length;
		for (int i=0;i<f;i++) {
			decarr+=""+(entrada[i]&0xff)+(i+1<f?",":"]");
		}
		return decarr;
	}
	public static byte[] comprimirZLIB(byte[] entrada) {
		ByteArrayOutputStream bArrStream = new ByteArrayOutputStream(entrada.length);
        Deflater zlib = new Deflater();
        zlib.setLevel(Deflater.BEST_COMPRESSION);//Nível mais alto de compressão.
        zlib.setInput(entrada);
        zlib.finish();
		byte[] buffer = new byte[4096];
		while (!zlib.finished()) {
			int count = zlib.deflate(buffer);
			bArrStream.write(buffer, 0, count);
		}
		try {
			bArrStream.close();
		} catch (IOException ioe) {}
        return bArrStream.toByteArray();
    }
	public static byte[] codificaB64(byte[] entrada) {
		byte[] retorno=null;
		try {
			retorno=Base64.encode(entrada,Base64.DEFAULT);//encodeToString
		} catch(Exception e) {
			retorno=e.getMessage().getBytes();
		}
		return retorno;
	}
}
