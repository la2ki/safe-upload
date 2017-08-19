package rs.marko.lalic.safe.core.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Roles class used for database constants
 * Created by Marko Lalic on 7/4/2017.
 */
public class Roles {

    public static final String ROLE_NAME_ADMIN = "ADMIN";
    public static final String ROLE_NAME_USER = "USER";

    public static final int ROLE_ID_ADMIN = 1;
    public static final int ROLE_ID_USER = 1;

    public static final Map<String, Integer> ROLES = new HashMap<>();

    static {
	ROLES.put(ROLE_NAME_ADMIN, ROLE_ID_ADMIN);
	ROLES.put(ROLE_NAME_USER, ROLE_ID_USER);
    }

    /**
     * Private empty construct so that constants class cant be instanced
     */
    private Roles() {
    }
}
