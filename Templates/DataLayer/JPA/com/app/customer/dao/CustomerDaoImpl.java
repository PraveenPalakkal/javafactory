package com.app.customer.dao;

import java.io.Serializable;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import com.app.customer.util.EmProvider;
import com.app.framework.exception.HexApplicationException;

public class CustomerDaoImpl extends AbstractCustomerDao implements CustomerDao,Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 13500913573837545L;
	
	private EntityManager entityManager; 
	
	@PostConstruct 
	public void setEntityManager() {		
	}
	
	public CustomerDaoImpl(String persistenceUnitName) {		
		super(persistenceUnitName);	
	}

	public CustomerDaoImpl() throws HexApplicationException {		 
			super();
			createEntityManager();
	}

	public CustomerDaoImpl(String persistenceUnitName,
			HashMap<Object, Object> propertiesMap) {
		super(persistenceUnitName, propertiesMap);
	}
	
	public CustomerDaoImpl(HashMap<Object, Object> propertiesMap) {
		super(propertiesMap);
	}

	public CustomerDaoImpl(EntityManagerFactory entityManagerFactory) {
		super(entityManagerFactory);
	}

	// TODO Add custom bussiness logic methods here.
	
	public void closeEntityManager(){
		try{	
		}catch(Exception e){			
		}finally {
	        if(entityManager != null) {
	        	entityManager.close(); //make sure to close EntityManager
	        }
	        //should I not close the EMF itself here?????
	        EmProvider.getInstance().closeEmf();
	    }
	}
	
	public void createEntityManager(){
		super.setEntityManager(EmProvider.getInstance().getEntityManagerFactory().createEntityManager());
		super.setContainerManaged(false);	
	}
	
}
