package sos.textprocessor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import sos.connection.SOSConnection;
import sos.hostware.Factory_processor;
import sos.settings.SOSConnectionSettings;
import sos.util.SOSLogger;
import sos.util.SOSStandardLogger;

public class SOSDocumentFactoryTextProcessor extends SOSTextProcessor {

    Factory_processor processor = null;

    public SOSDocumentFactoryTextProcessor() throws Exception {
        this.init();
    }

    public SOSDocumentFactoryTextProcessor(SOSLogger logger) throws Exception {
        this.setLogger(logger);
        this.init();
    }

    public SOSDocumentFactoryTextProcessor(SOSConnectionSettings settings) throws Exception {
        this.setSettings(settings);
        this.init();
    }

    public SOSDocumentFactoryTextProcessor(SOSConnectionSettings settings, SOSLogger logger) throws Exception {
        this.setLogger(logger);
        this.setSettings(settings);
        this.init();
    }

    public SOSDocumentFactoryTextProcessor(SOSConnectionSettings settings, String templateSectionName, String templateApplicationName)
            throws Exception {
        this.setSettings(settings);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);
        this.init();
    }

    public SOSDocumentFactoryTextProcessor(SOSConnectionSettings settings, SOSLogger logger, String templateSectionName,
            String templateApplicationName) throws Exception {
        this.setSettings(settings);
        this.setLogger(logger);
        this.setTemplateSectionName(templateSectionName);
        this.setTemplateApplicationName(templateApplicationName);
        this.init();
    }

    public void init() throws Exception {
        super.init();
        this.processor = new Factory_processor();
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
        return this.process(templateFile, this.getTemplateScriptLanguage(), this.getReplacements(), nl2br);
    }

    public File process(File templateFile, String templateScriptLanguage, HashMap replacements, boolean nl2br) throws Exception {
        Object key = null;
        Object value = null;
        this.setTemplateFile(templateFile);
        this.setTemplateScriptLanguage(templateScriptLanguage);
        this.setReplacements(replacements);
        try {
            if (this.processor == null) {
                this.processor = new Factory_processor();
            }
            if (this.documentFile == null) {
                this.documentFile = File.createTempFile("sos", ".tmp");
            }
            this.processor.set_document_filename(this.documentFile.getAbsolutePath());
            this.processor.set_language(this.getTemplateScriptLanguage());
            if ("javascript".equalsIgnoreCase(this.getTemplateScriptLanguage())) {
                this.processor.add_obj(this, "document_factory");
            }
            this.processor.set_template_filename(templateFile.getAbsolutePath());
            Iterator keys = replacements.keySet().iterator();
            while (keys.hasNext()) {
                key = keys.next();
                if (key != null) {
                    value = replacements.get(key.toString());
                    if (value != null) {
                        this.processor.set_parameter(key.toString(), value.toString());
                    }
                }
            }
            this.processor.add_parameters();
            if (this.scripts != null && !this.scripts.isEmpty() && this.scripts.containsKey("start_script." + this.getTemplateScriptLanguage())) {
                processor.parse(this.scripts.getProperty("start_script." + this.getTemplateScriptLanguage()));
            }
            this.processor.process();
            this.processor.close_output_file();
            this.processor.close();
            return this.documentFile;
        } catch (Exception e) {
            throw new Exception("error occurred processing template: " + e.getMessage());
        } finally {
            if (this.processor != null) {
                try {
                    this.processor.close();
                    this.processor = null;
                } catch (Exception ex) {
                    //
                }
            }
        }
    }

    public String process(String templateContent) throws Exception {
        this.setTemplateContent(templateContent);
        return this.process(this.getTemplateContent(), this.getTemplateScriptLanguage(), this.getReplacements());
    }

    public String process(String templateContent, String templateScriptLanguage) throws Exception {
        this.setTemplateContent(templateContent);
        this.setTemplateScriptLanguage(templateScriptLanguage);
        return this.process(this.getTemplateContent(), this.getTemplateScriptLanguage(), this.getReplacements());
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
        this.setTemplateContent(templateContent);
        this.setTemplateScriptLanguage(templateScriptLanguage);
        this.setReplacements(replacements);
        this.templateFile = File.createTempFile("sos", ".tmp");
        this.templateFile.deleteOnExit();
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(this.templateFile.getAbsolutePath()));
        out.write(this.getTemplateContent().getBytes());
        out.flush();
        out.close();
        this.process(this.templateFile, this.getTemplateScriptLanguage(), this.getReplacements(), nl2br);
        return this.getDocumentContent();
    }

    public String getDocumentContent() throws Exception {
        if (this.getDocumentFile() == null) {
            throw new Exception("no document has been created.");
        }
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(this.getDocumentFile().getAbsolutePath()));
        StringBuffer content = new StringBuffer("");
        byte buffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            content.append(new String(buffer, 0, bytesRead));
        }
        in.close();
        return content.toString();
    }

    public Factory_processor getProcessor() throws Exception {
        if (this.processor == null) {
            this.processor = new Factory_processor();
        }
        return processor;
    }

    public static void main(String[] args) {
        try {
            SOSDocumentFactoryTextProcessor processor = new SOSDocumentFactoryTextProcessor();
            processor.process("Hello World &(datetime)");
            System.out.println(processor.getDocumentContent());
            SOSStandardLogger logger = new SOSStandardLogger(9);
            SOSConnection connection = SOSConnection.createInstance("/scheduler/config/sos_settings.ini", logger);
            connection.connect();
            SOSConnectionSettings settings = new SOSConnectionSettings(connection, "SETTINGS", logger);
            processor = new SOSDocumentFactoryTextProcessor(settings, "email_templates", "email_templates");
            processor.setHasLocalizedTemplates(true);
            File outputFile = new File("/tmp/sample.txt");
            processor.setDocumentFile(outputFile);
            processor.process(processor.getTemplateFile("sample_body"));
            System.out.println(processor.getDocumentContent());
        } catch (Exception e) {
            System.out.println("error occurred: " + e.getMessage());
        }
    }

}
