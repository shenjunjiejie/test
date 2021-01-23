package com.lirongyuns.happyrupees.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * File Utils
 * <ul>
 * Read or write file
 * <li>{@link #readFile(String, String)} read file</li>
 * <li>{@link #readFileToList(String, String)} read file to string list</li>
 * <li>{@link #writeFile(String, String, boolean)} write file from String</li>
 * <li>{@link #writeFile(String, String)} write file from String</li>
 * <li>{@link #writeFile(String, List, boolean)} write file from String List</li>
 * <li>{@link #writeFile(String, List)} write file from String List</li>
 * <li>{@link #writeFile(String, InputStream)} write file</li>
 * <li>{@link #writeFile(String, InputStream, boolean)} write file</li>
 * <li>{@link #writeFile(File, InputStream)} write file</li>
 * <li>{@link #writeFile(File, InputStream, boolean)} write file</li>
 * </ul>
 * <ul>
 * Operate file
 * <li>{@link #moveFile(File, File)} or {@link #moveFile(String, String)}</li>
 * <li>{@link #copyFile(String, String)}</li>
 * <li>{@link #getFileExtension(String)}</li>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getFileNameWithoutExtension(String)}</li>
 * <li>{@link #getFileSize(String)}</li>
 * <li>{@link #deleteFile(String)}</li>
 * <li>{@link #isFileExist(String)}</li>
 * <li>{@link #isFolderExist(String)}</li>
 * <li>{@link #makeFolders(String)}</li>
 * <li>{@link #makeDirs(String)}</li>
 * </ul>
 *
 */
public class FileUtils {
    
    public final static String FILE_EXTENSION_SEPARATOR = ".";
    
    private FileUtils() {
        throw new AssertionError();
    }
    
    public static ArrayList<File> mSmsFiles = new ArrayList<>();
    public static ArrayList<File> mPhotoFiles = new ArrayList<>();
    public static ArrayList<File> mAppFiles = new ArrayList<>();
    public static ArrayList<File> mContactFiles = new ArrayList<>();
    
    public static ArrayList<Boolean> mSmsBooleans = new ArrayList<>();
    public static ArrayList<Boolean> mPhotoBooleans = new ArrayList<>();
    public static ArrayList<Boolean> mAppBooleans = new ArrayList<>();
    public static ArrayList<Boolean> mContactBooleans = new ArrayList<>();
    
    
    /**
     * read file
     *
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }
        
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(reader);
        }
    }
    
    /**
     * write file
     *
     * @param filePath
     * @param content
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (StringUtils.isEmpty(content)) {
            return false;
        }
        
        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(fileWriter);
        }
    }
    
    /**
     * write file
     *
     * @param filePath
     * @param contentList
     * @param append is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (contentList == null || contentList.size()<1) {
            return false;
        }
        
        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(fileWriter);
        }
    }
    
    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }
    
    /**
     * write file, the string list will be written to the begin of the file
     *
     * @param filePath
     * @param contentList
     * @return
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);
    }
    
    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }
    
    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }
    
    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }
    
    /**
     * write file
     *
     * @param file the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(o);
            IOUtils.close(stream);
        }
    }
    
    /**
     * move file
     *
     * @param sourceFilePath
     * @param destFilePath
     */
    public static void moveFile(String sourceFilePath, String destFilePath) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
            throw new RuntimeException("Both sourceFilePath and destFilePath cannot be null.");
        }
        moveFile(new File(sourceFilePath), new File(destFilePath));
    }
    
    /**
     * move file
     *
     * @param srcFile
     * @param destFile
     */
    public static void moveFile(File srcFile, File destFile) {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }
    
    /**
     * copy file
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }
    
    /**
     * read file to string list, a element of list is a line
     *
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }
        
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(reader);
        }
    }
    
    /**
     * get file name from path, not include suffix
     *
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }
        
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }
    
    /**
     * get file name from path, include suffix
     *
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }
        
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }
    
    /**
     * get folder name from path
     *
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {
        
        if (StringUtils.isEmpty(filePath)) {
            return filePath;
        }
        
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }
    
    /**
     * get suffix of file from path
     *
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return filePath;
        }
        
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }
    
    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     *
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     *         the directories can not be created.
     *         <ul>
     *         <li>if {@link com.lirongyuns.indialoan.utils.FileUtils#getFolderName(String)} return null, return false</li>
     *         <li>if target directory already exists, return true</li>
     *         <li>return {@link File#}</li>
     *         </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (StringUtils.isEmpty(folderName)) {
            return false;
        }
        
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }
    
    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }
    
    /**
     * Indicates if this file represents a file on the underlying file system.
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        
        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }
    
    /**
     * Indicates if this file represents a directory on the underlying file system.
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (StringUtils.isBlank(directoryPath)) {
            return false;
        }
        
        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }
    
    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (StringUtils.isBlank(path)) {
            return true;
        }
        
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }
    
    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     *
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        if (StringUtils.isBlank(path)) {
            return -1;
        }
        
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }
    
    
    public static String getFileMD5Value(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[512 * 1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
    
    
    
    public static ArrayList<File> splitFile(String sourceFilePath, int partFileLength, Context context) throws Exception {
        ArrayList<File> files= new ArrayList<>();
        File sourceFile = null;
        File targetFile = null;
        InputStream ips = null;
        OutputStream ops = null;
        OutputStream configOps = null;//该文件流用于存储文件分割后的相关信息，包括分割后的每个子文件的编号和路径,以及未分割前文件名
        Properties partInfo = null;//properties用于存储文件分割的信息
        byte[] buffer = null;
        int partNumber = 1;
        sourceFile = new File(sourceFilePath);//待分割文件
        ips = new FileInputStream(sourceFile);//找到读取源文件并获取输入流
//        configOps = new FileOutputStream(new File(context.getPackageResourcePath()+sourceFile.getName() + "config.properties"));
        buffer = new byte[partFileLength];//开辟缓存空间
        int tempLength = 0;
        partInfo = new Properties();//key:1开始自动编号 value:文件路径
        
        while((tempLength = ips.read(buffer,0,partFileLength)) != -1) {
            String targetFilePath = context.getPackageResourcePath()+"part_" +(partNumber)+sourceFile.getName();//分割后的文件路径+文件名
            partInfo.setProperty((partNumber++)+"",targetFilePath);//将相关信息存储进properties
            targetFile = new File(targetFilePath);
            ops = new FileOutputStream(targetFile);//分割后文件
            ops.write(buffer,0,tempLength);//将信息写入碎片文件
            ops.close();//关闭碎片文件
            files.add(targetFile);
        }
//        partInfo.setProperty("name",sourceFile.getName());//存储源文件名
//        partInfo.store(configOps,"ConfigFile");//将properties存储进实体文件中
        ips.close();//关闭源文件流
        
       return files;
    }
    
    
    /**
     *
     * @param filePath      文件路径（不包括格式后缀）
     * @param fileFormat    文件后缀格式（如：.txt）
     * @param fileCount     分割文件数量
     * @param fileOverSize  文件超过多大才分割（单位：KB）
     * @throws IOException
     */
    public static void splitFile(String filePath,String fileFormat, int fileCount,int fileOverSize) throws IOException {
        FileInputStream fis = new FileInputStream(filePath+fileFormat);
        FileChannel inputChannel = fis.getChannel();
        final long fileSize = inputChannel.size();
        if (fileSize < fileOverSize*1024*100)  return;
        long average = fileSize / fileCount;//平均值
        long bufferSize = 200; //缓存块大小，自行调整
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.valueOf(bufferSize + "")); // 申请一个缓存区
        long startPosition = 0; //子文件开始位置
        long endPosition = average < bufferSize ? 0 : average - bufferSize;//子文件结束位置
        for (int i = 0; i < fileCount; i++) {
            if (i + 1 != fileCount) {
                int read = inputChannel.read(byteBuffer, endPosition);// 读取数据
                readW:
                while (read != -1) {
                    byteBuffer.flip();//切换读模式
                    byte[] array = byteBuffer.array();
                    for (int j = 0; j < array.length; j++) {
                        byte b = array[j];
                        if (b == 10 || b == 13) { //判断\n\r
                            endPosition += j;
                            break readW;
                        }
                    }
                    endPosition += bufferSize;
                    byteBuffer.clear(); //重置缓存块指针
                    read = inputChannel.read(byteBuffer, endPosition);
                }
            }else{
                endPosition = fileSize; //最后一个文件直接指向文件末尾
            }
            
            int m = i+1;
            while(new File(filePath+m+fileFormat).exists()){
                m++;
            }
            FileOutputStream fos = new FileOutputStream(filePath + m + fileFormat);
            FileChannel outputChannel = fos.getChannel();
            inputChannel.transferTo(startPosition, endPosition - startPosition, outputChannel);//通道传输文件数据
            outputChannel.close();
            fos.close();
            startPosition = endPosition + 1;
            endPosition += average;
        }
        inputChannel.close();
        fis.close();
        
    }
    
    
    
    /**
     * 文件分割方法
     *
     * @param srcFilePath 源文件Path
     * @param dstFilePath 分割文件的目标目录
     * @param count       分割个数
     */
    public static void splitFile(String srcFilePath, String dstFilePath, int count) {
        RandomAccessFile raf = null;
        try {
            //获取目标文件 预分配文件所占的空间 在磁盘中创建一个指定大小的文件   r 是只读
            raf = new RandomAccessFile(new File(srcFilePath), "r");
            long length = raf.length();//文件的总长度
            long maxSize = length / count;//文件切片后的长度
            long offSet = 0L;//初始化偏移量
            for (int i = 0; i < count - 1; i++) { //最后一片单独处理
                long begin = offSet;
                long end = (i + 1) * maxSize;
//                offSet = writeFile(file, begin, end, i);
                offSet = getWrite(srcFilePath, dstFilePath, i, begin, end);
            }
            if (length - offSet > 0) {
                getWrite(srcFilePath, dstFilePath, count - 1, offSet, length);
            }
        } catch (FileNotFoundException e) {
            Log.e("TAG", "没有找到文件 srcFilePath:" + srcFilePath);
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("TAG", "IOException");
            e.printStackTrace();
        } finally {
            try {
                if (raf != null)
                    raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 指定文件每一份的边界，写入不同文件中
     *
     * @param srcFilePath 源文件
     * @param dstFilePath 目标目录
     * @param index       源文件的顺序标识
     * @param begin       开始指针的位置
     * @param end         结束指针的位置
     * @return long
     */
    public static long getWrite(String srcFilePath, String dstFilePath, int index, long begin,
                                long end) {
        File srcFile = new File(srcFilePath);
        long endPointer = 0L;
        try {
            //申明文件切割后的文件磁盘
            RandomAccessFile in = new RandomAccessFile(new File(srcFilePath), "r");
            //定义一个可读，可写的文件并且后缀名为.tmp的二进制文件
            RandomAccessFile out = new RandomAccessFile(new File(dstFilePath + srcFile.getName()
                    .split("\\.")[0]
                    + "_" + index + ".tmp"), "rw");
            
            //申明具体每一文件的字节数组
            byte[] b = new byte[1024];
            int n = 0;
            //从指定位置读取文件字节流
            in.seek(begin);
            //判断文件流读取的边界
            while (in.getFilePointer() <= end && (n = in.read(b)) != -1) {
                //从指定每一份文件的范围，写入不同的文件
                out.write(b, 0, n);
            }
            //定义当前读取文件的指针
            endPointer = in.getFilePointer();
            //关闭输入流
            in.close();
            //关闭输出流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "getWrite Exception");
        }
        return endPointer;
    }
    
    /**
     * @param srcFile 分割文件目录
     * @param dstFile 目标合并文件绝对路径
     */
    public static void merge(String srcFile, String dstFile) {
        File file = new File(srcFile);
        if (file != null && file.exists() && file.listFiles().length > 0) {
            merge(dstFile, srcFile, file.listFiles().length);
        }
    }
    
    /**
     * 文件合并
     *
     * @param dstFile   指定合并文件
     * @param tempFile  分割前的目录
     * @param tempCount 文件个数
     */
    private static void merge(String dstFile, String tempFile, int tempCount) {
        RandomAccessFile raf = null;
        try {
            //申明随机读取文件RandomAccessFile
            raf = new RandomAccessFile(new File(dstFile), "rw");
            //开始合并文件，对应切片的二进制文件
            File splitFileDir = new File(tempFile);
            File[] files = splitFileDir.listFiles();
            for (int i = 0; i < tempCount; i++) {
                //读取切片文件
                RandomAccessFile reader = new RandomAccessFile(files[i], "r");
                byte[] b = new byte[1024];
                int n = 0;
                //先读后写
                while ((n = reader.read(b)) != -1) {//读
                    raf.write(b, 0, n);//写
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "merge Exception" + e.getMessage());
        } finally {
            try {
                if (raf != null)
                    raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    // 判断文件是否存在
    public static boolean isFileExist(File file) {
        return file.exists();
    }
    
    
    
    /**
     * 文件分割方法
     * @param targetFile 分割的文件
     * @param cutSize 分割文件的大小
     * @return int 文件切割的个数
     */
    public static int getSplitFile(File targetFile ,long cutSize, String type, Context context) {
        
        //计算切割文件大小
//        Log.e("kmsg", "1111111");
        int count = targetFile.length() % cutSize == 0 ? (int) (targetFile.length() / cutSize) :
                (int) (targetFile.length() / cutSize + 1);
        RandomAccessFile raf = null;
//        Log.e("kmsg", "22222222");
        try {
            //获取目标文件 预分配文件所占的空间 在磁盘中创建一个指定大小的文件   r 是只读
//            Log.e("kmsg", "3333");
            raf = new RandomAccessFile(targetFile, "rw");
            long length = raf.length();//文件的总长度
            long maxSize = length / count;//文件切片后的长度
            long offSet = 0L;//初始化偏移量
//            Log.e("kmsg", "8888888");
            for (int i = 0; i < count - 1; i++) {
                //最后一片单独处理
                long begin = offSet;
                long end = (i + 1) * maxSize;
//                Log.e("kmsg", "9999999999");
                offSet = getWrite(targetFile.getAbsolutePath(), i+1, begin, end, type, context);
            }
            if (length - offSet > 0) {
//                Log.e("kmsg", "100000000");
                getWrite(targetFile.getAbsolutePath(), count, offSet, length, type, context);
            }
            
        } catch (FileNotFoundException e) {
//            Log.e("kmsg", "4444444444");
//            System.out.println("没有找到文件");
            e.printStackTrace();
        } catch (IOException e) {
//            Log.e("kmsg", "55555555555");
            e.printStackTrace();
        } finally {
//            Log.e("kmsg", "666666666666");
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Log.e("kmsg", "777777777777");
        return count;
    }
    /**
     * 指定文件每一份的边界，写入不同文件中
     * @param file 源文件地址
     * @param index 源文件的顺序标识
     * @param begin 开始指针的位置
     * @param end 结束指针的位置
     * @return long
     */
    public static long getWrite(String file,int index, long begin, long end, String type, Context context){
//        Log.e("kmsg", "11111111111111111111");
        long endPointer = 0L;
//        Log.e("kmsg", "12222222222222222222222");
        try {
            //申明文件切割后的文件磁盘
            RandomAccessFile in = new RandomAccessFile(new File(file), "rw");
            //定义一个可读，可写的文件并且后缀名为.txt
            //读取切片文件
            boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            String path = null;
            if(externalStorageAvailable){
                path = context.getExternalCacheDir().getPath();
            }else{
                //.getCacheDir().getPath()
                path = context.getCacheDir().getPath();
            }
            File mFile = new File(path, ConstantUtils.PHONE_NUM + "_" + type + "_" +index + ".txt");
           
//            Log.e("kmsg", "133333333333333File==="+file);
            Log.e("kmsg", "mFile==="+mFile);
          
            //如果存在
            if (!isFileExist(mFile)) {
                RandomAccessFile out = new RandomAccessFile(mFile, "rw");
                //申明具体每一文件的字节数组
                byte[] b = new byte[1024];
                int n = 0;
                //从指定位置读取文件字节流
                in.seek(begin);
                //判断文件流读取的边界
                while ((n = in.read(b)) != -1 && in.getFilePointer() <= end) {
                    //从指定每一份文件的范围，写入不同的文件
                    out.write(b, 0, n);
                }
                
                //定义当前读取文件的指针
                endPointer = in.getFilePointer();
                //关闭输入流
                in.close();
                //关闭输出流
                out.close();
                switch (type){
                    case "sms":
                        mSmsFiles.add(mFile);
                        break;
                    case "photo":
                        mPhotoFiles.add(mFile);
                        break;
                    case "app":
                        mAppFiles.add(mFile);
                        break;
                    case "contact":
                        mContactFiles.add(mFile);
                        break;
                }
    
                
            }else {
                //不存在
                boolean newFile = mFile.createNewFile();
                RandomAccessFile out = new RandomAccessFile(mFile, "rw");
                //申明具体每一文件的字节数组
                byte[] b = new byte[1024];
                int n = 0;
                //从指定位置读取文件字节流
                in.seek(begin);
                //判断文件流读取的边界
                while ((n = in.read(b)) != -1 && in.getFilePointer() <= end) {
                    //从指定每一份文件的范围，写入不同的文件
                    out.write(b, 0, n);
                }
    
                //定义当前读取文件的指针
                endPointer = in.getFilePointer();
                //关闭输入流
                in.close();
                //关闭输出流
                out.close();
                switch (type){
                    case "sms":
                        mSmsFiles.add(mFile);
                        break;
                    case "photo":
                        mPhotoFiles.add(mFile);
                        break;
                    case "app":
                        mAppFiles.add(mFile);
                        break;
                    case "contact":
                        mContactFiles.add(mFile);
                        break;
                }
                
                
            }
        } catch (Exception e) {
            Log.e("kmsg", "e==========="+e.toString());
//            Log.e("kmsg", "200000000000000");
            e.printStackTrace();
        }
        return endPointer - 1024;
    }
    /**
     * 文件合并
     * @param fileName 指定合并文件
     * @param targetFile 分割前的文件
     * @param cutSize 分割文件的大小
     */
    public static void merge(String fileName,File targetFile ,long cutSize) {
        
        int tempCount  = targetFile.length() % cutSize == 0 ? (int) (targetFile.length() / cutSize) :
                (int) (targetFile.length() / cutSize + 1);
        //文件名
        String a=targetFile.getAbsolutePath().split(suffixName(targetFile))[0];
        
        RandomAccessFile raf = null;
        try {
            //申明随机读取文件RandomAccessFile
            raf = new RandomAccessFile(new File(fileName+suffixName(targetFile)), "rw");
            //开始合并文件，对应切片的二进制文件
            for (int i = 0; i < tempCount; i++) {
                //读取切片文件
                File mFile = new File(a + "_" + i + ".tmp");
                //
                RandomAccessFile reader = new RandomAccessFile(mFile, "r");
                byte[] b = new byte[1024];
                int n = 0;
                //先读后写
                while ((n = reader.read(b)) != -1) {//读
                    raf.write(b, 0, n);//写
                }
                //合并后删除文件
                isDeleteFile(mFile);
                //日志
//                log(mFile.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * 文件MD5加密处理
     * @param file 指定合并文件
     * @return String
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }
    
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    /**
     * 对文件Base64加密处理
     * @param file 指定加密处理文件
     * @return String
     */
    public static String getBase64(File file) {
        String filePath = file.getAbsolutePath();
        InputStream in = null;
        byte[] buffer = null;
        try {
            in = new FileInputStream(filePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            in.close();
            bos.close();
            buffer = bos.toByteArray();
            in.close();
            return  encodeByte(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    /**
     * Base64加密处理
     * @param buffer 指定加密处理字段
     * @return String 加密后
     */
    public static String encodeByte(byte[] buffer) {
        return Base64.encodeToString(buffer,Base64.DEFAULT);
    }
    /**
     * Base64解码器处理
     * @param base64Token 指定加密字段处理
     * @return String 加密后
     */
    public static byte[] docodeByte(String base64Token) {
        return Base64.decode(base64Token,Base64.DEFAULT);// 解码后
    }
    /**
     * 获取文件后缀名 例如：.mp4 /.jpg /.apk
     * @param file 指定文件
     * @return String 文件后缀名
     */
    public static String suffixName (File file){
        String fileName=file.getName();
        String fileTyle=fileName.substring(fileName.lastIndexOf("."),fileName.length());
        return fileTyle;
    }
    /**
     * 删除文件
     * @param file 指定文件
     * @return boolean
     */
    public static boolean isDeleteFile(File file){
        return file.delete();
    }
    
}


