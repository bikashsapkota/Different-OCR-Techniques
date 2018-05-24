import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    private static Classify ci = new Classify();
    private static ImgToArff imgToArff = new ImgToArff();
    private static Logger logger = Logger.getLogger(ImgToArff.class.getName());

    public static void main(String[] args)  {
        action(choice());
    }

    static void printMenu(){
        System.out.println(
                "Enter 1 to create test.arff and train.arff from given images\n" +
                "Enter 2 to make model from train.arff\n" +
                "Enter 3 to get confusion matrix on test.arff\n" +
                "Enter 4 to predict output of image\n" +
                "Enter 5 to exit\n"
        );
    }

    static int choice(){
        Scanner input = new Scanner(System.in);
        printMenu();

        String choice = input.next();
        //int choice = input.nextInt();

        if(choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4")){
            return Integer.parseInt(choice);
        }else if (choice.equals("5")){
            System.exit(0);
        }


        logger.warning("Illegal Choice");
        return choice();
    }

    static boolean action(int choice) {
        Scanner input = new Scanner(System.in);
        switch (choice) {
            case 1:
                System.out.println("Enter path of data(the directory should consists of images.csv file with list of all trainging images in same directory)");
                String path = input.nextLine();
                System.out.println("Enter No of training data, and remaining will be used as testing");
                int count = input.nextInt();
                logger.info("It will take quite time, please hold tight!!");
                ImgToArff.trainArffData(path+"/","train.arff","test.arff", count);
                break;


            case 2:
                logger.info("Model creation on progress, model will be stored on file model.ser");
                Classify.makeModel("train.arff");
                break;


            case 3:
                logger.info("Generating Confusion Matrix");
                Classify.getConfusionMatrix("test.arff");
                break;


            case 4:
                logger.info("The model provided is created using binary images so the we recommend to use binary images for prediction");
                System.out.println("Enter the full path of image");
                String imagePath = input.next();

                imgToArff.testArff(imagePath,"b.arff");
                ci.test("b.arff");
                break;

        }
        return action(choice());
    }
}
