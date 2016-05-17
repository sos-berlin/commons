package sos.marshalling;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/** @author Titus Meyer */
public class SOSImExportTableFieldTypes {

    private HashMap fieldTypes = new HashMap();
    private String tableName = null;

    public void addField(String name, String typeName, Integer type, BigInteger length, Integer scale) {
        ArrayList field = new ArrayList(5);
        field.add(name);
        field.add(typeName);
        field.add(type);
        field.add(length);
        field.add(scale);
        fieldTypes.put(name, field);
    }

    public String getTypeName(String field) {
        ArrayList fieldType = (ArrayList) fieldTypes.get(field);
        if (fieldType != null) {
            return (String) fieldType.get(1);
        } else {
            return null;
        }
    }

    public int getTypeId(String field) {
        ArrayList fieldType = (ArrayList) fieldTypes.get(field);
        if (fieldType != null) {
            return ((Integer) fieldType.get(2)).intValue();
        } else {
            return -1;
        }
    }

    public BigInteger getLength(String field) {
        ArrayList fieldType = (ArrayList) fieldTypes.get(field);
        if (fieldType != null) {
            return (BigInteger) fieldType.get(3);
        } else {
            return new BigInteger("-1");
        }
    }

    public int getScale(String field) {
        ArrayList fieldType = (ArrayList) fieldTypes.get(field);
        if (fieldType != null) {
            return ((Integer) fieldType.get(4)).intValue();
        } else {
            return -1;
        }
    }

    public void setTable(String table) {
        this.tableName = table;
    }

    public String getTable() {
        return tableName;
    }

    public String getKeyString() {
        StringBuilder builder = new StringBuilder();
        for (Iterator it = fieldTypes.keySet().iterator(); it.hasNext();) {
            builder.append("\"").append(it.next().toString()).append("\"");
            if (it.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public Iterator getIterator() {
        return fieldTypes.keySet().iterator();
    }

    public int getSize() {
        return fieldTypes.size();
    }

}