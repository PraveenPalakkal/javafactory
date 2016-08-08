package com.hexaware.jsoftwarefactory.plugin.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Group;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

public class ContinuousIntegrationSelectorWizard extends WizardPage {

	Logger log = LogFactory.getLogger("ContinuousIntegrationSelectorWizard");
	private Button btnJenkins;
	private Button btnCruiseControl;
	private Button btnNone;
	private Group grpContinuousIntegrationci;

	/**
	 * Create the wizard.
	 */
	public ContinuousIntegrationSelectorWizard(String pageName) {
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

		grpContinuousIntegrationci = new Group(container, SWT.NONE);
		grpContinuousIntegrationci.setText("Continuous Integration Tool");
		grpContinuousIntegrationci.setBounds(121, 73, 273, 181);

		btnJenkins = new Button(grpContinuousIntegrationci, SWT.RADIO);
		btnJenkins.setBounds(48, 91, 160, 16);
		btnJenkins.setText("Jenkins");

		btnCruiseControl = new Button(grpContinuousIntegrationci, SWT.RADIO);
		btnCruiseControl.setBounds(48, 130, 160, 16);
		btnCruiseControl.setText("CruiseControl");

		btnNone = new Button(grpContinuousIntegrationci, SWT.RADIO);
		btnNone.setBounds(49, 50, 136, 16);
		btnNone.setText("None");
		btnNone.setSelection(true);

	}

	@Override
	public IWizardPage getNextPage() {
		log
				.info("\nInside getnext page of ContinuousIntegrationSelectorWizard");
		if (btnJenkins.getSelection() || btnCruiseControl.getSelection()) {
			MessageBox messageDialog = new MessageBox(getShell(), SWT.OK);
			messageDialog.setText("Alert!!");
			messageDialog
					.setMessage("It is under construction. Please deselect this option to proceed further.");
			if (messageDialog.open() == SWT.OK) {
				return this;
			}
		} else if (btnNone.getSelection()) {
			return super.getNextPage();
		}

		return this;
	}
}
