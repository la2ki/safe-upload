package rs.marko.lalic.safe.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import rs.marko.lalic.safe.core.UUIDGenerator;
import rs.marko.lalic.safe.core.Utils;
import rs.marko.lalic.safe.core.constants.DBConstants;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
import rs.marko.lalic.safe.core.exceptions.InvalidRequestException;
import rs.marko.lalic.safe.core.exceptions.ObjectExistsException;
import rs.marko.lalic.safe.core.exceptions.ObjectNotFoundException;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Service used for file databased operations
 * Created by Marko Lalic on 7/4/2017.
 */
public class DBFolderService {

    /**
     * QueryService instance
     */
    @Autowired
    private QueryService queryService;

    /**
     * Empty construct
     */
    public DBFolderService() {
    }

    /**
     * Method will create folder in database
     *
     * @param rootFolderPath Path where folder will be created
     * @param personUUID     UUID of person
     * @param folderName     Folder name
     * @param isRoot         <code>TRUE</code> if folder is root
     * @throws ObjectExistsException   thrown if folder already exists
     * @throws InternalErrorException  thrown if some internal error happens
     * @throws ObjectNotFoundException thrown if root folder is not found
     * @throws InvalidRequestException thrown if root folder is not directory
     */
    public void createFolderDB(String rootFolderPath, String personUUID, String folderName, boolean isRoot)
		    throws ObjectExistsException, InternalErrorException, ObjectNotFoundException,
		    InvalidRequestException {
	String rootFolderUUID = null;
	if (!isRoot) {
	    File rootFolder = new File(rootFolderPath);
	    if (!rootFolder.isDirectory()) {
		throw new InvalidRequestException(Utils.buildString("Parent folder invalid. Path: ", rootFolderPath));
	    }
	    rootFolderUUID = getFolderByName(rootFolder.getPath(), rootFolder.getName(), personUUID);
	}
	Map<String, Object> params = new HashMap<>();
	String folderId = UUIDGenerator.generateUUID();
	params.put(DBConstants.COMMON_USER_UUID, personUUID);
	params.put(DBConstants.COMMON_FOLDER_UUID, folderId);
	params.put(DBConstants.COMMON_CREATED_ON, new Date());
	params.put(DBConstants.COMMON_PATH, Utils.buildString(rootFolderPath, File.separator, folderName));
	params.put(DBConstants.COMMON_NAME, folderName);
	params.put(DBConstants.FOLDER_FOL_FOLDER_ID, rootFolderUUID);
	queryService.insertRecordInDatabase(params, DBConstants.FOLDER_TABLE);
    }

    /**
     * Method will get folder UUID for
     *
     * @param path       Path of folder
     * @param name       Name of folder
     * @param personUUID UUID of person
     * @return Folder UUID
     * @throws ObjectNotFoundException thrown if folder is not found
     * @throws InternalErrorException  thrown if some internal error happens
     */
    private String getFolderByName(String path, String name, String personUUID)
		    throws ObjectNotFoundException, InternalErrorException {
	String query = Utils.buildString("SELECT ", DBConstants.COMMON_FOLDER_UUID, " FROM ", DBConstants.FOLDER_TABLE,
			" WHERE ", DBConstants.COMMON_NAME, "=? AND ", DBConstants.COMMON_PATH, "=? AND ",
			DBConstants.COMMON_USER_UUID, "=?");
	return queryService.executeQueryAndReturnObject(query, Utils.getParametersList(name, path, personUUID),
			String.class);
    }
}
