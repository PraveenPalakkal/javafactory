package com.app.customer.controller;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.hex.framework.mail.MailingImpl;
import com.hex.framework.mail.vo.MailVO;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

import java.awt.Color;
import java.util.Locale;
import java.util.ResourceBundle;

public class EmailNotification extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField fromText;
	private JTextField subjectText;
	CustomerList customerList;
	static Logger log = LogFactory.getLogger(EmailNotification.class);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					UIManager.setLookAndFeel(UIManager
							.getCrossPlatformLookAndFeelClassName());
					EmailNotification frame = new EmailNotification();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public EmailNotification() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 611, 471);
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 602, 440);
		contentPane.add(panel);
		panel.setLayout(null);

		fromText = new JTextField();
		fromText.setBackground(new Color(255, 255, 255));
		fromText.setForeground(Color.BLACK);
		Border borderfromText = BorderFactory.createLineBorder(Color.BLACK);
		fromText.setBorder(borderfromText);
		fromText.setBounds(157, 24, 406, 32);
		panel.add(fromText);
		fromText.setColumns(10);

		JLabel lblFrom = new JLabel("From");
		lblFrom.setForeground(new Color(65, 105, 225));
		lblFrom.setBounds(36, 26, 83, 25);
		panel.add(lblFrom);
		lblFrom.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblTo = new JLabel("To..");
		lblTo.setForeground(new Color(65, 105, 225));
		lblTo.setBounds(36, 77, 83, 25);
		panel.add(lblTo);
		lblTo.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblCc = new JLabel("CC..");
		lblCc.setForeground(new Color(65, 105, 225));
		lblCc.setBounds(36, 133, 83, 29);
		panel.add(lblCc);
		lblCc.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblBcc = new JLabel("Bcc..");
		lblBcc.setForeground(new Color(65, 105, 225));
		lblBcc.setBounds(36, 185, 83, 29);
		panel.add(lblBcc);
		lblBcc.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setForeground(new Color(65, 105, 225));
		lblSubject.setBounds(36, 231, 83, 25);
		panel.add(lblSubject);
		lblSubject.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblContent = new JLabel("Content");
		lblContent.setForeground(new Color(65, 105, 225));
		lblContent.setBounds(36, 277, 83, 25);
		panel.add(lblContent);
		lblContent.setFont(new Font("Tahoma", Font.BOLD, 14));

		subjectText = new JTextField();
		subjectText.setBounds(157, 229, 408, 32);
		Border bordersubjectText = BorderFactory.createLineBorder(Color.BLACK);
		subjectText.setBorder(bordersubjectText);
		panel.add(subjectText);
		subjectText.setColumns(10);

		JButton btnSend = new JButton("Send");
		btnSend.setForeground(new Color(65, 105, 225));
		btnSend.setBounds(251, 397, 113, 32);
		panel.add(btnSend);
		btnSend.setFont(new Font("Tahoma", Font.BOLD, 14));

		final JTextArea contentText = new JTextArea();
		contentText.setBounds(157, 279, 406, 79);
		Border border3 = BorderFactory.createLineBorder(Color.BLACK);
		contentText.setBorder(border3);
		panel.add(contentText);
		contentText.setRows(5);

		final JTextArea bccText = new JTextArea();
		bccText.setBounds(157, 182, 406, 32);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		bccText.setBorder(border);
		panel.add(bccText);
		bccText.setRows(2);

		final JTextArea ccText = new JTextArea();
		ccText.setBounds(157, 130, 406, 32);
		Border border1 = BorderFactory.createLineBorder(Color.BLACK);
		ccText.setBorder(border1);
		panel.add(ccText);
		ccText.setRows(2);

		final JTextArea toText = new JTextArea();
		toText.setBounds(157, 74, 406, 32);
		Border border2 = BorderFactory.createLineBorder(Color.BLACK);
		toText.setBorder(border2);
		panel.add(toText);
		toText.setRows(2);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 369, 602, 8);
		panel.add(separator);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResourceBundle bundle = ResourceBundle.getBundle(
						"MailServerCridencials", Locale.ENGLISH);
				String[] to = new String[1];
				String[] cc = new String[1];
				String[] bc = new String[1];
				to[0] = toText.getText();
				cc[0] = ccText.getText();
				bc[0] = bccText.getText();
				String host = bundle.getString("SEVER_HOST");
				int port = Integer.parseInt(bundle.getString("SEVER_PORT"));
				MailVO mailVO = new MailVO();
				mailVO.setFrom(fromText.getText());
				mailVO.setSubject(subjectText.getText());
				mailVO.setBody(contentText.getText());
				mailVO.setHost(host);
				mailVO.setPort(port);
				mailVO.setBccs(bc);
				mailVO.setCcs(cc);
				try {
					new MailingImpl().sendEmail(mailVO);
					dispose();
					customerList = new CustomerList();
					customerList.setVisible(true);
				} catch (Exception exp) {
					log.debug("Login Failed");
				}
			}
		});

	}
}
