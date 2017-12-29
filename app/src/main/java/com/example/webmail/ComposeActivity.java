package com.example.webmail;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import com.example.CryptoRSA.Base64Utils;
import com.example.CryptoRSA.RSAUtils;
import com.example.CryptoRSA.RSAUtilss;
import com.example.RSA.CipherExample;
import com.example.RSA.Crypter;
import com.example.RSA.SimpleCrypto;
import com.example.filechooser.FileUtils;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.preference.PreferenceManager;
import net.superencryption.Decrypter;
import net.superencryption.Encrypter;
import net.superencryption.model.EncryptMeta;

import static com.example.webmail.EncryptFragment.byteArrayToHexString;
import static com.example.webmail.EncryptFragment.hexStringToByteArray;
import android.view.View;


public class ComposeActivity extends ActionBarActivity {

	private static final String TAG = "FileChooserExampleActivity";
	private static final int REQUEST_CODE = 6384; // onActivityResult request
	private static CheckBox fee_checkbox;
	private static boolean ceked;
	// code
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		fee_checkbox = (CheckBox)findViewById(R.id.checkbox);
        fee_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (fee_checkbox.isChecked()) {
                    // your code to checked checkbox
                    ceked = true;
                } else {
                    // your code to  no checked checkbox
                    ceked = false;
                }
        }
    });
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A4C639")));

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new ComposeFragment()).commit();
		}



	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class ComposeFragment extends Fragment implements View.OnClickListener {
		Button login;
		View v = getView();
//		TextView ambil_enkrip;
		EditText compose_body,  compose_password;
		private static final String TAG = "FileChooserExampleActivity";
		static ArrayList<String> path = null;
		private static Encrypter encrypter=new Encrypter();
		private static Decrypter decrypter=new Decrypter();

		private static final int REQUEST_CODE = 6384; // onActivityResult request
		// code

		public ComposeFragment() {
			setHasOptionsMenu(true);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
			inflater.inflate(R.menu.compose_menu, menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item){
			int id = item.getItemId();
			if(id == R.id.action_send){
			    System.out.print(ceked);
				if (ceked == true){

					send_message_enc();

				}else{

					send_message();
				}

				return true;
			}
			if(id == R.id.action_attachment){
				// Upload code here
			   showChooser();
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
		private void showChooser() {
			// Use the GET_CONTENT intent from the utility class
			Intent target = FileUtils.createGetContentIntent();
			// Create the chooser Intent
			Intent intent = Intent.createChooser(
					target, getString(R.string.chooser_title));
			try {
				startActivityForResult(intent, REQUEST_CODE);
			} catch (ActivityNotFoundException e) {
				// The reason for the existence of aFileChooser
			}
		}

		private void showAttachments(String name){
			try {
				if(name != null){
					LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.attachlayout);

					LinearLayout attach_layout = new LinearLayout(getActivity());
					attach_layout.setOrientation(LinearLayout.HORIZONTAL);
					attach_layout.setPadding(0, 10, 0, 10);
					attach_layout.setBackgroundColor(Color.LTGRAY);

					TextView heading = new TextView(getActivity());
					heading.setLayoutParams(new LinearLayout.LayoutParams(
							0,
							ViewGroup.LayoutParams.MATCH_PARENT, 3.0f));
					heading.setGravity(Gravity.CENTER);
					heading.setText(name.substring(name.lastIndexOf("/")+1));

					ImageView delete = new ImageView(getActivity());
					delete.setLayoutParams(new LinearLayout.LayoutParams(
							0,
							ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
					delete.setImageResource(R.drawable.ic_action_remove);

					final String delname = name;
					final LinearLayout rem = attach_layout;
					delete.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Log.e("REMOVE", delname);
							path.remove(path.indexOf(delname));
							rem.setVisibility(View.GONE);
						}
					});

					attach_layout.addView(heading);
					attach_layout.addView(delete);

					layout.addView(attach_layout);
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			switch (requestCode) {
			case REQUEST_CODE:
				// If the file selection was successful
				if (resultCode == RESULT_OK) {
					if (data != null) {
						// Get the URI of the selected file
						Uri uri = data.getData();
						try {
							// Get the file path from the URI
							if(path == null)
								path = new ArrayList<String>();
							path.add(FileUtils.getPath(MainActivity.getAppContext(), uri));
							String passwords = compose_password.getText().toString();
							String pattern = getParentDirPath(path.get(path.size()-1));
							File ori = new File(path.get(path.size()-1));


							EncryptMeta metadata=encrypter.docEncrypt(path.get(path.size()-1), pattern, passwords);
							File dec = new File(pattern+"/"+metadata.getFileName());

							Log.e("PASSSWOOOOORRRRRRDDDD",passwords);
							Log.e("PATHHHHHHHHHHHHHHHh", getParentDirPath(path.get(path.size()-1)));
							Log.e("ASELI", data.getData().getEncodedPath());
							Log.e("ASELI1", uri.getLastPathSegment());
							Log.e("ASELI2", uri.getPathSegments().toString());
							path.clear();
							uri = Uri.fromFile(dec);
							path.add(FileUtils.getPath(MainActivity.getAppContext(), uri));
							showAttachments(path.get(path.size()-1));

						} catch (Exception e) {
//							Log.e("FileSelectorTestActivity",  e.getMessage());
							e.printStackTrace();
						}catch (Throwable to){
							to.printStackTrace();
						}
					}
				}
				break;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}

		public static String getParentDirPath(String fileOrDirPath) {
			boolean endsWithSlash = fileOrDirPath.endsWith(File.separator);
			return fileOrDirPath.substring(0, fileOrDirPath.lastIndexOf(File.separatorChar,
					endsWithSlash ? fileOrDirPath.length() - 2 : fileOrDirPath.length() - 1));
		}


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_compose,
					container, false);
//			login = (Button) rootView.findViewById(R.id.buttonencrypt);
//			ambil_enkrip = (TextView) rootView.findViewById(R.id.compose_encrypt);
			compose_body = (EditText) rootView.findViewById(R.id.compose_body);
			compose_password = (EditText) rootView.findViewById(R.id.compose_password);
//			login.setOnClickListener(this);
			Intent intent = getActivity().getIntent();
			if(intent != null){

				String sub = intent.getStringExtra("SUBJECT");
				if(sub != null)
					((EditText) rootView.findViewById(R.id.compose_subject)).setText(sub);
				String to = intent.getStringExtra("FROM");
				if(to != null)
					((EditText) rootView.findViewById(R.id.compose_to)).setText(to);
				String body = intent.getStringExtra("BODYENCRYPT");

				String body_message = intent.getStringExtra("BODY");
				if(body_message != null)
					((EditText) rootView.findViewById(R.id.compose_body)).setText(body_message);

				if (intent.getStringExtra("flags") != null)
				{
					String flags = intent.getStringExtra("flags");
					if (flags.equals("1"))
						getActivity().setTitle("Forward");
					if (flags.equals("2"))
						getActivity().setTitle("Reply");
					if (flags.equals("0"))
						getActivity().setTitle("Compose");
				}

			}

			return rootView;
		}

		@Override
		public void onClick(View view) {


//			switch (view.getId()) {
//				case R.id.buttonencrypt: {
//					try {
//							send_message_enc();
//
//						} catch (Exception e) {
//							Log.d("e", "Exception");
//						}
//					}
//				}
			}

		public String append(String x){
			if(x.equals(""))
				return x;
			String y = "";
			String vals[] = x.split(",");
			for(int i=0;i<vals.length;i++){
				if(vals[i].indexOf("@") == -1){
					vals[i] += "@iitp.ac.in";
				}
				y += vals[i];
				if(i < vals.length - 1)
					y += ",";
			}
			return y;
		}

		public void send_message(){
			String subject = ((EditText) getActivity().findViewById(R.id.compose_subject)).getText().toString();
			String to = ((EditText) getActivity().findViewById(R.id.compose_to)).getText().toString();
			String cc = ((EditText) getActivity().findViewById(R.id.compose_cc)).getText().toString();
			String bcc = ((EditText) getActivity().findViewById(R.id.compose_bcc)).getText().toString();
			String body = ((EditText) getActivity().findViewById(R.id.compose_body)).getText().toString();
//			String bodyencrypt = ((TextView) getActivity().findViewById(R.id.compose_encrypt)).getText().toString();

//			if(bodyencrypt == null){
//				bodyencrypt = body;
//			}
			
			if(cc == null){
				cc = "";
			}
			if(bcc == null){
				bcc = "";
			}
			
			to = append(to);
			cc = append(cc);
			bcc = append(bcc);
		

			if(to == null || to.equals("")){
				Toast.makeText(getActivity(), "To field cannot be empty.", Toast.LENGTH_SHORT).show();
			}
			else if(to.contains(" ") || cc.contains(" ") || bcc.contains(" ")){
				Toast.makeText(getActivity(), "TO/CC/BCC cannot contain spaces.", Toast.LENGTH_SHORT).show();
			}
			else{
				String vals[] = {subject, to, cc, bcc, body};
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
				String server = prefs.getString(getString(R.string.pref_server_settings_key), getString(R.string.pref_server_settings_default));
				if(server.equalsIgnoreCase("sudatamakmur")){
					Intent send = new Intent(getActivity(), SendMessage.class);
					send.putExtra("MESSAGE_DETAILS", vals);
					send.putExtra("ATTACHMENT_DETAILS", path);
					getActivity().startService(send);
				}
				else{
					Intent send = new Intent(getActivity(), SendMessage.class);
					send.putExtra("MESSAGE_DETAILS", vals);
					send.putExtra("ATTACHMENT_DETAILS", path);
					getActivity().startService(send);
				}
				path=null;
				LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.attachlayout);
				layout.removeAllViews();
			}
		}

		public void send_message_enc(){
			try {
				String subject = ((EditText) getActivity().findViewById(R.id.compose_subject)).getText().toString();
				String to = ((EditText) getActivity().findViewById(R.id.compose_to)).getText().toString();
				String cc = ((EditText) getActivity().findViewById(R.id.compose_cc)).getText().toString();
				String bcc = ((EditText) getActivity().findViewById(R.id.compose_bcc)).getText().toString();
//				String body = ((EditText) getActivity().findViewById(R.id.compose_body)).getText().toString();
				String bodyencrypt = "";
				compose_body.getText().toString();
				compose_password.getText().toString();
				byte[] encryptedData = EncryptFragment.encrypt(hexStringToByteArray(compose_password.getText().toString()), compose_body.getText().toString().getBytes());
				String emsg = byteArrayToHexString(encryptedData);
//				ambil_enkrip.setText(emsg);
				bodyencrypt = emsg;


				if(cc == null){
					cc = "";
				}
				if(bcc == null){
					bcc = "";
				}

				to = append(to);
				cc = append(cc);
				bcc = append(bcc);


				if(to == null || to.equals("")){
					Toast.makeText(getActivity(), "To field cannot be empty.", Toast.LENGTH_SHORT).show();
				}
				else if(to.contains(" ") || cc.contains(" ") || bcc.contains(" ")){
					Toast.makeText(getActivity(), "TO/CC/BCC cannot contain spaces.", Toast.LENGTH_SHORT).show();
				}
				else{
					String vals[] = {subject, to, cc, bcc, bodyencrypt};
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
					String server = prefs.getString(getString(R.string.pref_server_settings_key), getString(R.string.pref_server_settings_default));
					if(server.equalsIgnoreCase("sudatamakmur")){
						Intent send = new Intent(getActivity(), SendMessage.class);
						send.putExtra("MESSAGE_DETAILS", vals);
						send.putExtra("ATTACHMENT_DETAILS", path);
						getActivity().startService(send);
					}
					else{
						Intent send = new Intent(getActivity(), SendMessage.class);
						send.putExtra("MESSAGE_DETAILS", vals);
						send.putExtra("ATTACHMENT_DETAILS", path);
						getActivity().startService(send);
					}
					path=null;
					LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.attachlayout);
					layout.removeAllViews();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

}
