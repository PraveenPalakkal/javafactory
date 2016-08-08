package com.hexaware.jsoftwarefactory.plugin.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MobilityEnablerWizard extends WizardPage {

	/**
	 * Create the wizard.
	 */
	public MobilityEnablerWizard(String pageName) {
		super(pageName);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Label lblWouldYouLike = new Label(container, SWT.NONE);
		lblWouldYouLike.setBounds(40, 21, 450, 15);
		lblWouldYouLike.setText("Would you like to enable support for Android ?");
		
		Button btnYes = new Button(container, SWT.CHECK);
		btnYes.setBounds(40, 54, 93, 16);
		btnYes.setText("Yes");
		
		/*Button btnEnableAndroidNative = new Button(container, SWT.CHECK);
		btnEnableAndroidNative.setEnabled(false);
		btnEnableAndroidNative.setBounds(105, 78, 385, 16);
		btnEnableAndroidNative.setText("Enable Android Native App support using Xamarin");*/
		
		Button btnEnableAndroidHybrid = new Button(container, SWT.CHECK);
		btnEnableAndroidHybrid.setEnabled(false);
		//btnEnableAndroidHybrid.setBounds(105, 107, 385, 16);
		btnEnableAndroidHybrid.setBounds(105, 78, 385, 16);
		btnEnableAndroidHybrid.setText("Enable Android Hybrid App support using Cordova");
		
		Label lblWouldYouLike_1 = new Label(container, SWT.NONE);
		lblWouldYouLike_1.setBounds(40, 149, 450, 15);
		lblWouldYouLike_1.setText("Would you like to enable support for iOS ?");
		
		Button btnYes_1 = new Button(container, SWT.CHECK);
		btnYes_1.setBounds(40, 183, 93, 16);
		btnYes_1.setText("Yes");
		
		/*Button btnEnableIosNative = new Button(container, SWT.CHECK);
		btnEnableIosNative.setEnabled(false);
		btnEnableIosNative.setBounds(105, 205, 385, 16);
		btnEnableIosNative.setText("Enable iOS Native App support using Xamarin");*/
		
		Button btnEnableIosHybrid = new Button(container, SWT.CHECK);
		btnEnableIosHybrid.setEnabled(false);
		//btnEnableIosHybrid.setBounds(105, 230, 385, 16);
		btnEnableIosHybrid.setBounds(105, 205, 385, 16);
		btnEnableIosHybrid.setText("Enable iOS Hybrid App support using Cordova");
	}
	/*
	@Override
	public IWizardPage getNextPage() {
		return super.getNextPage();
	}*/
}
