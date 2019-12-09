package ru.bams22.translate.view;


import ru.bams22.translate.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainForm {
    JFrame jFrame;
    TranslateOptions translateOptions; {
        translateOptions = new TranslateOptions();
        translateOptions.translater = new YandexTranslater();
        translateOptions.inputLang = Lang.RUSSIAN;
        translateOptions.outputLang = Lang.RUSSIAN;
        translateOptions.inputFileCharset = FileCharset.UTF_8;
        translateOptions.outputFileCharset = FileCharset.UTF_8;
    }

    JFrame getJFrame() {
        int wight = 600;
        int height = 350;
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        jFrame.setBounds(dimension.width/2 - wight/2 , dimension.height/2 - height/2, wight, height);
        return jFrame;
    }

    void init() {
        jFrame = getJFrame();
        jFrame.setTitle("Translator");
        JPanel jPanel = new JPanel();
        jFrame.add(jPanel);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("."));
        JButton jButtonChooser = new JButton("chooser");
        jButtonChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jFileChooser.showOpenDialog(jPanel);
            }
        });
        JButton jButtonRun = new JButton("run");
        jButtonRun.setEnabled(false);
        jButtonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButtonRun.setEnabled(false);
                new Translate(translateOptions).work();
                JOptionPane.showMessageDialog(null, "It's works!", "It's works!", JOptionPane.INFORMATION_MESSAGE);
                jButtonRun.setEnabled(true);
            }
        });
        JLabel jLabelFile = new JLabel("File don't selected");
        jFileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translateOptions.inputFile = jFileChooser.getSelectedFile();
                String str = translateOptions.inputFile.getPath();
                if (str.length() > 25) {
                    str = "..." + str.substring(str.length() - 22);
                }
                jLabelFile.setText(str);
                check(jButtonRun);
            }
        });

        FileCharset[] charsets = {FileCharset.UTF_8, FileCharset.windows_1251};
        JComboBox<FileCharset> cbInputCharset = new JComboBox<>(charsets);
//        JComboBox<FileCharset> cbIOutputCharset = new JComboBox<>(charsets);
        cbInputCharset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translateOptions.inputFileCharset = (FileCharset) ((JComboBox<Lang>)e.getSource()).getSelectedItem();
            }
        });

//        cbIOutputCharset.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                translateOptions.outputFileCharset = (FileCharset) ((JComboBox<Lang>)e.getSource()).getSelectedItem();
//            }
//        });

        Lang[] langs = {Lang.RUSSIAN, Lang.ENGLISH};
        JComboBox<Lang> cbInputLang = new JComboBox<>(langs);
        JComboBox<Lang> cbOutputLang = new JComboBox<>(langs);
        cbInputLang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translateOptions.inputLang = (Lang) ((JComboBox<Lang>)e.getSource()).getSelectedItem();
                check(jButtonRun);
            }
        });
        cbOutputLang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                translateOptions.outputLang = (Lang) ((JComboBox<Lang>)e.getSource()).getSelectedItem();
                check(jButtonRun);
            }
        });
        GridBagLayout gridBagLayout = new GridBagLayout();
        jPanel.setLayout(gridBagLayout);

        GridBagConstraints constraintsLabelFile = new GridBagConstraints();
        constraintsLabelFile.weightx = 0;
        constraintsLabelFile.weighty = 0;
        constraintsLabelFile.gridx = 0;
        constraintsLabelFile.gridy = 0;
        constraintsLabelFile.gridwidth = 3;
        constraintsLabelFile.gridheight = 1;
        jPanel.add(jLabelFile, constraintsLabelFile);

        GridBagConstraints constraintsButtonChooser = new GridBagConstraints();
        constraintsButtonChooser.weightx = 0;
        constraintsButtonChooser.weighty = 0;
        constraintsButtonChooser.gridx = 4;
        constraintsButtonChooser.gridy = 0;
        constraintsButtonChooser.gridwidth = 1;
        constraintsButtonChooser.gridheight = 1;

        jPanel.add(jButtonChooser, constraintsButtonChooser);

        GridBagConstraints constraintsLabelInputCharset = new GridBagConstraints();
        constraintsLabelInputCharset.weightx = 0;
        constraintsLabelInputCharset.weighty = 0;
        constraintsLabelInputCharset.gridx = 0;
        constraintsLabelInputCharset.gridy = 2;
        constraintsLabelInputCharset.gridwidth = 3;
        constraintsLabelInputCharset.gridheight = 1;

        jPanel.add(new JLabel("Charset input file: "), constraintsLabelInputCharset);

        GridBagConstraints constraintsCbInputCharset = new GridBagConstraints();
        constraintsCbInputCharset.weightx = 0;
        constraintsCbInputCharset.weighty = 0;
        constraintsCbInputCharset.gridx = 4;
        constraintsCbInputCharset.gridy = 2;
        constraintsCbInputCharset.gridwidth = 1;
        constraintsCbInputCharset.gridheight = 1;

        jPanel.add(cbInputCharset, constraintsCbInputCharset);

        GridBagConstraints constraintsLabelOutputCharset = new GridBagConstraints();
        constraintsLabelOutputCharset.weightx = 0;
        constraintsLabelOutputCharset.weighty = 0;
        constraintsLabelOutputCharset.gridx = 0;
        constraintsLabelOutputCharset.gridy = 4;
        constraintsLabelOutputCharset.gridwidth = 3;
        constraintsLabelOutputCharset.gridheight = 1;

//        jPanel.add(new JLabel("Charset output file: "), constraintsLabelOutputCharset);

//        GridBagConstraints constraintsCbOutputCharset = new GridBagConstraints();
//        constraintsCbOutputCharset.weightx = 0;
//        constraintsCbOutputCharset.weighty = 0;
//        constraintsCbOutputCharset.gridx = 4;
//        constraintsCbOutputCharset.gridy = 4;
//        constraintsCbOutputCharset.gridwidth = 1;
//        constraintsCbOutputCharset.gridheight = 1;
//
//        jPanel.add(cbIOutputCharset, constraintsCbOutputCharset);

        GridBagConstraints constraintsLabelInputLang = new GridBagConstraints();
        constraintsLabelInputLang.weightx = 0;
        constraintsLabelInputLang.weighty = 0;
        constraintsLabelInputLang.gridx = 0;
        constraintsLabelInputLang.gridy = 6;
        constraintsLabelInputLang.gridwidth = 3;
        constraintsLabelInputLang.gridheight = 1;

        jPanel.add(new JLabel("Input language: "), constraintsLabelInputLang);

        GridBagConstraints constraintsCbInputLang = new GridBagConstraints();
        constraintsCbInputLang.weightx = 0;
        constraintsCbInputLang.weighty = 0;
        constraintsCbInputLang.gridx = 4;
        constraintsCbInputLang.gridy = 6;
        constraintsCbInputLang.gridwidth = 1;
        constraintsCbInputLang.gridheight = 1;

        jPanel.add(cbInputLang, constraintsCbInputLang);

        GridBagConstraints constraintsLabelOutputLang = new GridBagConstraints();
        constraintsLabelOutputLang.weightx = 0;
        constraintsLabelOutputLang.weighty = 0;
        constraintsLabelOutputLang.gridx = 0;
        constraintsLabelOutputLang.gridy = 8;
        constraintsLabelOutputLang.gridwidth = 3;
        constraintsLabelOutputLang.gridheight = 1;

        jPanel.add(new JLabel("Output language: "), constraintsLabelOutputLang);

        GridBagConstraints constraintsCbOutputLang = new GridBagConstraints();
        constraintsCbOutputLang.weightx = 0;
        constraintsCbOutputLang.weighty = 0;
        constraintsCbOutputLang.gridx = 4;
        constraintsCbOutputLang.gridy = 8;
        constraintsCbOutputLang.gridwidth = 1;
        constraintsCbOutputLang.gridheight = 1;

        jPanel.add(cbOutputLang, constraintsCbOutputLang);

        GridBagConstraints constraintsButtonRun = new GridBagConstraints();
        constraintsButtonRun.weightx = 0;
        constraintsButtonRun.weighty = 0;
        constraintsButtonRun.gridx = 0;
        constraintsButtonRun.gridy = 10;
        constraintsButtonRun.gridwidth = 5;
        constraintsButtonRun.gridheight = 1;

        jPanel.add(jButtonRun, constraintsButtonRun);

        jPanel.revalidate();
    }

    private void check(JButton jButton) {
        if (translateOptions.inputLang == translateOptions.outputLang || translateOptions.inputFile == null) {
            jButton.setEnabled(false);
        } else {
            jButton.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
        mainForm.init();
    }
}

