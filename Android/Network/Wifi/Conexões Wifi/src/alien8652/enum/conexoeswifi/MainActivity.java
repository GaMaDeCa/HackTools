package alien8652.enum.conexoeswifi;

import android.app.*;
import android.os.Bundle;
import android.os.Build;
import android.os.AsyncTask;
import java.io.*;
import java.util.*;
import android.widget.*;
import android.view.*;
import android.view.inputmethod.*;

public class MainActivity extends Activity {
	EditText pesquisaET;
	TextView tvRes;
	String[] caminhos={"/data/wifi/bcm_supp.conf",
					   "/data/misc/wifi/wpa_supplicant.conf",
					   "/data/misc/wifi/wpa.conf"};
	String su="su -c ",cat="cat ";
    @Override protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		tvRes=findViewById(R.id.resultTV);
		pesquisaET=findViewById(R.id.termoPesquisaET);
		((Button)findViewById(R.id.carregarBtn)).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v){
				setResultado(getSenhasWifi());
			}
		});
		pesquisaBT=findViewById(R.id.pesquisarBtn);
		pesquisaBT.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v){
				esconderTeclado();
				pesquisaBT.setEnabled(false);
				new android.os.Handler().postDelayed(new Runnable(){
					public void run(){
						pesquisar();
					}
				},3000);
			}
		});
    }
	Button pesquisaBT;
	public void pesquisar(){
		if(resultadoSetado){
			String termo=pesquisaET.getText().toString(),
				dados=tvRes.getText().toString();
			if(dados!=""){
				int linha=-1;
				String[]linhas=dados.split("\n");
				for(int i=0;i<linhas.length;i++){
					if(linhas[i].contains(termo)){
						linha=i;
						break;
					}
				}
				if(linha!=-1){
					textViewScroll(linha);
				}
			}
		}
		pesquisaBT.setEnabled(true);
	}
	public void esconderTeclado(){
		try{
			InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
		}catch(Exception e){}
	}
	public void textViewScroll(int linha){
		int y=tvRes.getLayout().getLineTop(linha);
		tvRes.scrollTo(0,y);
	}
	boolean resultadoSetado=false;
	public void setResultado(String r){
		resultadoSetado=true;
		tvRes.setText(r);
	}
	public String getSenhasWifi() {
		WifiConfParser parser=new WifiConfParser();
		String info=getWPAInfo();
		ArrayList<Network> nets=null;
		try{
			nets=parser.parse(info);
		}catch(Exception e){
			info=e.toString();
		}
		info="";
		for(Network net:nets){
			info+="\n[+++REDE+++]";
			String ssid=net.getSSID(),
				   psk=net.getPSK(),
				   authAlg=net.getAuthAlg(),
				   pairwise=net.getPairWise(),
				   priority=net.getPriority(),
				   proto=net.getProto(),
				   keyMGMT=net.getKeyMGMT(),
				   simSlot=net.getSIMSlot(),
				   imsi=net.getIMSI(),
				   group=net.getGroup();
			if(!ssid.equals(""))info+="\nNome(SSID):"+ssid;
			if(!psk.equals(""))info+="\nSenha:"+psk;
			if(!authAlg.equals(""))info+="\nAuth Alg:"+authAlg;
			if(!pairwise.equals(""))info+="\nPairWise:"+pairwise;
			if(!priority.equals(""))info+="\nPriority:"+priority;
			if(!proto.equals(""))info+="\nProto:"+proto;
			if(!keyMGMT.equals(""))info+="\nKey MGMT:"+keyMGMT;
			if(!simSlot.equals(""))info+="\nSIM Slot:"+simSlot;
			if(!imsi.equals(""))info+="\nIMSI:"+imsi;
			if(!group.equals(""))info+="\nGroup:"+group;
			info+="\n---REDE---";
		}
		return info;
	}
	public String getWPAInfo(){
		String retorno="";
		for (String caminho:caminhos) {
			retorno=executarComando(cat+caminho);
			if(!retorno.equals(""))return retorno;
		}
		return retorno;
	}
	public String executarComando(String comando) {
		String resposta="";
		try {
			Process executor=Runtime.getRuntime().exec(su+comando);
			BufferedReader bfr=new BufferedReader(new InputStreamReader(executor.getInputStream()));
			//BufferedWriter bfw=new BufferedWriter(new OutputStreamWriter(executor.getOutputStream()));
			//bfw.write(comando);
			String lendo=bfr.readLine();
			while (lendo!=null) {
				resposta+=lendo+"\n";
				lendo=bfr.readLine();
			}
			bfr.close();
			//bfw.close();
		} catch (IOException ioe){resposta=ioe.getMessage();}
		return resposta;
	}
}
