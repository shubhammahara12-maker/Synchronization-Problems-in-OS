//package Synchronization_OS;
//
//public class readerWriter {
//
//    int sharedData = 0;
//
//    int readers = 0;
//    boolean writing = false;
//
//
//    // Reader starts reading
//    public synchronized void startReading(int id) throws InterruptedException {
//
//        // Wait if a Writer is writing
//        while (writing) {
//            wait();
//        }
//
//        readers++;
//
//        System.out.println(
//                "Reader " + id + " is reading: " + sharedData);
//    }
//
//
//    // Reader finishes reading
//    public synchronized void stopReading(int id) {
//
//        readers--;
//
//        System.out.println(
//                "Reader " + id + " finished reading.");
//
//        // If this was the last Reader
//        if (readers == 0) {
//            notifyAll();
//        }
//    }
//
//    public synchronized void write(int id, int value) throws InterruptedException {
//
//        // Wait if Readers are reading or another Writer is writing
//        while (readers > 0 || writing) {
//            wait();
//        }
//
//        // Writer starts writing
//        writing = true;
//
//        System.out.println(
//                "Writer " + id + " is writing...");
//
//        sharedData = value;
//
//        System.out.println(
//                "Writer " + id + " wrote: " + sharedData);
//
//        // Writer finishes
//        writing = false;
//
//        notifyAll();
//    }
//
//    static class Reader extends Thread {
//
//        readerWriter rw;
//        int id;
//
//        Reader(readerWriter rw, int id) {
//
//            this.rw = rw;
//            this.id = id;
//        }
//
//        public void run() {
//
//            try {
//
//                rw.startReading(id);
//
//                Thread.sleep(500);
//
//                rw.stopReading(id);
//
//            } catch (InterruptedException e) {
//
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//
//    static class Writer extends Thread {
//
//        readerWriter rw;
//        int id;
//        int value;
//
//        Writer(readerWriter rw, int id, int value) {
//
//            this.rw = rw;
//            this.id = id;
//            this.value = value;
//        }
//
//        public void run() {
//
//            try {
//
//                rw.write(id, value);
//
//            } catch (InterruptedException e) {
//
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//
//        readerWriter rw = new readerWriter();
//
//        Reader r1 = new Reader(rw, 1);
//        Reader r2 = new Reader(rw, 2);
//        Writer w1 = new Writer(rw, 1, 100);
//        Reader r3 = new Reader(rw, 3);
//        Writer w2 = new Writer(rw, 2, 200);
//
//        r1.start();
//        r2.start();
//        w1.start();
//        r3.start();
//        w2.start();
//    }
//}

package Synchronization_OS;

public class readerWriter {

    int sharedData = 0;

    int readers = 0;
    boolean writing = false;


    // Reader starts reading
    public synchronized void startReading(int id)
            throws InterruptedException {

        // Reader waits if Writer is writing
        while (writing) {
            wait();
        }

        readers++;

        System.out.println(
                "Reader " + id + " is reading: " + sharedData);
    }


    // Reader finishes reading
    public synchronized void stopReading(int id) {

        // Safety check
        if (readers > 0) {
            readers--;

            System.out.println(
                    "Reader " + id + " finished reading.");

            // If no Readers are left, wake waiting Writers
            if (readers == 0) {
                notifyAll();
            }
        }
    }


    // Writer writes data
    public synchronized void write(int id, int value)
            throws InterruptedException {

        // Writer waits if Readers are reading
        // or another Writer is writing
        while (readers > 0 || writing) {
            wait();
        }

        writing = true;

        System.out.println(
                "Writer " + id + " is writing...");

        sharedData = value;

        System.out.println(
                "Writer " + id + " wrote: " + sharedData);

        writing = false;

        // Wake waiting Readers/Writers
        notifyAll();
    }


    // Reader Thread
    static class Reader extends Thread {

        readerWriter rw;
        int id;

        Reader(readerWriter rw, int id) {

            this.rw = rw;
            this.id = id;
        }

        public void run() {

            boolean startedReading = false;

            try {

                rw.startReading(id);

                // Reader successfully started
                startedReading = true;

                Thread.sleep(500);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

            } finally {

                // Only stop if Reader actually started
                if (startedReading) {
                    rw.stopReading(id);
                }
            }
        }
    }


    // Writer Thread
    static class Writer extends Thread {

        readerWriter rw;
        int id;
        int value;

        Writer(readerWriter rw, int id, int value) {

            this.rw = rw;
            this.id = id;
            this.value = value;
        }

        public void run() {

            try {

                rw.write(id, value);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }
    }


    // Main
    public static void main(String[] args) {

        readerWriter rw = new readerWriter();


        Reader r1 = new Reader(rw, 1);

        Reader r2 = new Reader(rw, 2);

        Writer w1 = new Writer(rw, 1, 100);

        Reader r3 = new Reader(rw, 3);

        Writer w2 = new Writer(rw, 2, 200);


        r1.start();

        r2.start();

        w1.start();

        r3.start();

        w2.start();
    }
}
