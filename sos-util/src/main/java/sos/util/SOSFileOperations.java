package sos.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

/** @author Florian Schreiber
 * @deprecated because it is a singleton, use SOSFileSystemOperations */
@Deprecated
public class SOSFileOperations {

    private static final Logger LOGGER = Logger.getLogger(SOSFileOperations.class);
    private static Hashtable<String, String> BOOL = new Hashtable<String, String>();
    public static final int CREATE_DIR = 0x01;
    public static final int GRACIOUS = 0x02;
    public static final int NOT_OVERWRITE = 0x04;
    public static final int RECURSIVE = 0x08;
    public static final int REMOVE_DIR = 0x10;
    public static final int WIPE = 0x20;
    public static Vector<File> lstResultList = null;

    static {
        SOSFileOperations.BOOL.put("true", "true");
        SOSFileOperations.BOOL.put("false", "false");
        SOSFileOperations.BOOL.put("j", "true");
        SOSFileOperations.BOOL.put("ja", "true");
        SOSFileOperations.BOOL.put("y", "true");
        SOSFileOperations.BOOL.put("yes", "true");
        SOSFileOperations.BOOL.put("n", "false");
        SOSFileOperations.BOOL.put("nein", "false");
        SOSFileOperations.BOOL.put("no", "false");
        SOSFileOperations.BOOL.put("on", "true");
        SOSFileOperations.BOOL.put("off", "false");
        SOSFileOperations.BOOL.put("1", "true");
        SOSFileOperations.BOOL.put("0", "false");
        SOSFileOperations.BOOL.put("all", "true");
        SOSFileOperations.BOOL.put("none", "false");
    }

    public static boolean toBoolean(String value) throws Exception {
        try {
            if (value == null) {
                throw new Exception("null");
            }
            String v = value.toLowerCase();
            String bool = (String) SOSFileOperations.BOOL.get(v);
            if (bool == null) {
                throw new Exception("\"" + value + "\"");
            }
            return "true".equals(bool);
        } catch (Exception e) {
            throw new Exception("cannot evaluate to boolean: " + e.getMessage());
        }
    }

    public static boolean canWrite(String file) throws Exception {
        File filename = new File(file);
        return canWrite(filename, null, 0);
    }

    public static boolean canWrite(String file, String fileSpec) throws Exception {
        File filename = new File(file);
        return canWrite(filename, fileSpec, Pattern.CASE_INSENSITIVE);
    }

    public static boolean canWrite(String file, String fileSpec, int fileSpecFlags) throws Exception {
        File filename = new File(file);
        return canWrite(filename, fileSpec, fileSpecFlags);
    }

    public static boolean canWrite(File file) throws Exception {
        return canWrite(file, null, 0);
    }

    public static boolean canWrite(File file, String fileSpec) throws Exception {
        return canWrite(file, fileSpec, Pattern.CASE_INSENSITIVE);
    }

    public static boolean canWrite(File file, String fileSpec, int fileSpecFlags) throws Exception {
        try {
            logDebug1("arguments for canWrite:");
            logDebug1("argument file=" + file.toString());
            logDebug1("argument fileSpec=" + fileSpec);
            String msg = "";
            if (has(fileSpecFlags, Pattern.CANON_EQ)) {
                msg += "CANON_EQ";
            }
            if (has(fileSpecFlags, Pattern.CASE_INSENSITIVE)) {
                msg += "CASE_INSENSITIVE";
            }
            if (has(fileSpecFlags, Pattern.COMMENTS)) {
                msg += "COMMENTS";
            }
            if (has(fileSpecFlags, Pattern.DOTALL)) {
                msg += "DOTALL";
            }
            if (has(fileSpecFlags, Pattern.MULTILINE)) {
                msg += "MULTILINE";
            }
            if (has(fileSpecFlags, Pattern.UNICODE_CASE)) {
                msg += "UNICODE_CASE";
            }
            if (has(fileSpecFlags, Pattern.UNIX_LINES)) {
                msg += "UNIX_LINES";
            }
            logDebug1("argument fileSpecFlags=" + msg);
            String filename = file.getPath();
            filename = substituteAllDate(filename);
            Matcher m = Pattern.compile("\\[[^]]*\\]").matcher(filename);
            if (m.find()) {
                throw new Exception("unsupported file mask found: " + m.group());
            }
            file = new File(filename);
            if (!file.exists()) {
                log("checking file " + file.getAbsolutePath() + ": no such file or directory");
                return true;
            } else {
                if (!file.isDirectory()) {
                    log("checking the file " + file.getCanonicalPath() + ":: file exists");
                    boolean writable = false;
                    RandomAccessFile randomAccessFile = null;
                    try {
                        randomAccessFile = new RandomAccessFile(file.getAbsolutePath(), "rw");
                        writable = true;
                    } catch (Exception e) {
                        //
                    } finally {
                        if (randomAccessFile != null) {
                            try {
                                randomAccessFile.close();
                            } catch (Exception e) {} 
                        }
                    }
                    if (!writable) {
                        log("file " + file.getCanonicalPath() + ": cannot be written ");
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    if (fileSpec == null || fileSpec.isEmpty()) {
                        log("checking file " + file.getCanonicalPath() + ": directory exists");
                        return true;
                    }
                    Vector<File> fileList = getFilelist(file.getPath(), fileSpec, fileSpecFlags, false, 0, 0, -1, -1, 0, 0);
                    if (fileList.isEmpty()) {
                        log("checking file " + file.getCanonicalPath() + ": directory contains no files matching " + fileSpec);
                        return false;
                    } else {
                        log("checking file " + file.getCanonicalPath() + ": directory contains " + fileList.size() + " file(s) matching " + fileSpec);
                        for (int i = 0; i < fileList.size(); i++) {
                            File checkFile = (File) fileList.get(i);
                            log("found " + checkFile.getCanonicalPath());
                            boolean writable = false;
                            RandomAccessFile randomAccessFile = null;
                            try {
                                randomAccessFile = new RandomAccessFile(file.getAbsolutePath(), "rw");
                                writable = true;
                            } catch (Exception e) {
                                //
                            } finally {
                                if (randomAccessFile != null) {
                                    try {
                                        randomAccessFile.close();
                                    } catch (Exception e) {} 
                                }
                            }
                            if (!writable) {
                                log("file " + checkFile.getCanonicalPath() + ": cannot be written ");
                                return false;
                            }
                        }
                        lstResultList = fileList;
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("error checking file: " + file.toString() + ": " + e.getMessage());
        }
    }

    public static boolean existsFile(String file) throws Exception {
        File filename = new File(file);
        return existsFile(filename, null, 0);
    }

    public static boolean existsFile(String file, String fileSpec) throws Exception {
        File filename = new File(file);
        return existsFile(filename, fileSpec, Pattern.CASE_INSENSITIVE);
    }

    public static boolean existsFile(String file, String fileSpec, int fileSpecFlags) throws Exception {
        File filename = new File(file);
        return existsFile(filename, fileSpec, fileSpecFlags);
    }

    public static boolean existsFile(String file, String fileSpec, int fileSpecFlags, String minFileAge, String maxFileAge, String minFileSize,
            String maxFileSize, int skipFirstFiles, int skipLastFiles) throws Exception {
        File filename = new File(file);
        return existsFile(filename, fileSpec, fileSpecFlags, minFileAge, maxFileAge, minFileSize, maxFileSize, skipFirstFiles, skipLastFiles);
    }

    public static boolean existsFile(String file, String fileSpec, int fileSpecFlags, String minFileAge, String maxFileAge, String minFileSize,
            String maxFileSize, int skipFirstFiles, int skipLastFiles, int minNumOfFiles, int maxNumOfFiles) throws Exception {
        File filename = new File(file);
        return existsFile(filename, fileSpec, fileSpecFlags, minFileAge, maxFileAge, minFileSize, maxFileSize, skipFirstFiles, skipLastFiles,
                minNumOfFiles, maxNumOfFiles);
    }

    public static boolean existsFile(File file) throws Exception {
        return existsFile(file, null, 0);
    }

    public static boolean existsFile(File file, String fileSpec) throws Exception {
        return existsFile(file, fileSpec, Pattern.CASE_INSENSITIVE);
    }

    public static boolean existsFile(File file, String fileSpec, int fileSpecFlags) throws Exception {
        return existsFile(file, fileSpec, fileSpecFlags, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean existsFile(File file, String fileSpec, int fileSpecFlags, String minFileAge, String maxFileAge, String minFileSize,
            String maxFileSize, int skipFirstFiles, int skipLastFiles) throws Exception {
        return existsFile(file, fileSpec, fileSpecFlags, minFileAge, maxFileAge, minFileSize, maxFileSize, skipFirstFiles, skipLastFiles, -1, -1);
    }

    public static boolean existsFile(File file, String fileSpec, int fileSpecFlags, String minFileAge, String maxFileAge, String minFileSize,
            String maxFileSize, int skipFirstFiles, int skipLastFiles, int minNumOfFiles, int maxNumOfFiles) throws Exception {
        long minAge = 0;
        long maxAge = 0;
        long minSize = -1;
        long maxSize = -1;
        try {
            logDebug1("arguments for existsFile:");
            logDebug1("argument file=" + file.toString());
            logDebug1("argument fileSpec=" + fileSpec);
            String msg = "";
            if (has(fileSpecFlags, Pattern.CANON_EQ)) {
                msg += "CANON_EQ";
            }
            if (has(fileSpecFlags, Pattern.CASE_INSENSITIVE)) {
                msg += "CASE_INSENSITIVE";
            }
            if (has(fileSpecFlags, Pattern.COMMENTS)) {
                msg += "COMMENTS";
            }
            if (has(fileSpecFlags, Pattern.DOTALL)) {
                msg += "DOTALL";
            }
            if (has(fileSpecFlags, Pattern.MULTILINE)) {
                msg += "MULTILINE";
            }
            if (has(fileSpecFlags, Pattern.UNICODE_CASE)) {
                msg += "UNICODE_CASE";
            }
            if (has(fileSpecFlags, Pattern.UNIX_LINES)) {
                msg += "UNIX_LINES";
            }
            logDebug1("argument fileSpecFlags=" + msg);
            logDebug1("argument minFileAge=" + minFileAge);
            logDebug1("argument maxFileAge=" + maxFileAge);
            minAge = calculateFileAge(minFileAge);
            maxAge = calculateFileAge(maxFileAge);
            logDebug1("argument minFileSize=" + minFileSize);
            logDebug1("argument maxFileSize=" + maxFileSize);
            minSize = calculateFileSize(minFileSize);
            maxSize = calculateFileSize(maxFileSize);
            logDebug1("argument skipFirstFiles=" + skipFirstFiles);
            logDebug1("argument skipLastFiles=" + skipLastFiles);
            logDebug1("argument minNumOfFiles=" + minNumOfFiles);
            logDebug1("argument maxNumOfFiles=" + maxNumOfFiles);
            if (skipFirstFiles < 0) {
                throw new Exception("[" + skipFirstFiles + "] is no valid value for skipFirstFiles");
            }
            if (skipLastFiles < 0) {
                throw new Exception("[" + skipLastFiles + "] is no valid value for skipLastFiles");
            }
            if (skipFirstFiles > 0 && skipLastFiles > 0) {
                throw new Exception("skip only either first files or last files");
            }
            if ((skipFirstFiles > 0 || skipLastFiles > 0) && minAge == 0 && maxAge == 0 && minSize == -1 && maxSize == -1) {
                throw new Exception("missed constraint for file skipping (minFileAge, maxFileAge, minFileSize, maxFileSize)");
            }
            String filename = file.getPath();
            filename = substituteAllDate(filename);
            Matcher m = Pattern.compile("\\[[^]]*\\]").matcher(filename);
            if (m.find()) {
                throw new Exception("unsupported file mask found: " + m.group());
            }
            file = new File(filename);
            if (!file.exists()) {
                log("checking file " + file.getAbsolutePath() + ": no such file or directory");
                return false;
            } else {
                if (!file.isDirectory()) {
                    log("checking file " + file.getCanonicalPath() + ": file exists");
                    long currentTime = System.currentTimeMillis();
                    if (minAge > 0) {
                        long interval = currentTime - file.lastModified();
                        if (interval < 0) {
                            throw new Exception("Cannot filter by file age. File [" + file.getCanonicalPath() + "] was modified in the future.");
                        }
                        if (interval < minAge) {
                            log("checking file age " + file.lastModified() + ": minimum age required is " + minAge);
                            return false;
                        }
                    }
                    if (maxAge > 0) {
                        long interval = currentTime - file.lastModified();
                        if (interval < 0) {
                            throw new Exception("Cannot filter by file age. File [" + file.getCanonicalPath() + "] was modified in the future.");
                        }
                        if (interval > maxAge) {
                            log("checking file age " + file.lastModified() + ": maximum age required is " + maxAge);
                            return false;
                        }
                    }
                    if (minSize > -1 && minSize > file.length()) {
                        log("checking file size " + file.length() + ": minimum size required is " + minFileSize);
                        return false;
                    }
                    if (maxSize > -1 && maxSize < file.length()) {
                        log("checking file size " + file.length() + ": maximum size required is " + maxFileSize);
                        return false;
                    }
                    if (skipFirstFiles > 0 || skipLastFiles > 0) {
                        log("file skipped");
                        return false;
                    }
                    return true;
                } else {
                    if (fileSpec == null || fileSpec.isEmpty()) {
                        log("checking file " + file.getCanonicalPath() + ": directory exists");
                        return true;
                    }
                    Vector<File> fileList =
                            getFilelist(file.getPath(), fileSpec, fileSpecFlags, false, minAge, maxAge, minSize, maxSize, skipFirstFiles, skipLastFiles);
                    if (fileList.isEmpty()) {
                        log("checking file " + file.getCanonicalPath() + ": directory contains no files matching " + fileSpec);
                        return false;
                    } else {
                        log("checking file " + file.getCanonicalPath() + ": directory contains " + fileList.size() + " file(s) matching " + fileSpec);
                        for (int i = 0; i < fileList.size(); i++) {
                            File checkFile = (File) fileList.get(i);
                            log("found " + checkFile.getCanonicalPath());
                        }
                        if (minNumOfFiles >= 0 && fileList.size() < minNumOfFiles) {
                            log("found " + fileList.size() + " files, minimum expected " + minNumOfFiles + " files");
                            return false;
                        }
                        if (maxNumOfFiles >= 0 && fileList.size() > maxNumOfFiles) {
                            log("found " + fileList.size() + " files, maximum expected " + maxNumOfFiles + " files");
                            return false;
                        }
                        lstResultList = fileList;
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("error checking file: " + file.toString() + ": " + e.getMessage());
        }
    }

    public static boolean removeFile(File file) throws Exception {
        return removeFile(file, ".*", 0, Pattern.CASE_INSENSITIVE, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean removeFile(File file, int flags) throws Exception {
        return removeFile(file, ".*", flags, Pattern.CASE_INSENSITIVE, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean removeFile(File file, String fileSpec) throws Exception {
        return removeFile(file, fileSpec, 0, Pattern.CASE_INSENSITIVE, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean removeFile(File file, String fileSpec, int flags) throws Exception {
        return removeFile(file, fileSpec, flags, Pattern.CASE_INSENSITIVE, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean removeFile(File file, String fileSpec, int flags, int fileSpecFlags) throws Exception {
        return removeFile(file, fileSpec, flags, fileSpecFlags, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean removeFile(File file, String fileSpec, int flags, int fileSpecFlags, String minFileAge, String maxFileAge,
            String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles) throws Exception {
        int nrOfRemovedObjects =
                removeFileCnt(file, fileSpec, flags, fileSpecFlags, minFileAge, maxFileAge, minFileSize, maxFileSize, skipFirstFiles, skipLastFiles);
        return nrOfRemovedObjects > 0;
    }

    public static int removeFileCnt(File file, String fileSpec, int flags, int fileSpecFlags, String minFileAge, String maxFileAge,
            String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles) throws Exception {
        boolean gracious;
        boolean recursive;
        boolean removeDir;
        boolean wipe;
        int nrOfRemovedFiles = 0;
        int nrOfRemovedDirectories = 0;
        long minAge = 0;
        long maxAge = 0;
        long minSize = -1;
        long maxSize = -1;
        try {
            recursive = has(flags, SOSFileOperations.RECURSIVE);
            gracious = has(flags, SOSFileOperations.GRACIOUS);
            wipe = has(flags, SOSFileOperations.WIPE);
            removeDir = has(flags, SOSFileOperations.REMOVE_DIR);
            logDebug1("arguments for removeFile:");
            logDebug1("argument file=" + file);
            logDebug1("argument fileSpec=" + fileSpec);
            String msg = "";
            if (has(flags, SOSFileOperations.GRACIOUS)) {
                msg += "GRACIOUS ";
            }
            if (has(flags, SOSFileOperations.RECURSIVE)) {
                msg += "RECURSIVE ";
            }
            if (has(flags, SOSFileOperations.REMOVE_DIR)) {
                msg += "REMOVE_DIR ";
            }
            if (has(flags, SOSFileOperations.WIPE)) {
                msg += "WIPE ";
            }
            logDebug1("argument flags=" + msg);
            msg = "";
            if (has(fileSpecFlags, Pattern.CANON_EQ)) {
                msg += "CANON_EQ ";
            }
            if (has(fileSpecFlags, Pattern.CASE_INSENSITIVE)) {
                msg += "CASE_INSENSITIVE ";
            }
            if (has(fileSpecFlags, Pattern.COMMENTS)) {
                msg += "COMMENTS ";
            }
            if (has(fileSpecFlags, Pattern.DOTALL)) {
                msg += "DOTALL ";
            }
            if (has(fileSpecFlags, Pattern.MULTILINE)) {
                msg += "MULTILINE ";
            }
            if (has(fileSpecFlags, Pattern.UNICODE_CASE)) {
                msg += "UNICODE_CASE ";
            }
            if (has(fileSpecFlags, Pattern.UNIX_LINES)) {
                msg += "UNIX_LINES ";
            }
            logDebug1("argument fileSpecFlags=" + msg);
            logDebug1("argument minFileAge=" + minFileAge);
            logDebug1("argument maxFileAge=" + maxFileAge);
            minAge = calculateFileAge(minFileAge);
            maxAge = calculateFileAge(maxFileAge);
            logDebug1("argument minFileSize=" + minFileSize);
            logDebug1("argument maxFileSize=" + maxFileSize);
            minSize = calculateFileSize(minFileSize);
            maxSize = calculateFileSize(maxFileSize);
            logDebug1("argument skipFirstFiles=" + skipFirstFiles);
            logDebug1("argument skipLastFiles=" + skipLastFiles);
            if (skipFirstFiles < 0) {
                throw new Exception("[" + skipFirstFiles + "] is no valid value for skipFirstFiles");
            }
            if (skipLastFiles < 0) {
                throw new Exception("[" + skipLastFiles + "] is no valid value for skipLastFiles");
            }
            if (skipFirstFiles > 0 && skipLastFiles > 0) {
                throw new Exception("skip only either first files or last files");
            }
            if ((skipFirstFiles > 0 || skipLastFiles > 0) && minAge == 0 && maxAge == 0 && minSize == -1 && maxSize == -1) {
                throw new Exception("missed constraint for file skipping (minFileAge, maxFileAge, minFileSize, maxFileSize)");
            }
            if (!file.exists()) {
                if (gracious) {
                    log("cannot remove file: file does not exist: " + file.getCanonicalPath());
                    return 0;
                } else {
                    throw new Exception("file does not exist: " + file.getCanonicalPath());
                }
            }
            Vector<File> fileList;
            if (file.isDirectory()) {
                if (!file.canRead()) {
                    throw new Exception("directory is not readable: " + file.getCanonicalPath());
                }
                log("remove [" + fileSpec + "] from " + file.getCanonicalPath() + (recursive ? " (recursive)" : ""));
                fileList =
                        getFilelist(file.getPath(), fileSpec, fileSpecFlags, has(flags, SOSFileOperations.RECURSIVE), minAge, maxAge, minSize,
                                maxSize, skipFirstFiles, skipLastFiles);
            } else {
                fileList = new Vector<File>();
                fileList.add(file);
                fileList = filelistFilterAge(fileList, minAge, maxAge);
                fileList = filelistFilterSize(fileList, minSize, maxSize);
                if (skipFirstFiles > 0 || skipLastFiles > 0) {
                    fileList.clear();
                }
            }
            File currentFile;
            for (int i = 0; i < fileList.size(); i++) {
                currentFile = (File) fileList.get(i);
                log("remove file " + currentFile.getCanonicalPath());
                if (wipe) {
                    if (!wipe(currentFile)) {
                        throw new Exception("cannot remove file: " + currentFile.getCanonicalPath());
                    }
                } else {
                    if (!currentFile.delete()) {
                        throw new Exception("cannot remove file: " + currentFile.getCanonicalPath());
                    }
                }
                nrOfRemovedFiles++;
            }
            if (removeDir) {
                int firstSize = SOSFile.getFolderlist(file.getPath(), ".*", 0, recursive).size();
                if (recursive) {
                    recDeleteEmptyDir(file, fileSpec, fileSpecFlags);
                } else {
                    Vector<File> list = SOSFile.getFolderlist(file.getPath(), fileSpec, fileSpecFlags);
                    File f;
                    for (int i = 0; i < list.size(); i++) {
                        f = (File) list.get(i);
                        if (f.isDirectory()) {
                            if (!f.canRead()) {
                                throw new Exception("directory is not readable: " + f.getCanonicalPath());
                            }
                            if (f.list().length == 0) {
                                if (!f.delete()) {
                                    throw new Exception("cannot remove directory: " + f.getCanonicalPath());
                                }
                                log("remove directory " + f.getPath());
                            } else {
                                logDebug3("directory [" + f.getCanonicalPath() + "] cannot be removed because it is not empty");
                                String lst = f.list()[0];
                                for (int n = 1; n < f.list().length; n++) {
                                    lst += ", " + f.list()[n];
                                }
                                logDebug9("          contained files " + f.list().length + ": " + lst);
                            }
                        }
                    }
                }
                nrOfRemovedDirectories = firstSize - SOSFile.getFolderlist(file.getPath(), ".*", 0, recursive).size();
            }
            msg = "";
            if (removeDir) {
                if (nrOfRemovedDirectories == 1) {
                    msg = " + 1 directory removed";
                } else {
                    msg = " + " + nrOfRemovedDirectories + " directories removed";
                }
            }
            log(nrOfRemovedFiles + " file(s) removed" + msg);
            lstResultList = fileList;
            return nrOfRemovedFiles + nrOfRemovedDirectories;
        } catch (Exception e) {
            throw new Exception("error occurred removing file(s): " + e.getMessage());
        }
    }

    private static boolean recDeleteEmptyDir(File dir, String fileSpec, int fileSpecFlags) throws Exception {
        if (dir.isDirectory()) {
            if (!dir.canRead()) {
                throw new Exception("directory is not readable: " + dir.getCanonicalPath());
            }
        } else {
            return false;
        }
        File[] list = dir.listFiles();
        if (list.length == 0) {
            return true;
        }
        Pattern p = Pattern.compile(fileSpec, fileSpecFlags);
        File f;
        for (int i = 0; i < list.length; i++) {
            f = list[i];
            if (recDeleteEmptyDir(f, fileSpec, fileSpecFlags)) {
                if (p.matcher(f.getName()).matches()) {
                    if (!f.delete()) {
                        throw new Exception("cannot remove directory: " + f.getCanonicalPath());
                    }
                    log("remove directory " + f.getPath());
                }
            } else {
                if (f.isDirectory()) {
                    logDebug3("directory [" + f.getCanonicalPath() + "] cannot be removed because it is not empty");
                    String lst = f.list()[0];
                    for (int n = 1; n < f.list().length; n++) {
                        lst += ", " + f.list()[n];
                    }
                    logDebug9("          contained files " + f.list().length + ": " + lst);
                }
            }
        }
        return dir.list().length == 0;
    }

    public static boolean removeFile(String file) throws Exception {
        return removeFile(new File(file));
    }

    public static boolean removeFile(String file, int flags) throws Exception {
        return removeFile(new File(file), flags);
    }

    public static boolean removeFile(String file, String fileSpec) throws Exception {
        return removeFile(new File(file), fileSpec);
    }

    public static boolean removeFile(String file, String fileSpec, int flags) throws Exception {
        return removeFile(new File(file), fileSpec, flags);
    }

    public static boolean removeFile(String file, String fileSpec, int flags, int fileSpecFlags) throws Exception {
        return removeFile(new File(file), fileSpec, flags, fileSpecFlags);
    }

    public static boolean removeFile(String file, String fileSpec, int flags, int fileSpecFlags, String minFileAge, String maxFileAge,
            String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles) throws Exception {
        return removeFile(new File(file), fileSpec, flags, fileSpecFlags, minFileAge, maxFileAge, minFileSize, maxFileSize, skipFirstFiles,
                skipLastFiles);
    }

    public static int removeFileCnt(String file, String fileSpec, int flags, int fileSpecFlags, String minFileAge, String maxFileAge,
            String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles) throws Exception {
        return removeFileCnt(new File(file), fileSpec, flags, fileSpecFlags, minFileAge, maxFileAge, minFileSize, maxFileSize, skipFirstFiles,
                skipLastFiles);
    }

    public static boolean copyFile(File source, File target) throws Exception {
        return copyFile(source, target, ".*", 0, Pattern.CASE_INSENSITIVE, null, null, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean copyFile(File source, File target, int flags) throws Exception {
        return copyFile(source, target, ".*", flags, Pattern.CASE_INSENSITIVE, null, null, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean copyFile(File source, File target, String fileSpec) throws Exception {
        return copyFile(source, target, fileSpec, 0, Pattern.CASE_INSENSITIVE, null, null, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean copyFile(File source, File target, String fileSpec, int flags) throws Exception {
        return copyFile(source, target, fileSpec, flags, Pattern.CASE_INSENSITIVE, null, null, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean copyFile(File source, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement) throws Exception {
        return copyFile(source, null, fileSpec, flags, fileSpecFlags, replacing, replacement, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean copyFile(File source, File target, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement)
            throws Exception {
        return copyFile(source, target, fileSpec, flags, fileSpecFlags, replacing, replacement, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean copyFile(File source, File target, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement,
            String minFileAge, String maxFileAge, String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles) throws Exception {
        String mode = "copy";
        return transferFile(source, target, fileSpec, flags, fileSpecFlags, replacing, replacement, minFileAge, maxFileAge, minFileSize, maxFileSize,
                skipFirstFiles, skipLastFiles, mode);
    }

    public static boolean copyFile(String source, String target) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return copyFile(sourceFile, targetFile);
    }

    public static boolean copyFile(String source, String target, int flags) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return copyFile(sourceFile, targetFile, flags);
    }

    public static boolean copyFile(String source, String target, String fileSpec) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return copyFile(sourceFile, targetFile, fileSpec);
    }

    public static boolean copyFile(String source, String target, String fileSpec, int flags) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return copyFile(sourceFile, targetFile, fileSpec, flags);
    }

    public static boolean copyFile(String source, String target, String fileSpec, int flags, int fileSpecFlags) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return copyFile(sourceFile, targetFile, fileSpec, flags, fileSpecFlags, null, null, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean copyFile(String source, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement) throws Exception {
        File sourceFile = new File(source);
        return copyFile(sourceFile, fileSpec, flags, fileSpecFlags, replacing, replacement);
    }

    public static boolean copyFile(String source, String target, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement)
            throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return copyFile(sourceFile, targetFile, fileSpec, flags, fileSpecFlags, replacing, replacement);
    }

    public static boolean copyFile(String source, String target, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement,
            String minFileAge, String maxFileAge, String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return copyFile(sourceFile, targetFile, fileSpec, flags, fileSpecFlags, replacing, replacement, minFileAge, maxFileAge, minFileSize,
                maxFileSize, skipFirstFiles, skipLastFiles);
    }

    public static int copyFileCnt(String source, String target, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement,
            String minFileAge, String maxFileAge, String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        String mode = "copy";
        return transferFileCnt(sourceFile, targetFile, fileSpec, flags, fileSpecFlags, replacing, replacement, minFileAge, maxFileAge, minFileSize,
                maxFileSize, skipFirstFiles, skipLastFiles, mode);
    }

    public static boolean renameFile(File source, File target) throws Exception {
        return renameFile(source, target, ".*", 0, Pattern.CASE_INSENSITIVE, null, null, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean renameFile(File source, File target, int flags) throws Exception {
        return renameFile(source, target, ".*", flags, Pattern.CASE_INSENSITIVE, null, null, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean renameFile(File source, File target, String fileSpec) throws Exception {
        return renameFile(source, target, fileSpec, 0, Pattern.CASE_INSENSITIVE, null, null, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean renameFile(File source, File target, String fileSpec, int flags) throws Exception {
        return renameFile(source, target, fileSpec, flags, Pattern.CASE_INSENSITIVE, null, null, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean renameFile(File source, File target, String fileSpec, int flags, int fileSpecFlags) throws Exception {
        return renameFile(source, target, fileSpec, flags, fileSpecFlags, null, null, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean renameFile(File source, File target, String fileSpec, String replacing, String replacement) throws Exception {
        return renameFile(source, target, fileSpec, 0, Pattern.CASE_INSENSITIVE, replacing, replacement, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean renameFile(File source, File target, String fileSpec, int flags, String replacing, String replacement) throws Exception {
        return renameFile(source, target, fileSpec, flags, Pattern.CASE_INSENSITIVE, replacing, replacement, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean renameFile(File source, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement) throws Exception {
        return renameFile(source, null, fileSpec, flags, fileSpecFlags, replacing, replacement, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean renameFile(File source, File target, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement)
            throws Exception {
        return renameFile(source, target, fileSpec, flags, fileSpecFlags, replacing, replacement, "0", "0", "-1", "-1", 0, 0);
    }

    public static boolean renameFile(File source, File target, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement,
            String minFileAge, String maxFileAge, String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles) throws Exception {
        String mode = "rename";
        return transferFile(source, target, fileSpec, flags, fileSpecFlags, replacing, replacement, minFileAge, maxFileAge, minFileSize, maxFileSize,
                skipFirstFiles, skipLastFiles, mode);
    }

    public static boolean renameFile(String source, String target) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return renameFile(sourceFile, targetFile);
    }

    public static boolean renameFile(String source, String target, int flags) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return renameFile(sourceFile, targetFile, flags);
    }

    public static boolean renameFile(String source, String target, String fileSpec) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return renameFile(sourceFile, targetFile, fileSpec);
    }

    public static boolean renameFile(String source, String target, String fileSpec, int flags) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return renameFile(sourceFile, targetFile, fileSpec, flags);
    }

    public static boolean renameFile(String source, String target, String fileSpec, int flags, int fileSpecFlags) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return renameFile(sourceFile, targetFile, fileSpec, flags, fileSpecFlags);
    }

    public static boolean renameFile(String source, String target, String fileSpec, String replacing, String replacement) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return renameFile(sourceFile, targetFile, fileSpec, replacing, replacement);
    }

    public static boolean renameFile(String source, String target, String fileSpec, int flags, String replacing, String replacement) throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return renameFile(sourceFile, targetFile, fileSpec, flags, replacing, replacement);
    }

    public static boolean renameFile(String source, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement) throws Exception {
        File sourceFile = new File(source);
        return renameFile(sourceFile, fileSpec, flags, fileSpecFlags, replacing, replacement);
    }

    public static boolean renameFile(String source, String target, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement)
            throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return renameFile(sourceFile, targetFile, fileSpec, flags, fileSpecFlags, replacing, replacement);
    }

    public static boolean renameFile(String source, String target, String fileSpec, int flags, int fileSpecFlags, String replacing,
            String replacement, String minFileAge, String maxFileAge, String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles)
                    throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        return renameFile(sourceFile, targetFile, fileSpec, flags, fileSpecFlags, replacing, replacement, minFileAge, maxFileAge, minFileSize,
                maxFileSize, skipFirstFiles, skipLastFiles);
    }

    public static int renameFileCnt(String source, String target, String fileSpec, int flags, int fileSpecFlags, String replacing,
            String replacement, String minFileAge, String maxFileAge, String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles)
                    throws Exception {
        File sourceFile = new File(source);
        File targetFile = target == null ? null : new File(target);
        String mode = "rename";
        return transferFileCnt(sourceFile, targetFile, fileSpec, flags, fileSpecFlags, replacing, replacement, minFileAge, maxFileAge, minFileSize,
                maxFileSize, skipFirstFiles, skipLastFiles, mode);
    }

    private static boolean transferFile(File source, File target, String fileSpec, int flags, int fileSpecFlags, String replacing,
            String replacement, String minFileAge, String maxFileAge, String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles,
            String mode) throws Exception {
        int nrOfTransferedFiles =
                transferFileCnt(source, target, fileSpec, flags, fileSpecFlags, replacing, replacement, minFileAge, maxFileAge, minFileSize,
                        maxFileSize, skipFirstFiles, skipLastFiles, mode);
        return nrOfTransferedFiles > 0;
    }

    private static int transferFileCnt(File source, File target, String fileSpec, int flags, int fileSpecFlags, String replacing, String replacement,
            String minFileAge, String maxFileAge, String minFileSize, String maxFileSize, int skipFirstFiles, int skipLastFiles, String mode) throws Exception {
        int nrOfTransferedFiles = 0;
        boolean createDir;
        boolean gracious;
        boolean overwrite;
        boolean replace = false;
        boolean copying = false;
        boolean renaming = false;
        String targetFilename;
        long minAge = 0;
        long maxAge = 0;
        long minSize = -1;
        long maxSize = -1;
        try {
            if ("copy".equals(mode)) {
                copying = true;
            } else if ("rename".equals(mode)) {
                renaming = true;
            } else {
                throw new Exception("unsupported mode: " + mode);
            }
            createDir = has(flags, SOSFileOperations.CREATE_DIR);
            gracious = has(flags, SOSFileOperations.GRACIOUS);
            overwrite = !has(flags, SOSFileOperations.NOT_OVERWRITE);
            if (copying) {
                logDebug1("arguments for copyFile:");
            } else if (renaming) {
                logDebug1("arguments for renameFile:");
            }
            logDebug1("argument source=" + source.toString());
            if (target != null) {
                logDebug1("argument target=" + target.toString());
            }
            logDebug1("argument fileSpec=" + fileSpec);
            String msg = "";
            if (has(flags, SOSFileOperations.CREATE_DIR)) {
                msg += "CREATE_DIR ";
            }
            if (has(flags, SOSFileOperations.GRACIOUS)) {
                msg += "GRACIOUS ";
            }
            if (has(flags, SOSFileOperations.NOT_OVERWRITE)) {
                msg += "NOT_OVERWRITE ";
            }
            if (has(flags, SOSFileOperations.RECURSIVE)) {
                msg += "RECURSIVE ";
            }
            if ("".equals(msg)) {
                msg = "0";
            }
            logDebug1("argument flags=" + msg);
            msg = "";
            if (has(fileSpecFlags, Pattern.CANON_EQ)) {
                msg += "CANON_EQ ";
            }
            if (has(fileSpecFlags, Pattern.CASE_INSENSITIVE)) {
                msg += "CASE_INSENSITIVE ";
            }
            if (has(fileSpecFlags, Pattern.COMMENTS)) {
                msg += "COMMENTS ";
            }
            if (has(fileSpecFlags, Pattern.DOTALL)) {
                msg += "DOTALL ";
            }
            if (has(fileSpecFlags, Pattern.MULTILINE)) {
                msg += "MULTILINE ";
            }
            if (has(fileSpecFlags, Pattern.UNICODE_CASE)) {
                msg += "UNICODE_CASE ";
            }
            if (has(fileSpecFlags, Pattern.UNIX_LINES)) {
                msg += "UNIX_LINES ";
            }
            if ("".equals(msg)) {
                msg = "0";
            }
            logDebug1("argument fileSpecFlags=" + msg);
            logDebug1("argument replacing=" + replacing);
            logDebug1("argument replacement=" + replacement);
            logDebug1("argument minFileAge=" + minFileAge);
            logDebug1("argument maxFileAge=" + maxFileAge);
            minAge = calculateFileAge(minFileAge);
            maxAge = calculateFileAge(maxFileAge);
            logDebug1("argument minFileSize=" + minFileSize);
            logDebug1("argument maxFileSize=" + maxFileSize);
            minSize = calculateFileSize(minFileSize);
            maxSize = calculateFileSize(maxFileSize);
            logDebug1("argument skipFirstFiles=" + skipFirstFiles);
            logDebug1("argument skipLastFiles=" + skipLastFiles);
            if (skipFirstFiles < 0) {
                throw new Exception("[" + skipFirstFiles + "] is no valid value for skipFirstFiles");
            }
            if (skipLastFiles < 0) {
                throw new Exception("[" + skipLastFiles + "] is no valid value for skipLastFiles");
            }
            if (skipFirstFiles > 0 && skipLastFiles > 0) {
                throw new Exception("skip only either first files or last files");
            }
            if ((skipFirstFiles > 0 || skipLastFiles > 0) && minAge == 0 && maxAge == 0 && minSize == -1 && maxSize == -1) {
                throw new Exception("missed constraint for file skipping (minFileAge, maxFileAge, minFileSize, maxFileSize)");
            }
            if (replacing != null || replacement != null) {
                if (replacing == null) {
                    throw new Exception("replacing cannot be null if replacement is set");
                }
                if (replacement == null) {
                    throw new Exception("replacement cannot be null if replacing is set");
                }
                if (!"".equals(replacing)) {
                    try {
                        Pattern.compile(replacing);
                    } catch (PatternSyntaxException pse) {
                        throw new Exception("invalid pattern '" + replacing + "'");
                    }
                    replace = true;
                }
            }
            if (!source.exists()) {
                if (gracious) {
                    log(nrOfTransferedFiles + " file(s) renamed");
                    return nrOfTransferedFiles;
                } else {
                    throw new Exception("file or directory does not exist: " + source.getCanonicalPath());
                }
            }
            if (!source.canRead()) {
                throw new Exception("file or directory is not readable: " + source.getCanonicalPath());
            }
            if (target != null) {
                targetFilename = substituteAllDate(target.getPath());
                targetFilename = substituteAllDirectory(targetFilename, source.getPath());
                Matcher m = Pattern.compile("\\[[^]]*\\]").matcher(targetFilename);
                if (m.find()) {
                    throw new Exception("unsupported file mask found: " + m.group());
                }
                target = new File(targetFilename);
            }
            if (createDir && target != null && !target.exists()) {
                if (target.mkdirs()) {
                    log("create target directory " + target.getCanonicalPath());
                } else {
                    throw new Exception("cannot create directory " + target.getCanonicalPath());
                }
            }
            Vector<File> list = null;
            if (source.isDirectory()) {
                if (target != null) {
                    if (!target.exists()) {
                        throw new Exception("directory does not exist: " + target.getCanonicalPath());
                    }
                    if (!target.isDirectory()) {
                        throw new Exception("target is no directory: " + target.getCanonicalPath());
                    }
                }
                list = getFilelist(source.getPath(), fileSpec, fileSpecFlags, has(flags, SOSFileOperations.RECURSIVE), minAge, maxAge, minSize,
                            maxSize, skipFirstFiles, skipLastFiles);
            } else {
                list = new Vector<File>();
                list.add(source);
                list = filelistFilterAge(list, minAge, maxAge);
                list = filelistFilterSize(list, minSize, maxSize);
                if (skipFirstFiles > 0 || skipLastFiles > 0) {
                    list.clear();
                }
            }
            File sourceFile, targetFile;
            File dir;
            for (int i = 0; i < list.size(); i++) {
                sourceFile = (File) list.get(i);
                if (target != null) {
                    if (target.isDirectory()) {
                        String root = (source.isDirectory()) ? source.getPath() : source.getParent();
                        targetFilename = target.getPath() + sourceFile.getPath().substring(root.length());
                    } else {
                        targetFilename = target.getPath();
                    }
                } else {
                    if (source.isDirectory()) {
                        String root = (source.isDirectory()) ? source.getPath() : source.getParent();
                        targetFilename = source.getPath() + sourceFile.getPath().substring(root.length());
                    } else {
                        targetFilename = source.getParent() + "/" + sourceFile.getName();
                    }
                }
                targetFile = new File(targetFilename);
                try {
                    if (replace) {
                        targetFilename = targetFile.getName();
                        targetFilename = replaceGroups(targetFilename, replacing, replacement);
                        targetFilename = substituteAllDate(targetFilename);
                        targetFilename = substituteAllFilename(targetFilename, targetFile.getName());
                        Matcher matcher = Pattern.compile("\\[[^]]*\\]").matcher(targetFilename);
                        if (matcher.find()) {
                            throw new Exception("unsupported file mask found: " + matcher.group());
                        }
                        targetFile = new File(targetFile.getParent() + "/" + targetFilename);
                    }
                } catch (Exception re) {
                    throw new Exception("replacement error in file " + targetFilename + ": " + re.getMessage());
                }
                dir = new File(targetFile.getParent());
                if (!dir.exists()) {
                    if (dir.mkdirs()) {
                        log("create directory " + dir.getCanonicalPath());
                    } else {
                        throw new Exception("cannot create directory " + dir.getCanonicalPath());
                    }
                }
                if (copying && !copyOneFile(sourceFile, targetFile, overwrite, gracious)) {
                    continue;
                } else if (renaming && !renameOneFile(sourceFile, targetFile, overwrite, gracious)) {
                    continue;
                }
                nrOfTransferedFiles++;
            }
            if (copying) {
                log(nrOfTransferedFiles + " file(s) copied");
            } else if (renaming) {
                log(nrOfTransferedFiles + " file(s) renamed");
            }
            lstResultList = list;
            return nrOfTransferedFiles;
        } catch (Exception e) {
            LOGGER.error(e);
            if (copying) {
                throw new Exception("error occurred copying file(s): " + e.getMessage());
            } else if (renaming) {
                throw new Exception("error occurred renaming file(s): " + e.getMessage());
            } else {
                return 0;
            }
        }
    }

    private static Vector<File> filelistFilterAge(Vector<File> filelist, long minAge, long maxAge) throws Exception {
        long currentTime = System.currentTimeMillis();
        if (minAge != 0) {
            File file;
            Vector<File> newlist = new Vector<File>();
            for (int i = 0; i < filelist.size(); i++) {
                file = (File) filelist.get(i);
                long interval = currentTime - file.lastModified();
                if (interval < 0) {
                    throw new Exception("Cannot filter by file age. File [" + file.getCanonicalPath() + "] was modified in the future.");
                }
                if (interval >= minAge) {
                    newlist.add(file);
                }
            }
            filelist = newlist;
        }
        if (maxAge != 0) {
            File file;
            Vector<File> newlist = new Vector<File>();
            for (int i = 0; i < filelist.size(); i++) {
                file = (File) filelist.get(i);
                long interval = currentTime - file.lastModified();
                if (interval < 0) {
                    throw new Exception("Cannot filter by file age. File [" + file.getCanonicalPath() + "] was modified in the future.");
                }
                if (interval <= maxAge) {
                    newlist.add(file);
                }
            }
            filelist = newlist;
        }
        return filelist;
    }

    private static Vector<File> filelistFilterSize(Vector<File> filelist, long minSize, long maxSize) throws Exception {
        if (minSize > -1) {
            File file;
            Vector<File> newlist = new Vector<File>();
            for (int i = 0; i < filelist.size(); i++) {
                file = (File) filelist.get(i);
                if (file.length() >= minSize) {
                    newlist.add(file);
                }
            }
            filelist = newlist;
        }
        if (maxSize > -1) {
            File file;
            Vector<File> newlist = new Vector<File>();
            for (int i = 0; i < filelist.size(); i++) {
                file = (File) filelist.get(i);
                if (file.length() <= maxSize) {
                    newlist.add(file);
                }
            }
            filelist = newlist;
        }
        return filelist;
    }

    private static Vector<File> filelistSkipFiles(Vector<File> filelist, int skipFirstFiles, int skipLastFiles, String sorting) throws Exception {
        File[] oArr = filelist.toArray(new File[filelist.size()]);
        class SizeComparator implements Comparator<File> {

            public int compare(File o1, File o2) {
                int ret = 0;
                long val1 = o1.length();
                long val2 = o2.length();
                if (val1 < val2) {
                    ret = -1;
                }
                if (val1 == val2) {
                    ret = 0;
                }
                if (val1 > val2) {
                    ret = 1;
                }
                return ret;
            }
        }
        class AgeComparator implements Comparator<File> {

            public int compare(File o1, File o2) {
                int ret = 0;
                long val1 = o1.lastModified();
                long val2 = o2.lastModified();
                if (val1 > val2) {
                    ret = -1;
                }
                if (val1 == val2) {
                    ret = 0;
                }
                if (val1 < val2) {
                    ret = 1;
                }
                return ret;
            }
        }

        if ("sort_size".equals(sorting)) {
            Arrays.sort(oArr, new SizeComparator());
        } else if ("sort_age".equals(sorting)) {
            Arrays.sort(oArr, new AgeComparator());
        }
        filelist = new Vector<File>();
        for (int i = 0 + skipFirstFiles; i < oArr.length - skipLastFiles; i++) {
            filelist.add(oArr[i]);
        }
        return filelist;
    }

    private static Vector<File> getFilelist(String folder, String regexp, int flag, boolean withSubFolder, long minFileAge, long maxFileAge,
            long minFileSize, long maxFileSize, int skipFirstFiles, int skipLastFiles) throws Exception {
        Vector<File> filelist = new Vector<File>();
        Vector<File> temp = new Vector<File>();
        File file = null;
        File[] subDir = null;
        file = new File(folder);
        subDir = file.listFiles();
        temp = SOSFile.getFilelist(folder, regexp, flag);
        temp = filelistFilterAge(temp, minFileAge, maxFileAge);
        temp = filelistFilterSize(temp, minFileSize, maxFileSize);
        if ((minFileSize != -1 || minFileSize != -1) && minFileAge == 0 && maxFileAge == 0) {
            temp = filelistSkipFiles(temp, skipFirstFiles, skipLastFiles, "sort_size");
        } else if (minFileAge != 0 || maxFileAge != 0) {
            temp = filelistSkipFiles(temp, skipFirstFiles, skipLastFiles, "sort_age");
        }
        filelist.addAll(temp);
        if (withSubFolder) {
            for (int i = 0; i < subDir.length; i++) {
                if (subDir[i].isDirectory()) {
                    filelist.addAll(getFilelist(subDir[i].getPath(), regexp, flag, true, minFileAge, maxFileAge, minFileSize, maxFileSize,
                            skipFirstFiles, skipLastFiles));
                }
            }
        }
        return filelist;
    }

    private static long calculateFileAge(String fileage) throws Exception {
        long age = 0;
        if (fileage == null || fileage.trim().isEmpty()) {
            return 0;
        }
        if (fileage.indexOf(":") == -1) {
            if (!fileage.matches("[\\d]+")) {
                throw new Exception("[" + fileage + "] is no valid file age");
            } else {
                return Long.parseLong(fileage) * 1000;
            }
        }
        if (!fileage.matches("^[\\d].*[\\d]$")) {
            throw new Exception("[" + fileage + "] is no valid file age");
        }
        String[] timeArray = fileage.split(":");
        if (timeArray.length > 3) {
            throw new Exception("[" + fileage + "] is no valid file age");
        }
        for (int i = 0; i < timeArray.length; i++) {
            if (!timeArray[i].matches("[\\d]+")) {
                throw new Exception("[" + fileage + "] is no valid file age");
            }
        }
        long hours = Long.parseLong(timeArray[0]);
        long minutes = Long.parseLong(timeArray[1]);
        long seconds = 0;
        if (timeArray.length > 2) {
            seconds = Long.parseLong(timeArray[2]);
        }
        age = hours * 3600000 + minutes * 60000 + seconds * 1000;
        return age;
    }

    private static long calculateFileSize(String filesize) throws Exception {
        long size;
        if (filesize == null || filesize.trim().isEmpty()) {
            return -1;
        }
        if (filesize.matches("-1")) {
            return -1;
        }
        if (filesize.matches("[\\d]+")) {
            size = Long.parseLong(filesize);
        } else {
            if (filesize.matches("^[\\d]+[kK][bB]$")) {
                size = Long.parseLong(filesize.substring(0, filesize.length() - 2)) * 1024;
            } else if (filesize.matches("^[\\d]+[mM][bB]$")) {
                size = Long.parseLong(filesize.substring(0, filesize.length() - 2)) * 1024 * 1024;
            } else if (filesize.matches("^[\\d]+[gG][bB]$")) {
                size = Long.parseLong(filesize.substring(0, filesize.length() - 2)) * 1024 * 1024 * 1024;
            } else {
                throw new Exception("[" + filesize + "] is no valid file size");
            }
        }
        return size;
    }

    private static String substituteFirstFilename(String targetFilename, String original) throws Exception {
        Matcher matcher = Pattern.compile("\\[filename:([^\\]]*)\\]").matcher(targetFilename);
        if (matcher.find()) {
            if ("".equals(matcher.group(1))) {
                targetFilename = targetFilename.replaceFirst("\\[filename:\\]", original);
            } else if ("lowercase".equals(matcher.group(1))) {
                targetFilename = targetFilename.replaceFirst("\\[filename:lowercase\\]", original.toLowerCase());
            } else if ("uppercase".equals(matcher.group(1))) {
                targetFilename = targetFilename.replaceFirst("\\[filename:uppercase\\]", original.toUpperCase());
            }
        }
        return targetFilename;
    }

    private static String substituteAllFilename(String targetFilename, String original) throws Exception {
        String temp = substituteFirstFilename(targetFilename, original);
        while (!targetFilename.equals(temp)) {
            targetFilename = temp;
            temp = substituteFirstFilename(targetFilename, original);
        }
        return temp;
    }

    private static String substituteFirstDate(String targetFilename) throws Exception {
        final String conVarName = "[date:";
        try {
            if (targetFilename.matches("(.*)(\\" + conVarName + ")([^\\]]*)(\\])(.*)")) {
                int posBegin = targetFilename.indexOf(conVarName);
                if (posBegin > -1) {
                    int posEnd = targetFilename.indexOf("]", posBegin + 6);
                    if (posEnd > -1) {
                        String strDateMask = targetFilename.substring(posBegin + 6, posEnd);
                        if (strDateMask.length() <= 0) {
                            strDateMask = SOSDate.dateTimeFormat;
                        }
                        String strDateTime = SOSDate.getCurrentTimeAsString(strDateMask);
                        String strT = ((posBegin > 0) ? targetFilename.substring(0, posBegin) : "") + strDateTime;
                        if (targetFilename.length() > posEnd) {
                            strT += targetFilename.substring(posEnd + 1);
                        }
                        targetFilename = strT;
                    }
                }
            }
            return targetFilename;
        } catch (Exception e) {
            throw new RuntimeException("error substituting [date:]: " + e.getMessage());
        }
    }

    private static String substituteAllDate(String targetFilename) throws Exception {
        String temp = substituteFirstDate(targetFilename);
        while (!targetFilename.equals(temp)) {
            targetFilename = temp;
            temp = substituteFirstDate(targetFilename);
        }
        return temp;
    }

    private static String substituteFirstDirectory(String target, String source) throws Exception {
        try {
            File sourceFile = new File(source);
            if (!sourceFile.isDirectory()) {
                source = sourceFile.getParent();
            }
            source = source.replaceAll("\\\\", "/");
            target = target.replaceAll("\\\\", "/");
            Pattern p = Pattern.compile("\\[directory:(-[\\d]+|[\\d]*)\\]");
            Matcher m = p.matcher(target);
            if (m.find()) {
                String substitute = "";
                if (m.group(1).isEmpty() || "0".equals(m.group(1)) || "-0".equals(m.group(1))) {
                    substitute = source;
                } else {
                    int depth = Integer.valueOf(m.group(1)).intValue();
                    StringTokenizer st = new StringTokenizer(source, "/");
                    int absDepth = depth < 0 ? -depth : depth;
                    if (absDepth >= st.countTokens()) {
                        substitute = source;
                    } else {
                        String[] dirs = new String[st.countTokens()];
                        int n = 0;
                        while (st.hasMoreTokens()) {
                            dirs[n++] = st.nextToken();
                        }
                        if (depth > 0) {
                            while (depth > 0) {
                                substitute = dirs[--depth] + "/" + substitute;
                            }
                        } else if (depth < 0) {
                            while (depth < 0) {
                                substitute = substitute + dirs[dirs.length + depth++] + "/";
                            }
                        }
                    }
                }
                if (substitute.charAt(substitute.length() - 1) == '/') {
                    substitute = substitute.substring(0, substitute.length() - 1);
                }
                target = target.replaceFirst("\\[directory:[^\\]]*\\]", substitute);
            }
            return target;
        } catch (Exception e) {
            throw new Exception("error substituting [directory]: " + e.getMessage());
        }
    }

    private static String substituteAllDirectory(String target, String source) throws Exception {
        String temp = substituteFirstDirectory(target, source);
        while (!target.equals(temp)) {
            target = temp;
            temp = substituteFirstDirectory(target, source);
        }
        return temp;
    }

    public static String replaceGroups(String input, String replacing, String replacements) throws Exception {
        if (replacements == null) {
            throw new RuntimeException("replacements missing: 0 replacements defined");
        }
        return replaceGroups(input, replacing, replacements.split(";"));
    }

    public static String replaceGroups(String pstrSourceString, String replacing, String[] replacements) throws Exception {
        String result = "";
        if (replacements == null) {
            throw new RuntimeException("replacements missing: 0 replacements defined");
        }
        Pattern p = Pattern.compile(replacing);
        Matcher m = p.matcher(pstrSourceString);
        if (!m.find()) {
            return pstrSourceString;
        }
        int intGroupCount = m.groupCount();
        if (replacements.length < intGroupCount) {
            throw new RuntimeException("replacements missing: " + replacements.length + " replacement(s) defined but " + intGroupCount + " groups found");
        }
        if (intGroupCount == 0) {
            result = pstrSourceString.substring(0, m.start()) + replacements[0] + pstrSourceString.substring(m.end());
        } else {
            int index = 0;
            for (int i = 1; i <= intGroupCount; i++) {
                int intStart = m.start(i);
                if (intStart >= 0) {
                    String strRepl = replacements[i - 1].trim();
                    if (strRepl.length() > 0) {
                        if (strRepl.contains("\\")) {
                            strRepl = strRepl.replaceAll("\\\\-", "");
                            for (int j = 1; j <= intGroupCount; j++) {
                                strRepl = strRepl.replaceAll("\\\\" + j, m.group(j));
                            }
                        }
                        result += pstrSourceString.substring(index, intStart) + strRepl;
                    }
                }
                index = m.end(i);
            }
            result += pstrSourceString.substring(index);
        }
        return result;
    }

    private static boolean copyOneFile(File source, File target, boolean overwrite, boolean gracious) throws Exception {
        boolean rc = false;
        if (source.equals(target)) {
            throw new Exception("cannot copy file to itself: " + source.getCanonicalPath());
        }
        if (overwrite || !target.exists()) {
            long modificationDate = source.lastModified();
            rc = SOSFile.copyFile(source, target);
            target.setLastModified(modificationDate);
            log("copy " + source.getPath() + " to " + target.getPath());
            return rc;
        } else if (!gracious) {
            throw new Exception("file already exists: " + target.getCanonicalPath());
        } else {
            log("file already exists: " + target.getCanonicalPath());
            return rc;
        }
    }

    private static boolean renameOneFile(File source, File target, boolean overwrite, boolean gracious) throws Exception {
        if (source.equals(target)) {
            throw new Exception("cannot rename file to itself: " + source.getCanonicalPath());
        }
        if (!overwrite && target.exists()) {
            if (!gracious) {
                throw new Exception("file already exists: " + target.getCanonicalPath());
            } else {
                log("file already exists: " + target.getCanonicalPath());
                return false;
            }
        } else {
            if (target.exists() && !target.delete()) {
                throw new Exception("cannot overwrite " + target.getCanonicalPath());
            }
            if (!source.renameTo(target)) {
                boolean rc = SOSFile.copyFile(source, target);
                if (rc) {
                    rc = source.delete();
                    if (!rc) {
                        rc = target.delete();
                        throw new Exception("cannot rename file from " + source.getCanonicalPath() + " to " + target.getCanonicalPath());
                    }
                } else {
                    throw new Exception("cannot rename file from " + source.getCanonicalPath() + " to " + target.getCanonicalPath());
                }
            } else {
                log("rename " + source.getPath() + " to " + target.getPath());
            }
        }
        return true;
    }

    private static void log(String msg) {
        LOGGER.info(msg);
    }

    private static void logDebug1(String msg) {
        LOGGER.debug(msg);
    }

    private static void logDebug3(String msg) {
        LOGGER.debug(msg);
    }

    private static void logDebug9(String msg) {
        LOGGER.debug(msg);
    }

    private static boolean has(int flags, int f) {
        return (flags & f) > 0;
    }

    public static void callMethod(String methodname, Class<?>[] argtypes, Object[] args) throws Exception {
        Method method = null;
        try {
            if (argtypes.length != args.length) {
                throw new Exception("different array lengths: " + argtypes.length + " argument types but " + args.length + " arguments");
            }
            try {
                method = Class.forName("sos.util.SOSFileOperations").getMethod(methodname, argtypes);
            } catch (NoSuchMethodException nsme) {
                throw new Exception("method does not exist: " + nsme.getMessage());
            }
        } catch (Exception e) {
            throw new Exception("callMethod: " + e.getMessage());
        }
        try {
            method.invoke(null, args);
        } catch (Exception x) {
            if (x.getMessage() == null) {
                throw new Exception(x.getCause().getMessage());
            } else {
                throw new Exception("callMethod: " + x.getMessage());
            }
        }
    }

    public static String getReplacementFilename(String input, String replacing, String replacements) throws Exception {
        String targetFilename = input;
        try {
            targetFilename = replaceGroups(targetFilename, replacing, replacements.split(";"));
            targetFilename = substituteAllDate(targetFilename);
            targetFilename = substituteAllFilename(targetFilename, input);
            Matcher m = Pattern.compile("\\[[^\\]]*\\]").matcher(targetFilename);
            if (m.find()) {
                throw new Exception("unsupported file mask found:" + m.group());
            }
            return targetFilename;
        } catch (Exception e) {
            throw new Exception("getReplacementFilename: " + e.getMessage());
        }
    }

    private static boolean wipe(File file) {
        try {
            RandomAccessFile rwFile = new RandomAccessFile(file, "rw");
            byte[] bytes = new byte[(int) (rwFile.length())];
            int i = 0;
            while ((bytes[i++] = (byte) rwFile.read()) != -1) {

            }
            rwFile.seek(0);
            for (i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) ((Math.random() * 10) % 9);
            }
            rwFile.write(bytes);
            rwFile.close();
            LOGGER.debug("Deleting file");
            boolean rc = file.delete();
            LOGGER.debug("rc: " + rc);
            return rc;
        } catch (Exception e) {
            LOGGER.warn("Failed to wipe file: " + e);
            return false;
        }
    }

}