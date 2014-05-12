package sos.xml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import sos.connection.SOSConnection;
import sos.util.SOSClassUtil;
import sos.util.SOSStandardLogger;
import sos.util.SOSString;

/**   
 * <p>
 * Title: WriteXML
 * </p>
 * <p>
 * Description: Erzeugt eine XML-Datei. Die Struktur der XML-Datei ist in der
 * Datenbank abgebildet. Die XML-Datei wird anhand dieser Struktur erzeugt
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: SOS-Berlin GmbH
 * </p>
 * 
 * resource: ojdbc.jar; sos.connection.jar; sos.util.jar
 * 
 * @author Mueruevet Oeksuez
 * @version 1.0
 */
public class SOSWriteXML {

    /** Connection */
    private static SOSConnection conn = null;

    /** Model */
    private int content_model_id;

    /** BufferWriter */
    private BufferedWriter output = null;

    /** Content_id */
    private String outbound_content_id;

    /** Alle Tagnamen zu den Spaltennamen */
    private HashMap allColumnNamefromTagname = new HashMap();

    /** Alle Spaltennamen zu den Tagnamen */
    private HashMap allTagnamefromColumnName = new HashMap();

    /** Alle Tag typen zu den Tagnamen */
    private HashMap allTagTypeForTagName = new HashMap();

    /** Alle Minoccurs werte für Tagname */
    private HashMap allMinOccurForTagName = new HashMap();

    /** Name der Tabelle, der exportiert werden soll */
    private String tableExport;

    /**
     * es wird genau einmal der Technical Information geschrieben, wenn der
     * Parent gleich contentElementIdFromTI ist
     */
    private int contentElementIdFromTI;

    /** Namespace */
    private String nameSpace = "";

    /**
     * Hilfsvariable für den Namespace, damit er genau an der richtigen Position
     * geschrieben wird
     */
    private boolean bNameSpace = true;

    /** Name + Pfad der zu erzeugende XML-Datei */
    private String fileName = new String();

    /** Attribut: SOSStandardLogger Objekt */
    private static SOSStandardLogger sosLogger;

    private HashMap allChildrenFromParent = new HashMap();

    /** Anzahl der Stufe der indexes, die erzeugt werden sollen */
    private int countOfIndex = 2;
    
    /** Attribut: SOSString Objekt */
    private SOSString sosString = null;
    
    /** Attribut: beinhaltet den Startknoten: tagname und content_element_id */
    private HashMap startTag = null;
    
    /** Soll die XML-Datei gegen das Schema valisiert werden*/
    private boolean validate = true;
    

    /**
     * Erzeugen einer XML-Datei. Die information werden hier aus der Datenbank
     * geholt.
     * 
     * @param conn_
     * @param content_model_id_
     * @param tableExport_
     * @param outbound_content_id_
     * @param fileName
     * @throws Exception
     */
    public SOSWriteXML(SOSConnection conn_, int content_model_id_,
            String tableExport_, String outbound_content_id_, String fileName_,
            SOSStandardLogger sosLogger_) throws Exception {
        try {
            conn = conn_;
            content_model_id = content_model_id_;
            tableExport = tableExport_;
            outbound_content_id = outbound_content_id_;
            fileName = fileName_;
            sosLogger = sosLogger_;

            init(true);

        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    /**
     * Erzeugen einer XML-Datei. Die information werden als Paramtern über setMethdoden übergeben.
     * übergeben werden müssen Parameter über die Methoden:
     * 
     * 1. setAllColumnNamefromTagname(HashMap allColumnNamefromTagname)   
     * 2. setNameSpace(String nameSpace) 
     * 3. setAllMinOccurForTagName(HashMap allMinOccurForTagName)
     * 4. setAllTagnamefromColumnName(HashMap allTagnamefromColumnName) 
     * 5. setAllTagTypeForTagName(HashMap allTagTypeForTagName)
     * 
     * Siehe ein Beispiel in der TestWriteXML.java
     * 
     * @param conn_
     * @param content_model_id_
     * @param tableExport_
     * @param outbound_content_id_
     * @param fileName
     * @throws Exception
     */
    public SOSWriteXML(SOSConnection conn_, String tableExport_,  String outbound_content_id_,
            String fileName_,
            SOSStandardLogger sosLogger_) throws Exception {
        try {
            conn = conn_;
            tableExport = tableExport_;
            outbound_content_id = outbound_content_id_;
            fileName = fileName_;
            sosLogger = sosLogger_;            

        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }
    
    public void write() throws Exception{
        try {
            init(false);
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }
    /**
     * @param withDBSelection
     * @throws Exception
     */
    private void init(boolean withDBSelection) throws Exception {
        try {
            sosLogger.info("Creating XML-File.. ");
            try {
                output = new BufferedWriter((new FileWriter(fileName)));
            } catch (IOException e) {
                throw new Exception(
                        "Error while creating Bufferwriter in WriteXML: " + e);
            }
            
            sosString = new SOSString();
            
            //Alle informationen stehen in der Datenbank
            if (withDBSelection) {
                getAllColumnNameFromTagName();
                getContentElementIdFromTI();
                getAllParentsChildren();
                getNameSpace();
                createIndex();
                startWriteXML(false);
            } else {
                startWriteXML(true);
            }
            output.close();
            sosLogger.info("XML-File successfully created");

            //Validierung der XML-Datei gegen das XSD Schema
            try {
                if (validate) {
                SOSXMLValidator.validate(fileName);
                }
            } catch (Exception e) {
                throw new Exception("XML-File: " + fileName + " is not valid "
                        + e);
            }

        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }

    }

    /**
     * Ermitteln der Namespace
     * 
     * @throws Exception
     */
    private void getNameSpace() throws Exception {
        String selst  = null;
        try {
            selst = "SELECT \"NAME_SPACE\" FROM CONTENT_MODELS "
                    + " WHERE \"CONTENT_MODEL_ID\" =  " + content_model_id;            
            
            nameSpace = conn.getSingleValue(selst);
            System.out.println("----------namespace-----------: " + nameSpace);
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e + selst);
        }
    }

    /**
     * Ermitteln der Startknoten
     * 
     * @throws Exception
     */
    public void startWriteXML(boolean hasStartTag) throws Exception {
        String selst = "";
        try {
            if (!hasStartTag) {
                selst = "SELECT \"TAG_NAME\", \"CONTENT_ELEMENT_ID\"  FROM CONTENT_TAGS "
                    + " WHERE \"CONTENT_MODEL_ID\" = "
                    + content_model_id
                    + " AND \"CONTENT_ID\" = '"
                    + outbound_content_id
                    + "'"
                    + " AND \"PARENT\" = 0 ORDER BY \"CONTENT_ELEMENT_ORDER\" ";
                startTag = conn.getSingle(selst);
            }
            if (startTag.get("content_element_id") != null) {
                output.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                writeXML(Integer.parseInt(sosString.parseToString(startTag,"content_element_id")), 
                        sosString.parseToString(startTag, "tag_name"), 
                        new HashMap());
             } else {
                 sosLogger.info("Es wurden keine Startknoten ermittelt.");
             }
            //stmt.close();
            //rset.close();
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    /**
     * Ermitteln der Contentelementid für den Technical Information
     * 
     * @throws Exception
     */
    private void getContentElementIdFromTI() throws Exception {
        String sc = null;
        try {
            String selst = "SELECT \"PARENT\" FROM CONTENT_TAGS "
                    + " WHERE \"CONTENT_MODEL_ID\" =  " + content_model_id
                    + " AND \"CONTENT_ID\" = 'T' "
                    + " AND upper(\"TAG_NAME\") = 'TECHNICALINFORMATION'";
            /*
             * Statement stmt = conn.createStatement(); ResultSet rset =
             * stmt.executeQuery(selst); if (rset.next()) {
             * contentElementIdFromTI = rset.getInt("PARENT"); } stmt.close();
             * rset.close();
             */
            sc = conn.getSingleValue(selst);
            if (sc != null && sc.length() > 0) {
                contentElementIdFromTI = Integer.parseInt(conn.getSingleValue(selst));
            }
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    /**
     * Erzeugen der Technical Information
     * 
     * @throws Exception
     */
    private void writeHeader() throws Exception {
        HashMap rset = null;
        try {
            String selst = "SELECT \"SYSTEM_ID\", \"TIME_ZONE\", replace(to_char(\"CREATION_TIMESTAMP\",'YYYY-MM-DD HH24:MI:SS'),' ','T') as \"CREATION_TIMESTAMP\", \"DELIVERY_NUMBER\" "
                    + " FROM " + tableExport + " WHERE \"CONTENT_ID\" = 'T'";
            //Statement stmt = conn.createStatement();
            //ResultSet rset = stmt.executeQuery(selst);
            //if (rset.next()) {
            rset = conn.getSingle(selst);
                String ti = "<TechnicalInformation>" +
                		"<SystemID>" + sosString.parseToString(rset,"system_id") + "</SystemID>";
                ti = ti + "<InterfaceID />  <DemandID />"
                    + "<Decimal>.</Decimal> ";
                ti = ti + "<CreationTimeStamp>" + sosString.parseToString(rset,"creation_timestamp") + "</CreationTimeStamp>";
                ti = ti + "<TimeZone>" + sosString.parseToString(rset,"time_zone") + "</TimeZone>";
                ti = ti + "<XSDVersionNumber />"; 
                ti = ti + "<DeliveryNumber>" + sosString.parseToString(rset,"delivery_number")+"</DeliveryNumber>" + "</TechnicalInformation>";
            output.write(ti);
        } catch (Exception e) { throw
            new Exception("\n -> ..error in " +
            SOSClassUtil.getMethodName() + " " + e); } 
       }
                
            
    private void getAllParentsChildren() throws Exception {
        String selst = "";
        HashMap childHash = new HashMap();
        ArrayList childList = new ArrayList();
        ArrayList aset = null;
        HashMap rset = null;
        try {
            selst = " SELECT \"TAG_NAME\", " + " \"TAG_TYPE\", "
                    + " \"GROUPABLE\", " + " \"PARENT\", "
                    + " \"CONTENT_ELEMENT_ID\" " + " FROM CONTENT_TAGS "
                    + " WHERE \"CONTENT_MODEL_ID\" = " + content_model_id
                    + " AND \"CONTENT_ID\" = '" + outbound_content_id + "'" +                    
                    " ORDER BY \"CONTENT_ELEMENT_ORDER\" ";
            
            String lastParent = "";
            
            aset = conn.getArray(selst);
            
            for (int i = 0; i < aset.size(); i++) {
                rset = (HashMap)aset.get(i);
                if (!(lastParent.equals(rset.get("parent")))
                        && !lastParent.equals("0")
                        && lastParent.trim().length() > 0) {
                    allChildrenFromParent.put(lastParent, childList);
                    System.out.println("vergesseb allChildrenFromParent.put(\"" + lastParent + "\", childList);");
                    childList = new ArrayList(); //reinit
                    System.out.println("childList = new ArrayList();");
                }
                childHash = new HashMap();
                childHash.put("TAG_NAME", rset.get("tag_name"));
                childHash.put("TAG_TYPE", rset.get("tag_type"));
                childHash.put("GROUPABLE", rset.get("groupable"));
                childHash.put("PARENT", rset.get("parent"));
                childHash.put("CONTENT_ELEMENT_ID", rset.get("content_element_id"));
                
                System.out.println("--------------getAllParentsChildren-----------------------");
                System.out.println("childHash.put(\"" + "TAG_NAME" + "\", \""+ rset.get("tag_name") + "\");");
                System.out.println("childHash.put(\"" +" TAG_TYPE" + "\", \""+rset.get("tag_type") + "\");");
                System.out.println("childHash.put(\"" +"GROUPABLE" + "\", \""+rset.get("groupable") + "\");");
                System.out.println("childHash.put(\"" +"PARENT" + "\", \""+rset.get("parent") + "\");");
                System.out.println("childHash.put(\"" +"CONTENT_ELEMENT_ID" +"\", \""+ rset.get("content_element_id") + "\");");
                
                
                childList.add(childHash);
                System.out.println("childList.add(childHash);");
                if (sosString.parseToString(rset,"parent").equals("0")) { // sonderfall. 1.
                    // element: es gibt
                    // nur einen parent
                    // = 0
                    allChildrenFromParent.put(rset.get("parent"), childList);
                    System.out.println("allChildrenFromParent.put(\"" + rset.get("parent") + "\", childList);");
                    childList = new ArrayList();
                    System.out.println("childList = new ArrayList();");
                }

                if (childList.size() > 0) { //sonderfall: letzte element
                    lastParent = sosString.parseToString(rset.get("parent"));
                }
            }
            allChildrenFromParent.put(lastParent, childList);
            System.out.println("allChildrenFromParent.put(\"" + lastParent + "\", childList);");
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    /**
     * Schreiben der XML-Datei
     * 
     * @param parent
     * @param parentTagName
     * @param groupKeys
     * @throws Exception
     */
    public void writeXML(int parent, String parentTagName, HashMap groupKeys)
            throws Exception {

        HashMap complexChildren = new HashMap();
        ArrayList atomarChildren = new ArrayList();
        HashMap groupableChildren = new HashMap();
        //String selst = new String();
        //Statement stmt2;
        //ResultSet rset2;
        String selVal = "";
        try {

            boolean group = false; //vermeiden von Filtern der Datensätze durch
            // Gruppierungen. Wenn groubable = 2 dann
            // soll der outbound_record_id mit zu groub
            // by element gehören
            HashMap childrens = new HashMap();
            ArrayList parentsChildren = new ArrayList();
            if (allChildrenFromParent.get(String.valueOf(parent)) != null) {
                parentsChildren = (ArrayList) allChildrenFromParent.get(String
                        .valueOf(parent));
                for (int i = 0; i < parentsChildren.size(); i++) {
                    childrens = (HashMap) parentsChildren.get(i);
                    String tagName = getValues(childrens, "TAG_NAME");
                    String contentElementId = getValues(childrens,
                            "CONTENT_ELEMENT_ID");
                    String colName = "";
                    //test
                    /*Iterator keys = allColumnNamefromTagname.keySet().iterator();
                    Iterator vals = allColumnNamefromTagname.values().iterator();
                    while (keys.hasNext()) {
                    	System.out.println(keys.next() + " = " + vals.next()); 
                    }*/
                    //ende test
                    if (allColumnNamefromTagname.get(tagName) != null) {
                        colName = getValues(allColumnNamefromTagname, tagName);
                    }
                    int groupable = Integer.parseInt(getValues(childrens,
                            "GROUPABLE"));
                    if (getValues(childrens, "TAG_TYPE").equals("0")) {
                        complexChildren.put(tagName, contentElementId);
                    } else {
                        atomarChildren.add(colName);
                        if (groupable == 1) {
                            groupableChildren.put(colName, contentElementId);
                        } else if (groupable == 0) {
                            group = true;
                        }
                    }
                }
            }
            if (atomarChildren.size() > 0) {

                //select Statement, wenn atomare Kinder existieren
                if (allTagTypeForTagName.get(atomarChildren.get(0)) != null
                        && (allTagTypeForTagName.get(atomarChildren.get(0))
                                .equals("3") || allTagTypeForTagName.get(
                                atomarChildren.get(0)).equals("5"))) { //Tag
                    // ist
                    // ein
                    // datum,
                    // formatieren
                    selVal = selVal + "select to_char(" + atomarChildren.get(0)
                            + ",'YYYY-MM-DD HH24:MI:SS')";
                } else {
                    selVal = selVal + "select " + atomarChildren.get(0);
                }
                for (int i = 1; i < atomarChildren.size(); i++) {
                    if (allTagTypeForTagName.get(atomarChildren.get(0)) != null
                            && (allTagTypeForTagName.get(atomarChildren.get(0))
                                    .equals("3") || allTagTypeForTagName.get(
                                    atomarChildren.get(0)).equals("5"))) {
                        selVal = selVal + ", to_char(" + atomarChildren.get(i)
                                + ",'YYYY-MM-DD HH24:MI:SS')";
                    } else {
                        selVal = selVal + ", " + atomarChildren.get(i);
                    }
                }
                selVal = selVal + " from " + tableExport
                        + " where \"CONTENT_ID\" = '" + outbound_content_id
                        + "' ";
                for (int i = 0; i < groupKeys.keySet().toArray().length; i++) {
                    //if (i == 0) {
                    selVal = selVal + " and " + groupKeys.keySet().toArray()[i];
                    if (groupKeys.values().toArray()[i] == null && sosString.parseToString(groupKeys.values().toArray()[i]).replaceAll("'","").length() == 0)
                        selVal = selVal + " is null ";
                    else
                        selVal = selVal + " = "
                                + groupKeys.values().toArray()[i];
                }
                if (!group) {
                    for (int i = 0; i < atomarChildren.size(); i++) {
                        if (i == 0) {
                            selVal = selVal + " group by "
                                    + atomarChildren.get(i);
                        } else {
                            selVal = selVal + ", " + atomarChildren.get(i);
                        }
                    }
                } else {
                	sosLogger.debug5("test : " + selVal);
                }
                sosLogger.debug5("writexml query : " + selVal);
                //System.out.println("writexml query : " + selVal);
                
                ArrayList a = null;               //TODO: Bessere Variabelennamen aussuchen 
                HashMap rset2 = null;
                a = conn.getArray(selVal);
                //while (rset2.next()) {
                for (int k = 0; k < a.size(); k++) {
                    rset2 = (HashMap)a.get(k);
                    output.write("<" + parentTagName + ">");
                    HashMap localGroupKeys = (HashMap) groupKeys.clone();
                    for (int i = 0; i < atomarChildren.size(); i++) {
                        String columnName = atomarChildren.get(i).toString();
                        String t = (String) allTagnamefromColumnName.get(columnName);
                        //Es darf nichts geschrieben werden, wenn es sich um
                        // leeren datum handelt. Sonst Fehler in Validierung
                        if ((rset2.get(columnName.toLowerCase()) == null ||
                                ((rset2.get(columnName.toLowerCase()) != null) && rset2.get(columnName.toLowerCase()).toString().length() > 0))
                                && ((allTagTypeForTagName.get(t).equals("3")) || allTagTypeForTagName.get(t).equals("5"))) {
                            //System.out.println("tu nichts leerer datum");
                        //} else if ((rset2.get(columnName) == null || sosString.parseToString(rset2,columnName.toLowerCase()).trim().length() == 0)
                        } else if ((
                                rset2.get(columnName.toLowerCase()) == null || 
                                (rset2.get(columnName.toLowerCase()) != null && rset2.get(columnName.toLowerCase()).toString().length() == 0))
                                && allMinOccurForTagName.get(t).equals("0")) {//Es
                            // darf nichts geschrieben werden, wenn es sich um leeren Wert und tag minoccur = 0 ist
                            //System.out.println("tu nichts minoccur = 0 und
                            // tagwert = '" + rset2.getString(columnName) + "'
                            // für tagname = " + t);
                        } else {
                            output.write("<"
                                    + allTagnamefromColumnName.get(columnName)
                                    + ">");
                            if (rset2.get(columnName.toLowerCase()) != null) {
                                if (allTagTypeForTagName.get(t).equals("3")) {
                                    String da = sosString.parseToString(rset2, columnName.toLowerCase());
                                    output.write(da.replace(' ', 'T')
                                            .substring(0, da.indexOf(".")));
                                } else if (allTagTypeForTagName.get(t).equals("5")) {
                                    String da = sosString.parseToString(rset2, columnName.toLowerCase());
                                    output.write(da.substring(0, da.indexOf(" ")));
                                } else {
                                    String currStr = sosString.parseToString(rset2, columnName.toLowerCase());
                                    //Sonderzeichen (& --> &amp;) (' -->
                                    // &apos;) (> --> &gt;) (< --> &lt;) (" -->
                                    // &quot;)
                                    currStr = currStr.replaceAll("&", "&amp;");
                                    currStr = currStr.replaceAll("'", "&apos;");
                                    currStr = currStr.replaceAll(">", "&gt;");
                                    currStr = currStr.replaceAll("<", "&lt;");
                                    currStr = currStr
                                            .replaceAll("\"", "&quot;");
                                    output.write(currStr);
                                }
                            }
                            output.write("</"
                                    + allTagnamefromColumnName.get(columnName)
                                    + ">");
                        }
                        if (groupableChildren.containsKey(columnName)) {
                            Object tag = this.allTagnamefromColumnName.get(columnName);                            
                            
                            String col = null;
                            if (rset2.get(columnName.toLowerCase()) != null && sosString.parseToString(rset2.get(columnName.toLowerCase())).length()  > 0) {
                            	col = sosString.parseToString(rset2.get(columnName.toLowerCase()));
                            }
                             rset2.get(columnName.toLowerCase());
                            if (allTagTypeForTagName.get(tag) != null
                                    && (allTagTypeForTagName.get(tag).equals("3") || allTagTypeForTagName.get(tag).equals("5"))) {
                                if (col == null) {
                                    localGroupKeys.put(columnName, col);
                                } else {
                                    /*
                                     * localGroupKeys.put("to_char(" +
                                     * columnName + ",'YYYY-MM-DD HH24:MI:SS')",
                                     * "to_char('" + col.substring(0,
                                     * col.indexOf(".")) + "')");
                                     */
                                     if (col !=null)
                                    	localGroupKeys.put(columnName, "to_date('" + col.substring(0, col.indexOf("."))+ "')");

                                }
                            } else {
                                if (col == null) {
                                    localGroupKeys.put(columnName, null);
                                } else {
                                    localGroupKeys.put(columnName, "'"
                                            + sosString.parseToString(rset2, columnName.toLowerCase())
                                                    .replaceAll("'", "''")
                                            + "'");//quoten von '
                                }
                            }
                        }
                    }
                    for (int i = 0; i < complexChildren.keySet().toArray().length; i++) {
                        String tag = complexChildren.keySet().toArray()[i]
                                .toString();
                        writeXML(Integer.parseInt(complexChildren.values().toArray()[i].toString()), tag, localGroupKeys);
                        
                    }
                    output.write("</" + parentTagName + ">");
                }
            
            } else {
                for (int i = 0; i < complexChildren.keySet().toArray().length; i++) {
                    String tag = complexChildren.keySet().toArray()[i]
                            .toString();
                    if (bNameSpace) {
                        output.write("<" + parentTagName + " " + nameSpace
                                + ">");
                        bNameSpace = false;
                    } else {
                        output.write("<" + parentTagName + ">");
                    }

                    // einmal die Technical Information schreiben
                    if (contentElementIdFromTI == parent) {
                        writeHeader();
                    }

                    writeXML(Integer.parseInt(complexChildren.values()
                            .toArray()[i].toString()), tag, groupKeys);
                    output.write("</" + parentTagName + ">");
                }
            }
        } catch (SQLException e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e + " Statement call: "
                    + selVal);
        }
    }

    /**
     * Holt alle moeglichen Content_Element_Id zu allen Tagnamen
     * 
     * @return String
     */
    public void getAllColumnNameFromTagName() throws Exception {
        ArrayList list = null;
        HashMap result = null;
        try {
            String selst = " SELECT \"COLUMN_NAME\"" + " , \"TAG_NAME\""
                    + " , \"MIN_OCCURS\"" + " , \"TAG_TYPE\""
                    + " FROM CONTENT_TAGS a, CONTENT_COLUMNS b"
                    + " WHERE a.\"COLUMN_ID\" = b.\"COLUMN_ID\" "
                    + " and a.\"CONTENT_MODEL_ID\" = " + content_model_id
                    + " and a.\"CONTENT_ID\" = '" + outbound_content_id + "'";
            list = conn.getArray(selst);
            
            for (int i = 0; i < list.size(); i++) {
                result = (HashMap)list.get(i);
                allMinOccurForTagName.put(result.get("tag_name"), result.get("min_occurs"));
                allColumnNamefromTagname.put(result.get("tag_name"), sosString.parseToString(result.get("column_name")).toLowerCase());
                allTagnamefromColumnName.put(sosString.parseToString(result.get("column_name")).toLowerCase(), result.get("tag_name"));
                allTagTypeForTagName.put(result.get("tag_name"), String.valueOf(result.get("tag_type")));
                
                System.out.println("-------------getAllColumnNameFromTagName----------------------");
                System.out.println("allMinOccurForTagName.put(\"" + result.get("tag_name") + "\", \"" +  result.get("min_occurs") + "\")");
                System.out.println("allColumnNamefromTagname.put(\""+result.get("tag_name") + "\", \"" +  result.get("column_name")+ "\")");
                System.out.println("allTagnamefromColumnName.put(\"" +result.get("column_name") + "\", \"" +  result.get("tag_name")+ "\")");
                System.out.println("allTagTypeForTagName.put(\"" + result.get("tag_name") + "\", \"" +  result.get("tag_type")+ "\")");
            }
            
        } catch (SQLException e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    /**
     * Liefert den Wert zu den Key. Wenn Key keine Wert hat(value = null) dann
     * wird ein leer string übergeben.
     * 
     * @param hash
     * @param key
     * @return
     */
    private String getValues(HashMap hash, String key) {
        if (hash.get(key) == null) {
            return "";
        } else {
            return hash.get(key).toString();
        }
    }

    /**
     * Erzeugt n-mal indexes (countOfIndex). Als erstes werden die Indexes
     * ermittelt.
     *  
     */
    private void createIndex() throws Exception {
        ArrayList columnsList = new ArrayList();
        String createIndex = "";
        Statement stmt = null;
//        HashMap rset = null;
        String iTableName = tableExport;
        try {
            if (tableExport.length() > 8) {
                iTableName = tableExport.substring(0, 8);
            }
            columnsList = getColumns2Index();
            for (int i = 0; i < columnsList.size(); i++) {
                createIndex = " CREATE INDEX " + iTableName + "_" + i + " ON "
                        + tableExport + "( " + columnsList.get(i) + ")";
                sosLogger.debug("createIndex: " + createIndex);
                try {
                    //stmt = conn.createStatement();
                    conn.executeQuery(createIndex);
                } catch (Exception e) {
                    //System.out.println("Error in WriteXML.createIndex. 1
                    // Index " + iTableName + " could not created. " + e);
                    sosLogger.debug("Warning in WriteXML.createIndex. Index "
                            + iTableName + " could not created. " + e);
                }
            }
            
        } catch (Exception e) {
            //System.out.println("Error in WriteXML.createIndex. 2. Index " +
            // iTableName + " could not created. " + e);
            sosLogger.debug("Error in WriteXML.createIndex. Index "
                    + iTableName + " could not created. " + e);
        }
    }

    private ArrayList getColumns2Index() throws Exception {
        int count = countOfIndex;
        ArrayList retVal = new ArrayList();
        String selst = "";
        String index = "";
        
        ArrayList list = null;
        HashMap rset = null;
        try {
            selst = " SELECT \"TAG_NAME\"" + " , \"LEAF\" "
                    + " FROM CONTENT_TAGS a "
                    + " WHERE a.\"CONTENT_MODEL_ID\" = " + content_model_id
                    + " and a.\"CONTENT_ID\" = '" + outbound_content_id + "'"
                    + " order by a.\"CONTENT_ELEMENT_ORDER\"";
            //Statement stmt = conn.createStatement();
            //ResultSet rset = stmt.executeQuery(selst);
            list = conn.getArray(selst);
            //while (rset.next()) {
            for (int i =0; i < list.size(); i++) {
                rset = (HashMap)list.get(i);
                if (count == 0) {                    
                    return retVal;
                }
                if (rset.get("leaf").equals("1")) {
                    if (index.length() > 1) {
                        index = index + ", ";
                    }
                    index = index + getValues(allColumnNamefromTagname, sosString.parseToString(rset.get("tag_name")));
                } else if (index.trim().length() > 0) {
                    retVal.add(index);
                    index = "";
                    count--;
                }
            }
            
            return retVal;
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    /**
     * @param allColumnNamefromTagname The allColumnNamefromTagname to set.
     */
    public void setAllColumnNamefromTagname(HashMap allColumnNamefromTagname) {
        this.allColumnNamefromTagname = allColumnNamefromTagname;
    }
    /**
     * @param nameSpace The nameSpace to set.
     */
    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }
 
    /**
     * @param allMinOccurForTagName The allMinOccurForTagName to set.
     */
    public void setAllMinOccurForTagName(HashMap allMinOccurForTagName) {
        this.allMinOccurForTagName = allMinOccurForTagName;
    }
    /**
     * @param allTagnamefromColumnName The allTagnamefromColumnName to set.
     */
    public void setAllTagnamefromColumnName(HashMap allTagnamefromColumnName) {
        this.allTagnamefromColumnName = allTagnamefromColumnName;
    }
    /**
     * @param allTagTypeForTagName The allTagTypeForTagName to set.
     */
    public void setAllTagTypeForTagName(HashMap allTagTypeForTagName) {
        this.allTagTypeForTagName = allTagTypeForTagName;        
    }
    /**
     * @param allChildrenFromParent The allChildrenFromParent to set.
     */
    public void setAllChildrenFromParent(HashMap allChildrenFromParent) {
        this.allChildrenFromParent = allChildrenFromParent;
    }
    /**
     * @return Returns the startTag.
     */
    public HashMap getStartTag() {
        return startTag;
    }
    /**
     * @param startTag The startTag to set.
     */
    public void setStartTag(HashMap startTag) {
        this.startTag = startTag;
    }
    /**
     * Soll die XML-Datei gegen das Schema valisiert werden
     * @return Returns the validate.
     */
    public boolean isValidate() {
        return validate;
    }
    /**
     * Soll die XML-Datei gegen das Schema valisiert werden
     * @param validate The validate to set.
     */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }
}