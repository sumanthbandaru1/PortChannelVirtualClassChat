package VirtualClass;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;

import PortChannel.BoardMessage;
import PortChannel.ChannelEndPoint;

import java.awt.TextArea;
import java.awt.TextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Student {

	private JFrame student_Frame;
	String student_Id;
	String ip_Address;
	int port;
	ChannelEndPoint cE_Point;
	boolean connected = false;
	TextArea notes_Area = new TextArea();
	TextField question_Input = new TextField();
	JButton question_Button = new JButton("Rise Question");
	

	
	/**
	 * Launch the application.
	 */
	public void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Student window = new Student(student_Id, ip_Address, port);
					window.student_Frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setVisible(boolean b){
		if(b=true)
		{
			student_Frame.setVisible(true);
		}
		else{
			student_Frame.setVisible(false);
		}
	}
	
	
	
	public Student(String stuID, String ip_Address, int port) {
		this.student_Id = stuID;
		this.ip_Address = ip_Address;
		this.port = port;
		initialize();
		notes_Area.setEditable(false);
		question_Input.setEnabled(false);
		question_Button.setEnabled(true);
		cE_Point = new ChannelEndPoint(student_Id,ip_Address,port);
		Thread client = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				cE_Point.initialize();
				connected = true;
				
				BoardMessage msg = new BoardMessage(student_Id, "Login", "Success");
				cE_Point.send(msg);
				while(true){
					BoardMessage returnmsg = (BoardMessage)cE_Point.receive();
					addNotes(returnmsg);
					filter(returnmsg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		client.start();
	}

	public synchronized void addNotes(BoardMessage msg){
		notes_Area.append(msg.toString());
	}
	
	public synchronized void filter(BoardMessage msg){
		if (msg.getMessageType().equals("Approved")){
			question_Input.setEnabled(true);
			question_Button.setEnabled(false);
		}
	}
	
	public void send(BoardMessage msg){
		cE_Point.send(msg);
	}
	public void sendText(){
		String text = question_Input.getText();
		if(!text.isEmpty()){
			BoardMessage msg = new BoardMessage(student_Id, "Speaks", text);
			send(msg);
		}
		question_Input.setEnabled(false);
		question_Button.setEnabled(true);
		question_Input.setText(""); // clears the text
	}
	
	
	 // Initialize the contents of the frame.
	 
	private void initialize() {
		student_Frame = new JFrame();
		student_Frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				cE_Point.close();
			}
		});
		student_Frame.setTitle(student_Id);
		student_Frame.setBounds(100, 100, 467, 314);
		student_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		student_Frame.getContentPane().setLayout(null);
		
		
		notes_Area.setBounds(0, 0, 433, 209);
		student_Frame.getContentPane().add(notes_Area);
		student_Frame.getContentPane().setBackground(Color.YELLOW); 
		
		
		question_Input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keycode = e.getKeyCode();
				if (keycode == 10){
					sendText();
				}
			}
		});
		question_Input.setBounds(108, 224, 325, 24);
		student_Frame.getContentPane().add(question_Input);	
		
		question_Button.setBounds(6, 223, 96, 31);
		student_Frame.getContentPane().add(question_Button);
		question_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (connected == false)
					return;
				BoardMessage msg = new BoardMessage(student_Id, "Question","Raised");
				send(msg);
			}
		});
	}

}
