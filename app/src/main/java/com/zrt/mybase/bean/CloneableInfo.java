package com.zrt.mybase.bean;

/**
 * @author：Zrt
 * @date: 2021/10/9
 */
public class CloneableInfo implements Cloneable {
    public String name;
    public boolean sex;
    public int age;

    @Override
    public CloneableInfo clone() {
        try {
            return (CloneableInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "CloneableInfo{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                '}';
    }
}
