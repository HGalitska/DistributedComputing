import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        MainForm mainForm = new MainForm();

        Runnable r1 = () -> {
            System.out.println("Thread 1 started.");
            while (mainForm.slider.getValue() != 10) {
                mainForm.slideTo(mainForm.slider.getValue() - 1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Thread 1 finished.");
        };
        Thread T1 = new Thread(r1);
        T1.setDaemon(true);

        Runnable r2 = () -> {
            System.out.println("Thread 2 started.");
            while (mainForm.slider.getValue() != 90) {
                mainForm.slideTo(mainForm.slider.getValue() + 1);
            }
            System.out.println("Thread 2 finished.");
        };
        Thread T2 = new Thread(r2);
        T2.setDaemon(true);

        mainForm.startButton.addActionListener((e) -> {
            T1.start();
            T2.start();
        });
        mainForm.exitButton.addActionListener((e) -> System.exit(0));

        mainForm.priority1.addActionListener((e) -> {
            JComboBox<Integer> combo = (JComboBox<Integer>) e.getSource();
            int selectedPr = (Integer) combo.getSelectedItem();

            T1.setPriority(selectedPr);
            System.out.println(T1.getPriority());
        });
        mainForm.priority2.addActionListener((e) -> {
            JComboBox<Integer> combo = (JComboBox<Integer>) e.getSource();
            int selectedPr = (Integer) combo.getSelectedItem();

            T2.setPriority(selectedPr);
            System.out.println(T2.getPriority());
        });
    }
}
