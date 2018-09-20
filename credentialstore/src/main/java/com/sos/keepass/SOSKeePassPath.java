package com.sos.keepass;

import java.util.Map;

import org.linguafranca.pwdb.Entry;

import com.google.common.base.Splitter;

import sos.util.SOSString;

public class SOSKeePassPath {

    public static final String PATH_PREFIX = "cs://";
    public static final String PROPERTY_PREFIX = "@";

    public static final String QUERY_PARAMETER_FILE = "file";
    public static final String QUERY_PARAMETER_KEY_FILE = "key_file";
    public static final String QUERY_PARAMETER_PASSWORD = "password";
    // not set or ignore_expired=0 - throwing an entry expired exception if an entry is expired
    public static final String QUERY_PARAMETER_IGNORE_EXPIRED = "ignore_expired";
    public static final String QUERY_PARAMETER_ATTACHMENT = "attachment";

    private boolean _isKdbx;
    private boolean _valid;
    private String _entry;
    private String _entryPath;
    private String _propertyName;
    private String _originalPropertyName;
    private String _query;
    private Map<String, String> _queryParameters;
    private String _error;
    private Entry<?, ?, ?, ?> _databaseEntry;

    /** @param uri example: cs://server/SFTP/my_server@user?file=my_file.kdbx&key_file=my_keyfile.key&password=test&ignore_expired=1&attachment=1 */
    public SOSKeePassPath(final String uri) {
        if (SOSString.isEmpty(uri)) {
            _error = "missing uri";
            return;
        }
        int t = uri == null ? -1 : uri.indexOf("?");
        if (t < 0 || uri.endsWith("?")) {
            _error = "missing query parameters";
            return;
        }
        _query = uri.substring(t + 1, uri.length());
        _queryParameters = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(_query);
        String file = _queryParameters.get(QUERY_PARAMETER_FILE);
        if (SOSString.isEmpty(file)) {
            _error = String.format("missing query parameter '%s'", QUERY_PARAMETER_FILE);
        } else {
            parse(file.toLowerCase().endsWith(".kdbx"), uri.substring(0, t), null);
        }
    }

    public SOSKeePassPath(final boolean isKdbx, final String path) {
        this(isKdbx, path, null);
    }

    public SOSKeePassPath(final boolean isKdbx, final String path, final String entryPath) {
        parse(isKdbx, path, entryPath);
    }

    private void parse(final boolean isKdbx, final String path, final String entryPath) {
        _isKdbx = isKdbx;
        if (path == null || !path.startsWith(PATH_PREFIX) || !path.contains(PROPERTY_PREFIX)) {
            _error = String.format("is empty or not starts with '%s' or not contains '%s'", PATH_PREFIX, PROPERTY_PREFIX);
            return;
        }

        String[] arr = path.substring(PATH_PREFIX.length() - 1).split(PROPERTY_PREFIX);
        switch (arr.length) {
        case 2:
            setEntryPath(arr[0], entryPath, isKdbx);
            setPropertyName(arr[1]);
            if (_entry != null) {
                _valid = true;
            }
            break;
        }
    }

    public static boolean hasKeePassVariables(String command) {
        return command != null && command.contains("${" + PATH_PREFIX);
    }

    private void setEntryPath(final String path, final String entryPath, final boolean isKdbx) {
        if (path.equals("/")) {
            _entry = SOSString.isEmpty(entryPath) ? null : entryPath;
        } else {
            _entry = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        }
        if (_entry != null) {
            _entryPath = isKdbx ? _entry : "/" + SOSKeePassDatabase.KDB_ROOT_GROUP_NAME + _entry;
        }
    }

    private void setPropertyName(final String name) {
        _originalPropertyName = name;
        _propertyName = SOSKeePassDatabase.getPropertyName(_originalPropertyName);
    }

    public boolean isKdbx() {
        return _isKdbx;
    }

    public boolean isValid() {
        return _valid;
    }

    public String getEntry() {
        return _entry;
    }

    public String getEntryPath() {
        return _entryPath;
    }

    public String getPropertyName() {
        return _propertyName;
    }

    public String getOriginalPropertyName() {
        return _originalPropertyName;
    }

    public String getQuery() {
        return _query;
    }

    public Map<String, String> getQueryParameters() {
        return _queryParameters;
    }

    public String getError() {
        return _error;
    }

    public void setDatabaseEntry(Entry<?, ?, ?, ?> val) {
        _databaseEntry = val;
    }

    public Entry<?, ?, ?, ?> getDatabaseEntry() {
        return _databaseEntry;
    }

    @Override
    public String toString() {
        if (!_valid) {
            return "";
        }
        return _entry + PROPERTY_PREFIX + _propertyName;
    }
}
