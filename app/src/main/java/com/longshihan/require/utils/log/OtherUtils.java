package com.longshihan.require.utils.log;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.longshihan.require.utils.LogUtils;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * @author Administrator
 * @time 2016/8/1 16:19
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class OtherUtils {
    private static final int STRING_BUFFER_LENGTH = 100;

    private OtherUtils() {
    }

    public static String getUserAgent(Context context) {
        String webUserAgent = null;
        if(context != null) {
            try {
                Class locale = Class.forName("com.android.internal.R$string");
                Field buffer = locale.getDeclaredField("web_user_agent");
                Integer version = (Integer)buffer.get((Object)null);
                webUserAgent = context.getString(version.intValue());
            } catch (Throwable var7) {
                ;
            }
        }

        if(TextUtils.isEmpty(webUserAgent)) {
            webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 %sSafari/533.1";
        }

        Locale locale1 = Locale.getDefault();
        StringBuffer buffer1 = new StringBuffer();
        String version1 = Build.VERSION.RELEASE;
        if(version1.length() > 0) {
            buffer1.append(version1);
        } else {
            buffer1.append("1.0");
        }

        buffer1.append("; ");
        String language = locale1.getLanguage();
        String id;
        if(language != null) {
            buffer1.append(language.toLowerCase());
            id = locale1.getCountry();
            if(id != null) {
                buffer1.append("-");
                buffer1.append(id.toLowerCase());
            }
        } else {
            buffer1.append("en");
        }

        if("REL".equals(Build.VERSION.CODENAME)) {
            id = Build.MODEL;
            if(id.length() > 0) {
                buffer1.append("; ");
                buffer1.append(id);
            }
        }

        id = Build.ID;
        if(id.length() > 0) {
            buffer1.append(" Build/");
            buffer1.append(id);
        }

        return String.format(webUserAgent, new Object[]{buffer1, "Mobile "});
    }

    public static String getDiskCacheDir(Context context, String dirName) {
        String cachePath = null;
        File cacheDir;
        if("mounted".equals(Environment.getExternalStorageState())) {
            cacheDir = context.getExternalCacheDir();
            if(cacheDir != null) {
                cachePath = cacheDir.getPath();
            }
        }

        if(cachePath == null) {
            cacheDir = context.getCacheDir();
            if(cacheDir != null && cacheDir.exists()) {
                cachePath = cacheDir.getPath();
            }
        }

        return cachePath + File.separator + dirName;
    }

    public static long getAvailableSpace(File dir) {
        try {
            StatFs e = new StatFs(dir.getPath());
            return (long)e.getBlockSize() * (long)e.getAvailableBlocks();
        } catch (Throwable var2) {
            LogUtils.e(var2.getMessage(), var2);
            return -1L;
        }
    }

    public static boolean isSupportRange(HttpResponse response) {
        if(response == null) {
            return false;
        } else {
            Header header = response.getFirstHeader("Accept-Ranges");
            if(header != null) {
                return "bytes".equals(header.getValue());
            } else {
                header = response.getFirstHeader("Content-Range");
                if(header == null) {
                    return false;
                } else {
                    String value = header.getValue();
                    return value != null && value.startsWith("bytes");
                }
            }
        }
    }

    public static String getFileNameFromHttpResponse(HttpResponse response) {
        if(response == null) {
            return null;
        } else {
            String result = null;
            Header header = response.getFirstHeader("Content-Disposition");
            if(header != null) {
                HeaderElement[] arr$ = header.getElements();
                int len$ = arr$.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    HeaderElement element = arr$[i$];
                    NameValuePair fileNamePair = element.getParameterByName("filename");
                    if(fileNamePair != null) {
                        result = fileNamePair.getValue();
                        result = CharsetUtils.toCharset(result, "UTF-8", result.length());
                        break;
                    }
                }
            }

            return result;
        }
    }

    public static Charset getCharsetFromHttpRequest(HttpRequestBase request) {
        if(request == null) {
            return null;
        } else {
            String charsetName = null;
            Header header = request.getFirstHeader("Content-Type");
            if(header != null) {
                HeaderElement[] isSupportedCharset = header.getElements();
                int e = isSupportedCharset.length;

                for(int i$ = 0; i$ < e; ++i$) {
                    HeaderElement element = isSupportedCharset[i$];
                    NameValuePair charsetPair = element.getParameterByName("charset");
                    if(charsetPair != null) {
                        charsetName = charsetPair.getValue();
                        break;
                    }
                }
            }

            boolean var9 = false;
            if(!TextUtils.isEmpty(charsetName)) {
                try {
                    var9 = Charset.isSupported(charsetName);
                } catch (Throwable var8) {
                    ;
                }
            }

            return var9?Charset.forName(charsetName):null;
        }
    }

    public static long sizeOfString(String str, String charset) throws UnsupportedEncodingException {
        if(TextUtils.isEmpty(str)) {
            return 0L;
        } else {
            int len = str.length();
            if(len < 100) {
                return (long)str.getBytes(charset).length;
            } else {
                long size = 0L;

                for(int i = 0; i < len; i += 100) {
                    int end = i + 100;
                    end = end < len?end:len;
                    String temp = getSubString(str, i, end);
                    size += (long)temp.getBytes(charset).length;
                }

                return size;
            }
        }
    }

    public static String getSubString(String str, int start, int end) {
        return new String(str.substring(start, end));
    }

    public static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[3];
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }
}
