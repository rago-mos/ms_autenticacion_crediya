package co.com.crediya.model.utils;


public class Constant {

    public static final String PATH_VARIABLE = "documentIdentity";
    public static final String MISSING_PATH_VARIABLE = "Path Variable 'document' is required";

    public static final String LOG_INFO_USER_CREATED = "User created successfully: {}";
    public static final String LOG_INFO_USER_EXISTS = "User exists: {}";
    public static final String LOG_INFO_TOKEN = "token created successfully";
    public static final String LOG_LIST_USERS = "Users list: {}";

    public static final String ERROR_BUSINESS_DOCUMENT = "A user with this document already exists ";
    public static final String ERROR_BUSINESS_EMAIL = "A user with this email already exists ";
    public static final String ERROR_BUSINESS_SALARY = "The salary is not valid; it must be between %s and %s";
    public static final String ERROR_REQUEST_LASTNAME = "Last name is null or blank";
    public static final String ERROR_REQUEST_FIRTSNAME = "firtsName is null or blank";
    public static final String ERROR_BAD_CREDENTIALS = "bad credentials";
    public static final String ERROR_ROLE = "role not found";
    public static final String ERROR_BAD_TOKEN = "bad token";
    public static final String ERROR_ACCES_DENIED = "Access denied. You do not have the necessary permissions for this resource";
    public static final String ERROR_ACCES_DENIED_ARGUMENT = "Access denied. You do not have the necessary permissions for this resource: {}";


    private Constant() {
        throw new UnsupportedOperationException("util class");
    }
}
