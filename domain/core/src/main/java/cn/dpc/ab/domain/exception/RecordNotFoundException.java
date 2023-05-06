package cn.dpc.ab.domain.exception;

/**
 * Exception thrown when a record is not found for the given id.
 */
public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String id) {
        super("Record not found id: ["  + id + "]");
    }
}
