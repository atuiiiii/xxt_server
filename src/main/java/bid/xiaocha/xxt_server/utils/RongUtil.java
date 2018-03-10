package bid.xiaocha.xxt_server.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;


public class RongUtil {
	final static String APPKEY = "RC-App-Key";
	final static String NONCE = "RC-Nonce";
	final static String TIMESTAMP = "RC-Timestamp";
	final static String SIGNATURE = "RC-Signature";
	String charset = "utf-8";
	public static String getToken(String userId,String nickName,String headUri) {
		String nonce = String.valueOf(Math.random() * 1000000);
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		StringBuilder toSign = new StringBuilder("2L50ViTZuhEY").append(nonce)
				.append(timestamp);
		String sign = CodeUtil.hexSHA1(toSign.toString());
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(APPKEY, "mgb7ka1nmfhvg");
		headers.put(NONCE, nonce);
		headers.put(TIMESTAMP, timestamp);
		headers.put(SIGNATURE, sign);
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		String url = "http://api.cn.ronghub.com/user/getToken.json";
		JSONObject json = new JSONObject();
		Map<String, String> content = new HashMap<String, String>();
		content.put("userId", userId);
		content.put("name", nickName);
		content.put("portraitUri", headUri);
		Map<String, String> map = new HashMap<String, String>();
		String tokenResult = HttpClientUtil.post(url, content, headers);
		return tokenResult;
	}
}
