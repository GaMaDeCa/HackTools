package alien8652.code.obfuscator;

public class PythonObfuscator
{
	public static String Obfuscar(String codigo,String codificacao) {
		//Na próxima atualização:
		//String str="exec(''.join(\"i\"+i for i in [\"\".join(chr(x) for x in [109,112,111,114,116,32,98,97,115,101,54,52,32,97,115,32,122,10]),\"\".join(chr(y) for y in [109,112,111,114,116,32,122,108,105,98,32,97,115,32,119,10])])+f'exec({chr(round(ord(\"b\")+5.25*5-2))}.{\"\".join(chr(round(x/2)) for x in bytes.fromhex(\"c46c68c8cac6dec8ca\"))}'+\"(\"+f'{\"\".join(chr(x-49) if x!=136 else chr(x-49).lower() for x in [ord(\":c?4z/xffx;w/xa8jkdecode\"[9])+4*4]+[95,149,150,148,160,158,161,163,150,164,164])}(bytearray("...;
		return "exec('import zlib,base64\\nexec(base64.b64decode(zlib.decompress(bytearray("+Utils.DecArray(Utils.comprimirZLIB(Utils.codificaB64(Utils.getStrBytes(codigo,codificacao))))+"))))')";
	}
}
