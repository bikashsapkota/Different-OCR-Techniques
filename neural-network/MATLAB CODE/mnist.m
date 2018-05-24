trainData = csvread('/media/bikash/Study/Python/mnist_dataset/mnist_train.csv');
testData = csvread('/media/bikash/Study/Python/mnist_dataset/mnist_test.csv');
input_nodes = 784;
hidden_nodes = 100;
output_nodes = 10;
learningrate = 0.3;

wih = rand(hidden_nodes,input_nodes)-0.5;
who = rand(output_nodes,hidden_nodes)-0.5;


for i=1:60000
    targets = [0.01 0.01 0.01 0.01 0.01 0.01 0.01 0.01 0.01 0.01];
    targets(trainData(i,1)+1) = 0.99;
    input = trainData(i,(2:785))'/255*0.99+0.01;
    [wih,who, errors(i)] = train(input,targets',wih,who,learningrate);
end
plot(errors)%no need to include iteration as they are plotted in sequence
csvwrite('who.csv',who);
csvwrite('wih.csv',wih);


%wih = csvread('wih.csv'); %uncomment if you only want to test data 
%who = csvread('who.csv'); %uncomment if you only want to test data 
success = 0;
for i=1:10000
    value = testData(i,1);
    num = tests(testData(i,(2:785))',wih,who);
    if(value==num)
        success=success+1;
    end
end
