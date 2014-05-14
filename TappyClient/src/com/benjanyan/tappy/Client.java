package com.benjanyan.tappy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import android.util.Log;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.SystemClock;


public class Client {
	private DatagramSocket clientSocket;
	private byte[] input;
	private byte[] output;
	private InetAddress serverIP;
	private int port;
	
	private int bufferLength;
		
	private boolean[] keyPressed;
	private long[] keyLastTap;
	private int lastKey;
	private int cooldown;
	
	Client(InetAddress serverIP, int port) {
		this.serverIP = serverIP;
		this.port = port;
		bufferLength = 2;
		lastKey = -1;
		cooldown = 200;
		keyPressed = new boolean[2];
		keyPressed[0] = false;
		keyPressed[1] = false;
		keyLastTap = new long[2];
		keyLastTap[0] = System.currentTimeMillis();
		keyLastTap[1] = System.currentTimeMillis();
		
		try {
			clientSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.out.println("Failed to listen on port " + port);
			e.printStackTrace();
		}
		
		input = new byte[bufferLength];
		output = new byte[bufferLength];
	}
	
	protected void pressKey() {
		if (keyPressed[1] && !keyPressed[0] && !keyInCooldown(0)) {
			pressKey1();
		} else if (keyPressed[0] && !keyPressed[1] && !keyInCooldown(1)) {
			pressKey2();
		} else if (!keyPressed[0] && !keyPressed[1] && !keyInCooldown(0)) {
			pressKey1();
		} else if (!keyPressed[0] && !keyPressed[1] && !keyInCooldown(1)) {
			pressKey2();
		} else {
			if (lastKey == 2) {
				releaseKey1();
				pressKey1();
			} else {
				releaseKey2();
				pressKey2();
			}

			Log.e("Tapper-Client", "Something went wrong; Both keys pressed or in cooldown for this tap!");
			Log.d("Tapper-Client", "Key1: " + keyPressed[0] + " " + keyInCooldown(0));
			Log.d("Tapper-Client", "Key2: " + keyPressed[1] + " " + keyInCooldown(1));
		}

	}
	
	protected void releaseKey() {
		if (lastKey == 1 && keyPressed[0]) {
			releaseKey1();
		} else if (lastKey == 2 && keyPressed[1]) {
			releaseKey2();
		} else {
			releaseKey2();
			releaseKey1();
			//Log.e("Tapper-Client", "Something went wrong; attempted to release unpressed keys.");
		}
	}
	
	private void pressKey2() {
		keyPressed[1] = true;
		byte[] output = ("zp").getBytes();
		sendKey(output);
		lastKey = 2;
		keyLastTap[1] = System.currentTimeMillis();
		Log.d("Tapper-Client", "Key2 pressed");
	}
	
	private void releaseKey2() {
		keyPressed[1] = false;
		byte[] output = ("zr").getBytes();
		sendKey(output);
		Log.d("Tapper-Client", "Key2 released");
		
	}
	
	private void pressKey1() {
		keyPressed[0] = true;
		byte[] output = ("xp").getBytes();
		sendKey(output);
		lastKey = 1;
		keyLastTap[0] = System.currentTimeMillis();
		Log.d("Tapper-Client", "Key1 pressed");
	}
	
	private void releaseKey1() {
		keyPressed[0] = false;
		byte[] output = ("xr").getBytes();
		sendKey(output);
		Log.d("Tapper-Client", "Key1 released");
		
	}
	
	private void sendKey(byte[] output) {
		DatagramPacket send = new DatagramPacket(output, bufferLength, serverIP, port);
		try {
			clientSocket.send(send);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private long keyCooldown(int id) {
		return System.currentTimeMillis() - keyLastTap[id];
	}
	
	private boolean keyInCooldown(int id) {
		Log.d("Tapper-Client", "Cooldown for " + id + ": " + (System.currentTimeMillis() - keyLastTap[id]));
		return keyCooldown(id) < cooldown;
	}
	
	protected void close() {
		clientSocket.close();
	}
	
}