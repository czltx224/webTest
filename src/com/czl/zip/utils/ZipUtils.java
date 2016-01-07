package com.czl.zip.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.lang3.StringUtils;

/**
 * zip文件，压缩与解压缩工具类 <code>ZipUtils.java</code>
 * <p>
 * <p>
 * Copyright 2015 All right reserved.
 * 
 * @version 1.0 </br>最后修改人 无
 */
public class ZipUtils
{
	
	private static final String UTF_8 = "UTF-8";
	
	/**
	 * 对文件或文件目录进行压缩
	 * 
	 * @param srcPath
	 *        要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath
	 *        压缩文件保存的路径。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param zipFileName
	 *        压缩文件名，需要带后辍。如:test.zip
	 * @throws Exception
	 */
	public static void zip(String srcPath, String zipPath, String zipFileName) throws Exception
	{
		if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(zipPath) || StringUtils.isEmpty(zipFileName))
		{
			throw new IllegalArgumentException("Invalid argument . srcPath = " + srcPath + ",zipPath=" + zipPath
					+ ",zipFileName=" + zipFileName);
		}
		
		CheckedOutputStream cos = null;
		ZipOutputStream zos = null;
		try
		{
			File srcFile = new File(srcPath);
			
			// 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
			if (srcFile.isDirectory() && zipPath.indexOf(srcPath) != -1)
			{
				throw new IllegalArgumentException("zipPath must not be the child directory of srcPath.");
			}
			
			// 判断压缩文件保存的路径是否存在，如果不存在，则创建目录
			File zipDir = new File(zipPath);
			if (!zipDir.exists() || !zipDir.isDirectory())
			{
				zipDir.mkdirs();
			}
			
			// 创建压缩文件保存的文件对象
			String zipFilePath = zipPath + File.separator + zipFileName;
			File zipFile = new File(zipFilePath);
			if (zipFile.exists())
			{
				// 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
				SecurityManager securityManager = new SecurityManager();
				securityManager.checkDelete(zipFilePath);
				// 删除已存在的目标文件
				zipFile.delete();
			}
			
			cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
			zos = new ZipOutputStream(cos);
			
			// 如果只是压缩一个文件，则需要截取该文件的父目录
			String srcRootDir = srcPath;
			if (srcFile.isFile())
			{
				int index = srcPath.lastIndexOf(File.separator);
				if (index != -1)
				{
					srcRootDir = srcPath.substring(0, index);
				}
			}
			// 调用递归压缩方法进行目录或文件压缩
			zip(srcRootDir, srcFile, zos);
			zos.flush();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if (zos != null)
				{
					zos.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 递归压缩文件夹
	 * 
	 * @param srcRootDir
	 *        压缩文件夹根目录的子路径
	 * @param file
	 *        当前递归压缩的文件或目录对象
	 * @param zos
	 *        压缩文件存储对象
	 * @throws Exception
	 */
	private static void zip(String srcRootDir, File file, ZipOutputStream zos) throws Exception
	{
		if (file == null)
		{
			return;
		}
		
		// 如果是文件，则直接压缩该文件
		if (file.isFile())
		{
			int count, bufferLen = 1024;
			byte data[] = new byte[bufferLen];
			
			// 获取文件相对于压缩文件夹根目录的子路径
			String subPath = file.getAbsolutePath();
			int index = subPath.indexOf(srcRootDir);
			if (index != -1)
			{
				subPath = subPath.substring(srcRootDir.length() + File.separator.length());
			}
			ZipEntry entry = new ZipEntry(subPath);
			zos.putNextEntry(entry);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			while ((count = bis.read(data, 0, bufferLen)) != -1)
			{
				zos.write(data, 0, count);
			}
			bis.close();
			zos.closeEntry();
		}
		// 如果是目录，则压缩整个目录
		else
		{
			// 压缩目录中的文件或子目录
			File[] childFileList = file.listFiles();
			for (int n = 0; n < childFileList.length; n++)
			{
				childFileList[n].getAbsolutePath().indexOf(file.getAbsolutePath());
				zip(srcRootDir, childFileList[n], zos);
			}
		}
	}
	
	/**
	 * 把文件压缩成zip格式
	 * 
	 * @param files
	 *        需要压缩的文件
	 * @param zipFilePath
	 *        压缩后的zip文件路径 ,如"D:/test/aa.zip";
	 */
	public static void zip(File[] files, String zipFilePath)
	{
		if (files != null && files.length > 0)
		{
			if (isEndsWithZip(zipFilePath))
			{
				ZipArchiveOutputStream zaos = null;
				try
				{
					File zipFile = new File(zipFilePath);
					zaos = new ZipArchiveOutputStream(zipFile);
					// Use Zip64 extensions for all entries where they are
					// required
					zaos.setUseZip64(Zip64Mode.AsNeeded);
					
					// 将每个文件用ZipArchiveEntry封装
					// 再用ZipArchiveOutputStream写到压缩文件中
					for (File file : files)
					{
						if (file != null)
						{
							ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
							zaos.putArchiveEntry(zipArchiveEntry);
							InputStream is = null;
							try
							{
								is = new BufferedInputStream(new FileInputStream(file));
								byte[] buffer = new byte[1024 * 5];
								int len = -1;
								while ((len = is.read(buffer)) != -1)
								{
									// 把缓冲区的字节写入到ZipArchiveEntry
									zaos.write(buffer, 0, len);
								}
								// Writes all necessary data for this entry.
								zaos.closeArchiveEntry();
							}
							catch (Exception e)
							{
								throw new RuntimeException(e);
							}
							finally
							{
								if (is != null)
									is.close();
							}
							
						}
					}
					zaos.finish();
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
				finally
				{
					try
					{
						if (zaos != null)
						{
							zaos.close();
						}
					}
					catch (IOException e)
					{
						throw new RuntimeException(e);
					}
				}
				
			}
			
		}
		
	}
	
	/**
	 * 把zip文件解压到指定的文件夹
	 * 
	 * @param zipFilePath
	 *        zip文件路径, 如 "D:/test/aa.zip"
	 * @param saveFileDir
	 *        解压后的文件存放路径, 如"D:/test/"
	 */
	public static void unZip(String zipFilePath, String saveFileDir)
	{
		if (isEndsWithZip(zipFilePath))
		{
			File file = new File(zipFilePath);
			if (file.exists())
			{
				InputStream is = null;
				// can read Zip archives
				ZipArchiveInputStream zais = null;
				try
				{
					is = new FileInputStream(file);
					
					// 设置zip编码，处理中文路径乱码问题
					zais = new ZipArchiveInputStream(is, System.getProperty("sun.jnu.encoding"));
					
					ArchiveEntry archiveEntry = null;
					// 把zip包中的每个文件读取出来
					// 然后把文件写到指定的文件夹
					while ((archiveEntry = zais.getNextEntry()) != null)
					{
						// 获取文件名
						String entryFileName = archiveEntry.getName();
						// 构造解压出来的文件存放路径
						String entryFilePath = saveFileDir + entryFileName;
						entryFilePath = entryFilePath.replaceAll("\\\\", "/");
						entryFilePath = entryFilePath.replaceAll("\\/\\/", "/");
						// 判断路径是否存在,不存在则创建文件路径
						File outPathFile = new File(entryFilePath.substring(0, entryFilePath.lastIndexOf("/")));
						if (!outPathFile.exists())
						{
							outPathFile.mkdirs();
						}
						// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
						if (new File(entryFilePath).isDirectory())
						{
							continue;
						}
						
						byte[] content = new byte[1024];
						int num = 0;
						OutputStream os = null;
						FileOutputStream fileOutputStream = null;
						try
						{
							File entryFile = new File(entryFilePath);
							fileOutputStream = new FileOutputStream(entryFile);
							os = new BufferedOutputStream(fileOutputStream);
							while ((num = zais.read(content)) != -1)
							{
								os.write(content, 0, num);
							}
						}
						catch (IOException e)
						{
							throw new IOException(e);
						}
						finally
						{
							if (os != null)
							{
								os.flush();
								os.close();
							}
							
							if (null != fileOutputStream)
							{
								fileOutputStream.flush();
								fileOutputStream.close();
							}
						}
						
					}
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
				finally
				{
					try
					{
						if (zais != null)
						{
							zais.close();
						}
						if (is != null)
						{
							is.close();
						}
					}
					catch (IOException e)
					{
						throw new RuntimeException(e);
					}
				}
			}
		}
	}
	
	/**
	 * 判断文件名是否以.zip为后缀
	 * 
	 * @param fileName
	 *        需要判断的文件名
	 * @return 是zip文件返回true,否则返回false
	 */
	public static boolean isEndsWithZip(String fileName)
	{
		boolean flag = false;
		if (fileName != null && !"".equals(fileName.trim()))
		{
			if (fileName.endsWith(".ZIP") || fileName.endsWith(".zip"))
			{
				flag = true;
			}
		}
		return flag;
	}
	
	public static void main(String[] args)
	{
		
		String unzipFilePath = "G:\\DATA\\广州市站工程案例test\\";
		String zipFilePath = "G:\\DATA\\广州市站工程案例.zip";
		try
		{
			unZip(zipFilePath, unzipFilePath);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
