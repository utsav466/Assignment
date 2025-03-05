package Question6;


public class Controller


{

    /**
     * The NumberPrinter class contains methods to print 0, even, and odd numbers. 
     * Each method is synchronized to ensure thread safety during printing.
     */
    static class NumberPrinter {
        /**
         * Method to print 0. This method is synchronized to avoid race conditions.
         */
        public synchronized void printZero() {
            System.out.print("0");
        }

        /**
         * Method to print even numbers. This method is synchronized to avoid race conditions.
         * 
         * @param number The even number to be printed.
         */
        public synchronized void printEven(int number) {
            System.out.print(number);
        }

        /**
         * Method to print odd numbers. This method is synchronized to avoid race conditions.
         * 
         * @param number The odd number to be printed.
         */
        public synchronized void printOdd(int number) {
            System.out.print(number);
        }
    }

    /**
     * The ZeroThread class extends the Thread class and handles printing of the number 0.
     */
    static class ZeroThread extends Thread {
        private final NumberPrinter printer;

        /**
         * Constructor to initialize the ZeroThread with the given printer object.
         * 
         * @param printer The NumberPrinter object to print 0.
         */
        public ZeroThread(NumberPrinter printer) {
            this.printer = printer;
        }

        /**
         * The run method of the thread, which prints 0 when the thread is started.
         */
        @Override
        public void run() {
            // Print 0 using the printZero method of NumberPrinter
            printer.printZero();
        }
    }

    /**
     * The EvenThread class extends the Thread class and handles printing of even numbers.
     */
    static class EvenThread extends Thread {
        private final NumberPrinter printer;
        private final int evenNumber;

        /**
         * Constructor to initialize the EvenThread with the given printer object and even number to be printed.
         * 
         * @param printer The NumberPrinter object to print the even number.
         * @param evenNumber The even number to be printed.
         */
        public EvenThread(NumberPrinter printer, int evenNumber) {
            this.printer = printer;
            this.evenNumber = evenNumber;
        }

        /**
         * The run method of the thread, which prints the even number when the thread is started.
         */
        @Override
        public void run() {
            // Print the even number using the printEven method of NumberPrinter
            printer.printEven(evenNumber);
        }
    }

    /**
     * The OddThread class extends the Thread class and handles printing of odd numbers.
     */
    static class OddThread extends Thread {
        private final NumberPrinter printer;
        private final int oddNumber;

        /**
         * Constructor to initialize the OddThread with the given printer object and odd number to be printed.
         * 
         * @param printer The NumberPrinter object to print the odd number.
         * @param oddNumber The odd number to be printed.
         */
        public OddThread(NumberPrinter printer, int oddNumber) {
            this.printer = printer;
            this.oddNumber = oddNumber;
        }

        /**
         * The run method of the thread, which prints the odd number when the thread is started.
         */
        @Override
        public void run() {
            // Print the odd number using the printOdd method of NumberPrinter
            printer.printOdd(oddNumber);
        }
    }

    /**
     * The ThreadController class manages the creation and synchronization of threads. 
     * It ensures that 0 is printed first, followed by the even and odd numbers in the correct order.
     */
    static class ThreadController {
        private final NumberPrinter printer;
        private int n;

        /**
         * Constructor to initialize the ThreadController with the printer and the limit n up to which numbers are printed.
         * 
         * @param printer The NumberPrinter object used to print numbers.
         * @param n The limit up to which numbers are printed (n can be changed to print a different range).
         */
        public ThreadController(NumberPrinter printer, int n) {
            this.printer = printer;
            this.n = n;
        }

        /**
         * This method starts the printing process by creating and managing the ZeroThread, EvenThread, and OddThread.
         * It ensures that threads are executed in a synchronized manner.
         * 
         * @throws InterruptedException If the thread is interrupted while waiting for others to finish.
         */
        public void startPrinting() throws InterruptedException {
            // Loop through the numbers from 1 to n
            for (int i = 1; i <= n; i++) {
                // Create and start the ZeroThread to print 0
                ZeroThread zeroThread = new ZeroThread(printer);
                zeroThread.start();
                zeroThread.join(); // Wait for ZeroThread to complete before moving to the next step

                // If the current number is even, create and start the EvenThread
                if (i % 2 == 0) {
                    EvenThread evenThread = new EvenThread(printer, i);
                    evenThread.start();
                    evenThread.join(); // Wait for EvenThread to complete
                }

                // If the current number is odd, create and start the OddThread
                if (i % 2 != 0) {
                    OddThread oddThread = new OddThread(printer, i);
                    oddThread.start();
                    oddThread.join(); // Wait for OddThread to complete
                }
            }
        }
    }

    /**
     * The main method creates a NumberPrinter and ThreadController object, 
     * and starts the printing process for the given limit n.
     * 
     * @param args Command line arguments (not used in this case).
     * @throws InterruptedException If the thread is interrupted while waiting for others to finish.
     */
    public static void main(String[] args) throws InterruptedException {
        NumberPrinter printer = new NumberPrinter(); // Create a new NumberPrinter object
        int n = 5; // Set the limit n to 5 (this can be modified)
        ThreadController controller = new ThreadController(printer, n); // Create a ThreadController object
        controller.startPrinting(); // Start the printing process
    }
}
