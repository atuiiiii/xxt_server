package bid.xiaocha.xxt_server.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import bid.xiaocha.xxt_server.entities.UserEntity;

public class CommonUtils {
	public static final int  NumInAPage = 10;
	
    public static String getOddNum(String userId,Date date){
        final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        return df.format(date) + userId.substring(7, 11);
    }
    public static void copyFile(File fromFile,File toFile) throws IOException {
    	FileOutputStream out = new FileOutputStream(toFile);
    	FileInputStream in = new FileInputStream(fromFile);
    	byte[] reader = new byte[1024];
    	if (!toFile.exists()) {
			toFile.createNewFile();
		}
    	int n = 0;
    	while((n=in.read(reader))!=-1) {
    		out.write(reader, 0, n);
    	}
    	out.flush();
    	out.close();
    	in.close();
    }
    public static void copyMultipartFile(MultipartFile fromFile,File toFile) throws IOException {
    	FileOutputStream out = new FileOutputStream(toFile);
    	out.write(fromFile.getBytes());
    	out.flush();
    	out.close();
    }
}
