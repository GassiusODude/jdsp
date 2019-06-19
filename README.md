# jdsp
jdsp is my initial attempt at building a library for signal processing in the Java language.  

# Install
To install, perform the following actions.
1. Build
    ~~~BASH
    $ gradle build
    ~~~
2. Add the jar file to the $CLASSPATH
3. Generate Javadoc
    ~~~BASH
    $ gradle javadoc
    ~~~
# Examples
## Example: Creating and applying a Filter object
~~~Java
import jdsp.filters.Filter;
// constructor defaults to moving average filter
Filter f = new Filter(11);
// update filter
f.designFilter(101, "HAMMING", 0.1);

// apply filter
float[] data0 = {1.0f, 2.0f, 3.4f};
float[] data1 = {1.0f, 2.0f, 3.4f};
float [] out1, out2;
out1 = f.applyFilter(data0);

// continue filter on second data segment
out2 = f.applyFilter(data1);
~~~
## Example Plots and Figures
Some functionality was created with Java Swing to provide visual tools to be used with this package.
