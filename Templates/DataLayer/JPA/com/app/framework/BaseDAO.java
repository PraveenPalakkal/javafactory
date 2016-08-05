package com.app.framework;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.Parameter;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.RollbackException;
import javax.persistence.TransactionRequiredException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.app.framework.exception.HexApplicationException;
import com.hexaware.framework.logger.LogFactory;
import com.hexaware.framework.logger.Logger;
 


public class BaseDAO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3969693956317447301L;
	
	static Logger log = LogFactory.getLogger(BaseDAO.class);
		
	private EntityManagerFactory entityManagerFactory;

	private EntityManager entityManager = null;

	private boolean isContainerManaged = false;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public boolean isContainerManaged() {
		return isContainerManaged;
	}

	public void setContainerManaged(boolean isContainerManaged) {
		this.isContainerManaged = isContainerManaged;
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	/**
	 * Default Constructor for creating entityManagerFactory from default
	 * persistenceUnit.
	 */
	public BaseDAO() {
		
	}

	/**
	 * Constructor for creating entityManagerFactory from the given persistence
	 * unit name.
	 * 
	 * @param persistenceUnitName
	 *            - String persistenceUnitName
	 */
	public BaseDAO(String persistenceUnitName) {
		log.info("loading named persisting unit..");
		entityManagerFactory = Persistence
				.createEntityManagerFactory(persistenceUnitName);
		entityManager = entityManagerFactory.createEntityManager();
	}

	/**
	 * Constructor for creating entityManagerFactory from the given persistence
	 * unit Name along with the given properties.
	 * 
	 * @param persistenceUnitName
	 *            - String persistenceUnitName
	 */
	public BaseDAO(String persistenceUnitName,
			HashMap<Object, Object> propertiesMap) {
		log.info("loading named persisting unit with given properties..");
		entityManagerFactory = Persistence.createEntityManagerFactory(
				persistenceUnitName, propertiesMap);
		entityManager = entityManagerFactory.createEntityManager();
	}

	/**
	 * Constructor for creating entityManagerFactory from the named persistence
	 * unit using the given properties.
	 */
	public BaseDAO(HashMap<Object, Object> propertiesMap) {
		log.info("loading persisting unit with given properties..");
		entityManagerFactory = Persistence.createEntityManagerFactory(
				"default", propertiesMap);
		entityManager = entityManagerFactory.createEntityManager();
	}

	/**
	 * Constructor for assigning entityManagerFactory with given
	 * entityManagerFactory instance.
	 * 
	 * @param entityManagerFactory
	 *            -EntityManagerFactory entityManagerFactory
	 */
	public BaseDAO(EntityManagerFactory entityManagerFactory) {
		log.info("loading entityManagerFactory..");
		this.entityManagerFactory = entityManagerFactory;
		entityManager = entityManagerFactory.createEntityManager();
	}

	/**
	 * <b> insert </b> method inserts a new object in to the database.
	 * 
	 * @param Object
	 *            object-object to be inserted.
	 * @throws InsertException
	 * @throws ValidationException
	 * @throws SystemException
	 */
	public String insert(Object object) throws HexApplicationException{
		log.info("Inside insert method..");
		try {
			if (object != null) {
				if (!validateObject(object)) {
					if (!isContainerManaged)
						entityManager.getTransaction().begin();
					entityManager.persist(object);
					if (!isContainerManaged)
						entityManager.getTransaction().commit();
					log.info("Object inserted successfully!");
					log.info("End of insert method..");
				}
			}
		} catch (EntityExistsException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (RollbackException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}return "added Successfully";
	}

	/**
	 * <b> update </b> method updates the given object in the database.
	 * 
	 * @param object
	 *            -Entity object to be update
	 * @throws UpdateException
	 * @throws ValidationException
	 * @throws SystemException
	 */
	public String update(Object object) throws HexApplicationException {
		log.info("Inside update method..");
		try {
			if (object != null) {
				if (!validateObject(object)) {
					if (!isContainerManaged)
						entityManager.getTransaction().begin();
					entityManager.merge(object);
					if (!isContainerManaged)
						entityManager.getTransaction().commit();
					log.info("Object updated successfully!");
					log.info("End of update method..");
				}
			}
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}return "Updated Successfully";
	}

	/**
	 * <b> select </b> method finds the object from the database.
	 * 
	 * @param clazz
	 *            - Entity class
	 * @param primaryKey
	 *            -PrimarKey of the entity to be found
	 * @return Object - Entity object
	 * @throws SelectException
	 * @throws SystemException
	 */
	public Object select(Class clazz, Object primaryKey)
			throws HexApplicationException {
		log.info("Inside select method..");
		try {
			Object result = entityManager
					.find(clazz, (Serializable) primaryKey);
			log.debug(result.toString());
			log.info("Object found successfully!");
			log.info("End of select method..");
			return result;
		} catch (EntityNotFoundException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> select </b> method finds the object from the database.
	 * 
	 * @param clazz
	 *            -Entity class
	 * @param primaryKey
	 *            -primaryKey of the object to be select
	 * @param propertiesMap
	 *            -standard and vendor-specific properties and hints map
	 * @return Object -Entity object
	 * @throws SelectException
	 * @throws SystemException
	 */
	public Object select(Class clazz, Object primaryKey,
			Map<String, Object> propertiesMap) throws HexApplicationException {
		log.info("Inside select method..");
		try {
			Object result = entityManager.find(clazz,
					(Serializable) primaryKey, propertiesMap);
			log.debug(result.toString());
			log.info("Object found successfully!");
			log.info("End of select method..");
			return result;
		} catch (EntityNotFoundException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> select </b> method finds the object from the database.
	 * 
	 * @param clazz
	 *            -Entity class
	 * @param primaryKey
	 *            -primaryKey of the object to be select
	 * @param lockMode
	 *            -mode of lock type
	 * @return Object -Entity object
	 * @throws SelectException
	 * @throws SystemException
	 */
	public Object select(Class clazz, Object primaryKey, LockModeType lockMode)
			throws HexApplicationException {
		log.info("Inside select method..");
		try {
			Object result = entityManager.find(clazz,
					(Serializable) primaryKey, lockMode);
			log.debug(result.toString());
			log.info("Object found successfully!");
			log.info("End of select method..");
			return result;
		} catch (EntityNotFoundException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> select </b> method finds the object from the database.
	 * 
	 * @param clazz
	 *            -Entity class
	 * @param primaryKey
	 *            -primaryKey of the object to be select
	 * @param propertiesMap
	 *            -standard and vendor-specific properties and hints map
	 * @param lockMode
	 *            -mode of lock type
	 * @return Object -Entity object.
	 * @throws SelectException
	 * @throws SystemException
	 */
	public Object select(Class clazz, Object object, LockModeType lockMode,
			Map<String, Object> propertiesMap) throws HexApplicationException {
		log.info("Inside select method..");
		try {
			Object result = entityManager.find(clazz, (Serializable) object,
					lockMode, propertiesMap);
			log.debug(result.toString());
			log.info("Object found successfully!");
			log.info("End of select method..");
			return result;
		} catch (EntityNotFoundException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> delete </b> method removes the object from the database.
	 * 
	 * @param object
	 *            -Entity object to be delete
	 * @throws DeleteException
	 * @throws SystemException
	 */
	public void delete(Object object) throws HexApplicationException {
		log.info("Inside delete method..");
		try {
			if (object != null) {
				if (!isContainerManaged)
					entityManager.getTransaction().begin();
				entityManager.remove(entityManager.merge(object));
				if (!isContainerManaged)
					entityManager.getTransaction().commit();
				log.info("Object deleted successfully!!");
				log.info("End of delete method..");
			}
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> deleteAll </b> method deletes the list of objects from the
	 * database.
	 * 
	 * @param entries
	 *            -List of entity objects to delete
	 * @throws DeleteException
	 * @throws SystemException
	 */
	public String deleteAll(List entries) throws HexApplicationException{
		log.info("Inside deleteAll method..");
		try {
			if (entries != null && entries.size() > 0) {
				for (Iterator iterator = entries.iterator(); iterator.hasNext();) {
					if (!isContainerManaged)
						entityManager.getTransaction().begin();
					entityManager.remove(entityManager.merge(iterator.next()));
					if (!isContainerManaged)
						entityManager.getTransaction().commit();
					log.info("Objects deleted successfully!!");
					log.info("End of delete method..");
				}
			}
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}return "deleted Successfully";
	}

	/**
	 * <b> getObjectCount </b> method counts the number of Entity objects from
	 * the database.
	 * 
	 * @param entityName
	 *            -Name of the Entity
	 * @param orgIdAttribute - orgId attribute name
	 * @return long-Number of Entity objects
	 * @throws SelectException
	 * @throws SystemException
	 */
	public long getObjectCount(String entityName, String orgIdAttribute) throws HexApplicationException {
		log.info("Inside getObjectCount method..");
		try {
			if (entityName != null && entityName != "") {
				String query = "SELECT count(entity) FROM " + entityName + " entity";
				
				Long count = (Long) entityManager.createQuery(query)
						.getSingleResult();
				log.info("Number of Objects found successfully!");
				log.debug("Count value-->" + count.intValue());
				log.info("End of getObjectCount method..");
				return count;
			}
		} catch (NoResultException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (NonUniqueResultException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (QueryTimeoutException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
		return 0;
	}

	/**
	 * <b> isContains </b> method Check object belonging to the current
	 * persistence context.
	 * 
	 * @param object
	 *            -Object to be checked
	 * @return boolean- boolean flag indicating whether entity is present in
	 *         persistence context
	 * 
	 * @throws SystemException
	 */
	public boolean isContains(Object object) throws HexApplicationException {
		boolean containsFlag = false;
		try {
			if (!object.equals(null)) {
				containsFlag = entityManager.contains(object);
			}
			return containsFlag;
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> detach </b> method remove the given entity from the persistence
	 * context.
	 * 
	 * @param object
	 *            -object to be removed
	 * @throws SystemException
	 */
	public void detach(Object object) throws HexApplicationException {
		log.info("Inside delete method..");
		try {
			if (object != null) {
				if (!isContainerManaged)
					entityManager.getTransaction().begin();
				entityManager.detach(object);
				if (!isContainerManaged)
					entityManager.getTransaction().commit();
				log.info("Object successfully deleted from the context!");
				log.info("End of detach method..");
			}
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> isOpen </b> method determine whether the entity manager is open.
	 * 
	 * @return boolean-boolean flag indicating entityManager state
	 * @throws SystemException
	 */
	public boolean isOpen() throws HexApplicationException {
		log.info("Inside isOpen method..");
		boolean isOpenFlag = false;
		try {
			if (entityManager != null) {
				isOpenFlag = entityManager.isOpen();
				log.debug("isOpen flag-->" + isOpenFlag);
				log.info("End of method..");
			}
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
		return isOpenFlag;
	}

	/**
	 * <b> flush </b> method synchronize the persistence context to the
	 * database.
	 * 
	 * @throws SystemException
	 */
	public void flush() throws HexApplicationException {
		log.info("Inside flush method..");
		try {
			if (entityManager != null) {
				entityManager.flush();
				log.info("End of flush method..");
			}
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> clear </b> method clear the persistence context, causing all managed
	 * entities to become detached.
	 * 
	 * @throws ApplicationException
	 */
	public void clear() throws HexApplicationException {
		log.info("Inside clear method..");
		try {
			entityManager.clear();
			log.info("End of clear method..");
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 *<b> close </b> method close an application-managed entity manager.
	 * 
	 * @throws SystemException
	 */
	public void close() throws HexApplicationException {
		log.info("Inside close method..");
		try {
			entityManager.close();
			log.info("End of close method..");
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> getProperties </b>method get the properties and hints and associated
	 * with the entity manager. Changing the contents of the map does not change
	 * the configuration.
	 * 
	 * @return Map - properties of the entityManager.
	 * @throws SystemException
	 */
	public Map<String, Object> getProperties() throws HexApplicationException {
		log.info("Inside getProperties method..");
		try {
			Map<String, Object> propertiesMap = entityManager.getProperties();
			for (Object entry : propertiesMap.entrySet()) {
				log.debug(entry.toString());
			}
			log.info("End of getProperties method..");
			return propertiesMap;
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> setProperty </b>method set an entity manager property.
	 * 
	 * @param paramString
	 *            -name of the property
	 * @param paramObject
	 *            -value of the property
	 * @throws SystemException
	 */
	public void setProperty(String paramString, Object paramObject)
			throws HexApplicationException {
		log.info("Inside setProperty method..");
		try {
			if (paramString != null && paramObject != null)
				entityManager.setProperty(paramString, paramObject);
			log.info("End of setProperty method..");
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> getFlushMode </b>method get the flush mode that applies to all
	 * objects in the persistence context.
	 * 
	 * @return FlushModeType -flush mode type of the objects in context.
	 * @throws SystemException
	 */
	public FlushModeType getFlushMode() throws HexApplicationException {
		log.info("Inside getFlushMode method..");
		try {
			FlushModeType flushModeType = entityManager.getFlushMode();
			log.debug("FlushMode Type-->" + flushModeType);
			log.info("End of getFlushMode method..");
			return flushModeType;
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}

	}

	/**
	 * <b> setFlushMode </b>method set the flush mode to all objects in the
	 * persistence context.
	 * 
	 * @param paramFlushModeType
	 *            - flush mode type
	 * @throws SystemException
	 */
	public void setFlushMode(FlushModeType paramFlushModeType)
			throws HexApplicationException {
		log.info("Inside setFlushMode method..");
		try {
			if (paramFlushModeType != null)
				entityManager.setFlushMode(paramFlushModeType);
			log.info("End of setFlushMode method..");
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> lock </b>method lock the given entity instance with the specified
	 * lock mode type.
	 * 
	 * @param object
	 *            -Entity Object to be locked
	 * @param lockModeType
	 *            -mode of lock
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void lock(Object object, LockModeType lockModeType)
			throws HexApplicationException {
		log.info("Inside lock method..");
		try {
			if (object != null && lockModeType != null) {
				if (!isContainerManaged)
					entityManager.getTransaction().begin();
				entityManager.lock(object, lockModeType);
				if (!isContainerManaged)
					entityManager.getTransaction().commit();
				log.info("End of lock method..");
			}
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (OptimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (LockTimeoutException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> lock </b>method lock the given entity instance with the specified
	 * lock mode type and with specified properties.
	 * 
	 * @param object
	 *            -Entity Object to be locked
	 * @param lockModeType
	 *            -mode of lock
	 * @param propertiesMap
	 *            - standard and vendor-specific properties and hints map
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void lock(Object object, LockModeType lockModeType,
			Map<String, Object> propertiesMap) throws HexApplicationException {
		log.info("Inside lock method..");
		try {
			if (!isContainerManaged)
				entityManager.getTransaction().begin();
			entityManager.lock(object, lockModeType, propertiesMap);
			if (!isContainerManaged)
				entityManager.getTransaction().commit();
			log.info("End of lock method..");
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (OptimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (LockTimeoutException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> refresh </b>method refresh the state of the object from the database.
	 * 
	 * @param object
	 *            - Object to be refreshed
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void refresh(Object object) throws HexApplicationException {
		log.info("Inside refresh method..");
		try {
			if (object != null) {
				if (!isContainerManaged)
					entityManager.getTransaction().begin();
				entityManager.refresh(object);
				if (!isContainerManaged)
					entityManager.getTransaction().commit();
				log.info("End of refresh method..");
			}
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (EntityNotFoundException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> refresh </b>method refresh the state of the object from the database
	 * using the specified properties.
	 * 
	 * @param object
	 *            - Object to be refreshed
	 * @param propertiesMap
	 *            -standard and vendor-specific properties and hints map
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void refresh(Object object, Map<String, Object> propertiesMap)
			throws HexApplicationException {
		log.info("Inside refresh method..");
		try {
			if (!isContainerManaged)
				entityManager.getTransaction().begin();
			entityManager.refresh(object, propertiesMap);
			if (!isContainerManaged)
				entityManager.getTransaction().commit();
			log.info("End of refresh method..");
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (EntityNotFoundException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> refresh </b>method refresh the state of the object from the database
	 * and lock it with respect to given lock mode type.
	 * 
	 * @param object
	 *            - Object to be refreshed
	 * @param lockModeType
	 *            -lock mode type
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void refresh(Object object, LockModeType lockModeType)
			throws HexApplicationException {
		log.info("Inside refresh method..");
		try {
			if (!isContainerManaged)
				entityManager.getTransaction().begin();
			entityManager.refresh(object, lockModeType);
			if (!isContainerManaged)
				entityManager.getTransaction().commit();
			log.info("End of refresh method..");
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (EntityNotFoundException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (LockTimeoutException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> refresh </b>method refresh the state of the object from the database
	 * and lock it with respect to given lock mode type.
	 * 
	 * @param object
	 *            - Object to be refreshed
	 * @param lockModeType
	 *            -lock mode type
	 * @param propertiesMap
	 *            -standard and vendor-specific properties and hints map
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void refresh(Object object, LockModeType lockModeType,
			Map<String, Object> propertiesMap) throws HexApplicationException {
		log.info("Inside refresh method..");
		try {
			if (!isContainerManaged)
				entityManager.getTransaction().begin();
			entityManager.refresh(object, lockModeType, propertiesMap);
			if (!isContainerManaged)
				entityManager.getTransaction().commit();
			log.info("End of refresh method..");
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (EntityNotFoundException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (LockTimeoutException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> getReference </b>method get an instance, whose state may be lazily
	 * fetched.
	 * 
	 * @param clazz
	 *            -Entity class
	 * @param object
	 *            -Entity object to be found
	 * @return Object-Entity object
	 * @throws SelectException
	 * @throws SystemException
	 */
	public Object getReference(Class clazz, Object primaryKey)
			throws HexApplicationException {
		log.info("Inside select method..");
		try {
			if (primaryKey != null && primaryKey != "") {
				Object result = entityManager.getReference(clazz, primaryKey);
				log.debug(result.toString());
				log.info("Object found successfully!");
				log.info("End of select method..");
				return result;
			}
		} catch (EntityNotFoundException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
		return null;
	}

	/**
	 * <b> getAllObjects </b> method fetch all the objects from the database.
	 * 
	 * @param entityName
	 *            - Name of the Entity
	 * @param orgIdAttribute - orgId attribute name
	 * @return List -List of Entity objects
	 * @throws SystemException
	 */
	public List getAllObjects(String entityName, String orgIdAttribute) throws HexApplicationException {
		log.info("Inside getAllObjects method.."+entityName);
		try {
			if (entityName != null && entityName != "") {
				String query = "SELECT entity FROM "
						+ entityName + " entity";
						
				List result = entityManager.createQuery(query).getResultList();
				log.debug("Result list size-->" + result.size());
				log.info("Objects retrieved successfully!");
				log.info("End of getAllObjects method..");
				return result;
			}
		} catch (NoResultException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
		return null;
	}

	/**
	 * <b> getAllObjects </b> method fetch the range of Entity objects from the
	 * database.
	 * 
	 * @param startRecord
	 *            - index of the starting record
	 * @param endRecord
	 *            - index of the ending record
	 * @param entityName
	 *            - Name of the Entity
	 * @param orgIdAttribute - orgId attribute name
	 * @return List -List of Entity objects
	 * @throws SelectException
	 * @throws SystemException
	 */
	public List getAllObjects(int startRecord, int endRecord, String entityName, String orgIdAttribute)
			throws HexApplicationException {
		log.info("Inside getAllObjects method.."+startRecord+entityName);
		try {
			log.debug("startRecord-->" + startRecord);
			log.debug("endRecord-->" + endRecord);
			log.debug("entityName-->" + entityName);
			
			if (entityName != null && entityName != "") {
				String query = "SELECT entity FROM " + entityName + " entity";
				
			Query createQuery = entityManager.createQuery(query)
					.setFirstResult(startRecord).setMaxResults(endRecord);
			List resultList = createQuery.getResultList();
			log.debug("List size-->" + resultList.size());
			log.info("Objects retrieved successfully!");
			log.info("End of getAllObjects method..");
			return resultList;
			}
		} catch (NoResultException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
		return null;
	}

	/**
	 * <b> validateObject </b> method validates the Entity object against the
	 * specified validation constraints.
	 * 
	 * @param object
	 *            - Object to be validate
	 * @return boolean -returns the boolean value according to the validation of
	 *         the object
	 * @throws ValidationException
	 */
	private boolean validateObject(Object object) throws HexApplicationException {
		log.info("Inside validateObject method..");
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Object>> constraintViolations = null;
		constraintViolations = validator.validate(object);
		if (constraintViolations.size() > 0) {
			StringBuffer error = new StringBuffer();
			Iterator<ConstraintViolation<Object>> iterator = constraintViolations
					.iterator();
			while (iterator.hasNext()) {
				ConstraintViolation<Object> constraintviolation = iterator
						.next();
				String constraintsMessage = constraintviolation.getMessage();
				javax.validation.Path columnName = constraintviolation
						.getPropertyPath();
				error.append(columnName.toString() + "--->"
						+ constraintsMessage);
			}
			log.error(error.toString());
			throw new HexApplicationException(error.toString());
		} else {
			return false;
		}
	}

	/**
	 * <b> executeQuery </b> method execute the given query depending on the
	 * queryResultType.
	 * 
	 * @param queryString
	 *            -Query to be executed
	 * @param paramMap
	 *            -Query parameter map
	 * @param queryResultType
	 *            - queryResultType mode
	 * @return Object -Entity object depending on the queryResultType
	 * @throws SelectException
	 * @throws SystemException
	 */
	public List executeQuery(String queryString,
			Map<Object, Object> paramMap, QueryResultType queryResultType)
			throws HexApplicationException {
		log.info("Inside executeQuery method.." + queryResultType);
		try {
			List resultList = null;
			Query query = entityManager.createQuery(queryString);
			setQueryParameters(query, paramMap);
			log.debug("queryResultType-->" + queryResultType);
			if (queryResultType.equals(queryResultType.LIST)) {
				resultList = query.getResultList();
				log.debug("List size-->" + resultList.size());
				log.debug("Objects retrieved successfully!");
				log.info("End of executeQuery method..");
				return resultList;
			} else {
				resultList = new ArrayList();
				Object result = query.getSingleResult();
				resultList.add(result);
				log.debug("result object" + result.toString());
				log.info("Object retrieved successfully!");
				log.info("End of executeQuery method..");
				return resultList;
			}
		} catch (NoResultException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}
	
	/**
	 * <b> executeQuery </b> method execute the given query depending on the
	 * queryResultType.
	 * 
	 * @param queryString
	 *            -Query to be executed	 
	 * @param queryResultType
	 *            - queryResultType mode
	 * @return Object -Entity object depending on the queryResultType
	 * @throws SelectException
	 * @throws SystemException
	 */
	public List executeQuery(String queryString, QueryResultType queryResultType)
			throws HexApplicationException {
		log.info("Inside executeQuery method.." + queryResultType);
		try {
			List resultList = null;
			Query query = entityManager.createQuery(queryString);
			log.debug("queryResultType-->" + queryResultType);
			if (queryResultType.equals(queryResultType.LIST)) {
				resultList = query.getResultList();
				log.debug("List size-->" + resultList.size());
				log.debug("Objects retrieved successfully!");
				log.info("End of executeQuery method..");
				return resultList;
			} else {
				resultList = new ArrayList();
				Object result = query.getSingleResult();
				resultList.add(result);
				log.debug("result object" + result.toString());
				log.info("Object retrieved successfully!");
				log.info("End of executeQuery method..");
				return resultList;
			}
		} catch (NoResultException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> executeNamedQuery </b> method execute the given named query depending
	 * on the queryResultType.
	 * 
	 * @param namedQuery
	 *            -Query name to be executed
	 * @param paramMap
	 *            -Query parameter map
	 * @param queryResultType
	 *            - queryResultType mode
	 * @return Object -Entity object depending on the queryResultType
	 * @throws SelectException
	 * @throws SystemException
	 */
	public Object executeNamedQuery(String namedQuery,
			Map<Object, Object> paramMap, QueryResultType queryResultType)
			throws HexApplicationException {
		log.info("Inside executeNamedQuery method..");
		try {
			Query query = entityManager.createNamedQuery(namedQuery);
			setQueryParameters(query, paramMap);
			if (queryResultType.equals(queryResultType.LIST)) {
				System.out.println("list..");
				List resultList = query.getResultList();
				System.out.println("List size-->" + resultList.size());
				log.info("Objects retrieved successfully!");
				log.info("End of executeNamedQuery method..");
				return resultList;
			} else {
				System.out.println("obj..");
				Object result = query.getSingleResult();
				log.debug("result object" + result.toString());
				log.info("Object retrieved successfully!");
				log.info("End of executeNamedQuery method..");
				return result;
			}
		} catch (NoResultException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> createNativeQuery </b> method execute the given named query depending
	 * on the queryResultType.
	 * 
	 * @param nativeQuery
	 *            -Native query to be executed
	 * @param paramMap
	 *            -Query parameter map
	 * @param queryResultType
	 *            - queryResultType mode
	 * @return Object -Entity object depending on the queryResultType
	 * @throws SelectException
	 * @throws SystemException
	 */
	public Object executeNativeQuery(String nativeQuery,
			Map<Object, Object> paramMap, QueryResultType queryResultType,
			Class clazz) throws HexApplicationException {
		log.info("Inside executeNativeQuery method..");
		try {
			Query query = entityManager.createNativeQuery(nativeQuery, clazz);
			setQueryParameters(query, paramMap);
			if (queryResultType.equals(queryResultType.LIST)) {
				List resultList = query.getResultList();
				log.debug("List size-->" + resultList.size());
				log.info("Objects retrieved successfully!");
				log.info("End of executeNativeQuery method..");
				return resultList;
			} else {
				Object result = query.getSingleResult();
				log.debug("result object" + result.toString());
				log.info("Object retrieved successfully!");
				log.info("End of executeNativeQuery method..");
				return result;
			}
		} catch (NoResultException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> updateByCreateQuery </b> method execute the given queryString.
	 * 
	 * @param queryString
	 *            -Query to be executed
	 * @param paramMap
	 *            -Query parameter map
	 * @return int -either (1) the row count for 0 for query which returns
	 *         nothing
	 * @throws SelectException
	 * @throws SystemException
	 */
	public int updateByCreateQuery(String queryString,
			Map<Object, Object> paramMap) throws HexApplicationException {
		log.info("Inside updateByCreateQuery method..");
		try {
			Query query = entityManager.createQuery(queryString);
			setQueryParameters(query, paramMap);
			if (!isContainerManaged)
				entityManager.getTransaction().begin();
			int result = query.executeUpdate();
			if (!isContainerManaged)
				entityManager.getTransaction().commit();
			log.debug("result int-->" + result);
			log.info("Objects retrieved successfully!");
			log.info("End of updateByCreateQuery method..");
			return result;
		} catch (NoResultException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> updateByNamedQuery </b> method execute the given named query.
	 * 
	 * @param namedQuery
	 *            -Query name to be executed
	 * @param paramMap
	 *            -Query parameter map
	 * @return int -either (1) the row count for 0 for query which returns
	 *         nothing
	 * @throws SelectException
	 * @throws SystemException
	 */
	public int updateByNamedQuery(String namedQuery,
			Map<Object, Object> paramMap) throws HexApplicationException {
		log.info("Inside updateByNamedQuery method..");
		try {
			Query query = entityManager.createNamedQuery(namedQuery);
			setQueryParameters(query, paramMap);
			if (!isContainerManaged)
				entityManager.getTransaction().begin();
			int result = query.executeUpdate();
			if (!isContainerManaged)
				entityManager.getTransaction().commit();
			log.debug("result int-->" + result);
			log.info("Objects retrieved successfully!");
			log.info("End of updateByNamedQuery method..");
			return result;
		} catch (NoResultException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> updateByNativeQuery </b> method execute the given nativeQuery.
	 * 
	 * @param nativeQuery
	 *            -Native query to be executed
	 * @param paramMap
	 *            -Query parameter map
	 * @return int -either (1) the row count for 0 for query which returns
	 *         nothing
	 * @throws SelectException
	 * @throws SystemException
	 */
	public int updateByNativeQuery(String nativeQuery,
			Map<Object, Object> paramMap) throws HexApplicationException {
		log.info("Inside updateByNativeQuery method..");
		try {
			Query query = entityManager.createNativeQuery(nativeQuery);
			setQueryParameters(query, paramMap);
			if (!isContainerManaged)
				entityManager.getTransaction().begin();
			int result = query.executeUpdate();
			if (!isContainerManaged)
				entityManager.getTransaction().commit();
			log.debug("result int-->" + result);
			log.info("Objects retrieved successfully!");
			log.info("End of updateByNativeQuery method..");
			return result;
		} catch (NoResultException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (TransactionRequiredException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PessimisticLockException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		} catch (PersistenceException exception) {
			log.error(exception.getMessage());
			throw new HexApplicationException(exception);
		}
	}

	/**
	 * <b> setQueryParameters </b> method sets the given parameter with the
	 * given query.
	 * 
	 * @param query
	 *            - Querey in which the parameter has to be assigned
	 * @param paramMap
	 *            - Map of parameters to be assigned with query
	 */
	private void setQueryParameters(Query query, Map<Object, Object> paramMap) {
		if (paramMap != null && paramMap.size() > 0) {
			Iterator it = paramMap.keySet().iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				if (obj instanceof String) {
					query.setParameter((String) obj, paramMap.get(obj));
				}
				if (obj instanceof Integer) {
					query.setParameter((Integer) obj, paramMap.get(obj));
				}
				if (obj instanceof Parameter) {
					query.setParameter((Parameter) obj, paramMap.get(obj));
				}
			}
		}
	}

}