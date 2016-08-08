package com.hexaware.jsoftwarefactory.plugin.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CrosscuttingConcernsSelectorWizard extends WizardPage {

	Logger log = LogFactory.getLogger("CrosscuttingConcernsSelectorWizard");

	private Button btnNotification;
	private Button btnValidation;
	private Button btnCaching;
	private Button btnInstrumentation;
	private Button btnAuthenticationAutherization;

	public Logger getLog() {
		return log;
	}

	public Button getBtnNotification() {
		return btnNotification;
	}

	public Button getBtnValidation() {
		return btnValidation;
	}

	public Button getBtnCaching() {
		return btnCaching;
	}

	public Button getBtnInstrumentation() {
		return btnInstrumentation;
	}

	public Button getBtnAuthenticationAutherization() {
		return btnAuthenticationAutherization;
	}

	/**
	 * Create the wizard.
	 */
	public CrosscuttingConcernsSelectorWizard(String pageName) {
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

		Group grpDefaultFeatures = new Group(container, SWT.NONE);
		grpDefaultFeatures.setText("Default Features");
		grpDefaultFeatures.setBounds(32, 50, 191, 236);

		Button btnException = new Button(grpDefaultFeatures, SWT.CHECK);
		btnException.setEnabled(false);
		btnException.setSelection(true);
		btnException.setBounds(43, 46, 93, 16);
		btnException.setText("Exception");

		Button btnLogging = new Button(grpDefaultFeatures, SWT.CHECK);
		btnLogging.setEnabled(false);
		btnLogging.setSelection(true);
		btnLogging.setBounds(43, 86, 93, 16);
		btnLogging.setText("Logging");

		Group grpOptionalFeatures = new Group(container, SWT.NONE);
		grpOptionalFeatures.setText("Optional Features");
		grpOptionalFeatures.setBounds(253, 50, 212, 236);

		btnNotification = new Button(grpOptionalFeatures, SWT.CHECK);
		btnNotification.setEnabled(true);
		btnNotification.setBounds(48, 45, 141, 16);
		btnNotification.setText("Notification");

		btnValidation = new Button(grpOptionalFeatures, SWT.CHECK);
		btnValidation.setEnabled(true);
		btnValidation.setBounds(48, 82, 141, 16);
		btnValidation.setText("Validation");

		btnCaching = new Button(grpOptionalFeatures, SWT.CHECK);
		btnCaching.setEnabled(true);
		btnCaching.setBounds(48, 116, 141, 16);
		btnCaching.setText("Caching");

		btnAuthenticationAutherization = new Button(grpOptionalFeatures,
				SWT.CHECK);
		btnAuthenticationAutherization.setEnabled(true);
		btnAuthenticationAutherization.setBounds(48, 184, 13, 25);
		btnAuthenticationAutherization.setLayoutData("width 20:pref:pref");

		Label lblRoleBasedSecurity = new Label(grpOptionalFeatures, SWT.WRAP);
		lblRoleBasedSecurity.setBounds(67, 184, 139, 42);
		lblRoleBasedSecurity
				.setText("Role based Security with Forms Authentication and Authorization");

		btnInstrumentation = new Button(grpOptionalFeatures, SWT.CHECK);
		btnInstrumentation.setText("Instrumentation");
		btnInstrumentation.setBounds(48, 151, 141, 16);

	}

	@Override
	public IWizardPage getNextPage() {
		log.info("\nInside getnext page of CrosscuttingConcernsSelectorWizard");
		return super.getNextPage();
	}

}
