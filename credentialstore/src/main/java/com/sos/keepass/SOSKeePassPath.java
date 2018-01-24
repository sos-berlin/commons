package com.sos.keepass;

import sos.util.SOSString;

public class SOSKeePassPath {

    public static final String PATH_PREFIX = "cs://";
    public static final String PROPERTY_PREFIX = "@";

    private boolean _valid;
    private String _entry;
    private String _entryPath;
    private String _propertyName;
    private String _originalPropertyName;

    public SOSKeePassPath(final boolean isKdbx, final String path) {
        this(isKdbx, path, null);
    }

    public SOSKeePassPath(final boolean isKdbx, final String path, final String entryPath) {
        if (path == null || !path.startsWith(PATH_PREFIX) || !path.contains(PROPERTY_PREFIX)) {
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

    @Override
    public String toString() {
        if (!_valid) {
            return "";
        }
        return _entry + PROPERTY_PREFIX + _propertyName;
    }
}
