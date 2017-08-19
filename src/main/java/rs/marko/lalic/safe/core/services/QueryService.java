package rs.marko.lalic.safe.core.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import rs.marko.lalic.safe.core.Utils;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
import rs.marko.lalic.safe.core.exceptions.ObjectExistsException;
import rs.marko.lalic.safe.core.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.Map;

/**
 * Service for query related operations
 *
 * @author Marko Lalic
 */
public class QueryService {
    /**
     * Class logger
     */
    private static final Logger LOGGER = Logger.getLogger(QueryService.class);
    /**
     * Lock object for thread safe instancing
     */
    private static final String[] INSTANCE_MUTEX = new String[0];
    /**
     * Singleton instance
     */
    private static QueryService instance;
    /**
     * JdbcTemplate
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Hidden constructor
     */
    private QueryService() {
    }

    /**
     * Returns singleton instance of {@link QueryService}
     *
     * @return Singleton instance of QueryService
     */
    public static QueryService getInstance() {
	if (instance == null) {
	    synchronized (INSTANCE_MUTEX) {
		if (instance == null) {
		    instance = new QueryService();
		}
	    }
	}
	return instance;
    }

    /**
     * Executes search based on provided query and {@link RowMapper<ObjectNode>}, and returns list of JSON Object.
     * Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query     Query to be executed
     * @param rowMapper Implementation of RowMapper to be used
     * @return List of results
     * @throws InternalErrorException Thrown in case query execution fails
     */
    public List<ObjectNode> executeQueryAndReturnJsonList(String query, RowMapper<ObjectNode> rowMapper)
		    throws InternalErrorException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing query: ", query));
	    }
	    return jdbcTemplate.query(query, rowMapper);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute query [", query, "] Message: ", e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes search based on provided query, {@link List} of parameters and {@link RowMapper<ObjectNode>},
     * than returns list of JSON Object. Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query     Query to be executed
     * @param rowMapper Implementation of RowMapper to be used
     * @param params    List of query parameters
     * @return List of results
     * @throws InternalErrorException Thrown in case query execution fails
     */
    public List<ObjectNode> executeQueryAndReturnJsonList(String query, List<Object> params,
		    RowMapper<ObjectNode> rowMapper) throws InternalErrorException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing query [", query, "] Parameters: ", params));
	    }
	    return jdbcTemplate.query(query, params.toArray(), rowMapper);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute query [", query, "] Parameters: [", params, "] Message: ",
			    e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes search based on provided query, and {@link List} of parameters, then returns list of Map objects
     * Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query  Query to be executed
     * @param params List of query parameters
     * @return List of results
     * @throws InternalErrorException Thrown in case query execution fails
     */
    public List<Map<String, Object>> executeQueryAndReturnMapList(String query, List<Object> params)
		    throws InternalErrorException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing query [", query, "] Parameters: ", params));
	    }
	    return jdbcTemplate.queryForList(query, params.toArray());
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute query [", query, "] Parameters: [", params, "] Message: ",
			    e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes search based on provided query, then returns list of Map objects
     * Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query Query to be executed
     * @return List of results
     * @throws InternalErrorException Thrown in case query execution fails
     */
    public List<Map<String, Object>> executeQueryAndReturnMapList(String query) throws InternalErrorException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing query: ", query));
	    }
	    return jdbcTemplate.queryForList(query);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute query [", query, "] Message: ", e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes search based on provided query and returns back List of objects of specified
     * <code>type</code>. Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query Query to be executed
     * @param type  Expected result type
     * @return List of results
     * @throws InternalErrorException Thrown in case query execution fails
     */
    public <T> List<T> executeQueryAndReturnList(String query, RowMapper<T> type) throws InternalErrorException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing query for list: ", query));
	    }
	    return jdbcTemplate.query(query, type);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute query [", query, "] Expected type [", type, "] Message: ",
			    e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes search based on provided query and returns back List of objects of specified
     * <code>type</code>. Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query  Query to be executed
     * @param params List of query parameters
     * @param type   Expected result type
     * @return List of results
     * @throws InternalErrorException Thrown in case query execution fails
     */
    public <T> List<T> executeQueryAndReturnList(String query, List<Object> params, RowMapper<T> type)
		    throws InternalErrorException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing query for list: ", query));
	    }
	    return jdbcTemplate.query(query, params.toArray(), type);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute query [", query, "] Parameters [ ", params,
			    " ] Expected type [", type, "] Message: ", e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes search based on provided query and returns back List of objects of specified
     * <code>type</code>. Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query Query to be executed
     * @param type  Expected result type
     * @return List of results
     * @throws InternalErrorException Thrown in case query execution fails
     */
    public <T> List<T> executeQueryAndReturnList(String query, Class<T> type) throws InternalErrorException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing query for list: ", query));
	    }
	    return jdbcTemplate.queryForList(query, type);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute query [", query, "] Expected type [", type, "] Message: ",
			    e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes search based on provided query, {@link List} of parameters returns back List of objects of specified
     * <code>type</code>. Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query  Query to be executed
     * @param params List of query parameters
     * @param type   Expected result type
     * @return List of results
     * @throws InternalErrorException Thrown in case query execution fails
     */
    public <T> List<T> executeQueryAndReturnList(String query, List<Object> params, Class<T> type)
		    throws InternalErrorException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing query for list [", query, "] Parameters: ", params));
	    }
	    return jdbcTemplate.queryForList(query, params.toArray(), type);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute query [", query, "] Parameters: [", params,
			    "] Expected type [", type, "] Message: ", e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes search based on provided query, and returns back list of Maps.
     * Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query Query to be executed
     * @return List of results
     * @throws InternalErrorException Thrown in case query execution fails
     */
    public List<Map<String, Object>> executeQueryAndReturnList(String query) throws InternalErrorException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing query for list [", query, "]"));
	    }
	    return jdbcTemplate.queryForList(query);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute query [", query, "] Message: ", e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes query based on provided <code>query</code>, and tries to convert result into object of specified
     * <code>type</code>. Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query Query to be executed
     * @param type  Object type which will be returned as result
     * @return Query result object
     * @throws InternalErrorException  Thrown in case query execution fails
     * @throws ObjectNotFoundException Thrown in case query returns no results
     */
    public <T> T executeQueryAndReturnObject(String query, Class<T> type)
		    throws InternalErrorException, ObjectNotFoundException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Querying for object: ", query));
	    }
	    return jdbcTemplate.queryForObject(query, type);
	}
	catch (EmptyResultDataAccessException e) {
	    String err = Utils.buildString("No results found! Query: ", query);
	    throw new ObjectNotFoundException(err, e);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute query [", query, "] RequiredType: [", type, "] Message: ",
			    e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes query based on provided <code>query</code> and <code>parameters</code>, and tries to convert result
     * into object of specified <code>type</code>. Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query      Query to be executed
     * @param parameters List of query parameters
     * @param type       Object type which will be returned as result
     * @return Query result object
     * @throws InternalErrorException  Thrown in case query execution fails
     * @throws ObjectNotFoundException Thrown in case query returns no results
     */
    public <T> T executeQueryAndReturnObject(String query, List<Object> parameters, Class<T> type)
		    throws InternalErrorException, ObjectNotFoundException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils
				.buildString("Querying for object. Query [", query, "] ExpectedType [", type.toString(),
						"] Parameters ", parameters));
	    }
	    return jdbcTemplate.queryForObject(query, parameters.toArray(), type);
	}
	catch (EmptyResultDataAccessException e) {
	    String err = Utils.buildString("No results found! Query: ", query);
	    throw new ObjectNotFoundException(err, e);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute query [", query, "] Parameters ", parameters,
			    " RequiredType: [", type, "] Message: ", e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes query based on provided <code>query</code> and <code>parameters</code>, and uses provided
     * {@link RowMapper} to build result set. Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query      Query to be executed
     * @param parameters List of query parameters
     * @param mapper     RowMapper to be used for building response
     * @return Query result object
     * @throws InternalErrorException  Thrown in case query execution fails
     * @throws ObjectNotFoundException Thrown in case query returns no results
     */
    public <T> T executeQueryAndReturnObject(String query, List<Object> parameters, RowMapper<T> mapper)
		    throws InternalErrorException, ObjectNotFoundException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Querying for object. Query [", query, "] Parameters ", parameters));
	    }
	    return jdbcTemplate.queryForObject(query, parameters.toArray(), mapper);
	}
	catch (EmptyResultDataAccessException e) {
	    String err = Utils.buildString("No results found! Query: ", query);
	    throw new ObjectNotFoundException(err, e);
	}
	catch (Exception e) {
	    String err = Utils
			    .buildString("Failed to execute query [", query, "] Parameters ", parameters, " Message: ",
					    e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Executes query based on provided <code>query</code> and <code>parameters</code>, and returns result as a
     * {@link Map}. Method assumes that provided input is valid, and it will not validate it.
     *
     * @param query      Query to be executed
     * @param parameters List of query parameters
     * @return Query result object
     * @throws InternalErrorException  Thrown in case query execution fails
     * @throws ObjectNotFoundException Thrown in case query returns no results
     */
    public Map<String, Object> executeQueryAndReturnMap(String query, List<Object> parameters)
		    throws InternalErrorException, ObjectNotFoundException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Querying for map. Query [", query, "] Parameters ", parameters));
	    }
	    return jdbcTemplate.queryForMap(query, parameters.toArray());
	}
	catch (EmptyResultDataAccessException e) {
	    String err = Utils.buildString("No results found! Query: ", query);
	    throw new ObjectNotFoundException(err, e);
	}
	catch (Exception e) {
	    String err = Utils
			    .buildString("Failed to execute query [", query, "] Parameters ", parameters, " Message: ",
					    e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * This method will execute database update
     *
     * @param query  - Query
     * @param params - Query params
     * @throws InternalErrorException - thrown if reassigning fails
     */

    public int executeDatabaseUpdate(String query, List<Object> params) throws InternalErrorException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing database update. Query [", query, "]. Params [",
				params.toString(), "]"));
	    }
	    return jdbcTemplate.update(query, params.toArray());
	}
	catch (Exception e) {
	    throw new InternalErrorException(
			    Utils.buildString("Failed to execute database update. Query [", query, "]. Params [",
					    params.toString(), "]. Reason: [ ", e.getMessage(), " ]."), e);
	}
    }

    /**
     * This method is executing database insert
     *
     * @param params    - Parameters that will be added to record
     * @param tableName - Table name
     * @param columns   - Table columns return numbers of rows affected with insert
     * @throws InternalErrorException - thrown if inserting fails
     */
    public int insertRecordInDatabase(Map<String, Object> params, String tableName, List<String> columns)
		    throws InternalErrorException, ObjectExistsException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing database insert. Table name [ ", tableName, " ]. Columns: [ ",
				columns.toString(), " ]. Params: [ ", params, " ]."));
	    }
	    String[] cols = new String[columns.size()];
	    for (int i = 0; i < columns.size(); i++) {
		cols[i] = columns.get(i);
	    }
	    SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
	    insert.withTableName(tableName);
	    insert.usingColumns(cols);
	    return insert.execute(params);
	}
	catch (DuplicateKeyException e) {
	    String err = Utils.buildString("Failed to execute insert on database. Row already exists. Table name [ ",
			    tableName, " ]. Columns: [ ", columns.toString(), " ]. Params: [ ", params, " ] Reason: ",
			    e.getMessage());
	    throw new ObjectExistsException(err, e);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute insert on database. Table name [ ", tableName,
			    " ]. Columns: [ ", columns.toString(), " ]. Params: [ ", params, " ] Reason: ",
			    e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * This method is executing database insert
     *
     * @param params             - Parameters that will be added to record
     * @param tableName          - Table name
     * @param generatedKeyColumn - Name of the auto-generated key column
     * @throws InternalErrorException - thrown if inserting fails
     */
    public long insertRecordInDatabase(Map<String, Object> params, String tableName, String generatedKeyColumn)
		    throws InternalErrorException, ObjectExistsException {
	try {
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.debug(Utils.buildString("Executing database insert. Table name [ ", tableName, " ] Params: ",
				params));
	    }
	    SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
	    insert.withTableName(tableName);
	    if (!StringUtils.isEmpty(generatedKeyColumn)) {
		insert.usingGeneratedKeyColumns(generatedKeyColumn);
		return insert.executeAndReturnKey(params).longValue();
	    }
	    else {
		return insert.execute(params);
	    }
	}
	catch (DuplicateKeyException e) {
	    String err = Utils.buildString("Failed to execute insert on database. Row already exists. Table name [ ",
			    tableName, " ]. Params: [ ", params, " ] Reason: ", e.getMessage());
	    throw new ObjectExistsException(err, e);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to execute insert on database. Table name [ ", tableName,
			    " ] Params: ", params, " Reason: ", e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * This method is executing database insert
     *
     * @param params    - Parameters that will be added to record
     * @param tableName - Table name
     * @throws InternalErrorException - thrown if inserting fails
     */
    public long insertRecordInDatabase(Map<String, Object> params, String tableName)
		    throws InternalErrorException, ObjectExistsException {
	return insertRecordInDatabase(params, tableName, (String) null);
    }

    /**
     * Method retrieves column value from the database, based on specified arguments
     *
     * @param tableName      Name of the table to be queried
     * @param fieldName      Name of the column which will be retrieved
     * @param criteriaColumn Name of the column that will be used for WHERE statement
     * @param criteriaValue  Value that will be added to WHERE statement
     * @param requiredType   Expected result type
     * @return Column value
     * @throws ObjectNotFoundException Thrown in case row based on specified criteria is not found
     * @throws InternalErrorException  Thrown in case query execution fails
     */
    public <T> T getColumnValue(String tableName, String fieldName, String criteriaColumn, Object criteriaValue,
		    Class<T> requiredType) throws ObjectNotFoundException, InternalErrorException {
	String query = Utils.buildString("SELECT ", fieldName, " FROM ", tableName, " WHERE ", criteriaColumn, " = ?");

	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug(Utils.buildString("Getting column value. Query [", query, "] Param [", criteriaValue, "]"));
	}
	try {
	    return jdbcTemplate.queryForObject(query, new Object[] { criteriaValue }, requiredType);
	}
	catch (EmptyResultDataAccessException e) {
	    String err = Utils.buildString("Unable to find column value. Query [", query, "] Param [", criteriaValue,
			    "]");
	    throw new ObjectNotFoundException(err, e);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to get column value. Query [", query, "] Param [", criteriaValue,
			    "] Message: ", e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }


}