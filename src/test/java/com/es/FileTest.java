package com.es;



import java.io.*;
import java.util.Base64;

/**
 * @author ming.li
 * @date 2022/11/16 16:36
 */
public class FileTest {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Users\\ken\\Desktop\\ss.txt"))));
        StringBuffer buffer=new StringBuffer("");
        String s=null;
        while ((s=reader.readLine())!=null){
            buffer=buffer.append(s);

        }
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(buffer.toString());
        FileOutputStream stream = new FileOutputStream("C:\\Users\\ken\\Desktop\\5.zip");
        stream.write(decode);
        stream.close();
    }
}
