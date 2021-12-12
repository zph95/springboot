package com.zph.programmer.springboot.monitor;

/**
 * Created by ZengPengHui at 2021/9/7.
 */
public class DoubleWrapper {
    private double value;

    public DoubleWrapper(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
