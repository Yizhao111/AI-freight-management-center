package vip.penint.cloud.common.core.exception;


public class PenintException extends Exception {
    private static final long serialVersionUID = -994962710559017255L;

    public PenintException(String message) {
        super(message);
    }

    public PenintException(Throwable e) {
        super(e.getMessage(), e);
    }
}