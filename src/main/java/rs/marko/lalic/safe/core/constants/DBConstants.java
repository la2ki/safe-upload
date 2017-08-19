package rs.marko.lalic.safe.core.constants;

/**
 * Database constants
 * Created by Marko Lalic on 6/9/2017.
 */
public class DBConstants {

    public static final String COMMON_USER_UUID = "USERID";
    public static final String COMMON_FOLDER_UUID = "FOLDERID";
    public static final String COMMON_NAME = "NAME";
    public static final String COMMON_PATH = "PATH";
    public static final String COMMON_DESCRIPTION = "DESCRIPTION";
    public static final String COMMON_CREATED_ON = "CREATEDON";
    public static final String COMMON_ROLE_ID = "ROLEID";
    public static final String COMMON_VALUE = "VALUE";

    public static final String USER_TABLE = "USER";
    public static final String USER_EMAIL = "EMAIL";
    public static final String USER_PASSWORD = "PASSWORD";
    public static final String USER_REGISTRED_ON = "REGISTREDON";
    public static final String USER_LAST_LOGIN = "LASTLOGIN";
    public static final String USER_DISABLED = "DISABLED";

    public static final String FOLDER_TABLE = "FOLDER";
    public static final String FOLDER_FOL_FOLDER_ID = "FOL_FOLDERID";

    public static final String FILE_TABLE = "FILE";
    public static final String FILE_FILE_UUID = "FILEID";
    public static final String FILE_TYPE = "TYPE";
    public static final String FILE_SIZE = "SIZE";

    public static final String ROLE_TABLE = "ROLE";

    public static final String SHARED_TABLE = "SHARED";
    public static final String SHARED_READ_PERSMISSION = "READPERMISSION";
    public static final String SHARED_WRITE_PERSMISSION = "WRITEPERMISSION";

    public static final String CUSTOM_PROPERTY_TABLE = "CUSTOMPROPERTY";
    public static final String CUSTOM_PROPERTY_CUSTOM_PROPERTY_ID = "CUSTOMPROPERTYID";
    public static final String CUSTOM_PROPERTY_VALUE_TYPE = "VALUETYPE";

    public static final String FILE_CUSTOM_PROPERTY_TABLE = "FILECUSTOMPROPERTY";

    /**
     * Private empty construct so that constants class cant be instanced
     */
    private DBConstants() {
    }
}
