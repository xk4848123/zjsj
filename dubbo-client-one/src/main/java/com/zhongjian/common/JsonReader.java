
package com.zhongjian.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletInputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

public class JsonReader {
	private static Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<TreeMap<String, Object>>() {
	}.getType(), new JsonDeserializer<TreeMap<String, Object>>() {
		@Override
		public TreeMap<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {

			TreeMap<String, Object> treeMap = new TreeMap<>();
			JsonObject jsonObject = json.getAsJsonObject();
			Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
			for (Map.Entry<String, JsonElement> entry : entrySet) {
				treeMap.put(entry.getKey(), entry.getValue());
			}
			return treeMap;
		}
	}).create();

	public static Map<String, JsonPrimitive> receivePost(ServletInputStream inputStream)
			throws UnsupportedEncodingException, IOException {
		// 读取请求内容
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		// 将json字符串转换为json对象
		TreeMap<String, JsonPrimitive> jsonMap =
		         gson.fromJson(sb.toString(), new TypeToken<TreeMap<String, JsonPrimitive>>(){}.getType());
		return jsonMap;
	}
}