package sos.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SOSExcelFileWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSExcelFileWriter.class);
    private ArrayList<Object[]> data;
    private Object[] header;
    private String nameOfSheet = "Sheet";

    public SOSExcelFileWriter() {
        super();
        data = new ArrayList<Object[]>();
    }

    public void addRow(Object[] row) {
        data.add(row);
    }

    public void addHeader(Object[] header) {
        this.header = header;
    }

    public void createFile(String file) {
        createFile(new File(file));
    }

    private int addRow(int rownum, HSSFSheet sheet, Object[] objArr) {
        Row row = sheet.createRow(rownum++);
        int cellnum = 0;
        for (Object obj : objArr) {
            Cell cell = row.createCell(cellnum++);
            if (obj instanceof Date) {
                cell.setCellValue((Date) obj);
            } else if (obj instanceof Boolean) {
                cell.setCellValue((Boolean) obj);
            } else if (obj instanceof String) {
                cell.setCellValue((String) obj);
            } else if (obj instanceof Double) {
                cell.setCellValue((Double) obj);
            }
        }
        return rownum;
    }

    public void createFile(File file) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(nameOfSheet);
        int rownum = 0;
        rownum = addRow(rownum, sheet, header);
        for (int i = 0; i < data.size(); i++) {
            rownum = addRow(rownum, sheet, data.get(i));
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
