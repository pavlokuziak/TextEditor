import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditor extends JFrame implements ActionListener {

    JTextArea textArea;
    JScrollPane scrollPane;
    JLabel fontLabel;
    JSpinner fontSizeSpinner;
    JButton fontColorButton;
    JComboBox fontBox;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem saveItem;
    JMenuItem openItem;
    JMenuItem exitItem;

    TextEditor () {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(500, 500);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN,50));

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        fontLabel = new JLabel("Font: ");

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));
            }
        });

        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");

        //------menu bar area

        menuBar = new JMenuBar();
        fileMenu = new JMenu("FIle");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);



        //------menu bar area

        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(scrollPane);
        this.setVisible(true);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==fontColorButton) {
            JColorChooser colorChooser = new JColorChooser();

            Color color = colorChooser.showDialog(null, "Choose a color", Color.BLACK);

            textArea.setForeground(color);
        }

        if(e.getSource()==fontBox) {
            textArea.setFont(new Font((String)fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }

        if(e.getSource() == openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(""));
            FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(fileNameExtensionFilter);

            int response = fileChooser.showOpenDialog(null);

            if(response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;
                try {
                    fileIn = new Scanner(file);
                    if (file.isFile()) {
                        while (fileIn.hasNextLine()) {
                            String line = fileIn.nextLine() + "\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                finally {
                    fileIn.close();
                }
            }
        }

        if(e.getSource() == saveItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(""));

            int response = fileChooser.showSaveDialog(null);

            if(response == JFileChooser.APPROVE_OPTION) {
                File file;
                PrintWriter printWriter = null;
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                try {
                    printWriter = new PrintWriter(file);
                    printWriter.println(textArea.getText());
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                finally {
                    printWriter.close();
                }
            }
        }

        if(e.getSource() == exitItem) {
            System.exit(0);
        }
    }
}
