package by.mtz.reminder.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Roma on 04.10.2016.
 */

public class Utils {
    public final static String LOAD_All_DATA = "LOAD_ALL_DATA";
    public final static String LOAD_DATA = "LOAD_DATA";
    public final static String INTENT_SERVICE_INVOKE = "INTENT_SERVICE_INVOKE";
    public final static String READ_ACTIONS_DATA = "READ_ACTIONS_DATA";
    public final static String READ_PERSONS_DATA = "READ_PERSONS_DATA";
    public final static String IMG_DIR= "app_imageDir";
    public final static String IMG_DIR_NO_APP= "imageDir";
    public final static String IMG_PATH= "/data/data/by.mtz.reminder/app_imageDir/";
    public final static String NOTIFY_INTENT= "NOTIFY_INTENT";
    public final static String MANUAL_START_RECIEVER = "START";
    public final static String START_WAKEFULL_RECIEVER= "WAKEFUL";
    public final static String READ_ACTION_BY_ID= "READ_ACTION_BY_ID";
    public final static String READ_HOT_NEWS= "READ_HOW_NEWS";
    public final static int TASK_FINISHED= 1;
    public final static String RECEIVER= "RECEIVER";

    public static Bitmap decode64Bitmap(String base64String){
        //pureBase64Encoded is the string after removing 'data:image/jpeg;base64'
        String pureBase64Encoded = base64String.substring(base64String.indexOf(",")  + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static File savebitmap(Bitmap bmp, String locationFilePath) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(locationFilePath);

        if(!f.exists()) f.delete();

        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }
}
