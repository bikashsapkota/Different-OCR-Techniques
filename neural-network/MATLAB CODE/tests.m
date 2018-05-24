function num = tests(inputs,wih,who)
hidden_input = wih*inputs;
hidden_output = 1./(1+exp(-hidden_input));
final_input = who*hidden_output;
final_output = 1./(1+exp(-final_input));

max = final_output(1);
num = 0;
for i = 2:10
 if(max<final_output(i))
    num = i-1;
    max = final_output(i);
 end
end
end