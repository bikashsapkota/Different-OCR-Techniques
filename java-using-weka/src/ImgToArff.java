import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/*constructor to initialize log*/
public class ImgToArff {
    static Logger logger = Logger.getLogger(ImgToArff.class.getName());
    FileHandler fh;

    ImgToArff(){

        try {
            fh = new FileHandler("log.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        }catch (Exception e){
            System.out.println("log file error");
            System.exit(0);
        }

        //logger.setUseParentHandlers(false);

    }

    public static void main(String[] args) {

        System.out.println(arffPostData(10));

    }

    /*
    * creates arff file from given images for training and testing
    * */
    static void trainArffData(String imageSource, String trainArff, String testArff, int trainCount){

        File file = new File(imageSource+"images.csv");
        FileWriter trFileWriter;
        FileWriter teFileWriter;
        FileReader fileReader = null;



        /* Handling exception for dataset directory  */
        try {
            fileReader= new FileReader(file);
        }catch (IOException e){
            logger.warning(e.getMessage());
            logger.info("Please start from begining!!!");
            System.exit(0);
        }


        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        String traindata = "";
        String testdata = "";
        int count = 0;

        try {
            while ((line = bufferedReader.readLine()) != null) {
                String split[] = line.split(",");
                String imageFileName = split[0];
                String num = split[1];
                //System.out.println(imageFileName+":"+num);
                String csv = imageTOdata(imageSource + imageFileName, num); // gets gray pixel values for each image

                count++;
                if (count >= trainCount) {
                    testdata += csv + "\n";
                } else {
                    traindata += csv + "\n";
                }
            }

            fileReader.close();
        }catch (IOException e){
            logger.warning(e.getMessage());
            logger.info("Please start from begining!!!");
            System.exit(0);
        }
    String postData = arffPostData(784);

        try {
            teFileWriter = new FileWriter(testArff);
            teFileWriter.write(postData+testdata);
            teFileWriter.close();
        }catch (IOException e){
            logger.warning(e.getMessage());
            logger.info("Please start from begining!!!");
            System.exit(0);
        }

        try {
            trFileWriter = new FileWriter(trainArff);
            trFileWriter.write(postData+traindata);
            trFileWriter.close();
        }catch (IOException e){
            logger.warning(e.getMessage());
            logger.info("Please start from begining!!!");
            System.exit(0);

        }

        logger.info("train.arff and test.arff created successfully");

    }

    /*
    *  Create arff file of single image for prediction
    * */
    public void testArff(String path, String arffFile)  {
        String data = imageTOdata(path, "5");
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(arffFile);
            fileWriter.write(arffPostData(784)+ data);
            fileWriter.close();
        }catch (IOException ie){
            logger.warning(ie.getMessage());
            logger.info("Please Start from beginning!!!");
            System.exit(0);
        }
    }

    /*
    *  Reads Image and return gray value in comma sepetated format
    * */
    public static String imageTOdata(String path, String value)  {
        File pathToFile = new File(path);
        BufferedImage image = null;
        try {
            image = ImageIO.read(pathToFile);
        }catch (IOException ie){
            logger.warning(ie.getMessage());
            logger.info("Please start from begining");
            System.exit(0);
        }

        int width  = image.getWidth();
        int height = image.getHeight();

        if(width != 28 || height != 28){
            logger.warning( path +" should be of 28*28");
            logger.info("Please solve and start from begining");
            System.exit(0);
        }

        StringBuilder datas = new StringBuilder();


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int gray = getGRAY(i,j,image);
                datas.append(gray).append(",");
            }
        }
    return datas+value;

    }


    /*
    *  Get gray value of particular pixel of given image
    * */

    static int getGRAY(int i, int j, BufferedImage image){
        Color c = new Color(image.getRGB(i, j));
        double red = (c.getRed() * 0.299);
        double green = (c.getGreen() * 0.587);
        double blue = (c.getBlue() *0.114);
        return (int) (red+green+blue);
    }


    /*
    *   generate variables for arff file
    * */
    static String arffPostData(int varCount){
        String postdata = "@relation mnist";
        for (int i = 0; i < varCount; i++) {
            postdata+="\n@attribute "+ "var_"+ i +" numeric";
        }
        return postdata+"\n" + "@attribute digit {0,1,2,3,4,5,6,7,8,9}\n@data\n";
    }
}
