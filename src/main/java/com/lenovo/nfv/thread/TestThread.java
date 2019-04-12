package com.lenovo.nfv.thread;

public class TestThread {
    public static void main(String[] args) {
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        }.start();

        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(20*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        }.start();

        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(30*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        }.start();

        ThreadViewer.showThreads();
    }
}