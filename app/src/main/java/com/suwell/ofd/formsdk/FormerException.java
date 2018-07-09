package com.suwell.ofd.formsdk;

/**
 * Created by tl on 2018/7/9.
 */

public class FormerException extends Exception{

    public FormerException() {}

    public FormerException(String message)
    {
        super(message);
    }

    public FormerException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public FormerException(Throwable cause)
    {
        super(cause);
    }
}
