package com.hexaware.jsoftwarefactory.plugin.wizards;

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

public class PersistenceFrameworkSelectorWizard extends WizardPage {

	Logger log = LogFactory.getLogger("PersistenceFrameworkSelectorWizard");

	private Button btnHibernate;
	private Button btnJpa;

	public Button getBtnHibernate() {
		return btnHibernate;
	}

	public Button getBtnJpa() {
		return btnJpa;
	}

	/**
	 * Create the wizard.
	 */
	public PersistenceFrameworkSelectorWizard(String pageName) {
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
		Group grpPersistenceFramework = new Group(container, SWT.NONE);
		grpPersistenceFramework.setText("Persistence Framework");
		grpPersistenceFramework.setBounds(130, 101, 250, 144);

		btnJpa = new Button(grpPersistenceFramework, SWT.RADIO);
		btnJpa.setBounds(51, 41, 157, 16);
		btnJpa.setText("JPA");
		btnJpa.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				getContainer().updateButtons();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		btnHibernate = new Button(grpPersistenceFramework, SWT.RADIO);
		btnHibernate.setBounds(51, 88, 157, 16);
		btnHibernate.setText("Hibernate");
		btnHibernate.setSelection(true);
		btnHibernate.addSelectionListener(new SelectionListener() {

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
		log.info("Inside getnext page of ApplicationFrameworkSelectorWizard");
		if (btnHibernate.getSelection() || btnJpa.getSelection()) {
			return super.getNextPage();
		}

		return null;
	}
}
