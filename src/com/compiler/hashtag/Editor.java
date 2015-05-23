package com.compiler.hashtag;

import com.compiler.syntaxhighlight.*;
import com.compiler.ast.TreeAnalyzer;
import com.compiler.tools.LineEnumerator;
import com.compiler.tools.LinePainter;

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

    private String filePath;
    private JFrame treeFrame;
    private JPopupMenu popupMenu;
    private SyntaxHighlighter syntaxHighlighter;
    private boolean contentChanged;

    private final StyleContext styleContext = StyleContext.getDefaultStyleContext();

    private final AttributeSet KEYWORD = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#72a9cd"));
    private final AttributeSet PLAIN = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#CCCCCC"));
    private final AttributeSet COMMENT = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#656565"));
    private final AttributeSet STRING = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#f99b36"));
    private final AttributeSet FUNCTION = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#b4d864"));
    private final AttributeSet NUMBER = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#aa6164"));
    private final AttributeSet OPERATOR = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#9876aa"));
    private final AttributeSet READ = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.decode("#f7f36f"));

    //todo: highlight in red the lines that have errors
    public Editor() {
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        fillRecentItems();

        // <editor-fold desc="Syntax highlighter code">
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException { //cuando se insertan caracteres.
                super.insertString(offset, str, a);
                String text = getText(0, getLength());
                syntaxHighlighter = new SyntaxHighlighter(new java.io.StringReader(text));
                Token token;
                try {
                    while ((token = syntaxHighlighter.yylex()) != null) {
                        switch (token.type) {
                            case TokenConstants.KEYWORD:
                                setCharacterAttributes(token.start, token.length, KEYWORD, true);
                                break;
                            case TokenConstants.COMMENT:
                                setCharacterAttributes(token.start, token.length, COMMENT, true);
                                break;
                            case TokenConstants.CADENA:
                                setCharacterAttributes(token.start, token.length, STRING, true);
                                break;
                            case TokenConstants.FUNCTION:
                                setCharacterAttributes(token.start, token.length, FUNCTION, true);
                                break;
                            case TokenConstants.NUMBER:
                                setCharacterAttributes(token.start, token.length, NUMBER, true);
                                break;
                            case TokenConstants.OPERATOR:
                                setCharacterAttributes(token.start, token.length, OPERATOR, true);
                                break;
                            case TokenConstants.READ:
                                setCharacterAttributes(token.start, token.length, READ, true);
                                break;
                            case TokenConstants.CARACTER:
                                setCharacterAttributes(token.start, token.length, STRING, true);
                                break;
                            default:
                                setCharacterAttributes(token.start, token.length, PLAIN, true);
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
                syntaxHighlighter = new SyntaxHighlighter(new java.io.StringReader(text));
                Token token;
                try {
                    while ((token = syntaxHighlighter.yylex()) != null) {
                        switch (token.type) {
                            case TokenConstants.KEYWORD:
                                setCharacterAttributes(token.start, token.length, KEYWORD, true);
                                break;
                            case TokenConstants.COMMENT:
                                setCharacterAttributes(token.start, token.length, COMMENT, true);
                                break;
                            case TokenConstants.CADENA:
                                setCharacterAttributes(token.start, token.length, STRING, true);
                                break;
                            case TokenConstants.FUNCTION:
                                setCharacterAttributes(token.start, token.length, FUNCTION, true);
                                break;
                            case TokenConstants.NUMBER:
                                setCharacterAttributes(token.start, token.length, NUMBER, true);
                                break;
                            case TokenConstants.OPERATOR:
                                setCharacterAttributes(token.start, token.length, OPERATOR, true);
                                break;
                            case TokenConstants.READ:
                                setCharacterAttributes(token.start, token.length, READ, true);
                                break;
                            case TokenConstants.CARACTER:
                                setCharacterAttributes(token.start, token.length, STRING, true);
                                break;
                            default:
                                setCharacterAttributes(token.start, token.length, PLAIN, true);
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
        AttributeSet paraSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabSet);

        this.codeTextPane.setStyledDocument(doc);
        this.codeTextPane.setParagraphAttributes(paraSet, false);
        LinePainter linePainter = new LinePainter(codeTextPane, Color.decode("#323e41"));
        scrollPane.setViewportView(codeTextPane);
        LineEnumerator tln = new LineEnumerator(codeTextPane);
        tln.setBackground(Color.decode("#1b2426"));
        scrollPane.setRowHeaderView(tln);

        this.codeTextPane.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                status.setText("Line " + getRow(e.getDot(), (JTextComponent) e.getSource()) + ", Column " + getColumn(e.getDot(), (JTextComponent) e.getSource()));
            }
        });
        contentChanged = false;
        this.consolePane.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem2 = new javax.swing.JMenuItem();
        consolePane = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
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
        codeTextPane.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
        codeTextPane.setForeground(new java.awt.Color(204, 204, 204));
        codeTextPane.setCaretColor(new java.awt.Color(255, 255, 255));
        codeTextPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                codeTextPaneMouseClicked(evt);
            }
        });
        scrollPane.setViewportView(codeTextPane);

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
                        .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
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
                                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
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

    private int getRow(int pos, JTextComponent editor) {
        int row = (pos == 0) ? 1 : 0;
        try {
            int offs = pos;
            while (offs > 0) {
                offs = Utilities.getRowStart(editor, offs) - 1;
                row++;
            }
        } catch (BadLocationException e) {
            Editor.console.setText("Error: " + e.getMessage());
        }
        return row;
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
            filePath = file;
            this.codeTextPane.setText("");
            FileReader fr = null;
            try {
                fr = new FileReader(filePath);
                BufferedReader br = new BufferedReader(fr);
                StringBuilder str = new StringBuilder();
                String text;
                try {
                    while ((text = br.readLine()) != null) {
                        str.append(text.replaceAll("\\t", "    ")).append("\n");
                    }
                    str.deleteCharAt(str.length() - 1); //removes empty line at the end.
                    this.codeTextPane.setText(str.toString());
                    this.setTitle(filePath + " - Hashtag Compiler");
                    contentChanged = false;
                    resetComponents();
                    this.codeTextPane.setCaretPosition(0);

                    //save for recent opened files...
                    Writer writer = null;
                    try {
                        writer = new BufferedWriter(new FileWriter("cache/recent.cache", true));
                        writer.write(filePath + "\n");
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

    private void save(String path, String content) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(content);
            writer.close();
            Editor.console.setText("File saved successfully.");
            if (!consolePane.isVisible()) {
                consolePane.setVisible(true);
                this.setVisible(true);
            }
        } catch (IOException ex) {
            Editor.console.setText("Error: " + ex.getMessage());
        }
    }

    private void resetComponents() {
        Editor.console.setText("");
        if (treeFrame != null) {
            treeFrame.setVisible(false);
            treeFrame = null;
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

    private int getLineEnd(String text, int lineNo) {
        int lineEnd = 0;
        for (int i = 1; i <= lineNo && lineEnd + 1 < text.length(); i++) {
            lineEnd = text.indexOf('\n', lineEnd + 1);
        }
        return lineEnd;
    }

    private void openMenuItemActionPerformed(ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            open(fc.getSelectedFile().getPath());
        }
    }

    private void docMenuItemActionPerformed(ActionEvent evt) {
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
    }

    private void clearTextMenuItemActionPerformed(ActionEvent evt) {
        if (!this.codeTextPane.getText().isEmpty()) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "This will clear all text. Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.codeTextPane.setText("");
                console.setText("");
            }
        } else {
            console.setText("");
        }

    }

    private void showTreeMenuItemActionPerformed(ActionEvent evt) {
        if (this.treeFrame == null) {
            JOptionPane.showMessageDialog(rootPane, "Unexpected error. JFrame is null");
        } else {
            this.treeFrame.setVisible(true);
        }
    }

    private void toggleConsoleMenuItemActionPerformed(ActionEvent evt) {
        toggleConsole();
    }

    private void saveMenuItemActionPerformed(ActionEvent evt) {
        if (filePath == null) { //es porque aun no ha abierto un archivo y quiere guardar lo que hay en el textpane...
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Save As");
            int userSelection = fc.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fc.getSelectedFile();
                filePath = fileToSave.getAbsolutePath();
                save(filePath, this.codeTextPane.getText());
                this.setTitle(filePath + " - Hashtag Compiler");
            }
        } else { //ya hay una referencia de un archivo abierto y quiere guardarlo.
            int dialogResult = JOptionPane.showConfirmDialog(null, "File will be overwritten. Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                save(filePath, this.codeTextPane.getText());
                contentChanged = false;
            }

        }
    }

    private void saveAsMenuItemActionPerformed(ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fc.setDialogTitle("Save As");
        int userSelection = fc.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fc.getSelectedFile();
            filePath = fileToSave.getAbsolutePath();
            save(filePath, this.codeTextPane.getText());
            this.setTitle(filePath + " - Hashtag Compiler");

            //add newly created file to recents...
            Writer writer = null;
            try {
                writer = new BufferedWriter(new FileWriter("cache/recent.cache", true));
                writer.write(filePath + "\n");
                writer.close();
            } catch (IOException ex) {
                Editor.console.setText("Error: " + ex.getMessage());
            }

        }
    }

    private void exitMenuItemActionPerformed(ActionEvent evt) {
        if (contentChanged) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Close without saving?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        } else {
            System.exit(1);
        }
    }

    private void parseMenuItemActionPerformed(ActionEvent evt) {
        resetComponents();
        /*
        BONUS: highlight in red the lines that have errors.
        DefaultHighlighter.HighlightPainter err = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
        try {
            //highlight line 5
            codeTextPane.getHighlighter().addHighlight(getLineEnd(codeTextPane.getText(), 4) + 1, getLineEnd(codeTextPane.getText(), 5), err);
            //highlight line 13
            codeTextPane.getHighlighter().addHighlight(getLineEnd(codeTextPane.getText(), 12) + 1, getLineEnd(codeTextPane.getText(), 13), err);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        */
        try {
            if (this.codeTextPane.getText().isEmpty()) {
                Editor.console.setText("Please provide a valid source code first.\nTry loading it from a file or write it in the text area above.");
                if (!consolePane.isVisible()) {
                    consolePane.setVisible(true);
                    this.setVisible(true);
                }
            } else {
                Parser parser = new Parser(new Lexer(new java.io.StringReader(this.codeTextPane.getText()))); //asi no depende del archivo.
                parser.parse();
                if (parser.errors == 0 && parser.fatal == 0 && Editor.console.getText().isEmpty()) {
                    Editor.console.setText("Number of errors: 0\n"
                            + "Finished parsing successfully\n"
                            + "\n> Generating the AST...");

                    if (!consolePane.isVisible()) {
                        consolePane.setVisible(true);
                        this.setVisible(true);
                    }
                    if (this.treeFrame == null) {
                        String tree = parser.root.toString("", true);
                        this.showTreeMenuItem.setEnabled(true);
                        JTextArea info = new JTextArea(tree);
                        info.setFont(new Font("Consolas", Font.PLAIN, 12));
                        info.setEditable(false);
                        JScrollPane scroll = new JScrollPane(info);
                        treeFrame = new JFrame();
                        treeFrame.setSize(new Dimension(550, 700));
                        treeFrame.setResizable(true);
                        treeFrame.add(new JPanel() {

                            {
                                setBackground(Color.WHITE);
                            }
                        });
                        treeFrame.getContentPane().add(scroll);
                        treeFrame.setTitle("[AST] - " + filePath);
                        this.showTreeMenuItem.setEnabled(true);
                        Editor.console.setText(Editor.console.getText() + "\nCompleted. Go to 'View > Show AST' if you want to visualize the tree.");
                    }
                    Editor.console.setText(Editor.console.getText() + "\n\n> Traversing the tree to find other possible errors...\n");
                    TreeAnalyzer analyzer = new TreeAnalyzer(parser.root); //aqui se le manda el AST...
                    System.out.println("errors: " + TreeAnalyzer.semanticErrors);

                } else {
                    Editor.console.setText(Editor.console.getText() + "\nNumber of syntax errors found: " + parser.errors);
                    Editor.console.setText(Editor.console.getText() + "\nNumber of unexpected errors: " + parser.fatal);
                    if (!consolePane.isVisible()) {
                        consolePane.setVisible(true);
                        this.setVisible(true);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Oops! Exception triggered\n" + e.getMessage());
        }
    }

    // <editor-fold desc="init right click popup menu">
    private void initPopupMenu() {
        if (popupMenu == null) {
            popupMenu = new JPopupMenu();
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
            popupMenu.add(copy);
            popupMenu.add(cut);
            popupMenu.add(paste);
        }
    }
    // </editor-fold>

    private void codeTextPaneMouseClicked(java.awt.event.MouseEvent evt) {
        initPopupMenu();
        if (SwingUtilities.isRightMouseButton(evt)) {
            popupMenu.show(this.codeTextPane, evt.getX(), evt.getY());
        }
    }

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

    // <editor-fold desc = "Variables declaration">
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
    private javax.swing.JScrollPane scrollPane;
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
    //</editor-fold>
}
