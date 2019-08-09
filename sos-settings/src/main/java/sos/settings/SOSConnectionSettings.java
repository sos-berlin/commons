package sos.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import sos.connection.SOSConnection;
import sos.connection.SOSMSSQLConnection;
import sos.connection.SOSSybaseConnection;
import sos.util.SOSClassUtil;
import sos.util.SOSString;

/** @author Ghassan Beydoun
 * @author Andreas P�schel */
public class SOSConnectionSettings extends sos.settings.SOSSettings {

    protected String application = "";
    protected final static String APPLICATION = "APPLICATION";
    protected final static String SECTION = "SECTION";
    protected final static String NAME = "NAME";
    protected final static String VALUE = "VALUE";
    protected final static String TITLE = "TITLE";
    protected final static String LONG_VALUE = "LONG_VALUE";
    protected String entryCounterApplication = "counter";
    protected String entryCounterSection = "counter";
    protected String entryOrder = "NAME";
    protected String entrySettingTitle = "TITLE";
    protected String entrySchemaSection = "**schema**";
    protected String defaultDocumentFileName = "settings_file.dat";
    private static final Logger LOGGER = Logger.getLogger(SOSConnectionSettings.class);
    private SOSConnection sosConnection;

    public SOSConnectionSettings(SOSConnection sosConnection, String source) throws Exception {
        super(source);
        if (sosConnection == null) {
            throw new Exception(SOSClassUtil.getMethodName() + " invalid sosConnection !!.");
        }
        this.sosConnection = sosConnection;
    }

    public SOSConnectionSettings(SOSConnection sosConnection, String source, String application) throws Exception {
        super(source);
        if (sosConnection == null) {
            throw new Exception(SOSClassUtil.getMethodName() + ": invalid sosConnection !!.");
        }
        if (SOSString.isEmpty(application)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": invalid application name !!.");
        }
        this.sosConnection = sosConnection;
        this.application = application;
    }

    public SOSConnectionSettings(SOSConnection sosConnection, String source, String application, String section) throws Exception {
        super(source, section);
        if (sosConnection == null) {
            throw new Exception(SOSClassUtil.getMethodName() + ": invalid sosConnection name.");
        }
        if (SOSString.isEmpty(application)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": invalid application name.");
        }
        this.sosConnection = sosConnection;
        this.application = application;
    }

    public Properties getSection(String application, String section) throws Exception {
        Properties entries = new Properties();
        StringBuilder query = null;
        if (SOSString.isEmpty(application)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": application has no value!");
        }
        if (SOSString.isEmpty(section)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": section has no value!");
        }
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
        query = new StringBuilder();
        if (this.ignoreCase) {
            query.append("SELECT \"").append(NAME).append("\",\"").append(VALUE).append("\" FROM ").append(source).append(" WHERE %lcase(\"").append(
                    APPLICATION).append("\") = '").append(application.toLowerCase()).append("' AND %lcase(\"").append(SECTION).append("\") = '").append(
                    section.toLowerCase()).append("' AND \"").append(SECTION).append("\" <> \"").append(NAME).append("\"");
        } else {
            query.append("SELECT \"").append(NAME).append("\",\"").append(VALUE).append("\" FROM ").append(source).append(" WHERE \"").append(
                    APPLICATION).append("\" = '").append(application).append("' AND \"").append(SECTION).append("\" = '").append(section).append(
                    "' AND \"").append(SECTION).append("\" <> \"").append(NAME).append("\"");
        }
        LOGGER.debug(".. query: " + query.toString());
        try {
            entries = sosConnection.getArrayAsProperties(query.toString());
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString());
        }
        return entries;
    }

    public Properties getSection(String section) throws Exception {
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
        if (SOSString.isEmpty(section)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": section has no value!");
        }
        if (this.application == null || this.application.isEmpty()) {
            throw new Exception(SOSClassUtil.getMethodName() + ": application has no value!");
        }
        return getSection(application, section);
    }

    public Properties getSection() throws Exception {
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
        if (SOSString.isEmpty(application)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": application has no value!");
        }
        if (SOSString.isEmpty(section)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": section has no value!");
        }
        return getSection(application, section);
    }

    public List<String> getSections(String application) throws Exception {
        List<String> sections = new ArrayList<String>();
        StringBuilder query = null;
        if (SOSString.isEmpty(application)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": application has no value!");
        }
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
        query = new StringBuilder();
        if (this.ignoreCase) {
            query.append("SELECT \"").append(SECTION).append("\" FROM ").append(source).append(" WHERE %lcase(\"").append(APPLICATION).append(
                    "\") = '").append(application.toLowerCase()).append("' AND \"").append(SECTION).append("\" <> \"").append(APPLICATION).append(
                    "\"");
        } else {
            query.append("SELECT \"").append(SECTION).append("\" FROM ").append(source).append(" WHERE \"").append(APPLICATION).append("\" = '").append(
                    application).append("' AND \"").append(SECTION).append("\" <> \"").append(APPLICATION).append("\"");
        }
        LOGGER.debug(".. query: " + query.toString());
        try {
            sections = sosConnection.getArrayValue(query.toString());
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        }
        return sections;
    }

    public List<String> getSections() throws Exception {
        if (SOSString.isEmpty(application)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": application has no value!");
        }
        return this.getSections(application);
    }

    public String getSectionEntry(String entry) throws Exception {
        return this.getSectionEntry(application, section, entry);
    }

    public String getSectionEntry(String application, String section, String entry) throws Exception {
        try {
            LOGGER.debug("calling " + SOSClassUtil.getMethodName());
            String query = this.getSectionEntryStatement(application, section, entry, VALUE);
            LOGGER.debug(".. query: " + query);
            return sosConnection.getSingleValue(query);
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        }
    }

    public byte[] getSectionEntryDocument(String entry) throws Exception {
        return this.getSectionEntryDocument(application, section, entry);
    }

    public byte[] getSectionEntryDocument(String application, String section, String entry) throws Exception {
        try {
            LOGGER.debug("calling " + SOSClassUtil.getMethodName());
            String query = this.getSectionEntryStatement(application, section, entry, LONG_VALUE);
            LOGGER.debug(".. query: " + query);
            return sosConnection.getBlob(query);
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        }
    }

    public long getSectionEntryDocumentAsFile(String entry, String fileName) throws Exception {
        return this.getSectionEntryDocumentAsFile(application, section, entry, fileName);
    }

    public long getSectionEntryDocumentAsFile(String application, String section, String entry, String fileName) throws Exception {
        try {
            LOGGER.debug("calling " + SOSClassUtil.getMethodName());
            String query = this.getSectionEntryStatement(application, section, entry, LONG_VALUE);
            LOGGER.debug(".. query: " + query);
            return sosConnection.getBlob(query, fileName);
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        }
    }

    private String getSectionEntryStatement(String application, String section, String entry, String field) throws Exception {
        StringBuilder query = null;
        try {
            query = new StringBuilder();
            if (this.ignoreCase) {
                query.append("SELECT \"").append(field).append("\" FROM ").append(source).append(" WHERE %lcase(\"").append(APPLICATION).append(
                        "\") = '").append(application.toLowerCase()).append("' AND %lcase(\"").append(SECTION).append("\") = '").append(
                        section.toLowerCase()).append("' AND %lcase(\"").append(NAME).append("\") = '").append(entry.toLowerCase()).append("'");
            } else {
                query.append("SELECT \"").append(field).append("\" FROM ").append(source).append(" WHERE \"").append(APPLICATION).append("\" = '").append(
                        application).append("' AND \"").append(SECTION).append("\" = '").append(section).append("' AND \"").append(NAME).append(
                        "\" = '").append(entry).append("'");
            }
            return query.toString();
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        }
    }

    public boolean initSequence(String application, String section, String entry, String createdBy) throws Exception {
        boolean created = false;
        String counter = null;
        StringBuilder query = null;
        String initValue = "1";
        int inserted = 0;
        try {
            LOGGER.debug("calling " + SOSClassUtil.getMethodName());
            try {
                query = new StringBuilder();
                if (this.ignoreCase) {
                    query.append("SELECT \"").append(VALUE).append("\" FROM ").append(source).append(" WHERE %lcase(\"").append(APPLICATION).append(
                            "\") = '").append(application.toLowerCase()).append("' AND %lcase(\"").append(SECTION).append("\") = '").append(
                            section.toLowerCase()).append("' AND %lcase(\"").append(NAME).append("\") = '").append(entry.toLowerCase()).append("'");
                } else {
                    query.append("SELECT \"").append(VALUE).append("\" FROM ").append(source).append(" WHERE \"").append(APPLICATION).append("\" = '").append(
                            application).append("' AND \"").append(SECTION).append("\" = '").append(section).append("' AND \"").append(NAME).append(
                            "\" = '").append(entry).append("'");
                }
                LOGGER.debug("query: " + query.toString());
                counter = sosConnection.getSingleValue(query.toString());
                LOGGER.debug(".. current counter value: " + counter);
            } catch (Exception e) {
                created = false;
                throw e;
            }
            if (counter != null && !"".equalsIgnoreCase(counter)) {
                return true;
            }
            try {
                query = new StringBuilder();
                query.append("INSERT INTO ").append(source).append("(\"").append(APPLICATION).append("\",\"").append(SECTION).append("\",\"").append(
                        NAME).append("\",\"").append(VALUE).append("\",\"").append("CREATED_BY").append("\",\"").append("MODIFIED_BY").append(
                        "\",\"CREATED\",\"MODIFIED\") VALUES('").append(application).append("','").append(section).append("','").append(entry).append(
                        "','").append(initValue).append("','").append(createdBy).append("','").append(createdBy).append("',%now,%now").append(")");
                LOGGER.debug(".. query: " + query.toString());
                inserted = sosConnection.executeUpdate(query.toString());
                if (inserted > 0) {
                    created = true;
                }
            } catch (Exception e) {
                created = false;
            }
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        }
        return created;
    }

    public int getLockedSequence(String application, String section, String entry) throws Exception {
        StringBuilder query = null;
        String updlockFrom = "";
        String updlockWhere = "";
        int sequence = -1;
        try {
            LOGGER.debug("calling " + SOSClassUtil.getMethodName());
            updlockFrom = (this.sosConnection instanceof SOSMSSQLConnection || this.sosConnection instanceof SOSSybaseConnection) ? "%updlock" : "";
            updlockWhere = "".equals(updlockFrom) ? "%updlock" : "";
            boolean autoCommit = this.sosConnection.getAutoCommit();
            this.sosConnection.setAutoCommit(false);
            query = new StringBuilder();
            if (this.ignoreCase) {
                query.append("SELECT \"").append(VALUE).append("\" FROM ").append(source).append(" ").append(updlockFrom).append(" WHERE %lcase(\"").append(
                        APPLICATION).append("\") = '").append(application.toLowerCase()).append("' AND %lcase(\"").append(SECTION).append("\") = '").append(
                        section.toLowerCase()).append("' AND %lcase(\"").append(NAME).append("\") = '").append(entry.toLowerCase()).append("' ").append(
                        updlockWhere);
            } else {
                query.append("SELECT \"").append(VALUE).append("\" FROM ").append(source).append(" ").append(updlockFrom).append(" WHERE \"").append(
                        APPLICATION).append("\" = '").append(application).append("' AND \"").append(SECTION).append("\" = '").append(section).append(
                        "' AND \"").append(NAME).append("\" = '").append(entry).append("' ").append(updlockWhere);
            }
            LOGGER.debug(SOSClassUtil.getMethodName() + ": get result query: " + query.toString());
            sequence = Integer.valueOf(sosConnection.getSingleValue(query.toString())).intValue() + 1;
            query = new StringBuilder();
            if (this.ignoreCase) {
                query.append("UPDATE ").append(source).append(" SET \"").append(VALUE).append("\"=%cast(%cast(\"").append(VALUE).append(
                        "\" integer)+1 varchar) WHERE %lcase(\"").append(APPLICATION).append("\") = '").append(application.toLowerCase()).append(
                        "' AND %lcase(\"").append(SECTION).append("\") = '").append(section.toLowerCase()).append("' AND %lcase(\"").append(NAME).append(
                        "\") = '").append(entry.toLowerCase()).append("'");
            } else {
                query.append("UPDATE ").append(source).append(" SET \"").append(VALUE).append("\"=%cast(%cast(\"").append(VALUE).append(
                        "\" integer)+1 varchar) WHERE \"").append(APPLICATION).append("\" = '").append(application).append("' AND \"").append(SECTION).append(
                        "\" = '").append(section).append("' AND \"").append(NAME).append("\" = '").append(entry).append("'");
            }
            LOGGER.debug(SOSClassUtil.getMethodName() + ": query: " + query.toString());
            sosConnection.execute(query.toString());
            LOGGER.debug(SOSClassUtil.getMethodName() + ": successfully executed: " + query.toString());
            this.sosConnection.setAutoCommit(autoCommit);
        } catch (Exception e) {
            LOGGER.debug(SOSClassUtil.getMethodName() + ": an error occurred : " + e.toString());
            throw e;
        }
        return sequence;
    }

    public String getLockedSequenceAsString(String application, String section, String entry) throws Exception {
        StringBuilder query = null;
        String updlockFrom = "";
        String updlockWhere = "";
        String sequence = "";
        try {
            LOGGER.debug("calling " + SOSClassUtil.getMethodName());
            updlockFrom = (this.sosConnection instanceof SOSMSSQLConnection || this.sosConnection instanceof SOSSybaseConnection) ? "%updlock" : "";
            updlockWhere = "".equals(updlockFrom) ? "%updlock" : "";
            boolean autoCommit = this.sosConnection.getAutoCommit();
            this.sosConnection.setAutoCommit(false);
            query = new StringBuilder();
            if (this.ignoreCase) {
                query.append("SELECT \"").append(VALUE).append("\" FROM ").append(source).append(" ").append(updlockFrom).append(" WHERE %lcase(\"").append(
                        APPLICATION).append("\") = '").append(application.toLowerCase()).append("' AND %lcase(\"").append(SECTION).append("\") = '").append(
                        section.toLowerCase()).append("' AND %lcase(\"").append(NAME).append("\") = '").append(entry.toLowerCase()).append("' ").append(
                        updlockWhere);
            } else {
                query.append("SELECT \"").append(VALUE).append("\" FROM ").append(source).append(" ").append(updlockFrom).append(" WHERE \"").append(
                        APPLICATION).append("\" = '").append(application).append("' AND \"").append(SECTION).append("\" = '").append(section).append(
                        "' AND \"").append(NAME).append("\" = '").append(entry).append("' ").append(updlockWhere);
            }
            LOGGER.debug(SOSClassUtil.getMethodName() + ": get result query: " + query.toString());
            sequence = String.valueOf(Integer.parseInt(sosConnection.getSingleValue(query.toString())) + 1);
            query = new StringBuilder();
            if (this.ignoreCase) {
                query.append("UPDATE ").append(source).append(" SET \"").append(VALUE).append("\"=%cast(%cast(\"").append(VALUE).append(
                        "\" integer)+1 varchar) WHERE %lcase(\"").append(APPLICATION).append("\") = '").append(application.toLowerCase()).append(
                        "' AND %lcase(\"").append(SECTION).append("\") = '").append(section.toLowerCase()).append("' AND %lcase(\"").append(NAME).append(
                        "\") = '").append(entry.toLowerCase()).append("'");
            } else {
                query.append("UPDATE ").append(source).append(" SET \"").append(VALUE).append("\"=%cast(%cast(\"").append(VALUE).append(
                        "\" integer)+1 varchar) WHERE \"").append(APPLICATION).append("\" = '").append(application).append("' AND \"").append(SECTION).append(
                        "\" = '").append(section).append("' AND \"").append(NAME).append("\" = '").append(entry).append("'");
            }
            LOGGER.debug(SOSClassUtil.getMethodName() + ": query: " + query.toString());
            sosConnection.execute(query.toString());
            LOGGER.debug(SOSClassUtil.getMethodName() + ": successfully executed: " + query.toString());
            this.sosConnection.setAutoCommit(autoCommit);
        } catch (Exception e) {
            LOGGER.debug(SOSClassUtil.getMethodName() + ": an error occurred : " + e.toString());
            throw e;
        }
        return sequence;
    }

    public int getSequence(String application, String section, String entry) throws Exception {
        StringBuilder query = null;
        int sequence = -1;
        try {
            LOGGER.debug("calling " + SOSClassUtil.getMethodName());
            query = new StringBuilder();
            if (this.ignoreCase) {
                query.append("UPDATE ").append(source).append(" SET \"").append(VALUE).append("\"=%cast(%cast(\"").append(VALUE).append(
                        "\" integer)+1 varchar) WHERE %lcase(\"").append(APPLICATION).append("\") = '").append(application.toLowerCase()).append(
                        "' AND %lcase(\"").append(SECTION).append("\") = '").append(section.toLowerCase()).append("' AND %lcase(\"").append(NAME).append(
                        "\") = '").append(entry.toLowerCase()).append("'");
            } else {
                query.append("UPDATE ").append(source).append(" SET \"").append(VALUE).append("\"=%cast(%cast(\"").append(VALUE).append(
                        "\" integer)+1 varchar) WHERE \"").append(APPLICATION).append("\" = '").append(application).append("' AND \"").append(SECTION).append(
                        "\" = '").append(section).append("' AND \"").append(NAME).append("\" = '").append(entry).append("'");
            }
            LOGGER.debug(SOSClassUtil.getMethodName() + ": query: " + query.toString());
            sosConnection.execute(query.toString());
            LOGGER.debug(SOSClassUtil.getMethodName() + ": successfully executed: " + query.toString());
            query = new StringBuilder();
            if (this.ignoreCase) {
                query.append("SELECT \"").append(VALUE).append("\" FROM ").append(source).append(" WHERE %lcase(\"").append(APPLICATION).append(
                        "\") = '").append(application.toLowerCase()).append("' AND %lcase(\"").append(SECTION).append("\") = '").append(
                        section.toLowerCase()).append("' AND %lcase(\"").append(NAME).append("\") = '").append(entry.toLowerCase()).append("'");
            } else {
                query.append("SELECT \"").append(VALUE).append("\" FROM ").append(source).append(" WHERE \"").append(APPLICATION).append("\" = '").append(
                        application).append("' AND \"").append(SECTION).append("\" = '").append(section).append("' AND \"").append(NAME).append(
                        "\" = '").append(entry).append("'");
            }
            LOGGER.debug(SOSClassUtil.getMethodName() + ": get result query: " + query.toString());
            sequence = Integer.valueOf(sosConnection.getSingleValue(query.toString())).intValue();
        } catch (Exception e) {
            LOGGER.debug(SOSClassUtil.getMethodName() + ": an error occurred : " + e.toString());
            throw e;
        }
        return sequence;
    }

    public String getSequenceAsString(String application, String section, String entry) throws Exception {
        StringBuilder query = null;
        String sequence = "";
        try {
            LOGGER.debug("calling " + SOSClassUtil.getMethodName());
            query = new StringBuilder();
            if (this.ignoreCase) {
                query.append("UPDATE ").append(source).append(" SET \"").append(VALUE).append("\"=%cast(%cast(\"").append(VALUE).append(
                        "\" integer)+1 varchar) WHERE %lcase(\"").append(APPLICATION).append("\") = '").append(application.toLowerCase()).append(
                        "' AND %lcase(\"").append(SECTION).append("\") = '").append(section.toLowerCase()).append("' AND %lcase(\"").append(NAME).append(
                        "\") = '").append(entry.toLowerCase()).append("'");
            } else {
                query.append("UPDATE ").append(source).append(" SET \"").append(VALUE).append("\"=%cast(%cast(\"").append(VALUE).append(
                        "\" integer)+1 varchar) WHERE \"").append(APPLICATION).append("\" = '").append(application).append("' AND \"").append(SECTION).append(
                        "\" = '").append(section).append("' AND \"").append(NAME).append("\" = '").append(entry).append("'");
            }
            LOGGER.debug(SOSClassUtil.getMethodName() + ": query: " + query.toString());
            sosConnection.execute(query.toString());
            LOGGER.debug(SOSClassUtil.getMethodName() + ": successfully executed: " + query.toString());
            query = new StringBuilder();
            if (this.ignoreCase) {
                query.append("SELECT \"").append(VALUE).append("\" FROM ").append(source).append(" WHERE %lcase(\"").append(APPLICATION).append(
                        "\") = '").append(application.toLowerCase()).append("' AND %lcase(\"").append(SECTION).append("\") = '").append(
                        section.toLowerCase()).append("' AND %lcase(\"").append(NAME).append("\") = '").append(entry.toLowerCase()).append("'");
            } else {
                query.append("SELECT \"").append(VALUE).append("\" FROM ").append(source).append(" WHERE \"").append(APPLICATION).append("\" = '").append(
                        application).append("' AND \"").append(SECTION).append("\" = '").append(section).append("' AND \"").append(NAME).append(
                        "\" = '").append(entry).append("'");
            }
            LOGGER.debug(SOSClassUtil.getMethodName() + ": get result query: " + query.toString());
            sequence = sosConnection.getSingleValue(query.toString());
        } catch (Exception e) {
            LOGGER.debug(SOSClassUtil.getMethodName() + ": an error occurred : " + e.toString());
            throw e;
        }
        return sequence;
    }

    public void setKeysToLowerCase() throws Exception {
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
        sosConnection.setKeysToLowerCase();
        LOGGER.debug(".. now keys set to lower case.");
    }

    public void setKeysToUpperCase() throws Exception {
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
        sosConnection.setKeysToUpperCase();
        LOGGER.debug(".. now keys set to upper case.");
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public boolean getIgnoreCase() {
        return this.ignoreCase;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getEntryCounterApplication() {
        return entryCounterApplication;
    }

    public void setEntryCounterApplication(String entryCounterApplication) {
        this.entryCounterApplication = entryCounterApplication;
    }

    public String getEntryCounterSection() {
        return entryCounterSection;
    }

    public void setEntryCounterSection(String entryCounterSection) {
        this.entryCounterSection = entryCounterSection;
    }

    public String getEntryOrder() {
        return entryOrder;
    }

    public void setEntryOrder(String entryOrder) {
        this.entryOrder = entryOrder;
    }

    public String getEntrySchemaSection() {
        return entrySchemaSection;
    }

    public void setEntrySchemaSection(String entrySchemaSection) {
        this.entrySchemaSection = entrySchemaSection;
    }

    public String getEntrySettingTitle() {
        return entrySettingTitle;
    }

    public void setEntrySettingTitle(String entrySettingTitle) {
        this.entrySettingTitle = entrySettingTitle;
    }

    public String getDefaultDocumentFileName() {
        return defaultDocumentFileName;
    }

    public void setDefaultDocumentFileName(String fileName) {
        this.defaultDocumentFileName = fileName;
    }

}