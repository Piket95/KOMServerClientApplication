//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
//import java.util.Scanner;

public class Server {

	private int port;
	private NetworkInterface netInt;
	
	private Thread udpThread;
	private Thread tcpThread;
	
	private Date datum;
	
	public Server (int port, NetworkInterface s) {
		this.port = port;
		this.netInt = s;
	}
	
	public void startListening() {
		
		System.out.println("Starte UDP Listening auf Port " + port + "!");
		udpThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				//Der Server steht nach einer beendeten Verbindung direkt wieder für eine neue Verbindung bereit! (while (true))
				while (true) {
					try (DatagramSocket ds = new DatagramSocket(port)){
						System.out.println("[Server/UDP] Warte auf Verbindung...");
						
						//Leeres Paket erhalten (Initialisierung)
						byte[] b = new byte[1024];
						DatagramPacket dp = new DatagramPacket(b, b.length);
						ds.receive(dp);
						
						System.out.println("[Server/UDP] Paket erhalten von: " + dp.getAddress());
						
						//Versenden des Datums an Versender des zuvor erhaltenen leeren Pakets
						datum = new Date();
						String strDatum = datum.toString();
						byte[] b1 = strDatum.getBytes();
						DatagramPacket dp1 = new DatagramPacket(b1, b1.length, dp.getAddress(), dp.getPort());
						ds.send(dp1);
						
						System.out.println("[Server/UDP] Datum wurde an Client (" + dp.getAddress() + ") versandt!");
						
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		udpThread.start();
		
		System.out.println("Starte TCP Listening auf Port " + port + "!");
		tcpThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				//Der Server steht nach einer beendeten Verbindung direkt wieder für eine neue Verbindung bereit! (while (true))
				while (true) {
					try (ServerSocket serverSocket = new ServerSocket(port, 10, netInt.getInetAddresses().nextElement())){
						System.out.println("[Server/TCP] Warte auf Verbindung...");
						Socket client = serverSocket.accept(); //Programm bleibt hier so lange stehen, bis eine Verbindung anliegt!
						
						System.out.println("[Server/TCP] Client verbunden: " + client.getRemoteSocketAddress());
						
						//Senden des Datums an den Client, der aktuell verbunden ist
						PrintWriter pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
						pw.println(new Date());
						pw.flush();
						
						System.out.println("[Server/TCP] Datum wurde an Client (" + client.getRemoteSocketAddress() + ") versandt!");
						
						//Verbindung mit Client wird wieder getrennt
						client.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		
		tcpThread.start();
		
	}
}
