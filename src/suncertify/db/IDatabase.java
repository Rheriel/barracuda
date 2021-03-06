package suncertify.db;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Models the remote and local access to the database.
 * 
 * @author Leo Gutierrez
 */
public interface IDatabase extends Remote {

	/**
	 * Creates a new record into the database.
	 * 
	 * @param record Record data to save into the database.
	 * @return Database position where the record was saved. 
	 * @throws DuplicateKeyException If the record already exists in the 
	 *                               database.
	 * @throws RemoteException If any networking error occurs.
	 */
	int create(Record record) 
			throws DuplicateKeyException, RemoteException;
	
	/**
	 * Deletes the specified record in the database.
	 * 
	 * @param recordRow Record position where to delete.
	 * @throws RecordNotFoundException If the record is not found or already 
	 *                                 deleted.
	 * @throws RemoteException If any networking error occurs.
	 */
	void delete(int recordRow) 
			throws RecordNotFoundException, RemoteException;
	
	/**
	 * Updates the specified record position with he given record data.
	 * 
	 * @param recordRow Record position where to update.
	 * @param record Record data to update.
	 * @throws RecordNotFoundException If the record is not found or it is 
	 *                                 deleted.
	 * @throws RemoteException If any networking error occurs.
	 */
	void update(int recordRow, Record record) 
			throws RecordNotFoundException, RemoteException;
	
	/**
	 * Reads a record in the specified record position.
	 * 
	 * @param recordRow Record position where to read.
	 * @return The record read.
	 * @throws RecordNotFoundException If the record is not found or it is
	 * 								   deleted.
	 * @throws RemoteException If any networking error occurs.
	 */
	Record read(int recordRow) 
			throws RecordNotFoundException, RemoteException;
	
	/**
	 * Locates and retrieves record(s) in the database that start with
	 * the given name and location values. 
	 * <br />The values can be null or empty:
	 * <br />- If name is null or empty, all name values in database will 
	 * match.
	 * <br />- If location is null or empty, all location values in database 
	 * will match.
	 * <br />- If both are null or empty, all records are retrieved.
	 * 
	 * @param name Name to search. Can be null or empty (will match any name 
	 *             value).
	 * @param location Location to search. Can be null or empty (will match 
	 *                 any location value).
	 * @return Record(s) found during the search. If no record is found, an 
	 *         empty list is returned.
	 * @throws RemoteException If any networking error occurs.
	 */
	List<Record> find(String name, String location) 
			throws RemoteException;
	
}
