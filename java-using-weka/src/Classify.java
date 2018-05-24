import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.*;
import java.util.logging.Logger;


public class Classify {
	final static String model = "model.ser";
	static Logger logger = Logger.getLogger(ImgToArff.class.getName());

	public static void main(String args[]) throws Exception{
		//load training dataset
		DataSource source = new DataSource("train.arff");

		//create instance form data
		Instances trainDataset = source.getDataSet();

		//set class index to the last attribute
		trainDataset.setClassIndex(trainDataset.numAttributes()-1);


		//create and build the  classifier, Implements John Platt's sequential minimal optimization algorithm for training a support vector classifier
		Classifier nb = new SMO();
		nb.buildClassifier(trainDataset);

		//load new dataset
		DataSource source1 = new DataSource("test.arff");
		Instances testDataset = source1.getDataSet();
		//set class index to the last attribute
		testDataset.setClassIndex(testDataset.numAttributes()-1);

		//loop through the new dataset and make predictions


		for (int i = 0; i < testDataset.numInstances(); i++) {
			//get class double value for current instance
			double actualClass = testDataset.instance(i).classValue();
			//get class string value using the class index using the class's int value
			String actual = testDataset.classAttribute().value((int) actualClass);
			//get Instance object of current instance
			Instance newInst = testDataset.instance(i);
			//call classifyInstance, which returns a double value for the class
			double predNB = nb.classifyInstance(newInst);
			//use this value to get string value of the predicted class
			String predString = testDataset.classAttribute().value((int) predNB);
			System.out.println(actual+", "+predString);
		}

	}

	/*
	*  Creates and store model
	* */
	static void makeModel(String dataPath) {
		DataSource source;
		Instances trainDataset;
		Classifier smo = null;

		try {
			source = new DataSource(dataPath);
			trainDataset = source.getDataSet();
			trainDataset.setClassIndex(trainDataset.numAttributes()-1);
			smo = new SMO();
			smo.buildClassifier(trainDataset);
		}catch (Exception e){
			logger.warning(e.getMessage());
			logger.info("Please start from begining!!!");
			System.exit(0);
		}


		FileRW fr = new FileRW();
		fr.classifier = smo;

		try {
			FileOutputStream fileOut = new FileOutputStream(model);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(fr);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in "+model);
		} catch (IOException i) {
			logger.warning(i.getMessage());
			logger.info("Please start from begining!!!");
			System.exit(0);
		}


		System.out.println("Classification completed");

	}

	/*
	* prints and returns confusion matrix
	*
	* */

	public static int[][] getConfusionMatrix(String testData) {
		FileRW e = null;

		int[][] confusionMatrix = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                confusionMatrix[i][j] = 0;
            }

        }



		try {
			FileInputStream fileIn = new FileInputStream(model);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			e = (FileRW) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException | ClassNotFoundException i) {
			logger.warning(i.getMessage());
			logger.info("Please start from begining!!!");
			System.exit(0);
		}

		Classifier smo = e.classifier;

		DataSource source1;
		Instances testDataset= null;

		try {
			source1 = new DataSource(testData);
			testDataset = source1.getDataSet();
		}catch (Exception ex){
			logger.warning(ex.getMessage());
			logger.info("Please start from begining!!!");
			System.exit(0);
		}
		//set class index to the last attribute
		testDataset.setClassIndex(testDataset.numAttributes()-1);

		//loop through the new dataset and make predictions
		System.out.println("===================");
		//System.out.println("Actual Class, NB Predicted");
		for (int i = 0; i < testDataset.numInstances(); i++) {

			double actualClass = testDataset.instance(i).classValue();

			//get class string value using the class index using the class's int value
			int actual = Integer.parseInt(testDataset.classAttribute().value((int) actualClass));

			//get Instance object of current instance
			Instance newInst = testDataset.instance(i);
			//call classifyInstance, which returns a double value for the class

			double predNB = 0.0;
			try {
				predNB = smo.classifyInstance(newInst);
			}catch (Exception ex){
				logger.warning(ex.getMessage());
				logger.info("Please start from begining!!!");
				System.exit(0);
			}

			//use this value to get string value of the predicted class
			int predString = Integer.parseInt(testDataset.classAttribute().value((int) predNB));
			confusionMatrix[actual][predString]  += 1; //increases the count
		}


		System.out.println("\t\t\tP\tR\tE\tD\tI\tC\tT\tE\tD\n");

		System.out.println(" \t \t0\t1\t2\t3\t4\t5\t6\t7\t8\t9\n");
		System.out.println("\t\t-------------------------------------");
		String predString = " ACTUAL   ";
        for (int i = 0; i < 10; i++) {
			System.out.print(predString.charAt(i)+"\t"+ i + "\t" + "|");
            for (int j = 0; j < 10; j++) {
                System.out.print(confusionMatrix[i][j]+"\t");
            }
            System.out.println("\n");

        }

		return confusionMatrix;

	}

	public  void test(String path)  {
		FileRW e = null;

		try {
			FileInputStream fileIn = new FileInputStream(model);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			e = (FileRW) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException | ClassNotFoundException i) {
			logger.warning(i.getMessage());
			logger.info("Please start from begining!!!");
			System.exit(0);
		}

		Classifier smo = e.classifier;

		DataSource source1;
		Instances testDataset = null;

		try {
			source1 = new DataSource(path);
			testDataset = source1.getDataSet();
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
			logger.info("Please start from begining!!!");
			System.exit(0);
		}



		//set class index to the last attribute
		testDataset.setClassIndex(testDataset.numAttributes()-1);

		//loop through the new dataset and make predictions



		//get class double value for current instance
		double actualClass = testDataset.instance(0).classValue();
		//get class string value using the class index using the class's int value
		String actual = testDataset.classAttribute().value((int) actualClass);
		//get Instance object of current instance
		Instance newInst = testDataset.instance(0);
		//call classifyInstance, which returns a double value for the class

		double predNB = 0.0;

		try {
			predNB = smo.classifyInstance(newInst);
		} catch (Exception e1) {
			logger.warning(e1.getMessage());
			logger.info("Please start from begining!!!");
			System.exit(0);
		}

		//use this value to get string value of the predicted class
		String predString = testDataset.classAttribute().value((int) predNB);
		System.out.println("Predicted:"+ predString);

	}
}

class FileRW implements java.io.Serializable {
	Classifier classifier;
}
