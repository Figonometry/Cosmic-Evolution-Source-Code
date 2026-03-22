package spacegame.util;

import java.io.File;

public final class GeneralUtil {

    public static void deleteDirectory(File directoryToBeDeleted){
        File[] allContents = directoryToBeDeleted.listFiles();
        File file;
        if(allContents != null){
            for(int i = 0; i < allContents.length; i++){
                file = allContents[i];
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}
