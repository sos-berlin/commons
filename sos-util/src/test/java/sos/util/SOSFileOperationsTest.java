package sos.util;

import java.io.File;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/** @author KB */
public class SOSFileOperationsTest {

    private static final Logger LOGGER = Logger.getLogger(SOSFileOperationsTest.class);
    private final String conClassName = "SOSFileOperationsTest";
    private String strReplaceWhat = "";
    private String strReplaceWith = "";
    private String strStringToWorkOn = "";

    public SOSFileOperationsTest() {
        //
    }

    @Test
    public void testDoReplace() throws Exception {
        strReplaceWhat = "Hello";
        strReplaceWith = "World";
        strStringToWorkOn = "Hello";
        doTest("replace the full string", "Hello", "World", strStringToWorkOn, strReplaceWith);
        strReplaceWhat = "(1)abc(12)def(.*)";
        strReplaceWith = "A;BB;CCC";
        strStringToWorkOn = "1abc12def123.txt";
        doTest("replace the groups by absolute values", strReplaceWhat, strReplaceWith, strStringToWorkOn, "AabcBBdefCCC");
        strReplaceWhat = "(INT_)(.*)";
        strReplaceWith = "\\-;\\2";
        strStringToWorkOn = "INT_4711-0815.txt";
        doTest("suppres the first part of the value", strReplaceWhat, strReplaceWith, strStringToWorkOn, "4711-0815.txt");
        strReplaceWhat = "(.{5})(.{6})(.*)";
        strReplaceWith = "\\1;;\\3;";
        strStringToWorkOn = "abcba123456hallo.txt";
        doTest("suppres the first part of the value", strReplaceWhat, strReplaceWith, strStringToWorkOn, "abcbahallo.txt");
        doTest("Swapping", "(1)abc(12)def(.*)", "\\2;\\1;CCC", "1abc12def123.txt", "12abc1defCCC");
        doTest("Swapping", "(1)abc(12)def(.*)", "\\2;\\1;\\-", "1abc12def123.txt", "12abc1def");
        doTest("Prefix", "(.*)", "prefix\\1", "1abc12def123.txt", "prefix1abc12def123.txt");
        doTest("Prefix", "(.*)", "\\1suffix", "1abc12def123.txt", "1abc12def123.txtsuffix");
        doTest("Prefix", "(.*)", "prefix\\1suffix", "1abc12def123.txt", "prefix1abc12def123.txtsuffix");
        doTest("Prefix", "(prefix)(.*)", "\\-;\\2", "prefix1abc12def123.txt", "1abc12def123.txt");
        String strDate = SOSDate.getCurrentTimeAsString("yyyyMMddHHmm");
        doTest("Date insertion", "(.*)(.txt)", "\\1_[date:yyyyMMddHHmm];\\2", "1.txt", "1_" + strDate + ".txt");
        strDate = SOSDate.getCurrentTimeAsString();
        doTest("Date insertion without date-format", "(.*)(.txt)", "\\1_[date:];\\2", "1.txt", "1_" + strDate + ".txt");
        strDate = SOSDate.getCurrentTimeAsString();
        doTest("Date suffix", "(.*)(.txt)", "\\1;\\2_[date:]", "1.txt", "1.txt_" + strDate);
        strDate = SOSDate.getCurrentTimeAsString();
        doTest("Date prefix", "(.*)(.txt)", "[date:]_\\1;\\2", "1.txt", strDate + "_1.txt");
    }

    @Test
    public void VariableFileNameTest() throws Exception {
        doTest("FileName uppercase", ".*", "[filename:uppercase]", "1.txt", "1.TXT");
        doTest("FileName lowercase", ".*", "[filename:lowercase]", "1.txt", "1.txt");
    }

    private void doTest(final String strText, final String strReplaceWhat, final String strReplaceWith, final String strWork, final String strExpectedResult)
            throws Exception {
        String strResult = SOSFileOperations.getReplacementFilename(strWork, strReplaceWhat, strReplaceWith);
        assertEquals(strText, strExpectedResult, strResult);
    }

    @Test
    public void ResultListTest() throws Exception {
        String file = "c:/temp";
        String fileSpec = "^.*\\.kb$";
        String minFileAge = "0";
        String maxFileAge = "0";
        int skipFirstFiles = 0;
        int skipLastFiles = 0;
        String minFileSize = "-1";
        String maxFileSize = "-1";
        SOSSchedulerLogger objSOSLogger = null;
        boolean flgOperationWasSuccessful = SOSFileOperations.existsFile(file, fileSpec, Pattern.CASE_INSENSITIVE, minFileAge, maxFileAge, minFileSize, maxFileSize, 
                skipFirstFiles, skipLastFiles, objSOSLogger);
        System.out.println("flgOperationWasSuccessful: " + flgOperationWasSuccessful);
        if (flgOperationWasSuccessful) {
            Vector<File> lstR = SOSFileOperations.lstResultList;
            int intNoOfHits = lstR.size();
            LOGGER.info("intNoOfHits = " + intNoOfHits);
            for (File file2 : lstR) {
                LOGGER.info("FileName = " + file2.getAbsolutePath());
            }
        }
    }

}