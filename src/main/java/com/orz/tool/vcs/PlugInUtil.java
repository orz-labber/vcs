package com.orz.tool.vcs;

public class PlugInUtil {
	
	private PlugInUtil(){
		
	}
	

	public static String join(String[] array, String split) {
		StringBuffer result = new StringBuffer(20);
		for (int i = 0; i < array.length; i++) {
			String item = array[i];
			result.append(split).append(item);
		}
		if (result.length() != 0) {
			return result.toString().substring(split.length());
		} else {
			return result.toString();
		}
	}

}
