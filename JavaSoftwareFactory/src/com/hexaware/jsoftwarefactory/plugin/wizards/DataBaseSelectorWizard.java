package com.hexaware.jsoftwarefactory.plugin.wizards;

import java.awt.peer.ButtonPeer;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
import com.hexaware.jsoftwarefactory.plugin.dialogs.DatabaseConnectionDetailsDialog;
import com.swtdesigner.SWTResourceManager;

public class DataBaseSelectorWizard extends WizardPage {

	Logger log = LogFactory.getLogger("DataBaseSelectorWizard");
	public Button btnOracle;
	public Button btnMicrosoftSqlServer;
	public Button btnMysql;
	private Group grpDatabase;
	public DatabaseConnectionDetailsDialog databaseConnection;

	public Button getBtnMysql() {
		return btnMysql;
	}

	public void setBtnMysql(Button btnMysql) {
		this.btnMysql = btnMysql;
	}

	public Button getBtnOracle() {
		return btnOracle;
	}

	public void setBtnOracle(Button btnOracle) {
		this.btnOracle = btnOracle;
	}

	public Button getBtnMicrosoftSqlServer() {
		return btnMicrosoftSqlServer;
	}

	public void setBtnMicrosoftSqlServer(Button btnMicrosoftSqlServer) {
		this.btnMicrosoftSqlServer = btnMicrosoftSqlServer;
	}

	public DatabaseConnectionDetailsDialog getDatabaseConnection() {
		return databaseConnection;
	}

	public void setDatabaseConnection(
			DatabaseConnectionDetailsDialog databaseConnection) {
		this.databaseConnection = databaseConnection;
	}

	/**
	 * Create the wizard.
	 */
	public DataBaseSelectorWizard(String pageName) {
		super(pageName);
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);

		grpDatabase = new Group(container, SWT.NONE);
		grpDatabase.setText("Database");
		grpDatabase.setBounds(135, 72, 246, 173);

		btnOracle = new Button(grpDatabase, SWT.RADIO);
		btnOracle.setBounds(46, 44, 135, 16);
		btnOracle.setText("Oracle");
		btnOracle.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnOracle.getSelection()) {

					if (databaseConnection != null
							&& databaseConnection.getDbType().equalsIgnoreCase(
									"Oracle")) {
						int invokeDialog = databaseConnection.open();
						if (invokeDialog == IDialogConstants.OK_ID) {
							getContainer().updateButtons();
						}
					} else {
						databaseConnection = new DatabaseConnectionDetailsDialog(
								getShell());
						databaseConnection.setDriver("oracle.jdbc.driver.OracleDriver");
						int invokeDialog = databaseConnection.open();
						databaseConnection.setDbType("Oracle");
						if (invokeDialog == IDialogConstants.OK_ID) {
							getContainer().updateButtons();
						}
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btnMicrosoftSqlServer = new Button(grpDatabase, SWT.RADIO);
		btnMicrosoftSqlServer.setBounds(46, 85, 135, 16);
		btnMicrosoftSqlServer.setText("Microsoft SQL Server");
		btnMicrosoftSqlServer.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnMicrosoftSqlServer.getSelection()) {

					if (databaseConnection != null
							&& databaseConnection.getDbType().equalsIgnoreCase(
									"MSsql")) {
						int invokeDialog = databaseConnection.open();
						if (invokeDialog == IDialogConstants.OK_ID) {
							getContainer().updateButtons();
						}
					} else {

						databaseConnection = new DatabaseConnectionDetailsDialog(
								getShell());
						databaseConnection.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
						int invokeDialog = databaseConnection.open();
						databaseConnection.setDbType("MSsql");
						if (invokeDialog == IDialogConstants.OK_ID) {
							getContainer().updateButtons();
						}
					}

				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btnMysql = new Button(grpDatabase, SWT.RADIO);
		btnMysql.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		btnMysql.setBounds(46, 127, 135, 16);
		btnMysql.setText("MySQL");
		btnMysql.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnMysql.getSelection()) {
					if (databaseConnection != null
							&& databaseConnection.getDbType().equalsIgnoreCase(
									"Mysql")) {
						System.out.println("DB Type----->>"
								+ databaseConnection.getDbType());
						int invokeDialog = databaseConnection.open();
						if (invokeDialog == IDialogConstants.OK_ID) {
							getContainer().updateButtons();
						}
					} else {
						databaseConnection = new DatabaseConnectionDetailsDialog(
								getShell());
						databaseConnection.setDriver("com.mysql.jdbc.Driver");
						int invokeDialog = databaseConnection.open();
						databaseConnection.setDbType("Mysql");
						if (invokeDialog == IDialogConstants.OK_ID) {
							getContainer().updateButtons();
						}
					}
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public IWizardPage getNextPage() {
		log.info("\nInside getnext page of DataBaseSelectorWizard");
		if ((btnMysql.getSelection() || btnMicrosoftSqlServer.getSelection() || btnOracle
				.getSelection())
				&& IDialogConstants.OK_ID == databaseConnection.getReturnCode()) {
			return super.getNextPage();
		}
		return null;

	}
}
