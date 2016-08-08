package com.hexaware.jsoftwarefactory.plugin.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ConnectionDetailsDialog extends Dialog {
	private Text text;
	private Text text_1;
	private Text text_2;
	private char pass_char = '*';

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ConnectionDetailsDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.TITLE);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);
		container.getShell().setText("Connection Details");
		
		Label lblNameOfUrl = new Label(container, SWT.NONE);
		lblNameOfUrl.setBounds(20, 40, 70, 15);
		lblNameOfUrl.setText("Server URL*  :");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(96, 31, 385, 27);
		
		Label lblPath = new Label(container, SWT.NONE);
		lblPath.setBounds(136, 96, 96, 18);
		lblPath.setText("User Name*  :");
		
		Label lblPortNo = new Label(container, SWT.NONE);
		lblPortNo.setBounds(136, 157, 96, 18);
		lblPortNo.setText("Password* : ");
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(238, 93, 145, 27);
		
		text_2 = new Text(container, SWT.BORDER);
		text_2.setBounds(238, 154, 145, 27);
		text_2.setEchoChar(pass_char);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(512, 291);
	}

}
