package com.zrt.mybase.utils;

/**
 * 备注一些修改方法
 */
public class Remark {
                // 高危药品显示圆圈横向大小
//				if("红河州第三人民医院".equals(Constants.APP_REGION) && tagAry[i].length()>=2){
//					width = (int) roundTextView.getPaint().measureText(tagAry[i]) + 10;
//					if (width > flowLayout.getWidth()){
//						height = (int) Math.ceil((double)width / flowLayout.getWidth()) * size;
//						width = flowLayout.getWidth();
//					}
//				}
    /**
     * 动态添加标记控件
     */
//	public static void dynamicAddFlagView(Context context, FlowLayout flowLayout, String app_show_tag_list, String app_show_color_list){
//		if(flowLayout == null){
//			return;
//		}
//		if (flowLayout.getChildCount() > 0) {
//			flowLayout.removeAllViews();
//		}
//		int size = DensityUtils.dip2px(context, 20);
//		float radius = DensityUtils.dip2px(context, 90);
//
//		if(!TextUtils.isEmpty(app_show_tag_list)){
//			String[] tagAry = app_show_tag_list.split(",");
//			String[] colorAry = null;
//			if(!TextUtils.isEmpty(app_show_color_list)){
//				colorAry = app_show_color_list.split(",");
//			}
//			for (int i = 0; i < tagAry.length; i++) {
//				if(TextUtils.isEmpty(tagAry[i])){
//					continue;
//				}
//				if("红河州第三人民医院".equals(Constants.APP_REGION) && tagAry[i].length()>=2){
//					// 目前默认只显示1个字
//					continue;
//				}
//				RoundTextView roundTextView = new RoundTextView(context);
//				if ("枣庄妇幼保健院".equals(Constants.APP_REGION)){
//					//枣庄妇幼，术后1-3天，显示图标
//					Drawable drawable = null;
//					if ("术后1天".equals(tagAry[i])){
//						drawable = context.getResources().getDrawable(R.drawable.shuhou_1);
//					}else if ("术后2天".equals(tagAry[i])){
//						drawable = context.getResources().getDrawable(R.drawable.shuhou_2);
//					}else if ("术后3天".equals(tagAry[i])){
//						drawable = context.getResources().getDrawable(R.drawable.shuhou_3);
//					}
//					if (null != drawable){
//						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//						roundTextView.setBackground(drawable);
//						flowLayout.addView(roundTextView, new ViewGroup.LayoutParams(size, size));
//						continue;
//					}
//				}
//				roundTextView.setText(tagAry[i]);
//				roundTextView.setTextSize(13);
//				roundTextView.setGravity(Gravity.CENTER);
//				roundTextView.setTextColor(Color.WHITE);
//				String color = "#FF000000";
//				if(colorAry!=null && i<colorAry.length && !TextUtils.isEmpty(colorAry[i])){
//					color = colorAry[i];
//				}
//				roundTextView.setBackgroundColor(Color.parseColor(color), radius);
////				if("红河州第三人民医院".equals(Constants.APP_REGION) && tagAry[i].length()>=2){
////					width = (int) roundTextView.getPaint().measureText(tagAry[i]) + 10;
////					if (width > flowLayout.getWidth()){
////						height = (int) Math.ceil((double)width / flowLayout.getWidth()) * size;
////						width = flowLayout.getWidth();
////					}
////				}
//				flowLayout.addView(roundTextView, new ViewGroup.LayoutParams(size, size));
//			}
//		}
//	}
}
