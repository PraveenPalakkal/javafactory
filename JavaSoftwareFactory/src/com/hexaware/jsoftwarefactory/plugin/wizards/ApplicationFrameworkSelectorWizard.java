package com.hexaware.jsoftwarefactory.plugin.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Group;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

public class ApplicationFrameworkSelectorWizard extends WizardPage {

	Logger log = LogFactory.getLogger("ApplicationFrameworkSelectorWizard");

	private Button btnEjb;
	private Button btnSpring;
	private Button btnSoapService;
	private Button btnRestService;
	private Group grpApplicationFramework;
	private Group grpServiceLayer;

	public Button getBtnEjb() {
		return btnEjb;
	}

	public Button getBtnSpring() {
		return btnSpring;
	}

	public Button getBtnSoapService() {
		return btnSoapService;
	}

	public Button getBtnRestService() {
		return btnRestService;
	}

	/**
	 * Create the wizard.
	 */

	public ApplicationFrameworkSelectorWizard(String pageName) {
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

		grpApplicationFramework = new Group(container, SWT.NONE);
		grpApplicationFramework.setText("Application Framework");
		grpApplicationFramework.setBounds(47, 91, 195, 148);

		btnSpring = new Button(grpApplicationFramework, SWT.RADIO);
		btnSpring.setBounds(43, 44, 138, 16);
		btnSpring.setText("Spring");
		btnSpring.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getContainer().updateButtons();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnEjb = new Button(grpApplicationFramework, SWT.RADIO);
		btnEjb.setBounds(43, 89, 138, 16);
		btnEjb.setEnabled(true);
		btnEjb.setText("EJB");
		btnEjb.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnRestService.setSelection(false);
				getContainer().updateButtons();
				/*if (btnEjb.getSelection() == true) {
					log.debug("BtnEjb selection" + btnEjb.getSelection());
					MessageBox messageDialog = new MessageBox(getShell(),
							SWT.OK);
					messageDialog.setText("Alert!!");
					messageDialog
							.setMessage("It is under construction. Please select any other framework");
					if (messageDialog.open() == SWT.OK) {
						return;
					}
				}*/
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		grpServiceLayer = new Group(container, SWT.NONE);
		grpServiceLayer.setText("Service Layer(s)");
		grpServiceLayer.setBounds(268, 91, 195, 148);

		btnSoapService = new Button(grpServiceLayer, SWT.CHECK);
		btnSoapService.setBounds(47, 43, 129, 16);
		btnSoapService.setEnabled(true);
		btnSoapService
				.setText("SOAP");
		btnSoapService.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (btnSoapService.getSelection() == true) {
					MessageBox messageDialog = new MessageBox(getShell(),
							SWT.OK);
					messageDialog.setText("Alert!!");
					messageDialog
							.setMessage("It is under construction. Please deselect SOAP service to proceed further.");
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

		btnRestService = new Button(grpServiceLayer, SWT.CHECK);
		btnRestService.setBounds(47, 88, 129, 16);
		btnRestService.setText("REST");
		//btnRestService.setSelection(true);
		/*btnRestService.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (btnRestService.getSelection() == false) {
					MessageBox messageDialog = new MessageBox(getShell(),
							SWT.OK);
					messageDialog.setText("Alert!!");
					messageDialog.setMessage("Please select REST service");
					if (messageDialog.open() == SWT.OK) {
						return;
					}
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});*/
	}

	@Override
	public IWizardPage getNextPage() {
		log.info("Inside getnext page of ApplicationFrameworkSelectorWizard");
		/*if (btnSpring.getSelection()) {
			if(!btnRestService.getSelection())
			{
			MessageBox messageDialog = new MessageBox(getShell(),
					SWT.OK);
			messageDialog.setText("Alert!!");
			messageDialog.setMessage("Please select REST service");
			if (messageDialog.open() == SWT.OK) {
				return this;
			}
			}
			else
			{
				return super.getNextPage();
			}
		}
		else if ((btnSpring.getSelection()
				&& btnRestService.getSelection()) || btnEjb.getSelection()) {
			return super.getNextPage();
		}*/
		
		return super.getNextPage();
	}
}
