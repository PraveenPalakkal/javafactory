package com.app.customer.controller;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSeparator;

import com.app.customer.util.LdapAuthentication;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;

public class Login extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CustomerList customerList;
	private JPasswordField password;
	private JTextField user;
	static Logger log = LogFactory.getLogger(Login.class);

	/**
	 * @param args
	 */
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					UIManager.setLookAndFeel(UIManager
							.getCrossPlatformLookAndFeelClassName());
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {

				}

			}
		});
	}

	public Login() throws InterruptedException {
		start();
	}

	void start() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 436, 294);
		setResizable(false);
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 432, 269);
		contentPane.add(panel);
		panel.setLayout(null);

		user = new JTextField();
		user.setBounds(157, 40, 214, 33);
		panel.add(user);
		user.setColumns(10);

		password = new JPasswordField();
		password.setBounds(157, 107, 214, 33);
		password.setEchoChar('*');
		panel.add(password);
		password.setColumns(10);

		JLabel lblUsername = new JLabel("UserName*");
		lblUsername.setForeground(new Color(65, 105, 225));
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUsername.setBounds(54, 39, 93, 33);
		panel.add(lblUsername);

		JLabel lblPassword = new JLabel("Password*");
		lblPassword.setForeground(new Color(65, 105, 225));
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPassword.setBounds(54, 106, 93, 33);
		panel.add(lblPassword);

		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String passText = new String(password.getPassword());
				System.out.println(passText);
				LdapAuthentication authentication = new LdapAuthentication();
				try {

					if (!"".equalsIgnoreCase(passText)
							&& !"".equalsIgnoreCase(user.getText())
							&& authentication.checkAuthentication(
									user.getText(), passText)){
						log.info("Authentication success for user");
						msgbox("Login Successfull");
						CustomerList.flag=true;
						dispose();
						customerList = new CustomerList();
						customerList.start();
						customerList.setVisible(true);

					} else {
						log.info("Authentication faiiled for user");
						msgbox("Login Failed");
						user.setText("");
						password.setText("");
					}

				} catch (Exception exp) {
					log.debug("Authentication faiiled for user");
					log.info(exp.getMessage());
					msgbox("Login Failed");
					user.setText("");
					password.setText("");
				}

			}
		});
		btnLogin.setBounds(157, 207, 108, 33);
		panel.add(btnLogin);

		JSeparator separator = new JSeparator();
		separator.setBounds(-11, 181, 489, 8);
		panel.add(separator);
	}

	private void msgbox(String string) {
		JOptionPane.showMessageDialog(null, string);
	}
}
