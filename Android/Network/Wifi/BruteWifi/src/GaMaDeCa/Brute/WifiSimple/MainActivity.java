package GaMaDeCa.Brute.WifiSimple;
 
import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.MenuItem;
import android.view.Menu;
import android.os.Handler;
import android.text.Spanned;
import android.text.Html;
import java.util.Collections;
import java.util.Comparator;
import android.net.wifi.WifiNetworkSuggestion;
import android.net.wifi.WifiConfiguration;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.Gravity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import android.widget.TextView;

// This probably is not memory safe or whatever
public class MainActivity extends Activity {
    private ListView wifiListView;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<Spanned> wifiArray=new ArrayList<Spanned>();
    private ArrayList<String> wifiSSIDs=new ArrayList<String>();
    private WifiManager wifiMan;
    private List<ScanResult> wifiList;
    
    private String getStringRaw(int id){
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = getResources().openRawResource(id);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
        }
        return sb.toString();
    }
    private String WordList="";
    private void setWordList(String newWordList){
        WordList=newWordList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setWordList(getStringRaw(R.raw.another_random_wordlist));
        wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
       
        wifiListView = findViewById(R.id.mainListView);
        listAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wifiArray);

        wifiListView.setAdapter(listAdapter);
        wifiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Context ctx=view.getContext();
                    final Spanned full_network=wifiArray.get(position);
                    final String network = (String) wifiSSIDs.get(position);
                    AlertDialog.Builder choiceDialog=new AlertDialog.Builder(ctx);
                    choiceDialog.setTitle(network);
                    choiceDialog.setMessage(getString(R.string.choose));
                    choiceDialog.setPositiveButton(getString(R.string.brute_attack),new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface di,int p){
                            AlertDialog.Builder bruteDialog=new AlertDialog.Builder(ctx);
                            bruteDialog.setTitle(network);
                            bruteDialog.setMessage("Brute force "+network+"?");
                            bruteDialog.setPositiveButton(getString(R.string.yes),new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface di, int n){
                                    bruteForce(network);
                                }
                            });
                            bruteDialog.setNegativeButton(getString(R.string.no),null);
                            bruteDialog.show();
                        }
                    });
                    choiceDialog.setNeutralButton(getString(R.string.show_info),new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface di,int p){
                                AlertDialog.Builder infoDialog=new AlertDialog.Builder(ctx);
                                infoDialog.setTitle(network);
                                TextView tvShow=new TextView(ctx);
                                tvShow.setText(full_network);
                                tvShow.setTextIsSelectable(true);
                                infoDialog.setView(tvShow);
                                infoDialog.setPositiveButton(getString(R.string.ok),null);
                                infoDialog.show();
                            }
                        });
                    choiceDialog.setNegativeButton(getString(R.string.cancel),null);
                    choiceDialog.show();
                }
            });
    }
    // Sadly it will not work for now, but hopefully maybe someday with root and some C++ code
    private void bruteForce(String W_SSID){
        String[] passwords=WordList.split("\n");
        for (String pass: passwords) {
            if (connectWifi(W_SSID, pass)){
                AlertDialog.Builder adb=new AlertDialog.Builder(this);
                adb.setTitle(getString(R.string.password_found));
                TextView tv=new TextView(this);
                tv.setText(W_SSID+"\n"+(getString(R.string.password).replace("%PSW",pass)));
                tv.setTextIsSelectable(true);
                adb.setView(tv);
                adb.setPositiveButton(getString(R.string.ok),null);
                adb.show();
                break;
            }
        }
    }
    //-------------[ CONNECT WIFI ]
    //Ok, checking valid credentials when connecting with wifi looks like its officially broken now, thanks!
    private boolean connectWifi(String W_SSID, String W_PASS){
        //THIS DONT WORK!
        List<WifiNetworkSuggestion> suggestions = new ArrayList<>();
        suggestions.add(
            new WifiNetworkSuggestion.Builder()
                .setSsid(W_SSID)
                .setWpa2Passphrase(W_PASS)
                .build()
        );
        int status = wifiMan.addNetworkSuggestions(suggestions);
        if (status == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
            Toast.makeText(this,getString(R.string.password_found),Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
        /*
        // NEITHER THIS!
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", W_SSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", W_PASS);
        
        // For WPA/WPA2 networks
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

        int netId = wifiMan.addNetwork(wifiConfig);
        wifiMan.disconnect();
        wifiMan.enableNetwork(netId, true);
        return wifiMan.reconnect();*/
    }
   
    
    //---------[ SCAN WIFI ]
    private boolean wifiDelay=false;
    private long delay=5000;
    private Handler wifiRescanDelay=new Handler();
    private Runnable renable=new Runnable(){
        @Override
        public void run() {
            wifiDelay=false;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan_networks:
                scanWifiNetworks();
                break;
            case R.id.change_wordlist:
                AlertDialog.Builder adb=new AlertDialog.Builder(this);
                adb.setTitle(getString(R.string.update_wordlist));
                final EditText input=new EditText(this);
                input.setText(WordList);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                input.setSingleLine(false);
                input.setMaxLines(100);
                input.setMinLines(15);
                input.setGravity(Gravity.LEFT | Gravity.TOP);
                //input.setScrollbars(View.SCROLLBARS_VERTICAL);
                adb.setView(input);
                adb.setPositiveButton(getString(R.string.update),new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface di, int n){
                            setWordList(input.getText().toString());
                        }
                    });
                adb.setNegativeButton(getString(R.string.cancel),null);
                adb.show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        scanWifiNetworks();
        
        if (WordList=="")
            setWordList(getStringRaw(R.raw.another_random_wordlist));
        super.onResume();
    }
    private void scanWifiNetworks() {
        if (wifiMan==null)
            return;
        if (wifiDelay)
            return;
        
        if (!wifiMan.isWifiEnabled()){
            //wifiMan.setWifiEnabled(true); //Nah
            Toast.makeText(this,getString(R.string.turn_wifi_on),Toast.LENGTH_LONG).show();
            return;
        }
        wifiDelay=true;
        registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiMan.startScan();
      
        wifiRescanDelay.postDelayed(renable,delay);
    }
    private final String orange="#EE9700";
    private String fontColor(String text,String color){
        return "<font color=\""+color+"\">"+text+"</font>";
    }
    public class SignalStrengthComparator implements Comparator<ScanResult> {
        @Override
        public int compare(ScanResult netA, ScanResult netB){
            int sA = netA.level, sB = netB.level;
            if (sA > sB)
                return -1;
            else if (sA < sB)
                return 1;
            else
                return 0;
        }
    }
    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                wifiList = wifiMan.getScanResults();
                wifiArray.clear();
                wifiSSIDs.clear();
                if (wifiList != null) {
                    Collections.sort(wifiList, new SignalStrengthComparator());
                    for (ScanResult scanResult : wifiList) {
                        String SSID = scanResult.SSID;
                        wifiSSIDs.add(SSID);
                        String CHANNEL_WIDTH="Unknown";
                        switch (scanResult.channelWidth){
                            case scanResult.CHANNEL_WIDTH_20MHZ:
                                CHANNEL_WIDTH="20MHZ";
                                break;
                            case scanResult.CHANNEL_WIDTH_40MHZ:
                                CHANNEL_WIDTH="40MHZ";
                                break;
                            case scanResult.CHANNEL_WIDTH_80MHZ:
                                CHANNEL_WIDTH="80MHZ";
                                break;
                            case scanResult.CHANNEL_WIDTH_80MHZ_PLUS_MHZ:
                                CHANNEL_WIDTH="80MHZ+";
                                break;
                            case scanResult.CHANNEL_WIDTH_160MHZ:
                                CHANNEL_WIDTH="160MHZ";
                                break;
                            default:
                                break;
                        }
                       
                        wifiArray.add(Html.fromHtml(
                            ("\n\n"+
                            "\nSSID(Network Name):\n>---[+] "+fontColor(SSID,"green")+
                            (SSID.equals("")?fontColor("\n>---[!] Invisible Network Detected!","#FF0000"):"")+
                            "\nBSSID(MAC Address):\n>---[+] "+fontColor(scanResult.BSSID,"green")+
                            "\nLevel:\n>---[+] "+fontColor(""+scanResult.level,"green")+
                            "\nFrequency:\n>---[+] "+fontColor(""+scanResult.frequency,"green")+
                            "\nTime Stamp:\n>---[+] "+fontColor(""+scanResult.timestamp,"green")+
                            (scanResult.describeContents()==0?"":"\nContents Described:\n>---[?] "+fontColor(""+scanResult.describeContents(),orange))+
                            (scanResult.is80211mcResponder()?fontColor("\n{802.11 mc Responder}\n",orange):"")+
                            (scanResult.isPasspointNetwork()?fontColor("\n{Passpoint Network}\n",orange):"")+
                            (!scanResult.venueName.equals("")?"\nVenue Name:\n>---[?] "+fontColor(""+scanResult.venueName,orange):"")+
                            (!scanResult.operatorFriendlyName.equals("")?"\nOperator Friendly Name:\n>---[?] "+fontColor(""+scanResult.operatorFriendlyName,orange):"")+
                            "\nCenter Frequency 0 | 1:\n>---[+] "+fontColor(""+scanResult.centerFreq0+" | "+scanResult.centerFreq1,"green")+
                            "\nChannel Width:\n>---[+] "+fontColor(CHANNEL_WIDTH+"("+scanResult.channelWidth+")","green")+
                            /*
                            idk what is this
                            "\nCONTENTS FILE DESCRIPTOR:\n"+scanResult.CONTENTS_FILE_DESCRIPTOR+
                            "\nPARCEABLE WRITE RETURN VALUE:\n"+scanResult.PARCELABLE_WRITE_RETURN_VALUE+
                            */
                                          "\nCapabilities:\n"+fontColor(scanResult.capabilities,"green")+
                            "\n\n").replaceAll("\n","<br>"))
                        );
                    }
                   listAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_wifi), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
                                                }
