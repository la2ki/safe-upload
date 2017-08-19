package rs.marko.lalic.safe.core.services;

import org.apache.commons.io.FileUtils;
import rs.marko.lalic.safe.core.Utils;
import rs.marko.lalic.safe.core.exceptions.InternalErrorException;
import rs.marko.lalic.safe.core.exceptions.InvalidRequestException;

import java.io.File;
import java.io.IOException;

/**
 * Service used for file system operations
 * Created by Marko Lalic on 7/4/2017.
 */
public class FileSystemService {

    /**
     * Root folder loaded on startup
     */
    private File rootFolder;

    /**
     * Empty construct
     */
    public FileSystemService() {

    }

    /**
     * Method will set root folder
     * @param rootFolder Root folder path
     * @throws InternalErrorException thrown if folder is invalid
     */
    public void setRootFolder(String rootFolder) throws InternalErrorException {
	this.rootFolder = new File(rootFolder);
	if (!this.rootFolder.isDirectory()) {
	    throw new InternalErrorException(
			    Utils.buildString("Root folder invalid. Check configuration. Path: ", rootFolder));
	}
    }

    /**
     * Method will get root folder
     * @return
     */
    public File getRootFolder() {
	return rootFolder;
    }

    /**
     * Method will create folder in file systemon given destination with given name
     * @param destinationFolderPath Destination folder path
     * @param name Name of folder
     * @return Newly created folder
     * @throws InvalidRequestException thrown if some invalid request happens
     */
    public File createFolder(String destinationFolderPath, String name) throws InvalidRequestException {
	File destinationFolder = new File(destinationFolderPath);
	if (!destinationFolder.isDirectory()) {
	    throw new InvalidRequestException(Utils.buildString("Invalid path provided. Path: ", destinationFolder));
	}
	File folder = new File(destinationFolder, name);
	folder.mkdir();
	return folder;
    }

    /**
     * Method will create folder in file systemon given destination with given name
     * @param destinationFolder Destination folder
     * @param name Name of folder
     * @return Newly created folder
     * @throws InvalidRequestException thrown if some invalid request happens
     */
    public File createFolder(File destinationFolder, String name) throws InvalidRequestException {
	if (!destinationFolder.isDirectory()) {
	    throw new InvalidRequestException(Utils.buildString("Invalid path provided. Path: ", destinationFolder));
	}
	File folder = new File(destinationFolder, name);
	folder.mkdir();
	return folder;
    }

    /**
     * Method will rename folder
     * @param folder folder
     * @param newName New name
     * @return Renamed folder
     */
    public File renameFolder(File folder, String newName) {
	folder.renameTo(new File(folder.getParentFile(), newName));
	return folder;
    }

    /**
     * Method will remove folder
     * @param folder removed folder
     */
    public void removeFolder(File folder) {
	folder.delete();
    }

    /**
     * Method will add file to folder
     * @param folderPath Folder path
     * @param file File
     * @param name Name of file
     * @throws InvalidRequestException thrown if creating file in folder fails
     * @throws InternalErrorException thrown if some internal error happens
     */
    public void addFileToFolder(String folderPath, File file, String name)
		    throws InvalidRequestException, InternalErrorException {
	File folder = getFolderByPath(folderPath);
	File newFile = new File(folder, name);
	try {
	    FileUtils.copyFile(file, newFile);
	}
	catch (IOException e) {
	    throw new InternalErrorException(
			    Utils.buildString("Error occurred while trying to create new file. Temp file: ",
					    file.getAbsolutePath(), ". New file name: ", name));
	}
    }

    /**
     * Method will get folder by path
     * @param path Path of folder
     * @return File
     * @throws InvalidRequestException thrown if path was invalid
     */
    private File getFolderByPath(String path) throws InvalidRequestException {
	File folder = new File(path);
	if (!folder.isDirectory()) {
	    throw new InvalidRequestException(Utils.buildString("Invalid path provided. Path: ", path));
	}
	return folder;
    }
}
