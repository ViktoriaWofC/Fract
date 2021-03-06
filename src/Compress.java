import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 02.12.2016.
 */
public class Compress {
    private List<Rang> rangList = new ArrayList<>();
    private int r;//размер рангового блока
    private int[][] pix;
    private int width;//ширина картинки
    private int height;//высота картинки
    private int n;//количество блоков по высоте
    private int m;//количество блоков по ширине
    private double epsilon;//коэффициент компрессии?

    public Compress(int[][] pix,int r,double epsilon){
        this.pix = pix;
        this.r = r;
        this.width = pix[0].length;
        this.height = pix.length;
        this.m = width/r;
        this.n = height/r;
        this.epsilon = epsilon;
    }

    public List<Rang> getRangList(){
        return rangList;
    }

    public void compressImage(){
        boolean b = false;
        int[][] rang = new int[r][r];// ранговый блок

        //перебор ранговых блоков
        for(int i = 0; i<n;i++)
            for(int j = 0; j<m;j++){
                //выделяем ранговый блок
                for(int ii = 0; ii<r;ii++)
                    for(int jj = 0; jj<r;jj++)
                        rang[ii][jj] = pix[r*i+ii][r*j+jj];

                //пербор доменных блоков
                getDomenBloc(rang,1,j*r,i*r);
                //rangsList.add(ran);
            }

        //return rangsList;
    }

    public void getDomenBloc(int[][] rang, int k,int x, int y){
        //x - начальная координата блока
        //y - начальная координата блока
        //пербор доменных блоков
        Rang ran = null;
        int z = rang.length;//размер доменного блока = размер рангового
        int f = r*2/z;//кол-во усредняемых пикселей
        int[][] domen = new int[z][z];
        int[][] domenBig = new int[r*2][r*2];

        boolean b = false;
        int id = 0;
        int jd = 0;

        while((id < n - 1)&&(b==false)) {
            jd = 0;
            while ((jd < m - 1) && (b == false)) {
                int sum = 0;
                //выделяем доменный блок
                for (int i = 0; i < r*2; i++)
                    for (int j = 0; j < r*2; j++)
                        domenBig[i][j] = pix[r * id + i][r * jd + j];

                int d = 0;
                //и уменьшаем его усреднением
                //
                Color color;// = new Color(domen[i][j]);
                int sumR,sumG,sumB,dR,dG,dB;
                //
                for (int i = 0; i < z; i++)
                    for (int j = 0; j < z; j++) {
                        sum = 0;
                        //sumR = 0; sumG = 0; sumB = 0;
                        for(int ii = 0; ii<2*k;ii++)
                            for(int jj = 0; jj<2*k;jj++) {
                                sum += domenBig[i * 2 * k + ii][j * 2 * k + jj];

                                //color = new Color(domenBig[i * 2 * k + ii][j * 2 * k + jj]);
                                //sum += color.getRed();


                            }
                        d = (int) (sum/Math.pow(4,k));

                        //Color color = new Color(d);
                        //dR = color.getRed();
                        //dG = color.getGreen();
                        //dB = color.getBlue();
                        //int grey = (int) (0.3*dR + 0.59*dG + 0.11 *dB);
                        //d = grey + (grey << 8) + (grey << 16);

                        //color = new Color(d,d,d);
                        //domen[i][j] = color.getRGB();
                        domen[i][j] = d;

                        //dR = (int) (sumR/Math.pow(4,k));
                        //dG = (int) (sumG/Math.pow(4,k));
                        //dB = (int) (sumB/Math.pow(4,k));
                        //domen[i][j] = dB + (dG << 8) + (dR << 16);
                    }

                /*for (int i = 0; i < z; i++)
                    for (int j = 0; j < z; j++) {
                        sum = 0;
                        for(int ir = 0; ir<2*k;ir++)
                            for(int jr = 0; jr<2*k;jr++)
                                sum += pix[r * id + i*f+ir][r * jd + j*f +jr];
                        domen[i][j] = (int) (sum / 4^k);
                    }*/

                //сравниваем ранговый и доменный блок
                int h = 0;

                while((h<6)&&(b==false)) {
                    if (compareBlocs(rang, domen)) {
                        b = true;
                        ran = new Rang(jd * r, id * r, h,k,x,y,1);
                    }else {
                        double bright = 4;
                        while((bright>=0.25)&&(b==false)){
                            if (compareBlocs(rang, changeBright(domen,bright))) {
                                b = true;
                                ran = new Rang(jd * r, id * r, h,k,x,y,k);
                            }else{
                                if(bright/2==1)
                                    bright = bright/4;
                                else bright = bright/2;
                            }
                        }
                        h++;
                        domen = setAfinnInt(domen,h);
                    }

                }
                jd++;
            }
            id++;
        }
        //если для рангового блока не нашли доменного
        if(ran==null){


            k = k*2;
            //уменьшаем r/2 и снова ищем доменный пресуя его в 4 раза и т.д пока r>2
            if(r/k>4)
            while (ran==null) {
                int[][] rangDop = new int[z/2][z/2];
                for (int ir = 0; ir < 2; ir++)
                    for (int jr = 0; jr < 2; jr++) {
                        //выделяем ранговый блок
                        for (int i = 0; i < z / 2; i++)
                            for (int j = 0; j < z / 2; j++)
                                rangDop[i][j] = pix[r * x + ir*z/2+ i][r * y+ jr*z/2 + j];


                        getDomenBloc(rangDop,k,r * x + ir*z/2,r * y+ jr*z/2);
                    }

            }
            //ran = new Rang(0, 0, 0,k,x,y);
            //rangList.add(ran);

        }
        else rangList.add(ran);

        //return ran;
    }

    public boolean compareBlocs(int[][] rang,int[][] domen){
        boolean b = false;
        double sum = 0;
        int k = rang.length;
        double h = 0;

        Color colorDomen, colorRang;

        for (int i = 0; i < k; i++)
            for (int j = 0; j < k; j++) {

                //colorDomen = new Color(domen[i][j]);
                //colorRang = new Color(rang[i][j]);
                //h = (colorDomen.getRed() - colorRang.getRed())/1000;

                h = (domen[i][j] - rang[i][j]) / 10000;

                sum += h * h;
            }

            //sum = sum/100;
           // if((sum!=0)&&(sum!=1531)&&(sum!=441032))
             //   System.out.println("");
        if(sum<epsilon)
            b = true;
        else b = false;

        return b;
    }

    public int[][] changeBright(int[][] pix, double k){
        int n = pix.length;
        int x;
        int[][] p = new int[n][n];
        Color color;

        for(int i = 0; i<n;i++)
            for(int j = 0; j<n;j++){
                color = new Color(pix[i][j]);
                x = color.getRed();
                x = (int)(x*k);
                p[i][j] = x;
            }
        return p;
    }

    public int[][] setAfinnInt(int[][] pix, int k){
        //int argb;
        int n = pix.length;
        int x,y;
        int[][] p = new int[n][n];

        if(k<4){

            if(k==1){//поворот на 90

                for(int i = 0; i<n;i++)
                    for(int j = 0; j<n;j++){
                        x = n-1-i;
                        y = j;
                        p[y][x] = pix[i][j];
                    }

            }else if(k==2){//поворот на 180
                int h;
                for(int i = 0; i<n;i++)
                    for(int j = 0; j<n;j++){
                        x = n-1-i;
                        y = j;
                        h = x;
                        x = n-1-y;
                        y = h;
                        p[y][x] = pix[i][j];
                    }
            }
            else if(k==3){//поворот на 270
                for(int i = 0; i<n;i++)
                    for(int j = 0; j<n;j++){
                        x = i;
                        y = n-1-j;
                        p[y][x] = pix[i][j];
                    }
            }

        }
        else{
            if(k==4){//отражение по вертикали
                for(int i = 0; i<n;i++)
                    for(int j = 0; j<n;j++){
                        x = n-1-j;
                        y = i;
                        p[y][x] = pix[i][j];
                    }
            }
            else if(k==5){//отражение по горизонтали
                for(int i = 0; i<n;i++)
                    for(int j = 0; j<n;j++){
                        x = j;
                        y = n-1-i;
                        p[y][x] = pix[i][j];
                    }
            }
        }

        if(k==0)
            return pix;
        else return p;
    }


}
