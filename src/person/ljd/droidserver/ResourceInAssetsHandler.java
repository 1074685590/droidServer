package person.ljd.droidserver;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by Administrator on 2016/11/1.
 */
public class ResourceInAssetsHandler implements IResourceUriHandler{
    private String acceptPrefix = "/static/";
    private Context context;

    public ResourceInAssetsHandler(Context context) {
        this.context = context;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException{
        int startIndex = acceptPrefix.length();
        String assetsPath = uri.substring(startIndex);
        InputStream fis = context.getAssets().open(assetsPath);
        byte[] raw = StreamToolkit.readRawFormStream(fis);
        fis.close();
        OutputStream nos = httpContext.getUnderlySocket().getOutputStream();
        PrintStream printer = new PrintStream(nos);
        printer.println("HTTP/1.1 200 OK");
        printer.println("Content-Length:"+raw.length);
        if(assetsPath.endsWith(".html")){
            printer.println("Content-Type:text/html");
        }else if(assetsPath.endsWith(".html")){
            printer.println("Content-Type:text/html");
        }
        else if(assetsPath.endsWith(".js")){
            printer.println("Content-Type:text/js");
        }
        else if(assetsPath.endsWith(".css")){
            printer.println("Content-Type:text/css");
        }
        else if(assetsPath.endsWith(".png")){
            printer.println("Content-Type:text/png");
        }
        printer.println();
        printer.write(raw);
    }
}
