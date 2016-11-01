package person.ljd.droidserver;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/10/31.
 */
public class SimpleHttpServer {
    private final WebConfig webConfig;
    private final ExecutorService threadPool;
    private boolean isEnable;
    private ServerSocket socket;
    private Set<IResourceUriHandler> resourceUriHandlers;

    public SimpleHttpServer(WebConfig webConfig) {
        this.webConfig = webConfig;
        threadPool = Executors.newCachedThreadPool();
        resourceUriHandlers = new HashSet<IResourceUriHandler>();
    }

    public void startAsync(){
        isEnable =true;
        new Thread(new Runnable(){
            @Override
            public void run() {
                doProcSync();
            }
        }).start();
    }

    /**
     * 停止Server(异步)
     */
    public void stopAsync() throws  IOException{
        if(!isEnable){
            return;
        }
        isEnable = false;
        socket.close();
        socket=null;
    }
    private void doProcSync(){
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(webConfig.getPort());
            socket = new ServerSocket();
            socket.bind(socketAddress);
            while(isEnable){
                final Socket remotePeer = socket.accept();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("spy","a remote peer accepted..."+remotePeer.getRemoteSocketAddress().toString());
                        onAcceptRemotePeer(remotePeer);
                    }
                });
            }
        } catch (IOException e) {
            Log.e("spy",e.toString());
        }
    }
    public void reginsterResourceHandler(IResourceUriHandler handler){
        resourceUriHandlers.add(handler);
    }
    private void onAcceptRemotePeer(Socket remotePeer){
        try {
            HttpContext httpContext = new HttpContext();
            httpContext.setUnderlySocket(remotePeer);
            //remotePeer.getOutputStream().write("config,connected successful".getBytes());
            InputStream nis = remotePeer.getInputStream();
            String headline = null;
            String resourceUri = headline = StreamToolkit.readLine(nis).split(" ")[1];
            Log.d("spy",resourceUri);
            while((headline=StreamToolkit.readLine(nis)) != null){
                if(headline.equals("\r\n")){
                    break;
                }
                String[] pair = headline.split(": ");
                if(pair.length > 1){
                    httpContext.addRequestHeader(pair[0],pair[1]);
                }
                Log.d("spy","head line="+headline);
            }
            for(IResourceUriHandler handler:resourceUriHandlers){
                if(!handler.accept(resourceUri)){
                    continue;
                }
                handler.handle(resourceUri,httpContext);
            }
        } catch (IOException e) {
            Log.e("spy",e.toString());
        }
    }
}
