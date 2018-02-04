import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.firebase.FirebaseOptions;

import org.eclipse.swt.widgets.ProgressBar;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.FileNotFoundException;
import java.io.IOException;


public class LoadingActivity {

	protected static Shell shell;
	static String MacAddrress;
	static ProgressBar progressBar;

	static DatabaseReference mDatabase;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MacAddrress=getMacAddress();
			FirebaseInitialize();			
			LoadingActivity window = new LoadingActivity();
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
		shell.setSize(700, 313);
		shell.setText("Number Scraper Pk");


		Label lblNumberScraper = new Label(shell, SWT.NONE);
		lblNumberScraper.setFont(SWTResourceManager.getFont("Franklin Gothic Demi", 16, SWT.BOLD));
		lblNumberScraper.setBounds(247, 27, 168, 36);

		lblNumberScraper.setText("Number Scraper");
		progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);


		progressBar.setBounds(86, 162, 523, 50);

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		lblNewLabel.setBounds(275, 108, 121, 36);
		lblNewLabel.setText("Starting up..");

		checkAccount();
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				updateProgressBar();

			}
		});


	}
	private static void updateProgressBar()
	{
		new Thread(new Runnable()
		{
			private int                 progress    = 0;
			private static final int    INCREMENT   = 8;

			@Override
			public void run()
			{
				while (!progressBar.isDisposed())
				{
					Display.getDefault().asyncExec(new Runnable()
					{
						@Override
						public void run()
						{
							if (!progressBar.isDisposed())
								progressBar.setSelection((progress += INCREMENT) % (progressBar.getMaximum() + INCREMENT));


						}
					});

					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
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

	public static void checkAccount() {

		mDatabase=FirebaseDatabase.getInstance().getReference().child("macs");
		mDatabase.child(""+MacAddrress).addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				// TODO Auto-generated method stub
				if(snapshot.getValue()!=null) {
//					System.out.println("its not null\n"+snapshot);
					UserDetails user=snapshot.getValue(UserDetails.class);
					if(user.getActive().equals("yes")) {
//						System.out.println("yes");
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								shell.dispose();

								MainActivity main=new MainActivity();
								main.open();
								shell.close();
							}
						});
					}else {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								shell.dispose();
								
								ActivateAccount activate=new ActivateAccount();
								activate.open();
							
							}
						});
					}

				}else {
//					System.out.println("its null\n"+snapshot);

					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							shell.dispose();

							Register register=new Register();
							register.open();
						}
					});
					//					
				}

			}

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});
	}


}
