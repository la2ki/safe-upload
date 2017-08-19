package rs.marko.lalic.safe.core.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.marko.lalic.safe.core.Utils;
import rs.marko.lalic.safe.core.audit.Auditable;
import rs.marko.lalic.safe.core.constants.Event;
import rs.marko.lalic.safe.core.exceptions.BaseException;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
import rs.marko.lalic.safe.core.exceptions.InvalidRequestException;
import rs.marko.lalic.safe.core.processors.file.AddFileProcessor;

import java.io.File;

/**
 * FileController
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/file")
public class FileController {
    /**
     * Class logger
     */
    private static final Logger LOGGER = Logger.getLogger(FileController.class);
    /**
     * AddFileProcessor instance
     */
    @Autowired
    private AddFileProcessor addFileProcessor;

    /**
     * API for adding file to folder
     *
     * @param object     JSON from multipart request
     * @param file       File from multipart request
     * @param personUUID UUID of person
     * @return success JSON
     */
    @Auditable(value = Event.EVENT_ADD_FILE)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addFile(@RequestParam(value = "object") String object,
		    @RequestParam(value = "file") MultipartFile file, @RequestHeader String personUUID) {
	try {
	    addFileProcessor.process(Utils.createTempFile(file), (ObjectNode) Utils.parseJson(object), personUUID);
	}
	catch (InvalidRequestException e) {
	    LOGGER.error(e.getMessage(), e);
	    return new ResponseEntity<>(Utils.generateErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
	}
	catch (BaseException e) {
	    LOGGER.error(e.getMessage(), e);
	    return new ResponseEntity<>(Utils.generateErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(Utils.generateSuccessResponse(), HttpStatus.OK);
    }

}
