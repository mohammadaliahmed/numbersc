import org.eclipse.swt.SWT;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.swt.widgets.ProgressBar;

public class CountDownScreen {

	protected static Shell shell;

	static ProgressBar progressBar;
	static int sleep;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CountDownScreen window = new CountDownScreen();
			window.open(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open(int sleep) {
		CountDownScreen.sleep=sleep;
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
		shell.setSize(450, 215);
		shell.setText("Loading");


		progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setBounds(25, 58, 387, 66);
		
		Label lblScrapingpleaseWait = new Label(shell, SWT.NONE);
		lblScrapingpleaseWait.setBounds(142, 26, 138, 15);
		lblScrapingpleaseWait.setText("Scraping...Please wait");



		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				updateProgressBar();

			}
		});
	}private static void updateProgressBar()
	{
		new Thread(new Runnable()
		{
			private int                 progress    = 0;
			private static final int    INCREMENT   = 2;

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
							int selections = (progress += INCREMENT) % (progressBar.getMaximum() + INCREMENT);
							if (!progressBar.isDisposed())
								
								progressBar.setSelection((progress += INCREMENT) % (progressBar.getMaximum() + INCREMENT));
							System.out.println(""+selections);
							if(selections>80) {
								shell.dispose();
							}


						}
					});

					try
					{
						Thread.sleep(sleep*1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
	}







}
