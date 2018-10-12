package com.csscaps.tcs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.csscaps.common.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.locks.ReentrantLock;

import CommDevice.USBPort;
import aclasdriver.AclasBaseFunction;
import aclasdriver.Printer;

/**
 * Created by tl on 2018/10/12.
 */

public class PrintUtil {

    private static final String tag = "PrinterDemo";
    public static int DotLineWidth = 384; //576
    static int DotLineBytes = DotLineWidth / 8;
    private Printer mPrinter;
    private PrinterThread pThread = null;
    boolean bFlagPrinterOpen = false;
    private int print_mode = 1;
    boolean bStdEpson = false;
    private String strPrinterSerial = "";


    public void init() {
        mPrinter = new Printer();
        strPrinterSerial = USBPort.getDeviceName(0);
        bStdEpson = Printer.isEpsonPrinter(strPrinterSerial);
        String archStr = java.lang.System.getProperty("os.arch").toUpperCase();
        Log.d(tag, " print_mode:" + print_mode + " strPrinterSerial:" + strPrinterSerial + " bStdEpson:" + bStdEpson + " archStr:" + archStr);
        AclasBaseFunction function = new AclasBaseFunction();
        function.beepWithDevice(null, function.AL_BEEP_ONCE);
    }


    public void optPrinter(boolean bOpen) {
        if (bOpen) {
            int retopen = -1;
            if (!strPrinterSerial.isEmpty()) {
                mPrinter.SetStdEpsonMode(bStdEpson ? 1 : 0);
                retopen = mPrinter.Open(0, new USBPort("", strPrinterSerial, ""));
            } else {
                retopen = mPrinter.Open(0, null);
            }

            if (retopen >= 1) {
                bFlagPrinterOpen = true;
                int ret = mPrinter.SetPrintMode(print_mode);
                Log.d(tag, "Printer open set print mode result:" + ret + " print_mode:" + print_mode);
                int dotWidth = mPrinter.GetDotWidth();
                Log.d(tag, "Printer get dot width:" + dotWidth);
                if (dotWidth > 0) DotLineWidth = dotWidth;
                DotLineBytes = DotLineWidth / 8;
                pThread = new PrinterThread();
                pThread.start();
                //}
            } else {
                Log.e(tag, "Printer open error result:" + retopen);
            }
        } else {
            if (null != pThread) {
                pThread.runflag = false;
                try {
                    pThread.join();
                } catch (Exception e) {
                    // TODO: handle exception
                }
                pThread = null;
                Log.d(tag, "Printer ---> optPrinter false");
                bFlagPrinterOpen = false;
                mPrinter.Stop();
                mPrinter.Close();
                Log.d(tag, "Printer ---> optPrinter false ");
            }
        }
    }

    public void print( Bitmap bitmap) {
        if (pThread != null) {
            pThread.clearPrintBuffer();
        }
        picutre_bmp_print(bitmap);
        if (pThread != null) {
            pThread.startPrintData();
        }

    }

    private void picutre_bmp_print(Bitmap bmp) {
        int height = bmp.getHeight();
        int width = bmp.getWidth();
        int byteofline = DotLineBytes;

        int h = DotLineWidth * height / width;
        Bitmap bitmap = Bitmap.createBitmap(DotLineWidth, h, Bitmap.Config.ARGB_8888);
        Canvas PluCanvas = new Canvas(bitmap);
        PluCanvas.drawColor(Color.TRANSPARENT);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        PluCanvas.drawBitmap(bmp, new Rect(0, 0, width, height), new Rect(0, 0, DotLineWidth, h), paint);
        bmp = bitmap;
        width = DotLineWidth;

        byte[] BitMapBuf = new byte[byteofline];
        int[] tmpbuf = new int[width + 8];

        if (pThread != null) {
            pThread.clearPrintBuffer();
        }

        for (int i = 0; i < h; i++) {
            bmp.getPixels(tmpbuf, 0, width, 0, i, width, 1);

            for (int j = 0; j < width; j += 8) {
                for (int k = 0; k < 8; k++) {
                    if ((tmpbuf[j + k] == Color.TRANSPARENT) || (tmpbuf[j + k] == Color.WHITE)) {
                        BitMapBuf[j / 8] &= ~(0x80 >> k);
                    } else {
                        BitMapBuf[j / 8] |= (0x80 >> k);
                    }
                }
            }
            if (pThread != null) {
                pThread.appendPrintData(BitMapBuf, 0, DotLineBytes);
            }
        }

    }


    class PrinterThread extends Thread {
        ReentrantLock bufferLock = new ReentrantLock();
        boolean runflag = false;
        boolean enablePrint = false;
        public int cutterType = 1;
        public boolean bSerial = false;


        ByteArrayOutputStream printerBuffer = new ByteArrayOutputStream();

        public synchronized void clearPrintBuffer() {
            bufferLock.lock();
            printerBuffer.reset();
            bufferLock.unlock();
        }

        public synchronized void appendPrintData(byte[] data, int offset, int len) {
            bufferLock.lock();
            printerBuffer.write(data, offset, len);
            bufferLock.unlock();
        }

        public synchronized int startPrintData() {
            if (mPrinter == null) {
                return -1;
            }
            if (enablePrint == true) {
                return 1;
            }

            enablePrint = true;
            return 0;
        }

        public synchronized void setFlagSerielPrint(boolean bFlag) {
            bSerial = bFlag;
        }

        private synchronized int printData(int cuttype) {
            int ret = 0;
            final byte cutcmd[] = {0x1d, 0x56, 0x00};
            final byte halfcutcmd[] = {0x1d, 0x56, 0x01};

            if (printerBuffer.size() > 0) {
                bufferLock.lock();

                ret = mPrinter.WriteMix(printerBuffer.toByteArray());//do not use WriteMix,you use Write
                Log.d(tag, "kwq print printData printerBuffer len:" + printerBuffer.size());

                if (cuttype >= 0) {
                    if (print_mode != 0) {
                        mPrinter.SetPrintMode(0);
                    }
                    switch (cuttype) {
                        case 1:
                            mPrinter.Write(halfcutcmd);
                            break;
                        case 0:
                            mPrinter.Write(cutcmd);
                            break;
                        default:
                            break;
                    }
                    if (print_mode != 0) {
                        mPrinter.SetPrintMode(1);
                    }
                }

                bufferLock.unlock();
            }
            return ret;
        }

        public synchronized boolean checkPaper() {
            boolean havePaper = false;
            havePaper = mPrinter.IsPaperExist();
            if (!havePaper) {
                ToastUtil.showLong("没有打印纸了！");
            }
            return havePaper;
        }

        @Override
        public void run() {
            super.run();
            int timer = 0;
            int timerMax = 5;
            boolean havePaper = true;
            while (runflag) {
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (!runflag) break;
                if (!bFlagPrinterOpen) {
                    continue;
                }
                if (timer++ > timerMax) {
                    timer = 0;
                    timerMax = 5;
                    checkPaper();   //check paper status
                }
                if (havePaper && enablePrint) {
                    int ret = printData(cutterType);

                    Log.d(tag, "Print data result0:" + ret);


                    if (bSerial) {

                        try {
                            sleep(0);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    enablePrint = bSerial;

                    if (ret > 0) {  //delay some seconds to wait printer buffer clear
                        timer = 0;
                        timerMax = 10;
                    }
                } else if (enablePrint) {
                    enablePrint = false;
                }
            }
        }

        @Override
        public synchronized void start() {
            runflag = true;
            super.start();
        }
    }
}
