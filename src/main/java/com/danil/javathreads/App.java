package com.danil.javathreads;

class Foo {
    public static int phase = 0;

    public void first(Runnable r) {
        synchronized (r) {
            try {
                while (phase != 1) {
                    r.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        System.out.println("first");
    }

    public void second(Runnable r) {
        synchronized (r) {
            try {
                while (phase != 2) {
                    r.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        System.out.println("second");
    }

    public void third(Runnable r) {
        synchronized (r) {
            try {
                while (phase != 3) {
                    r.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        System.out.println("third");
    }
}

public class App {
    public static void main(String[] args) throws InterruptedException {
        Foo foo = new Foo();
        Runnable rA = new Runnable() {
            @Override
            public void run() {
                foo.first(this);
            }
        };
        Runnable rB = new Runnable() {
            @Override
            public void run() {
                foo.second(this);
            }
        };
        Runnable rC = new Runnable() {
            @Override
            public void run() {
                foo.third(this);
            }
        };

        Thread c = new Thread(rC);
        c.start();
        Thread a = new Thread(rA);
        a.start();
        Thread b = new Thread(rB);
        b.start();

        synchronized (rA) {
            Foo.phase = 1;
            rA.notify();
        }
        a.join();

        synchronized (rB) {
            Foo.phase = 2;
            rB.notify();
        }
        b.join();

        synchronized (rC) {
            Foo.phase = 3;
            rC.notify();
        }
        c.join();
    }
}
