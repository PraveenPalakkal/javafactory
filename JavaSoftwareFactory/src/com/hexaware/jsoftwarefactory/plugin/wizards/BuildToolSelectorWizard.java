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
import com.swtdesigner.SWTResourceManager;

public class BuildToolSelectorWizard extends WizardPage {

	Logger log = LogFactory.getLogger("BuildToolSelectorWizard");

	private Button btnMaven;
	private Button btnAnt;

	/**
	 * Create the wizard.
	 */
	public BuildToolSelectorWizard(String pageName) {
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

		Group grpBuildTool = new Group(container, SWT.NONE);
		grpBuildTool.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		grpBuildTool.setText("Build Tool");
		grpBuildTool.setBounds(142, 85, 236, 154);

		btnMaven = new Button(grpBuildTool, SWT.RADIO);
		btnMaven.setBounds(47, 47, 90, 16);
		btnMaven.setEnabled(true);
		btnMaven.setText("Maven");
		btnMaven.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (btnMaven.getSelection()) {
					MessageBox messageDialog = new MessageBox(getShell(), SWT.OK);
					messageDialog.setText("Alert!!");
					messageDialog
							.setMessage("It is under construction. Please select other build tool.");
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

		btnAnt = new Button(grpBuildTool, SWT.RADIO);
		btnAnt.setBounds(47, 94, 90, 16);
		btnAnt.setEnabled(true);
		btnAnt.setSelection(true);
		btnAnt.setText("Ant");
		btnAnt.addSelectionListener(new SelectionListener() {

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
		log.info("\nInside getnext page of BuildToolSelectorWizard");
		if (btnAnt.getSelection()) {
			return super.getNextPage();
		}

		return null;
	}

}
