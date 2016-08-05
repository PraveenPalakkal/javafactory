<?xml version="1.0" encoding="UTF-8"?><project-modules id="moduleCoreId" project-version="1.5.0">
    <wb-module deploy-name="${APP_NAME}">
        <wb-resource deploy-path="/" source-path="/EarContent" tag="defaultRootSource"/>
        <dependent-module archiveName="${APP_NAME}EJB.jar" deploy-path="/" handle="module:/resource/${APP_NAME}EJB/${APP_NAME}EJB">
            <dependent-object/>
            <dependency-type>uses</dependency-type>
        </dependent-module>
        <dependent-module archiveName="${APP_NAME}WEB.war" deploy-path="/" handle="module:/resource/${APP_NAME}WEB/${APP_NAME}WEB">
            <dependent-object/>
            <dependency-type>uses</dependency-type>
        </dependent-module>
    </wb-module>
</project-modules>
