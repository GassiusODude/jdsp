# jdsp
jdsp is my initial attempt at building a library for signal processing in the Java language.  

<span style="color:red">As I'm still learning how to setup projects and programming in Java, the code is by no means optimized (and not necessarily correct).</span>

# Install
To install, perform the following actions.
1. Build
    $ gradle build
2. Add the jar file to the $CLASSPATH

# Examples
## Example: Creating a Filter object
This is not ready yet.

## Example: Static method convolve
~~~Java
import jdsp.filters.Filter;
public class Test{
    public void main(String[] args){
        float[] f1 = {1,2,3};
        float[] f2 = {4,5,6,7,8,9};
        float[] out = Filter.convolve(f1, f2);
    }
}
~~~

