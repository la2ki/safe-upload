package rs.marko.lalic.safe.core.processors.file;

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
import rs.marko.lalic.safe.core.services.DBFileService;
import rs.marko.lalic.safe.core.services.FileSystemService;

import java.io.File;

/**
 * This processor is used for adding files to file system
 * Created by Marko Lalic on 7/15/2017.
 */
public class AddFileProcessor {

    /**
     * Class logger
     */
    private static final Logger LOGGER = Logger.getLogger(AddFileProcessor.class);
    /**
     * FileSystemService instance
     */
    @Autowired
    private FileSystemService fileSystemService;
    /**
     * TransactionTemplate instance
     */
    @Autowired
    private TransactionTemplate transactionTemplate;
    /**
     * DBFileService instance
     */
    @Autowired
    private DBFileService dBFileService;

    /**
     * Method will validate received data and execute transaction for adding file
     * @param file File
     * @param request Request JSON
     * @param personUUID UUID of person
     * @throws InvalidRequestException if JSON is invalid
     * @throws InternalErrorException is some internal error happens
     */
    public void process(File file, ObjectNode request, String personUUID)
		    throws BaseException {
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug(Utils.buildString("Adding file. JSON: ", request.toString()));
	}
	if (!request.has(JsonConstants.FIELD_FOLDER_UUID) || !request.has(JsonConstants.FIELD_NAME)) {
	    throw new InvalidRequestException(
			    Utils.buildString("Error occurred while trying to save file. Invalid json: ",
					    request.toString()));
	}
	String folderUUID = request.get(JsonConstants.FIELD_FOLDER_UUID).asText();
	String name = request.get(JsonConstants.FIELD_NAME).asText();
	try {
	    transactionTemplate.execute(new AddFileTransaction(name, file, folderUUID, personUUID));
	}
	catch (RuntimeException e) {
	    throw (BaseException) e.getCause();
	}
    }

    /**
     * Transaction for adding file
     */
    private class AddFileTransaction implements TransactionCallback {
	String name;
	File file;
	String folderUUID;
	String personUUID;

	/**
	 * Transaction construct
	 * @param name Name of file
	 * @param file File
	 * @param folderUUID UUID of folder
	 * @param personUUID UUID of person
	 */
	public AddFileTransaction(String name, File file, String folderUUID, String personUUID) {
	    this.name = name;
	    this.file = file;
	    this.folderUUID = folderUUID;
	    this.personUUID = personUUID;
	}

	/**
	 * This method will add file metadata in database and also in file system
	 * @param status
	 * @return
	 */
	@Override
	public Object doInTransaction(TransactionStatus status) {
	    try {
		String destinationPath = dBFileService.addFileDB(name, folderUUID, personUUID);
		fileSystemService.addFileToFolder(destinationPath, file, name);
	    }
	    catch (BaseException e) {
		status.setRollbackOnly();
		throw new RuntimeException(e);
	    }
	    catch (Exception e) {
		status.setRollbackOnly();
		throw new RuntimeException(new InternalErrorException(
				Utils.buildString("Unknown error occurred while adding file. Folder UUID: ", folderUUID,
						". Person UUID: ", personUUID, ". File name: [", name, "]. Message: ",
						e.getMessage())));
	    }
	    return null;
	}
    }
}
