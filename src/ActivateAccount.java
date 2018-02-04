import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.tasks.OnSuccessListener;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;

public class ActivateAccount {

	protected Shell shlNumberScraper;
	private Text code;
	private Button activate;
	static DatabaseReference mDatabase;
	static String codeFromDb;
	private Label lblNewLabel;
	private Label lblConatctMali;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FirebaseInitialize();
			ActivateAccount window = new ActivateAccount();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlNumberScraper.open();
		shlNumberScraper.layout();
		 valueFromDb();
		while (!shlNumberScraper.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void valueFromDb() {
		mDatabase=FirebaseDatabase.getInstance().getReference().child("macs");
		
			try {
				mDatabase.child(""+getMacAddress()).addValueEventListener(new ValueEventListener() {

					@Override
					public void onDataChange(DataSnapshot snapshot) {
						// TODO Auto-generated method stub
						if(snapshot!=null) {

							UserDetails user=snapshot.getValue(UserDetails.class);
							if(user!=null) {
								codeFromDb=user.getCode();
//								System.out.println(""+codeFromDb);
							}
						}

					}

					@Override
					public void onCancelled(DatabaseError error) {
						// TODO Auto-generated method stub

					}
				});
			} catch (UnknownHostException | SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	}
	
	

	/**
	 * Create contents of the window.
	 */

	
	protected void createContents() {
		shlNumberScraper = new Shell();
		shlNumberScraper.setSize(450, 400);
		shlNumberScraper.setText("Number Scraper");

		code = new Text(shlNumberScraper, SWT.BORDER);
		code.setToolTipText("");
		code.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
		code.setBounds(105, 152, 219, 41);

		Label lblEnterActivationCode = new Label(shlNumberScraper, SWT.NONE);
		lblEnterActivationCode.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblEnterActivationCode.setBounds(105, 124, 170, 22);
		lblEnterActivationCode.setText("Enter Activation Code");

		Label lblActivation = new Label(shlNumberScraper, SWT.NONE);
		lblActivation.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.BOLD));
		lblActivation.setBounds(105, 10, 208, 30);
		lblActivation.setText("Activation required!");

		activate = new Button(shlNumberScraper, SWT.NONE);
		activate.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		activate.setBounds(146, 225, 127, 41);
		activate.setText("Activate");

		lblNewLabel = new Label(shlNumberScraper, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblNewLabel.setBounds(79, 307, 294, 30);
		lblNewLabel.setText("");
		
		lblConatctMali = new Label(shlNumberScraper, SWT.NONE);
		lblConatctMali.setBounds(128, 72, 245, 15);
		lblConatctMali.setText("Conatct M.Ali  |  03158000333");

		activate.addSelectionListener(new SelectionListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				String codeEntered=code.getText();
//				System.out.println(codeEntered);
				if(codeFromDb!=null) {
					if(codeFromDb.equals(codeEntered)) {
						try {
							mDatabase.child(""+getMacAddress()).child("active").setValue("yes") .addOnSuccessListener(new OnSuccessListener<Void>() {
								@Override
								public void onSuccess(Void aVoid) {
//									System.out.println("here");
									Display.getDefault().asyncExec(new Runnable() {
										public void run() {
											shlNumberScraper.dispose();
											MainActivity l=new MainActivity();
											l.open();
											//                    	 registerBtn.setVisible(false);
										}
									});

								}
							});



						} catch (UnknownHostException | SocketException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}else {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								lblNewLabel.setText("Wrong Code. Please try again");
								//                    	 registerBtn.setVisible(false);
							}
						});

//						System.out.println("Wrong code");
					}

				}
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
	public static String getMacAddress() throws UnknownHostException,
	SocketException
	{
		InetAddress ipAddress = InetAddress.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface
				.getByInetAddress(ipAddress);
		byte[] macAddressBytes = networkInterface.getHardwareAddress();
		StringBuilder macAddressBuilder = new StringBuilder();

		for (int macAddressByteIndex = 0; macAddressByteIndex < macAddressBytes.length; macAddressByteIndex++)
		{
			String macAddressHexByte = String.format("%02X",
					macAddressBytes[macAddressByteIndex]);
			macAddressBuilder.append(macAddressHexByte);

			if (macAddressByteIndex != macAddressBytes.length - 1)
			{
				macAddressBuilder.append("");
			}
		}
		//		System.out.println(macAddressBuilder.toString());
		return macAddressBuilder.toString();
	}

}
