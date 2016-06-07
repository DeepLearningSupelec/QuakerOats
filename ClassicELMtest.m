function [ Outputs ] = ClassicELMtest( Inputs, Input_weights, Input_biases, Output_weights )
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
H = Inputs*Input_weights;
        for i = 1:size(H,1)
            H(i,:) = H(i,:) + Input_biases;
            H(i,:) = 1./(1+exp(-H(i,:)));   
        end
        
Outputs = H*Output_weights;
Outputs = 1./(1+exp(-Outputs(:,:)));
        
end

