package spacegame.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class Logger {
    public Logger(Exception exception){
        Calendar calendar = new GregorianCalendar();
        File crashFile = new File(SpaceGame.instance.launcherDirectory + "/crashReports/crashReport" + "-" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.HOUR_OF_DAY)  + calendar.get(Calendar.MINUTE)  + calendar.get(Calendar.SECOND) +  ".txt");
        PrintStream ps;
        try {
            ps = new PrintStream(crashFile);
            exception.printStackTrace(ps);
            exception.printStackTrace();
            ps.println("WARNING: EXCEPTION FOUND IN THREAD: " + Thread.currentThread().getName());
            System.out.println("WARNING: EXCEPTION FOUND IN THREAD: " + Thread.currentThread().getName());
            ps.println("Please forward the stacktrace to Fig");
            ps.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Should probably update the Logger to handle when the JVM aborts, launch a window to notify the player so they're not stuck wondering wtf happened

}
