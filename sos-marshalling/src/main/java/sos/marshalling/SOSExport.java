package sos.marshalling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.connection.SOSConnection;
import sos.connection.SOSMySQLConnection;

/** @author Titus Meyer */
public class SOSExport {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSExport.class);
    private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    private SOSConnection connection = null;
    private String application = null;
    private String fileName = null;
    private String xmlTagname = "sos_export";
    private String xmlEncoding = "iso-8859-1";
    private String normalizeFieldName = "strtoupper";
    private String normalizeTagName = "strtolower";
    private String xmlIndentation = "  ";
    private int xmlIndent = 0;
    private Queries queries = new Queries();
    private int queryCnt = 0;
    private int rekursionCnt = 0;
    private int lineWrap = 254;

    public SOSExport(SOSConnection conn, String fileName, String application) {
        if (conn != null) {
            this.connection = conn;
        }
        if (fileName != null) {
            this.fileName = fileName;
        }
        if (application != null) {
            this.application = application;
        }
        System.setProperty("oracledatabasemetadata.get_lob_precision", "false");
    }

    public void setConnection(SOSConnection conn) {
        this.connection = conn;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setXMLTagname(String xmlTagname) {
        this.xmlTagname = xmlTagname;
    }

    public void setXMLEncoding(String xmlEncoding) {
        this.xmlEncoding = xmlEncoding;
    }

    public void setNormalizeFieldName(String normalizeFieldName) {
        if (!"strtolower".equalsIgnoreCase(normalizeFieldName) || !"strtoupper".equalsIgnoreCase(normalizeFieldName)) {
            throw new IllegalArgumentException("SOSExport.setNormalizeFieldName: normalizeFielName must be \"strtolower\" or \"strtoupper\"");
        }
        this.normalizeFieldName = normalizeFieldName;
    }

    public void setNormalizeTagName(String normalizeTagName) {
        if (!"strtolower".equalsIgnoreCase(normalizeTagName) || !"strtoupper".equalsIgnoreCase(normalizeTagName)) {
            throw new IllegalArgumentException("SOSExport.setNormalizeTagName: normalizeTagName must be \"strtolower\" or \"strtoupper\"");
        }
        this.normalizeTagName = normalizeTagName;
    }

    public void setXMLIndentation(String indentation) {
        if ((indentation.length() & 0x1) != 0) {
            throw new IllegalArgumentException("SOSExport.setXMLIndentation: the indentation string must have an even length");
        }
        this.xmlIndentation = indentation;
    }

    public void setLineWrap(int lineWrap) {
        if ((lineWrap & 0x1) != 0) {
            throw new IllegalArgumentException("SOSExport.setLineWrap: the line must wrap at an even length");
        }
        this.lineWrap = lineWrap;
    }

    public int query(String tag, String key, String query, String parameter, int queryId) throws Exception {
        try {
            if (query != null && !"".equals(query) && tag != null && !"".equals(tag)) {
                Query obj = new Query(tag, key, query, parameter);
                if (queryId > -1) {
                    obj.setDependent(true);
                }
                if (obj.getParameterCnt() < countStr(query, "?")) {
                    throw new IllegalArgumentException("SOSExport.query: too few fields in parameter for substitution in the query");
                }
                LOGGER.debug("query: tag=" + tag + " key=" + key + " query_id=" + queryId + " query_cnt=" + queryCnt);
                if (queryId >= 0 && queryId < queries.cnt()) {
                    queries.get(queryId).addDependRef(new Integer(queryCnt));
                } else if (queryCnt > 0 && (queryCnt - 1) < queries.cnt()) {
                    queries.get(queryCnt - 1).addDependRef(new Integer(queryCnt));
                } else if (parameter != null) {
                    throw new IllegalArgumentException("SOSExport.query: query_id index out of range: " + queryId);
                }
                queries.add(obj);
                return queryCnt++;
            } else {
                throw new IllegalArgumentException("SOSExport.query: empty query statement!");
            }
        } catch (Exception e) {
            throw new Exception("SOSExport.query: " + e.getMessage(), e);
        }
    }

    public int query(String tag, String key, String query, String parameter, String operation, HashMap keys4Delete, int queryId) throws Exception {
        try {
            if (query != null && !"".equals(query) && tag != null && !"".equals(tag)) {
                Query obj = new Query(tag, key, query, parameter, operation, keys4Delete);
                if (queryId > -1) {
                    obj.setDependent(true);
                }
                if (obj.getParameterCnt() < countStr(query, "?")) {
                    throw new IllegalArgumentException("SOSExport.query: too few fields in parameter for substitution in the query");
                }
                LOGGER.debug("query: tag=" + tag + " key=" + key + " query_id=" + queryId + " query_cnt=" + queryCnt);
                if (queryId >= 0 && queryId < queries.cnt()) {
                    queries.get(queryId).addDependRef(new Integer(queryCnt));
                } else if (queryCnt > 0 && (queryCnt - 1) < queries.cnt()) {
                    queries.get(queryCnt - 1).addDependRef(new Integer(queryCnt));
                } else if (parameter != null) {
                    throw new IllegalArgumentException("SOSExport.query: query_id index out of range: " + queryId);
                }
                queries.add(obj);
                return queryCnt++;
            } else {
                throw new IllegalArgumentException("SOSExport.query: empty query statement!");
            }
        } catch (Exception e) {
            throw new Exception("SOSExport.query: " + e.getMessage(), e);
        }
    }

    public int query(String tag, String key, String query) throws Exception {
        return this.query(tag, key, query, null, -1);
    }

    public int add(String tag, String key, String query, String parameter, int queryId) throws Exception {
        try {
            if (query != null && !"".equals(query) && tag != null && !"".equals(tag)) {
                Query obj = new Query(tag, key, query, parameter);
                if (obj.getParameterCnt() < countStr(query, "?")) {
                    throw new IllegalArgumentException("SOSExport.query: too few fields in parameter for substitution in the query");
                }
                LOGGER.debug("add: tag=" + tag + " key=" + key + " query_id=" + queryId + "query_cnt=" + queryCnt);
                if (queryId >= 0 && queryId < queries.cnt()) {
                    queries.get(queryId).addIndepdRef(new Integer(queryCnt));
                } else if (parameter != null) {
                    throw new IllegalArgumentException("SOSExport.add: query_id index out of range: " + queryId);
                }
                queries.add(obj);
                return queryCnt++;
            } else {
                throw new IllegalArgumentException("SOSExport.query: tag and query must be defined!");
            }
        } catch (Exception e) {
            throw new Exception("SOSExport.add: " + e.getMessage(), e);
        }
    }

    public int add(String tag, String key, String query, String parameter, String operation, HashMap keys4Delete, int queryId) throws Exception {
        try {
            if (query != null && !"".equals(query) && tag != null && !"".equals(tag)) {
                Query obj = new Query(tag, key, query, parameter, operation, keys4Delete);
                if (obj.getParameterCnt() < countStr(query, "?")) {
                    throw new IllegalArgumentException("SOSExport.query: too few fields in parameter for substitution in the query");
                }
                LOGGER.debug("add: tag=" + tag + " key=" + key + " query_id=" + queryId + "query_cnt=" + queryCnt);
                if (queryId >= 0 && queryId < queries.cnt()) {
                    queries.get(queryId).addIndepdRef(new Integer(queryCnt));
                } else if (parameter != null) {
                    throw new IllegalArgumentException("SOSExport.add: query_id index out of range: " + queryId);
                }
                queries.add(obj);
                return queryCnt++;
            } else {
                throw new IllegalArgumentException("SOSExport.query: tag and query must be defined!");
            }
        } catch (Exception e) {
            throw new Exception("SOSExport.add: " + e.getMessage(), e);
        }
    }

    public String doExport() throws Exception, FileNotFoundException {
        try {
            if ("strtoupper".equalsIgnoreCase(normalizeFieldName)) {
                connection.setKeysToUpperCase();
                connection.setFieldNameToUpperCase(true);
            } else {
                connection.setKeysToLowerCase();
                connection.setFieldNameToUpperCase(false);
            }
            if (fileName != null && !"".equals(fileName)) {
                File file = new File(fileName);
                file.createNewFile();
                if (!file.canWrite()) {
                    throw new FileNotFoundException("File not writeable: " + fileName);
                }
                LOGGER.debug("Starte Export in die Datei...");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                FileOutputStream fos = new FileOutputStream(fileName);
                OutputStreamWriter fw = new OutputStreamWriter(fos, xmlEncoding);
                fw.write("<?xml version=\"1.0\" encoding=\"" + xmlEncoding + "\"?>\n");
                fw.write(indent(1) + "<" + normalizeTagName(xmlTagname) + " application=\"" + application + "\" created=\"" + dateFormat.format(
                        new Date()) + "\">\n");
                dateFormat = null;
                for (int i = 0; i < queries.cnt(); i++) {
                    if (!queries.get(i).isDone() && !queries.get(i).isDependent()) {
                        if (queries.get(i).getOperation() != null && "delete".equalsIgnoreCase(queries.get(i).getOperation())) {
                            exportQueriesForDelete(i, null, fw);
                        } else {
                            exportQueries(i, fw);
                        }
                    }
                }
                fw.write(indent(-1) + "</" + normalizeTagName(xmlTagname) + ">\n");
                fw.close();
                fos.close();
                LOGGER.debug("...Export in die Datei beendet");
                return "";
            } else {
                LOGGER.debug("Starte Export...");
                StringBuilder output = new StringBuilder();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // XML Header
                output.append("<?xml version=\"1.0\" encoding=\"" + xmlEncoding + "\"?>\n");
                output.append(indent(1) + "<" + normalizeTagName(xmlTagname) + " application=\"" + application + "\" created=\"" + dateFormat.format(
                        new Date()) + "\">\n");
                dateFormat = null;
                for (int i = 0; i < queries.cnt(); i++) {
                    if (!queries.get(i).isDone()) {
                        output.append(exportQueries(i));
                    }
                }
                output.append(indent(-1) + "</" + normalizeTagName(xmlTagname) + ">\n");
                LOGGER.debug("...Export beendet");
                return output.toString();
            }
        } catch (Exception e) {
            throw new Exception("SOSExport.export: " + e.getMessage(), e);
        }
    }

    public String doExport(SOSConnection conn, String fileName) throws Exception, FileNotFoundException {
        this.connection = conn;
        this.fileName = fileName;
        return doExport();
    }

    private String exportQueries(int queryId) throws Exception {
        return exportQueries(queryId, new ArrayList());
    }

    private String exportQueries(int queryId, Writer fw) throws Exception {
        return exportQueries(queryId, new ArrayList(), fw);
    }

    private String exportQueries(int queryId, ArrayList parameterValues, Writer fw) throws Exception {
        try {
            LOGGER.debug("export_queries: " + " name=\"" + queries.get(queryId).getTag() + "\" query_id=\"" + queryId + "\" key=\"" + queries.get(
                    queryId).keysToStr() + "\" dependend=\"" + queries.get(queryId).dependRefToStr() + "\" operation=\"" + queries.get(queryId)
                            .getOperation() + "\" independend=\"" + queries.get(queryId).indepdRefToStr() + "\"");
            rekursionCnt++;
            String queryStm = substituteQuery(queries.get(queryId).getQuery(), parameterValues);
            HashMap allFieldNames = prepareGetFieldName(queryStm);
            SOSImExportTableFieldTypes fieldTypes = getFieldTypes(queryStm.toString());
            ArrayList result = new ArrayList();
            result = getArray(queryStm.toString());
            fw.write(indent(1) + "<" + normalizeTagName(xmlTagname + "_package id=\"") + queries.get(queryId).getTag() + "\">\n");
            if (!result.isEmpty()) {
                fw.write(indent(1) + "<" + normalizeTagName(xmlTagname + "_meta") + ">\n");
                fw.write(indent(0) + "<" + normalizeTagName("table name=\"") + queries.get(queryId).getTag() + "\" />\n");
                fw.write(indent(1) + "<" + normalizeTagName("key_fields") + ">\n");
                for (int i = 0; i < queries.get(queryId).getKeyCnt(); i++) {
                    LOGGER.debug("key_field[" + i + "]=\"" + queries.get(queryId).getKey(i) + "\"");
                    fw.write(indent() + "<" + normalizeTagName("field name=\"") + normalizeFieldName(queries.get(queryId).getKey(i)) + "\"");
                    fw.write(" type=\"" + fieldTypes.getTypeName(normalizeFieldName(queries.get(queryId).getKey(i))) + "\"");
                    fw.write(" typeID=\"" + fieldTypes.getTypeId(normalizeFieldName(queries.get(queryId).getKey(i))) + "\"");
                    fw.write(" len=\"" + fieldTypes.getLength(normalizeFieldName(queries.get(queryId).getKey(i))) + "\"");
                    fw.write(" scale=\"" + fieldTypes.getScale(normalizeFieldName(queries.get(queryId).getKey(i))) + "\"");
                    fw.write(" />\n");
                }
                fw.write(indent(-1) + normalizeTagName("</key_fields>") + "\n");
                fw.write(indent(1) + normalizeTagName("<fields>") + "\n");
                Object[] fields = ((HashMap) result.get(0)).keySet().toArray();
                for (int i = 0; i < fields.length; i++) {
                    fw.write(indent() + "<" + normalizeTagName("field name=\"") + normalizeFieldName((String) fields[i]) + "\"");
                    fw.write(" type=\"" + fieldTypes.getTypeName(normalizeFieldName((String) fields[i])) + "\"");
                    fw.write(" typeID=\"" + fieldTypes.getTypeId(normalizeFieldName((String) fields[i])) + "\"");
                    fw.write(" len=\"" + fieldTypes.getLength(normalizeFieldName((String) fields[i])) + "\"");
                    fw.write(" scale=\"" + fieldTypes.getScale(normalizeFieldName((String) fields[i])) + "\"");
                    fw.write(" />\n");
                }
                fields = null;
                fw.write(indent(-1) + normalizeTagName("</fields>") + "\n");
                fw.write(indent(-1) + "</" + normalizeTagName(xmlTagname + "_meta") + ">\n");
                fw.write(indent(1) + "<" + normalizeTagName(xmlTagname + "_data") + ">\n");
                for (int i = 0; i < result.size(); i++) {
                    HashMap record = (HashMap) result.get(i);
                    LOGGER.trace("get: " + queries.get(queryId).getTag() + " query_id=" + queryId);
                    fw.write(indent(1) + "<" + normalizeTagName(xmlTagname + "_record name=\"") + queries.get(queryId).getTag() + "\">\n");
                    fw.write(indent(1) + "<" + normalizeTagName(xmlTagname + "_fields") + (queries.get(queryId).getOperation() != null && !queries
                            .get(queryId).getOperation().isEmpty() ? " operation=\"" + queries.get(queryId).getOperation() + "\" " : "") + ">\n");
                    for (Iterator it = record.keySet().iterator(); it.hasNext();) {
                        String key = it.next().toString();
                        String lobType = null;
                        switch (fieldTypes.getTypeId(normalizeFieldName(key))) {
                        case Types.CLOB:
                        case Types.LONGVARCHAR:
                            lobType = "clob";
                            break;
                        case Types.BLOB:
                        case Types.BINARY:
                        case Types.VARBINARY:
                        case Types.LONGVARBINARY:
                            lobType = "blob";
                            break;
                        default:
                            if (record.get(key) != null) {
                                switch (fieldTypes.getTypeId(normalizeFieldName(key))) {
                                case Types.DATE:
                                case Types.TIMESTAMP:
                                    String val = record.get(key).toString();
                                    if (val.endsWith(".0")) {
                                        record.put(key, val.substring(0, val.length() - 2));
                                    }
                                }
                            }
                            fw.write(indent() + "<" + normalizeTagName(key) + " null=");
                            if (record.get(key) != null) {
                                fw.write("\"false\"><![CDATA[" + asXml(record.get(key).toString()) + "]]>");
                            } else {
                                fw.write("\"true\"><![CDATA[]]>");
                            }
                            fw.write("</" + normalizeTagName(key) + ">\n");
                            break;
                        }
                        if (lobType != null) {
                            int posBegin = new String(queryStm).toLowerCase().indexOf("from");
                            int posEnd = new String(queryStm).toLowerCase().indexOf(" ", posBegin + 5);
                            StringBuilder queryBlobStm = new StringBuilder();
                            String blobFieldName = getBlobFieldName(allFieldNames, key.toString());
                            queryBlobStm.append("SELECT " + normalizeFieldName(blobFieldName) + " ");
                            if (posEnd < posBegin) {
                                posEnd = queryStm.length();
                            }
                            queryBlobStm.append(queryStm.substring(posBegin, posEnd));
                            String and = " WHERE ";
                            for (int j = 0; j < queries.get(queryId).getKeyCnt(); j++) {
                                String keyFieldName = getKeyFieldName(allFieldNames, queries.get(queryId).getKey(j));
                                queryBlobStm.append(and + normalizeFieldName(keyFieldName) + " =");
                                queryBlobStm.append(quote(fieldTypes.getTypeId(normalizeFieldName(queries.get(queryId).getKey(j))), (String) record
                                        .get(normalizeFieldName(queries.get(queryId).getKey(j)))));
                                and = " AND ";
                            }
                            byte[] blob = null;
                            if ("blob".equals(lobType)) {
                                blob = connection.getBlob(queryBlobStm.toString());
                            } else {
                                blob = str2bin(connection.getClob(queryBlobStm.toString()));
                            }
                            fw.write(indent() + "<" + normalizeTagName(key) + " null=");
                            if (blob != null && blob.length > 0) {
                                indent(1);
                                fw.write("\"false\">\n");
                                toHexString(blob, indent(), lineWrap, fw);
                                fw.write("\n" + indent(-1));
                            } else {
                                fw.write("\"true\">");
                            }
                            fw.write("</" + normalizeTagName(key) + ">\n");
                        }
                    }
                    fw.write(indent(-1) + "</" + normalizeTagName(xmlTagname + "_fields") + ">\n");
                    if (queries.get(queryId).getIndepdRefCnt() > 0) {
                        for (int j = 0; j < queries.get(queryId).getIndepdRefCnt(); j++) {
                            int recQuery = queries.get(queryId).getIndepdRef(j).intValue();
                            LOGGER.debug("recursive independend query: " + queries.get(recQuery).getQuery());
                            exportQueries(recQuery, queryParams(recQuery, record), fw);
                        }
                    }
                    // Rekursion dependend refs
                    if (queries.get(queryId).getDependRefCnt() > 0) {
                        for (int j = 0; j < queries.get(queryId).getDependRefCnt(); j++) {
                            int recQuery = queries.get(queryId).getDependRef(j).intValue();
                            LOGGER.debug("recursive dependend query: " + queries.get(recQuery).getQuery());
                            exportQueries(recQuery, queryParams(recQuery, record), fw);
                        }
                    }
                    fw.write(indent(-1) + "</" + normalizeTagName(xmlTagname + "_record") + ">\n");
                }
                fw.write(indent(-1) + "</" + normalizeTagName(xmlTagname + "_data") + ">\n");
            }
            fw.write(indent(-1) + "</" + normalizeTagName(xmlTagname + "_package") + ">\n");
            queries.get(queryId).setDone(true);
            return "";
        } catch (Exception e) {
            throw new Exception("SOSExport.exportQueries: " + e.getMessage(), e);
        }
    }

    public String exportQueriesForDelete(int queryId, ArrayList parameterValues, Writer fw) throws Exception {
        try {
            LOGGER.debug("export_queries: " + " name=\"" + queries.get(queryId).getTag() + "\" query_id=\"" + queryId + "\" key=\"" + queries.get(
                    queryId).keysToStr() + "\" dependend=\"" + queries.get(queryId).dependRefToStr() + "\" operation=\"" + queries.get(queryId)
                            .getOperation() + "\" field_keys=\"" + queries.get(queryId).getFieldsKeys() + "\" independend=\"" + queries.get(queryId)
                                    .indepdRefToStr() + "\"");
            rekursionCnt++;
            String queryStm = substituteQuery(queries.get(queryId).getQuery(), parameterValues);
            HashMap allFieldNames = prepareGetFieldName(queryStm);
            SOSImExportTableFieldTypes fieldTypes = getFieldTypes(queryStm.toString());
            fw.write(indent(1) + "<" + normalizeTagName(xmlTagname + "_package id=\"") + queries.get(queryId).getTag() + "\">\n");
            fw.write(indent(1) + "<" + normalizeTagName(xmlTagname + "_meta") + ">\n");
            fw.write(indent(0) + "<" + normalizeTagName("table name=\"") + queries.get(queryId).getTag() + "\" />\n");
            fw.write(indent(1) + "<" + normalizeTagName("key_fields") + ">\n");
            for (int i = 0; i < queries.get(queryId).getKeyCnt(); i++) {
                LOGGER.debug("key_field[" + i + "]=\"" + queries.get(queryId).getKey(i) + "\"");
                fw.write(indent() + "<" + normalizeTagName("field name=\"") + normalizeFieldName(queries.get(queryId).getKey(i)) + "\"");
                fw.write(" type=\"" + fieldTypes.getTypeName(normalizeFieldName(queries.get(queryId).getKey(i))) + "\"");
                fw.write(" typeID=\"" + fieldTypes.getTypeId(normalizeFieldName(queries.get(queryId).getKey(i))) + "\"");
                fw.write(" len=\"" + fieldTypes.getLength(normalizeFieldName(queries.get(queryId).getKey(i))) + "\"");
                fw.write(" scale=\"" + fieldTypes.getScale(normalizeFieldName(queries.get(queryId).getKey(i))) + "\"");
                fw.write(" />\n");
            }
            fw.write(indent(-1) + normalizeTagName("</key_fields>") + "\n");
            fw.write(indent(1) + normalizeTagName("<fields>") + "\n");
            Object[] fields = queries.get(queryId).getFieldsKeys().keySet().toArray();
            for (int i = 0; i < fields.length; i++) {
                fw.write(indent() + "<" + normalizeTagName("field name=\"") + normalizeFieldName((String) fields[i]) + "\"");
                fw.write(" type=\"" + fieldTypes.getTypeName(normalizeFieldName((String) fields[i])) + "\"");
                fw.write(" typeID=\"" + fieldTypes.getTypeId(normalizeFieldName((String) fields[i])) + "\"");
                fw.write(" len=\"" + fieldTypes.getLength(normalizeFieldName((String) fields[i])) + "\"");
                fw.write(" scale=\"" + fieldTypes.getScale(normalizeFieldName((String) fields[i])) + "\"");
                fw.write(" />\n");
            }
            fields = null;
            fw.write(indent(-1) + normalizeTagName("</fields>") + "\n");
            fw.write(indent(-1) + "</" + normalizeTagName(xmlTagname + "_meta") + ">\n");
            fw.write(indent(1) + "<" + normalizeTagName(xmlTagname + "_data") + ">\n");
            HashMap record = queries.get(queryId).getFieldsKeys();
            LOGGER.trace("get: " + queries.get(queryId).getTag() + " query_id=" + queryId);
            fw.write(indent(1) + "<" + normalizeTagName(xmlTagname + "_record name=\"") + queries.get(queryId).getTag() + "\">\n");
            fw.write(indent(1) + "<" + normalizeTagName(xmlTagname + "_fields") + (queries.get(queryId).getOperation() != null && queries.get(queryId)
                    .getOperation().length() > 0 ? " operation=\"" + queries.get(queryId).getOperation() + "\" " : "") + ">\n");
            for (Iterator it = record.keySet().iterator(); it.hasNext();) {
                String key = it.next().toString();
                if (record.get(key) != null) {
                    switch (fieldTypes.getTypeId(normalizeFieldName(key))) {
                    case Types.DATE:
                    case Types.TIMESTAMP:
                        String val = record.get(key).toString();
                        if (val.endsWith(".0")) {
                            record.put(key, val.substring(0, val.length() - 2));
                        }
                    }
                }
                if (record.get(key) != null && !record.get(key).toString().isEmpty()) {
                    fw.write(indent() + "<" + normalizeTagName(key) + " null=");
                    if (record.get(key) != null) {
                        fw.write("\"false\"><![CDATA[" + asXml(record.get(key).toString()) + "]]>");
                    } else {
                        fw.write("\"true\"><![CDATA[]]>");
                    }
                    fw.write("</" + normalizeTagName(key) + ">\n");
                }
            }
            fw.write(indent(-1) + "</" + normalizeTagName(xmlTagname + "_fields") + ">\n");
            if (queries.get(queryId).getIndepdRefCnt() > 0) {
                for (int j = 0; j < queries.get(queryId).getIndepdRefCnt(); j++) {
                    int recQuery = queries.get(queryId).getIndepdRef(j).intValue();
                    LOGGER.debug("recursive independend query: " + queries.get(recQuery).getQuery());
                    exportQueries(recQuery, queryParams(recQuery, record), fw);
                }
            }
            if (queries.get(queryId).getDependRefCnt() > 0) {
                for (int j = 0; j < queries.get(queryId).getDependRefCnt(); j++) {
                    int recQuery = queries.get(queryId).getDependRef(j).intValue();
                    LOGGER.debug("recursive dependend query: " + queries.get(recQuery).getQuery());
                    exportQueriesForDelete(recQuery, queryParams(recQuery, record), fw);
                }
            }

            fw.write(indent(-1) + "</" + normalizeTagName(xmlTagname + "_record") + ">\n");
            fw.write(indent(-1) + "</" + normalizeTagName(xmlTagname + "_data") + ">\n");
            fw.write(indent(-1) + "</" + normalizeTagName(xmlTagname + "_package") + ">\n");
            queries.get(queryId).setDone(true);
            return "";
        } catch (Exception e) {
            throw new Exception("SOSExport.exportQueries: " + e.getMessage(), e);
        }
    }

    private String exportQueries(int queryId, ArrayList parameterValues) throws Exception {
        StringBuilder output = new StringBuilder();
        try {
            LOGGER.debug("export_queries: name=\"" + queries.get(queryId).getTag() + "\" query_id=\"" + queryId + "\" key=\"" + queries.get(queryId)
                    .keysToStr() + "\" dependend=\"" + queries.get(queryId).dependRefToStr() + "\" independend=\"" + queries.get(queryId)
                            .indepdRefToStr() + "\"");
            rekursionCnt++;
            String queryStm = substituteQuery(queries.get(queryId).getQuery(), parameterValues);
            HashMap allFieldNames = prepareGetFieldName(queryStm);
            SOSImExportTableFieldTypes fieldTypes = getFieldTypes(queryStm.toString());
            ArrayList result = new ArrayList();
            result = getArray(queryStm.toString());
            output.append(indent(1) + "<" + normalizeTagName(xmlTagname + "_package id=\"") + queries.get(queryId).getTag() + "\">\n");
            if (!result.isEmpty()) {
                output.append(indent(1)).append("<").append(normalizeTagName(xmlTagname + "_meta")).append(">\n");
                output.append(indent(0)).append("<").append(normalizeTagName("table name=\"")).append(queries.get(queryId).getTag()).append(
                        "\" />\n");
                output.append(indent(1)).append("<").append(normalizeTagName("key_fields")).append(">\n");
                for (int i = 0; i < queries.get(queryId).getKeyCnt(); i++) {
                    LOGGER.debug("key_field[" + i + "]=\"" + queries.get(queryId).getKey(i) + "\"");
                    output.append(indent()).append("<").append(normalizeTagName("field name=\"")).append(normalizeFieldName(queries.get(queryId)
                            .getKey(i))).append("\"");
                    output.append(" type=\"").append(fieldTypes.getTypeName(normalizeFieldName(queries.get(queryId).getKey(i)))).append("\"");
                    output.append(" typeID=\"").append(fieldTypes.getTypeId(normalizeFieldName(queries.get(queryId).getKey(i)))).append("\"");
                    output.append(" len=\"").append(fieldTypes.getLength(normalizeFieldName(queries.get(queryId).getKey(i)))).append("\"");
                    output.append(" scale=\"").append(fieldTypes.getScale(normalizeFieldName(queries.get(queryId).getKey(i)))).append("\"");
                    output.append(" />\n");
                }
                output.append(indent(-1)).append(normalizeTagName("</key_fields>")).append("\n");
                // Felder
                output.append(indent(1)).append(normalizeTagName("<fields>")).append("\n");
                Object[] fields = ((HashMap) result.get(0)).keySet().toArray();
                for (int i = 0; i < fields.length; i++) {
                    output.append(indent()).append("<").append(normalizeTagName("field name=\"")).append(normalizeFieldName((String) fields[i]))
                            .append("\"");
                    output.append(" type=\"").append(fieldTypes.getTypeName(normalizeFieldName((String) fields[i]))).append("\"");
                    output.append(" typeID=\"").append(fieldTypes.getTypeId(normalizeFieldName((String) fields[i]))).append("\"");
                    output.append(" len=\"").append(fieldTypes.getLength(normalizeFieldName((String) fields[i]))).append("\"");
                    output.append(" scale=\"").append(fieldTypes.getScale(normalizeFieldName((String) fields[i]))).append("\"");
                    output.append(" />\n");
                }
                fields = null;
                output.append(indent(-1)).append(normalizeTagName("</fields>")).append("\n");
                output.append(indent(-1)).append("</").append(normalizeTagName(xmlTagname + "_meta")).append(">\n");
                output.append(indent(1)).append("<").append(normalizeTagName(xmlTagname + "_data")).append(">\n");
                for (int i = 0; i < result.size(); i++) {
                    HashMap record = (HashMap) result.get(i);
                    LOGGER.trace("get: " + queries.get(queryId).getTag() + " query_id=" + queryId);
                    output.append(indent(1)).append("<").append(normalizeTagName(xmlTagname + "_record name=\"")).append(queries.get(queryId)
                            .getTag()).append("\">\n").append(indent(1)).append("<").append(normalizeTagName(xmlTagname + "_fields")).append(">\n");
                    for (Iterator it = record.keySet().iterator(); it.hasNext();) {
                        String key = it.next().toString();
                        String lobType = null;
                        switch (fieldTypes.getTypeId(normalizeFieldName(key))) {
                        case Types.CLOB:
                        case Types.LONGVARCHAR:
                            lobType = "clob";
                            break;
                        case Types.BLOB:
                        case Types.BINARY:
                        case Types.VARBINARY:
                        case Types.LONGVARBINARY:
                            lobType = "blob";
                            break;
                        default:
                            if (record.get(key) != null) {
                                switch (fieldTypes.getTypeId(normalizeFieldName(key))) {
                                case Types.DATE:
                                case Types.TIMESTAMP:
                                    String val = record.get(key).toString();
                                    if (val.endsWith(".0")) {
                                        record.put(key, val.substring(0, val.length() - 2));
                                    }
                                }
                            }
                            output.append(indent()).append("<").append(normalizeTagName(key)).append(" null=");
                            if (record.get(key) != null) {
                                output.append("\"false\"><![CDATA[").append(asXml(record.get(key).toString())).append("]]>");
                            } else {
                                output.append("\"true\"><![CDATA[]]>");
                            }
                            output.append("</").append(normalizeTagName(key)).append(">\n");
                            break;
                        }
                        if (lobType != null) {
                            int posBegin = new String(queryStm).toLowerCase().indexOf("from");
                            int posEnd = new String(queryStm).toLowerCase().indexOf(" ", posBegin + 5);
                            StringBuilder queryBlobStm = new StringBuilder();
                            String blobFieldName = getBlobFieldName(allFieldNames, key.toString());
                            queryBlobStm.append("SELECT ").append(normalizeFieldName(blobFieldName)).append(" ");
                            if (posEnd < posBegin) {
                                posEnd = queryStm.length();
                            }
                            queryBlobStm.append(queryStm.substring(posBegin, posEnd));
                            String and = " WHERE ";
                            for (int j = 0; j < queries.get(queryId).getKeyCnt(); j++) {
                                String keyFieldName = getKeyFieldName(allFieldNames, queries.get(queryId).getKey(j));
                                queryBlobStm.append(and).append(normalizeFieldName(keyFieldName)).append(" =");
                                queryBlobStm.append(quote(fieldTypes.getTypeId(normalizeFieldName(queries.get(queryId).getKey(j))), (String) record
                                        .get(normalizeFieldName(queries.get(queryId).getKey(j)))));
                                and = " AND ";
                            }
                            byte[] blob = null;
                            if ("blob".equals(lobType)) {
                                blob = connection.getBlob(queryBlobStm.toString());
                            } else {
                                blob = str2bin(connection.getClob(queryBlobStm.toString()));
                            }
                            output.append(indent()).append("<").append(normalizeTagName(key)).append(" null=");
                            if (blob != null && blob.length > 0) {
                                indent(1);
                                output.append("\"false\">\n").append(toHexString(blob, indent(), lineWrap)).append("\n").append(indent(-1));
                            } else {
                                output.append("\"true\">");
                            }
                            output.append("</").append(normalizeTagName(key)).append(">\n");
                        }
                    }
                    output.append(indent(-1)).append("</").append(normalizeTagName(xmlTagname + "_fields")).append(">\n");
                    if (queries.get(queryId).getIndepdRefCnt() > 0) {
                        for (int j = 0; j < queries.get(queryId).getIndepdRefCnt(); j++) {
                            int recQuery = queries.get(queryId).getIndepdRef(j).intValue();
                            LOGGER.debug("recursive independend query: " + queries.get(recQuery).getQuery());
                            output.append(exportQueries(recQuery, queryParams(recQuery, record)));
                        }
                    }
                    if (queries.get(queryId).getDependRefCnt() > 0) {
                        for (int j = 0; j < queries.get(queryId).getDependRefCnt(); j++) {
                            int recQuery = queries.get(queryId).getDependRef(j).intValue();
                            LOGGER.debug("recursive dependend query: " + queries.get(recQuery).getQuery());
                            output.append(exportQueries(recQuery, queryParams(recQuery, record)));
                        }
                    }
                    output.append(indent(-1)).append("</").append(normalizeTagName(xmlTagname + "_record")).append(">\n");
                }
                output.append(indent(-1)).append("</").append(normalizeTagName(xmlTagname + "_data")).append(">\n");
            }
            output.append(indent(-1)).append("</").append(normalizeTagName(xmlTagname + "_package")).append(">\n");
            queries.get(queryId).setDone(true);
            return output.toString();
        } catch (Exception e) {
            throw new Exception("SOSExport.exportQueries: " + e.getMessage(), e);
        }
    }

    private HashMap prepareGetFieldName(String stmt) throws Exception {
        int posBegin = new String(stmt).toUpperCase().indexOf("SELECT");
        int posEnd = new String(stmt).toUpperCase().indexOf("FROM");
        if (posBegin == -1 || posEnd == -1) {
            throw new Exception("sql statement is not valid : " + stmt);
        }
        String selectFields = stmt.substring(posBegin + 6, posEnd).trim().toUpperCase();
        HashMap hm = new HashMap();
        if (!"*".equals(selectFields)) {
            String[] splitFields = selectFields.split(",");
            for (int i = 0; i < splitFields.length; i++) {
                String field = splitFields[i].trim();
                String[] split = field.split(" ");
                int len = split.length;
                if (len > 1) {
                    String lastName = split[len - 1].replace('"', ' ').trim();
                    if (field.indexOf(" AS ") != -1) {
                        String firstName = split[0].replace('"', ' ').trim();
                        hm.put(lastName, firstName);
                    }
                } else {
                    String firstName = field.replace('"', ' ').trim();
                    hm.put(firstName, firstName);
                }
            }
        }
        return hm;
    }

    private String getBlobFieldName(HashMap hm, String sampleFieldName) throws Exception {
        String sample = sampleFieldName.toUpperCase();
        if (hm != null && !hm.isEmpty() && hm.containsKey(sample)) {
            return "\"" + hm.get(sample) + "\" as \"" + sample + "\"";
        }
        return "\"" + sample + "\"";
    }

    private String getKeyFieldName(HashMap hm, String sampleFieldName) throws Exception {
        String sample = sampleFieldName.toUpperCase();
        if (hm != null && !hm.isEmpty() && hm.containsKey(sample)) {
            return "\"" + hm.get(sample) + "\"";
        }
        return "\"" + sample + "\"";
    }

    public ArrayList queryParams(int queryId, HashMap record) throws Exception {
        ArrayList values = new ArrayList();
        try {
            LOGGER.debug("query_params: query_id=" + queryId);
            for (int i = 0; i < queries.get(queryId).getParameterCnt(); i++) {
                LOGGER.debug("param_value[" + i + "] = " + queries.get(queryId).getParameter(i));
                if (record.containsKey(normalizeFieldName(queries.get(queryId).getParameter(i)))) {
                    values.add(record.get(normalizeFieldName(queries.get(queryId).getParameter(i))));
                }
            }
            return values;
        } catch (Exception e) {
            throw new Exception("SOSExport.queryParams: " + e.getMessage(), e);
        }
    }

    private String substituteQuery(String query, ArrayList values) {
        int matches = countStr(query, "?");
        String[] queryParts = query.split("\\?");
        if (matches == 0) {
            return query;
        }
        if (matches > values.size()) {
            throw new IllegalArgumentException("SOSExport.substituteQuery: too few values for substitution");
        }
        StringBuilder queryStm = new StringBuilder();
        for (int i = 0; i < queryParts.length; i++) {
            queryStm.append(queryParts[i]);
            if (i < values.size()) {
                if (values.get(i) == null || "".equals(values.get(i))) {
                    queryStm.append("NULL");
                } else {
                    queryStm.append(values.get(i));
                }
            }
        }
        return queryStm.toString();
    }

    private static int countStr(String string, String part) {
        int index = 0;
        int matches = 0;
        index = string.indexOf(part, index);
        while (index >= 0) {
            matches++;
            index = string.indexOf(part, ++index);
        }
        return matches;
    }

    private ArrayList getArray(String query) throws Exception {
        try {
            connection.executeQuery(query);
            ResultSet rs = connection.getResultSet();
            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            int i, n = rsmd.getColumnCount();
            ArrayList result = new ArrayList();
            while (rs.next()) {
                HashMap row = new HashMap(n);
                for (i = 1; i <= n; i++) {
                    switch (rsmd.getColumnType(i)) {
                    case Types.BIGINT:
                    case Types.DECIMAL:
                    case Types.DOUBLE:
                    case Types.FLOAT:
                    case Types.INTEGER:
                    case Types.REAL:
                    case Types.SMALLINT:
                    case Types.NUMERIC:
                    case Types.TINYINT:
                    case Types.DATE:
                    case Types.TIMESTAMP:
                        row.put(rsmd.getColumnName(i), rs.getString(i));
                        break;
                    case Types.LONGVARCHAR:
                    case Types.BINARY:
                    case Types.BLOB:
                    case Types.CLOB:
                    case Types.LONGVARBINARY:
                    case Types.VARBINARY:
                        row.put(rsmd.getColumnName(i), null);
                        break;
                    default:
                        row.put(rsmd.getColumnName(i), rs.getString(i));
                        break;
                    }
                }
                result.add(row);
            }
            rs.close();
            return result;
        } catch (Exception e) {
            throw new Exception("SOSExport.getArray: " + e.getMessage());
        }
    }

    private SOSImExportTableFieldTypes getFieldTypes(String query) throws Exception {
        try {
            StringBuilder stm = new StringBuilder();
            int index = query.toLowerCase().indexOf("order by");
            if (index >= 0) {
                stm.append(query.substring(0, index - 1));
            } else {
                stm.append(query);
            }
            if (query.toLowerCase().indexOf("where") >= 0) {
                stm.append(" AND 1=0");
            } else {
                stm.append(" WHERE 1=0");
            }
            if (index >= 0) {
                stm.append(query.substring(index - 1));
            }
            connection.executeQuery(stm.toString());
            ResultSet resultSet = connection.getResultSet();
            SOSImExportTableFieldTypes fieldTypes = new SOSImExportTableFieldTypes();
            Map<String, String> fieldDesc = new HashMap<String, String>();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                fieldDesc = connection.fieldDesc(i);
                Integer type = new Integer(fieldDesc.get("columnType"));
                BigInteger size = new BigInteger(fieldDesc.get("columnDisplaySize"));
                Integer scale = new Integer(fieldDesc.get("scale"));
                LOGGER.trace("field_type: name=" + fieldDesc.get("columnName") + " type=" + fieldDesc.get("columnTypeName") + " type_id=" + type
                        + " size=" + size + " scale=" + scale);
                fieldTypes.addField(normalizeFieldName(normalizeFieldName(fieldDesc.get("columnName"))), fieldDesc.get("columnTypeName"), type, size,
                        scale);
            }
            resultSet.close();
            return fieldTypes;
        } catch (Exception e) {
            throw new Exception("SOSExport.getFieldTypes: " + e.getMessage(), e);
        }
    }

    private String quote(int type, String val) {
        if (val == null) {
            return "NULL";
        } else if ("".equals(val)) {
            return "NULL";
        }
        switch (type) {
        case Types.DOUBLE:
            if ("null".equalsIgnoreCase(val)) {
                val = "NULL";
            }
            return val;
        case Types.BIGINT:
        case Types.DECIMAL:
        case Types.FLOAT:
        case Types.INTEGER:
        case Types.REAL:
        case Types.SMALLINT:
        case Types.NUMERIC:
        case Types.TINYINT:
            if ("null".equalsIgnoreCase(val)) {
                val = "NULL";
            }
            return val;
        case Types.DATE:
        case Types.TIMESTAMP:
            return "%timestamp_iso('" + val + "')";
        default:
            val = val.replaceAll("'", "''");
            if (connection instanceof SOSMySQLConnection) {
                val = val.replaceAll("\\\\", "\\\\\\\\");
            }
            return "'" + val + "'";
        }
    }

    private String indent(int indent) {
        int curIndent = xmlIndent;
        xmlIndent += indent;
        StringBuilder output = new StringBuilder(curIndent);
        if (indent > 0) {
            for (int i = 0; i < curIndent; i++) {
                output.append(xmlIndentation);
            }
        } else {
            for (int i = 0; i < xmlIndent; i++) {
                output.append(xmlIndentation);
            }
        }
        return output.toString();
    }

    private String indent() {
        return indent(0);
    }

    private String normalizeTagName(String tag) {
        if ("strtoupper".equalsIgnoreCase(normalizeTagName)) {
            return tag.toUpperCase();
        } else {
            return tag.toLowerCase();
        }
    }

    private String normalizeFieldName(String field) {
        if ("strtoupper".equalsIgnoreCase(normalizeFieldName)) {
            return field.toUpperCase();
        } else {
            return field.toLowerCase();
        }
    }

    private static String asXml(String str) {
        return str;
    }

    private static byte[] str2bin(String str) {
        return str.getBytes();
    }

    private static String toHexString(byte[] b, String indent, int wrap, Writer fw) throws Exception {
        int length = b.length * 2;
        int indentLength = indent.length();
        if (wrap > 0) {
            int indents = length / (wrap - indentLength);
            length = length + indents * (indentLength + "\n".length());
            length += indentLength;
        }
        int line = indentLength;
        if (wrap > 0) {
            fw.write(indent);
        }
        for (int i = 0; i < b.length; i++) {
            fw.write(HEX_CHAR[(b[i] & 0xf0) >>> 4]);
            fw.write(HEX_CHAR[b[i] & 0x0f]);
            line += 2;
            if (wrap > 0 && line >= wrap && i < (b.length - 1)) {
                fw.write("\n" + indent);
                line = indentLength;
            }
        }
        return "";
    }

    private static String toHexString(byte[] b, String indent, int wrap) {
        int length = b.length * 2;
        int indentLength = indent.length();
        if (wrap > 0) {
            int indents = length / (wrap - indentLength);
            length = length + indents * (indentLength + "\n".length());
            length += indentLength;
        }
        int line = indentLength;
        StringBuilder sb = new StringBuilder(length);
        if (wrap > 0) {
            sb.append(indent);
        }
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_CHAR[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_CHAR[b[i] & 0x0f]);
            line += 2;
            if (wrap > 0 && line >= wrap && i < (b.length - 1)) {
                sb.append("\n" + indent);
                line = indentLength;
            }
        }
        return sb.toString();
    }

    private class Query {

        private String tag = null;
        private ArrayList key = new ArrayList();
        private String query = null;
        private ArrayList parameters = new ArrayList();
        private ArrayList dependRefs = new ArrayList();
        private ArrayList indepdRefs = new ArrayList();
        private boolean done = false;
        private boolean dependent = false;
        private String operation = null;
        HashMap fieldsKeys = null;

        public Query(String tag, String key, String query, String parameters, int dependRef) {
            this.tag = tag;
            this.key.addAll(Arrays.asList(key.split(",")));
            this.query = query;
            this.parameters.addAll(Arrays.asList(parameters.split(",")));
            this.dependRefs.add(new Integer(dependRef));
        }

        public Query(String tag, String key, String query, String parameters) throws Exception {
            this.tag = tag;
            if (key != null && !"".equals(key)) {
                this.key.addAll(Arrays.asList(key.split(",")));
            }
            query = query;
            if (parameters != null) {
                this.parameters.addAll(Arrays.asList(parameters.split(",")));
            }
        }

        public Query(String tag, String key, String query, String parameters, String operation, HashMap keys4Delete) throws Exception {
            this.tag = tag;
            if (key != null && !"".equals(key)) {
                this.key.addAll(Arrays.asList(key.split(",")));
            }
            this.query = query;
            this.fieldsKeys = keys4Delete;
            this.operation = operation;
            if (parameters != null) {
                this.parameters.addAll(Arrays.asList(parameters.split(",")));
            }
        }

        public Integer getDependRef(int index) {
            return (Integer) dependRefs.get(index);
        }

        public int getDependRefCnt() {
            return dependRefs.size();
        }

        public void addDependRef(Integer dependRef) {
            dependRefs.add(dependRef);
        }

        public String dependRefToStr() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < dependRefs.size(); i++) {
                sb.append(dependRefs.get(i).toString());
                if (i < dependRefs.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        public Integer getIndepdRef(int index) {
            return (Integer) indepdRefs.get(index);
        }

        public int getIndepdRefCnt() {
            return indepdRefs.size();
        }

        public void addIndepdRef(Integer indepdRef) {
            this.indepdRefs.add(indepdRef);
        }

        public String indepdRefToStr() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < indepdRefs.size(); i++) {
                sb.append(indepdRefs.get(i).toString());
                if (i < indepdRefs.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        public String getKey(int index) {
            return (String) key.get(index);
        }

        public int getKeyCnt() {
            return key.size();
        }

        public void addKey(String key) {
            this.key.add(key);
        }

        public String keysToStr() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < key.size(); i++) {
                sb.append((String) key.get(i));
                if (i < key.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        public String getParameter(int index) {
            return (String) parameters.get(index);
        }

        public int getParameterCnt() {
            return parameters.size();
        }

        public void addParameter(String parameter) {
            this.parameters.add(parameter);
        }

        public String parametersToStr() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parameters.size(); i++) {
                sb.append((String) parameters.get(i));
                if (i < parameters.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public boolean isDependent() {
            return dependent;
        }

        public void setDependent(boolean dependent) {
            this.dependent = dependent;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public HashMap getFieldsKeys() {
            return fieldsKeys;
        }

        public void setFieldsKeys(HashMap fieldsKeys) {
            this.fieldsKeys = fieldsKeys;
        }
    }

    private class Queries {

        private ArrayList list = new ArrayList();

        public void add(Query query) throws Exception {
            this.list.add(query);
        }

        public Query get(int index) throws IndexOutOfBoundsException {
            return (Query) list.get(index);
        }

        public void clear() throws UnsupportedOperationException {
            this.list.clear();
        }

        public int cnt() {
            return list.size();
        }
    }

}