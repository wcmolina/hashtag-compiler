package com.compiler.hashtag;

import com.compiler.ast.Node;
import com.compiler.codegeneration.IntermediateCode;
import com.compiler.util.LineEnumerator;
import com.compiler.util.LinePainter;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.syntaxkits.HashtagSyntaxKit;
import jsyntaxpane.util.Configuration;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

//todo: highlight in red the lines that have errors (update: done, seems to work...)

public class Editor extends JFrame {

    private String filePath;
    private JFrame treeFrame;
    private boolean contentChanged;

    public Editor() {
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        fillRecentItems();

        //finally got it working (JSyntaxPane)
        DefaultSyntaxKit.initKit();
        Configuration config = DefaultSyntaxKit.getConfig(HashtagSyntaxKit.class);
        config.put("DefaultFont", "Consolas 13");
        editorPane.setContentType("text/hashtag");
        initPopupMenu();

        //highlights line where the caret is
        LinePainter lp = new LinePainter(editorPane, Color.decode("0x343D46")); //error line color: #463434

        scrollPane.setViewportView(editorPane);
        LineEnumerator lineEnumerator = new LineEnumerator(editorPane);
        lineEnumerator.setBackground(Color.decode("0x262B35"));
        scrollPane.setRowHeaderView(lineEnumerator);

        editorPane.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                status.setText("Line " + getRow(e.getDot(), (JTextComponent) e.getSource()) + ", Column " + getColumn(e.getDot(), (JTextComponent) e.getSource()));
            }
        });
        contentChanged = false;
        filePath = "";
        consolePane.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem2 = new javax.swing.JMenuItem();
        consolePane = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        editorPane = new javax.swing.JEditorPane();
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
        console.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        console.setForeground(new java.awt.Color(255, 255, 255));
        console.setRows(5);
        console.setTabSize(4);
        console.setToolTipText("");
        console.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));
        consolePane.setViewportView(console);

        editorPane.setBackground(new java.awt.Color(38, 43, 53));
        editorPane.setBorder(null);
        editorPane.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
        editorPane.setForeground(new java.awt.Color(255, 255, 255));
        editorPane.setCaretColor(new java.awt.Color(255, 255, 255));
        scrollPane.setViewportView(editorPane);

        statusPanel.setBackground(new java.awt.Color(57, 57, 57));
        statusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        status.setBackground(new java.awt.Color(255, 255, 255));
        status.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        status.setForeground(new java.awt.Color(255, 255, 255));
        status.setText("Line 1, Column 1");
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
                        .addGroup(layout.createSequentialGroup()
                                .addGap(479, 479, 479)
                                .addComponent(jLabel6)
                                .addGap(2, 421, Short.MAX_VALUE))
                        .addComponent(consolePane)
                        .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scrollPane)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel6)
                                .addGap(0, 0, 0)
                                .addComponent(consolePane, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            clear.setEnabled(false);
                            openRecentMenuItem.add(clear);

                            //remove entries from file
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

                    openRecentMenuItem.addSeparator();
                    openRecentMenuItem.add(clear);
                } else {
                    JMenuItem clear = new JMenuItem("Clear items");
                    clear.setEnabled(false);
                    openRecentMenuItem.add(clear);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, ex.getMessage());
            }
            fr.close();
        } catch (FileNotFoundException ex) {
            Editor.console.setText("Error: " + ex.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
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
            editorPane.setText("");
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
                    editorPane.setText(str.toString());
                    setTitle(filePath + " - Hashtag Compiler");
                    contentChanged = false;
                    reset();
                    editorPane.setCaretPosition(0);

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
                setVisible(true);
            }
        } catch (IOException ex) {
            Editor.console.setText("Error: " + ex.getMessage());
        }
    }

    private void reset() {
        Editor.console.setText("");
        clearHighlights();
        if (treeFrame != null) {
            treeFrame.setVisible(false);
            treeFrame = null;
        }
        showTreeMenuItem.setEnabled(false);
    }

    private void toggleConsole() {
        if (consolePane.isVisible()) {
            consolePane.setVisible(false);
            setVisible(true);
        } else {
            consolePane.setVisible(true);
            setVisible(true);
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
        if (!editorPane.getText().isEmpty()) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "This will clear all text. Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                editorPane.setText("");
                console.setText("");
            }
        } else {
            Editor.console.setText("");
        }

    }

    private void showTreeMenuItemActionPerformed(ActionEvent evt) {
        if (treeFrame == null) {
            JOptionPane.showMessageDialog(rootPane, "Unexpected error. JFrame is null");
        } else {
            treeFrame.setVisible(true);
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
                save(filePath, editorPane.getText());
                setTitle(filePath + " - Hashtag Compiler");
            }
        } else { //ya hay una referencia de un archivo abierto y quiere guardarlo.
            int dialogResult = JOptionPane.showConfirmDialog(null, "File will be overwritten. Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                save(filePath, editorPane.getText());
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
            save(filePath, editorPane.getText());
            setTitle(filePath + " - Hashtag Compiler");

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
        reset();
        DefaultHighlighter.HighlightPainter err = new DefaultHighlighter.DefaultHighlightPainter(Color.decode("#463434")); //#463434
        try {
            if (editorPane.getText().isEmpty()) {
                Editor.console.setText("Please provide a valid source code first.\nTry loading it from a file or write it in the text area above.");
                if (!consolePane.isVisible()) {
                    consolePane.setVisible(true);
                    setVisible(true);
                }
            } else {
                Parser parser = new Parser(new Lexer(new java.io.StringReader(editorPane.getText()))); //asi no depende del archivo.
                parser.parse();
                if (parser.errors == 0 && parser.fatal == 0) {
                    Editor.console.setText("Number of errors: 0\n"
                            + "Finished parsing successfully"
                            + "\n\n> Generating the AST...");

                    if (!consolePane.isVisible()) {
                        consolePane.setVisible(true);
                        setVisible(true);
                    }
                    if (treeFrame == null) {
                        Node.setConnection(null, parser.root);
                        String tree = parser.root.toString("", true);
                        showTreeMenuItem.setEnabled(true);
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
                        treeFrame.setTitle((filePath.isEmpty()) ? "[AST]" : "[AST] - " + filePath);
                        showTreeMenuItem.setEnabled(true);
                        Editor.console.setText(Editor.console.getText() + "\nDone! Go to 'View > Show AST' if you want to visualize the tree.");
                    }
                    Editor.console.setText(Editor.console.getText() + "\n\n> Traversing the tree to find other possible errors...\n");
                    SemanticAnalyzer analyzer = new SemanticAnalyzer();
                    analyzer.traverse(parser.root);
                    System.out.println("errors: " + SemanticAnalyzer.semanticErrors);

                    for (int num : analyzer.getErrorLines()) {
                        try {
                            editorPane.getHighlighter().addHighlight(getLineEnd(editorPane.getText(), num - 1) + 1, getLineEnd(editorPane.getText(), num), err);
                        } catch (BadLocationException e) {
                            JOptionPane.showMessageDialog(rootPane, "Error: something happened while highlighting error lines." + "\n" + e.getMessage());
                        }
                    }

                    System.out.println("trying out intermediate motherfucking code.");
                    IntermediateCode IR = new IntermediateCode();
                    IR.generateIntermediateCode(parser.root);
                    System.out.println(IR.toString());
                } else {
                    Editor.console.setText(Editor.console.getText() + "\nNumber of syntax errors found: " + parser.errors);
                    Editor.console.setText(Editor.console.getText() + "\nNumber of unexpected errors: " + parser.fatal);
                    if (!consolePane.isVisible()) {
                        consolePane.setVisible(true);
                        setVisible(true);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Oops! Exception triggered\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    // <editor-fold desc="init right click popup menu">
    private void initPopupMenu() {
        JMenuItem parse = new JMenuItem("Parse");
        JMenuItem clear = new JMenuItem("Clear highlights");
        parse.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        try {
            parse.setIcon(new ImageIcon("res/img/icons/run-icon.png"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }

        parse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseMenuItemActionPerformed(e);
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearHighlights();
            }
        });
        editorPane.getComponentPopupMenu().addSeparator();
        editorPane.getComponentPopupMenu().add(parse);
        editorPane.getComponentPopupMenu().add(clear);
    }
    // </editor-fold>

    public void clearHighlights() {
        editorPane.getHighlighter().removeAllHighlights();
        LinePainter lp = new LinePainter(editorPane, Color.decode("0x343D46"));
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
    public static javax.swing.JTextArea console;
    private javax.swing.JScrollPane consolePane;
    private javax.swing.JMenuItem docMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JEditorPane editorPane;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenu openRecentMenuItem;
    private javax.swing.JMenuItem parseMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JScrollPane scrollPane;
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
