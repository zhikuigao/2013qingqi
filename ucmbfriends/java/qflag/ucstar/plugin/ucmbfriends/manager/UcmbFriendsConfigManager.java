package qflag.ucstar.plugin.ucmbfriends.manager;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.jivesoftware.util.Log;
import org.jivesoftware.wildfire.XMPPServer;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class UcmbFriendsConfigManager {
	private static UcmbFriendsConfigManager instance = null;
	
	private UcmbFriendsConfigManager(){
		_init();
	}
	public void _init(){
	}
	
	public static UcmbFriendsConfigManager getInstance(){
		if(instance == null){
			synchronized (UcmbFriendsConfigManager.class) {
				if(instance == null){
					instance = new UcmbFriendsConfigManager();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 获取上传目录
	 * @return
	 */
	public String getUploadPath() {
		String uploadPath = getBaseUploadPath();
		return uploadPath;
	}
	
	public String getBaseConfPath() {
		File theWebFolder = XMPPServer.getInstance().getAdminFolder();
		String path = theWebFolder.getAbsolutePath() + File.separator + "ucstar_plugins/ucmbfriends/conf/";
		
		return path;
	}
	
	public String getBaseUploadPath() {
		File theWebFolder = XMPPServer.getInstance().getAdminFolder();
		String path = theWebFolder.getAbsolutePath() + File.separator + "ucstar_plugins/ucmbfriends/upload/";
		
		return path;
	}
	
	
	/**
	 * 生成缩略图
	 */
	public boolean getImagesmall(String fileId,String pref){
		FileOutputStream fileOutputStream = null;
		JPEGImageEncoder encoder = null;
		BufferedImage tagImage = null;
		Image srcImage = null;
		boolean isTrue = true;
		String path = getUploadPath() + fileId;
		//组装小图文件名字
		String dstImageFileName = path + "_small."+pref;
		//大图位置
		File imBig = new File(path + "."+pref);
		if(imBig.exists() && imBig.isFile()) {
			try {
				srcImage = ImageIO.read(imBig);
				int srcWidth = srcImage.getWidth(null);// 原图片宽度
				int srcHeight = srcImage.getHeight(null);// 原图片高度
				int dstMaxSize = 120;// 目标缩略图的最大宽度/高度，宽度与高度将按比例缩写
				int dstWidth = srcWidth;// 缩略图宽度
				int dstHeight = srcHeight;// 缩略图高度
				float scale = 0;
				 if(srcWidth>dstMaxSize){
		                dstWidth = dstMaxSize;
		                scale = (float)srcWidth/(float)dstMaxSize;
		                dstHeight = Math.round((float)srcHeight/scale);
		            }
		            srcHeight = dstHeight;
		            if(srcHeight>dstMaxSize){
		                dstHeight = dstMaxSize;
		                scale = (float)srcHeight/(float)dstMaxSize;
		                dstWidth = Math.round((float)dstWidth/scale);
		            }
		            //生成缩略图
		            tagImage = new BufferedImage(dstWidth,dstHeight,BufferedImage.TYPE_INT_RGB);
		            tagImage.getGraphics().drawImage(srcImage,0,0,dstWidth,dstHeight,null);
		            fileOutputStream = new FileOutputStream(dstImageFileName);
		            encoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
		            encoder.encode(tagImage);
		            fileOutputStream.close();
		            fileOutputStream = null;
		            if(fileOutputStream!=null){
		                try{
		                    fileOutputStream.close();
		                }catch(Exception e){
		                }
		                fileOutputStream = null;
		            }
		            encoder = null;
		            tagImage = null;
		            srcImage = null;
		            isTrue = false;
		            System.gc();
			} catch (Exception e) {
				Log.error("【工作圈:生成缩略图】<<<<<<<<<<<<<<<<<"+e);
			}finally{
			}
		}
		return isTrue;
	}
	
	
	//验证图片格式
	public boolean isValid(String pref){
		boolean isTrue = true;
		String[] fomat = {"jpg","JPG","png","PNG","jpeg","JPEG"};
		for(int i = 0; i<fomat.length;i++){
			if(fomat[i].equals(pref)){
				isTrue = false;
			}
		}
		return isTrue;
	}
	
	
	
}
