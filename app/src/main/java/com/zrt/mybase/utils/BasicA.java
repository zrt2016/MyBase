package com.zrt.mybase.utils;

public class BasicA extends DialogAbstract implements DialogInterface{

	@Override
	public void inA() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inB() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void absB() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void absA() {
		// TODO Auto-generated method stub
		super.absA();
	}
	
	public static void main(String[] args) {
		new DialogAbstract() {
			
			@Override
			public void absB() {
				// TODO Auto-generated method stub
				
			}
		};
	}

}
