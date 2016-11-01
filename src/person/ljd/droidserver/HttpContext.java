package person.ljd.droidserver;

import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/31.
 */
public class HttpContext {
    private final HashMap<String,String> requestHeaders;

    public HttpContext() {
        requestHeaders = new HashMap<String,String>();
    }

    private Socket underlySocket;

    public Socket getUnderlySocket() {
        return underlySocket;
    }

    public void setUnderlySocket(Socket underlySocket) {
        this.underlySocket = underlySocket;
    }

    public void addRequestHeader(String headerName,String headerValue){
        requestHeaders.put(headerName,headerValue);
    }
    public String getRequestHeaderValue(String headerName){
        return requestHeaders.get(headerName);
    }
}
