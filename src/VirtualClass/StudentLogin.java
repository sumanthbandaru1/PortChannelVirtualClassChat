package VirtualClass;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Label;
import java.awt.TextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StudentLogin {

	private JFrame student_login;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentLogin window = new StudentLogin();
					window.student_login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public StudentLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		student_login = new JFrame();
		student_login.setTitle("Student Login Window Open");
		student_login.setBounds(100, 100, 450, 300);
		student_login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		student_login.getContentPane().setLayout(null);
		student_login.getContentPane().setBackground(Color.YELLOW);
		
		JLabel lbl_HelloVirtual = new JLabel("Hello! This is Virtual Classroom");
		lbl_HelloVirtual.setBounds(122, 24, 230, 16);
		student_login.getContentPane().add(lbl_HelloVirtual);
		
		Label label_Id = new Label("Enter Your ID");
		label_Id.setAlignment(Label.RIGHT);
		label_Id.setBounds(27, 78, 122, 24);
		student_login.getContentPane().add(label_Id);
		
		TextField studentField = new TextField();
		studentField.setBounds(183, 78, 155, 24);
		student_login.getContentPane().add(studentField);
				
		Label label_Id_1 = new Label("Enter IP Address");
		label_Id_1.setAlignment(Label.RIGHT);
		label_Id_1.setBounds(27, 122, 122, 24);
		student_login.getContentPane().add(label_Id_1);
		
		TextField ip_Add = new TextField();
		ip_Add.setBounds(183, 122, 155, 24);
		student_login.getContentPane().add(ip_Add);
				
		Label label_Id_2 = new Label("Enter Port Number");
		label_Id_2.setAlignment(Label.RIGHT);
		label_Id_2.setBounds(10, 164, 139, 24);
		student_login.getContentPane().add(label_Id_2);
		
		TextField portField = new TextField();
		portField.setBounds(183, 164, 155, 24);
		student_login.getContentPane().add(portField);
		
		
		
		JButton btnLogin = new JButton("Login");

		btnLogin.setBounds(149, 215, 97, 25);
		student_login.getContentPane().add(btnLogin);
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String studentId = studentField.getText();
				String ipAddress = ip_Add.getText();
				int portAddress = Integer.parseInt(portField.getText());
				Student s = new Student(studentId, ipAddress, portAddress);
				student_login.dispose();
				s.setVisible(true);
			}
		});
	}
}
