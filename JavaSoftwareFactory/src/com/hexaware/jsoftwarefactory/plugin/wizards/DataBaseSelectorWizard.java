package com.hexaware.jsoftwarefactory.plugin.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Group;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

public class DataBaseSelectorWizard extends WizardPage {

	Logger log = LogFactory.getLogger("DataBaseSelectorWizard");

	private Button btnOracle;
	private Button btnMicrosoftSqlServer;
	private Button btnMysql;
	private Group grpDatabase;

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
		grpDatabase.setText("Database Server");
		grpDatabase.setBounds(124, 78, 249, 173);

		btnOracle = new Button(grpDatabase, SWT.RADIO);
		btnOracle.setBounds(46, 44, 174, 16);
		btnOracle.setText("Oracle");
		btnOracle.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (btnOracle.getSelection() == true) {
					MessageBox messageDialog = new MessageBox(getShell(),
							SWT.OK);
					messageDialog.setText("Alert!!");
					messageDialog
							.setMessage("It is under construction. Please select any other Database Server.");
					if (messageDialog.open() == SWT.OK) {
						return;
					}
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnMicrosoftSqlServer = new Button(grpDatabase, SWT.RADIO);
		btnMicrosoftSqlServer.setBounds(46, 85, 174, 16);
		btnMicrosoftSqlServer.setText("Microsoft SQL Server");
		btnMicrosoftSqlServer.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (btnMicrosoftSqlServer.getSelection() == true) {
					MessageBox messageDialog = new MessageBox(getShell(),
							SWT.OK);
					messageDialog.setText("Alert!!");
					messageDialog
							.setMessage("It is under construction. Please select any other Database Server.");
					if (messageDialog.open() == SWT.OK) {
						return;
					}
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnMysql = new Button(grpDatabase, SWT.RADIO);
		btnMysql.setBounds(46, 127, 174, 16);
		btnMysql.setText("MySQL");
		btnMysql.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getContainer().updateButtons();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public IWizardPage getNextPage() {
		log.info("\nInside getnext page of DataProviderSelectorWizard");
		if (btnMysql.getSelection() == true) {
			return super.getNextPage();
		}

		return null;
	}
}
