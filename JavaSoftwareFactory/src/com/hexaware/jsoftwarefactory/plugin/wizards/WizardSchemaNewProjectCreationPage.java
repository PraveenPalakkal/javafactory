package com.hexaware.jsoftwarefactory.plugin.wizards;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class WizardSchemaNewProjectCreationPage extends WizardNewProjectCreationPage {

	@Override
	public String getProjectName() {
		// TODO Auto-generated method stub
		return super.getProjectName();
	}

	public WizardSchemaNewProjectCreationPage(String pageName) {
		super(pageName);
//		setImageDescriptor(ResourceManager.getImageDescriptor(GeneratorUtility.getSourceParentPath()+ "icons/workquikrdescriptor.png"));
	}

	@Override
	protected boolean validatePage() {
		//CustomProjectNewWizards cs = new CustomProjectNewWizards();
		if (super.validatePage() == true) {
			// Give a chance to the wizard to do its own validation
			//IStatus validName = ((CustomProjectNewWizard) getWizard()).isValidName(getProjectName());//cs.isValidName(getProjectName());
			/*
			if (!validName.isOK()) {
				//setErrorMessage(validName.getMessage());
				return false;
			}*/
			// Give a chance to the wizard to do its own validation
			/*IStatus validLocation = ((CustomProjectNewWizards) getWizard()).isValidLocation(getLocationPath().toOSString());
			if (!validLocation.isOK()) {
				setErrorMessage(validLocation.getMessage());
				return false;
			}*/
			return true;
		}
		return false;
	}
	
	
}
