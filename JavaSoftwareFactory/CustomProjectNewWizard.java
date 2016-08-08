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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

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
import com.hexaware.jsoftwarefactory.plugin.dialogs.DatabaseConnectionDetailsDialog;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import sun.security.action.PutAllAction;

public class CustomProjectNewWizard extends Wizard implements INewWizard,
		IExecutableExtension, IProjectNature {

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
	DatabaseConnectionDetailsDialog databaseConnectionDetailsDialog;

	protected IStatus status;
	private IConfigurationElement _configurationElement;
	IProjectDescription projDesc;
	IProjectDescription javaProjDesc;
	IProgressMonitor monitor;
	URI location;
	private String webProjectName = "";
	private String earProjectName = "";
	private String javaProjectName = "";
	private String ejbProjectName = "";
	private String serviceProjectName = "";
	private BufferedReader bufferedReader;
	private String url;
	private String userName;
	private String password;

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
				.setDescription("Select the application type and framework below.");
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
		referenceImplementationWizard
				.setDescription("Select your required options to continue.");
		addPage(referenceImplementationWizard);

	}

	@Override
	public boolean performFinish() {
		log.info("Project creation starts..");
		try {
			url = dataBaseSelectorWizard.getDatabaseConnection().getUrl();
			userName = dataBaseSelectorWizard.getDatabaseConnection()
					.getUserName();
			password = dataBaseSelectorWizard.getDatabaseConnection()
					.getPassword();
			if (presentationFrameworkSelectorWizard.getBtnWebApplication()
					.getSelection()) {
				if (applicationFrameworkSelectorWizard.getBtnSpring()
						.getSelection()) {
					final IProject webProjectHandle = newProjectWizard
							.getProjectHandle();
					webProjectName = newProjectWizard.getProjectName();

					final IProject webServiceProjectHandle = ResourcesPlugin
							.getWorkspace()
							.getRoot()
							.getProject(
									newProjectWizard.getProjectName()
											+ "Service");
					serviceProjectName = webServiceProjectHandle.getProject()
							.getName();
					log.debug("Web service project name---->>>"
							+ serviceProjectName);
					createWebPresentationProject(webProjectHandle);
					createWebServiceProject(webServiceProjectHandle);
				} else if (applicationFrameworkSelectorWizard.getBtnEjb()
						.getSelection()) {
					createEnterpriseProject();
				}

			} else if (presentationFrameworkSelectorWizard
					.getBtnDesktopApplication().getSelection()) {

				if (applicationFrameworkSelectorWizard.getBtnSpring()
						.getSelection()) {
					final IProject javaProjectHandle = newProjectWizard
							.getProjectHandle();
					javaProjectName = newProjectWizard.getProjectName();

					final IProject WebProjectHandle = ResourcesPlugin
							.getWorkspace()
							.getRoot()
							.getProject(
									newProjectWizard.getProjectName()
											+ "Service");
					serviceProjectName = WebProjectHandle.getProject()
							.getName();
					log.debug("Web service project name---->>>"
							+ serviceProjectName);
					createJavaProject(javaProjectHandle);
					createWebServiceProject(WebProjectHandle);
				} else if (applicationFrameworkSelectorWizard.getBtnEjb()
						.getSelection()) {
					createEnterpriseProject();
				}

			}

		} catch (CoreException e) {
			e.printStackTrace();
			log.error("Perform Finish is not exist" + e.getMessage());
		}
		return true;
	}

	public IStatus isValidName(String webProjectName) {

		for (int i = 0; i < webProjectName.length(); i++) {
			if (Character.isDigit(webProjectName.charAt(i))) {
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

		int dotLoc = webProjectName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = webProjectName.substring(dotLoc + 1);
			if (ext.length() > 0) {
				// setErrorMessage("No extension must be given to a Project
				// Name");
				// return false;
			}
		}
		return null;

	}

	/*
	 * Dynamic Project Nature
	 */

	public void addWebNatures(IProject proj) {
		try {
			log.info("AddWebNature creation Starts");
			IProjectDescription description = proj.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 5];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[newNatures.length - 5] = com.hexaware.jsoftwarefactory.natures.MyNature.NATURE_ID1;
			newNatures[newNatures.length - 4] = com.hexaware.jsoftwarefactory.natures.MyNature.NATURE_ID2;
			newNatures[newNatures.length - 3] = com.hexaware.jsoftwarefactory.natures.MyNature.NATURE_ID3;
			newNatures[newNatures.length - 2] = com.hexaware.jsoftwarefactory.natures.MyNature.NATURE_ID4;
			newNatures[newNatures.length - 1] = com.hexaware.jsoftwarefactory.natures.MyNature.NATURE_ID5;
			description.setNatureIds(newNatures);
			proj.setDescription(description, null);

			System.out.println("AddWebNature creation ends");
		} catch (CoreException e) {
			e.printStackTrace();
			log.error("Natures is not added" + e.getMessage());
		}
	}

	/*
	 * Java Project Nature
	 */
	public void addJavaNatures(IProject proj) {
		try {
			log.info("AddJavaNature creation Starts");
			IProjectDescription javaDescription = proj.getDescription();
			String[] javaNatures = javaDescription.getNatureIds();
			String[] newJavaNatures = new String[javaNatures.length + 2];
			System.arraycopy(javaNatures, 0, newJavaNatures, 0,
					javaNatures.length);
			newJavaNatures[newJavaNatures.length - 2] = com.hexaware.jsoftwarefactory.natures.JavaProjectNature.NATURE_ID1;
			newJavaNatures[newJavaNatures.length - 1] = com.hexaware.jsoftwarefactory.natures.JavaProjectNature.NATURE_ID2;
			javaDescription.setNatureIds(newJavaNatures);
			proj.setDescription(javaDescription, null);

			System.out.println("AddJavaNature creation ends");
		} catch (CoreException e) {
			e.printStackTrace();
			log.error("Natures is not added" + e.getMessage());
		}
	}

	private void createJavaProject(IProject proj) throws CoreException {

		try {
			System.out.println("Inside Create Java Project");
			URI projectURI = (!newProjectWizard.useDefaults()) ? newProjectWizard
					.getLocationURI() : null;
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			if (proj == null) {
				throw new Exception("Application Exception : IProject is null");
			}
			javaProjDesc = workspace.newProjectDescription(proj.getName());
			javaProjDesc.setLocationURI(projectURI);
			proj.create(javaProjDesc, null);
			proj.open(IResource.BACKGROUND_REFRESH, null);

			IFacetedProject iFacetedProject = (IFacetedProject) ProjectFacetsManager
					.create(proj.getProject(), true, null);

			IProjectFacet JAVA_FACET = ProjectFacetsManager
					.getProjectFacet("jst.java");
			iFacetedProject.installProjectFacet(JAVA_FACET.getVersion("1.7"),
					null, monitor);

			addJavaNatures(proj);
			/*
			 * Generating Presentation Files from Template
			 */

			try {
				IContainer container = (IContainer) proj;

				createFolder(container, "src/com");
				createFolder(container, "src/com/app");
				createFolder(container, "src/com/app/customer");
				createFolder(container, "src/com/app/customer/vo");
				createFolder(container, "src/com/app/customer/controller");

				log.info("WORKSPACE : ", workspace.getRoot().getLocation()
						.toString());
				log.info("WORKSPACE : "
						+ workspace.getRoot().getLocation().toString());
				log.info("Application Name : " + javaProjectName);
				String templatePath = ConfigHandler.getInstance()
						.getSystemProperty("template.location");
				log.info("Template Path : " + templatePath);

				Map<String, Object> data = new HashMap<String, Object>();
				data.put("APP_NAME", "");
				data.put("projectName", javaProjectName);
				data.put("serviceProjectName", serviceProjectName);
				data.put("serverHost", "http://localhost:8080/");
				data.put("applicationName", javaProjectName);
				data.put("project", "${project}");
				data.put("springLib", "");
				data.put("hibernateLib", "");
				data.put("restLib", "");
				data.put("soapLib", "");
				data.put("EJB_Imports", "");
				data.put("Stateless", "");
				data.put("Remote", "");
				data.put("url", url);
				data.put("userName", userName);
				data.put("password", password);
				data.put("Driver", "");
				data.put("Dialect", "");
				data.put("springMvcLib", "");
				data.put("jsfLib", "");
				data.put("strutsLib", "");
				data.put("dataBaseLib", "");
				data.put("pojoLib", "");
				data.put("jpaLib", "");
				data.put("additionalLib", "");
				data.put("validationLib", "");
				data.put("cacheLib", "");
				data.put("notificationLib", "");
				data.put("instrumentationLib", "");
				data.put("jbossLib", "");

				data.put("Rest_URL", "rest");
				data.put("Service", "com.app.customer.service.CustomerImpl");
				data.put("webServiceReferer", "Service/");

				/*
				 * cross cutting
				 */
				data.put("Validation_Call", "");
				data.put("Validation_CatchEdit", "");
				data.put("Validation_CatchAdd", "");
				data.put("Validation_import", "");
				data.put("Cache_import", "");
				data.put("Cache_ImplStart", "");
				data.put("Cache_ImplEnd", "");
				data.put("Notification_method", "");
				data.put("Notification_Import", "");
				data.put("Email_Action", "");
				data.put("Email_button", "");
				data.put("AuthenticationAuthorization", "");
				data.put("Index_onload", "");
				data.put("Index_form", "");
				data.put("notemptyerror", "${not empty error}");
				data.put("error", "${error}");
				data.put("Jsf_Notification", "");
				data.put("Jsf_Notification_Import", "");
				data.put("Jsf_Notification_Object", "");
				data.put("CallValidation", "JndiLookup.doLookUp(\"Customer\")");
				data.put("idConvert", "");
				data.put("emailObjectCreation", "");
				data.put("emailButton", "");
				data.put("addButton", "");
				data.put("emailButtonAction", "");
				data.put("emailButtonInJavaFx", "");
				data.put("emailButtonCodeInJavaFx", "");
				data.put("emailCodeInJavaFx", "");

				/*
				 * copying common file
				 */

				/*
				 * Cross Cutting features - Validation
				 */
				if (crosscuttingConcernsSelectorWizard.getBtnValidation()
						.getSelection()) {
					/*
					 * Copying the libraries
					 */
					copyDirectory(templatePath + "/lib/Validation", workspace
							.getRoot().getLocation().toString()
							+ "/" + javaProjectName + "/lib/");
					String filePath = templatePath
							+ "/PresentationLayer/CommonClasspath/Validation.classpath";
					StringBuilder value = loadDeploymentDescriptorData(filePath);
					data.put("validationLib", value);
					data.put(
							"Validation_import",
							"import com.app.customer.validation.Validation; import com.hjsf.validator.HexValidationException;");
					data.put("Validation_Call",
							"new Validation().isCustomerValidate(customer);");
					data.put("Validation_CatchAdd",
							"catch (HexValidationException e) "
									+ "{ infoLabel.setText(e.getMessage()); "
									+ "return false;}");
					if (presentationFrameworkSelectorWizard
							.getComboDesktopAppFramework().getText()
							.equals("Java FX")) {
						data.put("idConvert",
								"Integer.toString(customer.getCustomerid())");
					} else {
						data.put("idConvert",
								"customer.getCustomerid().toString()");
					}
					createFolder(container, "src/com/app/customer/validation");
					copyTemplate(
							templatePath
									+ "/common/src/com/app/customer/validation/Validation.ftl",
							workspace.getRoot().getLocation().toString()
									+ "/"
									+ javaProjectName
									+ "/src/com/app/customer/validation/Validation.java/",
							data);

				}
				/*
				 * Cross Cutting features - Notification
				 */
				if (crosscuttingConcernsSelectorWizard.getBtnNotification()
						.getSelection()) {
					/*
					 * Copying the libraries - Notification
					 */
					copyDirectory(templatePath + "/lib/Notification", workspace
							.getRoot().getLocation().toString()
							+ "/" + javaProjectName + "/lib/");
					String filePath = templatePath
							+ "/PresentationLayer/CommonClasspath/Notification.classpath";
					StringBuilder value = loadDeploymentDescriptorData(filePath);
					data.put("notificationLib", value);
					data.put("emailObjectCreation",
							"EmailNotification emailNotification;");
					data.put("addButton", "btnAdd.setBounds(413, 406, 74, 30);");
					data.put("emailButtonAction", "dispose();"
							+ "emailNotification= new EmailNotification();"
							+ "emailNotification.setVisible(true);");
					data.put("emailButton",
							"btnEmail.setBounds(501, 406, 78, 30);");
					copyDirectory(templatePath + "/common/src/email", workspace
							.getRoot().getLocation().toString()
							+ "/" + javaProjectName + "/src/");

					/*
					 * Email button option for JavaFx
					 */
					data.put(
							"emailButtonCodeInJavaFx",
							"<Button id=\"email\" defaultButton=\"true\" layoutX=\"662.0\" layoutY=\"348.0\" mnemonicParsing=\"false\" onAction=\"#email\" prefHeight=\"39.0\" prefWidth=\"96.0001220703125\" style=\"\" text=\"Mail\">"
									+ "<font>"
									+ "<Font name=\"System Bold\" size=\"14.0\" fx:id=\"x1\" /> </font></Button>");

					if (presentationFrameworkSelectorWizard
							.getComboDesktopAppFramework().getText()
							.equals("Java Swing")) {
						copyFile(
								templatePath
										+ "/PresentationLayer/Swing/email/com/app/customer/controller/EmailNotification.java",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/controller/EmailNotification.java");
					}
					if (presentationFrameworkSelectorWizard
							.getComboDesktopAppFramework().getText()
							.equals("Java FX")) {
						copyFile(
								templatePath
										+ "/PresentationLayer/JavaFx/email/com/app/customer/controller/EmailNotification.java",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/controller/EmailNotification.java");
						String filePath1 = templatePath
								+ "/PresentationLayer/JavaFx/email/com/app/customer/controller/CustomerList.java";
						StringBuilder value1 = loadDeploymentDescriptorData(filePath1);
						data.put("emailCodeInJavaFx", value1);
						data.put("emailButtonInJavaFx",
								"@FXML private Button email;");
					}
				} else {
					data.put("addButton", "btnAdd.setBounds(501, 406, 74, 30);");
				}

				/*
				 * Cross Cutting features - Instrumentation
				 */
				if (crosscuttingConcernsSelectorWizard.getBtnInstrumentation()
						.getSelection()) {
					System.out
							.println("=================Cross Cutting Features - Instrumentation");
					copyDirectory(templatePath + "/lib/Instrumentation",
							workspace.getRoot().getLocation().toString() + "/"
									+ javaProjectName + "/lib/");

					String filePath = templatePath
							+ "/PresentationLayer/CommonClasspath/Instrumentation.classpath";
					StringBuilder value = loadDeploymentDescriptorData(filePath);
					data.put("instrumentationLib", value);

					copyFile(
							templatePath
									+ "/common/Instrumetation/Monitoring.java",
							workspace.getRoot().getLocation().toString()
									+ "/"
									+ javaProjectName
									+ "/src/com/jsf/instrumentaion/Monitoring.java");

					copyDirectory(templatePath + "/common/Instrumetation/aop/",
							workspace.getRoot().getLocation().toString() + "/"
									+ javaProjectName + "/WebContent/META-INF/");

				}

				/*
				 * Presentation Layer - JavaFx
				 */

				if (presentationFrameworkSelectorWizard
						.getComboDesktopAppFramework().getText()
						.equals("Java FX")) {

					/*
					 * SRC
					 */
					if (persistenceFrameworkSelectorWizard.getBtnJpa()
							.getSelection()) {
						if (!crosscuttingConcernsSelectorWizard
								.getBtnNotification().getSelection()) {
						copyDirectory(templatePath
								+ "/PresentationLayer/JavaFx/jpa/common/src",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName + "/src/");
						copyDirectory(templatePath
								+ "/PresentationLayer/JavaFx/jpa/common/view",
								workspace.getRoot().getLocation()
										.toString()
										+ "/" + javaProjectName + "/src/view");
						}
						else {
							copyDirectory(templatePath
									+ "/PresentationLayer/JavaFx/jpa/common/src",
									workspace.getRoot().getLocation()
											.toString()
											+ "/" + javaProjectName + "/src/");
							copyEmailFile(javaProjectName,templatePath, workspace, data, container);

						}
					} else if (applicationFrameworkSelectorWizard.getBtnEjb()
							.getSelection()
							&& persistenceFrameworkSelectorWizard
									.getBtnHibernate().getSelection()) {
						if (!crosscuttingConcernsSelectorWizard
								.getBtnNotification().getSelection()) {
						copyDirectory(templatePath
								+ "/PresentationLayer/JavaFx/ejb/common",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName + "/src/");
						copyDirectory(templatePath
								+ "/PresentationLayer/JavaFx/ejb/common/view",
								workspace.getRoot().getLocation()
										.toString()
										+ "/" + javaProjectName + "/src/view/");
						}
						else {
							copyDirectory(templatePath
									+ "/PresentationLayer/JavaFx/ejb/common/src",
									workspace.getRoot().getLocation()
											.toString()
											+ "/" + javaProjectName + "/src/");
							copyEmailFile(javaProjectName,templatePath, workspace, data, container);

						}
					} else {
						if (!crosscuttingConcernsSelectorWizard
								.getBtnNotification().getSelection()) {
							copyDirectory(templatePath
									+ "/PresentationLayer/JavaFx/common/src",
									workspace.getRoot().getLocation()
											.toString()
											+ "/" + javaProjectName + "/src/");
							copyDirectory(templatePath
									+ "/PresentationLayer/JavaFx/common/view",
									workspace.getRoot().getLocation()
											.toString()
											+ "/" + javaProjectName + "/src/view/");
						} else {
							copyDirectory(templatePath
									+ "/PresentationLayer/JavaFx/common/src",
									workspace.getRoot().getLocation()
											.toString()
											+ "/" + javaProjectName + "/src/");
							copyEmailFile(javaProjectName,templatePath, workspace, data, container);

						}
					}

					/*
					 * POJO
					 */

					if (applicationFrameworkSelectorWizard.getBtnSpring()
							.getSelection()
							|| applicationFrameworkSelectorWizard.getBtnEjb()
									.getSelection()
							&& (!applicationFrameworkSelectorWizard
									.getBtnRestService().getSelection() && !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection())) {
						/*
						 * Ejb With Pojo
						 */
						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {

							createFolder(container,
									"src/com/app/customer/service");
							data.put("Service",
									"com.app.customer.lookup.CustomerLookup");
							/*
							 * Copy Files
							 */
							copyEJBDesktopFiles(javaProjectName, templatePath,
									workspace, data, container);

							copyFile(
									templatePath
											+ "/ApplicationLayer/Ejb/Rest/com/app/customer/service/ICustomer.java",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ javaProjectName
											+ "/src/com/app/customer/service/ICustomer.java");
						}

						/*
						 * Libraries
						 */

						copyFile(templatePath + "/lib/common/antlr-2.7.5.jar",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName
										+ "/lib/antlr-2.7.5.jar");
						copyFile(templatePath
								+ "/lib/common/cglib-nodep-2.1_3.jar",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName
										+ "/lib/cglib-nodep-2.1_3.jar");
						copyFile(templatePath
								+ "/lib/common/commons-collections.jar",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName
										+ "/lib/commons-collections.jar");
						copyFile(templatePath
								+ "/lib/common/commons-logging.jar", workspace
								.getRoot().getLocation().toString()
								+ "/"
								+ javaProjectName
								+ "/lib/commons-logging.jar");
						copyFile(templatePath + "/lib/common/dom4j-1.6.jar",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName
										+ "/lib/dom4j-1.6.jar");

						String filePath1 = templatePath
								+ "/PresentationLayer/CommonClasspath/Pojo.classpath";
						StringBuilder value1 = loadDeploymentDescriptorData(filePath1);
						data.put("pojoLib", value1);

						if (dataBaseSelectorWizard.btnMysql.getSelection()) {
							copyDirectory(templatePath + "/lib/MySQL",
									workspace.getRoot().getLocation()
											.toString()
											+ "/" + javaProjectName + "/lib/");
							data.put(
									"dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/mysql-connector-java-5.1.15-bin.jar\"/>");
						} else if (dataBaseSelectorWizard.btnMicrosoftSqlServer
								.getSelection()) {
							copyDirectory(templatePath + "/lib/SQL", workspace
									.getRoot().getLocation().toString()
									+ "/" + javaProjectName + "/lib/");
							data.put("dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/sqljdbc4-4.0.jar\"/>");
						} else if (dataBaseSelectorWizard.getBtnOracle()
								.getSelection()) {
							copyDirectory(templatePath + "/lib/Oracle",
									workspace.getRoot().getLocation()
											.toString()
											+ "/" + javaProjectName + "/lib/");
							data.put("dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/ojdbc6.jar\"/>");
						}
						if (!persistenceFrameworkSelectorWizard.getBtnJpa()
								.getSelection()) {
							copyTemplate(
									templatePath
											+ "/PresentationLayer/JavaFx/pojo/src/com/app/customer/controller/CustomerAdd.ftl",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ javaProjectName
											+ "/src/com/app/customer/controller/CustomerAdd.java",
									data);

							copyTemplate(
									templatePath
											+ "/PresentationLayer/JavaFx/pojo/src/com/app/customer/controller/CustomerList.ftl",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ javaProjectName
											+ "/src/com/app/customer/controller/CustomerList.java",
									data);
						} else if (persistenceFrameworkSelectorWizard
								.getBtnJpa().getSelection()
								|| applicationFrameworkSelectorWizard
										.getBtnEjb().getSelection()) {
							copyTemplate(
									templatePath
											+ "/PresentationLayer/JavaFx/jpa/pojo/src/com/app/customer/controller/CustomerAdd.ftl",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ javaProjectName
											+ "/src/com/app/customer/controller/CustomerAdd.java",
									data);

							copyTemplate(
									templatePath
											+ "/PresentationLayer/JavaFx/jpa/pojo/src/com/app/customer/controller/CustomerList.ftl",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ javaProjectName
											+ "/src/com/app/customer/controller/CustomerList.java",
									data);
						}

						copyTemplate(
								templatePath
										+ "/PresentationLayer/JavaFx/common/build.fxbuild.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName
										+ "/build.fxbuild/", data);
					}

					/*
					 * Rest
					 */

					if ((applicationFrameworkSelectorWizard.getBtnSpring()
							.getSelection() || applicationFrameworkSelectorWizard
							.getBtnEjb().getSelection())
							&& applicationFrameworkSelectorWizard
									.getBtnRestService().getSelection()) {

						/*
						 * Ejb With Rest
						 */
						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {

							data.put("serviceProjectName", earProjectName
									+ "WEB");
							data.put("Rest_URL", "serviceController");

							/*
							 * Copy Files
							 */
							copyEJBDesktopFiles(javaProjectName, templatePath,
									workspace, data, container);

						}

						copyTemplate(
								templatePath
										+ "/PresentationLayer/JavaFx/rest/src/com/app/customer/controller/CustomerAdd.java.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/controller/CustomerAdd.java/",
								data);

						copyTemplate(
								templatePath
										+ "/PresentationLayer/JavaFx/rest/src/com/app/customer/controller/CustomerList.java.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/controller/CustomerList.java/",
								data);

						copyTemplate(
								templatePath
										+ "/PresentationLayer/JavaFx/common/build.fxbuild.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName
										+ "/build.fxbuild/", data);
					}

					/*
					 * Soap
					 */

					if ((applicationFrameworkSelectorWizard.getBtnSpring()
							.getSelection() || applicationFrameworkSelectorWizard
							.getBtnEjb().getSelection())
							&& applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						/*
						 * Ejb With Soap
						 */
						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {

							data.put("serviceProjectName", earProjectName
									+ "WEB");

							/*
							 * Copy Files
							 */
							copyEJBDesktopFiles(javaProjectName, templatePath,
									workspace, data, container);

						}
						if (persistenceFrameworkSelectorWizard.getBtnJpa()
								.getSelection()
								&& applicationFrameworkSelectorWizard
										.getBtnSoapService().getSelection()) {
							copyFile(
									templatePath
											+ "/PresentationLayer/JavaFx/jpa/soap/src/com/app/customer/controller/CustomerAdd.java",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ javaProjectName
											+ "/src/com/app/customer/controller/CustomerAdd.java");

							copyTemplate(
									templatePath
											+ "/PresentationLayer/JavaFx/jpa/soap/src/com/app/customer/controller/CustomerList.ftl",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ javaProjectName
											+ "/src/com/app/customer/controller/CustomerList.java",
									data);

						} else {
							copyTemplate(
									templatePath
											+ "/PresentationLayer/JavaFx/soap/src/com/app/customer/controller/CustomerAdd.ftl",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ javaProjectName
											+ "/src/com/app/customer/controller/CustomerAdd.java",
									data);

							copyTemplate(
									templatePath
											+ "/PresentationLayer/JavaFx/soap/src/com/app/customer/controller/CustomerList.ftl",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ javaProjectName
											+ "/src/com/app/customer/controller/CustomerList.java",data);
						}

						createFolder(container, "src/com/app/customer/service");
						copyFile(
								templatePath
										+ "/PresentationLayer/JavaFx/soap/src/com/app/customer/service/ICustomer.java",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/service/ICustomer.java");

						createFolder(container, "src/com/app/customer/stub");
						copyTemplate(
								templatePath
										+ "/PresentationLayer/JavaFx/soap/src/com/app/customer/stub/CustomerStub.java.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/stub/CustomerStub.java/",
								data);

						copyTemplate(
								templatePath
										+ "/PresentationLayer/JavaFx/common/build.fxbuild.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName
										+ "/build.fxbuild/", data);
					}
				}

				/*
				 * Presentation Layer - Swing
				 */

				if (presentationFrameworkSelectorWizard
						.getComboDesktopAppFramework().getText()
						.equals("Java Swing")) {

					/*
					 * SRC
					 */
					if (!persistenceFrameworkSelectorWizard.getBtnJpa()
							.getSelection()) {
						copyDirectory(templatePath
								+ "/PresentationLayer/Swing/common/src",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName + "/src/");

					} else {
						copyDirectory(templatePath
								+ "/PresentationLayer/Swing/jpa/common/src",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName + "/src/");
					}

					/*
					 * POJO
					 */

					if (applicationFrameworkSelectorWizard.getBtnSpring()
							.getSelection()
							|| applicationFrameworkSelectorWizard.getBtnEjb()
									.getSelection()
							&& (!applicationFrameworkSelectorWizard
									.getBtnRestService().getSelection() && !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection())) {

						/*
						 * Ejb With Pojo
						 */
						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {

							createFolder(container,
									"src/com/app/customer/service");
							data.put("Service",
									"com.app.customer.lookup.CustomerLookup");
							/*
							 * Copy Files
							 */
							copyEJBDesktopFiles(javaProjectName, templatePath,
									workspace, data, container);

							copyFile(
									templatePath
											+ "/ApplicationLayer/Ejb/Rest/com/app/customer/service/ICustomer.java",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ javaProjectName
											+ "/src/com/app/customer/service/ICustomer.java");
						}

						/*
						 * Libraries
						 */

						copyFile(templatePath + "/lib/common/antlr-2.7.5.jar",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName
										+ "/lib/antlr-2.7.5.jar");
						copyFile(templatePath
								+ "/lib/common/cglib-nodep-2.1_3.jar",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName
										+ "/lib/cglib-nodep-2.1_3.jar");
						copyFile(templatePath
								+ "/lib/common/commons-collections.jar",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName
										+ "/lib/commons-collections.jar");
						copyFile(templatePath
								+ "/lib/common/commons-logging.jar", workspace
								.getRoot().getLocation().toString()
								+ "/"
								+ javaProjectName
								+ "/lib/commons-logging.jar");
						copyFile(templatePath + "/lib/common/dom4j-1.6.jar",
								workspace.getRoot().getLocation().toString()
										+ "/" + javaProjectName
										+ "/lib/dom4j-1.6.jar");

						String filePath1 = templatePath
								+ "/PresentationLayer/CommonClasspath/Pojo.classpath";
						StringBuilder value1 = loadDeploymentDescriptorData(filePath1);
						data.put("pojoLib", value1);

						if (dataBaseSelectorWizard.btnMysql.getSelection()) {
							copyDirectory(templatePath + "/lib/MySQL",
									workspace.getRoot().getLocation()
											.toString()
											+ "/" + javaProjectName + "/lib/");
							data.put(
									"dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/mysql-connector-java-5.1.15-bin.jar\"/>");
						} else if (dataBaseSelectorWizard.btnMicrosoftSqlServer
								.getSelection()) {
							copyDirectory(templatePath + "/lib/SQL", workspace
									.getRoot().getLocation().toString()
									+ "/" + javaProjectName + "/lib/");
							data.put("dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/sqljdbc4-4.0.jar\"/>");
						} else if (dataBaseSelectorWizard.getBtnOracle()
								.getSelection()) {
							copyDirectory(templatePath + "/lib/Oracle",
									workspace.getRoot().getLocation()
											.toString()
											+ "/" + javaProjectName + "/lib/");
							data.put("dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/ojdbc6.jar\"/>");
						}

						copyTemplate(
								templatePath
										+ "/PresentationLayer/Swing/pojo/src/com/app/customer/controller/CustomerAdd.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/controller/CustomerAdd.java",
								data);

						copyTemplate(
								templatePath
										+ "/PresentationLayer/Swing/pojo/src/com/app/customer/controller/CustomerList.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/controller/CustomerList.java",
								data);

					}

					/*
					 * Rest
					 */

					if ((applicationFrameworkSelectorWizard.getBtnSpring()
							.getSelection() || applicationFrameworkSelectorWizard
							.getBtnEjb().getSelection())
							&& applicationFrameworkSelectorWizard
									.getBtnRestService().getSelection()) {

						/*
						 * Ejb With Rest
						 */
						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {

							data.put("serviceProjectName", earProjectName
									+ "WEB");
							data.put("Rest_URL", "serviceController");

							/*
							 * Copy Files
							 */
							copyEJBDesktopFiles(javaProjectName, templatePath,
									workspace, data, container);

						}

						copyTemplate(
								templatePath
										+ "/PresentationLayer/Swing/rest/src/com/app/customer/controller/CustomerAdd.java.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/controller/CustomerAdd.java/",
								data);

						copyTemplate(
								templatePath
										+ "/PresentationLayer/Swing/rest/src/com/app/customer/controller/CustomerList.java.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/controller/CustomerList.java/",
								data);

					}

					/*
					 * Soap
					 */

					if ((applicationFrameworkSelectorWizard.getBtnSpring()
							.getSelection() || applicationFrameworkSelectorWizard
							.getBtnEjb().getSelection())
							&& applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {

						/*
						 * Ejb With Soap
						 */
						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {

							data.put("serviceProjectName", earProjectName
									+ "WEB");
							/*
							 * Copy Files
							 */
							copyEJBDesktopFiles(javaProjectName, templatePath,
									workspace, data, container);

						}

						copyTemplate(
								templatePath
										+ "/PresentationLayer/Swing/soap/src/com/app/customer/controller/CustomerAdd.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/controller/CustomerAdd.java",
								data);

						copyTemplate(
								templatePath
										+ "/PresentationLayer/Swing/soap/src/com/app/customer/controller/CustomerList.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/controller/CustomerList.java",
								data);

						createFolder(container, "src/com/app/customer/service");
						copyFile(
								templatePath
										+ "/PresentationLayer/Swing/soap/src/com/app/customer/service/ICustomer.java",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/service/ICustomer.java");

						createFolder(container, "src/com/app/customer/stub");
						copyTemplate(
								templatePath
										+ "/PresentationLayer/Swing/soap/src/com/app/customer/stub/CustomerStub.java.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ javaProjectName
										+ "/src/com/app/customer/stub/CustomerStub.java/",
								data);
					}
				}

				/*
				 * Copying the libraries
				 */

				/* Coping common libraries */

				copyFile(templatePath + "/lib/common/paranamer-2.6.1.jar",
						workspace.getRoot().getLocation().toString() + "/"
								+ javaProjectName + "/lib/paranamer-2.6.1.jar");
				copyFile(templatePath + "/lib/common/log4j-1.2.16.jar",
						workspace.getRoot().getLocation().toString() + "/"
								+ javaProjectName + "/lib/log4j-1.2.16.jar");
				copyFile(templatePath + "/lib/common/HexLogger.jar", workspace
						.getRoot().getLocation().toString()
						+ "/" + javaProjectName + "/lib/HexLogger.jar");
				copyFile(templatePath + "/lib/common/HJSF-Framework.jar",
						workspace.getRoot().getLocation().toString() + "/"
								+ javaProjectName + "/lib/HJSF-Framework.jar");
				copyFile(templatePath + "/lib/common/jstl-1.2.jar", workspace
						.getRoot().getLocation().toString()
						+ "/" + javaProjectName + "/lib/jstl-1.2.jar");

				/* Coping Rest Service ClassPath */
				if (!applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
						&& !applicationFrameworkSelectorWizard
								.getBtnSoapService().getSelection()) {
					copyDirectory(templatePath + "/lib/Rest Service", workspace
							.getRoot().getLocation().toString()
							+ "/" + javaProjectName + "/lib/");
					String filePath4 = templatePath
							+ "/PresentationLayer/CommonClasspath/Rest.classpath";
					StringBuilder value4 = loadDeploymentDescriptorData(filePath4);
					data.put("restLib", value4);
				}

				/* Coping spring ClassPath */
				if (applicationFrameworkSelectorWizard.getBtnSpring()
						.getSelection()) {
					copyDirectory(templatePath + "/lib/Spring/common",
							workspace.getRoot().getLocation().toString() + "/"
									+ javaProjectName + "/lib/");
					String filePath3 = templatePath
							+ "/PresentationLayer/CommonClasspath/Spring.classpath";
					StringBuilder value3 = loadDeploymentDescriptorData(filePath3);
					data.put("springLib", value3);

				}

				/* Coping Rest Service ClassPath */
				if (applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()) {
					copyDirectory(templatePath + "/lib/Rest Service", workspace
							.getRoot().getLocation().toString()
							+ "/" + javaProjectName + "/lib/");
					String filePath4 = templatePath
							+ "/PresentationLayer/CommonClasspath/Rest.classpath";
					StringBuilder value4 = loadDeploymentDescriptorData(filePath4);
					data.put("restLib", value4);
				}

				/* Coping Soap Service ClassPath */
				if (applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()) {
					copyDirectory(templatePath + "/lib/Soap Service", workspace
							.getRoot().getLocation().toString()
							+ "/" + javaProjectName + "/lib/");
					String filePath5 = templatePath
							+ "/PresentationLayer/CommonClasspath/Soap.classpath";
					StringBuilder value5 = loadDeploymentDescriptorData(filePath5);
					data.put("soapLib", value5);

					copyDirectory(templatePath + "/lib/Rest Service", workspace
							.getRoot().getLocation().toString()
							+ "/" + javaProjectName + "/lib/");
					String filePath4 = templatePath
							+ "/PresentationLayer/CommonClasspath/Rest.classpath";
					StringBuilder value4 = loadDeploymentDescriptorData(filePath4);
					data.put("restLib", value4);
				}

				/* Coping Instrumentation ClassPath */
				/*
				 * if
				 * (crosscuttingConcernsSelectorWizard.getBtnInstrumentation()
				 * .getSelection()) { copyDirectory(templatePath +
				 * "/lib/Instrumentation",
				 * workspace.getRoot().getLocation().toString() + "/" +
				 * javaProjectName + "/lib/");
				 * 
				 * String filePath2 = templatePath +
				 * "/PresentationLayer/CommonClasspath/Instrumentation.classpath"
				 * ; StringBuilder value2 =
				 * loadDeploymentDescriptorData(filePath2);
				 * data.put("instrumentationLib", value2); }
				 */

				/* Coping Hibernate ClassPath */
				if (persistenceFrameworkSelectorWizard.getBtnHibernate()
						.getSelection()) {
					String filePath1 = templatePath
							+ "/PresentationLayer/CommonClasspath/Hibernate.classpath";
					StringBuilder value1 = loadDeploymentDescriptorData(filePath1);
					data.put("hibernateLib", value1);
					/*
					 * Copying the libraries
					 */
					copyDirectory(templatePath + "/lib/Hibernate", workspace
							.getRoot().getLocation().toString()
							+ "/" + javaProjectName + "/lib/");
				}

				/* Coping JPA ClassPath */
				if (persistenceFrameworkSelectorWizard.getBtnJpa()
						.getSelection()) {
					String filePath1 = templatePath
							+ "/PresentationLayer/CommonClasspath/Jpa.classpath";
					StringBuilder value1 = loadDeploymentDescriptorData(filePath1);
					data.put("hibernateLib", value1);
					/*
					 * copyTemplate( templatePath +
					 * "/DataLayer/JPA/com/app/customer/vo/Customer.ftl",
					 * workspace.getRoot().getLocation().toString() + "/" +
					 * javaProjectName +
					 * "/src/com/app/customer/vo/Customer.java", data);
					 */
					/*
					 * Copying the libraries
					 */
					copyDirectory(templatePath + "/lib/JPA", workspace
							.getRoot().getLocation().toString()
							+ "/" + javaProjectName + "/lib/");

					data.put("additionalLib",
							"<classpathentry kind=\"lib\" path=\"lib/javassist-3.18.1-GA.jar\"/>");
					copyFile(templatePath
							+ "/lib/Struts/javassist-3.18.1-GA.jar", workspace
							.getRoot().getLocation().toString()
							+ "/"
							+ javaProjectName
							+ "/lib/javassist-3.18.1-GA.jar");
				}

				/* Coping EJB ClassPath */
				if (applicationFrameworkSelectorWizard.getBtnEjb()
						.getSelection()) {
					String filePath1 = templatePath
							+ "/PresentationLayer/CommonClasspath/Jboss.classpath";
					StringBuilder value1 = loadDeploymentDescriptorData(filePath1);
					data.put("jbossLib", value1);
					/*
					 * Copying the libraries
					 */
					copyFile(templatePath + "/lib/Jboss/jboss-client.jar",
							workspace.getRoot().getLocation().toString() + "/"
									+ javaProjectName + "/lib/jboss-client.jar");

					/*
					 * Copy Jboss-Client Properties
					 */
					copyFile(
							templatePath
									+ "/ApplicationLayer/Ejb/jboss-ejb-client.properties",
							workspace.getRoot().getLocation().toString() + "/"
									+ javaProjectName
									+ "/src/jboss-ejb-client.properties");
				}
				copyTemplate(
						templatePath
								+ "/PresentationLayer/CommonClasspath/Common.classpath.ftl",
						workspace.getRoot().getLocation().toString() + "/"
								+ javaProjectName + "/.classpath/", data);

			} catch (Exception ioe) {
				ioe.printStackTrace();
				log.error("Project is not created" + ioe.getMessage());
				IStatus status = new Status(IStatus.ERROR,
						"JavaSoftwareFactory", IStatus.OK,
						ioe.getLocalizedMessage(), ioe);
				throw new CoreException(status);
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
			log.error("Excepton in createWebProject method" + ioe.getMessage());
			IStatus status = new Status(IStatus.ERROR, "JavaSoftwareFactory",
					IStatus.OK, ioe.getLocalizedMessage(), ioe);
			throw new CoreException(status);
		}
	}

	private void createWebPresentationProject(IProject proj)
			throws CoreException, OperationCanceledException {
		try {
			System.out.println("Inside Create Web Project");
			URI projectURI = (!newProjectWizard.useDefaults()) ? newProjectWizard
					.getLocationURI() : null;
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			if (proj == null) {
				throw new Exception("Application Exception : IProject is null");
			}
			projDesc = workspace.newProjectDescription(proj.getName());
			projDesc.setLocationURI(projectURI);
			proj.create(projDesc, null);
			proj.open(IResource.BACKGROUND_REFRESH, null);

			IFacetedProject iFacetedProject = (IFacetedProject) ProjectFacetsManager
					.create(proj.getProject(), true, null);

			IProjectFacet JAVA_FACET = ProjectFacetsManager
					.getProjectFacet("jst.java");
			IProjectFacet WEB_FACET = ProjectFacetsManager
					.getProjectFacet("jst.web");

			iFacetedProject.installProjectFacet(JAVA_FACET.getVersion("1.7"),
					null, monitor);
			iFacetedProject.installProjectFacet(WEB_FACET.getVersion("3.0"),
					null, monitor);

			/*
			 * Generating Presentation Files from Template
			 */

			try {
				IContainer container = (IContainer) proj;

				log.info("WORKSPACE : ", workspace.getRoot().getLocation()
						.toString());
				log.info("WORKSPACE : "
						+ workspace.getRoot().getLocation().toString());
				log.info("Application Name : " + webProjectName);
				String templatePath = ConfigHandler.getInstance()
						.getSystemProperty("template.location");
				log.info("Template Path : " + templatePath);

				Map<String, Object> data = new HashMap<String, Object>();
				data.put("projectName", webProjectName);
				data.put("serviceProjectName", serviceProjectName);
				data.put("serverHost", "http://localhost:8080/");
				data.put("importDateTimeFormat", "");
				data.put("dateTimeFormatAnnotation", "");
				data.put("jsonFormatAnnotation", "");
				data.put("importJsonFormat", "");
				data.put("restServiceConfiguration", "");
				data.put("springMVCConfiguration", "");
				data.put("JSFConfiguration", "");
				data.put("StrutsConfiguration", "");
				data.put("soapServiceConfiguration", "");
				data.put("welcomeFile", "");
				data.put("instrumentationBean", "");
				data.put("Constant_Rest", "");
				data.put("APP_NAME", "");
				// data.put("ApplicationContext", "");
				data.put("EJB_Imports", "");
				data.put("Imports", "");
				data.put("JNDI_Lookup", "");
				data.put("Stateless", "");
				data.put("Remote", "");
				data.put("EJB", "");
				data.put("JTA_Datasource", "");
				data.put("url", url);
				data.put("userName", userName);
				data.put("password", password);
				data.put("Driver", "");
				data.put("Dialect", "");
				data.put("dataBaseLib", "");
				/*
				 * cross cutting
				 */
				data.put("Validation_Call", "");
				data.put("Validation_CatchEdit", "");
				data.put("Validation_CatchAdd", "");
				data.put("Validation_import", "");
				data.put("Cache_import", "");
				data.put("Cache_ImplStart", "");
				data.put("Cache_ImplEnd", "");
				data.put("Notification_method", "");
				data.put("Notification_Import", "");
				data.put("Email_Action", "");
				data.put("Email_button", "");
				data.put("AuthenticationAuthorization", "");
				data.put("Index_onload", "");
				data.put("Index_form", "");
				data.put("notemptyerror", "${not empty error}");
				data.put("error", "${error}");
				data.put("Jsf_Notification", "");
				data.put("Jsf_Notification_Import", "");
				data.put("Jsf_Notification_Object", "");
				data.put("CallValidation", "JndiLookup.doLookUp(\"Customer\")");
				data.put("LoginPageJs", "false;");
				data.put("ListPageJs", "true");
				data.put("EmailPageJs", "false");
				data.put("Authentication", "");
				/*
				 * cross cutting EJB
				 */
				data.put("CacheImport", "");
				data.put("importValidation", "");
				data.put("importNotification", "");
				data.put("AddValidation", "");
				data.put("ValidationEdit", "");
				data.put("Cache_ImplStart", "");
				data.put("Cache_ImplEnd", "");
				data.put("Notification_method", "");

				/*
				 * End
				 */
				data.put("RestEasyConfig", "");
				data.put(
						"Absolute_Ordering",
						"<absolute-ordering>\n<name>HJSF_Framework</name>\n<others/></absolute-ordering>");
				data.put("Rest_URL", "rest");
				data.put("Service", "com.app.customer.service.CustomerImpl");
				data.put("webServiceReferer", "Service/");

				createFolder(container, "/WebContent/css");
				createFolder(container, "/WebContent/images");
				createFolder(container, "/src/com");
				copyDirectory(templatePath + "/PresentationLayer/images",
						workspace.getRoot().getLocation().toString() + "/"
								+ webProjectName + "/WebContent/images");
				/*
				 * Web Service Referer Changes For EJB RestEasy
				 */
				if (applicationFrameworkSelectorWizard.getBtnEjb()
						.getSelection()) {
					data.put("webServiceReferer", "/");
				}

				/*
				 * Cross Cutting Features - Validation
				 */

				if (crosscuttingConcernsSelectorWizard.getBtnValidation()
						.getSelection()) {
					copyDirectory(templatePath + "/lib/Validation", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");

					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText()
							.equals("Struts")) {
						data.put("Validation_Call",
								"new Validation().isCustomerValidate(customer);");
						copyFile(
								templatePath
										+ "/common/src/com/app/customer/validation/Validation.java",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/validation/Validation.java");
						data.put(
								"Validation_CatchEdit",
								"catch (HexValidationException ex) { "
										+ "ServletActionContext.getRequest().setAttribute(\"error\",ex.getMessage());"
										+ "return \"customerEdit\";" + "}");

						data.put(
								"Validation_CatchAdd",
								"catch (HexValidationException ex) { "
										+ "ServletActionContext.getRequest().setAttribute(\"error\",ex.getMessage());"
										+ "return \"customerAdd\";" + "}");
					}
					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText()
							.equals("Spring")) {
						data.put("Validation_Call",
								"new Validation().isCustomerValidate(customer);");
						copyFile(
								templatePath
										+ "/common/src/com/app/customer/validation/Validation.java",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/validation/Validation.java");
						data.put(
								"Validation_CatchEdit",
								"catch (HexValidationException ex) { "
										+ "request.setAttribute(\"error\", ex.getMessage());"
										+ "model = new ModelAndView(\"customerEdit\");"
										+ "}");

						data.put(
								"Validation_CatchAdd",
								"catch (HexValidationException ex) { "
										+ "request.setAttribute(\"error\", ex.getMessage());"
										+ "model = new ModelAndView(\"customerAdd\");"
										+ "}");

					}
					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText().equals("JSF")) {
						data.put("Validation_Call",
								"new Validation().isCustomerValidate(customer);");
						copyFile(
								templatePath
										+ "/common/src/com/app/customer/validation/Validation.java",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/validation/Validation.java");
						data.put(
								"Validation_CatchEdit",
								"catch (HexValidationException hex) { "
										+ "facesContext.addMessage(null,new FacesMessage(hex.getMessage()) );"
										+ "facesContext.getExternalContext().getFlash().setKeepMessages(true); "
										+ " return \"CustomerEdit.xhtml?faces-redirect=true\"; }");

						data.put(
								"Validation_CatchAdd",
								"catch (HexValidationException hex) { "
										+ " customer = new Customer(); "
										+ "facesContext.addMessage(null,new FacesMessage(hex.getMessage()) ); "
										+ "facesContext.getExternalContext().getFlash().setKeepMessages(true); "
										+ "return \"CustomerAdd.xhtml?faces-redirect=true\"; }");

					}

					data.put(
							"Validation_import",
							"import com.app.customer.validation.Validation; import com.hjsf.validator.HexValidationException;");
				}

				/*
				 * Cross Cutting Features - Cache
				 */

				if (crosscuttingConcernsSelectorWizard.getBtnCaching()
						.getSelection()) {

					copyDirectory(templatePath + "/lib/Cache", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");

					data.put(
							"Cache_ImplStart",
							" CachedObject obj = (CachedObject)CacheManager.getCache(new Long(1234));"
									+ " if(obj!=null)  customerList= (List) obj.getObject();"
									+ " if(customerList==null){ ");

					data.put("Cache_ImplEnd",
							"CachedObject co = new CachedObject(customerList, new Long(1234), 1);  "
									+ "CacheManager.putCache(co);  }");
					data.put("Cache_import",
							"import com.hexCache.CacheManager;import com.hexCache.CachedObject;");

				}
				/*
				 * Cross Cutting Features - Notification
				 */
				if (crosscuttingConcernsSelectorWizard.getBtnNotification()
						.getSelection()) {
					copyDirectory(templatePath + "/lib/Notification", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");

					data.put(
							"Notification_Import",
							"import com.hex.framework.mail.MailingImpl; import com.hex.framework.mail.vo.MailVO; import java.util.Locale; import java.util.ResourceBundle;");
					copyDirectory(templatePath + "/common/src/email", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/src/");
					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText()
							.equals("Struts")) {
						copyFile(
								templatePath
										+ "/PresentationLayer/Struts/common/email/email.jsp",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/pages/email.jsp/");

						data.put(
								"Notification_method",
								"public String sendEmail() { "
										+ " ResourceBundle bundle = ResourceBundle.getBundle(\"MailServerCridencials\", Locale.ENGLISH);"
										+ " String from=\"\"; String subject=\"\";		String message=\"\";    String[] to = new String[1]; "
										+ " String[] cc = new String[1];	String[] bc = new String[1]; "
										+ " from = ServletActionContext.getRequest().getParameter(\"name\"); 	to[0] = ServletActionContext.getRequest().getParameter(\"toemail\"); "
										+ " cc[0] = ServletActionContext.getRequest().getParameter(\"ccemail\");		bc[0] = ServletActionContext.getRequest().getParameter(\"bcemail\"); "
										+ " subject = ServletActionContext.getRequest().getParameter(\"subject\");	message = ServletActionContext.getRequest().getParameter(\"message\"); "
										+ " String host=bundle.getString(\"SEVER_HOST\");		int port=Integer.parseInt(bundle.getString(\"SEVER_PORT\")); "
										+ " MailVO mailVO = new MailVO();		mailVO.setFrom(from); mailVO.setTos(to);	mailVO.setHost(host);	mailVO.setSubject(subject);	mailVO.setBody(message);	mailVO.setPort(port); mailVO.setBccs(bc);	mailVO.setCcs(cc);"
										+ " try {	new MailingImpl().sendEmail(mailVO);	} catch (Exception e) { e.printStackTrace(); return \"email\";	} return \"customerList\";	} ");

					}
					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText()
							.equals("Spring")) {
						copyFile(
								templatePath
										+ "/PresentationLayer/Spring MVC/common/email/email.jsp",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/jsp/email.jsp");

						data.put(
								"Notification_method",
								"@RequestMapping(value = \"/sendEmail\", method = RequestMethod.POST) public ModelAndView sendEmail(HttpServletRequest request, HttpServletResponse response) { "
										+ " ResourceBundle bundle = ResourceBundle.getBundle(\"MailServerCridencials\", Locale.ENGLISH); ModelAndView model = new ModelAndView();"
										+ " String from=\"\"; String subject=\"\";		String message=\"\";    String[] to = new String[1]; "
										+ " String[] cc = new String[1];	String[] bc = new String[1]; "
										+ " from = request.getParameter(\"name\"); 	to[0] = request.getParameter(\"toemail\"); "
										+ " cc[0] = request.getParameter(\"ccemail\");		bc[0] = request.getParameter(\"bcemail\"); "
										+ " subject = request.getParameter(\"subject\");	message = request.getParameter(\"message\"); "
										+ " String host=bundle.getString(\"SEVER_HOST\");		int port=Integer.parseInt(bundle.getString(\"SEVER_PORT\")); "
										+ " MailVO mailVO = new MailVO();		mailVO.setFrom(from); mailVO.setTos(to);	mailVO.setHost(host);	mailVO.setSubject(subject);	mailVO.setBody(message);	mailVO.setPort(port); mailVO.setBccs(bc);	mailVO.setCcs(cc);"
										+ " try {	new MailingImpl().sendEmail(mailVO);	model = new ModelAndView(\"customerList\");"
										+ " List customerList=getCustomerList(); CustomerForm customerForm = new CustomerForm(); customerForm.setCustomers(customerList);"
										+ " List<String> searchFiledOptions = getSearchFieldOptions(); model.getModelMap().addAttribute(\"searchFieldOptions\", searchFiledOptions); model.getModelMap().addAttribute(\"searchTxt\", \"\"); model.getModelMap().addAttribute(\"customerForm\", customerForm);"
										+ " } catch (Exception e) { e.printStackTrace(); model = new ModelAndView(\"email\");	} return model;	} @RequestMapping(value = \"email\", method = RequestMethod.GET)"
										+ " public ModelAndView email() { ModelAndView model = new ModelAndView(\"email\");return model;}");

						data.put(
								"Email_button",
								"<input type=\"button\" class=\"btn btn-default\" value=\"Email\" "
										+ "onclick=\"location.href='/"
										+ webProjectName
										+ "/auth/email'\" style=\"float: right\">");
						data.put("welcomeFile", "index.jsp");
						copyFile(
								templatePath
										+ "/PresentationLayer/Spring MVC/common/index/index.jsp",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/index.jsp");

					}

					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText().equals("JSF")) {
						copyFile(
								templatePath
										+ "/PresentationLayer/Jsf/common/email/Email.xhtml",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/pages/Email.xhtml");

						data.put(
								"Notification_method",
								"public void emial() throws IOException { FacesContext.getCurrentInstance().renderResponse(); FacesContext.getCurrentInstance().getExternalContext().redirect(\"Email.xhtml\"); } "
										+ " public String sendEmail() throws IOException { ResourceBundle bundle = ResourceBundle.getBundle(\"MailServerCridencials\",Locale.ENGLISH); "
										+ " String from=\"\";  String subject=\"\"; String message=\"\";   String[] to = new String[1]; String[] cc = new String[1];	String[] bc = new String[1]; to[0]=email.getTo(); cc[0]=email.getCc(); bc[0]=email.getBc(); "
										+ " String host=bundle.getString(\"SEVER_HOST\");		int port=Integer.parseInt(bundle.getString(\"SEVER_PORT\"));  MailVO mailVO = new MailVO();	mailVO.setFrom(email.getFrom()); mailVO.setSubject(email.getSub()); "
										+ " mailVO.setBody(email.getMeg()); mailVO.setHost(host);	 mailVO.setPort(port); mailVO.setBccs(bc); mailVO.setCcs(cc);  try { new MailingImpl().sendEmail(mailVO); FacesContext.getCurrentInstance().getExternalContext().redirect(\"CustomerList.xhtml\"); "
										+ " }	catch (Exception e) { return \"Email.xhtml?faces-redirect=true\"; } return null; }");

						copyFile(
								templatePath
										+ "/PresentationLayer/Jsf/common/notification/EmailVo.java",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/vo/EmailVo.java");
						data.put(
								"Jsf_Notification",
								"private EmailVo email; public EmailVo getEmail() {return email; } public void setEmail(EmailVo email) {this.email = email;}");
						data.put("Jsf_Notification_Import",
								"import com.app.customer.vo.EmailVo;");
						data.put("Jsf_Notification_Object",
								"email=new EmailVo(); setShowEmailbutton(true);");

					}

					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText()
							.equals("Struts")) {
						data.put(
								"Email_Action",
								" <action name=\"email\">  <result >/pages/email.jsp</result> </action> "
										+ " <action name=\"sendEmail\"    class=\"com.app.customer.mbean.CustomerBean\"    method=\"sendEmail\"> "
										+ " <result name=\"customerList\">/pages/CustomerList.jsp</result>     <result name=\"email\">/pages/email.jsp</result>   </action>");
						data.put("Email_button", "setEmail(false);");
					}

				}

				/*
				 * Cross Cutting Features - Instrumentation
				 */
				if (crosscuttingConcernsSelectorWizard.getBtnInstrumentation()
						.getSelection()) {
					System.out
							.println("=================Cross Cutting Features - Instrumentation");
					copyDirectory(templatePath + "/lib/Instrumentation",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/WEB-INF/lib/");

					copyFile(
							templatePath
									+ "/common/Instrumetation/Monitoring.java",
							workspace.getRoot().getLocation().toString()
									+ "/"
									+ webProjectName
									+ "/src/com/jsf/instrumentaion/Monitoring.java");

					copyDirectory(templatePath + "/common/Instrumetation/aop/",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName + "/WebContent/META-INF/");

				}

				/*
				 * Cross Cutting Features - Authentication&Authorization
				 */
				if (crosscuttingConcernsSelectorWizard
						.getBtnAuthenticationAutherization().getSelection()) {
					copyDirectory(templatePath
							+ "/lib/Authentication&Authorization", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");

					copyFile(
							templatePath
									+ "/common/Authentication&Authorization/hibernate.properties",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/src/hibernate.properties");

					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText()
							.equals("Struts")) {
						data.put(
								"AuthenticationAuthorization",
								"<absolute-ordering> <name>Authentication</name> <name>StrutsPrepareAndExecuteFilter</name> <others/> </absolute-ordering>");
						data.put(
								"Index_form",
								"<form action=\"login\" method=\"post\">  <input type=\"text\" name=\"username\"> <input type=\"password\" name=\"password\"> <input type=\"submit\" name=\"submit\"> </form>");
						copyDirectory(templatePath
								+ "/lib/StrutsAuthentication", workspace
								.getRoot().getLocation().toString()
								+ "/"
								+ webProjectName
								+ "/WebContent/WEB-INF/lib/");
						copyFile(
								templatePath
										+ "/PresentationLayer/Struts/common/src/Authentication.properties",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/src/Authentication.properties");
					}

					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText()
							.equals("Spring")) {
						data.put(
								"AuthenticationAuthorization",
								"<absolute-ordering> <name>Authentication</name>  <others/> </absolute-ordering>");

						copyFile(
								templatePath
										+ "/PresentationLayer/Struts/common/src/Authentication.properties",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/src/Authentication.properties");

					}
					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText().equals("JSF")) {
						data.put(
								"AuthenticationAuthorization",
								"<absolute-ordering> <name>Authentication</name>  <others/> </absolute-ordering>");

						copyFile(
								templatePath
										+ "/PresentationLayer/Jsf/common/src/Authentication.properties",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/src/Authentication.properties");
						data.put("welcomeFile", "index.jsp");
						data.put(
								"Index_form",
								"<form action=\"login\" method=\"post\">  <input type=\"text\" name=\"username\"> <input type=\"password\" name=\"password\"> <input type=\"submit\" name=\"submit\"> </form>");

					}
				} else {
					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText()
							.equals("Struts")) {
						data.put(
								"AuthenticationAuthorization",
								" <filter> <filter-name>struts2</filter-name> <filter-class> org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter </filter-class>"
										+ "</filter> <filter-mapping> <filter-name>struts2</filter-name> <url-pattern>/auth/*</url-pattern> <dispatcher>REQUEST</dispatcher> <dispatcher>FORWARD</dispatcher></filter-mapping>");
						data.put("Index_form",
								"<form id='myform' action=\"auth/customerList\" method=\"post\"> </form>");
						data.put("Index_onload", "onload=\"redirect();\"");
					}
					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText().equals("JSF")) {
						data.put("Index_form",
								"<% response.sendRedirect(\"auth/pages/CustomerList.xhtml\"); %>");
					}
				}

				/**
				 * SPA-Angular JS Cross cutting
				 */
				if (presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText()
						.equals("SPA-Angular JS")

						&& (crosscuttingConcernsSelectorWizard
								.getBtnValidation().getSelection()
								|| crosscuttingConcernsSelectorWizard
										.getBtnCaching().getSelection() || crosscuttingConcernsSelectorWizard
								.getBtnNotification().getSelection())) {
					createFolder(container, "src/com/app/");
					createFolder(container, "src/com/app/customer");
					createFolder(container, "src/com/app/customer/vo");
					createFolder(container, "src/com/app/customer/validation");
					data.put(
							"Authentication",
							"import com.app.customer.vo.UserInformation; import com.app.customer.service.UserInformationService;import com.app.customer.service.UserInformationServiceImpl; ");
					if (applicationFrameworkSelectorWizard.getBtnEjb()
							.getSelection()) {
						data.put("Rest_URL", "serviceController");
					} else {
						copyTemplate(
								templatePath
										+ "/common/src/com/app/customer/vo/Customer.java.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/vo/Customer.java",
								data);
					}
					copyFile(
							templatePath
									+ "/PresentationLayer/AngularJS/crosscutting/com/app/customer/validation/Validation.java",
							workspace.getRoot().getLocation().toString()
									+ "/"
									+ webProjectName
									+ "/src/com/app/customer/validation/Validation.java");

					if (crosscuttingConcernsSelectorWizard.getBtnValidation()
							.getSelection()) {
						copyDirectory(templatePath + "/lib/Validation",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/WEB-INF/lib/");

						copyFile(
								templatePath
										+ "/PresentationLayer/AngularJS/crosscutting/com/app/customer/validation/Validation.java",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/validation/Validation.java");
						data.put("Validation_Call",
								"new Validation().isCustomerValidate(customer);");
						data.put("Validation_CatchEdit",
								"catch (HexValidationException e) {return \"Failure:\" + e.getMessage();}");

						data.put(
								"Validation_import",
								"import com.app.customer.validation.Validation; import com.hjsf.validator.HexValidationException;");

					}
					if (crosscuttingConcernsSelectorWizard.getBtnNotification()
							.getSelection()) {
						copyDirectory(templatePath + "/lib/Notification",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/WEB-INF/lib/");

						data.put(
								"importNotification",
								"import javax.mail.SendFailedException;import com.hex.framework.mail.vo.MailVO;import com.app.customer.vo.EmailVo;import com.hex.framework.mail.MailingImpl; "
										+ " import com.hex.framework.mail.exception.MailingException;");
						data.put("Email_button", "true");
						copyDirectory(templatePath + "/common/src/email",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName + "/src/");
						data.put(
								"Notification_method",
								"public String sendMail(EmailVo email) throws HexApplicationException { System.out.println(\"in side send email method \" + email); "
										+ " ResourceBundle bundle = ResourceBundle.getBundle(\"MailServerCridencials\", Locale.ENGLISH); String[] to = new String[1]; String[] cc = new String[1]; String[] bc = new String[1]; to[0] = email.getTo(); "
										+ " cc[0] = email.getCc(); bc[0] = email.getBc(); String host = bundle.getString(\"SEVER_HOST\"); int port = Integer.parseInt(bundle.getString(\"SEVER_PORT\")); MailVO mailVO = new MailVO(); mailVO.setFrom(email.getFrom()); "
										+ " mailVO.setSubject(email.getSub()); mailVO.setBody(email.getMeg()); mailVO.setHost(host); mailVO.setPort(port); mailVO.setBccs(bc); mailVO.setCcs(cc); try { new MailingImpl().sendEmail(mailVO); return \"Success:Email sent successfully\"; "
										+ " } catch (MailingException Me) { return \"Failure: Email sending failed\"; } catch (Exception e) { return \"Failure: Email sending failed\"; }}");

						copyFile(
								templatePath
										+ "/PresentationLayer/AngularJS/crosscutting/com/app/customer/vo/EmailVo.java",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/vo/EmailVo.java");

					}
					if (crosscuttingConcernsSelectorWizard.getBtnCaching()
							.getSelection()) {
						copyDirectory(templatePath + "/lib/Cache", workspace
								.getRoot().getLocation().toString()
								+ "/"
								+ webProjectName
								+ "/WebContent/WEB-INF/lib/");
						data.put(
								"Cache_ImplStart",
								"CachedObject obj = (CachedObject) CacheManager.getCache(new Long(1234)); if (obj != null) customerList = (List) obj.getObject(); if (customerList == null) {");
						data.put(
								"Cache_ImplEnd",
								"CachedObject co = new CachedObject(customerList, new Long(1234), 1); CacheManager.putCache(co);} ");
						data.put("CacheImport",
								"import com.hexCache.CacheManager; import com.hexCache.CachedObject;");

					}
					if (applicationFrameworkSelectorWizard.getBtnSpring()
							.getSelection()
							|| applicationFrameworkSelectorWizard.getBtnEjb()
									.getSelection()) {
						if (applicationFrameworkSelectorWizard
								.getBtnSoapService().getSelection()
								&& !applicationFrameworkSelectorWizard
										.getBtnRestService().getSelection()) {
							data.put("service",
									"com.app.customer.stub.CustomerStub");
						}
					}
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						if (applicationFrameworkSelectorWizard.getBtnSpring()
								.getSelection()) {
							data.put("service",
									"com.app.customer.service.CustomerImpl");
						} else {
							data.put("service",
									"com.app.customer.lookup.CustomerLookup");
						}
					}
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()) {
						copyTemplate(
								templatePath
										+ "/PresentationLayer/AngularJS/crosscutting/com/app/customer/validation/pojoSoap/CustomerValidation.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/validation/CustomerValidation.java",
								data);
					} else
						copyTemplate(
								templatePath
										+ "/PresentationLayer/AngularJS/crosscutting/com/app/customer/validation/rest/CustomerValidation.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/validation/CustomerValidation.java",
								data);

				}

				/*
				 * Angular JS Presentation Layer Code copying
				 */
				if (presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText()
						.equals("SPA-Angular JS")
						&& (crosscuttingConcernsSelectorWizard
								.getBtnValidation().getSelection()
								|| crosscuttingConcernsSelectorWizard
										.getBtnCaching().getSelection()
								|| crosscuttingConcernsSelectorWizard
										.getBtnNotification().getSelection()
								|| crosscuttingConcernsSelectorWizard
										.getBtnAuthenticationAutherization()
										.getSelection() || crosscuttingConcernsSelectorWizard
								.getBtnInstrumentation().getSelection())) {
					createFolder(container, "/WebContent/js");
					copyFile(
							templatePath
									+ "/PresentationLayer/AngularJS/common/css/bootstrap.min.css",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/css/bootstrap.min.css");
					copyFile(
							templatePath
									+ "/PresentationLayer/AngularJS/common/css/style.css",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/css/style.css");
					copyFile(
							templatePath
									+ "/PresentationLayer/AngularJS/common/js/ui-bootstrap-tpls-1.2.4.min.js",
							workspace.getRoot().getLocation().toString()
									+ "/"
									+ webProjectName
									+ "/WebContent/js/ui-bootstrap-tpls-1.2.4.min.js");
					data.put("welcomeFile", "Customer Application.html");
					if (crosscuttingConcernsSelectorWizard
							.getBtnAuthenticationAutherization().getSelection()) {

						String filePath = templatePath
								+ "/PresentationLayer/AngularJS/crosscutting/Login.ftl";
						StringBuilder value = loadDeploymentDescriptorData(filePath);
						data.put("LoginHtml", value);
						data.put("LoginPageJs", "true");
						data.put("ListPageJs", "false");

					}
					if (crosscuttingConcernsSelectorWizard.getBtnNotification()
							.getSelection()) {
						String filePath = templatePath
								+ "/PresentationLayer/AngularJS/crosscutting/Email.ftl";
						StringBuilder value = loadDeploymentDescriptorData(filePath);
						data.put("EmailHtml", value);
						data.put("EmailPageJs", "true");
					}

					copyTemplate(
							templatePath
									+ "/PresentationLayer/AngularJS/crosscutting/script.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/js/script.js", data);
					copyFile(
							templatePath
									+ "/PresentationLayer/AngularJS/crosscutting/SpaUrl.properties",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName + "/src/SpaUrl.properties");
					copyTemplate(
							templatePath
									+ "/PresentationLayer/AngularJS/crosscutting/Customer Application.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/Customer Application.html",
							data);

				} else if (presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText()
						.equals("SPA-Angular JS")) {
					copyDirectory(templatePath
							+ "/PresentationLayer/AngularJS/common", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/");
					data.put("welcomeFile", "Customer Application.html");
					createFolder(container, "/WebContent/js");
					/*
					 * Copying script.js with rest service if Rest Service is
					 * enabled
					 */
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {

						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
							data.put("Service",
									"com.app.customer.lookup.CustomerLookup");
						}

						copyTemplate(
								templatePath
										+ "/PresentationLayer/AngularJS/Pojo/js/script.js.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/js/script.js", data);
					} else if (applicationFrameworkSelectorWizard
							.getBtnRestService().getSelection()) {

						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
							data.put("serviceProjectName", webProjectName);
							data.put("Rest_URL", "serviceController");
						}

						copyTemplate(
								templatePath
										+ "/PresentationLayer/AngularJS/Rest/js/script.js.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/js/script.js", data);
					} else if (applicationFrameworkSelectorWizard
							.getBtnSoapService().getSelection()) {
						copyTemplate(
								templatePath
										+ "/PresentationLayer/AngularJS/Soap/js/script.js.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/js/script.js", data);
					}
				}

				/*
				 * Spring MVC Code Copying
				 */
				if (presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText().equals("Spring")) {
					/*
					 * WebContent
					 */
					createFolder(container, "/WebContent/jsp");
					copyDirectory(templatePath
							+ "/PresentationLayer/Spring MVC/common/css",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName + "/WebContent/css");
					copyTemplate(
							templatePath
									+ "/PresentationLayer/Spring MVC/common/jsp/customerList.jsp.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/jsp/customerList.jsp", data);
					copyTemplate(
							templatePath
									+ "/PresentationLayer/Spring MVC/common/jsp/customerAdd.jsp.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/jsp/customerAdd.jsp", data);
					copyTemplate(
							templatePath
									+ "/PresentationLayer/Spring MVC/common/jsp/customerEdit.jsp.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/jsp/customerEdit.jsp", data);
					if (applicationFrameworkSelectorWizard.getBtnSpring()
							.getSelection()) {
						data.put("ApplicationContext",
								"<import resource=\"classpath://applicationContext.xml\"/>");
					}
					copyTemplate(
							templatePath
									+ "/PresentationLayer/Spring MVC/common/WEB-INF/${projectName}-servlet.xml.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName + "/WebContent/WEB-INF/"
									+ webProjectName + "-servlet.xml", data);
					copyTemplate(
							templatePath
									+ "/PresentationLayer/Spring MVC/common/WEB-INF/web.xml.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/WEB-INF/web.xml", data);
					String filePath = workspace.getRoot().getLocation()
							.toString()
							+ "/"
							+ webProjectName
							+ "/WebContent/WEB-INF/web.xml";
					StringBuilder value = loadDeploymentDescriptorData(filePath);
					data.put("springMVCConfiguration", value);

					/*
					 * Src
					 */
					copyDirectory(templatePath
							+ "/PresentationLayer/Spring MVC/common/src",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName + "/src/");
					createFolder(container, "src/com/app");
					createFolder(container, "src/com/app/customer");
					createFolder(container, "src/com/app/customer/controller");
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						log.debug("Test--->>>>");
						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
							data.put("Service",
									"com.app.customer.lookup.CustomerLookup");
						}
						copyTemplate(
								templatePath
										+ "/PresentationLayer/Spring MVC/Pojo/src"
										+ "/com/app/customer/controller/CustomerController.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/controller/CustomerController.java",
								data);
					} else if (applicationFrameworkSelectorWizard
							.getBtnRestService().getSelection()) {

						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
							data.put("Rest_URL", "serviceController");
						}
						copyTemplate(
								templatePath
										+ "/PresentationLayer/Spring MVC/Rest/src"
										+ "/com/app/customer/controller/CustomerController.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/controller/CustomerController.java",
								data);
						/*
						 * copyDirectory(templatePath +
						 * "/PresentationLayer/Spring MVC/Rest/src",
						 * workspace.getRoot().getLocation().toString() + "/" +
						 * webProjectName + "/src/");
						 */
					} else if (applicationFrameworkSelectorWizard
							.getBtnSoapService().getSelection()) {

						copyTemplate(
								templatePath
										+ "/PresentationLayer/Spring MVC/Soap/src"
										+ "/com/app/customer/controller/CustomerController.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/controller/CustomerController.java",
								data);
						/*
						 * copyDirectory(templatePath +
						 * "/PresentationLayer/Spring MVC/Soap/src",
						 * workspace.getRoot().getLocation().toString() + "/" +
						 * webProjectName + "/src/");
						 */
					}
					if (!applicationFrameworkSelectorWizard.getBtnEjb()
							.getSelection()
							&& persistenceFrameworkSelectorWizard.getBtnJpa()
									.getSelection()) {
						data.put("importDateTimeFormat",
								"import org.springframework.format.annotation.DateTimeFormat;");
						data.put("dateTimeFormatAnnotation",
								"@DateTimeFormat(pattern = \"dd/MM/yyyy\")");

						/*
						 * Copying the libraries
						 */
						copyDirectory(templatePath + "/lib/Spring/common",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/WEB-INF/lib/");
						copyDirectory(templatePath + "/lib/Spring/MVC",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/WEB-INF/lib/");
					} else if (applicationFrameworkSelectorWizard.getBtnEjb()
							.getSelection()) {
						/*
						 * Copying the libraries
						 */
						copyFile(
								templatePath + "/lib/common/jstl-1.2.jar",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/WebContent/WEB-INF/lib/jstl-1.2.jar");
						copyFile(
								templatePath
										+ "/lib/Spring/MVC/spring-web-4.1.6.RELEASE.jar",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/WebContent/WEB-INF/lib/spring-web-4.1.6.RELEASE.jar");
						copyFile(
								templatePath
										+ "/lib/Spring/MVC/spring-webmvc-4.1.6.RELEASE.jar",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/WebContent/WEB-INF/lib/spring-webmvc-4.1.6.RELEASE.jar");
					} else if (persistenceFrameworkSelectorWizard.getBtnJpa()
							.getSelection()) {
						/*
						 * Copying the libraries
						 */
						copyFile(
								templatePath + "/lib/common/jstl-1.2.jar",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/WebContent/WEB-INF/lib/jstl-1.2.jar");
						/*
						 * copyDirectory(templatePath + "/lib/Spring/common",
						 * workspace.getRoot().getLocation().toString() + "/" +
						 * webProjectName + "/WebContent/WEB-INF/lib/");
						 */
						copyDirectory(templatePath + "/lib/Spring/MVC",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/WEB-INF/lib/");
					} else {
						data.put("importDateTimeFormat",
								"import org.springframework.format.annotation.DateTimeFormat;");
						data.put("dateTimeFormatAnnotation",
								"@DateTimeFormat(pattern = \"dd/MM/yyyy\")");
						/*
						 * Copying the libraries
						 */
						copyDirectory(templatePath + "/lib/Spring/MVC",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/WEB-INF/lib/");
					}

				}

				/*
				 * Application Layer - Spring IOC
				 */
				if (applicationFrameworkSelectorWizard.getBtnSpring()
						.getSelection()) {
					data.put("customerDaoObject",
							"(CustomerDao) BootStrapper.getService().getBean(\"CustomerDao\")");
					/*
					 * Copying the libraries
					 */
					if (!applicationFrameworkSelectorWizard.getBtnEjb()
							.getSelection()) {
						copyDirectory(templatePath + "/lib/Spring/common",
								workspace.getRoot().getLocation().toString()
										+ "/" + webProjectName
										+ "/WebContent/WEB-INF/lib/");
					}
					if (persistenceFrameworkSelectorWizard.getBtnJpa()
							.getSelection()) {
						data.put("Imports",
								"import com.app.customer.util.BootStrapper;");
						data.put("JNDI_Lookup",
								"BootStrapper.getService().getBean(\"CustomerService\")");
					}

				} else {
					data.put("customerDaoObject", "new CustomerDaoImpl()");
				}

				/*
				 * Application Layer - EJB
				 */
				if (applicationFrameworkSelectorWizard.getBtnEjb()
						.getSelection()) {
					data.put("APP_NAME", earProjectName);
					/**
					 * Set EJB in Web Meta-Inf Manifest Entries
					 */
					copyTemplate(
							templatePath
									+ "/PresentationLayer/Jsf/common/META-INF/MANIFEST.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/META-INF/MANIFEST.MF", data);
					/**
					 * JBoss client Lib
					 */
					if (!applicationFrameworkSelectorWizard.getBtnSoapService()
							.getSelection()) {
						copyFile(
								templatePath + "/lib/Jboss/jboss-client.jar",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/WebContent/WEB-INF/lib/jboss-client.jar");
						copyFile(
								templatePath + "/lib/Jboss/servlet-api.jar",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/WebContent/WEB-INF/lib/servlet-api.jar");
					} else {
						copyDirectory(templatePath + "/lib/Jboss", workspace
								.getRoot().getLocation().toString()
								+ "/"
								+ webProjectName
								+ "/WebContent/WEB-INF/lib/");
					}
					/*
					 * Copy EJB Web Module Files
					 */
					copyEJBWebFiles(webProjectName, templatePath, workspace,
							data, container);

					/*
					 * Web Xml Configuration
					 */
					data.put("Absolute_Ordering", "");
					String filePath = templatePath
							+ "/ApplicationLayer/Ejb/web.xml";
					StringBuilder value = loadDeploymentDescriptorData(filePath);
					data.put("RestEasyConfig", value);

				}

				/*
				 * JSF
				 */
				if (presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText().equals("JSF")) {
					/*
					 * WebContent
					 */
					copyDirectory(templatePath
							+ "/PresentationLayer/Jsf/common/css", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/css/");
					createFolder(container, "/WebContent/pages");
					copyDirectory(templatePath
							+ "/PresentationLayer/Jsf/common/pages", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/pages/");
					copyTemplate(templatePath
							+ "/PresentationLayer/Jsf/common/index.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName + "/WebContent/index.jsp",
							data);
					/*
					 * copyFile(templatePath +
					 * "/PresentationLayer/Jsf/common/index.jsp",
					 * workspace.getRoot().getLocation().toString() + "/" +
					 * webProjectName + "/WebContent/index.jsp");
					 */
					String filePath = templatePath
							+ "/PresentationLayer/Jsf/common/WEB-INF/web.xml";
					StringBuilder value = loadDeploymentDescriptorData(filePath);
					data.put("JSFConfiguration", value);
					data.put("welcomeFile", "index.jsp");
					/*
					 * Src
					 */
					createFolder(container, "src/com");
					createFolder(container, "src/com/app");
					createFolder(container, "src/com/app/customer");
					createFolder(container, "src/com/app/customer/mbean");
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {

						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
							data.put("Service",
									"com.app.customer.lookup.CustomerLookup");
						}

						copyTemplate(
								templatePath
										+ "/PresentationLayer/Jsf/common/src/com/app/customer/mbean/CustomerBean.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/mbean/CustomerBean.java",
								data);
					} else if (applicationFrameworkSelectorWizard
							.getBtnRestService().getSelection()) {

						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
							data.put("Rest_URL", "serviceController");
						}
						copyTemplate(
								templatePath
										+ "/PresentationLayer/Jsf/Rest/src/com/app/customer/mbean/CustomerBean.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/mbean/CustomerBean.java",
								data);
					} else if (applicationFrameworkSelectorWizard
							.getBtnSoapService().getSelection()) {
						/*
						 * copyDirectory(templatePath +
						 * "/PresentationLayer/Jsf/Soap/src", workspace
						 * .getRoot().getLocation().toString() + "/" +
						 * webProjectName + "/src/");
						 */
						copyTemplate(
								templatePath
										+ "/PresentationLayer/Jsf/Soap/src/com/app/customer/mbean/CustomerBean.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/mbean/CustomerBean.java",
								data);
					}

					copyFile(
							templatePath
									+ "/PresentationLayer/Jsf/common/src/ApplicationResources.properties",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/src/ApplicationResources.properties");
					copyFile(
							templatePath
									+ "/PresentationLayer/Jsf/common/src/ApplicationResources_en.properties",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/src/ApplicationResources_en.properties");

					/*
					 * Copying the libraries
					 */
					copyDirectory(templatePath + "/lib/JSF", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");
				}

				/*
				 * Struts - Presentation Layer
				 */
				if (presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText().equals("Struts")) {
					/*
					 * WebContent
					 */
					copyDirectory(templatePath
							+ "/PresentationLayer/Struts/common/css", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/css/");
					copyDirectory(templatePath + "/PresentationLayer/images",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName + "/WebContent/images/");
					createFolder(container, "/WebContent/pages");
					copyDirectory(templatePath
							+ "/PresentationLayer/Struts/common/pages",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName + "/WebContent/pages/");
					/*
					 * copyFile(templatePath +
					 * "/PresentationLayer/Struts/common/index.jsp",
					 * workspace.getRoot().getLocation().toString() + "/" +
					 * webProjectName + "/WebContent/index.jsp");
					 */
					copyTemplate(templatePath
							+ "/PresentationLayer/Struts/common/index.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName + "/WebContent/index.jsp",
							data);
					String filePath = templatePath
							+ "/PresentationLayer/Struts/common/WEB-INF/web.xml";
					StringBuilder value = loadDeploymentDescriptorData(filePath);
					data.put("welcomeFile", "index.jsp");
					data.put("StrutsConfiguration", value);
					if (applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						data.put(
								"Constant_Rest",
								"<constant name=\"struts.action.excludePattern\" value=\"/rest/.*,/BusinessDelegator.*,/serviceController.*\"/>");
					} else if (applicationFrameworkSelectorWizard
							.getBtnSoapService().getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnRestService().getSelection()) {
						data.put(
								"Constant_Rest",
								"<constant name=\"struts.action.excludePattern\" value=\"/BusinessDelegator.*,/customerService.*,/serviceController.*\"/>");
					} else if (applicationFrameworkSelectorWizard
							.getBtnRestService().getSelection()
							&& applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						data.put(
								"Constant_Rest",
								"<constant name=\"struts.action.excludePattern\" value=\"/rest/.*,/BusinessDelegator.*,/customerService.*,/serviceController.*\"/>");
					} else {
						data.put(
								"Constant_Rest",
								"<constant name=\"struts.action.excludePattern\" value=\"/serviceController.*\"/>");
					}
					/*
					 * Src
					 */
					createFolder(container, "src/com");
					createFolder(container, "src/com/app");
					createFolder(container, "src/com/app/customer");
					createFolder(container, "src/com/app/customer/mbean");
					String strutsPath = null;
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						strutsPath = "common";
						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
							data.put("Service",
									"com.app.customer.lookup.CustomerLookup");
						}
					} else if (applicationFrameworkSelectorWizard
							.getBtnRestService().getSelection()) {
						strutsPath = "Rest";
						if (applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
							data.put("Rest_URL", "serviceController");
						}
					} else if (applicationFrameworkSelectorWizard
							.getBtnSoapService().getSelection()) {
						strutsPath = "Soap";
					}
					copyTemplate(
							templatePath
									+ "/PresentationLayer/Struts/"
									+ strutsPath
									+ "/src/com/app/customer/mbean/CustomerBean.ftl",
							workspace.getRoot().getLocation().toString()
									+ "/"
									+ webProjectName
									+ "/src/com/app/customer/mbean/CustomerBean.java",
							data);
					copyFile(templatePath + "/PresentationLayer/Struts/"
							+ strutsPath
							+ "/src/ApplicationResources.properties", workspace
							.getRoot().getLocation().toString()
							+ "/"
							+ webProjectName
							+ "/src/ApplicationResources.properties");
					copyFile(templatePath + "/PresentationLayer/Struts/"
							+ strutsPath
							+ "/src/ApplicationResources_en.properties",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/src/ApplicationResources_en.properties");
					copyTemplate(templatePath + "/PresentationLayer/Struts/"
							+ strutsPath + "/src/struts.ftl", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/src/struts.xml", data);
					/*
					 * Copying the libraries
					 */
					copyDirectory(templatePath + "/lib/Struts", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");
					/*
					 * Copying common libraries
					 */
					if (!applicationFrameworkSelectorWizard.getBtnEjb()
							.getSelection()) {
						copyDirectory(templatePath + "/lib/common", workspace
								.getRoot().getLocation().toString()
								+ "/"
								+ webProjectName
								+ "/WebContent/WEB-INF/lib/");
					}

				}

				/*
				 * Data Layer - Hibernate
				 */
				if (persistenceFrameworkSelectorWizard.getBtnHibernate()
						.getSelection()
						&& !applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
					/*
					 * if
					 * (!applicationFrameworkSelectorWizard.getBtnRestService()
					 * .getSelection() && !applicationFrameworkSelectorWizard
					 * .getBtnSoapService().getSelection()) {
					 */
					/*
					 * Copying the libraries
					 */
					copyDirectory(templatePath + "/lib/Hibernate", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");
					/*
					 * Copy Database Lib
					 */
					copyDatabaseLib(webProjectName, templatePath, workspace,
							data);

					// }
				}
				/*
				 * Data Layer - JPA
				 */
				if (persistenceFrameworkSelectorWizard.getBtnJpa()
						.getSelection()
						&& !applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
					/*
					 * if
					 * (!applicationFrameworkSelectorWizard.getBtnRestService()
					 * .getSelection() && !applicationFrameworkSelectorWizard
					 * .getBtnSoapService().getSelection()) {
					 */

					/*
					 * Copying the libraries
					 */
					copyDirectory(templatePath + "/lib/JPA", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");
					copyFile(templatePath + "/lib/common/antlr-2.7.5.jar",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/WEB-INF/lib/antlr-2.7.5.jar");
					copyFile(
							templatePath + "/lib/common/commons-logging.jar",
							workspace.getRoot().getLocation().toString()
									+ "/"
									+ webProjectName
									+ "/WebContent/WEB-INF/lib/commons-logging.jar");
					copyFile(templatePath + "/lib/common/dom4j-1.6.jar",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/WEB-INF/lib/dom4j-1.6.jar");
					/*
					 * copyFile(templatePath + "/lib/Hibernate/hibernate3.jar",
					 * workspace .getRoot().getLocation().toString() + "/" +
					 * projName + "/lib/hibernate3.jar");
					 */
					copyFile(templatePath + "/lib/common/HexLogger.jar",
							workspace.getRoot().getLocation().toString() + "/"
									+ webProjectName
									+ "/WebContent/WEB-INF/lib/HexLogger.jar");
					copyFile(templatePath
							+ "/lib/Struts/javassist-3.18.1-GA.jar", workspace
							.getRoot().getLocation().toString()
							+ "/"
							+ webProjectName
							+ "/WebContent/WEB-INF/lib/javassist-3.18.1-GA.jar");
					copyFile(
							templatePath
									+ "/lib/Hibernate/javax-transaction-api.jar",
							workspace.getRoot().getLocation().toString()
									+ "/"
									+ webProjectName
									+ "/WebContent/WEB-INF/lib/javax-transaction-api.jar");
					copyFile(
							templatePath + "/lib/common/log4j-1.2.16.jar",
							workspace.getRoot().getLocation().toString()
									+ "/"
									+ webProjectName
									+ "/WebContent/WEB-INF/lib/log4j-1.2.16.jar");

					/*
					 * Copy Database Lib
					 */

					copyDatabaseLib(webProjectName, templatePath, workspace,
							data);

					// }
				}

				/*
				 * Application Layer - Rest Service
				 */

				if (applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
						&& !applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {

					String filePath = templatePath
							+ "/ApplicationLayer/RestService/web.xml";
					StringBuilder value = loadDeploymentDescriptorData(filePath);
					data.put("restServiceConfiguration", value);

					/*
					 * Copying the libraries
					 */
					copyDirectory(templatePath + "/lib/Rest Service", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");
				}

				/*
				 * Application Layer - SOAP Service
				 */
				if (applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()
						&& !applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
					createFolder(container, "src/com");
					createFolder(container, "src/com/app");
					createFolder(container, "src/com/app/customer");
					createFolder(container, "src/com/app/customer/stub");
					copySOAPStubFiles(templatePath, workspace, data,
							webProjectName);

					String filePath = templatePath
							+ "/ApplicationLayer/SoapService/web.xml";
					StringBuilder value = loadDeploymentDescriptorData(filePath);
					data.put("soapServiceConfiguration", value);

					/*
					 * Copying the libraries
					 */
					copyDirectory(templatePath + "/lib/Soap Service", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");
				}

				/*
				 * if (!applicationFrameworkSelectorWizard.getBtnEjb()
				 * .getSelection() &&
				 * !persistenceFrameworkSelectorWizard.getBtnJpa()
				 * .getSelection()) {
				 */
				if (!applicationFrameworkSelectorWizard.getBtnEjb()
						.getSelection()) {
					/*
					 * Copying common libraries
					 */
					copyDirectory(templatePath + "/lib/common", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");
					copyDirectory(templatePath + "/lib/Rest Service", workspace
							.getRoot().getLocation().toString()
							+ "/" + webProjectName + "/WebContent/WEB-INF/lib/");
				}

				/*
				 * Copying common Files
				 */
				if ((presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText()
						.equals("SPA-Angular JS") && applicationFrameworkSelectorWizard
						.getBtnSoapService().getSelection())
						|| (!presentationFrameworkSelectorWizard
								.getComboWebAppFramework().getText()
								.equals("SPA-Angular JS"))) {
					createFolder(container, "src/com");
					createFolder(container, "src/com/app");
					createFolder(container, "src/com/app/customer");
					createFolder(container, "src/com/app/customer/vo");

					if (!applicationFrameworkSelectorWizard.getBtnEjb()
							.getSelection()
							&& !persistenceFrameworkSelectorWizard.getBtnJpa()
									.getSelection()) {
						copyTemplate(
								templatePath
										+ "/common/src/com/app/customer/vo/Customer.java.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/vo/Customer.java",
								data);
					} else if (!applicationFrameworkSelectorWizard.getBtnEjb()
							.getSelection()) {
						copyTemplate(
								templatePath
										+ "/DataLayer/JPA/com/app/customer/vo/Customer.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ webProjectName
										+ "/src/com/app/customer/vo/Customer.java",
								data);
					}

				}
				createFolder(container, "src/com");
				createFolder(container, "src/com/app");
				createFolder(container, "src/com/app/framework");
				copyCommonFiles(templatePath, workspace, data, webProjectName);

				/*
				 * Copying web.xml file.
				 */
				String xmlTemplateFile = templatePath
						+ "/common/presentation/web.xml.ftl";
				String outputFile = workspace.getRoot().getLocation()
						.toString()
						+ "/" + webProjectName + "/WebContent/WEB-INF/web.xml";
				System.out.println("----===>>>"
						+ data.get("springMVCConfiguration"));
				copyTemplate(xmlTemplateFile, outputFile, data);

				/*
				 * Cross Cutting Features - Instrumentation
				 */
				/*
				 * if
				 * (crosscuttingConcernsSelectorWizard.getBtnInstrumentation()
				 * .getSelection()) { copyCrossCuttingFeatureFiles(templatePath,
				 * workspace, webProjectName); copyDirectory(templatePath +
				 * "/lib/Instrumentation",
				 * workspace.getRoot().getLocation().toString() + "/" +
				 * webProjectName + "/WebContent/WEB-INF/lib/"); }
				 */

			} catch (Exception ioe) {
				ioe.printStackTrace();
				log.error("Project is not created" + ioe.getMessage());
				IStatus status = new Status(IStatus.ERROR,
						"JavaSoftwareFactory", IStatus.OK,
						ioe.getLocalizedMessage(), ioe);
				throw new CoreException(status);
			}

			addWebNatures(proj);
			BasicNewProjectResourceWizard
					.updatePerspective(_configurationElement);
			IResource resour = ResourcesPlugin.getWorkspace().getRoot();

			resour.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception ioe) {
			ioe.printStackTrace();
			log.error("Excepton in createWebProject method" + ioe.getMessage());
			IStatus status = new Status(IStatus.ERROR, "JavaSoftwareFactory",
					IStatus.OK, ioe.getLocalizedMessage(), ioe);
			throw new CoreException(status);
		}
	}

	private void createWebServiceProject(IProject proj) throws CoreException,
			OperationCanceledException {
		try {
			System.out.println("Inside Create Web Project");
			URI projectURI = (!newProjectWizard.useDefaults()) ? newProjectWizard
					.getLocationURI() : null;
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			if (proj == null) {
				throw new Exception("Application Exception : IProject is null");
			}
			projDesc = workspace.newProjectDescription(proj.getName());
			projDesc.setLocationURI(projectURI);
			proj.create(projDesc, null);
			proj.open(IResource.BACKGROUND_REFRESH, null);

			if (!applicationFrameworkSelectorWizard.getBtnRestService()
					.getSelection()
					&& !applicationFrameworkSelectorWizard.getBtnSoapService()
							.getSelection()) {
				IFacetedProject iFacetedProject = (IFacetedProject) ProjectFacetsManager
						.create(proj.getProject(), true, null);
				IProjectFacet JAVA_FACET = ProjectFacetsManager
						.getProjectFacet("jst.java");
				iFacetedProject.installProjectFacet(
						JAVA_FACET.getVersion("1.7"), null, monitor);
				addJavaNatures(proj);

			} else {
				IFacetedProject iFacetedProject = (IFacetedProject) ProjectFacetsManager
						.create(proj.getProject(), true, null);

				IProjectFacet JAVA_FACET = ProjectFacetsManager
						.getProjectFacet("jst.java");
				IProjectFacet WEB_FACET = ProjectFacetsManager
						.getProjectFacet("jst.web");

				iFacetedProject.installProjectFacet(
						JAVA_FACET.getVersion("1.7"), null, monitor);
				iFacetedProject.installProjectFacet(
						WEB_FACET.getVersion("3.0"), null, monitor);
				addWebNatures(proj);
			}

			/*
			 * Generating Files for Service Project from template
			 */

			try {
				IContainer container = (IContainer) proj;

				createFolder(container, "src/com");
				createFolder(container, "src/com/app");
				createFolder(container, "src/com/app/customer");
				createFolder(container, "src/com/app/customer/vo");
				createFolder(container, "src/com/app/customer/util");
				createFolder(container, "src/com/app/customer/service");

				log.info("WORKSPACE : ", workspace.getRoot().getLocation()
						.toString());

				System.out.println("Webservice workspace------->>>>>>>>>."
						+ workspace.getRoot().getLocation().toString());
				log.info("Application Name : " + serviceProjectName);
				String templatePath = ConfigHandler.getInstance()
						.getSystemProperty("template.location");
				log.info("Template Path : " + templatePath);

				Map<String, Object> data = new HashMap<String, Object>();
				data.put("serviceProjectName", serviceProjectName);
				data.put("projectName", serviceProjectName);
				data.put("serverHost", "http://localhost:8080/");
				data.put("importDateTimeFormat", "");
				data.put("dateTimeFormatAnnotation", "");
				data.put("jsonFormatAnnotation", "");
				data.put("importJsonFormat", "");
				data.put("restServiceConfiguration", "");
				data.put("springMVCConfiguration", "");
				data.put("JSFConfiguration", "");
				data.put("StrutsConfiguration", "");
				data.put("soapServiceConfiguration", "");
				data.put("welcomeFile", "");
				data.put("instrumentationBean", "");
				data.put("Constant_Rest", "");
				data.put("APP_NAME", "");
				// data.put("ApplicationContext", "");
				data.put("EJB_Imports", "");
				data.put("Imports", "");
				data.put("JNDI_Lookup", "");
				data.put("Stateless", "");
				data.put("Remote", "");
				data.put("EJB", "");
				data.put("JTA_Datasource", "");
				data.put("url", url);
				data.put("userName", userName);
				data.put("password", password);
				data.put("Driver", "");
				data.put("Dialect", "");
				data.put("dataBaseLib", "");
				data.put("springLib", "");
				data.put("jbossLib", "");
				data.put("hibernateLib", "");
				data.put("instrumentationLib", "");
				data.put("restLib", "");
				data.put("soapLib", "");
				data.put("springMvcLib", "");
				data.put("jsfLib", "");
				data.put("strutsLib", "");
				data.put("pojoLib", "");
				data.put("jpaLib", "");
				data.put("additionalLib", "");
				data.put("RestEasyConfig", "");
				data.put(
						"Absolute_Ordering",
						"<absolute-ordering>\n<name>HJSF_Framework</name>\n<others/>\n</absolute-ordering>");
				data.put("Rest_URL", "rest");
				data.put("Service", "com.app.customer.service.CustomerImpl");
				data.put("webServiceReferer", "Service/");
				data.put("validationLib", "");
				data.put("cacheLib", "");
				data.put("notificationLib", "");

				/*
				 * Spring MVC Code Copying
				 */
				if (presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText().equals("Spring")) {
					/*
					 * WebContent
					 */
					if (applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							|| applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						copyTemplate(
								templatePath
										+ "/PresentationLayer/Spring MVC/common/WEB-INF/web.xml.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/" + serviceProjectName
										+ "/WebContent/WEB-INF/web.xml", data);
						String filePath = workspace.getRoot().getLocation()
								.toString()
								+ "/"
								+ serviceProjectName
								+ "/WebContent/WEB-INF/web.xml";
						StringBuilder value = loadDeploymentDescriptorData(filePath);
						data.put("springMVCConfiguration", value);

					}

					/*
					 * Src
					 */
					copyDirectory(templatePath
							+ "/PresentationLayer/Spring MVC/common/src",
							workspace.getRoot().getLocation().toString() + "/"
									+ serviceProjectName + "/src/");

					if (!applicationFrameworkSelectorWizard.getBtnEjb()
							.getSelection()
							&& !persistenceFrameworkSelectorWizard.getBtnJpa()
									.getSelection()) {
						data.put("importDateTimeFormat",
								"import org.springframework.format.annotation.DateTimeFormat;");
						data.put("dateTimeFormatAnnotation",
								"@DateTimeFormat(pattern = \"dd/MM/yyyy\")");

						/*
						 * Copying the libraries
						 */

						if (!applicationFrameworkSelectorWizard
								.getBtnRestService().getSelection()
								&& !applicationFrameworkSelectorWizard
										.getBtnSoapService().getSelection()) {
							createFolder(container, "/lib/");
							copyDirectory(templatePath + "/lib/Spring/common",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ serviceProjectName
											+ "/lib/");

							String filePath1 = templatePath
									+ "/PresentationLayer/CommonClasspath/Spring.classpath";
							StringBuilder value1 = loadDeploymentDescriptorData(filePath1);
							data.put("springLib", value1);

							copyDirectory(templatePath + "/lib/Spring/MVC",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ serviceProjectName
											+ "/lib/");

							String filePath2 = templatePath
									+ "/PresentationLayer/CommonClasspath/SpringMvc.classpath";
							StringBuilder value2 = loadDeploymentDescriptorData(filePath2);
							data.put("springMvcLib", value2);

						} else {
							copyDirectory(templatePath + "/lib/Spring/common",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ serviceProjectName
											+ "/WebContent/WEB-INF/lib/");
							copyDirectory(templatePath + "/lib/Spring/MVC",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ serviceProjectName
											+ "/WebContent/WEB-INF/lib/");
						}
					} else if (applicationFrameworkSelectorWizard.getBtnEjb()
							.getSelection()) {
						/*
						 * Copying the libraries
						 */
						copyFile(
								templatePath + "/lib/common/jstl-1.2.jar",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ serviceProjectName
										+ "/WebContent/WEB-INF/lib/jstl-1.2.jar");
						copyFile(
								templatePath
										+ "/lib/Spring/MVC/spring-web-4.1.6.RELEASE.jar",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ serviceProjectName
										+ "/WebContent/WEB-INF/lib/spring-web-4.1.6.RELEASE.jar");
						copyFile(
								templatePath
										+ "/lib/Spring/MVC/spring-webmvc-4.1.6.RELEASE.jar",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ serviceProjectName
										+ "/WebContent/WEB-INF/lib/spring-webmvc-4.1.6.RELEASE.jar");
					} else if (persistenceFrameworkSelectorWizard.getBtnJpa()
							.getSelection()
							&& (applicationFrameworkSelectorWizard
									.getBtnRestService().getSelection() || applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection())) {
						/*
						 * Copying the libraries
						 */
						copyFile(
								templatePath + "/lib/common/jstl-1.2.jar",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ serviceProjectName
										+ "/WebContent/WEB-INF/lib/jstl-1.2.jar");
						copyDirectory(templatePath + "/lib/Spring/common",
								workspace.getRoot().getLocation().toString()
										+ "/" + serviceProjectName
										+ "/WebContent/WEB-INF/lib/");
						copyDirectory(templatePath + "/lib/Spring/MVC",
								workspace.getRoot().getLocation().toString()
										+ "/" + serviceProjectName
										+ "/WebContent/WEB-INF/lib/");
					}

				}

				/*
				 * Application Layer - Spring IOC
				 */
				if (applicationFrameworkSelectorWizard.getBtnSpring()
						.getSelection()) {

					copySpringIOCFiles(templatePath, workspace, data,
							serviceProjectName);
					/*
					 * Copying the libraries
					 */
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						createFolder(container, "lib");
						copyDirectory(templatePath + "/lib/Spring/common",
								workspace.getRoot().getLocation().toString()
										+ "/" + serviceProjectName + "/lib/");
						String filePath3 = templatePath
								+ "/PresentationLayer/CommonClasspath/Spring.classpath";
						StringBuilder value1 = loadDeploymentDescriptorData(filePath3);
						data.put("springLib", value1);
					}

					if (!applicationFrameworkSelectorWizard.getBtnEjb()
							.getSelection()
							&& (applicationFrameworkSelectorWizard
									.getBtnRestService().getSelection() || applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection())) {
						copyDirectory(templatePath + "/lib/Spring/common",
								workspace.getRoot().getLocation().toString()
										+ "/" + serviceProjectName
										+ "/WebContent/WEB-INF/lib/");
					}
				} else {
					data.put("customerDaoObject", "new CustomerDaoImpl()");
				}

				/*
				 * Application Layer - EJB
				 */
				if (applicationFrameworkSelectorWizard.getBtnEjb()
						.getSelection()) {
					data.put("APP_NAME", earProjectName);
					/**
					 * Set EJB in Web Meta-Inf Manifest Entries
					 */
					copyTemplate(templatePath
							+ "/PresentationLayer/Jsf/META-INF/MANIFEST.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ serviceProjectName
									+ "/WebContent/META-INF/MANIFEST.MF", data);
					/**
					 * JBoss client Lib
					 */
					copyDirectory(templatePath + "/lib/Jboss", workspace
							.getRoot().getLocation().toString()
							+ "/"
							+ serviceProjectName
							+ "/WebContent/WEB-INF/lib/");
				}

				/*
				 * JSF
				 */
				if (presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText().equals("JSF")) {
					/*
					 * WebContent
					 */

					if (applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							|| applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						String filePath = templatePath
								+ "/PresentationLayer/Jsf/common/WEB-INF/web.xml";
						StringBuilder value = loadDeploymentDescriptorData(filePath);
						data.put("JSFConfiguration", value);
						data.put("welcomeFile", "index.jsp");
					}
					/*
					 * Copying the libraries
					 */
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						createFolder(container, "/lib/");
						copyDirectory(templatePath + "/lib/JSF", workspace
								.getRoot().getLocation().toString()
								+ "/" + serviceProjectName + "/lib/");
						String filePath1 = templatePath
								+ "/PresentationLayer/CommonClasspath/Jsf.classpath";
						StringBuilder value1 = loadDeploymentDescriptorData(filePath1);
						data.put("jsfLib", value1);

					} else {
						copyDirectory(templatePath + "/lib/JSF", workspace
								.getRoot().getLocation().toString()
								+ "/"
								+ serviceProjectName
								+ "/WebContent/WEB-INF/lib/");
					}
				}

				/*
				 * Struts - Presentation Layer
				 */
				if (presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText().equals("Struts")) {
					/*
					 * WebContent
					 */

					if (applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							|| applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						String filePath = templatePath
								+ "/PresentationLayer/Struts/common/WEB-INF/web.xml";
						StringBuilder value = loadDeploymentDescriptorData(filePath);
						data.put("welcomeFile", "index.jsp");
						data.put("StrutsConfiguration", value);
					}
					if (applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						data.put(
								"Constant_Rest",
								"<constant name=\"struts.action.excludePattern\" value=\"/rest/.*,/BusinessDelegator.*,/serviceController.*\"/>");
					} else if (applicationFrameworkSelectorWizard
							.getBtnSoapService().getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnRestService().getSelection()) {
						data.put(
								"Constant_Rest",
								"<constant name=\"struts.action.excludePattern\" value=\"/customerService.*\"/>");
					} else if (applicationFrameworkSelectorWizard
							.getBtnRestService().getSelection()
							&& applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						data.put(
								"Constant_Rest",
								"<constant name=\"struts.action.excludePattern\" value=\"/rest/.*,/customerService.*\"/>");
					} else {
						data.put(
								"Constant_Rest",
								"<constant name=\"struts.action.excludePattern\" value=\"/serviceController.*\"/>");
					}
					/*
					 * Src
					 */
					String strutsPath = null;
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						strutsPath = "common";
					} else if (applicationFrameworkSelectorWizard
							.getBtnRestService().getSelection()) {
						strutsPath = "Rest";
					} else if (applicationFrameworkSelectorWizard
							.getBtnSoapService().getSelection()) {
						strutsPath = "Soap";
					}

					/*
					 * Copying the libraries
					 */
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						copyDirectory(templatePath + "/lib/Struts", workspace
								.getRoot().getLocation().toString()
								+ "/" + serviceProjectName + "/lib/");
						String filePath1 = templatePath
								+ "/PresentationLayer/CommonClasspath/Struts.classpath";
						StringBuilder value1 = loadDeploymentDescriptorData(filePath1);
						data.put("strutsLib", value1);

					} else {
						copyDirectory(templatePath + "/lib/Struts", workspace
								.getRoot().getLocation().toString()
								+ "/"
								+ serviceProjectName
								+ "/WebContent/WEB-INF/lib/");
					}

				}

				/*
				 * Application Layer - Rest Service
				 */

				if (applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()) {

					copyRestServiceFiles(templatePath, workspace, data,
							serviceProjectName);

					String filePath = templatePath
							+ "/ApplicationLayer/RestService/web.xml";
					StringBuilder value = loadDeploymentDescriptorData(filePath);
					data.put("restServiceConfiguration", value);

					/*
					 * Copying the libraries
					 */
					copyDirectory(templatePath + "/lib/Rest Service", workspace
							.getRoot().getLocation().toString()
							+ "/"
							+ serviceProjectName
							+ "/WebContent/WEB-INF/lib/");
				}

				/*
				 * Application Layer - SOAP Service
				 */
				if (applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()) {

					copySOAPServiceFiles(templatePath, workspace, data,
							serviceProjectName);

					copyFile(
							templatePath
									+ "/ApplicationLayer/SoapService/WEB-INF/sun-jaxws.xml",
							workspace.getRoot().getLocation().toString() + "/"
									+ serviceProjectName
									+ "/WebContent/WEB-INF/sun-jaxws.xml");
					String filePath = templatePath
							+ "/ApplicationLayer/SoapService/web.xml";
					StringBuilder value = loadDeploymentDescriptorData(filePath);
					data.put("soapServiceConfiguration", value);

					/*
					 * Copying the libraries
					 */
					copyDirectory(templatePath + "/lib/Soap Service", workspace
							.getRoot().getLocation().toString()
							+ "/"
							+ serviceProjectName
							+ "/WebContent/WEB-INF/lib/");
				}

				/*
				 * Data Layer - Hibernate
				 */
				if (persistenceFrameworkSelectorWizard.getBtnHibernate()
						.getSelection()
						&& !applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
					createFolder(container, "src/com");
					createFolder(container, "src/com/app");
					createFolder(container, "src/com/app/customer");
					createFolder(container, "src/com/app/customer/dao");
					copyHibernateFiles(templatePath, workspace, data,
							serviceProjectName);
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						/*
						 * Copying the libraries
						 */
						copyDirectory(templatePath + "/lib/Hibernate",
								workspace.getRoot().getLocation().toString()
										+ "/" + serviceProjectName + "/lib/");

						String filePath1 = templatePath
								+ "/PresentationLayer/CommonClasspath/Hibernate.classpath";
						StringBuilder value2 = loadDeploymentDescriptorData(filePath1);
						data.put("hibernateLib", value2);

						if (dataBaseSelectorWizard.btnMysql.getSelection()) {
							copyDirectory(templatePath + "/lib/MySQL",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ serviceProjectName
											+ "/lib/");
							data.put(
									"dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/mysql-connector-java-5.1.15-bin.jar\"/>");
						} else if (dataBaseSelectorWizard.btnMicrosoftSqlServer
								.getSelection()) {
							copyDirectory(templatePath + "/lib/SQL", workspace
									.getRoot().getLocation().toString()
									+ "/" + serviceProjectName + "/lib/");
							data.put("dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/sqljdbc4-4.0.jar\"/>");
						} else if (dataBaseSelectorWizard.getBtnOracle()
								.getSelection()) {
							copyDirectory(templatePath + "/lib/Oracle",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ serviceProjectName
											+ "/lib/");
							data.put("dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/ojdbc6.jar\"/>");
						}
					} else {

						/*
						 * Copying the libraries
						 */

						copyDirectory(templatePath + "/lib/Hibernate",
								workspace.getRoot().getLocation().toString()
										+ "/" + serviceProjectName
										+ "/WebContent/WEB-INF/lib/");
						/*
						 * Copy Database Lib
						 */

						if (!presentationFrameworkSelectorWizard
								.getBtnDesktopApplication().getSelection()) {
							copyDatabaseLib(serviceProjectName, templatePath,
									workspace, data);
						} else {
							if (dataBaseSelectorWizard.btnMysql.getSelection()) {
								copyDirectory(templatePath + "/lib/MySQL",
										workspace.getRoot().getLocation()
												.toString()
												+ "/"
												+ serviceProjectName
												+ "/WebContent/WEB-INF/lib/");
							} else if (dataBaseSelectorWizard.btnMicrosoftSqlServer
									.getSelection()) {
								copyDirectory(templatePath + "/lib/SQL",
										workspace.getRoot().getLocation()
												.toString()
												+ "/"
												+ serviceProjectName
												+ "/WebContent/WEB-INF/lib/");
							} else if (dataBaseSelectorWizard.getBtnOracle()
									.getSelection()) {
								copyDirectory(templatePath + "/lib/Oracle",
										workspace.getRoot().getLocation()
												.toString()
												+ "/"
												+ serviceProjectName
												+ "/WebContent/WEB-INF/lib/");
							}
						}
					}

				}

				/*
				 * Data Layer - JPA
				 */
				if (persistenceFrameworkSelectorWizard.getBtnJpa()
						.getSelection()
						&& !applicationFrameworkSelectorWizard.getBtnEjb()
								.getSelection()) {
					createFolder(container, "src/com/app/framework");
					createFolder(container, "src/com/app/customer/util");
					createFolder(container, "src/com/app/customer/dao");
					copyTemplate(
							templatePath
									+ "/ApplicationLayer/Ejb/common/com/app/customer/util/GlobalConstants.ftl",
							workspace.getRoot().getLocation().toString()
									+ "/"
									+ serviceProjectName
									+ "/src/com/app/customer/util/GlobalConstants.java",
							data);
					if (presentationFrameworkSelectorWizard
							.getComboWebAppFramework().getText()
							.equals("Spring")) {
						data.put("importDateTimeFormat",
								"import org.springframework.format.annotation.DateTimeFormat;");
						data.put("dateTimeFormatAnnotation",
								"@DateTimeFormat(pattern = \"dd/MM/yyyy\")");
						copyTemplate(
								templatePath
										+ "/DataLayer/JPA/com/app/customer/vo/Customer.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ serviceProjectName
										+ "/src/com/app/customer/vo/Customer.java",
								data);
					} else {
						copyTemplate(
								templatePath
										+ "/DataLayer/JPA/com/app/customer/vo/Customer.ftl",
								workspace.getRoot().getLocation().toString()
										+ "/"
										+ serviceProjectName
										+ "/src/com/app/customer/vo/Customer.java",
								data);
					}

					String jpaPath = templatePath
							+ "/PresentationLayer/CommonClasspath/JPA.classpath";
					StringBuilder value = loadDeploymentDescriptorData(jpaPath);
					data.put("jpaLib", value);

					/*
					 * Copy JPA Files
					 */
					copyJpaFiles(serviceProjectName, templatePath, workspace,
							data, container);
					/*
					 * Copying the libraries
					 */
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {
						copyDirectory(templatePath + "/lib/JPA", workspace
								.getRoot().getLocation().toString()
								+ "/" + serviceProjectName + "/lib/");

						if (!presentationFrameworkSelectorWizard
								.getComboWebAppFramework().getText()
								.equals("Struts")) {
							data.put("additionalLib",
									"<classpathentry kind=\"lib\" path=\"lib/javassist-3.18.1-GA.jar\"/>");
							copyFile(templatePath
									+ "/lib/Struts/javassist-3.18.1-GA.jar",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ serviceProjectName
											+ "/lib/javassist-3.18.1-GA.jar");
						}

						if (dataBaseSelectorWizard.btnMysql.getSelection()) {
							data.put(
									"dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/mysql-connector-java-5.1.15-bin.jar\"/>");
							copyDirectory(templatePath + "/lib/MySQL",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ serviceProjectName
											+ "/lib/");
						} else if (dataBaseSelectorWizard.btnMicrosoftSqlServer
								.getSelection()) {
							data.put("dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/sqljdbc4-4.0.jar\"/>");
							copyDirectory(templatePath + "/lib/SQL", workspace
									.getRoot().getLocation().toString()
									+ "/" + serviceProjectName + "/lib/");
						} else if (dataBaseSelectorWizard.getBtnOracle()
								.getSelection()) {
							data.put("dataBaseLib",
									"<classpathentry kind=\"lib\" path=\"lib/ojdbc6.jar\"/>");
							copyDirectory(templatePath + "/lib/Oracle",
									workspace.getRoot().getLocation()
											.toString()
											+ "/"
											+ serviceProjectName
											+ "/lib/");
						}
					} else {
						copyJpaLib(serviceProjectName, templatePath, workspace,
								data);
					}

				}

				/*
				 * Copying common libraries
				 */

				if (!applicationFrameworkSelectorWizard.getBtnEjb()
						.getSelection()
						&& persistenceFrameworkSelectorWizard.getBtnJpa()
								.getSelection()) {
					if (!applicationFrameworkSelectorWizard.getBtnRestService()
							.getSelection()
							&& !applicationFrameworkSelectorWizard
									.getBtnSoapService().getSelection()) {

						copyFile(templatePath + "/lib/common/log4j-1.2.16.jar",
								workspace.getRoot().getLocation().toString()
										+ "/" + serviceProjectName
										+ "/lib/log4j-1.2.16.jar");
						copyFile(templatePath
								+ "/lib/common/HJSF-Framework.jar", workspace
								.getRoot().getLocation().toString()
								+ "/"
								+ serviceProjectName
								+ "/lib/HJSF-Framework.jar");
						copyFile(templatePath + "/lib/common/HexLogger.jar",
								workspace.getRoot().getLocation().toString()
										+ "/" + serviceProjectName
										+ "/lib/HexLogger.jar");
						copyFile(templatePath + "/lib/common/jstl-1.2.jar",
								workspace.getRoot().getLocation().toString()
										+ "/" + serviceProjectName
										+ "/lib/jstl-1.2.jar");
						copyFile(templatePath
								+ "/lib/common/paranamer-2.6.1.jar", workspace
								.getRoot().getLocation().toString()
								+ "/"
								+ serviceProjectName
								+ "/lib/paranamer-2.6.1.jar");

					} else {
						copyDirectory(templatePath + "/lib/common", workspace
								.getRoot().getLocation().toString()
								+ "/"
								+ serviceProjectName
								+ "/WebContent/WEB-INF/lib/");
						copyDirectory(templatePath + "/lib/Rest Service",
								workspace.getRoot().getLocation().toString()
										+ "/" + serviceProjectName
										+ "/WebContent/WEB-INF/lib/");
					}
				} else if (!applicationFrameworkSelectorWizard
						.getBtnRestService().getSelection()
						&& !applicationFrameworkSelectorWizard
								.getBtnSoapService().getSelection()) {

					copyFile(templatePath + "/lib/common/log4j-1.2.16.jar",
							workspace.getRoot().getLocation().toString() + "/"
									+ serviceProjectName
									+ "/lib/log4j-1.2.16.jar");
					copyFile(templatePath + "/lib/common/HJSF-Framework.jar",
							workspace.getRoot().getLocation().toString() + "/"
									+ serviceProjectName
									+ "/lib/HJSF-Framework.jar");
					copyFile(templatePath + "/lib/common/HexLogger.jar",
							workspace.getRoot().getLocation().toString() + "/"
									+ serviceProjectName + "/lib/HexLogger.jar");
					copyFile(templatePath + "/lib/common/jstl-1.2.jar",
							workspace.getRoot().getLocation().toString() + "/"
									+ serviceProjectName + "/lib/jstl-1.2.jar");

					copyFile(templatePath + "/lib/common/paranamer-2.6.1.jar",
							workspace.getRoot().getLocation().toString() + "/"
									+ serviceProjectName
									+ "/lib/paranamer-2.6.1.jar");
				} else {
					copyDirectory(templatePath + "/lib/common", workspace
							.getRoot().getLocation().toString()
							+ "/"
							+ serviceProjectName
							+ "/WebContent/WEB-INF/lib/");
					copyDirectory(templatePath + "/lib/Rest Service", workspace
							.getRoot().getLocation().toString()
							+ "/"
							+ serviceProjectName
							+ "/WebContent/WEB-INF/lib/");
				}

				/*
				 * Copying common Files
				 */
				if (!persistenceFrameworkSelectorWizard.getBtnJpa()
						.getSelection()) {
					copyTemplate(
							templatePath
									+ "/common/src/com/app/customer/vo/Customer.java.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ serviceProjectName
									+ "/src/com/app/customer/vo/Customer.java",
							data);
				}
				/*
				 * createFolder(container, "src/com/app/framework");
				 * copyCommonFiles(templatePath, workspace, data,
				 * serviceProjectName);
				 */

				/*
				 * Copying web.xml file.
				 */
				if (applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
						|| applicationFrameworkSelectorWizard
								.getBtnSoapService().getSelection()) {
					String xmlTemplateFile = templatePath
							+ "/common/service/web.xml.ftl";
					String outputFile = workspace.getRoot().getLocation()
							.toString()
							+ "/"
							+ serviceProjectName
							+ "/WebContent/WEB-INF/web.xml";
					System.out.println("----===>>>"
							+ data.get("springMVCConfiguration"));
					copyTemplate(xmlTemplateFile, outputFile, data);
				}

				/*
				 * Cross Cutting Features - Instrumentation
				 */
				/*
				 * if
				 * (crosscuttingConcernsSelectorWizard.getBtnInstrumentation()
				 * .getSelection()) { copyCrossCuttingFeatureFiles(templatePath,
				 * workspace, serviceProjectName); if
				 * (!applicationFrameworkSelectorWizard.getBtnRestService()
				 * .getSelection() && !applicationFrameworkSelectorWizard
				 * .getBtnSoapService().getSelection()) {
				 * copyDirectory(templatePath + "/lib/Instrumentation",
				 * workspace.getRoot().getLocation().toString() + "/" +
				 * serviceProjectName + "/lib/"); } else {
				 * copyDirectory(templatePath + "/lib/Instrumentation",
				 * workspace.getRoot().getLocation().toString() + "/" +
				 * serviceProjectName + "/WebContent/WEB-INF/lib/"); } }
				 */

				/*
				 * Coping classpath for java project
				 */
				if (!applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
						&& !applicationFrameworkSelectorWizard
								.getBtnSoapService().getSelection()) {
					copyTemplate(
							templatePath
									+ "/PresentationLayer/CommonClasspath/Common.classpath.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ serviceProjectName + "/.classpath/", data);
				}

			} catch (Exception ioe) {
				ioe.printStackTrace();
				log.error("Project is not created" + ioe.getMessage());
				IStatus status = new Status(IStatus.ERROR,
						"JavaSoftwareFactory", IStatus.OK,
						ioe.getLocalizedMessage(), ioe);
				throw new CoreException(status);
			}
			BasicNewProjectResourceWizard
					.updatePerspective(_configurationElement);
			IResource resour = ResourcesPlugin.getWorkspace().getRoot();

			resour.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception ioe) {
			ioe.printStackTrace();
			log.error("Excepton in createWebProject method" + ioe.getMessage());
			IStatus status = new Status(IStatus.ERROR, "JavaSoftwareFactory",
					IStatus.OK, ioe.getLocalizedMessage(), ioe);
			throw new CoreException(status);
		}
	}

	private void createEnterpriseProject() throws CoreException,
			OperationCanceledException {
		try {
			log.debug("Inside Create EnterpriseProject");
			/*
			 * Create EAR Project Handler
			 */
			final IProject earProjectHandle = ResourcesPlugin.getWorkspace()
					.getRoot().getProject(newProjectWizard.getProjectName());
			earProjectName = earProjectHandle.getProject().getName();
			/*
			 * Create EJB Project Handler
			 */
			final IProject ejbProjectHandle = ResourcesPlugin.getWorkspace()
					.getRoot()
					.getProject(newProjectWizard.getProjectName() + "EJB");
			ejbProjectName = ejbProjectHandle.getProject().getName();
			/*
			 * Create EJB-Project
			 */
			URI projectURI = (!newProjectWizard.useDefaults()) ? newProjectWizard
					.getLocationURI() : null;
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			projDesc = workspace.newProjectDescription(ejbProjectHandle
					.getName());
			projDesc.setLocationURI(projectURI);
			ejbProjectHandle.create(projDesc, null);
			ejbProjectHandle.open(IResource.BACKGROUND_REFRESH, null);

			IFacetedProject iFacetedProject = (IFacetedProject) ProjectFacetsManager
					.create(ejbProjectHandle.getProject(), true, null);
			IProjectFacet JAVA_FACET = ProjectFacetsManager
					.getProjectFacet("jst.java");
			IProjectFacet EJB_FACET = ProjectFacetsManager
					.getProjectFacet("jst.ejb");
			iFacetedProject.installProjectFacet(JAVA_FACET.getDefaultVersion(),
					null, null);
			iFacetedProject.installProjectFacet(EJB_FACET.getVersion("3.0"),
					null, null);
			IContainer container = (IContainer) ejbProjectHandle;
			createFolder(container, "src/com");
			createFolder(container, "src/com/app");
			createFolder(container, "src/com/app/customer");
			createFolder(container, "src/com/app/customer/dao");
			createFolder(container, "src/com/app/customer/service");
			createFolder(container, "src/com/app/customer/util");
			createFolder(container, "src/com/app/customer/vo");
			createFolder(container, "src/com/app/framework");
			createFolder(container, "src/com/app/framework/exception");
			createFolder(container, "src/com/app/framework/util");
			createFolder(container, "src/META-INF");

			log.info("WORKSPACE : ", workspace.getRoot().getLocation()
					.toString());
			log.info("WORKSPACE : "
					+ workspace.getRoot().getLocation().toString());
			log.info("Application Name : " + ejbProjectName);
			String templatePath = ConfigHandler.getInstance()
					.getSystemProperty("template.location");
			log.info("Template Path : " + templatePath);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("projectName", "");
			data.put("serverHost", "http://localhost:8080/");
			data.put("APP_NAME", earProjectName);
			data.put("importDateTimeFormat", "");
			data.put("importJsonFormat", "");
			data.put("jsonFormatAnnotation", "");
			data.put("dateTimeFormatAnnotation", "");
			data.put("JTA_Datasource", "");
			data.put("MODULE", "");
			data.put("SUB_DEPLOYMENT", "");
			data.put("url", url);
			data.put("userName", userName);
			data.put("password", password);
			data.put("Driver", "");
			data.put("Dialect", "");
			/*
			 * Copy EJB Files
			 */
			copyEJBFiles(ejbProjectName, templatePath, workspace, data,
					container);

			/*
			 * Application Layer - EJB Data layer - Hibernate
			 */
			if (applicationFrameworkSelectorWizard.getBtnEjb().getSelection()
					&& persistenceFrameworkSelectorWizard.getBtnHibernate()
							.getSelection()) {
				System.out.println("inside EJB selection");
				copyFile(
						templatePath
								+ "/DataLayer/Hibernate/com/app/customer/dao/CustomerDao.java",
						workspace.getRoot().getLocation().toString() + "/"
								+ ejbProjectName
								+ "/src/com/app/customer/dao/CustomerDao.java");
				copyFile(
						templatePath
								+ "/DataLayer/Hibernate/com/app/customer/dao/CustomerDaoImpl.java",
						workspace.getRoot().getLocation().toString()
								+ "/"
								+ ejbProjectName
								+ "/src/com/app/customer/dao/CustomerDaoImpl.java");
				/*
				 * copyDirectory(templatePath +
				 * "/ApplicationLayer/Ejb/src/com/app/customer/service",
				 * workspace.getRoot().getLocation().toString() + "/" +
				 * ejbProjectName + "/src/com/app/customer/service");
				 */
				copyDirectory(templatePath
						+ "/DataLayer/Hibernate/com/app/customer/util",
						workspace.getRoot().getLocation().toString() + "/"
								+ ejbProjectName + "/src/com/app/customer/util");
				if (dataBaseSelectorWizard.btnMysql.getSelection()) {
					data.put("Driver", "com.mysql.jdbc.Driver");
					data.put("Dialect", "org.hibernate.dialect.MySQLDialect");
				} else if (dataBaseSelectorWizard.getBtnMicrosoftSqlServer()
						.getSelection()) {
					data.put("Driver",
							"com.microsoft.sqlserver.jdbc.SQLServerDriver");
					data.put("Dialect",
							"org.hibernate.dialect.SQLServerDialect");
				} else if (dataBaseSelectorWizard.getBtnOracle().getSelection()) {
					data.put("Driver", "oracle.jdbc.driver.OracleDriver");
					data.put("Dialect", "org.hibernate.dialect.OracleDialect");
				}
				copyFile(templatePath
						+ "/DataLayer/Hibernate/CustomerApplication.hbm.xml",
						workspace.getRoot().getLocation().toString() + "/"
								+ ejbProjectName
								+ "/src/CustomerApplication.hbm.xml");
				copyTemplate(templatePath
						+ "/DataLayer/Hibernate/hibernate.cfg.ftl", workspace
						.getRoot().getLocation().toString()
						+ "/" + ejbProjectName + "/src/hibernate.cfg.xml", data);
				copyDirectory(templatePath
						+ "/Common/src/com/app/framework/exception", workspace
						.getRoot().getLocation().toString()
						+ "/"
						+ ejbProjectName
						+ "/src/com/app/framework/exception");
				/*
				 * if(!presentationFrameworkSelectorWizard.
				 * getBtnDesktopApplication ().getSelection()){
				 * copyFile(templatePath +
				 * "/ApplicationLayer/Ejb/src/com/app/framework/util/JndiLookup.java"
				 * , workspace.getRoot().getLocation().toString() + "/" +
				 * ejbProjectName +
				 * "/src/com/app/framework/util/JndiLookup.java");
				 * copyTemplate(templatePath +
				 * "/ApplicationLayer/Ejb/src/com/app/framework/util/GlobalConstants.ftl"
				 * , workspace.getRoot().getLocation().toString() + "/" +
				 * ejbProjectName +
				 * "/src/com/app/framework/util/GlobalConstants.java", data); }
				 */
				copyFile(templatePath
						+ "/ApplicationLayer/Ejb/jboss-ejb-client.properties",
						workspace.getRoot().getLocation().toString() + "/"
								+ ejbProjectName
								+ "/src/jboss-ejb-client.properties");
				if (presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText().equals("Spring")) {
					data.put("importDateTimeFormat",
							"import org.springframework.format.annotation.DateTimeFormat;");
					data.put("dateTimeFormatAnnotation",
							"@DateTimeFormat(pattern = \"dd/MM/yyyy\")");
					copyTemplate(
							templatePath
									+ "/common/src/com/app/customer/vo/Customer.java.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ ejbProjectName
									+ "/src/com/app/customer/vo/Customer.java",
							data);
				} else {
					copyTemplate(
							templatePath
									+ "/Common/src/com/app/customer/vo/Customer.java.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ ejbProjectName
									+ "/src/com/app/customer/vo/Customer.java",
							data);
				}
			}

			/*
			 * Application Layer - EJB Data layer - JPA
			 */
			if (applicationFrameworkSelectorWizard.getBtnEjb().getSelection()
					&& persistenceFrameworkSelectorWizard.getBtnJpa()
							.getSelection()) {
				System.out.println("inside EJB selection");
				if (dataBaseSelectorWizard.btnMysql.getSelection()) {
					data.put("JTA_Datasource",
							"<jta-data-source>java:jboss/test-ds</jta-data-source>");
				} else if (dataBaseSelectorWizard.getBtnMicrosoftSqlServer()
						.getSelection()) {
					data.put("JTA_Datasource",
							"<jta-data-source>java:jboss/sql-ds</jta-data-source>");
				} else if (dataBaseSelectorWizard.getBtnOracle().getSelection()) {
					data.put("JTA_Datasource",
							"<jta-data-source>java:jboss/oracle-ds</jta-data-source>");
				}
				/*
				 * Copy EJB Jpa Files
				 */
				copyJpaFiles(ejbProjectName, templatePath, workspace, data,
						container);

				/*
				 * if(!presentationFrameworkSelectorWizard.
				 * getBtnDesktopApplication ().getSelection()){
				 * copyTemplate(templatePath +
				 * "/DataLayer/JPA/com/app/framework/util/GlobalConstants.ftl",
				 * workspace.getRoot().getLocation().toString() + "/" +
				 * ejbProjectName +
				 * "/src/com/app/framework/util/GlobalConstants.java", data);
				 * copyFile(templatePath +
				 * "/DataLayer/JPA/com/app/framework/util/JndiLookup.java",
				 * workspace.getRoot().getLocation().toString() + "/" +
				 * ejbProjectName +
				 * "/src/com/app/framework/util/JndiLookup.java"); }else{
				 * copyTemplate(templatePath +
				 * "/DataLayer/JPA/com/app/framework/util/GlobalConstants.ftl",
				 * workspace.getRoot().getLocation().toString() + "/" +
				 * ejbProjectName +
				 * "/src/com/app/framework/util/GlobalConstants.java", data); }
				 */
				copyFile(templatePath
						+ "/ApplicationLayer/Ejb/jboss-ejb-client.properties",
						workspace.getRoot().getLocation().toString() + "/"
								+ ejbProjectName
								+ "/src/jboss-ejb-client.properties");
				if (presentationFrameworkSelectorWizard
						.getComboWebAppFramework().getText().equals("Spring")) {
					data.put("importDateTimeFormat",
							"import org.springframework.format.annotation.DateTimeFormat;");
					data.put("dateTimeFormatAnnotation",
							"@DateTimeFormat(pattern = \"dd/MM/yyyy\")");
					copyTemplate(
							templatePath
									+ "/DataLayer/JPA/com/app/customer/vo/Customer.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ ejbProjectName
									+ "/src/com/app/customer/vo/Customer.java",
							data);
				} else {
					copyTemplate(
							templatePath
									+ "/DataLayer/JPA/com/app/customer/vo/Customer.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ ejbProjectName
									+ "/src/com/app/customer/vo/Customer.java",
							data);
				}

			}

			/*
			 * Create Web Project Handler
			 */
			if (presentationFrameworkSelectorWizard.getBtnWebApplication()
					.getSelection()) {
				final IProject projectHandle = ResourcesPlugin.getWorkspace()
						.getRoot()
						.getProject(newProjectWizard.getProjectName() + "WEB");
				webProjectName = projectHandle.getProject().getName();

				/*
				 * Create Web Project
				 */

				createWebPresentationProject(projectHandle);
			} else if (presentationFrameworkSelectorWizard
					.getBtnDesktopApplication().getSelection()) {
				if (!applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
						&& !applicationFrameworkSelectorWizard
								.getBtnSoapService().getSelection()) {
					/*
					 * Create Java Project
					 */
					final IProject projectHandle = ResourcesPlugin
							.getWorkspace()
							.getRoot()
							.getProject(
									newProjectWizard.getProjectName()
											+ "Project");
					javaProjectName = projectHandle.getProject().getName();
					createJavaProject(projectHandle);
				} else {

					final IProject webProjectHandle = ResourcesPlugin
							.getWorkspace()
							.getRoot()
							.getProject(
									newProjectWizard.getProjectName() + "WEB");
					webProjectName = webProjectHandle.getProject().getName();

					/*
					 * Create Web Project
					 */

					createWebPresentationProject(webProjectHandle);

					/*
					 * Create Java Project
					 */
					final IProject projectHandle = ResourcesPlugin
							.getWorkspace()
							.getRoot()
							.getProject(
									newProjectWizard.getProjectName()
											+ "Project");
					javaProjectName = projectHandle.getProject().getName();
					createJavaProject(projectHandle);
				}
			}
			log.debug("Inside Create Ear Project");

			/*
			 * Creation of EAR Project
			 */
			projDesc = workspace.newProjectDescription(earProjectHandle
					.getName());
			projDesc.setLocationURI(projectURI);
			earProjectHandle.create(projDesc, null);
			earProjectHandle.open(IResource.BACKGROUND_REFRESH, null);

			iFacetedProject = (IFacetedProject) ProjectFacetsManager.create(
					earProjectHandle.getProject(), true, null);
			IProjectFacet EAR_FACET = ProjectFacetsManager
					.getProjectFacet("jst.ear");
			iFacetedProject.installProjectFacet(EAR_FACET.getDefaultVersion(),
					null, null);
			container = (IContainer) earProjectHandle;
			createFolder(container, "EarContent/lib");
			createFolder(container, "EarContent/META-INF");
			if (presentationFrameworkSelectorWizard.getBtnWebApplication()
					.getSelection()) {
				data.put("MODULE", "<module>" + "<web>" + "<web-uri>"
						+ earProjectName + "WEB.war</web-uri>"
						+ "<context-root>" + earProjectName
						+ "WEB</context-root>" + "</web>" + "</module>");
				data.put("SUB_DEPLOYMENT", "<sub-deployment name=" + '"'
						+ earProjectName + "WEB.war" + '"' + ">"
						+ "<exclusions>"
						+ "<module name=\"org.apache.log4j\"/>"
						+ "</exclusions>" + "</sub-deployment>");
				copyTemplate(templatePath
						+ "/Enterprise Application Archive/application.ftl",
						workspace.getRoot().getLocation().toString() + "/"
								+ earProjectName
								+ "/EarContent/META-INF/application.xml", data);
				copyTemplate(
						templatePath
								+ "/Enterprise Application Archive/jboss-deployment-structure.ftl",
						workspace.getRoot().getLocation().toString()
								+ "/"
								+ earProjectName
								+ "/EarContent/META-INF/jboss-deployment-structure.xml",
						data);
			} else if (presentationFrameworkSelectorWizard
					.getBtnDesktopApplication().getSelection()) {
				if (applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
						|| applicationFrameworkSelectorWizard
								.getBtnSoapService().getSelection()) {
					data.put("MODULE", "<module>" + "<web>" + "<web-uri>"
							+ earProjectName + "WEB.war</web-uri>"
							+ "<context-root>" + earProjectName
							+ "WEB</context-root>" + "</web>" + "</module>");
					data.put("SUB_DEPLOYMENT", "<sub-deployment name=" + '"'
							+ earProjectName + "WEB.war" + '"' + ">"
							+ "<exclusions>"
							+ "<module name=\"org.apache.log4j\"/>"
							+ "</exclusions>" + "</sub-deployment>");
					copyTemplate(
							templatePath
									+ "/Enterprise Application Archive/application.ftl",
							workspace.getRoot().getLocation().toString() + "/"
									+ earProjectName
									+ "/EarContent/META-INF/application.xml",
							data);
				}
				copyTemplate(templatePath
						+ "/Enterprise Application Archive/application.ftl",
						workspace.getRoot().getLocation().toString() + "/"
								+ earProjectName
								+ "/EarContent/META-INF/application.xml", data);
				copyTemplate(
						templatePath
								+ "/Enterprise Application Archive/jboss-deployment-structure.ftl",
						workspace.getRoot().getLocation().toString()
								+ "/"
								+ earProjectName
								+ "/EarContent/META-INF/jboss-deployment-structure.xml",
						data);
			}
			copyTemplate(
					templatePath
							+ "/Enterprise Application Archive/org.eclipse.wst.common.ftl",
					workspace.getRoot().getLocation().toString() + "/"
							+ earProjectName
							+ "/.settings/org.eclipse.wst.common.component",
					data);
			copyFile(templatePath + "/lib/common/log4j-1.2.16.jar", workspace
					.getRoot().getLocation().toString()
					+ "/" + earProjectName + "/EarContent/lib/log4j-1.2.16.jar");
			copyFile(templatePath + "/lib/common/HexLogger.jar", workspace
					.getRoot().getLocation().toString()
					+ "/" + earProjectName + "/EarContent/lib/HexLogger.jar");
			copyFile(templatePath
					+ "/lib/Rest Service/jackson-core-asl-1.9.2.jar", workspace
					.getRoot().getLocation().toString()
					+ "/"
					+ earProjectName
					+ "/EarContent/lib/jackson-core-asl-1.9.2.jar");
			copyFile(templatePath
					+ "/lib/Rest Service/jackson-mapper-asl-1.9.2.jar",
					workspace.getRoot().getLocation().toString() + "/"
							+ earProjectName
							+ "/EarContent/lib/jackson-mapper-asl-1.9.2.jar");
			copyFile(templatePath + "/lib/Rest Service/json-simple-1.1.1.jar",
					workspace.getRoot().getLocation().toString() + "/"
							+ earProjectName
							+ "/EarContent/lib/json-simple-1.1.1.jar");
			copyFile(templatePath + "/lib/common/paranamer-2.6.1.jar",
					workspace.getRoot().getLocation().toString() + "/"
							+ earProjectName
							+ "/EarContent/lib/paranamer-2.6.1.jar");
			copyFile(templatePath + "/lib/common/commons-beanutils.jar",
					workspace.getRoot().getLocation().toString() + "/"
							+ earProjectName
							+ "/EarContent/lib/commons-beanutils.jar");
			copyFile(templatePath + "/lib/common/commons-digester.jar",
					workspace.getRoot().getLocation().toString() + "/"
							+ earProjectName
							+ "/EarContent/lib/commons-digester.jar");
			copyFile(templatePath + "/lib/common/commons-fileupload.jar",
					workspace.getRoot().getLocation().toString() + "/"
							+ earProjectName
							+ "/EarContent/lib/commons-fileupload.jar");
			/*
			 * Copy Database Lib
			 */
			if (dataBaseSelectorWizard.btnMysql.getSelection()) {
				copyDirectory(templatePath + "/lib/MySQL", workspace.getRoot()
						.getLocation().toString()
						+ "/" + earProjectName + "/EarContent/lib/");
			} else if (dataBaseSelectorWizard.btnMicrosoftSqlServer
					.getSelection()) {
				copyDirectory(templatePath + "/lib/SQL", workspace.getRoot()
						.getLocation().toString()
						+ "/" + earProjectName + "/EarContent/lib/");
			} else if (dataBaseSelectorWizard.getBtnOracle().getSelection()) {
				copyDirectory(templatePath + "/lib/Oracle", workspace.getRoot()
						.getLocation().toString()
						+ "/" + earProjectName + "/EarContent/lib/");
			}
			if (persistenceFrameworkSelectorWizard.getBtnHibernate()
					.getSelection()) {
				copyFile(templatePath + "/lib/common/antlr-2.7.5.jar",
						workspace.getRoot().getLocation().toString() + "/"
								+ earProjectName
								+ "/EarContent/lib/antlr-2.7.5.jar");
				copyFile(templatePath + "/lib/common/cglib-nodep-2.1_3.jar",
						workspace.getRoot().getLocation().toString() + "/"
								+ earProjectName
								+ "/EarContent/lib/cglib-nodep-2.1_3.jar");
				copyFile(templatePath + "/lib/common/commons-collections.jar",
						workspace.getRoot().getLocation().toString() + "/"
								+ earProjectName
								+ "/EarContent/lib/commons-collections.jar");
				copyFile(templatePath + "/lib/common/dom4j-1.6.jar", workspace
						.getRoot().getLocation().toString()
						+ "/"
						+ earProjectName
						+ "/EarContent/lib/dom4j-1.6.jar");
				copyDirectory(templatePath + "/lib/Hibernate", workspace
						.getRoot().getLocation().toString()
						+ "/" + earProjectName + "/EarContent/lib/");
			}

			if (persistenceFrameworkSelectorWizard.getBtnJpa().getSelection()) {
				copyFile(templatePath + "/lib/common/antlr-2.7.5.jar",
						workspace.getRoot().getLocation().toString() + "/"
								+ earProjectName
								+ "/EarContent/lib/antlr-2.7.5.jar");
				copyFile(templatePath + "/lib/common/commons-logging.jar",
						workspace.getRoot().getLocation().toString() + "/"
								+ earProjectName
								+ "/EarContent/lib/commons-logging.jar");
				copyFile(templatePath + "/lib/common/dom4j-1.6.jar", workspace
						.getRoot().getLocation().toString()
						+ "/"
						+ earProjectName
						+ "/EarContent/lib/dom4j-1.6.jar");
				copyFile(templatePath + "/lib/Hibernate/hibernate3.jar",
						workspace.getRoot().getLocation().toString() + "/"
								+ earProjectName
								+ "/EarContent/lib/hibernate3.jar");

				copyFile(templatePath + "/lib/Struts/javassist-3.18.1-GA.jar",
						workspace.getRoot().getLocation().toString() + "/"
								+ earProjectName
								+ "/EarContent/lib/javassist-3.18.1-GA.jar");
				copyDirectory(templatePath + "/lib/JPA", workspace.getRoot()
						.getLocation().toString()
						+ "/" + earProjectName + "/EarContent/lib/");
			}

			if (presentationFrameworkSelectorWizard.getComboWebAppFramework()
					.getText().equals("Spring")) {
				copyDirectory(templatePath + "/lib/Spring/common", workspace
						.getRoot().getLocation().toString()
						+ "/" + earProjectName + "/EarContent/lib/");
				copyFile(
						templatePath
								+ "/lib/Spring/MVC/spring-aop-4.1.6.RELEASE.jar",
						workspace.getRoot().getLocation().toString()
								+ "/"
								+ earProjectName
								+ "/EarContent/lib/spring-aop-4.1.6.RELEASE.jar");
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
			log.error("Excepton in createWebProject method" + ioe.getMessage());
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

	private void copyCommonFiles(String templatePath, IWorkspace workspace,
			Map<String, Object> data, String projectName) throws IOException,
			TemplateException, CoreException {

		copyDirectory(templatePath + "/common/src/com/app/framework", workspace
				.getRoot().getLocation().toString()
				+ "/" + projectName + "/src/com/app/framework");
	}

	private void copySpringIOCFiles(String templatePath, IWorkspace workspace,
			Map<String, Object> data, String projectName) throws IOException,
			CoreException, TemplateException {
		data.put("customerDaoObject",
				"(CustomerDao) BootStrapper.getService().getBean(\"CustomerDao\")");
		data.put("customerServiceBean", "");
		data.put("instrumentationBean", "");
		copyDirectory(templatePath
				+ "/ApplicationLayer/Spring/com/app/customer/util", workspace
				.getRoot().getLocation().toString()
				+ "/" + projectName + "/src/com/app/customer/util");
		/*
		 * Cross cutting feature - Instrumentation
		 */
		/*
		 * if (crosscuttingConcernsSelectorWizard.getBtnInstrumentation()
		 * .getSelection()) { String filePath = templatePath +
		 * "/Cross Cutting Features/Instrumentation/applicationContext.xml";
		 * StringBuilder value = loadDeploymentDescriptorData(filePath);
		 * data.put("instrumentationBean", value); data.put("customerDaoObject",
		 * "(CustomerDao) BootStrapper.getService().getBean(\"customerServiceProxy\")"
		 * ); }
		 */

		if (!applicationFrameworkSelectorWizard.getBtnRestService()
				.getSelection()
				&& !applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()
				&& !persistenceFrameworkSelectorWizard.getBtnJpa()
						.getSelection()) {

			copyTemplate(
					templatePath
							+ "/ApplicationLayer/Spring/com/app/customer/service/CustomerImpl.java.ftl",
					workspace.getRoot().getLocation().toString() + "/"
							+ projectName
							+ "/src/com/app/customer/service/CustomerImpl.java",
					data);
			copyFile(
					templatePath
							+ "/ApplicationLayer/Spring/com/app/customer/service/ICustomer.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projectName
							+ "/src/com/app/customer/service/ICustomer.java");

		} else if (!applicationFrameworkSelectorWizard.getBtnRestService()
				.getSelection()
				&& !applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()
				&& persistenceFrameworkSelectorWizard.getBtnJpa()
						.getSelection()) {
			copyDirectory(templatePath
					+ "/ApplicationLayer/Spring/JPA/com/app/customer/service",
					workspace.getRoot().getLocation().toString() + "/"
							+ projectName + "/src/com/app/customer/service");
		}

		copyTemplate(templatePath
				+ "/ApplicationLayer/Spring/applicationContext.xml.ftl",
				workspace.getRoot().getLocation().toString() + "/"
						+ projectName + "/src/applicationContext.xml", data);
	}

	private void copyRestServiceFiles(String templatePath,
			IWorkspace workspace, Map<String, Object> data, String projectName)
			throws IOException, CoreException, TemplateException {
		data.put("jsonFormatAnnotation", "@JsonFormat(pattern=\"yyyy-MM-dd\")");
		data.put("importJsonFormat",
				"import com.fasterxml.jackson.annotation.JsonFormat;");
		if (persistenceFrameworkSelectorWizard.getBtnJpa().getSelection()) {
			copyDirectory(
					templatePath
							+ "/ApplicationLayer/RestService/JPA/com/app/customer/service",
					workspace.getRoot().getLocation().toString() + "/"
							+ projectName + "/src/com/app/customer/service");
		} else {
			// copyCrossCuttingAngularJsRestSoapFiles(serviceProjectName,templatePath,
			// workspace, data );
			copyFile(
					templatePath
							+ "/ApplicationLayer/RestService/src/com/app/customer/service/ICustomer.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projectName
							+ "/src/com/app/customer/service/ICustomer.java");
			copyTemplate(
					templatePath
							+ "/ApplicationLayer/RestService/src/com/app/customer/service/RestService.java.ftl",
					workspace.getRoot().getLocation().toString() + "/"
							+ projectName
							+ "/src/com/app/customer/service/RestService.java",
					data);
		}
	}

	private void copySOAPStubFiles(String templatePath, IWorkspace workspace,
			Map<String, Object> data, String projectName) throws IOException,
			CoreException, TemplateException {
		copyFile(
				templatePath
						+ "/ApplicationLayer/SoapService/src/com/app/customer/service/ICustomer.java",
				workspace.getRoot().getLocation().toString() + "/"
						+ projectName
						+ "/src/com/app/customer/service/ICustomer.java");
		copyTemplate(
				templatePath
						+ "/ApplicationLayer/SoapService/src/com/app/customer/stub/CustomerStub.java.ftl",
				workspace.getRoot().getLocation().toString() + "/"
						+ projectName
						+ "/src/com/app/customer/stub/CustomerStub.java", data);

	}

	private void copySOAPServiceFiles(String templatePath,
			IWorkspace workspace, Map<String, Object> data, String projectName)
			throws IOException, CoreException, TemplateException {
		if (persistenceFrameworkSelectorWizard.getBtnJpa().getSelection()) {
			copyDirectory(
					templatePath
							+ "/ApplicationLayer/SoapService/JPA/com/app/customer/service",
					workspace.getRoot().getLocation().toString() + "/"
							+ projectName + "/src/com/app/customer/service");
		} else {
			copyFile(
					templatePath
							+ "/ApplicationLayer/SoapService/src/com/app/customer/service/ICustomer.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projectName
							+ "/src/com/app/customer/service/ICustomer.java");
			copyTemplate(
					templatePath
							+ "/ApplicationLayer/SoapService/src/com/app/customer/service/SOAPService.java.ftl",
					workspace.getRoot().getLocation().toString() + "/"
							+ projectName
							+ "/src/com/app/customer/service/SOAPService.java",
					data);
		}

	}

	private void copyHibernateFiles(String templatePath, IWorkspace workspace,
			Map<String, Object> data, String projectName) throws IOException,
			CoreException, TemplateException {
		copyFile(
				templatePath
						+ "/DataLayer/Hibernate/com/app/customer/dao/CustomerDaoImpl.java",
				workspace.getRoot().getLocation().toString() + "/"
						+ projectName
						+ "/src/com/app/customer/dao/CustomerDaoImpl.java");
		/*
		 * copyTemplate( templatePath +
		 * "/DataLayer/Hibernate/com/app/customer/dao/CustomerDaoImpl.ftl",
		 * workspace.getRoot().getLocation().toString() + "/" + projectName +
		 * "/src/com/app/customer/dao/CustomerDaoImpl.java", data);
		 */
		copyFile(templatePath
				+ "/DataLayer/Hibernate/com/app/customer/dao/CustomerDao.java",
				workspace.getRoot().getLocation().toString() + "/"
						+ projectName
						+ "/src/com/app/customer/dao/CustomerDao.java");
		copyFile(
				templatePath
						+ "/DataLayer/Hibernate/com/app/customer/util/HibernateUtil.java",
				workspace.getRoot().getLocation().toString() + "/"
						+ projectName
						+ "/src/com/app/customer/util/HibernateUtil.java");
		copyFile(templatePath
				+ "/DataLayer/Hibernate/CustomerApplication.hbm.xml", workspace
				.getRoot().getLocation().toString()
				+ "/" + projectName + "/src/CustomerApplication.hbm.xml");
		if (dataBaseSelectorWizard.btnMysql.getSelection()) {
			data.put("Driver", "com.mysql.jdbc.Driver");
			data.put("Dialect", "org.hibernate.dialect.MySQLDialect");
		} else if (dataBaseSelectorWizard.getBtnMicrosoftSqlServer()
				.getSelection()) {
			data.put("Driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
			data.put("Dialect", "org.hibernate.dialect.SQLServerDialect");
		} else if (dataBaseSelectorWizard.getBtnOracle().getSelection()) {
			data.put("Driver", "oracle.jdbc.driver.OracleDriver");
			data.put("Dialect", "org.hibernate.dialect.OracleDialect");
		}
		copyTemplate(templatePath + "/DataLayer/Hibernate/hibernate.cfg.ftl",
				workspace.getRoot().getLocation().toString() + "/"
						+ projectName + "/src/hibernate.cfg.xml", data);

	}

	private void copyDatabaseLib(String projName, String templatePath,
			IWorkspace workspace, Map<String, Object> data) throws IOException,
			CoreException, TemplateException {
		String libPath = null;
		if (presentationFrameworkSelectorWizard.getBtnDesktopApplication()
				.getSelection()
				&& (!applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection() && !applicationFrameworkSelectorWizard
						.getBtnSoapService().getSelection())) {
			libPath = "/lib/";
		} else if (presentationFrameworkSelectorWizard.getBtnWebApplication()
				.getSelection()
				&& !applicationFrameworkSelectorWizard.getBtnEjb()
						.getSelection()) {
			libPath = "/WebContent/WEB-INF/lib/";
		} else if (presentationFrameworkSelectorWizard.getBtnWebApplication()
				.getSelection()
				&& applicationFrameworkSelectorWizard.getBtnEjb()
						.getSelection()) {
			libPath = "/EarContent/lib/";
		} else if (presentationFrameworkSelectorWizard
				.getBtnDesktopApplication().getSelection()
				&& (applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection() || applicationFrameworkSelectorWizard
						.getBtnSoapService().getSelection())) {
			libPath = "/WebContent/WEB-INF/lib/";
		}

		if (dataBaseSelectorWizard.btnMysql.getSelection()) {
			copyDirectory(templatePath + "/lib/MySQL", workspace.getRoot()
					.getLocation().toString()
					+ "/" + projName + libPath);
		} else if (dataBaseSelectorWizard.btnMicrosoftSqlServer.getSelection()) {
			copyDirectory(templatePath + "/lib/SQL", workspace.getRoot()
					.getLocation().toString()
					+ "/" + projName + libPath);
		} else if (dataBaseSelectorWizard.getBtnOracle().getSelection()) {
			copyDirectory(templatePath + "/lib/Oracle", workspace.getRoot()
					.getLocation().toString()
					+ "/" + projName + libPath);
		}

	}

	private void copyJpaFiles(String projName, String templatePath,
			IWorkspace workspace, Map data, IContainer container)
			throws IOException, CoreException, TemplateException {
		createFolder(container, "src/META-INF");
		copyDirectory(templatePath + "/Common/src/com/app/framework/exception",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/framework/exception");
		copyFile(templatePath
				+ "/DataLayer/JPA/com/app/customer/util/EmProvider.java",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/customer/util/EmProvider.java");
		copyFile(templatePath
				+ "/DataLayer/JPA/com/app/customer/dao/CustomerDaoImpl.java",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/customer/dao/CustomerDaoImpl.java");
		copyFile(templatePath
				+ "/DataLayer/JPA/com/app/customer/dao/CustomerDao.java",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/customer/dao/CustomerDao.java");
		copyFile(
				templatePath
						+ "/DataLayer/JPA/com/app/customer/dao/AbstractCustomerDao.java",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/customer/dao/AbstractCustomerDao.java");
		/*
		 * copyTemplate(templatePath +
		 * "/DataLayer/JPA/com/app/customer/service/CustomerImpl.ftl",
		 * workspace.getRoot().getLocation().toString() + "/" + projName +
		 * "/src/com/app/customer/service/CustomerImpl.java", data);
		 */
		if (applicationFrameworkSelectorWizard.getBtnEjb().getSelection()) {
			copyFile(templatePath
					+ "/DataLayer/JPA/com/app/customer/service/ICustomer.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/service/ICustomer.java");
		}
		copyFile(templatePath
				+ "/DataLayer/JPA/com/app/customer/util/HexHelper.java",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/customer/util/HexHelper.java");
		copyFile(
				templatePath + "/DataLayer/JPA/com/app/framework/BaseDAO.java",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/framework/BaseDAO.java");
		copyFile(templatePath
				+ "/DataLayer/JPA/com/app/framework/QueryResultType.java",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/framework/QueryResultType.java");
		if (dataBaseSelectorWizard.btnMysql.getSelection()) {
			data.put("Driver", "com.mysql.jdbc.Driver");
			data.put("Dialect", "org.hibernate.dialect.MySQLDialect");
		} else if (dataBaseSelectorWizard.getBtnMicrosoftSqlServer()
				.getSelection()) {
			data.put("Driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
			data.put("Dialect", "org.hibernate.dialect.SQLServerDialect");
		} else if (dataBaseSelectorWizard.getBtnOracle().getSelection()) {
			data.put("Driver", "oracle.jdbc.driver.OracleDriver");
			data.put("Dialect", "org.hibernate.dialect.OracleDialect");
		}
		copyTemplate(templatePath + "/DataLayer/JPA/META-INF/persistence.ftl",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/META-INF/persistence.xml", data);

	}

	private void copyJpaLib(String projName, String templatePath,
			IWorkspace workspace, Map data) throws IOException, CoreException,
			TemplateException {
		if (presentationFrameworkSelectorWizard.getBtnDesktopApplication()
				.getSelection()
				|| presentationFrameworkSelectorWizard.getBtnWebApplication()
						.getSelection()
				&& (applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection() || applicationFrameworkSelectorWizard
						.getBtnSoapService().getSelection())) {
			copyDirectory(templatePath + "/lib/JPA", workspace.getRoot()
					.getLocation().toString()
					+ "/" + projName + "/WebContent/WEB-INF/lib/");
			copyFile(templatePath + "/lib/common/antlr-2.7.5.jar", workspace
					.getRoot().getLocation().toString()
					+ "/"
					+ projName
					+ "/WebContent/WEB-INF/lib/antlr-2.7.5.jar");
			copyFile(templatePath + "/lib/common/commons-logging.jar",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/WebContent/WEB-INF/lib/commons-logging.jar");
			copyFile(templatePath + "/lib/common/dom4j-1.6.jar", workspace
					.getRoot().getLocation().toString()
					+ "/" + projName + "/WebContent/WEB-INF/lib/dom4j-1.6.jar");
			/*
			 * copyFile(templatePath + "/lib/Hibernate/hibernate3.jar",
			 * workspace .getRoot().getLocation().toString() + "/" + projName +
			 * "/WebContent/WEB-INF/lib/hibernate3.jar");
			 */
			copyFile(templatePath + "/lib/common/HexLogger.jar", workspace
					.getRoot().getLocation().toString()
					+ "/" + projName + "/WebContent/WEB-INF/lib/HexLogger.jar");
			copyFile(templatePath + "/lib/Struts/javassist-3.18.1-GA.jar",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/WebContent/WEB-INF/lib/javassist-3.18.1-GA.jar");
			copyFile(
					templatePath + "/lib/Hibernate/javax-transaction-api.jar",
					workspace.getRoot().getLocation().toString()
							+ "/"
							+ projName
							+ "/WebContent/WEB-INF/lib/javax-transaction-api.jar");
			copyFile(templatePath + "/lib/common/log4j-1.2.16.jar", workspace
					.getRoot().getLocation().toString()
					+ "/"
					+ projName
					+ "/WebContent/WEB-INF/lib/log4j-1.2.16.jar");
		} else if (presentationFrameworkSelectorWizard
				.getBtnDesktopApplication().getSelection()
				|| presentationFrameworkSelectorWizard.getBtnWebApplication()
						.getSelection()
				&& (!applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection() && !applicationFrameworkSelectorWizard
						.getBtnSoapService().getSelection())) {
			copyDirectory(templatePath + "/lib/JPA", workspace.getRoot()
					.getLocation().toString()
					+ "/" + projName + "/lib/");
			copyFile(templatePath + "/lib/common/antlr-2.7.5.jar", workspace
					.getRoot().getLocation().toString()
					+ "/" + projName + "/lib/antlr-2.7.5.jar");
			copyFile(templatePath + "/lib/common/commons-logging.jar",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName + "lib/commons-logging.jar");
			copyFile(templatePath + "/lib/common/dom4j-1.6.jar", workspace
					.getRoot().getLocation().toString()
					+ "/" + projName + "/lib/dom4j-1.6.jar");
			/*
			 * copyFile(templatePath + "/lib/Hibernate/hibernate3.jar",
			 * workspace .getRoot().getLocation().toString() + "/" + projName +
			 * "/lib/hibernate3.jar");
			 */
			copyFile(templatePath + "/lib/common/HexLogger.jar", workspace
					.getRoot().getLocation().toString()
					+ "/" + projName + "/lib/HexLogger.jar");
			copyFile(templatePath + "/lib/Struts/javassist-3.18.1-GA.jar",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName + "/lib/javassist-3.18.1-GA.jar");
			copyFile(templatePath + "/lib/Hibernate/javax-transaction-api.jar",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName + "/lib/javax-transaction-api.jar");
			copyFile(templatePath + "/lib/common/log4j-1.2.16.jar", workspace
					.getRoot().getLocation().toString()
					+ "/" + projName + "/lib/log4j-1.2.16.jar");
		}
		/*
		 * Copy Database Files
		 */
		copyDatabaseLib(projName, templatePath, workspace, data);
	}

	private void copyCrossCuttingFeatureFiles(String templatePath,
			IWorkspace workspace, String projectName) throws IOException,
			CoreException {
		copyDirectory(templatePath
				+ "/Cross Cutting Features/Instrumentation/src", workspace
				.getRoot().getLocation().toString()
				+ "/" + projectName + "/src/");
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

	private void copyEJBDesktopFiles(String projName, String templatePath,
			IWorkspace workspace, Map data, IContainer container)
			throws IOException, CoreException, TemplateException {
		String libPath = "";
		createFolder(container, "src/com");
		createFolder(container, "src/com/app");
		createFolder(container, "src/com/app/framework");
		createFolder(container, "src/com/app/framework/service");

		if (presentationFrameworkSelectorWizard.getBtnDesktopApplication()
				.getSelection()) {
			data.put("projectName", earProjectName);
			data.put("APP_NAME", earProjectName);
			libPath = "/lib/";
			if (!applicationFrameworkSelectorWizard.getBtnRestService()
					.getSelection()
					&& !applicationFrameworkSelectorWizard.getBtnSoapService()
							.getSelection()) {
				createFolder(container, "src/com/app/customer/util");
				copyTemplate(
						templatePath
								+ "/ApplicationLayer/Ejb/common/com/app/customer/util/GlobalConstants.ftl",
						workspace.getRoot().getLocation().toString()
								+ "/"
								+ projName
								+ "/src/com/app/customer/util/GlobalConstants.java",
						data);
				copyFile(
						templatePath
								+ "/ApplicationLayer/Ejb/common/com/app/customer/util/JndiLookup.java",
						workspace.getRoot().getLocation().toString() + "/"
								+ projName
								+ "/src/com/app/customer/util/JndiLookup.java");
				// EJB without Rest & Soap
				copyFile(
						templatePath
								+ "/ApplicationLayer/Ejb/common/com/app/customer/lookup/CustomerLookup.java",
						workspace.getRoot().getLocation().toString()
								+ "/"
								+ projName
								+ "/src/com/app/customer/lookup/CustomerLookup.java");
			}
		}
	}

	private void copyEJBWebFiles(String projName, String templatePath,
			IWorkspace workspace, Map data, IContainer container)
			throws IOException, CoreException, TemplateException {
		createFolder(container, "src/com");
		createFolder(container, "src/com/app");
		createFolder(container, "src/com/app/framework");
		createFolder(container, "src/com/app/framework/service");
		createFolder(container, "src/com/app/framework/util");
		createFolder(container, "src/com/app/framework/delegate");
		createFolder(container, "src/com/app/framework/exception");
		String libPath = "";
		data.put("projectName", projName);
		libPath = "/Webcontent/WEB-INF/";
		copyFile(
				templatePath
						+ "/common/src/com/app/framework/delegate/HJSFBusinessDelegator.java",
				workspace.getRoot().getLocation().toString()
						+ "/"
						+ projName
						+ "/src/com/app/framework/delegate/HJSFBusinessDelegator.java");
		copyFile(
				templatePath
						+ "/common/src/com/app/framework/exception/HexApplicationException.java",
				workspace.getRoot().getLocation().toString()
						+ "/"
						+ projName
						+ "/src/com/app/framework/exception/HexApplicationException.java");
		copyFile(
				templatePath
						+ "/common/src/com/app/framework/service/HJSFServiceController.java",
				workspace.getRoot().getLocation().toString()
						+ "/"
						+ projName
						+ "/src/com/app/framework/service/HJSFServiceController.java");
		copyFile(templatePath
				+ "/common/src/com/app/framework/util/HJSFParam.java",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/framework/util/HJSFParam.java");

		// }
		if (applicationFrameworkSelectorWizard.getBtnEjb().getSelection()
				&& applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
				&& applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()) {
			// EJB with Rest & Soap
			copyFile(
					templatePath
							+ "/ApplicationLayer/Ejb/Soap/com/app/customer/service/SOAPService.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/service/SOAPService.java");
			copyFile(
					templatePath
							+ "/ApplicationLayer/Ejb/Rest/com/app/customer/service/RestService.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/service/RestService.java");
			copyDirectory(templatePath + "/lib/Soap Service", workspace
					.getRoot().getLocation().toString()
					+ "/" + projName + libPath + "lib/");
			copyFile(templatePath
					+ "/ApplicationLayer/Ejb/Soap/WEB-INF/sun-jaxws.xml",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName + libPath + "sun-jaxws.xml");
		} else if (applicationFrameworkSelectorWizard.getBtnEjb()
				.getSelection()
				&& applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
				&& !applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()) {
			// EJB with Rest
			copyFile(
					templatePath
							+ "/ApplicationLayer/Ejb/Rest/com/app/customer/service/RestService.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/service/RestService.java");
		} else if (applicationFrameworkSelectorWizard.getBtnEjb()
				.getSelection()
				&& !applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
				&& applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()) {
			// EJB with Soap
			copyFile(
					templatePath
							+ "/ApplicationLayer/Ejb/Soap/com/app/customer/service/SOAPService.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/service/SOAPService.java");
			copyDirectory(templatePath + "/lib/Soap Service", workspace
					.getRoot().getLocation().toString()
					+ "/" + projName + libPath + "lib/");
			copyFile(templatePath
					+ "/ApplicationLayer/Ejb/Soap/WEB-INF/sun-jaxws.xml",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName + libPath + "sun-jaxws.xml");
		} else if (applicationFrameworkSelectorWizard.getBtnEjb()
				.getSelection()
				&& !applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
				&& !applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()) {
			// EJB without Rest & Soap
			copyFile(
					templatePath
							+ "/ApplicationLayer/Ejb/common/com/app/customer/lookup/CustomerLookup.java",
					workspace.getRoot().getLocation().toString()
							+ "/"
							+ projName
							+ "/src/com/app/customer/lookup/CustomerLookup.java");
		}
	}

	private void copyEJBFiles(String projName, String templatePath,
			IWorkspace workspace, Map data, IContainer container)
			throws IOException, CoreException, TemplateException {
		String ejbBeanPath = "";
		createFolder(container, "src/com/app/customer/ejb");
		createFolder(container, "src/com/app/customer/stub");
		data.put("projectName", earProjectName + "WEB");
		if (applicationFrameworkSelectorWizard.getBtnEjb().getSelection()
				&& applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
				&& applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()) {
			// EJB with Rest & Soap
			copyFile(
					templatePath
							+ "/ApplicationLayer/Ejb/Soap/com/app/customer/service/ICustomer.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/service/ICustomer.java");
			copyTemplate(
					templatePath
							+ "/ApplicationLayer/Ejb/Soap/com/app/customer/stub/CustomerStub.java.ftl",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/stub/CustomerStub.java",
					data);
		} else if (applicationFrameworkSelectorWizard.getBtnEjb()
				.getSelection()
				&& applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
				&& !applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()) {
			// EJB with Rest
			copyFile(
					templatePath
							+ "/ApplicationLayer/Ejb/Rest/com/app/customer/service/ICustomer.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/service/ICustomer.java");
		} else if (applicationFrameworkSelectorWizard.getBtnEjb()
				.getSelection()
				&& !applicationFrameworkSelectorWizard.getBtnRestService()
						.getSelection()
				&& applicationFrameworkSelectorWizard.getBtnSoapService()
						.getSelection()) {
			// EJB with Soap
			copyFile(
					templatePath
							+ "/ApplicationLayer/Ejb/Soap/com/app/customer/service/ICustomer.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/service/ICustomer.java");
			copyTemplate(
					templatePath
							+ "/ApplicationLayer/Ejb/Soap/com/app/customer/stub/CustomerStub.java.ftl",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/stub/CustomerStub.java",
					data);
		} else
			copyFile(
					templatePath
							+ "/ApplicationLayer/Ejb/Soap/com/app/customer/service/ICustomer.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/service/ICustomer.java");
		if (persistenceFrameworkSelectorWizard.getBtnJpa().getSelection()) {
			ejbBeanPath = "common";
			copyFile(
					templatePath
							+ "/ApplicationLayer/Ejb/common/com/app/customer/util/EmProvider.java",
					workspace.getRoot().getLocation().toString() + "/"
							+ projName
							+ "/src/com/app/customer/util/EmProvider.java");
		} else if (persistenceFrameworkSelectorWizard.getBtnHibernate()
				.getSelection()) {
			ejbBeanPath = "hibernate";
		}
		copyTemplate(templatePath + "/ApplicationLayer/Ejb/" + ejbBeanPath
				+ "/com/app/customer/ejb/CustomerBean.java", workspace
				.getRoot().getLocation().toString()
				+ "/"
				+ projName
				+ "/src/com/app/customer/ejb/CustomerBean.java", data);
		copyTemplate(
				templatePath
						+ "/ApplicationLayer/Ejb/common/com/app/customer/util/GlobalConstants.ftl",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/customer/util/GlobalConstants.java",
				data);
		copyFile(
				templatePath
						+ "/ApplicationLayer/Ejb/common/com/app/customer/util/HexHelper.java",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/customer/util/HexHelper.java");
		copyFile(
				templatePath
						+ "/ApplicationLayer/Ejb/common/com/app/customer/util/JndiLookup.java",
				workspace.getRoot().getLocation().toString() + "/" + projName
						+ "/src/com/app/customer/util/JndiLookup.java");

	}

	public void copyEmailFile(String javaProjectName, String templatePath,
			IWorkspace workspace, Map data, IContainer container)
			throws IOException, CoreException, TemplateException {
		copyFile(
				templatePath
						+ "/PresentationLayer/JavaFx/email/src/view/CustomerAddview.fxml",
				workspace.getRoot().getLocation()
						.toString()
						+ "/"
						+ javaProjectName
						+ "/src/view/CustomerAddview.fxml");
		copyFile(
				templatePath
						+ "/PresentationLayer/JavaFx/email/src/view/EmailNotificationview.fxml",
				workspace.getRoot().getLocation()
						.toString()
						+ "/"
						+ javaProjectName
						+ "/src/view/EmailNotificationview.fxml");
		copyTemplate(
				templatePath
						+ "/PresentationLayer/JavaFx/email/src/view/CustomerListview.ftl",
				workspace.getRoot().getLocation()
						.toString()
						+ "/"
						+ javaProjectName
						+ "/src/view/CustomerListview.fxml",
				data);
		
	}

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
