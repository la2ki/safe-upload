package rs.marko.lalic.safe.core.processors.person;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import rs.marko.lalic.safe.core.UUIDGenerator;
import rs.marko.lalic.safe.core.Utils;
import rs.marko.lalic.safe.core.constants.DBConstants;
import rs.marko.lalic.safe.core.exceptions.BaseException;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
import rs.marko.lalic.safe.core.exceptions.InvalidRequestException;
import rs.marko.lalic.safe.core.exceptions.ObjectExistsException;
import rs.marko.lalic.safe.core.services.DBFolderService;
import rs.marko.lalic.safe.core.services.FileSystemService;
import rs.marko.lalic.safe.core.services.PersonService;
import rs.marko.lalic.safe.core.services.QueryService;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Processor used for registering person
 * Created by Marko Lalic on 7/15/2017.
 */
public class RegisterPersonProcessor {

    /**
     * Class logger
     */
    private static final Logger LOGGER = Logger.getLogger(RegisterPersonProcessor.class);
    /**
     * FileSystemService instance
     */
    @Autowired
    private FileSystemService fileSystemService;
    /**
     * PersonService instance
     */
    @Autowired
    private PersonService personService;
    /**
     * DBFolderService instance
     */
    @Autowired
    private DBFolderService dbFolderService;
    /**
     * TransactionTemplate instance
     */
    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * Method will execute transaction for registering person
     * @param request JSON from request
     * @throws BaseException thrown if some internal error happens
     */
    public void process(ObjectNode request) throws BaseException {
	if (LOGGER.isDebugEnabled()) {
	    LOGGER.debug(Utils.buildString("Registering person. JSON: ", request.toString()));
	}
	try {
	    transactionTemplate.execute(new RegisterPersonTransaction(request));
	}
	catch (RuntimeException e) {
	    throw (BaseException) e.getCause();
	}
    }

    /**
     * Transaction for registering person
     */
    private class RegisterPersonTransaction implements TransactionCallback {
	private ObjectNode request;

	public RegisterPersonTransaction(ObjectNode request) {
	    this.request = request;
	}

	/**
	 * Transaction will record person metadata in database and create root folder for person
	 *
	 * @param status
	 * @return
	 */
	@Override
	public Object doInTransaction(TransactionStatus status) {
	    try {
		String personUUID = personService.addPerson(request);
		dbFolderService.createFolderDB(fileSystemService.getRootFolder().getAbsolutePath(), personUUID,
				personUUID, true);
		fileSystemService.createFolder(fileSystemService.getRootFolder(), personUUID);
		return null;
	    }
	    catch (BaseException e) {
		status.setRollbackOnly();
		throw new RuntimeException(e);
	    }
	    catch (Exception e) {
		status.setRollbackOnly();
		throw new RuntimeException(new InternalErrorException(
				Utils.buildString("Unknown error occurred while person register. JSON: ",
						request.toString(), ". Message: ", e.getMessage())));
	    }
	}
    }

}
