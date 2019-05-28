package com.sos.keepass.extensions.simple;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.linguafranca.pwdb.Credentials;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;
import org.linguafranca.pwdb.kdbx.simple.SimpleEntry;
import org.linguafranca.pwdb.kdbx.simple.SimpleGroup;
import org.linguafranca.pwdb.kdbx.simple.converter.EmptyStringConverter;
import org.linguafranca.pwdb.kdbx.simple.model.EntryClasses;
import org.linguafranca.pwdb.kdbx.simple.model.KeePassFile;
import org.linguafranca.pwdb.kdbx.stream_3_1.KdbxHeader;
import org.linguafranca.pwdb.kdbx.stream_3_1.KdbxSerializer;
import org.linguafranca.pwdb.kdbx.stream_3_1.Salsa20StreamEncryptor;
import org.linguafranca.xml.XmlOutputStreamFilter;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import com.sos.keepass.exceptions.SOSKeePassDatabaseException;
import com.sos.keepass.extensions.simple.transformer.SOSKdbxOutputTransformer;

public class SOSSimpleDatabase extends SimpleDatabase {

    KeePassFile _keePassFileModel;

    public SOSSimpleDatabase() {
    }

    public void saveAs(SimpleDatabase sd, Credentials credentials, Path file) throws SOSKeePassDatabaseException {
        FileOutputStream fos = null;
        try {
            Field kpField = FieldUtils.getField(sd.getClass(), "keePassFile", true);
            _keePassFileModel = (KeePassFile) kpField.get(sd);

            fos = new FileOutputStream(file.toFile());
            save(credentials, fos);
        } catch (Throwable e) {
            throw new SOSKeePassDatabaseException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                } catch (Exception e) {
                }
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void save(Credentials credentials, OutputStream outputStream) throws IOException {
        try {
            if (_keePassFileModel == null) {
                throw new Exception("_keePassFileModel is null");
            }

            // create the stream to accept unencrypted data and output to encrypted
            KdbxHeader kdbxHeader = new KdbxHeader();
            OutputStream kdbxInnerStream = KdbxSerializer.createEncryptedOutputStream(credentials, kdbxHeader, outputStream);

            // the database contains the hash of the headers
            _keePassFileModel.meta.headerHash.setContent(kdbxHeader.getHeaderHash());

            // encrypt the fields in the XML inner stream
            XmlOutputStreamFilter plainTextOutputStream = new XmlOutputStreamFilter(kdbxInnerStream, new SOSKdbxOutputTransformer(
                    new Salsa20StreamEncryptor(kdbxHeader.getProtectedStreamKey())));

            // set up the "protected" attributes of fields that need inner stream encryption
            prepareForSave(_keePassFileModel.root.group);

            // and save the database out
            getSerializer().write(_keePassFileModel, plainTextOutputStream);
            plainTextOutputStream.close();
            plainTextOutputStream.await();
            this.setDirty(false);

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static void prepareForSave(SimpleGroup parent) throws Exception {
        for (SimpleGroup group : parent.getGroups()) {
            prepareForSave(group);
        }
        for (SimpleEntry entry : parent.getEntries()) {
            Field f = FieldUtils.getField(entry.getClass(), "string", true);

            @SuppressWarnings("unchecked")
            List<EntryClasses.StringProperty> properties = (List<EntryClasses.StringProperty>) f.get(entry);
            for (EntryClasses.StringProperty property : properties) {
                boolean shouldProtect = parent.getDatabase().shouldProtect(property.getKey());
                property.getValue().setProtected(shouldProtect);
            }
        }
    }

    private static Serializer getSerializer() throws Exception {
        Registry registry = new Registry();
        registry.bind(String.class, EmptyStringConverter.class);
        Strategy strategy = new AnnotationStrategy(new RegistryStrategy(registry));
        return new Persister(strategy);
    }
}
