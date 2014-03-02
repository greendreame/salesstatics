package com.tjcuxulin.salesstatic.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.graphics.Paint;
import android.os.Environment;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class SalesUtil {

	/**
	 * 
	 * @param src
	 * @return String
	 */
	public static String getPinYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		// Set the out format
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// whether is a zh-rcn
				if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// save
																			// all
																			// possiable
																			// result
					t4 += t2[0];
				} else {
					t4 += Character.toString(t1[i]);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t4;
	}

	/**
	 * get head char
	 * 
	 * @param str
	 * @return String
	 */
	public static String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}

	/**
	 * to ascii
	 * 
	 * @param cnStr
	 * @return String
	 */
	public static String getCnASCII(String cnStr) {
		StringBuffer strBuf = new StringBuffer();
		byte[] bGBK = cnStr.getBytes();
		for (int i = 0; i < bGBK.length; i++) {
			strBuf.append(Integer.toHexString(bGBK[i] & 0xff) + " ");
		}
		return strBuf.toString();
	}

	public static String getDateFormatString(String pattern, long timeStamp) {
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
		return format.format(new Date(timeStamp));
	}

	public static boolean isFileExists(String fileName) {
		File file = new File(Environment.getExternalStorageDirectory(),
				fileName);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static boolean saveStringToFile(String pathString, String fileName,
			String string, boolean append) {
		File path = new File(pathString);
		if (!path.exists()) {
			path.mkdirs();
		}

		File file = new File(pathString, fileName);

		try {
			if (append == false && file.exists()) {
				file.delete();
				file.createNewFile();
			}

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(file, append), "utf-8");
			writer.write(string);
			writer.flush();
			writer.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static boolean saveStringToFile(String fileName, String string,
			boolean append) {
		return saveStringToFile(Environment.getExternalStorageDirectory()
				.getAbsolutePath(), fileName, string, append);
	}
	
	public static String getDataFileAblutePath(String fileName) {
		return Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + fileName;
	}
	
	public static int GetPixelByText(float textSize, String text) {  
        Paint mTextPaint = new Paint();  
        mTextPaint.setTextSize(textSize);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setAntiAlias(true);
  
        return (int) (mTextPaint.measureText(text) + textSize);  
    }
}
