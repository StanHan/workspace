package demo.java.util.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLock {
	static class Friend {
		private final String name;

		public Friend(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public synchronized void bow(Friend bower) {
			System.out.format("%s: %s" + "  has bowed to me!%n", this.name, bower.getName());
			bower.bowBack(this);
		}

		public synchronized void bowBack(Friend bower) {
			System.out.format("%s: %s" + " has bowed back to me!%n", this.name, bower.getName());
		}
	}

	public static void main(String[] args) {
		final Friend alphonse = new Friend("Alphonse");
		final Friend gaston = new Friend("Gaston");
		new Thread(new Runnable() {
			public void run() {
				alphonse.bow(gaston);
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				gaston.bow(alphonse);
			}
		}).start();
	}
}


class Deadlocker implements Runnable {
    public int flag = 1;
    static Object o1 = new Object(), o2 = new Object();

    public void run() {
         System.out.println("flag=" + flag);
         if (flag == 1) {
              synchronized (o1) {
                   try {
                        Thread.sleep(500);
                   } catch (Exception e) {
                        e.printStackTrace();
                   }
                   synchronized (o2) {
                        System.out.println("1");
                   }
              }
         }

         if (flag == 0) {
              synchronized (o2) {
                   try {
                        Thread.sleep(500);
                   } catch (Exception e) {
                        e.printStackTrace();
                   }
                   synchronized (o1) {
                        System.out.println("0");
                   }
              }
         }
    }

    public static void main(String[] args) {
         Deadlocker td1 = new Deadlocker();
         Deadlocker td2 = new Deadlocker();
         td1.flag = 1;
         td2.flag = 0;
         Thread t1 = new Thread(td1);
         Thread t2 = new Thread(td2);
         t1.start();
         t2.start();
    }
}

class Deadlocker2 implements Runnable {
    public int flag = 1;
    static Object o1 = new Object(), o2 = new Object();

    public synchronized void run() {
         System.out.println("flag=" + flag);
         if (flag == 1) {
              try {
                   Thread.sleep(500);
              } catch (Exception e) {
                   e.printStackTrace();
              }

              System.out.println("1");
         }
         if (flag == 0) {
              try {
                   Thread.sleep(500);
              } catch (Exception e) {
                   e.printStackTrace();
              }
              System.out.println("0");
         }
    }

    public static void main(String[] args) {
         Deadlocker td1 = new Deadlocker();
         Deadlocker td2 = new Deadlocker();
         td1.flag = 1;
         td2.flag = 0;
         Thread t1 = new Thread(td1);
         Thread t2 = new Thread(td2);
         t1.start();
         t2.start();
    }
}

class Deadlocker3 implements Runnable {
    public int flag = 1;
    static Object o1 = new Object(), o2 = new Object();
    private final Lock lock = new ReentrantLock();

    public boolean checkLock() {
         return lock.tryLock();
    }

    public void run() {
         if (checkLock()) {
              try {
                   System.out.println("flag=" + flag);
                   if (flag == 1) {
                        try {
                             Thread.sleep(500);
                        } catch (Exception e) {
                             e.printStackTrace();
                        }

                        System.out.println("1");
                   }
                   if (flag == 0) {
                        try {
                             Thread.sleep(500);
                        } catch (Exception e) {
                             e.printStackTrace();
                        }
                        System.out.println("0");
                   }
              } finally {
                   lock.unlock();
              }
         }
    }

    public static void main(String[] args) {
         Deadlocker td1 = new Deadlocker();
         Deadlocker td2 = new Deadlocker();
         td1.flag = 1;
         td2.flag = 0;
         Thread t1 = new Thread(td1);
         Thread t2 = new Thread(td2);
         t1.start();
         t2.start();
    }
}