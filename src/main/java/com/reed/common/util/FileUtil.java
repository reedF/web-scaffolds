/**
 * FileUtil.java
 * Copyright (c) 2013 by lashou.com.
 */
package com.reed.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.FileUtils;

/**
 * File utility class
 * 
 */
public final class FileUtil extends FileUtils {
    /**. read size */
    private static final int PER_READ_SIZE = 1024;


    /**.
     * private constructor
     */
    private FileUtil() {

    }

    /**
     * make directory
     * @param dir pathname
     * @return if success
     */
    public static boolean mkdir(final String dir) {
        File file = new File(dir);
        if (file.exists() && file.isDirectory()) {
            return true;
        }
        return file.mkdirs();
    }

    /**
     * Tests whether the file or directory denoted by this abstract pathname exists.
     * @param filePath file path
     * @return <code>true</code> if and only if the file or directory denoted
     *          by this abstract pathname exists; <code>false</code> otherwise
     */
    public static boolean exists(final String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * move file
     * @param srcPath source file
     * @param destPath destination file
     * @return <code>true</code> if and only if the renaming succeeded;
     *          <code>false</code> otherwise
     */
    public static boolean mv(final String srcPath, final String destPath) {
        File src = new File(srcPath);
        File dest = new File(destPath);
        if (!src.exists()) {
            return false;
        }
        return src.renameTo(dest);
    }

    /**
     * get a File instance
     * @param path file path
     * @return new File object
     */
    public static File getFile(final String path) {
        return new File(path);
    }

    /**
     * delete a file or an empty directory
     * @param filePath file path
     * @return <code>true</code> if and only if the file or directory is
     *          successfully deleted or not existed; <code>false</code> otherwise
     */
    public static boolean delete(final String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        } else {
            return true;
        }
    }

    /**
     * get FileWriter instance
     * @param path
     *            file path
     * @param isMakedirs
     *            make direction
     * @return new FileWriter object
     * @throws IOException if the named file exists but is a directory rather
     *                  than a regular file, does not exist but cannot be
     *                  created, or cannot be opened for any other reason
     */
    public static Writer getFileWriter(final String path, final boolean isMakedirs)
            throws IOException {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            if (isMakedirs) {
                file.getParentFile().mkdirs();
            } else {
                return null;
            }
        }
        return new FileWriter(path);
    }

    /**
     * get FileWriter instance
     * @param path
     *            file path
     * @param isMakedirs
     *            make direction
     * @param encoding
     *            file encoding
     * @return new FileWriter object
     * @throws IOException if the named file exists but is a directory rather
     *                  than a regular file, does not exist but cannot be
     *                  created, or cannot be opened for any other reason
     */
    public static Writer getFileWriter(final String path, final boolean isMakedirs, final String encoding)
            throws IOException {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            if (isMakedirs) {
                file.getParentFile().mkdirs();
            } else {
                return null;
            }
        }
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                file), encoding));
    }

    /**
     * copy file
     * 
     * @param src
     *            source file
     * @param destine
     *            destine file
     * @return if successfully copied
     */
    public static boolean cp(final File src, final File destine) {
        FileInputStream fins = null;
        try {
            if (!src.exists()
                    || src.getCanonicalPath()
                            .equals(destine.getCanonicalPath())) {
                return false;
            }
            fins = new FileInputStream(src);
            destine.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(destine);
            try {
                byte[] buf = new byte[PER_READ_SIZE];
                int readLen;
                while ((readLen = fins.read(buf, 0, buf.length)) > 0) {
                    fos.write(buf, 0, readLen);
                }
                fos.flush();
                fos.close();
            } catch (Exception ex) {
                return false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {
                    	e.printStackTrace();
                    	/* IGNORE */
                    }
                }
            }
            fins.close();
            destine.setLastModified(src.lastModified());
        } catch (Exception e) {
            return false;
        } finally {
            if (fins != null) {
                try {
                    fins.close();
                } catch (Exception e) {
                	e.printStackTrace();
                	/* IGNORE */
                }
            }
        }
        return true;
    }

    /**
     * copy file
     * 
     * @param srcPath
     *            source file path
     * @param desPath
     *            destine file path
     * @return if successfully copied
     * @see FileUtil#cp(File, File)
     */
    public static boolean cp(final String srcPath, final String desPath) {
        File src = new File(srcPath);
        File destine = new File(desPath);
        return cp(src, destine);
    }
    
    /**
     * delete all the files and child folders in this path
     * @param path pathname
     * @return <code>true</code> only if all the files and child folders are successfully deleted
     */
    public static boolean delAllFile(final String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
          return flag;
        }
        if (!file.isDirectory()) {
          return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
           if (path.endsWith(File.separator)) {
              temp = new File(path + tempList[i]);
           } else {
               temp = new File(path + File.separator + tempList[i]);
           }
           if (temp.isFile()) {
              temp.delete();
           }
           flag = true;
        }
        return flag;
      }
 
    /**
     * Deletes a directory recursively. 
     *
     * @param dir  directory to delete
     * @throws IOException in case deletion is unsuccessful
     */
    public static void deleteDirectory(final String dir) throws IOException {
    	FileUtils.deleteDirectory(new File(dir));
    } 
    
}
