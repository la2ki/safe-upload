package rs.marko.lalic.safe.core.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.marko.lalic.safe.core.Utils;
import rs.marko.lalic.safe.core.audit.Auditable;
import rs.marko.lalic.safe.core.constants.Event;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
import rs.marko.lalic.safe.core.exceptions.InvalidRequestException;
import rs.marko.lalic.safe.core.processors.folder.CreateFolderProcessor;

/**
 * FileController
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/folder")
public class FolderController {
    /**
     * Class logger
     */
    private static final Logger LOGGER = Logger.getLogger(FolderController.class);
    /**
     * CreateFolderProcessor instance
     */
    @Autowired
    private CreateFolderProcessor createFolderProcessor;

    /**
     * API for creating folder
     * @param personUUID UUID of person from header
     * @param body JSON body
     * @return success JSON
     */
    @Auditable(value = Event.EVENT_CREATE_FOLDER)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=utf-8", produces = "application/json;charset=utf-8")
    public ResponseEntity<?> createFolder(@RequestHeader String personUUID, @RequestBody String body) {
	try {
	    createFolderProcessor.process((ObjectNode) Utils.parseJson(body), personUUID);
	}
	catch (InvalidRequestException e) {
	    LOGGER.error(e.getMessage(), e);
	    return new ResponseEntity<>(Utils.generateErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	catch (InternalErrorException e) {
	    LOGGER.error(e.getMessage(), e);
	    return new ResponseEntity<>(Utils.generateErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Unknown error occurred. Reason: ", e.getMessage());
	    LOGGER.error(err, e);
	    return new ResponseEntity<>(Utils.generateErrorResponse(err), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(Utils.generateSuccessResponse(), HttpStatus.OK);
    }

}
