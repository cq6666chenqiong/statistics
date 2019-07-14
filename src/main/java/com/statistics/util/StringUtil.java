package com.statistics.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialException;

/**
 * 
 * @author chenqiong
 *
 */
public class StringUtil {

	public static String getString(Object obj){
    	if(obj!=null){
    		return obj.toString();
    	}else{
    		return "";
    	}
    }
	
	public static boolean isEmpty(String str) {
        boolean tag = false;
        if(str == null || str.equals("")){
            tag = true;
        }
        return tag;
    }
	
	public static String ClobToString(Clob clob) throws SQLException, IOException {
        Reader is = clob.getCharacterStream();
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {
            sb.append(s);
            s = br.readLine();
        }
        String reString = sb.toString();
        return reString;
    }
	
	public static Clob StringToClob(String str) throws SerialException, SQLException {
		if(str == null) {
			str = "";
		}
		Clob   c   =  new javax.sql.rowset.serial.SerialClob(str.toCharArray());
		return c;		
	}
	
}

