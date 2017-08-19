package rs.marko.lalic.safe.core.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import rs.marko.lalic.safe.core.UUIDGenerator;
import rs.marko.lalic.safe.core.Utils;
import rs.marko.lalic.safe.core.constants.DBConstants;
import rs.marko.lalic.safe.core.constants.JsonConstants;
import rs.marko.lalic.safe.core.constants.Roles;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
import rs.marko.lalic.safe.core.exceptions.InvalidRequestException;
import rs.marko.lalic.safe.core.exceptions.ObjectExistsException;
import rs.marko.lalic.safe.core.exceptions.ObjectNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Marko Lalic on 7/4/2017.
 */
public class PersonService {
    /**
     * JSON Mapper
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private QueryService queryService;

    public String addPerson(ObjectNode json) throws InternalErrorException, InvalidRequestException {
	if (!json.has(JsonConstants.FIELD_EMAIL) || !json.has(JsonConstants.FIELD_PASSWORD)) {
	    throw new InvalidRequestException(
			    Utils.buildString("JSON from request is missing required fields. Json: ", json.toString()));
	}
	String email = json.get(JsonConstants.FIELD_EMAIL).asText();
	String pass = json.get(JsonConstants.FIELD_PASSWORD).asText();
	validateRegistrationData(email, pass);
	Map<String, Object> params = new HashMap<>();
	String personUUID = UUIDGenerator.generateUUID();
	params.put(DBConstants.COMMON_USER_UUID, personUUID);
	params.put(DBConstants.USER_EMAIL, email);
	params.put(DBConstants.USER_PASSWORD, Utils.getSecurePassword(pass));
	params.put(DBConstants.COMMON_ROLE_ID, Roles.ROLE_ID_USER);
	params.put(DBConstants.USER_REGISTRED_ON, new Date());
	params.put(DBConstants.USER_LAST_LOGIN, new Date());
	params.put(DBConstants.USER_DISABLED, false);
	try {
	    queryService.insertRecordInDatabase(params, DBConstants.USER_TABLE);
	}
	catch (ObjectExistsException e) {
	    throw new InternalErrorException(e);
	}
	return personUUID;
    }

    public void updatePerson(ObjectNode json, String personUUID)
		    throws InvalidRequestException, InternalErrorException {
	StringBuilder queryBuilder = new StringBuilder();
	Utils.appendToBuffer(queryBuilder, "UPDATE ", DBConstants.USER_TABLE, " SET ");
	List<Object> params = new ArrayList<>();
	if (json.has(JsonConstants.FIELD_EMAIL)) {
	    String email = json.get(JsonConstants.FIELD_EMAIL).asText();
	    if (!Utils.isEmailValid(email)) {
		throw new InvalidRequestException(Utils.buildString("Invalid email. Json: ", json.toString()));
	    }
	    Utils.appendToBuffer(queryBuilder, DBConstants.USER_EMAIL, "=? ");
	    params.add(email);
	}
	if (json.has(JsonConstants.FIELD_PASSWORD)) {
	    String sifra = json.get(JsonConstants.FIELD_PASSWORD).asText();
	    if (!Utils.isPassValid(sifra)) {
		throw new InvalidRequestException(Utils.buildString("Invalid password. Json: ", json.toString()));
	    }
	    if (!params.isEmpty()) {
		Utils.appendToBuffer(queryBuilder, ", ");
	    }
	    Utils.appendToBuffer(queryBuilder, DBConstants.USER_PASSWORD, "=? ");
	    params.add(sifra);
	}
	if (json.has(JsonConstants.FIELD_ROLE)) {
	    String rola = json.get(JsonConstants.FIELD_ROLE).asText();
	    if (!Roles.ROLES.containsKey(rola)) {
		throw new InvalidRequestException(Utils.buildString("Invalid role. Json: ", json.toString()));
	    }
	    if (!params.isEmpty()) {
		Utils.appendToBuffer(queryBuilder, ", ");
	    }
	    Utils.appendToBuffer(queryBuilder, DBConstants.COMMON_ROLE_ID, "=? ");
	    params.add(rola);
	}
	if (json.has(JsonConstants.FIELD_IS_DISABLED)) {
	    boolean iskljucen = json.get(JsonConstants.FIELD_IS_DISABLED).asBoolean();
	    if (!params.isEmpty()) {
		Utils.appendToBuffer(queryBuilder, ", ");
	    }
	    Utils.appendToBuffer(queryBuilder, DBConstants.USER_DISABLED, "=? ");
	    params.add(iskljucen);
	}
	if (params.isEmpty()) {
	    throw new InvalidRequestException(
			    Utils.buildString("Invalid json, no fields to insert. Json: ", json.toString()));
	}
	Utils.appendToBuffer(queryBuilder, " WHERE ", DBConstants.COMMON_USER_UUID, "=? ");
	params.add(personUUID);
	queryService.executeDatabaseUpdate(queryBuilder.toString(), params);
    }

    public ObjectNode getPerson(String personUUID) throws InternalErrorException, ObjectNotFoundException {
	String query = Utils
			.buildString("SELECT * FROM ", DBConstants.USER_TABLE, " WHERE ", DBConstants.COMMON_USER_UUID,
					"=?");
	return queryService.executeQueryAndReturnObject(query, Utils.getParametersList(personUUID), new PersonMapper());
    }

    public List<ObjectNode> getPersons() throws InternalErrorException {
	String query = Utils.buildString("SELECT * FROM ", DBConstants.USER_TABLE);
	return queryService.executeQueryAndReturnJsonList(query, new PersonMapper());
    }

    private void validateRegistrationData(String email, String pass)
		    throws InvalidRequestException, InternalErrorException {
	if (!Utils.isEmailValid(email)) {
	    throw new InvalidRequestException(Utils.buildString("Invalid email. Email: ", email));
	}
	if (!Utils.isPassValid(pass)) {
	    throw new InvalidRequestException(Utils.buildString("Invalid password. Password: ", pass));
	}
	String query = Utils.buildString("SELECT ", DBConstants.COMMON_USER_UUID, " FROM ", DBConstants.USER_TABLE,
			" WHERE ", DBConstants.USER_EMAIL, "=?");
	try {
	    queryService.executeQueryAndReturnObject(query, Utils.getParametersList(email), String.class);
	    throw new InvalidRequestException(Utils.buildString("Person exists with given email. Email: ", email));
	}
	catch (ObjectNotFoundException e) {
	    // No need to handle this
	}
    }

    /**
     * {@link RowMapper} implementation, used when fetching person metadata
     */
    private class PersonMapper implements RowMapper<ObjectNode> {

	@Override
	public ObjectNode mapRow(ResultSet rs, int i) throws SQLException {
	    ObjectNode person = MAPPER.createObjectNode();
	    person.put(DBConstants.COMMON_USER_UUID, rs.getString(DBConstants.COMMON_USER_UUID));
	    person.put(DBConstants.USER_EMAIL, rs.getString(DBConstants.USER_EMAIL));
	    person.put(DBConstants.COMMON_ROLE_ID, rs.getString(DBConstants.COMMON_ROLE_ID));
	    person.put(DBConstants.USER_REGISTRED_ON, rs.getDate(DBConstants.USER_REGISTRED_ON).getTime());
	    person.put(DBConstants.USER_LAST_LOGIN, rs.getDate(DBConstants.USER_LAST_LOGIN).getTime());
	    person.put(DBConstants.USER_DISABLED, rs.getBoolean(DBConstants.USER_DISABLED));
	    return person;
	}
    }
}
