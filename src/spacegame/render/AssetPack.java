package spacegame.render;

import spacegame.core.CosmicEvolution;

import javax.imageio.IIOException;
import java.io.*;
import java.util.regex.Pattern;

public final class AssetPack {
    public String filepath;
    public String title;
    public String description;
    public int icon = -1; //Icon cannot be allocated here as there would be too many allocated images in OpenGL,
    // allocate dynamically if they intersect the frustum and deallocate as they exit


    public AssetPack(String filepath){
        this.filepath = filepath;

        File infoFile = new File(this.filepath + "/info.txt");
        if(!infoFile.exists()){
           String[] pathContents = this.filepath.split(Pattern.quote(File.separator));
           this.title = pathContents[pathContents.length - 1];

           if(this.title.equals("Default")){
               this.description = "Default Game Textures";
           } else {
               this.description = "An Asset Pack";
           }

        } else {
            try {
              BufferedReader reader = new BufferedReader(new FileReader(infoFile));

                String line = "";
                while ((line = reader.readLine()) != null) {

                    String[] info = line.split(":");

                    if (info[0].equals("title")) {
                        this.title = info[1];
                    }

                    if (info[0].equals("description")) {
                        this.description = info[1];
                    }
                }

            } catch (IOException e) {
                String[] pathContents = this.filepath.split(Pattern.quote(File.separator));
                this.title = pathContents[pathContents.length - 1];

                this.description = "An Asset Pack";
            }
        }
    }

    public void loadIcon(){
        File iconFile = new File(this.filepath + "/icon.png");
        String filepath;
        if(!iconFile.exists()){
            filepath = "src/spacegame/assets/textures/gui/defaultAssetPack.png";
        } else {
            filepath = this.filepath + "/icon.png";

            filepath = filepath.replace("\\", "/");
        }
        this.icon = CosmicEvolution.instance.renderEngine.createTexture(filepath, RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    public void deleteIcon(){
        CosmicEvolution.instance.renderEngine.deleteTexture(this.icon);
    }
}
