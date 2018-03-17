public class Main {
    private static int semaphore = 0;

    synchronized static void lockSemaphore() {
        if (semaphore == 0) semaphore = 1;
    }

    synchronized static void unlockSemaphore() {
        if (semaphore == 1) semaphore = 0;
    }

    private static void setListeners(MainForm mainForm, Thread T1, Thread T2) {

        mainForm.Start1Button.addActionListener((event) -> {

            if (semaphore == 1) {
                mainForm.textPane.setText("Thread 1 couldn't be started. Critical section is busy.");
                return;
            }

            mainForm.textPane.setText("Thread 1 started.");
            lockSemaphore();
            T1.setPriority(Thread.MIN_PRIORITY);
            T1.start();

            mainForm.Stop2Button.setEnabled(false);
            mainForm.Stop1Button.setEnabled(true);
        });

        mainForm.Stop1Button.addActionListener((event) -> {
            unlockSemaphore();
            try {
                T1.interrupt();
                mainForm.textPane.setText("Thread 1 is destroyed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            mainForm.Stop2Button.setEnabled(true);
            mainForm.Stop1Button.setEnabled(false);
        });

        mainForm.Start2Button.addActionListener((event) -> {
            if (semaphore == 1) {
                mainForm.textPane.setText("Thread 2 couldn't be started. Critical section is busy.");
                return;
            }

            mainForm.textPane.setText("Thread 2 started.");
            lockSemaphore();
            T2.setPriority(Thread.MAX_PRIORITY);
            T2.start();

            mainForm.Stop1Button.setEnabled(false);
            mainForm.Stop2Button.setEnabled(true);
        });

        mainForm.Stop2Button.addActionListener((event) -> {
            unlockSemaphore();
            try {
                T2.interrupt();
                mainForm.textPane.setText("Thread 2 is destroyed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            mainForm.Stop1Button.setEnabled(true);
            mainForm.Stop2Button.setEnabled(false);
        });
    }

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();

        Runnable r1 = () -> {
            while (mainForm.slider.getValue() != 10 && !Thread.currentThread().isInterrupted()) {
                mainForm.slideTo(mainForm.slider.getValue() - 1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            mainForm.textPane.setText("Thread 1 finished. Click STOP 1 to destroy it.");
        };
        Thread T1 = new Thread(r1);
        T1.setDaemon(true);

        Runnable r2 = () -> {
            while (mainForm.slider.getValue() != 90 && !Thread.currentThread().isInterrupted()) {
                mainForm.slideTo(mainForm.slider.getValue() + 1);
            }
            mainForm.textPane.setText("Thread 2 finished. Click STOP 2 to destroy it.");
        };
        Thread T2 = new Thread(r2);
        T2.setDaemon(true);

        setListeners(mainForm, T1, T2);
    }
}