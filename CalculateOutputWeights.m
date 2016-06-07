function Output_weights = CalculateOutputWeights( Inputs, Input_weights, Input_biais, Targets )

%   This function calculates the output weights matrix (often called beta
%   in the literrature) using a batch of K inputs,
%
%   Inputs : Matrix  K x Inputsize
%   Input_weights : Matrix Nb_input_neurons x Nb_hidden_neurons
%   Input_biais : Vector Nd_hidden_neurons
%   Output_target : Matrix K x Outputsize


H = Inputs*Input_weights;
for i = 1:size(H,1)
    H(i,:) = H(i,:) + Input_biais;
    H(i,:) = 1./(1+exp(-H(i,:)));
end
% for i = 1:size(H,1)
%     for j = 1:Nhiddenneuron
%         H(i,j) = 1/(1+exp(-H(i,j)));
%     end
% end
C = 10;
Output_weights = (eye(size(H,2))/C + H'*H)\H'*Targets;
end

