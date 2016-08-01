package com.longshihan.require.utils.log;

/**
 * @author Administrator
 * @time 2016/8/1 16:19
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public interface Logger {
    int V = 2;
    int D = 3;
    int I = 4;
    int W = 5;
    int E = 6;
    int A = 7;

    void v(String var1, String var2);

    void v(String var1, String var2, Throwable var3);

    void v(String var1, String var2, Object[] var3);

    void v(String var1, Throwable var2, String var3, Object[] var4);

    void d(String var1, String var2);

    void d(String var1, String var2, Throwable var3);

    void d(String var1, String var2, Object[] var3);

    void d(String var1, Throwable var2, String var3, Object[] var4);

    void i(String var1, String var2);

    void i(String var1, String var2, Throwable var3);

    void i(String var1, String var2, Object[] var3);

    void i(String var1, Throwable var2, String var3, Object[] var4);

    void w(String var1, String var2);

    void w(String var1, String var2, Throwable var3);

    void w(String var1, Throwable var2);

    void w(String var1, String var2, Object[] var3);

    void w(String var1, Throwable var2, String var3, Object[] var4);

    void e(String var1, String var2);

    void e(String var1, String var2, Throwable var3);

    void e(String var1, String var2, Object[] var3);

    void e(String var1, Throwable var2, String var3, Object[] var4);

    void wtf(String var1, String var2);

    void wtf(String var1, String var2, Throwable var3);

    void wtf(String var1, Throwable var2);

    void wtf(String var1, String var2, Object[] var3);

    void wtf(String var1, Throwable var2, String var3, Object[] var4);

    void trace(int var1, String var2, String var3, String var4);
}
