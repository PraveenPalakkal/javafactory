package com.hexaware.jsoftwarefactory.plugin.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Group;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

public class PresentationFrameworkSelectorWizard extends WizardPage {

	Logger log = LogFactory.getLogger("PresentationFrameworkSelectorWizard");
	private Combo comboWebAppFramework;
	private Combo comboDesktopAppFramework;
	private Combo comboMobileAppFramework;
	private Button btnWebApplication;
	private Button btnMobileApplication;
	private Button btnDesktopApplication;
	
	/*
	 * Getter and setter for components.
	 */
	public Button getBtnDesktopApplication() {
		return btnDesktopApplication;
	}

	public Button getBtnWebApplication() {
		return btnWebApplication;
	}

	public void setBtnWebApplication(Button btnWebApplication) {
		this.btnWebApplication = btnWebApplication;
	}

	public Button getBtnMobileApplication() {
		return btnMobileApplication;
	}

	public void setBtnMobileApplication(Button btnMobileApplication) {
		this.btnMobileApplication = btnMobileApplication;
	}

	public Combo getComboWebAppFramework() {
		return comboWebAppFramework;
	}

	public Combo getComboDesktopAppFramework() {
		return comboDesktopAppFramework;
	}

	public Combo getComboMobileAppFramework() {
		return comboMobileAppFramework;
	}

	/**
	 * Create the wizard.
	 */
	public PresentationFrameworkSelectorWizard(String pageName) {
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
		Group grpApplicationType = new Group(container, SWT.NONE);
		grpApplicationType.setText("Application Type");
		grpApplicationType.setBounds(38, 71, 424, 185);

		btnWebApplication = new Button(grpApplicationType,
				SWT.RADIO);
		btnWebApplication.setBounds(39, 42, 140, 24);
		btnWebApplication.setEnabled(true);
		btnWebApplication.setSelection(true);
		btnWebApplication.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comboDesktopAppFramework.setText("Select");
				comboMobileAppFramework.setText("Select");
			}
		});
		btnWebApplication.setText("Web Application");

		comboWebAppFramework = new Combo(grpApplicationType, SWT.NONE);
		comboWebAppFramework.setBounds(231, 44, 150, 30);
		comboWebAppFramework.setItems(new String[] { "JSF", "Spring", "Struts",
				"SPA-Angular JS" });
		comboWebAppFramework.setText("Select");
		comboWebAppFramework.setEnabled(true);
		comboWebAppFramework.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getContainer().updateButtons();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnDesktopApplication = new Button(grpApplicationType,
				SWT.RADIO);
		btnDesktopApplication.setBounds(39, 94, 140, 16);
		btnDesktopApplication.setEnabled(true);
		btnDesktopApplication.setText("Desktop Application");
		btnDesktopApplication.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				comboWebAppFramework.setText("Select");
				comboMobileAppFramework.setText("Select");

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		comboDesktopAppFramework = new Combo(grpApplicationType, SWT.NONE);
		comboDesktopAppFramework.setBounds(231, 92, 150, 23);
		comboDesktopAppFramework.setItems(new String[] { "Java FX",
				"Java Swing" });
		comboDesktopAppFramework.setText("Select");
		comboDesktopAppFramework.setEnabled(false);
		comboDesktopAppFramework.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getContainer().updateButtons();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});


		btnMobileApplication = new Button(grpApplicationType,
				SWT.RADIO);
		btnMobileApplication.setBounds(39, 139, 140, 16);
		btnMobileApplication.setText("Mobile Application");
		btnMobileApplication.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				comboWebAppFramework.setText("Select");
				comboDesktopAppFramework.setText("Select");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		comboMobileAppFramework = new Combo(grpApplicationType, SWT.NONE);
		comboMobileAppFramework.setBounds(231, 137, 150, 23);
		comboMobileAppFramework.setItems(new String[] { "Android", "iOS",
				"SPA-Angular JS" });
		comboMobileAppFramework.setText("Select");
		comboMobileAppFramework.setEnabled(false);

		btnMobileApplication.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnMobileApplication.getSelection()) {
					comboMobileAppFramework.setEnabled(true);
				} else {
					comboMobileAppFramework.setEnabled(false);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		btnDesktopApplication.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnDesktopApplication.getSelection()) {
					comboDesktopAppFramework.setEnabled(true);
				} else {
					comboDesktopAppFramework.setEnabled(false);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		btnWebApplication.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnWebApplication.getSelection()) {

					comboWebAppFramework.setEnabled(true);
				} else {
					comboWebAppFramework.setEnabled(false);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

	}

	@Override
	public IWizardPage getNextPage() {
		log.info("\nInside getnext page of PresentationTypeSelectorWizard");
		log.debug("fm-->" + comboWebAppFramework.getText());
		if (comboMobileAppFramework.getText().equals("Android")
				|| comboMobileAppFramework.getText().equals("iOS")
				|| comboMobileAppFramework.getText().equals("SPA-Angular JS")) {
			log.debug("Inside getnext" + comboMobileAppFramework.getText());
			MessageBox messageDialog = new MessageBox(getShell(), SWT.OK);
			messageDialog.setText("Alert!!");
			messageDialog
					.setMessage("It is under construction. Please select any other framework.");
			if (messageDialog.open() == SWT.OK) {
				return this;
			}
		} else if ((comboWebAppFramework.getText().equals("SPA-Angular JS")
				|| comboWebAppFramework.getText().equals("JSF")
				|| comboWebAppFramework.getText().equals("Spring")
				|| comboWebAppFramework.getText().equals("Struts") || comboDesktopAppFramework
				.getText().equals("Java FX") || comboDesktopAppFramework.getText().equals("Java Swing"))
				&& comboMobileAppFramework.getText().equals("Select")) {
			log.debug("Inside getnext" + comboWebAppFramework.getText()+comboDesktopAppFramework.getText());
			return super.getNextPage();
		}
		return null;
	}
}
