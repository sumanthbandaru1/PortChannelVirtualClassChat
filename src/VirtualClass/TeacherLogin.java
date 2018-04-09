package VirtualClass;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import PortChannel.*;

public class TeacherLogin {

	private JFrame teacher_Login; //create a JFrame from swings to create a frame

	/**
	 * This code will launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TeacherLogin window = new TeacherLogin(); // Constructor call will initialize the frame
					window.teacher_Login.setVisible(true); // Make the frame visible
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TeacherLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		teacher_Login = new JFrame();
		teacher_Login.setTitle("Teacher Login Window");
		teacher_Login.setBounds(200, 200, 700, 250);
		teacher_Login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		teacher_Login.getContentPane().setLayout(null);
		teacher_Login.getContentPane().setBackground(Color.white);
		
		Label label_port = new Label("Enter the Port Number");
		label_port.setBounds(79, 98, 190, 24);
		teacher_Login.getContentPane().add(label_port); // adds label_port to the frame
		
		TextField port_number = new TextField();
		port_number.setBounds(270, 98, 100, 24);
		teacher_Login.getContentPane().add(port_number);
		
		Button button_login = new Button("Login");
		button_login.setBounds(164, 167, 79, 24);
		teacher_Login.getContentPane().add(button_login); // adds button to the frame
		//Login button click event action performed here
		button_login.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int port = Integer.parseInt(port_number.getText());
				Teacher t = new Teacher(port); // Parameterized constructor call to Teacher
				teacher_Login.dispose();
				t.setVisible(true);
			}
		});
		
	}
}
