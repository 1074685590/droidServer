package person.ljd.droidserver;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private SimpleHttpServer shs;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        WebConfig wc = new WebConfig();
        wc.setPort(8088);
        wc.setMaxParallels(50);
        shs = new SimpleHttpServer(wc);
        shs.reginsterResourceHandler(new ResourceInAssetsHandler(this));
        shs.reginsterResourceHandler(new UploadImageHandler(){
            @Override
            protected void onImageLoaded(String path) {
                showImage(path);
            }
        });
        shs.startAsync();
    }
    private void showImage(final String path){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView ivImage = (ImageView)findViewById(R.id.ivImage);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                ivImage.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this,"image received ok",Toast.LENGTH_SHORT);
            }
        });

    }
    @Override
    protected  void onDestroy(){
        try {
            shs.stopAsync();
        } catch (Exception e) {
            Log.e("spy",e.toString());
        }
        super.onDestroy();
    }
}
