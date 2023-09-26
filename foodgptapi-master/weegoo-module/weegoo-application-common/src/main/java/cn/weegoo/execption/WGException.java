package cn.weegoo.execption;

/**
 * @author weegoo
 */
public class WGException extends RuntimeException {
    private static final long serialVersionUID = 2359767895161832954L;

    public WGException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }
}
