package sos.xml;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import sos.connection.SOSConnection;
import sos.util.SOSClassUtil;
import sos.util.SOSStandardLogger;
import sos.util.SOSString;

/** @author Mueruevet Oeksuez */
public class SOSWriteXML {

    private SOSConnection connection = null;
    private int contentModelId;
    private BufferedWriter output = null;
    private String outboundContentId;
    private HashMap allColumnNamefromTagname = new HashMap();
    private HashMap allTagnamefromColumnName = new HashMap();
    private HashMap allTagTypeForTagName = new HashMap();
    private HashMap allMinOccurForTagName = new HashMap();
    private String tableExport;
    private int contentElementIdFromTI;
    private String nameSpace = "";
    private boolean bNameSpace = true;
    private String fileName = new String();
    private SOSStandardLogger sosLogger;
    private HashMap allChildrenFromParent = new HashMap();
    private int countOfIndex = 2;
    private SOSString sosString = null;
    private HashMap startTag = null;
    private boolean validate = true;

    public SOSWriteXML(SOSConnection connection, int contentModelId, String tableExport, String outboundContentId, String fileName,
            SOSStandardLogger sosLogger) throws Exception {
        try {
            this.connection = connection;
            this.contentModelId = contentModelId;
            this.tableExport = tableExport;
            this.outboundContentId = outboundContentId;
            this.fileName = fileName;
            this.sosLogger = sosLogger;
            init(true);
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public SOSWriteXML(SOSConnection connection, String tableExport, String outboundContentId, String fileName, SOSStandardLogger sosLogger)
            throws Exception {
        try {
            this.connection = connection;
            this.tableExport = tableExport;
            this.outboundContentId = outboundContentId;
            this.fileName = fileName;
            this.sosLogger = sosLogger;
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public void write() throws Exception {
        try {
            init(false);
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private void init(boolean withDBSelection) throws Exception {
        try {
            sosLogger.info("Creating XML-File.. ");
            try {
                output = new BufferedWriter(new FileWriter(fileName));
            } catch (IOException e) {
                throw new Exception("Error while creating Bufferwriter in WriteXML: " + e);
            }
            sosString = new SOSString();
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
            try {
                if (validate) {
                    SOSXMLValidator.validate(fileName);
                }
            } catch (Exception e) {
                throw new Exception("XML-File: " + fileName + " is not valid " + e);
            }
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private void getNameSpace() throws Exception {
        String selst = null;
        try {
            selst = "SELECT \"NAME_SPACE\" FROM CONTENT_MODELS " + " WHERE \"CONTENT_MODEL_ID\" =  " + contentModelId;
            nameSpace = connection.getSingleValue(selst);
            sosLogger.info("----------namespace-----------: " + nameSpace);
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e + selst);
        }
    }

    public void startWriteXML(boolean hasStartTag) throws Exception {
        String selst = "";
        try {
            if (!hasStartTag) {
                selst =
                        "SELECT \"TAG_NAME\", \"CONTENT_ELEMENT_ID\"  FROM CONTENT_TAGS " + " WHERE \"CONTENT_MODEL_ID\" = " + contentModelId
                                + " AND \"CONTENT_ID\" = '" + outboundContentId + "'" + " AND \"PARENT\" = 0 ORDER BY \"CONTENT_ELEMENT_ORDER\" ";
                startTag = connection.getSingle(selst);
            }
            if (startTag.get("content_element_id") != null) {
                output.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
                writeXML(Integer.parseInt(sosString.parseToString(startTag, "content_element_id")), sosString.parseToString(startTag, "tag_name"),
                        new HashMap());
            } else {
                sosLogger.info("Es wurden keine Startknoten ermittelt.");
            }
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private void getContentElementIdFromTI() throws Exception {
        String sc = null;
        try {
            String selst =
                    "SELECT \"PARENT\" FROM CONTENT_TAGS " + " WHERE \"CONTENT_MODEL_ID\" =  " + contentModelId + " AND \"CONTENT_ID\" = 'T' "
                            + " AND upper(\"TAG_NAME\") = 'TECHNICALINFORMATION'";
            sc = connection.getSingleValue(selst);
            if (sc != null && !sc.isEmpty()) {
                contentElementIdFromTI = Integer.parseInt(connection.getSingleValue(selst));
            }
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private void writeHeader() throws Exception {
        HashMap rset = null;
        try {
            String selst =
                    "SELECT \"SYSTEM_ID\", \"TIME_ZONE\", replace(to_char(\"CREATION_TIMESTAMP\",'YYYY-MM-DD HH24:MI:SS'),' ','T') as "
                            + "\"CREATION_TIMESTAMP\", \"DELIVERY_NUMBER\" " + " FROM " + tableExport + " WHERE \"CONTENT_ID\" = 'T'";
            rset = connection.getSingle(selst);
            String ti = "<TechnicalInformation>" + "<SystemID>" + sosString.parseToString(rset, "system_id") + "</SystemID>";
            ti = ti + "<InterfaceID />  <DemandID />" + "<Decimal>.</Decimal> ";
            ti = ti + "<CreationTimeStamp>" + sosString.parseToString(rset, "creation_timestamp") + "</CreationTimeStamp>";
            ti = ti + "<TimeZone>" + sosString.parseToString(rset, "time_zone") + "</TimeZone>";
            ti = ti + "<XSDVersionNumber />";
            ti = ti + "<DeliveryNumber>" + sosString.parseToString(rset, "delivery_number") + "</DeliveryNumber>" + "</TechnicalInformation>";
            output.write(ti);
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private void getAllParentsChildren() throws Exception {
        String selst = "";
        HashMap childHash = new HashMap();
        ArrayList childList = new ArrayList();
        ArrayList aset = null;
        HashMap rset = null;
        try {
            selst =
                    " SELECT \"TAG_NAME\", " + " \"TAG_TYPE\", " + " \"GROUPABLE\", " + " \"PARENT\", " + " \"CONTENT_ELEMENT_ID\" "
                            + " FROM CONTENT_TAGS " + " WHERE \"CONTENT_MODEL_ID\" = " + contentModelId + " AND \"CONTENT_ID\" = '"
                            + outboundContentId + "'" + " ORDER BY \"CONTENT_ELEMENT_ORDER\" ";
            String lastParent = "";
            aset = connection.getArray(selst);
            for (int i = 0; i < aset.size(); i++) {
                rset = (HashMap) aset.get(i);
                if (!(lastParent.equals(rset.get("parent"))) && !"0".equals(lastParent) && !lastParent.trim().isEmpty()) {
                    allChildrenFromParent.put(lastParent, childList);
                    sosLogger.info("vergesseb allChildrenFromParent.put(\"" + lastParent + "\", childList);");
                    childList = new ArrayList();
                    sosLogger.info("childList = new ArrayList();");
                }
                childHash = new HashMap();
                childHash.put("TAG_NAME", rset.get("tag_name"));
                childHash.put("TAG_TYPE", rset.get("tag_type"));
                childHash.put("GROUPABLE", rset.get("groupable"));
                childHash.put("PARENT", rset.get("parent"));
                childHash.put("CONTENT_ELEMENT_ID", rset.get("content_element_id"));
                sosLogger.info("--------------getAllParentsChildren-----------------------");
                sosLogger.info("childHash.put(\"" + "TAG_NAME" + "\", \"" + rset.get("tag_name") + "\");");
                sosLogger.info("childHash.put(\"" + " TAG_TYPE" + "\", \"" + rset.get("tag_type") + "\");");
                sosLogger.info("childHash.put(\"" + "GROUPABLE" + "\", \"" + rset.get("groupable") + "\");");
                sosLogger.info("childHash.put(\"" + "PARENT" + "\", \"" + rset.get("parent") + "\");");
                sosLogger.info("childHash.put(\"" + "CONTENT_ELEMENT_ID" + "\", \"" + rset.get("content_element_id") + "\");");
                childList.add(childHash);
                sosLogger.info("childList.add(childHash);");
                if ("0".equals(sosString.parseToString(rset, "parent"))) {
                    allChildrenFromParent.put(rset.get("parent"), childList);
                    sosLogger.info("allChildrenFromParent.put(\"" + rset.get("parent") + "\", childList);");
                    childList = new ArrayList();
                    sosLogger.info("childList = new ArrayList();");
                }
                if (!childList.isEmpty()) {
                    lastParent = sosString.parseToString(rset.get("parent"));
                }
            }
            allChildrenFromParent.put(lastParent, childList);
            sosLogger.info("allChildrenFromParent.put(\"" + lastParent + "\", childList);");
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public void writeXML(int parent, String parentTagName, HashMap groupKeys) throws Exception {
        HashMap complexChildren = new HashMap();
        ArrayList atomarChildren = new ArrayList();
        HashMap groupableChildren = new HashMap();
        String selVal = "";
        try {
            boolean group = false;
            HashMap childrens = new HashMap();
            ArrayList parentsChildren = new ArrayList();
            if (allChildrenFromParent.get(String.valueOf(parent)) != null) {
                parentsChildren = (ArrayList) allChildrenFromParent.get(String.valueOf(parent));
                for (int i = 0; i < parentsChildren.size(); i++) {
                    childrens = (HashMap) parentsChildren.get(i);
                    String tagName = getValues(childrens, "TAG_NAME");
                    String contentElementId = getValues(childrens, "CONTENT_ELEMENT_ID");
                    String colName = "";
                    if (allColumnNamefromTagname.get(tagName) != null) {
                        colName = getValues(allColumnNamefromTagname, tagName);
                    }
                    int groupable = Integer.parseInt(getValues(childrens, "GROUPABLE"));
                    if ("0".equals(getValues(childrens, "TAG_TYPE"))) {
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
            if (!atomarChildren.isEmpty()) {
                if (allTagTypeForTagName.get(atomarChildren.get(0)) != null
                        && ("3".equals(allTagTypeForTagName.get(atomarChildren.get(0)))
                                || "5".equals(allTagTypeForTagName.get(atomarChildren.get(0))))) {
                    selVal = selVal + "select to_char(" + atomarChildren.get(0) + ",'YYYY-MM-DD HH24:MI:SS')";
                } else {
                    selVal = selVal + "select " + atomarChildren.get(0);
                }
                for (int i = 1; i < atomarChildren.size(); i++) {
                    if (allTagTypeForTagName.get(atomarChildren.get(0)) != null
                            && ("3".equals(allTagTypeForTagName.get(atomarChildren.get(0)))
                                    || "5".equals(allTagTypeForTagName.get(atomarChildren.get(0))))) {
                        selVal = selVal + ", to_char(" + atomarChildren.get(i) + ",'YYYY-MM-DD HH24:MI:SS')";
                    } else {
                        selVal = selVal + ", " + atomarChildren.get(i);
                    }
                }
                selVal = selVal + " from " + tableExport + " where \"CONTENT_ID\" = '" + outboundContentId + "' ";
                for (int i = 0; i < groupKeys.keySet().toArray().length; i++) {
                    selVal = selVal + " and " + groupKeys.keySet().toArray()[i];
                    if (groupKeys.values().toArray()[i] == null
                            && sosString.parseToString(groupKeys.values().toArray()[i]).replaceAll("'", "").isEmpty()) {
                        selVal = selVal + " is null ";
                    } else {
                        selVal = selVal + " = " + groupKeys.values().toArray()[i];
                    }
                }
                if (!group) {
                    for (int i = 0; i < atomarChildren.size(); i++) {
                        if (i == 0) {
                            selVal = selVal + " group by " + atomarChildren.get(i);
                        } else {
                            selVal = selVal + ", " + atomarChildren.get(i);
                        }
                    }
                } else {
                    sosLogger.debug5("test : " + selVal);
                }
                sosLogger.debug5("writexml query : " + selVal);
                ArrayList a = null;
                HashMap rset2 = null;
                a = connection.getArray(selVal);
                for (int k = 0; k < a.size(); k++) {
                    rset2 = (HashMap) a.get(k);
                    output.write("<" + parentTagName + ">");
                    HashMap localGroupKeys = (HashMap) groupKeys.clone();
                    for (int i = 0; i < atomarChildren.size(); i++) {
                        String columnName = atomarChildren.get(i).toString();
                        String t = (String) allTagnamefromColumnName.get(columnName);
                        if ((rset2.get(columnName.toLowerCase()) == null || ((rset2.get(columnName.toLowerCase()) != null) && !rset2.get(
                                columnName.toLowerCase()).toString().isEmpty()))
                                && ("3".equals(allTagTypeForTagName.get(t)) || "5".equals(allTagTypeForTagName.get(t)))) {
                            //
                        } else if ((rset2.get(columnName.toLowerCase()) == null || (rset2.get(columnName.toLowerCase()) != null && rset2.get(
                                columnName.toLowerCase()).toString().isEmpty()))
                                && "0".equals(allMinOccurForTagName.get(t))) {
                            // in this case do nothing
                        } else {
                            output.write("<" + allTagnamefromColumnName.get(columnName) + ">");
                            if (rset2.get(columnName.toLowerCase()) != null) {
                                if ("3".equals(allTagTypeForTagName.get(t))) {
                                    String da = sosString.parseToString(rset2, columnName.toLowerCase());
                                    output.write(da.replace(' ', 'T').substring(0, da.indexOf(".")));
                                } else if ("5".equals(allTagTypeForTagName.get(t))) {
                                    String da = sosString.parseToString(rset2, columnName.toLowerCase());
                                    output.write(da.substring(0, da.indexOf(" ")));
                                } else {
                                    String currStr = sosString.parseToString(rset2, columnName.toLowerCase());
                                    currStr = currStr.replaceAll("&", "&amp;");
                                    currStr = currStr.replaceAll("'", "&apos;");
                                    currStr = currStr.replaceAll(">", "&gt;");
                                    currStr = currStr.replaceAll("<", "&lt;");
                                    currStr = currStr.replaceAll("\"", "&quot;");
                                    output.write(currStr);
                                }
                            }
                            output.write("</" + allTagnamefromColumnName.get(columnName) + ">");
                        }
                        if (groupableChildren.containsKey(columnName)) {
                            Object tag = this.allTagnamefromColumnName.get(columnName);
                            String col = null;
                            if (rset2.get(columnName.toLowerCase()) != null
                                    && !sosString.parseToString(rset2.get(columnName.toLowerCase())).isEmpty()) {
                                col = sosString.parseToString(rset2.get(columnName.toLowerCase()));
                            }
                            rset2.get(columnName.toLowerCase());
                            if (allTagTypeForTagName.get(tag) != null
                                    && ("3".equals(allTagTypeForTagName.get(tag)) || "5".equals(allTagTypeForTagName.get(tag)))) {
                                if (col == null) {
                                    localGroupKeys.put(columnName, col);
                                } else {
                                    localGroupKeys.put(columnName, "to_date('" + col.substring(0, col.indexOf(".")) + "')");
                                }
                            } else {
                                if (col == null) {
                                    localGroupKeys.put(columnName, null);
                                } else {
                                    localGroupKeys.put(columnName,
                                            "'" + sosString.parseToString(rset2, columnName.toLowerCase()).replaceAll("'", "''") + "'");
                                }
                            }
                        }
                    }
                    for (int i = 0; i < complexChildren.keySet().toArray().length; i++) {
                        String tag = complexChildren.keySet().toArray()[i].toString();
                        writeXML(Integer.parseInt(complexChildren.values().toArray()[i].toString()), tag, localGroupKeys);
                    }
                    output.write("</" + parentTagName + ">");
                }
            } else {
                for (int i = 0; i < complexChildren.keySet().toArray().length; i++) {
                    String tag = complexChildren.keySet().toArray()[i].toString();
                    if (bNameSpace) {
                        output.write("<" + parentTagName + " " + nameSpace + ">");
                        bNameSpace = false;
                    } else {
                        output.write("<" + parentTagName + ">");
                    }
                    if (contentElementIdFromTI == parent) {
                        writeHeader();
                    }
                    writeXML(Integer.parseInt(complexChildren.values().toArray()[i].toString()), tag, groupKeys);
                    output.write("</" + parentTagName + ">");
                }
            }
        } catch (SQLException e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e + " Statement call: " + selVal);
        }
    }

    public void getAllColumnNameFromTagName() throws Exception {
        ArrayList list = null;
        HashMap result = null;
        try {
            String selst = " SELECT \"COLUMN_NAME\"" + " , \"TAG_NAME\"" + " , \"MIN_OCCURS\"" + " , \"TAG_TYPE\""
                            + " FROM CONTENT_TAGS a, CONTENT_COLUMNS b" + " WHERE a.\"COLUMN_ID\" = b.\"COLUMN_ID\" "
                            + " and a.\"CONTENT_MODEL_ID\" = " + contentModelId + " and a.\"CONTENT_ID\" = '" + outboundContentId + "'";
            list = connection.getArray(selst);
            for (int i = 0; i < list.size(); i++) {
                result = (HashMap) list.get(i);
                allMinOccurForTagName.put(result.get("tag_name"), result.get("min_occurs"));
                allColumnNamefromTagname.put(result.get("tag_name"), sosString.parseToString(result.get("column_name")).toLowerCase());
                allTagnamefromColumnName.put(sosString.parseToString(result.get("column_name")).toLowerCase(), result.get("tag_name"));
                allTagTypeForTagName.put(result.get("tag_name"), String.valueOf(result.get("tag_type")));
                sosLogger.info("-------------getAllColumnNameFromTagName----------------------");
                sosLogger.info("allMinOccurForTagName.put(\"" + result.get("tag_name") + "\", \"" + result.get("min_occurs") + "\")");
                sosLogger.info("allColumnNamefromTagname.put(\"" + result.get("tag_name") + "\", \"" + result.get("column_name") + "\")");
                sosLogger.info("allTagnamefromColumnName.put(\"" + result.get("column_name") + "\", \"" + result.get("tag_name") + "\")");
                sosLogger.info("allTagTypeForTagName.put(\"" + result.get("tag_name") + "\", \"" + result.get("tag_type") + "\")");
            }
        } catch (SQLException e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private String getValues(HashMap hash, String key) {
        if (hash.get(key) == null) {
            return "";
        } else {
            return hash.get(key).toString();
        }
    }

    private void createIndex() throws Exception {
        ArrayList columnsList = new ArrayList();
        String createIndex = "";
        String iTableName = tableExport;
        try {
            if (tableExport.length() > 8) {
                iTableName = tableExport.substring(0, 8);
            }
            columnsList = getColumns2Index();
            for (int i = 0; i < columnsList.size(); i++) {
                createIndex = " CREATE INDEX " + iTableName + "_" + i + " ON " + tableExport + "( " + columnsList.get(i) + ")";
                sosLogger.debug("createIndex: " + createIndex);
                try {
                    connection.executeQuery(createIndex);
                } catch (Exception e) {
                    sosLogger.debug("Warning in WriteXML.createIndex. Index " + iTableName + " could not created. " + e);
                }
            }
        } catch (Exception e) {
            sosLogger.debug("Error in WriteXML.createIndex. Index " + iTableName + " could not created. " + e);
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
            selst = " SELECT \"TAG_NAME\"" + " , \"LEAF\" " + " FROM CONTENT_TAGS a " + " WHERE a.\"CONTENT_MODEL_ID\" = " + contentModelId
                            + " and a.\"CONTENT_ID\" = '" + outboundContentId + "'" + " order by a.\"CONTENT_ELEMENT_ORDER\"";
            list = connection.getArray(selst);
            for (int i = 0; i < list.size(); i++) {
                rset = (HashMap) list.get(i);
                if (count == 0) {
                    return retVal;
                }
                if ("1".equals(rset.get("leaf"))) {
                    if (index.length() > 1) {
                        index = index + ", ";
                    }
                    index = index + getValues(allColumnNamefromTagname, sosString.parseToString(rset.get("tag_name")));
                } else if (!index.trim().isEmpty()) {
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

    public void setAllColumnNamefromTagname(HashMap allColumnNamefromTagname) {
        this.allColumnNamefromTagname = allColumnNamefromTagname;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public void setAllMinOccurForTagName(HashMap allMinOccurForTagName) {
        this.allMinOccurForTagName = allMinOccurForTagName;
    }

    public void setAllTagnamefromColumnName(HashMap allTagnamefromColumnName) {
        this.allTagnamefromColumnName = allTagnamefromColumnName;
    }

    public void setAllTagTypeForTagName(HashMap allTagTypeForTagName) {
        this.allTagTypeForTagName = allTagTypeForTagName;
    }

    public void setAllChildrenFromParent(HashMap allChildrenFromParent) {
        this.allChildrenFromParent = allChildrenFromParent;
    }

    public HashMap getStartTag() {
        return startTag;
    }

    public void setStartTag(HashMap startTag) {
        this.startTag = startTag;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

}