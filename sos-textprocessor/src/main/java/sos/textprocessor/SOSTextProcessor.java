package sos.textprocessor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import sos.settings.SOSConnectionSettings;
import sos.util.SOSClassUtil;
import sos.util.SOSLogger;

public abstract class SOSTextProcessor {

    protected SOSConnectionSettings settings = null;
    protected SOSLogger logger = null;
    protected String language = "de";
    protected boolean hasLocalizedTemplates = false;
    protected HashMap scriptLanguages = new HashMap();
    protected String scriptLanguage = "javascript";
    protected String dateFormat = "dd.MM.yyyy";
    protected String datetimeFormat = "dd.MM.yyyy HH:mm";
    protected HashMap dateFormats = new HashMap();
    protected HashMap datetimeFormats = new HashMap();
    protected HashMap replacements = new HashMap();
    protected String templateApplicationName = "";
    protected String templateSectionName = "";
    protected String templateName = "";
    protected File templateFile = null;
    protected Properties templates = null;
    protected Properties templateScriptLanguages = null;
    protected String templateScriptLanguage = null;
    protected String templateContent = "";
    protected Properties scripts = null;
    protected String scriptApplicationName = "";
    protected String scriptSectionName = "";
    protected File documentFile = null;
    protected String documentContent = "";
    protected boolean forceReload = false;

    public SOSTextProcessor() throws Exception {
        this.init();
    }

    public SOSTextProcessor(SOSLogger logger) throws Exception {
        this.setLogger(logger);
        this.init();
    }

    public SOSTextProcessor(SOSConnectionSettings settings) throws Exception {
        this.setSettings(settings);
        this.init();
    }

    public SOSTextProcessor(SOSConnectionSettings settings, SOSLogger logger) throws Exception {
        this.setSettings(settings);
        this.setLogger(logger);
        this.init();
    }

    public SOSTextProcessor(SOSConnectionSettings settings, String templateSectionName, String templateApplicationName) throws Exception {
        this.setSettings(settings);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);
        this.init();
    }

    public SOSTextProcessor(SOSConnectionSettings settings, SOSLogger logger, String templateSectionName, String templateApplicationName)
            throws Exception {
        this.setSettings(settings);
        this.setLogger(logger);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);
        this.init();
    }

    public void init() throws Exception {
        this.dateFormats.put("de", "dd.MM.yyyy");
        this.dateFormats.put("en", "MM/dd/yyyy");
        this.datetimeFormats.put("de", "dd.MM.yyyy HH:mm");
        this.datetimeFormats.put("en", "MM/dd/yyyy HH:mm");
        this.scriptLanguages = new HashMap();
        this.scriptLanguages.put("javascript", "Javascript");
        this.scriptLanguages.put("perlscript", "PerlScript");
        this.scriptLanguages.put("vbscript", "VBScript");
        this.initLanguage();
    }

    public void init(SOSConnectionSettings settings) throws Exception {
        this.setSettings(settings);
        this.init();
    }

    public void initLanguage() throws Exception {
        if (this.getDateFormats().containsKey(this.getLanguage()) && this.getDatetimeFormats().containsKey(this.getLanguage())) {
            this.setDateFormat(this.getDateFormats().get(this.getLanguage()).toString());
            this.setDatetimeFormat(this.getDatetimeFormats().get(this.getLanguage()).toString());
        } else {
            this.setDateFormat(this.getDateFormats().get("de").toString());
            this.setDatetimeFormat(this.getDatetimeFormats().get("de").toString());
        }
    }

    public String getTemplate(String templateName) throws Exception {
        if (this.getLogger() != null) {
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " begin: template=" + templateName);
        }
        this.setTemplateName(templateName);
        if (this.templates == null || !this.templates.containsKey(templateName)) {
            this.loadTemplate(templateName);
        }
        if (this.templates == null || !this.templates.containsKey(templateName)) {
            throw new Exception("template not found: " + templateName);
        }
        this.templateContent = (this.templates.get(templateName) == null) ? null : this.templates.get(templateName).toString();
        if (this.templateScriptLanguages == null) {
            this.templateScriptLanguages = new Properties();
        }
        if (!this.templateScriptLanguages.containsKey(templateName)) {
            this.templateScriptLanguage = "javascript";
        } else {
            this.templateScriptLanguage = this.templateScriptLanguages.get(templateName).toString();
        }
        if (this.templateScriptLanguage == null || this.templateScriptLanguage.isEmpty()) {
            this.templateScriptLanguage = "javascript";
        }
        if (this.getLogger() != null) {
            this.getLogger().debug9(SOSClassUtil.getMethodName() + ": templateContent=" + this.templateContent);
            this.getLogger().debug9(SOSClassUtil.getMethodName() + ": templateScriptLanguage=" + this.templateScriptLanguage);
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " end");
        }
        return this.templateContent;
    }

    public Properties getTemplates() throws Exception {
        Object key = null;
        Object value = null;
        if (!this.isForceReload() && this.templates != null && !this.templates.isEmpty()) {
            return this.templates;
        }
        if (this.settings == null) {
            throw new Exception("settings have not been loaded.");
        }
        if (this.getTemplateApplicationName() == null || this.getTemplateApplicationName().isEmpty()) {
            throw new Exception("no template application name has been given for settings.");
        }
        if (this.getTemplateSectionName() == null || this.getTemplateSectionName().isEmpty()) {
            throw new Exception("no template section name has been given for settings.");
        }
        this.setTemplates(this.getSettings().getSection(this.getTemplateApplicationName(),
                this.getTemplateSectionName() + (this.hasLocalizedTemplates() ? "_" + this.getLanguage() : "")));
        if (this.templates == null || this.templates.isEmpty()) {
            return this.templates;
        }
        Iterator keys = this.templates.keySet().iterator();
        while (keys.hasNext()) {
            key = keys.next();
            if (key != null) {
                value = this.templates.get(key.toString());
                if (value != null && value.toString() != null && value.toString().trim().isEmpty()) {
                    // retrieve the document content from this setting
                    value = this.loadTemplateDocument(key.toString());
                    this.templates.put(key.toString(), value == null ? "" : value.toString());
                }
            }
        }
        return this.templates;
    }

    public Properties getTemplates(boolean forceReload) throws Exception {
        this.setForceReload(forceReload);
        return this.getTemplates();
    }

    public Properties getTemplates(SOSConnectionSettings settings) throws Exception {
        this.setSettings(settings);
        return this.getTemplates();
    }

    public Properties getTemplates(String templateSectionName, String templateApplicationName) throws Exception {
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);
        return this.getTemplates();
    }

    public Properties getTemplates(SOSConnectionSettings settings, String templateSectionName, String templateApplicationName) throws Exception {
        this.setSettings(settings);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);
        return this.getTemplates();
    }

    protected String loadTemplate(String templateName) throws Exception {
        if (this.getLogger() != null) {
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " begin: templateName=" + templateName);
        }
        this.setTemplateName(templateName);
        this.templateContent =
                this.getSettings().getSectionEntry(this.getTemplateApplicationName(),
                        this.getTemplateSectionName() + (this.hasLocalizedTemplates() ? "_" + this.getLanguage() : ""), templateName);
        if (this.getLogger() != null) {
            this.getLogger().debug9(SOSClassUtil.getMethodName() + ": templateContent=" + this.templateContent);
        }
        if (this.templateContent == null || this.templateContent.trim().isEmpty()) {
            this.loadTemplateDocument(templateName);
        } else {
            if (this.templates == null) {
                this.templates = new Properties();
            }
            this.templates.put(templateName, this.templateContent);
            this.checkTemplateScriptLanguage(templateName);
        }
        if (this.getLogger() != null) {
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " end");
        }
        return this.templateContent;
    }

    protected String loadTemplateDocument(String templateName) throws Exception {
        if (this.getLogger() != null) {
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " begin: templateName=" + templateName);
        }
        this.setTemplateName(templateName);
        Object content =
                this.getSettings().getSectionEntryDocument(this.getTemplateApplicationName(),
                        this.getTemplateSectionName() + (this.hasLocalizedTemplates() ? "_" + this.getLanguage() : ""), templateName);
        this.templateContent = (content == null) ? null : new String((byte[]) content);
        if (this.getLogger() != null) {
            this.getLogger().debug9(SOSClassUtil.getMethodName() + ": templateContent=" + this.templateContent);
        }
        if (this.templateContent != null) {
            if (this.templates == null) {
                this.templates = new Properties();
            }
            this.templates.put(templateName, this.templateContent);
            this.checkTemplateScriptLanguage(templateName);
        }
        if (this.getLogger() != null) {
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " end");
        }
        return this.templateContent;
    }

    protected String checkTemplateScriptLanguage(String templateName) throws Exception {
        if (this.templateContent == null || this.templateContent.isEmpty()) {
            return this.templateContent;
        }
        if (this.templateScriptLanguages == null) {
            this.templateScriptLanguages = new Properties();
        }
        this.templateScriptLanguages.put(templateName, "javascript");
        int firstPos = this.templateContent.indexOf("\n");
        if (firstPos > 0) {
            String firstLine = this.templateContent.substring(0, this.templateContent.indexOf("\n") - 1);
            if (firstLine.toLowerCase().matches("(.*)(vbscript)(.*)")) {
                this.templateScriptLanguages.put(templateName, "vbscript");
                this.templateContent = this.templateContent.substring(firstPos + 1);
            } else if (firstLine.toLowerCase().matches("(.*)(perlscript)(.*)")) {
                this.templateScriptLanguages.put(templateName, "perlscript");
                this.templateContent = this.templateContent.substring(firstPos + 1);
            }
        }
        return this.templateContent;
    }

    public String getTemplateScriptLanguage(String templateName) throws Exception {
        if (this.templateScriptLanguages == null || !this.templateScriptLanguages.containsKey(templateName)) {
            throw new Exception("template not found: " + templateName);
        }
        return this.templateScriptLanguages.get(templateName).toString();
    }

    public File getTemplateFile(String templateName) throws Exception {
        this.setTemplateName(templateName);
        if (this.templates == null || !this.templates.containsKey(templateName)) {
            this.loadTemplate(templateName);
        }
        if (!this.templates.containsKey(templateName)) {
            throw new Exception("template not found: " + templateName);
        }
        this.templateContent = (this.templates.get(templateName) == null) ? null : this.templates.get(templateName).toString();
        if (this.templateContent == null) {
            return null;
        }
        this.setTemplateFile(File.createTempFile("sos", ".tmp"));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(this.templateFile.getAbsolutePath()));
        out.write(this.templateContent.getBytes());
        out.flush();
        out.close();
        return this.templateFile;
    }

    public File getTemplateFile(SOSConnectionSettings settings, String templateName) throws Exception {
        this.setSettings(settings);
        this.setTemplateName(templateName);
        return this.getTemplateFile(templateName);
    }

    public File getTemplateFile(SOSConnectionSettings settings, String templateName, String templateSectionName, String templateApplicationName)
            throws Exception {
        this.setSettings(settings);
        this.setTemplateName(templateName);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);
        return this.getTemplateFile(templateName);
    }

    public File getTemplateFile(String templateName, String templateSectionName, String templateApplicationName) throws Exception {
        this.setTemplateName(templateName);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);
        return this.getTemplateFile(templateName);
    }

    protected String loadScript(String scriptName) throws Exception {
        Object content = this.getSettings().getSectionEntryDocument(this.getScriptApplicationName(), this.getScriptSectionName(), scriptName);
        if (content != null) {
            if (this.scripts == null) {
                this.scripts = new Properties();
            }
            this.scripts.put(scriptName, new String((byte[]) content));
            return new String((byte[]) content);
        }
        return null;
    }

    public String getScript(String scriptName) throws Exception {
        if (this.scripts == null || !this.scripts.containsKey(scriptName)) {
            this.loadScript(scriptName);
        }
        if (this.scripts == null || !this.scripts.containsKey(scriptName)) {
            throw new Exception("script not found: " + scriptName);
        }
        return (this.scripts.get(scriptName) == null) ? null : this.scripts.get(scriptName).toString();
    }

    public Properties getScripts() throws Exception {
        return this.getScripts(false);
    }

    public Properties getScripts(boolean force) throws Exception {
        Object key = null;
        if (!force && this.scripts != null && !this.scripts.isEmpty()) {
            return this.scripts;
        }
        if (this.settings == null) {
            throw new Exception("settings have not been loaded.");
        }
        if (this.getScriptApplicationName() == null || this.getScriptApplicationName().isEmpty()) {
            throw new Exception("no script application name has been given for settings.");
        }
        if (this.getScriptSectionName() == null || this.getScriptSectionName().isEmpty()) {
            throw new Exception("no script section name has been given for settings.");
        }
        this.setScripts(this.getSettings().getSection(this.getScriptApplicationName(), this.getScriptSectionName()));
        if (this.scripts == null || this.scripts.isEmpty()) {
            return this.scripts;
        }
        Iterator keys = this.scripts.keySet().iterator();
        while (keys.hasNext()) {
            key = keys.next();
            if (key != null) {
                this.loadScript(key.toString());
            }
        }
        return this.scripts;
    }

    public Properties getScripts(SOSConnectionSettings settings) throws Exception {
        this.setSettings(settings);
        return this.getScripts();
    }

    public Properties getScripts(SOSConnectionSettings settings, String scriptSectionName, String scriptApplicationName) throws Exception {
        this.setSettings(settings);
        this.setScriptSectionName(scriptSectionName);
        this.setScriptApplicationName(scriptApplicationName);
        return this.getScripts();
    }

    public Properties getScripts(String scriptSectionName, String scriptApplicationName) throws Exception {
        this.setScriptSectionName(scriptSectionName);
        this.setScriptApplicationName(scriptApplicationName);
        return this.getScripts();
    }

    public abstract void process() throws Exception;

    public abstract File process(File templateFile) throws Exception;

    public abstract File process(File templateFile, String scriptLanguage) throws Exception;

    public abstract File process(File templateFile, HashMap replacements) throws Exception;

    public abstract File process(File templateFile, String scriptLanguage, HashMap replacements) throws Exception;

    public abstract String process(String templateContent) throws Exception;

    public abstract String process(String templateContent, String scriptLanguage) throws Exception;

    public abstract String process(String templateContent, HashMap replacements) throws Exception;

    public abstract String process(String templateContent, String scriptLanguage, HashMap replacements) throws Exception;

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public HashMap getDateFormats() {
        return dateFormats;
    }

    protected void setDateFormats(HashMap dateFormats) {
        this.dateFormats = dateFormats;
    }

    public String getDatetimeFormat() {
        return datetimeFormat;
    }

    public void setDatetimeFormat(String datetimeFormat) {
        this.datetimeFormat = datetimeFormat;
    }

    public HashMap getDatetimeFormats() {
        return datetimeFormats;
    }

    protected void setDatetimeFormats(HashMap datetimeFormats) {
        this.datetimeFormats = datetimeFormats;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) throws Exception {
        this.language = language;
        this.initLanguage();
    }

    public HashMap getReplacements() {
        return replacements;
    }

    public void setReplacements(HashMap replacements) {
        this.replacements = replacements;
    }

    public String getDocumentContent() throws Exception {
        return documentContent;
    }

    protected void setDocumentContent(String documentContent) {
        this.documentContent = documentContent;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    protected void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public SOSConnectionSettings getSettings() {
        return settings;
    }

    public void setSettings(SOSConnectionSettings settings) throws Exception {
        if (settings == null) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing settings object.");
        }
        this.settings = settings;
    }

    public File getDocumentFile() throws Exception {
        return documentFile;
    }

    public void setDocumentFile(File documentFile) throws Exception {
        this.documentFile = documentFile;
    }

    public String getTemplateApplicationName() {
        return templateApplicationName;
    }

    public void setTemplateApplicationName(String templateApplicationName) {
        this.templateApplicationName = templateApplicationName;
    }

    public String getTemplateSectionName() {
        return templateSectionName;
    }

    public void setTemplateSectionName(String templateSectionName) {
        this.templateSectionName = templateSectionName;
    }

    protected void setTemplates(Properties templates) {
        this.templates = templates;
    }

    public File getTemplateFile() {
        return templateFile;
    }

    protected void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public boolean hasLocalizedTemplates() {
        return hasLocalizedTemplates;
    }

    public void setHasLocalizedTemplates(boolean hasLocalizedTemplates) {
        this.hasLocalizedTemplates = hasLocalizedTemplates;
    }

    public String getScriptLanguage() {
        return scriptLanguage;
    }

    public void setScriptLanguage(String scriptLanguage) throws Exception {
        if (!this.scriptLanguages.containsKey(scriptLanguage)) {
            throw new Exception("unknown script language: " + scriptLanguage);
        }
        this.scriptLanguage = scriptLanguage;
    }

    public HashMap getScriptLanguages() {
        return scriptLanguages;
    }

    public String getTemplateScriptLanguage() {
        return templateScriptLanguage;
    }

    protected void setTemplateScriptLanguage(String templateScriptLanguage) {
        this.templateScriptLanguage = templateScriptLanguage;
    }

    public Properties getTemplateScriptLanguages() {
        return templateScriptLanguages;
    }

    protected void setTemplateScriptLanguages(Properties templateScriptLanguages) {
        this.templateScriptLanguages = templateScriptLanguages;
    }

    public String getScriptApplicationName() {
        return scriptApplicationName;
    }

    public void setScriptApplicationName(String scriptApplicationName) {
        this.scriptApplicationName = scriptApplicationName;
    }

    public String getScriptSectionName() {
        return scriptSectionName;
    }

    public void setScriptSectionName(String scriptSectionName) {
        this.scriptSectionName = scriptSectionName;
    }

    protected void setScripts(Properties scripts) {
        this.scripts = scripts;
    }

    public SOSLogger getLogger() {
        return logger;
    }

    public void setLogger(SOSLogger logger) {
        this.logger = logger;
    }

    public boolean isForceReload() {
        return forceReload;
    }

    public void setForceReload(boolean forceReload) {
        this.forceReload = forceReload;
    }

}
