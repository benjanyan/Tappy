package com.benjanyan.tappy;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class Tapper extends Activity {
	
	private Client client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tapper);
		
		InetAddress address = null;
		try {
			address = InetAddress.getByName("192.168.0.5");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client = new Client(address,7788);
		Button button = (Button) findViewById(R.id.button1);
		button.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getActionMasked() == (MotionEvent.ACTION_DOWN)) {
					new NetworkAction().execute("press");
				} else if (event.getActionMasked() == (MotionEvent.ACTION_POINTER_DOWN)) {
					new NetworkAction().execute("press");
				} else if (event.getActionMasked() == (MotionEvent.ACTION_POINTER_UP)) {
					new NetworkAction().execute("release");
				} else if (event.getActionMasked() == (MotionEvent.ACTION_UP)) {
					new NetworkAction().execute("release");
				}
				return false;
			}
		});
		
	}
	
	private class NetworkAction extends AsyncTask<String, Void, String>  {
		@Override
		protected String doInBackground(String... params) {
			if (params[0].equals("press")) {
				client.pressKey();
			} else {
				client.releaseKey();
			}
			return null;
		}
	}
	
}
