package com.hexaware.jsoftwarefactory.plugin.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.swtdesigner.SWTResourceManager;
import com.swtdesigner.ResourceManager;

public class SplashScreenWizard extends WizardPage {

	protected SplashScreenWizard(String pageName) {
		super(pageName);
		setTitle("");
		setDescription("");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		
		Label lblHexjava = new Label(container, SWT.NONE);
		lblHexjava.setImage(ResourceManager.getPluginImage("JavaSoftwareFactory", "icons/logo.png"));
		lblHexjava.setFont(SWTResourceManager.getFont("Segoe UI", 22, SWT.BOLD));
		lblHexjava.setBounds(170, 35, 175, 82);
		
		Label lblThisWizardWill = new Label(container, SWT.NONE);
		lblThisWizardWill.setBounds(61, 240, 382, 21);
		lblThisWizardWill.setText("This tool generates Java based solution framework for n-tier application  ");
		
		Label label = new Label(container, SWT.NONE);
		label.setImage(ResourceManager.getPluginImage("JavaSoftwareFactory", "icons/splashScreen.png"));
		label.setBounds(107, 110, 304, 124);
		
		Label lblWithBuiltinCrosscutting = new Label(container, SWT.NONE);
		lblWithBuiltinCrosscutting.setBounds(61, 261, 329, 15);
		lblWithBuiltinCrosscutting.setText("with built-in cross-cutting features.");
		
	}
}
