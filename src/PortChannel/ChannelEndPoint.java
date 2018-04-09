package PortChannel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ChannelEndPoint {
	String clientId;
	ObjectInputStream input;
	ObjectOutputStream output;
	Socket soc = null;
	String ipAddress;
	int port;
	
	public ChannelEndPoint(String clientId, String ipAddress, int port){
		this.clientId = clientId;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public void initialize(){
		while (soc == null){
			try {
				soc = new Socket(ipAddress, port);
				
				if (soc != null){
					System.out.println("Got a Socket");
					input = new ObjectInputStream(soc.getInputStream());
					output = new ObjectOutputStream(soc.getOutputStream());
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				System.err.println("Dont know about the server\n");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Couldn't get I/O for the connection to the server\n");
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public BoardMessage receive(){
		BoardMessage result = null;
		
		try {
			//stores 
			result = (BoardMessage)input.readObject();
		} catch (ClassNotFoundException cnfe) {
			// TODO Auto-generated catch block
			cnfe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
		
		return result;
	}
	
	public void send(BoardMessage msg){
		try {
			output.writeObject(msg);
			output.flush();
		} catch(SocketException se){
			System.exit(1);
		}catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
	}
	
	public void close(){
		System.out.println("Shutting Down");
		
		send(new BoardMessage(clientId, "Logout", clientId));
		try {
			input.close();
			output.close();
			soc.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
