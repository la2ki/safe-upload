package rs.marko.lalic.safe.core.processors.folder;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import rs.marko.lalic.safe.core.Utils;
import rs.marko.lalic.safe.core.constants.JsonConstants;
import rs.marko.lalic.safe.core.exceptions.BaseException;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
import rs.marko.lalic.safe.core.exceptions.InvalidRequestException;
import rs.marko.lalic.safe.core.services.DBFolderService;
import rs.marko.lalic.safe.core.services.FileSystemService;

import java.io.File;

/**
 * Processor is used for creating folder
 * Created by Marko Lalic on 7/15/2017.
 */
public class CreateFolderProcessor {

    /**
     * Class logger
     */
    private static final Logger LOGGER = Logger.getLogger(CreateFolderProcessor.class);

    /**
     * FileSystemService instance
     */
    @Autowired
    private FileSystemService fileSystemService;

    /**
     * DBFolderService instance
     */
    @Autowired
    private DBFolderService dBFolderService;

    /**
     * TransactionTemplate instance
     */
    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * Method will validate received data and execute transaction for creating folder
     *
     * @param request    Request JSON
     * @param personUUID UUID of person
     * @throws InvalidRequestException thrown if JSON doesn't contain required fields
     * @throws InternalErrorException  thrown if some internal error happens
     */
    public void process(ObjectNode request, String personUUID) throws BaseException {
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug(Utils.buildString("Registering person. JSON: ", request.toString()));
	}
	if (!request.has(JsonConstants.FIELD_NAME) || !request.has(JsonConstants.FIELD_DESTINATION)) {
	    throw new InvalidRequestException(
			    Utils.buildString("Invalid JSON from request, required fields missing. JSON: ",
					    request.toString()));
	}
	String name = request.get(JsonConstants.FIELD_NAME).asText();
	String destination = request.get(JsonConstants.FIELD_DESTINATION).asText();
	try {
	    transactionTemplate.execute(new CreateFolderTransaction(destination, name, personUUID));
	}
	catch (RuntimeException e) {
	    throw (BaseException) e.getCause();
	}
    }

    /**
     * Transaction for creating folders
     */
    private class CreateFolderTransaction implements TransactionCallback {
	private String destination;
	private String folderName;
	private String personUUID;

	/**
	 * Construct for create folder transaction
	 *
	 * @param destination Folder destination
	 * @param folderName  Name of folder
	 * @param personUUID  UUID of person
	 */
	public CreateFolderTransaction(String destination, String folderName, String personUUID) {
	    this.destination = destination;
	    this.folderName = folderName;
	    this.personUUID = personUUID;
	}

	/**
	 * Method will insert folder metadata in database and create it in file system
	 *
	 * @param status
	 * @return
	 */
	@Override
	public Object doInTransaction(TransactionStatus status) {
	    try {
		destination = Utils.buildString(fileSystemService.getRootFolder(), File.separator, destination);
		dBFolderService.createFolderDB(destination, personUUID, folderName, false);
		fileSystemService.createFolder(destination, folderName);
		return null;
	    }
	    catch (BaseException e) {
		status.setRollbackOnly();
		throw new RuntimeException(e);
	    }
	    catch (Exception e) {
		status.setRollbackOnly();
		throw new RuntimeException(new InternalErrorException(
				Utils.buildString("Unknown error occurred while person register. Folder name: [",
						folderName, "]. Person UUID: [", personUUID, "]. Destination: [",
						destination, "]. Message: ", e.getMessage())));
	    }
	}
    }

}
