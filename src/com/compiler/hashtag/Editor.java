package com.compiler.hashtag;

import com.compiler.syntaxhighlight.*;
import com.compiler.tools.TreeAnalyzer;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.swing.text.Utilities;

public class Editor extends javax.swing.JFrame {

    private String FILE_PATH;
    private JFrame ASTFrame;
    private JPopupMenu popup;
    private SyntaxHighlighter syntax;
    private boolean CONTENT_CHANGED; //flag to see if there are changes in the jtextpane

    public Editor() {
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        fillRecentItems();

        final StyleContext cont = StyleContext.getDefaultStyleContext();

        final AttributeSet keyword = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#72a9cd"));
        final AttributeSet plain = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#CCCCCC"));
        final AttributeSet comment = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#656565"));
        final AttributeSet string = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#f99b36"));
        final AttributeSet function = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#b4d864"));
        final AttributeSet number = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#f7f36f"));
        final AttributeSet operator = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#cc99cc"));

        // <editor-fold desc="Syntax highlighter code">
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException { //cuando se insertan caracteres.
                super.insertString(offset, str, a);
                String text = getText(0, getLength());
                syntax = new SyntaxHighlighter(new java.io.StringReader(text));
                Token val;
                try {

                    while ((val = syntax.yylex()) != null) {
                        switch (val.type) {
                            case TokenType.KEYWORD:
                                setCharacterAttributes(val.start, val.length, keyword, true);
                                break;
                            case TokenType.COMMENT:
                                setCharacterAttributes(val.start, val.length, comment, true);
                                break;
                            case TokenType.CADENA:
                                setCharacterAttributes(val.start, val.length, string, true);
                                break;
                            case TokenType.FUNCTION:
                                setCharacterAttributes(val.start, val.length, function, true);
                                break;
                            case TokenType.NUMBER:
                                setCharacterAttributes(val.start, val.length, plain, true);
                                break;
                            case TokenType.OPERATOR:
                                setCharacterAttributes(val.start, val.length, operator, true);
                                break;
                            case TokenType.READ:
                                setCharacterAttributes(val.start, val.length, number, true);
                                break;
                            default:
                                setCharacterAttributes(val.start, val.length, plain, true);
                                break;
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Oops! Exception triggered\n" + ex.getMessage());
                }
            }

            @Override
            public void remove(int offs, int len) throws BadLocationException { //para cuando se borra algun caracter
                super.remove(offs, len);
                String text = getText(0, getLength());
                syntax = new SyntaxHighlighter(new java.io.StringReader(text));
                Token val;
                try {

                    while ((val = syntax.yylex()) != null) {
                        switch (val.type) {
                            case TokenType.KEYWORD:
                                setCharacterAttributes(val.start, val.length, keyword, true);
                                break;
                            case TokenType.COMMENT:
                                setCharacterAttributes(val.start, val.length, comment, true);
                                break;
                            case TokenType.CADENA:
                                setCharacterAttributes(val.start, val.length, string, true);
                                break;
                            case TokenType.FUNCTION:
                                setCharacterAttributes(val.start, val.length, function, true);
                                break;
                            case TokenType.NUMBER:
                                setCharacterAttributes(val.start, val.length, plain, true);
                                break;
                            case TokenType.OPERATOR:
                                setCharacterAttributes(val.start, val.length, operator, true);
                                break;
                            case TokenType.READ:
                                setCharacterAttributes(val.start, val.length, number, true);
                                break;
                            default:
                                setCharacterAttributes(val.start, val.length, plain, true);
                                break;
                        }
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Oops! Exception triggered\n" + ex.getMessage());
                }
            }
        };
        // </editor-fold>

        TabStop[] tabs = new TabStop[30];
        for (int j = 0; j < tabs.length; j++) {
            tabs[j] = new TabStop((j + 1) * 28);
        }

        TabSet tabSet = new TabSet(tabs);
        AttributeSet paraSet = cont.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabSet);

        this.codeTextPane.setStyledDocument(doc);
        this.codeTextPane.setParagraphAttributes(paraSet, false);
        LinePainter lp = new LinePainter(codeTextPane, Color.decode("#323e41"));
        jsp.setViewportView(codeTextPane);
        LineNumber tln = new LineNumber(codeTextPane);
        tln.setBackground(Color.decode("#1b2426"));
        jsp.setRowHeaderView(tln);

/*
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
            }

            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                setContentChange(true);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                setContentChange(true);
            }
        };
*/
        //this.codeTextPane.getDocument().addDocumentListener(documentListener);
        this.codeTextPane.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                status.setText("Line " + getRow(e.getDot(), (JTextComponent) e.getSource()) + ", Column " + getColumn(e.getDot(), (JTextComponent) e.getSource()));
            }
        });
        CONTENT_CHANGED = false;
        this.consolePane.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem2 = new javax.swing.JMenuItem();
        consolePane = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jsp = new javax.swing.JScrollPane();
        codeTextPane = new javax.swing.JTextPane();
        statusPanel = new javax.swing.JPanel();
        status = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        openRecentMenuItem = new javax.swing.JMenu();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        separator = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        clearTextMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        showTreeMenuItem = new javax.swing.JMenuItem();
        toggleConsoleMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        parseMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        docMenuItem = new javax.swing.JMenuItem();

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hashtag Compiler");

        console.setEditable(false);
        console.setBackground(new java.awt.Color(40, 40, 40));
        console.setColumns(20);
        console.setFont(new java.awt.Font("Lucida Console", 0, 12)); // NOI18N
        console.setForeground(new java.awt.Color(255, 255, 255));
        console.setRows(5);
        console.setToolTipText("");
        console.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        consolePane.setViewportView(console);

        codeTextPane.setBackground(new java.awt.Color(27, 36, 38));
        codeTextPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        codeTextPane.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        codeTextPane.setForeground(new java.awt.Color(204, 204, 204));
        codeTextPane.setCaretColor(new java.awt.Color(255, 255, 255));
        codeTextPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                codeTextPaneMouseClicked(evt);
            }
        });
        jsp.setViewportView(codeTextPane);

        statusPanel.setBackground(new java.awt.Color(83, 83, 83));
        statusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        status.setBackground(new java.awt.Color(255, 255, 255));
        status.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        status.setForeground(new java.awt.Color(255, 255, 255));
        status.setText("Linea 1, Columna 1");
        status.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
                statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(status, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
                statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(statusPanelLayout.createSequentialGroup()
                                .addComponent(status)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        fileMenu.setText("File");

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setText("Open file...");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        openRecentMenuItem.setText("Open recent");
        fileMenu.add(openRecentMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMenuItem.setText("Save As...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(separator);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        clearTextMenuItem.setText("Clear Text");
        clearTextMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearTextMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(clearTextMenuItem);

        menuBar.add(editMenu);

        viewMenu.setText("View");

        showTreeMenuItem.setText("Show AST");
        showTreeMenuItem.setEnabled(false);
        showTreeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showTreeMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(showTreeMenuItem);

        toggleConsoleMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LESS, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        toggleConsoleMenuItem.setText("Toggle console");
        toggleConsoleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleConsoleMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(toggleConsoleMenuItem);

        menuBar.add(viewMenu);

        toolsMenu.setText("Tools");

        parseMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        parseMenuItem.setText("Parse");
        parseMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parseMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(parseMenuItem);

        menuBar.add(toolsMenu);

        helpMenu.setText("Help");

        docMenuItem.setText("Manual");
        docMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(docMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jsp, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(479, 479, 479)
                                .addComponent(jLabel6)
                                .addGap(2, 421, Short.MAX_VALUE))
                        .addComponent(consolePane)
                        .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jsp, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel6)
                                .addGap(0, 0, 0)
                                .addComponent(consolePane, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold desc="Fill recent items">
    private void fillRecentItems() {
        FileReader fr = null;
        ArrayList<String> unique = new ArrayList();
        try {
            fr = new FileReader("cache/recent.cache");
            BufferedReader br = new BufferedReader(fr);
            String text;
            File f;
            try {
                while ((text = br.readLine()) != null) {
                    if (!unique.contains(text)) {
                        f = new File(text);
                        if (f.exists()) {
                            unique.add(text);
                        }
                    }
                }
                if (!unique.isEmpty()) {
                    for (final String file : unique) {
                        JMenuItem item = new JMenuItem(file);
                        item.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                open(file);
                            }
                        });
                        openRecentMenuItem.add(item);
                    }
                    JMenuItem clear = new JMenuItem("Clear items");
                    clear.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            openRecentMenuItem.removeAll();
                            JMenuItem c = new JMenuItem("Clear items");
                            c.setEnabled(false);
                            openRecentMenuItem.add(c);
                            revalidate();
                            repaint();
                            Writer writer = null;
                            try {
                                writer = new BufferedWriter(new FileWriter("cache/recent.cache"));
                                writer.write("");
                                writer.close();
                            } catch (IOException ex) {
                                Editor.console.setText("Error: " + ex.getMessage());
                            }
                        }
                    });

                    openRecentMenuItem.add(new JSeparator());
                    openRecentMenuItem.add(clear);
                } else {
                    JMenuItem clear = new JMenuItem("Clear items");
                    clear.setEnabled(false);
                    openRecentMenuItem.add(clear);
                    this.revalidate();
                    this.repaint();
                }
            } catch (IOException ex) {
                Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
            }
            fr.close();
        } catch (FileNotFoundException ex) {
            Editor.console.setText("Error: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    //</editor-fold>

    private void setContentChange(boolean b) {
        CONTENT_CHANGED = b;
    }

    private int getRow(int pos, JTextComponent editor) {
        int rn = (pos == 0) ? 1 : 0;
        try {
            int offs = pos;
            while (offs > 0) {
                offs = Utilities.getRowStart(editor, offs) - 1;
                rn++;
            }
        } catch (BadLocationException e) {
            Editor.console.setText("Error: " + e.getMessage());
        }
        return rn;
    }

    private int getColumn(int pos, JTextComponent editor) {
        try {
            return pos - Utilities.getRowStart(editor, pos) + 1;
        } catch (BadLocationException e) {
            Editor.console.setText("Error: " + e.getMessage());
        }
        return -1;
    }

    private void open(String file) {
        String extension = "";

        int i = file.lastIndexOf('.');
        if (i > 0) {
            extension = file.substring(i + 1);
        }
        if (extension.equalsIgnoreCase("ht")) {
            FILE_PATH = file;
            this.codeTextPane.setText("");
            FileReader fr = null;
            try {
                fr = new FileReader(FILE_PATH);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder str = new StringBuilder();
                String text;
                try {
                    while ((text = br.readLine()) != null) {
                        str.append(text.replaceAll("\\t", "    ")).append("\n");
                    }
                    str.deleteCharAt(str.length() - 1); //removes empty line at the end.
                    this.codeTextPane.setText(str.toString());
                    this.setTitle(FILE_PATH + " - Hashtag Compiler");
                    CONTENT_CHANGED = false;
                    resetComponents();
                    this.codeTextPane.setCaretPosition(0);

                    //save for recent opened files...
                    Writer writer = null;
                    try {
                        writer = new BufferedWriter(new FileWriter("cache/recent.cache", true));
                        writer.write(FILE_PATH + "\n");
                        writer.close();
                    } catch (IOException ex) {
                        Editor.console.setText("Error: " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(getRootPane(), "IOException caught", "Error", JOptionPane.ERROR_MESSAGE);
                }
                fr.close();
            } catch (FileNotFoundException ex) {
                Editor.console.setText("Error: " + ex.getMessage());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(getRootPane(), "IOException caught", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(getRootPane(), "File type not supported", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    void save(String path, String content) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(content);
            writer.close();
        } catch (IOException ex) {
            Editor.console.setText("Error: " + ex.getMessage());
        }
    }

    void resetComponents() {
        Editor.console.setText("");
        if (ASTFrame != null) {
            ASTFrame.setVisible(false);
            ASTFrame = null;
        }
        this.showTreeMenuItem.setEnabled(false);
    }

    private void toggleConsole() {
        if (this.consolePane.isVisible()) {
            this.consolePane.setVisible(false);
            this.setVisible(true);
        } else {
            this.consolePane.setVisible(true);
            this.setVisible(true);
        }
    }

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            open(fc.getSelectedFile().getPath());
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void docMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_docMenuItemActionPerformed
        try {
            File document = new File("doc/manual.pdf");
            if (document.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(document);
                } else {
                    JOptionPane.showMessageDialog(null, "Not supported");
                }

            } else {
                JOptionPane.showMessageDialog(null, "File not found");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_docMenuItemActionPerformed

    private void clearTextMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearTextMenuItemActionPerformed
        if (!this.codeTextPane.getText().isEmpty()) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "This will clear all text. Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.codeTextPane.setText("");
                console.setText("");
            }
        } else {
            console.setText("");
        }

    }//GEN-LAST:event_clearTextMenuItemActionPerformed

    private void showTreeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showTreeMenuItemActionPerformed
        if (this.ASTFrame == null) {
            JOptionPane.showMessageDialog(rootPane, "Unexpected error. JFrame is null");
        } else {
            this.ASTFrame.setVisible(true);
        }
    }//GEN-LAST:event_showTreeMenuItemActionPerformed

    private void toggleConsoleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleConsoleMenuItemActionPerformed
        toggleConsole();
    }//GEN-LAST:event_toggleConsoleMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        if (FILE_PATH == null) { //es porque aun no ha abierto un archivo y quiere guardar lo que hay en el textpane...
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Save As");
            int userSelection = fc.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fc.getSelectedFile();
                FILE_PATH = fileToSave.getAbsolutePath();
                save(FILE_PATH, this.codeTextPane.getText());
                this.setTitle(FILE_PATH + " - Hashtag Compiler");
            }
        } else { //ya hay una referencia de un archivo abierto y quiere guardarlo.
            int dialogResult = JOptionPane.showConfirmDialog(null, "File will be overwritten. Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                save(FILE_PATH, this.codeTextPane.getText());
                CONTENT_CHANGED = false;
            }

        }
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fc.setDialogTitle("Save As");
        int userSelection = fc.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fc.getSelectedFile();
            FILE_PATH = fileToSave.getAbsolutePath();
            save(FILE_PATH, this.codeTextPane.getText());
            this.setTitle(FILE_PATH + " - Hashtag Compiler");

            //add newly created file to recents...
            Writer writer = null;
            try {
                writer = new BufferedWriter(new FileWriter("cache/recent.cache", true));
                writer.write(FILE_PATH + "\n");
                writer.close();
            } catch (IOException ex) {
                Editor.console.setText("Error: " + ex.getMessage());
            }

        }
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        if (CONTENT_CHANGED) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Close without saving?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        } else {
            System.exit(1);
        }
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void parseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parseMenuItemActionPerformed
        resetComponents();
        try {
            if (this.codeTextPane.getText().isEmpty()) {
                Editor.console.setText("Please provide a valid source code first.\nTry loading it from a file or write it in the text area above.");
                if (!consolePane.isVisible()) {
                    consolePane.setVisible(true);
                    this.setVisible(true);
                }
            } else {
                Parser p = new Parser(new Lexer(new java.io.StringReader(this.codeTextPane.getText()))); //asi no depende del archivo.
                p.parse();
                if (p.errors == 0 && p.fatal == 0 && Editor.console.getText().isEmpty()) {
                    Editor.console.setText("Number of errors: 0\n"
                            + "Finished parsing successfully\n"
                            + "Generating AST...");

                    if (!consolePane.isVisible()) {
                        consolePane.setVisible(true);
                        this.setVisible(true);
                    }
                    if (this.ASTFrame == null) {
                        String tree = p.root.treeToString("", true);
                        this.showTreeMenuItem.setEnabled(true);
                        JTextArea info = new JTextArea(tree);
                        info.setFont(new Font("Consolas", Font.PLAIN, 12));
                        info.setEditable(false);
                        JScrollPane scroll = new JScrollPane(info);
                        ASTFrame = new JFrame();
                        ASTFrame.setSize(new Dimension(550, 700));
                        ASTFrame.setResizable(true);
                        ASTFrame.add(new JPanel() {

                            {
                                setBackground(Color.WHITE);
                            }
                        });
                        ASTFrame.getContentPane().add(scroll);
                        ASTFrame.setTitle("[AST] - " + FILE_PATH);
                        this.showTreeMenuItem.setEnabled(true);
                        Editor.console.setText(Editor.console.getText() + " Completed. Go to View > Show AST for visualization.");
                    }
                    TreeAnalyzer analyzer = new TreeAnalyzer(p.root); //aqui se le manda el AST...

                } else {
                    Editor.console.setText(Editor.console.getText() + "\nNumber of syntax errors found: " + p.errors);
                    Editor.console.setText(Editor.console.getText() + "\nNumber of unexpected errors: " + p.fatal);
                    if (!consolePane.isVisible()) {
                        consolePane.setVisible(true);
                        this.setVisible(true);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Oops! Exception triggered\n" + e.getMessage());
        }
    }//GEN-LAST:event_parseMenuItemActionPerformed

    // <editor-fold desc="init right click popup menu">
    private void initPopupMenu() {
        if (popup == null) {
            popup = new JPopupMenu();
            JMenuItem copy = new JMenuItem("Copy        Ctrl+C");
            JMenuItem cut = new JMenuItem("Cut           Ctrl+X");
            JMenuItem paste = new JMenuItem("Paste        Ctrl+V");
            copy.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (codeTextPane.getSelectedText() != null) {
                        //StringSelection strSelection = new StringSelection(codeTextPane.getSelectedText());
                        //Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                        //clpbrd.setContents(strSelection, null);

                        //use this instead, I was reinventing the wheel...
                        codeTextPane.copy();
                    }

                }
            });

            cut.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (codeTextPane.getSelectedText() != null) {
                        codeTextPane.cut();
                    }

                }
            });

            paste.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    codeTextPane.paste();

                }
            });
            popup.add(copy);
            popup.add(cut);
            popup.add(paste);
        }
    }
    // </editor-fold>

    private void codeTextPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_codeTextPaneMouseClicked
        initPopupMenu();
        if (SwingUtilities.isRightMouseButton(evt)) {
            popup.show(this.codeTextPane, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_codeTextPaneMouseClicked

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Editor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Editor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem clearTextMenuItem;
    private javax.swing.JTextPane codeTextPane;
    public static javax.swing.JTextArea console;
    private javax.swing.JScrollPane consolePane;
    private javax.swing.JMenuItem docMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JScrollPane jsp;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenu openRecentMenuItem;
    private javax.swing.JMenuItem parseMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JPopupMenu.Separator separator;
    private javax.swing.JMenuItem showTreeMenuItem;
    private javax.swing.JLabel status;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenuItem toggleConsoleMenuItem;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
}
