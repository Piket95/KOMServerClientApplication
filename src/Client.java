import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.util.Scanner;

public class Client {
	
	private InetSocketAddress address;
	private String transport;
	
	public Client (String hostname, int port, String transport) {
		address = new InetSocketAddress(hostname, port);
		this.transport = transport;
	}
	
	public void request() throws IOException, ParseException {
		System.out.println("[Client] Verbindung wird aufgebaut...");
		
		if(transport.equals("udp")) {
			
			try (DatagramSocket ds = new DatagramSocket()){
				
				byte[] b = new byte[0];
				DatagramPacket dp = new DatagramPacket(b, b.length, address.getAddress(), address.getPort());
				ds.send(dp);
				
				byte[] b1 = new byte[1024];
				DatagramPacket dpReceive = new DatagramPacket(b1, b1.length);
				ds.receive(dpReceive);
				
				String date = new String(dpReceive.getData());				
				
				System.out.println("[Server] Nachricht von Server: " + date);
				
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		else {
			try (Socket socket = new Socket()){
				socket.connect(address, 5000); //5000 sind der Timeout, bis die Verbindung abgebrochen wird und kein erneuter Versuch mehr unternommen wird!
				
				Scanner serverScanner = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));
			
				if (serverScanner.hasNext()) {
					System.out.println("[Server] Nachricht von " + socket.getRemoteSocketAddress() + ": " + serverScanner.nextLine());
					System.out.println("[Server] Verwendetes Transportprotokoll: " + socket.getTrafficClass());
				}
				
				serverScanner.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
