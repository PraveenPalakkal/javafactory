package com.hexaware.jsoftwarefactory.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
import com.hexaware.jsoftwarefactory.plugin.wizards.CustomProjectNewWizard;

public class ToolBarHandler extends AbstractHandler {
	private IStructuredSelection selection;
	static Logger log = LogFactory.getLogger("ToolBarHandler");

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		String commandName = "";
		// selection = (IStructuredSelection)
		// HandlerUtil.getCurrentSelection(event);
		// log.info("Selection is --------------- "+selection.toString());
		try {
			selection = (IStructuredSelection) HandlerUtil
					.getActiveWorkbenchWindow(event).getSelectionService()
					.getSelection();
			log.info("Selection is --------------- "
					+ selection.toString());
			commandName = event.getCommand().getName();
			System.out
					.println("Command Name is --------------- " + commandName);
			if (commandName.equalsIgnoreCase("JSoftwareFactory")) {
				WizardDialog dialog = new WizardDialog(null,
						new CustomProjectNewWizard());
				dialog.open();
			}
		} catch (NotDefinedException e) {
			// TODO Auto-generated catch block
			// logger.error("Event Not Defined "+e.getMessage());
			log.error("Event Not Defined");
			e.printStackTrace();
		}
		return null;
	}

}
