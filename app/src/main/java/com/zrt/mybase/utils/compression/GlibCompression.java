package com.zrt.mybase.utils.compression;

import com.zrt.mybase.utils.MyLogger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class GlibCompression implements ICompression {

    @Override
//	public byte[] compress(byte[] data) {
//		byte[] output = new byte[0];
//
//		Deflater compresser = new Deflater();
//
//		compresser.reset();
//		compresser.setInput(data);
//		compresser.finish();
//		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
//		try {
//			byte[] buf = new byte[1024];
//			while (!compresser.finished()) {
//				int i = compresser.deflate(buf);
//				bos.write(buf, 0, i);
//			}
//			output = bos.toByteArray();
//		} catch (Exception e) {
//			output = data;
//			e.printStackTrace();
//		} finally {
//			try {
//				bos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		compresser.end();
//		return output;
//	}
//
//	@Override
//	public String decompress(byte[] data) {
//		byte[] output = new byte[0];
//
//		Inflater decompresser = new Inflater();
//		decompresser.reset();
//		decompresser.setInput(data);
//
//		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
//		try {
//			byte[] buf = new byte[1024];
//			while (!decompresser.finished()) {
//				int i = decompresser.inflate(buf);
//				o.write(buf, 0, i);
//			}
//			output = o.toByteArray();
//		} catch (Exception e) {
//			output = data;
//			e.printStackTrace();
//		} finally {
//			try {
//				o.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		decompresser.end();
//
//		return new String(output);
//	}

    public byte[] compress(String from, byte[] paramArrayOfByte) {
        byte[] compressed = null;
        ByteArrayOutputStream bos = null;
        Deflater compressor = null;
        byte[] buf = null;
        try {
            bos = new ByteArrayOutputStream(paramArrayOfByte.length);
            compressor = new Deflater();
            compressor.setLevel(Deflater.BEST_COMPRESSION); // 将当前压缩级别设置为指定值。
            compressor.setInput(paramArrayOfByte, 0, paramArrayOfByte.length);
            compressor.finish(); // 调用时，指示压缩应当以输入缓冲区的当前内容结尾。

            // Compress the data
            buf = new byte[1024];
            while (!compressor.finished()) {
                int count = compressor.deflate(buf);
                bos.write(buf, 0, count);
            }
            compressor.end(); // 关闭解压缩器并放弃所有未处理的输入。
            compressor = null;
            compressed = bos.toByteArray();
            buf = null;
        } catch (Exception e) {
            e.printStackTrace();
            MyLogger.Log().e("===>"+from+"===>"+e.getMessage());
            MyLogger.Log().e(new String(paramArrayOfByte));
        } finally{
            try {
                if (bos != null){
                    bos.close();
                    bos = null;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // System.gc();
        }
        //Log.i("", "##压缩成功：压缩前大小："+paramArrayOfByte.length+"; 压缩后大小："+compressed.length);
        return compressed;
    }

    public java.lang.String decompress(String from, byte[] paramArrayOfByte){

        //byte[] unCompressed = null;
        ByteArrayOutputStream bos = null;
        BufferedOutputStream osw = null;
        OutputStreamWriter oos = null;
        Inflater decompressor = null;
        byte[] buf = null;
        try {
            decompressor = new Inflater();
            bos = new ByteArrayOutputStream(paramArrayOfByte.length);
            osw = new BufferedOutputStream(bos, 1024);
            oos = new OutputStreamWriter(osw, "utf-8");
            decompressor.setInput(paramArrayOfByte);
            buf = new byte[1024];
            while (!decompressor.finished()) {
                int count = decompressor.inflate(buf);
//                bos.write(buf, 0, count);  //TODO
                oos.write(new String(buf, 0, count));
            }
            oos.flush();
//            unCompressed = bos.toByteArray(); 
            return bos.toString();
//            return bos.toString("utf-8");
//            return new String(unCompressed, "utf-8");//TODO
        } catch (Exception e) {
            e.printStackTrace();
            MyLogger.Log().e("===>"+from+"===>"+e.getMessage());
            MyLogger.Log().e(new String(paramArrayOfByte));
        }
        finally {
            decompressor.end();
            decompressor = null;
            try {
                if (bos != null){
                    bos.close();
                    bos = null;
                }
                if (osw != null){
                    osw.close();
                    osw = null;
                }
                if (oos != null){
                    oos.close();
                    oos = null;
                }
                buf = null;
            } catch (IOException e) {
                e.printStackTrace();
            }

            // System.gc();
        }
        // String test = bos.toString();  
        // return unCompressed;
        return null;

    }

//	public java.lang.String decompress(byte[] paramArrayOfByte){
//		
//		byte[] unCompressed = null;  
//        ByteArrayOutputStream bos = new ByteArrayOutputStream(paramArrayOfByte.length);  
//        Inflater decompressor = new Inflater();  
//        try {  
//            decompressor.setInput(paramArrayOfByte);  
//            final byte[] buf = new byte[1024];  
//            while (!decompressor.finished()) {  
//                int count = decompressor.inflate(buf);  
//                bos.write(buf, 0, count);
//            }  
//  
//            unCompressed = bos.toByteArray();  
//            bos.close();  
//            
//            return new String(unCompressed, "utf-8");
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }
//        finally {  
//            decompressor.end();  
//        } 
//        // String test = bos.toString();  
//        // return unCompressed;
//        return null;
//		
//	}

//	public java.lang.String decompress2(byte[] compress){
//		
//		ByteArrayInputStream bais = new ByteArrayInputStream(compress);
//		InflaterInputStream iis = new InflaterInputStream(bais);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//		try { 
//			int c = 0;
//			byte[] buf = new byte[1024];
//			while (true) {
//				c = iis.read(buf);
//				if (c == -1)
//					break;
//				baos.write(buf, 0, c);
//			}
//			baos.flush();
//			return new String(baos.toByteArray(), "utf-8");
//		}catch (Exception e) {  
//            e.printStackTrace();  
//        }
//		// return baos.toByteArray();
//		return null;
//	}
//	
//	
//	public java.lang.String unGZip(byte[] bContent)  
//	{  
//	    byte[] data = new byte[1024];  
//	    try  
//	    {  
//	        ByteArrayInputStream in = new ByteArrayInputStream(bContent);  
//	        GZIPInputStream pIn = new GZIPInputStream(in);  
//	        DataInputStream objIn = new DataInputStream(pIn);  
//
//	        int len = 0;  
//	        int count = 0;  
//	        while ((count = objIn.read(data, len, len + 1024)) != -1)  
//	        {  
//	            len = len + count;  
//	        }  
//
//	        byte[] trueData = new byte[len];  
//	        System.arraycopy(data, 0, trueData, 0, len);  
//
//	        objIn.close();  
//	        pIn.close();  
//	        in.close();  
//
//	        return new String(trueData, "utf-8");
//	        // return trueData;  
//
//	    }  
//	    catch (Exception e)  
//	    {  
//	        e.printStackTrace();  
//	    }
//	    return null;
//	} 
}