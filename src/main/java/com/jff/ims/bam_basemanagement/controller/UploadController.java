package com.jff.ims.bam_basemanagement.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.jff.ims.bam_basemanagement.vo.ResponseEntity;
import com.jff.ims.constant.CommonConstant;
import com.jff.ims.util.ZipUtil;

//告诉spring mvc这是一个控制器类
@Controller
@RequestMapping("upload")
public class UploadController {
	
	private Log logger = LogFactory.getLog(this.getClass());
	private static final String [] extensionPermit = {"rar", "zip"};
	
	private static final String uploadPackageLocation = "/uploadPackage/";//zip、rar上传压缩包存放地
	private static final String uploadStorePicturesLocation = "/uploadStorePictures";//上传商城的相关图
	private static final String homepage = "/homepage/";//首页的图片
	private static final String commodity = "/commodity/";//商品的图--以产品编号分组，包含：产品封面图、产品展示的图组和详情图组
	private static final String cover = "/cover/";//商品的封面图
	private static final String display = "/display/";//商品展示的图组
	private static final String detail = "/detail/";//商品详情的图组
	private static final String others = "/others/";//除以上三种产品图之外的图片集合
	
	private static final String uploadPic = "/uploadPic/";//上传的图片
	private static final String uploadVideo = "/uploadVideo/";//上传的图片
	private static final String zip = "zip";//zip上传压缩包存放地
	private static final String rar = "rar";//rar上传压缩包存放地
	
	
	@RequestMapping("/uploadPic")
    public @ResponseBody ResponseEntity uploadPic(HttpServletRequest request, MultipartFile file)
            throws IllegalStateException, IOException {
		ResponseEntity responseEntity = new ResponseEntity();
		Date date = new Date();
		String strDate = new SimpleDateFormat("yyyyMMdd").format(date);
		String strDateTime = new SimpleDateFormat("HHmmss").format(date);
		
		String curProjectPath = request.getServletContext().getRealPath("/");
		String userPhone = request.getParameter("phone");
		
		String uploadFileAddress = curProjectPath  + uploadPic;
		
		String fileName = file.getOriginalFilename();
		
		String newFileName = FilenameUtils.getBaseName(fileName)+"-"+strDate+strDateTime+"."+FilenameUtils.getExtension(fileName);
		
		try {
			File newFile = new File(uploadFileAddress, newFileName);
			/**
			 * 没有则新建目录
			 */
			if (!newFile.getParentFile().exists()) {
				newFile.getParentFile().mkdirs();
			}
			if (!newFile.exists()) {
				newFile.mkdirs();
			}
	        file.transferTo(newFile);
	        Map<String, Object> data = new HashMap<String, Object>();
	        String uploadImagePath = newFile.getAbsolutePath();
	        data.put("uploadImagePath",uploadImagePath.substring(uploadImagePath.indexOf("ims")));
	        responseEntity.setData(data);
		} catch (Exception e) {
			responseEntity.setSuccess(CommonConstant.RESPONSE_FAIL);
			responseEntity.setErrorMsg(e.getMessage());
			e.printStackTrace();
		} 
		return responseEntity;
    }
	
	@RequestMapping("/uploadVideo")
    public @ResponseBody ResponseEntity uploadVideo(HttpServletRequest request, MultipartFile file)
            throws IllegalStateException, IOException {
		ResponseEntity responseEntity = new ResponseEntity();
		Date date = new Date();
		String strDate = new SimpleDateFormat("yyyyMMdd").format(date);
		String strDateTime = new SimpleDateFormat("HHmmss").format(date);
		
		String curProjectPath = request.getServletContext().getRealPath("/");
		
		String uploadFileAddress = curProjectPath  + uploadVideo;
		
		String fileName = file.getOriginalFilename();
		
		String newFileName = FilenameUtils.getBaseName(fileName)+"-"+strDate+strDateTime+"."+FilenameUtils.getExtension(fileName);
		
		try {
			File newFile = new File(uploadFileAddress, newFileName);
			/**
			 * 没有则新建目录
			 */
			if (!newFile.getParentFile().exists()) {
				newFile.getParentFile().mkdirs();
			}
			if (!newFile.exists()) {
				newFile.mkdirs();
			}
	        file.transferTo(newFile);
	        Map<String, Object> data = new HashMap<String, Object>();
	        String uploadImagePath = newFile.getAbsolutePath();
	        data.put("uploadVideoPath",uploadImagePath.substring(uploadImagePath.indexOf("ims")));
	        responseEntity.setData(data);
		} catch (Exception e) {
			responseEntity.setSuccess(CommonConstant.RESPONSE_FAIL);
			responseEntity.setErrorMsg(e.getMessage());
			e.printStackTrace();
		} 
		return responseEntity;
    }
	
	@RequestMapping("/uploadZip")
    public @ResponseBody ResponseEntity uploadZip(HttpServletRequest request, @RequestParam("file") MultipartFile file)
            throws IllegalStateException, IOException {
		ResponseEntity responseEntity = new ResponseEntity();
		
		String curProjectPath = request.getServletContext().getRealPath("/");
		String surname = request.getParameter("surname");
		String familyIdAndFamilyName = request.getParameter("familyIdAndFamilyName");
		String volumeId = request.getParameter("volumeId");
		Date date = new Date();
		String strDate = new SimpleDateFormat("yyyyMMdd").format(date);
		String strDateTime = new SimpleDateFormat("HHmmss").format(date);
		//上传压缩文件地址
		String savePackagePath = curProjectPath + uploadPackageLocation;
		/**
		 * 没有则新建目录
		 */
		File savePackageDirectory = new File(savePackagePath);
		if (!savePackageDirectory.exists()) {
			savePackageDirectory.mkdirs();
		}
		
		//解压后的图片文件地址
		String saveClanPicturePath = curProjectPath + uploadStorePicturesLocation 
				+ surname + "/" + familyIdAndFamilyName + "/" +volumeId+"-"+ strDate+strDateTime;
		
		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			String newFileName = FilenameUtils.getBaseName(fileName)+"-"+strDate+strDateTime+"."+FilenameUtils.getExtension(fileName);
			
			String fileExtension = FilenameUtils.getExtension(fileName);
			if(!ArrayUtils.contains(extensionPermit, fileExtension)) {
				//throw new NoSupportExtensionException("No Support extension.");
				return null;
			}
			//下面的方法用于上传文件
			File packageFile = new File(savePackageDirectory, newFileName);
			if (!packageFile.isDirectory())
				packageFile.createNewFile();
			//解压缩,上传的压缩包存放在saveClanPicturePath目录下，解压后的文件存在saveClanPicturePath目录下
			try {
				file.transferTo(packageFile);
				List<String> uploadImagePathList = new ArrayList<String>();
				if(fileExtension.equalsIgnoreCase(zip)) {
					uploadImagePathList = ZipUtil.unZipAndReturnPath(savePackagePath + newFileName,saveClanPicturePath);
				}
				if(fileExtension.equalsIgnoreCase(rar)) {
					uploadImagePathList = ZipUtil.unRarAndReturnPath(savePackagePath + newFileName,saveClanPicturePath);
				}
				// 将返回的图片地址列表保存
//				clanPictureService.addClanPictureByVolumeIdAndAddress(Integer.valueOf(volumeId), uploadImagePathList);

			} catch (Exception e) {
				responseEntity.setSuccess(CommonConstant.RESPONSE_FAIL);
				responseEntity.setErrorMsg(e.getMessage());
				e.printStackTrace();
			} finally {
//				packageFile.deleteOnExit();
				if(fileExtension.equalsIgnoreCase(zip)) {
					ZipUtil.deleteFile(packageFile);
				}
				if(fileExtension.equalsIgnoreCase(rar)) {
					
				}
			}
		}
		logger.info("UploadController#uploadZip() end");
    	return responseEntity;
	}
	
	
//	@RequestMapping("/uploadImage")
//    public ModelAndView uploadImage(HttpServletRequest request, UploadedImageFile file)
//            throws IllegalStateException, IOException {
//        String name = RandomStringUtils.randomAlphanumeric(10);
//        String newFileName = name + ".jpg";
//        File newFile = new File(request.getServletContext().getRealPath("/image"), newFileName);
//        newFile.getParentFile().mkdirs();
//        file.getImage().transferTo(newFile);
// 
//        ModelAndView mav = new ModelAndView("showUploadedFile");
//        mav.addObject("imageName", newFileName);
//        return mav;
//    }
	
}
