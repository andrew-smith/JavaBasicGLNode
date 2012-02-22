package scene;


import static org.lwjgl.opengl.GL11.*;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Defines a textureable object
 * @author Andrew
 */
public abstract class GLTextureable implements GLRenderable
{
    /** Value for no texture */
    private static final int NO_TEXTURE_ID = -1;

    /* Texture object */
    private int textureID = NO_TEXTURE_ID;
    
    
    /** The filename of the texture to bind */
    private String fileName;
    
    /**
     * Creates default GLTexturable object
     */
    public GLTextureable()
    {
    }


    /**
     * Sets the file for this texture to be applied from
     * @param file the file name
     */
    public void setFileName(String file)
    {
        this.fileName = file;
    }

    /**
     * Checks if the texture has been loaded
     * @return true if texture is ready to use - false if not.
     */
    public boolean textureLoaded()
    {
        return textureID != NO_TEXTURE_ID;
    }

    /**
     * Binds the texture
     * @param gl
     */
    public void bindTexture()
    {
        if(textureLoaded())
        {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, textureID);
        }
    }

    
    /**
     * Unbinds the texture
     * @param gl
     */
    public void unbindTexture()
    {
        if(textureLoaded())
        {
            glDisable(GL_TEXTURE_2D);
            glBlendFunc(GL_ONE, GL_ZERO);
            glDisable(GL_BLEND);
        }
    }



    /**
     * Inits the texture file
     */
    public void init()
    {
        if(fileName != null)
        {
            try
            {
                //read file
                InputStream in = new FileInputStream(fileName);
                PNGDecoder decoder = new PNGDecoder(in);

                //load data into buffer
                ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
                decoder.decode(buf, decoder.getWidth()*4, Format.RGBA);
                buf.flip();

                in.close();

                //send texture data to opengl

                //create int buffer to get the id
                IntBuffer idBuf = IntBuffer.allocate(1);
                glGenTextures(idBuf);
                textureID = idBuf.get();

                //bind texture id so we are working with only this texture
                glBindTexture(GL_TEXTURE_2D, textureID);
                //apply variables
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                //send the data
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

            }
            catch(IOException ex)
            {
                //TODO logging information

                textureID = NO_TEXTURE_ID;
            }
        }
    }

}
