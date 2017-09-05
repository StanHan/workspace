package com.daikuan.platform.ma.test;

public class Local {
    public static ThreadLocal<Integer> i = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

//    public static void main(String[] args) {
//
//    }
}
