package GaMaDeCa.hacktools.portscan;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.IOException;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import java.net.InetAddress;
import java.net.UnknownHostException;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.text.SpannableStringBuilder;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import android.widget.TextView.*;
import java.util.Random;
import android.widget.CheckBox;
import android.widget.Toast;
import android.text.InputFilter;
import android.text.Spanned;

//TODO:
// Fix TextView to show up to 5000 lines(Cut each ~500 lines)
// Fix stop action(randomly crashes when pressed sucessively or when the scan is started again)
// Add ports descriptions
public class MainActivity extends Activity
{
	ProgressBar progress_scan;
	EditText spet,epet,tout; // Start Port EditText, End Port EditText, Timeout
	TextView scanning,scan_results;
	
	PortScan PortScanner=null;
	SpannableStringBuilder port_array=null;
	String ip;
	int start_port=0,end_port=65535,timeout=20;
	boolean open=true,closed=false;
	InputFilter portFilter=new InputFilter(){
		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			try {
				String text=dest.toString()+source.toString();
				int port = Integer.parseInt(text);
				if (port>65535||(text.startsWith("0")&&!text.equals("0"))) {
					return "";
				} else {
					return source;
				}
			} catch (NumberFormatException nfe) {}
			return source;
		}
	};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		progress_scan=findViewById(R.id.scanProgressBar);
		scanning=findViewById(R.id.resultsTextView);
		scan_results=findViewById(R.id.scan_resultsTextView);
		spet=findViewById(R.id.start_portEditText);
		epet=findViewById(R.id.end_portEditText);
		tout=findViewById(R.id.timeoutEditText);
		spet.setFilters(new InputFilter[]{portFilter});
		epet.setFilters(new InputFilter[]{portFilter});
		
		logo();
		((Button)findViewById(R.id.start_scanButton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v1) {
				open=((CheckBox)findViewById(R.id.show_openCheckBox)).isChecked();
				closed=((CheckBox)findViewById(R.id.show_closedCheckBox)).isChecked();
				ip=((EditText)findViewById(R.id.ipaddrEditText)).getText().toString();
				if (!ip.equals("")) {
					progress_scan.setVisibility(View.VISIBLE);
					progress_scan.setProgress(0);
					progress_scan.setMax(100);
					PortScanner=new PortScan();
					PortScanner.execute(ip,
									    spet.getText().toString(),
									    epet.getText().toString(),
									    tout.getText().toString());
				}
			}
		});
		((Button)findViewById(R.id.stopButton)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (PortScanner!=null) {
					if (!PortScanner.isCancelled()) {
						PortScanner.cancel(true);
						setColoredText(scan_results,port_array);
						port_array=null;
						progress_scan.setVisibility(View.GONE);
						PortScanner=null;
						Toast.makeText(getApplicationContext(),getString(R.string.scan_stopped),Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
    }
	
	public String getRandomHexColor() {
		String hex="ABCDEF0123456789",random_color="#";
		for (int i=0;i<6;i++)
			random_color+=hex.charAt(new Random().nextInt(hex.length()));
		return random_color;
	}
	public void logo() {
		String text_logo="PortScan - Alien8652";
		SpannableStringBuilder ssb_logo=new SpannableStringBuilder();
		for (int i=0;i<text_logo.length();i++)
			ssb_logo.append(MulticoloredText(""+text_logo.charAt(i),Color.parseColor(getRandomHexColor())));
		setColoredText(((TextView)findViewById(R.id.logo_tv)),ssb_logo);
	}
	public SpannableString MulticoloredText(String text,int color) {
		SpannableString colored_string=new SpannableString(text);
		colored_string.setSpan(new ForegroundColorSpan(color),0,text.length(),0);
		return colored_string;
	}
	public TextView setColoredText(TextView tv,SpannableStringBuilder ssb) {
		tv.setText(ssb,BufferType.SPANNABLE);
		return tv;
	}
	
	public class PortScan extends AsyncTask<String,Integer,SpannableStringBuilder>
	{
		@Override
		protected SpannableStringBuilder doInBackground(String[] strings)
		{
			/*// Testing publishProgress
			int end=300;
			for (int i=0;i<end;i++) {
				try {
					Thread.sleep(1);
					publishProgress((int) ((((float)i)/end)*100),i);
				} catch(InterruptedException ie) {}
			}*/
			ip=strings[0];
			start_port=Integer.parseInt(strings[1]);
			end_port=Integer.parseInt(strings[2])+1;
			timeout=Integer.parseInt(strings[3]);
			if (start_port>end_port) {//swap
				start_port+=end_port;
				end_port=start_port-end_port;
				start_port-=end_port;
			}
			port_array=new SpannableStringBuilder();
			int progress=0;
			boolean first_port_find=false;
			for (int current_port=start_port;current_port<end_port;current_port++) {
				try {
					Socket socket=new Socket();
					socket.connect(new InetSocketAddress(ip,current_port),timeout);
					socket.close();
					
					if (open) {
						if (!first_port_find) {
							first_port_find=true;
						} else {
							port_array.append(MulticoloredText("\n",Color.GREEN));
						}
						port_array.append(MulticoloredText("\t\t -> "+String.valueOf(current_port),Color.GREEN));
					}
				} catch (IOException io_error) {
					if (closed) {
						if (!first_port_find) {
							first_port_find=true;
						} else {
							port_array.append(MulticoloredText("\n",Color.RED));
						}
						port_array.append(MulticoloredText("\t\t -> "+String.valueOf(current_port),Color.RED));
					}
				}
				progress=(int)((((float)current_port)/end_port)*100);
				publishProgress(progress,current_port);
			}
			return port_array;
		}

		@Override
		protected void onProgressUpdate(Integer[] values)
		{
			progress_scan.setProgress(values[0]);
			scanning.setText(getString(R.string.scanning)+" "+values[1]+"...");
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(SpannableStringBuilder result)
		{
			setColoredText(scan_results,result);
			progress_scan.setVisibility(View.GONE);
			scanning.setText(getString(R.string.results_tv));
			Toast.makeText(getApplicationContext(),getString(R.string.scan_end),Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
	}
}
