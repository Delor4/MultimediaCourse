package pl.delor.graphprocessing;

import java.awt.Color;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.GridBagConstraints;

/**
 *
 * @author delor
 */
public class JHistogramDialog extends javax.swing.JDialog {

    /**
     * Creates new form JHistogramDialog
     */
    public JHistogramDialog(int[][] histData) {
        this.hist = histData;
        initComponents();
        chartPanelConstraints = getChartPanelConstraints();
        selectLineChartPanel();
    }
    
    private void selectLineChartPanel() {
        java.awt.Dimension oldDim = jPanelChart.getSize();
        remove(jPanelChart);
        ChartPanel chartPanel = (ChartPanel) createLineChartPanel();
        jPanelChart = chartPanel;
        getContentPane().add(chartPanel, chartPanelConstraints);
        chartPanel.setPreferredSize(oldDim);
        pack();
    }
    private void selectCumulativeChartPanel() {
        java.awt.Dimension oldDim = jPanelChart.getSize();
        remove(jPanelChart);
        ChartPanel chartPanel = (ChartPanel) createCumulativeChartPanel();
        jPanelChart = chartPanel;
        getContentPane().add(chartPanel, chartPanelConstraints);
        chartPanel.setPreferredSize(oldDim);
        pack();
    }
    
    private GridBagConstraints chartPanelConstraints = null;
    
    private GridBagConstraints getChartPanelConstraints() {
        var gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        
        return gridBagConstraints;
    }
    private int[][] hist = null;

    private JFreeChart createLineChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.
                createXYLineChart(
                "Image histogram", // title
                "Color value", // x-axis label
                "Values count", // y-axis label
                dataset);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDefaultShapesVisible(false);
            renderer.setDefaultShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
            renderer.setSeriesPaint(0, new Color(0xFF, 0x55, 0x55));
            renderer.setSeriesPaint(1, new Color(0x55, 0xFF, 0x55));
            renderer.setSeriesPaint(2, new Color(0x55, 0x55, 0xFF));
        }

        return chart;
    }
    private JFreeChart createCumulativeChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.
                createXYAreaChart(
                "Image cumulative histogram", // title
                "Color value", // x-axis label
                "Values count", // y-axis label
                dataset);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYBarRenderer renderer = new XYBarRenderer();
        plot.setRenderer(0, renderer);
        renderer.setSeriesPaint(0, new Color(0xFF, 0x55, 0x55));
        renderer.setSeriesPaint(1, new Color(0x55, 0xFF, 0x55));
        renderer.setSeriesPaint(2, new Color(0x55, 0x55, 0xFF));
        
        renderer.setDrawBarOutline(false);
        renderer.setBarPainter(new StandardXYBarPainter());
        renderer.setShadowVisible(false);

        return chart;
    }

    private XYDataset createLineDataset() {

        float ds[][] = new float[3][256];
        int maxVal = 0;
        long sumVal[] = new long[3];

        for (int j = 0; j < hist.length; j++) {
            for (int v : hist[j]) {
                sumVal[j] += v;
                if (maxVal < v) {
                    maxVal = v;
                }
            }
        }
        for (int j = 0; j < hist.length; j++) {
            for (int i = 0; i < hist[0].length; i++) {
                ds[j][i] = (float) hist[j][i] / sumVal[j];
            }
        }

        String seriesTitles[] = new String[]{"Red", "Green", "Blue"};

        XYSeriesCollection dataset = new XYSeriesCollection();

        for (int i = 0; i < ds.length; i++) {
            XYSeries s = new XYSeries(seriesTitles[i]);
            for (int j = 0; j < ds[i].length; j++) {
                s.add(j, ds[i][j]);
            }
            dataset.addSeries(s);
        }

        return dataset;
    }

    private XYDataset createCumulativeDataset() {
        float ds[][] = new float[3][256];
        int maxVal = 0;
        long sumVal[] = new long[3];

        for (int j = 0; j < hist.length; j++) {
            for (int v : hist[j]) {
                sumVal[j] += v;
                if (maxVal < v) {
                    maxVal = v;
                }
            }
        }
        for (int j = 0; j < hist.length; j++) {
            float sum = 0;
            for (int i = 0; i < hist[0].length; i++) {
                sum += (float) hist[j][i] / sumVal[j];
                ds[j][i] = sum;
            }
        }

        String seriesTitles[] = new String[]{"Red", "Green", "Blue"};

        XYSeriesCollection dataset = new XYSeriesCollection();

        for (int i = 0; i < ds.length; i++) {
            XYSeries s = new XYSeries(seriesTitles[i]);
            for (int j = 0; j < ds[i].length; j++) {
                s.add(j, ds[i][j]);
            }
            dataset.addSeries(s);
        }

        return dataset;
    }
    public JPanel createLineChartPanel() {
        return createChartPanel(createLineChart(createLineDataset()));
    }
    public JPanel createCumulativeChartPanel() {
        return createChartPanel(createCumulativeChart(createCumulativeDataset()));
    }
    public JPanel createChartPanel(JFreeChart ch) {
        JFreeChart chart = ch;
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelChart = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        org.jdesktop.layout.GroupLayout jPanelChartLayout = new org.jdesktop.layout.GroupLayout(jPanelChart);
        jPanelChart.setLayout(jPanelChartLayout);
        jPanelChartLayout.setHorizontalGroup(
            jPanelChartLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        jPanelChartLayout.setVerticalGroup(
            jPanelChartLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 262, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanelChart, gridBagConstraints);

        jPanelButtons.setPreferredSize(new java.awt.Dimension(500, 38));

        jButton1.setText("Line");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButton1);

        jButton2.setText("Cumulative");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButton2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanelButtons, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        selectLineChartPanel();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        selectCumulativeChartPanel();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelChart;
    // End of variables declaration//GEN-END:variables
}
