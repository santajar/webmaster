package com.example.webmail;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.RSA.CipherExample;
import com.example.RSA.SimpleCrypto;
import net.superencryption.Decrypter;
import net.superencryption.Encrypter;
import net.superencryption.model.EncryptMeta;
public class DownloadAttachment extends IntentService{

	static String imap_address;
	static String imap_port;
	static String username;
	static String password;
	static String server;
	static String protocol;
	static String folder;

	static Handler mHandler;
	private static Encrypter encrypter=new Encrypter();
	private static Decrypter decrypter=new Decrypter();
	public DownloadAttachment() {
		super("DownloadAttachment");
		mHandler = new Handler();
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		mHandler.post(new Runnable() {            
			@Override
			public void run() {
				Toast.makeText(DownloadAttachment.this, "Fetching Attachment", Toast.LENGTH_SHORT).show();                
			}
		});

		int index = Integer.parseInt(intent.getStringExtra("MY_INDEX"));
		int attachment_index = Integer.parseInt(intent.getStringExtra("ATTACHMENT_INDEX"));
		String attachment_name = intent.getStringExtra("ATTACHMENT_NAME");
		String passwords = intent.getStringExtra("password");


		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		server = prefs.getString(getString(R.string.pref_server_settings_key), getString(R.string.pref_server_settings_default));
		if(server.equalsIgnoreCase("sudatamakmur")){
			imap_address = "mail.sudatamakmur.com";
			imap_port = "143";
			protocol = "imap";
			String extra = intent.getStringExtra("FOLDER");
			if(extra.compareTo("Sent")==0)
				extra = "Sent Mail";
			folder = "INBOX.SENT";
			if(extra.compareTo("Inbox") == 0)
				folder = "INBOX";
		}
		else{
			imap_address = "mail.sudatamakmur.com";
			imap_port = "143";
			protocol = "imap";
			String extra = intent.getStringExtra("FOLDER");
			folder = "INBOX."+extra;
			if(extra.compareTo("Inbox") == 0)
				folder = "Inbox";
		}
		username = prefs.getString(getString(R.string.pref_username_key), getString(R.string.pref_username_default));
		password = prefs.getString(getString(R.string.pref_password_key), getString(R.string.pref_password_default));

		String toastMessage = "initial";
		Log.e("Attachment Index", String.valueOf(attachment_index));
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", protocol);
		try{
			Session session = Session.getInstance(props, null);
			Store store = session.getStore(protocol);
			if(imap_port.compareTo("0") == 0)
				store.connect(imap_address, username, password);
			else
				store.connect(imap_address, Integer.parseInt(imap_port), username, password);
			Log.e("Message", "Connected");

			Folder inbox = store.getFolder(folder);
			inbox.open(Folder.READ_ONLY);
			index = inbox.getMessageCount() + 1 - index;
			Log.e("index", String.valueOf(index));
			Message msg = inbox.getMessage(index);
			Log.e("SUBJECT", msg.getSubject());
			Object content = msg.getContent();

			int chk_index = -1;
			if(content instanceof Multipart){
				Multipart mp = (Multipart) content;
				for(int i=0; i<mp.getCount();i++){
					Part part = mp.getBodyPart(i);
					if(part.getFileName() != null || part.getFileName() != ""){
						//Log.e("filename", part.getFileName());
						//Log.e("disposition", part.getDisposition());
						if(part.getDisposition() != null && part.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)){
							chk_index ++;
							if(chk_index != attachment_index){
								continue;
							}
							MimeBodyPart mbp = (MimeBodyPart)part;
							Log.e("Attachment Found", "true");

							mHandler.post(new Runnable() {            
								@Override
								public void run() {
									Toast.makeText(DownloadAttachment.this, "Download Started", Toast.LENGTH_SHORT).show();                
								}
							});
							Log.e("PASSSSWOOOOOORRRRD",passwords);
							Log.e("PASSSSWOOOOOORRRRD AJA",password);
							// Saving file
							if(Environment.getExternalStorageState() == null){
								File dir = new File(Environment.getDataDirectory() + "/WebmailData/");
								Log.e("DIR", Environment.getDataDirectory() + "/WebmailData/");
								if(dir.exists()){
									File file = new File(Environment.getDataDirectory() + "/WebmailData/" + part.getFileName());

									mbp.saveFile(file);
									String fileName=decrypter.docDecrypt(Environment.getExternalStorageDirectory() + "/WebmailData/" + part.getFileName(), Environment.getExternalStorageDirectory() + "/WebmailData/", passwords);
									file.delete();


								}
								else{
									boolean make = dir.mkdir();
									if(make){
											File file = new File(Environment.getDataDirectory() + "/WebmailData/" + part.getFileName());
//										SimpleCrypto.decryptFile(file,file,passwords);
										mbp.saveFile(file);
										String fileName=decrypter.docDecrypt(Environment.getExternalStorageDirectory() + "/WebmailData/" + part.getFileName(), Environment.getExternalStorageDirectory() + "/WebmailData/", passwords);
										Log.e("FILENAMEOOOY ",fileName);
										file.delete();

									}
								}
							}
							else{
								File dir = new File(Environment.getExternalStorageDirectory() + "/WebmailData/");
								Log.e("DIR", Environment.getExternalStorageDirectory() + "/WebmailData/");
								if(dir.exists()){
									File file = new File(Environment.getExternalStorageDirectory() + "/WebmailData/" + part.getFileName());
//									SimpleCrypto.decryptFile(file,file,passwords);
										mbp.saveFile(file);
									String fileName=decrypter.docDecrypt(Environment.getExternalStorageDirectory() + "/WebmailData/" + part.getFileName(), Environment.getExternalStorageDirectory() + "/WebmailData/", passwords);
									Log.e("FILENAMEOOOY ",fileName);
									file.delete();

								}
								else{
									boolean make = dir.mkdir();
									if(make){
										File file = new File(Environment.getExternalStorageDirectory() + "/WebmailData/" + part.getFileName());
//										SimpleCrypto.decryptFile(file,file,passwords);
										mbp.saveFile(file);
										String fileName=decrypter.docDecrypt(Environment.getExternalStorageDirectory() + "/WebmailData/" + part.getFileName(), Environment.getExternalStorageDirectory() + "/WebmailData/", passwords);
										Log.e("FILENAMEOOOY ",fileName);
										file.delete();
									}
								}
							}
						}
					}
				}
			}
			store.close();
		}
		catch(MessagingException e){
			e.printStackTrace();
			toastMessage = e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			toastMessage = e.getMessage();
		}
		if(toastMessage == null){
			toastMessage = "Error in Downloading";
		}
		if(toastMessage.equals("initial")){
			toastMessage = "Download Complete";
		}
		final String message = toastMessage;
		mHandler.post(new Runnable() {            
			@Override
			public void run() {
				Toast.makeText(DownloadAttachment.this, message, Toast.LENGTH_SHORT).show();                
			}
		});
	}

}
