package com.suwell.ofd.formsdk;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tl on 2018/7/9.
 */

@SuppressWarnings("JniMissingFunction")
public class FORMSDK {

    private static final int TYPE_XML = 1;
    private static final int RET_OK = 0;
    private static Lock lock = new ReentrantLock();
    private volatile boolean opened;
    private volatile boolean parsed;
    private volatile boolean closed;
    private long uid;


    static {
        System.loadLibrary("crypto");
        System.loadLibrary("ssl");
        System.loadLibrary("gnustl_shared");
        System.loadLibrary("log4cpp");
        System.loadLibrary("Qt5Core");
        System.loadLibrary("Qt5Gui");
        System.loadLibrary("Qt5Network");
        System.loadLibrary("Qt5Xml");
        System.loadLibrary("sw3");
        System.loadLibrary("swpdf");
        System.loadLibrary("swsignwrapper");
        System.loadLibrary("swformsdkjni");
    }


    private FORMSDK openZIP(String pkg) throws FormerException {
        open(pkg, 0);
        return this;
    }

    private FORMSDK openXML(String xml) throws FormerException {
        open(xml, 1);
        return this;
    }

    private void open(String path, int type) throws FormerException {
        checkClosed();
        this.uid = Open(path, type);
        if (this.uid == 0L) {
            throw new FormerException("Open failed");
        }
        this.opened = true;
    }

    private void checkClosed() throws FormerException {
        if (this.closed) {
            throw new FormerException("SDK already closed!");
        }
    }

    private void checkOpen() throws FormerException {
        if (!this.opened) {
            throw new FormerException("Need call open method");
        }
    }

    private FORMSDK parse() throws FormerException {
        checkClosed();
        checkOpen();
        if (Parse(this.uid) != 0) {
            throw new FormerException("Parse failed");
        }
        this.parsed = true;
        return this;
    }

    private FORMSDK save(String ofd) throws FormerException {
        checkClosed();
        checkOpen();
        if (!this.parsed) {
            throw new FormerException("Need call parse method");
        }
        if (Save(ofd, this.uid) != 0) {
            throw new FormerException("Save failed");
        }
        return this;
    }

    private void close() throws FormerException {
        if (this.uid != 0L) {
            Close(this.uid);
        }
        this.closed = true;
    }


    /* Error */
    public  void parseZIP(String path, String out)
            throws FormerException {

    }

    /* Error */
    public  void parseXML(String path, String out)
            throws FormerException {
       openXML(path).parse().save(out).close();
    }


    private native long Open(String paramString, int paramInt);

    private native int Parse(long paramLong);

    private native int Save(String paramString, long paramLong);

    private native int Close(long paramLong);
}
