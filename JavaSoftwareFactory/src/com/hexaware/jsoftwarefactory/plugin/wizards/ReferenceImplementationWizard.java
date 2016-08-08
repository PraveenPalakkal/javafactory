package com.hexaware.jsoftwarefactory.plugin.wizards;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.MessageBox;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.widgets.Group;

public class ReferenceImplementationWizard extends WizardPage {

	Logger log = LogFactory.getLogger("ReferenceImplementationWizard");

	private Button btnYes;
	private Button btnNo;

	/**
	 * Create the wizard.
	 */

	protected ReferenceImplementationWizard(String pageName) {
		super(pageName);
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container
				.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		setControl(container);

		Group grpWouldYouLike = new Group(container, SWT.NONE);
		grpWouldYouLike
				.setText("Would you like to create a reference implementation?");
		grpWouldYouLike.setBounds(99, 125, 300, 97);

		btnYes = new Button(grpWouldYouLike, SWT.RADIO);
		btnYes.setBounds(62, 40, 90, 16);
		btnYes.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		btnYes.setText("Yes");
		btnYes.setSelection(true);

		btnNo = new Button(grpWouldYouLike, SWT.RADIO);
		btnNo.setBounds(180, 40, 90, 16);
		btnNo.setFont(SWTResourceManager.getFont("Segoe UI", 8, SWT.NORMAL));
		btnNo.setText("No");
		btnNo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (btnNo.getSelection()) {
					MessageBox messageDialog = new MessageBox(getShell(),
							SWT.OK);
					messageDialog.setText("Alert!!");
					messageDialog
							.setMessage("It is under construction. Please deselect this option to proceed further.");
					if (messageDialog.open() == SWT.OK) {
						btnNo.setSelection(false);
						btnYes.setSelection(true);
						return;
					}
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public IWizardPage getNextPage() {
		return super.getNextPage();
	}

}
