package com.hexaware.jsoftwarefactory.natures;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class JavaProjectNature implements IProjectNature {
	public static final String NATURE_ID1 = "org.eclipse.wst.common.project.facet.core.nature";
	public static final String NATURE_ID2= "org.eclipse.jdt.core.javanature";

	@Override
	public void configure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProject getProject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProject(IProject arg0) {
		// TODO Auto-generated method stub

	}
}