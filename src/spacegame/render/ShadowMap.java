package spacegame.render;

import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public final class ShadowMap {
    public int fboID;
    public int depthMap;
    public int width;
    public int height;


    public ShadowMap(int width, int height){
        this.width = width;
        this.height = height;
        this.fboID = GL46.glGenFramebuffers();
        this.depthMap = GL46.glGenTextures();
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.depthMap);
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, GL46.GL_DEPTH_COMPONENT32F, this.width, this.height, 0, GL46.GL_DEPTH_COMPONENT, GL46.GL_FLOAT, (ByteBuffer)null);

        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_NEAREST);
        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_NEAREST);

        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, this.fboID);
        GL46.glFramebufferTexture2D(GL46.GL_FRAMEBUFFER, GL46.GL_DEPTH_ATTACHMENT, GL46.GL_TEXTURE_2D, this.depthMap, 0);
        GL46.glDrawBuffer(GL46.GL_NONE);
        GL46.glReadBuffer(GL46.GL_NONE);
        GL46.glBindFramebuffer(GL46.GL_FRAMEBUFFER, 0);
    }

    public void cleanupGLObjects(){
        GL46.glDeleteFramebuffers(this.fboID);
        GL46.glDeleteTextures(this.depthMap);
    }
}
