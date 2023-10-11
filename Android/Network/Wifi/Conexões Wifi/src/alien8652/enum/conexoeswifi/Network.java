package alien8652.enum.conexoeswifi;
public class Network{
	String SSID="",
		   PSK="",
		   PASSWORD="",
		   WEP_KEY="",
		   WEP_KEY0="",
		   WEP_KEY1="",
		   WEP_KEY2="",
		   WEP_KEY3="",
		   PROTO="",
		   KEY_MGMT="",
		   PAIRWISE="",
		   AUTH_ALG="",
		   PRIORITY="",
		   SIM_SLOT="",
		   IMSI="",
		   GROUP="",
		   IDENTITY="",
		   ANONYMOUS_IDENTITY="",
		   PHASE2="",
		   EAP="";
	public Network(){}
	public void setSSID(String s){SSID=s;}
	public void setPSK(String p){PSK=p;}
	public void setPassword(String s){PASSWORD=s;}
	public void setWEPKey(String s){WEP_KEY=s;}
	public void setWEPKey0(String s){WEP_KEY0=s;}
	public void setWEPKey1(String s){WEP_KEY1=s;}
	public void setWEPKey2(String s){WEP_KEY2=s;}
	public void setWEPKey3(String s){WEP_KEY3=s;}
	public void setIdentity(String s){IDENTITY=s;}
	public void setEAP(String s){EAP=s;}
	public void setProto(String s){PROTO=s;}
	public void setKeyMGMT(String s){KEY_MGMT=s;}
	public void setPairWise(String p){PAIRWISE=p;}
	public void setAuthAlg(String s){AUTH_ALG=s;}
	public void setPriority(String s){PRIORITY=s;}
	public void setSIMSlot(String s){SIM_SLOT=s;}
	public void setIMSI(String s){IMSI=s;}
	public void setGroup(String s){GROUP=s;}
	public void setPhase2(String s){PHASE2=s;}
	public void setAnonymousIdentity(String s){ANONYMOUS_IDENTITY=s;}
    public String getSSID(){return SSID;}
	public String getPSK(){return PSK;}
	public String getPassword(){return PASSWORD;}
	public String getWEPKey(){return WEP_KEY;}
	public String getWEPKey0(){return WEP_KEY0;}
	public String getWEPKey1(){return WEP_KEY1;}
	public String getWEPKey2(){return WEP_KEY2;}
	public String getWEPKey3(){return WEP_KEY3;}
	public String getIdentity(){return IDENTITY;}
	public String getEAP(){return EAP;}
	public String getProto(){return PROTO;}
    public String getKeyMGMT(){return KEY_MGMT;}
	public String getPairWise(){return PAIRWISE;}
	public String getAuthAlg(){return AUTH_ALG;}
    public String getPriority(){return PRIORITY;}
	public String getSIMSlot(){return SIM_SLOT;}
	public String getIMSI(){return IMSI;}
	public String getGroup(){return GROUP;}
	public String getPhase2(){return PHASE2;}
	public String getAnonymousIdentity(){return ANONYMOUS_IDENTITY;}
}
