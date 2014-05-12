package sos.xml.parser;

/**
 * <p>Parsierung einer XML-Datei.</p>
 * 
 * <p>Verwendung einer DOMParser. </p>
 * 
 * <p>Die Parsierung erfolgt von der root bis zu letzte Blatt. Die parsierte Pfad
 * wird mit seinen Knoten und Knotenwerten in eine Hashtabelle zusammengefaßt.</p>
 * 
 * <p>Wiederholende Elemente oder Pfade werden in eine neue HashTabelle
 * geschrieben. Alle Hashtabellen werden in eine Liste zusammengefügt. Mithilfe
 * der Methode getListOfXMLPath() kann diese Liste ausgelesen werden.
 * 
 * <p>Die Klasse schreibt für einen Pfad einen insertstatment. Die Methode
 * getInsertStatement() liefert eine Liste der gesamten insertstatements.</p>
 * 
 * <p>Eine XML-Datei in der Form:</p>
 * <p>-------------------------------------</p>
 * <p>root                                </p>
 * <p>	  Familien </p>
 * <p>		- Vater</p>
 * <p>		- Mutter</p>
 * <p>      - Kinder</p>
 * <p>			- Kind 1</p>
 * <p>			- Kind 2</p>
 * 
 * <p>-------------------------------------</p>
 * <p> wird interpretiert als:</p>
 * <p> 1. Vater, Mutter, Kind 1</p>
 * <p> 2. Vater, Mutter, Kind 2</p>
 * 
 * <p>Die XML-Datei sollte so aufgebaut sein, das Komplextyps immer in der unteren
 * Ebenen liegen.</p>
 * 
 * z.B. Falsch wäre das Beispiel:  
 *  * <p>-------------------------------------</p>
 * <p>root                                </p>
 * <p>	  Familien </p>
 * <p>		- Vater</p>
 * <p>      - Kinder</p>
 * <p>			- Kind 1</p>
 * <p>			- Kind 2</p>
 * <p>		- Mutter</p>
 * 
 * <p> -------------------------------------</p>
 * <p> wird interpretiert als:</p>
 * <p> 1. Vater, Kind 1</p>
 * <p> 2. Vater, Kind 2, Mutter</p>
 * <p> Somit hat Kind 1 keine Mutter und das wäre ja traurig.  </p>    
 * 
 * <p>verwendetet Libraries: xerces.jar sos.util.jar</p>
 * 
 * <p>TestProgramm lieg unter J:\E\java\apps\samples\sos\xmlparser\TestDOMParserXML.java.</p>
 * 
 * <p></p>
 *  
 */

import java.net.URL;
import java.util.*;

//import org.w3c.dom.Element;
//import org.w3c.dom.Element;
//import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.CDATASection;
import org.xml.sax.InputSource;
//import org.apache.xerces.parsers.DOMParser;
import sos.util.*;
import java.net.MalformedURLException;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class SOSDOMParserXML {

    /**
     * Attribut: Hier werden Tagnamen und Tagvalues füe einen insertStatement
     * gesammelt
     */
    private HashMap hashTag 				= new HashMap();

    /** Attribut: Alle insertStatement werden in eine Arraylist zusammengefaßt */
    private ArrayList insertStatement 		= new ArrayList();

    /** Liste alle Tags mit den gesammten XML-datei Tags */
    private ArrayList listOfTags 			= new ArrayList();

    /**
     * Liste für die HashTable. In der HashTable werden die Daten
     * zusammengehalten, die für einen Statement gesammelt wurden
     */
    private ArrayList listOfXMLPath 		= new ArrayList();
    
    /** 
     * Die Eintrag in die hashTabelle entspricht je ein Hashtabelle.
     * Die Hashtabelle hat alle Attribute von Tags.
     * Der Key der HashTabelle ist der tagname, die values eine HashTabelle
     * von Attributen
     */
    private HashMap listOfAttribut 			= new HashMap();

    /** SOSString Objekt */
    private SOSString sosString 			= null;

    /** Tabellenname, indem insertet werden soll */
    private String tableName 				= "tabellenname";

    /** Mappen der Tagnamen */
    private HashMap mappingTagNames 		= new HashMap();

    /** Vaterknoten werden nicht berücksichtigt. Default ist true */
    private boolean removeParents 			= true;

    /** Attribut: Beim bilden der insertStament können defaults übergeben werden */
    private HashMap defaultFields 			= new HashMap();
    
    /** Attribut: Es soll eine Ausgabe Script Datei erzeugt werden */
    private String outputScripFilename 		= "";

    private BufferedWriter output 			= null;
    
    /**
     * Attribut: Wenn counter gesetz ist, dann wird ein Zähler zu den Statement
     * generiert
     */
    private boolean counter 				= false;
    
    /** Attribut: Zähler, wenn counter=true ist, dann wird zu den Statement ein Zähler geschrieben */
    private int count 						= 0;

    /** SOSLOgger Object */
    private SOSLogger sosLogger 			= null;

    /** Attribut: encodin der XMl Datei */
    private String encoding 				= "";
    
    
    /** Konstruktor */    
    public SOSDOMParserXML(SOSLogger sosLogger_) throws Exception {
        try {
            sosLogger = sosLogger_;           
        } catch(Exception e) {
            throw new Exception ("\n ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    /**
     * Attribut: Key wird gebildet aus: Absoluter Pfad (99) oder nur mit einem
     * Vaterknoten (1) oder ohne die Angabe der Vaterknoten (0) (nur der
     * Tagname)
     */
    private int depth = 0;

    /**
     * Parsieren der XML-Datei.
     * Liefert true, wenn es erfolgreich abgearbeitet werden konnte. Sonst False,
     * @param bytesRead -> byte[] Objekt
     * @return boolean
     * @throws Exception        
     */    
    public boolean parse(byte[] bytesRead) throws Exception {        
    	return parse(null, null, bytesRead);                   
    }           
    
    /**
     * Parsieren der XML-Datei.
     * Liefert true, wenn es erfolgreich abgearbeitet werden konnte. Sonst False,
     * @param ipSource -> org.xml.sax.InputSource Objekt
     * @return boolean
     * @throws Exception
     */
    public boolean parse(InputSource ipSource) throws Exception {        
        return parse(null, ipSource, null);                   
    }    
    
    /**
     * Parsieren der XML-Datei.
     * Liefert true, wenn es erfolgreich abgearbeitet werden konnte. Sonst False,
     * @param fileName -> String
     * @return boolean
     * @throws Exception
     */    
    public boolean parse(String fileName) throws Exception {
        return parse(fileName, null, null);
    }
    
    
    
    /**
     * Parsieren der XML-Datei.
     * Liefert true, wenn es erfolgreich abgearbeitet werden konnte. Sonst False,
     * @param fileName -> String
     * @param ipSource -> InputSource
     * @return boolean
     * @throws Exception
     */  
    public boolean parse(String fileName, InputSource ipSource, byte[] bytesRead) throws Exception {
        try {
        	
        	//zum testen
        	DocumentBuilderFactory docBFac;
            DocumentBuilder docBuild;
            Document doc = null;
            docBFac = DocumentBuilderFactory.newInstance();                    
            docBuild = docBFac.newDocumentBuilder();                        
                        
            
            if (fileName != null) {            
            	doc = docBuild.parse(fileName);
            } else if (ipSource != null) {
            	//ipSource.getCharacterStream().mark(10000);
            	doc = docBuild.parse(ipSource);
            	//ipSource.getCharacterStream().reset();
            } else if (bytesRead != null) {
            	Reader reader = ((Reader)(new CharArrayReader(new String(bytesRead).toCharArray())));
    		    ipSource = new org.xml.sax.InputSource(reader);
    		    doc = docBuild.parse(ipSource);
    		    //parse(ipSource);
            }
            

            
            /*OutputFormat o = new OutputFormat(doc);
            encoding = o.getEncoding();*/
      	//ende test
            // Get an instance of the parser
            /*DOMParser parser = new DOMParser();                        

            // Parse the document.          
            if (fileName != null) {            
                parser.parse(fileName);
            } else if (ipSource != null) {
                parser.parse(ipSource);
            }            
           
            Document doc = parser.getDocument();
            */
            
            // Print document elements
            //System.out.print("The elements are: ");
            sosString = new SOSString();
            parseDocument(doc);
            writeInsertStatement();
            
//          Encoding bestimmen
            //leider funktioniert das reset und mark nicht, darum kann nur die encoding ausgelesen werden, die 
            //als nicht ipSource sind
            
            /*if (bytesRead != null) {
            	Reader readers = ((Reader)(new CharArrayReader(new String(bytesRead).toCharArray())));            	
            	ipSource = new org.xml.sax.InputSource(readers);
            }*/
                        
            sosLogger.debug4("encoding wurde aus der XML-Datei bestimmt: " + getEncoding(fileName, bytesRead));   
            
            return true;
        } catch (Exception e) {
            throw new Exception("\n -> ..error in "
                    + SOSClassUtil.getMethodName() + " " + e.toString());
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
    
    

    /**
     * Durchläuft das übergebene Document Object und schreibt beim
     * Arrayliste in geordneten Reihenfolge "tagname=tagwert". Der Tagname
     * kann durch den Eigenschaft 
     * depth=0 tagname=tagwert sein, oder
     * depth=1 Vaterknoten_@_tagname=tagwert sein, oder
     * depth=99 alle_Vaterknoten_@_tagname=tagwert sein.
     * @param doc -> Document
     * @throws Exception
     */
    public void parseDocument(Document doc) throws Exception {
        try {
//			"*" steht für root
            NodeList nl = doc.getElementsByTagName("*");
            Node n;
            String allParentname = "";
            String lastTagname = "";
//          Tagname mit Tagvalue soll beim Wiederholungen nicht ausgeschrieben werden.
            String lastTagnameAndValue = "";
//			Wiederholungen der lastTagnameAndValue zählen             
            int countOfRepeat = 1;
            String curTagnameAndValue = ""; 
            
            
            //NodeList Objeckt durchlaufen
            for (int i = 0; i < nl.getLength(); i++) {
                n = nl.item(i);
                if (lastTagname.equals(n.getParentNode().getNodeName())) {
                    //erst jetzt weiß ich, das der letzte Knoten ein Komplextyp
                    // war. Also wenn gewünscht, dann kann der Komplextyp jetzt
                    // gelöscht werden.
                    if (removeParents) {
                        listOfTags.remove(listOfTags.size() - 1);
                    }
                }                

                if (depth == 0) {
                    allParentname = n.getNodeName();
                } else if (depth == 99) {
                    allParentname = getParentName(n) + "_@_" + n.getNodeName();
                } else {
                    allParentname = n.getParentNode().getNodeName() + "_@_"
                            + n.getNodeName();
                }
                
                getAttributs(n);  
                
                curTagnameAndValue = allParentname + "=" + getChildText(n);
                if (curTagnameAndValue.equals(lastTagnameAndValue)){
                	countOfRepeat++;
                } else {                	 
                	if (countOfRepeat > 1) {
                		sosLogger.debug5("..count of repeat last Tagname and Tagvalue: " + countOfRepeat);
                	}
                	sosLogger.debug5(curTagnameAndValue);
                	countOfRepeat = 1;
                }
                listOfTags.add(curTagnameAndValue);
                lastTagnameAndValue = curTagnameAndValue;
                lastTagname = n.getNodeName();
            }
            if (countOfRepeat > 1) {
        		sosLogger.debug5("..count of repeat last Tagname and Tagvalue: " + countOfRepeat);
        	}
        } catch (Exception ec) {
            throw new Exception("\n -> ..error in "
                    + SOSClassUtil.getMethodName() + " " + ec);
        }
    }
    
    /**
     * Bestimmt alle Attribute zu den Node
     * @param n -> Node
     * @throws Exception
     */
    public void getAttributs(Node n) throws Exception {
        HashMap attr = new HashMap();
        try {
            NamedNodeMap nm = n.getAttributes();
            attr.put("tagname", n.getNodeName());
            for (int i =0; i < nm.getLength(); i++) {
                attr.put(nm.item(i).getNodeName(), nm.item(i).getNodeValue() );                
            }
            listOfAttribut.put(n.getNodeName(), attr);
        } catch (Exception ec) {
            throw new Exception("\n -> ..error in "
                    + SOSClassUtil.getMethodName() + " " + ec);
        }
    }
    /**
     * Es wird hier ein String gebildet mit der Namen aller Vaterknotennamen und
     * seine eigenen Knotennamen. Die Tiefe der Parentnamen ist durch depth
     * angegeben.
     * 
     * @param node -> Node Objekt
     * @return String 
     * @throws Exception
     */
    private String getParentName(Node node) throws Exception {
        String retVal = "";
        ArrayList parentName = new ArrayList();
        try {
            while (node.getParentNode() != null) {
                parentName.add(node.getParentNode().getNodeName());
                node = node.getParentNode();
            }

            for (int i = parentName.size() - 1; i != -1; i--) {
                retVal = retVal + parentName.get(i) + "_";
            }

            return retVal;
        } catch (Exception e) {
            throw new Exception("\n ->..error in "
                    + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    /**
     * listOfTags ist eine geordnete Liste mit allen Taginformationen. Alle Tags
     * werden in neue eine Hastable geschrieben. Existiert bereits ein Eintrag
     * in der HashTabel, dann wird eine insertStatement geschrieben und die
     * Inhalte der HashTable wird gelöscht. Die Liste wird aktualisiert, bis zu den
     * letzen sich wiederholenden Vaterknoten.
     * 
     * @throws Exception
     */
    protected void writeInsertStatement() throws Exception {
        String[] splitTag = null;
        String tags = "";
        try {
             for (int i = 0; i < listOfTags.size(); i++) {
             	tags = sosString.parseToString(listOfTags.get(i));
                splitTag = tags.split("=");
                if (hashTag.containsKey(splitTag[0])) {
                    writeStatement();
                    updatelistOfTags(splitTag[0], i);
                    i = -1;            
                } else {
                    if (splitTag.length == 2) {
                        hashTag.put(splitTag[0],tags.substring(tags.indexOf(splitTag[1])));
                    } else {
                        hashTag.put(splitTag[0], "NULL");
                    }
                }
            }
            writeStatement();//letze Statement bilden          
        } catch (Exception e) {
            throw new Exception("\n ->..error in "
                    + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    /**
     * Hier wird die Liste der Tags aktualisert. Aus der Liste listOfTags wird
     * ab der Parameter Tagname bis zur Position entfernt; Die Parametern geben
     * die Stellen an, in dem sich der Tag wiederholt vorkommt (Komplextyp)
     * 
     * @param tagname -> String
     * @param position -> int
     * @throws Exception
     */
    private void updatelistOfTags(String tagname, int position)
            throws Exception {
        String[] split = null;
        boolean breakOK = true;
        try {
            for (int i = 0; i < listOfTags.size() && breakOK; i++) {
                split = sosString.parseToString(listOfTags.get(i)).split("=");
                if (split[0].equals(tagname)) {
                    for (int j = i; j < position; j++) {
                        listOfTags.remove(i);
                        breakOK = false;
                    }
                }
            }
            //zum Testen
            /*for (int i = 0; i < listOfTags.size(); i++) {
                sosLogger.debug5("neue Liste: " + listOfTags.get(i));
            }*/
        } catch (Exception e) {
            throw new Exception("\n ->..error in "
                    + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    /**
     * Schreibt ein Insertstatement.
     * 
     * @throws Exception
     */
    private void writeStatement() throws Exception {

        try {
            HashMap currHash = new HashMap();
            Iterator keys = hashTag.keySet().iterator();
            Iterator vals = hashTag.values().iterator();
            String key = "";
            String val = "";
            String insStr = " insert into " + tableName + " ( ";
            String insStr2 = " values ( ";

            //defaults ausschreiben:
            Iterator dkeys = defaultFields.keySet().iterator();
            Iterator dvals = defaultFields.values().iterator();
            String dkey = "";
            String dval = "";
            while (dkeys.hasNext()) {
                //defaults schreiben
                dkey = sosString.parseToString(dkeys.next());
                dval = sosString.parseToString(dvals.next());
                insStr = insStr + dkey;
                insStr2 = insStr2 + "'" + dval + "'";
                if (dkeys.hasNext()) {
                    insStr = insStr + ", ";
                    insStr2 = insStr2 + ", ";
                }
                if (key != null && key.length() > 0)
                	currHash.put(key, val);
            }
            
            if (defaultFields.size() > 0 ){
                insStr = insStr + ", ";
                insStr2 = insStr2 + ", ";
            }
            
            if (this.isCounter()) {
                //eindeutigen Zähler einfügen
                insStr = insStr + " counter";
                insStr2 = insStr2 + " " + this.count++;
                if (key != null && key.length() > 0)
                	currHash.put(key, val);
                insStr = insStr + ", ";
                insStr2 = insStr2 + ", ";
            }
            
            while (keys.hasNext()) {
                key = sosString.parseToString(keys.next());
                val = sosString.parseToString(vals.next());
                if (mappingTagNames.containsKey(key)) {
                    key = sosString.parseToString(mappingTagNames.get(key));
                }
                //System.out.println(".. " + key + "=" + val);
                insStr = insStr + key;
                if (val.equals("NULL")) {
                    insStr2 = insStr2 +  val;
                } else {
                	insStr2 = insStr2 + "'" + val + "'";
                }
                if (keys.hasNext()) {
                    insStr = insStr + ", ";
                    insStr2 = insStr2 + ", ";
                }
                if (key != null && key.length() > 0)
                	currHash.put(key, val);
            }
            insStr = insStr + " ) " + insStr2 + " ) ";
            sosLogger.debug9(" InsertStament: " + insStr);
            if (output != null) {
                output.write(insStr + ";\n");
            }
            //Statements merken
            insertStatement.add(insStr);
            //HashTabelle in eine ArrayList schreiben.
            listOfXMLPath.add(currHash.clone());
            hashTag.clear();

        } catch (Exception e) {
            throw new Exception("\n ->..error in "
                    + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    /**
     * Liefert den Tagwert zu den Node Objekt
     * @param node -> Node 
     * @return String -> Tagwert 
     * @throws Exception
     */
    private String getChildText(Node node) throws Exception {
        NodeList list = node.getChildNodes();
        StringBuffer ret = new StringBuffer();
        //    -------------------------------------------------------------------
        try {
            if (list != null) {

                for (int i = 0; i < list.getLength(); i++) {
                    Node son = list.item(i);
                    if (son.getNodeType() == Node.TEXT_NODE) {
                        ret.append(son.getNodeValue().trim());
                    }
                    if (son.getNodeType() == Node.CDATA_SECTION_NODE) {
                        CDATASection cs = (CDATASection) son;
                        ret.append(cs.getData());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Error in getChildText : " + e);
        }

        return ret.toString();
    }

    
    
    ////////////////////////////////getter/setter//////////////////////////////
    /**
     * Liefert den Namen der Tabelle
     * @return String -> Returns the tableName.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Tabellenname, in die die Datensätze insertet werden soll
     * @param tableName_
     *            The tableName to set.
     */
    public void setTableName(String tableName_) {
        tableName = tableName_;
    }

    /**
     * Key/Feldname für den insertstatement wird gebildet aus: 
     * Absoluter Pfad (depth=99) oder 
     * nur mit einem Vaterknoten (depth=1) oder 
     * ohne die Angabe der Vaterknoten (depth=0) (nur der Tagname)
     * 
     * Default ist 0
     * 
     * @return int Returns the depth.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Key/Feldname für den insertstatement wird gebildet aus: 
     * Absoluter Pfad (depth=99) oder 
     * nur mit einem Vaterknoten (depth=1) oder 
     * ohne die Angabe der Vaterknoten (depth=0) (nur der Tagname)
     * 
     * @param depth_
     *            The depth to set.
     */
    public void setDepth(int depth_) {
        depth = depth_;
    }

    /**
     * Liefert die InsertStatement in einer ArrayListe.
     * Die Liste ist geordnet. 
     * 
     * @return ArrayList -> Returns the insertStatement.
     */
    public ArrayList getInsertStatement() {
        return insertStatement;
    }

    /**
     * Liefert eine HashTabelle, die den Tagname mappen soll.
     * Z.B. Wenn der key=name und der value=myname ist, dann wird beim insertstatement
     * schreiben der Tagname=name als myname geschrieben. 
     * Diese Methode ist notwendig, wenn die Tagname anders sein sollen als die 
     * Tabellenfeldnamen.
     * @return HashMap -> Returns the mappingTagNames.
     */
    public HashMap getMappingTagNames() {
        return mappingTagNames;
    }

    /**
     * Setzen eine HashTabelle, die den Tagname mappen soll.
     * Z.B. Wenn der key=name und der value=myname ist, dann wird beim insertstatement
     * schreiben der Tagname=name als myname geschrieben. 
     * Diese Methode ist notwendig, wenn die Tagname anders sein sollen als die 
     * Tabellenfeldnamen.
     * @param mappingTagNames
     *            The mappingTagNames to set.
     */
    public void setMappingTagNames(HashMap mappingTagNames) {
        this.mappingTagNames = mappingTagNames;
    }

    /**
     * Auslesen der Vaterknoten, ob diese mit berücksichtigt werden sollen.
     * Default ist true
     * 
     * @return boolean Returns the removeParents.
     */
    public boolean isRemoveParents() {
        return removeParents;
    }

    /**
     * Setzen der Vaterknoten, ob diese mit berücksichtigt werden sollen.
     * Default ist true
     * 
     * @param removeParents_
     *            The removeParents to set.
     */
    public void setRemoveParents(boolean removeParents_) {
        removeParents = removeParents_;
    }

    /**
     * Alle möglichen XML-Pfade wurden in die ArrayList geschrieben. Diese
     * ArrayList beiinhaltet Hashtabellen, dessen Key der tagname/gemapte Tagname
     * und value die Tagwert für einen Datensatz entspricht.
     * 
     * @return ArrayList -> Returns the listOfXMLPath.
     */
    public ArrayList getListOfXMLPath() {
        return listOfXMLPath;
    }

    /**
     * Helper method to create a URL from a file name
     * @return URL
     * @param fileName -> String
     */  
    private URL createURL(String fileName) throws Exception {
        URL url = null;
        try {
            url = new URL(fileName);
        } catch (MalformedURLException ex) {
            File f = new File(fileName);
            try {
                String path = f.getAbsolutePath();
                // This is a bunch of weird code that is required to
                // make a valid URL on the Windows platform, due
                // to inconsistencies in what getAbsolutePath returns.
                String fs = System.getProperty("file.separator");
                if (fs.length() == 1) {
                    char sep = fs.charAt(0);
                    if (sep != '/') path = path.replace(sep, '/');
                    if (path.charAt(0) != '/') path = '/' + path;
                }
                path = "file://" + path;
                url = new URL(path);
            } catch (MalformedURLException e) {
                throw new Exception("\n ..error in "
                        + SOSClassUtil.getMethodName()
                        + " Cannot create url for: " + fileName);

            }
        }
        return url;
    }

    /**
     * Auslesen der Hashtabelle, die mit in der insert-Stament stehen sollen.
     * Sinnvoll sind z.B. created, created_by etc.
     * 
     * @return HashMap -> Returns the defaultFields.
     */
    public HashMap getDefaultFields() {
        return defaultFields;
    }

    /**
     * Hier können Werte mitübergeben werden, die nicht in der XML-Datei sind,
     * aber mit in der insert-Stament stehen sollen
     * 
     * Sinnvoll sind z.B. created, created_by etc.
     * 
     * @param defaultFields
     *            The defaultFields to set.
     */
    public void setDefaultFields(HashMap defaultFields) {
        this.defaultFields = defaultFields;
    }

    /**
     * Auslesen, ob ein Zähler mitgeschrieben werden soll.
     * 
     * @return Returns the counter.
     */
    public boolean isCounter() {
        return counter;
    }

    /**
     * Soll ein Zähler für den Statement gebildet werden.
     * 
     * @param counter
     *            The counter to set.
     */
    public void setCounter(boolean counter) {
        this.counter = counter;
    }
    
    /**
     * Auslesen der Ausgabe Script Dateiname.
     * Die Ausgabescriptdateiname wird nur erzeugt, 
     * wenn auch Dateiname angegeben wurde.
     * 
     * @return String Returns the outputScripFilename.
     */
    public String getOutputScripFilename() {
        return outputScripFilename;
    }
    
    /**
     * Setzen der Ausgabe Script Dateiname.
     * Die Ausgabescriptdateiname wird nur erzeugt, wenn auch Dateiname angegeben wurde.
     * @param outputScripFilename The outputScripFilename to set.
     */
    public void setOutputScripFilename(String outputScripFilename) throws Exception {
        try {
        this.outputScripFilename = outputScripFilename;
        if (outputScripFilename != null && outputScripFilename.length() > 0) {                       
            output = new BufferedWriter((new FileWriter(outputScripFilename)));                                                                  
        }
        } catch (Exception e) {
            throw new Exception ("\n ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }
    /**
     * Diese Methode liefert den Wert der Property <i>listOfAttribut</i>. 
     * Der Typ der Property <i>listOfAttribut</i> ist ArrayList.
     * Ein Eintrag der ArrayList beiinhaltet eine Hashtabelle. Die Hastabelle
     * ist gefüllt mit Attributnamen=Attributwerten zu einem Tag.
     * 
     * @return ArrayList.
     */
    public HashMap getListOfAttribut() {
        return listOfAttribut;
    }
    
    /**
	 * Liefert die encoding.
	 * Der Typ ist String.  
	 *
	 * @return String
	 */
    public String getEncoding() {
    	return encoding;
    }
    
    /**
     * Bestimmt die Encoding
     * @param fileName -> String
     * @param bytesRead -> byte[]
     * @return String -> encoding
     * @throws Exception
     */
    //TODO: encoding wird hier anhand von String überprüft; Suchen nach anderen Methodiken
	//public String getEncoding(String fileName, InputSource ipSource) throws Exception{
    public String getEncoding(String fileName, byte[] bytesRead) throws Exception{
		String line = "";
		String[] split = null;
		BufferedReader f = null;
		InputSource ipSource = null; //TODO: wird zur Zeiit nicht supported
		try {
			
			if (fileName != null) {
				f = new BufferedReader( new FileReader( new File(fileName) ) ) ;				
			} else if (ipSource != null) {
				//Reader r = ipSource.getCharacterStream( );			
				//f = new BufferedReader(r);
				sosLogger.warn("..could not read the encoding for InputSource.");
				return "";
			} else if (bytesRead != null) {
	            	Reader readers = ((Reader)(new CharArrayReader(new String(bytesRead).toCharArray())));            	
	            	//ipSource = new org.xml.sax.InputSource(readers);
	            	f = new BufferedReader(readers);
	            
			}
			
			line = f.readLine();
			split = line.split(" ");
			for (int i = 0; i < split.length; i++) {
				if (split[i].indexOf("encoding") > -1) {
					encoding = split[i].substring(split[i].indexOf("\"") +1, split[i].lastIndexOf("\""));					
					return encoding;
				}
			}						
			return encoding;
			
						
			/*DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource src = new InputSource( new BufferedReader( new FileReader( new File("myfile.xml") ) ) );
			// Load the xml - how do I get the encoding?
			Document doc = db.parse( src );
			
			*/
			
			/*org.xml.sax.InputSource is = new InputSource( new FileInputStream( new File ("C:/temp/b.xml" ) ));
			return is.getEncoding();
			*/
			/*String bytesRead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> " +
			"<sos.hostware.Variables>" +
			"<variable name=\"name\" value=\"AAA\"/>	" +
			"<variable name=\"uservar\">" +
			"<sos.hostware.Variables>" +
			"<variable name=\"kto3\" value=\"Kto. 1070 320 003\"/>" +
			"<variable name=\"name\" value=\"MÃ¼ller\"/>" +
			"<variable name=\"aok_email\"/>" +
			"<variable name=\"aok_fr\" value=\"Freitag\"/>" +
			"<variable name=\"durchwahl\" value=\"(030) 86 47 90-24\"/>" +
			"<variable name=\"aok_fr_z\" value=\"8.00 - 16.00 Uhr\"/>" +
			"<variable name=\"aok_internet\"/><variable name=\"bearb_email\"/>" +
			"<variable name=\"bearb_vorname\"/>" +
			"<variable name=\"blz3\" value=\"BLZ  290 500 00\"/>" +
			"<variable name=\"Gemdarfaendern\"/>" +
			"<variable name=\"bearb_name\"/>" +
			"<variable name=\"aok_do\" value=\"Donnerstag\"/>" +
			"<variable name=\"bank1\" value=\"Die Sparkasse in Bremen\"/>" +
			"<variable name=\"ort\" value=\"Berlin\"/>" +
			"<variable name=\"queue\"/>" +
			"<variable name=\"strasse\" value=\"Badensche Str. 29\"/>" +
			"<variable name=\"telefon\" value=\"1234-567\"/>" +
			"<variable name=\"www\" value=\"http://www.sos-berlin.com\"/>" +
			"<variable name=\"aok_do_z\" value=\"8.00 - 18.00 Uhr\"/>" +
			"<variable name=\"aok_mo\" value=\"Montag bis Mittwoch\"/>" +
			"<variable name=\"aok_sa\" value=\"Sonnabend\"/>" +
			"<variable name=\"bank2\" value=\"Bremer Bank\"/>" +
			"<variable name=\"plz\" value=\"10715\"/>" +
			"<variable name=\"zeichen\" value=\"ft\"/>" +
			"<variable name=\"anrede\" value=\"Herr\"/>" +
			"<variable name=\"aok_mo_z\" value=\"8.00 - 17.00 Uhr\"/>" +
			"<variable name=\"aok_sa_z\" value=\"10.00 - 13.00 Uhr\"/>" +
			"<variable name=\"bank3\" value=\"Bremer Landesbank\"/>" +
			"<variable name=\"kto1\" value=\"Kto. 106 5002\"/>" +
			"<variable name=\"email\" value=\"info@sos-berlin.com\"/>" +
			"<variable name=\"kto2\" value=\"Kto. 1 000 111 00\"/>" +
			"<variable name=\"kurzwahl\" value=\"24\"/>" +
			"<variable name=\"vorname\" value=\"Fritz Ã¶ Ã¤ Ã– Ã„ Ãœ\"/>" +
			"<variable name=\"blz1\" value=\"BLZ  290 501 01\"/>" +
			"<variable name=\"ik_zeichen\" value=\"103119199\"/>" +
			"<variable name=\"telefax\" value=\"1234-999\"/>" +
			"<variable name=\"basedir\"/>" +
			"<variable name=\"blz2\" value=\"BLZ  290 800 10\"/>" +
			"</sos.hostware.Variables>" +
			"</variable>" +
			"<variable name=\"x\" value=\"XX\"/>" +
			"</sos.hostware.Variables>";
			
			
			Reader reader = ((Reader)(new CharArrayReader(bytesRead.toCharArray())));
			org.xml.sax.InputSource ipSource = new org.xml.sax.InputSource(reader);
			return ipSource.getEncoding();*/
			

		 } catch (Exception e) {
            throw new Exception ("\n ->..error in" + SOSClassUtil.getMethodName() + " " + e);
        } finally {
        	//f.reset();
			//f.close();
        }
		
		/*try {
		 java.io.File inputFile = new  java.io.File("C:/temp/a.xml");
		 java.io.FileInputStream in = new  java.io.FileInputStream(inputFile);
		 
		InputStreamReader incode = new InputStreamReader(in);
		
//		 Get the encoding that is in use
		System.out.println("Encoding: "+incode.getEncoding());
		return incode.getEncoding();
		 } catch (Exception e) {
            throw new Exception ("\n ->..error in" + SOSClassUtil.getMethodName() + " " + e);
        }*/
	}
    
//////////////////An hier zum testen der Klasse ////////////////////////////    
    
    public static void main(String[] args) {
    	try{
    		SOSStandardLogger sosLogger = new SOSStandardLogger(SOSStandardLogger.DEBUG9);
    		SOSString sosString = new SOSString();
    		
    		SOSDOMParserXML parser = new SOSDOMParserXML(sosLogger);
    		
    		//      depth=> 0=nur Tagname; 1=nur der Vaterknoten; 99=gesamte Pfad
    		parser.setDepth(0); 
    		
    		//		sollen Komplextyps in den insertStatement mit übernommen werden            
    		parser.setRemoveParents(true);
    		
    		//Tagnamen zu Columnnamen mappen            
    		//HashMap mappingTagNames = getTestMappingTagNames();                        
    		//parser.setMappingTagNames(mappingTagNames);
    		
    		//		Name der Tabelle, in die insertet werden soll            
    		//parser.setTableName("tableXYZ");
    		
    		//		Beim generieren der Statements werden diese Defaultwerte mitgeschrieben
    		//HashMap defaults = getTestDefaults();            
    		//parser.setDefaultFields(defaults);
    		
    		//		Soll ein eindeutiger Zähler für einen Statement generiert werden.             
    		//parser.setCounter(true);
    		
    		//		Name der Ausgabe Script Datei            
    		//parser.setOutputScripFilename("C:/temp/script.sql");
    		
    		
    		//parser.parse("C:/eclipse/workspace/sos.util/sos/util/testsicher.xml");
    		//parser.parse("J:/E/java/mo/xmlparser/src/test/Familien.xml"); 
    		//parser.parse("C:/backup/xmlparser/src/test/Familien3.xml");
    		//parser.parse("O:/scheduler/tmp/SUBITOaccounting_xml_SUBITO_9999000000076.xml");
    		parser.parse("J:/E/java/mo/sos.stacks/servingxml/samples/sos/resources/mapping.xml");
    		
    		
    		//////////Alle Attribut bestimmen
    		/*System.out.println("-------Ergebnisse-------------------------------------------------");
    		System.out.println("-------Alle Attribut auslesen:----------------------");
    		HashMap res = null;
    		HashMap att = parser.getListOfAttribut();
    		Iterator keys = att.keySet().iterator();
    		Iterator vals = att.values().iterator(); //neu 
    		while(keys.hasNext()) {
    			res = (HashMap)keys.next();
    			System.out.println("name =" + res.get("name"));
    			
    			//System.out.println("name =" + keys.next()); // neu res.get("name"));
    			//System.out.println("value =" + vals.next()); // neu res.get("value"));
    		}
    		*/
    		System.out.println("-------Ergebnisse-------------------------------------------------");
    		System.out.println("-------Alle Tag auslesen:----------------------");
    		HashMap res = null;
    		ArrayList tags = parser.getListOfXMLPath();
    		for (int i = 0; i < tags.size(); i++) {
    			res = (HashMap)tags.get(i);    			
    			Iterator keys = res.keySet().iterator();
        		Iterator vals = res.values().iterator(); //neu        		
        		while(keys.hasNext()) {        			
        			System.out.println(keys.next() + "=" + vals.next());        			        			
        		}
    		}
    	} catch (Exception e) {
    		System.out.println ("error: " + e);
    	}
    }
    /**
     * Mappen der Tagnamen zu Columnnamen
     * @return HashMap
     * @throws Exception
     */
    private static HashMap getTestMappingTagNames() throws Exception {
        try {
            HashMap mappingTagNames = new HashMap();
            //wenn setDepth(0) ist; sonst muß der Pfad auch angegeben werden
            mappingTagNames.put("Quantity", "myQuantity");
            mappingTagNames.put("StockLevelTimeStamp", "myStockLevelTimeStamp");
            mappingTagNames.put("VendorBatchNumber", "myVendorBatchNumber");
            mappingTagNames.put("Decimal", "myDecimal");
            mappingTagNames.put("OwningClient", "myOwningClient");
            
            //wenn depth = 99 ist, dann sieht der Pfad immer so aus:
            // #document_[alle vaterknoten getrennt mit "_"]__@_[knotenname]
            
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch_BatchStatus__@_StockUsage", "myStockUsage");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2__@_ReportingClient", "#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2__@_ReportingClient");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_DemandID", "myDemandID");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item__@_UnitOfMeasurement", "myUnitOfMeasurement");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_SystemID","mySystemId");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport__@_StockLevelTimeStamp","myStockLevelTimeStamp");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item__@_ItemType","ItemType");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_Decimal","myDecimal");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item__@_LocalItemNumber","myLocalItemNumber");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch__@_VendorBatchNumber","myVendorBatchNumber");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_TimeZone","myTimeZone");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch_BatchStatus__@_Quantity","myQuantity");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch_BatchStatus__@_QualityControlStatus","myQualityControlStatus");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_DeliveryNumber","myDeliveryNumber");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch__@_InventoryBatchNumber","myInventoryBatchNumber");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_CreationTimeStamp","myCreationTimeStamp");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch__@_OwningClient","myOwningClient");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch__@_PackagingBatchNumber","myPackagingBatchNumber");
            return	mappingTagNames;
        } catch (Exception e) {
            throw new Exception ("\n ->..error in" + SOSClassUtil.getMethodName() + " " + e);
        }
    }
    
    /**
     * Defaultwerte zu einem Statement generieren
     * @return HashMap
     * @throws Exception
     */
    private static HashMap getTestDefaults() throws Exception {
        try {
            
            HashMap defaults = new HashMap();
            defaults.put("created", "24.06.2004");
            defaults.put("createdBy", "mo");
            return	defaults;
        } catch (Exception e) {
            throw new Exception ("\n ->..error in" + SOSClassUtil.getMethodName() + " " + e);
        }
    }	
	
	
}