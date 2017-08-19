package rs.marko.lalic.safe.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
import rs.marko.lalic.safe.core.exceptions.InvalidRequestException;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils class containing utility methods
 * Created by Marko Lalic on 7/4/2017.
 */
public class Utils {
    /**
     * JSON Mapper
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();
    /**
     * Patter for validating email address
     */
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern
		    .compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    /**
     * Patter for validating password
     */
    public static final Pattern VALID_PASS_REGEX = Pattern
		    .compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
				    Pattern.CASE_INSENSITIVE);
    /**
     * For hashing password
     */
    @Value("${service.hash.salt}")
    public String salt = null;

    /**
     * Building string by creating {@link StringBuilder}, and than appending all elements from <code>args</code>
     * {@link Object} array.
     * <p/>
     * If passed array is <code>null</code> or empty, method will return <code>null</code>
     *
     * @param args Array of {@link Object} to append to {@link StringBuilder}
     * @return Reference to the newly built String object
     * @author Marko Lalic
     */
    public static String buildString(Object... args) {
	if (args == null || args.length == 0) {
	    return null;
	}
	return appendToBuffer(new StringBuilder(), args).toString();
    }

    /**
     * Building string while using existing {@link StringBuilder}, and than appending all elements from <code>args</code>
     * {@link Object} array.
     * <p/>
     * If passed array is <code>null</code> or empty, method will return <code>null</code>
     *
     * @param bufferToUse StringBuilder to append objects to
     * @param args        Array of {@link Object} to append to {@link StringBuilder}
     * @return Reference to the newly built String object
     * @author Marko Lalic
     */
    public static String buildString(StringBuilder bufferToUse, Object... args) {
	if (args == null || args.length == 0) {
	    return null;
	}
	return appendToBuffer(true, bufferToUse, args).toString();
    }

    /**
     * Appending all elements from <code>args</code> {@link Object} array to passed {@link StringBuilder}. If builder is
     * null, it will be created.
     * <p/>
     * If passed array is <code>null</code> or empty, method will return <code>null</code>
     *
     * @param buffer      StringBuilder to append objects to
     * @param resetBuffer Flag to indicate if specified buffer should be cleared before new objects are added.
     * @param args        Array of {@link Object} to append to {@link StringBuilder}
     * @return Reference to passed {@link StringBuilder} object
     * @author Marko Lalic
     */
    public static StringBuilder appendToBuffer(boolean resetBuffer, StringBuilder buffer, Object... args) {
	if (args == null || args.length == 0) {
	    return null;
	}
	if (buffer == null) {
	    buffer = new StringBuilder();
	}
	else if (resetBuffer) {
	    buffer.setLength(0);
	}
	for (Object obj : args) {
	    buffer.append(obj);
	}
	return buffer;
    }

    /**
     * Appending all elements from <code>args</code> {@link Object} array to passed {@link StringBuilder}. If builder is
     * null, it will be created.
     * <p/>
     * If passed array is <code>null</code> or empty, method will return <code>null</code>
     *
     * @param buffer StringBuilder to append objects to
     * @param args   Array of {@link Object} to append to {@link StringBuilder}
     * @return Reference to passed {@link StringBuilder} object
     * @author Marko Lalic
     */
    public static StringBuilder appendToBuffer(StringBuilder buffer, Object... args) {
	if (args == null || args.length == 0) {
	    return null;
	}
	if (buffer == null) {
	    buffer = new StringBuilder();
	}
	for (Object obj : args) {
	    buffer.append(obj);
	}
	return buffer;
    }

    /**
     * Parsing {@link String} into JSON object using {@link ObjectMapper} from the latest version of Jackson JSON
     * library.
     * <p/>
     * If parsing fails or passed string is <code>null</code>, exception will be thrown
     *
     * @param json String representation of JSON object
     * @return {@link JsonNode} object
     * @throws InvalidRequestException Thrown if JSON parsing fails, or passed string is <code>null</code>
     */
    public static JsonNode parseJson(String json) throws InvalidRequestException {
	if (json == null) {
	    throw new InvalidRequestException("NULL String cannot be parsed into JSON!");
	}
	try {
	    return MAPPER.readTree(json);
	}
	catch (IOException e) {
	    throw new InvalidRequestException(
			    Utils.buildString("Failed to parse JSON [", json, "] Reason [", e.getMessage(), "]"), e);
	}
    }

    /**
     * This method will create list of object parameters from arguments passed to the method.
     * This list is commonly used for creating parameters for querying database.
     *
     * @param params Parameters of any type
     * @return List of parameters passed to the method
     */
    public static List<Object> getParametersList(Object... params) {
	List<Object> paramList = new ArrayList<>();
	for (Object param : params) {
	    paramList.add(param);
	}
	return paramList;
    }

    /**
     * Method for generating error response JSON
     *
     * @param msg Error message
     * @return Error response JSON
     */
    public static JsonNode generateErrorResponse(String msg) {
	ObjectNode responseNode = MAPPER.createObjectNode();
	responseNode.put("type", "error");
	responseNode.put("message", msg);
	return responseNode;
    }

    /**
     * Method fo generating success response
     *
     * @return Success response JSON
     */
    public static JsonNode generateSuccessResponse() {
	ObjectNode responseNode = MAPPER.createObjectNode();
	responseNode.put("type", "success");
	return responseNode;
    }

    /**
     * Method for checking if email is valid
     *
     * @param email String containing email
     * @return <code>TRUE</code> if email is valid, or <code>FALSE</code> if it isn't
     */
    public static boolean isEmailValid(String email) {
	Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
	return matcher.find();
    }

    /**
     * Method for checking if password is valid
     *
     * @param pass String containing password
     * @return <code>TRUE</code> if password is valid, or <code>FALSE</code> if it isn't
     */
    public static boolean isPassValid(String pass) {
	Matcher matcher = VALID_PASS_REGEX.matcher(pass);
	return matcher.find();
    }

    /**
     * Extracts content of {@link MultipartFile} and transfers it to temporary file, using
     * {@link File#createTempFile(String, String)}
     *
     * @param mpFile Multipart file
     * @return New temporary file
     * @throws InternalErrorException Thrown in case file transfer fails
     */
    public static File createTempFile(MultipartFile mpFile) throws InternalErrorException {
	File temp = null;
	try {
	    temp = File.createTempFile(mpFile.getOriginalFilename(), String.valueOf(System.currentTimeMillis()));
	    mpFile.transferTo(temp);
	    return temp;
	}
	catch (Exception e) {
	    String err = Utils.buildString("Failed to copy request file to temp file [", temp, "]. Message: ",
			    e.getMessage());
	    throw new InternalErrorException(err, e);
	}
    }

    /**
     * Method will secure password
     *
     * @param password Password for hashing
     * @return secure password
     * @throws InternalErrorException thrown if some internal error happens
     */
    public static String getSecurePassword(String password) throws InternalErrorException {
	String generatedPassword = null;
	try {
	    MessageDigest md = MessageDigest.getInstance("SHA-512");
	    md.update("bv5PehSMfV11Cd".getBytes("UTF-8")); // TODO: Load from property
	    byte[] bytes = md.digest(password.getBytes("UTF-8"));
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < bytes.length; i++) {
		sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    generatedPassword = sb.toString();
	}
	catch (Exception e) {
	    throw new InternalErrorException(
			    "Error occurred while trying to secure password. Reason: " + e.getMessage());
	}
	return generatedPassword;
    }
}
