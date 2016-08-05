package com.app.customer.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;

import com.app.customer.vo.Customer;
import com.app.framework.QueryResultType;
import com.app.framework.exception.HexApplicationException;

public interface CustomerDao  {
	 
	/**
	 * <b> insert </b> method inserts a new object in to the database.
	 * 
	 * @param object
	 *            -Entity object to be inserted
	 * @throws InsertException
	 * @throws ValidationException
	 * @throws SystemException
	 */
	public String insert(Customer object) throws HexApplicationException;

	/**
	 * <b> update </b> method updates the given object in the database.
	 * 
	 * @param object
	 *            -Entity object to be update
	 * @throws UpdateException
	 * @throws ValidationException
	 * @throws SystemException
	 */
	public String update(Customer object) throws HexApplicationException;

	/**
	 * <b> select </b> method finds the object from the database.
	 * 
	 * @param clazz
	 *            - Entity class
	 * @param primaryKey
	 *            -PrimarKey of the entity to be found
	 * @return Customer -Customer entity object
	 * @throws SelectException
	 * @throws SystemException
	 */
	public Customer select(Class clazz, Customer primaryKey) throws HexApplicationException;

	/**
	 * <b> select </b> method finds the object from the database.
	 * 
	 * @param clazz
	 *            -Entity class
	 * @param primaryKey
	 *            -primaryKey of the object to be select
	 * @param propertiesMap
	 *            -standard and vendor-specific properties and hints map
	 * @return Customer -Customer entity object
	 * @throws SelectException
	 * @throws SystemException
	 */
	public Customer select(Class clazz, Customer primaryKey,
			Map<String, Object> propertiesMap) 
					throws HexApplicationException;

	/**
	 * <b> select </b> method finds the object from the database.
	 * 
	 * @param clazz
	 *            -Entity class
	 * @param primaryKey
	 *            -primaryKey of the object to be select
	 * @param lockMode
	 *            -mode of lock type
	 * @return Customer -Customer entity object
	 * @throws SelectException
	 * @throws SystemException
	 */
	public Customer select(Class clazz, Customer primaryKey, LockModeType lockMode) 
			throws HexApplicationException;

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
	 * @return Customer -Customer entity object.
	 * @throws SelectException
	 * @throws SystemException
	 */
	public Customer select(Class clazz, Customer primaryKey, LockModeType lockMode,
			Map<String, Object> propertiesMap) throws HexApplicationException;

	/**
	 * <b> delete </b> method removes the object from the database.
	 * 
	 * @param object
	 *            -Entity object to be delete.
	 * @throws DeleteException
	 * @throws SystemException
	 */
	public void delete(Customer object) throws HexApplicationException;

	/**
	 * <b> deleteAll </b> method deletes the list of objects from the
	 * database.
	 * 
	 * @param entries
	 *            -List of entity objects to delete
	 * @throws DeleteException
	 * @throws SystemException
	 */
	public String deleteAll(List entries) throws HexApplicationException;

	/**
	 * <b> getObjectCount </b> method counts the number of Entity objects from
	 * the database.
	 * 
	 * @param entityName
	 *            -Name of the Entity
	 * @return long-Number of Entity objects
	 * @throws SelectException
	 * @throws SystemException
	 */
	public long getObjectCount(String entityName) throws HexApplicationException;

	/**
	 * <b> isContains </b> method Check object belonging to the current
	 * persistence context.
	 * 
	 * @param object
	 *            -Customer to be checked
	 * @return boolean- boolean flag indicating whether entity is present in
	 *         persistence context
	 * 
	 * @throws SystemException
	 */
	public boolean isContains(Customer object) throws HexApplicationException;

	/**
	 * <b> detach </b> method remove the given entity from the persistence
	 * context.
	 * 
	 * @param object
	 *            -object to be removed
	 * @throws SystemException
	 */
	public void detach(Customer object) throws HexApplicationException;

	/**
	 * <b> isOpen </b> method determine whether the entity manager is open.
	 * 
	 * @return boolean-boolean flag indicating entityManager state
	 * @throws SystemException
	 */
	public boolean isOpen() throws HexApplicationException;

	/**
	 * <b> flush </b> method synchronize the persistence context to the
	 * database.
	 * 
	 * @throws SystemException
	 */
	public void flush() throws HexApplicationException;

	/**
	 * <b> clear </b> method clear the persistence context, causing all managed
	 * entities to become detached.
	 * 
	 * @throws ApplicationException
	 */
	public void clear() throws HexApplicationException;

	/**
	 *<b> close </b> method close an application-managed entity manager.
	 * 
	 * @throws SystemException
	 */
	public void close() throws HexApplicationException;

	/**
	 * <b> getProperties </b>method get the properties and hints and associated
	 * with the entity manager. Changing the contents of the map does not change
	 * the configuration.
	 * 
	 * @return Map - properties of the entityManager.
	 * @throws SystemException
	 */
	public Map<String, Object> getProperties() throws HexApplicationException;

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
			throws HexApplicationException;

	/**
	 * <b> getFlushMode </b>method get the flush mode that applies to all
	 * objects in the persistence context.
	 * 
	 * @return FlushModeType -flush mode type of the objects in context.
	 * @throws SystemException
	 */
	public FlushModeType getFlushMode() throws HexApplicationException;

	/**
	 * <b> setFlushMode </b>method set the flush mode to all objects in the
	 * persistence context.
	 * 
	 * @param paramFlushModeType
	 *            - flush mode type
	 * @throws SystemException
	 */
	public void setFlushMode(FlushModeType paramFlushModeType)
			throws HexApplicationException;

	/**
	 * <b> lock </b>method lock the given entity instance with the specified
	 * lock mode type.
	 * 
	 * @param object
	 *            -Entity Customer to be locked
	 * @param lockModeType
	 *            -mode of lock
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void lock(Customer object, LockModeType lockModeType)
			throws HexApplicationException;

	/**
	 * <b> lock </b>method lock the given entity instance with the specified
	 * lock mode type and with specified properties.
	 * 
	 * @param object
	 *            -Entity Customer to be locked
	 * @param lockModeType
	 *            -mode of lock
	 * @param propertiesMap
	 *            - standard and vendor-specific properties
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void lock(Customer object, LockModeType lockModeType,
			Map<String, Object> propertiesMap) throws HexApplicationException;

	/**
	 * <b> refresh </b>method refresh the state of the object from the database.
	 * 
	 * @param object
	 *            - Customer to be refreshed
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void refresh(Customer object) throws HexApplicationException;

	/**
	 * <b> refresh </b>method refresh the state of the object from the database
	 * using the specified properties.
	 * 
	 * @param object
	 *            - Customer to be refreshed
	 * @param propertiesMap
	 *            -standard and vendor-specific properties
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void refresh(Customer object, Map<String, Object> propertiesMap)
			throws HexApplicationException;

	/**
	 * <b> refresh </b>method refresh the state of the object from the database
	 * and lock it with respect to given lock mode type.
	 * 
	 * @param object
	 *            - Customer to be refreshed
	 * @param lockModeType
	 *            -lock mode type
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void refresh(Customer object, LockModeType lockModeType)
			throws HexApplicationException;

	/**
	 * <b> refresh </b>method refresh the state of the object from the database
	 * and lock it with respect to given lock mode type.
	 * 
	 * @param object
	 *            - Customer to be refreshed
	 * @param LockModeType
	 *            lockModeType-lock mode type
	 * @param Map
	 *            <String, Object> propertiesMap-standard and vendor specific
	 *            properties
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	public void refresh(Customer object, LockModeType lockModeType,
			Map<String, Object> propertiesMap) throws HexApplicationException;

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
			throws HexApplicationException;

	/**
	 * <b> getAllObjects </b> method fetch all the objects from the database.
	 * 
	 * @param entityName
	 *            - Name of the Entity
	 * @return List -List of Entity objects
	 * @throws SystemException
	 */
	public List getAllObjects(String entityName) throws HexApplicationException;

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
	 * @return List -List of Entity objects
	 * @throws SelectException
	 * @throws SystemException
	 */
	public List getAllObjects(int startRecord, int endRecord, String entityName)
			throws HexApplicationException;
	/**
	 * <b> setContainerManaged </b> method sets the containerManaged transaction
	 * flag.
	 * 
	 * @param boolean isContainerManaged -flag to be set.
	 */
	public void setContainerManaged(boolean isContainerManaged);
	
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
					throws HexApplicationException;
	
	
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
			throws HexApplicationException;

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
					throws HexApplicationException;

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
			Class clazz) throws HexApplicationException;

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
			Map<Object, Object> paramMap) throws HexApplicationException;

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
			Map<Object, Object> paramMap) throws HexApplicationException;

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
			Map<Object, Object> paramMap) throws HexApplicationException;
 
	/* User Specific methods STARTs here..*/
	public void closeEntityManager();
	
	public void createEntityManager();
	/* User Specific methods ENDs here..*/
			
}
