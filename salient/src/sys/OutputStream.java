package sys;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yunghegel.gdx.utils.StringUtils;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.yunghegel.gdx.utils.console.ConsoleUtils.getClassMethodLine;

public class OutputStream{

    public interface OutputListener{
        void onLog(String msg);
    }

    public static OutputListener listener;

    public OutputStream() {
//        init();
    }

    void init(){
//        System.setOut(new PrintStream(System.out){
//            @Override
//            public void println(String x) {
//                super.println(x);
//                if(listener != null){
//                    listener.onLog(x);
//                }
//            }
//
//            @Override
//            public void print(String s) {
//                super.print(s);
//                if(listener != null){
//                    listener.onLog(s);
//                }
//            }
//        });
//        System.out.flush();
    }


}
