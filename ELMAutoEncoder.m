function [ Outputs ] = TestELMAutoEncoder( Inputs, Targets, Input_weights, Input_biases, Output_weights )
%ELMAutoEncoder Creates an auto-encoder by pretreating the inputs 
%   Detailed explanation goes here

I = emptyCell(1, size(Output_weights,1) + 1);
I{1} = Inputs;

for Layer = 1 : size(Output_weights,1) - 1
    
    I{Layer + 1} = CrossLayer( I{Layer}, Output_weights{Layer} );
    
end

I{size(Output_weights,1) + 1} = ClassicELMtrain( I{size(Nlayers,2)}, Targets, Neurons(Layer) );

end

