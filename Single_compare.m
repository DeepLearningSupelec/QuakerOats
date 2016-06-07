function [ Delta ] = Single_compare( Outputs, Targets )
%UNTITLED5 Summary of this function goes here
%   Detailed explanation goes here
[~, Output_labels] = max(Outputs, [], 2);
[~, Target_labels] = max(Targets, [], 2);

Delta = Output_labels == Target_labels;

end

