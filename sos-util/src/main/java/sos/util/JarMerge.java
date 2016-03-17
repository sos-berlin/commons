package sos.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

public class JarMerge {

    private boolean check = false;
    private boolean unzip = false;
    private boolean zip = false;
    private boolean add = false;
    private boolean silent = false;
    private boolean oldOverwrites = false;
    private boolean standardManifestToCreate = false;
    private Pattern fileFilterPattern = Pattern.compile(".*");
    private int compression = -1;
    private boolean detach = false;
    private String source = null;
    private String targetPath = null;
    private String manifestFile = null;
    private String identity = null;

    private JarMerge(final String[] args) {
        identity = this.getClass().getName();
        try {
            this.parseArguments(args);
        } catch (Exception e) {
            this.loglnError("\n abort: " + e.getMessage());
            this.printUsage();
            System.exit(-1);
        }
    }

    private void log(final String msg) {
        System.out.print(msg);
    }

    private void logln(final String msg) {
        System.out.println(msg);
    }

    private void loglnError(final String msg) {
        System.err.println(msg);
    }

    private void printUsage() {
        String usage = "\n usage:";
        usage += " java " + identity + " [-s] check PATH";
        usage += "\n";
        usage += "\n        -all jar/zip archives in directory PATH are tested";
        usage += "\n         for duplicate classnames respecting the package structure";
        usage += "\n         -s: silent mode, if no duplicates are found nothing will be written to console";
        usage += "\n";
        usage += "\n        java " + identity + " [-o] [-f=REGEXP] [-d] unzip SOURCE [TARGETPATH]";
        usage += "\n";
        usage += "\n        -extract all files contained in jar/zip archive(s) to subdirectories";
        usage += "\n         relative to package structure.";
        usage += "\n         SOURCE can be a directory or a single archive.";
        usage += "\n         If the directory TARGETPATH is set the structure of subdirectories is created there.";
        usage += "\n         By default the structure of subdirectories is created in current working directory.";
        usage += "\n         If the flag -o is set older files overwrite newer ones.";
        usage += "\n         By default newer files overwrite older ones.";
        usage += "\n         -f: file filter for extracting, REGEXP is a regular expression";
        usage += "\n         -d: (detach) all files are extracted directly to target directory";
        usage += "\n             without attending to package structure";
        usage += "\n";
        usage += "\n        java " + identity + " [-m] [-f=REGEXP] [-c=LEVEL] zip SOURCEPATH JARFILE [MANIFESTFILE]";
        usage += "\n";
        usage += "\n        -all files of directory SOURCEPATH and subdirectories are compressed";
        usage += "\n         to jar archive file JARFILE.";
        usage += "\n         If a manifest file is set for MANIFESTFILE this file will be added to jar archive.";
        usage += "\n         If the flag -m is set a standard manifest will be created.";
        usage += "\n         This flag will be ignored if MANIFESTFILE is set.";
        usage += "\n         By default it will be searched in SOURCEPATH for '/META-INF/MANIFEST.MF'.";
        usage += "\n         If this manifest file is found it will be added to jar archive.";
        usage += "\n         If this manifest file is not found a standard manifest will be created.";
        usage += "\n         -f: file filter for compression, REGEXP is a regular expression";
        usage += "\n         -c: compression level, LEVEL can be 0 to 9";
        usage += "\n";
        usage += "\n        java " + identity + " [-m] [-f=REGEXP] [-c=LEVEL] add SOURCEPATH JARFILE [MANIFESTFILE]";
        usage += "\n";
        usage += "\n        -all files of directory SOURCEPATH and subdirectories are added";
        usage += "\n         to existing jar archive file JARFILE.";
        usage += "\n         If JARFILE does not yet exist a new file will be created.";
        usage += "\n         If a manifest file is set for MANIFESTFILE this file will be added to jar archive.";
        usage += "\n         If the flag -m is set a standard manifest will be created.";
        usage += "\n         This flag will be ignored if MANIFESTFILE is set.";
        usage += "\n         By default it will be searched in SOURCEPATH for '/META-INF/MANIFEST.MF'.";
        usage += "\n         If this manifest file is found it will be added to jar archive.";
        usage += "\n         If this manifest file is not found a standard manifest will be created.";
        usage += "\n         -f: file filter for compression, REGEXP is a regular expression";
        usage += "\n         -c: compression level, LEVEL can be 0 to 9. It affects all files in the archive.";
        usage += "\n";
        usage += "\n        java " + identity + " -h";
        usage += "\n";
        usage += "\n        -show this help";
        this.logln(usage);
    }

    private void parseArguments(final String[] args) throws Exception {
        if (args == null) {
            throw new Exception("args is null");
        }
        if (args.length == 0) {
            throw new Exception("arguments missing");
        }
        if ("-h".equals(args[0].toLowerCase())) {
            if (args.length > 1) {
                String arg = args[1];
                if ("check".equals(arg) || "unzip".equals(arg) || "zip".equals(arg) || "add".equals(arg)) {
                    throw new Exception("unexpected command '" + arg + "'");
                }
                if (args[1].length() < 2) {
                    throw new Exception("unknown parameter '" + args[1] + "'");
                }
                arg = args[1].substring(0, 2).toLowerCase();
                if ("-s".equals(arg) || "-o".equals(arg) || "-f".equals(arg) || "-m".equals(arg) || "-c".equals(arg) || "-d".equals(arg) || "-h".equals(arg)) {
                    throw new Exception("unexpected parameter '" + args[1] + "'");
                } else {
                    throw new Exception("unknown parameter '" + args[1] + "'");
                }
            }
            this.printUsage();
            System.exit(0);
        }
        ArrayList<String> arguments = new ArrayList<>();
        ArrayList<String> switches = new ArrayList<>();
        for (String arg : args) {
            if (arg.charAt(0) == '-') {
                if (arg.length() == 1) {
                    if (args.length == 1) {
                        throw new Exception("unknown command '-'");
                    } else {
                        throw new Exception("unknown parameter '-'");
                    }
                } else {
                    switches.add(arg);
                }
            } else {
                arguments.add(arg);
            }
        }
        if (arguments.isEmpty()) {
            throw new Exception("unknown command '" + args[0] + "'");
        }
        String command = arguments.get(0);
        int minNrOfArgs = 0;
        int maxNrOfArgs = 0;
        if ("check".equals(command)) {
            check = true;
            minNrOfArgs = 1;
            maxNrOfArgs = 1;
        } else if ("unzip".equals(command)) {
            unzip = true;
            minNrOfArgs = 1;
            maxNrOfArgs = 2;
        } else if ("zip".equals(command)) {
            zip = true;
            minNrOfArgs = 2;
            maxNrOfArgs = 3;
        } else if ("add".equals(command)) {
            add = true;
            minNrOfArgs = 2;
            maxNrOfArgs = 3;
        } else {
            throw new Exception("unknown command '" + command + "'");
        }
        arguments.remove(0);
        if (arguments.size() < minNrOfArgs) {
            throw new Exception("arguments missing");
        }
        if (arguments.size() > maxNrOfArgs) {
            throw new Exception("too many arguments");
        }
        String sw = null;
        if (check) {
            source = arguments.get(0);
            for (int i = 0; i < switches.size(); i++) {
                sw = switches.get(i).toLowerCase();
                if ("-s".equals(sw)) {
                    silent = true;
                } else if ("-o".equals(sw) || "-m".equals(sw) || "-h".equals(sw) || "-d".equals(sw) || sw.matches("-f=.+") || sw.matches("-c=.+")) {
                    throw new Exception("unexpected parameter '" + switches.get(i) + "'");
                } else {
                    throw new Exception("unknown parameter '" + switches.get(i) + "'");
                }
            }
        } else if (unzip) {
            source = arguments.get(0);
            if (arguments.size() == 2) {
                targetPath = arguments.get(1);
            }
            for (int i = 0; i < switches.size(); i++) {
                sw = switches.get(i).toLowerCase();
                if ("-d".equals(sw)) {
                    detach = true;
                } else if ("-o".equals(sw)) {
                    oldOverwrites = true;
                } else if (sw.matches("-f=.+")) {
                    try {
                        fileFilterPattern = Pattern.compile(switches.get(i).substring(3));
                    } catch (Exception e) {
                        throw new Exception("cannot compile pattern '" + switches.get(i).substring(3) + "'");
                    }
                } else if ("-s".equals(sw) || "-m".equals(sw) || "-h".equals(sw) || sw.matches("-c=.+")) {
                    throw new Exception("unexpected parameter '" + switches.get(i) + "'");
                } else {
                    throw new Exception("unknown parameter '" + switches.get(i) + "'");
                }
            }
        } else if (zip || add) {
            source = arguments.get(0);
            targetPath = arguments.get(1);
            if (arguments.size() == 3) {
                manifestFile = arguments.get(2);
            }
            for (int i = 0; i < switches.size(); i++) {
                sw = switches.get(i).toLowerCase();
                if ("-m".equals(sw)) {
                    standardManifestToCreate = true;
                } else if (sw.matches("-f=.+")) {
                    try {
                        fileFilterPattern = Pattern.compile(switches.get(i).substring(3));
                    } catch (Exception e) {
                        throw new Exception("cannot compile pattern '" + switches.get(i).substring(3) + "'");
                    }
                } else if (sw.matches("-c=.+")) {
                    try {
                        compression = Integer.parseInt(switches.get(i).substring(3));
                    } catch (Exception e) {
                        throw new Exception("cannot convert '" + switches.get(i).substring(3) + "' to int");
                    }
                    if (compression < 0 || compression > 9) {
                        throw new Exception("level of compression must be between 0 and 9");
                    }
                } else if ("-s".equals(sw) || "-o".equals(sw) || "-d".equals(sw) || "-h".equals(sw)) {
                    throw new Exception("unexpected parameter '" + switches.get(i) + "'");
                } else {
                    throw new Exception("unknown parameter '" + switches.get(i) + "'");
                }
            }
        }
    }

    private String[] archiveFilter(final String[] list) {
        String[] fList;
        String suffix;
        int deleted = 0;
        if (list == null) {
            return null;
        }
        for (int i = 0; i < list.length; i++) {
            if (list[i].length() < 4 || list[i] == null) {
                list[i] = null;
                deleted++;
            } else {
                suffix = list[i].substring(list[i].length() - 4);
                if (!(".jar".equals(suffix) || ".zip".equals(suffix))) {
                    list[i] = null;
                    deleted++;
                }
            }
        }
        fList = new String[list.length - deleted];
        for (int n = 0, i = 0; i < list.length; i++) {
            if (list[i] != null) {
                fList[n++] = list[i];
            }
        }
        return fList;
    }

    private File[] fileFilter(final File[] list, final Pattern pattern) throws Exception {
        String newjarPath = new File(targetPath).getCanonicalPath();
        File[] fList;
        int deleted = 0;
        if (list == null) {
            return null;
        }
        for (int i = 0; i < list.length; i++) {
            if (list[i] == null || list[i].getCanonicalPath().equals(newjarPath) || Pattern.matches(".*META-INF[/\\\\]MANIFEST[.]MF", list[i].getPath())
                    || !pattern.matcher(list[i].getName()).matches()) {
                list[i] = null;
                deleted++;
            }
        }
        fList = new File[list.length - deleted];
        for (int n = 0, i = 0; i < list.length; i++) {
            if (list[i] != null) {
                fList[n++] = list[i];
            }
        }
        return fList;
    }

    private void executeCheck() throws Exception {
        JarFile jar;
        Enumeration content;
        JarEntry entry;
        String classFile;
        long lDate;
        long size;
        StringTokenizer st;
        SimpleDateFormat sdf;
        String firstFile;
        String firstDate;
        String firstSize;
        int gap;
        int duplicates = 0;
        File file = new File(source);
        String[] list = this.archiveFilter(file.list());
        if (list.length == 0) {
            this.logln("\n no jar/zip archives found");
            return;
        }
        Hashtable inventory = new Hashtable();
        try {
            int i = 0;
            for (i = 0; i < list.length; i++) {
                jar = new JarFile(source + list[i]);
                content = jar.entries();
                while (content.hasMoreElements()) {
                    entry = (JarEntry) content.nextElement();
                    classFile = entry.getName();
                    lDate = entry.getTime();
                    size = entry.getSize();
                    if (classFile.length() > 6) {
                        if (".class".equals(classFile.substring(classFile.length() - 6))) {
                            if (!inventory.containsKey(classFile)) {
                                inventory.put(classFile, list[i] + "|" + String.valueOf(lDate) + "|" + String.valueOf(size));
                            } else {
                                st = new StringTokenizer((String) inventory.get(classFile), "|");
                                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                firstFile = st.nextToken();
                                firstDate = st.nextToken();
                                firstSize = st.nextToken();
                                if (Long.parseLong(firstDate) < 0) {
                                    firstDate = "unknown";
                                } else {
                                    firstDate = sdf.format(new Date(Long.parseLong(firstDate)));
                                }
                                if (Long.parseLong(firstSize) < 0) {
                                    firstSize = "unknown";
                                }
                                gap = Math.max(firstFile.length(), firstDate.length());
                                gap = Math.max(gap, firstSize.length());
                                while (firstFile.length() < gap) {
                                    firstFile += " ";
                                }
                                while (firstDate.length() < gap) {
                                    firstDate += " ";
                                }
                                while (firstSize.length() < gap) {
                                    firstSize += " ";
                                }
                                this.logln("\n duplicate class found: " + classFile);
                                this.logln(" " + firstFile + "   " + list[i]);
                                this.logln(" " + firstDate + "   " + (lDate < 0 ? "unknown" : sdf.format(new Date(lDate))));
                                this.logln(" " + firstSize + "   " + (size < 0 ? "unknown" : String.valueOf(size)));
                                duplicates++;
                            }
                        }
                    }
                }
            }
            if (duplicates == 0 && !silent) {
                this.logln("\n no duplicates found");
                this.logln(" " + i + " archive" + (i != 1 ? "s" : "") + " passed");
            } else {
                this.logln("\n " + duplicates + " duplicate" + (duplicates != 1 ? "s" : "") + " found");
                this.logln(" " + i + " archive" + (i != 1 ? "s" : "") + " passed");
            }
        } catch (Exception e) {
            this.loglnError("check error: " + e.getMessage());
        }
    }

    private String unpackJarEntry(final JarFile jarFile, final JarEntry jarEntry, String targetPath) throws JarException, IOException, Exception {
        try {
            if (jarFile == null) {
                throw new Exception("jarFile is null");
            }
            if (jarEntry == null) {
                throw new Exception("jarEntry is null");
            }
            if (targetPath == null) {
                targetPath = "";
            }
            String filename = jarEntry.getName();
            if (detach) {
                int index = filename.lastIndexOf('/') + 1;
                filename = filename.substring(index);
            }
            File file = new File(targetPath + filename);
            BufferedInputStream bis = new BufferedInputStream(jarFile.getInputStream(jarEntry));
            File dir = new File(file.getParent());
            dir.mkdirs();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            for (int c; (c = bis.read()) != -1;) {
                bos.write((byte) c);
            }
            bos.close();
            bis.close();
            file = new File(targetPath + filename);
            file.setLastModified(jarEntry.getTime());
            return targetPath + filename;
        } catch (JarException jax) {
            throw new JarException(jax.getMessage() + ": " + identity + ".unpackJarEntry: " + jarFile.getName() + " => " + targetPath + jarEntry.getName());
        } catch (IOException iox) {
            throw new IOException(iox.getMessage() + ": " + identity + ".unpackJarEntry: " + jarFile.getName() + " => " + targetPath + jarEntry.getName(), iox);
        } catch (Exception e) {
            throw new Exception(e.getMessage() + ": " + identity + ".unpackJarEntry: " + jarFile.getName() + " => " + targetPath + jarEntry.getName(), e);
        }
    }

    private void createManifestFile(final ArrayList<String> iSecName, final ArrayList<String> iSecOrigin, final ArrayList<String> iSecDate, String targetPath)
            throws IOException, Exception {
        Manifest manifest = null;
        File file = null;
        try {
            if (iSecName == null || iSecOrigin == null || iSecDate == null) {
                throw new Exception("iSec is null");
            }
            if (!(iSecName.size() == iSecOrigin.size() && iSecName.size() == iSecDate.size())) {
                throw new Exception("iSec ArrayList sizes are inconsistent " + iSecName.size() + "," + iSecOrigin.size() + "," + iSecDate.size());
            }
            if (iSecName.isEmpty()) {
                return;
            }
            if (targetPath == null) {
                targetPath = "";
            }
            file = new File(targetPath + "META-INF");
            if (!file.isDirectory()) {
                file.mkdir();
            }
            file = new File(targetPath + "META-INF/MANIFEST.MF");
            file.createNewFile();
            StringBuilder sb = new StringBuilder();
            sb.append("Manifest-Version: 1.0\n");
            sb.append("Created-By: " + System.getProperty("java.vm.version") + " (" + System.getProperty("java.vm.vendor") + ")\n");
            sb.append("\n");
            for (int i = 0; i < iSecName.size(); i++) {
                sb.append("Name: " + iSecName.get(i) + "\n");
                sb.append("Origin: " + iSecOrigin.get(i) + "\n");
                sb.append("Archive-Date: " + iSecDate.get(i) + "\n");
                sb.append("\n");
            }
            InputStream is = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
            manifest = new Manifest(is);
            manifest.write(new FileOutputStream(file));
            this.logln("\n manifest file " + file.getCanonicalPath() + " created");
        } catch (IOException iox) {
            throw new IOException(identity + "constructManifest: " + iox.getMessage(), iox);
        } catch (Exception e) {
            throw new Exception(identity + "constructManifest: " + e.getMessage(), e);
        }
    }

    private Manifest readManifestFile(final String path) throws IOException {
        Manifest manifest = null;
        if (path == null) {
            return null;
        }
        File file = new File(path);
        if (file.canRead()) {
            try {
                manifest = new Manifest(new FileInputStream(file));
            } catch (FileNotFoundException nfx) {
            } catch (IOException iox) {
                throw new IOException(identity + "readManifestFile: " + file.getCanonicalPath() + ": " + iox.getMessage(), iox);
            }
        }
        return manifest;
    }

    private void executeUnzip() throws Exception {
        File file = null;
        JarFile jar;
        Enumeration<JarEntry> content;
        JarEntry entry;
        String classFile = "";
        String filename = "";
        long archiveDate;
        long hdDate;
        int extracted = 0;
        int processed = 0;
        ArrayList<String> iSecName = new ArrayList<String>();
        ArrayList<String> iSecOrigin = new ArrayList<String>();
        ArrayList<String> iSecDate = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String jarDate;
        int index;
        file = new File(source);
        String path = null;
        String[] list = null;
        if (file.isDirectory()) {
            path = source;
            list = this.archiveFilter(file.list());
        } else {
            path = file.getPath().substring(0, file.getPath().length() - file.getName().length());
            list = new String[1];
            list[0] = file.getName();
        }
        if (list.length == 0) {
            this.logln("\n no jar/zip archives found");
            return;
        }
        if (oldOverwrites) {
            this.logln("\n directive: overwrite newer versions");
        } else {
            this.logln("\n directive: overwrite older versions");
        }
        try {
            for (String element : list) {
                this.logln("\n unpack " + element + ":");
                jar = new JarFile(path + element);
                content = jar.entries();
                file = new File(path + element);
                jarDate = file.lastModified() < 0 ? "unknown" : sdf.format(new Date(file.lastModified()));
                while (content.hasMoreElements()) {
                    entry = content.nextElement();
                    classFile = entry.getName();
                    if (!(classFile.charAt(classFile.length() - 1) == '/' || "META-INF/MANIFEST.MF".equals(classFile.toUpperCase()))) {
                        index = classFile.lastIndexOf('/') + 1;
                        if (fileFilterPattern.matcher(classFile.substring(index)).matches()) {
                            if (detach) {
                                filename = classFile.substring(index);
                            } else {
                                filename = classFile;
                            }
                            file = new File((targetPath != null ? targetPath : "") + filename);
                            if (file.canRead()) {
                                hdDate = file.lastModified();
                                archiveDate = entry.getTime();
                                if (!oldOverwrites && archiveDate > hdDate || oldOverwrites && archiveDate < hdDate) {
                                    this.unpackJarEntry(jar, entry, targetPath);
                                    if (iSecName.contains(filename)) {
                                        index = iSecName.indexOf(filename);
                                        iSecName.remove(index);
                                        iSecOrigin.remove(index);
                                        iSecDate.remove(index);
                                        iSecName.add(index, filename);
                                        iSecOrigin.add(index, element);
                                        iSecDate.add(index, jarDate);
                                    } else {
                                        iSecName.add(filename);
                                        iSecOrigin.add(element);
                                        iSecDate.add(jarDate);
                                    }
                                    this.logln("  extract and overwrite " + filename);
                                    extracted++;
                                } else {
                                    this.logln("  do not overwrite      " + filename);
                                    processed++;
                                }
                            } else {
                                this.unpackJarEntry(jar, entry, targetPath);
                                this.logln("  extract and write     " + filename);
                                iSecName.add(filename);
                                iSecOrigin.add(element);
                                iSecDate.add(jarDate);
                                extracted++;
                            }
                        }
                    }
                }
            }
            this.createManifestFile(iSecName, iSecOrigin, iSecDate, targetPath);
            processed += extracted;
            this.logln("\n " + list.length + " archive" + (list.length != 1 ? "s" : "") + " passed");
            this.logln(" " + processed + " file" + (processed != 1 ? "s" : "") + " passed");
            this.logln(" " + extracted + " file" + (extracted != 1 ? "s" : "") + " extracted");
        } catch (Exception e) {
            this.loglnError("unzip error: " + e.getMessage() + ": " + classFile);
        }
    }

    private void createJarArchive(final File jarFile, final File[] tobeJared, final String manifestFile) throws IOException, Exception {
        if (jarFile == null) {
            throw new Exception("jarFile is null");
        }
        if (tobeJared == null) {
            throw new Exception("array of files is null");
        }
        if (tobeJared.length == 0) {
            throw new Exception("no files to archive");
        }
        int BUFFER_SIZE = 10240;
        String path;
        try {
            byte buffer[] = new byte[BUFFER_SIZE];
            FileOutputStream stream = new FileOutputStream(jarFile);
            Manifest manifest = readManifestFile(manifestFile);
            if (manifest != null) {
                File file = new File(manifestFile);
                this.logln("\n adding existing manifest file " + file.getCanonicalPath());
            } else {
                this.logln("\n create new manifest");
                StringBuilder sb = new StringBuilder();
                sb.append("Manifest-Version: 1.0\n");
                sb.append("Created-By: ").append(System.getProperty("java.vm.version")).append(" (").append(System.getProperty("java.vm.vendor")).append(")\n");
                sb.append("\n");
                InputStream is = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
                manifest = new Manifest(is);
            }
            JarOutputStream out = new JarOutputStream(stream, manifest);
            if (compression != -1) {
                out.setLevel(compression);
            }
            JarEntry jarAdd;
            for (int i = 0; i < tobeJared.length; i++) {
                if (tobeJared[i] == null) {
                    continue;
                }
                if (!tobeJared[i].exists() || tobeJared[i].isDirectory()) {
                    continue;
                }
                path = tobeJared[i].getPath().substring(source.length());
                path = path.replaceAll("\\\\", "/");
                this.log("\n adding " + path);
                jarAdd = new JarEntry(path);
                jarAdd.setTime(tobeJared[i].lastModified());
                out.putNextEntry(jarAdd);
                FileInputStream in = new FileInputStream(tobeJared[i]);
                while (true) {
                    int nRead = in.read(buffer, 0, buffer.length);
                    if (nRead <= 0) {
                        break;
                    }
                    out.write(buffer, 0, nRead);
                }
                in.close();
            }
            out.close();
            stream.close();
            this.logln("\n " + tobeJared.length + " file" + (tobeJared.length != 1 ? "s" : "") + " added");
        } catch (IOException iox) {
            throw new IOException(iox.getMessage() + ": " + identity + ".createJarArchive: " + jarFile.getAbsolutePath());
        } catch (Exception e) {
            throw new Exception(e.getMessage() + ": " + identity + ".createJarArchive: " + jarFile.getAbsolutePath());
        }
    }

    private void addFilesToExistingJar(final File jarFile, final File[] tobeJared, final String manifestFile) throws IOException, Exception {
        if (jarFile == null) {
            throw new Exception("jarFile is null");
        }
        if (tobeJared == null) {
            throw new Exception("array of files is null");
        }
        if (tobeJared.length == 0) {
            throw new Exception("no files to archive");
        }
        JarFile jar = new JarFile(jarFile);
        Enumeration jarEnum = jar.entries();
        Hashtable jarContent = new Hashtable();
        String elem;
        while (jarEnum.hasMoreElements()) {
            elem = ((JarEntry) jarEnum.nextElement()).getName();
            jarContent.put(elem, "notnull");
        }
        jar.close();
        Manifest manifest = readManifestFile(manifestFile);
        if (manifest != null) {
            File file = new File(manifestFile);
            this.logln("\n adding existing manifest file " + file.getCanonicalPath());
        } else {
            this.logln("\n create new manifest");
            StringBuilder sb = new StringBuilder();
            sb.append("Manifest-Version: 1.0\n");
            sb.append("Created-By: ").append(System.getProperty("java.vm.version")).append(" (").append(System.getProperty("java.vm.vendor")).append(")\n");
            sb.append("\n");
            InputStream is = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
            manifest = new Manifest(is);
        }
        String rand1 = String.valueOf(Math.random()).substring(2);
        String rand2 = String.valueOf(Math.random()).substring(2);
        String randomName = jarFile.getName() + ".tmp." + rand1 + rand2;
        String tempfile = jarFile.getCanonicalPath();
        tempfile = tempfile.substring(0, tempfile.length() - jarFile.getName().length()) + randomName;
        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarFile.getPath()));
        JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(tempfile), manifest);
        if (compression != -1) {
            jarOut.setLevel(compression);
        }
        byte[] buffer = new byte[4096];
        JarEntry entry;
        String path;
        Hashtable inventory = new Hashtable();
        int nrOfWrittenFiles = 0;
        for (int i = 0; i < tobeJared.length; i++) {
            if (tobeJared[i] == null) {
                continue;
            }
            if (!tobeJared[i].exists() || tobeJared[i].isDirectory()) {
                continue;
            }
            path = tobeJared[i].getPath().substring(source.length());
            path = path.replaceAll("\\\\", "/");
            if (jarContent.get(path) != null) {
                this.log("\n overwriting " + path);
            } else {
                this.log("\n      adding " + path);
            }
            entry = new JarEntry(path);
            entry.setTime(tobeJared[i].lastModified());
            jarOut.putNextEntry(entry);
            FileInputStream in = new FileInputStream(tobeJared[i]);
            while (true) {
                int nRead = in.read(buffer, 0, buffer.length);
                if (nRead <= 0) {
                    break;
                }
                jarOut.write(buffer, 0, nRead);
            }
            inventory.put(entry.getName(), "notnull");
            in.close();
            nrOfWrittenFiles++;
        }
        while ((entry = jarIn.getNextJarEntry()) != null) {
            if ("META-INF/MANIFEST.MF".equals(entry.getName())) {
                continue;
            }
            if (inventory.get(entry.getName()) != null) {
                continue;
            }
            jarOut.putNextEntry(entry);
            int read;
            while ((read = jarIn.read(buffer)) != -1) {
                jarOut.write(buffer, 0, read);
            }
            jarOut.closeEntry();
        }
        jarOut.flush();
        jarOut.close();
        jarIn.close();
        File oldJar = new File(targetPath);
        try {
            if (!jarFile.delete()) {
                throw new Exception("cannot delete old " + oldJar.getCanonicalPath());
            }
            File newJar = new File(tempfile);
            if (!newJar.renameTo(oldJar)) {
                throw new Exception("cannot rename tempfile " + tempfile + " to " + oldJar.getCanonicalPath());
            }
        } catch (Exception e) {
            throw new Exception("cannot update jar: " + e.getMessage());
        }
        this.logln("\n " + nrOfWrittenFiles + " file" + (tobeJared.length != 1 ? "s" : "") + " added");
    }

    private ArrayList<File> getAllFiles(final File file, ArrayList<File> allFiles) {
        if (allFiles == null) {
            allFiles = new ArrayList<File>();
        }
        if (file.isDirectory()) {
            String[] list = file.list();
            for (String element : list) {
                this.getAllFiles(new File(file.getPath() + "/" + element), allFiles);
            }
        } else {
            allFiles.add(file);
        }
        return allFiles;
    }

    private void executeZip() throws Exception {
        File file = null;
        ArrayList<File> classFiles;
        try {
            file = new File(targetPath);
            if (file.isFile()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException iox) {
            throw new Exception(iox.getMessage() + ": " + file.getCanonicalPath());
        }
        File directory = new File(source);
        classFiles = new ArrayList<File>();
        classFiles = this.getAllFiles(directory, classFiles);
        File[] classes = this.fileFilter(classFiles.toArray(new File[classFiles.size()]), fileFilterPattern);
        if (classes != null && classes.length > 0) {
            if (manifestFile != null) {
                this.createJarArchive(file, classes, manifestFile);
            } else if (standardManifestToCreate) {
                this.createJarArchive(file, classes, null);
            } else {
                this.createJarArchive(file, classes, source + "META-INF/MANIFEST.MF");
            }
            this.logln("\n " + file.getCanonicalPath() + " created");
        } else {
            this.logln("\n no files to archive");
        }
    }

    private void executeAdd() throws Exception {
        File file = null;
        ArrayList<File> content;
        file = new File(targetPath);
        if (!file.exists()) {
            this.executeZip();
            return;
        } else if (!file.canWrite()) {
            try {
                throw new Exception("cannot write " + file.getCanonicalPath());
            } catch (Exception e) {
                throw new Exception("cannot write " + targetPath);
            }
        }
        File directory = new File(source);
        content = new ArrayList<File>();
        content = this.getAllFiles(directory, content);
        File[] filesToAdd = this.fileFilter(content.toArray(new File[content.size()]), fileFilterPattern);
        if (filesToAdd != null && filesToAdd.length > 0) {
            if (manifestFile != null) {
                this.addFilesToExistingJar(file, filesToAdd, manifestFile);
            } else if (standardManifestToCreate) {
                this.addFilesToExistingJar(file, filesToAdd, null);
            } else {
                this.addFilesToExistingJar(file, filesToAdd, source + "META-INF/MANIFEST.MF");
            }
            this.logln("\n " + file.getCanonicalPath() + " updated");
        } else {
            this.logln("\n no files to archive");
        }
    }

    private void execute() {
        try {
            File file = new File(source);
            if (!file.canRead()) {
                throw new Exception(source + " not found or not readable");
            }
            if ((check || zip || add) && !file.isDirectory()) {
                throw new Exception(file.getCanonicalPath() + " is no directory");
            } else if (unzip) {
                if (!file.isDirectory()) {
                    if (!file.isFile()) {
                        throw new Exception(source + " is no normal file");
                    } else {
                        if (source.length() < 5) {
                            throw new Exception(file.getCanonicalPath() + " is no valid archiv");
                        } else {
                            String suffix = source.substring(source.length() - 4);
                            if (!(".jar".equals(suffix) || ".zip".equals(suffix))) {
                                throw new Exception(file.getCanonicalPath() + " is no valid archiv");
                            }
                        }
                    }
                }
            }
            if (file.isDirectory()) {
                source = source.replaceAll("\\\\", "/");
                if (!(source.charAt(source.length() - 1) == '/')) {
                    source += "/";
                }
            }
            if (unzip) {
                if (targetPath != null) {
                    try {
                        file = new File(targetPath);
                        if (!file.isDirectory()) {
                            throw new Exception(targetPath + " is no directory");
                        } else if (!file.canWrite()) {
                            throw new Exception(targetPath + " is not writable");
                        }
                        targetPath = targetPath.replaceAll("\\\\", "/");
                        if (!(targetPath.charAt(targetPath.length() - 1) == '/')) {
                            targetPath += "/";
                        }
                    } catch (IOException iox) {
                        if (file.isFile()) {
                            throw new Exception(file.getCanonicalPath() + ": " + iox.getMessage());
                        } else {
                            throw new Exception(targetPath + ": " + iox.getMessage());
                        }
                    }
                }
                if (zip || add) {
                    if (manifestFile != null) {
                        file = new File(manifestFile);
                        if (!file.canRead()) {
                            throw new Exception("manifest file " + file.getCanonicalPath() + " not found or not readable");
                        }
                    }
                    file = new File(targetPath);
                    if (file.isDirectory()) {
                        throw new Exception(file.getCanonicalPath() + " is a directory");
                    }
                }
            }
        } catch (Exception e) {
            this.loglnError("\n abort: " + e.getMessage());
            System.exit(-1);
        }
        try {
            if (check) {
                this.executeCheck();
            } else if (unzip) {
                this.executeUnzip();
            } else if (zip) {
                this.executeZip();
            } else if (add) {
                this.executeAdd();
            }
        } catch (Exception e) {
            this.loglnError("\n error: " + e.getMessage());
            System.exit(-1);
        }
        System.exit(0);
    }

    public static void main(final String[] args) {
        JarMerge jarmerge = new JarMerge(args);
        jarmerge.execute();
    }

}