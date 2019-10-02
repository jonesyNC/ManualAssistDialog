package com.jonesync.automation.madialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

/***
 * Manual Assist Dialog Application
 * 
 * It is not always desirable or cost effective to automate all areas of an application under test. 
 * When you encounter one of these areas this code will allow you to prompt the automation test runner to perform manual steps.
 * 
 * The idea behind this stand-alone application is that your automation can do all the setup necessary for a test,
 * and then display a GUI dialog prompting the person running the test automation to manually perform steps via the application GUI.
 * 
 * The manual tester will then click on one of several buttons to indicate what should happen next.
 * A status code is returned to the calling application.
	     * 0 = Test Passed button selected
	     * 1 = Test Failed, but continue button selected
	     * 2 = Test Failed, stop executing button selected
	     * 3 = No button selected, user closed window.
 *
 * Upon receiving the return code your test automation code can perform additional actions such as verifications or just set the test status based on the returned status code.
 * 
 * Because this is a Java application it will work on all platforms that support Java.
 * 
 * @author Steven Jones
 *
 */
public class MaDialog {

	String message;
	String userInput;

	private JFrame frame;
	private JPanel panel;
	private JScrollPane scrollPane;

	private String[] buttons = { "OK", "Fail and continue", "Fail Testcase" };
	private ImageIcon icon;

	private JEditorPane ep;
	private StringBuffer style;
	JLabel label = new JLabel();
	private Font font = label.getFont();

	Container contentPane;


	/**
	 * Manual Assist dialog presented as stand alone application
	 * 
	 * The first string argument is the HTML to display in the dialog.
	 * 
	 * Returns an integer status code.
	 *
	 */
	
	    public static void main(String[] args )
	    {
	    	String message = "This is a default string that includes a hyper <a href=\"http://www.google.com\">link.</a>";
	    	
	    	if ((args != null) && (args.length != 0)) {
	    		if (args[0] != null) {
	    			message = args[0];
	    		}
	    	}
	    	MaDialog app = new MaDialog(message);
	    	int status = app.showDialog();
	    	System.out.println("*** Return status was: " + status + " ***");
	    	System.exit(status);
	    }
	    
	    public MaDialog(String message)
	    {
	    	this.message = message;
	    }
	    
	    
	    /**
	     * Displays the dialog and returns integer code:
	     * 
	     * 0 = Test Passed button selected
	     * 1 = Test Failed, but continue button selected
	     * 2 = Test Failed, stop executing button selected
	     * 3 = No button selected, user closed window.
	     * 
	     * @return returnCode
	     */
		public int showDialog() {

			int retValue = 0;
			setupDialogOptions();
			
			//Using direct method so we can make dialog resizable
			JOptionPane pane = new JOptionPane();
			pane.setMessage(panel);
			pane.setMessageType(JOptionPane.OK_CANCEL_OPTION);
			pane.setOptions(buttons);
			pane.setIcon(icon);
			pane.setInitialValue(buttons[0]);
			
			JDialog dialog = pane.createDialog(frame, "Manual Assist Automation Prompt");
			dialog.setResizable(true);
			dialog.setVisible(true);
			Object selectedValue = pane.getValue();

			//If User closed the window, return code 3
			if (selectedValue == null) return 3;
			
			//Scan selected value and return correct code.
			int maxCounter = buttons.length;
			for (int counter = 0; counter < maxCounter; counter++)
			{
				if(buttons[counter].equals(selectedValue)) {
					retValue = counter;
					break;
				}
			}
			return retValue;
		}
		

		/**
		 * setupDialogOptions sets up all the dialog options, erm yeah.
		 */
		private void setupDialogOptions() {
			// adding note in Console
			System.out.println("*** Waiting on Manual Verification ***");

			//Create a frame for this so we can deal with focus issues
			frame = new JFrame();
			frame.setAlwaysOnTop(true);

			java.net.URL imgURL = this.getClass().getResource("/Graphic.png");
			if (imgURL == null) {
				//No icon then no problem.  Just use default icon.
				icon = null;
			} else {
				icon = new ImageIcon(imgURL, "Manual Assist Logo");
			}
			

			style = new StringBuffer("font-family:" + font.getFamily() + ";");
			style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
			style.append("font-size:" + font.getSize() + "pt;");

			ep = new JEditorPane();
			ep.setContentType("text/html");
			ep.setText("<html><body>" + message + "</body></html>");
			((HTMLDocument)ep.getDocument()).getStyleSheet().addRule(style.toString());

			ep.setEditable(false);

			scrollPane = new JScrollPane(ep, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			//Set the size to something decent. You might have to adjust.
			scrollPane.setMinimumSize(new Dimension(550, 300));
			scrollPane.setPreferredSize(new Dimension(550, 300));
			
			panel = new JPanel(new BorderLayout());
			panel.add(scrollPane, BorderLayout.CENTER);
			frame.pack();
			frame.setLocationRelativeTo(null);


			// handle hyperlink events so you can click on hyperlink in the dialog text and it works.
			ep.addHyperlinkListener(new HyperlinkListener()
			{
				public void hyperlinkUpdate(HyperlinkEvent e)
				{
					if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
						try {
							Desktop.getDesktop().browse(e.getURL().toURI());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			});
		}
	}

