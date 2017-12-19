package com.example.webmail;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import static com.example.webmail.DecryptFragment.convertHexToString;
import static com.example.webmail.DecryptFragment.decrypt;
import static com.example.webmail.DecryptFragment.byteArrayToHexString;
import static com.example.webmail.DecryptFragment.hexStringToByteArray;


public class BodyActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_body);

		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A4C639")));

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new BodyFragment()).commit();
		}
	}

    public static String[] Load(File file)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String test;
        int anzahl=0;
        try
        {
            while ((test=br.readLine()) != null)
            {
                anzahl++;
            }
        }
        catch (IOException e) {e.printStackTrace();}

        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try
        {
            while((line=br.readLine())!=null)
            {
                array[i] = line;
                i++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return array;
    }


//    public static  String ReadFile( Context context){
//        String line = null, pk = null;
//
//        try {
//            FileInputStream fileInputStream = new FileInputStream (new File(Environment.getExternalStorageDirectory() +File.separator + "aja.txt"));
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            StringBuilder stringBuilder = new StringBuilder();
////            BufferedReader dir = new BufferedReader(new InputStreamReader(new FileInputStream(new File(Environment.getExternalStorageDirectory() +File.separator + "aja.txt"))));
//            File dir = new File(Environment.getExternalStorageDirectory() +File.separator + "aja.txt");
//            dir.
//            while ( (line = bufferedReader.readLine()) != null )
//            {
//                stringBuilder.append(line + System.getProperty("line.separator"));
//            }
//            fileInputStream.close();
//            line = stringBuilder.toString();
//
//            bufferedReader.close();
//        }catch (NoSuchAlgorithmException e) {
//            Log.d("e", "NoSuchAlgorithmException");
//        } catch (NoSuchProviderException e) {
//            Log.d("e", "NoSuchProviderException");
//        } catch (InvalidKeySpecException e) {
//            Log.d("e", "InvalidKeySpecException" + pk);
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//        return line;
//    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.body, menu);
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
	public static class BodyFragment extends Fragment implements View.OnClickListener {
		Button login;
		TextView body_view, body_finish;
		EditText passworddecrypt;

		static Context context;
		static int staticPosition;
		static String staticFolder;
		static MessageParcel staticMessageParcel;

		static String staticBody;

		public BodyFragment() {
			setHasOptionsMenu(true);
			context = getActivity();
		}
		@Override
//		public void onClick(View view) {
//			switch (view.getId()) {
//				case R.id.buttondecrypt:
//					String edmsgs = null;
////					String a = null;
//					String c = null;
//						try {
////						edpks = edpk.getText().toString();
//						edmsgs = body_view.getText().toString();
////						a = passworddecrypt.getText().toString();
////							String b = a.toString();
////							c = passworddecrypt.getText().toString();
//
//						//Log.d("prkey",edpks);
//						//Log.d("msg",edmsgs);
////						byte [] tes = hexStringToByteArray(c);
//						byte[] encrypted = hexStringToByteArray(edmsgs);
//						byte[] encryptedData = decrypt(hexStringToByteArray(passworddecrypt.getText().toString() , encrypted));
////					byte [] encryptedData = decrypt(   tes, encrypted);
//							String msg = byteArrayToHexString(encryptedData);
//						//Log.d("msg",edmsgs);
////						body_finish.setText(convertHexToString(msg));
//						body_finish.setText(msg);
//						//Toast.makeText(getActivity(), "msg", Toast.LENGTH_LONG).show();
//					} catch (NoSuchAlgorithmException e) {
//						Log.d("e", "NoSuchAlgorithmException");
//					} catch (NoSuchProviderException e) {
//						Log.d("e", "NoSuchProviderException");
//					} catch (InvalidKeySpecException e) {
//						Log.d("e", "InvalidKeySpecException");
//					}
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.buttondecrypt:
					final EditText edpk, edmsg;
					String edmsgs = null;
					String krip = null;
					try {
//						edpks = edpk.getText().toString();
						edmsgs = body_view.getText().toString();
						edpk = (EditText) view.findViewById(R.id.passworddecrypt);

						//Log.d("prkey",edpks);
						//Log.d("msg",edmsgs);
						byte[] passkrip = hexStringToByteArray(passworddecrypt.getText().toString());
						byte[] encrypted = hexStringToByteArray(edmsgs);
						byte[] encryptedData = decrypt(hexStringToByteArray(passworddecrypt.getText().toString()), encrypted);
						String msg = byteArrayToHexString(encryptedData);
						//Log.d("msg",edmsgs);
						body_finish.setText(convertHexToString(msg));
						//Toast.makeText(getActivity(), "msg", Toast.LENGTH_LONG).show();
					} catch (NoSuchAlgorithmException e) {
						Log.d("e", "NoSuchAlgorithmException");
					} catch (NoSuchProviderException e) {
						Log.d("e", "NoSuchProviderException");
					} catch (InvalidKeySpecException e) {
							Log.d("e", "InvalidKeySpecException");
					}
					catch (Exception e) {
						Toast.makeText(getActivity(), "Password Salah", Toast.LENGTH_LONG).show();
					}
					}
			}

//
//	String password = passworddecrypt.getText().toString();
//	String body = body_view.getText().toString();
//	byte[] encrypted = DecryptFragment.hexStringToByteArray(password);
//	byte[] encryptedData = DecryptFragment.decrypt(hexStringToByteArray(passworddecrypt.getText().toString()), body_view.getText().toString().getBytes());
//                        String msg = byteArrayToHexString(encryptedData);
//                        //Log.d("msg",edmsgs);
//                        body_finish.setText(convertHexToString(msg));
//} catch (Exception io) {
//	io.printStackTrace();
//	Toast.makeText(getActivity(), io.getMessage(),
//			Toast.LENGTH_LONG).show();;

////
////	byte[] decode = dec.decryptBytes(password.getBytes(), body.getBytes());
//	String decrypt = Crypter.decrypt(password,body);
////	body_finish.setText(decrypt);
//	byte[] encrypted = hexStringToByteArray(password);
//	byte[] encryptedData = DecryptFragment.decrypt(hexStringToByteArray(body), encrypted);
//	String msg = byteArrayToHexString(encryptedData);
//	//Log.d("msg",edmsgs);
//	body_finish.setText(convertHexToString(msg));  CipherExample dec = new CipherExample(password);
////	byte[] decodedData = RSAUtilss.decryptByPrivateKey(body.getBytes(), password);
////	String target = new String(decodedData);

//}catch (Exception io){
////	io.printStackTrace();
////	Toast.makeText(getActivity(), io.getMessage(),
////			Toast.LENGTH_LONG).show();
//}


		@Override
		public void onStart(){
			super.onStart();
			context = getActivity();
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
			inflater.inflate(R.menu.body_menu, menu);

//			MenuItem shareMenu = menu.findItem(R.id.action_share);
//
//			ShareActionProvider provider = (ShareActionProvider)MenuItemCompat.getActionProvider(shareMenu);
//			if(provider != null){
//				provider.setShareIntent(shareIntent());
//			}
//			else{
//				Log.d("Error", "Share Action Provider is null.");
//			}
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item){
			int id = item.getItemId();
			if (id == R.id.action_delete) {
				AlertDialog ConfirmDel =new AlertDialog.Builder(getActivity()) 
				//set message, title, and icon
				.setTitle("Delete") 
				.setMessage("Are you sure?")
				.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) { 
						//your deleting code
						Intent intent = new Intent(getActivity(), DeleteMail.class);
						intent.putExtra("MY_INDEX", staticPosition);
						intent.putExtra("FOLDER", staticFolder);
						intent.putExtra("messageParcel", (Parcelable)staticMessageParcel);
						getActivity().startService(intent);

						Intent goBack = new Intent(getActivity(), MainActivity.class);
						startActivity(goBack);

						dialog.dismiss();
					}   

				})



				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();

					}
				})
				.show();

				return true;

			}
			if (id == R.id.action_forward) {
				Intent intent = new Intent(getActivity(), ComposeActivity.class);
				TextView subject = (TextView) getActivity().findViewById(R.id.subject_view);
				intent.putExtra("SUBJECT", "[Fwd: " + subject.getText() + " ]");
				TextView body = (TextView) getActivity().findViewById(R.id.body_view);
				intent.putExtra("BODY", body.getText().toString());
				intent.putExtra("flags", "1");
				startActivity(intent);
				return true;
			}
			if (id == R.id.action_reply) {
				Log.e("REPLY", "TRUE");
				Intent intent = new Intent(getActivity(), ComposeActivity.class);
				TextView from = (TextView) getActivity().findViewById(R.id.from_view);
				TextView subject = (TextView) getActivity().findViewById(R.id.subject_view);
				intent.putExtra("SUBJECT", "[Re: " + subject.getText() + " ]");
				Log.e("FROM", from.getText().toString());
				intent.putExtra("FROM", from.getText().toString());
                intent.putExtra("flags", "2");
				startActivity(intent);
				return true;
			}
//			if(id == R.id.action_share){
//				Intent share = shareIntent();
//				startActivity(share);
//				return true;
//			}
			return super.onOptionsItemSelected(item);
		}

//		private Intent shareIntent(){
//			Intent share = new Intent(Intent.ACTION_SEND);
//			share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//			share.setType("text/plain");
//			share.putExtra(Intent.EXTRA_TEXT, staticBody+"\n\n#Webmail App");
//			return share;
//		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_body, container,
					false);
			login = (Button) rootView.findViewById(R.id.buttondecrypt);
			body_view = (TextView) rootView.findViewById(R.id.body_view);
			body_finish = (TextView) rootView.findViewById(R.id.body_finish);
			passworddecrypt = (EditText) rootView.findViewById(R.id.passworddecrypt);
			login.setOnClickListener(this);
			Intent intent = getActivity().getIntent();
			final int msg_index = intent.getIntExtra("MY_INDEX", -1);
			staticPosition = msg_index;
			final String folder_name = intent.getStringExtra("FOLDER");
			staticFolder = folder_name;
			Log.e("index", String.valueOf(msg_index));
			MessageParcel msg = intent.getParcelableExtra("messageParcel");
			staticMessageParcel = msg;


			TextView from = (TextView)rootView.findViewById(R.id.from_view);
			TextView to = (TextView)rootView.findViewById(R.id.to_view);
			TextView date = (TextView)rootView.findViewById(R.id.date_view);
			TextView subject = (TextView)rootView.findViewById(R.id.subject_view);
			TextView body = (TextView)rootView.findViewById(R.id.body_view);
			TextView cobload = (TextView) rootView.findViewById(R.id.passworddecrypt);
			String string = new String(cobload.toString()).toString();


			from.setText(msg.getFrom());

			String to_text = msg.getTo();
			int ind = to_text.indexOf(","); 
			if(ind != -1){
				to_text = to_text.substring(0, ind) + "\n" + to_text.substring(ind);
			}
			to.setText(to_text);

			subject.setText(msg.getSub());
			body.setText(msg.getBody());
			staticBody = msg.getBody();

			date.setText(msg.getDate());

			String cc = msg.getCc();
			String bcc = msg.getBcc();
			if(cc != null){
				TextView cc2 = (TextView)rootView.findViewById(R.id.cc_view);
				cc2.setText(cc);
			}
			if(bcc != null){
				TextView bcc2 = (TextView)rootView.findViewById(R.id.bcc_view);
				bcc2.setText(bcc);
			}


			ArrayList<String> attachments = msg.getAttachmentNames();
			ArrayList<String> sizes = msg.getAttachmentSizes();
			if(attachments.size() != 0){
				LinearLayout attachmentLayout = (LinearLayout) rootView.findViewById(R.id.attachments);

				Log.e("NO.OF ATTACHMENTS", String.valueOf(attachments.size()));
				for(int i=0;i<attachments.size();i++){
					ImageView image = new ImageView(getActivity());
					image.setLayoutParams(new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.MATCH_PARENT));
					image.setImageResource(R.drawable.ic_action_attachment);
					image.setBackgroundColor(Color.GRAY);
					image.setPadding(20, 20, 20, 20);

					final ImageView download = new ImageView(getActivity());
					download.setLayoutParams(new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.MATCH_PARENT));
					download.setImageResource(R.drawable.ic_action_download);
					download.setBackgroundColor(Color.GRAY);
					download.setPadding(20, 20, 20, 20);

					TextView attachment_name = new TextView(getActivity());
					attachment_name.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.FILL_PARENT,
							0, 1.0f));
					attachment_name.setPadding(10, 0, 10, 0);
					attachment_name.setSingleLine();
					attachment_name.setGravity(Gravity.CENTER);


					TextView attachment_size = new TextView(getActivity());
					attachment_size.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.FILL_PARENT,
							0, 1.0f));
					attachment_size.setPadding(10, 0, 10, 0);
					attachment_size.setSingleLine();
					attachment_size.setGravity(Gravity.CENTER);

					final int val = i;
					final String my_name = attachment_name.getText().toString();
					download.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent download_service = new Intent(getActivity(), DownloadAttachment.class);
							download_service.putExtra("MY_INDEX", String.valueOf(msg_index + 1));
							download_service.putExtra("ATTACHMENT_INDEX", String.valueOf(val));
							download_service.putExtra("ATTACHMENT_NAME", my_name);
							download_service.putExtra("FOLDER", folder_name);
							download_service.putExtra("password",passworddecrypt.getText().toString());
							getActivity().startService(download_service);
						}
					});

					String name = attachments.get(i);
					String size = sizes.get(i);
					double s = Double.parseDouble(size);
					int c = 0;
					while(s > 1024){
						s /= 1024;
						c++;
					}
					s = Math.round(s * 100.0) / 100.0;
					if(c == 0)
						size = s + " bytes";
					if(c == 1)
						size = s + " KB ";
					if(c == 2)
						size = s + " MB ";
					attachment_name.setText(name);
					attachment_size.setText(size);

					LinearLayout innerLayout = new LinearLayout(getActivity());
					innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
							0,
							ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
					innerLayout.setOrientation(LinearLayout.VERTICAL);
					innerLayout.addView(attachment_name);
					innerLayout.addView(attachment_size);

					LinearLayout layout = new LinearLayout(getActivity());
					layout.setOrientation(LinearLayout.HORIZONTAL);
					layout.setBackgroundColor(Color.LTGRAY);
					layout.addView(image);
					layout.addView(innerLayout);
					layout.addView(download);

					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					layoutParams.setMargins(0, 5, 0, 5);
					attachmentLayout.addView(layout, i, layoutParams);
				}
			}
//			try {
//				File file = new File (Environment.getExternalStorageDirectory() +File.separator + "aja.txt");
//				String [] loadText = Load(file);
//
//				String finalString = "";
//
//
//				for (int i = 0; i < loadText.length; i++)
//				{
//					finalString += loadText[i] + System.getProperty("line.separator");
//				}
//
//
//				cobload.setText(finalString.toString());
////				String textkrip = finalString.toString();
////
////				int ckr = textkrip.indexOf(",");
////				if(ckr != -1){
////					textkrip = textkrip.substring(0, ind) + "\n" + textkrip.substring(ind);
////				}
////				cobload.setText(textkrip);
//
//				Toast.makeText(getActivity(), "tes", Toast.LENGTH_LONG).show();
//			}   catch (Exception e) {
//				Log.d("e", "Exception");
//				Toast.makeText(getActivity(), "msg", Toast.LENGTH_LONG).show();
//			}    //   // deklarasi upload
			try {
				String myData ="";
				File file = new File (Environment.getExternalStorageDirectory() +File.separator + "aja.txt");
				FileInputStream fis = new FileInputStream(file);
				DataInputStream in = new DataInputStream(fis);
				BufferedReader br =
						new BufferedReader(new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					myData = myData + strLine;
				}
				in.close();
				cobload.setText(myData);
			} catch (IOException e) {
				e.printStackTrace();
			}



			return rootView;
		}

	}

}
