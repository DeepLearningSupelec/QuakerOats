%% Loading the dataset
Inputs_train = loadMNISTImages('E:/Deep Learning/ELM Code Matlab/train-images.idx3-ubyte');
Inputs_train = Inputs_train';
Inputs_test = loadMNISTImages('E:/Deep Learning/ELM Code Matlab/t10k-images.idx3-ubyte');
Inputs_test = Inputs_test';

Ot = loadMNISTLabels('E:/Deep Learning/ELM Code Matlab/train-labels.idx1-ubyte');
Targets_train = zeros(10, size(Ot,1), 2);
for j = 1:10
    for i = 1:size(Ot,1)
        Targets_train(Ot(i)+1,i,1) = 1;
    end
end
clear Ot

Ot = loadMNISTLabels('E:/Deep Learning/ELM Code Matlab/t10k-labels.idx1-ubyte');
Targets_test = zeros(10, size(Ot,1), 2);
for j = 1:10
    for i = 1:size(Ot,1)
        Targets_test(Ot(i)+1,i,1) = 1;
    end
end
clear Ot

%% train
Nneurons = 5000;
Nbatch = 2;
Input_size = 784;
Nlabel = 10;

Input_weights_oaa = zeros(Nlabel,Input_size,Nneurons);
Input_biases_oaa = zeros(Nlabel,1,Nneurons);
Sorting_weights_oaa = zeros(Nlabel,784,1);
Batch_bounds_oaa = zeros(Nlabel,Nbatch,2);
Output_weights_oaa = zeros(Nlabel,Nbatch,Nneurons,2);

trainOutputs = zeros(Nlabel,size(Targets_train,2),2);
testOutputs = zeros(Nlabel,size(Targets_test,2),2);

for i = 1:10
    [Iw, Ib, Sw, Bb, Ow] = RealTimeELMtrain( Inputs_train, squeeze(Targets_train(i,:,:)), Nneurons, Nbatch );
    
    trainOutputs(i,:,:) = RealTimeELMtest( Inputs_train, Iw, Ib, Sw, Bb, Ow );
    disp([ 'Recognising ' num2str(i-1) ' labels, accuracy on train data : ' num2str(100*mean(Single_compare(squeeze(trainOutputs(i,:,:)), squeeze(Targets_train(i,:,:))))) '%'] );
    testOutputs(i,:,:) = RealTimeELMtest( Inputs_test, Iw, Ib, Sw, Bb, Ow );
    disp([ 'Recognising ' num2str(i-1) ' labels, accuracy on test data : ' num2str(100*mean(Single_compare(squeeze(testOutputs(i,:,:)), squeeze(Targets_test(i,:,:))))) '%'] );
    
    Input_weights_oaa(i,:,:) = Iw;
    Input_biases_oaa(i,:,:) = Ib;
    Sorting_weights_oaa(i,:,:) = Sw;
    Batch_bounds_oaa(i,:,:) = Bb;
    Output_weights_oaa(i,:,:,:) = Ow;
end
%% Second train

Otrn = loadMNISTLabels('E:/Deep Learning/ELM Code Matlab/train-labels.idx1-ubyte');
Targets_train = zeros(size(Otrn,1), 10);
for i = 1:size(Otrn,1)
    Targets_train(i,Otrn(i)+1) = 1;
end

Outputs = squeeze(trainOutputs(:,:,1))';
disp(['train accuracy with' num2str(Nneurons) ' neurons and ' num2str(Nbatch) ' batches is ' num2str(100*mean(Single_compare(Outputs, Targets_train))) '%']);

[~, Output_labels] = max(Outputs, [], 2);
x = zeros(60000,1);
for i = 1:60000
    x(i) = Outputs(i,Output_labels(i));
end
I = find(x>0.62);
I_train_difficult = find(x<=0.62);
sureOutputs = Outputs(I,:);
sureTargets = Targets_train(I,:);
difficultOutputs = Outputs(I_train_difficult,:);
difficultTargets = Targets_train(I_train_difficult,:);
difficultInputs = Inputs_train(I_train_difficult,:);

disp(['train accuracy (one against all) on ' num2str(size(I,1)/600) '% of data, with ' num2str(Nneurons) ' neurons and ' num2str(Nbatch) ' batches is ' num2str(100*mean(Single_compare(sureOutputs, sureTargets))) '%']);

%%
[~, Output_labels] = max(difficultOutputs, [], 2);
[~, Target_labels] = max(difficultTargets, [], 2);

Labels = cell(1,10);
for i = 0:9
    Labels{i+1} = find(Output_labels == i+1);
end

%%
Input_weights_oao = cell(Nlabel,Nlabel);
Input_biases_oao = cell(Nlabel,Nlabel);
Sorting_weights_oao = cell(Nlabel,Nlabel);
Batch_bounds_oao = cell(Nlabel,Nlabel);
Output_weights_oao = cell(Nlabel,Nlabel);



for i = 1:Nlabel-1
    for j = i+1:Nlabel
        
        It = difficultInputs([Labels{i}; Labels{j}],:);
        Tt = difficultTargets([Labels{i}; Labels{j}],:);
        [Iw, Ib, Sw, Bb, Ow] = RealTimeELMtrain( It, Tt, Nneurons/200, Nbatch );
        Tsto = RealTimeELMtest( It, Iw, Ib, Sw, Bb, Ow );
        % disp([ num2str(i) ' vs ' num2str(j) ' labels, train accuracy : ' num2str(100*mean(Single_compare(Tsto, Tt))) '%'] );
        
        Input_weights_oao{i,j} = Iw;
        Input_biases_oao{i,j} = Ib;
        Sorting_weights_oao{i,j} = Sw;
        Batch_bounds_oao{i,j} = Bb;
        Output_weights_oao{i,j} = Ow;
    end
end

%% Loading data for test


Otst = loadMNISTLabels('E:/Deep Learning/ELM Code Matlab/t10k-labels.idx1-ubyte');
Targets_test = zeros(size(Otst,1), 10);
for i = 1:size(Otst,1)
    Targets_test(i,Otst(i)+1) = 1;
end

Outputs = squeeze(testOutputs(:,:,1))';
disp(['test accuracy with' num2str(Nneurons) ' neurons and ' num2str(Nbatch) ' batches is ' num2str(100*mean(Single_compare(Outputs, Targets_test))) '%']);


[~, Output_labels] = max(Outputs, [], 2);
x = zeros(10000,1);
for i = 1:10000
    x(i) = Outputs(i,Output_labels(i));
end
I = find(x>0.63);
J = find(x<=0.63);
I_test_difficult = J;
sureOutputs = Outputs(I,:);
difficultOutputs = Outputs(J,:);
sureTargets = Targets_test(I,:);
difficultTargets = Targets_test(J,:);
difficultInputs = Inputs_test(J,:);

disp(['test accuracy (one agains all) on ' num2str(size(I,1)/100) '% of data, with ' num2str(Nneurons) ' neurons and ' num2str(Nbatch) ' batches is ' num2str(100*mean(Single_compare(sureOutputs, sureTargets))) '%']);
disp(['current test accuracy (one agains all) on the other ' num2str(size(J,1)/100) '% of data, with ' num2str(Nneurons) ' neurons and ' num2str(Nbatch) ' batches is ' num2str(100*mean(Single_compare(difficultOutputs, difficultTargets))) '%']);

acc = size(I,1)/10000*mean(Single_compare(sureOutputs, sureTargets));

Labels = cell(1,10);
for i = 0:9
    Labels{i+1} = find(Otst == i);
end

Duels = zeros(size(J,1),2);
[~,Sort] = sort(difficultOutputs,2);
for i = 1:size(J,1)
    Duels(i,:) = find(Sort(i,:) == Nlabel | Sort(i,:) == Nlabel-1);
end

Duels = sort(Duels,2);
difficultOutputs = zeros(size(difficultOutputs,1),size(difficultOutputs,2));

tic
for i = 1:size(Duels,1)
    difficultOutputs(i,:) = RealTimeELMtest( difficultInputs(i,:), Input_weights_oao{Duels(i,1),Duels(i,2)}, Input_biases_oao{Duels(i,1),Duels(i,2)}, Sorting_weights_oao{Duels(i,1),Duels(i,2)}, Batch_bounds_oao{Duels(i,1),Duels(i,2)}, Output_weights_oao{Duels(i,1),Duels(i,2)});
end
toc


disp(['test accuracy (one against one) on the resting ' num2str(size(J,1)/100) '% of data, with ' num2str(Nneurons/50) ' neurons and ' num2str(Nbatch) ' batches is ' num2str(100*mean(Single_compare(difficultOutputs, difficultTargets))) '%']);
acc = acc + size(J,1)/10000*mean(Single_compare(difficultOutputs, difficultTargets));

disp( [ 'final accuracy is ' num2str(100*acc) '%' ])
%%


%% Plotting accuracy= f(datasize)

treshold = 0.5:0.01:1;
datasizee = zeros(51,1);
accuracyy = zeros(51,1);
for i = 1:51
    I = find(x>treshold(i));
    sureOutputs = Outputs(I,:);
    sureTargets = Targets_test(I,:);
    datasizee(i) = size(I,1)/100;
    accuracyy(i) = 100*mean(Single_compare(sureOutputs, sureTargets));

end


plot(datasize, accuracy, datasizee, accuracyy);

%%



%%
% Labels = cell(1,10);
% for i = 0:9
%     Labels{i+1} = find(Otst == i);
% end
%
%
% Input_weights_oao = cell(Nlabel,Nlabel);
% Input_biases_oao = cell(Nlabel,Nlabel);
% Sorting_weights_oao = cell(Nlabel,Nlabel);
% Batch_bounds_oao = cell(Nlabel,Nlabel);
% Output_weights_oao = cell(Nlabel,Nlabel);
%
% %%
%
% for i = 1:Nlabel-1
%     for j = i+1:Nlabel
%         It = Inputs_train([Labels{i}; Labels{j}],:);
%         Tt = Targets_train([Labels{i}; Labels{j}],:);
%         [Iw, Ib, Sw, Bb, Ow] = RealTimeELMtrain( It, Tt, Nneurons/2, Nbatch );
%         Tsto = RealTimeELMtest( It, Iw, Ib, Sw, Bb, Ow );
%         disp([ num2str(i) ' vs ' num2str(j) ' labels, train accuracy : ' num2str(100*mean(Single_compare(Tsto, Tt))) '%'] );
%
%         It = Inputs_test([Labels{i}; Labels{j}],:);
%         Tt = Targets_test([Labels{i}; Labels{j}],:);
%         Tsto = RealTimeELMtest( It, Iw, Ib, Sw, Bb, Ow );
%         disp([ num2str(i) ' vs ' num2str(j) ' labels, test accuracy : ' num2str(100*mean(Single_compare(Tsto, Tt))) '%'] );
%
%     end
% end
%
% %%
%
% for i = 1:Nlabel-1
%     for j = i+1:Nlabel
%         It = Inputs_train([Labels{i}; Labels{j}],:);
%         Tt = Targets_train([Labels{i}; Labels{j}],:);
%         [Iw, Ib, Sw, Bb, Ow] = RealTimeELMtrain( It, Tt, Nneurons/2, Nbatch );
%         Tsto = RealTimeELMtest( It, Iw, Ib, Sw, Bb, Ow );
%         disp([ num2str(i) ' vs ' num2str(j) ' labels, train accuracy : ' num2str(100*mean(Single_compare(Tsto, Tt))) '%'] );
%
%         It = Inputs_test([Labels{i}; Labels{j}],:);
%         Tt = Targets_test([Labels{i}; Labels{j}],:);
%         Tsto = RealTimeELMtest( It, Iw, Ib, Sw, Bb, Ow );
%         disp([ num2str(i) ' vs ' num2str(j) ' labels, test accuracy : ' num2str(100*mean(Single_compare(Tsto, Tt))) '%'] );
%
%     end
% end