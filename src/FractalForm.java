import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by User on 21.12.2016.
 */
public class FractalForm extends JFrame {
    private JLabel labelTest;
    private JButton buttonTest;
    private JPanel panelMain;
    private JPanel startInfoPanel;
    private JPanel endInfoPanel;
    private JPanel settingsPanel;
    private JPanel imagePanel;
    private JPanel statisticPanel;
    private JButton buttonDownloadPhoto;
    private JPanel menuPanel;
    private JSpinner spinnerBlockSize;
    private JSpinner spinnerCoefCompress;
    private JComboBox comboBoxBaseImage;
    private JSpinner spinnerCountDecompress;
    private JButton buttonCompress;
    private JPanel panelStartImage;
    private JPanel panelEndImage;
    private JLabel labelTest2;
    private JLabel labelTest3;
    private JMenuBar menuBar;


    ////////
    Graphics g;

    BufferedImage bi = null;
    int[][] pixels;
    int n, m;
    Compress compress;
    List<Rang> rangList;
    Decompress decompress;
    File file;
    ////////

    public FractalForm() {
        $$$setupUI$$$();
        this.setContentPane(panelMain);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        //add menu item
        /*Font font = new Font("Verdana", Font.PLAIN, 11);
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        openItem.setFont(font);
        fileMenu.add(openItem);
        menuBar.add(fileMenu);*/
        this.setJMenuBar(menuBar);


    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        ///
        SpinnerNumberModel model1 = new SpinnerNumberModel(4, 2, 32, 1);
        spinnerBlockSize = new JSpinner(model1);

        ///
        SpinnerNumberModel model2 = new SpinnerNumberModel(20000, 2, 40000, 1);
        spinnerCoefCompress = new JSpinner(model2);

        ///
        SpinnerNumberModel model3 = new SpinnerNumberModel(5, 1, 16, 1);
        spinnerCountDecompress = new JSpinner(model3);

        ///
        String[] baseImage = {
                "Клеточка",
                "Белая",
                "Черная"
        };
        comboBoxBaseImage = new JComboBox(baseImage);

        //
        buttonCompress = new JButton();
        buttonCompress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    bi = ImageIO.read(file);
                } catch (IOException ee) {
                    ee.printStackTrace();
                }

                pixels = new int[n][m];
                //
                int argb = 0;
                Color color;
                int f;

                for (int i = 0; i < n; i++)
                    for (int j = 0; j < m; j++) {
                        //argb = bi.getRGB(j,i);
                        //color = new Color(argb);
                        //f = color.getRed();
                        //pixels[i][j] = f;
                        //pixels[i][j] = getRGBValue(bi, j, i);
                        pixels[i][j] = bi.getRGB(j, i);

                    }

                int r = Integer.valueOf(spinnerBlockSize.getValue().toString());
                int eps = Integer.valueOf(spinnerCoefCompress.getValue().toString());

                String s = "";

                compress = new Compress(pixels, r, eps);

                long t1 = System.currentTimeMillis();
                compress.compressImage();
                rangList = compress.getRangList();
                long t2 = System.currentTimeMillis();
                String ss = "compress time :" + (t2 - t1) / 1000;

                s += ss + " ";
                ////////////////////////////////////
                String pathBase = "D:/университет/диплом/fractImage/";
                String pb;
                if (comboBoxBaseImage.getSelectedIndex() == 0)
                    pb = pathBase + "baseClet.jpg";
                else if (comboBoxBaseImage.getSelectedIndex() == 1)
                    pb = pathBase + "baseWhite.jpg";
                else pb = pathBase + "baseBlack.jpg";

                bi = null;
                try {
                    //bi = ImageIO.read(new File(path2));
                    bi = ImageIO.read(new File(pb));
                } catch (IOException ee) {
                    ee.printStackTrace();
                }

                bi = getGray(bi);

                decompress = new Decompress(rangList, bi, r);

                int col = Integer.valueOf(spinnerCountDecompress.getValue().toString());

                t1 = System.currentTimeMillis();
                bi = decompress.decompressImage(col);
                t2 = System.currentTimeMillis();

                ss = "decompress time :" + (t2 - t1) / 1000;

                s += "\n" + ss + " ";

                g = panelEndImage.getGraphics();
                //g.drawImage(image, 0, 0, null);

                //bi = getGray(bi);
                g.drawImage(bi, 0, 0, null);

                //////////////////////////////////////////
                //записываем rangList  в файл
                //List<Long> longList = new ArrayList<>();
                long longList[] = new long[rangList.size()];
                int ii = 0;
                long d = 0;
                for (Rang rang : rangList) {
                    //преобразование из Rang в число long
                    d = rang.getY0() + (rang.getX0() << 21) + (rang.getK() << 25) + (rang.getAfinn() << 29) + (rang.getY() << 40) + (rang.getX() << 51);
                    //longList.add(d);
                    longList[ii] = d;
                    ii++;
                }

                String pathBat = "D:/университет/диплом/fractImage/bat.bat";
                File fi = new File(pathBat);

                try {
                    //проверяем, что если файл не существует то создаем его
                    if (!fi.exists()) {
                        fi.createNewFile();
                    }

                    //PrintWriter обеспечит возможности записи в файл
                    PrintWriter out = new PrintWriter(fi.getAbsoluteFile());

                    try {
                        //Записываем текст у файл
                        out.print(longList);
                    } finally {
                        //После чего мы должны закрыть файл
                        //Иначе файл не запишется
                        out.close();
                    }
                } catch (IOException ee) {
                    throw new RuntimeException(ee);
                }

                //////////////////////////////////////////


                labelTest.setText(s);
                s = " \n image size:" + file.length() + " byte \n compres size:" + fi.length() + " byte";
                labelTest2.setText(s);
                s = "компрессия: " + file.length() / fi.length();
                labelTest3.setText(s);
                //s = spinnerBlockSize.getValue().toString() + " " + spinnerCoefCompress.getValue().toString()
                //        + " " + spinnerCountDecompress.getValue().toString() + " " + comboBoxBaseImage.getSelectedItem().toString();
                //labelTest.setText(s);

            }
        });

        buttonDownloadPhoto = new JButton();
        buttonDownloadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    file = fileopen.getSelectedFile();
                    labelTest.setText(file.getName());
                    Image image = null;
                    try {
                        image = ImageIO.read(file);
                        bi = ImageIO.read(file);
                    } catch (IOException ee) {
                        ee.printStackTrace();
                    }
                    g = panelStartImage.getGraphics();
                    //g.drawImage(image, 0, 0, null);

                    bi = getGray(bi);
                    g.drawImage(bi, 0, 0, null);
                    m = bi.getWidth();
                    n = bi.getHeight();
                }
            }
        });
        //

    }

    public static BufferedImage getGray(BufferedImage bi) {
        int argb;
        int r, g, b, grey;
        Color color, color1, color2;

        for (int i = 0; i < bi.getHeight(); i++)
            for (int j = 0; j < bi.getWidth(); j++) {
                argb = getRGBValue(bi, j, i);
                color = new Color(argb, true);
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
                grey = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                bi.setRGB(j, i, grey + (grey << 8) + (grey << 16));
            }

        /*for(int i = 0; i<bi.getHeight();i++)
            for(int j = 0; j<bi.getWidth();j++){
                argb = getRGBValue(bi,j,i);
                color = new Color(argb, true);
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
                grey = (int) (0.3*r + 0.59*g + 0.11 *b);
                color = new Color(grey + (grey << 8) + (grey << 16));

                int k = 0;
                color2 = null;
                while (color2==null){
                    for(k = 0; k<255;k++){
                        color1 = new Color(0,0,0,k);
                        if(color.getRGB()==color1.getRGB())
                            color2 = new Color(0,0,0,k);
                    }
                }


                bi.setRGB(j,i,color2.getRGB());
            }*/

        return bi;
    }

    public static int getRGBValue(BufferedImage bi, int x, int y) {
        //Object colorData1 = bi.getRaster().getDataElements(j, i, null);//данные о пикселе
        return bi.getColorModel().getRGB(bi.getRaster().getDataElements(x, y, null));//преобразование данных в цветовое значение
    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panelMain = new JPanel();
        panelMain.setLayout(new FormLayout("fill:d:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:noGrow,center:max(d;4px):noGrow,center:512px:noGrow,top:4dlu:noGrow,center:44px:noGrow"));
        panelMain.setPreferredSize(new Dimension(1050, 498));
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new FormLayout("fill:283px:grow,left:5dlu:noGrow,fill:d:grow", "center:d:grow"));
        CellConstraints cc = new CellConstraints();
        panelMain.add(settingsPanel, cc.xy(1, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
        startInfoPanel = new JPanel();
        startInfoPanel.setLayout(new FormLayout("fill:165px:noGrow,left:8dlu:noGrow,fill:0px:grow,left:5dlu:noGrow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:26px:noGrow"));
        startInfoPanel.setPreferredSize(new Dimension(198, 125));
        startInfoPanel.setRequestFocusEnabled(true);
        settingsPanel.add(startInfoPanel, cc.xy(1, 1));
        startInfoPanel.setBorder(BorderFactory.createTitledBorder("Настройки компрессии"));
        buttonDownloadPhoto.setText("Загрузить изображение");
        startInfoPanel.add(buttonDownloadPhoto, cc.xyw(1, 1, 4, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel label1 = new JLabel();
        label1.setText("Размер рангового блока:");
        startInfoPanel.add(label1, new CellConstraints(1, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));
        spinnerBlockSize.setPreferredSize(new Dimension(50, 26));
        startInfoPanel.add(spinnerBlockSize, cc.xy(3, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        label2.setText("Коэффициент компрессии:");
        startInfoPanel.add(label2, new CellConstraints(1, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));
        spinnerCoefCompress.setPreferredSize(new Dimension(110, 26));
        startInfoPanel.add(spinnerCoefCompress, cc.xy(3, 5, CellConstraints.LEFT, CellConstraints.DEFAULT));
        endInfoPanel = new JPanel();
        endInfoPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:48px:grow,top:4dlu:noGrow,center:48px:noGrow"));
        endInfoPanel.setPreferredSize(new Dimension(364, 125));
        settingsPanel.add(endInfoPanel, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.FILL));
        endInfoPanel.setBorder(BorderFactory.createTitledBorder("Настройки декомпрессии"));
        final JLabel label3 = new JLabel();
        label3.setText("Базовое изображение:");
        endInfoPanel.add(label3, new CellConstraints(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 5, 0, 0)));
        comboBoxBaseImage.setPreferredSize(new Dimension(150, 26));
        endInfoPanel.add(comboBoxBaseImage, cc.xy(3, 1));
        final JLabel label4 = new JLabel();
        label4.setText("Количество итераций декопрессии:");
        endInfoPanel.add(label4, cc.xy(1, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));
        spinnerCountDecompress.setPreferredSize(new Dimension(50, 26));
        endInfoPanel.add(spinnerCountDecompress, cc.xy(3, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));
        imagePanel = new JPanel();
        imagePanel.setLayout(new FormLayout("fill:512px:grow,left:4dlu:noGrow,fill:512px:grow", "center:512px:grow"));
        panelMain.add(imagePanel, cc.xy(1, 5, CellConstraints.DEFAULT, CellConstraints.FILL));
        panelStartImage = new JPanel();
        panelStartImage.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:max(d;4px):noGrow,top:201dlu:noGrow,center:57px:grow,top:31px:noGrow,top:4dlu:noGrow,top:50px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        panelStartImage.setOpaque(true);
        panelStartImage.setPreferredSize(new Dimension(512, 512));
        imagePanel.add(panelStartImage, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        labelTest = new JLabel();
        labelTest.setText("Label");
        panelStartImage.add(labelTest, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.CENTER));
        labelTest2 = new JLabel();
        labelTest2.setText("Label");
        panelStartImage.add(labelTest2, cc.xy(3, 4));
        labelTest3 = new JLabel();
        labelTest3.setText("Label");
        panelStartImage.add(labelTest3, cc.xy(3, 6));
        panelEndImage = new JPanel();
        panelEndImage.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
        panelEndImage.setPreferredSize(new Dimension(512, 512));
        imagePanel.add(panelEndImage, cc.xy(3, 1));
        statisticPanel = new JPanel();
        statisticPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(statisticPanel, cc.xy(1, 7));
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(menuPanel, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));
        buttonCompress.setText("Выполнить сжатие");
        panelMain.add(buttonCompress, cc.xy(1, 4));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
