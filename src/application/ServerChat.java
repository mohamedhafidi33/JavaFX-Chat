package application;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerChat extends Thread{
	private int numClient=0;
	private boolean isActive=true;
	List<Conversation> clients=new ArrayList<Conversation>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ServerChat().start();
	}
	@Override
	public void run() {
		try {
			ServerSocket ss=new ServerSocket(1234);
			while(isActive) {
				Socket s=ss.accept();
				++numClient;
				Conversation client=new Conversation(s,numClient);
				clients.add(client);
				client.start();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public class Conversation extends Thread{
		private Socket socket;
		private int numClient;
		public Conversation(Socket s, int numClient) {
			this.socket = s;
			this.numClient = numClient;
		}
		public void broadcastMessage(String msg,Socket socket,int numClient) {
			for(Conversation client:clients) {
				if(client.socket!=socket) {
					if(client.numClient==numClient || numClient==-1) {
					try {
						PrintWriter pw=new PrintWriter(client.socket.getOutputStream(),true);
						pw.println(msg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}
			}
		}
		public void run() {
			try {
				InputStream is=socket.getInputStream();
				BufferedReader br=new BufferedReader(new InputStreamReader(is));
				PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
				pw.println("Bonjour Client ,vous etes client numero "+numClient);
				System.out.println("Un client vient de se connecter numero"+numClient);
				while(true) {
					String req=br.readLine();
					if(req.contains("=>")) {
						String[] par=req.split("=>");
						if(par.length==2) {
							String msg= par[1];
							int numClient=Integer.parseInt(par[0]);
							broadcastMessage(msg,socket,numClient);
						}
					}else
						broadcastMessage(req,socket,-1);
					
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
