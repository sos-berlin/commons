package sos.textprocessor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import sos.connection.SOSConnection;
import sos.settings.SOSConnectionSettings;
import sos.util.SOSDate;
import sos.util.SOSLogger;
import sos.util.SOSStandardLogger;

public class SOSPlainTextProcessor extends SOSTextProcessor {

    public SOSPlainTextProcessor() throws Exception {
        this.init();
    }

    public SOSPlainTextProcessor(SOSLogger logger) throws Exception {
        this.setLogger(logger);
        this.init();
    }

    public SOSPlainTextProcessor(SOSConnectionSettings settings) throws Exception {
        this.setSettings(settings);
        this.init();
    }

    public SOSPlainTextProcessor(SOSConnectionSettings settings, SOSLogger logger) throws Exception {
        this.setSettings(settings);
        this.setLogger(logger);
        this.init();
    }

    public SOSPlainTextProcessor(SOSConnectionSettings settings, String templateSectionName, String templateApplicationName) throws Exception {
        this.setSettings(settings);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);
        this.init();
    }

    public SOSPlainTextProcessor(SOSConnectionSettings settings, SOSLogger logger, String templateSectionName, String templateApplicationName)
            throws Exception {
        this.setSettings(settings);
        this.setLogger(logger);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);
        this.init();
    }

    public void process() throws Exception {
        if (this.getTemplateContent() != null && !this.getTemplateContent().isEmpty()) {
            this.process(this.getTemplateContent(), this.getTemplateScriptLanguage(), this.getReplacements(), false);
        } else if (this.getTemplateFile() != null) {
            this.process(this.getTemplateFile(), this.getTemplateScriptLanguage(), this.getReplacements(), false);
        } else {
            throw new Exception("no template was given.");
        }
    }

    public File process(File templateFile) throws Exception {
        this.setTemplateFile(templateFile);
        return this.process(this.getTemplateFile(), this.getTemplateScriptLanguage(), this.getReplacements());
    }

    public File process(File templateFile, String templateScriptLanguage) throws Exception {
        this.setTemplateFile(templateFile);
        this.setTemplateScriptLanguage(templateScriptLanguage);
        return this.process(this.getTemplateFile(), this.getTemplateScriptLanguage(), this.getReplacements(), false);
    }

    public File process(File templateFile, HashMap replacements) throws Exception {
        this.setTemplateFile(templateFile);
        this.setReplacements(replacements);
        return this.process(this.getTemplateFile(), this.getTemplateScriptLanguage(), this.getReplacements(), false);
    }

    public File process(File templateFile, String templateScriptLanguage, HashMap replacements) throws Exception {
        this.setTemplateFile(templateFile);
        this.setTemplateScriptLanguage(templateScriptLanguage);
        this.setReplacements(replacements);
        return this.process(this.getTemplateFile(), this.getTemplateScriptLanguage(), this.getReplacements(), false);
    }

    public File process(File templateFile, HashMap replacements, boolean nl2br) throws Exception {
        this.setTemplateFile(templateFile);
        this.setReplacements(replacements);
        return this.process(this.getTemplateFile(), this.getTemplateScriptLanguage(), this.getReplacements(), nl2br);
    }

    public File process(File templateFile, String templateScriptLanguage, HashMap replacements, boolean nl2br) throws Exception {
        this.setTemplateFile(templateFile);
        this.setTemplateScriptLanguage(templateScriptLanguage);
        this.setReplacements(replacements);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(this.getTemplateFile().getAbsolutePath()));
        StringBuffer content = new StringBuffer("");
        byte buffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            content.append(new String(buffer, 0, bytesRead));
        }
        in.close();
        this.process(content.toString(), this.getTemplateScriptLanguage(), this.getReplacements(), nl2br);
        return this.getDocumentFile();
    }

    public String process(String templateContent) throws Exception {
        this.setTemplateContent(templateContent);
        return this.process(this.getTemplateContent(), this.getTemplateScriptLanguage(), this.getReplacements());
    }

    public String process(String templateContent, String templateScriptLanguage) throws Exception {
        this.setTemplateContent(templateContent);
        this.setTemplateScriptLanguage(templateScriptLanguage);
        return this.process(this.getTemplateContent(), this.getTemplateScriptLanguage(), this.getReplacements(), false);
    }

    public String process(String templateContent, HashMap replacements) throws Exception {
        this.setTemplateContent(templateContent);
        this.setReplacements(replacements);
        return this.process(this.getTemplateContent(), this.getTemplateScriptLanguage(), this.getReplacements(), false);
    }

    public String process(String templateContent, String templateScriptLanguage, HashMap replacements) throws Exception {
        this.setTemplateContent(templateContent);
        this.setTemplateScriptLanguage(templateScriptLanguage);
        this.setReplacements(replacements);
        return this.process(this.getTemplateContent(), this.getTemplateScriptLanguage(), this.getReplacements(), false);
    }

    public String process(String templateContent, HashMap replacements, boolean nl2br) throws Exception {
        this.setTemplateContent(templateContent);
        this.setReplacements(replacements);
        return this.process(this.getTemplateContent(), this.getTemplateScriptLanguage(), this.getReplacements(), nl2br);
    }

    public String process(String templateContent, String templateScriptLanguage, HashMap replacements, boolean nl2br) throws Exception {
        Object key = null;
        Object value = null;
        String content = templateContent;
        this.setTemplateContent(templateContent);
        this.setTemplateScriptLanguage(templateScriptLanguage);
        this.setReplacements(replacements);
        content = content.replaceAll("&\\(date\\)", SOSDate.getCurrentDateAsString(this.getDateFormat()));
        content = content.replaceAll("&\\(datetime\\)", SOSDate.getCurrentTimeAsString(this.getDatetimeFormat()));
        content = content.replaceAll("&\\#\\(date\\)", SOSDate.getCurrentDateAsString(this.getDateFormat()));
        content = content.replaceAll("&\\#\\#\\(datetime\\)", SOSDate.getCurrentTimeAsString(this.getDatetimeFormat()));
        if (nl2br) {
            content = content.replaceAll("\n", "<br/>");
        }
        if (replacements != null) {
            Iterator keys = replacements.keySet().iterator();
            while (keys.hasNext()) {
                key = keys.next();
                if (key != null) {
                    value = replacements.get(key.toString());
                    if (value != null) {
                        try {
                            content =
                                    content.replaceAll("&\\#\\(" + key.toString() + "\\)", SOSDate.getDateAsString(SOSDate.getDate(value.toString()),
                                            this.getDateFormat()));
                            content =
                                    content.replaceAll("&\\#\\#\\(" + key.toString() + "\\)", SOSDate.getDateTimeAsString(
                                            SOSDate.getDate(value.toString()), this.getDatetimeFormat()));
                        } catch (Exception ex) {
                            // ignore this error: replacement is not convertible
                            // to date
                        }
                        Locale defaultLocale = Locale.getDefault();
                        try {
                            double doubleValue = Double.parseDouble(value.toString());
                            if ("de".equalsIgnoreCase(this.getLanguage())) {
                                Locale.setDefault(Locale.GERMAN);
                            } else if ("en".equalsIgnoreCase(this.getLanguage())) {
                                Locale.setDefault(Locale.US);
                            }
                            DecimalFormat formatter = new DecimalFormat("#,###.00");
                            content = content.replaceAll("&\\$\\(" + key.toString() + "\\)", formatter.format(doubleValue).toString());
                        } catch (Exception ex) {
                            //
                        } finally {
                            Locale.setDefault(defaultLocale);
                        }
                        content = content.replaceAll("&\\(" + key.toString() + "\\)", value.toString());
                    }
                }
            }
        }
        content = content.replaceAll("&\\#\\(.*\\)", "");
        content = content.replaceAll("&\\#\\#\\(.*\\)", "");
        content = content.replaceAll("&\\$\\(.*\\)", "");
        this.setDocumentContent(content.replaceAll("&\\(.*\\)", ""));
        this.setDocumentFile(null);
        return this.getDocumentContent();
    }

    public File getDocumentFile() throws Exception {
        if (this.documentFile == null) {
            this.documentFile = File.createTempFile("sos", ".tmp");
        }
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(this.documentFile.getAbsolutePath()));
        out.write(this.getDocumentContent().getBytes());
        out.flush();
        out.close();
        return this.documentFile;
    }

    public static void main(String[] args) {
        try {
            SOSPlainTextProcessor processor = new SOSPlainTextProcessor();
            processor.process("Hello World &(datetime)");
            System.out.println(processor.getDocumentContent());
            SOSStandardLogger logger = new SOSStandardLogger(9);
            SOSConnection connection = SOSConnection.createInstance("/scheduler/config/hibernate.cfg.xml");
            connection.connect();
            SOSConnectionSettings settings = new SOSConnectionSettings(connection, "SETTINGS");
            processor = new SOSPlainTextProcessor(settings, "email_templates", "email_templates");
            processor.setHasLocalizedTemplates(true);
            System.out.println(processor.process(processor.getTemplate("sample_body")));
        } catch (Exception e) {
            System.out.println("error occurred: " + e.getMessage());
        }
    }

}
