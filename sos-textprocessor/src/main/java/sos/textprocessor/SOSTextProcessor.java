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

    /** application name for templates in Settings */
    protected String templateApplicationName = "";

    /** section name for templates in settings */
    protected String templateSectionName = "";

    /** entry name for template in settings */
    protected String templateName = "";

    protected File templateFile = null;

    protected Properties templates = null;

    protected Properties templateScriptLanguages = null;

    protected String templateScriptLanguage = null;

    protected String templateContent = "";

    protected Properties scripts = null;

    /** application name for scripts in Settings */
    protected String scriptApplicationName = "";

    /** section name for scripts in settings */
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

    public SOSTextProcessor(SOSConnectionSettings settings, SOSLogger logger, String templateSectionName, String templateApplicationName) throws Exception {

        this.setSettings(settings);
        this.setLogger(logger);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);
        this.init();
    }

    /** initialization - set date format, datetime format
     * 
     * @throws java.lang.Exception */
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

    /** initialization - set date format, datetime format
     * 
     * @param sosSettings SOSSettings settings from class SOSProfileSettings or
     *            SOSConnectionSettings
     * @throws java.lang.Exception */
    public void init(SOSConnectionSettings settings) throws Exception {

        this.setSettings(settings);
        this.init();
    }

    /** language dependent initialization - set date format, datetime format
     * 
     * @throws java.lang.Exception */
    public void initLanguage() throws Exception {

        if (this.getDateFormats().containsKey(this.getLanguage()) && this.getDatetimeFormats().containsKey(this.getLanguage())) {
            this.setDateFormat(this.getDateFormats().get(this.getLanguage()).toString());
            this.setDatetimeFormat(this.getDatetimeFormats().get(this.getLanguage()).toString());
        } else {
            this.setDateFormat(this.getDateFormats().get("de").toString());
            this.setDatetimeFormat(this.getDatetimeFormats().get("de").toString());
        }
    }

    /** retrieve template content
     * 
     * @param templateName String template identification
     * @throws java.lang.Exception */
    public String getTemplate(String templateName) throws Exception {

        if (this.getLogger() != null)
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " begin: template=" + templateName);

        this.setTemplateName(templateName);

        if (this.templates == null || !this.templates.containsKey(templateName))
            this.loadTemplate(templateName);

        if (this.templates == null || !this.templates.containsKey(templateName))
            throw new Exception("template not found: " + templateName);

        this.templateContent = (this.templates.get(templateName) == null) ? null : this.templates.get(templateName).toString();

        if (this.templateScriptLanguages == null)
            this.templateScriptLanguages = new Properties();
        if (!this.templateScriptLanguages.containsKey(templateName)) {
            this.templateScriptLanguage = "javascript";
        } else {
            this.templateScriptLanguage = this.templateScriptLanguages.get(templateName).toString();
        }
        if (this.templateScriptLanguage == null || this.templateScriptLanguage.length() == 0)
            this.templateScriptLanguage = "javascript";

        if (this.getLogger() != null) {
            this.getLogger().debug9(SOSClassUtil.getMethodName() + ": templateContent=" + this.templateContent);
            this.getLogger().debug9(SOSClassUtil.getMethodName() + ": templateScriptLanguage=" + this.templateScriptLanguage);
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " end");
        }

        return this.templateContent;
    }

    /** retrieve templates from settings
     * 
     * @throws java.lang.Exception */
    public Properties getTemplates() throws Exception {

        Object key = null;
        Object value = null;

        if (!this.isForceReload() && this.templates != null && !this.templates.isEmpty())
            return this.templates;

        if (this.settings == null)
            throw new Exception("settings have not been loaded.");
        if (this.getTemplateApplicationName() == null || this.getTemplateApplicationName().length() == 0)
            throw new Exception("no template application name has been given for settings.");
        if (this.getTemplateSectionName() == null || this.getTemplateSectionName().length() == 0)
            throw new Exception("no template section name has been given for settings.");

        this.setTemplates(this.getSettings().getSection(this.getTemplateApplicationName(), this.getTemplateSectionName()
                + (this.hasLocalizedTemplates() ? "_" + this.getLanguage() : "")));

        if (this.templates == null || this.templates.isEmpty())
            return this.templates;

        // throw new Exception(SOSClassUtil.getMethodName()
        // + ": missing settings entries for template application \"" +
        // this.getTemplateApplicationName() + "\" in template section \"" +
        // this.getTemplateSectionName() + "\".");

        Iterator keys = this.templates.keySet().iterator();
        while (keys.hasNext()) {
            key = keys.next();
            if (key != null) {
                value = this.templates.get(key.toString());
                if (value != null && value.toString() != null && value.toString().trim().length() == 0) {
                    // retrieve the document content from this setting
                    value = this.loadTemplateDocument(key.toString());
                    this.templates.put(key.toString(), (value == null ? "" : value.toString()));
                }
            }
        }

        return this.templates;
    }

    /** retrieve templates from settings
     * 
     * @param sosSettings SOSSettings settings from class SOSProfileSettings or
     *            SOSConnectionSettings
     * @throws java.lang.Exception */
    public Properties getTemplates(boolean forceReload) throws Exception {

        this.setForceReload(forceReload);
        return this.getTemplates();
    }

    /** retrieve templates from settings
     * 
     * @param sosSettings SOSSettings settings from class SOSProfileSettings or
     *            SOSConnectionSettings
     * @throws java.lang.Exception */
    public Properties getTemplates(SOSConnectionSettings settings) throws Exception {

        this.setSettings(settings);

        return this.getTemplates();
    }

    /** retrieve templates from settings
     * 
     * @param templateApplicationName name of application in settings table
     * @param templateSectionName name of section in settings table
     * @throws java.lang.Exception */
    public Properties getTemplates(String templateSectionName, String templateApplicationName) throws Exception {

        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);

        return this.getTemplates();
    }

    /** retrieve templates from settings
     * 
     * @param sosSettings SOSSettings settings from class SOSProfileSettings or
     *            SOSConnectionSettings
     * @param templateApplicationName name of application in settings table
     * @param templateSectionName name of section in settings table
     * @throws java.lang.Exception */
    public Properties getTemplates(SOSConnectionSettings settings, String templateSectionName, String templateApplicationName) throws Exception {

        this.setSettings(settings);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);

        return this.getTemplates();
    }

    /** load template content from database
     * 
     * @param templateName String name of template
     * @throws java.lang.Exception */
    protected String loadTemplate(String templateName) throws Exception {

        if (this.getLogger() != null)
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " begin: templateName=" + templateName);

        this.setTemplateName(templateName);

        this.templateContent = this.getSettings().getSectionEntry(this.getTemplateApplicationName(), this.getTemplateSectionName()
                + (this.hasLocalizedTemplates() ? "_" + this.getLanguage() : ""), templateName);
        if (this.getLogger() != null)
            this.getLogger().debug9(SOSClassUtil.getMethodName() + ": templateContent=" + this.templateContent);

        if (this.templateContent == null || this.templateContent.trim().length() == 0) {
            this.loadTemplateDocument(templateName);
        } else {
            if (this.templates == null)
                this.templates = new Properties();
            this.templates.put(templateName, this.templateContent);
            this.checkTemplateScriptLanguage(templateName);
        }

        if (this.getLogger() != null)
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " end");

        return this.templateContent;
    }

    /** load template document content from database
     * 
     * @param templateName String name of template
     * @throws java.lang.Exception */
    protected String loadTemplateDocument(String templateName) throws Exception {

        if (this.getLogger() != null)
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " begin: templateName=" + templateName);

        this.setTemplateName(templateName);

        Object content = this.getSettings().getSectionEntryDocument(this.getTemplateApplicationName(), this.getTemplateSectionName()
                + (this.hasLocalizedTemplates() ? "_" + this.getLanguage() : ""), templateName);
        this.templateContent = (content == null) ? null : new String((byte[]) content);
        if (this.getLogger() != null)
            this.getLogger().debug9(SOSClassUtil.getMethodName() + ": templateContent=" + this.templateContent);
        if (this.templateContent != null) {
            if (this.templates == null)
                this.templates = new Properties();
            this.templates.put(templateName, this.templateContent);
            this.checkTemplateScriptLanguage(templateName);
        }

        if (this.getLogger() != null)
            this.getLogger().debug6(SOSClassUtil.getMethodName() + " end");

        return this.templateContent;
    }

    /** check script language in template content
     * 
     * @param templateName String name of template
     * @throws java.lang.Exception */
    protected String checkTemplateScriptLanguage(String templateName) throws Exception {

        if (this.templateContent == null || this.templateContent.length() == 0)
            return this.templateContent;

        if (this.templateScriptLanguages == null)
            this.templateScriptLanguages = new Properties();
        this.templateScriptLanguages.put(templateName, "javascript");

        int firstPos = this.templateContent.indexOf("\n");
        if (firstPos > 0) { // newline in the first position is ignored
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

    /** retrieve the script language of the given template
     * 
     * @param templateName String name of template
     * @return Returns the templateScriptLanguage. */
    public String getTemplateScriptLanguage(String templateName) throws Exception {

        if (this.templateScriptLanguages == null || !this.templateScriptLanguages.containsKey(templateName))
            throw new Exception("template not found: " + templateName);

        return this.templateScriptLanguages.get(templateName).toString();
    }

    /** get template content as file
     * 
     * @param templateName String name of template
     * @throws java.lang.Exception */
    public File getTemplateFile(String templateName) throws Exception {

        this.setTemplateName(templateName);

        if (this.templates == null || !this.templates.containsKey(templateName))
            this.loadTemplate(templateName);

        if (!this.templates.containsKey(templateName))
            throw new Exception("template not found: " + templateName);

        this.templateContent = (this.templates.get(templateName) == null) ? null : this.templates.get(templateName).toString();
        if (this.templateContent == null)
            return null;

        this.setTemplateFile(File.createTempFile("sos", ".tmp"));
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(this.templateFile.getAbsolutePath()));
        out.write(this.templateContent.getBytes());
        out.flush();
        out.close();

        return this.templateFile;
    }

    /** get template content as file
     * 
     * @param sosSettings SOSSettings settings from class SOSProfileSettings or
     *            SOSConnectionSettings
     * @param templateName String name of template
     * @throws java.lang.Exception */
    public File getTemplateFile(SOSConnectionSettings settings, String templateName) throws Exception {

        this.setSettings(settings);
        this.setTemplateName(templateName);

        return this.getTemplateFile(templateName);
    }

    /** get template content as file
     * 
     * @param sosSettings SOSSettings settings from class SOSProfileSettings or
     *            SOSConnectionSettings
     * @param templateName String name of template
     * @param templateSectionName name of section in settings table
     * @param templateApplicationName name of application in settings table
     * @throws java.lang.Exception */
    public File getTemplateFile(SOSConnectionSettings settings, String templateName, String templateSectionName, String templateApplicationName)
            throws Exception {

        this.setSettings(settings);
        this.setTemplateName(templateName);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);

        return this.getTemplateFile(templateName);
    }

    /** get template content as file
     * 
     * @param templateName String name of template
     * @param templateSectionName name of section in settings table
     * @param templateApplicationName name of application in settings table
     * @throws java.lang.Exception */
    public File getTemplateFile(String templateName, String templateSectionName, String templateApplicationName) throws Exception {

        this.setTemplateName(templateName);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);

        return this.getTemplateFile(templateName);
    }

    /** load script content from database
     * 
     * @param scriptName String name of script
     * @throws java.lang.Exception */
    protected String loadScript(String scriptName) throws Exception {

        Object content = this.getSettings().getSectionEntryDocument(this.getScriptApplicationName(), this.getScriptSectionName(), scriptName);
        if (content != null) {
            if (this.scripts == null)
                this.scripts = new Properties();
            this.scripts.put(scriptName, new String((byte[]) content));
            return new String((byte[]) content);
        }

        return null;
    }

    /** retrieve script content
     * 
     * @param scriptName String name of script
     * @throws java.lang.Exception */
    public String getScript(String scriptName) throws Exception {

        if (this.scripts == null || !this.scripts.containsKey(scriptName))
            this.loadScript(scriptName);

        if (this.scripts == null || !this.scripts.containsKey(scriptName))
            throw new Exception("script not found: " + scriptName);

        return (this.scripts.get(scriptName) == null) ? null : this.scripts.get(scriptName).toString();
    }

    /** retrieve scripts from settings
     * 
     * @throws java.lang.Exception */
    public Properties getScripts() throws Exception {

        return this.getScripts(false);
    }

    /** retrieve scripts from settings
     * 
     * @param sosSettings SOSSettings settings from class SOSProfileSettings or
     *            SOSConnectionSettings
     * @throws java.lang.Exception */
    public Properties getScripts(boolean force) throws Exception {

        Object key = null;

        if (!force && this.scripts != null && !this.scripts.isEmpty())
            return this.scripts;

        if (this.settings == null)
            throw new Exception("settings have not been loaded.");
        if (this.getScriptApplicationName() == null || this.getScriptApplicationName().length() == 0)
            throw new Exception("no script application name has been given for settings.");
        if (this.getScriptSectionName() == null || this.getScriptSectionName().length() == 0)
            throw new Exception("no script section name has been given for settings.");

        this.setScripts(this.getSettings().getSection(this.getScriptApplicationName(), this.getScriptSectionName()));

        if (this.scripts == null || this.scripts.isEmpty())
            return this.scripts;

        // throw new Exception(SOSClassUtil.getMethodName()
        // + ": missing settings entries for script application \"" +
        // this.getScriptApplicationName() + "\" in script section \"" +
        // this.getScriptSectionName() + "\".");

        Iterator keys = this.scripts.keySet().iterator();
        while (keys.hasNext()) {
            key = keys.next();
            if (key != null) {
                // retrieve the script content from this setting
                this.loadScript(key.toString());
            }
        }

        return this.scripts;
    }

    /** retrieve scripts from settings
     * 
     * @param sosSettings SOSSettings settings from class SOSProfileSettings or
     *            SOSConnectionSettings
     * @throws java.lang.Exception */
    public Properties getScripts(SOSConnectionSettings settings) throws Exception {

        this.setSettings(settings);

        return this.getScripts();
    }

    /** retrieve scripts from settings
     * 
     * @param sosSettings SOSSettings settings from class SOSProfileSettings or
     *            SOSConnectionSettings
     * @param scriptApplicationName name of script application in settings table
     * @param scriptSectionName name of script section in settings table
     * @throws java.lang.Exception */
    public Properties getScripts(SOSConnectionSettings settings, String scriptSectionName, String scriptApplicationName) throws Exception {

        this.setSettings(settings);
        this.setScriptSectionName(scriptSectionName);
        this.setScriptApplicationName(scriptApplicationName);

        return this.getScripts();
    }

    /** retrieve scripts from settings
     * 
     * @param scriptApplicationName name of script application in settings table
     * @param scriptSectionName name of script section in settings table
     * @throws java.lang.Exception */
    public Properties getScripts(String scriptSectionName, String scriptApplicationName) throws Exception {

        this.setScriptSectionName(scriptSectionName);
        this.setScriptApplicationName(scriptApplicationName);

        return this.getScripts();
    }

    /** process text: this method has to be implemented by derived text
     * processors
     * 
     * @throws java.lang.Exception */
    public abstract void process() throws Exception;

    /** process text: this method has to be implemented by derived text
     * processors
     * 
     * @throws java.lang.Exception */
    public abstract File process(File templateFile) throws Exception;

    /** process text: this method has to be implemented by derived text
     * processors
     * 
     * @throws java.lang.Exception */
    public abstract File process(File templateFile, String scriptLanguage) throws Exception;

    /** process text: this method has to be implemented by derived text
     * processors
     * 
     * @throws java.lang.Exception */
    public abstract File process(File templateFile, HashMap replacements) throws Exception;

    /** process text: this method has to be implemented by derived text
     * processors
     * 
     * @throws java.lang.Exception */
    public abstract File process(File templateFile, String scriptLanguage, HashMap replacements) throws Exception;

    /** process text: this method has to be implemented by derived text
     * processors
     * 
     * @throws java.lang.Exception */
    public abstract String process(String templateContent) throws Exception;

    /** process text: this method has to be implemented by derived text
     * processors
     * 
     * @throws java.lang.Exception */
    public abstract String process(String templateContent, String scriptLanguage) throws Exception;

    /** process text: this method has to be implemented by derived text
     * processors
     * 
     * @throws java.lang.Exception */
    public abstract String process(String templateContent, HashMap replacements) throws Exception;

    /** process text: this method has to be implemented by derived text
     * processors
     * 
     * @throws java.lang.Exception */
    public abstract String process(String templateContent, String scriptLanguage, HashMap replacements) throws Exception;

    /** @return Returns the dateFormat. */
    public String getDateFormat() {
        return dateFormat;
    }

    /** @param dateFormat The dateFormat to set. */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /** @return Returns the dateFormats. */
    public HashMap getDateFormats() {
        return dateFormats;
    }

    /** @param dateFormats The dateFormats to set. */
    protected void setDateFormats(HashMap dateFormats) {
        this.dateFormats = dateFormats;
    }

    /** @return Returns the datetimeFormat. */
    public String getDatetimeFormat() {
        return datetimeFormat;
    }

    /** @param datetimeFormat The datetimeFormat to set. */
    public void setDatetimeFormat(String datetimeFormat) {
        this.datetimeFormat = datetimeFormat;
    }

    /** @return Returns the datetimeFormats. */
    public HashMap getDatetimeFormats() {
        return datetimeFormats;
    }

    /** @param datetimeFormats The datetimeFormats to set. */
    protected void setDatetimeFormats(HashMap datetimeFormats) {
        this.datetimeFormats = datetimeFormats;
    }

    /** @return Returns the language. */
    public String getLanguage() {
        return language;
    }

    /** @param language The language to set. */
    public void setLanguage(String language) throws Exception {
        this.language = language;
        this.initLanguage();
    }

    /** @return Returns the replacements. */
    public HashMap getReplacements() {
        return replacements;
    }

    /** @param replacements The replacements to set. */
    public void setReplacements(HashMap replacements) {
        this.replacements = replacements;
    }

    /** @return Returns the documentContent. */
    public String getDocumentContent() throws Exception {
        return documentContent;
    }

    /** @param documentContent The documentContent to set. */
    protected void setDocumentContent(String documentContent) {
        this.documentContent = documentContent;
    }

    /** @return Returns the templateContent. */
    public String getTemplateContent() {
        return templateContent;
    }

    /** @param templateContent The templateContent to set. */
    protected void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    /** @return Returns the settings. */
    public SOSConnectionSettings getSettings() {
        return settings;
    }

    /** @param settings The settings to set. */
    public void setSettings(SOSConnectionSettings settings) throws Exception {

        if (settings == null)
            throw new Exception(SOSClassUtil.getMethodName() + ": missing settings object.");

        this.settings = settings;
    }

    /** @return Returns the documentFile. */
    public File getDocumentFile() throws Exception {
        return documentFile;
    }

    /** @param documentFile The documentFile to set. */
    public void setDocumentFile(File documentFile) throws Exception {
        this.documentFile = documentFile;
    }

    /** @return Returns the templateApplicationName. */
    public String getTemplateApplicationName() {
        return templateApplicationName;
    }

    /** @param templateApplication The templateApplicationName to set. */
    public void setTemplateApplicationName(String templateApplicationName) {
        this.templateApplicationName = templateApplicationName;
    }

    /** @return Returns the templateSectionName. */
    public String getTemplateSectionName() {
        return templateSectionName;
    }

    /** @param templateSection The templateSectionName to set. */
    public void setTemplateSectionName(String templateSectionName) {
        this.templateSectionName = templateSectionName;
    }

    /** @param templates The templates to set. */
    protected void setTemplates(Properties templates) {
        this.templates = templates;
    }

    /** @return Returns the templateFile. */
    public File getTemplateFile() {
        return templateFile;
    }

    /** @param templateFile The templateFile to set. */
    protected void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }

    /** @return Returns the templateName. */
    public String getTemplateName() {
        return templateName;
    }

    /** @param templateName The templateName to set. */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /** @return Returns the hasLocalizedTemplates. */
    public boolean hasLocalizedTemplates() {
        return hasLocalizedTemplates;
    }

    /** @param hasLocalizedTemplates The hasLocalizedTemplates to set. */
    public void setHasLocalizedTemplates(boolean hasLocalizedTemplates) {
        this.hasLocalizedTemplates = hasLocalizedTemplates;
    }

    /** @return Returns the scriptLanguage. */
    public String getScriptLanguage() {
        return scriptLanguage;
    }

    /** @param scriptLanguage The scriptLanguage to set. */
    public void setScriptLanguage(String scriptLanguage) throws Exception {

        if (!this.scriptLanguages.containsKey(scriptLanguage))
            throw new Exception("unknown script language: " + scriptLanguage);

        this.scriptLanguage = scriptLanguage;
    }

    /** @return Returns the scriptLanguages. */
    public HashMap getScriptLanguages() {
        return scriptLanguages;
    }

    /** @return Returns the templateScriptLanguage. */
    public String getTemplateScriptLanguage() {
        return templateScriptLanguage;
    }

    /** @param templateScriptLanguage The templateScriptLanguage to set. */
    protected void setTemplateScriptLanguage(String templateScriptLanguage) {
        this.templateScriptLanguage = templateScriptLanguage;
    }

    /** @return Returns the templateScriptLanguages. */
    public Properties getTemplateScriptLanguages() {
        return templateScriptLanguages;
    }

    /** @param templateScriptLanguages The templateScriptLanguages to set. */
    protected void setTemplateScriptLanguages(Properties templateScriptLanguages) {
        this.templateScriptLanguages = templateScriptLanguages;
    }

    /** @return Returns the scriptApplicationName. */
    public String getScriptApplicationName() {
        return scriptApplicationName;
    }

    /** @param scriptApplicationName The scriptApplicationName to set. */
    public void setScriptApplicationName(String scriptApplicationName) {
        this.scriptApplicationName = scriptApplicationName;
    }

    /** @return Returns the scriptSectionName. */
    public String getScriptSectionName() {
        return scriptSectionName;
    }

    /** @param scriptSectionName The scriptSectionName to set. */
    public void setScriptSectionName(String scriptSectionName) {
        this.scriptSectionName = scriptSectionName;
    }

    /** @param scripts The scripts to set. */
    protected void setScripts(Properties scripts) {
        this.scripts = scripts;
    }

    /** @return Returns the logger. */
    public SOSLogger getLogger() {
        return logger;
    }

    /** @param logger The logger to set. */
    public void setLogger(SOSLogger logger) {
        this.logger = logger;
    }

    /** @return Returns the forceReload. */
    public boolean isForceReload() {
        return forceReload;
    }

    /** @param forceReload The forceReload to set. */
    public void setForceReload(boolean forceReload) {
        this.forceReload = forceReload;
    }

}
