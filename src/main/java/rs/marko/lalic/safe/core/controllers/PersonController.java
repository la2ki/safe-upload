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
import rs.marko.lalic.safe.core.exceptions.ObjectNotFoundException;
import rs.marko.lalic.safe.core.processors.person.RegisterPersonProcessor;
import rs.marko.lalic.safe.core.services.PersonService;

/**
 * FileController
 *
 * @since 1.0
 */
@RestController
@RequestMapping("/api/person")
public class PersonController {
    /**
     * Class logger
     */
    private static final Logger LOGGER = Logger.getLogger(PersonController.class);
    /**
     * PersonService instance
     */
    @Autowired
    private PersonService personService;
    /**
     * RegisterPersonProcessor instance
     */
    @Autowired
    private RegisterPersonProcessor registerPersonProcessor;

    /**
     * API for adding new person
     *
     * @param body JSON body
     * @return success JSON
     */
    @Auditable(value = Event.EVENT_ADD_PERSON)
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addPerson(@RequestBody String body) {
	try {
	    registerPersonProcessor.process((ObjectNode) Utils.parseJson(body));
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

    /**
     * API for updating person
     *
     * @param body       JSON from request
     * @param personUUID UUID of person from url
     * @return success JSON
     */
    @Auditable(value = Event.EVENT_UPDATE_PERSON)
    @RequestMapping(method = RequestMethod.PUT, value = "/{personUUID}")
    public ResponseEntity<?> updatePerson(@RequestBody String body, @RequestParam String personUUID) {
	try {
	    personService.updatePerson((ObjectNode) Utils.parseJson(body), personUUID);
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

    /**
     * API for getting persons
     *
     * @return JSON with persons data
     */
    @Auditable(value = Event.EVENT_GET_PERSONS)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getPersons() {
	try {
	    return new ResponseEntity<>(personService.getPersons(), HttpStatus.OK);
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
    }

    /**
     * Api for getting person
     *
     * @param personUUID UUID of person
     * @return JSON with person data
     */
    @Auditable(value = Event.EVENT_GET_PERSON)
    @RequestMapping(method = RequestMethod.GET, value = "/{personUUID}")
    public ResponseEntity<?> getPerson(@PathVariable String personUUID) {
	try {
	    return new ResponseEntity<>(personService.getPerson(personUUID), HttpStatus.OK);
	}
	catch (ObjectNotFoundException e) {
	    LOGGER.error(e.getMessage());
	    return new ResponseEntity<>(Utils.generateErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
	}
	catch (InternalErrorException e) {
	    LOGGER.error(e.getMessage());
	    return new ResponseEntity<>(Utils.generateErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	catch (Exception e) {
	    String err = Utils.buildString("Unknown error occurred. Reason: ", e.getMessage());
	    LOGGER.error(err);
	    return new ResponseEntity<>(Utils.generateErrorResponse(err), HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

}
