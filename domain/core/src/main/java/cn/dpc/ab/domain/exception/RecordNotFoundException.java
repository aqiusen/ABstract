package cn.dpc.ab.domain.exception;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String id) {
        super("Record not found id: ["  + id + "]");
    }
}
