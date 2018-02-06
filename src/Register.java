import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.tasks.OnSuccessListener;

import org.eclipse.swt.widgets.Label;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class Register {

	protected Shell shell;
	
	private Text username_text;
	private Text phone_text;
	private Text password_text;
	private Text email_text;
	private Label lblUsername;
	private Label lblEmail;
	private Label lblPassword;
	private Label lblPhone;
	private DatabaseReference mDatabase;
	private static String macAddress;
	static Preferences preferences;
	static String userKey;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			FirebaseInitialize();
			Register window = new Register();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		preferences = Preferences.userNodeForPackage(LoadingActivity.class);
		 userKey=preferences.get("userKey", "default");
//		 System.out.println("from re"+userKey);
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(700, 650);
		shell.setText("Number Scraper Pk");

		Label lblNumberScraper = new Label(shell, SWT.NONE);
		lblNumberScraper.setFont(SWTResourceManager.getFont("Franklin Gothic Demi", 16, SWT.BOLD));
		lblNumberScraper.setBounds(260, 20, 168, 36);
		lblNumberScraper.setText("Register");
		
		username_text = new Text(shell, SWT.BORDER);
		username_text.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		username_text.setBounds(164, 82, 317, 43);
		

		email_text = new Text(shell, SWT.BORDER);
		email_text.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		email_text.setBounds(164, 185, 317, 43);

		password_text = new Text(shell, SWT.BORDER);
		password_text.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		password_text.setBounds(164, 279, 317, 43);


		phone_text = new Text(shell, SWT.BORDER);
		phone_text.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		phone_text.setBounds(164, 386, 317, 43);


	

		lblUsername = new Label(shell, SWT.NONE);
		lblUsername.setBounds(164, 62, 55, 15);
		lblUsername.setText("Username");

		lblEmail = new Label(shell, SWT.NONE);
		lblEmail.setBounds(164, 164, 55, 15);
		lblEmail.setText("Email");

		lblPassword = new Label(shell, SWT.NONE);
		lblPassword.setBounds(164, 258, 55, 15);
		lblPassword.setText("Password");

		lblPhone = new Label(shell, SWT.NONE);
		lblPhone.setBounds(164, 362, 55, 15);
		lblPhone.setText("Phone");

		Button registerBtn = new Button(shell, SWT.NONE);
		registerBtn.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		registerBtn.setBounds(260, 495, 148, 43);
		registerBtn.setText("Rgister");

		registerBtn.addSelectionListener(new SelectionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
				String username=username_text.getText();
				String email=email_text.getText();
				String password=password_text.getText();
				String phone=phone_text.getText();
				long time=System.currentTimeMillis();
				String code = Long.toHexString(Double.doubleToLongBits(Math.random()));
				mDatabase=FirebaseDatabase.getInstance().getReference().child("pcUser");

				
					mDatabase.child(""+userKey).setValue(new UserDetails(username,email,password,phone,"no",""+userKey,""+time,""+code)) .addOnSuccessListener(new OnSuccessListener<Void>() {
					    @Override
					    public void onSuccess(Void aVoid) {
//					    	System.out.println("here");
					    	Display.getDefault().asyncExec(new Runnable() {
					            public void run() {
					            	shell.dispose();
					            	MainActivity l=new MainActivity();
					            	l.open();
//                            	 registerBtn.setVisible(false);
					            }
					        });
					   
					    }
					});
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});




	}
	public static void FirebaseInitialize() {
		FileInputStream refreshToken;
		try {
			refreshToken = new FileInputStream("conf.json");

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(refreshToken))
					.setDatabaseUrl("https://numberscraper.firebaseio.com/")
					.build();

			FirebaseApp.initializeApp(options);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
