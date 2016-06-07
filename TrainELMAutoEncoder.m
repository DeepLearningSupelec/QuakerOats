function [ Network_weights ] = TrainELMAutoEncoder( Inputs, Layers )
%ELMAutoEncoder Creates an auto-encoder by pretreating the inputs 
%   Detailed explanation goes here

I = emptyCell(1,size(Layers,2) + 1);
Network_weights = emptyCell(1,size(Layers,2) + 1);
I{1} = Inputs;

for Layer = 1 : size(Nlayers,2) - 1
    
    [ ~, ~, Output_weights] = ClassicELMtrain( I{Layer}, I{Layer}, Neurons(Layer) );
    Network_weights{Layer} = Output_weights';
    I{Layer + 1} = CrossLayer( I{Layer}, Network_weights{Layer});
    
end


end

