import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;
import sun.rmi.runtime.Log;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.renderable.RenderableImage;
import java.awt.image.renderable.RenderableImageOp;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {



        BufferedImage bi = null;
        BufferedImage testBI = null;
        int[][] pixels;
        int n,m;

        String startImageName = "7.jpg";
        String greyImageName = "grey.jpg";
        String baseImageName = "baseClet.jpg";
        String endImageName = "end.jpg";
        String bat = "bat.bat";
        String path1 = "D:/университет/диплом/fractImage/"+startImageName;
        String path2 = "D:/университет/диплом/fractImage/"+greyImageName;
        String pathBase = "D:/университет/диплом/fractImage/"+baseImageName;
        String pathEnd = "D:/университет/диплом/fractImage/"+endImageName;
        String pathBat = "D:/университет/диплом/fractImage/"+bat;



        try {
            bi = ImageIO.read(new File(path1));
            testBI  = ImageIO.read(new File(path1));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(bi.getRGB(0,0));

        bi = getGray(bi);

        try {
            ImageIO.write(bi,"jpg", new File(path2));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("");
        //////////////////////////////////////////////////////

        m = bi.getWidth();
        n = bi.getHeight();
        pixels = new int[n][m];
        //
        int argb = 0;
        Color color;
        int f;

        for(int i = 0; i<n;i++)
            for(int j = 0; j<m;j++) {
                //argb = bi.getRGB(j,i);
                //color = new Color(argb);
                //f = color.getRed();
                //pixels[i][j] = f;
                //pixels[i][j] = getRGBValue(bi, j, i);
                pixels[i][j] = bi.getRGB(j,i);

            }


        /*for(int i = 0; i<50;i++) {
            for (int j = 200; j < 250; j++)
                System.out.print(pixels[i][j]+" ");
            System.out.println("");
        }*/

        //System.out.println(bi.getRGB(1,1));
        //System.out.println(pixels[1][1]);
        //System.out.println(pixels[30][30]);
        //System.out.println(pixels[100][100]);

        //color = new Color(124,124,124);
        //color = new Color(235,235,235);
        //////////////////////////////////////////////////////////////////////
        /*
        color = new Color(-131587);
        int grey = (int) (0.3*color.getRed() + 0.59*color.getGreen() + 0.11 *color.getBlue());
        //int d = pixels[1][1];
        int d = grey + (grey << 8) + (grey << 16);
        color = new Color(d);

        System.out.println("color "+color.getRGB());
        System.out.println("grey = "+grey+ " d = "+ d);

        int red,green,blue;


        //d = d<<8;
        //grey = (int) (0.3*r + 0.59*g + 0.11 *b);
        //bi.setRGB(j,i,blue + (green << 8) + (red << 16));

        //System.out.println("color "+color.getRed()+" "+color.getGreen()+" "+color.getBlue());

        System.out.println("\npix " +(d>>16)+ " "+((d- (d<<16))>>8)+" "+(d-(d<<16)-(d<<8)));
        System.out.println("\npix " +color.getRed()+ " "+color.getGreen()+" "+color.getBlue());


        //d = y0 +(x0 << 21)+(k << 25)+(af<< 29)+(y << 40)+(x << 51);
        //System.out.println(""+d);
        //System.out.println(""+(d >> 51)+" "+((d -  (x << 51))>>40)
        //        +" "+((d -  (x << 51)-(y<<40))>>29)+" "+((d -  (x << 51)-(y<<40)-(af<<29))>>25)
        //        +" "+((d -  (x << 51)-(y<<40)-(af<<29)-(k<<25))>>21)+" "+((d -  (x << 51)-(y<<40)-(af<<29)-(k<<25)-(x0<<21)))+" ");
*/
        ////////////////////////////////////////////////////////

        /*
        testBI = setAfinnBuff(bi,testBI,5);
        try {
            ImageIO.write(testBI,"jpg", new File(pathTest));
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        ////////////////////////////////////////////////////////
/*
        int n = 16;
        int[][] pixels = new int[n][n];
        for(int i = 0; i<n;i++)
            for(int j = 0; j<n;j++){
                if((i<4)&&(j<4)) pixels[i][j] = 1;
                else if((i>=8)&&(j>=8)) pixels[i][j] = 1;
                else if((i<8)&&(j<4)) pixels[i][j] = 2;
                else if((i<8)&&(j>=8)) pixels[i][j] = 2;
                else pixels[i][j] = 0;
            }

        for(int i = 0; i<n;i++) {
            for (int j = 0; j < n; j++)
                System.out.print(pixels[i][j]+ " ");
            System.out.println("");
        }*/
///////////////////////////////////////////////////////////

        int r = 4;
        Compress compress = new Compress(pixels,r,20000);


        System.out.println("");
        long t1 = System.currentTimeMillis();
        System.out.println("start compress :"+t1);
        compress.compressImage();
        List<Rang> rangList = compress.getRangList();
        long t2 = System.currentTimeMillis();
        System.out.println("end compress :"+t2);
        System.out.println("time :"+(t2-t1)/1000);
        System.out.println("");


        //записываем rangList  в файл
        //List<Long> longList = new ArrayList<>();
        long longList[] = new long[rangList.size()];
        int ii = 0;
        long d = 0;
        for(Rang rang:rangList){
            //преобразование из Rang в число long
            d = rang.getY0() +(rang.getX0() << 21)+(rang.getK() << 25)+(rang.getAfinn()<< 29)+(rang.getY() << 40)+(rang.getX() << 51);
            //longList.add(d);
            longList[ii] = d;
            ii++;
        }

        File file = new File(pathBat);

        try {
            //проверяем, что если файл не существует то создаем его
            if(!file.exists()){
                file.createNewFile();
            }

            //PrintWriter обеспечит возможности записи в файл
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                //Записываем текст у файл
                out.print(longList);
            } finally {
                //После чего мы должны закрыть файл
                //Иначе файл не запишется
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        /////////////////////////////////////////////////////////////
        bi = null;
        try {
            //bi = ImageIO.read(new File(path2));
            bi = ImageIO.read(new File(pathBase));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bi = getGray(bi);

        ////
        //color = new Color(bi.getRGB(0, 0));
       // System.out.println(color.getRGB());
        //color = new Color(-1);
       // color = new Color(-7763575);

        //try {
        //    ImageIO.write(bi,"jpg", new File(path2));
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        /////////

        Decompress decompress = new Decompress(rangList,bi,r);

        System.out.println("");
        t1 = System.currentTimeMillis();
        System.out.println("start decompress :"+t1);
        bi = decompress.decompressImage(10);
        t2 = System.currentTimeMillis();
        System.out.println("end decompress :"+t2);
        System.out.println("time :"+(t2-t1)/1000);
        System.out.println("");

        //bi = getGray(bi);

        try {
            ImageIO.write(bi,"jpg", new File(pathEnd));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(rangList.size());


        color = new Color(bi.getRGB(0,0));
        System.out.println(color.getRGB());
        color = new Color(-9145228);


        //////////////////////////////////////////////////////////////////
        /*bi = null;
        try {
            bi = ImageIO.read(new File(pathBase));
        } catch (IOException e) {
            e.printStackTrace();
        }

        bi = getGray(bi);
        bi = changeColor(bi);

        try {
            ImageIO.write(bi,"jpg", new File(pathEnd));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*
        long x = 5, y = 10, af = 3, k = 2, x0 = 15, y0 = 23;
        //преобразование из Rang в число long
        long d = 0;
        d = y0 +(x0 << 21)+(k << 25)+(af<< 29)+(y << 40)+(x << 51);
        System.out.println(""+d);
        System.out.println(""+(d >> 51)+" "+((d -  (x << 51))>>40)
                +" "+((d -  (x << 51)-(y<<40))>>29)+" "+((d -  (x << 51)-(y<<40)-(af<<29))>>25)
                +" "+((d -  (x << 51)-(y<<40)-(af<<29)-(k<<25))>>21)+" "+((d -  (x << 51)-(y<<40)-(af<<29)-(k<<25)-(x0<<21)))+" ");
*/



        FractalForm fractalForm = new FractalForm();
        fractalForm.pack();
        fractalForm.setSize(1050,700);
        fractalForm.setVisible(true);

    }


    public static BufferedImage setAfinnBuff(BufferedImage bim,BufferedImage bi, int k){
        //BufferedImage bi;// = bim;

        int argb;
        int n = bi.getWidth();
        int x,y;

        if(k<4){


            if(k==1){//поворот на 90

                for(int i = 0; i<n;i++)
                    for(int j = 0; j<n;j++){
                        argb = getRGBValue(bim,j,i);
                        x = n-1-i;
                        y = j;
                        bi.setRGB(x,y,argb);
                    }

            }else if(k==2){//поворот на 180
                int h;
                for(int i = 0; i<n;i++)
                    for(int j = 0; j<n;j++){
                        argb = getRGBValue(bim,j,i);
                        x = n-1-i;
                        y = j;
                        h = x;
                        x = n-1-y;
                        y = h;
                        bi.setRGB(x,y,argb);
                    }
            }
            else if(k==3){//поворот на 270
                for(int i = 0; i<n;i++)
                    for(int j = 0; j<n;j++){
                        argb = getRGBValue(bim,j,i);
                        x = i;
                        y = n-1-j;
                        bi.setRGB(x,y,argb);
                    }
            }

        }
        else{
            if(k==4){//отражение по вертикали
                for(int i = 0; i<n;i++)
                    for(int j = 0; j<n;j++){
                        argb = getRGBValue(bim,j,i);
                        x = n-1-j;
                        y = i;
                        bi.setRGB(x,y,argb);
                    }
            }
            else if(k==5){//отражение по горизонтали
                for(int i = 0; i<n;i++)
                    for(int j = 0; j<n;j++){
                        argb = getRGBValue(bim,j,i);
                        x = j;
                        y = n-1-i;
                        bi.setRGB(x,y,argb);
                    }
            }
        }

        return bi;
    }

    public static double[] umnMatrixVec(double[][] m1, double[] m2){
        int n = m1.length;
        int m = m1[0].length;
        double[] m3 = new double[n];
        for(int i = 0; i<n;i++)
            for (int k = 0; k < m; k++) {
                m3[i] += m1[i][k] * m2[k];
            }
        return m3;
    }

    public static int getRGBValue(BufferedImage bi,int x, int y){
        //Object colorData1 = bi.getRaster().getDataElements(j, i, null);//данные о пикселе
        return bi.getColorModel().getRGB(bi.getRaster().getDataElements(x, y, null));//преобразование данных в цветовое значение
    }

    public static BufferedImage getGray(BufferedImage bi){
        int argb;
        int r,g,b,grey;
        Color color,color1,color2;

        for(int i = 0; i<bi.getHeight();i++)
            for(int j = 0; j<bi.getWidth();j++){
                argb = getRGBValue(bi,j,i);
                color = new Color(argb, true);
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
                grey = (int) (0.3*r + 0.59*g + 0.11 *b);
                bi.setRGB(j,i,grey + (grey << 8) + (grey << 16));
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

    public static BufferedImage changeColor(BufferedImage bi){
        int argb;
        int r,g,b,grey;
        Color color;
        color = new Color(-13816531);
        color = new Color(0,0,0,25);
        color = new Color(133,93,99);

        color = new Color(122,162,156);
        System.out.println(color.getRGB());


        for(int i = 0; i<bi.getHeight();i++)
            for(int j = 0; j<bi.getWidth();j++){
                argb = getRGBValue(bi,j,i);
                color = new Color(argb);
                //color = new Color(argb, true);
                r = color.getRed();//+50;
                g = 0;//color.getGreen();
                b = 0;//color.getBlue();
                //grey = (int) (0.3*r + 0.59*g + 0.11 *b);
                bi.setRGB(j,i,b + (g << 8) + (r << 16));
                //bi.setRGB(j,i,color.getRGB()+50);
            }

        return bi;
    }

    public static class CoolImage{
        private int     height;             // высота изображения
        private int     width;              // ширина изображения
        private int[]   pixels;             // собственно массив цветов точек составляющих изображение

        public int getPixel(int x, int y)   { return pixels[y*width+x]; }   // получить пиксель в формате RGB
        public int getRed(int color)        { return color >> 16; }         // получить красную составляющую цвета
        public int getGreen(int color)      { return (color >> 8) & 0xFF; } // получить зеленую составляющую цвета
        public int getBlue(int color)       { return color  & 0xFF;}        // получить синюю   составляющую цвета

        /*
        String path1 = "D:/университет/диплом/fractImage/1.jpg";
        String path2 = "D:/университет/диплом/fractImage/end.jpg";
        CoolImage picture;
        try {

            picture = new CoolImage(path1); // загружаем файл изображения
            picture.convertToGrey();
            picture.saveAsJpeg(path2);
            //picture = new CoolImage(ar[1]); // загружаем файл изображения
            //if ("-n".equals(args[0]))  picture.convertToNegative();
            //if ("-g".equals(args[0]))  picture.addColorGreenChannel(-100);
            //if ("-bw".equals(args[0])) picture.convertToBlackAndWhite();
            //picture.saveAsJpeg(args[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        // Конструктор - создание изображения из файла
        public CoolImage(String fileName) throws IOException {
            BufferedImage img = readFromFile(fileName);
            this.height = img.getHeight();
            this.width  = img.getWidth();
            this.pixels = copyFromBufferedImage(img);
        }

        // Чтение изображения из файла в BufferedImage
        private BufferedImage readFromFile(String fileName) throws IOException {
            ImageReader r  = new JPEGImageReader(new JPEGImageReaderSpi());
            r.setInput(new FileImageInputStream(new File(fileName)));
            BufferedImage  bi = r.read(0, new ImageReadParam());
            ((FileImageInputStream) r.getInput()).close();
            return bi;
        }

        // Формирование BufferedImage из массива pixels
        private BufferedImage copyToBufferedImage()  {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    bi.setRGB(j, i, pixels[i*width +j]);
            return bi;
        }

        // Формирование массива пикселей из BufferedImage
        private int[] copyFromBufferedImage(BufferedImage bi)  {
            int[] pict = new int[height*width];
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    pict[i*width + j] = bi.getRGB(j, i) & 0xFFFFFF; // 0xFFFFFF: записываем только 3 младших байта RGB
            return pict;
        }

        // Запись изображения в jpeg-формате
        public void saveAsJpeg(String fileName) throws IOException {
            ImageWriter writer = new JPEGImageWriter(new JPEGImageWriterSpi());
            saveToImageFile(writer, fileName);
        }

        // Запись изображения в png-формате (другие графические форматы по аналогии)
        public void saveAsPng(String fileName) throws IOException {
            ImageWriter writer = new PNGImageWriter(new PNGImageWriterSpi());
            saveToImageFile(writer, fileName);
        }

        // Собственно запись файла (общая для всех форматов часть).
        private void saveToImageFile(ImageWriter iw, String fileName) throws IOException {
            iw.setOutput(new FileImageOutputStream(new File(fileName)));
            iw.write(copyToBufferedImage());
            ((FileImageOutputStream) iw.getOutput()).close();
        }

        // конвертация изображения в негатив
        public void  convertToNegative() {
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++)
                    // Применяем логическое отрицание и отбрасываем старший байт
                    pixels[i*width + j] = ~pixels[i*width + j] & 0xFFFFFF;
        }

        // конвертация изображения в оттенки серого
        public void convertToGrey() {
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++) {
                    // находим среднюю арифметическую интенсивность пикселя по всем цветам
                    int intens = (int)(getRed(pixels[i * width + j])*0.3 +
                            getGreen(pixels[i * width + j])*0.59 +
                            getBlue(pixels[i * width + j])*11);
                    // ... и записываем ее в каждый цвет за раз , сдвигая байты RGB на свои места
                    pixels[i * width + j] = intens+intens+intens;// + (intens << 8) + (intens << 16);
                }
        }

        // конвертация изображения в черно-белый вид
        public void convertToBlackAndWhite() {
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++) {
                    // находим среднюю арифметическую интенсивность пикселя по всем цветам
                    int intens = (getRed(pixels[i * width + j]) +
                            getGreen(pixels[i * width + j]) +
                            getBlue(pixels[i * width + j])) / 3;
                    // ... и записываем ее в каждый цвет за раз , сдвигая байты RGB на свои места
                    pixels[i * width + j] = intens + (intens << 8) + (intens << 16);
                }
        }

        // изменяем интесивность зеленого цвета
        public void addColorGreenChannel(int delta) {
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++) {
                    int newGreen =  getGreen(pixels[i * width + j]) + delta;
                    if (newGreen > 255) newGreen=255;  // Отсекаем при превышении границ байта
                    if (newGreen < 0)   newGreen=0;
                    // В итоговом пикселе R и B цвета оставляем без изменений: & 0xFF00FF
                    // Полученный новый G (зеленый) засунем в "серединку" RGB: | (newGreen << 8)
                    pixels[i * width + j] = pixels[i * width + j] & 0xFF00FF | (newGreen << 8);
                }
        }
    }


}
