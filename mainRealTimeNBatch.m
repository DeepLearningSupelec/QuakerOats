%% Loading the dataset
tic
Inputs_data = loadMNISTImages('E:/Deep Learning/ELM Code Matlab/train-images.idx3-ubyte');
Inputs_data = Inputs_data';
Ot = loadMNISTLabels('E:/Deep Learning/ELM Code Matlab/train-labels.idx1-ubyte');
Targets_data = zeros(60000, 10);
for i = 1:60000
    Targets_data(i,Ot(i)+1) = 1;
end
clear Ot

Inputstest = loadMNISTImages('E:/Deep Learning/ELM Code Matlab/t10k-images.idx3-ubyte');
Inputstest = Inputstest';
Ot = loadMNISTLabels('E:/Deep Learning/ELM Code Matlab/t10k-labels.idx1-ubyte');
Targetstest = zeros(10000, 10);
for i = 1:10000
    Targetstest(i,Ot(i)+1) = 1;
end
clear Ot
disp(['Dataset loaded in ' num2str(toc) 's']);


% Batches = 1:20;
% Batches = [1, 2, 3, 4, 5, 10, 15, 20, 30, 40, 50, 75, 100];
Batches = [ 400, 500, 600, 700, 800, 900, 1000 ];
Train_error_rates = zeros(100,size(Batches,2));
Test_error_rates = zeros(100,size(Batches,2));
Training_time = zeros(1,size(Batches,2));
Nneurones = 500;

for loop = 1:size(Batches,2)
    
    Nbatch = Batches(loop);
    train_std = 10;
    test_std = 10;
    Niter = 1;
    t = tinv(0.95,2);
    
    while ((train_std*t/sqrt(Niter) > 0.0015)||(test_std*t/sqrt(Niter) > 0.0015)||(Niter < 5))&&(Niter < 100)
       
        disp( ['Nbatch = ' num2str(Nbatch) ', ' num2str(train_std*t/sqrt(Niter)) ' ' num2str(test_std*t/sqrt(Niter))]);
        %% Training the network
        tic
        disp('Start training the network ...');
        [Input_weights, Input_biases, Sorting_weights, Batch_bounds, Output_weights] = RealTimeELMtrain( Inputs_data, Targets_data, Nneurones, Nbatch );
        disp(['Network trained in ' num2str(toc) 's']);
        Training_time(loop) = toc;
        %% Training error
        Outputs = RealTimeELMtest( Inputs_data, Input_weights, Input_biases, Sorting_weights, Batch_bounds, Output_weights );
        disp(['training accuracy is ' num2str(100*mean(Single_compare(Outputs, Targets_data))) '%']);
        
        Train_error_rates(Niter,loop) = mean(Single_compare(Outputs, Targets_data));
        train_std = std(Train_error_rates(1:Niter,loop));
               
        %% Testing the network on test dataset
        
        Outputs = RealTimeELMtest( Inputstest, Input_weights, Input_biases, Sorting_weights, Batch_bounds, Output_weights );       
        disp(['test accuracy is ' num2str(100*mean(Single_compare(Outputs, Targetstest))) '%']);
        Test_error_rates(Niter,loop) = mean(Single_compare(Outputs, Targetstest));
        test_std = std(Test_error_rates(1:Niter,loop));
        Niter = Niter + 1;
    end
    Test_error_rates(100, loop) = mean(Test_error_rates(1:Niter-1, loop));
    Train_error_rates(100, loop) = mean(Train_error_rates(1:Niter-1, loop));
    
end

%%
figure
Nneurones = 500;

plot(Batches, Test_error_rates(100,:) , Batches, Train_error_rates(100,:))

title(['Erreur en test (-) et en train (--) pour ' num2str(Nneurones) ' neurones par batch, en fonction du nombre de batch '])
xlabel('Nombre de batch')
ylabel('Taux d''erreur (+/- 0,1%)')
hold on
disp([num2str(toc)]);