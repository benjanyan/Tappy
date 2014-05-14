package benjanyan.tappy;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class Server {
	private DatagramSocket serverSocket;
	private byte[] input;
	private byte[] output;
	
	private int bufferLength;
	private Robot robot;
	
	Server(int port) {
		bufferLength = 2;
		
		try {
			serverSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.out.println("Failed to listen on port " + port);
			e.printStackTrace();
		}
		
		input = new byte[bufferLength];
		output = new byte[bufferLength];
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void next() {
		DatagramPacket receive = new DatagramPacket(input, bufferLength);
		String key = null;
		try {
			serverSocket.receive(receive);
			key = new String(receive.getData());
			
			if (key.equals("zp")) {
				robot.keyPress(KeyEvent.VK_Q);
			} else if (key.equals("xp")) {
				robot.keyPress(KeyEvent.VK_W);
			} else if (key.equals("zr")) {
				robot.keyRelease(KeyEvent.VK_Q);
			} else {
				robot.keyRelease(KeyEvent.VK_W);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void close() {
		serverSocket.close();
	}
}
