/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.delor.graphprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import static pl.delor.graphprocessing.GP.toRGB;

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

    /**
     * Creates new form MultimediaMainFrame
     */
    public MultimediaMainFrame() {
        initComponents();
        if (appIconString != null) {
            ImageIcon img = getIconFromBase64(appIconString);
            setIconImage(img.getImage());
        }
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
        jPanelSeparator = new javax.swing.JPanel();
        jImageChanged = new pl.delor.graphprocessing.JImage();
        jButtonOpen = new javax.swing.JButton();
        jMenuBarMain = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuEdit = new javax.swing.JMenu();
        jMenuItemToGray = new javax.swing.JMenuItem();
        jMenuItemBrightness = new javax.swing.JMenuItem();
        jMenuItemInvert = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Graphics processing");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jImageSource.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jImageSourceLayout = new javax.swing.GroupLayout(jImageSource);
        jImageSource.setLayout(jImageSourceLayout);
        jImageSourceLayout.setHorizontalGroup(
            jImageSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 261, Short.MAX_VALUE)
        );
        jImageSourceLayout.setVerticalGroup(
            jImageSourceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 261, Short.MAX_VALUE)
        );

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

        javax.swing.GroupLayout jImageChangedLayout = new javax.swing.GroupLayout(jImageChanged);
        jImageChanged.setLayout(jImageChangedLayout);
        jImageChangedLayout.setHorizontalGroup(
            jImageChangedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 261, Short.MAX_VALUE)
        );
        jImageChangedLayout.setVerticalGroup(
            jImageChangedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 261, Short.MAX_VALUE)
        );

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

        jMenuItemToGray.setText("To gray");
        jMenuItemToGray.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemToGrayActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemToGray);

        jMenuItemBrightness.setText("Brightness");
        jMenuItemBrightness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemBrightnessActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemBrightness);

        jMenuItemInvert.setText("Invert");
        jMenuItemInvert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemInvertActionPerformed(evt);
            }
        });
        jMenuEdit.add(jMenuItemInvert);

        jMenuBarMain.add(jMenuEdit);

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
    }

    private static void showPreviewPanel(MultimediaMainFrame mf, JPreviewPanel panel) {
        if (mf == null || panel == null || mf.getImageOutput() == null) {
            return;
        }
        JPreview p = new JPreview(mf, mf.getImageOutput(), panel);
        if (p.getExitStatus() == JPreview.STATUS.OK) {
            mf.setChangedImage(p.getImageOutput());
        }
        p.dispose();
    }

    private void setChangedImage(BufferedImage image) {
        imageOutput = image;
        jImageChanged.setImage(image);
        edited = true;
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
            int r = getRed(pixel);
            int g = getGreen(pixel);
            int b = getBlue(pixel);
            return toRGB(255 - r, 255 - g, 255 - b);
        }));
    }//GEN-LAST:event_jMenuItemInvertActionPerformed

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
    private pl.delor.graphprocessing.JImage jImageChanged;
    private pl.delor.graphprocessing.JImage jImageSource;
    private javax.swing.JMenuBar jMenuBarMain;
    private javax.swing.JMenu jMenuEdit;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemBrightness;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemInvert;
    private javax.swing.JMenuItem jMenuItemOpen;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenuItem jMenuItemToGray;
    private javax.swing.JPanel jPanelSeparator;
    // End of variables declaration//GEN-END:variables
}
