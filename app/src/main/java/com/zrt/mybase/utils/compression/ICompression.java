package com.zrt.mybase.utils.compression;

public abstract interface ICompression
{
  public abstract byte[] compress(String from, byte[] paramArrayOfByte);

  public abstract String decompress(String from, byte[] paramArrayOfByte);
  
//  public abstract String unGZip(byte[] paramArrayOfByte);
//  
//  public abstract String decompress2(byte[] paramArrayOfByte);
  
}