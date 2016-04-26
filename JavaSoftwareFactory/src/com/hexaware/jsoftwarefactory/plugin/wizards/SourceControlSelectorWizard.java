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
import com.hexaware.jsoftwarefactory.plugin.dialogs.ConnectionDetailsDialog;
import org.eclipse.swt.widgets.Group;

public class SourceControlSelectorWizard extends WizardPage {

	Logger log = LogFactory.getLogger("SourceControlSelectorWizard");
	private Button btnGithub;
	private Button btnStarteam;
	private Button btnSvn;
	private Group grpSourceControlTool;
	private Button btnNone;

	/**
	 * Create the wizard.
	 */
	public SourceControlSelectorWizard(String pageName) {
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

		grpSourceControlTool = new Group(container, SWT.NONE);
		grpSourceControlTool.setText("Source Control Tool");
		grpSourceControlTool.setBounds(136, 66, 237, 191);

		btnGithub = new Button(grpSourceControlTool, SWT.RADIO);
		btnGithub.setBounds(52, 78, 147, 16);
		btnGithub.setText("GitHub");

		btnStarteam = new Button(grpSourceControlTool, SWT.RADIO);
		btnStarteam.setBounds(52, 113, 147, 16);
		btnStarteam.setText("StarTeam");

		btnSvn = new Button(grpSourceControlTool, SWT.RADIO);
		btnSvn.setBounds(52, 151, 147, 16);
		btnSvn.setText("SVN");
		
		btnNone = new Button(grpSourceControlTool, SWT.RADIO);
		btnNone.setSelection(true);
		btnNone.setBounds(52, 42, 90, 16);
		btnNone.setText("None");

		btnSvn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnSvn.getSelection()) {
					ConnectionDetailsDialog con = new ConnectionDetailsDialog(
							getShell());
					int invokeDialog = con.open();
					if (invokeDialog == 1)
						System.out
								.println("Connection Settings cancelled by the user ");
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		btnStarteam.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnStarteam.getSelection()) {
					ConnectionDetailsDialog con = new ConnectionDetailsDialog(
							getShell());
					int invokeDialog = con.open();
					if (invokeDialog == 1)
						System.out
								.println("Connection Settings cancelled by the user ");
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		btnGithub.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnGithub.getSelection()) {
					ConnectionDetailsDialog con = new ConnectionDetailsDialog(
							getShell());
					int invokeDialog = con.open();
					if (invokeDialog == 1)
						System.out
								.println("Connection Settings cancelled by the user ");
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	@Override
	public IWizardPage getNextPage() {
		log.info("\nInside getnext page of SourceControlSelectorWizard");
		if (btnGithub.getSelection() || btnStarteam.getSelection()|| btnSvn.getSelection()) {
			MessageBox messageDialog = new MessageBox(getShell(), SWT.OK);
			messageDialog.setText("Alert!!");
			messageDialog
					.setMessage("It is under construction. Please deselect this option to proceed further.");
			if (messageDialog.open() == SWT.OK) {
				return this;
			}
		} else if (btnNone.getSelection() == true) {
			return super.getNextPage();
		}

		return this;
	}
}
