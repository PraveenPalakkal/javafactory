<?xml version="1.0" encoding="UTF-8"?>
<jboss-deployment-structure>
   <deployment>
     <exclusions>
       <module name="org.apache.log4j" />
     </exclusions>
   </deployment>
   ${SUB_DEPLOYMENT}
  <sub-deployment name="${APP_NAME}EJB.jar">
    <exclusions>
       <module name="org.apache.log4j" />
    </exclusions>
 </sub-deployment>
</jboss-deployment-structure>