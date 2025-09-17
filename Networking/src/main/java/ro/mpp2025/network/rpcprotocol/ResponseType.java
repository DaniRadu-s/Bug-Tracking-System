package ro.mpp2025.network.rpcprotocol;

public enum ResponseType {
    OK,
    ERROR,
    GET_TESTERS,
    GET_PROGRAMMERS,
    FIND_BY_EMAIL_TESTER,
    FIND_BY_EMAIL_PROGRAMMER,
    FIND_BY_EMAIL_ADMIN,
    FIND_BY_NAME_BUG,
    UPDATED_PROGRAMMER,
    GET_BUGS, UPDATED_BUG, REPORTED_BUG, DELETED_BUG;

    private ResponseType() {
    }
}
