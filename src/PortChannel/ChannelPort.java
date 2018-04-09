package PortChannel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ChannelPort implements Runnable {
	private int portNum;
	
	ServerSocket serversocket = null;	//Socket for Server
	Socket clientsocket = null;			//Socket for client
	/*Declaring a Hashset for storing client ID's*/
	private HashSet<String> clientids = new HashSet<String>();
	public String id;
	/*Declaring Arraylist to store server output to clients*/
	private ArrayList<ObjectOutputStream> output = new ArrayList<ObjectOutputStream>();
	/**/
	private ArrayList<Listener> listeners = new ArrayList<Listener>();
	/*Declaring LinkedBlocking queue for posting */
	private ConcurrentLinkedQueue<BoardMessage> que = new ConcurrentLinkedQueue<BoardMessage>();
	
	/*Constructor Declaration*/
	public ChannelPort(int portNum){
		this.portNum = portNum;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
				try {
					//The server instantiates a ServerSocket object, denoting which port number communication is to occur on.
					serversocket = new ServerSocket(getPortNum());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (serversocket == null){
					return;
				}
				
				while(true){
					try {
						//The server invokes the accept() method of the ServerSocket class. 
						//This method waits until a client connects to the server on the given port.
						//On the server side, the accept() method returns a reference,
						//to a new socket on the server that is connected to the client's socket.
						clientsocket = serversocket.accept();
						
						output.add(new ObjectOutputStream(clientsocket.getOutputStream()));
						//All the client input is stored in here
						ObjectInputStream in = new ObjectInputStream(clientsocket.getInputStream());
						Listener listener = new Listener(in, ChannelPort.this);
						listeners.add(listener);
						listener.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("All connections Established\n");
			        }
	}
			
	synchronized void gotMessage(BoardMessage msg){
		que.offer(msg);
		notifyAll();
	}
	
	public synchronized BoardMessage receive(){
		
		while(que.isEmpty()){
			try {
				System.out.println("waiting");
				wait();
				System.out.println("waiting finished");

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BoardMessage msg = que.poll();
		System.out.println("Reached till poll");
		System.out.println(msg);
		id = msg.getPersonId();
		String msgType = msg.getMessageType();
		if(msgType.equals("Login")){
			System.out.println("ids adding");
			synchronized (clientids) {
                if (!clientids.contains(id)) {
                	clientids.add(id);
                  
                }
            }
			/*clientids.add(id);*/
			
		}
		else if(msgType.equals("LogOff")){	

			clientids.remove(id);

		}
		return msg;
	}
	
	public synchronized void broadcast(BoardMessage msg){
		System.out.println(msg);
		for (int i=0; i<output.size();i++){
			send(i,msg);
		}
			
	}
	
	public void send(int index, BoardMessage msg){
		
		try {
			output.get(index).writeObject(msg);
			output.get(index).flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getPortNum() {
		return portNum;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}

	
/*}*/

private class Listener extends Thread{
	ObjectInputStream in;
	ChannelPort cPort;
	
	public Listener(ObjectInputStream in, ChannelPort cPort){
		this.in = in;
		this.cPort = cPort;
	}
	
	public void run(){
		BoardMessage msg;
		while(in != null){
				try {
					msg = (BoardMessage)in.readObject();
					cPort.gotMessage(msg);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SocketException se){
					se.printStackTrace();
				} catch (IOException ioe){
					ioe.printStackTrace();
				}finally{
					if(id!=null){
						clientids.remove(id);
					}
				}
		}
	}
}
}