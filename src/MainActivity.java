import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.utilities.Validation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.swing.JFrame;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.ProgressBar;


public class MainActivity {

	protected static Shell shell;
	private Text url;
	private Text filename;
	private Label lblUrl;
	private Label lblFilename;
	private static Button Download;
	String fileN;
	FirebaseDatabase database;
	static DatabaseReference mDatabase ;
	private Text startpage;

	public static Button scrape ;
	private Text endpage;


	static int canScrape=0;
	long  start = 100;

	static ArrayList<String> macAddressList=new ArrayList<>();
	public static String message;

	public static Label lblNewLabel;
	private Label lblTime;
	int  countdown=100;
	
	static String linksFileName;
	static Preferences preferences;
	static String userKey;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			checkAccount();
			MainActivity window = new MainActivity();
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
//		 System.out.println("from main "+userKey);
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		getValueFromDb();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}


	public void startScraping() {
		//		System.out.println("inside scraping"  );

		String olxUrl=url.getText();
		fileN=filename.getText();
		int startPage=Integer.parseInt(startpage.getText());
		int endPage=Integer.parseInt(endpage.getText());


		Thread t=new Thread(new Runnable() {

			@Override
			public void run() {

				// TODO Auto-generated method stub
				for(int i=startPage;i<=endPage;i++) {
					URL url;
					try {
						url = new URL("http://getnumbers.co/app/ali.php");

						Map<String,Object> params = new LinkedHashMap<>();
						params.put("page", i);
						params.put("links", linksFileName+"-olx-java");
						params.put("filename", fileN+"-"+linksFileName+"-olx");
						params.put("url", olxUrl.toString());

						StringBuilder postData = new StringBuilder();
						for (Map.Entry<String,Object> param : params.entrySet()) {
							if (postData.length() != 0) postData.append('&');
							postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
							postData.append('=');
							postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
						}
						byte[] postDataBytes = postData.toString().getBytes("UTF-8");

						HttpURLConnection conn = (HttpURLConnection)url.openConnection();
						conn.setRequestMethod("POST");
						conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
						conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
						conn.setDoOutput(true);
						conn.getOutputStream().write(postDataBytes);


						Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
						//
						//				        for (int c; (c = in.read()) >= 0;)
						//				            System.out.print((char)c);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
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
		lblNumberScraper.setBounds(246, 10, 168, 36);

		lblNumberScraper.setText("Number Scraper");

		ScraperLayout();	

	}




	public void ScraperLayout() {
		url = new Text(shell, SWT.BORDER);
		url.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		url.setBounds(61, 85, 568, 43);



		filename = new Text(shell, SWT.BORDER);
		filename.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		filename.setBounds(191, 202, 317, 43);
		
		startpage = new Text(shell, SWT.BORDER);
		startpage.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		startpage.setBounds(191, 320, 94, 43);

		endpage = new Text(shell, SWT.BORDER);
		endpage.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		endpage.setBounds(416, 320, 92, 43);


		scrape= new Button(shell, SWT.NONE);
		scrape.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		scrape.setBounds(123, 424, 168, 48);
		scrape.setText("Scrape");


		scrape.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
//				System.out.println("can scrape here"+canScrape);
				//				shell.dispose();
				//				
				if(canScrape==1) {
//					System.out.println("Will work ");
					
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							//						            	scrape.setVisible(false);
							CountDownScreen count=new CountDownScreen();
							int end=Integer.parseInt(endpage.getText());
							int star=Integer.parseInt(startpage.getText());
							int formula=end-star+1;
							count.open(formula);


						}
					});
					startScraping();

				}
				else {
					
//					System.out.println("fuck off bye");
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							//						            	scrape.setVisible(false);
							shell.dispose();
							ActivateAccount activate=new ActivateAccount();
							activate.open();
							scrape.setVisible(false);
							Download.setVisible(false);


						}
					});



				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});





		lblUrl = new Label(shell, SWT.NONE);
		lblUrl.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblUrl.setBounds(61, 58, 62, 21);
		lblUrl.setText("URL");


		lblFilename = new Label(shell, SWT.NONE);
		lblFilename.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblFilename.setBounds(191, 171, 94, 25);
		lblFilename.setText("Filename");
		
		
	


		Download = new Button(shell, SWT.NONE);
		Download.setFont(SWTResourceManager.getFont("Segoe UI", 16, SWT.NORMAL));
		Download.addSelectionListener(new SelectionAdapter() {


			@Override
			public void widgetSelected(SelectionEvent e) {
				URL url;
				try {
					url = new URL("http://getnumbers.co/app/files/"+fileN+"-"+linksFileName+"-olx"+".txt");

					InputStream in = url.openStream();
					Files.copy(in, Paths.get(fileN+".txt"), StandardCopyOption.REPLACE_EXISTING);
					in.close();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		Download.setText("Download");
		Download.setBounds(416, 424, 168, 48);

	

		Label lblEndPage = new Label(shell, SWT.NONE);
		lblEndPage.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblEndPage.setBounds(416, 291, 69, 23);
		lblEndPage.setText("End page");

		Label lblStartPage = new Label(shell, SWT.NONE);
		lblStartPage.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblStartPage.setBounds(191, 291, 75, 25);
		lblStartPage.setText("Start page");

		lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setAlignment(SWT.CENTER);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe Print", 12, SWT.BOLD));
		lblNewLabel.setBounds(61, 549, 549, 52);
		lblNewLabel.setText("For issue and querries contact\nM Ali  |  0315800333");

		lblTime = new Label(shell, SWT.NONE);
		lblTime.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblTime.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblTime.setBounds(246, 512, 170, 31);
		lblTime.setText("");

	}

	public void getValueFromDb() {
		mDatabase=FirebaseDatabase.getInstance().getReference().child("pcUser");
		mDatabase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				// TODO Auto-generated method stub
				if(snapshot!=null) {
//					System.out.println(""+snapshot);


					UserDetails userDetails;
					
						userDetails = snapshot.child(""+userKey).getValue(UserDetails.class);

						if(userDetails!=null) {
//							System.out.println(" can scrape above"+canScrape);

							if(userDetails.getActive().equals("yes")) {

								canScrape=1;
								linksFileName=""+userDetails.getUsername();
//								System.out.println(" can scrape"+canScrape);

							}
							else {
								canScrape=0;

								lblNewLabel.setText("You havent purchased this software\nPlease contact M Ali  |  03158000333");
								Display.getDefault().asyncExec(new Runnable() {
									public void run() {
										//						            	scrape.setVisible(false);
										shell.dispose();
										ActivateAccount activate=new ActivateAccount();
										activate.open();
										scrape.setVisible(false);
										Download.setVisible(false);


									}
								});


								//							System.out.println("can scrape"+canScrape);

							}
						}else {
//							System.out.println("else me hai");

						}

					
				}
			}	

			@Override
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub

			}
		});
	}


	@SuppressWarnings("deprecation")
	public static void checkAccount() {
		FileInputStream refreshToken;
		try {
			refreshToken = new FileInputStream("googleFirebase/numberscraper-firebase-adminsdk-ruxwx-07dc503e0d.json");

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
