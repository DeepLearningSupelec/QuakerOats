function [ Input_weights, Input_biases, Sorting_weights, Batch_bounds, Output_weights ] = trainFindingLabels( Inputs, Targets, Nneurons, Nbatch )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here

Nlabel = size(Targets,2);
Input_size = size(Inputs,2);

Targets_train = zeros(Nlabel, size(Targets,1), 2);
for i = 1:10
    
    Targets_train(i,:,1) = Targets(:,i);
end


Input_weights = zeros(Nlabel,Input_size,Nneurons);
Input_biases = zeros(Nlabel,1,Nneurons);
Sorting_weights = zeros(Nlabel,Input_size,1);
Batch_bounds = zeros(Nlabel,Nbatch,2);
Output_weights = zeros(Nlabel,Nbatch,Nneurons,2);

for i = 1:10
    
    [Iw, Ib, Sw, Bb, Ow] = RealTimeELMtrain( Inputs, squeeze(Targets_train(i,:,:)), Nneurons, Nbatch );
    
    Input_weights(i,:,:) = Iw;
    Input_biases(i,:,:) = Ib;
    Sorting_weights(i,:,:) = Sw;
    Batch_bounds(i,:,:) = Bb;
    Output_weights(i,:,:,:) = Ow;
end


end

