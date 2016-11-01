package person.ljd.droidserver;

import java.io.*;

/**
 * Created by Administrator on 2016/11/1.
 */
public class UploadImageHandler implements IResourceUriHandler{
    private String acceptPrefix = "/upload_image/";
    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException{
        String tmpPath = "/mnt/sdcard/test_upload.jpg";
        long totallength=Long.parseLong(httpContext.getRequestHeaderValue("Content-Length"));
        FileOutputStream fos = new FileOutputStream(tmpPath);
        InputStream nis = httpContext.getUnderlySocket().getInputStream();
        byte[] buffer = new byte[10240];
        int nReaded=0;
        long nLeftLength=totallength;
        while((nReaded=nis.read(buffer)) > 0 && nLeftLength > 0){
            fos.write(buffer,0,nReaded);
            nLeftLength -= nReaded;
        }
        fos.close();

        OutputStream nos = httpContext.getUnderlySocket().getOutputStream();
        PrintWriter writer = new PrintWriter(nos);
        writer.println("HTTP/1.1 200 OK");
        writer.println();
        //writer.println("from upload image handler");
        onImageLoaded(tmpPath);
        writer.flush();
    }
    protected void onImageLoaded(String path){

    }
}
