package pl.delor.graphprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import static pl.delor.graphprocessing.GP.copyImage;
import static pl.delor.graphprocessing.GP.getBlue;
import static pl.delor.graphprocessing.GP.getFileExtension;
import static pl.delor.graphprocessing.GP.getGreen;
import static pl.delor.graphprocessing.GP.getIconFromBase64;
import static pl.delor.graphprocessing.GP.getImageExtension;
import static pl.delor.graphprocessing.GP.getRed;
import static pl.delor.graphprocessing.GP.toLuminance;
import static pl.delor.graphprocessing.GP.toRGB;
import javax.swing.*;    
import java.awt.event.*;    
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.border.LineBorder;
import java.awt.Color;

/**
 *
 * @author delor
 */
public class MultimediaMainFrame extends javax.swing.JFrame {

    BufferedImage imageInput = null;
    BufferedImage imageOutput = null;

    public BufferedImage getImageOutput() {
        return imageOutput;
    }

    Boolean edited = false;

    private final String appIconString = "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAMAAACdt4HsAAAAB3RJTUUH5AoNBxcXesQknAAAAAlwSFlzAAAu4AAALuABDw7X4QAAAARnQU1BAACxjwv8YQUAAABRUExURQAAAGZmZjMzM2YzAMzM/zOZ/2ZmmczMzJlmM/+ZM//Mmf/MZv+ZAP/MM8zMmf9mADOZzMyZZpmZmWaZzMyZM8yZmZlmAMyZAJkzAMyZzMxmADlTPGMAAAABdFJOUwBA5thmAAABLUlEQVR42u3U2Q6CMBAF0FJbK7sbuPz/hwqWaQHTMItveh8ICemxvQwq9UupKun6MSKgHSIwPNAKgFYGbG5gtxEAbkzgDsBT84AeKui1ZgE3qGCndZYETDKuhgqYQFcJgVOogAnACao7D3B5OAEPeIQpYAIlnKBnArMKWICLL5EHNAD0TOAU5hgJWLsE4CXWWGAQ5kSooKQAM6GDOe7QwFuwHxU4PLDYwxUqoALW31yggtwQgEkYL2eooCEBXhgTK+ABxzycgAaAEOa4JAKwgxKAgg/4OBIQKrTD39k7tSEAdp7GAwUBgKXTMIyHqJ1BA/G3p3t3yAv/iAbEedw7QwDi6vVXiQOWS3jAahoMFTAyYBUxYMTAIj8AoPIH0oBSGpsUgCbSgMpwUf98Ny9G+CvZvnzSbwAAAABJRU5ErkJggg==";
    
    Map<String, int[][]> filters = new HashMap<>();
    
    /**
     * Creates new form MultimediaMainFrame
     */
    public MultimediaMainFrame() {
        initComponents();
        addAllMenus();
        if (appIconString != null) {
            ImageIcon img = getIconFromBase64(appIconString);
            setIconImage(img.getImage());
        }
        jCheckBoxMenuItemShowStats.setState(false);
    }
    
    private JMenuItem makeFilterMenuItem(String name, int[][] filter){
       JMenuItem item = new JMenuItem(name);
       filters.put(name, filter);
       item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterActionPerformed(evt, name);
            }
        });
       return item;
    }
    
    private JMenu makePrevittMenu(){
        JMenu menu = new JMenu("Previtt");
        menu.add(makeFilterMenuItem("Previtt N Filter", new int[][]{
        {1, 1, 1},
        {0, 0, 0},
        {-1, -1, -1},
    }));
        menu.add(makeFilterMenuItem("Previtt NE Filter", new int[][]{
        {0, 1, 1},
        {-1, 0, 1},
        {-1, -1, 0},
    }));
        menu.add(makeFilterMenuItem("Previtt E Filter", new int[][]{
        {-1, 0, 1},
        {-1, 0, 1},
        {-1, 0, 1},
    }));
        menu.add(makeFilterMenuItem("Previtt SE Filter", new int[][]{
        {-1, -1, 0},
        {-1, 0, 1},
        {0, 1, 1},
    }));
        menu.add(makeFilterMenuItem("Previtt S Filter", new int[][]{
        {-1, -1, -1},
        {0, 0, 0},
        {1, 1, 1},
    }));
        menu.add(makeFilterMenuItem("Previtt SW Filter", new int[][]{
        {0, -1, -1},
        {1, 0, -1},
        {1, 1, 0},
    }));
        menu.add(makeFilterMenuItem("Previtt W Filter", new int[][]{
        {1, 0, -1},
        {1, 0, -1},
        {1, 0, -1},
    }));
        menu.add(makeFilterMenuItem("Previtt NW Filter", new int[][]{
        {1, 1, 0},
        {1, 0, -1},
        {0, -1, -1},
    }));
        return menu;
    }
    private JMenu makeSobelMenu(){
        JMenu menu = new JMenu("Sobel");
        menu.add(makeFilterMenuItem("Sobel N Filter", new int[][]{
        {1, 2, 1},
        {0, 0, 0},
        {-1, -2, -1},
    }));
        menu.add(makeFilterMenuItem("Sobel NE Filter", new int[][]{
        {0, 1, 2},
        {-1, 0, 1},
        {-2, -1, 0},
    }));
        menu.add(makeFilterMenuItem("Sobel E Filter", new int[][]{
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1},
    }));
        menu.add(makeFilterMenuItem("Sobel SE Filter", new int[][]{
        {-2, -1, 0},
        {-1, 0, 1},
        {0, 1, 2},
    }));
        menu.add(makeFilterMenuItem("Sobel S Filter", new int[][]{
        {-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1},
    }));
        menu.add(makeFilterMenuItem("Sobel SW Filter", new int[][]{
        {0, -1, -2},
        {1, 0, -1},
        {2, 1, 0},
    }));
        menu.add(makeFilterMenuItem("Sobel W Filter", new int[][]{
        {1, 0, -1},
        {2, 0, -2},
        {1, 0, -1},
    }));
        menu.add(makeFilterMenuItem("Sobel NW Filter", new int[][]{
        {2, 1, 0},
        {1, 0, -1},
        {0, -1, -2},
    }));
        return menu;
    }
    private JMenu makeBlurMenu(){
        JMenu menu = new JMenu("Blur");
        menu.add(makeFilterMenuItem("Blur Filter 1", new int[][]{
        {1, 1, 1},
        {1, 1, 1},
        {1, 1, 1},
    }));
        menu.add(makeFilterMenuItem("Blur Filter 2", new int[][]{
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1},
    }));
        menu.add(makeFilterMenuItem("Blur Filter 3", new int[][]{
        {1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1},
    }));
        menu.add(makeFilterMenuItem("Blur Filter 4", new int[][]{
        {1, 1, 1},
        {1, 2, 1},
        {1, 1, 1},
    }));
        menu.add(makeFilterMenuItem("Blur Filter 5", new int[][]{
        {1, 1, 1},
        {1, 4, 1},
        {1, 1, 1},
    }));
        menu.add(makeFilterMenuItem("Blur Filter 6", new int[][]{
        {1, 1, 1},
        {1, 12, 1},
        {1, 1, 1},
    }));
      menu.add(makeFilterMenuItem("Gauss Filter 1", new int[][]{
        {1, 2, 1},
        {2, 4, 2},
        {1, 2, 1},
    }));
      menu.add(makeFilterMenuItem("Gaussian Filter 2", new int[][]{
        {1, 3, 1},
        {3, 9, 3},
        {1, 3, 1},
    }));
      menu.add(makeFilterMenuItem("Gaussian Filter 3", new int[][]{
        {1, 4, 1},
        {4, 16, 4},
        {1, 4, 1},
    }));
      menu.add(makeFilterMenuItem("Gaussian Filter 4", new int[][]{
        {1, 4, 1},
        {4, 12, 4},
        {1, 4, 1},
    }));
      menu.add(makeFilterMenuItem("Gaussian Filter 5", new int[][]{
        {1, 3, 1},
        {3, 16, 3},
        {1, 3, 1},
    }));
        return menu;
    }
     private JMenu makeSharpMenu(){
        JMenu menu = new JMenu("Sharp");
        menu.add(makeFilterMenuItem("Sharp Filter 1", new int[][]{
        {0, -2, 0},
        {-2, 11, -2},
        {0, -2, 0},
    }));
      menu.add(makeFilterMenuItem("Sharp Filter 2", new int[][]{
        {0, -1, 0},
        {-1, 5, -1},
        {0, -1, 0},
    }));
        return menu;
    }
     private JMenuItem makeFilterMenuItemCustom(String name){
       JMenuItem item = new JMenuItem(name);
       item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCustomFilterActionPerformed(evt);
            }
        });
       return item;
    }
     private JMenu makeRobertsMenu(){
        JMenu menu = new JMenu("Roberts");
        JMenuItem item;
        
        item = new JMenuItem("Roberts Filter 1");
        item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterRoberts1ActionPerformed(evt);
            }
        });
        menu.add(item);
        
        item = new JMenuItem("Roberts Filter 2");
        item.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemFilterRoberts2ActionPerformed(evt);
            }
        });
        menu.add(item);
        
        return menu;
    }
    private void addAllMenus(){
        jSubMenuMenuFilters.add(makePrevittMenu());
        jSubMenuMenuFilters.add(makeSobelMenu());
        jSubMenuMenuFilters.add(makeBlurMenu());
        jSubMenuMenuFilters.add(makeSharpMenu());
        jSubMenuMenuFilters.add(makeRobertsMenu());
        jSubMenuMenuFilters.add(makeFilterMenuItemCustom("Custom"));
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

        fileChooserLoad = new javax.swing.JFileChooser("./");
        fileChooserSave = new javax.swing.JFileChooser("./");
        jImageSource = new pl.delor.graphprocessing.JImage();
        statsSourceAvg = new pl.delor.graphprocessing.JOutlinedLabel();
        statsSourceCv = new pl.delor.graphprocessing.JOutlinedLabel();
        statsSourceCd = new pl.delor.graphprocessing.JOutlinedLabel();
        jPanelSeparator = new javax.swing.JPanel();
        jImageChanged = new pl.delor.graphprocessing.JImage();
        statsChangedAvg = new pl.delor.graphprocessing.JOutlinedLabel();
        statsChangedCv = new pl.delor.graphprocessing.JOutlinedLabel();
        statsChangedCd = new pl.delor.graphprocessing.JOutlinedLabel();
        jMenuBarMain = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jSubMenuPixelManip = new javax.swing.JMenu();
        jMenuItemToGray = new javax.swing.JMenuItem();
        jMenuItemGrayscale = new javax.swing.JMenuItem();
        jMenuItemBrightness = new javax.swing.JMenuItem();
        jMenuItemInvert = new javax.swing.JMenuItem();
        jMenuItemPower = new javax.swing.JMenuItem();
        jMenuItemGamma = new javax.swing.JMenuItem();
        jMenuItemLog = new javax.swing.JMenuItem();
        jMenuItemBinarization = new javax.swing.JMenuItem();
        jMenuItemErosion = new javax.swing.JMenuItem();
        jMenuItemDilation = new javax.swing.JMenuItem();
        jSubMenuMenuFilters = new javax.swing.JMenu();
        jMenuHistogram = new javax.swing.JMenu();
        jMenuItemHistogramShow = new javax.swing.JMenuItem();
        jMenuItemProjections = new javax.swing.JMenuItem();
        jMenuItemEHistNorm = new javax.swing.JMenuItem();
        jMenuItemHistEqual = new javax.swing.JMenuItem();
        jMenuStats = new javax.swing.JMenu();
        jCheckBoxMenuItemShowStats = new javax.swing.JCheckBoxMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Graphics processing");
        setPreferredSize(new java.awt.Dimension(717, 415));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jImageSource.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jImageSource.setMinimumSize(new java.awt.Dimension(100, 182));
        jImageSource.setLayout(new java.awt.GridLayout(10, 0));

        statsSourceAvg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsSourceAvg.setText("jLabel1");
        statsSourceAvg.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        statsSourceAvg.setOutlineColor(new java.awt.Color(0, 0, 0));
        statsSourceAvg.setStroke(new java.awt.BasicStroke(3.0f));
        jImageSource.add(statsSourceAvg);

        statsSourceCv.setForeground(new java.awt.Color(0, 0, 0));
        statsSourceCv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsSourceCv.setText("jLabel1");
        statsSourceCv.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        statsSourceCv.setOutlineColor(new java.awt.Color(255, 255, 255));
        statsSourceCv.setStroke(new java.awt.BasicStroke(3.0f));
        jImageSource.add(statsSourceCv);

        statsSourceCd.setForeground(new java.awt.Color(0, 0, 0));
        statsSourceCd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsSourceCd.setText("jLabel1");
        statsSourceCd.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        statsSourceCd.setOutlineColor(new java.awt.Color(255, 255, 255));
        statsSourceCd.setStroke(new java.awt.BasicStroke(3.0f));
        jImageSource.add(statsSourceCd);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jImageSource, gridBagConstraints);

        jPanelSeparator.setMinimumSize(new java.awt.Dimension(5, 5));

        javax.swing.GroupLayout jPanelSeparatorLayout = new javax.swing.GroupLayout(jPanelSeparator);
        jPanelSeparator.setLayout(jPanelSeparatorLayout);
        jPanelSeparatorLayout.setHorizontalGroup(
            jPanelSeparatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanelSeparatorLayout.setVerticalGroup(
            jPanelSeparatorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jPanelSeparator, gridBagConstraints);

        jImageChanged.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jImageChanged.setMinimumSize(new java.awt.Dimension(100, 182));
        jImageChanged.setLayout(new java.awt.GridLayout(10, 0));

        statsChangedAvg.setForeground(new java.awt.Color(0, 0, 0));
        statsChangedAvg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsChangedAvg.setText("jLabel2");
        statsChangedAvg.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        statsChangedAvg.setOutlineColor(new java.awt.Color(255, 255, 255));
        statsChangedAvg.setStroke(new java.awt.BasicStroke(3.0f));
        jImageChanged.add(statsChangedAvg);

        statsChangedCv.setForeground(new java.awt.Color(0, 0, 0));
        statsChangedCv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsChangedCv.setText("jLabel1");
        statsChangedCv.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        statsChangedCv.setOutlineColor(new java.awt.Color(255, 255, 255));
        statsChangedCv.setStroke(new java.awt.BasicStroke(3.0f));
        jImageChanged.add(statsChangedCv);

        statsChangedCd.setForeground(new java.awt.Color(0, 0, 0));
        statsChangedCd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsChangedCd.setText("jLabel1");
        statsChangedCd.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        statsChangedCd.setOutlineColor(new java.awt.Color(255, 255, 255));
        statsChangedCd.setStroke(new java.awt.BasicStroke(3.0f));
        jImageChanged.add(statsChangedCd);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jImageChanged, gridBagConstraints);

        jMenuFile.setText("File");

        jMenuItemOpen.setText("Load ...");
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemOpen);

        jMenuItemSave.setText("Save ...");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSave);

        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);

        jMenuBarMain.add(jMenuFile);

        jMenuEdit.setText("Edit");

        jSubMenuPixelManip.setText("Pixel manipulations");

        jMenuItemToGray.setText("Grayscale (avg)");
        jMenuItemToGray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemToGrayActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemToGray);

        jMenuItemGrayscale.setText("Grayscale (lum)");
        jMenuItemGrayscale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGrayscaleActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemGrayscale);

        jMenuItemBrightness.setText("Brightness");
        jMenuItemBrightness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBrightnessActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemBrightness);

        jMenuItemInvert.setText("Invert");
        jMenuItemInvert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemInvertActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemInvert);

        jMenuItemPower.setText("Exponent");
        jMenuItemPower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPowerActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemPower);

        jMenuItemGamma.setText("Gamma Correction");
        jMenuItemGamma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGammaActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemGamma);

        jMenuItemLog.setText("Logarithm");
        jMenuItemLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLogActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemLog);

        jMenuItemBinarization.setText("Binarization");
        jMenuItemBinarization.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBinarizationActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemBinarization);

        jMenuItemErosion.setText("Erosion");
        jMenuItemErosion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemErosionActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemErosion);

        jMenuItemDilation.setText("Dilation");
        jMenuItemDilation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDilationActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemDilation);

        jMenuEdit.add(jSubMenuPixelManip);

        jSubMenuMenuFilters.setText("Filters");
        jMenuEdit.add(jSubMenuMenuFilters);

        jMenuBarMain.add(jMenuEdit);

        jMenuHistogram.setText("Charts");

        jMenuItemHistogramShow.setText("Show histogram");
        jMenuItemHistogramShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHistogramShowActionPerformed(evt);
            }
        });
        jMenuHistogram.add(jMenuItemHistogramShow);

        jMenuItemProjections.setText("Show projections");
        jMenuItemProjections.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemProjectionsActionPerformed(evt);
            }
        });
        jMenuHistogram.add(jMenuItemProjections);

        jMenuItemEHistNorm.setText("Histogram Normalization");
        jMenuItemEHistNorm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemEHistNormActionPerformed(evt);
            }
        });
        jMenuHistogram.add(jMenuItemEHistNorm);

        jMenuItemHistEqual.setText("Histogram Equalization");
        jMenuItemHistEqual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemHistEqualActionPerformed(evt);
            }
        });
        jMenuHistogram.add(jMenuItemHistEqual);

        jMenuBarMain.add(jMenuHistogram);

        jMenuStats.setText("Stats");

        jCheckBoxMenuItemShowStats.setSelected(true);
        jCheckBoxMenuItemShowStats.setText("Show stats");
        jCheckBoxMenuItemShowStats.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxMenuItemShowStatsStateChanged(evt);
            }
        });
        jMenuStats.add(jCheckBoxMenuItemShowStats);

        jMenuBarMain.add(jMenuStats);

        jMenuHelp.setText("Help");

        jMenuItemAbout.setText("About");
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemAbout);

        jMenuBarMain.add(jMenuHelp);

        setJMenuBar(jMenuBarMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveImage() {
        if (imageOutput == null) {
            return;
        }

        FileNameExtensionFilter f = new FileNameExtensionFilter("JPG & GIF", "jpg", "jpeg", "gif");
        fileChooserSave.setFileFilter(f);
        if (fileChooserSave.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooserSave.getSelectedFile();
        String ext = getImageExtension(getFileExtension(file));

        try {
            ImageIO.write(imageOutput, ext, file);
            edited = false;
        } catch (IOException ex) {
            Logger.getLogger(MultimediaMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadImage() {
        FileNameExtensionFilter f = new FileNameExtensionFilter("JPG & GIF", "jpg", "jpeg", "gif");
        fileChooserLoad.setFileFilter(f);
        if (fileChooserLoad.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        if (fileChooserLoad.getSelectedFile() == null) {
            return;
        }

        try {
            imageInput = ImageIO.read(fileChooserLoad.getSelectedFile());
            jImageSource.setImage(imageInput);

            imageOutput = copyImage(imageInput);
            jImageChanged.setImage(imageOutput);
            edited = false;
        } catch (IOException ex) {
            Logger.getLogger(MultimediaMainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateStatsLabels();
    }

    private void showPreviewPanel(JPreviewPanel panel) {
        if (panel == null || this.getImageOutput() == null) {
            return;
        }
        JPreview p = new JPreview(this, this.getImageOutput(), panel);
        if (p.getExitStatus() == JPreview.STATUS.OK) {
            this.setChangedImage(p.getImageOutput());
            updateStatsLabels();
        }
        p.dispose();
    }

    private void setChangedImage(BufferedImage image) {
        imageOutput = image;
        jImageChanged.setImage(image);
        edited = true;
    }
    
    String[] getFormatedStats(JImage image){
        Integer avg = image.getAvgBrightness();
        String[] out = {" ", " ", " "};
        if(avg == null) {
            out[0] = "Load some image.";
            return out;
        }

        out[0] = String.format("Avg brightness: (%d, %d, %d)", getRed(avg), getGreen(avg), getBlue(avg));

        int cv = image.getContrastVariance();
        out[1] = String.format("Contrast variance: (%d, %d, %d)", getRed(cv), getGreen(cv), getBlue(cv));
        
        int cd = image.getContrastDynamic();
        out[2] = String.format("Contrast dynamic: (%d, %d, %d)", getRed(cd), getGreen(cd), getBlue(cd));
        
        return out;
    }
    
    private void updateStatsLabels(){
        if(jCheckBoxMenuItemShowStats.isSelected()){
           
            String[] s = getFormatedStats(jImageSource);
            statsSourceAvg.setOutlineColor(Color.white);
            statsSourceAvg.setText(s[0]);
            statsSourceAvg.setVisible(true);
            statsSourceCv.setText(s[1]);
            statsSourceCv.setVisible(true);
            statsSourceCd.setText(s[2]);
            statsSourceCd.setVisible(true);
            
            s = getFormatedStats(jImageChanged);
            statsChangedAvg.setText(s[0]);
            statsChangedAvg.setVisible(true);
            statsChangedCv.setText(s[1]);
            statsChangedCv.setVisible(true);
            statsChangedCd.setText(s[2]);
            statsChangedCd.setVisible(true);
        }else{
            statsSourceAvg.setVisible(false);
            statsSourceCv.setVisible(false);
            statsSourceCd.setVisible(false);
            statsChangedAvg.setVisible(false);
            statsChangedCv.setVisible(false);
            statsChangedCd.setVisible(false);
        }
    }

    private void showHistogramEqualization() {
        if(imageOutput == null) return;
        long [][] cdf = jImageChanged.getCDF();
            
        int minCDF[] = new int[]{(int)cdf[0][255], (int)cdf[1][255], (int)cdf[2][255]};
        for(int c = 0; c < cdf.length; c++) {
            for(int i = 0; i < cdf[c].length; i++) {
                if(cdf[c][i] > 0) {
                    minCDF[c] = (int)cdf[c][i];
                    break;
                }
            }
        }
        showPreviewPanel(new JPreviewPanelProcess("Histogram Equalization", (pixel) -> {
            int r = (cdf[0][255] - cdf[0][0]) > 0 ? (int)(255 * (cdf[0][getRed(pixel)] - minCDF[0])/(cdf[0][255] - minCDF[0])) : (int)cdf[0][0];
            int g = (cdf[1][255] - cdf[1][0]) > 0 ?(int)(255 * (cdf[1][getGreen(pixel)] - minCDF[1])/(cdf[1][255] - minCDF[1])) : (int)cdf[1][0];
            int b = (cdf[2][255] - cdf[2][0]) > 0 ?(int)(255 * (cdf[2][getBlue(pixel)] - minCDF[2])/(cdf[2][255] - minCDF[2])) : (int)cdf[2][0];
            return toRGB(r, g, b);
        }));
    }
    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
        loadImage();
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
        saveImage();
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed
        JOptionPane.showMessageDialog(this,
                "  Graphics processing.\n\n ver. 1.0\n\n by Sebastian Kucharczyk");
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    /* Simple panel */
    private void jMenuItemToGrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemToGrayActionPerformed
        showPreviewPanel(new JPreviewPanelTG());
    }//GEN-LAST:event_jMenuItemToGrayActionPerformed

    /* Panel with slider */
    private void jMenuItemBrightnessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBrightnessActionPerformed
        showPreviewPanel(new JPreviewPanelBrightness());
    }//GEN-LAST:event_jMenuItemBrightnessActionPerformed

    /* Simple panel with pixel processing in lambda expression */
    private void jMenuItemInvertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemInvertActionPerformed
        showPreviewPanel(new JPreviewPanelProcess("Invert", (pixel) -> {
            return toRGB(255 - getRed(pixel), 255 - getGreen(pixel), 255 - getBlue(pixel));
        }));
    }//GEN-LAST:event_jMenuItemInvertActionPerformed

    private void jMenuItemGrayscaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGrayscaleActionPerformed
        showPreviewPanel(new JPreviewPanelProcess("Grayscale (luminance)", (pixel) -> {
            int luminance = toLuminance(getRed(pixel), getGreen(pixel), getBlue(pixel));
            return toRGB(luminance, luminance, luminance);
        }));
    }//GEN-LAST:event_jMenuItemGrayscaleActionPerformed

    private void jCheckBoxMenuItemShowStatsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemShowStatsStateChanged
        updateStatsLabels();
    }//GEN-LAST:event_jCheckBoxMenuItemShowStatsStateChanged

    private void jMenuItemPowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPowerActionPerformed
        showPreviewPanel(new JPreviewPanelPower());
    }//GEN-LAST:event_jMenuItemPowerActionPerformed

    private void jMenuItemGammaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGammaActionPerformed
        showPreviewPanel(new JPreviewPanelGamma());
    }//GEN-LAST:event_jMenuItemGammaActionPerformed

    private void jMenuItemLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLogActionPerformed
        double l_256 = Math.log(1+255); 
        showPreviewPanel(new JPreviewPanelProcess("Logarithm", (pixel) -> {
            int r = (int)(255 * Math.log(getRed(pixel))/l_256);
            int g = (int)(255 * Math.log(getGreen(pixel))/l_256);
            int b = (int)(255 * Math.log(getBlue(pixel))/l_256);
            return toRGB(r, g, b);
        }));
    }//GEN-LAST:event_jMenuItemLogActionPerformed

    private void jMenuItemBinarizationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBinarizationActionPerformed
        showPreviewPanel(new JPreviewPanelBinary());
    }//GEN-LAST:event_jMenuItemBinarizationActionPerformed

    private void jMenuItemHistogramShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHistogramShowActionPerformed
        if(imageOutput == null) return;
        JHistogramDialog hist = new JHistogramDialog(jImageChanged.getHistogram());
        hist.setVisible(true);
    }//GEN-LAST:event_jMenuItemHistogramShowActionPerformed

    private void jMenuItemProjectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemProjectionsActionPerformed
        if(imageOutput == null) return;
        JProjectionsFrame proj = new JProjectionsFrame(jImageChanged.getProjections());
        proj.setVisible(true);
    }//GEN-LAST:event_jMenuItemProjectionsActionPerformed

    private void jMenuItemEHistNormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEHistNormActionPerformed
        showPreviewPanel(new JPreviewPanelHistExtend());
    }//GEN-LAST:event_jMenuItemEHistNormActionPerformed
           
    private void jMenuItemHistEqualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHistEqualActionPerformed
        showHistogramEqualization();
    }//GEN-LAST:event_jMenuItemHistEqualActionPerformed

    private void jMenuItemErosionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemErosionActionPerformed
        showPreviewPanel(new JPreviewPanelMorf(1));
    }//GEN-LAST:event_jMenuItemErosionActionPerformed

    private void jMenuItemDilationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDilationActionPerformed
        showPreviewPanel(new JPreviewPanelMorf(0));
    }//GEN-LAST:event_jMenuItemDilationActionPerformed
    /* Filters events */
    private void jMenuItemFilterActionPerformed(java.awt.event.ActionEvent evt, String name) {
        showPreviewPanel(new JPreviewPanelFilter(name, filters.get(name)));
    } 
    private void jMenuItemCustomFilterActionPerformed(java.awt.event.ActionEvent evt) {
        showPreviewPanel(new JPreviewPanelFilterCustom());
    } 
    private void jMenuItemFilterRoberts1ActionPerformed(java.awt.event.ActionEvent evt) {
        showPreviewPanel(new JPreviewPanelFilterRoberts("Roberts Filter 1"));
    } 
    private void jMenuItemFilterRoberts2ActionPerformed(java.awt.event.ActionEvent evt) {
        showPreviewPanel(new JPreviewPanelFilterRoberts2("Roberts Filter 2"));
    } 
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MultimediaMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MultimediaMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MultimediaMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MultimediaMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MultimediaMainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser fileChooserLoad;
    private javax.swing.JFileChooser fileChooserSave;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemShowStats;
    private pl.delor.graphprocessing.JImage jImageChanged;
    private pl.delor.graphprocessing.JImage jImageSource;
    private javax.swing.JMenuBar jMenuBarMain;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenu jMenuHistogram;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemBinarization;
    private javax.swing.JMenuItem jMenuItemBrightness;
    private javax.swing.JMenuItem jMenuItemDilation;
    private javax.swing.JMenuItem jMenuItemEHistNorm;
    private javax.swing.JMenuItem jMenuItemErosion;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemGamma;
    private javax.swing.JMenuItem jMenuItemGrayscale;
    private javax.swing.JMenuItem jMenuItemHistEqual;
    private javax.swing.JMenuItem jMenuItemHistogramShow;
    private javax.swing.JMenuItem jMenuItemInvert;
    private javax.swing.JMenuItem jMenuItemLog;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemPower;
    private javax.swing.JMenuItem jMenuItemProjections;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemToGray;
    private javax.swing.JMenu jMenuStats;
    private javax.swing.JPanel jPanelSeparator;
    private javax.swing.JMenu jSubMenuMenuFilters;
    private javax.swing.JMenu jSubMenuPixelManip;
    private pl.delor.graphprocessing.JOutlinedLabel statsChangedAvg;
    private pl.delor.graphprocessing.JOutlinedLabel statsChangedCd;
    private pl.delor.graphprocessing.JOutlinedLabel statsChangedCv;
    private pl.delor.graphprocessing.JOutlinedLabel statsSourceAvg;
    private pl.delor.graphprocessing.JOutlinedLabel statsSourceCd;
    private pl.delor.graphprocessing.JOutlinedLabel statsSourceCv;
    // End of variables declaration//GEN-END:variables
}
