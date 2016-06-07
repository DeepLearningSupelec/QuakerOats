%% Loading the dataset
tic
Inputs = loadMNISTImages('E:/Deep Learning/ELM Code Matlab/train-images.idx3-ubyte');
Inputs = Inputs';
Ot = loadMNISTLabels('E:/Deep Learning/ELM Code Matlab/train-labels.idx1-ubyte');
Targets = zeros(60000, 10);
for i = 1:60000
    Targets(i,Ot(i)+1) = 1;
end
clear Ot

%% Training the network
tic
Nneurons = 10000;
Nbatch = 2;
[Input_weights, Input_biases, Sorting_weights, Batch_bounds, Output_weights] = RealTimeELMtrain( Inputs, Targets, Nneurons, Nbatch );

%% Training error
Outputs = RealTimeELMtest( Inputs, Input_weights, Input_biases, Sorting_weights, Batch_bounds, Output_weights );
disp(['train accuracy with' num2str(Nneurons) ' neurons and ' num2str(Nbatch) ' batches is ' num2str(100*mean(Single_compare(Outputs, Targets))) '%']);

%% Testing the network on test dataset
Inputs = loadMNISTImages('E:/Deep Learning/ELM Code Matlab/t10k-images.idx3-ubyte');
Inputs = Inputs';

Ot = loadMNISTLabels('E:/Deep Learning/ELM Code Matlab/t10k-labels.idx1-ubyte');
Targets_test = zeros(10000, 10);
for i = 1:10000
    Targets_test(i,Ot(i)+1) = 1;
end
clear Ot
%%
Outputs = RealTimeELMtest( Inputs, Input_weights, Input_biases, Sorting_weights, Batch_bounds, Output_weights );
%%
disp(['test accuracy with' num2str(Nneurons) ' neurons and ' num2str(Nbatch) ' batches is ' num2str(100*mean(Single_compare(Outputs, Targets_test))) '%']);
toc
%% plotting acuracy = f(datasize)

[~, Output_labels] = max(Outputs, [], 2);
x = zeros(10000,1);
for i = 1:10000
    x(i) = Outputs(i,Output_labels(i));
end
y = max(Outputs,[],2);
I = find(x>0.63);

treshold = 0.5:0.01:1;
datasize = zeros(51,1);
accuracy = zeros(51,1);
for i = 1:51
    I = find(x>treshold(i));
    sureOutputs = Outputs(I,:);
    sureTargets = Targets_test(I,:);
    datasize(i) = size(I,1)/100;
    accuracy(i) = 100*mean(Single_compare(sureOutputs, sureTargets));
end

plot(datasize, accuracy,datasizee, accuracyy);

