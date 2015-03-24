/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashtag;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Driver;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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

/**
 *
 * @author stephanie
 */
public class GUI extends javax.swing.JFrame {

    String FILE_PATH;
    JFrame ASTFrame;
    SyntaxHighlighter PARSER;
    boolean CONTENT_CHANGED; //bandera para ver si hay cambios en el textpane

    public GUI() {
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        final StyleContext cont = StyleContext.getDefaultStyleContext();

        final AttributeSet keyword = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#6ac9c8"));
        final AttributeSet plain = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.WHITE);
        final AttributeSet comment = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#5e5e5e"));
        final AttributeSet string = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#e4944b"));
        final AttributeSet function = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#b4be5a"));
        final AttributeSet number = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#c46563"));
        final AttributeSet operator = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#cc99cc"));
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            @Override
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException { //cuando se insertan caracteres.
                super.insertString(offset, str, a);
                String text = getText(0, getLength());
                PARSER = new SyntaxHighlighter(new java.io.StringReader(text));
                Token val;
                try {

                    while ((val = PARSER.yylex()) != null) {
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
                                setCharacterAttributes(val.start, val.length, number, true);
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
                    JOptionPane.showMessageDialog(rootPane, "Oops! Exception triggered");
                }
            }

            @Override
            public void remove(int offs, int len) throws BadLocationException { //para cuando se borra algun caracter
                super.remove(offs, len);
                String text = getText(0, getLength());
                PARSER = new SyntaxHighlighter(new java.io.StringReader(text));
                Token val;
                try {

                    while ((val = PARSER.yylex()) != null) {
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
                                setCharacterAttributes(val.start, val.length, number, true);
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
                    JOptionPane.showMessageDialog(rootPane, "Oops! Exception triggered");
                }

            }
        };

        TabStop[] tabs = new TabStop[30];
        for (int j = 0; j < tabs.length; j++) {
            tabs[j] = new TabStop((j + 1) * 28);
        }

        TabSet tabSet = new TabSet(tabs);
        AttributeSet paraSet = cont.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabSet);

        this.codeTextPane.setStyledDocument(doc);
        this.codeTextPane.setParagraphAttributes(paraSet, false);
        LinePainter lp = new LinePainter(codeTextPane, Color.decode("#3a444e"));
        jsp.setViewportView(codeTextPane);
        LineNumber tln = new LineNumber(codeTextPane);
        tln.setBackground(Color.decode("#2b303b"));
        jsp.setRowHeaderView(tln);

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

        this.codeTextPane.getDocument().addDocumentListener(documentListener);
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

        consolePane = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jsp = new javax.swing.JScrollPane();
        codeTextPane = new javax.swing.JTextPane();
        statusPanel = new javax.swing.JPanel();
        status = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        openMU = new javax.swing.JMenuItem();
        saveMU = new javax.swing.JMenuItem();
        saveAsMU = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitMU = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        clearTextMU = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        parseMU = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        showAstMU = new javax.swing.JMenuItem();
        toggleConsoleMU = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        docMenuItem = new javax.swing.JMenuItem();

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

        codeTextPane.setBackground(new java.awt.Color(43, 48, 59));
        codeTextPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        codeTextPane.setFont(new java.awt.Font("Menlo", 0, 11)); // NOI18N
        codeTextPane.setForeground(new java.awt.Color(255, 255, 255));
        codeTextPane.setCaretColor(new java.awt.Color(255, 255, 255));
        codeTextPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codeTextPaneKeyTyped(evt);
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

        jMenu1.setText("File");

        openMU.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMU.setText("Open file...");
        openMU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMUActionPerformed(evt);
            }
        });
        jMenu1.add(openMU);

        saveMU.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMU.setText("Save");
        saveMU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMUActionPerformed(evt);
            }
        });
        jMenu1.add(saveMU);

        saveAsMU.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMU.setText("Save As...");
        saveAsMU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMUActionPerformed(evt);
            }
        });
        jMenu1.add(saveAsMU);
        jMenu1.add(jSeparator1);

        exitMU.setText("Exit");
        exitMU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMUActionPerformed(evt);
            }
        });
        jMenu1.add(exitMU);

        menuBar.add(jMenu1);

        jMenu2.setText("Edit");

        clearTextMU.setText("Clear Text");
        clearTextMU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearTextMUActionPerformed(evt);
            }
        });
        jMenu2.add(clearTextMU);

        menuBar.add(jMenu2);

        jMenu5.setText("Analyze");

        parseMU.setText("Parse source code");
        parseMU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parseMUActionPerformed(evt);
            }
        });
        jMenu5.add(parseMU);

        menuBar.add(jMenu5);

        jMenu3.setText("View");

        showAstMU.setText("Show AST");
        showAstMU.setEnabled(false);
        showAstMU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAstMUActionPerformed(evt);
            }
        });
        jMenu3.add(showAstMU);

        toggleConsoleMU.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LESS, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        toggleConsoleMU.setText("Toggle console");
        toggleConsoleMU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleConsoleMUActionPerformed(evt);
            }
        });
        jMenu3.add(toggleConsoleMU);

        menuBar.add(jMenu3);

        jMenu4.setText("About");

        docMenuItem.setText("Manual");
        docMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                docMenuItemActionPerformed(evt);
            }
        });
        jMenu4.add(docMenuItem);

        menuBar.add(jMenu4);

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

    /*
     The following are methods used in code above
     */
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
            GUI.console.setText("Error: " + e.getMessage());
        }
        return rn;
    }

    private int getColumn(int pos, JTextComponent editor) {
        try {
            return pos - Utilities.getRowStart(editor, pos) + 1;
        } catch (BadLocationException e) {
            GUI.console.setText("Error: " + e.getMessage());
        }
        return -1;
    }

    private void save(String path, String content) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(content);
            writer.close();
        } catch (IOException ex) {
            GUI.console.setText("Error: " + ex.getMessage());
        }
    }

    private void resetComponents() {
        GUI.console.setText("");
        ASTFrame = null;
        this.showAstMU.setEnabled(false);
    }

    void toggleConsole() {
        if (this.consolePane.isVisible()) {
            this.consolePane.setVisible(false);
            this.setVisible(true);
        } else {
            this.consolePane.setVisible(true);
            this.setVisible(true);
        }
    }

    private void openMUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMUActionPerformed
        JFileChooser jf = new JFileChooser();
        if (jf.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            FILE_PATH = jf.getSelectedFile().getPath();
            this.codeTextPane.setText("");
            FileReader fr = null;
            try {
                fr = new FileReader(FILE_PATH);
            } catch (FileNotFoundException ex) {
                GUI.console.setText("Error: " + ex.getMessage());
            }
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
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_openMUActionPerformed

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

    private void clearTextMUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearTextMUActionPerformed
        if (!this.codeTextPane.getText().isEmpty()) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "This will clear all text. Are you sure?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.codeTextPane.setText("");
            }
        }

    }//GEN-LAST:event_clearTextMUActionPerformed

    private void showAstMUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAstMUActionPerformed
        if (this.ASTFrame == null) {
            JOptionPane.showMessageDialog(rootPane, "Error. Null JFrame");
        } else {
            this.ASTFrame.setVisible(true);
        }
    }//GEN-LAST:event_showAstMUActionPerformed

    private void toggleConsoleMUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleConsoleMUActionPerformed
        toggleConsole();
    }//GEN-LAST:event_toggleConsoleMUActionPerformed

    private void codeTextPaneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codeTextPaneKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_codeTextPaneKeyTyped

    private void saveMUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMUActionPerformed
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
            if (CONTENT_CHANGED) { //si hay cambios, entonces
                int dialogResult = JOptionPane.showConfirmDialog(null, "File will be overwritten. Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    save(FILE_PATH, this.codeTextPane.getText());
                    CONTENT_CHANGED = false;
                }
            } else {
                GUI.console.setText("No changes have been made");
            }

        }
    }//GEN-LAST:event_saveMUActionPerformed

    private void saveAsMUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMUActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save As");
        int userSelection = fc.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fc.getSelectedFile();
            FILE_PATH = fileToSave.getAbsolutePath();
            save(FILE_PATH, this.codeTextPane.getText());
            this.setTitle(FILE_PATH + " - Hashtag Compiler");
        }
    }//GEN-LAST:event_saveAsMUActionPerformed

    private void exitMUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMUActionPerformed
        if (CONTENT_CHANGED) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Close without saving?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        } else {
            System.exit(1);
        }
    }//GEN-LAST:event_exitMUActionPerformed

    private void parseMUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parseMUActionPerformed
        resetComponents();
        try {
            if (this.codeTextPane.getText().isEmpty()) {
                GUI.console.setText("Please provide a valid source code first.\nTry loading it from a file or write it in the text area above.");
                consolePane.setVisible(true);
                this.setVisible(true);
            } else {
                Parser p = new Parser(new Lexer(new java.io.StringReader(this.codeTextPane.getText()))); //asi no depende del archivo.
                p.parse();
                if (p.errors == 0 && GUI.console.getText().isEmpty()) {
                    GUI.console.setText("Number of errors: 0\n"
                            + "Finished parsing successfully\n"
                            + "Generating AST...");
                    consolePane.setVisible(true);
                    this.setVisible(true);
                    if (this.ASTFrame == null) {
                        String tree = p.AST.get(0).print("", true);
                        this.showAstMU.setEnabled(true);
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
                        this.showAstMU.setEnabled(true);
                        //ASTFrame.setVisible(true);
                        GUI.console.setText(GUI.console.getText() + " Completed. Go to View > Show AST for visualization.");
                    }

                } else {
                    GUI.console.setText(GUI.console.getText() + "Number of errors: " + p.errors);
                    consolePane.setVisible(true);
                    this.setVisible(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Oops! Exception triggered");
        }
    }//GEN-LAST:event_parseMUActionPerformed

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
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem clearTextMU;
    private javax.swing.JTextPane codeTextPane;
    public static javax.swing.JTextArea console;
    private javax.swing.JScrollPane consolePane;
    private javax.swing.JMenuItem docMenuItem;
    private javax.swing.JMenuItem exitMU;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JScrollPane jsp;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMU;
    private javax.swing.JMenuItem parseMU;
    private javax.swing.JMenuItem saveAsMU;
    private javax.swing.JMenuItem saveMU;
    private javax.swing.JMenuItem showAstMU;
    private javax.swing.JLabel status;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenuItem toggleConsoleMU;
    // End of variables declaration//GEN-END:variables
}
