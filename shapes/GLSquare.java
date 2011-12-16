

package shapes;

import javax.media.opengl.GL;
import scene.GLTextureable;

/**
 * Basic square (1.0 x 1.0)
 * Use scale to resize.
 * @author Andrew
 */
public class GLSquare extends GLTextureable
{

    public void update()
    {
    }

    public void draw(GL gl)
    {

        bindTexture(gl);

        //gl.glDisable(GL.GL_LIGHTING);
        gl.glBegin(GL.GL_TRIANGLE_FAN);
                gl.glColor3f(1.0f,1.0f,1.0f);
                gl.glNormal3f(0.0f, 1.0f, 0.0f);

                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex2f(-1.0f, 1.0f);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex2f( 1.0f, 1.0f);
                gl.glTexCoord2f(1.0f, 1.0f);
                gl.glVertex2f( 1.0f,-1.0f);
                gl.glTexCoord2f(0.0f, 1.0f);
                gl.glVertex2f(-1.0f,-1.0f);
        gl.glEnd();
        //gl.glEnable(GL.GL_LIGHTING);

        unbindTexture(gl);


    }

}
