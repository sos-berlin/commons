package sos.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

import sos.connection.SOSConnection;
import sos.util.SOSClassUtil;
import sos.util.SOSFile;
import sos.util.SOSLogger;
import sos.util.SOSStandardLogger;

public class SOSConnectionFileProcessor {

	SOSConnection connection = null;

	SOSLogger logger = null;

	String settingsFilename = null;

	String fileSpec = "^(.*)";
	
	boolean hasDirectory	= false;
    
    boolean commitAtEnd = false;
    
    private ArrayList<String> successFiles;
    private LinkedHashMap<String,String> errorFiles;
    
    /**
     * 
     * @param sosConnection
     * @throws Exception
     */
	public SOSConnectionFileProcessor(SOSConnection sosConnection)
			throws Exception {

		this.setConnection(sosConnection);
		this.init();
	}

	/**
	 * 
	 * @param sosConnection
	 * @param sosLogger
	 * @throws Exception
	 */
	public SOSConnectionFileProcessor(SOSConnection sosConnection,
			SOSLogger sosLogger) throws Exception {

		this.setConnection(sosConnection);
		this.setLogger(sosLogger);
		this.init();
	}

	/**
	 * 
	 * @param settingsFilename
	 * @throws Exception
	 */
	public SOSConnectionFileProcessor(String settingsFilename) throws Exception {

		this.setSettingsFilename(settingsFilename);
		this.init();
	}

	/**
	 * 
	 * @param settingsFilename
	 * @param sosLogger
	 * @throws Exception
	 */
	public SOSConnectionFileProcessor(String settingsFilename,
			SOSLogger sosLogger) throws Exception {

		this.setSettingsFilename(settingsFilename);
		this.setLogger(sosLogger);
		this.init();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {

		try {
			if (this.getConnection() == null) {
				if (this.getSettingsFilename() == null
						|| this.getSettingsFilename().trim().length() == 0)
					throw new Exception(
							"no connection and no settings filename were given for connection");

				// DB Initialisierung
				if (this.getLogger() != null)
					this.getLogger().debug3("DB Connecting.. .");
				this.setConnection(SOSConnection.createInstance(this
						.getSettingsFilename(), this.getLogger()));
				this.getConnection().connect();
				if (this.getLogger() != null)
					this.getLogger().debug3("DB Connected");

			}
		} catch (Exception e) {
			throw (new Exception("connect to database failed: "
					+ e.getMessage()));
		}
	}

	/**
	 * 
	 * @param inputFile
	 * @param fileSpec
	 * @throws Exception
	 */
	public void process(File inputFile, String fileSpec) throws Exception {

		this.setFileSpec(fileSpec);
		this.process(inputFile);
	}

	private void initCounters(){
		this.errorFiles = new LinkedHashMap<String,String>();
		this.successFiles = new ArrayList<String>();
	}
	/**
	 * 
	 * @param inputFile
	 * @throws Exception
	 */
	public void process(File inputFile) throws Exception {
		final String methodName = SOSClassUtil.getMethodName();
		
		boolean isEnd = false;
		
		try {
			if (inputFile.isDirectory()) {
				if (this.getLogger() != null){
					this.getLogger().info(String.format("%s: process directory %s, fileSpec = %s",
							methodName,
							inputFile.getAbsolutePath(),
							this.getFileSpec()));
				}
				this.hasDirectory = true;
				this.initCounters();
				
				Vector<File> filelist = SOSFile.getFilelist(inputFile
						.getAbsolutePath(), this.getFileSpec(), 0);
				
				Iterator<File> iterator = filelist.iterator();
				while (iterator.hasNext()) {
					this.process(iterator.next());
				}
				isEnd = true;
				
				if (this.getLogger() != null){
					this.getLogger().info(String.format("%s: directory proccessed (total = %s, success = %s, error = %s) %s",
							methodName,
							filelist.size(),
							this.successFiles.size(),
							this.errorFiles.size(),
							inputFile.getAbsolutePath()));
					
					if(this.successFiles.size() > 0){
						this.getLogger().info(String.format("%s:   success:",methodName));
						for(int i=0;i<this.successFiles.size();i++){
							this.getLogger().info(String.format("%s:     %s) %s",
									methodName,
									(i+1),
									this.successFiles.get(i)));
						}
					}
					if(this.errorFiles.size() > 0){
						this.getLogger().info(String.format("%s:   error:",methodName));
						int i = 1;
						for(Entry<String,String> entry : this.errorFiles.entrySet()){
							this.getLogger().info(String.format("%s:     %s) %s: %s",
									methodName,
									i,
									entry.getKey(),
									entry.getValue()));
							i++;
						}
					}
				}
			} 
			else {
				FileReader fr = null;
				BufferedReader br = null;
				StringBuffer sb = new StringBuffer();
				
				if (this.getLogger() != null){
					this.getLogger().info(String.format("%s: process file %s",
							methodName,
							inputFile.getAbsolutePath()));
				}
				
				try{
					fr = new FileReader(inputFile.getAbsolutePath());
					br = new BufferedReader(fr);
					String nextLine = "";
					while ((nextLine = br.readLine()) != null) {
						sb.append(nextLine);
						sb.append("\n");
					}
				}
				catch(Exception ex){
					throw ex;
				}
				finally{
					if(br != null){
						try{ br.close();}catch(Exception ex){}
					}
					if(fr != null){
						try{ fr.close();}catch(Exception ex){}
					}
				}
				
				this.getConnection().executeStatements(sb.toString());
				if(!this.hasDirectory){
					isEnd = true;
				}
				
				this.successFiles.add(inputFile.getAbsolutePath());
				if (this.getLogger() != null){
					this.getLogger().info(String.format("%s: file successfully processed %s",
							methodName,
							inputFile.getAbsolutePath()));
				}
			}
		} catch (Exception e) {
			this.errorFiles.put(inputFile.getAbsolutePath(), e.getMessage());
			if (this.getLogger() != null){
				this.getLogger().warn(String.format("%s: an error occurred processing file [%s]: %s",
						methodName,		
						inputFile.getAbsolutePath(),
						e.getMessage()));
			}
		} finally {
			// SQL Server 2005 compatibility issue: bei connection.rollback()
			// wird der Transaktionszähler falsch erhöht, daher explizites
			// "rollback"
			// try { if (this.getConnection() != null)
			// this.getConnection().rollback(); } catch (Exception ex) {}
			try {
				if (this.getConnection() != null && isEnd) {
                    if (this.isCommitAtEnd()) {
                        this.getConnection().commit();
                    } else {
                        this.getConnection().executeUpdate("ROLLBACK");
                    }
				}
			} catch (Exception ex) {
			}

		}
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	private String getParamValue(String param){
		String[] arr = param.split("=");
		if(arr.length > 1){
			return arr[1].trim();
		}
		else{
			return param;
		}
	}
	
	/**
	 * @return Returns the connection.
	 */
	public SOSConnection getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            The connection to set.
	 */
	public void setConnection(SOSConnection connection) {
		this.connection = connection;
	}

	/**
	 * @return Returns the logger.
	 */
	public SOSLogger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            The logger to set.
	 */
	public void setLogger(SOSLogger logger) {
		this.logger = logger;
	}

	/**
	 * @return Returns the settingsFilename.
	 */
	public String getSettingsFilename() {
		return settingsFilename;
	}

	/**
	 * @param settingsFilename
	 *            The settingsFilename to set.
	 */
	public void setSettingsFilename(String settingsFilename) {
		this.settingsFilename = settingsFilename;
	}

	/**
	 * @return Returns the fileSpec.
	 */
	public String getFileSpec() {
		return fileSpec;
	}

	/**
	 * @param fileSpec
	 *            The fileSpec to set.
	 */
	public void setFileSpec(String fileSpec) {
		this.fileSpec = fileSpec;
	}

    /**
     * @return the commitAtEnd
     */
    public boolean isCommitAtEnd() {
        return commitAtEnd;
    }

    /**
     * @param commitAtEnd the commitAtEnd to set
     */
    public void setCommitAtEnd(boolean commitAtEnd) {
        this.commitAtEnd = commitAtEnd;
    }

    
    /** 
     * 1. configuration-file
     * 2. path
     * 3. file-specification
     * 4. log-level 
     * weitere [-commit-at-end|-auto-commit|-execute-batch|batch-size=xxx]
     * 
     * process directory or files */
	public static void main(String args[]) throws Exception {
		if (args.length < 2) {
			System.out.println("Usage: SOSConnectionFileProcessor configuration-file  path  [file-specification]  [log-level] [-commit-at-end|-auto-commit|-execute-batch|batch-size=xxx]");
			return;
		}

		String settingsFile = args[0];
		int logLevel = 0;
		if (args.length > 3){
			logLevel = Integer.parseInt(args[3]);
		}
		
		SOSConnectionFileProcessor processor = new SOSConnectionFileProcessor(
				settingsFile, (SOSLogger) new SOSStandardLogger(logLevel));

		File inputFile = null;
		for(int i=0;i<args.length;i++){
			String param = args[i].trim();
			System.out.println(String.format("  %s) %s",(i+1),param));
			
			if(i == 1){
				inputFile = new File(param);
			}
			else if(i == 2){
				processor.setFileSpec(param);
			}
			else if(i > 3){
				if (param.equalsIgnoreCase("-commit-at-end")){ 
					processor.setCommitAtEnd(true);
				}
				else if (param.equalsIgnoreCase("-auto-commit")){ 
					processor.getConnection().setAutoCommit(true);
				}
				else if (param.equalsIgnoreCase("-execute-batch")){ 
					processor.getConnection().setUseExecuteBatch(true);
				}
				else if (param.startsWith("batch-size")){ 
					int batchSize = processor.getConnection().getBatchSize();
					try{
						batchSize = Integer.parseInt(processor.getParamValue(param));
					}
					catch(Exception ex){
						System.out.println(String.format("   error: invalid value of the param %s",param));
						System.out.println(String.format("          batch-size setted to default value = %s",batchSize));
					}
					processor.getConnection().setBatchSize(batchSize);
				}
			}
		}
		processor.process(inputFile);
	}
}