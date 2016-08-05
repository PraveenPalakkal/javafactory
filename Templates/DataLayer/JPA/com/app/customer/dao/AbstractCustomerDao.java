package com.app.customer.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;

import com.app.customer.vo.Customer;
import com.app.framework.BaseDAO;
import com.app.framework.QueryResultType;
import com.app.framework.exception.HexApplicationException;


public abstract class AbstractCustomerDao extends  BaseDAO implements CustomerDao,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6413734471482141348L;
	
	public AbstractCustomerDao() {
		super();
	}

	public AbstractCustomerDao(String persistenceUnitName) {
		super(persistenceUnitName);
	}

	public AbstractCustomerDao(String persistenceUnitName,
			HashMap<Object, Object> propertiesMap) {
		super(persistenceUnitName, propertiesMap);
	}

	public AbstractCustomerDao(HashMap<Object, Object> propertiesMap) {
		super(propertiesMap);
	}

	public AbstractCustomerDao(EntityManagerFactory entityManagerFactory) {
		super(entityManagerFactory);
	}

	@Override
	public String insert(Customer object) throws HexApplicationException {
		
		return super.insert(object);
	}

	@Override
	public String update(Customer object) throws HexApplicationException {
		
		return super.update(object);
	}

	@Override
	public void delete(Customer object) throws HexApplicationException {
		
		super.delete(object);
	}

	@Override
	public String deleteAll(List entries) throws HexApplicationException {
		return super.deleteAll(entries);
	}

	@Override
	public long getObjectCount(String entityName) throws HexApplicationException {
		return super.getObjectCount(entityName, null);
	}

	@Override
	public boolean isContains(Customer object) throws HexApplicationException {
		
		return super.isContains(object);
	}

	@Override
	public void detach(Customer object) throws HexApplicationException {
		
		super.detach(object);
	}

	@Override
	public boolean isOpen() throws HexApplicationException {
		return super.isOpen();
	}

	@Override
	public void flush() throws HexApplicationException {
		super.flush();
	}

	@Override
	public void clear() throws HexApplicationException {
		super.clear();
	}

	@Override
	public void close() throws HexApplicationException {
		super.close();
	}

	@Override
	public Map<String, Object> getProperties() throws HexApplicationException {
		return super.getProperties();
	}

	@Override
	public void setProperty(String paramString, Object paramDepartment)
			throws HexApplicationException {
		super.setProperty(paramString, paramDepartment);
	}

	@Override
	public FlushModeType getFlushMode() throws HexApplicationException {
		return super.getFlushMode();
	}

	@Override
	public void setFlushMode(FlushModeType paramFlushModeType)
			throws HexApplicationException {
		super.setFlushMode(paramFlushModeType);
	}

	@Override
	public void lock(Customer object, LockModeType lockModeType)
			throws HexApplicationException {
		
		super.lock(object, lockModeType);
	}

	@Override
	public void lock(Customer object, LockModeType lockModeType,
			Map<String, Object> propertiesMap) throws HexApplicationException {
		
		super.lock(object, lockModeType, propertiesMap);
	}

	@Override
	public void refresh(Customer object) throws HexApplicationException {
		
		super.refresh(object);
	}

	@Override
	public void refresh(Customer object, Map<String, Object> propertiesMap)
			throws HexApplicationException {
		
		super.refresh(object, propertiesMap);
	}

	@Override
	public void refresh(Customer object, LockModeType lockModeType)
			throws HexApplicationException {
		
		super.refresh(object, lockModeType);
	}

	@Override
	public void refresh(Customer object, LockModeType lockModeType,
			Map<String, Object> propertiesMap) throws HexApplicationException {
		
		super.refresh(object, propertiesMap);
	}

	@Override
	public Object getReference(Class clazz, Object primarykey)
			throws HexApplicationException {
		return super.getReference(clazz, primarykey);
	}

	@Override
	public List getAllObjects(String entityName) throws HexApplicationException {
		return super.getAllObjects(entityName, null);
	}

	@Override
	public List getAllObjects(int startRecord, int endRecord, String entityName)
			throws HexApplicationException {
		return super.getAllObjects(startRecord, endRecord, entityName, null);
	}

	@Override
	public List executeQuery(String queryString,
			Map<Object, Object> paramMap, QueryResultType queryResultType)
					throws HexApplicationException {
		return super.executeQuery(queryString, paramMap, queryResultType);
	}
	
	@Override
	public List executeQuery(String queryString,
							 QueryResultType queryResultType)
					throws HexApplicationException {
		return super.executeQuery(queryString, queryResultType);
	}

	@Override
	public Object executeNamedQuery(String namedQuery,
			Map<Object, Object> paramMap, QueryResultType queryResultType)
					throws HexApplicationException {
		return super.executeNamedQuery(namedQuery, paramMap, queryResultType);
	}

	@Override
	public Object executeNativeQuery(String nativeQuery,
			Map<Object, Object> paramMap, QueryResultType queryResultType,
			Class clazz) throws HexApplicationException {
		return super.executeNativeQuery(nativeQuery, paramMap, queryResultType,
				clazz);
	}

	@Override
	public int updateByCreateQuery(String queryString,
			Map<Object, Object> paramMap) throws HexApplicationException {
		return super.updateByCreateQuery(queryString, paramMap);
	}

	@Override
	public int updateByNamedQuery(String namedQuery,
			Map<Object, Object> paramMap) throws HexApplicationException {
		return super.updateByNamedQuery(namedQuery, paramMap);
	}

	@Override
	public int updateByNativeQuery(String nativeQuery,
			Map<Object, Object> paramMap) throws HexApplicationException {
		return super.updateByNativeQuery(nativeQuery, paramMap);
	}

	@Override
	public void setContainerManaged(boolean isContainerManaged) {
		super.setContainerManaged(isContainerManaged);
	}

	@Override
	public Customer select(Class clazz, Customer object)
			throws HexApplicationException {
		BaseDAO baseDAO = new BaseDAO();
		EntityManager entityManager = null;
		try {
			entityManager = baseDAO.getEntityManager();
			Customer result = (Customer) entityManager.find(
					Customer.class, object.getCustomerid());
			
			return result;
		} catch (EntityNotFoundException exception) {
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			throw new HexApplicationException(exception);
		}
	}

	@Override
	public Customer select(Class clazz, Customer object, LockModeType lockMode)
			throws HexApplicationException {
		BaseDAO baseDAO = new BaseDAO();
		EntityManager entityManager = null;
		try {
			entityManager = baseDAO.getEntityManager();
			Customer result = (Customer) entityManager.find(
					Customer.class, object.getCustomerid(), lockMode);
			
			return result;
		} catch (EntityNotFoundException exception) {
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			throw new HexApplicationException(exception);
		}
	}

	@Override
	public Customer select(Class clazz, Customer object,
			Map<String, Object> propertiesMap) throws HexApplicationException {
		BaseDAO baseDAO = new BaseDAO();
		EntityManager entityManager = null;
		try {
			entityManager = baseDAO.getEntityManager();
			Customer result = (Customer) entityManager.find(
					Customer.class, object.getCustomerid(), propertiesMap);
			
			return result;
		} catch (EntityNotFoundException exception) {
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			throw new HexApplicationException(exception);
		}
	}

	@Override
	public Customer select(Class clazz, Customer object, LockModeType lockMode,
			Map<String, Object> propertiesMap) throws HexApplicationException {
		BaseDAO baseDAO = new BaseDAO();
		EntityManager entityManager = null;
		try {
			entityManager = baseDAO.getEntityManager();
			Customer result = (Customer) entityManager.find(
					Customer.class, object.getCustomerid(), lockMode, propertiesMap);
			
			return result;
		} catch (EntityNotFoundException exception) {
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			throw new HexApplicationException(exception);
		}
	}

}
