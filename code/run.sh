clear
javac *.java

echo "\033[0;46mStarting Simulation: \033[0m"
for FLAG in 1 4 5 2 3 9 8 6 10
do
    java Driver $FLAG    
done
rm *.class

python3 plotter.py
