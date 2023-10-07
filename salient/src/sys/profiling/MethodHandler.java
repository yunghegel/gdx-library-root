package sys.profiling;

import com.badlogic.gdx.math.WindowedMean;
import com.badlogic.gdx.utils.PerformanceCounter;
import sys.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

public class MethodHandler implements javassist.util.proxy.MethodHandler {

    public static class Metric {
        public WindowedMean mean;
        public float start;
        public float end;
        public float length;
        public int calls;

        public Metric(WindowedMean mean) {
            this.mean = mean;
        }

        public void start() {
            start = System.currentTimeMillis();
        }

        public void end() {
            end = System.currentTimeMillis();
            length = end - start;
            mean.addValue(length);
            calls++;
        }

        String trim(float f) {
            return String.format("%.5f",f);
        }


        @Override
        public String toString() {
            return "Metric{" +
                    "mean=" + mean.getMean() +
                    ", length=" + (length) +
                    ", calls=" + calls +
                    '}';
        }
    }

    HashMap<Class, HashMap<Method,Metric>> classMetrics = new HashMap<>();
    HashMap<Class, HashMap<Method,WindowedMean>> classMeans = new HashMap<>();

    public Metric getMetric(String name, Class c,Method method) {
        HashMap<Method, Metric> metrics = classMetrics.computeIfAbsent(c, k -> new HashMap<>());
        Metric metric = metrics.get(method);
        if(metric == null) {
            metric = new Metric(new WindowedMean(100));
            metrics.put(method,metric);
        }
        return metric;
    }

    public void start(String name, Class c,Method method) {
        Metric metric = getMetric(name,c,method);
        metric.start();
    }

    public void end(String name, Class c,Method method) {
        Metric metric = getMetric(name,c,method);
        metric.end();
    }


    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        start(thisMethod.getName(),self.getClass(),thisMethod);
        var ret = proceed.invoke(self, args);
        end(thisMethod.getName(),self.getClass(),thisMethod);
        Log.info(getMetric(thisMethod.getName(),self.getClass(),thisMethod).toString());
        return ret;
    }
}
