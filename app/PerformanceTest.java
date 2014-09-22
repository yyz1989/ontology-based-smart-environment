import controllers.OntoControlApi;

/**
 * Created with IntelliJ IDEA.
 * User: yuanzhe
 * Date: 13-12-22
 * Time: 下午11:50
 * To change this template use File | Settings | File Templates.
 */

/**
 * A test case to evaluate the processing performance of incoming state changes
 */
public class PerformanceTest {
    public static void main(String[] args) {
        Runtime r = Runtime.getRuntime();
        long t1 = r.totalMemory()/1024;
        long f1 = r.freeMemory()/1024;
        System.out.println("total memory at initialization: "+t1+"KB");
        System.out.println("total memory at initialization: "+f1+"KB");
        System.out.println("application started at "+System.currentTimeMillis());
        OntoControlApi.getInstance();
        r = Runtime.getRuntime();
        t1 = r.totalMemory()/1024;
        f1 = r.freeMemory()/1024;
        System.out.println("total memory at initialization: "+t1+"KB");
        System.out.println("total memory at initialization: "+f1+"KB");
        int n;
        for (int i=1;i<=400;i++) {
            n=i%10;
            switch(n) {
                case 0:
                    OntoControlApi.addDataPropertyAssertion("hasSensedState","presencedetector5","1.0","0.0",Boolean.toString(false));
                    break;
                case 1:
                    OntoControlApi.addDataPropertyAssertion("hasSensedState","lightsensor5","1.0","0.0",Boolean.toString(false));
                    break;
                case 2:
                    OntoControlApi.addDataPropertyAssertion("hasSensedState","thermometer5","15.0","25.0",Boolean.toString(false));
                    break;
                case 6:
                    OntoControlApi.addDataPropertyAssertion("hasSensedState","presencedetector5","0.0","1.0",Boolean.toString(false));
                    break;
                case 7:
                    OntoControlApi.addDataPropertyAssertion("hasSensedState","lightsensor5","0.0","1.0",Boolean.toString(false));
                    break;
                case 5:
                    OntoControlApi.addDataPropertyAssertion("hasSensedState","thermometer5","25.0","15.0",Boolean.toString(false));
                    break;
                case 3:
                    OntoControlApi.addDataPropertyAssertion("hasSensedState","presencedetector4","1.0","0.0",Boolean.toString(false));
                    break;
                case 4:
                    OntoControlApi.addDataPropertyAssertion("hasSensedState","lightsensor4","1.0","0.0",Boolean.toString(false));
                    break;
                case 8:
                    OntoControlApi.addDataPropertyAssertion("hasSensedState","presencedetector4","0.0","1.0",Boolean.toString(false));
                    break;
                case 9:
                    OntoControlApi.addDataPropertyAssertion("hasSensedState","lightsensor4","0.0","1.0",Boolean.toString(false));
                    break;
            }
        }
        System.out.println("application finished at "+System.currentTimeMillis());
        r = Runtime.getRuntime();
        t1 = r.totalMemory()/1024;
        f1 = r.freeMemory()/1024;
        System.out.println("total memory at initialization: "+t1+"KB");
        System.out.println("total memory at initialization: "+f1+"KB");
    }
}
