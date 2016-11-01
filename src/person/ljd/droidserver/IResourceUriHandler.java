package person.ljd.droidserver;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/1.
 */
public interface IResourceUriHandler {
    boolean accept(String uri);

    void handle(String uri,HttpContext httpContext) throws IOException;
}
