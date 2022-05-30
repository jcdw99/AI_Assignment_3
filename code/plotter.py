import matplotlib.pyplot as plt
import os
import pandas as pd
import numpy as np

colPair = {'pso':'blue', 'de':'red', 'bb_mod':'green', 'bb_orig':'black'}


def swapName(input):
    if input == "BB_ORIG":
        return 'BB-BC'
    if input == "BB_MOD":
        return 'MBB-BC'
    return input

""" 
This function returns the AVERAGE data stored in the specified directory. 
The benchmarkfunction to which this data corresponds is specified by the directory name
"""
def get_average(direct, alg, width=80):
    path = rootway + direct + '/' + alg + '_avg.csv'
    df = pd.read_csv(path).transpose()
    df.reset_index(inplace=True)
    df.columns = ['iters', 'avg']
    df = np.transpose(df.to_numpy())
    df[0] = np.array([int(i) for i in df[0]])
    return df[:,:width]

"""
This function returns the raw data stored in the specified directory.
The benchmarkfunction to which this data corresponds is specified by the directory name
"""
def get_raw(direct, alg, width=80):
    path = rootway + direct + '/' + alg + '_raw.csv'
    df = pd.read_csv(path).transpose()
    df.reset_index(inplace=True)
    df = df.transpose()
    df = df.to_numpy()
    df[0] = np.array([int(i) for i in df[0]])
    return df[:,:width]

""" 
This function returns the VARIANCE data stored in the specified directory. 
The benchmarkfunction to which this data corresponds is specified by the directory name
"""
def get_var(direct, alg, width=80):
    path = rootway + direct + '/' + alg + '_var.csv'
    df = pd.read_csv(path).transpose()
    df.reset_index(inplace=True)
    df.columns = ['iters', 'dev']
    df = np.transpose(df.to_numpy())
    df[0] = np.array([int(i) for i in df[0]])
    return df[:,:width]
    
"""
This function plots the 2d array of data. The first row of this array must be the desired x axis.
Thereafter, the rows represent the data that should be plotted on this x axis.
"""
def plot_2darr_raw(data):
    x_axis = data[0]
    observations = data[1:]
    for i in range(1,len(observations)):
        plt.plot(x_axis, observations[i], alpha=0.3)
    ax = plt.gca()
    ax.set_xlabel("Iteration")
    ax.set_ylabel("Objective function evaluation")
    return plt    

"""
Add Average to center of plot
"""
def add_avg_center(plot, directory, type1, width, dev=True, colorr='blue'):
        averages = get_average(directory, type1, width)
        avgdat = np.array((averages[1].astype(float)), dtype=float)
        plot.plot(averages[0], averages[1], alpha = 1, linewidth=2, label='Average for ' + str(directory) + ' using ' + swapName(type1.upper()), color=colorr)
        if (dev):
            vardat = get_var(directory, type1, width)
            devdat = np.array(np.sqrt(vardat[1].astype(float)), dtype=float)
            plot.fill_between([int(i) for i in averages[0]], np.array(avgdat + devdat), np.array(avgdat - devdat), alpha=0.3, label="Standard Deviation", color=colorr)
        plot.legend()
        return plot


"""
plots techniques on the same plot, for easier comparison
"""
def plot_supplied_on_same(supplied, directory, width):
    for index in range(len(supplied)):
        add_avg_center(plt, directory, supplied[index], width, True, colPair.get(supplied[index]))

    ax = plt.gca()
    ax.set_xlabel("Iteration")
    ax.set_ylabel("Objective function evaluation")
    return plt 

"""
plots techniques on the same plot, for easier comparison
"""
def plot_both_on_same(directory, width):

    decolor = 'red'
    psocolor='blue'
    # raw_de_data = get_raw(directory, 'de', width)
    # observations_de = raw_de_data[1:]
    # for i in range(1,len(observations_de)):
    #     pass
    #     # plt.plot(x_axis, observations_pso[i], alpha=0.3, color=psocolor)
    #     # plt.plot(x_axis, observations_de[i], alpha=0.3, color=decolor)

    add_avg_center(plt, directory, 'pso', width, True, psocolor)
    add_avg_center(plt, directory, 'de', width, True, decolor)
    return plt    

"""
Plot raw data with average center plot
"""
def plot_single(directory, type, width):
    plot = add_avg_center(plot_2darr_raw(get_raw(directory, type, width)), directory, type, width)
    return plot


rootway = '../results/'
directory_list = next(os.walk(rootway))[1]

def plot_all_means(type, show=False):
    names = ["Ackley", "Rastrigin", "Ellip", "CosMix", "Step", "Quartic", "Mishra1", "Salomon", "BentCigar"]
    widths = []
    if (type == 'pso'):
        widths = [400,200,30,100,20, 30, 20, 250, 25]
    elif (type == 'de'):
        # widths = [400,200,30,100,20, 30, 20, 250, 25]
        widths = [80, 80, 20, 50, 20, 10, 10, 100, 20]
    else:
        widths = [1000] * 9

    for i in range(len(names)):
        plot = plot_single(names[i], type, widths[i])
        if (show):
            plot.show()
        else:
            plot.savefig(rootway + "/{}/{}_average_{}".format(names[i], names[i], str(type)) + ".png")
        plot.clf()


def plot_all_both(show=False):
    names = ["Ackley", "Rastrigin", "Ellip", "CosMix", "Step", "Quartic", "Mishra1", "Salomon", "BentCigar"]
    widths = [600,650,440,450,350, 450, 455, 750, 480]
    
    for i in range(len(names)):
        # plots all on this axis
        plot = plot_supplied_on_same(['pso', 'de', 'bb_mod', "bb_orig"], names[i], widths[i])
        if (show):
            plot.show()
        else:
            plot.savefig(rootway + "/{}/{}_all".format(names[i], names[i]) + ".png")
        plot.clf()
       
        plot = plot_supplied_on_same(['pso', 'de'], names[i], widths[i])
        if (show):
            plot.show()
        else:
            plot.savefig(rootway + "/{}/{}_pso_de".format(names[i], names[i]) + ".png")
        plot.clf()
        
        plot = plot_supplied_on_same(['pso', 'bb_mod'], names[i], widths[i])
        if (show):
            plot.show()
        else:
            plot.savefig(rootway + "/{}/{}_pso_bbmod".format(names[i], names[i]) + ".png")
        plot.clf()
       
        plot = plot_supplied_on_same(['de', 'bb_mod'], names[i], widths[i])
        if (show):
            plot.show()
        else:
            plot.savefig(rootway + "/{}/{}_de_bbmod".format(names[i], names[i]) + ".png")
        plot.clf()
        
        plot = plot_supplied_on_same(['bb_orig', 'bb_mod'], names[i], widths[i])
        if (show):
            plot.show()
        else:
            plot.savefig(rootway + "/{}/{}_bborig_bbmod".format(names[i], names[i]) + ".png")
        plot.clf()

        plot = plot_supplied_on_same(['bb_orig', 'de'], names[i], widths[i])
        if (show):
            plot.show()
        else:
            plot.savefig(rootway + "/{}/{}_bborig_de".format(names[i], names[i]) + ".png")
        plot.clf()


        plot = plot_supplied_on_same(['bb_orig', 'pso'], names[i], widths[i])
        if (show):
            plot.show()
        else:
            plot.savefig(rootway + "/{}/{}_bborig_pso".format(names[i], names[i]) + ".png")
        plot.clf()



plot_all_means('pso')
plot_all_means('de')
plot_all_means('bb_mod')
plot_all_means('bb_orig')
plot_all_both()

print("\nDONE\n")



# for directory in directory_list:
#     file_list = os.listdir(rootway + str(directory))
#     plt = plot_2darr_raw(get_raw(directory, 'pso'), directory, 'pso')
#     plt.show()

