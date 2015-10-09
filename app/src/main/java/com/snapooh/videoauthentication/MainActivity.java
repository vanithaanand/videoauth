package com.snapooh.videoauthentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    static String IV = "AAAAAAAAAAAAAAAA";

    static String encryptionKey;
    private Uri videoUri;


    private byte[] cipher;


    private byte[] bytes;

    public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static byte[] encrypt(byte[] file, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(file);
    }

    public static byte[] decrypt(byte[] cipherText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(cipherText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private long getFileSize(String path) {
        return new File(path).length();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Intent i=new Intent(this,VideoPreview.class);
        i.putExtra("videoUri",String.valueOf(intent.getData()));
        Toast.makeText(this,String.valueOf(intent.getData()),Toast.LENGTH_SHORT).show();
        startActivity(i);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void record(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }
}

    /* public void encrypt(View view) {

        try {




           bytes = new byte[(int)getFileSize(path)];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(new File(path)));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (Exception e) {

                e.printStackTrace();
            }
            cipher = encrypt(bytes, encryptionKey);
            Log.i(TAG, "cipher len: " + cipher.length);


        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {

            Log.i(TAG, Uri.parse(path).getPath());
            path = path.substring(0, path.lastIndexOf(File.separator));
            Log.i(TAG, path);


            FileInputStream fis = new FileInputStream(new File(getRealPathFromURI(this, videoUri)));

            File encFile = new File(path + "/encVideo.mp4");
            int read;
            if (!encFile.exists())
                Log.i(TAG,""+encFile.createNewFile());

            File decfile = new File(path + "/decVideo.mp4");
            if (!decfile.exists())
                Log.i(TAG, "" + decfile.createNewFile());*/

           /* FileOutputStream fos = new FileOutputStream(outfile);
            FileInputStream encfis = new FileInputStream(outfile);
            FileOutputStream decfos = new FileOutputStream(decfile);


            Cipher encipher = Cipher.getInstance("AES");
            Cipher decipher = Cipher.getInstance("AES");
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //byte key[] = {0x00,0x32,0x22,0x11,0x00,0x00,0x00,0x00,0x00,0x23,0x00,0x00,0x00,0x00,
            // 0x00,0x00,0x00};
            SecretKey skey = kgen.generateKey();
            //Lgo
            encipher.init(Cipher.ENCRYPT_MODE, skey);
            CipherInputStream cis = new CipherInputStream(fis, encipher);
            decipher.init(Cipher.DECRYPT_MODE, skey);
            CipherOutputStream cos = new CipherOutputStream(decfos, decipher);
            while ((read = cis.read()) != -1) {
                fos.write((char) read);
                fos.flush();
            }
            fos.close();
            while ((read = encfis.read()) != -1) {
                cos.write(read);
                cos.flush();
            }
            cos.close();*/
       /* }catch (Exception ex){
            ex.printStackTrace();
        }*/


            /*public void decrypt (View view){
                String decrypted = null;
                try {
                    byte[] dec = decrypt(cipher, encryptionKey);
                    System.out.println("decrypt: " + decrypted);
                    if (Arrays.equals(dec, bytes)) {
                        Toast.makeText(this, "equal", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }*/








