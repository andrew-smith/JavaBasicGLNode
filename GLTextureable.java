


import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GLException;

/**
 * Defines a textureable object
 * @author Andrew
 */
public abstract class GLTextureable implements GLRenderable
{

    /* Texture object to load */
    private Texture texture;
    
    /** The filename of the texture to bind */
    private String fileName;
    
    /**
     * Creates default GLTexturable object
     */
    public GLTextureable()
    {
        texture = null;
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
        return texture != null;
    }

    /**
     * Binds the texture
     * @param gl
     */
    public void bindTexture(GL gl)
    {
        if(texture != null)
        {
            gl.glEnable (GL.GL_BLEND);
            gl.glBlendFunc (GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
            gl.glEnable(GL.GL_TEXTURE_2D);
            texture.enable();
            texture.bind();
        }
    }

    
    /**
     * Unbinds the texture
     * @param gl
     */
    public void unbindTexture(GL gl)
    {
        if(texture != null)
        {
            gl.glDisable(GL.GL_TEXTURE_2D);
            gl.glBlendFunc (GL.GL_ONE, GL.GL_ZERO);
            gl.glDisable (GL.GL_BLEND);
            texture.disable();
        }
    }



    /**
     * Inits the texture file
     * @param gl
     */
    public void init(GL gl)
    {
        try
        {
            if(fileName != null)
                texture = TextureIO.newTexture(new File(fileName), false);
        }
        catch (IOException ex)
        {
            Logger.getLogger(GLTextureable.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (GLException ex)
        {
            Logger.getLogger(GLTextureable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
