package com.hexaware.jsoftwarefactory.plugin.wizards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

import com.hexaware.framework.config.ConfigHandler;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CustomProjectNewWizard extends Wizard implements INewWizard,
		IExecutableExtension, IProjectNature{

	static Logger log = LogFactory.getLogger("CustomProjectNewWizard");

	SplashScreenWizard splashScreenWizard;
	WizardNewProjectCreationPage newProjectWizard;
	PresentationFrameworkSelectorWizard presentationFrameworkSelectorWizard;
	ApplicationFrameworkSelectorWizard applicationFrameworkSelectorWizard;
	PersistenceFrameworkSelectorWizard persistenceFrameworkSelectorWizard;
	DataBaseSelectorWizard dataBaseSelectorWizard;
	CrosscuttingConcernsSelectorWizard crosscuttingConcernsSelectorWizard;
	SourceControlSelectorWizard sourceControlSelectorWizard;
	BuildToolSelectorWizard buildToolSelectorWizard;
	ContinuousIntegrationSelectorWizard continuousIntegrationSelectorWizard;
	ReferenceImplementationWizard referenceImplementationWizard;

	protected IStatus status;
	private IConfigurationElement _configurationElement;
	IProject project;
	IProjectDescription projDesc;
	IProgressMonitor monitor;
	URI location;
	private String projectName = "";
	private BufferedReader bufferedReader;

	public CustomProjectNewWizard() {
		setWindowTitle("New HexJava Software Factory Project");
	}

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {

	}

	@Override
	public void setInitializationData(IConfigurationElement arg0, String arg1,
			Object arg2) throws CoreException {
	}

	@Override
	public void addPages() {
		super.addPages();

		splashScreenWizard = new SplashScreenWizard("JSoftware Factory");
		splashScreenWizard.setTitle("Welcome to the HexJava Software Factory");
		addPage(splashScreenWizard);
		getNextPage(splashScreenWizard);

		newProjectWizard = new WizardNewProjectCreationPage("JSoftware Factory");
		newProjectWizard.setTitle("Project Workspace Configuration");
		newProjectWizard.setDescription("Enter Project Configuration Details.");
		addPage(newProjectWizard);
		getNextPage(newProjectWizard);

		presentationFrameworkSelectorWizard = new PresentationFrameworkSelectorWizard(
				"JSoftware Factory");
		presentationFrameworkSelectorWizard.setTitle("Presentation Layer");
		presentationFrameworkSelectorWizard
				.setDescription("Select the applicaton type and framework below.");
		addPage(presentationFrameworkSelectorWizard);
		getNextPage(presentationFrameworkSelectorWizard);

		applicationFrameworkSelectorWizard = new ApplicationFrameworkSelectorWizard(
				"JSoftware factory");
		applicationFrameworkSelectorWizard.setTitle("Application Layer");
		applicationFrameworkSelectorWizard
				.setDescription("Select the application framework and service layer for your application.");
		addPage(applicationFrameworkSelectorWizard);
		getNextPage(applicationFrameworkSelectorWizard);

		persistenceFrameworkSelectorWizard = new PersistenceFrameworkSelectorWizard(
				"JSoftware factory");
		persistenceFrameworkSelectorWizard.setTitle("Persistence Layer");
		persistenceFrameworkSelectorWizard
				.setDescription("Select the persistence framework for your application.");
		addPage(persistenceFrameworkSelectorWizard);
		getNextPage(persistenceFrameworkSelectorWizard);

		dataBaseSelectorWizard = new DataBaseSelectorWizard("JSoftware factory");
		dataBaseSelectorWizard.setTitle("Database Layer");
		dataBaseSelectorWizard
				.setDescription("Select the database server for your application.");
		addPage(dataBaseSelectorWizard);
		getNextPage(dataBaseSelectorWizard);

		crosscuttingConcernsSelectorWizard = new CrosscuttingConcernsSelectorWizard(
				"JSoftware factory");
		crosscuttingConcernsSelectorWizard.setTitle("Crosscutting Features");
		crosscuttingConcernsSelectorWizard
				.setDescription("Select the crosscutting features for your application.");
		addPage(crosscuttingConcernsSelectorWizard);
		getNextPage(crosscuttingConcernsSelectorWizard);

		sourceControlSelectorWizard = new SourceControlSelectorWizard(
				"JSoftware factory");
		sourceControlSelectorWizard.setTitle("Source Control");
		sourceControlSelectorWizard
				.setDescription("Select the source control tool for your application.");
		addPage(sourceControlSelectorWizard);
		getNextPage(sourceControlSelectorWizard);

		buildToolSelectorWizard = new BuildToolSelectorWizard(
				"JSoftware factory");
		buildToolSelectorWizard.setTitle("Build Tool");
		buildToolSelectorWizard
				.setDescription("Select the build tool for your application.");
		addPage(buildToolSelectorWizard);
		getNextPage(buildToolSelectorWizard);

		continuousIntegrationSelectorWizard = new ContinuousIntegrationSelectorWizard(
				"JSoftware factory");
		continuousIntegrationSelectorWizard
				.setTitle("Continuous Integration Tool");
		continuousIntegrationSelectorWizard
				.setDescription("Select the Continuous Integration (CI) tool for your application.");
		addPage(continuousIntegrationSelectorWizard);

		referenceImplementationWizard = new ReferenceImplementationWizard(
				"JSoftware factory");
		referenceImplementationWizard.setTitle("Reference Implementation");
		referenceImplementationWizard.setDescription("Select your required options to continue.");
		addPage(referenceImplementationWizard);

	}

	@Override
	public boolean performFinish() {
		log.info("Project creation starts..");
		try {
			projectName = newProjectWizard.getProjectName();
			final IProject projectHandle = newProjectWizard.getProjectHandle();
			URI projectURI = (!newProjectWizard.useDefaults()) ? newProjectWizard
					.getLocationURI() : null;
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			projDesc = workspace.newProjectDescription(projectHandle.getName());
			projDesc.setLocationURI(projectURI);
			createProject(projDesc, projectHandle);
			project = projectHandle;
			if (project == null) {
				return false;
			}
			addNatures();
			BasicNewProjectResourceWizard
					.updatePerspective(_configurationElement);
			IResource resour = ResourcesPlugin.getWorkspace().getRoot();

			resour.refreshLocal(IResource.DEPTH_INFINITE, null);
			log.info("Project creation ends..");
		} catch (CoreException e) {
			e.printStackTrace();
			log.error("Perform Finish is not exist" + e.getMessage());
		}
		return true;
	}

	public IStatus isValidName(String projectName) {

		for (int i = 0; i < projectName.length(); i++) {
			if (Character.isDigit(projectName.charAt(i))) {
				status = new IStatus() {

					@Override
					public boolean matches(int arg0) {
						return false;
					}

					@Override
					public boolean isOK() {
						return false;
					}

					@Override
					public boolean isMultiStatus() {
						return false;
					}

					@Override
					public int getSeverity() {
						return 0;
					}

					@Override
					public String getPlugin() {
						return null;
					}

					@Override
					public String getMessage() {
						return "Project name should not contain numbers";
					}

					@Override
					public Throwable getException() {
						return null;
					}

					@Override
					public int getCode() {
						return 0;
					}

					@Override
					public IStatus[] getChildren() {
						return null;
					}
				};

				return status;
			}
		}

		int dotLoc = projectName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = projectName.substring(dotLoc + 1);
			if (ext.length() > 0) {
				// setErrorMessage("No extension must be given to a Project
				// Name");
				// return false;
			}
		}
		return null;

	}

	public void addNatures() {
		try {

			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 5];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[newNatures.length - 5] = com.hexaware.jsoftwarefactory.natures.MyNature.NATURE_ID1;
			newNatures[newNatures.length - 4] = com.hexaware.jsoftwarefactory.natures.MyNature.NATURE_ID2;
			newNatures[newNatures.length - 3] = com.hexaware.jsoftwarefactory.natures.MyNature.NATURE_ID3;
			newNatures[newNatures.length - 2] = com.hexaware.jsoftwarefactory.natures.MyNature.NATURE_ID4;
			newNatures[newNatures.length - 1] = com.hexaware.jsoftwarefactory.natures.MyNature.NATURE_ID5;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		} catch (CoreException e) {
			e.printStackTrace();
			log.error("Natures is not added" + e.getMessage());
		}
	}

	void createProject(IProjectDescription description, IProject proj)
			throws CoreException, OperationCanceledException {
		try {
			proj.create(description, null);
			proj.open(IResource.BACKGROUND_REFRESH, null);

			IFacetedProject iFacetedProject = (IFacetedProject) ProjectFacetsManager
					.create(proj.getProject(), true, null);

			IProjectFacet JAVA_FACET = ProjectFacetsManager
					.getProjectFacet("jst.java");
			IProjectFacet WEB_FACET = ProjectFacetsManager
					.getProjectFacet("jst.web");

			iFacetedProject.installProjectFacet(JAVA_FACET.getVersion("1.7"),
					null, null);
			iFacetedProject.installProjectFacet(WEB_FACET.getVersion("3.0"),
					null, null);

			IContainer container = (IContainer) proj;

			createFolder(container, "src/com");
			createFolder(container, "src/com/app");
			createFolder(container, "src/com/app/framework");
			createFolder(container, "src/com/app/framework/caching");
			createFolder(container, "src/com/app/framework/exception");
			createFolder(container, "src/com/app/framework/notification");
			createFolder(container, "src/com/app/framework/config");

			createFolder(container, "src/com/app/customer");
			createFolder(container, "src/com/app/customer/vo");
			createFolder(container, "src/com/app/customer/util");
			createFolder(container, "src/com/app/customer/delegate");

			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			log.info("WORKSPACE : ", workspace.getRoot().getLocation()
					.toString());
			log.info("WORKSPACE : "
					+ workspace.getRoot().getLocation().toString());
			log.info("Application Name : " + projectName);
			String templatePath = ConfigHandler.getInstance()
					.getSystemProperty("template.location");// System.getProperty("template.location");
			log.info("Template Path : " + templatePath);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("projectName", projectName);
			data.put("importDateTimeFormat", "");
			data.put("dateTimeFormatAnnotation", "");
			data.put("restServiceConfiguration", "");
			data.put("springMVCConfiguration", "");
			data.put("JSFConfiguration", "");
			data.put("StrutsConfiguration", "");
			data.put("WelcomeFile", "");

			createFolder(container, "/WebContent/css");
			createFolder(container, "/WebContent/images");
			copyDirectory(templatePath + "/PresentationLayer/images", workspace
					.getRoot().getLocation().toString()
					+ "/" + projectName + "/WebContent/images");
			/*
			 * Angular JS Presentation Layer Code copying
			 */
			if (presentationFrameworkSelectorWizard.getComboWebAppFramework()
					.getText().equals("SPA-Angular JS")) {
				copyDirectory(templatePath + "/PresentationLayer/AngularJS",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/");
				data.put("WelcomeFile", "Customer Application.html");
			}

			/*
			 * Spring MVC Code Copying
			 */
			if (presentationFrameworkSelectorWizard.getComboWebAppFramework()
					.getText().equals("Spring")) {
				/*
				 * WebContent
				 */
				createFolder(container, "/WebContent/jsp");
				copyDirectory(templatePath
						+ "/PresentationLayer/Spring MVC/css", workspace
						.getRoot().getLocation().toString()
						+ "/" + projectName + "/WebContent/css");
				copyTemplate(
						templatePath
								+ "/PresentationLayer/Spring MVC/jsp/customerList.jsp.ftl",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName
								+ "/WebContent/jsp/customerList.jsp", data);
				copyTemplate(
						templatePath
								+ "/PresentationLayer/Spring MVC/jsp/customerAdd.jsp.ftl",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName
								+ "/WebContent/jsp/customerAdd.jsp", data);
				copyTemplate(
						templatePath
								+ "/PresentationLayer/Spring MVC/jsp/customerEdit.jsp.ftl",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName
								+ "/WebContent/jsp/customerEdit.jsp", data);
				copyTemplate(
						templatePath
								+ "/PresentationLayer/Spring MVC/WEB-INF/${projectName}-servlet.xml.ftl",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/WEB-INF/"
								+ projectName + "-servlet.xml", data);
				copyTemplate(templatePath
						+ "/PresentationLayer/Spring MVC/WEB-INF/web.xml.ftl",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/WEB-INF/web.xml",
						data);
				String filePath = workspace.getRoot().getLocation().toString()
						+ "/" + projectName + "/WebContent/WEB-INF/web.xml";
				StringBuilder value = loadDeploymentDescriptorData(filePath);
				data.put("springMVCConfiguration", value);

				/*
				 * Src
				 */
				createFolder(container, "src/com/app/customer/controller");
				copyDirectory(templatePath
						+ "/PresentationLayer/Spring MVC/src", workspace
						.getRoot().getLocation().toString()
						+ "/" + projectName + "/src/");
				data.put("importDateTimeFormat",
						"import org.springframework.format.annotation.DateTimeFormat;");
				data.put("dateTimeFormatAnnotation",
						"@DateTimeFormat(pattern = \"dd/MM/yyyy\")");
				copyTemplate(templatePath
						+ "/common/src/com/app/customer/vo/Customer.java.ftl",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName
								+ "/src/com/app/customer/vo/Customer.java",
						data);
				/*
				 * Copying the libraries
				 */
				copyDirectory(templatePath + "/lib/Spring/common", workspace
						.getRoot().getLocation().toString()
						+ "/" + projectName + "/WebContent/WEB-INF/lib/");
				copyDirectory(templatePath + "/lib/Spring/MVC", workspace
						.getRoot().getLocation().toString()
						+ "/" + projectName + "/WebContent/WEB-INF/lib/");
			}

			/*
			 * Application Layer - Spring IOC
			 */
			if (applicationFrameworkSelectorWizard.getBtnSpring()
					.getSelection()) {
				data.put(
						"customerBusinessDelegateObject",
						"(CustomerBusinessDelegate) BootStrapper.getService().getBean(\"CustomerDelegate\")");
				copyDirectory(templatePath + "/ApplicationLayer/Spring",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/src/");
				/*
				 * Copying the libraries
				 */
				copyDirectory(templatePath + "/lib/Spring/common", workspace
						.getRoot().getLocation().toString()
						+ "/" + projectName + "/WebContent/WEB-INF/lib/");

			} else {
				data.put("customerBusinessDelegateObject",
						"new CustomerBusinessDelegate()");
			}

			/*
			 * JSF
			 */
			if (presentationFrameworkSelectorWizard.getComboWebAppFramework()
					.getText().equals("JSF")) {
				/*
				 * WebContent
				 */
				copyDirectory(templatePath + "/PresentationLayer/Jsf/css",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/css/");
				copyDirectory(templatePath + "/PresentationLayer/images",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/images/");
				createFolder(container, "/WebContent/pages");
				copyDirectory(templatePath + "/PresentationLayer/Jsf/pages",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/pages/");
				copyFile(templatePath + "/PresentationLayer/Jsf/index.jsp",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/index.jsp");
				String filePath = templatePath
						+ "/PresentationLayer/Jsf/WEB-INF/web.xml";
				StringBuilder value = loadDeploymentDescriptorData(filePath);
				data.put("JSFConfiguration", value);
				data.put("WelcomeFile", "index.jsp");
				/*
				 * Src
				 */
				createFolder(container, "src/com/app/customer/mbean");
				copyTemplate(
						templatePath
								+ "/PresentationLayer/Jsf/src/com/app/customer/mbean/CustomerBean.java.ftl",
						workspace.getRoot().getLocation().toString()
								+ "/"
								+ projectName
								+ "/src/com/app/customer/mbean/CustomerBean.java",
						data);
				copyFile(
						templatePath
								+ "/PresentationLayer/Jsf/src/ApplicationResources.properties",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName
								+ "/src/ApplicationResources.properties");
				copyFile(
						templatePath
								+ "/PresentationLayer/Jsf/src/ApplicationResources_en.properties",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName
								+ "/src/ApplicationResources_en.properties");

				/*
				 * Copying the libraries
				 */
				copyDirectory(templatePath + "/lib/JSF", workspace.getRoot()
						.getLocation().toString()
						+ "/" + projectName + "/WebContent/WEB-INF/lib/");
			}

			/*
			 * Struts - Presentation Layer
			 */
			if (presentationFrameworkSelectorWizard.getComboWebAppFramework()
					.getText().equals("Struts")) {
				/*
				 * WebContent
				 */
				copyDirectory(templatePath + "/PresentationLayer/Struts/css",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/css/");
				copyDirectory(templatePath + "/PresentationLayer/images",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/images/");
				createFolder(container, "/WebContent/pages");
				copyDirectory(templatePath + "/PresentationLayer/Struts/pages",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/pages/");
				copyFile(templatePath + "/PresentationLayer/Struts/index.jsp",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/index.jsp");
				String filePath = templatePath
						+ "/PresentationLayer/Struts/WEB-INF/web.xml";
				StringBuilder value = loadDeploymentDescriptorData(filePath);
				data.put("WelcomeFile", "index.jsp");
				data.put("StrutsConfiguration", value);

				/*
				 * Src
				 */
				createFolder(container, "src/com/app/customer/mbean");
				copyTemplate(
						templatePath
								+ "/PresentationLayer/Struts/src/com/app/customer/mbean/CustomerBean.ftl",
						workspace.getRoot().getLocation().toString()
								+ "/"
								+ projectName
								+ "/src/com/app/customer/mbean/CustomerBean.java",
						data);
				copyFile(
						templatePath
								+ "/PresentationLayer/Struts/src/ApplicationResources.properties",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName
								+ "/src/ApplicationResources.properties");
				copyFile(
						templatePath
								+ "/PresentationLayer/Struts/src/ApplicationResources_en.properties",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName
								+ "/src/ApplicationResources_en.properties");
				copyFile(templatePath
						+ "/PresentationLayer/Struts/src/struts.xml", workspace
						.getRoot().getLocation().toString()
						+ "/" + projectName + "/src/struts.xml");

				/*
				 * Copying the libraries
				 */
				copyDirectory(templatePath + "/lib/Struts", workspace.getRoot()
						.getLocation().toString()
						+ "/" + projectName + "/WebContent/WEB-INF/lib/");
			}

			/*
			 * Application Layer - Rest Service
			 */

			if (applicationFrameworkSelectorWizard.getBtnRestService()
					.getSelection()) {
				createFolder(container, "src/com/app/customer/rest");
				createFolder(container, "src/com/app/customer/rest/service");
				createFolder(container, "/WebContent/js");

				/*
				 * Copying script.js with rest service if Rest Service is
				 * enabled
				 */

				copyTemplate(templatePath
						+ "/PresentationLayer/RestService/js/script.js.ftl",
						workspace.getRoot().getLocation().toString() + "/"
								+ projectName + "/WebContent/js/script.js",
						data);
				copyTemplate(
						templatePath
								+ "/ApplicationLayer/RestService/src/com/app/customer/rest/service/DaoService.ftl",
						workspace.getRoot().getLocation().toString()
								+ "/"
								+ projectName
								+ "/src/com/app/customer/rest/service/DaoService.java",
						data);
				String filePath = templatePath
						+ "/ApplicationLayer/RestService/web.xml";
				StringBuilder value = loadDeploymentDescriptorData(filePath);
				data.put("restServiceConfiguration", value);

				/*
				 * Copying the libraries
				 */
				copyDirectory(templatePath + "/lib/Rest Service", workspace
						.getRoot().getLocation().toString()
						+ "/" + projectName + "/WebContent/WEB-INF/lib/");
			}

			/*
			 * Data Layer - Hibernate
			 */
			if (persistenceFrameworkSelectorWizard.getBtnHibernate()
					.getSelection()) {
				copyDirectory(templatePath + "/DataLayer/Hibernate", workspace
						.getRoot().getLocation().toString()
						+ "/" + projectName + "/src/");
				/*
				 * Copying the libraries
				 */
				copyDirectory(templatePath + "/lib/Hibernate", workspace
						.getRoot().getLocation().toString()
						+ "/" + projectName + "/WebContent/WEB-INF/lib/");
			}

			/*
			 * Copying common libraries
			 */
			copyDirectory(templatePath + "/lib/common", workspace.getRoot()
					.getLocation().toString()
					+ "/" + projectName + "/WebContent/WEB-INF/lib/");

			/*
			 * Copying common Files
			 */
			copyTemplate(templatePath
					+ "/common/src/com/app/customer/vo/Customer.java.ftl",
					workspace.getRoot().getLocation().toString() + "/"
							+ projectName
							+ "/src/com/app/customer/vo/Customer.java", data);
			copyDirectory(templatePath + "/common/src/com/app/framework",
					workspace.getRoot().getLocation().toString() + "/"
							+ projectName + "/src/com/app/framework");

			/*
			 * Copying web.xml file.
			 */
			String xmlTemplateFile = templatePath + "/common/web.xml.ftl";
			String outputFile = workspace.getRoot().getLocation().toString()
					+ "/" + projectName + "/WebContent/WEB-INF/web.xml";
			System.out.println("----===>>>"
					+ data.get("springMVCConfiguration"));
			copyTemplate(xmlTemplateFile, outputFile, data);

		} catch (Exception ioe) {
			ioe.printStackTrace();
			log.error("Project is not created" + ioe.getMessage());
			IStatus status = new Status(IStatus.ERROR, "JavaSoftwareFactory",
					IStatus.OK, ioe.getLocalizedMessage(), ioe);
			throw new CoreException(status);
		}
	}

	private StringBuilder loadDeploymentDescriptorData(String filePath)
			throws IOException {
		File file = new File(filePath);
		Reader reader = new FileReader(file);
		bufferedReader = new BufferedReader(reader);
		String line = null;
		StringBuilder value = new StringBuilder();
		while ((line = bufferedReader.readLine()) != null) {
			value.append(line + "\n");
		}

		return value;
	}

	private void copyTemplate(String templateFile, String outputFile,
			Object data) throws IOException, TemplateException {
		log.info("copyTemplate method starts");
		Configuration cfg = new Configuration();
		File tempFile = new File(templateFile);
		File templateDir = tempFile.getParentFile();
		if (null == templateDir) {
			templateDir = new File("./");
		}
		cfg.setDirectoryForTemplateLoading(templateDir);
		Template template = cfg.getTemplate(tempFile.getName());

		Writer file = new FileWriter(new File(outputFile));
		template.process(data, file);
		file.flush();
		file.close();
		log.info("copyTemplate method ends");
	}

	@Override
	public boolean canFinish() {
		if (getContainer().getCurrentPage() == referenceImplementationWizard) {
			return true;
		} else {
			return false;
		}
	}

	private void createFolder(IContainer container, String srcFolder)
			throws CoreException {
		final IFolder style1 = container.getFolder(new Path(srcFolder));
		if (!style1.exists()) {
			style1.create(true, true, monitor);
		}
	}

	private void copyDirectory(String srcFile, String destFile)
			throws IOException, CoreException {
		log.info("Inside writeOther file----->");
		log.info("SCR FILE : " + srcFile);
		log.info("DEST FILE : " + destFile);
		File srcDir = new File(srcFile);
		File destDir = new File(destFile);
		try {
			FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Copy directory is failed", e.getMessage());
		}
	}

	private void copyFile(String srcFile, String destFile) throws IOException,
			CoreException {
		log.info("Inside writeOther file----->");
		log.info("SCR FILE : " + srcFile);
		log.info("DEST FILE : " + destFile);
		File srcDir = new File(srcFile);
		File destDir = new File(destFile);
		try {
			FileUtils.copyFile(srcDir, destDir);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Copy directory is failed", e.getMessage());
		}
	}

	public static String replaceTags(String psSource, String psTag, String psVar) {
		if (psSource.indexOf(psTag) >= 0) {
			psSource = psSource.replaceAll(psTag, psVar);
		}
		return psSource;
	}

	public static void writeContent(String content, String fileName) {
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			out.write(content.getBytes("restServiceMappings"));
			out.close();
		} catch (IOException exception) {
			exception.printStackTrace();
			log.error("Content in not written", exception.getMessage());
		}
	}

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
