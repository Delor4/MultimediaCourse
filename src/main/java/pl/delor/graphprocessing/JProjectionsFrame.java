
package pl.delor.graphprocessing;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author delor
 */
public class JProjectionsFrame extends javax.swing.JFrame {

    /** Creates new form JProjectionsFrame */
    public JProjectionsFrame(List<int[][]> data) {
        this.data = data;
        initComponents();
        makeHorizChartPanel();
        makeVerticalChartPanel();
    }

    private List<int[][]> data = null;
    
    private void makeHorizChartPanel() {
        java.awt.Dimension oldDim = jPanelHorizontal.getSize();
        remove(jPanelHorizontal);
        jPanelHorizontal = (ChartPanel) createHorizontalChartPanel();
        getContentPane().add(jPanelHorizontal);
        jPanelHorizontal.setPreferredSize(oldDim);
        pack();
    }
    private void makeVerticalChartPanel() {
        java.awt.Dimension oldDim = jPanelVertical.getSize();
        remove(jPanelVertical);
        jPanelVertical = (ChartPanel) createVerticalChartPanel();
        getContentPane().add(jPanelVertical);
        jPanelVertical.setPreferredSize(oldDim);
        pack();
    }
    public JPanel createHorizontalChartPanel() {
        return createChartPanel(createCumulativeChart(createProjectionDataset(0),"Projection (horizontal)"));
    }
    public JPanel createVerticalChartPanel() {
        return createChartPanel(createCumulativeChart(createProjectionDataset(1),"Projection (vertical)"));
    }
    public JPanel createChartPanel(JFreeChart ch) {
        JFreeChart chart = ch;
        ChartPanel panel = new ChartPanel(chart, false);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }
private JFreeChart createCumulativeChart(XYDataset dataset, String title) {

        JFreeChart chart = ChartFactory.
                createXYLineChart(
                title,
                "Line of image", // x-axis label
                "Pixels in line", // y-axis label
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

    private XYDataset createProjectionDataset(int proj_id) {
        int maxVal[] = new int[3];
        
        int[][] proj = data.get(proj_id);
        
        for (int i = 0; i < proj.length; i++) {
            maxVal[i] = Arrays.stream(proj[i]).summaryStatistics().getMax();
        }
        
        String seriesTitles[] = new String[]{"Red", "Green", "Blue"};

        XYSeriesCollection dataset = new XYSeriesCollection();

        for (int i = 0; i < proj.length; i++) {
            XYSeries s = new XYSeries(seriesTitles[i]);
            for (int j = 0; j < proj[i].length; j++) {
                s.add(j, (float)proj[i][j] / maxVal[i]);
            }
            dataset.addSeries(s);
        }

        return dataset;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelHorizontal = new javax.swing.JPanel();
        jPanelVertical = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(2, 0));

        org.jdesktop.layout.GroupLayout jPanelHorizontalLayout = new org.jdesktop.layout.GroupLayout(jPanelHorizontal);
        jPanelHorizontal.setLayout(jPanelHorizontalLayout);
        jPanelHorizontalLayout.setHorizontalGroup(
            jPanelHorizontalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        jPanelHorizontalLayout.setVerticalGroup(
            jPanelHorizontalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 150, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelHorizontal);

        org.jdesktop.layout.GroupLayout jPanelVerticalLayout = new org.jdesktop.layout.GroupLayout(jPanelVertical);
        jPanelVertical.setLayout(jPanelVerticalLayout);
        jPanelVerticalLayout.setHorizontalGroup(
            jPanelVerticalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        jPanelVerticalLayout.setVerticalGroup(
            jPanelVerticalLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 150, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelVertical);

        pack();
    }// </editor-fold>//GEN-END:initComponents

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelHorizontal;
    private javax.swing.JPanel jPanelVertical;
    // End of variables declaration//GEN-END:variables

}
