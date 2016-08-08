package com.hexaware.jsoftwarefactory.plugin.dialogs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

public class DatabaseConnectionDetailsDialog extends Dialog {
	private Text textUrl;
	private Text textUsername;
	private Text textPassword;
	private String url = "";
	private String userName = "";
	private String password = "";
	private char pass_char = '*';
	private String dbType = "";
	private String driver="";

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Text getTextUrl() {
		return textUrl;
	}

	public void setTextUrl(Text textUrl) {
		this.textUrl = textUrl;
	}

	public Text getTextUsername() {
		return textUsername;
	}

	public void setTextUsername(Text textUsername) {
		this.textUsername = textUsername;
	}

	public Text getTextPassword() {
		return textPassword;
	}

	public void setTextPassword(Text textPassword) {
		this.textPassword = textPassword;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public DatabaseConnectionDetailsDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.TITLE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);
		container.getShell().setText("Connection Details");

		Label lblIp = new Label(container, SWT.NONE);
		lblIp.setBounds(62, 31, 87, 28);
		lblIp.setText("URL* :");

		Label lblUsername = new Label(container, SWT.NONE);
		lblUsername.setBounds(62, 94, 87, 28);
		lblUsername.setText("UserName* :");

		Label lblPassword = new Label(container, SWT.NONE);
		lblPassword.setBounds(62, 155, 87, 28);
		lblPassword.setText("Password* :");

		textUrl = new Text(container, SWT.BORDER);
		textUrl.setBounds(166, 28, 218, 28);
		textUrl.setText(url);
		textUrl.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text textUrl = (Text) e.getSource();
				String userText = textUrl.getText();
				url = userText;
			}
		});

		textUsername = new Text(container, SWT.BORDER);
		textUsername.setBounds(166, 91, 218, 28);
		textUsername.setText(userName);
		textUsername.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text textUser = (Text) e.getSource();
				String userText = textUser.getText();
				userName = userText;
			}
		});

		textPassword = new Text(container, SWT.BORDER);
		textPassword.setBounds(166, 152, 218, 28);
		textPassword.setEchoChar(pass_char);
		textPassword.setText(password);
		textPassword.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text textPass = (Text) e.getSource();
				String userText = textPass.getText();
				password = userText;
			}
		});

		return container;

	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		button.setText("Test");
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	protected void okPressed() {
		if (textUrl.getText().isEmpty() || textUsername.getText().isEmpty()
				|| textPassword.getText().isEmpty()) {
			MessageBox messageDialog = new MessageBox(getShell(), SWT.OK);
			messageDialog.setText("Alert!!");
			messageDialog.setMessage("Please enter the credentials.");
			if (messageDialog.open() == SWT.OK) {
				return;
			}
		} else {
			Connection connection = null;
			url = textUrl.getText();
			userName = textUsername.getText();
			password = textPassword.getText();
			String dbDriver= getDriver();
			MessageBox msgbox = new MessageBox(getShell(), SWT.OK);
			System.out.println("Driver----->>>>"+dbDriver);
			try {
				Class.forName(dbDriver);
				connection = DriverManager.getConnection(url, userName,
						password);
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (connection!=null) {
				msgbox.setText("Success");
				msgbox.setMessage("Successfully connected");
				msgbox.open();
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				super.okPressed();
			}
			else
			{
				msgbox.setText("Failed");
				msgbox.setMessage("Connection failed");
				msgbox.open();
			}

		
		}
	}

}
