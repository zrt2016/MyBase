package com.zrt.mybase.zpd.scan;

import android.view.KeyEvent;

public abstract interface IScanner
{
  public abstract void addListener(IScanListener paramIScanListener);

  public abstract boolean dispatchKeyEvent(KeyEvent paramKeyEvent);

  public abstract int getKeyCode(KeyEvent paramKeyEvent);

  public abstract String getScanResult();

  public abstract int scanExit();

  public abstract void startScan();

  public abstract void stopScan();
  
  public abstract int scanExitAll();
}