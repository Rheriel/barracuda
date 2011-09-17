package suncertify.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import suncertify.gui.AbstractServerWindow;
import suncertify.gui.GUIConstants;
import suncertify.gui.GUIMessages;
import suncertify.gui.GUIUtils;
import suncertify.remote.RegisterDatabase;

/**
 * Provides the functionality when user clicks on 'Start server' button on 
 * the Server Window. 
 * <br />Starts the server if the input user data is valid.
 * 
 * @author Leo Gutierrez
 */
public class StartServerListener implements ActionListener {

	/**
	 * Class name.
	 */
	static final private String CLASS_NAME = 
			StartServerListener.class.getName();
	
	/**
	 * Reference to the Server window frame.
	 */
	private AbstractServerWindow serverWindow;
	
	/**
	 * Constructs a <code>StartServerListener</code> object.
	 * 
	 * @param serverWindow Reference of the Server window frame.
	 */
	public StartServerListener(final AbstractServerWindow serverWindow) {
		this.serverWindow = serverWindow;
	}
	
	/**
	 * Invoked when user clicks on Start Server button, and does:
	 * <br />- Validates user inputs.
	 * <br />- Updates the properties file with the user input.
	 * <br />- Starts the server.
	 * <br />- Disable any editable option in the server window.
	 * 
	 * @param actionEvent The action event. 
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		
		final String methodName = "actionPerformed";
		ControllerLogger.entering(CLASS_NAME, methodName);
		
		try {
			
			if (isValidUserInput()) {
			
				updatePropertiesWithUserInput();

				RegisterDatabase.bind();

				disableComponentsOnStart();
				
				ControllerLogger.info(CLASS_NAME, methodName, 
						"Server is running");
				
			}
			
		} catch (RemoteException e) {
			
			ControllerLogger.severe(CLASS_NAME, methodName, 
					"Unable to start the server: " + e.getMessage());
			
			JOptionPane.showMessageDialog(serverWindow,
                    GUIMessages.UNABLE_TO_START_SERVER_MESSAGE,
                    GUIMessages.ERROR_TEXT,
                    JOptionPane.ERROR_MESSAGE);
			
		} finally {
			
			ControllerLogger.exiting(CLASS_NAME, methodName);
			
		}
		
	}

	/**
	 * Verifies if the database location and port number provided by the user 
	 * are valid or not.
	 * <br />If either the database location or the port number is not valid, 
	 * a warning message is shown to the user.
	 * 
	 * @return True if the database location and port number provided by the 
	 * user are valid; False otherwise.
	 */
	private boolean isValidUserInput() {
		
		final String methodName = "isValidUserInput";
		ControllerLogger.entering(CLASS_NAME, methodName);
		
		try {
			
			final String databasePath = serverWindow.getServerLocationFieldText();

			if (!isValidDatabasePath(databasePath)) {

				JOptionPane.showMessageDialog(serverWindow,
						GUIMessages.INVALID_DATABASE_MESSAGE,
						GUIMessages.WARNING_TEXT,
						JOptionPane.WARNING_MESSAGE);

				return false;
			}

			if (!isDatabaseFileEditable(databasePath)) {

				JOptionPane.showMessageDialog(serverWindow,
						GUIMessages.DATABASE_NOT_EDITABLE_MESSAGE, 
						GUIMessages.WARNING_TEXT,
						JOptionPane.WARNING_MESSAGE);

				return false;

			}

			if (!GUIUtils.isPortNumberValid(
					serverWindow.getPortNumberFieldText())) {

				JOptionPane.showMessageDialog(serverWindow,
						GUIMessages.INVALID_PORT_NUMBER_MESSAGE,
						GUIMessages.WARNING_TEXT,
						JOptionPane.WARNING_MESSAGE);

				return false;

			}

			return true;
		
		} finally {
			
			ControllerLogger.exiting(CLASS_NAME, methodName);
			
		}
	}
	
	/**
	 * Verifies if the database file is readable and writable.
	 * 
	 * @param databasePath Database file path to verify.
	 * @return True if the database file has the proper permissions (+rw); 
	 *         False, otherwise.
	 */
	private boolean isDatabaseFileEditable(final String databasePath) {
		
		final String methodName = "isDatabaseFileEditable";
		ControllerLogger.entering(CLASS_NAME, methodName, databasePath);
		
		try {
			
			final File databaseFile = new File(databasePath);

			if (!databaseFile.canRead()) {

				ControllerLogger.warning(CLASS_NAME, methodName, 
						"Database file " + databasePath + " is not readable");
				
				return false;

			}
			
			if (!databaseFile.canWrite()) {
				
				ControllerLogger.warning(CLASS_NAME, methodName, 
						"Database file " + databasePath + " is not writable");
				
				return false;
				
			}
			
			return true;
		
		} finally {
			
			ControllerLogger.exiting(CLASS_NAME, methodName);
			
		}
	}
	
	/**
	 * Verifies if the database location entered by the user is valid. Verifies
	 * if it exists, is a file and has the .db file extension.
	 * 
	 * @param databasePath Database location to verify.
	 * @return True if it is a valid database path; False otherwise.
	 */
	private boolean isValidDatabasePath(final String databasePath) {

		final String methodName = "isValidDatabasePath";
		ControllerLogger.entering(CLASS_NAME, methodName, databasePath);
		
		try {
		
			if ((databasePath == null) || ("".equals(databasePath.trim()))) {

				ControllerLogger.warning(CLASS_NAME, methodName, 
						"Database file path empty");
				
				return false;

			}

			final File databaseFile = new File(databasePath);

			if ((databaseFile.exists()) && (databaseFile.isFile())) {

				return databaseFile.getName().endsWith(
						GUIConstants.DATABASE_FILE_EXTENSION);

			} else {

				ControllerLogger.warning(CLASS_NAME, methodName, 
						"Database file path is not a file or a non-existing file: " 
								+ databasePath);
				
				return false;

			}

		} finally {
			
			ControllerLogger.exiting(CLASS_NAME, methodName);
		}
		
	}
	
	/**
	 * Disable any editable component on server window once the server started 
	 * successfully.
	 */
	private void disableComponentsOnStart() {
		
		final String methodName = "disableComponentsOnStart";
		ControllerLogger.entering(CLASS_NAME, methodName);
		
		serverWindow.setEnabledPrimaryServerButton(false);
		serverWindow.setEnabledServerLocationField(false);
		serverWindow.setEnabledPortNumberField(false);
		serverWindow.setEnabledBrowseButton(false);
		serverWindow.setStatusLabelText(
				GUIMessages.SERVER_RUNNING_STATUS_MESSAGE);
		
		ControllerLogger.exiting(CLASS_NAME, methodName);
	}

	/**
	 * Updates the properties file with the user input.
	 */
	private void updatePropertiesWithUserInput() {
		
		final String methodName = "updatePropertiesWithUserInput";
		ControllerLogger.entering(CLASS_NAME, methodName);
		
		serverWindow.updateDatabasePath(
				serverWindow.getServerLocationFieldText());
		serverWindow.updateRMIPort(serverWindow.getPortNumberFieldText());
		
		ControllerLogger.exiting(CLASS_NAME, methodName);
	}

}