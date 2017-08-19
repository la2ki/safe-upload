package rs.marko.lalic.safe.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import rs.marko.lalic.safe.core.UUIDGenerator;
import rs.marko.lalic.safe.core.Utils;
import rs.marko.lalic.safe.core.constants.DBConstants;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
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
public class DBFileService {

    /**
     * QueryService instance
     */
    @Autowired
    private QueryService queryService;

    /**
     * Empty construct
     */
    public DBFileService() {
    }

    /**
     * Method will add file in database
     * @param fileName File name
     * @param folderUUID UUID of folder
     * @param personUUID UUID of person
     * @return Folder path where file is will be saved
     * @throws InternalErrorException
     * @throws ObjectNotFoundException
     */
    public String addFileDB(String fileName, String folderUUID, String personUUID)
		    throws InternalErrorException, ObjectNotFoundException {
	String folderPath = getFolderPath(folderUUID, personUUID);
	Map<String, Object> params = new HashMap<>();
	String fileId = UUIDGenerator.generateUUID();
	params.put(DBConstants.COMMON_USER_UUID, personUUID);
	params.put(DBConstants.FILE_FILE_UUID, fileId);
	params.put(DBConstants.COMMON_CREATED_ON, new Date());
	params.put(DBConstants.COMMON_PATH, Utils.buildString(folderPath, File.separator, fileName));
	params.put(DBConstants.COMMON_NAME, fileName);
	params.put(DBConstants.COMMON_FOLDER_UUID, folderUUID);
	try {
	    queryService.insertRecordInDatabase(params, DBConstants.FILE_TABLE);
	    return folderPath;
	}
	catch (ObjectExistsException e) {
	    throw new InternalErrorException(e);
	}
    }

    /**
     * Method will get folder path for given UUID
     * @param folderUUID UUD of folder
     * @param personUUID UUID of person
     * @return Folder path
     * @throws ObjectNotFoundException thrown if object is not found
     * @throws InternalErrorException thrown if some internal error happens
     */
    private String getFolderPath(String folderUUID, String personUUID)
		    throws ObjectNotFoundException, InternalErrorException {
	String query = Utils.buildString("SELECT ", DBConstants.COMMON_PATH, " FROM ", DBConstants.FOLDER_TABLE,
			" WHERE ", DBConstants.COMMON_USER_UUID, "=? AND ", DBConstants.COMMON_FOLDER_UUID, "=?");
	return queryService.executeQueryAndReturnObject(query, Utils.getParametersList(personUUID, folderUUID), String.class);
    }
}
