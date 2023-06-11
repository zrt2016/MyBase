package com.zrt.mybase.activity.jisuanqi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zrt.mybase.R;
import com.zrt.mybase.utils.Arith;

/**
 * 计算器
 */
public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

        //对每一个按钮定义一个变量
        EditText result,old_display;
        String value;
        String numstr = "";
        String firstStr,secondStr;
        String stead = "0";
        Double firstNum,secondNum;
        int flag = 0;
        boolean but_flag = false;
        boolean bool = false;
        Button[] number = new Button[10];
        Button[] operation = new Button[5];
        Button[] special = new  Button[2];
        Button[] normal = new Button[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        //对每一个变量通过findViewById()方法找到对应的按钮（对象），进行实例化
        result = findViewById(R.id.editText1);
        old_display = findViewById(R.id.editText2);
        special[0] = findViewById(R.id.percent);
        special[1] = findViewById(R.id.point);
        normal[0] = findViewById(R.id.all_clear);
        normal[1] = findViewById(R.id.delete);
        normal[2] = findViewById(R.id.convert);
        operation[0] = findViewById(R.id.division);
        operation[1] = findViewById(R.id.multiplication);
        operation[2] = findViewById(R.id.subtraction);
        operation[3] = findViewById(R.id.addition);
        operation[4] = findViewById(R.id.equal);
        number[0] = findViewById(R.id.but_0);
        number[1] = findViewById(R.id.but_1);
        number[2] = findViewById(R.id.but_2);
        number[3] = findViewById(R.id.but_3);
        number[4] = findViewById(R.id.but_4);
        number[5] = findViewById(R.id.but_5);
        number[6] = findViewById(R.id.but_6);
        number[7] = findViewById(R.id.but_7);
        number[8] = findViewById(R.id.but_8);
        number[9] = findViewById(R.id.but_9);

        //为每一个按钮设置点击事件监听器
        special[0].setOnClickListener(this);
        special[1].setOnClickListener(this);
        normal[0].setOnClickListener(this);
        normal[1].setOnClickListener(this);
        normal[2].setOnClickListener(this);
        for(int i = 0;i < operation.length;i++) {
        operation[i].setOnClickListener(this);
        }
        for(int i = 0;i < number.length;i++) {
        number[i].setOnClickListener(this);
        }
        }
    //实现onClick()方法
    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    public void onClick(View view) {
        value = result.getText().toString(); // 将编辑框1中的字符串赋值给value
        stead = value;
        switch (view.getId()) {
            case R.id.all_clear:
                numstr = "";
                result.setText("0");
                old_display.setText("");
                break;
            case R.id.delete:
                numstr = value.substring(0, value.length() - 1);
                if (value.length() != 1) {
                    result.setText(value.substring(0, value.length() - 1));
                } else {
                    result.setText("0");
                }
                break;

            case R.id.point:
                if (stead.equals("0")) {
                    result.setText("0.");
                    numstr = "0.";
                } else {
                    numstr += ((Button) view).getText().toString();
                    result.setText(numstr);
                }
                break;
            case R.id.but_0:
                if (stead.equals("0")) {
                    numstr = "";
                } else {
                    numstr += ((Button) view).getText().toString();
                    result.setText(numstr);
                }
                break;
            case R.id.but_1:
            case R.id.but_2:
            case R.id.but_3:
            case R.id.but_4:
            case R.id.but_5:
            case R.id.but_6:
            case R.id.but_7:
            case R.id.but_8:
            case R.id.but_9:
                numstr += ((Button) view).getText().toString();
                result.setText(numstr);
                but_flag = true;
                break;

            case R.id.addition:
                firstStr = value;
                firstNum = Double.parseDouble(value);
                flag = 1;
                numstr = "";
                old_display.setText(firstStr + "＋");
                result.setText("0");
                break;
            case R.id.subtraction:
                firstStr = value;
                firstNum = Double.parseDouble(value);
                flag = 2;
                numstr = "";
                old_display.setText(firstStr + "－");
                result.setText("0");
                break;
            case R.id.multiplication:
                firstStr = value;
                firstNum = Double.parseDouble(value);
                flag = 3;
                numstr = "";
                old_display.setText(firstStr + "×");
                result.setText("0");
                break;
            case R.id.division:
                firstStr = value;
                firstNum = Double.parseDouble(value);
                flag = 4;
                numstr = "";
                old_display.setText(firstStr + "÷");
                result.setText("0");
                break;
            case R.id.percent:
                firstStr = value;
                firstNum = Double.parseDouble(value);
                flag = 5;
                numstr = "";
                old_display.setText(firstStr + "%");
                result.setText("0");
                break;

            case R.id.equal:
                secondStr = value;
                secondNum = Double.parseDouble(value);
                switch (flag) {
                    case 1:
                        old_display.setText(firstStr + "＋" + secondStr);
                        // result.setText(firstNum+secondNum+"");
                        result.setText(Arith.doubleTrans(Arith.add(firstNum, secondNum)) + "");
                        break;
                    case 2:
                        old_display.setText(firstStr + "－" + secondStr);
                        result.setText(Arith.doubleTrans(Arith.sub(firstNum, secondNum)) + "");
                        break;
                    case 3:
                        old_display.setText(firstStr + "×" + secondStr);
                        result.setText(Arith.doubleTrans(Arith.mul(firstNum, secondNum)) + "");
                        break;
                    case 4:
                        old_display.setText(firstStr + "÷" + secondStr);
                        if (secondNum == 0) {
                            result.setText("错误");
                        } else {
                            result.setText(Arith.doubleTrans(Arith.div(firstNum, secondNum)) + "");
                        }
                        break;
                    case 5:
                        old_display.setText(firstStr + "%" + secondStr);
                        if (secondNum == 0) {
                            result.setText("错误");
                        } else {
                            result.setText(Arith.doubleTrans(firstNum % secondNum) + "");
                        }
                }
                flag = 0;
                numstr = "";
                stead = "0";
                break;
        }
    }
}
