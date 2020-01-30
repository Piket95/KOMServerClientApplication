import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Scanner;

public class Main {
	
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		
		System.out.println("Welche Rolle übernimmt dieser PC? (Bitte Nummer eingeben!)");
		System.out.println("1: Client");
		System.out.println("2: Server");
		System.out.println("3: Test");
		
		int role = Integer.parseInt(sc.nextLine());
		
		switch (role) {
		case 1:
			startClient();
			break;
		case 2:
			startServer();
			break;
		case 3:
			test();
			break;
		default:
			break;
		}
		
		sc.close();
		
	}
	
	private static void startClient() throws IOException, ParseException {
		String transport = "udp";
		
		System.out.println("Auf welchen Server möchtest du dich verbinden? (IP/Hostname)");
		String hostname = sc.nextLine();
		System.out.println("Welcher Port soll genutzt werden?");
		int cPort = Integer.parseInt(sc.nextLine());
		System.out.println("Soll TCP oder UDP verwendet werden?");
		System.out.println("1: TCP");
		System.out.println("2: UDP");
		
		if (Integer.parseInt(sc.nextLine()) == 1) {
			transport = "tcp";
		}
		
		Client client = new Client(hostname, cPort, transport);
		client.request();
	}
	
	private static void startServer() throws SocketException {		
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		NetworkInterface sNetInt;
		ArrayList<NetworkInterface> activeInterfaces = new ArrayList<>();
		
		System.out.println("Auf welchem Port, soll der Server hören?");
		int sPort = Integer.parseInt(sc.nextLine());
		
		System.out.println("Welche Schnittstelle soll verwendet werden?");
		int i = 1;
		for (NetworkInterface netInt : Collections.list(nets)) {
			if (netInt.isUp()) {
				activeInterfaces.add(netInt);
				System.out.println(i + ": " + netInt.getDisplayName() + " (" + netInt.getName() + ")");
				i++;
			}
		}
		
		int schnittstelle = Integer.parseInt(sc.nextLine());
		sNetInt = activeInterfaces.get(schnittstelle - 1);
		System.out.println(sNetInt.getName());

		//Start server
		Server server = new Server(sPort, sNetInt);
		System.out.println("[Server] Server wird gestartet...");
		server.startListening();
		
		sc.close();
	}
	
	public static void test() throws SocketException {
		
		
		
	}

}
