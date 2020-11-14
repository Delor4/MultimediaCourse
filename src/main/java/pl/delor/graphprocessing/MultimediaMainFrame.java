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
      menu.add(makeFilterMenuItem("Gauss Filter 2", new int[][]{
        {1, 3, 1},
        {3, 9, 3},
        {1, 3, 1},
    }));
      menu.add(makeFilterMenuItem("Gauss Filter 3", new int[][]{
        {1, 4, 1},
        {4, 16, 4},
        {1, 4, 1},
    }));
      menu.add(makeFilterMenuItem("Gauss Filter 4", new int[][]{
        {1, 4, 1},
        {4, 12, 4},
        {1, 4, 1},
    }));
      menu.add(makeFilterMenuItem("Gauss Filter 5", new int[][]{
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
        statsSourceAvg = new javax.swing.JLabel();
        statsSourceCv = new javax.swing.JLabel();
        statsSourceCd = new javax.swing.JLabel();
        jPanelSeparator = new javax.swing.JPanel();
        jImageChanged = new pl.delor.graphprocessing.JImage();
        statsChangedAvg = new javax.swing.JLabel();
        statsChangedCv = new javax.swing.JLabel();
        statsChangedCd = new javax.swing.JLabel();
        jButtonOpen = new javax.swing.JButton();
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
        jSubMenuMenuFilters = new javax.swing.JMenu();
        jMenuHistogram = new javax.swing.JMenu();
        jMenuItemHistogramShow = new javax.swing.JMenuItem();
        jMenuItemProjections = new javax.swing.JMenuItem();
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
        jImageSource.add(statsSourceAvg);

        statsSourceCv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsSourceCv.setText("jLabel1");
        jImageSource.add(statsSourceCv);

        statsSourceCd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsSourceCd.setText("jLabel1");
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

        statsChangedAvg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsChangedAvg.setText("jLabel2");
        jImageChanged.add(statsChangedAvg);

        statsChangedCv.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsChangedCv.setText("jLabel1");
        jImageChanged.add(statsChangedCv);

        statsChangedCd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statsChangedCd.setText("jLabel1");
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

        jButtonOpen.setText("Open");
        jButtonOpen.setToolTipText("Open file ...");
        jButtonOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        getContentPane().add(jButtonOpen, gridBagConstraints);

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

        jMenuItemPower.setText("Power");
        jMenuItemPower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemPowerActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemPower);

        jMenuItemGamma.setText("Gamma");
        jMenuItemGamma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemGammaActionPerformed(evt);
            }
        });
        jSubMenuPixelManip.add(jMenuItemGamma);

        jMenuItemLog.setText("Log");
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

    private void showPreviewPanel(MultimediaMainFrame mf, JPreviewPanel panel) {
        if (mf == null || panel == null || mf.getImageOutput() == null) {
            return;
        }
        JPreview p = new JPreview(mf, mf.getImageOutput(), panel);
        if (p.getExitStatus() == JPreview.STATUS.OK) {
            mf.setChangedImage(p.getImageOutput());
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
        String[] out = {"", "", ""};
        if(avg == null) {
            out[0] = "Load some image.";
            return out;
        }

        out[0] = String.format("Avg brightness: (%d, %d, %d)", getRed(avg), getGreen(avg), getBlue(avg));

        int cv = image.getContrastVariance();
        out[1] = String.format("contrast variance: (%d, %d, %d)", getRed(cv), getGreen(cv), getBlue(cv));
        
        int cd = image.getContrastDynamic();
        out[2] = String.format("contrast dynamic: (%d, %d, %d)", getRed(cd), getGreen(cd), getBlue(cd));
        
        return out;
    }
    
    private void updateStatsLabels(){
        if(jCheckBoxMenuItemShowStats.isSelected()){
           
            String[] s = getFormatedStats(jImageSource);
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
    private int[][] getHistogramData() {        
        if(imageOutput == null) return null;
        
        int hist[][] = new int[3][256];
        for (int y = 0; y < imageOutput.getHeight(); y++) {
            for (int x = 0; x < imageOutput.getWidth(); x++) {
                int pixel = imageOutput.getRGB(x, y);
                hist[0][getRed(pixel)]++;
                hist[1][getGreen(pixel)]++;
                hist[2][getBlue(pixel)]++;
            }
        }
        return hist;
    }
    private List<int[][]> getProjectionsData() {
        if(imageOutput == null) return null;
        
        List<int[][]> projList = new ArrayList<int[][]>();
        
        int projHoriz[][] = new int[3][imageOutput.getWidth()];
        int projVert[][] = new int[3][imageOutput.getHeight()];
        
        for (int y = 0; y < imageOutput.getHeight(); y++) {
            for (int x = 0; x < imageOutput.getWidth(); x++) {
                int pixel = imageOutput.getRGB(x, y);
                if(getRed(pixel) > 127){
                    projHoriz[0][x]++;
                    projVert[0][y]++;
                }
                if(getGreen(pixel) > 127){
                    projHoriz[1][x]++;
                    projVert[1][y]++;
                }
                if(getBlue(pixel) > 127){
                    projHoriz[2][x]++;
                    projVert[2][y]++;
                }
            }
        }
        projList.add(projHoriz);
        projList.add(projVert);
        return projList;
    }

    private void jButtonOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenActionPerformed
        loadImage();
    }//GEN-LAST:event_jButtonOpenActionPerformed

    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenActionPerformed
        jButtonOpenActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemOpenActionPerformed

    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed
        saveImage();
    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed
        JOptionPane.showMessageDialog(this,
                "  Graphics processing.\n\n by Sebastian Kucharczyk");
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    /* Simple panel */
    private void jMenuItemToGrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemToGrayActionPerformed
        showPreviewPanel(this, new JPreviewPanelTG());
    }//GEN-LAST:event_jMenuItemToGrayActionPerformed

    /* Panel with slider */
    private void jMenuItemBrightnessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBrightnessActionPerformed
        showPreviewPanel(this, new JPreviewPanelBrightness());
    }//GEN-LAST:event_jMenuItemBrightnessActionPerformed

    /* Simple panel with pixel processing in lambda expression */
    private void jMenuItemInvertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemInvertActionPerformed
        showPreviewPanel(this, new JPreviewPanelProcess("Invert", (pixel) -> {
            return toRGB(255 - getRed(pixel), 255 - getGreen(pixel), 255 - getBlue(pixel));
        }));
    }//GEN-LAST:event_jMenuItemInvertActionPerformed

    private void jMenuItemGrayscaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGrayscaleActionPerformed
        showPreviewPanel(this, new JPreviewPanelProcess("Grayscale (luminance)", (pixel) -> {
            int luminance = toLuminance(getRed(pixel), getGreen(pixel), getBlue(pixel));
            return toRGB(luminance, luminance, luminance);
        }));
    }//GEN-LAST:event_jMenuItemGrayscaleActionPerformed

    private void jCheckBoxMenuItemShowStatsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemShowStatsStateChanged
        updateStatsLabels();
    }//GEN-LAST:event_jCheckBoxMenuItemShowStatsStateChanged

    private void jMenuItemPowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPowerActionPerformed
        showPreviewPanel(this, new JPreviewPanelPower());
    }//GEN-LAST:event_jMenuItemPowerActionPerformed

    private void jMenuItemGammaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemGammaActionPerformed
        showPreviewPanel(this, new JPreviewPanelGamma());
    }//GEN-LAST:event_jMenuItemGammaActionPerformed

    private void jMenuItemLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLogActionPerformed
        double l_256 = Math.log(1+255); 
        showPreviewPanel(this, new JPreviewPanelProcess("Logarithm", (pixel) -> {
            int r = (int)(255 * Math.log(getRed(pixel))/l_256);
            int g = (int)(255 * Math.log(getGreen(pixel))/l_256);
            int b = (int)(255 * Math.log(getBlue(pixel))/l_256);
            return toRGB(r, g, b);
        }));
    }//GEN-LAST:event_jMenuItemLogActionPerformed

    private void jMenuItemBinarizationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemBinarizationActionPerformed
        showPreviewPanel(this, new JPreviewPanelBinary());
    }//GEN-LAST:event_jMenuItemBinarizationActionPerformed

    private void jMenuItemHistogramShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemHistogramShowActionPerformed
        if(imageOutput == null) return;
        JHistogramDialog hist = new JHistogramDialog(getHistogramData());
        hist.setVisible(true);
    }//GEN-LAST:event_jMenuItemHistogramShowActionPerformed

    private void jMenuItemProjectionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemProjectionsActionPerformed
        if(imageOutput == null) return;
        JProjectionsFrame proj = new JProjectionsFrame(getProjectionsData());
        proj.setVisible(true);
    }//GEN-LAST:event_jMenuItemProjectionsActionPerformed
    /* Filters events */
    private void jMenuItemFilterActionPerformed(java.awt.event.ActionEvent evt, String name) {
        showPreviewPanel(this, new JPreviewPanelFilter(name, filters.get(name)));
    } 
    private void jMenuItemCustomFilterActionPerformed(java.awt.event.ActionEvent evt) {
        showPreviewPanel(this, new JPreviewPanelFilterCustom());
    } 
    private void jMenuItemFilterRoberts1ActionPerformed(java.awt.event.ActionEvent evt) {
        showPreviewPanel(this, new JPreviewPanelFilterRoberts("Roberts Filter 1"));
    } 
    private void jMenuItemFilterRoberts2ActionPerformed(java.awt.event.ActionEvent evt) {
        showPreviewPanel(this, new JPreviewPanelFilterRoberts2("Roberts Filter 2"));
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
    private javax.swing.JButton jButtonOpen;
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
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemGamma;
    private javax.swing.JMenuItem jMenuItemGrayscale;
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
    private javax.swing.JLabel statsChangedAvg;
    private javax.swing.JLabel statsChangedCd;
    private javax.swing.JLabel statsChangedCv;
    private javax.swing.JLabel statsSourceAvg;
    private javax.swing.JLabel statsSourceCd;
    private javax.swing.JLabel statsSourceCv;
    // End of variables declaration//GEN-END:variables
}
