package sys;



import app.Editor;
import app.Salient;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yunghegel.gdx.utils.StringUtils;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.yunghegel.gdx.utils.console.ConsoleUtils.*;

public class Log extends Formatter
{

    public static Logger logger;

    static public final int LEVEL_NONE = 6;
    /** Critical errors. The application may no longer work correctly. */
    static public final int LEVEL_ERROR = 5;
    /** Important warnings. The application will continue to work correctly. */
    static public final int LEVEL_WARN = 4;
    /** Informative messages. Typically used for deployment. */
    static public final int LEVEL_INFO = 3;
    /** Debug messages. This level is useful during development. */
    static public final int LEVEL_DEBUG = 2;
    /** Trace messages. A lot of information is logged, so this level is usually only needed when debugging a problem. */
    static public final int LEVEL_TRACE = 1;


    static Throwable ex;
    /**
     * The level of messages that will be logged. Compiling this and the booleans below as "final" will cause the compiler to
     * remove all "if (Log.info) ..." type statements below the set level.
     */
    static private int level = LEVEL_INFO;
    /** True when the ERROR level will be logged. */
    static public boolean ERROR = level <= LEVEL_ERROR;
    /** True when the WARN level will be logged. */
    static public boolean WARN = level <= LEVEL_WARN;
    /** True when the INFO level will be logged. */
    static public boolean INFO = level <= LEVEL_INFO;
    /** True when the DEBUG level will be logged. */
    static public boolean DEBUG = level <= LEVEL_DEBUG;
    /** True when the TRACE level will be logged. */
    static public boolean TRACE = level <= LEVEL_TRACE;

    static {
        logger = addLogger(Log.class);
        //        console = EditorStage.getInstance().getConsole();
    }

    static Consumer<LogRecord> consumer;

    public static String l(String message, Consumer<LogRecord> get) {
        logger.info(message);
        get.accept(records.pop());
       return message;

    }

    //conveinence method to add a logger to a class
    public static Logger addLogger(Class<?> clazz) {
        Logger logger = new Logger(clazz.getName() , null)
        {
            @Override
            public void info(String msg) {
                super.info(msg);
            }
        };

        Handler handler = new java.util.logging.ConsoleHandler();
        handler.setFormatter(new Log());
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        return logger;

    }

    public static void info(String message) {
        info(null , message);
    }

    public static String method(){
        return getCallerCallerClassName();
    }

    public static void info(String category , String message) {


        String method = getClassMethodLine(Log.class);
        final long firstLogTime = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder(256);

        String time = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        builder.append(StringUtils.colorize(time , StringUtils.Ansi.BLUE));
        builder.append(' ');




        builder.append(StringUtils.colorize(method , StringUtils.Ansi.YELLOW));

        if (category != null) {

        }

        builder.append('\n');
        builder.append(StringUtils.colorize(message , StringUtils.Ansi.GREEN));


        if (ex != null) {
            StringWriter writer = new StringWriter(256);
            ex.printStackTrace(new PrintWriter(writer));
            builder.append('\n');
            builder.append(writer.toString().trim());

        }




        System.out.println(builder);
    }

    static Stack<LogRecord> records = new Stack<>();

    @Override
    public String format(LogRecord record) {
        records.push(record);
        String builder = "[" + record.getLevel() + "] " + record.getSourceClassName() + "." + record.getSourceMethodName() + " " + "\n" + this.formatMessage(record) + System.lineSeparator();
        return builder;
    }


}






