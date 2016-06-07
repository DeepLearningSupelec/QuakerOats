function [ Outputs ] = RealTimeELMtest( Inputs, Input_weights, Input_biases, Sorting_weights, Batch_bounds, Output_weights )
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here

L = size(Batch_bounds,1);
Outputs = zeros(size(Inputs,1),size(Output_weights,3));

Batched_outputs = cell(1,L);
I = cell(1,L);

bounds = (Batch_bounds(1:L-1,2) + Batch_bounds(2:L,1))/2;
bounds = [ -inf bounds' inf ];

% % debug
% helllo = size(Inputs);
% hellloo = size(Sorting_weights);
% helllooo = size(bounds);
% hellloooo = Inputs*Sorting_weights <= bounds(2+1) & Inputs*Sorting_weights > bounds(2);
% %




for i = 1:L
    
    I{i} = find(Inputs*Sorting_weights <= bounds(i+1) & Inputs*Sorting_weights > bounds(i));
    Batched_outputs{i} = ClassicELMtest(Inputs(I{i},:), Input_weights, Input_biases, squeeze(Output_weights(i,:,:)));
    Outputs(I{i},:) = Batched_outputs{i};
    
end
end