package java_Parsing_Stock_Ticker_Data;
import java.util.*;
import java.util.zip.*;
import java.io.*;

public class UnzipUtility {
	//we will use this class to abstract the rest of the code from 2 operations
	//1. downloading the zip from the NSE website
	//2. unzipping the downloaded from the website to some location on our local machine
	
	//note the order in which we went about this - we first decided what member functions we would like our class to have. 
	//We defined the 'interface' for these functions, i.e. what inputs and outputs those functions would have
	
	//note also that we made all 3 member functions here 'static', which was because these member functions have very 
	//little (none) object-specific state or behavior. They almost perfectly resemble functions from a pure procedural 
	//or imperative language, but they are still housed in a class because in java all code must reside in a class
	
	private static final int S_BYTE_SIZE=4096;
	
	public static List<String> downloadAndUnzip(String urlString, String zipFilePath, String destDirectory)
	throws IOException {
		
	}
	
	public static List<String> unzip(String zipFilePath, String destDirectory) throws IOException {
		// this member function will take the name of a zip file and the name of a directory to extract it to
		// and return a list of the files that were unzipped
		
		List<String> unzippedFileList = new ArrayList<>();
		
		File destDir = new File(destDirectory);
		//get a low level file object corresponding to the destination directory
		
		if(!destDir.exists()) {
			destDir.mkdir();
			//create the destination directory if it does not yet exist
		}
		
		//now - iterate over entries in the zip file, and extract 1-by-1 using the extractFile method
		//that we just wrote
		
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		//remember that the zip file is already downloaded and saved to some local path in the 'downloadAndUnzip'
		//member function which will call this one
		
		ZipEntry zipEntry = zipIn.getNextEntry();
		while (zipEntry!=null) {
			String filePath = destDirectory+File.separator+zipEntry.getName();
			//construct the path that we wish to extract ths file to. How? Use a concatenation of the destination directory
			//passd in, the patg separator for this platform/OS (ie File.Separator)
			//and the name of the file itself
			
			if(!zipEntry.isDirectory()) {
				//if the zip entry is a file, extract it using the function we just wrote, and add to our list 
				//of unzipped files
				String oneUnzippedFile=extractFile(zipIn, filePath);
				unzippedFileList.add(oneUnzippedFile);	
			} else {
				//if the zip entry is a directory, make the direcotry and keep going
				File dir = new File(filePath);
				dir.mkdir();
			}
			//Advance the iterator variable to the next value, esle this code will go into an Infinite Loop  
			zipEntry = zipIn.getNextEntry();
		}
		
	}
	
	private static String extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		//pretty standard boiler-plate for anytime we need to save a file i.e. write to disk
		
		byte[] bytesIn = new byte[S_BYTE_SIZE];
		
		//we typically need to create (binary) files in chunks of bytes. Those chunks need ot be in powers of 2. 
		//Here we choose 4096. It's not good practice to hardcode a value like this, though, 
		//so we will extract this to a static member variable
		
		//ok - now more low level boiler-plate to save the file 
		
		int read = 0; 
		while ((read=zipIn.read(bytesIn))!= -1) {
			//what are we doing here? remember that the zip file is by default downloaded and placed 
			//in some folder that but will happen in the 'downloadAndUnZip' member function above. 
			//Now, this function is extracting one single file form inside that zip file, and saving 
			//it byte-by-byte, into the corresponding unzipped file in the location
			bos.write(bytesIn,0,read);
			//'read' tells us how many bytes we read in this iteration. If the file is done reading, or if an error
		    //occured, we will get a negative number of -1
		}
		bos.close();
		//if we got here it means that the file was unzipped successfully
		return filePath;
		// let's return the filepath of the unzipped file so the calling code can keep track of this. 
	}
}
