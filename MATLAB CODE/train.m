function [wih,who,error] = train(inputs,targets,wih,who,learningrate)
    hidden_input = wih*inputs;
    hidden_output = 1./(1+exp(-hidden_input));
    final_input = who*hidden_output;
    final_output = 1./(1+exp(-final_input));
    output_error = targets - final_output;
    %error = output_error * output_error';%mse calculation 
    error = output_error .*output_error
    error = sum(error)
    hidden_error = who' * output_error;

    wih = wih + learningrate * (hidden_error .* hidden_output .* (1.0 - hidden_output))* inputs';
    who = who + learningrate * (output_error .* final_output .* (1.0 - final_output))* hidden_output';
end
