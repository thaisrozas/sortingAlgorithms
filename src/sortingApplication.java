import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SortingApplication extends JFrame {
    private JButton runButton;
    private JTextArea resultsArea;
    private SortingAlgorithms sa;

    public SortingApplication() {
        sa = new SortingAlgorithms();
        setTitle("Sorting Algorithm Performance Test");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createUI();
    }

    private void createUI() {
        runButton = new JButton("Run Sorting Tests");
        resultsArea = new JTextArea(16, 50);
        resultsArea.setEditable(false);
        
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSortingTests();
            }
        });

        getContentPane().add(runButton, "North");
        getContentPane().add(resultsArea, "Center");
    }

    private void performSortingTests() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                int[] sizes = {100, 1000, 10000};
                XYSeries bubbleSeries = new XYSeries("Bubble Sort");
                XYSeries quickSeries = new XYSeries("Quick Sort");
                XYSeries mergeSeries = new XYSeries("Merge Sort");
                XYSeries selectionSeries = new XYSeries("Selection Sort");

                for (int size : sizes) {
                    int[] array = generateRandomArray(size);

                    // Record times and add to series
                    long startTime = System.nanoTime();
                    bubbleSort(array.clone());
                    long endTime = System.nanoTime();
                    bubbleSeries.add(size, endTime - startTime);

                    startTime = System.nanoTime();
                    quickSort(array.clone(), 0, size - 1);
                    endTime = System.nanoTime();
                    quickSeries.add(size, endTime - startTime);

                    startTime = System.nanoTime();
                    mergeSort(array.clone(), 0, size - 1);
                    endTime = System.nanoTime();
                    mergeSeries.add(size, endTime - startTime);

                    startTime = System.nanoTime();
                    selectionSort(array.clone());
                    endTime = System.nanoTime();
                    selectionSeries.add(size, endTime - startTime);
                }

                dataset.addSeries(bubbleSeries);
                dataset.addSeries(quickSeries);
                dataset.addSeries(mergeSeries);
                dataset.addSeries(selectionSeries);
                return null;
            }
        };

        worker.execute();
    }

    private void exportDataToCSV(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Algorithm,Array Size,Execution Time (Nanoseconds)\n");
            for (int i = 0; i < dataset.getSeriesCount(); i++) {
                XYSeries series = dataset.getSeries(i);
                for (int j = 0; j < series.getItemCount(); j++) {
                    writer.append(String.format("%s,%d,%f\n", series.getKey(), series.getX(j).intValue(), series.getY(j).doubleValue()));
                }
            }
            JOptionPane.showMessageDialog(this, "Data exported to " + filePath, "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error exporting data: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
