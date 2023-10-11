package alien8652.enum.conexoeswifi;
import java.util.*;
public class WifiConfParser{
	public WifiConfParser(){}
	public ArrayList<Network> parse(String data) throws Exception{
		ArrayList<Network> networks=new ArrayList<Network>();
		String[] nList=data.split("network=[{]");
		for(int i=1;i<nList.length;i++){
			Network net=parseNetworkInfo(nList[i]);
			if(!net.getSSID().equals("")||!net.getPSK().equals(""))networks.add(net);
		}
		return networks;
	}
	public Network parseNetworkInfo(String info){
		Network net=new Network();
		net.setSSID(cutInfo(info,"ssid=\"","\""));
		net.setPSK(cutInfo(info,"psk=\"","\""));
		net.setPassword(cutInfo(info,"password=","\n"));
		net.setWEPKey(cutInfo(info,"wep_key=","\n"));
		net.setWEPKey0(cutInfo(info,"wep_key0=","\n"));
		net.setWEPKey1(cutInfo(info,"wep_key1=","\n"));
		net.setWEPKey2(cutInfo(info,"wep_key2=","\n"));
		net.setWEPKey3(cutInfo(info,"wep_key3=","\n"));
		net.setIdentity(cutInfo(info,"identity=","\n"));
		net.setEAP(cutInfo(info,"eap=","\n"));
		net.setProto(cutInfo(info,"proto=","\n"));
		net.setKeyMGMT(cutInfo(info,"key_mgmt=","\n"));
		net.setPairWise(cutInfo(info,"pairwise=","\n"));
		net.setAuthAlg(cutInfo(info,"auth_alg=","\n"));
		net.setPriority(cutInfo(info,"priority=","\n"));
		net.setSIMSlot(cutInfo(info,"sim_slot=\"","\""));
		net.setIMSI(cutInfo(info,"imsi=\"","\""));
		net.setGroup(cutInfo(info,"group=","\n"));
		net.setAnonymousIdentity(cutInfo(info,"anonymous_identity=","\n"));
		net.setPhase2(cutInfo(info,"phase2=","\n"));
		return net;
	}
	public String cutInfo(String info,String start,String end){
		int s=info.indexOf(start);
		if(s==-1)return"";
		s+=start.length();
		int e=info.substring(s).indexOf(end);
		if(e==-1)return"";
		return info.substring(s,e+s);
	}
}
