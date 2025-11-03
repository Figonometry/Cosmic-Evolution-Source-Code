package spacegame.util;

import spacegame.core.CosmicEvolution;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class Logger {
    public JFrame window;
    public JPanel mainPanel;
    public Logger(Throwable throwable){
        Calendar calendar = new GregorianCalendar();
        File crashFile = new File(CosmicEvolution.instance.launcherDirectory + "/crashReports/crashReport" + "-" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.HOUR_OF_DAY)  + calendar.get(Calendar.MINUTE)  + calendar.get(Calendar.SECOND) +  ".txt");
        PrintStream ps;
        try {
            ps = new PrintStream(crashFile);
            throwable.printStackTrace(ps);
            throwable.printStackTrace();
            ps.println("WARNING: EXCEPTION FOUND IN THREAD: " + Thread.currentThread().getName());
            System.out.println("WARNING: EXCEPTION FOUND IN THREAD: " + Thread.currentThread().getName());
            ps.println("Please forward the stacktrace to Fig");
            ps.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Logger(String string){
        Calendar calendar = new GregorianCalendar();
        File crashFile = new File(CosmicEvolution.instance.launcherDirectory + "/crashReports/crashReport" + "-" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.HOUR_OF_DAY)  + calendar.get(Calendar.MINUTE)  + calendar.get(Calendar.SECOND) +  ".txt");
        PrintStream ps;
        try {
            ps = new PrintStream(crashFile);
            ps.println(string);
            ps.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        JFrame frame = new JFrame("Cosmic Evolution Info Window");
        this.window = frame;
        this.window.setSize(new Dimension(854, 480));
        this.window.setLocationRelativeTo(null);
        this.window.setResizable(false);
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.BLACK);
        this.mainPanel = panel;

        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setForeground(Color.YELLOW);
        textArea.setFocusable(false);
        textArea.setBackground(new Color(0,0,0,0));
        textArea.setBorder(null);
        textArea.setOpaque(false);
        textArea.setText(string);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(0, 0, 854, 480);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBackground(new Color(0,0,0,0));
        scrollPane.getViewport().setBackground(new Color(0,0,0,0));
        scrollPane.setViewportBorder(null);

        panel.add(scrollPane);
        frame.add(panel);
        frame.setVisible(true);
    }

    public Logger(String string, boolean forMainInvocation){
        JFrame frame = new JFrame("Cosmic Evolution Info Window");
        this.window = frame;
        this.window.setSize(new Dimension(854, 480));
        this.window.setLocationRelativeTo(null);
        this.window.setResizable(false);
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.BLACK);
        this.mainPanel = panel;

        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setForeground(Color.YELLOW);
        textArea.setFocusable(false);
        textArea.setBackground(new Color(0,0,0,0));
        textArea.setBorder(null);
        textArea.setOpaque(false);
        textArea.setText(string);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(0, 0, 854, 480);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBackground(new Color(0,0,0,0));
        scrollPane.getViewport().setBackground(new Color(0,0,0,0));
        scrollPane.setViewportBorder(null);

        panel.add(scrollPane);
        frame.add(panel);
        frame.setVisible(true);
    }


    public Logger(Throwable throwable, boolean launchWindow){
        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(throwable.toString());
        for(int i = 0; i < stackTraceElements.length; i++){
            stackTrace.append("\n");
            stackTrace.append(stackTraceElements[i].toString());
        }
        stackTrace.append("\n");
        stackTrace.append("\n");
        stackTrace.append("Please forward the stacktrace to Fig at cosmicevolution.bugreport@gmail.com");
        stackTrace.append("\n");
        stackTrace.append("A crash file has been generated within the crashLogs folder that can be attached and sent");
        stackTrace.append("\n");
        stackTrace.append("On Windows this is under AppData");
        stackTrace.append("\n");
        stackTrace.append("On Mac this is under Library/Application Support/");
        stackTrace.append("\n");
        stackTrace.append("On Linux this is under the user's home directory");
        String stackTraceMessage = stackTrace.toString(); //Acquire each element of the stack trace as a string with a new line to display in the window

        //Still log in a crash file
        Calendar calendar = new GregorianCalendar();
        File crashFile = new File(CosmicEvolution.instance.launcherDirectory + "/crashReports/crashReport" + "-" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.HOUR_OF_DAY)  + calendar.get(Calendar.MINUTE)  + calendar.get(Calendar.SECOND) +  ".txt");
        PrintStream ps;
        try {
            ps = new PrintStream(crashFile);
            throwable.printStackTrace(ps);
            throwable.printStackTrace();
            ps.println("WARNING: EXCEPTION FOUND IN THREAD: " + Thread.currentThread().getName());
            System.out.println("WARNING: EXCEPTION FOUND IN THREAD: " + Thread.currentThread().getName());
            ps.println("Please forward the stacktrace to Fig");
            ps.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }



        JFrame frame = new JFrame("Cosmic Evolution Crash Window");
        this.window = frame;
        this.window.setSize(new Dimension(854, 480));
        this.window.setLocationRelativeTo(null);
        this.window.setResizable(false);
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.BLACK);
        this.mainPanel = panel;

        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setForeground(Color.YELLOW);
        textArea.setFocusable(false);
        textArea.setBackground(new Color(0,0,0,0));
        textArea.setBorder(null);
        textArea.setOpaque(false);
        textArea.setText(stackTraceMessage);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(0, 0, 854, 480);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBackground(new Color(0,0,0,0));
        scrollPane.getViewport().setBackground(new Color(0,0,0,0));
        scrollPane.setViewportBorder(null);

        panel.add(scrollPane);
        frame.add(panel);
        frame.setVisible(true);
    }



}
