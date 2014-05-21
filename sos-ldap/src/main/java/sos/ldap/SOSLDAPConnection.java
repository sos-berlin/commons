package sos.ldap;

import sos.settings.SOSProfileSettings;
import sos.util.SOSClassUtil;
import sos.util.SOSLogger;
import sos.util.SOSStandardLogger;
import sos.util.SOSString;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;


import netscape.ldap.LDAPAttribute;
import netscape.ldap.LDAPAttributeSet;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPModification;
import netscape.ldap.LDAPObjectClassSchema;
import netscape.ldap.LDAPReferralException;
import netscape.ldap.LDAPSchema;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPv2;

/**
 * <p>Title: </p>
 * <p>Description: Basisklasse für LDAP Server Verbindung</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:robert.ehrlich@sos-berlin.com">Robert Ehrlich</a>
 * @author <a href="mailto:mueruevet.oeksuez@sos-berlin.com">Mürüvet Öksüz</a>
 * @resource ldapjdk.jar
 * @version 1.0 $
 */
public class SOSLDAPConnection {
	
	/** logger Object */
	private SOSLogger logger   				= null;
	
	/** host Name des LDAP-Servers */
	private String _host      				= null;
	
	/** Port-Nummer des LDAP-Server*/
	private int _port          				= LDAPConnection.DEFAULT_PORT;
	
	/** distinguished name used for authentication*/
	private String _authid     				= null;
	
	/** passwd password used for authentication */
	private String _authpw     				= null;
	
	/** the base distinguished name from which to search */
	private String _base       				= null;       
	
	/** Filterkriterien*/
	private String _filter     				= null; 
	
	/**
    * Bestimmt wie tief soll die Suche gehen
    * 
    * LDAPv2.SCOPE_BASE		nur die base wiedergeben
    * LDAPv2.SCOPE_ONE 		nur auf einer Ebene suchen, nicht in Äste verzweigen
    * LDAPv2.SCOPE_SUB		alle (unter) Ebenen zurückgeben
    **/ 
	private int _scope						= LDAPv2.SCOPE_SUB;
	
	/** LDAPConnection Object*/
	private LDAPConnection connection		= null;
		
	/** Alle Ergebnisse werden in diese Liste gemerkt, ein Eintrag dieser Klasse ist ein HashMap */
    //private ArrayList attrsList				= new ArrayList();
    
	/** Alle Ergebnisse werden in diesm Vector gemerkt, ein Eintrag ist ein LDAPEntry */
   //private ArrayList entries					= null;
	
    /** LDAP Protokoll version*/
    private static int LDAP_PROTOKOLL_VERSION= 3;
    
  
    /** Suche liefert nur Attribute - keine Werte */
	private boolean	searchOnlyAttributes	= false; 
	
	/**
	 *
	 * @throws java.lang.Exception
	 */
	public SOSLDAPConnection (
			String host, 
			int port, 
			String authid, 
			String authpw, 
			String base, 			
			String filter
			) throws Exception {
		init( 
				host, 
				port, 
				authid, 
				authpw, 
				base,     				
				filter,
				null);
	}
	
	/**
	 *
	 * @throws java.lang.Exception
	 */
	public SOSLDAPConnection (
			String host, 
			int port, 
			String authid, 
			String authpw, 
			String base, 			
			String filter,
			SOSLogger sosLogger) throws Exception {
				
		if(sosLogger == null)
			throw (new Exception("invalid SOSLogger object !!"));
					
		init( 
				host, 
				port, 
				authid, 
				authpw, 
				base,     				
				filter,
				sosLogger);
	}
	
	/**
	 * 
	 * @param sosLogger_
	 * @param configfile
	 * @throws Exception
	 */
	public SOSLDAPConnection(
			String configfile) throws Exception {
		try {    		
			
			SOSProfileSettings jobSettings = new SOSProfileSettings(configfile);
			Properties section = jobSettings.getSection("ldap");
			
			
			int port = LDAPConnection.DEFAULT_PORT;
			if (section.getProperty("port") != null 
					&& section.getProperty("port").length() > 0)  {
				port    = Integer.parseInt(section.getProperty("port"));
			}    		    		
			
			init( 
					section.getProperty("host"), 
					port, 
					section.getProperty("authid"), 
					section.getProperty("authpw"), 
					section.getProperty("base"),     				
					section.getProperty("filter"),
					null);			
			
		} catch (Exception e) {
			throw new Exception ("..error in " + SOSClassUtil.getMethodName() + " " + e);
		}
		
	}
	
	/**
	 * 
	 * @param sosLogger_
	 * @param configfile
	 * @throws Exception
	 */
	public SOSLDAPConnection(
			String configfile,SOSLogger sosLogger) throws Exception {
		try {    		
			
			if(sosLogger == null)
				throw (new Exception("invalid SOSLogger object !!"));
			
			SOSProfileSettings jobSettings = new SOSProfileSettings(configfile, sosLogger);
			Properties section = jobSettings.getSection("ldap");
			
			
			int port = LDAPConnection.DEFAULT_PORT;
			if (section.getProperty("port") != null 
					&& section.getProperty("port").length() > 0)  {
				port    = Integer.parseInt(section.getProperty("port"));
			}    		    		
			
			init( 
					section.getProperty("host"), 
					port, 
					section.getProperty("authid"), 
					section.getProperty("authpw"), 
					section.getProperty("base"),     				
					section.getProperty("filter"),
					sosLogger);			
			
		} catch (Exception e) {
			throw new Exception ("..error in " + SOSClassUtil.getMethodName() + " " + e);
		}
		
	}
	
	/**
	 * 
	 * @param host
	 * @param port
	 * @param authid
	 * @param authpw
	 * @param base
	 * @param filter
	 * @param sosLogger
	 * @throws Exception
	 */
	private void init(
			String host, 
			int port, 
			String authid, 
			String authpw, 
			String base, 			
			String filter,
			SOSLogger sosLogger) throws Exception {            	   
                        
		this.logger 	= sosLogger;
		
		this._host    	= host;
		if (port != -1) {
			this._port    	= port;
		}
		this._authid  	= authid;
		this._authpw  	= authpw;
		this._base    	= base;   
		
		// filter darf nicht "" sein sondern null
		if(filter != null && filter.length() == 0) filter = null;
		this._filter  	= filter;
		
		
		connection 				= new LDAPConnection(); 
		
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList search() throws Exception {
		return search(null, this._base, this._filter);		
	}
	
	/**
	 * 
	 * @param attrs
	 * @return
	 * @throws Exception
	 */
	public ArrayList search(String[] attrs) throws Exception {
		return search(attrs, this._base, this._filter);		
	}

	/**
	 * 
	 * @param attrs
	 * @param base
	 * @return
	 * @throws Exception
	 */
	public ArrayList search(String[] attrs, String base) throws Exception {
		return search(attrs, base, this._filter);		
	}
	
	/**
	 *  
	 * @param attrs  	 	zB {"sn","cn","uid","parent"}
	 * @param base  		zB ou=users,dc=my-domain,dc=com
	 * @param filter		zB (objectClass=*) <- alles oder (cn=*) usw
	 * @return ArrayList
	 * @throws Exception
	 */
	public ArrayList search(String[] attrs, String base, String filter) throws Exception {
		
	    ArrayList entries	= new ArrayList();
		
		try {
		    
		    if(logger != null){ logger.debug9("calling " + SOSClassUtil.getMethodName());}

		     if (connection == null || !connection.isConnected()){
		        throw new Exception(SOSClassUtil.getMethodName() +
		            ": sorry, there is no successful connection established." +
		            " may be the connect method was not called");
		     }
			
		     if(logger != null){logger.debug5("search : base = "+base+" filter = " + filter);}
		     
		     LDAPSearchResults res = connection.search(   base
						,this.getScope() // SCOPE_BASE, SCOPE_ONE oder SCOPE_SUB 											   
						, filter
						, attrs
						,this.getSearchOnlyAttributes());
				
			/*
			 *  Verwendung von SORT
			 */
			/*
			String[]  sortAttrs = {"sn", "cn"};
			boolean[] ascending = {true, true};
			res.sort( new LDAPCompareAttrNames(sortAttrs, ascending) );
			*/
				
				
			// alle entries lesen
			while (res.hasMoreElements()) {
			    try {
			        // The next entry
					LDAPEntry entry = res.next();
					entries.add(entry);
						
				} 
			    catch (LDAPReferralException e) {
			        // Ignore any referrals
					continue;
				} catch (LDAPException e) {
				    if(logger != null){	logger.debug5(e.toString());}
					continue;
				}
			}
			
		} 
		catch (Exception e) {
			throw new Exception("..error in " + SOSClassUtil.getMethodName() + " "+e );
		}

	return entries;
	}
	


	/**
	 * simple Authenticates to the LDAP server (to which you are currently connected)
	 * 
	 * @param dn				DN Name
	 * @param password			Password
	 * @throws LDAPException
	 */
	public void bind(String dn,String password) throws Exception{
	 
	    this.connection.bind(dn,password);
	}
	
	
	/**
	 * simple Authenticates to the LDAP server (to which you are currently connected)
	 * 
	 * @param version			specified protocol version
	 * @param dn				DN Name
	 * @param password			Password
	 * @throws LDAPException
	 */
	public void bind(int version,String dn,String password) throws Exception{
	    if(logger != null){ logger.debug9(SOSClassUtil.getMethodName()+" : version = "+version+" dn = "+dn+" password = "+password);}

	    this.connection.bind(version,dn,password);
	}
	
	
	/**
	 * im Test
	 * 
	 * @throws LDAPException
	 */
	public Object getSchemaObjects() throws Exception{
	    try{
	        
	        if (connection == null || !connection.isConnected()){
		        throw new Exception(SOSClassUtil.getMethodName() +
		            ": sorry, there is no successful connection established." +
		            " may be the connect method was not called");
		    }
	        
	        LDAPSchema dirSchema = new LDAPSchema();
	    
	        // Gib schemas.
	        dirSchema.fetchSchema( connection);
	        Enumeration ocs = dirSchema.getObjectClasses();
	        while ( ocs.hasMoreElements() ) {
	            LDAPObjectClassSchema oc = (LDAPObjectClassSchema)ocs.nextElement();
	            
	            if(logger != null){
	                logger.debug5(SOSClassUtil.getMethodName()+" Object = "+oc.getName());
	            }
	            
	            System.out.println("Schema Object = "+oc.getName());
	        }
	        System.out.println("---------------------------------------");
	       
	    }
	    catch(Exception e){
	        throw new Exception("..error in " + SOSClassUtil.getMethodName() + " " + e );
	    }
	    
        return "";
	}
	
	/**
	 * im Test
	 * 
	 * @throws LDAPException
	 */
	public Object getSchemaObject(String object) throws Exception{
	    try{
	        
	        if (connection == null || !connection.isConnected()){
		        throw new Exception(SOSClassUtil.getMethodName() +
		            ": sorry, there is no successful connection established." +
		            " may be the connect method was not called");
		    }
	        
	        LDAPSchema dirSchema = new LDAPSchema();
		    
	        // Get the schema from the directory.
	        dirSchema.fetchSchema( connection );
	        
	        LDAPObjectClassSchema objClass = dirSchema.getObjectClass(object);
	        if ( objClass == null ) {
	            System.out.println("Object = "+object+" not found ");
	        }
	        else{
	            System.out.println("-------------------------");
	    	    System.out.println(object+" : "+objClass.toString());
	            
	    	    Enumeration reqAttribute = objClass.getRequiredAttributes();
	    	    
	    	    System.out.println("MUST Attributes");
		        
	    	    while ( reqAttribute.hasMoreElements() ) {
		            String req = (String)reqAttribute.nextElement();
	                System.out.println("\t\t"+req);
		        }
	            
	            Enumeration optAttribute = objClass.getOptionalAttributes();
	            System.out.println("MAY Attributes");
	            while ( optAttribute.hasMoreElements() ) {
		            String opt = (String)optAttribute.nextElement();
	            
		            //System.out.println("schema oc = "+oc.getName());
		            System.out.println("\t\t"+opt);
		        }
	            	            
	            System.out.println("--- ende Objekt "+object+" ----");
	            
	        }

	      
	    }
	    catch(Exception e){
	        throw new Exception("..error in " + SOSClassUtil.getMethodName() + " " + e );
	    }
	    
        return "";
	}
	

	
	
	/**
     * Ändern eines Eintrages auf der LDAAP-Server
     * 
     * @param attrKey
     * @param value
     * @return boolean
     * @throws Exception
     */
    public boolean modify(String attrKey, String value) throws Exception {
    	return this.modify(attrKey, value, this._base);    	
    }
	
    /**
     * Ändern eines Eintrages auf der LDAAP-Server.
     * Der base_ muß genau die Pfadangabe geben
     * @param attrKey
     * @param value
     * @param base_
     * @return boolean
     * @throws Exception
     */
    public boolean modify(String attrKey, String value, String base) throws Exception {
    
    	try {
    	    if (connection == null || !connection.isConnected()){
		        throw new Exception(SOSClassUtil.getMethodName() +
		            ": sorry, there is no successful connection established." +
		            " may be the connect method was not called");
		    }
    	    
    	    try {
    			// bind to the server                
    		    connection.bind(_authid, _authpw );
    			
    			//change attribut    			
    		    connection.modify( base, 
    					new LDAPModification(LDAPModification.REPLACE,
    							new LDAPAttribute( attrKey, value)));
    			return true;
    		} catch( LDAPException e1) {
    			throw new Exception ("..can not change the Key. " + attrKey + "=" + value + " " + e1.toString());                   
    		}
    		
  		
    	} catch (Exception e) {
    		throw new Exception ("..error in " + SOSClassUtil.getMethodName() + " " + e);
    	}
    	
    }
    
    /**
     * 
     */
    public String toDate(String dateString)  throws Exception {
      if( SOSString.isEmpty(dateString)) throw new Exception(SOSClassUtil.getMethodName() + ": dateString has no value.");
      return "'" + dateString + "'";
    }

    
    /**
     * Stellt die LDAP Verbindung dar
     * @throws Exception
     */
    public void connect() throws Exception {    	
        connection.connect(LDAP_PROTOKOLL_VERSION, _host, _port, _authid, _authpw);    	    	
    }
    
    /**
     * schliesst alle offenen Resourcen.
     *
     * @throws Exception
     */
    public void disconnect() throws Exception {
        
        if(connection != null){
            connection.disconnect();
            
            connection = null;
            
            try {
              logger.debug6(SOSClassUtil.getMethodName() +
                            ": successfully disconnected.");
            }
            catch (Exception e) {}
        }
    }
    
    /**
	 * Liefert die Variable "authid".
	 * Der Typ ist String.  
	 *
	 * @return String
	 */
	public String getAuthid() {
		return _authid;
	}
	/**
	 * Setzt die Variable "authid". 
	 * 
	 * @param authid
	 */
	public void setAuthid(String authid) {
		this._authid = authid;
	}
	/**
	 * Liefert die Variable "authpw".
	 * Der Typ ist String.  
	 *
	 * @return String
	 */
	public String getAuthpw() {
		return _authpw;
	}
	/**
	 * Setzt die Variable "authpw". 
	 * 
	 * @param authpw
	 */
	public void setAuthpw(String authpw) {
		this._authpw = authpw;
	}
	/**
	 * Liefert die Variable "base".
	 * Der Typ ist String.  
	 *
	 * @return String
	 */
	public String getBase() {
		return _base;
	}
	/**
	 * Setzt die Variable "base". 
	 * 
	 * @param base
	 */
	public void setBase(String base) {
		this._base = base;
	}
	/**
	 * Liefert die Variable "host".
	 * Der Typ ist String.  
	 *
	 * @return String
	 */
	public String getHost() {
		return _host;
	}
	/**
	 * Setzt die Variable "host". 
	 * 
	 * @param host
	 */
	public void setHost(String host) {
		this._host = host;
	}
    
	/**
	 * Liefert die Variable "port".
	 * Der Typ ist int.  
	 *
	 * @return int
	 */
	public int getPort() {
		return _port;
	}
	
	/**
	 * Setzt die Variable "port". 
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this._port = port;
	}
	
	/**
	 * Liefert die LDAPConnection Objekt.
	 * Der Typ ist LDAPConnection.  
	 *
	 * @return LDAPConnection
	 */
	public LDAPConnection getLDAPConnection() {
		return connection;
	}
	
	/**
	 * Setzt die LDAPConnection Objekt. 
	 * 
	 * @param ld -> LDAPConnection Objekt
	 */
	public void setLDAPConnection(LDAPConnection connection) {
		this.connection = connection;
	}
	
	
	
	/**
	 * @return Returns the filter.
	 */
	public String getFilter() {
		return _filter;
	}
	/**
	 * @param filter The filter to set.
	 */
	public void setFilter(String filter) {
		this._filter = filter;
	}
	
	
	   /**
     * @return Returns the scope.
     */
    public int getScope() {
        return _scope;
    }
    
    
    /**
    * Bestimmt wie tief soll die Suche gehen
    * 
    * 0 - LDAPv2.SCOPE_BASE	nur die base wiedergeben
    * 1 - LDAPv2.SCOPE_ONE 	nur auf einer Ebene suchen, nicht in Äste verzweigen
    * 2 - LDAPv2.SCOPE_SUB		alle (unter) Ebenen zurückgeben
    * 
    * @param scope  0, 1 oder 2
    * 
    */
    public void setScope(int scope) {
        
        if(scope < 0 || scope > 2){
            this._scope = LDAPv2.SCOPE_SUB;
        }
        else{
            this._scope = scope;
        }
    }

    
    /**
     * 2 Argumente
     * 
     * 1) config file
     * 	 
     *   Suchstring 
     * 2) 
     * 
     * 	a)	Alle Eigenschaften eines Knotes lesen
     * 		filter Kriterien => Eigenschaft=Wert;Eigenschaft=Wert....
     * 		zB :"cn=admin;userPassword="
     * 	b)  Alle vorhandene Objekte auflisten
     * 		Aufruf : objects (als zweites Argument)
     *  c)	Ein Objekt lesen 
     * 		Aufruf : object <object name> (als zweites und drittes Argument)		
     * 
     * @param args
     */
    public static void main(String[] args) {
        
        SOSLogger sosLogger 		= null;
		SOSLDAPConnection lc 		= null;
        File 		argIniFile 		= null;
        String		argAttributes 	= null;
        String[]	attributes		= null;
        
        StringBuffer searchParametrs	= new StringBuffer();
        
        try{
            if(args == null || args.length == 0){
                throw new Exception("no arguments found");
            }
                        
            for(int i=0; i<args.length; i++){
    		    System.out.println("arg "+i+" = "+args[i]);
    		}
            
            argIniFile = new File(args[0]);
            if(!argIniFile.exists()){
                throw new Exception("..file does not exist: " + argIniFile.getAbsolutePath());
            }
            
            try{
                argAttributes = args[1];
            }
            catch(Exception e){
                throw new Exception("no search attributes arguments found");
            }
            
            sosLogger = new SOSStandardLogger(0);
            lc = new SOSLDAPConnection(argIniFile.toString(),sosLogger);
            
            lc.connect();
            
            // lc.connection.bind(3,"cn=Manager,dc=my-domain,dc=com","secret");
            
            
            // alle Objekte zurückgeben
            if(argAttributes.equalsIgnoreCase("objects")){
                lc.getSchemaObjects();
            }
            else if(argAttributes.equalsIgnoreCase("object")){
                try{
                    String object = args[2];
                    lc.getSchemaObject(object);
                }
                catch(Exception e){
                    System.out.println("Objekt name fehlt");
                }
            }
            else if(argAttributes.equalsIgnoreCase("login")){
                
                int version =  SOSLDAPConnection.getLDAP_PROTOKOLL_VERSION();
                
                String cn 	= "";
                String pass = "";
                                
                try{
                    cn = args[2];
                }
                catch(Exception e){}
                try{
                    pass = args[3];
                }
                catch(Exception e){}
                
                //lc.setAuthid(cn);
                //lc.setAuthpw(pass);
                
                System.out.println("AUTHID = "+lc._authid);
                System.out.println("AUTHPW = "+lc._authpw);
                
                
                try{
                    lc.connection.bind(cn,pass);
                    
                    /*
                    lc.setFilter("(cn=re)");
                    ArrayList result = lc.search();
                    
                    LDAPEntry entry =  (LDAPEntry)result.get(0);
                    LDAPAttribute up = entry.getAttribute("userPassword");
                    
                    
                    System.out.println(up.toString());
                    
                    
                    Enumeration enumVals = up.getStringValues();
			        
                    String userPass = "";
                    while ( enumVals.hasMoreElements() ) {
                        userPass = ( String )enumVals.nextElement();
                        System.out.println( "\t" + userPass );
                    }
                    
                    //System.out.println(up.g));
                    
                    //lc.connection.compare("cn=re,ou=sos,dc=my-domain,dc=com",up);
                    */
                    
                    System.out.println("");
                    System.out.println("Anmeldung für "+cn+" mit kennwort "+pass+" erfolgreich");
                }
                catch(Exception e){
                    System.out.println("");
                    System.out.println("Anmeldung für "+cn+" mit kennwort "+pass+" fehlgeschlagen : "+e);
                }
              
            }
            else{
                //lc.setBase("ou=users,cn=Manager,dc=my-domain,dc=com");
                
                attributes = argAttributes.split(";");
                StringBuffer filter = new StringBuffer("(&");
                StringBuffer ldapAttr = new StringBuffer();
            
                String[] ldapAttributes = new String[attributes.length];
            
                for(int i=0;i<attributes.length;i++){
                    String[] value = attributes[i].split("=");
                
                    ldapAttr.append(value[0]+";");
                    ldapAttributes[i] = value[0];
                    try{
                        if(value[1].equalsIgnoreCase("null")){
                            filter.append("(!"+value[0]+"=*)");
                        }
                        else{
                            filter.append("("+value[0]+"="+value[1]+")");
                        
                        }
                        searchParametrs.append(value[0]+"="+value[1]+" ");
                    }
                    catch(Exception e){ // ist NULL
                        filter.append("(!"+value[0]+"=*)"); 
                        searchParametrs.append(value[0]+" is null");
                    }
                }
            
                filter.append(")");
                lc.setFilter(filter.toString());
            
                //lc.search(ldapAttributes);
                // alle Attribute zurückgeben
                ArrayList entries = lc.search(null);
            
                System.out.println("");
            
                if(entries == null || entries.size() == 0){
                    System.out.println("LDAP Server hat keinen Eintrag gefunden für "+searchParametrs.toString());
                }
                else{
                    System.out.println("LDAP Server hat "+entries.size()+" Eintrag(e) gefunden für "+searchParametrs.toString());
                
                    int j=1;
                    for(int i=0;i<entries.size();i++){
                        System.out.println("----Eintrag "+j+"------------------------");
                        LDAPEntry entry = (LDAPEntry)entries.get(i);
                        System.out.println("DN ist : "+entry.getDN());
					
                        LDAPAttributeSet set = entry.getAttributeSet();

                        //System.out.println("attribute 1 = "+set);
                        System.out.println("");

                        Enumeration enumAttrs = set.getAttributes();
                        while ( enumAttrs.hasMoreElements() ) {
                            LDAPAttribute anAttr = (LDAPAttribute)enumAttrs.nextElement();
                            String attrName = anAttr.getName();
				           
                            System.out.print( attrName );
				           
                            Enumeration enumVals = anAttr.getStringValues();
				           
                            if(enumVals != null){
                                while ( enumVals.hasMoreElements() ) {
                                    String aVal = ( String )enumVals.nextElement();
                                    System.out.println( "\t" + aVal );
                                }
                            }
                        }
                    
                    
                    
                        System.out.println("----Ende Eintrag "+j+"------------------------");
                        j++;
                    
                    }
                
                }
            
            }
        }
        catch(Exception e){
            System.out.println("error : "+e);
        }
        finally{
            try{  lc.disconnect(); }
            catch(Exception e){}
        }
        
    }
	
	
    /**
     * @return Returns the lDAP_PROTOKOLL_VERSION.
     */
    public static int getLDAP_PROTOKOLL_VERSION() {
        return LDAP_PROTOKOLL_VERSION;
    }
    /**
     * @param ldap_protokoll_version The lDAP_PROTOKOLL_VERSION to set.
     */
    public static void setLDAP_PROTOKOLL_VERSION(int ldap_protokoll_version) {
        LDAP_PROTOKOLL_VERSION = ldap_protokoll_version;
    }
    
    /**
     * @return Returns the searchOnlyAttributes.
     */
    public boolean getSearchOnlyAttributes() {
        return searchOnlyAttributes;
    }
    /**
     * @param searchOnlyAttributes The searchOnlyAttributes to set.
     */
    public void setSearchOnlyAttributes(boolean searchOnlyAttributes) {
        this.searchOnlyAttributes = searchOnlyAttributes;
    }
 
	
	   
    
 }