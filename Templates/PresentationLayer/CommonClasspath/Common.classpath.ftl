<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" path="src"/>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/jdk1.8.0_45">
		<attributes>
			<attribute name="owner.project.facets" value="java"/>
		</attributes>
	</classpathentry>
    <classpathentry kind="lib" path="lib/log4j-1.2.16.jar"/>
	<classpathentry kind="lib" path="lib/HexLogger.jar"/>
	<classpathentry kind="lib" path="lib/HJSF-Framework.jar"/>
	<classpathentry kind="lib" path="lib/jstl-1.2.jar"/>
	<classpathentry kind="lib" path="lib/paranamer-2.6.1.jar"/>
	
	${hibernateLib}
	
	${springLib}
	
	${springMvcLib}
	
	${jsfLib}
	
	${strutsLib}
	
	${instrumentationLib}
	
	${restLib}
	
	${soapLib}
	
	${pojoLib}
	
	${jpaLib}

	${jbossLib}

	${additionalLib}
	
	${dataBaseLib}

	${validationLib}

	${cacheLib}

	${notificationLib}
	
	${AuthenticationLib}
	
	<classpathentry kind="output" path="build/classes"/>
</classpath>
