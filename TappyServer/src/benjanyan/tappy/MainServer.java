package benjanyan.tappy;

public class MainServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server(7788);
		
		for (int i = 0; i < 50000; ++i) {
			server.next();
		}

		server.close();
	}

}