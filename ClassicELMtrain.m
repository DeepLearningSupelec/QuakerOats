function [ Input_weights, Input_biases, Output_weights] = ClassicELMtrain( Inputs, Targets, Nb_hidden_neurons )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
Input_weights = randn(size(Inputs, 2), Nb_hidden_neurons);
Input_biases = randn(1, Nb_hidden_neurons);
Output_weights = CalculateOutputWeights( Inputs, Input_weights, Input_biases, Targets );

end

