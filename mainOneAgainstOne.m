
%% Loading the dataset
Inputs_train = loadMNISTImages('E:/Deep Learning/ELM Code Matlab/train-images.idx3-ubyte');
Inputs_train = Inputs_train';
Inputs_test = loadMNISTImages('E:/Deep Learning/ELM Code Matlab/t10k-images.idx3-ubyte');
Inputs_test = Inputs_test';

Otrn = loadMNISTLabels('E:/Deep Learning/ELM Code Matlab/train-labels.idx1-ubyte');
Targets_train = zeros(size(Otrn,1),10);

for i = 1:size(Otrn,1)
    Targets_train(i,Otrn(i)+1) = 1;
end


Ot = loadMNISTLabels('E:/Deep Learning/ELM Code Matlab/t10k-labels.idx1-ubyte');
Targets_test = zeros(size(Ot,1),10);

for i = 1:size(Ot,1)
    Targets_test(i,Ot(i)+1) = 1;
end

%% train
tic
Nneurons = 500;
Nbatch = 2;
Input_size = 784;
Nlabel = 10;

Labels = cell(1,10);
for i = 0:9
    Labels{i+1} = find(Otrn == i);
end

%%
Input_weights_oao = cell(Nlabel,Nlabel);
Input_biases_oao = cell(Nlabel,Nlabel);
Sorting_weights_oao = cell(Nlabel,Nlabel);
Batch_bounds_oao = cell(Nlabel,Nlabel);
Output_weights_oao = cell(Nlabel,Nlabel);

%%

for i = 1:Nlabel-1
    for j = i+1:Nlabel
        
        It = Inputs_train([Labels{i}; Labels{j}],:);
        Tt = Targets_train([Labels{i}; Labels{j}],:);
        [Iw, Ib, Sw, Bb, Ow] = RealTimeELMtrain( It, Tt, Nneurons, Nbatch );
        Tsto = RealTimeELMtest( It, Iw, Ib, Sw, Bb, Ow );
        disp([ num2str(i) ' vs ' num2str(j) ' labels, train accuracy : ' num2str(100*mean(Single_compare(Tsto, Tt))) '%'] );
        
        Input_weights_oao{i,j} = Iw;
        Input_biases_oao{i,j} = Ib;
        Sorting_weights_oao{i,j} = Sw;
        Batch_bounds_oao{i,j} = Bb;
        Output_weights_oao{i,j} = Ow;
    end
end

%% test

Outputs = cell(Nlabel, Nlabel);
%%
for i = 1:Nlabel-1
    for j = i+1:Nlabel
        Iw = Input_weights_oao{i,j};
        Ib = Input_biases_oao{i,j};
        Sw = Sorting_weights_oao{i,j};
        Bb = Batch_bounds_oao{i,j};
        Ow = Output_weights_oao{i,j};
        Outputs{i,j} = RealTimeELMtest( Inputs_test, Iw, Ib, Sw, Bb, Ow );
    end
end
%%
for i = 1:10
    Outputs{i,i} = zeros(10000,10);
    for j = 1:i-1
        Outputs{i,j} = Outputs{j,i};
    end
end
%%
test = zeros(10,10000,10);
for i = 1:10
    for j = 1:10
        test(i,:,:) = squeeze(test(i,:,:)) + Outputs{i,j};
    end
end
%%
Value = zeros(10000,10);
for i = 1:10000
    for j = 1:10
        Value(i,j) = test(j,i,j);
    end
end
%%
% Output = zeros(10000,10);
% for i = 1:10000
%     V = Value{i};
%     m = 0;
%     for j = 1:10
%         t = V(j);
%         l = 1;
%         if t > m
%             m = t;
%             l = j;
%         end
%     end
%     Output(i,l) = 1;
% end
%%
disp(num2str(mean(Single_compare(Value, Targets_test))));
toc


%%

[~, Output_labels] = max(Value, [], 2);
x = zeros(10000,1);
for i = 1:10000
    x(i) = Value(i,Output_labels(i));
end
y = max(Value,[],2);


treshold = 6:0.01:9.99;
datasizee = zeros(400,1);
accuracyy = zeros(400,1);
for i = 1:400
    I = find(x>treshold(i));
    sureOutputs = Value(I,:);
    sureTargets = Targets_test(I,:);
    datasizee(i) = size(I,1)/100;
    accuracyy(i) = 100*mean(Single_compare(sureOutputs, sureTargets));
end

plot(datasizee, accuracyy);

        