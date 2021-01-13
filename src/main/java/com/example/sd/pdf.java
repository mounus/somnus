package com.example.sd;

import java.io.File;

public class pdf implements  Runnable {
    private int ticket = 50;//剩余的票数
    //重写run方法
    public void run() {
        while (true) {
            if (this.ticket > 0) {
                SellTicket();
            } else {
                break;
            }
        }
    }
    public synchronized void SellTicket() {
        if (ticket > 0) {
            try {
                //线程休眠50毫秒
                Thread.sleep(50);
                System.out.println(Thread.currentThread().getName()+"出售第"+ticket--+"张车票");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        //创建一个Runnable实例
        pdf p = new pdf();
        //创建3条线程，并为3条线程指定名称
        Thread thread01 = new Thread(p,"窗口1");
        Thread thread02 = new Thread(p,"窗口2");
        Thread thread03 = new Thread(p,"窗口3");
        thread01.start();
        thread02.start();
        thread03.start();
    }


}
