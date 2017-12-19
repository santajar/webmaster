package com.example.webmail;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;

import static android.content.Context.MODE_PRIVATE;
import static com.example.webmail.BodyActivity.BodyFragment.context;
import static java.security.AccessController.getContext;


public class KeyFragment extends Fragment {
    //GenerateKeyPair gkp;
    private String filename = "aja.txt";
    private String filepath = "MyFileStorage";
    File myExternalFile;


    String myData = "";
    TextView tespr;

    Context mContext;

    View vi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (vi == null) {
            vi = inflater.inflate(R.layout.key_layout, container, false);
        }
        return vi;
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }*/
    @Override
    @Nullable
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        Button gk, cp, cpr, sv;
        final TextView tvp, tvpr;

        this.mContext = mContext;
        //View view = inflater.inflate(R.layout.key_layout, container, false);
        View v = getView();
        gk = (Button) v.findViewById(R.id.button);
        cp = (Button) v.findViewById(R.id.button_pk);
        cpr = (Button) v.findViewById(R.id.button_prk);
        sv = (Button) v.findViewById(R.id.button_sv);
        tvp = (TextView) v.findViewById(R.id.textView_p);
        tvpr = (TextView) v.findViewById(R.id.textView_pr);
       final String es = ((TextView) getActivity().findViewById(R.id.textView_pr)).getText().toString();

        gk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                KeyPair gkp;
                String pk = null, prk = null;

                try {
                    // gkp = generateKeyPair();
                    KeyPair c;
                    c = generateKeyPair();
                    pk = getPublicKey(c);
                    prk = getPrivateKey(c);
                    byte [] bytes = es.getBytes();
                    File dir = new File(Environment.getExternalStorageDirectory() +File.separator + filename);
                    System.out.print(filename + dir.getAbsolutePath());
                    dir.createNewFile();
                    writeToFile(dir.getAbsolutePath(), getPrivateKey(c).getBytes());
//                    c.writeToFile("KeyPair/publicKey", KeyFragment.getPublicKey().getEncoded());
//                    c.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
                    // Log.d("e","inside button pressed"+pk);
                } catch (NoSuchAlgorithmException e) {
                    Log.d("e", "NoSuchAlgorithmException");
                } catch (NoSuchProviderException e) {
                    Log.d("e", "NoSuchProviderException");
                } catch (InvalidKeySpecException e) {
                    Log.d("e", "InvalidKeySpecException" + pk);
                } catch (IOException e){
                    e.printStackTrace();
                }

                //Log.d("msg"," outside button pressed"+pk);
                tvp.setText(pk);
                tvpr.setText(prk);

            }
        });

        cp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tvp.getText().toString().length() != 0) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", tvp.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), "Public key copied!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "No Msg to copy!", Toast.LENGTH_LONG).show();
                }
            }
        });

        cpr.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (tvpr.getText().toString().length() != 0) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", tvpr.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), "Private key copied!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "No Msg to copy!", Toast.LENGTH_LONG).show();
                }
            }
        });


        sv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                try {
//                    File dir = new File(Environment.getExternalStorageDirectory()+ File.separator + filename);
//                    System.out.print("es" + dir.getAbsolutePath());
//                    dir.createNewFile();
//                }
//                KeyPair c;
//                byte [] bytes = es.getBytes();
//            writeToFile(filepath, es.);
//                if (tvpr.getText().toString().length() != 0) {
////                    FileOutputStream fos = Context.openFileOutput(filename,MODE_PRIVATE);
////                    fos.write(es.getBytes());
////                    fos.close();
//                    File path = context.getExternalFilesDir(null);
//                    File file = new File(Environment.getDataDirectory() + "/WebmailData/" + part.getFileName());
//                    try {
//                        FileOutputStream fos = new FileOutputStream(myExternalFile);
//                        fos.write(es.getBytes());
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        stream.write("text-to-write".getBytes());
//                    } finally {
//                        stream.close();
//                    }
//                    Toast.makeText(getActivity(), "Private key copied!", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getActivity(), "No Msg to copy!", Toast.LENGTH_LONG).show();
            }
        });
}



    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static final String ALGORITHM = "RSA";
    KeyPair generateKeyPair;

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = null;
        KeyPair generateKeyPair = null;
        keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");//("SHA1PRNG" ,"SUN");
        //SecureRandom.getInstance("SHA1PRNG");
        keyGen.initialize(1024, random);// 512 is keysize
        generateKeyPair = keyGen.generateKeyPair();
        return generateKeyPair;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10)
                buffer.append("0");
            buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buffer.toString();
    }

    public String getPublicKey(KeyPair x) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {

        byte[] publicKey = x.getPublic().getEncoded();
        String pb4 = byteArrayToHexString(publicKey);
        return pb4;
    }

    public String getPrivateKey(KeyPair y) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        //KeyPair generateKeyPair = generateKeyPair();
        byte[] privateKey = y.getPrivate().getEncoded();
        String pr4 = byteArrayToHexString(privateKey);
        return pr4;
    }
    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

//    public static void main(String[] args) {
//        KeyFragment gk;
//        try {
//            File dir = new File(Environment.getExternalStorageDirectory() +File.separator + filename);
//            gk.writeToFile("KeyPair/publicKey", gk.getPublicKey().getEncoded());
//            gk.writeToFile("KeyPair/privateKey", gk.getPrivateKey().getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            System.err.println(e.getMessage());
//        }catch (NoSuchProviderException e) {
//            System.err.println(e.getMessage());
//        }catch (IOException e) {
//            System.err.println(e.getMessage());
//        }
//
//    }
}
