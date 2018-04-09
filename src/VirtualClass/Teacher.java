package VirtualClass;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import PortChannel.BoardMessage;
import PortChannel.ChannelPort;

import java.awt.TextArea;
import java.awt.List;
import java.awt.Button;
import java.awt.Color;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JList;

public class Teacher {
	ChannelPort channel_Port;
	
	private JFrame teacher_Frame;
	TextArea message_Area = new TextArea();
	private final Button send_Button = new Button("SEND");
	private final JList question_List = new JList();
	Button question_Button = new Button("Approve");
	TextField msg_Input = new TextField();
	
	private int port_Address;
	DefaultListModel<String> questionModel = new DefaultListModel<String>();
	DefaultListModel<String> StudentModel = new DefaultListModel<String>();
	
	
	/**
	 * Launch the application.
	 */
	public void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Teacher window = new Teacher(port_Address);
					window.teacher_Frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public int getPort(){
		return port_Address;
	}
	
	public void setVisible(boolean b){
		if(b=true)
		{
			teacher_Frame.setVisible(true);
		}
		else{
			teacher_Frame.setVisible(false);
		}
		
	}
	/**
	 * Create the application.
	 */
	public Teacher( int port) {
		this.port_Address = port;
		initialize(); // initializes teacher frame
		try {
			InetAddress ip = InetAddress.getLocalHost(); // Returns the address of the local host from system and resolving it to InetAdress
			message_Area.append("The local host is: ");
			message_Area.append(ip.toString() + "\n");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		question_Button.setEnabled(false);
		
		port_Address = getPort(); // Port address given at TeacherLogin input
		
		channel_Port = new ChannelPort(port_Address);
		new Thread(channel_Port).start();
		
		question_List.setModel(questionModel);
		
		Thread messagecheck = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					System.out.println("Reached here");
					BoardMessage receivedmsg = channel_Port.receive();
					String messageType = receivedmsg.getMessageType();
					
					if (messageType.equals("Login")){
						addStudent(receivedmsg);
					}
					
					else if (messageType.equals("Logout")){
						removeStudent(receivedmsg);}
					
					else {
						if (messageType.equals("Question")){
							addQuestion(receivedmsg);
						}
						channel_Port.broadcast(receivedmsg);
					}
					
					addNotesToMessageArea(receivedmsg);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		messagecheck.start();
		System.out.println("All Fine " + getPort());
		
	}
	
	public synchronized void addNotesToMessageArea(BoardMessage msg){
		message_Area.append(msg.toString());
		boolean empty = questionModel.isEmpty();
		question_Button.setEnabled(!empty);
	}
	
	public synchronized void addQuestion(BoardMessage msg){
		String student_Id = msg.getPersonId();
		if (questionModel.contains(student_Id)){
			return;
		}
		else{
			questionModel.addElement(student_Id);
		}
	}
	
	public synchronized void addStudent(BoardMessage msg){
		StudentModel.addElement(msg.getPersonId());
	}
	
	public synchronized void removeStudent(BoardMessage msg){
		StudentModel.removeElement(msg.getPersonId());
	}
	
	public void sendMessage(){
		String message = msg_Input.getText();
		if (!message.isEmpty()){
			BoardMessage msg = new BoardMessage("Teacher", "Notes", message);
			addNotesToMessageArea(msg);
			channel_Port.broadcast(msg);
			System.out.println("message delivered");
		}
		msg_Input.setText("");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		port_Address = getPort();
		teacher_Frame = new JFrame();
		teacher_Frame.setTitle("Teacher main window");
		teacher_Frame.setBounds(100, 100, 671, 542);
		teacher_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		teacher_Frame.getContentPane().setLayout(null);
		teacher_Frame.getContentPane().setBackground(Color.white);
		
		
		message_Area.setBounds(10, 138, 633, 347);
		teacher_Frame.getContentPane().add(message_Area);
		
		question_List.setBounds(484, 10, 157, 88);
		
		teacher_Frame.getContentPane().add(question_List);
		
		question_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = question_List.getSelectedIndex();
				if (index == -1)
					return;
				
				String st = questionModel.getElementAt(index);
				
				int stuindex = StudentModel.indexOf(st);
				
				BoardMessage msg = new BoardMessage("Teacher","Approved","OK");
				addNotesToMessageArea(msg);
				channel_Port.send(stuindex, msg);
				questionModel.clear();
			}
		});
		question_Button.setBounds(496, 108, 125, 24);
		teacher_Frame.getContentPane().add(question_Button);
		msg_Input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				int keycode = arg0.getKeyCode();
				if (keycode == 10){
					sendMessage();
				}
			}
		});
		
		
		msg_Input.setBounds(10, 10, 455, 54);
		teacher_Frame.getContentPane().add(msg_Input);
		send_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		send_Button.setBounds(20, 89, 445, 24);
		
		teacher_Frame.getContentPane().add(send_Button);
	}
}
