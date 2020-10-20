package com.jff.ims.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

public class ZipUtil {
//	/**
//	     * 解压zip文件
//	     * 
//	     * @param sourceFile,待解压的zip文件;
//	     *            toFolder,解压后的存放路径
//	     * @throws Exception
//	     **/
//
//	public static void zipToFile(String sourceFile, String toFolder) throws Exception {
//		String toDisk = toFolder;// 接收解压后的存放路径
//		ZipFile zfile = new ZipFile(sourceFile, "utf-8");// 连接待解压文件
//		Enumeration zList = zfile.getEntries();// 得到zip包里的所有元素
//		ZipEntry ze = null;
//		byte[] buf = new byte[1024];
//		while (zList.hasMoreElements()) {
//			ze = (ZipEntry) zList.nextElement();
//			if (ze.isDirectory()) {
//				// log.info("打开zip文件里的文件夹:"+ ze.getName() +"skipped...");
//				continue;
//			}
//			OutputStream outputStream = null;
//			InputStream inputStream = null;
//			try {
//				// 以ZipEntry为参数得到一个InputStream，并写到OutputStream中
//				outputStream = new BufferedOutputStream(new FileOutputStream(getRealFileName(toDisk, ze.getName())));
//				inputStream = new BufferedInputStream(zfile.getInputStream(ze));
//                int readLen = 0;
//				while ((readLen = inputStream.read(buf, 0, 1024)) != -1) {
//					outputStream.write(buf, 0, readLen);
//				}
//				inputStream.close();
//				outputStream.close();
//			} catch (Exception e) {
//				// log.info("解压失败："+e.toString());
//				throw new IOException("解压失败：" + e.toString());
//			} finally {
//				if (inputStream != null) {
//					try {
//						inputStream.close();
//					} catch (IOException ex) {
//					}
//				}
//				if (outputStream != null) {
//					try {
//						outputStream.close();
//					} catch (IOException ex) {
//						ex.printStackTrace();
//					}	
//				}
//				inputStream = null;
//				outputStream = null;
//			}
//		}
//		zfile.close();
//	}
//
//		/**
//	 		* 给定根目录，返回一个相对路径所对应的实际文件名.
//	     * 
//	     * @param zippath
//	     *            指定根目录
//	     * 
//	     * @param absFileName
//	     *            相对路径名，来自于ZipEntry中的name
//	     * 
//	     * @return java.io.File 实际的文件
//	     * 
//	     */
//
//	private static File getRealFileName(String zippath, String absFileName) {
//		// log.info("文件名："+absFileName);
//		String[] dirs = absFileName.split("/", absFileName.length());
//		File ret = new File(zippath);// 创建文件对象
//		if (dirs.length > 1) {
//			for (int i = 0; i < dirs.length - 1; i++) {
//				ret = new File(ret, dirs[i]);
//			}
//		}
//		if (!ret.exists()) {// 检测文件是否存在
//			ret.mkdirs();// 创建此抽象路径名指定的目录
//		}
//		ret = new File(ret, dirs[dirs.length - 1]);// 根据 ret 抽象路径名和 child
//		// 路径名字符串创建一个新 File 实例
//		return ret;
//	}
	
	/**
     * 解析附件zip包，并返回每张图片的上传路径 uploadImagePath
     *
     * @param unZipFileName   解压的zip文件
     * @param outputDirectory 输出目录
     */
    public static List<String> unZipAndReturnPath(String unZipFileName, String outputDirectory) {
    	List<String> uploadImagePathList = new ArrayList<String>();
        FileOutputStream fileOut = null;
        InputStream inputStream = null;
        ZipFile zipFile = null;
        File file = null;
        int readedBytes;
        try {
 
            //创建输出目录
            File outputDirFile = new File(outputDirectory);
            if (!outputDirFile.exists()) {
                outputDirFile.mkdir();
            }
 
            //这里需要考虑压缩工具是什么，如果是好压的话就需要把它改为UTF-8的编码，如果是WinRAR的话就是GBK编码
            // 然后具体如何判断压缩包是什么压缩的需要参考 http://blog.csdn.net/songylwq/article/details/6139753 这篇文章
            zipFile = new ZipFile(unZipFileName, "GBK");
            //不断遍历zip中的文件
            for (Enumeration entries = zipFile.getEntries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String filename = entry.getName();
                file = new File(outputDirectory + "/" + filename);
                //如果是文件夹先创建
                if (entry.isDirectory()) {
                    file.mkdirs();
                    continue;
                } else { //如果是文件，先查看文件目录是否存在，不存在就创建他
                    File parent = file.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                }
                file.createNewFile(); //创建文件
                // 输出文件
                inputStream = zipFile.getInputStream(entry);
                fileOut = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                while ((readedBytes = inputStream.read(buf)) > 0) {
                    fileOut.write(buf, 0, readedBytes);
                }
                uploadImagePathList.add(file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭文件流
            try {
                if (fileOut != null) {
                    fileOut.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                //关闭zip
                if(zipFile != null) {
                	zipFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return uploadImagePathList;
    }
	
	/**
     * 解析附件zip包
     *
     * @param unZipFileName   解压的zip文件
     * @param outputDirectory 输出目录
     */
    public static void unZip(String unZipFileName, String outputDirectory) {
        FileOutputStream fileOut = null;
        InputStream inputStream = null;
        ZipFile zipFile = null;
        File file = null;
        int readedBytes;
        try {
 
            //创建输出目录
            File outputDirFile = new File(outputDirectory);
            if (!outputDirFile.exists()) {
                outputDirFile.mkdir();
            }
 
            //这里需要考虑压缩工具是什么，如果是好压的话就需要把它改为UTF-8的编码，如果是WinRAR的话就是GBK编码
            // 然后具体如何判断压缩包是什么压缩的需要参考 http://blog.csdn.net/songylwq/article/details/6139753 这篇文章
            zipFile = new ZipFile(unZipFileName, "GBK");
            //不断遍历zip中的文件
            for (Enumeration entries = zipFile.getEntries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String filename = entry.getName();
                file = new File(outputDirectory + "/" + filename);
                //如果是文件夹先创建
                if (entry.isDirectory()) {
                    file.mkdirs();
                    continue;
                } else { //如果是文件，先查看文件目录是否存在，不存在就创建他
                    File parent = file.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                }
                file.createNewFile(); //创建文件
                // 输出文件
                inputStream = zipFile.getInputStream(entry);
                fileOut = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                while ((readedBytes = inputStream.read(buf)) > 0) {
                    fileOut.write(buf, 0, readedBytes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭文件流
            try {
                if (fileOut != null) {
                    fileOut.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析附件rar包
     *
     * @param unRarFileName   解压的zip文件
     * @param outputDirectory 输出目录
     * @return result 成功与否
     */
    public static List<String> unRarAndReturnPath(String  unRarFileName, String outputDirectory) {
    	List<String> uploadImagePathList = new ArrayList<String>();
    	//返回结果
//        Boolean result;
        //生成解压文件
        Archive a = null;
        FileOutputStream fos = null;
        try{
            a = new Archive(new File(unRarFileName));
            FileHeader fh = a.nextFileHeader();
            while(fh!=null){
                if(!fh.isDirectory()){
                    //当前文件的相对路径
                    String compressFileName = fh.getFileNameW().isEmpty()?fh.getFileNameString():fh.getFileNameW();
                    String destFileName = "";
                    //非windows系统 很重要
                    if(File.separator.equals("/")){
                        //当前文件的绝对路径
                        destFileName = outputDirectory + File.separator + compressFileName.replaceAll("\\\\", "/");
                        //windows系统
                    }else{
                        destFileName = outputDirectory + File.separator + compressFileName.replaceAll("/", "\\\\");
                    }
                    //取得母文件夹，如果不存在就创建文件夹
                    File file = new File(destFileName);
                    File parent = file.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                    //解压缩文件
                    fos = new FileOutputStream(new File(destFileName));
                    a.extractFile(fh, fos);
                    //
                    uploadImagePathList.add(file.getAbsolutePath());
                }
                fh = a.nextFileHeader();
            }
//            result = true;
        }catch(Exception e){
            e.printStackTrace();
//            result = false;
            uploadImagePathList = null;
        }finally{
            if(fos!=null){
                try{fos.close();}catch(Exception e){e.printStackTrace();}
            }
            if(a!=null){
                try{a.close();}catch(Exception e){e.printStackTrace();}
            }
        }
        return uploadImagePathList;
    }
    
    /**
     * 解析附件rar包
     *
     * @param unRarFileName   解压的zip文件
     * @param outputDirectory 输出目录
     * @return result 成功与否
     */
    public static boolean unRar(String  unRarFileName, String outputDirectory) {
        //返回结果
        Boolean result;
        //生成解压文件
        Archive a = null;
        FileOutputStream fos = null;
        try{
            a = new Archive(new File(unRarFileName));
            FileHeader fh = a.nextFileHeader();
            while(fh!=null){
                if(!fh.isDirectory()){
                    //当前文件的相对路径
                    String compressFileName = fh.getFileNameW().isEmpty()?fh.getFileNameString():fh.getFileNameW();
                    String destFileName = "";
                    //非windows系统 很重要
                    if(File.separator.equals("/")){
                        //当前文件的绝对路径
                        destFileName = outputDirectory + File.separator + compressFileName.replaceAll("\\\\", "/");
                        //windows系统
                    }else{
                        destFileName = outputDirectory + File.separator + compressFileName.replaceAll("/", "\\\\");
                    }
                    //取得母文件夹，如果不存在就创建文件夹
                    File file = new File(destFileName);
                    File parent = file.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                    //解压缩文件
                    fos = new FileOutputStream(new File(destFileName));
                    a.extractFile(fh, fos);
                }
                fh = a.nextFileHeader();
            }
            result = true;
        }catch(Exception e){
            e.printStackTrace();
            result = false;
        }finally{
            if(fos!=null){
                try{fos.close();}catch(Exception e){e.printStackTrace();}
            }
            if(a!=null){
                try{a.close();}catch(Exception e){e.printStackTrace();}
            }
        }
        return result;
    }

    /**
     * 删除文件  用来删除临时文件
     * @param file
     */
    public static void deleteFile(File file){
    	if(file.exists()) {
    		file.delete();
    	}
    }
}
